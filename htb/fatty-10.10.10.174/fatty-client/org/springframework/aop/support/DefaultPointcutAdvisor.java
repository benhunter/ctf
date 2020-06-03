/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.Pointcut;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class DefaultPointcutAdvisor
/*    */   extends AbstractGenericPointcutAdvisor
/*    */   implements Serializable
/*    */ {
/* 41 */   private Pointcut pointcut = Pointcut.TRUE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultPointcutAdvisor() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultPointcutAdvisor(Advice advice) {
/* 58 */     this(Pointcut.TRUE, advice);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultPointcutAdvisor(Pointcut pointcut, Advice advice) {
/* 67 */     this.pointcut = pointcut;
/* 68 */     setAdvice(advice);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPointcut(@Nullable Pointcut pointcut) {
/* 78 */     this.pointcut = (pointcut != null) ? pointcut : Pointcut.TRUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 83 */     return this.pointcut;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return getClass().getName() + ": pointcut [" + getPointcut() + "]; advice [" + getAdvice() + "]";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/DefaultPointcutAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */