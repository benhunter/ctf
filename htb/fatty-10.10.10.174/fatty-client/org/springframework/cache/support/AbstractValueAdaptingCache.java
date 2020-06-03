/*     */ package org.springframework.cache.support;
/*     */ 
/*     */ import org.springframework.cache.Cache;
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
/*     */ public abstract class AbstractValueAdaptingCache
/*     */   implements Cache
/*     */ {
/*     */   private final boolean allowNullValues;
/*     */   
/*     */   protected AbstractValueAdaptingCache(boolean allowNullValues) {
/*  44 */     this.allowNullValues = allowNullValues;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isAllowNullValues() {
/*  52 */     return this.allowNullValues;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Cache.ValueWrapper get(Object key) {
/*  58 */     Object value = lookup(key);
/*  59 */     return toValueWrapper(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T get(Object key, @Nullable Class<T> type) {
/*  66 */     Object value = fromStoreValue(lookup(key));
/*  67 */     if (value != null && type != null && !type.isInstance(value)) {
/*  68 */       throw new IllegalStateException("Cached value is not of required type [" + type
/*  69 */           .getName() + "]: " + value);
/*     */     }
/*  71 */     return (T)value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract Object lookup(Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object fromStoreValue(@Nullable Object storeValue) {
/*  91 */     if (this.allowNullValues && storeValue == NullValue.INSTANCE) {
/*  92 */       return null;
/*     */     }
/*  94 */     return storeValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object toStoreValue(@Nullable Object userValue) {
/* 104 */     if (userValue == null) {
/* 105 */       if (this.allowNullValues) {
/* 106 */         return NullValue.INSTANCE;
/*     */       }
/* 108 */       throw new IllegalArgumentException("Cache '" + 
/* 109 */           getName() + "' is configured to not allow null values but null was provided");
/*     */     } 
/* 111 */     return userValue;
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
/*     */   protected Cache.ValueWrapper toValueWrapper(@Nullable Object storeValue) {
/* 123 */     return (storeValue != null) ? new SimpleValueWrapper(fromStoreValue(storeValue)) : null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/support/AbstractValueAdaptingCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */