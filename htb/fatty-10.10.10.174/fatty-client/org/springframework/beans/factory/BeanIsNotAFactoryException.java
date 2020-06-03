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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeanIsNotAFactoryException
/*    */   extends BeanNotOfRequiredTypeException
/*    */ {
/*    */   public BeanIsNotAFactoryException(String name, Class<?> actualType) {
/* 38 */     super(name, FactoryBean.class, actualType);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/BeanIsNotAFactoryException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */