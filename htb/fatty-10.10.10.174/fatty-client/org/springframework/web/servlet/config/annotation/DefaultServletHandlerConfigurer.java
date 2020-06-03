/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultServletHandlerConfigurer
/*     */ {
/*     */   private final ServletContext servletContext;
/*     */   @Nullable
/*     */   private DefaultServletHttpRequestHandler handler;
/*     */   
/*     */   public DefaultServletHandlerConfigurer(ServletContext servletContext) {
/*  56 */     Assert.notNull(servletContext, "ServletContext is required");
/*  57 */     this.servletContext = servletContext;
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
/*     */   public void enable() {
/*  69 */     enable(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enable(@Nullable String defaultServletName) {
/*  79 */     this.handler = new DefaultServletHttpRequestHandler();
/*  80 */     if (defaultServletName != null) {
/*  81 */       this.handler.setDefaultServletName(defaultServletName);
/*     */     }
/*  83 */     this.handler.setServletContext(this.servletContext);
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
/*     */   protected SimpleUrlHandlerMapping buildHandlerMapping() {
/*  95 */     if (this.handler == null) {
/*  96 */       return null;
/*     */     }
/*     */     
/*  99 */     SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
/* 100 */     handlerMapping.setUrlMap(Collections.singletonMap("/**", this.handler));
/* 101 */     handlerMapping.setOrder(2147483647);
/* 102 */     return handlerMapping;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/DefaultServletHandlerConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */