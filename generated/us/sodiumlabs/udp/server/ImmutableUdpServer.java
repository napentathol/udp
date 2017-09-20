package us.sodiumlabs.udp.server;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import us.sodiumlabs.udp.common.Packet;
import us.sodiumlabs.udp.common.PacketParser;
import us.sodiumlabs.udp.common.UdpCommon;

/**
 * Immutable implementation of {@link UdpServer}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableUdpServer.builder()}.
 */
@SuppressWarnings({"all"})
@Generated({"Immutables.generator", "UdpServer"})
public final class ImmutableUdpServer extends UdpServer {
  private final Function<UUID, SecretKeySpec> clientKeyProvider;
  private final InetAddress destination;
  private final DatagramSocket socket;
  private final UUID id;
  private final SecretKeySpec secretKey;
  private final BiConsumer<UdpServer, Packet> packetHandler;
  private final int port;
  private final PacketParser packetParser;
  private final Logger logger;
  private final ExecutorService executorService;
  private final Function<UdpServer, UdpCommon.ReaderThread> readerThreadProvider;

  private ImmutableUdpServer(ImmutableUdpServer.Builder builder) {
    this.clientKeyProvider = builder.clientKeyProvider;
    this.id = builder.id;
    this.secretKey = builder.secretKey;
    this.packetHandler = builder.packetHandler;
    this.port = builder.port;
    if (builder.destination != null) {
      initShim.withDestination(builder.destination);
    }
    if (builder.socket != null) {
      initShim.withSocket(builder.socket);
    }
    if (builder.packetParser != null) {
      initShim.withPacketParser(builder.packetParser);
    }
    if (builder.logger != null) {
      initShim.withLogger(builder.logger);
    }
    if (builder.executorService != null) {
      initShim.withExecutorService(builder.executorService);
    }
    if (builder.readerThreadProvider != null) {
      initShim.withReaderThreadProvider(builder.readerThreadProvider);
    }
    this.destination = initShim.getDestination();
    this.socket = initShim.getSocket();
    this.packetParser = initShim.getPacketParser();
    this.logger = initShim.getLogger();
    this.executorService = initShim.getExecutorService();
    this.readerThreadProvider = initShim.getReaderThreadProvider();
    this.initShim = null;
  }

  private ImmutableUdpServer(
      Function<UUID, SecretKeySpec> clientKeyProvider,
      InetAddress destination,
      DatagramSocket socket,
      UUID id,
      SecretKeySpec secretKey,
      BiConsumer<UdpServer, Packet> packetHandler,
      int port,
      PacketParser packetParser,
      Logger logger,
      ExecutorService executorService,
      Function<UdpServer, UdpCommon.ReaderThread> readerThreadProvider) {
    this.clientKeyProvider = clientKeyProvider;
    this.destination = destination;
    this.socket = socket;
    this.id = id;
    this.secretKey = secretKey;
    this.packetHandler = packetHandler;
    this.port = port;
    this.packetParser = packetParser;
    this.logger = logger;
    this.executorService = executorService;
    this.readerThreadProvider = readerThreadProvider;
    this.initShim = null;
  }

  private static final int STAGE_INITIALIZING = -1;
  private static final int STAGE_UNINITIALIZED = 0;
  private static final int STAGE_INITIALIZED = 1;
  private transient volatile InitShim initShim = new InitShim();

  private final class InitShim {
    private InetAddress destination;
    private int destinationBuildStage;

    InetAddress getDestination() {
      if (destinationBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (destinationBuildStage == STAGE_UNINITIALIZED) {
        destinationBuildStage = STAGE_INITIALIZING;
        this.destination = Objects.requireNonNull(ImmutableUdpServer.super.getDestination(), "destination");
        destinationBuildStage = STAGE_INITIALIZED;
      }
      return this.destination;
    }

    void withDestination(InetAddress destination) {
      this.destination = destination;
      destinationBuildStage = STAGE_INITIALIZED;
    }
    private DatagramSocket socket;
    private int socketBuildStage;

    DatagramSocket getSocket() {
      if (socketBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (socketBuildStage == STAGE_UNINITIALIZED) {
        socketBuildStage = STAGE_INITIALIZING;
        this.socket = Objects.requireNonNull(ImmutableUdpServer.super.getSocket(), "socket");
        socketBuildStage = STAGE_INITIALIZED;
      }
      return this.socket;
    }

    void withSocket(DatagramSocket socket) {
      this.socket = socket;
      socketBuildStage = STAGE_INITIALIZED;
    }
    private PacketParser packetParser;
    private int packetParserBuildStage;

    PacketParser getPacketParser() {
      if (packetParserBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (packetParserBuildStage == STAGE_UNINITIALIZED) {
        packetParserBuildStage = STAGE_INITIALIZING;
        this.packetParser = Objects.requireNonNull(ImmutableUdpServer.super.getPacketParser(), "packetParser");
        packetParserBuildStage = STAGE_INITIALIZED;
      }
      return this.packetParser;
    }

    void withPacketParser(PacketParser packetParser) {
      this.packetParser = packetParser;
      packetParserBuildStage = STAGE_INITIALIZED;
    }
    private Logger logger;
    private int loggerBuildStage;

    Logger getLogger() {
      if (loggerBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (loggerBuildStage == STAGE_UNINITIALIZED) {
        loggerBuildStage = STAGE_INITIALIZING;
        this.logger = Objects.requireNonNull(ImmutableUdpServer.super.getLogger(), "logger");
        loggerBuildStage = STAGE_INITIALIZED;
      }
      return this.logger;
    }

    void withLogger(Logger logger) {
      this.logger = logger;
      loggerBuildStage = STAGE_INITIALIZED;
    }
    private ExecutorService executorService;
    private int executorServiceBuildStage;

    ExecutorService getExecutorService() {
      if (executorServiceBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (executorServiceBuildStage == STAGE_UNINITIALIZED) {
        executorServiceBuildStage = STAGE_INITIALIZING;
        this.executorService = Objects.requireNonNull(ImmutableUdpServer.super.getExecutorService(), "executorService");
        executorServiceBuildStage = STAGE_INITIALIZED;
      }
      return this.executorService;
    }

    void withExecutorService(ExecutorService executorService) {
      this.executorService = executorService;
      executorServiceBuildStage = STAGE_INITIALIZED;
    }
    private Function<UdpServer, UdpCommon.ReaderThread> readerThreadProvider;
    private int readerThreadProviderBuildStage;

    Function<UdpServer, UdpCommon.ReaderThread> getReaderThreadProvider() {
      if (readerThreadProviderBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (readerThreadProviderBuildStage == STAGE_UNINITIALIZED) {
        readerThreadProviderBuildStage = STAGE_INITIALIZING;
        this.readerThreadProvider = Objects.requireNonNull(ImmutableUdpServer.super.getReaderThreadProvider(), "readerThreadProvider");
        readerThreadProviderBuildStage = STAGE_INITIALIZED;
      }
      return this.readerThreadProvider;
    }

    void withReaderThreadProvider(Function<UdpServer, UdpCommon.ReaderThread> readerThreadProvider) {
      this.readerThreadProvider = readerThreadProvider;
      readerThreadProviderBuildStage = STAGE_INITIALIZED;
    }

    private String formatInitCycleMessage() {
      ArrayList<String> attributes = Lists.newArrayList();
      if (destinationBuildStage == STAGE_INITIALIZING) attributes.add("destination");
      if (socketBuildStage == STAGE_INITIALIZING) attributes.add("socket");
      if (packetParserBuildStage == STAGE_INITIALIZING) attributes.add("packetParser");
      if (loggerBuildStage == STAGE_INITIALIZING) attributes.add("logger");
      if (executorServiceBuildStage == STAGE_INITIALIZING) attributes.add("executorService");
      if (readerThreadProviderBuildStage == STAGE_INITIALIZING) attributes.add("readerThreadProvider");
      return "Cannot build UdpServer, attribute initializers form cycle" + attributes;
    }
  }

  /**
   * @return The value of the {@code clientKeyProvider} attribute
   */
  @Override
  public Function<UUID, SecretKeySpec> getClientKeyProvider() {
    return clientKeyProvider;
  }

  /**
   * @return The value of the {@code destination} attribute
   */
  @Override
  public InetAddress getDestination() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.getDestination()
        : this.destination;
  }

  /**
   * @return The value of the {@code socket} attribute
   */
  @Override
  public DatagramSocket getSocket() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.getSocket()
        : this.socket;
  }

  /**
   * @return The value of the {@code id} attribute
   */
  @Override
  public UUID getId() {
    return id;
  }

  /**
   * @return The value of the {@code secretKey} attribute
   */
  @Override
  public SecretKeySpec getSecretKey() {
    return secretKey;
  }

  /**
   * @return The value of the {@code packetHandler} attribute
   */
  @Override
  public BiConsumer<UdpServer, Packet> getPacketHandler() {
    return packetHandler;
  }

  /**
   * @return The value of the {@code port} attribute
   */
  @Override
  public int getPort() {
    return port;
  }

  /**
   * @return The value of the {@code packetParser} attribute
   */
  @Override
  public PacketParser getPacketParser() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.getPacketParser()
        : this.packetParser;
  }

  /**
   * @return The value of the {@code logger} attribute
   */
  @Override
  public Logger getLogger() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.getLogger()
        : this.logger;
  }

  /**
   * @return The value of the {@code executorService} attribute
   */
  @Override
  public ExecutorService getExecutorService() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.getExecutorService()
        : this.executorService;
  }

  /**
   * @return The value of the {@code readerThreadProvider} attribute
   */
  @Override
  public Function<UdpServer, UdpCommon.ReaderThread> getReaderThreadProvider() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.getReaderThreadProvider()
        : this.readerThreadProvider;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getClientKeyProvider() clientKeyProvider} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for clientKeyProvider
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withClientKeyProvider(Function<UUID, SecretKeySpec> value) {
    if (this.clientKeyProvider == value) return this;
    Function<UUID, SecretKeySpec> newValue = Objects.requireNonNull(value, "clientKeyProvider");
    return new ImmutableUdpServer(
        newValue,
        this.destination,
        this.socket,
        this.id,
        this.secretKey,
        this.packetHandler,
        this.port,
        this.packetParser,
        this.logger,
        this.executorService,
        this.readerThreadProvider);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getDestination() destination} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for destination
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withDestination(InetAddress value) {
    if (this.destination == value) return this;
    InetAddress newValue = Objects.requireNonNull(value, "destination");
    return new ImmutableUdpServer(
        this.clientKeyProvider,
        newValue,
        this.socket,
        this.id,
        this.secretKey,
        this.packetHandler,
        this.port,
        this.packetParser,
        this.logger,
        this.executorService,
        this.readerThreadProvider);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getSocket() socket} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for socket
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withSocket(DatagramSocket value) {
    if (this.socket == value) return this;
    DatagramSocket newValue = Objects.requireNonNull(value, "socket");
    return new ImmutableUdpServer(
        this.clientKeyProvider,
        this.destination,
        newValue,
        this.id,
        this.secretKey,
        this.packetHandler,
        this.port,
        this.packetParser,
        this.logger,
        this.executorService,
        this.readerThreadProvider);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getId() id} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for id
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withId(UUID value) {
    if (this.id == value) return this;
    UUID newValue = Objects.requireNonNull(value, "id");
    return new ImmutableUdpServer(
        this.clientKeyProvider,
        this.destination,
        this.socket,
        newValue,
        this.secretKey,
        this.packetHandler,
        this.port,
        this.packetParser,
        this.logger,
        this.executorService,
        this.readerThreadProvider);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getSecretKey() secretKey} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for secretKey
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withSecretKey(SecretKeySpec value) {
    if (this.secretKey == value) return this;
    SecretKeySpec newValue = Objects.requireNonNull(value, "secretKey");
    return new ImmutableUdpServer(
        this.clientKeyProvider,
        this.destination,
        this.socket,
        this.id,
        newValue,
        this.packetHandler,
        this.port,
        this.packetParser,
        this.logger,
        this.executorService,
        this.readerThreadProvider);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getPacketHandler() packetHandler} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for packetHandler
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withPacketHandler(BiConsumer<UdpServer, Packet> value) {
    if (this.packetHandler == value) return this;
    BiConsumer<UdpServer, Packet> newValue = Objects.requireNonNull(value, "packetHandler");
    return new ImmutableUdpServer(
        this.clientKeyProvider,
        this.destination,
        this.socket,
        this.id,
        this.secretKey,
        newValue,
        this.port,
        this.packetParser,
        this.logger,
        this.executorService,
        this.readerThreadProvider);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getPort() port} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for port
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withPort(int value) {
    if (this.port == value) return this;
    return new ImmutableUdpServer(
        this.clientKeyProvider,
        this.destination,
        this.socket,
        this.id,
        this.secretKey,
        this.packetHandler,
        value,
        this.packetParser,
        this.logger,
        this.executorService,
        this.readerThreadProvider);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getPacketParser() packetParser} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for packetParser
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withPacketParser(PacketParser value) {
    if (this.packetParser == value) return this;
    PacketParser newValue = Objects.requireNonNull(value, "packetParser");
    return new ImmutableUdpServer(
        this.clientKeyProvider,
        this.destination,
        this.socket,
        this.id,
        this.secretKey,
        this.packetHandler,
        this.port,
        newValue,
        this.logger,
        this.executorService,
        this.readerThreadProvider);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getLogger() logger} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for logger
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withLogger(Logger value) {
    if (this.logger == value) return this;
    Logger newValue = Objects.requireNonNull(value, "logger");
    return new ImmutableUdpServer(
        this.clientKeyProvider,
        this.destination,
        this.socket,
        this.id,
        this.secretKey,
        this.packetHandler,
        this.port,
        this.packetParser,
        newValue,
        this.executorService,
        this.readerThreadProvider);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getExecutorService() executorService} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for executorService
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withExecutorService(ExecutorService value) {
    if (this.executorService == value) return this;
    ExecutorService newValue = Objects.requireNonNull(value, "executorService");
    return new ImmutableUdpServer(
        this.clientKeyProvider,
        this.destination,
        this.socket,
        this.id,
        this.secretKey,
        this.packetHandler,
        this.port,
        this.packetParser,
        this.logger,
        newValue,
        this.readerThreadProvider);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UdpServer#getReaderThreadProvider() readerThreadProvider} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for readerThreadProvider
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUdpServer withReaderThreadProvider(Function<UdpServer, UdpCommon.ReaderThread> value) {
    if (this.readerThreadProvider == value) return this;
    Function<UdpServer, UdpCommon.ReaderThread> newValue = Objects.requireNonNull(value, "readerThreadProvider");
    return new ImmutableUdpServer(
        this.clientKeyProvider,
        this.destination,
        this.socket,
        this.id,
        this.secretKey,
        this.packetHandler,
        this.port,
        this.packetParser,
        this.logger,
        this.executorService,
        newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableUdpServer} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableUdpServer
        && equalTo((ImmutableUdpServer) another);
  }

  private boolean equalTo(ImmutableUdpServer another) {
    return clientKeyProvider.equals(another.clientKeyProvider)
        && destination.equals(another.destination)
        && socket.equals(another.socket)
        && id.equals(another.id)
        && secretKey.equals(another.secretKey)
        && packetHandler.equals(another.packetHandler)
        && port == another.port
        && packetParser.equals(another.packetParser)
        && logger.equals(another.logger)
        && executorService.equals(another.executorService)
        && readerThreadProvider.equals(another.readerThreadProvider);
  }

  /**
   * Computes a hash code from attributes: {@code clientKeyProvider}, {@code destination}, {@code socket}, {@code id}, {@code secretKey}, {@code packetHandler}, {@code port}, {@code packetParser}, {@code logger}, {@code executorService}, {@code readerThreadProvider}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + clientKeyProvider.hashCode();
    h += (h << 5) + destination.hashCode();
    h += (h << 5) + socket.hashCode();
    h += (h << 5) + id.hashCode();
    h += (h << 5) + secretKey.hashCode();
    h += (h << 5) + packetHandler.hashCode();
    h += (h << 5) + port;
    h += (h << 5) + packetParser.hashCode();
    h += (h << 5) + logger.hashCode();
    h += (h << 5) + executorService.hashCode();
    h += (h << 5) + readerThreadProvider.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code UdpServer} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper("UdpServer")
        .omitNullValues()
        .add("clientKeyProvider", clientKeyProvider)
        .add("destination", destination)
        .add("socket", socket)
        .add("id", id)
        .add("secretKey", secretKey)
        .add("packetHandler", packetHandler)
        .add("port", port)
        .add("packetParser", packetParser)
        .add("logger", logger)
        .add("executorService", executorService)
        .add("readerThreadProvider", readerThreadProvider)
        .toString();
  }

  /**
   * Creates an immutable copy of a {@link UdpServer} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable UdpServer instance
   */
  public static ImmutableUdpServer copyOf(UdpServer instance) {
    if (instance instanceof ImmutableUdpServer) {
      return (ImmutableUdpServer) instance;
    }
    return ImmutableUdpServer.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableUdpServer ImmutableUdpServer}.
   * @return A new ImmutableUdpServer builder
   */
  public static ImmutableUdpServer.Builder builder() {
    return new ImmutableUdpServer.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableUdpServer ImmutableUdpServer}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  public static final class Builder {
    private static final long INIT_BIT_CLIENT_KEY_PROVIDER = 0x1L;
    private static final long INIT_BIT_ID = 0x2L;
    private static final long INIT_BIT_SECRET_KEY = 0x4L;
    private static final long INIT_BIT_PACKET_HANDLER = 0x8L;
    private static final long INIT_BIT_PORT = 0x10L;
    private long initBits = 0x1fL;

    private Function<UUID, SecretKeySpec> clientKeyProvider;
    private InetAddress destination;
    private DatagramSocket socket;
    private UUID id;
    private SecretKeySpec secretKey;
    private BiConsumer<UdpServer, Packet> packetHandler;
    private int port;
    private PacketParser packetParser;
    private Logger logger;
    private ExecutorService executorService;
    private Function<UdpServer, UdpCommon.ReaderThread> readerThreadProvider;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code UdpServer} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(UdpServer instance) {
      Objects.requireNonNull(instance, "instance");
      withClientKeyProvider(instance.getClientKeyProvider());
      withDestination(instance.getDestination());
      withSocket(instance.getSocket());
      withId(instance.getId());
      withSecretKey(instance.getSecretKey());
      withPacketHandler(instance.getPacketHandler());
      withPort(instance.getPort());
      withPacketParser(instance.getPacketParser());
      withLogger(instance.getLogger());
      withExecutorService(instance.getExecutorService());
      withReaderThreadProvider(instance.getReaderThreadProvider());
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getClientKeyProvider() clientKeyProvider} attribute.
     * @param clientKeyProvider The value for clientKeyProvider 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withClientKeyProvider(Function<UUID, SecretKeySpec> clientKeyProvider) {
      this.clientKeyProvider = Objects.requireNonNull(clientKeyProvider, "clientKeyProvider");
      initBits &= ~INIT_BIT_CLIENT_KEY_PROVIDER;
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getDestination() destination} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link UdpServer#getDestination() destination}.</em>
     * @param destination The value for destination 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withDestination(InetAddress destination) {
      this.destination = Objects.requireNonNull(destination, "destination");
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getSocket() socket} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link UdpServer#getSocket() socket}.</em>
     * @param socket The value for socket 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withSocket(DatagramSocket socket) {
      this.socket = Objects.requireNonNull(socket, "socket");
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getId() id} attribute.
     * @param id The value for id 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withId(UUID id) {
      this.id = Objects.requireNonNull(id, "id");
      initBits &= ~INIT_BIT_ID;
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getSecretKey() secretKey} attribute.
     * @param secretKey The value for secretKey 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withSecretKey(SecretKeySpec secretKey) {
      this.secretKey = Objects.requireNonNull(secretKey, "secretKey");
      initBits &= ~INIT_BIT_SECRET_KEY;
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getPacketHandler() packetHandler} attribute.
     * @param packetHandler The value for packetHandler 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withPacketHandler(BiConsumer<UdpServer, Packet> packetHandler) {
      this.packetHandler = Objects.requireNonNull(packetHandler, "packetHandler");
      initBits &= ~INIT_BIT_PACKET_HANDLER;
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getPort() port} attribute.
     * @param port The value for port 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withPort(int port) {
      this.port = port;
      initBits &= ~INIT_BIT_PORT;
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getPacketParser() packetParser} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link UdpServer#getPacketParser() packetParser}.</em>
     * @param packetParser The value for packetParser 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withPacketParser(PacketParser packetParser) {
      this.packetParser = Objects.requireNonNull(packetParser, "packetParser");
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getLogger() logger} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link UdpServer#getLogger() logger}.</em>
     * @param logger The value for logger 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withLogger(Logger logger) {
      this.logger = Objects.requireNonNull(logger, "logger");
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getExecutorService() executorService} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link UdpServer#getExecutorService() executorService}.</em>
     * @param executorService The value for executorService 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withExecutorService(ExecutorService executorService) {
      this.executorService = Objects.requireNonNull(executorService, "executorService");
      return this;
    }

    /**
     * Initializes the value for the {@link UdpServer#getReaderThreadProvider() readerThreadProvider} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link UdpServer#getReaderThreadProvider() readerThreadProvider}.</em>
     * @param readerThreadProvider The value for readerThreadProvider 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder withReaderThreadProvider(Function<UdpServer, UdpCommon.ReaderThread> readerThreadProvider) {
      this.readerThreadProvider = Objects.requireNonNull(readerThreadProvider, "readerThreadProvider");
      return this;
    }

    /**
     * Builds a new {@link ImmutableUdpServer ImmutableUdpServer}.
     * @return An immutable instance of UdpServer
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableUdpServer build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableUdpServer(this);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = Lists.newArrayList();
      if ((initBits & INIT_BIT_CLIENT_KEY_PROVIDER) != 0) attributes.add("clientKeyProvider");
      if ((initBits & INIT_BIT_ID) != 0) attributes.add("id");
      if ((initBits & INIT_BIT_SECRET_KEY) != 0) attributes.add("secretKey");
      if ((initBits & INIT_BIT_PACKET_HANDLER) != 0) attributes.add("packetHandler");
      if ((initBits & INIT_BIT_PORT) != 0) attributes.add("port");
      return "Cannot build UdpServer, some of required attributes are not set " + attributes;
    }
  }
}
