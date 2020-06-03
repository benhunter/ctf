/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultDataBuffer
/*     */   implements DataBuffer
/*     */ {
/*     */   private static final int MAX_CAPACITY = 2147483647;
/*     */   private static final int CAPACITY_THRESHOLD = 4194304;
/*     */   private final DefaultDataBufferFactory dataBufferFactory;
/*     */   private ByteBuffer byteBuffer;
/*     */   private int capacity;
/*     */   private int readPosition;
/*     */   private int writePosition;
/*     */   
/*     */   private DefaultDataBuffer(DefaultDataBufferFactory dataBufferFactory, ByteBuffer byteBuffer) {
/*  63 */     Assert.notNull(dataBufferFactory, "DefaultDataBufferFactory must not be null");
/*  64 */     Assert.notNull(byteBuffer, "ByteBuffer must not be null");
/*  65 */     this.dataBufferFactory = dataBufferFactory;
/*  66 */     ByteBuffer slice = byteBuffer.slice();
/*  67 */     this.byteBuffer = slice;
/*  68 */     this.capacity = slice.remaining();
/*     */   }
/*     */   
/*     */   static DefaultDataBuffer fromFilledByteBuffer(DefaultDataBufferFactory dataBufferFactory, ByteBuffer byteBuffer) {
/*  72 */     DefaultDataBuffer dataBuffer = new DefaultDataBuffer(dataBufferFactory, byteBuffer);
/*  73 */     dataBuffer.writePosition(byteBuffer.remaining());
/*  74 */     return dataBuffer;
/*     */   }
/*     */   
/*     */   static DefaultDataBuffer fromEmptyByteBuffer(DefaultDataBufferFactory dataBufferFactory, ByteBuffer byteBuffer) {
/*  78 */     return new DefaultDataBuffer(dataBufferFactory, byteBuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer getNativeBuffer() {
/*  87 */     return this.byteBuffer;
/*     */   }
/*     */   
/*     */   private void setNativeBuffer(ByteBuffer byteBuffer) {
/*  91 */     this.byteBuffer = byteBuffer;
/*  92 */     this.capacity = byteBuffer.remaining();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBufferFactory factory() {
/*  98 */     return this.dataBufferFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(IntPredicate predicate, int fromIndex) {
/* 103 */     Assert.notNull(predicate, "IntPredicate must not be null");
/* 104 */     if (fromIndex < 0) {
/* 105 */       fromIndex = 0;
/*     */     }
/* 107 */     else if (fromIndex >= this.writePosition) {
/* 108 */       return -1;
/*     */     } 
/* 110 */     for (int i = fromIndex; i < this.writePosition; i++) {
/* 111 */       byte b = this.byteBuffer.get(i);
/* 112 */       if (predicate.test(b)) {
/* 113 */         return i;
/*     */       }
/*     */     } 
/* 116 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(IntPredicate predicate, int fromIndex) {
/* 121 */     Assert.notNull(predicate, "IntPredicate must not be null");
/* 122 */     int i = Math.min(fromIndex, this.writePosition - 1);
/* 123 */     for (; i >= 0; i--) {
/* 124 */       byte b = this.byteBuffer.get(i);
/* 125 */       if (predicate.test(b)) {
/* 126 */         return i;
/*     */       }
/*     */     } 
/* 129 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readableByteCount() {
/* 134 */     return this.writePosition - this.readPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public int writableByteCount() {
/* 139 */     return this.capacity - this.writePosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readPosition() {
/* 144 */     return this.readPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer readPosition(int readPosition) {
/* 149 */     assertIndex((readPosition >= 0), "'readPosition' %d must be >= 0", new Object[] { Integer.valueOf(readPosition) });
/* 150 */     assertIndex((readPosition <= this.writePosition), "'readPosition' %d must be <= %d", new Object[] {
/* 151 */           Integer.valueOf(readPosition), Integer.valueOf(this.writePosition) });
/* 152 */     this.readPosition = readPosition;
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int writePosition() {
/* 158 */     return this.writePosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer writePosition(int writePosition) {
/* 163 */     assertIndex((writePosition >= this.readPosition), "'writePosition' %d must be >= %d", new Object[] {
/* 164 */           Integer.valueOf(writePosition), Integer.valueOf(this.readPosition) });
/* 165 */     assertIndex((writePosition <= this.capacity), "'writePosition' %d must be <= %d", new Object[] {
/* 166 */           Integer.valueOf(writePosition), Integer.valueOf(this.capacity) });
/* 167 */     this.writePosition = writePosition;
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 173 */     return this.capacity;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer capacity(int newCapacity) {
/* 178 */     if (newCapacity <= 0) {
/* 179 */       throw new IllegalArgumentException(String.format("'newCapacity' %d must be higher than 0", new Object[] { Integer.valueOf(newCapacity) }));
/*     */     }
/* 181 */     int readPosition = readPosition();
/* 182 */     int writePosition = writePosition();
/* 183 */     int oldCapacity = capacity();
/*     */     
/* 185 */     if (newCapacity > oldCapacity) {
/* 186 */       ByteBuffer oldBuffer = this.byteBuffer;
/* 187 */       ByteBuffer newBuffer = allocate(newCapacity, oldBuffer.isDirect());
/* 188 */       oldBuffer.position(0).limit(oldBuffer.capacity());
/* 189 */       newBuffer.position(0).limit(oldBuffer.capacity());
/* 190 */       newBuffer.put(oldBuffer);
/* 191 */       newBuffer.clear();
/* 192 */       setNativeBuffer(newBuffer);
/*     */     }
/* 194 */     else if (newCapacity < oldCapacity) {
/* 195 */       ByteBuffer oldBuffer = this.byteBuffer;
/* 196 */       ByteBuffer newBuffer = allocate(newCapacity, oldBuffer.isDirect());
/* 197 */       if (readPosition < newCapacity) {
/* 198 */         if (writePosition > newCapacity) {
/* 199 */           writePosition = newCapacity;
/* 200 */           writePosition(writePosition);
/*     */         } 
/* 202 */         oldBuffer.position(readPosition).limit(writePosition);
/* 203 */         newBuffer.position(readPosition).limit(writePosition);
/* 204 */         newBuffer.put(oldBuffer);
/* 205 */         newBuffer.clear();
/*     */       } else {
/*     */         
/* 208 */         readPosition(newCapacity);
/* 209 */         writePosition(newCapacity);
/*     */       } 
/* 211 */       setNativeBuffer(newBuffer);
/*     */     } 
/* 213 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer ensureCapacity(int length) {
/* 218 */     if (length > writableByteCount()) {
/* 219 */       int newCapacity = calculateCapacity(this.writePosition + length);
/* 220 */       capacity(newCapacity);
/*     */     } 
/* 222 */     return this;
/*     */   }
/*     */   
/*     */   private static ByteBuffer allocate(int capacity, boolean direct) {
/* 226 */     return direct ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/* 231 */     assertIndex((index >= 0), "index %d must be >= 0", new Object[] { Integer.valueOf(index) });
/* 232 */     assertIndex((index <= this.writePosition - 1), "index %d must be <= %d", new Object[] { Integer.valueOf(index), Integer.valueOf(this.writePosition - 1) });
/* 233 */     return this.byteBuffer.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte read() {
/* 238 */     assertIndex((this.readPosition <= this.writePosition - 1), "readPosition %d must be <= %d", new Object[] {
/* 239 */           Integer.valueOf(this.readPosition), Integer.valueOf(this.writePosition - 1) });
/* 240 */     int pos = this.readPosition;
/* 241 */     byte b = this.byteBuffer.get(pos);
/* 242 */     this.readPosition = pos + 1;
/* 243 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer read(byte[] destination) {
/* 248 */     Assert.notNull(destination, "Byte array must not be null");
/* 249 */     read(destination, 0, destination.length);
/* 250 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer read(byte[] destination, int offset, int length) {
/* 255 */     Assert.notNull(destination, "Byte array must not be null");
/* 256 */     assertIndex((this.readPosition <= this.writePosition - length), "readPosition %d and length %d should be smaller than writePosition %d", new Object[] {
/*     */           
/* 258 */           Integer.valueOf(this.readPosition), Integer.valueOf(length), Integer.valueOf(this.writePosition)
/*     */         });
/* 260 */     ByteBuffer tmp = this.byteBuffer.duplicate();
/* 261 */     int limit = this.readPosition + length;
/* 262 */     tmp.clear().position(this.readPosition).limit(limit);
/* 263 */     tmp.get(destination, offset, length);
/*     */     
/* 265 */     this.readPosition += length;
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer write(byte b) {
/* 271 */     ensureCapacity(1);
/* 272 */     int pos = this.writePosition;
/* 273 */     this.byteBuffer.put(pos, b);
/* 274 */     this.writePosition = pos + 1;
/* 275 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer write(byte[] source) {
/* 280 */     Assert.notNull(source, "Byte array must not be null");
/* 281 */     write(source, 0, source.length);
/* 282 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer write(byte[] source, int offset, int length) {
/* 287 */     Assert.notNull(source, "Byte array must not be null");
/* 288 */     ensureCapacity(length);
/*     */     
/* 290 */     ByteBuffer tmp = this.byteBuffer.duplicate();
/* 291 */     int limit = this.writePosition + length;
/* 292 */     tmp.clear().position(this.writePosition).limit(limit);
/* 293 */     tmp.put(source, offset, length);
/*     */     
/* 295 */     this.writePosition += length;
/* 296 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer write(DataBuffer... buffers) {
/* 301 */     if (!ObjectUtils.isEmpty((Object[])buffers)) {
/* 302 */       write((ByteBuffer[])Arrays.<DataBuffer>stream(buffers).map(DataBuffer::asByteBuffer).toArray(x$0 -> new ByteBuffer[x$0]));
/*     */     }
/* 304 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer write(ByteBuffer... buffers) {
/* 309 */     if (!ObjectUtils.isEmpty((Object[])buffers)) {
/* 310 */       int capacity = Arrays.<ByteBuffer>stream(buffers).mapToInt(Buffer::remaining).sum();
/* 311 */       ensureCapacity(capacity);
/* 312 */       Arrays.<ByteBuffer>stream(buffers).forEach(this::write);
/*     */     } 
/* 314 */     return this;
/*     */   }
/*     */   
/*     */   private void write(ByteBuffer source) {
/* 318 */     int length = source.remaining();
/* 319 */     ByteBuffer tmp = this.byteBuffer.duplicate();
/* 320 */     int limit = this.writePosition + source.remaining();
/* 321 */     tmp.clear().position(this.writePosition).limit(limit);
/* 322 */     tmp.put(source);
/* 323 */     this.writePosition += length;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer slice(int index, int length) {
/* 328 */     checkIndex(index, length);
/* 329 */     int oldPosition = this.byteBuffer.position();
/*     */ 
/*     */     
/* 332 */     Buffer buffer = this.byteBuffer;
/*     */     try {
/* 334 */       buffer.position(index);
/* 335 */       ByteBuffer slice = this.byteBuffer.slice();
/*     */       
/* 337 */       slice.limit(length);
/* 338 */       return new SlicedDefaultDataBuffer(slice, this.dataBufferFactory, length);
/*     */     } finally {
/*     */       
/* 341 */       buffer.position(oldPosition);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer asByteBuffer() {
/* 347 */     return asByteBuffer(this.readPosition, readableByteCount());
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer asByteBuffer(int index, int length) {
/* 352 */     checkIndex(index, length);
/*     */     
/* 354 */     ByteBuffer duplicate = this.byteBuffer.duplicate();
/*     */ 
/*     */     
/* 357 */     Buffer buffer = duplicate;
/* 358 */     buffer.position(index);
/* 359 */     buffer.limit(index + length);
/* 360 */     return duplicate.slice();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream asInputStream() {
/* 365 */     return new DefaultDataBufferInputStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream asInputStream(boolean releaseOnClose) {
/* 370 */     return new DefaultDataBufferInputStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream asOutputStream() {
/* 375 */     return new DefaultDataBufferOutputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int calculateCapacity(int neededCapacity) {
/* 384 */     Assert.isTrue((neededCapacity >= 0), "'neededCapacity' must >= 0");
/*     */     
/* 386 */     if (neededCapacity == 4194304) {
/* 387 */       return 4194304;
/*     */     }
/* 389 */     if (neededCapacity > 4194304) {
/* 390 */       int i = neededCapacity / 4194304 * 4194304;
/* 391 */       if (i > 2143289343) {
/* 392 */         i = Integer.MAX_VALUE;
/*     */       } else {
/*     */         
/* 395 */         i += 4194304;
/*     */       } 
/* 397 */       return i;
/*     */     } 
/*     */     
/* 400 */     int newCapacity = 64;
/* 401 */     while (newCapacity < neededCapacity) {
/* 402 */       newCapacity <<= 1;
/*     */     }
/* 404 */     return Math.min(newCapacity, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 411 */     if (this == other) {
/* 412 */       return true;
/*     */     }
/* 414 */     if (!(other instanceof DefaultDataBuffer)) {
/* 415 */       return false;
/*     */     }
/* 417 */     DefaultDataBuffer otherBuffer = (DefaultDataBuffer)other;
/* 418 */     return (this.readPosition == otherBuffer.readPosition && this.writePosition == otherBuffer.writePosition && this.byteBuffer
/*     */       
/* 420 */       .equals(otherBuffer.byteBuffer));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 425 */     return this.byteBuffer.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 430 */     return String.format("DefaultDataBuffer (r: %d, w: %d, c: %d)", new Object[] {
/* 431 */           Integer.valueOf(this.readPosition), Integer.valueOf(this.writePosition), Integer.valueOf(this.capacity)
/*     */         });
/*     */   }
/*     */   
/*     */   private void checkIndex(int index, int length) {
/* 436 */     assertIndex((index >= 0), "index %d must be >= 0", new Object[] { Integer.valueOf(index) });
/* 437 */     assertIndex((length >= 0), "length %d must be >= 0", new Object[] { Integer.valueOf(index) });
/* 438 */     assertIndex((index <= this.capacity), "index %d must be <= %d", new Object[] { Integer.valueOf(index), Integer.valueOf(this.capacity) });
/* 439 */     assertIndex((length <= this.capacity), "length %d must be <= %d", new Object[] { Integer.valueOf(index), Integer.valueOf(this.capacity) });
/*     */   }
/*     */   
/*     */   private void assertIndex(boolean expression, String format, Object... args) {
/* 443 */     if (!expression) {
/* 444 */       String message = String.format(format, args);
/* 445 */       throw new IndexOutOfBoundsException(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   private class DefaultDataBufferInputStream
/*     */     extends InputStream {
/*     */     private DefaultDataBufferInputStream() {}
/*     */     
/*     */     public int available() {
/* 454 */       return DefaultDataBuffer.this.readableByteCount();
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() {
/* 459 */       return (available() > 0) ? (DefaultDataBuffer.this.read() & 0xFF) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] bytes, int off, int len) throws IOException {
/* 464 */       int available = available();
/* 465 */       if (available > 0) {
/* 466 */         len = Math.min(len, available);
/* 467 */         DefaultDataBuffer.this.read(bytes, off, len);
/* 468 */         return len;
/*     */       } 
/*     */       
/* 471 */       return -1;
/*     */     }
/*     */   }
/*     */   
/*     */   private class DefaultDataBufferOutputStream
/*     */     extends OutputStream
/*     */   {
/*     */     private DefaultDataBufferOutputStream() {}
/*     */     
/*     */     public void write(int b) throws IOException {
/* 481 */       DefaultDataBuffer.this.write((byte)b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] bytes, int off, int len) throws IOException {
/* 486 */       DefaultDataBuffer.this.write(bytes, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SlicedDefaultDataBuffer
/*     */     extends DefaultDataBuffer
/*     */   {
/*     */     SlicedDefaultDataBuffer(ByteBuffer byteBuffer, DefaultDataBufferFactory dataBufferFactory, int length) {
/* 494 */       super(dataBufferFactory, byteBuffer);
/* 495 */       writePosition(length);
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultDataBuffer capacity(int newCapacity) {
/* 500 */       throw new UnsupportedOperationException("Changing the capacity of a sliced buffer is not supported");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/buffer/DefaultDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */