/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.function.IntPredicate;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class NettyDataBuffer
/*     */   implements PooledDataBuffer
/*     */ {
/*     */   private final ByteBuf byteBuf;
/*     */   private final NettyDataBufferFactory dataBufferFactory;
/*     */   
/*     */   NettyDataBuffer(ByteBuf byteBuf, NettyDataBufferFactory dataBufferFactory) {
/*  54 */     Assert.notNull(byteBuf, "ByteBuf must not be null");
/*  55 */     Assert.notNull(dataBufferFactory, "NettyDataBufferFactory must not be null");
/*  56 */     this.byteBuf = byteBuf;
/*  57 */     this.dataBufferFactory = dataBufferFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf getNativeBuffer() {
/*  66 */     return this.byteBuf;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBufferFactory factory() {
/*  71 */     return this.dataBufferFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(IntPredicate predicate, int fromIndex) {
/*  76 */     Assert.notNull(predicate, "IntPredicate must not be null");
/*  77 */     if (fromIndex < 0) {
/*  78 */       fromIndex = 0;
/*     */     }
/*  80 */     else if (fromIndex >= this.byteBuf.writerIndex()) {
/*  81 */       return -1;
/*     */     } 
/*  83 */     int length = this.byteBuf.writerIndex() - fromIndex;
/*  84 */     return this.byteBuf.forEachByte(fromIndex, length, predicate.negate()::test);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(IntPredicate predicate, int fromIndex) {
/*  89 */     Assert.notNull(predicate, "IntPredicate must not be null");
/*  90 */     if (fromIndex < 0) {
/*  91 */       return -1;
/*     */     }
/*  93 */     fromIndex = Math.min(fromIndex, this.byteBuf.writerIndex() - 1);
/*  94 */     return this.byteBuf.forEachByteDesc(0, fromIndex + 1, predicate.negate()::test);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readableByteCount() {
/*  99 */     return this.byteBuf.readableBytes();
/*     */   }
/*     */ 
/*     */   
/*     */   public int writableByteCount() {
/* 104 */     return this.byteBuf.writableBytes();
/*     */   }
/*     */ 
/*     */   
/*     */   public int readPosition() {
/* 109 */     return this.byteBuf.readerIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer readPosition(int readPosition) {
/* 114 */     this.byteBuf.readerIndex(readPosition);
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int writePosition() {
/* 120 */     return this.byteBuf.writerIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer writePosition(int writePosition) {
/* 125 */     this.byteBuf.writerIndex(writePosition);
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/* 131 */     return this.byteBuf.getByte(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 136 */     return this.byteBuf.capacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer capacity(int capacity) {
/* 141 */     this.byteBuf.capacity(capacity);
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer ensureCapacity(int capacity) {
/* 147 */     this.byteBuf.ensureWritable(capacity);
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte read() {
/* 153 */     return this.byteBuf.readByte();
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer read(byte[] destination) {
/* 158 */     this.byteBuf.readBytes(destination);
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer read(byte[] destination, int offset, int length) {
/* 164 */     this.byteBuf.readBytes(destination, offset, length);
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(byte b) {
/* 170 */     this.byteBuf.writeByte(b);
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(byte[] source) {
/* 176 */     this.byteBuf.writeBytes(source);
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(byte[] source, int offset, int length) {
/* 182 */     this.byteBuf.writeBytes(source, offset, length);
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(DataBuffer... buffers) {
/* 188 */     if (!ObjectUtils.isEmpty((Object[])buffers)) {
/* 189 */       if (hasNettyDataBuffers(buffers)) {
/* 190 */         ByteBuf[] nativeBuffers = new ByteBuf[buffers.length];
/* 191 */         for (int i = 0; i < buffers.length; i++) {
/* 192 */           nativeBuffers[i] = ((NettyDataBuffer)buffers[i]).getNativeBuffer();
/*     */         }
/* 194 */         write(nativeBuffers);
/*     */       } else {
/*     */         
/* 197 */         ByteBuffer[] byteBuffers = new ByteBuffer[buffers.length];
/* 198 */         for (int i = 0; i < buffers.length; i++) {
/* 199 */           byteBuffers[i] = buffers[i].asByteBuffer();
/*     */         }
/*     */         
/* 202 */         write(byteBuffers);
/*     */       } 
/*     */     }
/* 205 */     return this;
/*     */   }
/*     */   
/*     */   private static boolean hasNettyDataBuffers(DataBuffer[] buffers) {
/* 209 */     for (DataBuffer buffer : buffers) {
/* 210 */       if (!(buffer instanceof NettyDataBuffer)) {
/* 211 */         return false;
/*     */       }
/*     */     } 
/* 214 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(ByteBuffer... buffers) {
/* 219 */     if (!ObjectUtils.isEmpty((Object[])buffers)) {
/* 220 */       for (ByteBuffer buffer : buffers) {
/* 221 */         this.byteBuf.writeBytes(buffer);
/*     */       }
/*     */     }
/* 224 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(ByteBuf... byteBufs) {
/* 234 */     if (!ObjectUtils.isEmpty((Object[])byteBufs)) {
/* 235 */       for (ByteBuf byteBuf : byteBufs) {
/* 236 */         this.byteBuf.writeBytes(byteBuf);
/*     */       }
/*     */     }
/* 239 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer write(CharSequence charSequence, Charset charset) {
/* 244 */     Assert.notNull(charSequence, "CharSequence must not be null");
/* 245 */     Assert.notNull(charset, "Charset must not be null");
/* 246 */     if (StandardCharsets.UTF_8.equals(charset)) {
/* 247 */       ByteBufUtil.writeUtf8(this.byteBuf, charSequence);
/*     */     }
/* 249 */     else if (StandardCharsets.US_ASCII.equals(charset)) {
/* 250 */       ByteBufUtil.writeAscii(this.byteBuf, charSequence);
/*     */     } else {
/*     */       
/* 253 */       return super.write(charSequence, charset);
/*     */     } 
/* 255 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer slice(int index, int length) {
/* 260 */     ByteBuf slice = this.byteBuf.slice(index, length);
/* 261 */     return new NettyDataBuffer(slice, this.dataBufferFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer asByteBuffer() {
/* 266 */     return this.byteBuf.nioBuffer();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer asByteBuffer(int index, int length) {
/* 271 */     return this.byteBuf.nioBuffer(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream asInputStream() {
/* 276 */     return (InputStream)new ByteBufInputStream(this.byteBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream asInputStream(boolean releaseOnClose) {
/* 281 */     return (InputStream)new ByteBufInputStream(this.byteBuf, releaseOnClose);
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream asOutputStream() {
/* 286 */     return (OutputStream)new ByteBufOutputStream(this.byteBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllocated() {
/* 291 */     return (this.byteBuf.refCnt() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public PooledDataBuffer retain() {
/* 296 */     return new NettyDataBuffer(this.byteBuf.retain(), this.dataBufferFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 301 */     return this.byteBuf.release();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 307 */     return (this == other || (other instanceof NettyDataBuffer && this.byteBuf
/* 308 */       .equals(((NettyDataBuffer)other).byteBuf)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 313 */     return this.byteBuf.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 318 */     return this.byteBuf.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/buffer/NettyDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */