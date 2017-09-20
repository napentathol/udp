package us.sodiumlabs.udp.common;

import com.google.common.collect.Lists;
import com.google.common.io.ByteSource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Generated;

/**
 * Immutable implementation of {@link Packet}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutablePacket.builder()}.
 */
@SuppressWarnings({"all"})
@Generated({"Immutables.generator", "Packet"})
public final class ImmutablePacket extends Packet {
  private final UUID senderId;
  private final int length;
  private final PacketType type;
  private final ByteSource signature;
  private final ByteSource payload;

  private ImmutablePacket(
      UUID senderId,
      int length,
      PacketType type,
      ByteSource signature,
      ByteSource payload) {
    this.senderId = senderId;
    this.length = length;
    this.type = type;
    this.signature = signature;
    this.payload = payload;
  }

  /**
   * @return The value of the {@code senderId} attribute
   */
  @Override
  public UUID getSenderId() {
    return senderId;
  }

  /**
   * @return The value of the {@code length} attribute
   */
  @Override
  public int getLength() {
    return length;
  }

  /**
   * @return The value of the {@code type} attribute
   */
  @Override
  public PacketType getType() {
    return type;
  }

  /**
   * @return The value of the {@code signature} attribute
   */
  @Override
  public ByteSource getSignature() {
    return signature;
  }

  /**
   * @return The value of the {@code payload} attribute
   */
  @Override
  public ByteSource getPayload() {
    return payload;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Packet#getSenderId() senderId} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for senderId
   * @return A modified copy of the {@code this} object
   */
  public final ImmutablePacket withSenderId(UUID value) {
    if (this.senderId == value) return this;
    UUID newValue = Objects.requireNonNull(value, "senderId");
    return new ImmutablePacket(newValue, this.length, this.type, this.signature, this.payload);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Packet#getLength() length} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for length
   * @return A modified copy of the {@code this} object
   */
  public final ImmutablePacket withLength(int value) {
    if (this.length == value) return this;
    return new ImmutablePacket(this.senderId, value, this.type, this.signature, this.payload);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Packet#getType() type} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for type
   * @return A modified copy of the {@code this} object
   */
  public final ImmutablePacket withType(PacketType value) {
    if (this.type == value) return this;
    PacketType newValue = Objects.requireNonNull(value, "type");
    return new ImmutablePacket(this.senderId, this.length, newValue, this.signature, this.payload);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Packet#getSignature() signature} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for signature
   * @return A modified copy of the {@code this} object
   */
  public final ImmutablePacket withSignature(ByteSource value) {
    if (this.signature == value) return this;
    ByteSource newValue = Objects.requireNonNull(value, "signature");
    return new ImmutablePacket(this.senderId, this.length, this.type, newValue, this.payload);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Packet#getPayload() payload} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for payload
   * @return A modified copy of the {@code this} object
   */
  public final ImmutablePacket withPayload(ByteSource value) {
    if (this.payload == value) return this;
    ByteSource newValue = Objects.requireNonNull(value, "payload");
    return new ImmutablePacket(this.senderId, this.length, this.type, this.signature, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutablePacket} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutablePacket
        && equalTo((ImmutablePacket) another);
  }

  private boolean equalTo(ImmutablePacket another) {
    return senderId.equals(another.senderId)
        && length == another.length
        && type.equals(another.type)
        && signature.equals(another.signature)
        && payload.equals(another.payload);
  }

  /**
   * Computes a hash code from attributes: {@code senderId}, {@code length}, {@code type}, {@code signature}, {@code payload}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + senderId.hashCode();
    h += (h << 5) + length;
    h += (h << 5) + type.hashCode();
    h += (h << 5) + signature.hashCode();
    h += (h << 5) + payload.hashCode();
    return h;
  }

  /**
   * Creates an immutable copy of a {@link Packet} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable Packet instance
   */
  public static ImmutablePacket copyOf(Packet instance) {
    if (instance instanceof ImmutablePacket) {
      return (ImmutablePacket) instance;
    }
    return ImmutablePacket.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutablePacket ImmutablePacket}.
   * @return A new ImmutablePacket builder
   */
  public static ImmutablePacket.Builder builder() {
    return new ImmutablePacket.Builder();
  }

  /**
   * Builds instances of type {@link ImmutablePacket ImmutablePacket}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  public static final class Builder {
    private static final long INIT_BIT_SENDER_ID = 0x1L;
    private static final long INIT_BIT_LENGTH = 0x2L;
    private static final long INIT_BIT_TYPE = 0x4L;
    private static final long INIT_BIT_SIGNATURE = 0x8L;
    private static final long INIT_BIT_PAYLOAD = 0x10L;
    private long initBits = 0x1fL;

    private UUID senderId;
    private int length;
    private PacketType type;
    private ByteSource signature;
    private ByteSource payload;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code Packet} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(Packet instance) {
      Objects.requireNonNull(instance, "instance");
      withSenderId(instance.getSenderId());
      withLength(instance.getLength());
      withType(instance.getType());
      withSignature(instance.getSignature());
      withPayload(instance.getPayload());
      return this;
    }

    /**
     * Initializes the value for the {@link Packet#getSenderId() senderId} attribute.
     * @param senderId The value for senderId 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withSenderId(UUID senderId) {
      this.senderId = Objects.requireNonNull(senderId, "senderId");
      initBits &= ~INIT_BIT_SENDER_ID;
      return this;
    }

    /**
     * Initializes the value for the {@link Packet#getLength() length} attribute.
     * @param length The value for length 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withLength(int length) {
      this.length = length;
      initBits &= ~INIT_BIT_LENGTH;
      return this;
    }

    /**
     * Initializes the value for the {@link Packet#getType() type} attribute.
     * @param type The value for type 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withType(PacketType type) {
      this.type = Objects.requireNonNull(type, "type");
      initBits &= ~INIT_BIT_TYPE;
      return this;
    }

    /**
     * Initializes the value for the {@link Packet#getSignature() signature} attribute.
     * @param signature The value for signature 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withSignature(ByteSource signature) {
      this.signature = Objects.requireNonNull(signature, "signature");
      initBits &= ~INIT_BIT_SIGNATURE;
      return this;
    }

    /**
     * Initializes the value for the {@link Packet#getPayload() payload} attribute.
     * @param payload The value for payload 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withPayload(ByteSource payload) {
      this.payload = Objects.requireNonNull(payload, "payload");
      initBits &= ~INIT_BIT_PAYLOAD;
      return this;
    }

    /**
     * Builds a new {@link ImmutablePacket ImmutablePacket}.
     * @return An immutable instance of Packet
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutablePacket build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutablePacket(senderId, length, type, signature, payload);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = Lists.newArrayList();
      if ((initBits & INIT_BIT_SENDER_ID) != 0) attributes.add("senderId");
      if ((initBits & INIT_BIT_LENGTH) != 0) attributes.add("length");
      if ((initBits & INIT_BIT_TYPE) != 0) attributes.add("type");
      if ((initBits & INIT_BIT_SIGNATURE) != 0) attributes.add("signature");
      if ((initBits & INIT_BIT_PAYLOAD) != 0) attributes.add("payload");
      return "Cannot build Packet, some of required attributes are not set " + attributes;
    }
  }
}
