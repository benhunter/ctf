/*     */ package org.springframework.instrument.classloading;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.OverridingClassLoader;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class ReflectiveLoadTimeWeaver
/*     */   implements LoadTimeWeaver
/*     */ {
/*     */   private static final String ADD_TRANSFORMER_METHOD_NAME = "addTransformer";
/*     */   private static final String GET_THROWAWAY_CLASS_LOADER_METHOD_NAME = "getThrowawayClassLoader";
/*  72 */   private static final Log logger = LogFactory.getLog(ReflectiveLoadTimeWeaver.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassLoader classLoader;
/*     */ 
/*     */   
/*     */   private final Method addTransformerMethod;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final Method getThrowawayClassLoaderMethod;
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectiveLoadTimeWeaver() {
/*  88 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectiveLoadTimeWeaver(@Nullable ClassLoader classLoader) {
/*  99 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 100 */     this.classLoader = classLoader;
/*     */     
/* 102 */     Method addTransformerMethod = ClassUtils.getMethodIfAvailable(this.classLoader
/* 103 */         .getClass(), "addTransformer", new Class[] { ClassFileTransformer.class });
/* 104 */     if (addTransformerMethod == null) {
/* 105 */       throw new IllegalStateException("ClassLoader [" + classLoader
/* 106 */           .getClass().getName() + "] does NOT provide an 'addTransformer(ClassFileTransformer)' method.");
/*     */     }
/*     */     
/* 109 */     this.addTransformerMethod = addTransformerMethod;
/*     */     
/* 111 */     Method getThrowawayClassLoaderMethod = ClassUtils.getMethodIfAvailable(this.classLoader
/* 112 */         .getClass(), "getThrowawayClassLoader", new Class[0]);
/*     */     
/* 114 */     if (getThrowawayClassLoaderMethod == null && 
/* 115 */       logger.isDebugEnabled()) {
/* 116 */       logger.debug("The ClassLoader [" + classLoader.getClass().getName() + "] does NOT provide a 'getThrowawayClassLoader()' method; SimpleThrowawayClassLoader will be used instead.");
/*     */     }
/*     */ 
/*     */     
/* 120 */     this.getThrowawayClassLoaderMethod = getThrowawayClassLoaderMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/* 126 */     Assert.notNull(transformer, "Transformer must not be null");
/* 127 */     ReflectionUtils.invokeMethod(this.addTransformerMethod, this.classLoader, new Object[] { transformer });
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 132 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/* 137 */     if (this.getThrowawayClassLoaderMethod != null) {
/*     */       
/* 139 */       ClassLoader target = (ClassLoader)ReflectionUtils.invokeMethod(this.getThrowawayClassLoaderMethod, this.classLoader);
/* 140 */       return (target instanceof org.springframework.core.DecoratingClassLoader) ? target : (ClassLoader)new OverridingClassLoader(this.classLoader, target);
/*     */     } 
/*     */ 
/*     */     
/* 144 */     return (ClassLoader)new SimpleThrowawayClassLoader(this.classLoader);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/ReflectiveLoadTimeWeaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */