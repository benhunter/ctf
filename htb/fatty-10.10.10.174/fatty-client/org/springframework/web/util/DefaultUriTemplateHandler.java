/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultUriTemplateHandler
/*     */   extends AbstractUriTemplateHandler
/*     */ {
/*     */   private boolean parsePath;
/*     */   private boolean strictEncoding;
/*     */   
/*     */   public void setParsePath(boolean parsePath) {
/*  60 */     this.parsePath = parsePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldParsePath() {
/*  67 */     return this.parsePath;
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
/*     */   public void setStrictEncoding(boolean strictEncoding) {
/*  86 */     this.strictEncoding = strictEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStrictEncoding() {
/*  93 */     return this.strictEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected URI expandInternal(String uriTemplate, Map<String, ?> uriVariables) {
/*  99 */     UriComponentsBuilder uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
/* 100 */     UriComponents uriComponents = expandAndEncode(uriComponentsBuilder, uriVariables);
/* 101 */     return createUri(uriComponents);
/*     */   }
/*     */ 
/*     */   
/*     */   protected URI expandInternal(String uriTemplate, Object... uriVariables) {
/* 106 */     UriComponentsBuilder uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
/* 107 */     UriComponents uriComponents = expandAndEncode(uriComponentsBuilder, uriVariables);
/* 108 */     return createUri(uriComponents);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UriComponentsBuilder initUriComponentsBuilder(String uriTemplate) {
/* 117 */     UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
/* 118 */     if (shouldParsePath() && !isStrictEncoding()) {
/* 119 */       List<String> pathSegments = builder.build().getPathSegments();
/* 120 */       builder.replacePath((String)null);
/* 121 */       for (String pathSegment : pathSegments) {
/* 122 */         builder.pathSegment(new String[] { pathSegment });
/*     */       } 
/*     */     } 
/* 125 */     return builder;
/*     */   }
/*     */   
/*     */   protected UriComponents expandAndEncode(UriComponentsBuilder builder, Map<String, ?> uriVariables) {
/* 129 */     if (!isStrictEncoding()) {
/* 130 */       return builder.buildAndExpand(uriVariables).encode();
/*     */     }
/*     */     
/* 133 */     Map<String, ?> encodedUriVars = UriUtils.encodeUriVariables(uriVariables);
/* 134 */     return builder.buildAndExpand(encodedUriVars);
/*     */   }
/*     */ 
/*     */   
/*     */   protected UriComponents expandAndEncode(UriComponentsBuilder builder, Object[] uriVariables) {
/* 139 */     if (!isStrictEncoding()) {
/* 140 */       return builder.buildAndExpand(uriVariables).encode();
/*     */     }
/*     */     
/* 143 */     Object[] encodedUriVars = UriUtils.encodeUriVariables(uriVariables);
/* 144 */     return builder.buildAndExpand(encodedUriVars);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private URI createUri(UriComponents uriComponents) {
/*     */     try {
/* 151 */       return new URI(uriComponents.toUriString());
/*     */     }
/* 153 */     catch (URISyntaxException ex) {
/* 154 */       throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/DefaultUriTemplateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */