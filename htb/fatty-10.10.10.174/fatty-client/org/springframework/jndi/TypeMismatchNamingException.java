/*    */ package org.springframework.jndi;
/*    */ 
/*    */ import javax.naming.NamingException;
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
/*    */ public class TypeMismatchNamingException
/*    */   extends NamingException
/*    */ {
/*    */   private final Class<?> requiredType;
/*    */   private final Class<?> actualType;
/*    */   
/*    */   public TypeMismatchNamingException(String jndiName, Class<?> requiredType, Class<?> actualType) {
/* 45 */     super("Object of type [" + actualType + "] available at JNDI location [" + jndiName + "] is not assignable to [" + requiredType
/* 46 */         .getName() + "]");
/* 47 */     this.requiredType = requiredType;
/* 48 */     this.actualType = actualType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Class<?> getRequiredType() {
/* 56 */     return this.requiredType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Class<?> getActualType() {
/* 63 */     return this.actualType;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/TypeMismatchNamingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */