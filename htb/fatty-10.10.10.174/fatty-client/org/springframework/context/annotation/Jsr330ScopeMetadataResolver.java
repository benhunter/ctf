/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
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
/*     */ public class Jsr330ScopeMetadataResolver
/*     */   implements ScopeMetadataResolver
/*     */ {
/*  45 */   private final Map<String, String> scopeMap = new HashMap<>();
/*     */ 
/*     */   
/*     */   public Jsr330ScopeMetadataResolver() {
/*  49 */     registerScope("javax.inject.Singleton", "singleton");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void registerScope(Class<?> annotationType, String scopeName) {
/*  60 */     this.scopeMap.put(annotationType.getName(), scopeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void registerScope(String annotationType, String scopeName) {
/*  70 */     this.scopeMap.put(annotationType, scopeName);
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
/*     */   protected String resolveScopeName(String annotationType) {
/*  82 */     return this.scopeMap.get(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
/*  88 */     ScopeMetadata metadata = new ScopeMetadata();
/*  89 */     metadata.setScopeName("prototype");
/*  90 */     if (definition instanceof AnnotatedBeanDefinition) {
/*  91 */       AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition)definition;
/*  92 */       Set<String> annTypes = annDef.getMetadata().getAnnotationTypes();
/*  93 */       String found = null;
/*  94 */       for (String annType : annTypes) {
/*  95 */         Set<String> metaAnns = annDef.getMetadata().getMetaAnnotationTypes(annType);
/*  96 */         if (metaAnns.contains("javax.inject.Scope")) {
/*  97 */           if (found != null) {
/*  98 */             throw new IllegalStateException("Found ambiguous scope annotations on bean class [" + definition
/*  99 */                 .getBeanClassName() + "]: " + found + ", " + annType);
/*     */           }
/* 101 */           found = annType;
/* 102 */           String scopeName = resolveScopeName(annType);
/* 103 */           if (scopeName == null) {
/* 104 */             throw new IllegalStateException("Unsupported scope annotation - not mapped onto Spring scope name: " + annType);
/*     */           }
/*     */           
/* 107 */           metadata.setScopeName(scopeName);
/*     */         } 
/*     */       } 
/*     */     } 
/* 111 */     return metadata;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/Jsr330ScopeMetadataResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */