/*     */ package org.springframework.context.weaving;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.instrument.IllegalClassFormatException;
/*     */ import java.security.ProtectionDomain;
/*     */ import org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AspectJWeavingEnabler
/*     */   implements BeanFactoryPostProcessor, BeanClassLoaderAware, LoadTimeWeaverAware, Ordered
/*     */ {
/*     */   public static final String ASPECTJ_AOP_XML_RESOURCE = "META-INF/aop.xml";
/*     */   @Nullable
/*     */   private ClassLoader beanClassLoader;
/*     */   @Nullable
/*     */   private LoadTimeWeaver loadTimeWeaver;
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  62 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
/*  67 */     this.loadTimeWeaver = loadTimeWeaver;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  72 */     return Integer.MIN_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/*  77 */     enableAspectJWeaving(this.loadTimeWeaver, this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void enableAspectJWeaving(@Nullable LoadTimeWeaver weaverToUse, @Nullable ClassLoader beanClassLoader) {
/*     */     InstrumentationLoadTimeWeaver instrumentationLoadTimeWeaver;
/*  89 */     if (weaverToUse == null) {
/*  90 */       if (InstrumentationLoadTimeWeaver.isInstrumentationAvailable()) {
/*  91 */         instrumentationLoadTimeWeaver = new InstrumentationLoadTimeWeaver(beanClassLoader);
/*     */       } else {
/*     */         
/*  94 */         throw new IllegalStateException("No LoadTimeWeaver available");
/*     */       } 
/*     */     }
/*  97 */     instrumentationLoadTimeWeaver.addTransformer(new AspectJClassBypassingClassFileTransformer((ClassFileTransformer)new ClassPreProcessorAgentAdapter()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AspectJClassBypassingClassFileTransformer
/*     */     implements ClassFileTransformer
/*     */   {
/*     */     private final ClassFileTransformer delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AspectJClassBypassingClassFileTransformer(ClassFileTransformer delegate) {
/* 112 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
/* 119 */       if (className.startsWith("org.aspectj") || className.startsWith("org/aspectj")) {
/* 120 */         return classfileBuffer;
/*     */       }
/* 122 */       return this.delegate.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/weaving/AspectJWeavingEnabler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */