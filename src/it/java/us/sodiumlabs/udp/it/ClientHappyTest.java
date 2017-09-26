package us.sodiumlabs.udp.it;

import org.junit.jupiter.api.Test;
import us.sodiumlabs.udp.client.UdpClient;
import us.sodiumlabs.udp.common.PacketParser;
import us.sodiumlabs.udp.server.UdpServer;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class ClientHappyTest {
    @Test
    public void clientHappyTest() throws IOException {
        assertTimeoutPreemptively(Duration.ofSeconds(2), ()-> {
            final int port = 19900;

            final UUID serverId = UUID.randomUUID();
            final UUID clientId = UUID.randomUUID();

            final SecretKeySpec clientKey = new SecretKeySpec("client".getBytes(), PacketParser.HMAC_SHA_256);
            final SecretKeySpec serverKey = new SecretKeySpec("server".getBytes(), PacketParser.HMAC_SHA_256);

            // Set up server
            final UdpServer server = UdpServer.builder()
                .withClientKeyProvider( u -> {
                    if(u.equals(clientId)) {
                        return clientKey;
                    }
                    return null;
                })
                .withSecretKey(serverKey)
                .withPort(port)
                .withId(serverId)
                .withPacketHandler((s, p) -> {})
                .build();
            server.initializeServer();

            // Set up client.
            final UdpClient client = UdpClient.builder()
                .withDestination(InetAddress.getLoopbackAddress())
                .withPort(port)
                .withId(clientId)
                .withServerKey(serverKey)
                .withSecretKey(clientKey)
                .withPacketHandler((c, p) -> {})
                .build();
            client.initiateConnection();
        });
    }
}
