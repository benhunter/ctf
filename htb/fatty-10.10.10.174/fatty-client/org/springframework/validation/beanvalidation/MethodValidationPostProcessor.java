/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.annotation.Validated;
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
/*     */ public class MethodValidationPostProcessor
/*     */   extends AbstractBeanFactoryAwareAdvisingPostProcessor
/*     */   implements InitializingBean
/*     */ {
/*  63 */   private Class<? extends Annotation> validatedAnnotationType = (Class)Validated.class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Validator validator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidatedAnnotationType(Class<? extends Annotation> validatedAnnotationType) {
/*  78 */     Assert.notNull(validatedAnnotationType, "'validatedAnnotationType' must not be null");
/*  79 */     this.validatedAnnotationType = validatedAnnotationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidator(Validator validator) {
/*  88 */     if (validator instanceof LocalValidatorFactoryBean) {
/*  89 */       this.validator = ((LocalValidatorFactoryBean)validator).getValidator();
/*     */     }
/*  91 */     else if (validator instanceof SpringValidatorAdapter) {
/*  92 */       this.validator = (Validator)validator.unwrap(Validator.class);
/*     */     } else {
/*     */       
/*  95 */       this.validator = validator;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidatorFactory(ValidatorFactory validatorFactory) {
/* 106 */     this.validator = validatorFactory.getValidator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 112 */     AnnotationMatchingPointcut annotationMatchingPointcut = new AnnotationMatchingPointcut(this.validatedAnnotationType, true);
/* 113 */     this.advisor = (Advisor)new DefaultPointcutAdvisor((Pointcut)annotationMatchingPointcut, createMethodValidationAdvice(this.validator));
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
/*     */   protected Advice createMethodValidationAdvice(@Nullable Validator validator) {
/* 125 */     return (validator != null) ? (Advice)new MethodValidationInterceptor(validator) : (Advice)new MethodValidationInterceptor();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/beanvalidation/MethodValidationPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */