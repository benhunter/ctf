/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import org.springframework.expression.EvaluationException;
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
/*    */ class VariableNotAvailableException
/*    */   extends EvaluationException
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public VariableNotAvailableException(String name) {
/* 35 */     super("Variable not available");
/* 36 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 41 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/VariableNotAvailableException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */