/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.HttpRequestHandler;
/*     */ import org.springframework.web.context.request.WebRequestInterceptor;
/*     */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.cors.CorsConfigurationSource;
/*     */ import org.springframework.web.cors.CorsProcessor;
/*     */ import org.springframework.web.cors.CorsUtils;
/*     */ import org.springframework.web.cors.DefaultCorsProcessor;
/*     */ import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
/*     */ import org.springframework.web.servlet.HandlerExecutionChain;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHandlerMapping
/*     */   extends WebApplicationObjectSupport
/*     */   implements HandlerMapping, Ordered, BeanNameAware
/*     */ {
/*     */   @Nullable
/*     */   private Object defaultHandler;
/*  76 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*  78 */   private PathMatcher pathMatcher = (PathMatcher)new AntPathMatcher();
/*     */   
/*  80 */   private final List<Object> interceptors = new ArrayList();
/*     */   
/*  82 */   private final List<HandlerInterceptor> adaptedInterceptors = new ArrayList<>();
/*     */   
/*  84 */   private CorsConfigurationSource corsConfigurationSource = (CorsConfigurationSource)new UrlBasedCorsConfigurationSource();
/*     */   
/*  86 */   private CorsProcessor corsProcessor = (CorsProcessor)new DefaultCorsProcessor();
/*     */   
/*  88 */   private int order = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String beanName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultHandler(@Nullable Object defaultHandler) {
/* 100 */     this.defaultHandler = defaultHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getDefaultHandler() {
/* 109 */     return this.defaultHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
/* 117 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/* 118 */     if (this.corsConfigurationSource instanceof UrlBasedCorsConfigurationSource) {
/* 119 */       ((UrlBasedCorsConfigurationSource)this.corsConfigurationSource).setAlwaysUseFullPath(alwaysUseFullPath);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlDecode(boolean urlDecode) {
/* 128 */     this.urlPathHelper.setUrlDecode(urlDecode);
/* 129 */     if (this.corsConfigurationSource instanceof UrlBasedCorsConfigurationSource) {
/* 130 */       ((UrlBasedCorsConfigurationSource)this.corsConfigurationSource).setUrlDecode(urlDecode);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveSemicolonContent(boolean removeSemicolonContent) {
/* 139 */     this.urlPathHelper.setRemoveSemicolonContent(removeSemicolonContent);
/* 140 */     if (this.corsConfigurationSource instanceof UrlBasedCorsConfigurationSource) {
/* 141 */       ((UrlBasedCorsConfigurationSource)this.corsConfigurationSource).setRemoveSemicolonContent(removeSemicolonContent);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/* 152 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 153 */     this.urlPathHelper = urlPathHelper;
/* 154 */     if (this.corsConfigurationSource instanceof UrlBasedCorsConfigurationSource) {
/* 155 */       ((UrlBasedCorsConfigurationSource)this.corsConfigurationSource).setUrlPathHelper(urlPathHelper);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlPathHelper getUrlPathHelper() {
/* 163 */     return this.urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathMatcher(PathMatcher pathMatcher) {
/* 172 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/* 173 */     this.pathMatcher = pathMatcher;
/* 174 */     if (this.corsConfigurationSource instanceof UrlBasedCorsConfigurationSource) {
/* 175 */       ((UrlBasedCorsConfigurationSource)this.corsConfigurationSource).setPathMatcher(pathMatcher);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatcher getPathMatcher() {
/* 184 */     return this.pathMatcher;
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
/*     */   public void setInterceptors(Object... interceptors) {
/* 198 */     this.interceptors.addAll(Arrays.asList(interceptors));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorsConfigurations(Map<String, CorsConfiguration> corsConfigurations) {
/* 208 */     Assert.notNull(corsConfigurations, "corsConfigurations must not be null");
/* 209 */     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
/* 210 */     source.setCorsConfigurations(corsConfigurations);
/* 211 */     source.setPathMatcher(this.pathMatcher);
/* 212 */     source.setUrlPathHelper(this.urlPathHelper);
/* 213 */     this.corsConfigurationSource = (CorsConfigurationSource)source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorsConfigurationSource(CorsConfigurationSource corsConfigurationSource) {
/* 223 */     Assert.notNull(corsConfigurationSource, "corsConfigurationSource must not be null");
/* 224 */     this.corsConfigurationSource = corsConfigurationSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Map<String, CorsConfiguration> getCorsConfigurations() {
/* 234 */     if (this.corsConfigurationSource instanceof UrlBasedCorsConfigurationSource) {
/* 235 */       return ((UrlBasedCorsConfigurationSource)this.corsConfigurationSource).getCorsConfigurations();
/*     */     }
/*     */     
/* 238 */     throw new IllegalStateException("No CORS configurations available when the source is not an UrlBasedCorsConfigurationSource");
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
/*     */   public void setCorsProcessor(CorsProcessor corsProcessor) {
/* 250 */     Assert.notNull(corsProcessor, "CorsProcessor must not be null");
/* 251 */     this.corsProcessor = corsProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CorsProcessor getCorsProcessor() {
/* 258 */     return this.corsProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/* 267 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 272 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String name) {
/* 277 */     this.beanName = name;
/*     */   }
/*     */   
/*     */   protected String formatMappingName() {
/* 281 */     return (this.beanName != null) ? ("'" + this.beanName + "'") : "<unknown>";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initApplicationContext() throws BeansException {
/* 292 */     extendInterceptors(this.interceptors);
/* 293 */     detectMappedInterceptors(this.adaptedInterceptors);
/* 294 */     initInterceptors();
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
/*     */   protected void extendInterceptors(List<Object> interceptors) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void detectMappedInterceptors(List<HandlerInterceptor> mappedInterceptors) {
/* 317 */     mappedInterceptors.addAll(
/* 318 */         BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)
/* 319 */           obtainApplicationContext(), MappedInterceptor.class, true, false).values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initInterceptors() {
/* 330 */     if (!this.interceptors.isEmpty()) {
/* 331 */       for (int i = 0; i < this.interceptors.size(); i++) {
/* 332 */         Object interceptor = this.interceptors.get(i);
/* 333 */         if (interceptor == null) {
/* 334 */           throw new IllegalArgumentException("Entry number " + i + " in interceptors array is null");
/*     */         }
/* 336 */         this.adaptedInterceptors.add(adaptInterceptor(interceptor));
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
/*     */ 
/*     */   
/*     */   protected HandlerInterceptor adaptInterceptor(Object interceptor) {
/* 354 */     if (interceptor instanceof HandlerInterceptor) {
/* 355 */       return (HandlerInterceptor)interceptor;
/*     */     }
/* 357 */     if (interceptor instanceof WebRequestInterceptor) {
/* 358 */       return (HandlerInterceptor)new WebRequestHandlerInterceptorAdapter((WebRequestInterceptor)interceptor);
/*     */     }
/*     */     
/* 361 */     throw new IllegalArgumentException("Interceptor type not supported: " + interceptor.getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected final HandlerInterceptor[] getAdaptedInterceptors() {
/* 371 */     return !this.adaptedInterceptors.isEmpty() ? this.adaptedInterceptors
/* 372 */       .<HandlerInterceptor>toArray(new HandlerInterceptor[0]) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected final MappedInterceptor[] getMappedInterceptors() {
/* 381 */     List<MappedInterceptor> mappedInterceptors = new ArrayList<>(this.adaptedInterceptors.size());
/* 382 */     for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
/* 383 */       if (interceptor instanceof MappedInterceptor) {
/* 384 */         mappedInterceptors.add((MappedInterceptor)interceptor);
/*     */       }
/*     */     } 
/* 387 */     return !mappedInterceptors.isEmpty() ? mappedInterceptors.<MappedInterceptor>toArray(new MappedInterceptor[0]) : null;
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
/*     */   public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
/* 401 */     Object handler = getHandlerInternal(request);
/* 402 */     if (handler == null) {
/* 403 */       handler = getDefaultHandler();
/*     */     }
/* 405 */     if (handler == null) {
/* 406 */       return null;
/*     */     }
/*     */     
/* 409 */     if (handler instanceof String) {
/* 410 */       String handlerName = (String)handler;
/* 411 */       handler = obtainApplicationContext().getBean(handlerName);
/*     */     } 
/*     */     
/* 414 */     HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);
/*     */     
/* 416 */     if (this.logger.isTraceEnabled()) {
/* 417 */       this.logger.trace("Mapped to " + handler);
/*     */     }
/* 419 */     else if (this.logger.isDebugEnabled() && !request.getDispatcherType().equals(DispatcherType.ASYNC)) {
/* 420 */       this.logger.debug("Mapped to " + executionChain.getHandler());
/*     */     } 
/*     */     
/* 423 */     if (CorsUtils.isCorsRequest(request)) {
/* 424 */       CorsConfiguration globalConfig = this.corsConfigurationSource.getCorsConfiguration(request);
/* 425 */       CorsConfiguration handlerConfig = getCorsConfiguration(handler, request);
/* 426 */       CorsConfiguration config = (globalConfig != null) ? globalConfig.combine(handlerConfig) : handlerConfig;
/* 427 */       executionChain = getCorsHandlerExecutionChain(request, executionChain, config);
/*     */     } 
/*     */     
/* 430 */     return executionChain;
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
/*     */ 
/*     */   
/*     */   protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
/* 473 */     HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain) ? (HandlerExecutionChain)handler : new HandlerExecutionChain(handler);
/*     */ 
/*     */     
/* 476 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 477 */     for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
/* 478 */       if (interceptor instanceof MappedInterceptor) {
/* 479 */         MappedInterceptor mappedInterceptor = (MappedInterceptor)interceptor;
/* 480 */         if (mappedInterceptor.matches(lookupPath, this.pathMatcher)) {
/* 481 */           chain.addInterceptor(mappedInterceptor.getInterceptor());
/*     */         }
/*     */         continue;
/*     */       } 
/* 485 */       chain.addInterceptor(interceptor);
/*     */     } 
/*     */     
/* 488 */     return chain;
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
/*     */   protected CorsConfiguration getCorsConfiguration(Object handler, HttpServletRequest request) {
/* 500 */     Object resolvedHandler = handler;
/* 501 */     if (handler instanceof HandlerExecutionChain) {
/* 502 */       resolvedHandler = ((HandlerExecutionChain)handler).getHandler();
/*     */     }
/* 504 */     if (resolvedHandler instanceof CorsConfigurationSource) {
/* 505 */       return ((CorsConfigurationSource)resolvedHandler).getCorsConfiguration(request);
/*     */     }
/* 507 */     return null;
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
/*     */   protected HandlerExecutionChain getCorsHandlerExecutionChain(HttpServletRequest request, HandlerExecutionChain chain, @Nullable CorsConfiguration config) {
/* 525 */     if (CorsUtils.isPreFlightRequest(request)) {
/* 526 */       HandlerInterceptor[] interceptors = chain.getInterceptors();
/* 527 */       chain = new HandlerExecutionChain(new PreFlightHandler(config), interceptors);
/*     */     } else {
/*     */       
/* 530 */       chain.addInterceptor((HandlerInterceptor)new CorsInterceptor(config));
/*     */     } 
/* 532 */     return chain;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract Object getHandlerInternal(HttpServletRequest paramHttpServletRequest) throws Exception;
/*     */   
/*     */   private class PreFlightHandler
/*     */     implements HttpRequestHandler, CorsConfigurationSource
/*     */   {
/*     */     public PreFlightHandler(CorsConfiguration config) {
/* 542 */       this.config = config;
/*     */     }
/*     */     @Nullable
/*     */     private final CorsConfiguration config;
/*     */     public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
/* 547 */       AbstractHandlerMapping.this.corsProcessor.processRequest(this.config, request, response);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
/* 553 */       return this.config;
/*     */     }
/*     */   }
/*     */   
/*     */   private class CorsInterceptor
/*     */     extends HandlerInterceptorAdapter
/*     */     implements CorsConfigurationSource {
/*     */     @Nullable
/*     */     private final CorsConfiguration config;
/*     */     
/*     */     public CorsInterceptor(CorsConfiguration config) {
/* 564 */       this.config = config;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
/* 571 */       return AbstractHandlerMapping.this.corsProcessor.processRequest(this.config, request, response);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
/* 577 */       return this.config;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/AbstractHandlerMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */