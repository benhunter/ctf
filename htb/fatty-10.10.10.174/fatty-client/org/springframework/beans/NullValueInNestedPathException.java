/*    */ package org.springframework.beans;
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
/*    */ public class NullValueInNestedPathException
/*    */   extends InvalidPropertyException
/*    */ {
/*    */   public NullValueInNestedPathException(Class<?> beanClass, String propertyName) {
/* 38 */     super(beanClass, propertyName, "Value of nested property '" + propertyName + "' is null");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NullValueInNestedPathException(Class<?> beanClass, String propertyName, String msg) {
/* 48 */     super(beanClass, propertyName, msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NullValueInNestedPathException(Class<?> beanClass, String propertyName, String msg, Throwable cause) {
/* 60 */     super(beanClass, propertyName, msg, cause);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/NullValueInNestedPathException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */