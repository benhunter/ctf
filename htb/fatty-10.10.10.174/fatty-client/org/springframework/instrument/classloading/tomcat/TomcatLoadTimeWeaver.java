/*     */ package org.springframework.instrument.classloading.tomcat;
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
/*     */ public class TomcatLoadTimeWeaver
/*     */   implements LoadTimeWeaver
/*     */ {
/*     */   private static final String INSTRUMENTABLE_LOADER_CLASS_NAME = "org.apache.tomcat.InstrumentableClassLoader";
/*     */   private final ClassLoader classLoader;
/*     */   private final Method addTransformerMethod;
/*     */   private final Method copyMethod;
/*     */   
/*     */   public TomcatLoadTimeWeaver() {
/*  55 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TomcatLoadTimeWeaver(@Nullable ClassLoader classLoader) {
/*     */     Class<?> instrumentableLoaderClass;
/*  64 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*  65 */     this.classLoader = classLoader;
/*     */ 
/*     */     
/*     */     try {
/*  69 */       instrumentableLoaderClass = classLoader.loadClass("org.apache.tomcat.InstrumentableClassLoader");
/*  70 */       if (!instrumentableLoaderClass.isInstance(classLoader))
/*     */       {
/*  72 */         instrumentableLoaderClass = classLoader.getClass();
/*     */       }
/*     */     }
/*  75 */     catch (ClassNotFoundException ex) {
/*     */       
/*  77 */       instrumentableLoaderClass = classLoader.getClass();
/*     */     } 
/*     */     
/*     */     try {
/*  81 */       this.addTransformerMethod = instrumentableLoaderClass.getMethod("addTransformer", new Class[] { ClassFileTransformer.class });
/*     */       
/*  83 */       Method copyMethod = ClassUtils.getMethodIfAvailable(instrumentableLoaderClass, "copyWithoutTransformers", new Class[0]);
/*  84 */       if (copyMethod == null)
/*     */       {
/*  86 */         copyMethod = instrumentableLoaderClass.getMethod("getThrowawayClassLoader", new Class[0]);
/*     */       }
/*  88 */       this.copyMethod = copyMethod;
/*     */     }
/*  90 */     catch (Throwable ex) {
/*  91 */       throw new IllegalStateException("Could not initialize TomcatLoadTimeWeaver because Tomcat API classes are not available", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*     */     try {
/* 100 */       this.addTransformerMethod.invoke(this.classLoader, new Object[] { transformer });
/*     */     }
/* 102 */     catch (InvocationTargetException ex) {
/* 103 */       throw new IllegalStateException("Tomcat addTransformer method threw exception", ex.getCause());
/*     */     }
/* 105 */     catch (Throwable ex) {
/* 106 */       throw new IllegalStateException("Could not invoke Tomcat addTransformer method", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 112 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/*     */     try {
/* 118 */       return (ClassLoader)new OverridingClassLoader(this.classLoader, (ClassLoader)this.copyMethod.invoke(this.classLoader, new Object[0]));
/*     */     }
/* 120 */     catch (InvocationTargetException ex) {
/* 121 */       throw new IllegalStateException("Tomcat copy method threw exception", ex.getCause());
/*     */     }
/* 123 */     catch (Throwable ex) {
/* 124 */       throw new IllegalStateException("Could not invoke Tomcat copy method", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/tomcat/TomcatLoadTimeWeaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */