/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.aop.DynamicIntroductionAdvice;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.support.DefaultIntroductionAdvisor;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadLocalTargetSource
/*     */   extends AbstractPrototypeBasedTargetSource
/*     */   implements ThreadLocalTargetSourceStats, DisposableBean
/*     */ {
/*  60 */   private final ThreadLocal<Object> targetInThread = (ThreadLocal<Object>)new NamedThreadLocal("Thread-local instance of bean '" + 
/*  61 */       getTargetBeanName() + "'");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private final Set<Object> targetSet = new HashSet();
/*     */ 
/*     */ 
/*     */   
/*     */   private int invocationCount;
/*     */ 
/*     */ 
/*     */   
/*     */   private int hitCount;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTarget() throws BeansException {
/*  80 */     this.invocationCount++;
/*  81 */     Object target = this.targetInThread.get();
/*  82 */     if (target == null) {
/*  83 */       if (this.logger.isDebugEnabled()) {
/*  84 */         this.logger.debug("No target for prototype '" + getTargetBeanName() + "' bound to thread: creating one and binding it to thread '" + 
/*  85 */             Thread.currentThread().getName() + "'");
/*     */       }
/*     */       
/*  88 */       target = newPrototypeInstance();
/*  89 */       this.targetInThread.set(target);
/*  90 */       synchronized (this.targetSet) {
/*  91 */         this.targetSet.add(target);
/*     */       } 
/*     */     } else {
/*     */       
/*  95 */       this.hitCount++;
/*     */     } 
/*  97 */     return target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 106 */     this.logger.debug("Destroying ThreadLocalTargetSource bindings");
/* 107 */     synchronized (this.targetSet) {
/* 108 */       for (Object target : this.targetSet) {
/* 109 */         destroyPrototypeInstance(target);
/*     */       }
/* 111 */       this.targetSet.clear();
/*     */     } 
/*     */     
/* 114 */     this.targetInThread.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInvocationCount() {
/* 120 */     return this.invocationCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHitCount() {
/* 125 */     return this.hitCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getObjectCount() {
/* 130 */     synchronized (this.targetSet) {
/* 131 */       return this.targetSet.size();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntroductionAdvisor getStatsMixin() {
/* 141 */     DelegatingIntroductionInterceptor dii = new DelegatingIntroductionInterceptor(this);
/* 142 */     return (IntroductionAdvisor)new DefaultIntroductionAdvisor((DynamicIntroductionAdvice)dii, ThreadLocalTargetSourceStats.class);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/ThreadLocalTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */