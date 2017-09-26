package us.sodiumlabs.udp.common;

import com.google.common.io.ByteSource;
import org.slf4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class PacketParser {
    private final AtomicLong packetOrderCounter = new AtomicLong(0);

    public static final String HMAC_SHA_256 = "HmacSHA256";

    private final Logger logger;

    PacketParser(final Logger logger) {
        this.logger = requireNonNull(logger, "logger");
    }

    public Packet parsePacket(final ByteBuffer packet, final Function<UUID, SecretKeySpec> secret)
    {
        return new ReadPacketBuilder()
            .withIn(packet)
            .withPacketParser(this)
            .withSecretKeySpec(secret)
            .build();
    }

    Packet createPacket(final PacketType type, final UUID id, final ByteSource data, final SecretKeySpec secret) {
        return new CreatePacketBuilder()
            .withId(id)
            .withType(type)
            .withCustomId(0)
            .withPacketOrderCount(packetOrderCounter.getAndIncrement())
            .withPayload(data)
            .withPacketParser(this)
            .withSecretKeySpec(secret)
            .build();
    }

    private String byteSourceToString(final ByteSource sig) throws IOException {
        final StringBuilder builder = new StringBuilder();

        try (final InputStream signatureStream = sig.openStream()) {
            int in;
            while(-1 != (in = signatureStream.read())) {
                builder.append(String.format("%02X", in));
            }
        }

        return builder.toString();
    }

    void compareSignatures(final ByteSource calculatedSignature, final ByteSource signatureSource)
        throws IOException
    {
        if(!calculatedSignature.contentEquals(signatureSource)) {
            final String calculated = byteSourceToString(calculatedSignature);
            final String source = byteSourceToString(signatureSource);

            throw new RuntimeException(String.format("Invalid signature!\nExpected: [%s],\nFound:    [%s]",
                calculated, source));
        }
    }

    ByteSource signPayload(final ByteSource payload, final SecretKeySpec secret) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        if(logger.isTraceEnabled()) {
            logger.trace("Signing payload:\n" + byteSourceToString(payload));
        }
        final Mac mac = Mac.getInstance(HMAC_SHA_256);
        mac.init(secret);
        mac.update(payload.read());
        return ByteSource.wrap(mac.doFinal());
    }
}
