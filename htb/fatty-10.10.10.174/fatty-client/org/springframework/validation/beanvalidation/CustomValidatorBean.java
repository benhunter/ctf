/*    */ package org.springframework.validation.beanvalidation;
/*    */ 
/*    */ import javax.validation.MessageInterpolator;
/*    */ import javax.validation.TraversableResolver;
/*    */ import javax.validation.Validation;
/*    */ import javax.validation.Validator;
/*    */ import javax.validation.ValidatorContext;
/*    */ import javax.validation.ValidatorFactory;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomValidatorBean
/*    */   extends SpringValidatorAdapter
/*    */   implements Validator, InitializingBean
/*    */ {
/*    */   @Nullable
/*    */   private ValidatorFactory validatorFactory;
/*    */   @Nullable
/*    */   private MessageInterpolator messageInterpolator;
/*    */   @Nullable
/*    */   private TraversableResolver traversableResolver;
/*    */   
/*    */   public void setValidatorFactory(ValidatorFactory validatorFactory) {
/* 54 */     this.validatorFactory = validatorFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMessageInterpolator(MessageInterpolator messageInterpolator) {
/* 61 */     this.messageInterpolator = messageInterpolator;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTraversableResolver(TraversableResolver traversableResolver) {
/* 68 */     this.traversableResolver = traversableResolver;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() {
/* 74 */     if (this.validatorFactory == null) {
/* 75 */       this.validatorFactory = Validation.buildDefaultValidatorFactory();
/*    */     }
/*    */     
/* 78 */     ValidatorContext validatorContext = this.validatorFactory.usingContext();
/* 79 */     MessageInterpolator targetInterpolator = this.messageInterpolator;
/* 80 */     if (targetInterpolator == null) {
/* 81 */       targetInterpolator = this.validatorFactory.getMessageInterpolator();
/*    */     }
/* 83 */     validatorContext.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
/* 84 */     if (this.traversableResolver != null) {
/* 85 */       validatorContext.traversableResolver(this.traversableResolver);
/*    */     }
/*    */     
/* 88 */     setTargetValidator(validatorContext.getValidator());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/beanvalidation/CustomValidatorBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */