/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public class ProxyCreatorSupport
/*     */   extends AdvisedSupport
/*     */ {
/*     */   private AopProxyFactory aopProxyFactory;
/*  37 */   private final List<AdvisedSupportListener> listeners = new LinkedList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean active = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyCreatorSupport() {
/*  47 */     this.aopProxyFactory = new DefaultAopProxyFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyCreatorSupport(AopProxyFactory aopProxyFactory) {
/*  55 */     Assert.notNull(aopProxyFactory, "AopProxyFactory must not be null");
/*  56 */     this.aopProxyFactory = aopProxyFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
/*  67 */     Assert.notNull(aopProxyFactory, "AopProxyFactory must not be null");
/*  68 */     this.aopProxyFactory = aopProxyFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AopProxyFactory getAopProxyFactory() {
/*  75 */     return this.aopProxyFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(AdvisedSupportListener listener) {
/*  83 */     Assert.notNull(listener, "AdvisedSupportListener must not be null");
/*  84 */     this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeListener(AdvisedSupportListener listener) {
/*  92 */     Assert.notNull(listener, "AdvisedSupportListener must not be null");
/*  93 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized AopProxy createAopProxy() {
/* 102 */     if (!this.active) {
/* 103 */       activate();
/*     */     }
/* 105 */     return getAopProxyFactory().createAopProxy(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void activate() {
/* 113 */     this.active = true;
/* 114 */     for (AdvisedSupportListener listener : this.listeners) {
/* 115 */       listener.activated(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void adviceChanged() {
/* 125 */     super.adviceChanged();
/* 126 */     synchronized (this) {
/* 127 */       if (this.active) {
/* 128 */         for (AdvisedSupportListener listener : this.listeners) {
/* 129 */           listener.adviceChanged(this);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized boolean isActive() {
/* 139 */     return this.active;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/ProxyCreatorSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */