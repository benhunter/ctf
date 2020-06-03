/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ class WebMvcConfigurerComposite
/*     */   implements WebMvcConfigurer
/*     */ {
/*  40 */   private final List<WebMvcConfigurer> delegates = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public void addWebMvcConfigurers(List<WebMvcConfigurer> configurers) {
/*  44 */     if (!CollectionUtils.isEmpty(configurers)) {
/*  45 */       this.delegates.addAll(configurers);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void configurePathMatch(PathMatchConfigurer configurer) {
/*  52 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  53 */       delegate.configurePathMatch(configurer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
/*  59 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  60 */       delegate.configureContentNegotiation(configurer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
/*  66 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  67 */       delegate.configureAsyncSupport(configurer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
/*  73 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  74 */       delegate.configureDefaultServletHandling(configurer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFormatters(FormatterRegistry registry) {
/*  80 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  81 */       delegate.addFormatters(registry);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInterceptors(InterceptorRegistry registry) {
/*  87 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  88 */       delegate.addInterceptors(registry);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addResourceHandlers(ResourceHandlerRegistry registry) {
/*  94 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  95 */       delegate.addResourceHandlers(registry);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCorsMappings(CorsRegistry registry) {
/* 101 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 102 */       delegate.addCorsMappings(registry);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addViewControllers(ViewControllerRegistry registry) {
/* 108 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 109 */       delegate.addViewControllers(registry);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void configureViewResolvers(ViewResolverRegistry registry) {
/* 115 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 116 */       delegate.configureViewResolvers(registry);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
/* 122 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 123 */       delegate.addArgumentResolvers(argumentResolvers);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
/* 129 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 130 */       delegate.addReturnValueHandlers(returnValueHandlers);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
/* 136 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 137 */       delegate.configureMessageConverters(converters);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
/* 143 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 144 */       delegate.extendMessageConverters(converters);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
/* 150 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 151 */       delegate.configureHandlerExceptionResolvers(exceptionResolvers);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
/* 157 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 158 */       delegate.extendHandlerExceptionResolvers(exceptionResolvers);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Validator getValidator() {
/* 164 */     Validator selected = null;
/* 165 */     for (WebMvcConfigurer configurer : this.delegates) {
/* 166 */       Validator validator = configurer.getValidator();
/* 167 */       if (validator != null) {
/* 168 */         if (selected != null) {
/* 169 */           throw new IllegalStateException("No unique Validator found: {" + selected + ", " + validator + "}");
/*     */         }
/*     */         
/* 172 */         selected = validator;
/*     */       } 
/*     */     } 
/* 175 */     return selected;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MessageCodesResolver getMessageCodesResolver() {
/* 181 */     MessageCodesResolver selected = null;
/* 182 */     for (WebMvcConfigurer configurer : this.delegates) {
/* 183 */       MessageCodesResolver messageCodesResolver = configurer.getMessageCodesResolver();
/* 184 */       if (messageCodesResolver != null) {
/* 185 */         if (selected != null) {
/* 186 */           throw new IllegalStateException("No unique MessageCodesResolver found: {" + selected + ", " + messageCodesResolver + "}");
/*     */         }
/*     */         
/* 189 */         selected = messageCodesResolver;
/*     */       } 
/*     */     } 
/* 192 */     return selected;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/WebMvcConfigurerComposite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */