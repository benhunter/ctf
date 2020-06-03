/*    */ package org.springframework.cache.annotation;
/*    */ 
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
/*    */ import org.springframework.cache.interceptor.CacheInterceptor;
/*    */ import org.springframework.cache.interceptor.CacheOperationSource;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Role;
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
/*    */ @Role(2)
/*    */ public class ProxyCachingConfiguration
/*    */   extends AbstractCachingConfiguration
/*    */ {
/*    */   @Bean(name = {"org.springframework.cache.config.internalCacheAdvisor"})
/*    */   @Role(2)
/*    */   public BeanFactoryCacheOperationSourceAdvisor cacheAdvisor() {
/* 45 */     BeanFactoryCacheOperationSourceAdvisor advisor = new BeanFactoryCacheOperationSourceAdvisor();
/* 46 */     advisor.setCacheOperationSource(cacheOperationSource());
/* 47 */     advisor.setAdvice((Advice)cacheInterceptor());
/* 48 */     if (this.enableCaching != null) {
/* 49 */       advisor.setOrder(((Integer)this.enableCaching.getNumber("order")).intValue());
/*    */     }
/* 51 */     return advisor;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @Role(2)
/*    */   public CacheOperationSource cacheOperationSource() {
/* 57 */     return (CacheOperationSource)new AnnotationCacheOperationSource();
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @Role(2)
/*    */   public CacheInterceptor cacheInterceptor() {
/* 63 */     CacheInterceptor interceptor = new CacheInterceptor();
/* 64 */     interceptor.configure(this.errorHandler, this.keyGenerator, this.cacheResolver, this.cacheManager);
/* 65 */     interceptor.setCacheOperationSource(cacheOperationSource());
/* 66 */     return interceptor;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/annotation/ProxyCachingConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */