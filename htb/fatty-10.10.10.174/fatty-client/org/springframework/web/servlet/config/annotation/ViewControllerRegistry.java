/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ViewControllerRegistry
/*     */ {
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*  42 */   private final List<ViewControllerRegistration> registrations = new ArrayList<>(4);
/*     */   
/*  44 */   private final List<RedirectViewControllerRegistration> redirectRegistrations = new ArrayList<>(10);
/*     */   
/*  46 */   private int order = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ViewControllerRegistry(@Nullable ApplicationContext applicationContext) {
/*  54 */     this.applicationContext = applicationContext;
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
/*     */   public ViewControllerRegistration addViewController(String urlPath) {
/*  66 */     ViewControllerRegistration registration = new ViewControllerRegistration(urlPath);
/*  67 */     registration.setApplicationContext(this.applicationContext);
/*  68 */     this.registrations.add(registration);
/*  69 */     return registration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectViewControllerRegistration addRedirectViewController(String urlPath, String redirectUrl) {
/*  79 */     RedirectViewControllerRegistration registration = new RedirectViewControllerRegistration(urlPath, redirectUrl);
/*  80 */     registration.setApplicationContext(this.applicationContext);
/*  81 */     this.redirectRegistrations.add(registration);
/*  82 */     return registration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStatusController(String urlPath, HttpStatus statusCode) {
/*  91 */     ViewControllerRegistration registration = new ViewControllerRegistration(urlPath);
/*  92 */     registration.setApplicationContext(this.applicationContext);
/*  93 */     registration.setStatusCode(statusCode);
/*  94 */     registration.getViewController().setStatusOnly(true);
/*  95 */     this.registrations.add(registration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/* 105 */     this.order = order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SimpleUrlHandlerMapping buildHandlerMapping() {
/* 116 */     if (this.registrations.isEmpty() && this.redirectRegistrations.isEmpty()) {
/* 117 */       return null;
/*     */     }
/*     */     
/* 120 */     Map<String, Object> urlMap = new LinkedHashMap<>();
/* 121 */     for (ViewControllerRegistration registration : this.registrations) {
/* 122 */       urlMap.put(registration.getUrlPath(), registration.getViewController());
/*     */     }
/* 124 */     for (RedirectViewControllerRegistration registration : this.redirectRegistrations) {
/* 125 */       urlMap.put(registration.getUrlPath(), registration.getViewController());
/*     */     }
/*     */     
/* 128 */     SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
/* 129 */     handlerMapping.setUrlMap(urlMap);
/* 130 */     handlerMapping.setOrder(this.order);
/* 131 */     return handlerMapping;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/ViewControllerRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */