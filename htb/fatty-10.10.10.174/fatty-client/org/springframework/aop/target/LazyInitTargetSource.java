/*    */ package org.springframework.aop.target;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
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
/*    */ public class LazyInitTargetSource
/*    */   extends AbstractBeanFactoryBasedTargetSource
/*    */ {
/*    */   @Nullable
/*    */   private Object target;
/*    */   
/*    */   @Nullable
/*    */   public synchronized Object getTarget() throws BeansException {
/* 70 */     if (this.target == null) {
/* 71 */       this.target = getBeanFactory().getBean(getTargetBeanName());
/* 72 */       postProcessTargetObject(this.target);
/*    */     } 
/* 74 */     return this.target;
/*    */   }
/*    */   
/*    */   protected void postProcessTargetObject(Object targetObject) {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/LazyInitTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */