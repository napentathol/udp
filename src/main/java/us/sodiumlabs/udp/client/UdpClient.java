package us.sodiumlabs.udp.client;

import org.immutables.value.Value;
import us.sodiumlabs.udp.common.Packet;
import us.sodiumlabs.udp.common.PacketParser;
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
                .parsePacket(ByteBuffer.wrap(packet.getData()), u -> getServerKey())
                .orElseThrow( () -> new RuntimeException("Invalid packet. Failed to read."));
        } catch (PacketParser.ParsingFailureException e) {
            throw new RuntimeException("Invalid packet. Failed to read.", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read.", e);
        }
    }

    protected void handlePacket(final DatagramPacket packet) {}

    // Send packets
    private void sendHelloPacket() throws IOException {
        sendTimestampPacket(PacketType.HELLO, getDestination(), getPort());
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
        // TODO: Initialize ping thread.

        initiated.set(true);
        getLogger().info("Initialization completed!");
    }

    public static ImmutableUdpClient.Builder builder() {
        return ImmutableUdpClient.builder();
    }
}
