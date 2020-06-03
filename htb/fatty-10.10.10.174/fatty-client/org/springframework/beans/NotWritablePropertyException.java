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
/*    */ public class NotWritablePropertyException
/*    */   extends InvalidPropertyException
/*    */ {
/*    */   @Nullable
/*    */   private final String[] possibleMatches;
/*    */   
/*    */   public NotWritablePropertyException(Class<?> beanClass, String propertyName) {
/* 42 */     super(beanClass, propertyName, "Bean property '" + propertyName + "' is not writable or has an invalid setter method: Does the return type of the getter match the parameter type of the setter?");
/*    */ 
/*    */     
/* 45 */     this.possibleMatches = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NotWritablePropertyException(Class<?> beanClass, String propertyName, String msg) {
/* 55 */     super(beanClass, propertyName, msg);
/* 56 */     this.possibleMatches = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NotWritablePropertyException(Class<?> beanClass, String propertyName, String msg, Throwable cause) {
/* 67 */     super(beanClass, propertyName, msg, cause);
/* 68 */     this.possibleMatches = null;
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
/*    */   public NotWritablePropertyException(Class<?> beanClass, String propertyName, String msg, String[] possibleMatches) {
/* 80 */     super(beanClass, propertyName, msg);
/* 81 */     this.possibleMatches = possibleMatches;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String[] getPossibleMatches() {
/* 91 */     return this.possibleMatches;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/NotWritablePropertyException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */