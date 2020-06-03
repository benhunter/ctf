/*     */ package org.springframework.web.server.adapter;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.Function;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.InvalidMediaTypeException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpInputMessage;
/*     */ import org.springframework.http.codec.HttpMessageReader;
/*     */ import org.springframework.http.codec.ServerCodecConfigurer;
/*     */ import org.springframework.http.codec.multipart.Part;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.server.ServerWebExchange;
/*     */ import org.springframework.web.server.WebSession;
/*     */ import org.springframework.web.server.i18n.LocaleContextResolver;
/*     */ import org.springframework.web.server.session.WebSessionManager;
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
/*     */ public class DefaultServerWebExchange
/*     */   implements ServerWebExchange
/*     */ {
/*  63 */   private static final List<HttpMethod> SAFE_METHODS = Arrays.asList(new HttpMethod[] { HttpMethod.GET, HttpMethod.HEAD });
/*     */ 
/*     */   
/*  66 */   private static final ResolvableType FORM_DATA_TYPE = ResolvableType.forClassWithGenerics(MultiValueMap.class, new Class[] { String.class, String.class });
/*     */   
/*  68 */   private static final ResolvableType MULTIPART_DATA_TYPE = ResolvableType.forClassWithGenerics(MultiValueMap.class, new Class[] { String.class, Part.class });
/*     */ 
/*     */ 
/*     */   
/*  72 */   private static final Mono<MultiValueMap<String, String>> EMPTY_FORM_DATA = Mono.just(CollectionUtils.unmodifiableMultiValueMap((MultiValueMap)new LinkedMultiValueMap(0)))
/*  73 */     .cache();
/*     */ 
/*     */   
/*  76 */   private static final Mono<MultiValueMap<String, Part>> EMPTY_MULTIPART_DATA = Mono.just(CollectionUtils.unmodifiableMultiValueMap((MultiValueMap)new LinkedMultiValueMap(0)))
/*  77 */     .cache();
/*     */ 
/*     */   
/*     */   private final ServerHttpRequest request;
/*     */   
/*     */   private final ServerHttpResponse response;
/*     */   
/*  84 */   private final Map<String, Object> attributes = new ConcurrentHashMap<>();
/*     */   
/*     */   private final Mono<WebSession> sessionMono;
/*     */   private final LocaleContextResolver localeContextResolver;
/*     */   private final Mono<MultiValueMap<String, String>> formDataMono;
/*     */   private final Mono<MultiValueMap<String, Part>> multipartDataMono;
/*     */   @Nullable
/*     */   private final ApplicationContext applicationContext;
/*     */   private volatile boolean notModified;
/*     */   private Function<String, String> urlTransformer;
/*     */   @Nullable
/*     */   private Object logId;
/*     */   private String logPrefix;
/*     */   
/*     */   DefaultServerWebExchange(ServerHttpRequest request, ServerHttpResponse response, WebSessionManager sessionManager, ServerCodecConfigurer codecConfigurer, LocaleContextResolver localeContextResolver, @Nullable ApplicationContext applicationContext) {
/*  99 */     this.urlTransformer = (url -> url);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     this.logPrefix = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     Assert.notNull(request, "'request' is required");
/* 119 */     Assert.notNull(response, "'response' is required");
/* 120 */     Assert.notNull(sessionManager, "'sessionManager' is required");
/* 121 */     Assert.notNull(codecConfigurer, "'codecConfigurer' is required");
/* 122 */     Assert.notNull(localeContextResolver, "'localeContextResolver' is required");
/*     */ 
/*     */     
/* 125 */     this.attributes.put(ServerWebExchange.LOG_ID_ATTRIBUTE, request.getId());
/*     */     
/* 127 */     this.request = request;
/* 128 */     this.response = response;
/* 129 */     this.sessionMono = sessionManager.getSession(this).cache();
/* 130 */     this.localeContextResolver = localeContextResolver;
/* 131 */     this.formDataMono = initFormData(request, codecConfigurer, getLogPrefix());
/* 132 */     this.multipartDataMono = initMultipartData(request, codecConfigurer, getLogPrefix());
/* 133 */     this.applicationContext = applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Mono<MultiValueMap<String, String>> initFormData(ServerHttpRequest request, ServerCodecConfigurer configurer, String logPrefix) {
/*     */     try {
/* 141 */       MediaType contentType = request.getHeaders().getContentType();
/* 142 */       if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
/* 143 */         return ((HttpMessageReader)configurer.getReaders().stream()
/* 144 */           .filter(reader -> reader.canRead(FORM_DATA_TYPE, MediaType.APPLICATION_FORM_URLENCODED))
/* 145 */           .findFirst()
/* 146 */           .orElseThrow(() -> new IllegalStateException("No form data HttpMessageReader.")))
/* 147 */           .readMono(FORM_DATA_TYPE, (ReactiveHttpInputMessage)request, Hints.from(Hints.LOG_PREFIX_HINT, logPrefix))
/* 148 */           .switchIfEmpty(EMPTY_FORM_DATA)
/* 149 */           .cache();
/*     */       }
/*     */     }
/* 152 */     catch (InvalidMediaTypeException invalidMediaTypeException) {}
/*     */ 
/*     */     
/* 155 */     return EMPTY_FORM_DATA;
/*     */   }
/*     */   public DefaultServerWebExchange(ServerHttpRequest request, ServerHttpResponse response, WebSessionManager sessionManager, ServerCodecConfigurer codecConfigurer, LocaleContextResolver localeContextResolver) {
/*     */     this(request, response, sessionManager, codecConfigurer, localeContextResolver, (ApplicationContext)null);
/*     */   }
/*     */   
/*     */   private static Mono<MultiValueMap<String, Part>> initMultipartData(ServerHttpRequest request, ServerCodecConfigurer configurer, String logPrefix) {
/*     */     try {
/* 163 */       MediaType contentType = request.getHeaders().getContentType();
/* 164 */       if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
/* 165 */         return ((HttpMessageReader)configurer.getReaders().stream()
/* 166 */           .filter(reader -> reader.canRead(MULTIPART_DATA_TYPE, MediaType.MULTIPART_FORM_DATA))
/* 167 */           .findFirst()
/* 168 */           .orElseThrow(() -> new IllegalStateException("No multipart HttpMessageReader.")))
/* 169 */           .readMono(MULTIPART_DATA_TYPE, (ReactiveHttpInputMessage)request, Hints.from(Hints.LOG_PREFIX_HINT, logPrefix))
/* 170 */           .switchIfEmpty(EMPTY_MULTIPART_DATA)
/* 171 */           .cache();
/*     */       }
/*     */     }
/* 174 */     catch (InvalidMediaTypeException invalidMediaTypeException) {}
/*     */ 
/*     */     
/* 177 */     return EMPTY_MULTIPART_DATA;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerHttpRequest getRequest() {
/* 183 */     return this.request;
/*     */   }
/*     */   
/*     */   private HttpHeaders getRequestHeaders() {
/* 187 */     return getRequest().getHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerHttpResponse getResponse() {
/* 192 */     return this.response;
/*     */   }
/*     */   
/*     */   private HttpHeaders getResponseHeaders() {
/* 196 */     return getResponse().getHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getAttributes() {
/* 201 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<WebSession> getSession() {
/* 206 */     return this.sessionMono;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends java.security.Principal> Mono<T> getPrincipal() {
/* 211 */     return Mono.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<MultiValueMap<String, String>> getFormData() {
/* 216 */     return this.formDataMono;
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<MultiValueMap<String, Part>> getMultipartData() {
/* 221 */     return this.multipartDataMono;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocaleContext getLocaleContext() {
/* 226 */     return this.localeContextResolver.resolveLocaleContext(this);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ApplicationContext getApplicationContext() {
/* 232 */     return this.applicationContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNotModified() {
/* 237 */     return this.notModified;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(Instant lastModified) {
/* 242 */     return checkNotModified((String)null, lastModified);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(String etag) {
/* 247 */     return checkNotModified(etag, Instant.MIN);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(@Nullable String etag, Instant lastModified) {
/* 252 */     HttpStatus status = getResponse().getStatusCode();
/* 253 */     if (this.notModified || (status != null && !HttpStatus.OK.equals(status))) {
/* 254 */       return this.notModified;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     if (validateIfUnmodifiedSince(lastModified)) {
/* 261 */       if (this.notModified) {
/* 262 */         getResponse().setStatusCode(HttpStatus.PRECONDITION_FAILED);
/*     */       }
/* 264 */       return this.notModified;
/*     */     } 
/*     */     
/* 267 */     boolean validated = validateIfNoneMatch(etag);
/* 268 */     if (!validated) {
/* 269 */       validateIfModifiedSince(lastModified);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 274 */     boolean isHttpGetOrHead = SAFE_METHODS.contains(getRequest().getMethod());
/* 275 */     if (this.notModified) {
/* 276 */       getResponse().setStatusCode(isHttpGetOrHead ? HttpStatus.NOT_MODIFIED : HttpStatus.PRECONDITION_FAILED);
/*     */     }
/*     */     
/* 279 */     if (isHttpGetOrHead) {
/* 280 */       if (lastModified.isAfter(Instant.EPOCH) && getResponseHeaders().getLastModified() == -1L) {
/* 281 */         getResponseHeaders().setLastModified(lastModified.toEpochMilli());
/*     */       }
/* 283 */       if (StringUtils.hasLength(etag) && getResponseHeaders().getETag() == null) {
/* 284 */         getResponseHeaders().setETag(padEtagIfNecessary(etag));
/*     */       }
/*     */     } 
/*     */     
/* 288 */     return this.notModified;
/*     */   }
/*     */   
/*     */   private boolean validateIfUnmodifiedSince(Instant lastModified) {
/* 292 */     if (lastModified.isBefore(Instant.EPOCH)) {
/* 293 */       return false;
/*     */     }
/* 295 */     long ifUnmodifiedSince = getRequestHeaders().getIfUnmodifiedSince();
/* 296 */     if (ifUnmodifiedSince == -1L) {
/* 297 */       return false;
/*     */     }
/*     */     
/* 300 */     Instant sinceInstant = Instant.ofEpochMilli(ifUnmodifiedSince);
/* 301 */     this.notModified = sinceInstant.isBefore(lastModified.truncatedTo(ChronoUnit.SECONDS));
/* 302 */     return true;
/*     */   }
/*     */   private boolean validateIfNoneMatch(@Nullable String etag) {
/*     */     List<String> ifNoneMatch;
/* 306 */     if (!StringUtils.hasLength(etag)) {
/* 307 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 311 */       ifNoneMatch = getRequestHeaders().getIfNoneMatch();
/*     */     }
/* 313 */     catch (IllegalArgumentException ex) {
/* 314 */       return false;
/*     */     } 
/* 316 */     if (ifNoneMatch.isEmpty()) {
/* 317 */       return false;
/*     */     }
/*     */     
/* 320 */     etag = padEtagIfNecessary(etag);
/* 321 */     if (etag.startsWith("W/")) {
/* 322 */       etag = etag.substring(2);
/*     */     }
/* 324 */     for (String clientEtag : ifNoneMatch) {
/*     */       
/* 326 */       if (StringUtils.hasLength(clientEtag)) {
/* 327 */         if (clientEtag.startsWith("W/")) {
/* 328 */           clientEtag = clientEtag.substring(2);
/*     */         }
/* 330 */         if (clientEtag.equals(etag)) {
/* 331 */           this.notModified = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 336 */     return true;
/*     */   }
/*     */   
/*     */   private String padEtagIfNecessary(String etag) {
/* 340 */     if (!StringUtils.hasLength(etag)) {
/* 341 */       return etag;
/*     */     }
/* 343 */     if ((etag.startsWith("\"") || etag.startsWith("W/\"")) && etag.endsWith("\"")) {
/* 344 */       return etag;
/*     */     }
/* 346 */     return "\"" + etag + "\"";
/*     */   }
/*     */   
/*     */   private boolean validateIfModifiedSince(Instant lastModified) {
/* 350 */     if (lastModified.isBefore(Instant.EPOCH)) {
/* 351 */       return false;
/*     */     }
/* 353 */     long ifModifiedSince = getRequestHeaders().getIfModifiedSince();
/* 354 */     if (ifModifiedSince == -1L) {
/* 355 */       return false;
/*     */     }
/*     */     
/* 358 */     this.notModified = (ChronoUnit.SECONDS.between(lastModified, Instant.ofEpochMilli(ifModifiedSince)) >= 0L);
/* 359 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String transformUrl(String url) {
/* 364 */     return this.urlTransformer.apply(url);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addUrlTransformer(Function<String, String> transformer) {
/* 369 */     Assert.notNull(transformer, "'encoder' must not be null");
/* 370 */     this.urlTransformer = this.urlTransformer.andThen(transformer);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLogPrefix() {
/* 375 */     Object value = getAttribute(LOG_ID_ATTRIBUTE);
/* 376 */     if (this.logId != value) {
/* 377 */       this.logId = value;
/* 378 */       this.logPrefix = (value != null) ? ("[" + value + "] ") : "";
/*     */     } 
/* 380 */     return this.logPrefix;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/adapter/DefaultServerWebExchange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */