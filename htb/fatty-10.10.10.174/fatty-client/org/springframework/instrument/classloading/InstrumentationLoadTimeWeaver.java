/*     */ package org.springframework.instrument.classloading;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.instrument.IllegalClassFormatException;
/*     */ import java.lang.instrument.Instrumentation;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.instrument.InstrumentationSavingAgent;
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
/*     */ public class InstrumentationLoadTimeWeaver
/*     */   implements LoadTimeWeaver
/*     */ {
/*  52 */   private static final boolean AGENT_CLASS_PRESENT = ClassUtils.isPresent("org.springframework.instrument.InstrumentationSavingAgent", InstrumentationLoadTimeWeaver.class
/*     */       
/*  54 */       .getClassLoader());
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */   @Nullable
/*     */   private final Instrumentation instrumentation;
/*     */   
/*  63 */   private final List<ClassFileTransformer> transformers = new ArrayList<>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InstrumentationLoadTimeWeaver() {
/*  70 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InstrumentationLoadTimeWeaver(@Nullable ClassLoader classLoader) {
/*  78 */     this.classLoader = classLoader;
/*  79 */     this.instrumentation = getInstrumentation();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*  85 */     Assert.notNull(transformer, "Transformer must not be null");
/*  86 */     FilteringClassFileTransformer actualTransformer = new FilteringClassFileTransformer(transformer, this.classLoader);
/*     */     
/*  88 */     synchronized (this.transformers) {
/*  89 */       Assert.state((this.instrumentation != null), "Must start with Java agent to use InstrumentationLoadTimeWeaver. See Spring documentation.");
/*     */       
/*  91 */       this.instrumentation.addTransformer(actualTransformer);
/*  92 */       this.transformers.add(actualTransformer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 103 */     Assert.state((this.classLoader != null), "No ClassLoader available");
/* 104 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/* 112 */     return (ClassLoader)new SimpleThrowawayClassLoader(getInstrumentableClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTransformers() {
/* 119 */     synchronized (this.transformers) {
/* 120 */       if (this.instrumentation != null && !this.transformers.isEmpty()) {
/* 121 */         for (int i = this.transformers.size() - 1; i >= 0; i--) {
/* 122 */           this.instrumentation.removeTransformer(this.transformers.get(i));
/*     */         }
/* 124 */         this.transformers.clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInstrumentationAvailable() {
/* 135 */     return (getInstrumentation() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Instrumentation getInstrumentation() {
/* 145 */     if (AGENT_CLASS_PRESENT) {
/* 146 */       return InstrumentationAccessor.getInstrumentation();
/*     */     }
/*     */     
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class InstrumentationAccessor
/*     */   {
/*     */     public static Instrumentation getInstrumentation() {
/* 160 */       return InstrumentationSavingAgent.getInstrumentation();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FilteringClassFileTransformer
/*     */     implements ClassFileTransformer
/*     */   {
/*     */     private final ClassFileTransformer targetTransformer;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final ClassLoader targetClassLoader;
/*     */ 
/*     */ 
/*     */     
/*     */     public FilteringClassFileTransformer(ClassFileTransformer targetTransformer, @Nullable ClassLoader targetClassLoader) {
/* 178 */       this.targetTransformer = targetTransformer;
/* 179 */       this.targetClassLoader = targetClassLoader;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
/* 187 */       if (this.targetClassLoader != loader) {
/* 188 */         return null;
/*     */       }
/* 190 */       return this.targetTransformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 196 */       return "FilteringClassFileTransformer for: " + this.targetTransformer.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/InstrumentationLoadTimeWeaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */