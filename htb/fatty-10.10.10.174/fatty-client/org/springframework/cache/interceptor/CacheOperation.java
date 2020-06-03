/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ public abstract class CacheOperation
/*     */   implements BasicOperation
/*     */ {
/*     */   private final String name;
/*     */   private final Set<String> cacheNames;
/*     */   private final String key;
/*     */   private final String keyGenerator;
/*     */   private final String cacheManager;
/*     */   private final String cacheResolver;
/*     */   private final String condition;
/*     */   private final String toString;
/*     */   
/*     */   protected CacheOperation(Builder b) {
/*  57 */     this.name = b.name;
/*  58 */     this.cacheNames = b.cacheNames;
/*  59 */     this.key = b.key;
/*  60 */     this.keyGenerator = b.keyGenerator;
/*  61 */     this.cacheManager = b.cacheManager;
/*  62 */     this.cacheResolver = b.cacheResolver;
/*  63 */     this.condition = b.condition;
/*  64 */     this.toString = b.getOperationDescription().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  69 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getCacheNames() {
/*  74 */     return this.cacheNames;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  78 */     return this.key;
/*     */   }
/*     */   
/*     */   public String getKeyGenerator() {
/*  82 */     return this.keyGenerator;
/*     */   }
/*     */   
/*     */   public String getCacheManager() {
/*  86 */     return this.cacheManager;
/*     */   }
/*     */   
/*     */   public String getCacheResolver() {
/*  90 */     return this.cacheResolver;
/*     */   }
/*     */   
/*     */   public String getCondition() {
/*  94 */     return this.condition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 104 */     return (other instanceof CacheOperation && toString().equals(other.toString()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 113 */     return toString().hashCode();
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
/*     */   public final String toString() {
/* 125 */     return this.toString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Builder
/*     */   {
/* 135 */     private String name = "";
/*     */     
/* 137 */     private Set<String> cacheNames = Collections.emptySet();
/*     */     
/* 139 */     private String key = "";
/*     */     
/* 141 */     private String keyGenerator = "";
/*     */     
/* 143 */     private String cacheManager = "";
/*     */     
/* 145 */     private String cacheResolver = "";
/*     */     
/* 147 */     private String condition = "";
/*     */     
/*     */     public void setName(String name) {
/* 150 */       Assert.hasText(name, "Name must not be empty");
/* 151 */       this.name = name;
/*     */     }
/*     */     
/*     */     public void setCacheName(String cacheName) {
/* 155 */       Assert.hasText(cacheName, "Cache name must not be empty");
/* 156 */       this.cacheNames = Collections.singleton(cacheName);
/*     */     }
/*     */     
/*     */     public void setCacheNames(String... cacheNames) {
/* 160 */       this.cacheNames = new LinkedHashSet<>(cacheNames.length);
/* 161 */       for (String cacheName : cacheNames) {
/* 162 */         Assert.hasText(cacheName, "Cache name must be non-empty if specified");
/* 163 */         this.cacheNames.add(cacheName);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Set<String> getCacheNames() {
/* 168 */       return this.cacheNames;
/*     */     }
/*     */     
/*     */     public void setKey(String key) {
/* 172 */       Assert.notNull(key, "Key must not be null");
/* 173 */       this.key = key;
/*     */     }
/*     */     
/*     */     public String getKey() {
/* 177 */       return this.key;
/*     */     }
/*     */     
/*     */     public String getKeyGenerator() {
/* 181 */       return this.keyGenerator;
/*     */     }
/*     */     
/*     */     public String getCacheManager() {
/* 185 */       return this.cacheManager;
/*     */     }
/*     */     
/*     */     public String getCacheResolver() {
/* 189 */       return this.cacheResolver;
/*     */     }
/*     */     
/*     */     public void setKeyGenerator(String keyGenerator) {
/* 193 */       Assert.notNull(keyGenerator, "KeyGenerator name must not be null");
/* 194 */       this.keyGenerator = keyGenerator;
/*     */     }
/*     */     
/*     */     public void setCacheManager(String cacheManager) {
/* 198 */       Assert.notNull(cacheManager, "CacheManager name must not be null");
/* 199 */       this.cacheManager = cacheManager;
/*     */     }
/*     */     
/*     */     public void setCacheResolver(String cacheResolver) {
/* 203 */       Assert.notNull(cacheResolver, "CacheResolver name must not be null");
/* 204 */       this.cacheResolver = cacheResolver;
/*     */     }
/*     */     
/*     */     public void setCondition(String condition) {
/* 208 */       Assert.notNull(condition, "Condition must not be null");
/* 209 */       this.condition = condition;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected StringBuilder getOperationDescription() {
/* 217 */       StringBuilder result = new StringBuilder(getClass().getSimpleName());
/* 218 */       result.append("[").append(this.name);
/* 219 */       result.append("] caches=").append(this.cacheNames);
/* 220 */       result.append(" | key='").append(this.key);
/* 221 */       result.append("' | keyGenerator='").append(this.keyGenerator);
/* 222 */       result.append("' | cacheManager='").append(this.cacheManager);
/* 223 */       result.append("' | cacheResolver='").append(this.cacheResolver);
/* 224 */       result.append("' | condition='").append(this.condition).append("'");
/* 225 */       return result;
/*     */     }
/*     */     
/*     */     public abstract CacheOperation build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */