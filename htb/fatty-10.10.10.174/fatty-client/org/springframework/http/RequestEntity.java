/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.URI;
/*     */ import java.nio.charset.Charset;
/*     */ import java.time.Instant;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ public class RequestEntity<T>
/*     */   extends HttpEntity<T>
/*     */ {
/*     */   @Nullable
/*     */   private final HttpMethod method;
/*     */   private final URI url;
/*     */   @Nullable
/*     */   private final Type type;
/*     */   
/*     */   public RequestEntity(HttpMethod method, URI url) {
/*  86 */     this((T)null, (MultiValueMap<String, String>)null, method, url, (Type)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestEntity(@Nullable T body, HttpMethod method, URI url) {
/*  96 */     this(body, (MultiValueMap<String, String>)null, method, url, (Type)null);
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
/*     */   public RequestEntity(@Nullable T body, HttpMethod method, URI url, Type type) {
/* 108 */     this(body, (MultiValueMap<String, String>)null, method, url, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestEntity(MultiValueMap<String, String> headers, HttpMethod method, URI url) {
/* 118 */     this((T)null, headers, method, url, (Type)null);
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
/*     */   public RequestEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, @Nullable HttpMethod method, URI url) {
/* 131 */     this(body, headers, method, url, (Type)null);
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
/*     */   public RequestEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, @Nullable HttpMethod method, URI url, @Nullable Type type) {
/* 146 */     super(body, headers);
/* 147 */     this.method = method;
/* 148 */     this.url = url;
/* 149 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HttpMethod getMethod() {
/* 159 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getUrl() {
/* 167 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Type getType() {
/* 177 */     if (this.type == null) {
/* 178 */       T body = getBody();
/* 179 */       if (body != null) {
/* 180 */         return body.getClass();
/*     */       }
/*     */     } 
/* 183 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 189 */     if (this == other) {
/* 190 */       return true;
/*     */     }
/* 192 */     if (!super.equals(other)) {
/* 193 */       return false;
/*     */     }
/* 195 */     RequestEntity<?> otherEntity = (RequestEntity)other;
/* 196 */     return (ObjectUtils.nullSafeEquals(getMethod(), otherEntity.getMethod()) && 
/* 197 */       ObjectUtils.nullSafeEquals(getUrl(), otherEntity.getUrl()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 202 */     int hashCode = super.hashCode();
/* 203 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.method);
/* 204 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.url);
/* 205 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 210 */     StringBuilder builder = new StringBuilder("<");
/* 211 */     builder.append(getMethod());
/* 212 */     builder.append(' ');
/* 213 */     builder.append(getUrl());
/* 214 */     builder.append(',');
/* 215 */     T body = getBody();
/* 216 */     HttpHeaders headers = getHeaders();
/* 217 */     if (body != null) {
/* 218 */       builder.append(body);
/* 219 */       builder.append(',');
/*     */     } 
/* 221 */     builder.append(headers);
/* 222 */     builder.append('>');
/* 223 */     return builder.toString();
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
/*     */   public static BodyBuilder method(HttpMethod method, URI url) {
/* 236 */     return new DefaultBodyBuilder(method, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HeadersBuilder<?> get(URI url) {
/* 245 */     return method(HttpMethod.GET, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HeadersBuilder<?> head(URI url) {
/* 254 */     return method(HttpMethod.HEAD, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder post(URI url) {
/* 263 */     return method(HttpMethod.POST, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder put(URI url) {
/* 272 */     return method(HttpMethod.PUT, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder patch(URI url) {
/* 281 */     return method(HttpMethod.PATCH, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HeadersBuilder<?> delete(URI url) {
/* 290 */     return method(HttpMethod.DELETE, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HeadersBuilder<?> options(URI url) {
/* 299 */     return method(HttpMethod.OPTIONS, url);
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
/*     */   private static class DefaultBodyBuilder
/*     */     implements BodyBuilder
/*     */   {
/*     */     private final HttpMethod method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final URI url;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 418 */     private final HttpHeaders headers = new HttpHeaders();
/*     */     
/*     */     public DefaultBodyBuilder(HttpMethod method, URI url) {
/* 421 */       this.method = method;
/* 422 */       this.url = url;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder header(String headerName, String... headerValues) {
/* 427 */       for (String headerValue : headerValues) {
/* 428 */         this.headers.add(headerName, headerValue);
/*     */       }
/* 430 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder accept(MediaType... acceptableMediaTypes) {
/* 435 */       this.headers.setAccept(Arrays.asList(acceptableMediaTypes));
/* 436 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder acceptCharset(Charset... acceptableCharsets) {
/* 441 */       this.headers.setAcceptCharset(Arrays.asList(acceptableCharsets));
/* 442 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder contentLength(long contentLength) {
/* 447 */       this.headers.setContentLength(contentLength);
/* 448 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder contentType(MediaType contentType) {
/* 453 */       this.headers.setContentType(contentType);
/* 454 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder ifModifiedSince(ZonedDateTime ifModifiedSince) {
/* 459 */       this.headers.setIfModifiedSince(ifModifiedSince);
/* 460 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder ifModifiedSince(Instant ifModifiedSince) {
/* 465 */       this.headers.setIfModifiedSince(ifModifiedSince);
/* 466 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder ifModifiedSince(long ifModifiedSince) {
/* 471 */       this.headers.setIfModifiedSince(ifModifiedSince);
/* 472 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder ifNoneMatch(String... ifNoneMatches) {
/* 477 */       this.headers.setIfNoneMatch(Arrays.asList(ifNoneMatches));
/* 478 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity<Void> build() {
/* 483 */       return new RequestEntity<>(this.headers, this.method, this.url);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> RequestEntity<T> body(T body) {
/* 488 */       return new RequestEntity<>(body, this.headers, this.method, this.url);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> RequestEntity<T> body(T body, Type type) {
/* 493 */       return new RequestEntity<>(body, this.headers, this.method, this.url, type);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface BodyBuilder extends HeadersBuilder<BodyBuilder> {
/*     */     BodyBuilder contentLength(long param1Long);
/*     */     
/*     */     BodyBuilder contentType(MediaType param1MediaType);
/*     */     
/*     */     <T> RequestEntity<T> body(T param1T);
/*     */     
/*     */     <T> RequestEntity<T> body(T param1T, Type param1Type);
/*     */   }
/*     */   
/*     */   public static interface HeadersBuilder<B extends HeadersBuilder<B>> {
/*     */     B header(String param1String, String... param1VarArgs);
/*     */     
/*     */     B accept(MediaType... param1VarArgs);
/*     */     
/*     */     B acceptCharset(Charset... param1VarArgs);
/*     */     
/*     */     B ifModifiedSince(ZonedDateTime param1ZonedDateTime);
/*     */     
/*     */     B ifModifiedSince(Instant param1Instant);
/*     */     
/*     */     B ifModifiedSince(long param1Long);
/*     */     
/*     */     B ifNoneMatch(String... param1VarArgs);
/*     */     
/*     */     RequestEntity<Void> build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/RequestEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */