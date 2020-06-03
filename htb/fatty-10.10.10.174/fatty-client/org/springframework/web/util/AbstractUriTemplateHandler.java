/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ @Deprecated
/*     */ public abstract class AbstractUriTemplateHandler
/*     */   implements UriTemplateHandler
/*     */ {
/*     */   @Nullable
/*     */   private String baseUrl;
/*  45 */   private final Map<String, Object> defaultUriVariables = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBaseUrl(@Nullable String baseUrl) {
/*  56 */     if (baseUrl != null) {
/*  57 */       UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUrl).build();
/*  58 */       Assert.hasText(uriComponents.getScheme(), "'baseUrl' must have a scheme");
/*  59 */       Assert.hasText(uriComponents.getHost(), "'baseUrl' must have a host");
/*  60 */       Assert.isNull(uriComponents.getQuery(), "'baseUrl' cannot have a query");
/*  61 */       Assert.isNull(uriComponents.getFragment(), "'baseUrl' cannot have a fragment");
/*     */     } 
/*  63 */     this.baseUrl = baseUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getBaseUrl() {
/*  71 */     return this.baseUrl;
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
/*     */   public void setDefaultUriVariables(@Nullable Map<String, ?> defaultUriVariables) {
/*  83 */     this.defaultUriVariables.clear();
/*  84 */     if (defaultUriVariables != null) {
/*  85 */       this.defaultUriVariables.putAll(defaultUriVariables);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ?> getDefaultUriVariables() {
/*  93 */     return Collections.unmodifiableMap(this.defaultUriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URI expand(String uriTemplate, Map<String, ?> uriVariables) {
/*  99 */     if (!getDefaultUriVariables().isEmpty()) {
/* 100 */       Map<String, Object> map = new HashMap<>();
/* 101 */       map.putAll(getDefaultUriVariables());
/* 102 */       map.putAll(uriVariables);
/* 103 */       uriVariables = map;
/*     */     } 
/* 105 */     URI url = expandInternal(uriTemplate, uriVariables);
/* 106 */     return insertBaseUrl(url);
/*     */   }
/*     */ 
/*     */   
/*     */   public URI expand(String uriTemplate, Object... uriVariables) {
/* 111 */     URI url = expandInternal(uriTemplate, uriVariables);
/* 112 */     return insertBaseUrl(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract URI expandInternal(String paramString, Map<String, ?> paramMap);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract URI expandInternal(String paramString, Object... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private URI insertBaseUrl(URI url) {
/*     */     try {
/* 132 */       String baseUrl = getBaseUrl();
/* 133 */       if (baseUrl != null && url.getHost() == null) {
/* 134 */         url = new URI(baseUrl + url.toString());
/*     */       }
/* 136 */       return url;
/*     */     }
/* 138 */     catch (URISyntaxException ex) {
/* 139 */       throw new IllegalArgumentException("Invalid URL after inserting base URL: " + url, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/AbstractUriTemplateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */