/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.validation.MessageCodesResolver;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ public class DelegatingWebMvcConfiguration
/*     */   extends WebMvcConfigurationSupport
/*     */ {
/*  45 */   private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();
/*     */ 
/*     */   
/*     */   @Autowired(required = false)
/*     */   public void setConfigurers(List<WebMvcConfigurer> configurers) {
/*  50 */     if (!CollectionUtils.isEmpty(configurers)) {
/*  51 */       this.configurers.addWebMvcConfigurers(configurers);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void configurePathMatch(PathMatchConfigurer configurer) {
/*  58 */     this.configurers.configurePathMatch(configurer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
/*  63 */     this.configurers.configureContentNegotiation(configurer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
/*  68 */     this.configurers.configureAsyncSupport(configurer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
/*  73 */     this.configurers.configureDefaultServletHandling(configurer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addFormatters(FormatterRegistry registry) {
/*  78 */     this.configurers.addFormatters(registry);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addInterceptors(InterceptorRegistry registry) {
/*  83 */     this.configurers.addInterceptors(registry);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addResourceHandlers(ResourceHandlerRegistry registry) {
/*  88 */     this.configurers.addResourceHandlers(registry);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addCorsMappings(CorsRegistry registry) {
/*  93 */     this.configurers.addCorsMappings(registry);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addViewControllers(ViewControllerRegistry registry) {
/*  98 */     this.configurers.addViewControllers(registry);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configureViewResolvers(ViewResolverRegistry registry) {
/* 103 */     this.configurers.configureViewResolvers(registry);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
/* 108 */     this.configurers.addArgumentResolvers(argumentResolvers);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
/* 113 */     this.configurers.addReturnValueHandlers(returnValueHandlers);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
/* 118 */     this.configurers.configureMessageConverters(converters);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
/* 123 */     this.configurers.extendMessageConverters(converters);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
/* 128 */     this.configurers.configureHandlerExceptionResolvers(exceptionResolvers);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
/* 133 */     this.configurers.extendHandlerExceptionResolvers(exceptionResolvers);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Validator getValidator() {
/* 139 */     return this.configurers.getValidator();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MessageCodesResolver getMessageCodesResolver() {
/* 145 */     return this.configurers.getMessageCodesResolver();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/DelegatingWebMvcConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */