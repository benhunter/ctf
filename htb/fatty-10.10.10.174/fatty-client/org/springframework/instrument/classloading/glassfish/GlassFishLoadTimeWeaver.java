/*     */ package org.springframework.instrument.classloading.glassfish;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.core.OverridingClassLoader;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class GlassFishLoadTimeWeaver
/*     */   implements LoadTimeWeaver
/*     */ {
/*     */   private static final String INSTRUMENTABLE_LOADER_CLASS_NAME = "org.glassfish.api.deployment.InstrumentableClassLoader";
/*     */   private final ClassLoader classLoader;
/*     */   private final Method addTransformerMethod;
/*     */   private final Method copyMethod;
/*     */   
/*     */   public GlassFishLoadTimeWeaver() {
/*  58 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GlassFishLoadTimeWeaver(@Nullable ClassLoader classLoader) {
/*     */     Class<?> instrumentableLoaderClass;
/*  67 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*     */ 
/*     */     
/*     */     try {
/*  71 */       instrumentableLoaderClass = classLoader.loadClass("org.glassfish.api.deployment.InstrumentableClassLoader");
/*  72 */       this.addTransformerMethod = instrumentableLoaderClass.getMethod("addTransformer", new Class[] { ClassFileTransformer.class });
/*  73 */       this.copyMethod = instrumentableLoaderClass.getMethod("copy", new Class[0]);
/*     */     }
/*  75 */     catch (Throwable ex) {
/*  76 */       throw new IllegalStateException("Could not initialize GlassFishLoadTimeWeaver because GlassFish API classes are not available", ex);
/*     */     } 
/*     */ 
/*     */     
/*  80 */     ClassLoader clazzLoader = null;
/*     */ 
/*     */     
/*  83 */     for (ClassLoader cl = classLoader; cl != null && clazzLoader == null; cl = cl.getParent()) {
/*  84 */       if (instrumentableLoaderClass.isInstance(cl)) {
/*  85 */         clazzLoader = cl;
/*     */       }
/*     */     } 
/*     */     
/*  89 */     if (clazzLoader == null) {
/*  90 */       throw new IllegalArgumentException(classLoader + " and its parents are not suitable ClassLoaders: A [" + instrumentableLoaderClass
/*  91 */           .getName() + "] implementation is required.");
/*     */     }
/*     */     
/*  94 */     this.classLoader = clazzLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*     */     try {
/* 101 */       this.addTransformerMethod.invoke(this.classLoader, new Object[] { transformer });
/*     */     }
/* 103 */     catch (InvocationTargetException ex) {
/* 104 */       throw new IllegalStateException("GlassFish addTransformer method threw exception", ex.getCause());
/*     */     }
/* 106 */     catch (Throwable ex) {
/* 107 */       throw new IllegalStateException("Could not invoke GlassFish addTransformer method", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 113 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/*     */     try {
/* 119 */       return (ClassLoader)new OverridingClassLoader(this.classLoader, (ClassLoader)this.copyMethod.invoke(this.classLoader, new Object[0]));
/*     */     }
/* 121 */     catch (InvocationTargetException ex) {
/* 122 */       throw new IllegalStateException("GlassFish copy method threw exception", ex.getCause());
/*     */     }
/* 124 */     catch (Throwable ex) {
/* 125 */       throw new IllegalStateException("Could not invoke GlassFish copy method", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/glassfish/GlassFishLoadTimeWeaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */