/*    */ package org.springframework.aop.support;
/*    */ 
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
/*    */ 
/*    */ public class DefaultBeanFactoryPointcutAdvisor
/*    */   extends AbstractBeanFactoryPointcutAdvisor
/*    */ {
/* 39 */   private Pointcut pointcut = Pointcut.TRUE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPointcut(@Nullable Pointcut pointcut) {
/* 48 */     this.pointcut = (pointcut != null) ? pointcut : Pointcut.TRUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 53 */     return this.pointcut;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return getClass().getName() + ": pointcut [" + getPointcut() + "]; advice bean '" + getAdviceBeanName() + "'";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/DefaultBeanFactoryPointcutAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */