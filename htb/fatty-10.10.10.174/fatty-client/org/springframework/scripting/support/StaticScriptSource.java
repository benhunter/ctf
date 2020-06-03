/*    */ package org.springframework.scripting.support;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.scripting.ScriptSource;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class StaticScriptSource
/*    */   implements ScriptSource
/*    */ {
/* 35 */   private String script = "";
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean modified;
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   private String className;
/*    */ 
/*    */ 
/*    */   
/*    */   public StaticScriptSource(String script) {
/* 48 */     setScript(script);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StaticScriptSource(String script, @Nullable String className) {
/* 58 */     setScript(script);
/* 59 */     this.className = className;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void setScript(String script) {
/* 67 */     Assert.hasText(script, "Script must not be empty");
/* 68 */     this.modified = !script.equals(this.script);
/* 69 */     this.script = script;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized String getScriptAsString() {
/* 75 */     this.modified = false;
/* 76 */     return this.script;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized boolean isModified() {
/* 81 */     return this.modified;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String suggestedClassName() {
/* 87 */     return this.className;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 93 */     return "static script" + ((this.className != null) ? (" [" + this.className + "]") : "");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/support/StaticScriptSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */