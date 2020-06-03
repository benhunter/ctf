/*    */ package org.springframework.scripting.support;
/*    */ 
/*    */ import javax.script.ScriptException;
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
/*    */ 
/*    */ 
/*    */ public class StandardScriptEvalException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final ScriptException scriptException;
/*    */   
/*    */   public StandardScriptEvalException(ScriptException ex) {
/* 46 */     super(ex.getMessage());
/* 47 */     this.scriptException = ex;
/*    */   }
/*    */ 
/*    */   
/*    */   public final ScriptException getScriptException() {
/* 52 */     return this.scriptException;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized Throwable fillInStackTrace() {
/* 57 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/support/StandardScriptEvalException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */