/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import org.springframework.aop.Pointcut;
/*    */ import org.springframework.aop.support.AbstractGenericPointcutAdvisor;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
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
/*    */ public class AspectJExpressionPointcutAdvisor
/*    */   extends AbstractGenericPointcutAdvisor
/*    */   implements BeanFactoryAware
/*    */ {
/* 34 */   private final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
/*    */ 
/*    */   
/*    */   public void setExpression(@Nullable String expression) {
/* 38 */     this.pointcut.setExpression(expression);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public String getExpression() {
/* 43 */     return this.pointcut.getExpression();
/*    */   }
/*    */   
/*    */   public void setLocation(@Nullable String location) {
/* 47 */     this.pointcut.setLocation(location);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public String getLocation() {
/* 52 */     return this.pointcut.getLocation();
/*    */   }
/*    */   
/*    */   public void setParameterNames(String... names) {
/* 56 */     this.pointcut.setParameterNames(names);
/*    */   }
/*    */   
/*    */   public void setParameterTypes(Class<?>... types) {
/* 60 */     this.pointcut.setParameterTypes(types);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory) {
/* 65 */     this.pointcut.setBeanFactory(beanFactory);
/*    */   }
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 70 */     return (Pointcut)this.pointcut;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJExpressionPointcutAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */