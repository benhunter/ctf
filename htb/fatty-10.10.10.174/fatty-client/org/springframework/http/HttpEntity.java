/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.util.Map;
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
/*     */ public class HttpEntity<T>
/*     */ {
/*  63 */   public static final HttpEntity<?> EMPTY = new HttpEntity();
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpHeaders headers;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final T body;
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpEntity() {
/*  76 */     this(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntity(T body) {
/*  84 */     this(body, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntity(MultiValueMap<String, String> headers) {
/*  92 */     this(null, headers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers) {
/* 101 */     this.body = body;
/* 102 */     HttpHeaders tempHeaders = new HttpHeaders();
/* 103 */     if (headers != null) {
/* 104 */       tempHeaders.putAll((Map)headers);
/*     */     }
/* 106 */     this.headers = HttpHeaders.readOnlyHttpHeaders(tempHeaders);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/* 114 */     return this.headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T getBody() {
/* 122 */     return this.body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasBody() {
/* 129 */     return (this.body != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 135 */     if (this == other) {
/* 136 */       return true;
/*     */     }
/* 138 */     if (other == null || other.getClass() != getClass()) {
/* 139 */       return false;
/*     */     }
/* 141 */     HttpEntity<?> otherEntity = (HttpEntity)other;
/* 142 */     return (ObjectUtils.nullSafeEquals(this.headers, otherEntity.headers) && 
/* 143 */       ObjectUtils.nullSafeEquals(this.body, otherEntity.body));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 148 */     return ObjectUtils.nullSafeHashCode(this.headers) * 29 + ObjectUtils.nullSafeHashCode(this.body);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 153 */     StringBuilder builder = new StringBuilder("<");
/* 154 */     if (this.body != null) {
/* 155 */       builder.append(this.body);
/* 156 */       builder.append(',');
/*     */     } 
/* 158 */     builder.append(this.headers);
/* 159 */     builder.append('>');
/* 160 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/HttpEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */