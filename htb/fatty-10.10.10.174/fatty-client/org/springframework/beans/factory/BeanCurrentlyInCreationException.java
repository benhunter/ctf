/*    */ package org.springframework.beans.factory;
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
/*    */ public class BeanCurrentlyInCreationException
/*    */   extends BeanCreationException
/*    */ {
/*    */   public BeanCurrentlyInCreationException(String beanName) {
/* 35 */     super(beanName, "Requested bean is currently in creation: Is there an unresolvable circular reference?");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BeanCurrentlyInCreationException(String beanName, String msg) {
/* 45 */     super(beanName, msg);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/BeanCurrentlyInCreationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */