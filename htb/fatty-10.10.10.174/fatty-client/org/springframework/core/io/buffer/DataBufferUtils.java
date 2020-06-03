/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AsynchronousFileChannel;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Consumer;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscription;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import reactor.core.CoreSubscriber;
/*     */ import reactor.core.Disposable;
/*     */ import reactor.core.publisher.BaseSubscriber;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.FluxSink;
/*     */ import reactor.core.publisher.Mono;
/*     */ import reactor.core.publisher.SynchronousSink;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DataBufferUtils
/*     */ {
/*  58 */   private static final Consumer<DataBuffer> RELEASE_CONSUMER = DataBufferUtils::release;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> readInputStream(Callable<InputStream> inputStreamSupplier, DataBufferFactory bufferFactory, int bufferSize) {
/*  77 */     Assert.notNull(inputStreamSupplier, "'inputStreamSupplier' must not be null");
/*  78 */     return readByteChannel(() -> Channels.newChannel(inputStreamSupplier.call()), bufferFactory, bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> readByteChannel(Callable<ReadableByteChannel> channelSupplier, DataBufferFactory bufferFactory, int bufferSize) {
/*  93 */     Assert.notNull(channelSupplier, "'channelSupplier' must not be null");
/*  94 */     Assert.notNull(bufferFactory, "'dataBufferFactory' must not be null");
/*  95 */     Assert.isTrue((bufferSize > 0), "'bufferSize' must be > 0");
/*     */     
/*  97 */     return Flux.using(channelSupplier, channel -> Flux.generate(new ReadableByteChannelGenerator(channel, bufferFactory, bufferSize)), DataBufferUtils::closeChannel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> readAsynchronousFileChannel(Callable<AsynchronousFileChannel> channelSupplier, DataBufferFactory bufferFactory, int bufferSize) {
/* 116 */     return readAsynchronousFileChannel(channelSupplier, 0L, bufferFactory, bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> readAsynchronousFileChannel(Callable<AsynchronousFileChannel> channelSupplier, long position, DataBufferFactory bufferFactory, int bufferSize) {
/* 133 */     Assert.notNull(channelSupplier, "'channelSupplier' must not be null");
/* 134 */     Assert.notNull(bufferFactory, "'dataBufferFactory' must not be null");
/* 135 */     Assert.isTrue((position >= 0L), "'position' must be >= 0");
/* 136 */     Assert.isTrue((bufferSize > 0), "'bufferSize' must be > 0");
/*     */     
/* 138 */     Flux<DataBuffer> flux = Flux.using(channelSupplier, channel -> Flux.create(()), channel -> {
/*     */         
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     return flux.doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> read(Resource resource, DataBufferFactory bufferFactory, int bufferSize) {
/* 168 */     return read(resource, 0L, bufferFactory, bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> read(Resource resource, long position, DataBufferFactory bufferFactory, int bufferSize) {
/*     */     try {
/* 189 */       if (resource.isFile()) {
/* 190 */         File file = resource.getFile();
/* 191 */         return readAsynchronousFileChannel(() -> AsynchronousFileChannel.open(file.toPath(), new OpenOption[] { StandardOpenOption.READ }), position, bufferFactory, bufferSize);
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 196 */     catch (IOException iOException) {}
/*     */ 
/*     */     
/* 199 */     Flux<DataBuffer> result = readByteChannel(resource::readableChannel, bufferFactory, bufferSize);
/* 200 */     return (position == 0L) ? result : skipUntilByteCount((Publisher<DataBuffer>)result, position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> write(Publisher<DataBuffer> source, OutputStream outputStream) {
/* 224 */     Assert.notNull(source, "'source' must not be null");
/* 225 */     Assert.notNull(outputStream, "'outputStream' must not be null");
/*     */     
/* 227 */     WritableByteChannel channel = Channels.newChannel(outputStream);
/* 228 */     return write(source, channel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> write(Publisher<DataBuffer> source, WritableByteChannel channel) {
/* 247 */     Assert.notNull(source, "'source' must not be null");
/* 248 */     Assert.notNull(channel, "'channel' must not be null");
/*     */     
/* 250 */     Flux<DataBuffer> flux = Flux.from(source);
/* 251 */     return Flux.create(sink -> {
/*     */           WritableByteChannelSubscriber subscriber = new WritableByteChannelSubscriber(sink, channel);
/*     */           sink.onDispose((Disposable)subscriber);
/*     */           flux.subscribe((CoreSubscriber)subscriber);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> write(Publisher<DataBuffer> source, AsynchronousFileChannel channel) {
/* 275 */     return write(source, channel, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> write(Publisher<DataBuffer> source, AsynchronousFileChannel channel, long position) {
/* 297 */     Assert.notNull(source, "'source' must not be null");
/* 298 */     Assert.notNull(channel, "'channel' must not be null");
/* 299 */     Assert.isTrue((position >= 0L), "'position' must be >= 0");
/*     */     
/* 301 */     Flux<DataBuffer> flux = Flux.from(source);
/* 302 */     return Flux.create(sink -> {
/*     */           WriteCompletionHandler handler = new WriteCompletionHandler(sink, channel, position);
/*     */           sink.onDispose((Disposable)handler);
/*     */           flux.subscribe((CoreSubscriber)handler);
/*     */         });
/*     */   }
/*     */   
/*     */   static void closeChannel(@Nullable Channel channel) {
/* 310 */     if (channel != null && channel.isOpen()) {
/*     */       try {
/* 312 */         channel.close();
/*     */       }
/* 314 */       catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> takeUntilByteCount(Publisher<DataBuffer> publisher, long maxByteCount) {
/* 333 */     Assert.notNull(publisher, "Publisher must not be null");
/* 334 */     Assert.isTrue((maxByteCount >= 0L), "'maxByteCount' must be a positive number");
/*     */     
/* 336 */     AtomicLong countDown = new AtomicLong(maxByteCount);
/* 337 */     return Flux.from(publisher)
/* 338 */       .map(buffer -> {
/*     */           long remainder = countDown.addAndGet(-buffer.readableByteCount());
/*     */           
/*     */           if (remainder < 0L) {
/*     */             int length = buffer.readableByteCount() + (int)remainder;
/*     */             
/*     */             return buffer.slice(0, length);
/*     */           } 
/*     */           
/*     */           return buffer;
/* 348 */         }).takeUntil(buffer -> (countDown.get() <= 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Flux<DataBuffer> skipUntilByteCount(Publisher<DataBuffer> publisher, long maxByteCount) {
/* 362 */     Assert.notNull(publisher, "Publisher must not be null");
/* 363 */     Assert.isTrue((maxByteCount >= 0L), "'maxByteCount' must be a positive number");
/*     */     
/* 365 */     return Flux.defer(() -> {
/*     */           AtomicLong countDown = new AtomicLong(maxByteCount);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return (Publisher)Flux.from(publisher).skipUntil(()).map(());
/* 384 */         }).doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends DataBuffer> T retain(T dataBuffer) {
/* 394 */     if (dataBuffer instanceof PooledDataBuffer) {
/* 395 */       return (T)((PooledDataBuffer)dataBuffer).retain();
/*     */     }
/*     */     
/* 398 */     return dataBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean release(@Nullable DataBuffer dataBuffer) {
/* 409 */     if (dataBuffer instanceof PooledDataBuffer) {
/* 410 */       PooledDataBuffer pooledDataBuffer = (PooledDataBuffer)dataBuffer;
/* 411 */       if (pooledDataBuffer.isAllocated()) {
/* 412 */         return pooledDataBuffer.release();
/*     */       }
/*     */     } 
/* 415 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Consumer<DataBuffer> releaseConsumer() {
/* 423 */     return RELEASE_CONSUMER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Mono<DataBuffer> join(Publisher<DataBuffer> dataBuffers) {
/* 442 */     Assert.notNull(dataBuffers, "'dataBuffers' must not be null");
/*     */     
/* 444 */     return Flux.from(dataBuffers)
/* 445 */       .collectList()
/* 446 */       .filter(list -> !list.isEmpty())
/* 447 */       .map(list -> ((DataBuffer)list.get(0)).factory().join(list))
/* 448 */       .doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ReadableByteChannelGenerator
/*     */     implements Consumer<SynchronousSink<DataBuffer>>
/*     */   {
/*     */     private final ReadableByteChannel channel;
/*     */     
/*     */     private final DataBufferFactory dataBufferFactory;
/*     */     
/*     */     private final int bufferSize;
/*     */ 
/*     */     
/*     */     public ReadableByteChannelGenerator(ReadableByteChannel channel, DataBufferFactory dataBufferFactory, int bufferSize) {
/* 464 */       this.channel = channel;
/* 465 */       this.dataBufferFactory = dataBufferFactory;
/* 466 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(SynchronousSink<DataBuffer> sink) {
/* 471 */       boolean release = true;
/* 472 */       DataBuffer dataBuffer = this.dataBufferFactory.allocateBuffer(this.bufferSize);
/*     */       
/*     */       try {
/* 475 */         ByteBuffer byteBuffer = dataBuffer.asByteBuffer(0, dataBuffer.capacity()); int read;
/* 476 */         if ((read = this.channel.read(byteBuffer)) >= 0) {
/* 477 */           dataBuffer.writePosition(read);
/* 478 */           release = false;
/* 479 */           sink.next(dataBuffer);
/*     */         } else {
/*     */           
/* 482 */           sink.complete();
/*     */         }
/*     */       
/* 485 */       } catch (IOException ex) {
/* 486 */         sink.error(ex);
/*     */       } finally {
/*     */         
/* 489 */         if (release) {
/* 490 */           DataBufferUtils.release(dataBuffer);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ReadCompletionHandler
/*     */     implements CompletionHandler<Integer, DataBuffer>
/*     */   {
/*     */     private final AsynchronousFileChannel channel;
/*     */     
/*     */     private final FluxSink<DataBuffer> sink;
/*     */     
/*     */     private final DataBufferFactory dataBufferFactory;
/*     */     
/*     */     private final int bufferSize;
/*     */     
/*     */     private final AtomicLong position;
/* 509 */     private final AtomicBoolean disposed = new AtomicBoolean();
/*     */ 
/*     */ 
/*     */     
/*     */     public ReadCompletionHandler(AsynchronousFileChannel channel, FluxSink<DataBuffer> sink, long position, DataBufferFactory dataBufferFactory, int bufferSize) {
/* 514 */       this.channel = channel;
/* 515 */       this.sink = sink;
/* 516 */       this.position = new AtomicLong(position);
/* 517 */       this.dataBufferFactory = dataBufferFactory;
/* 518 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void completed(Integer read, DataBuffer dataBuffer) {
/* 523 */       if (read.intValue() != -1 && !this.disposed.get()) {
/* 524 */         long pos = this.position.addAndGet(read.intValue());
/* 525 */         dataBuffer.writePosition(read.intValue());
/* 526 */         this.sink.next(dataBuffer);
/*     */         
/* 528 */         if (this.disposed.get()) {
/* 529 */           complete();
/*     */         } else {
/*     */           
/* 532 */           DataBuffer newDataBuffer = this.dataBufferFactory.allocateBuffer(this.bufferSize);
/* 533 */           ByteBuffer newByteBuffer = newDataBuffer.asByteBuffer(0, this.bufferSize);
/* 534 */           this.channel.read(newByteBuffer, pos, newDataBuffer, this);
/*     */         } 
/*     */       } else {
/*     */         
/* 538 */         DataBufferUtils.release(dataBuffer);
/* 539 */         complete();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void complete() {
/* 544 */       this.sink.complete();
/* 545 */       DataBufferUtils.closeChannel(this.channel);
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Throwable exc, DataBuffer dataBuffer) {
/* 550 */       DataBufferUtils.release(dataBuffer);
/* 551 */       this.sink.error(exc);
/* 552 */       DataBufferUtils.closeChannel(this.channel);
/*     */     }
/*     */     
/*     */     public void dispose() {
/* 556 */       this.disposed.set(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class WritableByteChannelSubscriber
/*     */     extends BaseSubscriber<DataBuffer>
/*     */   {
/*     */     private final FluxSink<DataBuffer> sink;
/*     */     private final WritableByteChannel channel;
/*     */     
/*     */     public WritableByteChannelSubscriber(FluxSink<DataBuffer> sink, WritableByteChannel channel) {
/* 568 */       this.sink = sink;
/* 569 */       this.channel = channel;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void hookOnSubscribe(Subscription subscription) {
/* 574 */       request(1L);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void hookOnNext(DataBuffer dataBuffer) {
/*     */       try {
/* 580 */         ByteBuffer byteBuffer = dataBuffer.asByteBuffer();
/* 581 */         while (byteBuffer.hasRemaining()) {
/* 582 */           this.channel.write(byteBuffer);
/*     */         }
/* 584 */         this.sink.next(dataBuffer);
/* 585 */         request(1L);
/*     */       }
/* 587 */       catch (IOException ex) {
/* 588 */         this.sink.next(dataBuffer);
/* 589 */         this.sink.error(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void hookOnError(Throwable throwable) {
/* 595 */       this.sink.error(throwable);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void hookOnComplete() {
/* 600 */       this.sink.complete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class WriteCompletionHandler
/*     */     extends BaseSubscriber<DataBuffer>
/*     */     implements CompletionHandler<Integer, ByteBuffer>
/*     */   {
/*     */     private final FluxSink<DataBuffer> sink;
/*     */     
/*     */     private final AsynchronousFileChannel channel;
/* 612 */     private final AtomicBoolean completed = new AtomicBoolean();
/*     */     
/* 614 */     private final AtomicReference<Throwable> error = new AtomicReference<>();
/*     */     
/*     */     private final AtomicLong position;
/*     */     
/* 618 */     private final AtomicReference<DataBuffer> dataBuffer = new AtomicReference<>();
/*     */ 
/*     */ 
/*     */     
/*     */     public WriteCompletionHandler(FluxSink<DataBuffer> sink, AsynchronousFileChannel channel, long position) {
/* 623 */       this.sink = sink;
/* 624 */       this.channel = channel;
/* 625 */       this.position = new AtomicLong(position);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void hookOnSubscribe(Subscription subscription) {
/* 630 */       request(1L);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void hookOnNext(DataBuffer value) {
/* 635 */       if (!this.dataBuffer.compareAndSet(null, value)) {
/* 636 */         throw new IllegalStateException();
/*     */       }
/* 638 */       ByteBuffer byteBuffer = value.asByteBuffer();
/* 639 */       this.channel.write(byteBuffer, this.position.get(), byteBuffer, this);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void hookOnError(Throwable throwable) {
/* 644 */       this.error.set(throwable);
/*     */       
/* 646 */       if (this.dataBuffer.get() == null) {
/* 647 */         this.sink.error(throwable);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void hookOnComplete() {
/* 653 */       this.completed.set(true);
/*     */       
/* 655 */       if (this.dataBuffer.get() == null) {
/* 656 */         this.sink.complete();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void completed(Integer written, ByteBuffer byteBuffer) {
/* 662 */       long pos = this.position.addAndGet(written.intValue());
/* 663 */       if (byteBuffer.hasRemaining()) {
/* 664 */         this.channel.write(byteBuffer, pos, byteBuffer, this);
/*     */         return;
/*     */       } 
/* 667 */       sinkDataBuffer();
/*     */       
/* 669 */       Throwable throwable = this.error.get();
/* 670 */       if (throwable != null) {
/* 671 */         this.sink.error(throwable);
/*     */       }
/* 673 */       else if (this.completed.get()) {
/* 674 */         this.sink.complete();
/*     */       } else {
/*     */         
/* 677 */         request(1L);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Throwable exc, ByteBuffer byteBuffer) {
/* 683 */       sinkDataBuffer();
/* 684 */       this.sink.error(exc);
/*     */     }
/*     */     
/*     */     private void sinkDataBuffer() {
/* 688 */       DataBuffer dataBuffer = this.dataBuffer.get();
/* 689 */       Assert.state((dataBuffer != null), "DataBuffer should not be null");
/* 690 */       this.sink.next(dataBuffer);
/* 691 */       this.dataBuffer.set(null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/buffer/DataBufferUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */