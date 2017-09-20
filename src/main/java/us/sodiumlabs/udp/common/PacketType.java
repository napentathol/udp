package us.sodiumlabs.udp.common;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum PacketType {
    HELLO(PacketSenderContext.CLIENT_ONLY, (byte) 0x00),
    DISCONNECT(PacketSenderContext.CLIENT_ONLY, (byte) 0x01) {
        @Override
        public boolean isTerminalPacket() {
            return true;
        }
    },
    ACCEPT(PacketSenderContext.SERVER_ONLY, (byte) 0x02),
    REJECT(PacketSenderContext.SERVER_ONLY, (byte) 0x03) {
        @Override
        public boolean isTerminalPacket() {
            return true;
        }
    },
    PING(PacketSenderContext.CLIENT_ONLY, (byte) 0x04),
    PONG(PacketSenderContext.SERVER_ONLY, (byte) 0x05),
    BAD_ACK(PacketSenderContext.BOTH, (byte) 0x06),
    ACK(PacketSenderContext.BOTH, (byte) 0x07),
    DATA_ACK(PacketSenderContext.BOTH, (byte) 0x08),
    DATA_NOACK(PacketSenderContext.BOTH, (byte) 0x09),
    INVALID(PacketSenderContext.BOTH, (byte) 0xff);

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private static final Map<Byte, PacketType> packetTypeMap;

    static {
        final ImmutableMap.Builder<Byte, PacketType> bytePacketTypeBuilder = ImmutableMap.builder();
        for(PacketType type : PacketType.values()) {
            bytePacketTypeBuilder.put(type.getPacketType(), type);
        }
        packetTypeMap = bytePacketTypeBuilder.build();
    }

    public static PacketType fromPacketType(byte packetType) {
        return packetTypeMap.getOrDefault(packetType, INVALID);
    }


    private final boolean sentFromServer;

    private final boolean sentFromClient;

    private final byte packetType;

    PacketType(PacketSenderContext senderContext, final byte packetType) {
        this.sentFromServer = PacketSenderContext.BOTH == senderContext
            || PacketSenderContext.SERVER_ONLY == senderContext;
        this.sentFromClient = PacketSenderContext.BOTH == senderContext
            || PacketSenderContext.CLIENT_ONLY == senderContext;
        this.packetType = packetType;
    }

    public boolean isSentFromServer() {
        return sentFromServer;
    }

    public boolean isSentFromClient() {
        return sentFromClient;
    }

    public boolean isTerminalPacket() {
        return false;
    }

    public byte getPacketType() {
        return packetType;
    }

    private enum PacketSenderContext {
        SERVER_ONLY,
        BOTH,
        CLIENT_ONLY
    }
}
