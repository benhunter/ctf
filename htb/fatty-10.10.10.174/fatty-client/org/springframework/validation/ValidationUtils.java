/*     */ package org.springframework.validation;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class ValidationUtils
/*     */ {
/*  42 */   private static final Log logger = LogFactory.getLog(ValidationUtils.class);
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
/*     */   public static void invokeValidator(Validator validator, Object target, Errors errors) {
/*  56 */     invokeValidator(validator, target, errors, (Object[])null);
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
/*     */   
/*     */   public static void invokeValidator(Validator validator, Object target, Errors errors, @Nullable Object... validationHints) {
/*  73 */     Assert.notNull(validator, "Validator must not be null");
/*  74 */     Assert.notNull(target, "Target object must not be null");
/*  75 */     Assert.notNull(errors, "Errors object must not be null");
/*     */     
/*  77 */     if (logger.isDebugEnabled()) {
/*  78 */       logger.debug("Invoking validator [" + validator + "]");
/*     */     }
/*  80 */     if (!validator.supports(target.getClass())) {
/*  81 */       throw new IllegalArgumentException("Validator [" + validator
/*  82 */           .getClass() + "] does not support [" + target.getClass() + "]");
/*     */     }
/*     */     
/*  85 */     if (!ObjectUtils.isEmpty(validationHints) && validator instanceof SmartValidator) {
/*  86 */       ((SmartValidator)validator).validate(target, errors, validationHints);
/*     */     } else {
/*     */       
/*  89 */       validator.validate(target, errors);
/*     */     } 
/*     */     
/*  92 */     if (logger.isDebugEnabled()) {
/*  93 */       if (errors.hasErrors()) {
/*  94 */         logger.debug("Validator found " + errors.getErrorCount() + " errors");
/*     */       } else {
/*     */         
/*  97 */         logger.debug("Validator found no errors");
/*     */       } 
/*     */     }
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
/*     */   public static void rejectIfEmpty(Errors errors, String field, String errorCode) {
/* 115 */     rejectIfEmpty(errors, field, errorCode, null, null);
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
/*     */   
/*     */   public static void rejectIfEmpty(Errors errors, String field, String errorCode, String defaultMessage) {
/* 132 */     rejectIfEmpty(errors, field, errorCode, null, defaultMessage);
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
/*     */ 
/*     */   
/*     */   public static void rejectIfEmpty(Errors errors, String field, String errorCode, Object[] errorArgs) {
/* 150 */     rejectIfEmpty(errors, field, errorCode, errorArgs, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmpty(Errors errors, String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
/* 171 */     Assert.notNull(errors, "Errors object must not be null");
/* 172 */     Object value = errors.getFieldValue(field);
/* 173 */     if (value == null || !StringUtils.hasLength(value.toString())) {
/* 174 */       errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*     */     }
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
/*     */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode) {
/* 191 */     rejectIfEmptyOrWhitespace(errors, field, errorCode, null, null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, String defaultMessage) {
/* 210 */     rejectIfEmptyOrWhitespace(errors, field, errorCode, null, defaultMessage);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, @Nullable Object[] errorArgs) {
/* 230 */     rejectIfEmptyOrWhitespace(errors, field, errorCode, errorArgs, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
/* 251 */     Assert.notNull(errors, "Errors object must not be null");
/* 252 */     Object value = errors.getFieldValue(field);
/* 253 */     if (value == null || !StringUtils.hasText(value.toString()))
/* 254 */       errors.rejectValue(field, errorCode, errorArgs, defaultMessage); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/ValidationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */