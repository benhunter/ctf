/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.beans.factory.config.Scope;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletContextScope
/*     */   implements Scope, DisposableBean
/*     */ {
/*     */   private final ServletContext servletContext;
/*  53 */   private final Map<String, Runnable> destructionCallbacks = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletContextScope(ServletContext servletContext) {
/*  61 */     Assert.notNull(servletContext, "ServletContext must not be null");
/*  62 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(String name, ObjectFactory<?> objectFactory) {
/*  68 */     Object scopedObject = this.servletContext.getAttribute(name);
/*  69 */     if (scopedObject == null) {
/*  70 */       scopedObject = objectFactory.getObject();
/*  71 */       this.servletContext.setAttribute(name, scopedObject);
/*     */     } 
/*  73 */     return scopedObject;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object remove(String name) {
/*  79 */     Object scopedObject = this.servletContext.getAttribute(name);
/*  80 */     if (scopedObject != null) {
/*  81 */       synchronized (this.destructionCallbacks) {
/*  82 */         this.destructionCallbacks.remove(name);
/*     */       } 
/*  84 */       this.servletContext.removeAttribute(name);
/*  85 */       return scopedObject;
/*     */     } 
/*     */     
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDestructionCallback(String name, Runnable callback) {
/*  94 */     synchronized (this.destructionCallbacks) {
/*  95 */       this.destructionCallbacks.put(name, callback);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object resolveContextualObject(String key) {
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getConversationId() {
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 119 */     synchronized (this.destructionCallbacks) {
/* 120 */       for (Runnable runnable : this.destructionCallbacks.values()) {
/* 121 */         runnable.run();
/*     */       }
/* 123 */       this.destructionCallbacks.clear();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/ServletContextScope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */