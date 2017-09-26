# Connection

## Initiating

1. Client connects with HELLO packet.
2. Server responds with ACCEPT packet or REJECT packet.
3. Connection is established.

## Continuing

1. Client sends PING packet.
2. Server responds with PONG packet.
3. If there is a timeout, connection should be reestablished. Server should respond to client requests with REJECT and
    client should not do anything.

## Communication NOACK

1. Client or server send DATA_NOACK packet.
2. Server or client do not respond.

## Communication ACK

1. Client or server send DATA_ACK packet.
2. Server or client respond with ACK packet. Unless the data is corrupt, in which case it should send a BAD_ACK packet.

## Client Communication termination

1. Client sends DISCONNECT packet.
2. Server sends REJECT packet to client.
3. Client should retry sending DISCONNECT until REJECT packet received or a number of tries is exceeded.

## Server Communication termination

1. Server sends REJECT packet.
2. Client sends no more packets to server.

# Packets

## Design

1. A SHA256 of the rest of the packet. (32 bytes)
2. Client ID (16 bytes)
3. The packet ID. (1 byte)
4. Reserved space (3 bytes)
5. Length (2 bytes)
6. Custom ID (2 bytes)
7. Packet order count (8 bytes)
8. Data payload (< 65,443 bytes)

```
|                SHA 256                |
|                SHA 256                |
|                SHA 256                |
|                SHA 256                |
|               CLIENT ID               |
|               CLIENT ID               |
| ID |   RESERVED   | LENGTH  | CUSTOM  |
|          PACKET ORDER COUNT           |
| DATA ...
```


## HELLO, DISCONNECT, REJECT, ACCEPT, PING, and PONG packets

Hello packets should have a timestamp as the payload. If the timestamp is out of date, it should be discarded.

## ACK and BAD_ACK packets

ACK and BAD_ACK packets should send the SHA-256 signature of the packet they are responding to as the payload.

## DATA_ACK and DATA_NOACK packets

DATA_ACK and DATA_NOACK packets should contain data payloads.
