package us.sodiumlabs.udp.common;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import org.immutables.builder.Builder;
import org.immutables.value.Value;
import us.sodiumlabs.udp.immutables.Style;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.function.Function;

@Style
@Value.Immutable
public abstract class Packet {

    /**
     * A limitation of this protocol.
     */
    private static final int MAX_INTERNAL_PACKET_BYTE_LENGTH = 65_443;

    private static final int SIGNATURE_BYTE_SIZE = 32;

    private static final int LOWER_HEADER_BYTE_SIZE = 32;

    private static final int UNSIGNED_SHORT_BITMASK = 0xffff;

    public abstract UUID getSenderId();

    public abstract int getLength();

    public abstract int getCustomId();

    public abstract Long getOrderCount();

    public abstract PacketType getType();

    public abstract ByteSource getSignature();

    public abstract ByteSource getPayload();

    @Value.Check
    protected void check() {
        Preconditions.checkArgument( getLength() < MAX_INTERNAL_PACKET_BYTE_LENGTH,
            "Length should be less than the protocol limitation of 65,443 bytes." );
        Preconditions.checkArgument( getLength() > 0, "You must send some data.");
        Preconditions.checkArgument( getCustomId() < 0x1_0000,
            "Custom ID must fit within a short value.");
        Preconditions.checkArgument( getCustomId() >= 0,
            "Custom ID must be postive or zero.");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
            .add("Sender Id", getSenderId())
            .add("Length", getLength())
            .add("Type", getType())
            .add("Signature", byteSourceToString(getSignature()))
            .add("Payload", byteSourceToString(getPayload()))
            .toString();
    }

    private String byteSourceToString(final ByteSource sig) {
        try {
            final StringBuilder builder = new StringBuilder();

            try (final InputStream signatureStream = sig.openStream()) {
                int in;
                while (-1 != (in = signatureStream.read())) {
                    builder.append(String.format("%02X", in));
                }
            }

            return builder.toString();
        } catch (IOException e) {
            return "!!IOException!! " + e.getMessage();
        }
    }

    public ByteBuffer toPacket() {
        try {
            final ByteBuffer writeBuffer = ByteBuffer.allocate(64 + getLength());
            final ByteBuffer readBuffer = writeBuffer.asReadOnlyBuffer();

            writeBuffer.put(getSignature().read());
            putLowerHeaderOnWriteBuffer(getSenderId(), getType(), getLength(), getCustomId(), getOrderCount(), writeBuffer );
            writeBuffer.put(getPayload().read());

            return readBuffer;
        } catch (IOException e) {
            throw new RuntimeException("Unable to create packet.", e);
        }
    }

    private static void putLowerHeaderOnWriteBuffer(
        final UUID id,
        final PacketType type,
        final int length,
        final int customId,
        final long orderCount,
        final ByteBuffer writeBuffer
    ) {
        writeBuffer.putLong(id.getMostSignificantBits());
        writeBuffer.putLong(id.getLeastSignificantBits());
        writeBuffer.put(type.getPacketType());
        // Advance the byte buffer
        writeBuffer.put((byte) 0); writeBuffer.put((byte) 0); writeBuffer.put((byte) 0);
        writeBuffer.putShort((short) length);
        writeBuffer.putShort((short) customId);
        writeBuffer.putLong(orderCount);
    }

    @Builder.Factory
    static Packet readPacket(
        final ByteBuffer in,
        final PacketParser packetParser,
        final Function<UUID, SecretKeySpec> secretKeySpec
    ) {
        try {
            final ByteSource signature = readNBytes(SIGNATURE_BYTE_SIZE, in);

            final ByteBuffer signedPacket = in.asReadOnlyBuffer();

            final UUID senderId = new UUID(in.getLong(), in.getLong());
            final PacketType type = PacketType.fromPacketType(in.get());
            // Advance the Byte Buffer.
            in.get(); in.get(); in.get();
            final int length = UNSIGNED_SHORT_BITMASK & in.getShort();

            // Check signature here so we don't waste cycles reading anything else if it does not match.
            final ByteSource readSignableSource = readNBytes(length + LOWER_HEADER_BYTE_SIZE, signedPacket);
            final ByteSource computedSignature = packetParser.signPayload(readSignableSource, secretKeySpec.apply(senderId));
            packetParser.compareSignatures(computedSignature, signature);

            final int customId = UNSIGNED_SHORT_BITMASK & in.getShort();
            final long orderCount = in.getLong();
            final ByteSource payload = readNBytes(length, in);

            return Packet.builder()
                .withSignature(signature)
                .withSenderId(senderId)
                .withType(type)
                .withLength(length)
                .withCustomId(customId)
                .withOrderCount(orderCount)
                .withPayload(payload)
                .build();

        } catch (IOException e) {
            throw new RuntimeException("Failed to create packet.", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("This JVM does not support the hashing function.", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Failed to read secret key.", e);
        }
    }

    private static ByteSource readNBytes(final int n, final ByteBuffer in) {
        final byte[] byteArray = new byte[n];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = in.get();
        }
        return ByteSource.wrap(byteArray);
    }

    @Builder.Factory
    static Packet createPacket(
        final UUID id,
        final PacketType type,
        final int customId,
        final long packetOrderCount,
        final ByteSource payload,
        final PacketParser packetParser,
        final SecretKeySpec secretKeySpec
    ) {
        try {
            final int length = (int) payload.size();

            final ByteBuffer writeBuffer = ByteBuffer.allocate(32);
            putLowerHeaderOnWriteBuffer(id, type, length, customId, packetOrderCount, writeBuffer);

            final ByteSource packetToSign = ByteSource.concat(ByteSource.wrap(writeBuffer.array()), payload);

            final ByteSource signature = packetParser.signPayload(packetToSign, secretKeySpec);

            return Packet.builder()
                .withSignature(signature)
                .withSenderId(id)
                .withType(type)
                .withLength(length)
                .withCustomId(customId)
                .withOrderCount(packetOrderCount)
                .withPayload(payload)
                .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create packet.", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("This JVM does not support the hashing function.", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Failed to read secret key.", e);
        }
    }

    public static ImmutablePacket.Builder builder() {
        return ImmutablePacket.builder();
    }
}
