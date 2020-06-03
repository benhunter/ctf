/*     */ package org.springframework.cache.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class ConcurrentMapCacheFactoryBean
/*     */   implements FactoryBean<ConcurrentMapCache>, BeanNameAware, InitializingBean
/*     */ {
/*  43 */   private String name = "";
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ConcurrentMap<Object, Object> store;
/*     */ 
/*     */   
/*     */   private boolean allowNullValues = true;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ConcurrentMapCache cache;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  59 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStore(ConcurrentMap<Object, Object> store) {
/*  68 */     this.store = store;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowNullValues(boolean allowNullValues) {
/*  77 */     this.allowNullValues = allowNullValues;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String beanName) {
/*  82 */     if (!StringUtils.hasLength(this.name)) {
/*  83 */       setName(beanName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  89 */     this.cache = (this.store != null) ? new ConcurrentMapCache(this.name, this.store, this.allowNullValues) : new ConcurrentMapCache(this.name, this.allowNullValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ConcurrentMapCache getObject() {
/*  97 */     return this.cache;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 102 */     return ConcurrentMapCache.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 107 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/concurrent/ConcurrentMapCacheFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */