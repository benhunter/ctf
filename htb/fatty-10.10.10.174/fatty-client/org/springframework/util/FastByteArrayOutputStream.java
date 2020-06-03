/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class FastByteArrayOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static final int DEFAULT_BLOCK_SIZE = 256;
/*  53 */   private final LinkedList<byte[]> buffers = (LinkedList)new LinkedList<>();
/*     */ 
/*     */   
/*     */   private final int initialBlockSize;
/*     */ 
/*     */   
/*  59 */   private int nextBlockSize = 0;
/*     */ 
/*     */ 
/*     */   
/*  63 */   private int alreadyBufferedSize = 0;
/*     */ 
/*     */   
/*  66 */   private int index = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean closed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastByteArrayOutputStream() {
/*  77 */     this(256);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastByteArrayOutputStream(int initialBlockSize) {
/*  86 */     Assert.isTrue((initialBlockSize > 0), "Initial block size must be greater than 0");
/*  87 */     this.initialBlockSize = initialBlockSize;
/*  88 */     this.nextBlockSize = initialBlockSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int datum) throws IOException {
/*  96 */     if (this.closed) {
/*  97 */       throw new IOException("Stream closed");
/*     */     }
/*     */     
/* 100 */     if (this.buffers.peekLast() == null || ((byte[])this.buffers.getLast()).length == this.index) {
/* 101 */       addBuffer(1);
/*     */     }
/*     */     
/* 104 */     ((byte[])this.buffers.getLast())[this.index++] = (byte)datum;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] data, int offset, int length) throws IOException {
/* 110 */     if (offset < 0 || offset + length > data.length || length < 0) {
/* 111 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 113 */     if (this.closed) {
/* 114 */       throw new IOException("Stream closed");
/*     */     }
/*     */     
/* 117 */     if (this.buffers.peekLast() == null || ((byte[])this.buffers.getLast()).length == this.index) {
/* 118 */       addBuffer(length);
/*     */     }
/* 120 */     if (this.index + length > ((byte[])this.buffers.getLast()).length) {
/* 121 */       int pos = offset;
/*     */       do {
/* 123 */         if (this.index == ((byte[])this.buffers.getLast()).length) {
/* 124 */           addBuffer(length);
/*     */         }
/* 126 */         int copyLength = ((byte[])this.buffers.getLast()).length - this.index;
/* 127 */         if (length < copyLength) {
/* 128 */           copyLength = length;
/*     */         }
/* 130 */         System.arraycopy(data, pos, this.buffers.getLast(), this.index, copyLength);
/* 131 */         pos += copyLength;
/* 132 */         this.index += copyLength;
/* 133 */         length -= copyLength;
/*     */       }
/* 135 */       while (length > 0);
/*     */     }
/*     */     else {
/*     */       
/* 139 */       System.arraycopy(data, offset, this.buffers.getLast(), this.index, length);
/* 140 */       this.index += length;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 147 */     this.closed = true;
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
/*     */   public String toString() {
/* 164 */     return new String(toByteArrayUnsafe());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 174 */     return this.alreadyBufferedSize + this.index;
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
/*     */   public byte[] toByteArrayUnsafe() {
/* 190 */     int totalSize = size();
/* 191 */     if (totalSize == 0) {
/* 192 */       return new byte[0];
/*     */     }
/* 194 */     resize(totalSize);
/* 195 */     return this.buffers.getFirst();
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
/*     */   public byte[] toByteArray() {
/* 208 */     byte[] bytesUnsafe = toByteArrayUnsafe();
/* 209 */     byte[] ret = new byte[bytesUnsafe.length];
/* 210 */     System.arraycopy(bytesUnsafe, 0, ret, 0, bytesUnsafe.length);
/* 211 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 220 */     this.buffers.clear();
/* 221 */     this.nextBlockSize = this.initialBlockSize;
/* 222 */     this.closed = false;
/* 223 */     this.index = 0;
/* 224 */     this.alreadyBufferedSize = 0;
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
/*     */   public InputStream getInputStream() {
/* 236 */     return new FastByteArrayInputStream(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 244 */     Iterator<byte[]> it = (Iterator)this.buffers.iterator();
/* 245 */     while (it.hasNext()) {
/* 246 */       byte[] bytes = it.next();
/* 247 */       if (it.hasNext()) {
/* 248 */         out.write(bytes, 0, bytes.length);
/*     */         continue;
/*     */       } 
/* 251 */       out.write(bytes, 0, this.index);
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
/*     */   public void resize(int targetCapacity) {
/* 264 */     Assert.isTrue((targetCapacity >= size()), "New capacity must not be smaller than current size");
/* 265 */     if (this.buffers.peekFirst() == null) {
/* 266 */       this.nextBlockSize = targetCapacity - size();
/*     */     }
/* 268 */     else if (size() != targetCapacity || ((byte[])this.buffers.getFirst()).length != targetCapacity) {
/*     */ 
/*     */ 
/*     */       
/* 272 */       int totalSize = size();
/* 273 */       byte[] data = new byte[targetCapacity];
/* 274 */       int pos = 0;
/* 275 */       Iterator<byte[]> it = (Iterator)this.buffers.iterator();
/* 276 */       while (it.hasNext()) {
/* 277 */         byte[] bytes = it.next();
/* 278 */         if (it.hasNext()) {
/* 279 */           System.arraycopy(bytes, 0, data, pos, bytes.length);
/* 280 */           pos += bytes.length;
/*     */           continue;
/*     */         } 
/* 283 */         System.arraycopy(bytes, 0, data, pos, this.index);
/*     */       } 
/*     */       
/* 286 */       this.buffers.clear();
/* 287 */       this.buffers.add(data);
/* 288 */       this.index = totalSize;
/* 289 */       this.alreadyBufferedSize = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addBuffer(int minCapacity) {
/* 298 */     if (this.buffers.peekLast() != null) {
/* 299 */       this.alreadyBufferedSize += this.index;
/* 300 */       this.index = 0;
/*     */     } 
/* 302 */     if (this.nextBlockSize < minCapacity) {
/* 303 */       this.nextBlockSize = nextPowerOf2(minCapacity);
/*     */     }
/* 305 */     this.buffers.add(new byte[this.nextBlockSize]);
/* 306 */     this.nextBlockSize *= 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int nextPowerOf2(int val) {
/* 313 */     val--;
/* 314 */     val = val >> 1 | val;
/* 315 */     val = val >> 2 | val;
/* 316 */     val = val >> 4 | val;
/* 317 */     val = val >> 8 | val;
/* 318 */     val = val >> 16 | val;
/* 319 */     val++;
/* 320 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class FastByteArrayInputStream
/*     */     extends UpdateMessageDigestInputStream
/*     */   {
/*     */     private final FastByteArrayOutputStream fastByteArrayOutputStream;
/*     */ 
/*     */     
/*     */     private final Iterator<byte[]> buffersIterator;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private byte[] currentBuffer;
/*     */     
/* 337 */     private int currentBufferLength = 0;
/*     */     
/* 339 */     private int nextIndexInCurrentBuffer = 0;
/*     */     
/* 341 */     private int totalBytesRead = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FastByteArrayInputStream(FastByteArrayOutputStream fastByteArrayOutputStream) {
/* 348 */       this.fastByteArrayOutputStream = fastByteArrayOutputStream;
/* 349 */       this.buffersIterator = (Iterator)fastByteArrayOutputStream.buffers.iterator();
/* 350 */       if (this.buffersIterator.hasNext()) {
/* 351 */         this.currentBuffer = this.buffersIterator.next();
/* 352 */         if (this.currentBuffer == fastByteArrayOutputStream.buffers.getLast()) {
/* 353 */           this.currentBufferLength = fastByteArrayOutputStream.index;
/*     */         } else {
/*     */           
/* 356 */           this.currentBufferLength = (this.currentBuffer != null) ? this.currentBuffer.length : 0;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() {
/* 363 */       if (this.currentBuffer == null)
/*     */       {
/* 365 */         return -1;
/*     */       }
/*     */       
/* 368 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 369 */         this.totalBytesRead++;
/* 370 */         return this.currentBuffer[this.nextIndexInCurrentBuffer++] & 0xFF;
/*     */       } 
/*     */       
/* 373 */       if (this.buffersIterator.hasNext()) {
/* 374 */         this.currentBuffer = this.buffersIterator.next();
/* 375 */         updateCurrentBufferLength();
/* 376 */         this.nextIndexInCurrentBuffer = 0;
/*     */       } else {
/*     */         
/* 379 */         this.currentBuffer = null;
/*     */       } 
/* 381 */       return read();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int read(byte[] b) {
/* 388 */       return read(b, 0, b.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) {
/* 393 */       if (off < 0 || len < 0 || len > b.length - off) {
/* 394 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 396 */       if (len == 0) {
/* 397 */         return 0;
/*     */       }
/*     */       
/* 400 */       if (this.currentBuffer == null)
/*     */       {
/* 402 */         return -1;
/*     */       }
/*     */       
/* 405 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 406 */         int bytesToCopy = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
/* 407 */         System.arraycopy(this.currentBuffer, this.nextIndexInCurrentBuffer, b, off, bytesToCopy);
/* 408 */         this.totalBytesRead += bytesToCopy;
/* 409 */         this.nextIndexInCurrentBuffer += bytesToCopy;
/* 410 */         int remaining = read(b, off + bytesToCopy, len - bytesToCopy);
/* 411 */         return bytesToCopy + Math.max(remaining, 0);
/*     */       } 
/*     */       
/* 414 */       if (this.buffersIterator.hasNext()) {
/* 415 */         this.currentBuffer = this.buffersIterator.next();
/* 416 */         updateCurrentBufferLength();
/* 417 */         this.nextIndexInCurrentBuffer = 0;
/*     */       } else {
/*     */         
/* 420 */         this.currentBuffer = null;
/*     */       } 
/* 422 */       return read(b, off, len);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long skip(long n) throws IOException {
/* 430 */       if (n > 2147483647L) {
/* 431 */         throw new IllegalArgumentException("n exceeds maximum (2147483647): " + n);
/*     */       }
/* 433 */       if (n == 0L) {
/* 434 */         return 0L;
/*     */       }
/* 436 */       if (n < 0L) {
/* 437 */         throw new IllegalArgumentException("n must be 0 or greater: " + n);
/*     */       }
/* 439 */       int len = (int)n;
/* 440 */       if (this.currentBuffer == null)
/*     */       {
/* 442 */         return 0L;
/*     */       }
/*     */       
/* 445 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 446 */         int bytesToSkip = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
/* 447 */         this.totalBytesRead += bytesToSkip;
/* 448 */         this.nextIndexInCurrentBuffer += bytesToSkip;
/* 449 */         return bytesToSkip + skip((len - bytesToSkip));
/*     */       } 
/*     */       
/* 452 */       if (this.buffersIterator.hasNext()) {
/* 453 */         this.currentBuffer = this.buffersIterator.next();
/* 454 */         updateCurrentBufferLength();
/* 455 */         this.nextIndexInCurrentBuffer = 0;
/*     */       } else {
/*     */         
/* 458 */         this.currentBuffer = null;
/*     */       } 
/* 460 */       return skip(len);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int available() {
/* 467 */       return this.fastByteArrayOutputStream.size() - this.totalBytesRead;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateMessageDigest(MessageDigest messageDigest) {
/* 476 */       updateMessageDigest(messageDigest, available());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateMessageDigest(MessageDigest messageDigest, int len) {
/* 487 */       if (this.currentBuffer == null) {
/*     */         return;
/*     */       }
/*     */       
/* 491 */       if (len == 0) {
/*     */         return;
/*     */       }
/* 494 */       if (len < 0) {
/* 495 */         throw new IllegalArgumentException("len must be 0 or greater: " + len);
/*     */       }
/*     */       
/* 498 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 499 */         int bytesToCopy = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
/* 500 */         messageDigest.update(this.currentBuffer, this.nextIndexInCurrentBuffer, bytesToCopy);
/* 501 */         this.nextIndexInCurrentBuffer += bytesToCopy;
/* 502 */         updateMessageDigest(messageDigest, len - bytesToCopy);
/*     */       } else {
/*     */         
/* 505 */         if (this.buffersIterator.hasNext()) {
/* 506 */           this.currentBuffer = this.buffersIterator.next();
/* 507 */           updateCurrentBufferLength();
/* 508 */           this.nextIndexInCurrentBuffer = 0;
/*     */         } else {
/*     */           
/* 511 */           this.currentBuffer = null;
/*     */         } 
/* 513 */         updateMessageDigest(messageDigest, len);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void updateCurrentBufferLength() {
/* 519 */       if (this.currentBuffer == this.fastByteArrayOutputStream.buffers.getLast()) {
/* 520 */         this.currentBufferLength = this.fastByteArrayOutputStream.index;
/*     */       } else {
/*     */         
/* 523 */         this.currentBufferLength = (this.currentBuffer != null) ? this.currentBuffer.length : 0;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/FastByteArrayOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */