/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.Validation;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
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
/*     */ public class BeanValidationPostProcessor
/*     */   implements BeanPostProcessor, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private Validator validator;
/*     */   private boolean afterInitialization = false;
/*     */   
/*     */   public void setValidator(Validator validator) {
/*  55 */     this.validator = validator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidatorFactory(ValidatorFactory validatorFactory) {
/*  65 */     this.validator = validatorFactory.getValidator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAfterInitialization(boolean afterInitialization) {
/*  76 */     this.afterInitialization = afterInitialization;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  81 */     if (this.validator == null) {
/*  82 */       this.validator = Validation.buildDefaultValidatorFactory().getValidator();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/*  89 */     if (!this.afterInitialization) {
/*  90 */       doValidate(bean);
/*     */     }
/*  92 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
/*  97 */     if (this.afterInitialization) {
/*  98 */       doValidate(bean);
/*     */     }
/* 100 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doValidate(Object bean) {
/* 110 */     Assert.state((this.validator != null), "No Validator set");
/* 111 */     Object objectToValidate = AopProxyUtils.getSingletonTarget(bean);
/* 112 */     if (objectToValidate == null) {
/* 113 */       objectToValidate = bean;
/*     */     }
/* 115 */     Set<ConstraintViolation<Object>> result = this.validator.validate(objectToValidate, new Class[0]);
/*     */     
/* 117 */     if (!result.isEmpty()) {
/* 118 */       StringBuilder sb = new StringBuilder("Bean state is invalid: ");
/* 119 */       for (Iterator<ConstraintViolation<Object>> it = result.iterator(); it.hasNext(); ) {
/* 120 */         ConstraintViolation<Object> violation = it.next();
/* 121 */         sb.append(violation.getPropertyPath()).append(" - ").append(violation.getMessage());
/* 122 */         if (it.hasNext()) {
/* 123 */           sb.append("; ");
/*     */         }
/*     */       } 
/* 126 */       throw new BeanInitializationException(sb.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/beanvalidation/BeanValidationPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */