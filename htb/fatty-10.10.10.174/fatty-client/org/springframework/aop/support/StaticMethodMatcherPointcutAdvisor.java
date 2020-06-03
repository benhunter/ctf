/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.Pointcut;
/*    */ import org.springframework.aop.PointcutAdvisor;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class StaticMethodMatcherPointcutAdvisor
/*    */   extends StaticMethodMatcherPointcut
/*    */   implements PointcutAdvisor, Ordered, Serializable
/*    */ {
/* 39 */   private Advice advice = EMPTY_ADVICE;
/*    */   
/* 41 */   private int order = Integer.MAX_VALUE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StaticMethodMatcherPointcutAdvisor() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StaticMethodMatcherPointcutAdvisor(Advice advice) {
/* 57 */     Assert.notNull(advice, "Advice must not be null");
/* 58 */     this.advice = advice;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOrder(int order) {
/* 63 */     this.order = order;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 68 */     return this.order;
/*    */   }
/*    */   
/*    */   public void setAdvice(Advice advice) {
/* 72 */     this.advice = advice;
/*    */   }
/*    */ 
/*    */   
/*    */   public Advice getAdvice() {
/* 77 */     return this.advice;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPerInstance() {
/* 82 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 87 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/StaticMethodMatcherPointcutAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */