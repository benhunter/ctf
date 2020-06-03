/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
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
/*     */ public class BeanFactoryAspectJAdvisorsBuilder
/*     */ {
/*     */   private final ListableBeanFactory beanFactory;
/*     */   private final AspectJAdvisorFactory advisorFactory;
/*     */   @Nullable
/*     */   private volatile List<String> aspectBeanNames;
/*  50 */   private final Map<String, List<Advisor>> advisorsCache = new ConcurrentHashMap<>();
/*     */   
/*  52 */   private final Map<String, MetadataAwareAspectInstanceFactory> aspectFactoryCache = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory) {
/*  60 */     this(beanFactory, new ReflectiveAspectJAdvisorFactory((BeanFactory)beanFactory));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory, AspectJAdvisorFactory advisorFactory) {
/*  69 */     Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
/*  70 */     Assert.notNull(advisorFactory, "AspectJAdvisorFactory must not be null");
/*  71 */     this.beanFactory = beanFactory;
/*  72 */     this.advisorFactory = advisorFactory;
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
/*     */   public List<Advisor> buildAspectJAdvisors() {
/*  84 */     List<String> aspectNames = this.aspectBeanNames;
/*     */     
/*  86 */     if (aspectNames == null) {
/*  87 */       synchronized (this) {
/*  88 */         aspectNames = this.aspectBeanNames;
/*  89 */         if (aspectNames == null) {
/*  90 */           List<Advisor> list = new ArrayList<>();
/*  91 */           aspectNames = new ArrayList<>();
/*  92 */           String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, Object.class, true, false);
/*     */           
/*  94 */           for (String beanName : beanNames) {
/*  95 */             if (isEligibleBean(beanName)) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 100 */               Class<?> beanType = this.beanFactory.getType(beanName);
/* 101 */               if (beanType != null)
/*     */               {
/*     */                 
/* 104 */                 if (this.advisorFactory.isAspect(beanType)) {
/* 105 */                   aspectNames.add(beanName);
/* 106 */                   AspectMetadata amd = new AspectMetadata(beanType, beanName);
/* 107 */                   if (amd.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
/* 108 */                     MetadataAwareAspectInstanceFactory factory = new BeanFactoryAspectInstanceFactory((BeanFactory)this.beanFactory, beanName);
/*     */                     
/* 110 */                     List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
/* 111 */                     if (this.beanFactory.isSingleton(beanName)) {
/* 112 */                       this.advisorsCache.put(beanName, classAdvisors);
/*     */                     } else {
/*     */                       
/* 115 */                       this.aspectFactoryCache.put(beanName, factory);
/*     */                     } 
/* 117 */                     list.addAll(classAdvisors);
/*     */                   }
/*     */                   else {
/*     */                     
/* 121 */                     if (this.beanFactory.isSingleton(beanName)) {
/* 122 */                       throw new IllegalArgumentException("Bean with name '" + beanName + "' is a singleton, but aspect instantiation model is not singleton");
/*     */                     }
/*     */                     
/* 125 */                     MetadataAwareAspectInstanceFactory factory = new PrototypeAspectInstanceFactory((BeanFactory)this.beanFactory, beanName);
/*     */                     
/* 127 */                     this.aspectFactoryCache.put(beanName, factory);
/* 128 */                     list.addAll(this.advisorFactory.getAdvisors(factory));
/*     */                   } 
/*     */                 }  } 
/*     */             } 
/* 132 */           }  this.aspectBeanNames = aspectNames;
/* 133 */           return list;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 138 */     if (aspectNames.isEmpty()) {
/* 139 */       return Collections.emptyList();
/*     */     }
/* 141 */     List<Advisor> advisors = new ArrayList<>();
/* 142 */     for (String aspectName : aspectNames) {
/* 143 */       List<Advisor> cachedAdvisors = this.advisorsCache.get(aspectName);
/* 144 */       if (cachedAdvisors != null) {
/* 145 */         advisors.addAll(cachedAdvisors);
/*     */         continue;
/*     */       } 
/* 148 */       MetadataAwareAspectInstanceFactory factory = this.aspectFactoryCache.get(aspectName);
/* 149 */       advisors.addAll(this.advisorFactory.getAdvisors(factory));
/*     */     } 
/*     */     
/* 152 */     return advisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleBean(String beanName) {
/* 161 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/BeanFactoryAspectJAdvisorsBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */