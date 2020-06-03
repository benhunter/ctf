/*     */ package org.springframework.context.weaving;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.ReflectiveLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.glassfish.GlassFishLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.jboss.JBossLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.tomcat.TomcatLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.weblogic.WebLogicLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.websphere.WebSphereLoadTimeWeaver;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultContextLoadTimeWeaver
/*     */   implements LoadTimeWeaver, BeanClassLoaderAware, DisposableBean
/*     */ {
/*  60 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private LoadTimeWeaver loadTimeWeaver;
/*     */ 
/*     */   
/*     */   public DefaultContextLoadTimeWeaver() {}
/*     */ 
/*     */   
/*     */   public DefaultContextLoadTimeWeaver(ClassLoader beanClassLoader) {
/*  70 */     setBeanClassLoader(beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  76 */     LoadTimeWeaver serverSpecificLoadTimeWeaver = createServerSpecificLoadTimeWeaver(classLoader);
/*  77 */     if (serverSpecificLoadTimeWeaver != null) {
/*  78 */       if (this.logger.isDebugEnabled()) {
/*  79 */         this.logger.debug("Determined server-specific load-time weaver: " + serverSpecificLoadTimeWeaver
/*  80 */             .getClass().getName());
/*     */       }
/*  82 */       this.loadTimeWeaver = serverSpecificLoadTimeWeaver;
/*     */     }
/*  84 */     else if (InstrumentationLoadTimeWeaver.isInstrumentationAvailable()) {
/*  85 */       this.logger.debug("Found Spring's JVM agent for instrumentation");
/*  86 */       this.loadTimeWeaver = (LoadTimeWeaver)new InstrumentationLoadTimeWeaver(classLoader);
/*     */     } else {
/*     */       
/*     */       try {
/*  90 */         this.loadTimeWeaver = (LoadTimeWeaver)new ReflectiveLoadTimeWeaver(classLoader);
/*  91 */         if (this.logger.isDebugEnabled()) {
/*  92 */           this.logger.debug("Using reflective load-time weaver for class loader: " + this.loadTimeWeaver
/*  93 */               .getInstrumentableClassLoader().getClass().getName());
/*     */         }
/*     */       }
/*  96 */       catch (IllegalStateException ex) {
/*  97 */         throw new IllegalStateException(ex.getMessage() + " Specify a custom LoadTimeWeaver or start your Java virtual machine with Spring's agent: -javaagent:org.springframework.instrument.jar");
/*     */       } 
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
/*     */   @Nullable
/*     */   protected LoadTimeWeaver createServerSpecificLoadTimeWeaver(ClassLoader classLoader) {
/* 111 */     String name = classLoader.getClass().getName();
/*     */     try {
/* 113 */       if (name.startsWith("org.apache.catalina")) {
/* 114 */         return (LoadTimeWeaver)new TomcatLoadTimeWeaver(classLoader);
/*     */       }
/* 116 */       if (name.startsWith("org.glassfish")) {
/* 117 */         return (LoadTimeWeaver)new GlassFishLoadTimeWeaver(classLoader);
/*     */       }
/* 119 */       if (name.startsWith("org.jboss.modules")) {
/* 120 */         return (LoadTimeWeaver)new JBossLoadTimeWeaver(classLoader);
/*     */       }
/* 122 */       if (name.startsWith("com.ibm.ws.classloader")) {
/* 123 */         return (LoadTimeWeaver)new WebSphereLoadTimeWeaver(classLoader);
/*     */       }
/* 125 */       if (name.startsWith("weblogic")) {
/* 126 */         return (LoadTimeWeaver)new WebLogicLoadTimeWeaver(classLoader);
/*     */       }
/*     */     }
/* 129 */     catch (Exception ex) {
/* 130 */       if (this.logger.isInfoEnabled()) {
/* 131 */         this.logger.info("Could not obtain server-specific LoadTimeWeaver: " + ex.getMessage());
/*     */       }
/*     */     } 
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 139 */     if (this.loadTimeWeaver instanceof InstrumentationLoadTimeWeaver) {
/* 140 */       if (this.logger.isDebugEnabled()) {
/* 141 */         this.logger.debug("Removing all registered transformers for class loader: " + this.loadTimeWeaver
/* 142 */             .getInstrumentableClassLoader().getClass().getName());
/*     */       }
/* 144 */       ((InstrumentationLoadTimeWeaver)this.loadTimeWeaver).removeTransformers();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/* 151 */     Assert.state((this.loadTimeWeaver != null), "Not initialized");
/* 152 */     this.loadTimeWeaver.addTransformer(transformer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 157 */     Assert.state((this.loadTimeWeaver != null), "Not initialized");
/* 158 */     return this.loadTimeWeaver.getInstrumentableClassLoader();
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/* 163 */     Assert.state((this.loadTimeWeaver != null), "Not initialized");
/* 164 */     return this.loadTimeWeaver.getThrowawayClassLoader();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/weaving/DefaultContextLoadTimeWeaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */