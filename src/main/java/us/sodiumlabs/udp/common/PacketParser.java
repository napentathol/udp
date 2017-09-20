package us.sodiumlabs.udp.common;

import com.google.common.io.ByteSource;
import org.slf4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class PacketParser {
    /**
     * A limitation of this protocol.
     */
    private static final int MAX_INTERNAL_PACKET_BYTE_LENGTH = 65451;

    public static final String HMAC_SHA_256 = "HmacSHA256";

    private final Logger logger;

    protected PacketParser(final Logger logger) {
        this.logger = requireNonNull(logger, "logger");
    }

    public Optional<Packet> parsePacket(final ByteBuffer packet, final Function<UUID, SecretKeySpec> secret)
        throws ParsingFailureException
    {
        final PacketType type = parsePacketType(packet);

        if(PacketType.INVALID == type) return Optional.empty();

        try {
            // Advance the buffer three bytes.
            packet.get();
            packet.get();
            packet.get();

            final int size = packet.getInt();

            final byte[] signature = new byte[32];
            for (int i = 0; i < signature.length; i++) {
                signature[i] = packet.get();
            }
            final ByteSource signatureSource = ByteSource.wrap(signature);

            final long msb = packet.getLong();
            final long lsb = packet.getLong();
            final UUID senderId = new UUID(msb, lsb);

            final byte[] payload = new byte[size];
            for (int i = 0; i < payload.length; i++) {
                payload[i] = packet.get();
            }
            final ByteSource payloadSource = ByteSource.wrap(payload);

            final ByteSource calculatedSignature = signPayload(payloadSource, secret.apply(senderId));

            final Packet returnPacket = Packet.builder()
                .withType(type)
                .withLength(size)
                .withPayload(payloadSource)
                .withSenderId(senderId)
                .withSignature(signatureSource)
                .build();

            if(logger.isTraceEnabled()) logger.trace("Receiving packet: " + returnPacket.toString());

            compareSignatures(calculatedSignature, signatureSource, type);

            return Optional.of(returnPacket);

        } catch (BufferUnderflowException ex) {
            throw new ParsingFailureException("Failed to get necessary bytes.", ex, type);
        } catch (NoSuchAlgorithmException ex) {
            throw new ParsingFailureException("This JVM does not support the hashing function.", ex, type);
        } catch (IOException ex) {
            throw new ParsingFailureException("Encountered IO exception, weird.", ex, type);
        } catch (InvalidKeyException ex) {
            throw new ParsingFailureException("Failed to read secret key.", ex, type);
        }
    }

    public Packet createPacket(final PacketType type, final UUID id, final ByteSource data, final SecretKeySpec secret) {
        try {
            if(data.size() > MAX_INTERNAL_PACKET_BYTE_LENGTH) throw new RuntimeException("Packet is too large.");

            return Packet.builder()
                .withType(type)
                .withLength((int) data.size())
                .withPayload(data)
                .withSenderId(id)
                .withSignature(signPayload(data, secret))
                .build();

        } catch (IOException e) {
            throw new RuntimeException("Failed to Create packet.", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("This JVM does not support the hashing function.", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Failed to read secret key.", e);
        }
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

    private void compareSignatures(final ByteSource calculatedSignature, final ByteSource signatureSource, final PacketType type)
        throws IOException, ParsingFailureException
    {
        final String calculated = byteSourceToString(calculatedSignature);
        final String source = byteSourceToString(signatureSource);

        if(!calculated.equals(source))
            throw new ParsingFailureException(String.format("Invalid signature!\nExpected: [%s],\nFound:    [%s]",
                calculated, source), type);
    }

    private ByteSource signPayload(final ByteSource payload, final SecretKeySpec secret) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        final Mac mac = Mac.getInstance(HMAC_SHA_256);
        mac.init(secret);
        mac.update(payload.read());
        return ByteSource.wrap(mac.doFinal());
    }

    private PacketType parsePacketType(ByteBuffer packet) {
        try {
            return PacketType.fromPacketType(packet.get());
        } catch (BufferUnderflowException ex) {
            return PacketType.INVALID;
        }
    }

    public class ParsingFailureException extends Exception {
        private PacketType type;

        ParsingFailureException(final String msg, final Exception cause, final PacketType type) {
            super("Received packet of type " + type + ". " + msg, cause);
            this.type = type;
        }

        ParsingFailureException(final String msg, final PacketType type) {
            super(msg);
            this.type = type;
        }

        public PacketType getType() {
            return type;
        }
    }
}
