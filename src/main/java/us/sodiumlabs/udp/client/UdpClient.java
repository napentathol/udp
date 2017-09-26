package us.sodiumlabs.udp.client;

import org.immutables.value.Value;
import us.sodiumlabs.udp.common.Packet;
import us.sodiumlabs.udp.common.PacketType;
import us.sodiumlabs.udp.common.UdpCommon;
import us.sodiumlabs.udp.immutables.Style;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

@Style
@Value.Immutable
public abstract class UdpClient
    extends UdpCommon<UdpClient>
{
    public abstract SecretKeySpec getServerKey();

    @Value.Default
    public DatagramSocket getSocket() {
        try {
            final DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout((int) TimeUnit.SECONDS.toMillis(1));
            return new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException("Unable to create socket.", e);
        }
    }

    private AtomicBoolean initiated = new AtomicBoolean(false);

    // Receive packets
    private Packet receivePacket() {
        try {
            final DatagramPacket packet = receiveRawPacket();

            return getPacketParser()
                .parsePacket(ByteBuffer.wrap(packet.getData()), u -> getServerKey());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read.", e);
        }
    }

    protected void handlePacket(final DatagramPacket packet) {
        try {
            final Packet parsedPacket = getPacketParser().parsePacket(ByteBuffer.wrap(packet.getData()), id -> getServerKey());

            if(!parsedPacket.getType().isSentFromServer()) {
                if(getLogger().isDebugEnabled()) {
                    getLogger().debug(
                        String.format("Received type that is not handled by the server [%s].", parsedPacket.getType()));
                }
                return;
            }

            if(PacketType.PANG == parsedPacket.getType()) {
                sendPongPacket();
                // TODO: add listener event here.
                getLogger().trace("Client has received pang from server.");
            }
        } catch (IOException | RuntimeException e) {
            if(getLogger().isDebugEnabled()) getLogger().debug("Failed to handle packet.", e);
        }
    }

    // Send packets
    private void sendHelloPacket() throws IOException {
        sendTimestampPacket(PacketType.HELLO, getDestination(), getPort());
    }

    private void sendPingPacket() throws IOException {
        sendTimestampPacket(PacketType.PING, getDestination(), getPort());
    }

    private void sendPongPacket() throws IOException {
        sendTimestampPacket(PacketType.PONG, getDestination(), getPort());
    }

    // Initialization and closing.
    public synchronized void initiateConnection() throws IOException {
        if(initiated.get()) throw new RuntimeException("Already initiated.");

        getLogger().info("Connecting to server...");
        // Send HELLO packet.
        sendHelloPacket();

        // Receive ACCEPT or REJECT packet.
        final Packet reply = receivePacket();

        if(PacketType.ACCEPT != reply.getType()) {
            throw new RuntimeException(
                String.format("Failed to start up server. Expected an ACCEPT packet, received a %s packet.", reply.getType()));
        }

        getLogger().info("Connected!");

        // TODO: Initialize read thread.
        new Thread(getReaderThreadProvider().apply(this), "ClientReader").start();

        // TODO: Initialize ping thread.
        new Thread(new PingThread(this), "ClientPingThread").start();

        initiated.set(true);
        getLogger().info("Initialization completed!");
    }

    public static class PingThread implements Runnable, AutoCloseable {
        private final AtomicBoolean open = new AtomicBoolean(true);

        private final UdpClient client;

        private PingThread(final UdpClient client) {
            this.client = requireNonNull(client);
        }

        @Override
        public void close() throws Exception {
            open.set(false);
        }

        @Override
        public void run() {
            while (open.get()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (final InterruptedException e) {
                    if (client.getLogger().isTraceEnabled()) client.getLogger().trace("Ping thread interrupted.", e);
                }

                try {
                    client.sendPingPacket();
                } catch (final IOException e) {
                    if(client.getLogger().isDebugEnabled()) client.getLogger().debug("IO Exception.", e);
                }
            }
        }
    }

    public static ImmutableUdpClient.Builder builder() {
        return ImmutableUdpClient.builder();
    }
}
