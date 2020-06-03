/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
/*     */ public abstract class AbstractBeanFactoryPointcutAdvisor
/*     */   extends AbstractPointcutAdvisor
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   @Nullable
/*     */   private String adviceBeanName;
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */   @Nullable
/*     */   private volatile transient Advice advice;
/*  55 */   private volatile transient Object adviceMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdviceBeanName(@Nullable String adviceBeanName) {
/*  67 */     this.adviceBeanName = adviceBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getAdviceBeanName() {
/*  75 */     return this.adviceBeanName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  80 */     this.beanFactory = beanFactory;
/*  81 */     resetAdviceMonitor();
/*     */   }
/*     */   
/*     */   private void resetAdviceMonitor() {
/*  85 */     if (this.beanFactory instanceof ConfigurableBeanFactory) {
/*  86 */       this.adviceMonitor = ((ConfigurableBeanFactory)this.beanFactory).getSingletonMutex();
/*     */     } else {
/*     */       
/*  89 */       this.adviceMonitor = new Object();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvice(Advice advice) {
/*  99 */     synchronized (this.adviceMonitor) {
/* 100 */       this.advice = advice;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Advice getAdvice() {
/* 106 */     Advice advice = this.advice;
/* 107 */     if (advice != null) {
/* 108 */       return advice;
/*     */     }
/*     */     
/* 111 */     Assert.state((this.adviceBeanName != null), "'adviceBeanName' must be specified");
/* 112 */     Assert.state((this.beanFactory != null), "BeanFactory must be set to resolve 'adviceBeanName'");
/*     */     
/* 114 */     if (this.beanFactory.isSingleton(this.adviceBeanName)) {
/*     */       
/* 116 */       advice = (Advice)this.beanFactory.getBean(this.adviceBeanName, Advice.class);
/* 117 */       this.advice = advice;
/* 118 */       return advice;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     synchronized (this.adviceMonitor) {
/* 125 */       advice = this.advice;
/* 126 */       if (advice == null) {
/* 127 */         advice = (Advice)this.beanFactory.getBean(this.adviceBeanName, Advice.class);
/* 128 */         this.advice = advice;
/*     */       } 
/* 130 */       return advice;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 138 */     sb.append(": advice ");
/* 139 */     if (this.adviceBeanName != null) {
/* 140 */       sb.append("bean '").append(this.adviceBeanName).append("'");
/*     */     } else {
/*     */       
/* 143 */       sb.append(this.advice);
/*     */     } 
/* 145 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 155 */     ois.defaultReadObject();
/*     */ 
/*     */     
/* 158 */     resetAdviceMonitor();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/AbstractBeanFactoryPointcutAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */