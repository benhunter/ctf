/*     */ package org.springframework.web.cors;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public class CorsConfiguration
/*     */ {
/*     */   public static final String ALL = "*";
/*  55 */   private static final List<HttpMethod> DEFAULT_METHODS = Collections.unmodifiableList(
/*  56 */       Arrays.asList(new HttpMethod[] { HttpMethod.GET, HttpMethod.HEAD }));
/*     */   
/*  58 */   private static final List<String> DEFAULT_PERMIT_METHODS = Collections.unmodifiableList(
/*  59 */       Arrays.asList(new String[] { HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name() }));
/*     */   
/*  61 */   private static final List<String> DEFAULT_PERMIT_ALL = Collections.unmodifiableList(
/*  62 */       Collections.singletonList("*"));
/*     */   
/*     */   @Nullable
/*     */   private List<String> allowedOrigins;
/*     */   
/*     */   @Nullable
/*     */   private List<String> allowedMethods;
/*     */   
/*     */   @Nullable
/*  71 */   private List<HttpMethod> resolvedMethods = DEFAULT_METHODS;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private List<String> allowedHeaders;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private List<String> exposedHeaders;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Boolean allowCredentials;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Long maxAge;
/*     */ 
/*     */ 
/*     */   
/*     */   public CorsConfiguration() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public CorsConfiguration(CorsConfiguration other) {
/* 100 */     this.allowedOrigins = other.allowedOrigins;
/* 101 */     this.allowedMethods = other.allowedMethods;
/* 102 */     this.resolvedMethods = other.resolvedMethods;
/* 103 */     this.allowedHeaders = other.allowedHeaders;
/* 104 */     this.exposedHeaders = other.exposedHeaders;
/* 105 */     this.allowCredentials = other.allowCredentials;
/* 106 */     this.maxAge = other.maxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowedOrigins(@Nullable List<String> allowedOrigins) {
/* 116 */     this.allowedOrigins = (allowedOrigins != null) ? new ArrayList<>(allowedOrigins) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> getAllowedOrigins() {
/* 126 */     return this.allowedOrigins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAllowedOrigin(String origin) {
/* 133 */     if (this.allowedOrigins == null) {
/* 134 */       this.allowedOrigins = new ArrayList<>(4);
/*     */     }
/* 136 */     else if (this.allowedOrigins == DEFAULT_PERMIT_ALL) {
/* 137 */       setAllowedOrigins(DEFAULT_PERMIT_ALL);
/*     */     } 
/* 139 */     this.allowedOrigins.add(origin);
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
/*     */   public void setAllowedMethods(@Nullable List<String> allowedMethods) {
/* 157 */     this.allowedMethods = (allowedMethods != null) ? new ArrayList<>(allowedMethods) : null;
/* 158 */     if (!CollectionUtils.isEmpty(allowedMethods)) {
/* 159 */       this.resolvedMethods = new ArrayList<>(allowedMethods.size());
/* 160 */       for (String method : allowedMethods) {
/* 161 */         if ("*".equals(method)) {
/* 162 */           this.resolvedMethods = null;
/*     */           break;
/*     */         } 
/* 165 */         this.resolvedMethods.add(HttpMethod.resolve(method));
/*     */       } 
/*     */     } else {
/*     */       
/* 169 */       this.resolvedMethods = DEFAULT_METHODS;
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
/*     */   @Nullable
/*     */   public List<String> getAllowedMethods() {
/* 182 */     return this.allowedMethods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAllowedMethod(HttpMethod method) {
/* 189 */     addAllowedMethod(method.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAllowedMethod(String method) {
/* 196 */     if (StringUtils.hasText(method)) {
/* 197 */       if (this.allowedMethods == null) {
/* 198 */         this.allowedMethods = new ArrayList<>(4);
/* 199 */         this.resolvedMethods = new ArrayList<>(4);
/*     */       }
/* 201 */       else if (this.allowedMethods == DEFAULT_PERMIT_METHODS) {
/* 202 */         setAllowedMethods(DEFAULT_PERMIT_METHODS);
/*     */       } 
/* 204 */       this.allowedMethods.add(method);
/* 205 */       if ("*".equals(method)) {
/* 206 */         this.resolvedMethods = null;
/*     */       }
/* 208 */       else if (this.resolvedMethods != null) {
/* 209 */         this.resolvedMethods.add(HttpMethod.resolve(method));
/*     */       } 
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
/*     */ 
/*     */   
/*     */   public void setAllowedHeaders(@Nullable List<String> allowedHeaders) {
/* 225 */     this.allowedHeaders = (allowedHeaders != null) ? new ArrayList<>(allowedHeaders) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> getAllowedHeaders() {
/* 235 */     return this.allowedHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAllowedHeader(String allowedHeader) {
/* 242 */     if (this.allowedHeaders == null) {
/* 243 */       this.allowedHeaders = new ArrayList<>(4);
/*     */     }
/* 245 */     else if (this.allowedHeaders == DEFAULT_PERMIT_ALL) {
/* 246 */       setAllowedHeaders(DEFAULT_PERMIT_ALL);
/*     */     } 
/* 248 */     this.allowedHeaders.add(allowedHeader);
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
/*     */   public void setExposedHeaders(@Nullable List<String> exposedHeaders) {
/* 260 */     if (exposedHeaders != null && exposedHeaders.contains("*")) {
/* 261 */       throw new IllegalArgumentException("'*' is not a valid exposed header value");
/*     */     }
/* 263 */     this.exposedHeaders = (exposedHeaders != null) ? new ArrayList<>(exposedHeaders) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> getExposedHeaders() {
/* 273 */     return this.exposedHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExposedHeader(String exposedHeader) {
/* 281 */     if ("*".equals(exposedHeader)) {
/* 282 */       throw new IllegalArgumentException("'*' is not a valid exposed header value");
/*     */     }
/* 284 */     if (this.exposedHeaders == null) {
/* 285 */       this.exposedHeaders = new ArrayList<>(4);
/*     */     }
/* 287 */     this.exposedHeaders.add(exposedHeader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowCredentials(@Nullable Boolean allowCredentials) {
/* 295 */     this.allowCredentials = allowCredentials;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Boolean getAllowCredentials() {
/* 304 */     return this.allowCredentials;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxAge(@Nullable Long maxAge) {
/* 313 */     this.maxAge = maxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Long getMaxAge() {
/* 322 */     return this.maxAge;
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
/*     */   public CorsConfiguration applyPermitDefaultValues() {
/* 343 */     if (this.allowedOrigins == null) {
/* 344 */       this.allowedOrigins = DEFAULT_PERMIT_ALL;
/*     */     }
/* 346 */     if (this.allowedMethods == null) {
/* 347 */       this.allowedMethods = DEFAULT_PERMIT_METHODS;
/* 348 */       this
/* 349 */         .resolvedMethods = (List<HttpMethod>)DEFAULT_PERMIT_METHODS.stream().map(HttpMethod::resolve).collect(Collectors.toList());
/*     */     } 
/* 351 */     if (this.allowedHeaders == null) {
/* 352 */       this.allowedHeaders = DEFAULT_PERMIT_ALL;
/*     */     }
/* 354 */     if (this.maxAge == null) {
/* 355 */       this.maxAge = Long.valueOf(1800L);
/*     */     }
/* 357 */     return this;
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
/*     */   @Nullable
/*     */   public CorsConfiguration combine(@Nullable CorsConfiguration other) {
/* 380 */     if (other == null) {
/* 381 */       return this;
/*     */     }
/* 383 */     CorsConfiguration config = new CorsConfiguration(this);
/* 384 */     config.setAllowedOrigins(combine(getAllowedOrigins(), other.getAllowedOrigins()));
/* 385 */     config.setAllowedMethods(combine(getAllowedMethods(), other.getAllowedMethods()));
/* 386 */     config.setAllowedHeaders(combine(getAllowedHeaders(), other.getAllowedHeaders()));
/* 387 */     config.setExposedHeaders(combine(getExposedHeaders(), other.getExposedHeaders()));
/* 388 */     Boolean allowCredentials = other.getAllowCredentials();
/* 389 */     if (allowCredentials != null) {
/* 390 */       config.setAllowCredentials(allowCredentials);
/*     */     }
/* 392 */     Long maxAge = other.getMaxAge();
/* 393 */     if (maxAge != null) {
/* 394 */       config.setMaxAge(maxAge);
/*     */     }
/* 396 */     return config;
/*     */   }
/*     */   
/*     */   private List<String> combine(@Nullable List<String> source, @Nullable List<String> other) {
/* 400 */     if (other == null) {
/* 401 */       return (source != null) ? source : Collections.<String>emptyList();
/*     */     }
/* 403 */     if (source == null) {
/* 404 */       return other;
/*     */     }
/* 406 */     if (source == DEFAULT_PERMIT_ALL || source == DEFAULT_PERMIT_METHODS) {
/* 407 */       return other;
/*     */     }
/* 409 */     if (other == DEFAULT_PERMIT_ALL || other == DEFAULT_PERMIT_METHODS) {
/* 410 */       return source;
/*     */     }
/* 412 */     if (source.contains("*") || other.contains("*")) {
/* 413 */       return new ArrayList<>(Collections.singletonList("*"));
/*     */     }
/* 415 */     Set<String> combined = new LinkedHashSet<>(source);
/* 416 */     combined.addAll(other);
/* 417 */     return new ArrayList<>(combined);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String checkOrigin(@Nullable String requestOrigin) {
/* 428 */     if (!StringUtils.hasText(requestOrigin)) {
/* 429 */       return null;
/*     */     }
/* 431 */     if (ObjectUtils.isEmpty(this.allowedOrigins)) {
/* 432 */       return null;
/*     */     }
/*     */     
/* 435 */     if (this.allowedOrigins.contains("*")) {
/* 436 */       if (this.allowCredentials != Boolean.TRUE) {
/* 437 */         return "*";
/*     */       }
/*     */       
/* 440 */       return requestOrigin;
/*     */     } 
/*     */     
/* 443 */     for (String allowedOrigin : this.allowedOrigins) {
/* 444 */       if (requestOrigin.equalsIgnoreCase(allowedOrigin)) {
/* 445 */         return requestOrigin;
/*     */       }
/*     */     } 
/*     */     
/* 449 */     return null;
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
/*     */   @Nullable
/*     */   public List<HttpMethod> checkHttpMethod(@Nullable HttpMethod requestMethod) {
/* 462 */     if (requestMethod == null) {
/* 463 */       return null;
/*     */     }
/* 465 */     if (this.resolvedMethods == null) {
/* 466 */       return Collections.singletonList(requestMethod);
/*     */     }
/* 468 */     return this.resolvedMethods.contains(requestMethod) ? this.resolvedMethods : null;
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
/*     */   @Nullable
/*     */   public List<String> checkHeaders(@Nullable List<String> requestHeaders) {
/* 481 */     if (requestHeaders == null) {
/* 482 */       return null;
/*     */     }
/* 484 */     if (requestHeaders.isEmpty()) {
/* 485 */       return Collections.emptyList();
/*     */     }
/* 487 */     if (ObjectUtils.isEmpty(this.allowedHeaders)) {
/* 488 */       return null;
/*     */     }
/*     */     
/* 491 */     boolean allowAnyHeader = this.allowedHeaders.contains("*");
/* 492 */     List<String> result = new ArrayList<>(requestHeaders.size());
/* 493 */     for (String requestHeader : requestHeaders) {
/* 494 */       if (StringUtils.hasText(requestHeader)) {
/* 495 */         requestHeader = requestHeader.trim();
/* 496 */         if (allowAnyHeader) {
/* 497 */           result.add(requestHeader);
/*     */           continue;
/*     */         } 
/* 500 */         for (String allowedHeader : this.allowedHeaders) {
/* 501 */           if (requestHeader.equalsIgnoreCase(allowedHeader)) {
/* 502 */             result.add(requestHeader);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 509 */     return result.isEmpty() ? null : result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/cors/CorsConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */