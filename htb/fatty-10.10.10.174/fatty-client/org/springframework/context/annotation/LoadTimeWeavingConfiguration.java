/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.weaving.AspectJWeavingEnabler;
/*     */ import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
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
/*     */ @Configuration
/*     */ public class LoadTimeWeavingConfiguration
/*     */   implements ImportAware, BeanClassLoaderAware
/*     */ {
/*     */   @Nullable
/*     */   private AnnotationAttributes enableLTW;
/*     */   @Nullable
/*     */   private LoadTimeWeavingConfigurer ltwConfigurer;
/*     */   @Nullable
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*     */   public void setImportMetadata(AnnotationMetadata importMetadata) {
/*  59 */     this.enableLTW = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)importMetadata, EnableLoadTimeWeaving.class);
/*  60 */     if (this.enableLTW == null) {
/*  61 */       throw new IllegalArgumentException("@EnableLoadTimeWeaving is not present on importing class " + importMetadata
/*  62 */           .getClassName());
/*     */     }
/*     */   }
/*     */   
/*     */   @Autowired(required = false)
/*     */   public void setLoadTimeWeavingConfigurer(LoadTimeWeavingConfigurer ltwConfigurer) {
/*  68 */     this.ltwConfigurer = ltwConfigurer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/*  73 */     this.beanClassLoader = beanClassLoader;
/*     */   }
/*     */   
/*     */   @Bean(name = {"loadTimeWeaver"})
/*     */   @Role(2)
/*     */   public LoadTimeWeaver loadTimeWeaver() {
/*     */     DefaultContextLoadTimeWeaver defaultContextLoadTimeWeaver;
/*  80 */     Assert.state((this.beanClassLoader != null), "No ClassLoader set");
/*  81 */     LoadTimeWeaver loadTimeWeaver = null;
/*     */     
/*  83 */     if (this.ltwConfigurer != null)
/*     */     {
/*  85 */       loadTimeWeaver = this.ltwConfigurer.getLoadTimeWeaver();
/*     */     }
/*     */     
/*  88 */     if (loadTimeWeaver == null)
/*     */     {
/*  90 */       defaultContextLoadTimeWeaver = new DefaultContextLoadTimeWeaver(this.beanClassLoader);
/*     */     }
/*     */     
/*  93 */     if (this.enableLTW != null) {
/*  94 */       EnableLoadTimeWeaving.AspectJWeaving aspectJWeaving = (EnableLoadTimeWeaving.AspectJWeaving)this.enableLTW.getEnum("aspectjWeaving");
/*  95 */       switch (aspectJWeaving) {
/*     */ 
/*     */ 
/*     */         
/*     */         case AUTODETECT:
/* 100 */           if (this.beanClassLoader.getResource("META-INF/aop.xml") == null) {
/*     */             break;
/*     */           }
/*     */ 
/*     */           
/* 105 */           AspectJWeavingEnabler.enableAspectJWeaving((LoadTimeWeaver)defaultContextLoadTimeWeaver, this.beanClassLoader);
/*     */           break;
/*     */         case ENABLED:
/* 108 */           AspectJWeavingEnabler.enableAspectJWeaving((LoadTimeWeaver)defaultContextLoadTimeWeaver, this.beanClassLoader);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 113 */     return (LoadTimeWeaver)defaultContextLoadTimeWeaver;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/LoadTimeWeavingConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */