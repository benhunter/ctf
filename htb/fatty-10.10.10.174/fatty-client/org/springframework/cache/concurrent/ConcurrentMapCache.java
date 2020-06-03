/*     */ package org.springframework.cache.concurrent;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.support.AbstractValueAdaptingCache;
/*     */ import org.springframework.core.serializer.support.SerializationDelegate;
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
/*     */ 
/*     */ 
/*     */ public class ConcurrentMapCache
/*     */   extends AbstractValueAdaptingCache
/*     */ {
/*     */   private final String name;
/*     */   private final ConcurrentMap<Object, Object> store;
/*     */   @Nullable
/*     */   private final SerializationDelegate serialization;
/*     */   
/*     */   public ConcurrentMapCache(String name) {
/*  64 */     this(name, new ConcurrentHashMap<>(256), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentMapCache(String name, boolean allowNullValues) {
/*  74 */     this(name, new ConcurrentHashMap<>(256), allowNullValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
/*  86 */     this(name, store, allowNullValues, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues, @Nullable SerializationDelegate serialization) {
/* 105 */     super(allowNullValues);
/* 106 */     Assert.notNull(name, "Name must not be null");
/* 107 */     Assert.notNull(store, "Store must not be null");
/* 108 */     this.name = name;
/* 109 */     this.store = store;
/* 110 */     this.serialization = serialization;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isStoreByValue() {
/* 121 */     return (this.serialization != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 126 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ConcurrentMap<Object, Object> getNativeCache() {
/* 131 */     return this.store;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object lookup(Object key) {
/* 137 */     return this.store.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T get(Object key, Callable<T> valueLoader) {
/* 144 */     return (T)fromStoreValue(this.store.computeIfAbsent(key, k -> {
/*     */             
/*     */             try {
/*     */               return toStoreValue(valueLoader.call());
/* 148 */             } catch (Throwable ex) {
/*     */               throw new Cache.ValueRetrievalException(key, valueLoader, ex);
/*     */             } 
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(Object key, @Nullable Object value) {
/* 156 */     this.store.put(key, toStoreValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Cache.ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
/* 162 */     Object existing = this.store.putIfAbsent(key, toStoreValue(value));
/* 163 */     return toValueWrapper(existing);
/*     */   }
/*     */ 
/*     */   
/*     */   public void evict(Object key) {
/* 168 */     this.store.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 173 */     this.store.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object toStoreValue(@Nullable Object userValue) {
/* 178 */     Object storeValue = super.toStoreValue(userValue);
/* 179 */     if (this.serialization != null) {
/*     */       try {
/* 181 */         return serializeValue(this.serialization, storeValue);
/*     */       }
/* 183 */       catch (Throwable ex) {
/* 184 */         throw new IllegalArgumentException("Failed to serialize cache value '" + userValue + "'. Does it implement Serializable?", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 189 */     return storeValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object serializeValue(SerializationDelegate serialization, Object storeValue) throws IOException {
/* 194 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */     try {
/* 196 */       serialization.serialize(storeValue, out);
/* 197 */       return out.toByteArray();
/*     */     } finally {
/*     */       
/* 200 */       out.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object fromStoreValue(@Nullable Object storeValue) {
/* 206 */     if (storeValue != null && this.serialization != null) {
/*     */       try {
/* 208 */         return super.fromStoreValue(deserializeValue(this.serialization, storeValue));
/*     */       }
/* 210 */       catch (Throwable ex) {
/* 211 */         throw new IllegalArgumentException("Failed to deserialize cache value '" + storeValue + "'", ex);
/*     */       } 
/*     */     }
/*     */     
/* 215 */     return super.fromStoreValue(storeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object deserializeValue(SerializationDelegate serialization, Object storeValue) throws IOException {
/* 221 */     ByteArrayInputStream in = new ByteArrayInputStream((byte[])storeValue);
/*     */     try {
/* 223 */       return serialization.deserialize(in);
/*     */     } finally {
/*     */       
/* 226 */       in.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/concurrent/ConcurrentMapCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */