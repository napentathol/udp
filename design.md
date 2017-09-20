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

## In General

A packet shall consist of the following fields:

1. A 1-byte field representing the packet type.
2. 3-bytes of reserved space
3. A 4-byte field representing the payload size.
4. A 32-byte field representing the SHA-256 of the payload and a secret.
5. A 16-byte field representing the UUID of the sender.
6. A variable sized payload no larger than 65451 bytes.

## HELLO, DISCONNECT, REJECT, ACCEPT, PING, and PONG packets

Hello packets should have a timestamp as the payload. If the timestamp is out of date, it should be discarded.

## ACK and BAD_ACK packets

ACK and BAD_ACK packets should send the SHA-256 signature of the packet they are responding to as the payload.

## DATA_ACK and DATA_NOACK packets

DATA_ACK and DATA_NOACK packets should contain data payloads.
