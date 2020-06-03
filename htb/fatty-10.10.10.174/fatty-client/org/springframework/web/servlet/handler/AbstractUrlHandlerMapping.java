/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.servlet.HandlerExecutionChain;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractUrlHandlerMapping
/*     */   extends AbstractHandlerMapping
/*     */   implements MatchableHandlerMapping
/*     */ {
/*     */   @Nullable
/*     */   private Object rootHandler;
/*     */   private boolean useTrailingSlashMatch = false;
/*     */   private boolean lazyInitHandlers = false;
/*  63 */   private final Map<String, Object> handlerMap = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRootHandler(@Nullable Object rootHandler) {
/*  72 */     this.rootHandler = rootHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getRootHandler() {
/*  81 */     return this.rootHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch) {
/*  90 */     this.useTrailingSlashMatch = useTrailingSlashMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean useTrailingSlashMatch() {
/*  97 */     return this.useTrailingSlashMatch;
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
/*     */   public void setLazyInitHandlers(boolean lazyInitHandlers) {
/* 111 */     this.lazyInitHandlers = lazyInitHandlers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
/* 122 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 123 */     Object handler = lookupHandler(lookupPath, request);
/* 124 */     if (handler == null) {
/*     */ 
/*     */       
/* 127 */       Object rawHandler = null;
/* 128 */       if ("/".equals(lookupPath)) {
/* 129 */         rawHandler = getRootHandler();
/*     */       }
/* 131 */       if (rawHandler == null) {
/* 132 */         rawHandler = getDefaultHandler();
/*     */       }
/* 134 */       if (rawHandler != null) {
/*     */         
/* 136 */         if (rawHandler instanceof String) {
/* 137 */           String handlerName = (String)rawHandler;
/* 138 */           rawHandler = obtainApplicationContext().getBean(handlerName);
/*     */         } 
/* 140 */         validateHandler(rawHandler, request);
/* 141 */         handler = buildPathExposingHandler(rawHandler, lookupPath, lookupPath, (Map<String, String>)null);
/*     */       } 
/*     */     } 
/* 144 */     return handler;
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
/*     */   @Nullable
/*     */   protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
/* 163 */     Object handler = this.handlerMap.get(urlPath);
/* 164 */     if (handler != null) {
/*     */       
/* 166 */       if (handler instanceof String) {
/* 167 */         String handlerName = (String)handler;
/* 168 */         handler = obtainApplicationContext().getBean(handlerName);
/*     */       } 
/* 170 */       validateHandler(handler, request);
/* 171 */       return buildPathExposingHandler(handler, urlPath, urlPath, (Map<String, String>)null);
/*     */     } 
/*     */ 
/*     */     
/* 175 */     List<String> matchingPatterns = new ArrayList<>();
/* 176 */     for (String registeredPattern : this.handlerMap.keySet()) {
/* 177 */       if (getPathMatcher().match(registeredPattern, urlPath)) {
/* 178 */         matchingPatterns.add(registeredPattern); continue;
/*     */       } 
/* 180 */       if (useTrailingSlashMatch() && 
/* 181 */         !registeredPattern.endsWith("/") && getPathMatcher().match(registeredPattern + "/", urlPath)) {
/* 182 */         matchingPatterns.add(registeredPattern + "/");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 187 */     String bestMatch = null;
/* 188 */     Comparator<String> patternComparator = getPathMatcher().getPatternComparator(urlPath);
/* 189 */     if (!matchingPatterns.isEmpty()) {
/* 190 */       matchingPatterns.sort(patternComparator);
/* 191 */       if (this.logger.isTraceEnabled() && matchingPatterns.size() > 1) {
/* 192 */         this.logger.trace("Matching patterns " + matchingPatterns);
/*     */       }
/* 194 */       bestMatch = matchingPatterns.get(0);
/*     */     } 
/* 196 */     if (bestMatch != null) {
/* 197 */       handler = this.handlerMap.get(bestMatch);
/* 198 */       if (handler == null) {
/* 199 */         if (bestMatch.endsWith("/")) {
/* 200 */           handler = this.handlerMap.get(bestMatch.substring(0, bestMatch.length() - 1));
/*     */         }
/* 202 */         if (handler == null) {
/* 203 */           throw new IllegalStateException("Could not find handler for best pattern match [" + bestMatch + "]");
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 208 */       if (handler instanceof String) {
/* 209 */         String handlerName = (String)handler;
/* 210 */         handler = obtainApplicationContext().getBean(handlerName);
/*     */       } 
/* 212 */       validateHandler(handler, request);
/* 213 */       String pathWithinMapping = getPathMatcher().extractPathWithinPattern(bestMatch, urlPath);
/*     */ 
/*     */ 
/*     */       
/* 217 */       Map<String, String> uriTemplateVariables = new LinkedHashMap<>();
/* 218 */       for (String matchingPattern : matchingPatterns) {
/* 219 */         if (patternComparator.compare(bestMatch, matchingPattern) == 0) {
/* 220 */           Map<String, String> vars = getPathMatcher().extractUriTemplateVariables(matchingPattern, urlPath);
/* 221 */           Map<String, String> decodedVars = getUrlPathHelper().decodePathVariables(request, vars);
/* 222 */           uriTemplateVariables.putAll(decodedVars);
/*     */         } 
/*     */       } 
/* 225 */       if (this.logger.isTraceEnabled() && uriTemplateVariables.size() > 0) {
/* 226 */         this.logger.trace("URI variables " + uriTemplateVariables);
/*     */       }
/* 228 */       return buildPathExposingHandler(handler, bestMatch, pathWithinMapping, uriTemplateVariables);
/*     */     } 
/*     */ 
/*     */     
/* 232 */     return null;
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
/*     */   protected void validateHandler(Object handler, HttpServletRequest request) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object buildPathExposingHandler(Object rawHandler, String bestMatchingPattern, String pathWithinMapping, @Nullable Map<String, String> uriTemplateVariables) {
/* 260 */     HandlerExecutionChain chain = new HandlerExecutionChain(rawHandler);
/* 261 */     chain.addInterceptor((HandlerInterceptor)new PathExposingHandlerInterceptor(bestMatchingPattern, pathWithinMapping));
/* 262 */     if (!CollectionUtils.isEmpty(uriTemplateVariables)) {
/* 263 */       chain.addInterceptor((HandlerInterceptor)new UriTemplateVariablesHandlerInterceptor(uriTemplateVariables));
/*     */     }
/* 265 */     return chain;
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
/*     */   protected void exposePathWithinMapping(String bestMatchingPattern, String pathWithinMapping, HttpServletRequest request) {
/* 277 */     request.setAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE, bestMatchingPattern);
/* 278 */     request.setAttribute(PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, pathWithinMapping);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void exposeUriTemplateVariables(Map<String, String> uriTemplateVariables, HttpServletRequest request) {
/* 288 */     request.setAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriTemplateVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RequestMatchResult match(HttpServletRequest request, String pattern) {
/* 294 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 295 */     if (getPathMatcher().match(pattern, lookupPath)) {
/* 296 */       return new RequestMatchResult(pattern, lookupPath, getPathMatcher());
/*     */     }
/* 298 */     if (useTrailingSlashMatch() && 
/* 299 */       !pattern.endsWith("/") && getPathMatcher().match(pattern + "/", lookupPath)) {
/* 300 */       return new RequestMatchResult(pattern + "/", lookupPath, getPathMatcher());
/*     */     }
/*     */     
/* 303 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerHandler(String[] urlPaths, String beanName) throws BeansException, IllegalStateException {
/* 314 */     Assert.notNull(urlPaths, "URL path array must not be null");
/* 315 */     for (String urlPath : urlPaths) {
/* 316 */       registerHandler(urlPath, beanName);
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
/*     */   protected void registerHandler(String urlPath, Object handler) throws BeansException, IllegalStateException {
/* 329 */     Assert.notNull(urlPath, "URL path must not be null");
/* 330 */     Assert.notNull(handler, "Handler object must not be null");
/* 331 */     Object resolvedHandler = handler;
/*     */ 
/*     */     
/* 334 */     if (!this.lazyInitHandlers && handler instanceof String) {
/* 335 */       String handlerName = (String)handler;
/* 336 */       ApplicationContext applicationContext = obtainApplicationContext();
/* 337 */       if (applicationContext.isSingleton(handlerName)) {
/* 338 */         resolvedHandler = applicationContext.getBean(handlerName);
/*     */       }
/*     */     } 
/*     */     
/* 342 */     Object mappedHandler = this.handlerMap.get(urlPath);
/* 343 */     if (mappedHandler != null) {
/* 344 */       if (mappedHandler != resolvedHandler) {
/* 345 */         throw new IllegalStateException("Cannot map " + 
/* 346 */             getHandlerDescription(handler) + " to URL path [" + urlPath + "]: There is already " + 
/* 347 */             getHandlerDescription(mappedHandler) + " mapped.");
/*     */       
/*     */       }
/*     */     }
/* 351 */     else if (urlPath.equals("/")) {
/* 352 */       if (this.logger.isTraceEnabled()) {
/* 353 */         this.logger.trace("Root mapping to " + getHandlerDescription(handler));
/*     */       }
/* 355 */       setRootHandler(resolvedHandler);
/*     */     }
/* 357 */     else if (urlPath.equals("/*")) {
/* 358 */       if (this.logger.isTraceEnabled()) {
/* 359 */         this.logger.trace("Default mapping to " + getHandlerDescription(handler));
/*     */       }
/* 361 */       setDefaultHandler(resolvedHandler);
/*     */     } else {
/*     */       
/* 364 */       this.handlerMap.put(urlPath, resolvedHandler);
/* 365 */       if (this.logger.isTraceEnabled()) {
/* 366 */         this.logger.trace("Mapped [" + urlPath + "] onto " + getHandlerDescription(handler));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String getHandlerDescription(Object handler) {
/* 373 */     return (handler instanceof String) ? ("'" + handler + "'") : handler.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Map<String, Object> getHandlerMap() {
/* 384 */     return Collections.unmodifiableMap(this.handlerMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supportsTypeLevelMappings() {
/* 391 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class PathExposingHandlerInterceptor
/*     */     extends HandlerInterceptorAdapter
/*     */   {
/*     */     private final String bestMatchingPattern;
/*     */ 
/*     */     
/*     */     private final String pathWithinMapping;
/*     */ 
/*     */ 
/*     */     
/*     */     public PathExposingHandlerInterceptor(String bestMatchingPattern, String pathWithinMapping) {
/* 407 */       this.bestMatchingPattern = bestMatchingPattern;
/* 408 */       this.pathWithinMapping = pathWithinMapping;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
/* 413 */       AbstractUrlHandlerMapping.this.exposePathWithinMapping(this.bestMatchingPattern, this.pathWithinMapping, request);
/* 414 */       request.setAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE, handler);
/* 415 */       request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING, Boolean.valueOf(AbstractUrlHandlerMapping.this.supportsTypeLevelMappings()));
/* 416 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class UriTemplateVariablesHandlerInterceptor
/*     */     extends HandlerInterceptorAdapter
/*     */   {
/*     */     private final Map<String, String> uriTemplateVariables;
/*     */ 
/*     */ 
/*     */     
/*     */     public UriTemplateVariablesHandlerInterceptor(Map<String, String> uriTemplateVariables) {
/* 431 */       this.uriTemplateVariables = uriTemplateVariables;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
/* 436 */       AbstractUrlHandlerMapping.this.exposeUriTemplateVariables(this.uriTemplateVariables, request);
/* 437 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/AbstractUrlHandlerMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */