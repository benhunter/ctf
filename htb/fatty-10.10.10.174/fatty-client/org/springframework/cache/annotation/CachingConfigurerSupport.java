/*    */ package org.springframework.cache.annotation;
/*    */ 
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.cache.interceptor.CacheErrorHandler;
/*    */ import org.springframework.cache.interceptor.CacheResolver;
/*    */ import org.springframework.cache.interceptor.KeyGenerator;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class CachingConfigurerSupport
/*    */   implements CachingConfigurer
/*    */ {
/*    */   @Nullable
/*    */   public CacheManager cacheManager() {
/* 38 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public CacheResolver cacheResolver() {
/* 44 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public KeyGenerator keyGenerator() {
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public CacheErrorHandler errorHandler() {
/* 56 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/annotation/CachingConfigurerSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */