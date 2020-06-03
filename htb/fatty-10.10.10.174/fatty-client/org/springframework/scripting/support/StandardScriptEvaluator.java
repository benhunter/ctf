/*     */ package org.springframework.scripting.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import javax.script.Bindings;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import javax.script.ScriptException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptEvaluator;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardScriptEvaluator
/*     */   implements ScriptEvaluator, BeanClassLoaderAware
/*     */ {
/*     */   @Nullable
/*     */   private String engineName;
/*     */   @Nullable
/*     */   private volatile Bindings globalBindings;
/*     */   @Nullable
/*     */   private volatile ScriptEngineManager scriptEngineManager;
/*     */   
/*     */   public StandardScriptEvaluator() {}
/*     */   
/*     */   public StandardScriptEvaluator(ClassLoader classLoader) {
/*  67 */     this.scriptEngineManager = new ScriptEngineManager(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardScriptEvaluator(ScriptEngineManager scriptEngineManager) {
/*  77 */     this.scriptEngineManager = scriptEngineManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLanguage(String language) {
/*  89 */     this.engineName = language;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEngineName(String engineName) {
/*  99 */     this.engineName = engineName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGlobalBindings(Map<String, Object> globalBindings) {
/* 111 */     Bindings bindings = StandardScriptUtils.getBindings(globalBindings);
/* 112 */     this.globalBindings = bindings;
/* 113 */     ScriptEngineManager scriptEngineManager = this.scriptEngineManager;
/* 114 */     if (scriptEngineManager != null) {
/* 115 */       scriptEngineManager.setBindings(bindings);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 121 */     ScriptEngineManager scriptEngineManager = this.scriptEngineManager;
/* 122 */     if (scriptEngineManager == null) {
/* 123 */       scriptEngineManager = new ScriptEngineManager(classLoader);
/* 124 */       this.scriptEngineManager = scriptEngineManager;
/* 125 */       Bindings bindings = this.globalBindings;
/* 126 */       if (bindings != null) {
/* 127 */         scriptEngineManager.setBindings(bindings);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object evaluate(ScriptSource script) {
/* 136 */     return evaluate(script, null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object evaluate(ScriptSource script, @Nullable Map<String, Object> argumentBindings) {
/* 142 */     ScriptEngine engine = getScriptEngine(script);
/*     */     try {
/* 144 */       if (CollectionUtils.isEmpty(argumentBindings)) {
/* 145 */         return engine.eval(script.getScriptAsString());
/*     */       }
/*     */       
/* 148 */       Bindings bindings = StandardScriptUtils.getBindings(argumentBindings);
/* 149 */       return engine.eval(script.getScriptAsString(), bindings);
/*     */     
/*     */     }
/* 152 */     catch (IOException ex) {
/* 153 */       throw new ScriptCompilationException(script, "Cannot access script for ScriptEngine", ex);
/*     */     }
/* 155 */     catch (ScriptException ex) {
/* 156 */       throw new ScriptCompilationException(script, new StandardScriptEvalException(ex));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScriptEngine getScriptEngine(ScriptSource script) {
/* 166 */     ScriptEngineManager scriptEngineManager = this.scriptEngineManager;
/* 167 */     if (scriptEngineManager == null) {
/* 168 */       scriptEngineManager = new ScriptEngineManager();
/* 169 */       this.scriptEngineManager = scriptEngineManager;
/*     */     } 
/*     */     
/* 172 */     if (StringUtils.hasText(this.engineName)) {
/* 173 */       return StandardScriptUtils.retrieveEngineByName(scriptEngineManager, this.engineName);
/*     */     }
/* 175 */     if (script instanceof ResourceScriptSource) {
/* 176 */       Resource resource = ((ResourceScriptSource)script).getResource();
/* 177 */       String extension = StringUtils.getFilenameExtension(resource.getFilename());
/* 178 */       if (extension == null) {
/* 179 */         throw new IllegalStateException("No script language defined, and no file extension defined for resource: " + resource);
/*     */       }
/*     */       
/* 182 */       ScriptEngine engine = scriptEngineManager.getEngineByExtension(extension);
/* 183 */       if (engine == null) {
/* 184 */         throw new IllegalStateException("No matching engine found for file extension '" + extension + "'");
/*     */       }
/* 186 */       return engine;
/*     */     } 
/*     */     
/* 189 */     throw new IllegalStateException("No script language defined, and no resource associated with script: " + script);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/support/StandardScriptEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */