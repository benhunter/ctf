/*    */ package org.springframework.beans;
/*    */ 
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
/*    */ public class InvalidPropertyException
/*    */   extends FatalBeanException
/*    */ {
/*    */   private final Class<?> beanClass;
/*    */   private final String propertyName;
/*    */   
/*    */   public InvalidPropertyException(Class<?> beanClass, String propertyName, String msg) {
/* 43 */     this(beanClass, propertyName, msg, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InvalidPropertyException(Class<?> beanClass, String propertyName, String msg, @Nullable Throwable cause) {
/* 54 */     super("Invalid property '" + propertyName + "' of bean class [" + beanClass.getName() + "]: " + msg, cause);
/* 55 */     this.beanClass = beanClass;
/* 56 */     this.propertyName = propertyName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getBeanClass() {
/* 63 */     return this.beanClass;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPropertyName() {
/* 70 */     return this.propertyName;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/InvalidPropertyException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */