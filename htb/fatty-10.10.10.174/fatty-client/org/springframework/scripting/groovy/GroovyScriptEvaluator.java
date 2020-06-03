/*     */ package org.springframework.scripting.groovy;
/*     */ 
/*     */ import groovy.lang.Binding;
/*     */ import groovy.lang.GroovyRuntimeException;
/*     */ import groovy.lang.GroovyShell;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import org.codehaus.groovy.control.CompilerConfiguration;
/*     */ import org.codehaus.groovy.control.customizers.CompilationCustomizer;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptEvaluator;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.scripting.support.ResourceScriptSource;
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
/*     */ public class GroovyScriptEvaluator
/*     */   implements ScriptEvaluator, BeanClassLoaderAware
/*     */ {
/*     */   @Nullable
/*     */   private ClassLoader classLoader;
/*  47 */   private CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroovyScriptEvaluator() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroovyScriptEvaluator(@Nullable ClassLoader classLoader) {
/*  61 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompilerConfiguration(@Nullable CompilerConfiguration compilerConfiguration) {
/*  71 */     this.compilerConfiguration = (compilerConfiguration != null) ? compilerConfiguration : new CompilerConfiguration();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompilerConfiguration getCompilerConfiguration() {
/*  81 */     return this.compilerConfiguration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompilationCustomizers(CompilationCustomizer... compilationCustomizers) {
/*  91 */     this.compilerConfiguration.addCompilationCustomizers(compilationCustomizers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  96 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object evaluate(ScriptSource script) {
/* 103 */     return evaluate(script, null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object evaluate(ScriptSource script, @Nullable Map<String, Object> arguments) {
/* 109 */     GroovyShell groovyShell = new GroovyShell(this.classLoader, new Binding(arguments), this.compilerConfiguration);
/*     */ 
/*     */     
/*     */     try {
/* 113 */       String filename = (script instanceof ResourceScriptSource) ? ((ResourceScriptSource)script).getResource().getFilename() : null;
/* 114 */       if (filename != null) {
/* 115 */         return groovyShell.evaluate(script.getScriptAsString(), filename);
/*     */       }
/*     */       
/* 118 */       return groovyShell.evaluate(script.getScriptAsString());
/*     */     
/*     */     }
/* 121 */     catch (IOException ex) {
/* 122 */       throw new ScriptCompilationException(script, "Cannot access Groovy script", ex);
/*     */     }
/* 124 */     catch (GroovyRuntimeException ex) {
/* 125 */       throw new ScriptCompilationException(script, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/groovy/GroovyScriptEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */