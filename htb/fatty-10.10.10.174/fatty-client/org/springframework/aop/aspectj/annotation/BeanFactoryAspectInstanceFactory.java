/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.OrderUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanFactoryAspectInstanceFactory
/*     */   implements MetadataAwareAspectInstanceFactory, Serializable
/*     */ {
/*     */   private final BeanFactory beanFactory;
/*     */   private final String name;
/*     */   private final AspectMetadata aspectMetadata;
/*     */   
/*     */   public BeanFactoryAspectInstanceFactory(BeanFactory beanFactory, String name) {
/*  62 */     this(beanFactory, name, null);
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
/*     */   public BeanFactoryAspectInstanceFactory(BeanFactory beanFactory, String name, @Nullable Class<?> type) {
/*  75 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/*  76 */     Assert.notNull(name, "Bean name must not be null");
/*  77 */     this.beanFactory = beanFactory;
/*  78 */     this.name = name;
/*  79 */     Class<?> resolvedType = type;
/*  80 */     if (type == null) {
/*  81 */       resolvedType = beanFactory.getType(name);
/*  82 */       Assert.notNull(resolvedType, "Unresolvable bean type - explicitly specify the aspect class");
/*     */     } 
/*  84 */     this.aspectMetadata = new AspectMetadata(resolvedType, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAspectInstance() {
/*  90 */     return this.beanFactory.getBean(this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader getAspectClassLoader() {
/*  96 */     return (this.beanFactory instanceof ConfigurableBeanFactory) ? ((ConfigurableBeanFactory)this.beanFactory)
/*  97 */       .getBeanClassLoader() : 
/*  98 */       ClassUtils.getDefaultClassLoader();
/*     */   }
/*     */ 
/*     */   
/*     */   public AspectMetadata getAspectMetadata() {
/* 103 */     return this.aspectMetadata;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getAspectCreationMutex() {
/* 109 */     if (this.beanFactory.isSingleton(this.name))
/*     */     {
/* 111 */       return null;
/*     */     }
/* 113 */     if (this.beanFactory instanceof ConfigurableBeanFactory)
/*     */     {
/*     */ 
/*     */       
/* 117 */       return ((ConfigurableBeanFactory)this.beanFactory).getSingletonMutex();
/*     */     }
/*     */     
/* 120 */     return this;
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
/*     */   public int getOrder() {
/* 136 */     Class<?> type = this.beanFactory.getType(this.name);
/* 137 */     if (type != null) {
/* 138 */       if (Ordered.class.isAssignableFrom(type) && this.beanFactory.isSingleton(this.name)) {
/* 139 */         return ((Ordered)this.beanFactory.getBean(this.name)).getOrder();
/*     */       }
/* 141 */       return OrderUtils.getOrder(type, 2147483647);
/*     */     } 
/* 143 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 149 */     return getClass().getSimpleName() + ": bean name '" + this.name + "'";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/BeanFactoryAspectInstanceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */