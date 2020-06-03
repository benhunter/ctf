/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.time.Instant;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseEntity<T>
/*     */   extends HttpEntity<T>
/*     */ {
/*     */   private final Object status;
/*     */   
/*     */   public ResponseEntity(HttpStatus status) {
/*  83 */     this((T)null, (MultiValueMap<String, String>)null, status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseEntity(@Nullable T body, HttpStatus status) {
/*  92 */     this(body, (MultiValueMap<String, String>)null, status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
/* 101 */     this((T)null, headers, status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
/* 111 */     super(body, headers);
/* 112 */     Assert.notNull(status, "HttpStatus must not be null");
/* 113 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, Object status) {
/* 124 */     super(body, headers);
/* 125 */     Assert.notNull(status, "HttpStatus must not be null");
/* 126 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpStatus getStatusCode() {
/* 135 */     if (this.status instanceof HttpStatus) {
/* 136 */       return (HttpStatus)this.status;
/*     */     }
/*     */     
/* 139 */     return HttpStatus.valueOf(((Integer)this.status).intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStatusCodeValue() {
/* 149 */     if (this.status instanceof HttpStatus) {
/* 150 */       return ((HttpStatus)this.status).value();
/*     */     }
/*     */     
/* 153 */     return ((Integer)this.status).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 160 */     if (this == other) {
/* 161 */       return true;
/*     */     }
/* 163 */     if (!super.equals(other)) {
/* 164 */       return false;
/*     */     }
/* 166 */     ResponseEntity<?> otherEntity = (ResponseEntity)other;
/* 167 */     return ObjectUtils.nullSafeEquals(this.status, otherEntity.status);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 172 */     return 29 * super.hashCode() + ObjectUtils.nullSafeHashCode(this.status);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 177 */     StringBuilder builder = new StringBuilder("<");
/* 178 */     builder.append(this.status.toString());
/* 179 */     if (this.status instanceof HttpStatus) {
/* 180 */       builder.append(' ');
/* 181 */       builder.append(((HttpStatus)this.status).getReasonPhrase());
/*     */     } 
/* 183 */     builder.append(',');
/* 184 */     T body = getBody();
/* 185 */     HttpHeaders headers = getHeaders();
/* 186 */     if (body != null) {
/* 187 */       builder.append(body);
/* 188 */       builder.append(',');
/*     */     } 
/* 190 */     builder.append(headers);
/* 191 */     builder.append('>');
/* 192 */     return builder.toString();
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
/*     */   public static BodyBuilder status(HttpStatus status) {
/* 205 */     Assert.notNull(status, "HttpStatus must not be null");
/* 206 */     return new DefaultBuilder(status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder status(int status) {
/* 216 */     return new DefaultBuilder(Integer.valueOf(status));
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
/*     */   public static <T> ResponseEntity<T> of(Optional<T> body) {
/* 228 */     Assert.notNull(body, "Body must not be null");
/* 229 */     return body.<ResponseEntity<T>>map(ResponseEntity::ok).orElse(notFound().build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder ok() {
/* 238 */     return status(HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> ResponseEntity<T> ok(T body) {
/* 248 */     BodyBuilder builder = ok();
/* 249 */     return builder.body(body);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder created(URI location) {
/* 260 */     BodyBuilder builder = status(HttpStatus.CREATED);
/* 261 */     return builder.location(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder accepted() {
/* 270 */     return status(HttpStatus.ACCEPTED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HeadersBuilder<?> noContent() {
/* 279 */     return status(HttpStatus.NO_CONTENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder badRequest() {
/* 288 */     return status(HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HeadersBuilder<?> notFound() {
/* 297 */     return status(HttpStatus.NOT_FOUND);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder unprocessableEntity() {
/* 307 */     return status(HttpStatus.UNPROCESSABLE_ENTITY);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DefaultBuilder
/*     */     implements BodyBuilder
/*     */   {
/*     */     private final Object statusCode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 462 */     private final HttpHeaders headers = new HttpHeaders();
/*     */     
/*     */     public DefaultBuilder(Object statusCode) {
/* 465 */       this.statusCode = statusCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder header(String headerName, String... headerValues) {
/* 470 */       for (String headerValue : headerValues) {
/* 471 */         this.headers.add(headerName, headerValue);
/*     */       }
/* 473 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder headers(@Nullable HttpHeaders headers) {
/* 478 */       if (headers != null) {
/* 479 */         this.headers.putAll((Map<? extends String, ? extends List<String>>)headers);
/*     */       }
/* 481 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder allow(HttpMethod... allowedMethods) {
/* 486 */       this.headers.setAllow(new LinkedHashSet<>(Arrays.asList(allowedMethods)));
/* 487 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder contentLength(long contentLength) {
/* 492 */       this.headers.setContentLength(contentLength);
/* 493 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder contentType(MediaType contentType) {
/* 498 */       this.headers.setContentType(contentType);
/* 499 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder eTag(String etag) {
/* 504 */       if (!etag.startsWith("\"") && !etag.startsWith("W/\"")) {
/* 505 */         etag = "\"" + etag;
/*     */       }
/* 507 */       if (!etag.endsWith("\"")) {
/* 508 */         etag = etag + "\"";
/*     */       }
/* 510 */       this.headers.setETag(etag);
/* 511 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder lastModified(ZonedDateTime date) {
/* 516 */       this.headers.setLastModified(date);
/* 517 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder lastModified(Instant date) {
/* 522 */       this.headers.setLastModified(date);
/* 523 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder lastModified(long date) {
/* 528 */       this.headers.setLastModified(date);
/* 529 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder location(URI location) {
/* 534 */       this.headers.setLocation(location);
/* 535 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder cacheControl(CacheControl cacheControl) {
/* 540 */       this.headers.setCacheControl(cacheControl);
/* 541 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity.BodyBuilder varyBy(String... requestHeaders) {
/* 546 */       this.headers.setVary(Arrays.asList(requestHeaders));
/* 547 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> ResponseEntity<T> build() {
/* 552 */       return body(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> ResponseEntity<T> body(@Nullable T body) {
/* 557 */       return new ResponseEntity<>(body, this.headers, this.statusCode);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface BodyBuilder extends HeadersBuilder<BodyBuilder> {
/*     */     BodyBuilder contentLength(long param1Long);
/*     */     
/*     */     BodyBuilder contentType(MediaType param1MediaType);
/*     */     
/*     */     <T> ResponseEntity<T> body(@Nullable T param1T);
/*     */   }
/*     */   
/*     */   public static interface HeadersBuilder<B extends HeadersBuilder<B>> {
/*     */     B header(String param1String, String... param1VarArgs);
/*     */     
/*     */     B headers(@Nullable HttpHeaders param1HttpHeaders);
/*     */     
/*     */     B allow(HttpMethod... param1VarArgs);
/*     */     
/*     */     B eTag(String param1String);
/*     */     
/*     */     B lastModified(ZonedDateTime param1ZonedDateTime);
/*     */     
/*     */     B lastModified(Instant param1Instant);
/*     */     
/*     */     B lastModified(long param1Long);
/*     */     
/*     */     B location(URI param1URI);
/*     */     
/*     */     B cacheControl(CacheControl param1CacheControl);
/*     */     
/*     */     B varyBy(String... param1VarArgs);
/*     */     
/*     */     <T> ResponseEntity<T> build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/ResponseEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */