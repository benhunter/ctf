/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NameMatchCacheOperationSource
/*     */   implements CacheOperationSource, Serializable
/*     */ {
/*  46 */   protected static final Log logger = LogFactory.getLog(NameMatchCacheOperationSource.class);
/*     */ 
/*     */ 
/*     */   
/*  50 */   private Map<String, Collection<CacheOperation>> nameMap = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNameMap(Map<String, Collection<CacheOperation>> nameMap) {
/*  60 */     nameMap.forEach(this::addCacheMethod);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCacheMethod(String methodName, Collection<CacheOperation> ops) {
/*  71 */     if (logger.isDebugEnabled()) {
/*  72 */       logger.debug("Adding method [" + methodName + "] with cache operations [" + ops + "]");
/*     */     }
/*  74 */     this.nameMap.put(methodName, ops);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Collection<CacheOperation> getCacheOperations(Method method, @Nullable Class<?> targetClass) {
/*  81 */     String methodName = method.getName();
/*  82 */     Collection<CacheOperation> ops = this.nameMap.get(methodName);
/*     */     
/*  84 */     if (ops == null) {
/*     */       
/*  86 */       String bestNameMatch = null;
/*  87 */       for (String mappedName : this.nameMap.keySet()) {
/*  88 */         if (isMatch(methodName, mappedName) && (bestNameMatch == null || bestNameMatch
/*  89 */           .length() <= mappedName.length())) {
/*  90 */           ops = this.nameMap.get(mappedName);
/*  91 */           bestNameMatch = mappedName;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     return ops;
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
/*     */   protected boolean isMatch(String methodName, String mappedName) {
/* 109 */     return PatternMatchUtils.simpleMatch(mappedName, methodName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 114 */     if (this == other) {
/* 115 */       return true;
/*     */     }
/* 117 */     if (!(other instanceof NameMatchCacheOperationSource)) {
/* 118 */       return false;
/*     */     }
/* 120 */     NameMatchCacheOperationSource otherTas = (NameMatchCacheOperationSource)other;
/* 121 */     return ObjectUtils.nullSafeEquals(this.nameMap, otherTas.nameMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 126 */     return NameMatchCacheOperationSource.class.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 131 */     return getClass().getName() + ": " + this.nameMap;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/NameMatchCacheOperationSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */