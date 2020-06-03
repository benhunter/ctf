/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.BeanReference;
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
/*     */ public class BeanComponentDefinition
/*     */   extends BeanDefinitionHolder
/*     */   implements ComponentDefinition
/*     */ {
/*     */   private BeanDefinition[] innerBeanDefinitions;
/*     */   private BeanReference[] beanReferences;
/*     */   
/*     */   public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName) {
/*  50 */     this(new BeanDefinitionHolder(beanDefinition, beanName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName, @Nullable String[] aliases) {
/*  60 */     this(new BeanDefinitionHolder(beanDefinition, beanName, aliases));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanComponentDefinition(BeanDefinitionHolder beanDefinitionHolder) {
/*  69 */     super(beanDefinitionHolder);
/*     */     
/*  71 */     List<BeanDefinition> innerBeans = new ArrayList<>();
/*  72 */     List<BeanReference> references = new ArrayList<>();
/*  73 */     MutablePropertyValues mutablePropertyValues = beanDefinitionHolder.getBeanDefinition().getPropertyValues();
/*  74 */     for (PropertyValue propertyValue : mutablePropertyValues.getPropertyValues()) {
/*  75 */       Object value = propertyValue.getValue();
/*  76 */       if (value instanceof BeanDefinitionHolder) {
/*  77 */         innerBeans.add(((BeanDefinitionHolder)value).getBeanDefinition());
/*     */       }
/*  79 */       else if (value instanceof BeanDefinition) {
/*  80 */         innerBeans.add((BeanDefinition)value);
/*     */       }
/*  82 */       else if (value instanceof BeanReference) {
/*  83 */         references.add((BeanReference)value);
/*     */       } 
/*     */     } 
/*  86 */     this.innerBeanDefinitions = innerBeans.<BeanDefinition>toArray(new BeanDefinition[0]);
/*  87 */     this.beanReferences = references.<BeanReference>toArray(new BeanReference[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  93 */     return getBeanName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  98 */     return getShortDescription();
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDefinition[] getBeanDefinitions() {
/* 103 */     return new BeanDefinition[] { getBeanDefinition() };
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDefinition[] getInnerBeanDefinitions() {
/* 108 */     return this.innerBeanDefinitions;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanReference[] getBeanReferences() {
/* 113 */     return this.beanReferences;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 123 */     return getDescription();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 132 */     return (this == other || (other instanceof BeanComponentDefinition && super.equals(other)));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/BeanComponentDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */