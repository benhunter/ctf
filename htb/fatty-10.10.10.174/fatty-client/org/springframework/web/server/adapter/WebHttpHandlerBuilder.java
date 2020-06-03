/*     */ package org.springframework.web.server.adapter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.http.codec.ServerCodecConfigurer;
/*     */ import org.springframework.http.server.reactive.HttpHandler;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.server.WebExceptionHandler;
/*     */ import org.springframework.web.server.WebFilter;
/*     */ import org.springframework.web.server.WebHandler;
/*     */ import org.springframework.web.server.handler.ExceptionHandlingWebHandler;
/*     */ import org.springframework.web.server.handler.FilteringWebHandler;
/*     */ import org.springframework.web.server.i18n.LocaleContextResolver;
/*     */ import org.springframework.web.server.session.WebSessionManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WebHttpHandlerBuilder
/*     */ {
/*     */   public static final String WEB_HANDLER_BEAN_NAME = "webHandler";
/*     */   public static final String WEB_SESSION_MANAGER_BEAN_NAME = "webSessionManager";
/*     */   public static final String SERVER_CODEC_CONFIGURER_BEAN_NAME = "serverCodecConfigurer";
/*     */   public static final String LOCALE_CONTEXT_RESOLVER_BEAN_NAME = "localeContextResolver";
/*     */   public static final String FORWARDED_HEADER_TRANSFORMER_BEAN_NAME = "forwardedHeaderTransformer";
/*     */   private final WebHandler webHandler;
/*     */   @Nullable
/*     */   private final ApplicationContext applicationContext;
/*  85 */   private final List<WebFilter> filters = new ArrayList<>();
/*     */   
/*  87 */   private final List<WebExceptionHandler> exceptionHandlers = new ArrayList<>();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private WebSessionManager sessionManager;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ServerCodecConfigurer codecConfigurer;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private LocaleContextResolver localeContextResolver;
/*     */   
/*     */   @Nullable
/*     */   private ForwardedHeaderTransformer forwardedHeaderTransformer;
/*     */ 
/*     */   
/*     */   private WebHttpHandlerBuilder(WebHandler webHandler, @Nullable ApplicationContext applicationContext) {
/* 106 */     Assert.notNull(webHandler, "WebHandler must not be null");
/* 107 */     this.webHandler = webHandler;
/* 108 */     this.applicationContext = applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WebHttpHandlerBuilder(WebHttpHandlerBuilder other) {
/* 115 */     this.webHandler = other.webHandler;
/* 116 */     this.applicationContext = other.applicationContext;
/* 117 */     this.filters.addAll(other.filters);
/* 118 */     this.exceptionHandlers.addAll(other.exceptionHandlers);
/* 119 */     this.sessionManager = other.sessionManager;
/* 120 */     this.codecConfigurer = other.codecConfigurer;
/* 121 */     this.localeContextResolver = other.localeContextResolver;
/* 122 */     this.forwardedHeaderTransformer = other.forwardedHeaderTransformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WebHttpHandlerBuilder webHandler(WebHandler webHandler) {
/* 132 */     return new WebHttpHandlerBuilder(webHandler, null);
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
/*     */   public static WebHttpHandlerBuilder applicationContext(ApplicationContext context) {
/* 157 */     WebHttpHandlerBuilder builder = new WebHttpHandlerBuilder((WebHandler)context.getBean("webHandler", WebHandler.class), context);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     List<WebFilter> webFilters = (List<WebFilter>)context.getBeanProvider(WebFilter.class).orderedStream().collect(Collectors.toList());
/* 163 */     builder.filters(filters -> filters.addAll(webFilters));
/*     */ 
/*     */ 
/*     */     
/* 167 */     List<WebExceptionHandler> exceptionHandlers = (List<WebExceptionHandler>)context.getBeanProvider(WebExceptionHandler.class).orderedStream().collect(Collectors.toList());
/* 168 */     builder.exceptionHandlers(handlers -> handlers.addAll(exceptionHandlers));
/*     */     
/*     */     try {
/* 171 */       builder.sessionManager((WebSessionManager)context
/* 172 */           .getBean("webSessionManager", WebSessionManager.class));
/*     */     }
/* 174 */     catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 179 */       builder.codecConfigurer((ServerCodecConfigurer)context
/* 180 */           .getBean("serverCodecConfigurer", ServerCodecConfigurer.class));
/*     */     }
/* 182 */     catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 187 */       builder.localeContextResolver((LocaleContextResolver)context
/* 188 */           .getBean("localeContextResolver", LocaleContextResolver.class));
/*     */     }
/* 190 */     catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 195 */       builder.localeContextResolver((LocaleContextResolver)context
/* 196 */           .getBean("localeContextResolver", LocaleContextResolver.class));
/*     */     }
/* 198 */     catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 203 */       builder.forwardedHeaderTransformer((ForwardedHeaderTransformer)context
/* 204 */           .getBean("forwardedHeaderTransformer", ForwardedHeaderTransformer.class));
/*     */     }
/* 206 */     catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */ 
/*     */ 
/*     */     
/* 210 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebHttpHandlerBuilder filter(WebFilter... filters) {
/* 219 */     if (!ObjectUtils.isEmpty((Object[])filters)) {
/* 220 */       this.filters.addAll(Arrays.asList(filters));
/* 221 */       updateFilters();
/*     */     } 
/* 223 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebHttpHandlerBuilder filters(Consumer<List<WebFilter>> consumer) {
/* 231 */     consumer.accept(this.filters);
/* 232 */     updateFilters();
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateFilters() {
/* 238 */     if (this.filters.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     List<WebFilter> filtersToUse = (List<WebFilter>)this.filters.stream().peek(filter -> { if (filter instanceof ForwardedHeaderTransformer && this.forwardedHeaderTransformer == null) this.forwardedHeaderTransformer = (ForwardedHeaderTransformer)filter;  }).filter(filter -> !(filter instanceof ForwardedHeaderTransformer)).collect(Collectors.toList());
/*     */     
/* 251 */     this.filters.clear();
/* 252 */     this.filters.addAll(filtersToUse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebHttpHandlerBuilder exceptionHandler(WebExceptionHandler... handlers) {
/* 260 */     if (!ObjectUtils.isEmpty((Object[])handlers)) {
/* 261 */       this.exceptionHandlers.addAll(Arrays.asList(handlers));
/*     */     }
/* 263 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebHttpHandlerBuilder exceptionHandlers(Consumer<List<WebExceptionHandler>> consumer) {
/* 271 */     consumer.accept(this.exceptionHandlers);
/* 272 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebHttpHandlerBuilder sessionManager(WebSessionManager manager) {
/* 283 */     this.sessionManager = manager;
/* 284 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSessionManager() {
/* 293 */     return (this.sessionManager != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebHttpHandlerBuilder codecConfigurer(ServerCodecConfigurer codecConfigurer) {
/* 301 */     this.codecConfigurer = codecConfigurer;
/* 302 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasCodecConfigurer() {
/* 312 */     return (this.codecConfigurer != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebHttpHandlerBuilder localeContextResolver(LocaleContextResolver localeContextResolver) {
/* 321 */     this.localeContextResolver = localeContextResolver;
/* 322 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLocaleContextResolver() {
/* 331 */     return (this.localeContextResolver != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebHttpHandlerBuilder forwardedHeaderTransformer(ForwardedHeaderTransformer transformer) {
/* 341 */     this.forwardedHeaderTransformer = transformer;
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasForwardedHeaderTransformer() {
/* 352 */     return (this.forwardedHeaderTransformer != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHandler build() {
/* 361 */     FilteringWebHandler filteringWebHandler = new FilteringWebHandler(this.webHandler, this.filters);
/* 362 */     ExceptionHandlingWebHandler exceptionHandlingWebHandler = new ExceptionHandlingWebHandler((WebHandler)filteringWebHandler, this.exceptionHandlers);
/*     */     
/* 364 */     HttpWebHandlerAdapter adapted = new HttpWebHandlerAdapter((WebHandler)exceptionHandlingWebHandler);
/* 365 */     if (this.sessionManager != null) {
/* 366 */       adapted.setSessionManager(this.sessionManager);
/*     */     }
/* 368 */     if (this.codecConfigurer != null) {
/* 369 */       adapted.setCodecConfigurer(this.codecConfigurer);
/*     */     }
/* 371 */     if (this.localeContextResolver != null) {
/* 372 */       adapted.setLocaleContextResolver(this.localeContextResolver);
/*     */     }
/* 374 */     if (this.forwardedHeaderTransformer != null) {
/* 375 */       adapted.setForwardedHeaderTransformer(this.forwardedHeaderTransformer);
/*     */     }
/* 377 */     if (this.applicationContext != null) {
/* 378 */       adapted.setApplicationContext(this.applicationContext);
/*     */     }
/* 380 */     adapted.afterPropertiesSet();
/*     */     
/* 382 */     return adapted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebHttpHandlerBuilder clone() {
/* 391 */     return new WebHttpHandlerBuilder(this);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/adapter/WebHttpHandlerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */