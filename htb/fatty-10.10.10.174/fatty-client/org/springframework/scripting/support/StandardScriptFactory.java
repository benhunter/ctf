/*     */ package org.springframework.scripting.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import javax.script.Invocable;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptFactory;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class StandardScriptFactory
/*     */   implements ScriptFactory, BeanClassLoaderAware
/*     */ {
/*     */   @Nullable
/*     */   private final String scriptEngineName;
/*     */   private final String scriptSourceLocator;
/*     */   @Nullable
/*     */   private final Class<?>[] scriptInterfaces;
/*     */   @Nullable
/*  60 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile ScriptEngine scriptEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardScriptFactory(String scriptSourceLocator) {
/*  72 */     this(null, scriptSourceLocator, (Class[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardScriptFactory(String scriptSourceLocator, Class<?>... scriptInterfaces) {
/*  83 */     this(null, scriptSourceLocator, scriptInterfaces);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardScriptFactory(String scriptEngineName, String scriptSourceLocator) {
/*  94 */     this(scriptEngineName, scriptSourceLocator, (Class[])null);
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
/*     */   public StandardScriptFactory(@Nullable String scriptEngineName, String scriptSourceLocator, @Nullable Class<?>... scriptInterfaces) {
/* 109 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/* 110 */     this.scriptEngineName = scriptEngineName;
/* 111 */     this.scriptSourceLocator = scriptSourceLocator;
/* 112 */     this.scriptInterfaces = scriptInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 118 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getScriptSourceLocator() {
/* 123 */     return this.scriptSourceLocator;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?>[] getScriptInterfaces() {
/* 129 */     return this.scriptInterfaces;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresConfigInterface() {
/* 134 */     return false;
/*     */   }
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
/* 146 */     Object script = evaluateScript(scriptSource);
/*     */     
/* 148 */     if (!ObjectUtils.isEmpty((Object[])actualInterfaces)) {
/* 149 */       boolean adaptationRequired = false;
/* 150 */       for (Class<?> requestedIfc : actualInterfaces) {
/* 151 */         if ((script instanceof Class) ? !requestedIfc.isAssignableFrom((Class)script) : 
/* 152 */           !requestedIfc.isInstance(script)) {
/* 153 */           adaptationRequired = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 157 */       if (adaptationRequired) {
/* 158 */         script = adaptToInterfaces(script, scriptSource, actualInterfaces);
/*     */       }
/*     */     } 
/*     */     
/* 162 */     if (script instanceof Class) {
/* 163 */       Class<?> scriptClass = (Class)script;
/*     */       try {
/* 165 */         return ReflectionUtils.accessibleConstructor(scriptClass, new Class[0]).newInstance(new Object[0]);
/*     */       }
/* 167 */       catch (NoSuchMethodException ex) {
/* 168 */         throw new ScriptCompilationException("No default constructor on script class: " + scriptClass
/* 169 */             .getName(), ex);
/*     */       }
/* 171 */       catch (InstantiationException ex) {
/* 172 */         throw new ScriptCompilationException(scriptSource, "Unable to instantiate script class: " + scriptClass
/* 173 */             .getName(), ex);
/*     */       }
/* 175 */       catch (IllegalAccessException ex) {
/* 176 */         throw new ScriptCompilationException(scriptSource, "Could not access script constructor: " + scriptClass
/* 177 */             .getName(), ex);
/*     */       }
/* 179 */       catch (InvocationTargetException ex) {
/* 180 */         throw new ScriptCompilationException("Failed to invoke script constructor: " + scriptClass
/* 181 */             .getName(), ex.getTargetException());
/*     */       } 
/*     */     } 
/*     */     
/* 185 */     return script;
/*     */   }
/*     */   
/*     */   protected Object evaluateScript(ScriptSource scriptSource) {
/*     */     try {
/* 190 */       ScriptEngine scriptEngine = this.scriptEngine;
/* 191 */       if (scriptEngine == null) {
/* 192 */         scriptEngine = retrieveScriptEngine(scriptSource);
/* 193 */         if (scriptEngine == null) {
/* 194 */           throw new IllegalStateException("Could not determine script engine for " + scriptSource);
/*     */         }
/* 196 */         this.scriptEngine = scriptEngine;
/*     */       } 
/* 198 */       return scriptEngine.eval(scriptSource.getScriptAsString());
/*     */     }
/* 200 */     catch (Exception ex) {
/* 201 */       throw new ScriptCompilationException(scriptSource, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected ScriptEngine retrieveScriptEngine(ScriptSource scriptSource) {
/* 207 */     ScriptEngineManager scriptEngineManager = new ScriptEngineManager(this.beanClassLoader);
/*     */     
/* 209 */     if (this.scriptEngineName != null) {
/* 210 */       return StandardScriptUtils.retrieveEngineByName(scriptEngineManager, this.scriptEngineName);
/*     */     }
/*     */     
/* 213 */     if (scriptSource instanceof ResourceScriptSource) {
/* 214 */       String filename = ((ResourceScriptSource)scriptSource).getResource().getFilename();
/* 215 */       if (filename != null) {
/* 216 */         String extension = StringUtils.getFilenameExtension(filename);
/* 217 */         if (extension != null) {
/* 218 */           ScriptEngine engine = scriptEngineManager.getEngineByExtension(extension);
/* 219 */           if (engine != null) {
/* 220 */             return engine;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object adaptToInterfaces(@Nullable Object script, ScriptSource scriptSource, Class<?>... actualInterfaces) {
/*     */     Class<?> adaptedIfc;
/* 234 */     if (actualInterfaces.length == 1) {
/* 235 */       adaptedIfc = actualInterfaces[0];
/*     */     } else {
/*     */       
/* 238 */       adaptedIfc = ClassUtils.createCompositeInterface(actualInterfaces, this.beanClassLoader);
/*     */     } 
/*     */     
/* 241 */     if (adaptedIfc != null) {
/* 242 */       ScriptEngine scriptEngine = this.scriptEngine;
/* 243 */       if (!(scriptEngine instanceof Invocable)) {
/* 244 */         throw new ScriptCompilationException(scriptSource, "ScriptEngine must implement Invocable in order to adapt it to an interface: " + scriptEngine);
/*     */       }
/*     */       
/* 247 */       Invocable invocable = (Invocable)scriptEngine;
/* 248 */       if (script != null) {
/* 249 */         script = invocable.getInterface(script, adaptedIfc);
/*     */       }
/* 251 */       if (script == null) {
/* 252 */         script = invocable.getInterface(adaptedIfc);
/* 253 */         if (script == null) {
/* 254 */           throw new ScriptCompilationException(scriptSource, "Could not adapt script to interface [" + adaptedIfc
/* 255 */               .getName() + "]");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 260 */     return script;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getScriptedObjectType(ScriptSource scriptSource) throws IOException, ScriptCompilationException {
/* 268 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource) {
/* 273 */     return scriptSource.isModified();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 279 */     return "StandardScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/support/StandardScriptFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */