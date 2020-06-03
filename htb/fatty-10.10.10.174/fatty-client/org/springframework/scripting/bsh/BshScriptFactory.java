/*     */ package org.springframework.scripting.bsh;
/*     */ 
/*     */ import bsh.EvalError;
/*     */ import java.io.IOException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptFactory;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class BshScriptFactory
/*     */   implements ScriptFactory, BeanClassLoaderAware
/*     */ {
/*     */   private final String scriptSourceLocator;
/*     */   @Nullable
/*     */   private final Class<?>[] scriptInterfaces;
/*     */   @Nullable
/*  54 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/*     */   @Nullable
/*     */   private Class<?> scriptClass;
/*     */   
/*  59 */   private final Object scriptClassMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean wasModifiedForTypeCheck = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BshScriptFactory(String scriptSourceLocator) {
/*  72 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/*  73 */     this.scriptSourceLocator = scriptSourceLocator;
/*  74 */     this.scriptInterfaces = null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public BshScriptFactory(String scriptSourceLocator, @Nullable Class<?>... scriptInterfaces) {
/*  89 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/*  90 */     this.scriptSourceLocator = scriptSourceLocator;
/*  91 */     this.scriptInterfaces = scriptInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  97 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScriptSourceLocator() {
/* 103 */     return this.scriptSourceLocator;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?>[] getScriptInterfaces() {
/* 109 */     return this.scriptInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresConfigInterface() {
/* 117 */     return true;
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
/*     */   @Nullable
/*     */   public Object getScriptedObject(ScriptSource scriptSource, @Nullable Class<?>... actualInterfaces) throws IOException, ScriptCompilationException {
/*     */     Class<?> clazz;
/*     */     try {
/* 132 */       synchronized (this.scriptClassMonitor) {
/* 133 */         boolean requiresScriptEvaluation = (this.wasModifiedForTypeCheck && this.scriptClass == null);
/* 134 */         this.wasModifiedForTypeCheck = false;
/*     */         
/* 136 */         if (scriptSource.isModified() || requiresScriptEvaluation) {
/*     */           
/* 138 */           Object result = BshScriptUtils.evaluateBshScript(scriptSource
/* 139 */               .getScriptAsString(), actualInterfaces, this.beanClassLoader);
/* 140 */           if (result instanceof Class) {
/*     */ 
/*     */             
/* 143 */             this.scriptClass = (Class)result;
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 150 */             return result;
/*     */           } 
/*     */         } 
/* 153 */         clazz = this.scriptClass;
/*     */       }
/*     */     
/* 156 */     } catch (EvalError ex) {
/* 157 */       this.scriptClass = null;
/* 158 */       throw new ScriptCompilationException(scriptSource, ex);
/*     */     } 
/*     */     
/* 161 */     if (clazz != null) {
/*     */       
/*     */       try {
/* 164 */         return ReflectionUtils.accessibleConstructor(clazz, new Class[0]).newInstance(new Object[0]);
/*     */       }
/* 166 */       catch (Throwable ex) {
/* 167 */         throw new ScriptCompilationException(scriptSource, "Could not instantiate script class: " + clazz
/* 168 */             .getName(), ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 174 */       return BshScriptUtils.createBshObject(scriptSource
/* 175 */           .getScriptAsString(), actualInterfaces, this.beanClassLoader);
/*     */     }
/* 177 */     catch (EvalError ex) {
/* 178 */       throw new ScriptCompilationException(scriptSource, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getScriptedObjectType(ScriptSource scriptSource) throws IOException, ScriptCompilationException {
/* 188 */     synchronized (this.scriptClassMonitor) {
/*     */       
/* 190 */       if (scriptSource.isModified()) {
/*     */         
/* 192 */         this.wasModifiedForTypeCheck = true;
/* 193 */         this.scriptClass = BshScriptUtils.determineBshObjectType(scriptSource
/* 194 */             .getScriptAsString(), this.beanClassLoader);
/*     */       } 
/* 196 */       return this.scriptClass;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource) {
/* 207 */     synchronized (this.scriptClassMonitor) {
/* 208 */       return (scriptSource.isModified() || this.wasModifiedForTypeCheck);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 215 */     return "BshScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/bsh/BshScriptFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */