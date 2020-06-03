/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.PointcutAdvisor;
/*     */ import org.springframework.core.Ordered;
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
/*     */ public class AspectJPointcutAdvisor
/*     */   implements PointcutAdvisor, Ordered
/*     */ {
/*     */   private final AbstractAspectJAdvice advice;
/*     */   private final Pointcut pointcut;
/*     */   @Nullable
/*     */   private Integer order;
/*     */   
/*     */   public AspectJPointcutAdvisor(AbstractAspectJAdvice advice) {
/*  50 */     Assert.notNull(advice, "Advice must not be null");
/*  51 */     this.advice = advice;
/*  52 */     this.pointcut = advice.buildSafePointcut();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/*  57 */     this.order = Integer.valueOf(order);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  62 */     if (this.order != null) {
/*  63 */       return this.order.intValue();
/*     */     }
/*     */     
/*  66 */     return this.advice.getOrder();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPerInstance() {
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Advice getAdvice() {
/*  77 */     return this.advice;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pointcut getPointcut() {
/*  82 */     return this.pointcut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAspectName() {
/*  91 */     return this.advice.getAspectName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  97 */     if (this == other) {
/*  98 */       return true;
/*     */     }
/* 100 */     if (!(other instanceof AspectJPointcutAdvisor)) {
/* 101 */       return false;
/*     */     }
/* 103 */     AspectJPointcutAdvisor otherAdvisor = (AspectJPointcutAdvisor)other;
/* 104 */     return this.advice.equals(otherAdvisor.advice);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 109 */     return AspectJPointcutAdvisor.class.hashCode() * 29 + this.advice.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJPointcutAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */