/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.PointcutAdvisor;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractPointcutAdvisor
/*    */   implements PointcutAdvisor, Ordered, Serializable
/*    */ {
/*    */   @Nullable
/*    */   private Integer order;
/*    */   
/*    */   public void setOrder(int order) {
/* 46 */     this.order = Integer.valueOf(order);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 51 */     if (this.order != null) {
/* 52 */       return this.order.intValue();
/*    */     }
/* 54 */     Advice advice = getAdvice();
/* 55 */     if (advice instanceof Ordered) {
/* 56 */       return ((Ordered)advice).getOrder();
/*    */     }
/* 58 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPerInstance() {
/* 63 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 69 */     if (this == other) {
/* 70 */       return true;
/*    */     }
/* 72 */     if (!(other instanceof PointcutAdvisor)) {
/* 73 */       return false;
/*    */     }
/* 75 */     PointcutAdvisor otherAdvisor = (PointcutAdvisor)other;
/* 76 */     return (ObjectUtils.nullSafeEquals(getAdvice(), otherAdvisor.getAdvice()) && 
/* 77 */       ObjectUtils.nullSafeEquals(getPointcut(), otherAdvisor.getPointcut()));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 82 */     return PointcutAdvisor.class.hashCode();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/AbstractPointcutAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */