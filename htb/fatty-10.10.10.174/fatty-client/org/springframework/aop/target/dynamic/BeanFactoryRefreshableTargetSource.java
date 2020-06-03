/*    */ package org.springframework.aop.target.dynamic;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanFactory;
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
/*    */ public class BeanFactoryRefreshableTargetSource
/*    */   extends AbstractRefreshableTargetSource
/*    */ {
/*    */   private final BeanFactory beanFactory;
/*    */   private final String beanName;
/*    */   
/*    */   public BeanFactoryRefreshableTargetSource(BeanFactory beanFactory, String beanName) {
/* 54 */     Assert.notNull(beanFactory, "BeanFactory is required");
/* 55 */     Assert.notNull(beanName, "Bean name is required");
/* 56 */     this.beanFactory = beanFactory;
/* 57 */     this.beanName = beanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final Object freshTarget() {
/* 66 */     return obtainFreshBean(this.beanFactory, this.beanName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object obtainFreshBean(BeanFactory beanFactory, String beanName) {
/* 77 */     return beanFactory.getBean(beanName);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/dynamic/BeanFactoryRefreshableTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */