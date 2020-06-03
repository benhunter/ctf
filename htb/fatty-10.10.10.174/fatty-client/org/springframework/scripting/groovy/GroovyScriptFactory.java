/*     */ package org.springframework.scripting.groovy;
/*     */ 
/*     */ import groovy.lang.GroovyClassLoader;
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.MetaClass;
/*     */ import groovy.lang.Script;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.codehaus.groovy.control.CompilerConfiguration;
/*     */ import org.codehaus.groovy.control.customizers.CompilationCustomizer;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptFactory;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GroovyScriptFactory
/*     */   implements ScriptFactory, BeanFactoryAware, BeanClassLoaderAware
/*     */ {
/*     */   private final String scriptSourceLocator;
/*     */   @Nullable
/*     */   private GroovyObjectCustomizer groovyObjectCustomizer;
/*     */   @Nullable
/*     */   private CompilerConfiguration compilerConfiguration;
/*     */   @Nullable
/*     */   private GroovyClassLoader groovyClassLoader;
/*     */   @Nullable
/*     */   private Class<?> scriptClass;
/*     */   @Nullable
/*     */   private Class<?> scriptResultClass;
/*     */   @Nullable
/*     */   private CachedResultHolder cachedResult;
/*  82 */   private final Object scriptClassMonitor = new Object();
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
/*     */   public GroovyScriptFactory(String scriptSourceLocator) {
/*  95 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/*  96 */     this.scriptSourceLocator = scriptSourceLocator;
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
/*     */   public GroovyScriptFactory(String scriptSourceLocator, @Nullable GroovyObjectCustomizer groovyObjectCustomizer) {
/* 111 */     this(scriptSourceLocator);
/* 112 */     this.groovyObjectCustomizer = groovyObjectCustomizer;
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
/*     */   public GroovyScriptFactory(String scriptSourceLocator, @Nullable CompilerConfiguration compilerConfiguration) {
/* 127 */     this(scriptSourceLocator);
/* 128 */     this.compilerConfiguration = compilerConfiguration;
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
/*     */   
/*     */   public GroovyScriptFactory(String scriptSourceLocator, CompilationCustomizer... compilationCustomizers) {
/* 144 */     this(scriptSourceLocator);
/* 145 */     if (!ObjectUtils.isEmpty((Object[])compilationCustomizers)) {
/* 146 */       this.compilerConfiguration = new CompilerConfiguration();
/* 147 */       this.compilerConfiguration.addCompilationCustomizers(compilationCustomizers);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 154 */     if (beanFactory instanceof ConfigurableListableBeanFactory) {
/* 155 */       ((ConfigurableListableBeanFactory)beanFactory).ignoreDependencyType(MetaClass.class);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 161 */     this.groovyClassLoader = buildGroovyClassLoader(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroovyClassLoader getGroovyClassLoader() {
/* 168 */     synchronized (this.scriptClassMonitor) {
/* 169 */       if (this.groovyClassLoader == null) {
/* 170 */         this.groovyClassLoader = buildGroovyClassLoader(ClassUtils.getDefaultClassLoader());
/*     */       }
/* 172 */       return this.groovyClassLoader;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected GroovyClassLoader buildGroovyClassLoader(@Nullable ClassLoader classLoader) {
/* 182 */     return (this.compilerConfiguration != null) ? new GroovyClassLoader(classLoader, this.compilerConfiguration) : new GroovyClassLoader(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScriptSourceLocator() {
/* 189 */     return this.scriptSourceLocator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?>[] getScriptInterfaces() {
/* 200 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresConfigInterface() {
/* 209 */     return false;
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
/* 222 */     synchronized (this.scriptClassMonitor) {
/*     */ 
/*     */       
/* 225 */       this.wasModifiedForTypeCheck = false;
/*     */       
/* 227 */       if (this.cachedResult != null) {
/* 228 */         Object result = this.cachedResult.object;
/* 229 */         this.cachedResult = null;
/* 230 */         return result;
/*     */       } 
/*     */       
/* 233 */       if (this.scriptClass == null || scriptSource.isModified()) {
/*     */         
/* 235 */         this.scriptClass = getGroovyClassLoader().parseClass(scriptSource
/* 236 */             .getScriptAsString(), scriptSource.suggestedClassName());
/*     */         
/* 238 */         if (Script.class.isAssignableFrom(this.scriptClass)) {
/*     */           
/* 240 */           Object result = executeScript(scriptSource, this.scriptClass);
/* 241 */           this.scriptResultClass = (result != null) ? result.getClass() : null;
/* 242 */           return result;
/*     */         } 
/*     */         
/* 245 */         this.scriptResultClass = this.scriptClass;
/*     */       } 
/*     */       
/* 248 */       Class<?> scriptClassToExecute = this.scriptClass;
/*     */ 
/*     */       
/* 251 */       return executeScript(scriptSource, scriptClassToExecute);
/*     */     } 
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
/*     */   @Nullable
/*     */   public Class<?> getScriptedObjectType(ScriptSource scriptSource) throws IOException, ScriptCompilationException {
/* 266 */     synchronized (this.scriptClassMonitor) {
/*     */       
/* 268 */       if (this.scriptClass == null || scriptSource.isModified()) {
/*     */         
/* 270 */         this.wasModifiedForTypeCheck = true;
/* 271 */         this.scriptClass = getGroovyClassLoader().parseClass(scriptSource
/* 272 */             .getScriptAsString(), scriptSource.suggestedClassName());
/*     */         
/* 274 */         if (Script.class.isAssignableFrom(this.scriptClass)) {
/*     */           
/* 276 */           Object result = executeScript(scriptSource, this.scriptClass);
/* 277 */           this.scriptResultClass = (result != null) ? result.getClass() : null;
/* 278 */           this.cachedResult = new CachedResultHolder(result);
/*     */         } else {
/*     */           
/* 281 */           this.scriptResultClass = this.scriptClass;
/*     */         } 
/*     */       } 
/* 284 */       return this.scriptResultClass;
/*     */     } 
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
/*     */   public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource) {
/* 297 */     synchronized (this.scriptClassMonitor) {
/* 298 */       return (scriptSource.isModified() || this.wasModifiedForTypeCheck);
/*     */     } 
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
/*     */   @Nullable
/*     */   protected Object executeScript(ScriptSource scriptSource, Class<?> scriptClass) throws ScriptCompilationException {
/*     */     try {
/* 314 */       GroovyObject goo = ReflectionUtils.accessibleConstructor(scriptClass, new Class[0]).newInstance(new Object[0]);
/*     */       
/* 316 */       if (this.groovyObjectCustomizer != null)
/*     */       {
/* 318 */         this.groovyObjectCustomizer.customize(goo);
/*     */       }
/*     */       
/* 321 */       if (goo instanceof Script)
/*     */       {
/* 323 */         return ((Script)goo).run();
/*     */       }
/*     */ 
/*     */       
/* 327 */       return goo;
/*     */     
/*     */     }
/* 330 */     catch (NoSuchMethodException ex) {
/* 331 */       throw new ScriptCompilationException("No default constructor on Groovy script class: " + scriptClass
/* 332 */           .getName(), ex);
/*     */     }
/* 334 */     catch (InstantiationException ex) {
/* 335 */       throw new ScriptCompilationException(scriptSource, "Unable to instantiate Groovy script class: " + scriptClass
/* 336 */           .getName(), ex);
/*     */     }
/* 338 */     catch (IllegalAccessException ex) {
/* 339 */       throw new ScriptCompilationException(scriptSource, "Could not access Groovy script constructor: " + scriptClass
/* 340 */           .getName(), ex);
/*     */     }
/* 342 */     catch (InvocationTargetException ex) {
/* 343 */       throw new ScriptCompilationException("Failed to invoke Groovy script constructor: " + scriptClass
/* 344 */           .getName(), ex.getTargetException());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 351 */     return "GroovyScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CachedResultHolder
/*     */   {
/*     */     @Nullable
/*     */     public final Object object;
/*     */ 
/*     */ 
/*     */     
/*     */     public CachedResultHolder(@Nullable Object object) {
/* 364 */       this.object = object;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/groovy/GroovyScriptFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */