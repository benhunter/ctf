/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.HttpRequestHandler;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceHandlerRegistry
/*     */ {
/*     */   private final ServletContext servletContext;
/*     */   private final ApplicationContext applicationContext;
/*     */   @Nullable
/*     */   private final ContentNegotiationManager contentNegotiationManager;
/*     */   @Nullable
/*     */   private final UrlPathHelper pathHelper;
/*  67 */   private final List<ResourceHandlerRegistration> registrations = new ArrayList<>();
/*     */   
/*  69 */   private int order = 2147483646;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceHandlerRegistry(ApplicationContext applicationContext, ServletContext servletContext) {
/*  78 */     this(applicationContext, servletContext, null);
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
/*     */   public ResourceHandlerRegistry(ApplicationContext applicationContext, ServletContext servletContext, @Nullable ContentNegotiationManager contentNegotiationManager) {
/*  91 */     this(applicationContext, servletContext, contentNegotiationManager, null);
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
/*     */   public ResourceHandlerRegistry(ApplicationContext applicationContext, ServletContext servletContext, @Nullable ContentNegotiationManager contentNegotiationManager, @Nullable UrlPathHelper pathHelper) {
/* 103 */     Assert.notNull(applicationContext, "ApplicationContext is required");
/* 104 */     this.applicationContext = applicationContext;
/* 105 */     this.servletContext = servletContext;
/* 106 */     this.contentNegotiationManager = contentNegotiationManager;
/* 107 */     this.pathHelper = pathHelper;
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
/*     */   public ResourceHandlerRegistration addResourceHandler(String... pathPatterns) {
/* 121 */     ResourceHandlerRegistration registration = new ResourceHandlerRegistration(pathPatterns);
/* 122 */     this.registrations.add(registration);
/* 123 */     return registration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMappingForPattern(String pathPattern) {
/* 130 */     for (ResourceHandlerRegistration registration : this.registrations) {
/* 131 */       if (Arrays.<String>asList(registration.getPathPatterns()).contains(pathPattern)) {
/* 132 */         return true;
/*     */       }
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceHandlerRegistry setOrder(int order) {
/* 144 */     this.order = order;
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected AbstractHandlerMapping getHandlerMapping() {
/* 154 */     if (this.registrations.isEmpty()) {
/* 155 */       return null;
/*     */     }
/*     */     
/* 158 */     Map<String, HttpRequestHandler> urlMap = new LinkedHashMap<>();
/* 159 */     for (ResourceHandlerRegistration registration : this.registrations) {
/* 160 */       for (String pathPattern : registration.getPathPatterns()) {
/* 161 */         ResourceHttpRequestHandler handler = registration.getRequestHandler();
/* 162 */         if (this.pathHelper != null) {
/* 163 */           handler.setUrlPathHelper(this.pathHelper);
/*     */         }
/* 165 */         if (this.contentNegotiationManager != null) {
/* 166 */           handler.setContentNegotiationManager(this.contentNegotiationManager);
/*     */         }
/* 168 */         handler.setServletContext(this.servletContext);
/* 169 */         handler.setApplicationContext(this.applicationContext);
/*     */         try {
/* 171 */           handler.afterPropertiesSet();
/*     */         }
/* 173 */         catch (Throwable ex) {
/* 174 */           throw new BeanInitializationException("Failed to init ResourceHttpRequestHandler", ex);
/*     */         } 
/* 176 */         urlMap.put(pathPattern, handler);
/*     */       } 
/*     */     } 
/*     */     
/* 180 */     SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
/* 181 */     handlerMapping.setOrder(this.order);
/* 182 */     handlerMapping.setUrlMap(urlMap);
/* 183 */     return (AbstractHandlerMapping)handlerMapping;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/ResourceHandlerRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */