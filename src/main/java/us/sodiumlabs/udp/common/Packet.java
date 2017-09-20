package us.sodiumlabs.udp.common;

import com.google.common.base.MoreObjects;
import com.google.common.io.ByteSource;
import org.immutables.value.Value;
import us.sodiumlabs.udp.immutables.Style;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

@Style
@Value.Immutable
public abstract class Packet {

    public abstract UUID getSenderId();

    public abstract int getLength();

    public abstract PacketType getType();

    public abstract ByteSource getSignature();

    public abstract ByteSource getPayload();

    public ByteSource toPacket() {
        ByteSource packetSource = ByteSource.wrap(new byte[]{ getType().getPacketType(), 0, 0, 0 });
        packetSource = ByteSource.concat(packetSource, ByteSource.wrap(ByteBuffer.allocate(4).putInt(getLength()).array()));
        packetSource = ByteSource.concat(packetSource, getSignature());
        packetSource = ByteSource.concat(packetSource, ByteSource.wrap(
            ByteBuffer.allocate(16)
                .putLong(getSenderId().getMostSignificantBits())
                .putLong(getSenderId().getLeastSignificantBits())
                .array()));
        return ByteSource.concat(packetSource, getPayload());
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

    public static ImmutablePacket.Builder builder() {
        return ImmutablePacket.builder();
    }
}
