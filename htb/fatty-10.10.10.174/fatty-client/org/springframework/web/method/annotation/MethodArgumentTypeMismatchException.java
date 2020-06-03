/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import org.springframework.beans.TypeMismatchException;
/*    */ import org.springframework.core.MethodParameter;
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
/*    */ public class MethodArgumentTypeMismatchException
/*    */   extends TypeMismatchException
/*    */ {
/*    */   private final String name;
/*    */   private final MethodParameter parameter;
/*    */   
/*    */   public MethodArgumentTypeMismatchException(@Nullable Object value, @Nullable Class<?> requiredType, String name, MethodParameter param, Throwable cause) {
/* 42 */     super(value, requiredType, cause);
/* 43 */     this.name = name;
/* 44 */     this.parameter = param;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 52 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodParameter getParameter() {
/* 59 */     return this.parameter;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/MethodArgumentTypeMismatchException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */