/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.PropertyChangeEvent;
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
/*    */ public class MethodInvocationException
/*    */   extends PropertyAccessException
/*    */ {
/*    */   public static final String ERROR_CODE = "methodInvocation";
/*    */   
/*    */   public MethodInvocationException(PropertyChangeEvent propertyChangeEvent, Throwable cause) {
/* 42 */     super(propertyChangeEvent, "Property '" + propertyChangeEvent.getPropertyName() + "' threw exception", cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getErrorCode() {
/* 47 */     return "methodInvocation";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/MethodInvocationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */