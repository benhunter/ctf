/*    */ package org.springframework.scripting.support;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.script.Bindings;
/*    */ import javax.script.ScriptEngine;
/*    */ import javax.script.ScriptEngineFactory;
/*    */ import javax.script.ScriptEngineManager;
/*    */ import javax.script.SimpleBindings;
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
/*    */ public abstract class StandardScriptUtils
/*    */ {
/*    */   public static ScriptEngine retrieveEngineByName(ScriptEngineManager scriptEngineManager, String engineName) {
/* 49 */     ScriptEngine engine = scriptEngineManager.getEngineByName(engineName);
/* 50 */     if (engine == null) {
/* 51 */       Set<String> engineNames = new LinkedHashSet<>();
/* 52 */       for (ScriptEngineFactory engineFactory : scriptEngineManager.getEngineFactories()) {
/* 53 */         List<String> factoryNames = engineFactory.getNames();
/* 54 */         if (factoryNames.contains(engineName)) {
/*    */           
/*    */           try {
/*    */ 
/*    */             
/* 59 */             engine = engineFactory.getScriptEngine();
/* 60 */             engine.setBindings(scriptEngineManager.getBindings(), 200);
/*    */           }
/* 62 */           catch (Throwable ex) {
/* 63 */             throw new IllegalStateException("Script engine with name '" + engineName + "' failed to initialize", ex);
/*    */           } 
/*    */         }
/*    */         
/* 67 */         engineNames.addAll(factoryNames);
/*    */       } 
/* 69 */       throw new IllegalArgumentException("Script engine with name '" + engineName + "' not found; registered engine names: " + engineNames);
/*    */     } 
/*    */     
/* 72 */     return engine;
/*    */   }
/*    */   
/*    */   static Bindings getBindings(Map<String, Object> bindings) {
/* 76 */     return (bindings instanceof Bindings) ? (Bindings)bindings : new SimpleBindings(bindings);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/support/StandardScriptUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */