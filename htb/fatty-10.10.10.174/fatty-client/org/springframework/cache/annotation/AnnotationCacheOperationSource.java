/*     */ package org.springframework.cache.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.cache.interceptor.AbstractFallbackCacheOperationSource;
/*     */ import org.springframework.cache.interceptor.CacheOperation;
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
/*     */ public class AnnotationCacheOperationSource
/*     */   extends AbstractFallbackCacheOperationSource
/*     */   implements Serializable
/*     */ {
/*     */   private final boolean publicMethodsOnly;
/*     */   private final Set<CacheAnnotationParser> annotationParsers;
/*     */   
/*     */   public AnnotationCacheOperationSource() {
/*  60 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationCacheOperationSource(boolean publicMethodsOnly) {
/*  71 */     this.publicMethodsOnly = publicMethodsOnly;
/*  72 */     this.annotationParsers = Collections.singleton(new SpringCacheAnnotationParser());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationCacheOperationSource(CacheAnnotationParser annotationParser) {
/*  80 */     this.publicMethodsOnly = true;
/*  81 */     Assert.notNull(annotationParser, "CacheAnnotationParser must not be null");
/*  82 */     this.annotationParsers = Collections.singleton(annotationParser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationCacheOperationSource(CacheAnnotationParser... annotationParsers) {
/*  90 */     this.publicMethodsOnly = true;
/*  91 */     Assert.notEmpty((Object[])annotationParsers, "At least one CacheAnnotationParser needs to be specified");
/*  92 */     this.annotationParsers = new LinkedHashSet<>(Arrays.asList(annotationParsers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationCacheOperationSource(Set<CacheAnnotationParser> annotationParsers) {
/* 100 */     this.publicMethodsOnly = true;
/* 101 */     Assert.notEmpty(annotationParsers, "At least one CacheAnnotationParser needs to be specified");
/* 102 */     this.annotationParsers = annotationParsers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Collection<CacheOperation> findCacheOperations(Class<?> clazz) {
/* 109 */     return determineCacheOperations(parser -> parser.parseCacheAnnotations(clazz));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Collection<CacheOperation> findCacheOperations(Method method) {
/* 115 */     return determineCacheOperations(parser -> parser.parseCacheAnnotations(method));
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
/*     */   @Nullable
/*     */   protected Collection<CacheOperation> determineCacheOperations(CacheOperationProvider provider) {
/* 129 */     Collection<CacheOperation> ops = null;
/* 130 */     for (CacheAnnotationParser annotationParser : this.annotationParsers) {
/* 131 */       Collection<CacheOperation> annOps = provider.getCacheOperations(annotationParser);
/* 132 */       if (annOps != null) {
/* 133 */         if (ops == null) {
/* 134 */           ops = annOps;
/*     */           continue;
/*     */         } 
/* 137 */         Collection<CacheOperation> combined = new ArrayList<>(ops.size() + annOps.size());
/* 138 */         combined.addAll(ops);
/* 139 */         combined.addAll(annOps);
/* 140 */         ops = combined;
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     return ops;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean allowPublicMethodsOnly() {
/* 152 */     return this.publicMethodsOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 158 */     if (this == other) {
/* 159 */       return true;
/*     */     }
/* 161 */     if (!(other instanceof AnnotationCacheOperationSource)) {
/* 162 */       return false;
/*     */     }
/* 164 */     AnnotationCacheOperationSource otherCos = (AnnotationCacheOperationSource)other;
/* 165 */     return (this.annotationParsers.equals(otherCos.annotationParsers) && this.publicMethodsOnly == otherCos.publicMethodsOnly);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 171 */     return this.annotationParsers.hashCode();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   protected static interface CacheOperationProvider {
/*     */     @Nullable
/*     */     Collection<CacheOperation> getCacheOperations(CacheAnnotationParser param1CacheAnnotationParser);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/annotation/AnnotationCacheOperationSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */