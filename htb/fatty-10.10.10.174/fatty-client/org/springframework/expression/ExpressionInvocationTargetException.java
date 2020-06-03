/*    */ package org.springframework.expression;
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
/*    */ public class ExpressionInvocationTargetException
/*    */   extends EvaluationException
/*    */ {
/*    */   public ExpressionInvocationTargetException(int position, String message, Throwable cause) {
/* 32 */     super(position, message, cause);
/*    */   }
/*    */   
/*    */   public ExpressionInvocationTargetException(int position, String message) {
/* 36 */     super(position, message);
/*    */   }
/*    */   
/*    */   public ExpressionInvocationTargetException(String expressionString, String message) {
/* 40 */     super(expressionString, message);
/*    */   }
/*    */   
/*    */   public ExpressionInvocationTargetException(String message, Throwable cause) {
/* 44 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public ExpressionInvocationTargetException(String message) {
/* 48 */     super(message);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/ExpressionInvocationTargetException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */