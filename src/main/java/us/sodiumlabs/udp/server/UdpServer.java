package us.sodiumlabs.udp.server;

import com.google.common.annotations.VisibleForTesting;
import org.immutables.value.Value;
import us.sodiumlabs.udp.common.Packet;
import us.sodiumlabs.udp.common.PacketType;
import us.sodiumlabs.udp.common.UdpCommon;
import us.sodiumlabs.udp.immutables.Style;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@Style
@Value.Immutable
public abstract class UdpServer
    extends UdpCommon<UdpServer>
    implements AutoCloseable
{
    public abstract Function<UUID, SecretKeySpec> getClientKeyProvider();

    private AtomicBoolean initiated = new AtomicBoolean(false);

    @Value.Default
    public InetAddress getDestination() {
        try {
            return InetAddress.getByName("0.0.0.0");
        } catch (UnknownHostException e) {
            throw new RuntimeException("Unable to create inet address.", e);
        }
    }

    @Value.Default
    public DatagramSocket getSocket() {
        try {
            return new DatagramSocket(getPort(), getDestination());
        } catch (SocketException e) {
            throw new RuntimeException("Unable to create socket.", e);
        }
    }

    // Receive Packets
    @VisibleForTesting
    protected void handlePacket(final DatagramPacket packet) {
        try {
            final Packet parsedPacket = getPacketParser().parsePacket(ByteBuffer.wrap(packet.getData()), getClientKeyProvider());

            if(!parsedPacket.getType().isSentFromClient()) {
                if(getLogger().isDebugEnabled()) {
                    getLogger().debug(
                        String.format("Received type that is not handled by the server [%s].", parsedPacket.getType()));
                }
                return;
            }

            if(PacketType.HELLO == parsedPacket.getType()) {
                sendAcceptPacket(packet);
                // TODO: add listener event here.
                getLogger().info(String.format("Client with uuid [%s] has connected.", parsedPacket.getSenderId()));
            }
        } catch (IOException | RuntimeException e) {
            if(getLogger().isDebugEnabled()) getLogger().debug("Failed to handle packet.", e);
        }
    }

    // Send Packets
    private void sendAcceptPacket(final DatagramPacket packet) throws IOException {
        sendTimestampPacket(PacketType.ACCEPT, packet);
    }

    private void sendRejectPacket(final DatagramPacket packet) throws IOException {
        sendTimestampPacket(PacketType.REJECT, packet);
    }

    // Initialization and closing.
    public synchronized void initializeServer() throws IOException {
        if (initiated.get()) throw new RuntimeException("Already initiated.");

        getLogger().info("Initializing server...");

        // Initialize Reader thread.
        new Thread(getReaderThreadProvider().apply(this), "ServerReader").start();

        // TODO: Initialize client sweeping thread.

        initiated.set(true);

        getLogger().info("Server initialized.");
    }

    public static ImmutableUdpServer.Builder builder() {
        return ImmutableUdpServer.builder();
    }
}
