package us.sodiumlabs.udp.common;

import com.google.common.io.ByteSource;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public abstract class UdpCommon<T extends UdpCommon<T>>
    implements AutoCloseable
{
    /**
     * A limitation of UDP.
     */
    private static final int MAX_PACKET_SIZE = 65_507;

    public abstract DatagramSocket getSocket();

    public abstract UUID getId();

    public abstract SecretKeySpec getSecretKey();

    public abstract BiConsumer<T, Packet> getPacketHandler();

    public abstract InetAddress getDestination();

    public abstract int getPort();

    @Value.Default
    public PacketParser getPacketParser() {
        return new PacketParser(getLogger());
    }

    @Value.Default
    public Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    @Value.Default
    public ExecutorService getExecutorService() {
        return Executors.newFixedThreadPool(2);
    }

    @Value.Default
    public Function<T, ReaderThread> getReaderThreadProvider() {
        return ReaderThread::new;
    }

    // Receive packets
    protected abstract void handlePacket(final DatagramPacket packet);

    protected DatagramPacket receiveRawPacket() throws IOException {
        final byte[] data = new byte[MAX_PACKET_SIZE];
        final DatagramPacket packet = new DatagramPacket(data, data.length);
        getSocket().receive(packet);
        return packet;
    }

    // Send packets
    public void sendPacket(final Packet packet, final InetAddress destination, final int port)
        throws IOException
    {
        if(getLogger().isTraceEnabled()) getLogger().trace("Sending packet: " + packet.toString());

        final ByteBuffer packetBuffer = packet.toPacket();
        final byte[] packetBytes = new byte[packetBuffer.remaining()];
        packetBuffer.get(packetBytes);
        final DatagramPacket datagramPacket = new DatagramPacket(packetBytes, packetBytes.length, destination, port);
        getSocket().send(datagramPacket);
    }

    protected void sendTimestampPacket(final PacketType type, final DatagramPacket packet)
        throws IOException
    {
        sendTimestampPacket(type, packet.getAddress(), packet.getPort());
    }

    protected void sendTimestampPacket(final PacketType type, final InetAddress destination, final int port)
        throws IOException
    {
        final Packet packet = getPacketParser().createPacket(type, getId(), timeStampData(), getSecretKey());
        sendPacket(packet, destination, port);
    }

    // Utility functions
    private ByteSource timeStampData() {
        final ZonedDateTime dateTime = ZonedDateTime.now();
        return ByteSource.wrap(dateTime.toString().getBytes());
    }

    @Override
    public void close() throws Exception {
        getExecutorService().shutdownNow();
        getSocket().close();
    }

    public static class ReaderThread implements Runnable, AutoCloseable {
        private final AtomicBoolean open = new AtomicBoolean(true);

        private final UdpCommon udpCommon;

        private ReaderThread(final UdpCommon udpCommon) {
            this.udpCommon = requireNonNull(udpCommon);
        }

        @Override
        public void close() throws Exception {
            open.set(false);
        }

        @Override
        public void run() {
            while (open.get()) {
                try {
                    final DatagramPacket packet = udpCommon.receiveRawPacket();

                    udpCommon.getExecutorService().submit(() -> udpCommon.handlePacket(packet));
                } catch (IOException e) {
                    udpCommon.getLogger().debug("Unable to read from socket.", e);
                }
            }
        }
    }
}
