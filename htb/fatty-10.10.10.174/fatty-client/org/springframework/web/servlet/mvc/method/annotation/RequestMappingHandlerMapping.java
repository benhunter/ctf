/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.bind.annotation.CrossOrigin;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.servlet.handler.MatchableHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.RequestMatchResult;
/*     */ import org.springframework.web.servlet.mvc.condition.RequestCondition;
/*     */ import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
/*     */ import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestMappingHandlerMapping
/*     */   extends RequestMappingInfoHandlerMapping
/*     */   implements MatchableHandlerMapping, EmbeddedValueResolverAware
/*     */ {
/*     */   private boolean useSuffixPatternMatch = true;
/*     */   private boolean useRegisteredSuffixPatternMatch = false;
/*     */   private boolean useTrailingSlashMatch = true;
/*  69 */   private Map<String, Predicate<Class<?>>> pathPrefixes = new LinkedHashMap<>();
/*     */   
/*  71 */   private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
/*     */   
/*     */   @Nullable
/*     */   private StringValueResolver embeddedValueResolver;
/*     */   
/*  76 */   private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch) {
/*  87 */     this.useSuffixPatternMatch = useSuffixPatternMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseRegisteredSuffixPatternMatch(boolean useRegisteredSuffixPatternMatch) {
/*  98 */     this.useRegisteredSuffixPatternMatch = useRegisteredSuffixPatternMatch;
/*  99 */     this.useSuffixPatternMatch = (useRegisteredSuffixPatternMatch || this.useSuffixPatternMatch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch) {
/* 108 */     this.useTrailingSlashMatch = useTrailingSlashMatch;
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
/*     */   public void setPathPrefixes(Map<String, Predicate<Class<?>>> prefixes) {
/* 122 */     this.pathPrefixes = Collections.unmodifiableMap(new LinkedHashMap<>(prefixes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Predicate<Class<?>>> getPathPrefixes() {
/* 130 */     return this.pathPrefixes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
/* 138 */     Assert.notNull(contentNegotiationManager, "ContentNegotiationManager must not be null");
/* 139 */     this.contentNegotiationManager = contentNegotiationManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationManager getContentNegotiationManager() {
/* 146 */     return this.contentNegotiationManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEmbeddedValueResolver(StringValueResolver resolver) {
/* 151 */     this.embeddedValueResolver = resolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 156 */     this.config = new RequestMappingInfo.BuilderConfiguration();
/* 157 */     this.config.setUrlPathHelper(getUrlPathHelper());
/* 158 */     this.config.setPathMatcher(getPathMatcher());
/* 159 */     this.config.setSuffixPatternMatch(this.useSuffixPatternMatch);
/* 160 */     this.config.setTrailingSlashMatch(this.useTrailingSlashMatch);
/* 161 */     this.config.setRegisteredSuffixPatternMatch(this.useRegisteredSuffixPatternMatch);
/* 162 */     this.config.setContentNegotiationManager(getContentNegotiationManager());
/*     */     
/* 164 */     super.afterPropertiesSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean useSuffixPatternMatch() {
/* 172 */     return this.useSuffixPatternMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean useRegisteredSuffixPatternMatch() {
/* 179 */     return this.useRegisteredSuffixPatternMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean useTrailingSlashMatch() {
/* 186 */     return this.useTrailingSlashMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> getFileExtensions() {
/* 194 */     return this.config.getFileExtensions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isHandler(Class<?> beanType) {
/* 205 */     return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) || 
/* 206 */       AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class));
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
/*     */   @Nullable
/*     */   protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
/* 220 */     RequestMappingInfo info = createRequestMappingInfo(method);
/* 221 */     if (info != null) {
/* 222 */       RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
/* 223 */       if (typeInfo != null) {
/* 224 */         info = typeInfo.combine(info);
/*     */       }
/* 226 */       String prefix = getPathPrefix(handlerType);
/* 227 */       if (prefix != null) {
/* 228 */         info = RequestMappingInfo.paths(new String[] { prefix }).build().combine(info);
/*     */       }
/*     */     } 
/* 231 */     return info;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   String getPathPrefix(Class<?> handlerType) {
/* 236 */     for (Map.Entry<String, Predicate<Class<?>>> entry : this.pathPrefixes.entrySet()) {
/* 237 */       if (((Predicate<Class<?>>)entry.getValue()).test(handlerType)) {
/* 238 */         String prefix = entry.getKey();
/* 239 */         if (this.embeddedValueResolver != null) {
/* 240 */           prefix = this.embeddedValueResolver.resolveStringValue(prefix);
/*     */         }
/* 242 */         return prefix;
/*     */       } 
/*     */     } 
/* 245 */     return null;
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
/*     */   private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
/* 257 */     RequestMapping requestMapping = (RequestMapping)AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
/*     */     
/* 259 */     RequestCondition<?> condition = (element instanceof Class) ? getCustomTypeCondition((Class)element) : getCustomMethodCondition((Method)element);
/* 260 */     return (requestMapping != null) ? createRequestMappingInfo(requestMapping, condition) : null;
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
/*     */   @Nullable
/*     */   protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
/* 276 */     return null;
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
/*     */   @Nullable
/*     */   protected RequestCondition<?> getCustomMethodCondition(Method method) {
/* 292 */     return null;
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
/*     */   protected RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping, @Nullable RequestCondition<?> customCondition) {
/* 311 */     RequestMappingInfo.Builder builder = RequestMappingInfo.paths(resolveEmbeddedValuesInPatterns(requestMapping.path())).methods(requestMapping.method()).params(requestMapping.params()).headers(requestMapping.headers()).consumes(requestMapping.consumes()).produces(requestMapping.produces()).mappingName(requestMapping.name());
/* 312 */     if (customCondition != null) {
/* 313 */       builder.customCondition(customCondition);
/*     */     }
/* 315 */     return builder.options(this.config).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] resolveEmbeddedValuesInPatterns(String[] patterns) {
/* 323 */     if (this.embeddedValueResolver == null) {
/* 324 */       return patterns;
/*     */     }
/*     */     
/* 327 */     String[] resolvedPatterns = new String[patterns.length];
/* 328 */     for (int i = 0; i < patterns.length; i++) {
/* 329 */       resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
/*     */     }
/* 331 */     return resolvedPatterns;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestMatchResult match(HttpServletRequest request, String pattern) {
/* 337 */     RequestMappingInfo info = RequestMappingInfo.paths(new String[] { pattern }).options(this.config).build();
/* 338 */     RequestMappingInfo matchingInfo = info.getMatchingCondition(request);
/* 339 */     if (matchingInfo == null) {
/* 340 */       return null;
/*     */     }
/* 342 */     Set<String> patterns = matchingInfo.getPatternsCondition().getPatterns();
/* 343 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 344 */     return new RequestMatchResult(patterns.iterator().next(), lookupPath, getPathMatcher());
/*     */   }
/*     */ 
/*     */   
/*     */   protected CorsConfiguration initCorsConfiguration(Object handler, Method method, RequestMappingInfo mappingInfo) {
/* 349 */     HandlerMethod handlerMethod = createHandlerMethod(handler, method);
/* 350 */     Class<?> beanType = handlerMethod.getBeanType();
/* 351 */     CrossOrigin typeAnnotation = (CrossOrigin)AnnotatedElementUtils.findMergedAnnotation(beanType, CrossOrigin.class);
/* 352 */     CrossOrigin methodAnnotation = (CrossOrigin)AnnotatedElementUtils.findMergedAnnotation(method, CrossOrigin.class);
/*     */     
/* 354 */     if (typeAnnotation == null && methodAnnotation == null) {
/* 355 */       return null;
/*     */     }
/*     */     
/* 358 */     CorsConfiguration config = new CorsConfiguration();
/* 359 */     updateCorsConfig(config, typeAnnotation);
/* 360 */     updateCorsConfig(config, methodAnnotation);
/*     */     
/* 362 */     if (CollectionUtils.isEmpty(config.getAllowedMethods())) {
/* 363 */       for (RequestMethod allowedMethod : mappingInfo.getMethodsCondition().getMethods()) {
/* 364 */         config.addAllowedMethod(allowedMethod.name());
/*     */       }
/*     */     }
/* 367 */     return config.applyPermitDefaultValues();
/*     */   }
/*     */   
/*     */   private void updateCorsConfig(CorsConfiguration config, @Nullable CrossOrigin annotation) {
/* 371 */     if (annotation == null) {
/*     */       return;
/*     */     }
/* 374 */     for (String origin : annotation.origins()) {
/* 375 */       config.addAllowedOrigin(resolveCorsAnnotationValue(origin));
/*     */     }
/* 377 */     for (RequestMethod method : annotation.methods()) {
/* 378 */       config.addAllowedMethod(method.name());
/*     */     }
/* 380 */     for (String header : annotation.allowedHeaders()) {
/* 381 */       config.addAllowedHeader(resolveCorsAnnotationValue(header));
/*     */     }
/* 383 */     for (String header : annotation.exposedHeaders()) {
/* 384 */       config.addExposedHeader(resolveCorsAnnotationValue(header));
/*     */     }
/*     */     
/* 387 */     String allowCredentials = resolveCorsAnnotationValue(annotation.allowCredentials());
/* 388 */     if ("true".equalsIgnoreCase(allowCredentials)) {
/* 389 */       config.setAllowCredentials(Boolean.valueOf(true));
/*     */     }
/* 391 */     else if ("false".equalsIgnoreCase(allowCredentials)) {
/* 392 */       config.setAllowCredentials(Boolean.valueOf(false));
/*     */     }
/* 394 */     else if (!allowCredentials.isEmpty()) {
/* 395 */       throw new IllegalStateException("@CrossOrigin's allowCredentials value must be \"true\", \"false\", or an empty string (\"\"): current value is [" + allowCredentials + "]");
/*     */     } 
/*     */ 
/*     */     
/* 399 */     if (annotation.maxAge() >= 0L && config.getMaxAge() == null) {
/* 400 */       config.setMaxAge(Long.valueOf(annotation.maxAge()));
/*     */     }
/*     */   }
/*     */   
/*     */   private String resolveCorsAnnotationValue(String value) {
/* 405 */     if (this.embeddedValueResolver != null) {
/* 406 */       String resolved = this.embeddedValueResolver.resolveStringValue(value);
/* 407 */       return (resolved != null) ? resolved : "";
/*     */     } 
/*     */     
/* 410 */     return value;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/RequestMappingHandlerMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */