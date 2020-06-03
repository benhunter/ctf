/*    */ package org.springframework.scripting.bsh;
/*    */ 
/*    */ import bsh.EvalError;
/*    */ import bsh.Interpreter;
/*    */ import java.io.IOException;
/*    */ import java.io.StringReader;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.scripting.ScriptCompilationException;
/*    */ import org.springframework.scripting.ScriptEvaluator;
/*    */ import org.springframework.scripting.ScriptSource;
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
/*    */ public class BshScriptEvaluator
/*    */   implements ScriptEvaluator, BeanClassLoaderAware
/*    */ {
/*    */   @Nullable
/*    */   private ClassLoader classLoader;
/*    */   
/*    */   public BshScriptEvaluator() {}
/*    */   
/*    */   public BshScriptEvaluator(ClassLoader classLoader) {
/* 56 */     this.classLoader = classLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 62 */     this.classLoader = classLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object evaluate(ScriptSource script) {
/* 69 */     return evaluate(script, null);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object evaluate(ScriptSource script, @Nullable Map<String, Object> arguments) {
/*    */     try {
/* 76 */       Interpreter interpreter = new Interpreter();
/* 77 */       interpreter.setClassLoader(this.classLoader);
/* 78 */       if (arguments != null) {
/* 79 */         for (Map.Entry<String, Object> entry : arguments.entrySet()) {
/* 80 */           interpreter.set(entry.getKey(), entry.getValue());
/*    */         }
/*    */       }
/* 83 */       return interpreter.eval(new StringReader(script.getScriptAsString()));
/*    */     }
/* 85 */     catch (IOException ex) {
/* 86 */       throw new ScriptCompilationException(script, "Cannot access BeanShell script", ex);
/*    */     }
/* 88 */     catch (EvalError ex) {
/* 89 */       throw new ScriptCompilationException(script, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/bsh/BshScriptEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */