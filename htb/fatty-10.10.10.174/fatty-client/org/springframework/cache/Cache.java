/*     */ package org.springframework.cache;
/*     */ 
/*     */ import java.util.concurrent.Callable;
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
/*     */ public interface Cache
/*     */ {
/*     */   String getName();
/*     */   
/*     */   Object getNativeCache();
/*     */   
/*     */   @Nullable
/*     */   ValueWrapper get(Object paramObject);
/*     */   
/*     */   @Nullable
/*     */   <T> T get(Object paramObject, @Nullable Class<T> paramClass);
/*     */   
/*     */   @Nullable
/*     */   <T> T get(Object paramObject, Callable<T> paramCallable);
/*     */   
/*     */   void put(Object paramObject1, @Nullable Object paramObject2);
/*     */   
/*     */   @Nullable
/*     */   ValueWrapper putIfAbsent(Object paramObject1, @Nullable Object paramObject2);
/*     */   
/*     */   void evict(Object paramObject);
/*     */   
/*     */   void clear();
/*     */   
/*     */   public static class ValueRetrievalException
/*     */     extends RuntimeException
/*     */   {
/*     */     @Nullable
/*     */     private final Object key;
/*     */     
/*     */     public ValueRetrievalException(@Nullable Object key, Callable<?> loader, Throwable ex) {
/* 178 */       super(String.format("Value for key '%s' could not be loaded using '%s'", new Object[] { key, loader }), ex);
/* 179 */       this.key = key;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Object getKey() {
/* 184 */       return this.key;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ValueWrapper {
/*     */     @Nullable
/*     */     Object get();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/Cache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */