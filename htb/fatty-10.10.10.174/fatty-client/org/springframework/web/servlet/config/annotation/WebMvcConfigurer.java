/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface WebMvcConfigurer
/*     */ {
/*     */   default void configurePathMatch(PathMatchConfigurer configurer) {}
/*     */   
/*     */   default void configureContentNegotiation(ContentNegotiationConfigurer configurer) {}
/*     */   
/*     */   default void configureAsyncSupport(AsyncSupportConfigurer configurer) {}
/*     */   
/*     */   default void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {}
/*     */   
/*     */   default void addFormatters(FormatterRegistry registry) {}
/*     */   
/*     */   default void addInterceptors(InterceptorRegistry registry) {}
/*     */   
/*     */   default void addResourceHandlers(ResourceHandlerRegistry registry) {}
/*     */   
/*     */   default void addCorsMappings(CorsRegistry registry) {}
/*     */   
/*     */   default void addViewControllers(ViewControllerRegistry registry) {}
/*     */   
/*     */   default void configureViewResolvers(ViewResolverRegistry registry) {}
/*     */   
/*     */   default void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {}
/*     */   
/*     */   default void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {}
/*     */   
/*     */   default void configureMessageConverters(List<HttpMessageConverter<?>> converters) {}
/*     */   
/*     */   default void extendMessageConverters(List<HttpMessageConverter<?>> converters) {}
/*     */   
/*     */   default void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {}
/*     */   
/*     */   default void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {}
/*     */   
/*     */   @Nullable
/*     */   default Validator getValidator() {
/* 220 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default MessageCodesResolver getMessageCodesResolver() {
/* 230 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/WebMvcConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */