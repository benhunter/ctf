/*     */ package org.springframework.instrument.classloading.websphere;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
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
/*     */ class WebSphereClassLoaderAdapter
/*     */ {
/*     */   private static final String COMPOUND_CLASS_LOADER_NAME = "com.ibm.ws.classloader.CompoundClassLoader";
/*     */   private static final String CLASS_PRE_PROCESSOR_NAME = "com.ibm.websphere.classloader.ClassLoaderInstancePreDefinePlugin";
/*     */   private static final String PLUGINS_FIELD = "preDefinePlugins";
/*     */   private ClassLoader classLoader;
/*     */   private Class<?> wsPreProcessorClass;
/*     */   private Method addPreDefinePlugin;
/*     */   private Constructor<? extends ClassLoader> cloneConstructor;
/*     */   private Field transformerList;
/*     */   
/*     */   public WebSphereClassLoaderAdapter(ClassLoader classLoader) {
/*     */     Class<?> wsCompoundClassLoaderClass;
/*     */     try {
/*  62 */       wsCompoundClassLoaderClass = classLoader.loadClass("com.ibm.ws.classloader.CompoundClassLoader");
/*  63 */       this.cloneConstructor = (Constructor)classLoader.getClass().getDeclaredConstructor(new Class[] { wsCompoundClassLoaderClass });
/*  64 */       this.cloneConstructor.setAccessible(true);
/*     */       
/*  66 */       this.wsPreProcessorClass = classLoader.loadClass("com.ibm.websphere.classloader.ClassLoaderInstancePreDefinePlugin");
/*  67 */       this.addPreDefinePlugin = classLoader.getClass().getMethod("addPreDefinePlugin", new Class[] { this.wsPreProcessorClass });
/*  68 */       this.transformerList = wsCompoundClassLoaderClass.getDeclaredField("preDefinePlugins");
/*  69 */       this.transformerList.setAccessible(true);
/*     */     }
/*  71 */     catch (Throwable ex) {
/*  72 */       throw new IllegalStateException("Could not initialize WebSphere LoadTimeWeaver because WebSphere API classes are not available", ex);
/*     */     } 
/*     */ 
/*     */     
/*  76 */     if (!wsCompoundClassLoaderClass.isInstance(classLoader)) {
/*  77 */       throw new IllegalArgumentException("ClassLoader must be an instance of [com.ibm.ws.classloader.CompoundClassLoader]: " + classLoader);
/*     */     }
/*     */     
/*  80 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/*  85 */     return this.classLoader;
/*     */   }
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*  89 */     Assert.notNull(transformer, "ClassFileTransformer must not be null");
/*     */     try {
/*  91 */       InvocationHandler adapter = new WebSphereClassPreDefinePlugin(transformer);
/*  92 */       Object adapterInstance = Proxy.newProxyInstance(this.wsPreProcessorClass.getClassLoader(), new Class[] { this.wsPreProcessorClass }, adapter);
/*     */       
/*  94 */       this.addPreDefinePlugin.invoke(this.classLoader, new Object[] { adapterInstance });
/*     */     }
/*  96 */     catch (InvocationTargetException ex) {
/*  97 */       throw new IllegalStateException("WebSphere addPreDefinePlugin method threw exception", ex.getCause());
/*     */     }
/*  99 */     catch (Throwable ex) {
/* 100 */       throw new IllegalStateException("Could not invoke WebSphere addPreDefinePlugin method", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/*     */     try {
/* 106 */       ClassLoader loader = this.cloneConstructor.newInstance(new Object[] { getClassLoader() });
/*     */       
/* 108 */       List<?> list = (List)this.transformerList.get(loader);
/* 109 */       list.clear();
/* 110 */       return loader;
/*     */     }
/* 112 */     catch (InvocationTargetException ex) {
/* 113 */       throw new IllegalStateException("WebSphere CompoundClassLoader constructor failed", ex.getCause());
/*     */     }
/* 115 */     catch (Throwable ex) {
/* 116 */       throw new IllegalStateException("Could not construct WebSphere CompoundClassLoader", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/websphere/WebSphereClassLoaderAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */