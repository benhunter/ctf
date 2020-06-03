/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultUriBuilderFactory
/*     */   implements UriBuilderFactory
/*     */ {
/*     */   @Nullable
/*     */   private final UriComponentsBuilder baseUri;
/*     */   
/*     */   public enum EncodingMode
/*     */   {
/*  64 */     TEMPLATE_AND_VALUES,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     VALUES_ONLY,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     URI_COMPONENT,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     NONE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private EncodingMode encodingMode = EncodingMode.TEMPLATE_AND_VALUES;
/*     */   
/*  96 */   private final Map<String, Object> defaultUriVariables = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean parsePath = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultUriBuilderFactory() {
/* 106 */     this.baseUri = null;
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
/*     */   public DefaultUriBuilderFactory(String baseUriTemplate) {
/* 119 */     this.baseUri = UriComponentsBuilder.fromUriString(baseUriTemplate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultUriBuilderFactory(UriComponentsBuilder baseUri) {
/* 127 */     this.baseUri = baseUri;
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
/*     */   public void setEncodingMode(EncodingMode encodingMode) {
/* 145 */     this.encodingMode = encodingMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EncodingMode getEncodingMode() {
/* 152 */     return this.encodingMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultUriVariables(@Nullable Map<String, ?> defaultUriVariables) {
/* 161 */     this.defaultUriVariables.clear();
/* 162 */     if (defaultUriVariables != null) {
/* 163 */       this.defaultUriVariables.putAll(defaultUriVariables);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ?> getDefaultUriVariables() {
/* 171 */     return Collections.unmodifiableMap(this.defaultUriVariables);
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
/*     */   public void setParsePath(boolean parsePath) {
/* 183 */     this.parsePath = parsePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldParsePath() {
/* 191 */     return this.parsePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI expand(String uriTemplate, Map<String, ?> uriVars) {
/* 198 */     return uriString(uriTemplate).build(uriVars);
/*     */   }
/*     */   
/*     */   public URI expand(String uriTemplate, Object... uriVars) {
/* 202 */     return uriString(uriTemplate).build(uriVars);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UriBuilder uriString(String uriTemplate) {
/* 208 */     return new DefaultUriBuilder(uriTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public UriBuilder builder() {
/* 213 */     return new DefaultUriBuilder("");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class DefaultUriBuilder
/*     */     implements UriBuilder
/*     */   {
/*     */     private final UriComponentsBuilder uriComponentsBuilder;
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder(String uriTemplate) {
/* 225 */       this.uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
/*     */     }
/*     */     
/*     */     private UriComponentsBuilder initUriComponentsBuilder(String uriTemplate) {
/*     */       UriComponentsBuilder result;
/* 230 */       if (!StringUtils.hasLength(uriTemplate)) {
/* 231 */         result = (DefaultUriBuilderFactory.this.baseUri != null) ? DefaultUriBuilderFactory.this.baseUri.cloneBuilder() : UriComponentsBuilder.newInstance();
/*     */       }
/* 233 */       else if (DefaultUriBuilderFactory.this.baseUri != null) {
/* 234 */         UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
/* 235 */         UriComponents uri = builder.build();
/* 236 */         result = (uri.getHost() == null) ? DefaultUriBuilderFactory.this.baseUri.cloneBuilder().uriComponents(uri) : builder;
/*     */       } else {
/*     */         
/* 239 */         result = UriComponentsBuilder.fromUriString(uriTemplate);
/*     */       } 
/* 241 */       if (DefaultUriBuilderFactory.this.encodingMode.equals(DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES)) {
/* 242 */         result.encode();
/*     */       }
/* 244 */       parsePathIfNecessary(result);
/* 245 */       return result;
/*     */     }
/*     */     
/*     */     private void parsePathIfNecessary(UriComponentsBuilder result) {
/* 249 */       if (DefaultUriBuilderFactory.this.parsePath && DefaultUriBuilderFactory.this.encodingMode.equals(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT)) {
/* 250 */         UriComponents uric = result.build();
/* 251 */         String path = uric.getPath();
/* 252 */         result.replacePath((String)null);
/* 253 */         for (String segment : uric.getPathSegments()) {
/* 254 */           result.pathSegment(new String[] { segment });
/*     */         } 
/* 256 */         if (path != null && path.endsWith("/")) {
/* 257 */           result.path("/");
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder scheme(@Nullable String scheme) {
/* 265 */       this.uriComponentsBuilder.scheme(scheme);
/* 266 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder userInfo(@Nullable String userInfo) {
/* 271 */       this.uriComponentsBuilder.userInfo(userInfo);
/* 272 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder host(@Nullable String host) {
/* 277 */       this.uriComponentsBuilder.host(host);
/* 278 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder port(int port) {
/* 283 */       this.uriComponentsBuilder.port(port);
/* 284 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder port(@Nullable String port) {
/* 289 */       this.uriComponentsBuilder.port(port);
/* 290 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder path(String path) {
/* 295 */       this.uriComponentsBuilder.path(path);
/* 296 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder replacePath(@Nullable String path) {
/* 301 */       this.uriComponentsBuilder.replacePath(path);
/* 302 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder pathSegment(String... pathSegments) {
/* 307 */       this.uriComponentsBuilder.pathSegment(pathSegments);
/* 308 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder query(String query) {
/* 313 */       this.uriComponentsBuilder.query(query);
/* 314 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder replaceQuery(@Nullable String query) {
/* 319 */       this.uriComponentsBuilder.replaceQuery(query);
/* 320 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder queryParam(String name, Object... values) {
/* 325 */       this.uriComponentsBuilder.queryParam(name, values);
/* 326 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder replaceQueryParam(String name, Object... values) {
/* 331 */       this.uriComponentsBuilder.replaceQueryParam(name, values);
/* 332 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder queryParams(MultiValueMap<String, String> params) {
/* 337 */       this.uriComponentsBuilder.queryParams(params);
/* 338 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder replaceQueryParams(MultiValueMap<String, String> params) {
/* 343 */       this.uriComponentsBuilder.replaceQueryParams(params);
/* 344 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultUriBuilder fragment(@Nullable String fragment) {
/* 349 */       this.uriComponentsBuilder.fragment(fragment);
/* 350 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public URI build(Map<String, ?> uriVars) {
/* 355 */       if (!DefaultUriBuilderFactory.this.defaultUriVariables.isEmpty()) {
/* 356 */         Map<String, Object> map = new HashMap<>();
/* 357 */         map.putAll(DefaultUriBuilderFactory.this.defaultUriVariables);
/* 358 */         map.putAll(uriVars);
/* 359 */         uriVars = map;
/*     */       } 
/* 361 */       if (DefaultUriBuilderFactory.this.encodingMode.equals(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY)) {
/* 362 */         uriVars = UriUtils.encodeUriVariables(uriVars);
/*     */       }
/* 364 */       UriComponents uric = this.uriComponentsBuilder.build().expand(uriVars);
/* 365 */       return createUri(uric);
/*     */     }
/*     */ 
/*     */     
/*     */     public URI build(Object... uriVars) {
/* 370 */       if (ObjectUtils.isEmpty(uriVars) && !DefaultUriBuilderFactory.this.defaultUriVariables.isEmpty()) {
/* 371 */         return build(Collections.emptyMap());
/*     */       }
/* 373 */       if (DefaultUriBuilderFactory.this.encodingMode.equals(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY)) {
/* 374 */         uriVars = UriUtils.encodeUriVariables(uriVars);
/*     */       }
/* 376 */       UriComponents uric = this.uriComponentsBuilder.build().expand(uriVars);
/* 377 */       return createUri(uric);
/*     */     }
/*     */     
/*     */     private URI createUri(UriComponents uric) {
/* 381 */       if (DefaultUriBuilderFactory.this.encodingMode.equals(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT)) {
/* 382 */         uric = uric.encode();
/*     */       }
/* 384 */       return URI.create(uric.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/DefaultUriBuilderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */