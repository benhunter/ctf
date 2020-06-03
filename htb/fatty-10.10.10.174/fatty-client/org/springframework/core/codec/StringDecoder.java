/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.io.buffer.DefaultDataBufferFactory;
/*     */ import org.springframework.core.io.buffer.PooledDataBuffer;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import reactor.core.publisher.Flux;
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
/*     */ public final class StringDecoder
/*     */   extends AbstractDataBufferDecoder<String>
/*     */ {
/*  60 */   private static final DataBuffer END_FRAME = (DataBuffer)(new DefaultDataBufferFactory()).wrap(new byte[0]);
/*     */ 
/*     */   
/*  63 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */ 
/*     */   
/*  66 */   public static final List<String> DEFAULT_DELIMITERS = Arrays.asList(new String[] { "\r\n", "\n" });
/*     */ 
/*     */   
/*     */   private final List<String> delimiters;
/*     */   
/*     */   private final boolean stripDelimiter;
/*     */   
/*  73 */   private final ConcurrentMap<Charset, List<byte[]>> delimitersCache = new ConcurrentHashMap<>();
/*     */ 
/*     */   
/*     */   private StringDecoder(List<String> delimiters, boolean stripDelimiter, MimeType... mimeTypes) {
/*  77 */     super(mimeTypes);
/*  78 */     Assert.notEmpty(delimiters, "'delimiters' must not be empty");
/*  79 */     this.delimiters = new ArrayList<>(delimiters);
/*  80 */     this.stripDelimiter = stripDelimiter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/*  86 */     return (elementType.resolve() == String.class && super.canDecode(elementType, mimeType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<String> decode(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  93 */     List<byte[]> delimiterBytes = getDelimiterBytes(mimeType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     Flux<DataBuffer> inputFlux = Flux.from(input).flatMapIterable(buffer -> splitOnDelimiter(buffer, delimiterBytes)).bufferUntil(buffer -> (buffer == END_FRAME)).map(StringDecoder::joinUntilEndFrame).doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
/*     */     
/* 101 */     return super.decode((Publisher<DataBuffer>)inputFlux, elementType, mimeType, hints);
/*     */   }
/*     */   
/*     */   private List<byte[]> getDelimiterBytes(@Nullable MimeType mimeType) {
/* 105 */     return this.delimitersCache.computeIfAbsent(getCharset(mimeType), charset -> {
/*     */           List<byte[]> list = (List)new ArrayList<>();
/*     */           for (String delimiter : this.delimiters) {
/*     */             byte[] bytes = delimiter.getBytes(charset);
/*     */             list.add(bytes);
/*     */           } 
/*     */           return list;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<DataBuffer> splitOnDelimiter(DataBuffer buffer, List<byte[]> delimiterBytes) {
/* 120 */     List<DataBuffer> frames = new ArrayList<>();
/*     */     try {
/*     */       do {
/* 123 */         int length = Integer.MAX_VALUE;
/* 124 */         byte[] matchingDelimiter = null;
/* 125 */         for (byte[] delimiter : delimiterBytes) {
/* 126 */           int index = indexOf(buffer, delimiter);
/* 127 */           if (index >= 0 && index < length) {
/* 128 */             length = index;
/* 129 */             matchingDelimiter = delimiter;
/*     */           } 
/*     */         } 
/*     */         
/* 133 */         int readPosition = buffer.readPosition();
/* 134 */         if (matchingDelimiter != null) {
/*     */ 
/*     */           
/* 137 */           DataBuffer frame = this.stripDelimiter ? buffer.slice(readPosition, length) : buffer.slice(readPosition, length + matchingDelimiter.length);
/* 138 */           buffer.readPosition(readPosition + length + matchingDelimiter.length);
/* 139 */           frames.add(DataBufferUtils.retain(frame));
/* 140 */           frames.add(END_FRAME);
/*     */         } else {
/*     */           
/* 143 */           DataBuffer frame = buffer.slice(readPosition, buffer.readableByteCount());
/* 144 */           buffer.readPosition(readPosition + buffer.readableByteCount());
/* 145 */           frames.add(DataBufferUtils.retain(frame));
/*     */         }
/*     */       
/* 148 */       } while (buffer.readableByteCount() > 0);
/*     */     }
/* 150 */     catch (Throwable ex) {
/* 151 */       for (DataBuffer frame : frames) {
/* 152 */         DataBufferUtils.release(frame);
/*     */       }
/* 154 */       throw ex;
/*     */     } finally {
/*     */       
/* 157 */       DataBufferUtils.release(buffer);
/*     */     } 
/* 159 */     return frames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(DataBuffer buffer, byte[] delimiter) {
/* 167 */     for (int i = buffer.readPosition(); i < buffer.writePosition(); i++) {
/* 168 */       int bufferPos = i;
/* 169 */       int delimiterPos = 0;
/* 170 */       while (delimiterPos < delimiter.length && 
/* 171 */         buffer.getByte(bufferPos) == delimiter[delimiterPos]) {
/*     */ 
/*     */ 
/*     */         
/* 175 */         bufferPos++;
/* 176 */         boolean endOfBuffer = (bufferPos == buffer.writePosition());
/* 177 */         boolean endOfDelimiter = (delimiterPos == delimiter.length - 1);
/* 178 */         if (endOfBuffer && !endOfDelimiter) {
/* 179 */           return -1;
/*     */         }
/*     */         
/* 182 */         delimiterPos++;
/*     */       } 
/* 184 */       if (delimiterPos == delimiter.length) {
/* 185 */         return i - buffer.readPosition();
/*     */       }
/*     */     } 
/* 188 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DataBuffer joinUntilEndFrame(List<DataBuffer> dataBuffers) {
/* 195 */     if (!dataBuffers.isEmpty()) {
/* 196 */       int lastIdx = dataBuffers.size() - 1;
/* 197 */       if (dataBuffers.get(lastIdx) == END_FRAME) {
/* 198 */         dataBuffers.remove(lastIdx);
/*     */       }
/*     */     } 
/* 201 */     return ((DataBuffer)dataBuffers.get(0)).factory().join(dataBuffers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String decodeDataBuffer(DataBuffer dataBuffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 208 */     Charset charset = getCharset(mimeType);
/* 209 */     CharBuffer charBuffer = charset.decode(dataBuffer.asByteBuffer());
/* 210 */     DataBufferUtils.release(dataBuffer);
/* 211 */     String value = charBuffer.toString();
/* 212 */     LogFormatUtils.traceDebug(this.logger, traceOn -> {
/*     */           String formatted = LogFormatUtils.formatValue(value, !traceOn.booleanValue());
/*     */           return Hints.getLogPrefix(hints) + "Decoded " + formatted;
/*     */         });
/* 216 */     return value;
/*     */   }
/*     */   
/*     */   private static Charset getCharset(@Nullable MimeType mimeType) {
/* 220 */     if (mimeType != null && mimeType.getCharset() != null) {
/* 221 */       return mimeType.getCharset();
/*     */     }
/*     */     
/* 224 */     return DEFAULT_CHARSET;
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
/*     */   @Deprecated
/*     */   public static StringDecoder textPlainOnly(boolean ignored) {
/* 237 */     return textPlainOnly();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringDecoder textPlainOnly() {
/* 244 */     return textPlainOnly(DEFAULT_DELIMITERS, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringDecoder textPlainOnly(List<String> delimiters, boolean stripDelimiter) {
/* 254 */     return new StringDecoder(delimiters, stripDelimiter, new MimeType[] { new MimeType("text", "plain", DEFAULT_CHARSET) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static StringDecoder allMimeTypes(boolean ignored) {
/* 265 */     return allMimeTypes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringDecoder allMimeTypes() {
/* 272 */     return allMimeTypes(DEFAULT_DELIMITERS, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringDecoder allMimeTypes(List<String> delimiters, boolean stripDelimiter) {
/* 282 */     return new StringDecoder(delimiters, stripDelimiter, new MimeType[] { new MimeType("text", "plain", DEFAULT_CHARSET), MimeTypeUtils.ALL });
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/codec/StringDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */