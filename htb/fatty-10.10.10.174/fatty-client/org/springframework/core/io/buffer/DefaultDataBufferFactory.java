/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class DefaultDataBufferFactory
/*     */   implements DataBufferFactory
/*     */ {
/*     */   public static final int DEFAULT_INITIAL_CAPACITY = 256;
/*     */   private final boolean preferDirect;
/*     */   private final int defaultInitialCapacity;
/*     */   
/*     */   public DefaultDataBufferFactory() {
/*  51 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBufferFactory(boolean preferDirect) {
/*  62 */     this(preferDirect, 256);
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
/*     */   public DefaultDataBufferFactory(boolean preferDirect, int defaultInitialCapacity) {
/*  74 */     Assert.isTrue((defaultInitialCapacity > 0), "'defaultInitialCapacity' should be larger than 0");
/*  75 */     this.preferDirect = preferDirect;
/*  76 */     this.defaultInitialCapacity = defaultInitialCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer allocateBuffer() {
/*  82 */     return allocateBuffer(this.defaultInitialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer allocateBuffer(int initialCapacity) {
/*  89 */     ByteBuffer byteBuffer = this.preferDirect ? ByteBuffer.allocateDirect(initialCapacity) : ByteBuffer.allocate(initialCapacity);
/*  90 */     return DefaultDataBuffer.fromEmptyByteBuffer(this, byteBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer wrap(ByteBuffer byteBuffer) {
/*  95 */     return DefaultDataBuffer.fromFilledByteBuffer(this, byteBuffer.slice());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer wrap(byte[] bytes) {
/* 100 */     return DefaultDataBuffer.fromFilledByteBuffer(this, ByteBuffer.wrap(bytes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer join(List<? extends DataBuffer> dataBuffers) {
/* 110 */     Assert.notEmpty(dataBuffers, "DataBuffer List must not be empty");
/* 111 */     int capacity = dataBuffers.stream().mapToInt(DataBuffer::readableByteCount).sum();
/* 112 */     DefaultDataBuffer result = allocateBuffer(capacity);
/* 113 */     dataBuffers.forEach(xva$0 -> rec$.write(new DataBuffer[] { xva$0 }));
/* 114 */     dataBuffers.forEach(DataBufferUtils::release);
/* 115 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 121 */     return "DefaultDataBufferFactory (preferDirect=" + this.preferDirect + ")";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/buffer/DefaultDataBufferFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */