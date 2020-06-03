/*    */ package org.springframework.aop.target;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
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
/*    */ public class PrototypeTargetSource
/*    */   extends AbstractPrototypeBasedTargetSource
/*    */ {
/*    */   public Object getTarget() throws BeansException {
/* 43 */     return newPrototypeInstance();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void releaseTarget(Object target) {
/* 52 */     destroyPrototypeInstance(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "PrototypeTargetSource for target bean with name '" + getTargetBeanName() + "'";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/PrototypeTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */