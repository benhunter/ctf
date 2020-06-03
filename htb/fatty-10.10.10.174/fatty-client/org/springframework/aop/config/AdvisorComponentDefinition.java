/*     */ package org.springframework.aop.config;
/*     */ 
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanReference;
/*     */ import org.springframework.beans.factory.parsing.AbstractComponentDefinition;
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
/*     */ public class AdvisorComponentDefinition
/*     */   extends AbstractComponentDefinition
/*     */ {
/*     */   private final String advisorBeanName;
/*     */   private final BeanDefinition advisorDefinition;
/*     */   private final String description;
/*     */   private final BeanReference[] beanReferences;
/*     */   private final BeanDefinition[] beanDefinitions;
/*     */   
/*     */   public AdvisorComponentDefinition(String advisorBeanName, BeanDefinition advisorDefinition) {
/*  50 */     this(advisorBeanName, advisorDefinition, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AdvisorComponentDefinition(String advisorBeanName, BeanDefinition advisorDefinition, @Nullable BeanDefinition pointcutDefinition) {
/*  56 */     Assert.notNull(advisorBeanName, "'advisorBeanName' must not be null");
/*  57 */     Assert.notNull(advisorDefinition, "'advisorDefinition' must not be null");
/*  58 */     this.advisorBeanName = advisorBeanName;
/*  59 */     this.advisorDefinition = advisorDefinition;
/*     */     
/*  61 */     MutablePropertyValues pvs = advisorDefinition.getPropertyValues();
/*  62 */     BeanReference adviceReference = (BeanReference)pvs.get("adviceBeanName");
/*  63 */     Assert.state((adviceReference != null), "Missing 'adviceBeanName' property");
/*     */     
/*  65 */     if (pointcutDefinition != null) {
/*  66 */       this.beanReferences = new BeanReference[] { adviceReference };
/*  67 */       this.beanDefinitions = new BeanDefinition[] { advisorDefinition, pointcutDefinition };
/*  68 */       this.description = buildDescription(adviceReference, pointcutDefinition);
/*     */     } else {
/*     */       
/*  71 */       BeanReference pointcutReference = (BeanReference)pvs.get("pointcut");
/*  72 */       Assert.state((pointcutReference != null), "Missing 'pointcut' property");
/*  73 */       this.beanReferences = new BeanReference[] { adviceReference, pointcutReference };
/*  74 */       this.beanDefinitions = new BeanDefinition[] { advisorDefinition };
/*  75 */       this.description = buildDescription(adviceReference, pointcutReference);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String buildDescription(BeanReference adviceReference, BeanDefinition pointcutDefinition) {
/*  80 */     return "Advisor <advice(ref)='" + adviceReference
/*  81 */       .getBeanName() + "', pointcut(expression)=[" + pointcutDefinition
/*  82 */       .getPropertyValues().get("expression") + "]>";
/*     */   }
/*     */   
/*     */   private String buildDescription(BeanReference adviceReference, BeanReference pointcutReference) {
/*  86 */     return "Advisor <advice(ref)='" + adviceReference
/*  87 */       .getBeanName() + "', pointcut(ref)='" + pointcutReference
/*  88 */       .getBeanName() + "'>";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  94 */     return this.advisorBeanName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  99 */     return this.description;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDefinition[] getBeanDefinitions() {
/* 104 */     return this.beanDefinitions;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanReference[] getBeanReferences() {
/* 109 */     return this.beanReferences;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/* 115 */     return this.advisorDefinition.getSource();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/AdvisorComponentDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */