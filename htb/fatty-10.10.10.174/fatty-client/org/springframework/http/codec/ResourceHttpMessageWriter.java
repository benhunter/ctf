/*     */ package org.springframework.http.codec;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.codec.ResourceEncoder;
/*     */ import org.springframework.core.codec.ResourceRegionEncoder;
/*     */ import org.springframework.core.io.InputStreamResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.support.ResourceRegion;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpLogging;
/*     */ import org.springframework.http.HttpRange;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.MediaTypeFactory;
/*     */ import org.springframework.http.ReactiveHttpOutputMessage;
/*     */ import org.springframework.http.ZeroCopyHttpOutputMessage;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
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
/*     */ public class ResourceHttpMessageWriter
/*     */   implements HttpMessageWriter<Resource>
/*     */ {
/*  72 */   private static final ResolvableType REGION_TYPE = ResolvableType.forClass(ResourceRegion.class);
/*     */   
/*  74 */   private static final Log logger = HttpLogging.forLogName(ResourceHttpMessageWriter.class);
/*     */ 
/*     */   
/*     */   private final ResourceEncoder encoder;
/*     */   
/*     */   private final ResourceRegionEncoder regionEncoder;
/*     */   
/*     */   private final List<MediaType> mediaTypes;
/*     */ 
/*     */   
/*     */   public ResourceHttpMessageWriter() {
/*  85 */     this(4096);
/*     */   }
/*     */   
/*     */   public ResourceHttpMessageWriter(int bufferSize) {
/*  89 */     this.encoder = new ResourceEncoder(bufferSize);
/*  90 */     this.regionEncoder = new ResourceRegionEncoder(bufferSize);
/*  91 */     this.mediaTypes = MediaType.asMediaTypes(this.encoder.getEncodableMimeTypes());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
/*  97 */     return this.encoder.canEncode(elementType, (MimeType)mediaType);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getWritableMediaTypes() {
/* 102 */     return this.mediaTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> write(Publisher<? extends Resource> inputStream, ResolvableType elementType, @Nullable MediaType mediaType, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
/* 112 */     return Mono.from(inputStream).flatMap(resource -> writeResource(resource, elementType, mediaType, message, hints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Mono<Void> writeResource(Resource resource, ResolvableType type, @Nullable MediaType mediaType, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
/* 119 */     HttpHeaders headers = message.getHeaders();
/* 120 */     MediaType resourceMediaType = getResourceMediaType(mediaType, resource, hints);
/* 121 */     headers.setContentType(resourceMediaType);
/*     */     
/* 123 */     if (headers.getContentLength() < 0L) {
/* 124 */       long length = lengthOf(resource);
/* 125 */       if (length != -1L) {
/* 126 */         headers.setContentLength(length);
/*     */       }
/*     */     } 
/*     */     
/* 130 */     return zeroCopy(resource, null, message, hints)
/* 131 */       .orElseGet(() -> {
/*     */           Mono<Resource> input = Mono.just(resource);
/*     */           DataBufferFactory factory = message.bufferFactory();
/*     */           Flux<DataBuffer> body = this.encoder.encode((Publisher)input, factory, type, (MimeType)resourceMediaType, hints);
/*     */           return message.writeWith((Publisher)body);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static MediaType getResourceMediaType(@Nullable MediaType mediaType, Resource resource, Map<String, Object> hints) {
/* 142 */     if (mediaType != null && mediaType.isConcrete() && !mediaType.equals(MediaType.APPLICATION_OCTET_STREAM)) {
/* 143 */       return mediaType;
/*     */     }
/* 145 */     mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
/* 146 */     if (logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
/* 147 */       logger.debug(Hints.getLogPrefix(hints) + "Resource associated with '" + mediaType + "'");
/*     */     }
/* 149 */     return mediaType;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long lengthOf(Resource resource) {
/* 154 */     if (InputStreamResource.class != resource.getClass()) {
/*     */       try {
/* 156 */         return resource.contentLength();
/*     */       }
/* 158 */       catch (IOException iOException) {}
/*     */     }
/*     */     
/* 161 */     return -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Optional<Mono<Void>> zeroCopy(Resource resource, @Nullable ResourceRegion region, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
/* 167 */     if (message instanceof ZeroCopyHttpOutputMessage && resource.isFile()) {
/*     */       try {
/* 169 */         File file = resource.getFile();
/* 170 */         long pos = (region != null) ? region.getPosition() : 0L;
/* 171 */         long count = (region != null) ? region.getCount() : file.length();
/* 172 */         if (logger.isDebugEnabled()) {
/* 173 */           String formatted = (region != null) ? ("region " + pos + "-" + count + " of ") : "";
/* 174 */           logger.debug(Hints.getLogPrefix(hints) + "Zero-copy " + formatted + "[" + resource + "]");
/*     */         } 
/* 176 */         return Optional.of(((ZeroCopyHttpOutputMessage)message).writeWith(file, pos, count));
/*     */       }
/* 178 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/* 182 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> write(Publisher<? extends Resource> inputStream, @Nullable ResolvableType actualType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
/*     */     List<HttpRange> ranges;
/* 193 */     HttpHeaders headers = response.getHeaders();
/* 194 */     headers.set("Accept-Ranges", "bytes");
/*     */ 
/*     */     
/*     */     try {
/* 198 */       ranges = request.getHeaders().getRange();
/*     */     }
/* 200 */     catch (IllegalArgumentException ex) {
/* 201 */       response.setStatusCode(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
/* 202 */       return response.setComplete();
/*     */     } 
/*     */     
/* 205 */     return Mono.from(inputStream).flatMap(resource -> {
/*     */           if (ranges.isEmpty()) {
/*     */             return writeResource(resource, elementType, mediaType, (ReactiveHttpOutputMessage)response, hints);
/*     */           }
/*     */           response.setStatusCode(HttpStatus.PARTIAL_CONTENT);
/*     */           List<ResourceRegion> regions = HttpRange.toResourceRegions(ranges, resource);
/*     */           MediaType resourceMediaType = getResourceMediaType(mediaType, resource, hints);
/*     */           if (regions.size() == 1) {
/*     */             ResourceRegion region = regions.get(0);
/*     */             headers.setContentType(resourceMediaType);
/*     */             long contentLength = lengthOf(resource);
/*     */             if (contentLength != -1L) {
/*     */               long start = region.getPosition();
/*     */               long end = start + region.getCount() - 1L;
/*     */               end = Math.min(end, contentLength - 1L);
/*     */               headers.add("Content-Range", "bytes " + start + '-' + end + '/' + contentLength);
/*     */               headers.setContentLength(end - start + 1L);
/*     */             } 
/*     */             return writeSingleRegion(region, (ReactiveHttpOutputMessage)response, hints);
/*     */           } 
/*     */           String boundary = MimeTypeUtils.generateMultipartBoundaryString();
/*     */           MediaType multipartType = MediaType.parseMediaType("multipart/byteranges;boundary=" + boundary);
/*     */           headers.setContentType(multipartType);
/*     */           Map<String, Object> allHints = Hints.merge(hints, ResourceRegionEncoder.BOUNDARY_STRING_HINT, boundary);
/*     */           return encodeAndWriteRegions((Publisher<? extends ResourceRegion>)Flux.fromIterable(regions), resourceMediaType, (ReactiveHttpOutputMessage)response, allHints);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Mono<Void> writeSingleRegion(ResourceRegion region, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
/* 238 */     return zeroCopy(region.getResource(), region, message, hints)
/* 239 */       .orElseGet(() -> {
/*     */           Mono mono = Mono.just(region);
/*     */           MediaType mediaType = message.getHeaders().getContentType();
/*     */           return encodeAndWriteRegions((Publisher<? extends ResourceRegion>)mono, mediaType, message, hints);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Mono<Void> encodeAndWriteRegions(Publisher<? extends ResourceRegion> publisher, @Nullable MediaType mediaType, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
/* 249 */     Flux<DataBuffer> body = this.regionEncoder.encode(publisher, message
/* 250 */         .bufferFactory(), REGION_TYPE, (MimeType)mediaType, hints);
/*     */     
/* 252 */     return message.writeWith((Publisher)body);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/ResourceHttpMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */