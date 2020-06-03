/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import org.aopalliance.aop.Advice;
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
/*    */ public abstract class AbstractGenericPointcutAdvisor
/*    */   extends AbstractPointcutAdvisor
/*    */ {
/* 33 */   private Advice advice = EMPTY_ADVICE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAdvice(Advice advice) {
/* 40 */     this.advice = advice;
/*    */   }
/*    */ 
/*    */   
/*    */   public Advice getAdvice() {
/* 45 */     return this.advice;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return getClass().getName() + ": advice [" + getAdvice() + "]";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/AbstractGenericPointcutAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */