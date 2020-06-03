/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public abstract class AbstractExpressionPointcut
/*    */   implements ExpressionPointcut, Serializable
/*    */ {
/*    */   @Nullable
/*    */   private String location;
/*    */   @Nullable
/*    */   private String expression;
/*    */   
/*    */   public void setLocation(@Nullable String location) {
/* 47 */     this.location = location;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getLocation() {
/* 58 */     return this.location;
/*    */   }
/*    */   
/*    */   public void setExpression(@Nullable String expression) {
/* 62 */     this.expression = expression;
/*    */     try {
/* 64 */       onSetExpression(expression);
/*    */     }
/* 66 */     catch (IllegalArgumentException ex) {
/*    */       
/* 68 */       if (this.location != null) {
/* 69 */         throw new IllegalArgumentException("Invalid expression at location [" + this.location + "]: " + ex);
/*    */       }
/*    */       
/* 72 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onSetExpression(@Nullable String expression) throws IllegalArgumentException {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getExpression() {
/* 94 */     return this.expression;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/AbstractExpressionPointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */