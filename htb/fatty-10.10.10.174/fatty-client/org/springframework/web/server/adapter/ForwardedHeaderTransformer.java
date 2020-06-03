/*     */ package org.springframework.web.server.adapter;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Collections;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ForwardedHeaderTransformer
/*     */   implements Function<ServerHttpRequest, ServerHttpRequest>
/*     */ {
/*  53 */   static final Set<String> FORWARDED_HEADER_NAMES = Collections.newSetFromMap((Map<String, Boolean>)new LinkedCaseInsensitiveMap(8, Locale.ENGLISH));
/*     */   
/*     */   static {
/*  56 */     FORWARDED_HEADER_NAMES.add("Forwarded");
/*  57 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Host");
/*  58 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Port");
/*  59 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Proto");
/*  60 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Prefix");
/*  61 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Ssl");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean removeOnly;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveOnly(boolean removeOnly) {
/*  74 */     this.removeOnly = removeOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRemoveOnly() {
/*  82 */     return this.removeOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerHttpRequest apply(ServerHttpRequest request) {
/*  92 */     if (hasForwardedHeaders(request)) {
/*  93 */       ServerHttpRequest.Builder builder = request.mutate();
/*  94 */       if (!this.removeOnly) {
/*  95 */         URI uri = UriComponentsBuilder.fromHttpRequest((HttpRequest)request).build(true).toUri();
/*  96 */         builder.uri(uri);
/*  97 */         String prefix = getForwardedPrefix(request);
/*  98 */         if (prefix != null) {
/*  99 */           builder.path(prefix + uri.getRawPath());
/* 100 */           builder.contextPath(prefix);
/*     */         } 
/*     */       } 
/* 103 */       removeForwardedHeaders(builder);
/* 104 */       request = builder.build();
/*     */     } 
/* 106 */     return request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasForwardedHeaders(ServerHttpRequest request) {
/* 114 */     HttpHeaders headers = request.getHeaders();
/* 115 */     for (String headerName : FORWARDED_HEADER_NAMES) {
/* 116 */       if (headers.containsKey(headerName)) {
/* 117 */         return true;
/*     */       }
/*     */     } 
/* 120 */     return false;
/*     */   }
/*     */   
/*     */   private void removeForwardedHeaders(ServerHttpRequest.Builder builder) {
/* 124 */     builder.headers(map -> FORWARDED_HEADER_NAMES.forEach(map::remove));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static String getForwardedPrefix(ServerHttpRequest request) {
/* 130 */     HttpHeaders headers = request.getHeaders();
/* 131 */     String prefix = headers.getFirst("X-Forwarded-Prefix");
/* 132 */     if (prefix != null) {
/* 133 */       int endIndex = prefix.length();
/* 134 */       while (endIndex > 1 && prefix.charAt(endIndex - 1) == '/') {
/* 135 */         endIndex--;
/*     */       }
/* 137 */       prefix = (endIndex != prefix.length()) ? prefix.substring(0, endIndex) : prefix;
/*     */     } 
/* 139 */     return prefix;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/adapter/ForwardedHeaderTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */