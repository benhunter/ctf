/*    */ package org.springframework.cache.annotation;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.function.Supplier;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.cache.interceptor.CacheErrorHandler;
/*    */ import org.springframework.cache.interceptor.CacheResolver;
/*    */ import org.springframework.cache.interceptor.KeyGenerator;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.ImportAware;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ public abstract class AbstractCachingConfiguration
/*    */   implements ImportAware
/*    */ {
/*    */   @Nullable
/*    */   protected AnnotationAttributes enableCaching;
/*    */   @Nullable
/*    */   protected Supplier<CacheManager> cacheManager;
/*    */   @Nullable
/*    */   protected Supplier<CacheResolver> cacheResolver;
/*    */   @Nullable
/*    */   protected Supplier<KeyGenerator> keyGenerator;
/*    */   @Nullable
/*    */   protected Supplier<CacheErrorHandler> errorHandler;
/*    */   
/*    */   public void setImportMetadata(AnnotationMetadata importMetadata) {
/* 65 */     this.enableCaching = AnnotationAttributes.fromMap(importMetadata
/* 66 */         .getAnnotationAttributes(EnableCaching.class.getName(), false));
/* 67 */     if (this.enableCaching == null) {
/* 68 */       throw new IllegalArgumentException("@EnableCaching is not present on importing class " + importMetadata
/* 69 */           .getClassName());
/*    */     }
/*    */   }
/*    */   
/*    */   @Autowired(required = false)
/*    */   void setConfigurers(Collection<CachingConfigurer> configurers) {
/* 75 */     if (CollectionUtils.isEmpty(configurers)) {
/*    */       return;
/*    */     }
/* 78 */     if (configurers.size() > 1) {
/* 79 */       throw new IllegalStateException(configurers.size() + " implementations of CachingConfigurer were found when only 1 was expected. Refactor the configuration such that CachingConfigurer is implemented only once or not at all.");
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 84 */     CachingConfigurer configurer = configurers.iterator().next();
/* 85 */     useCachingConfigurer(configurer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void useCachingConfigurer(CachingConfigurer config) {
/* 92 */     this.cacheManager = config::cacheManager;
/* 93 */     this.cacheResolver = config::cacheResolver;
/* 94 */     this.keyGenerator = config::keyGenerator;
/* 95 */     this.errorHandler = config::errorHandler;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/annotation/AbstractCachingConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */