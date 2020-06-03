/*     */ package org.springframework.instrument.classloading.jboss;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.SimpleThrowawayClassLoader;
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
/*     */ public class JBossLoadTimeWeaver
/*     */   implements LoadTimeWeaver
/*     */ {
/*     */   private static final String DELEGATING_TRANSFORMER_CLASS_NAME = "org.jboss.as.server.deployment.module.DelegatingClassFileTransformer";
/*     */   private static final String WRAPPER_TRANSFORMER_CLASS_NAME = "org.jboss.modules.JLIClassTransformer";
/*     */   private final ClassLoader classLoader;
/*     */   private final Object delegatingTransformer;
/*     */   private final Method addTransformer;
/*     */   
/*     */   public JBossLoadTimeWeaver() {
/*  63 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBossLoadTimeWeaver(@Nullable ClassLoader classLoader) {
/*  72 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*  73 */     this.classLoader = classLoader;
/*     */     
/*     */     try {
/*  76 */       Field transformer = ReflectionUtils.findField(classLoader.getClass(), "transformer");
/*  77 */       if (transformer == null) {
/*  78 */         throw new IllegalArgumentException("Could not find 'transformer' field on JBoss ClassLoader: " + classLoader
/*  79 */             .getClass().getName());
/*     */       }
/*  81 */       transformer.setAccessible(true);
/*     */       
/*  83 */       Object suggestedTransformer = transformer.get(classLoader);
/*  84 */       if (suggestedTransformer.getClass().getName().equals("org.jboss.modules.JLIClassTransformer")) {
/*  85 */         Field wrappedTransformer = ReflectionUtils.findField(suggestedTransformer.getClass(), "transformer");
/*  86 */         if (wrappedTransformer == null) {
/*  87 */           throw new IllegalArgumentException("Could not find 'transformer' field on JBoss JLIClassTransformer: " + suggestedTransformer
/*     */               
/*  89 */               .getClass().getName());
/*     */         }
/*  91 */         wrappedTransformer.setAccessible(true);
/*  92 */         suggestedTransformer = wrappedTransformer.get(suggestedTransformer);
/*     */       } 
/*  94 */       if (!suggestedTransformer.getClass().getName().equals("org.jboss.as.server.deployment.module.DelegatingClassFileTransformer")) {
/*  95 */         throw new IllegalStateException("Transformer not of the expected type DelegatingClassFileTransformer: " + suggestedTransformer
/*     */             
/*  97 */             .getClass().getName());
/*     */       }
/*  99 */       this.delegatingTransformer = suggestedTransformer;
/*     */       
/* 101 */       Method addTransformer = ReflectionUtils.findMethod(this.delegatingTransformer.getClass(), "addTransformer", new Class[] { ClassFileTransformer.class });
/*     */       
/* 103 */       if (addTransformer == null) {
/* 104 */         throw new IllegalArgumentException("Could not find 'addTransformer' method on JBoss DelegatingClassFileTransformer: " + this.delegatingTransformer
/*     */             
/* 106 */             .getClass().getName());
/*     */       }
/* 108 */       addTransformer.setAccessible(true);
/* 109 */       this.addTransformer = addTransformer;
/*     */     }
/* 111 */     catch (Throwable ex) {
/* 112 */       throw new IllegalStateException("Could not initialize JBoss LoadTimeWeaver", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*     */     try {
/* 120 */       this.addTransformer.invoke(this.delegatingTransformer, new Object[] { transformer });
/*     */     }
/* 122 */     catch (Throwable ex) {
/* 123 */       throw new IllegalStateException("Could not add transformer on JBoss ClassLoader: " + this.classLoader, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 129 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/* 134 */     return (ClassLoader)new SimpleThrowawayClassLoader(getInstrumentableClassLoader());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/jboss/JBossLoadTimeWeaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */