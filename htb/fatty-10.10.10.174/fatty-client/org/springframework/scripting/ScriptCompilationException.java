/*    */ package org.springframework.scripting;
/*    */ 
/*    */ import org.springframework.core.NestedRuntimeException;
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
/*    */ public class ScriptCompilationException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   @Nullable
/*    */   private final ScriptSource scriptSource;
/*    */   
/*    */   public ScriptCompilationException(String msg) {
/* 40 */     super(msg);
/* 41 */     this.scriptSource = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScriptCompilationException(String msg, Throwable cause) {
/* 50 */     super(msg, cause);
/* 51 */     this.scriptSource = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScriptCompilationException(ScriptSource scriptSource, String msg) {
/* 61 */     super("Could not compile " + scriptSource + ": " + msg);
/* 62 */     this.scriptSource = scriptSource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScriptCompilationException(ScriptSource scriptSource, Throwable cause) {
/* 71 */     super("Could not compile " + scriptSource, cause);
/* 72 */     this.scriptSource = scriptSource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScriptCompilationException(ScriptSource scriptSource, String msg, Throwable cause) {
/* 82 */     super("Could not compile " + scriptSource + ": " + msg, cause);
/* 83 */     this.scriptSource = scriptSource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ScriptSource getScriptSource() {
/* 93 */     return this.scriptSource;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/ScriptCompilationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */