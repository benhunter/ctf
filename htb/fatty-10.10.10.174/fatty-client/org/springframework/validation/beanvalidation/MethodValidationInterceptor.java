/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Set;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.ConstraintViolationException;
/*     */ import javax.validation.Validation;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import javax.validation.executable.ExecutableValidator;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.SmartFactoryBean;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class MethodValidationInterceptor
/*     */   implements MethodInterceptor
/*     */ {
/*     */   private final Validator validator;
/*     */   
/*     */   public MethodValidationInterceptor() {
/*  68 */     this(Validation.buildDefaultValidatorFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodValidationInterceptor(ValidatorFactory validatorFactory) {
/*  76 */     this(validatorFactory.getValidator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodValidationInterceptor(Validator validator) {
/*  84 */     this.validator = validator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/*  92 */     if (isFactoryBeanMetadataMethod(invocation.getMethod())) {
/*  93 */       return invocation.proceed();
/*     */     }
/*     */     
/*  96 */     Class<?>[] groups = determineValidationGroups(invocation);
/*     */ 
/*     */     
/*  99 */     ExecutableValidator execVal = this.validator.forExecutables();
/* 100 */     Method methodToValidate = invocation.getMethod();
/*     */ 
/*     */     
/*     */     try {
/* 104 */       result = execVal.validateParameters(invocation
/* 105 */           .getThis(), methodToValidate, invocation.getArguments(), groups);
/*     */     }
/* 107 */     catch (IllegalArgumentException ex) {
/*     */ 
/*     */       
/* 110 */       methodToValidate = BridgeMethodResolver.findBridgedMethod(
/* 111 */           ClassUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass()));
/* 112 */       result = execVal.validateParameters(invocation
/* 113 */           .getThis(), methodToValidate, invocation.getArguments(), groups);
/*     */     } 
/* 115 */     if (!result.isEmpty()) {
/* 116 */       throw new ConstraintViolationException(result);
/*     */     }
/*     */     
/* 119 */     Object returnValue = invocation.proceed();
/*     */     
/* 121 */     Set<ConstraintViolation<Object>> result = execVal.validateReturnValue(invocation.getThis(), methodToValidate, returnValue, groups);
/* 122 */     if (!result.isEmpty()) {
/* 123 */       throw new ConstraintViolationException(result);
/*     */     }
/*     */     
/* 126 */     return returnValue;
/*     */   }
/*     */   
/*     */   private boolean isFactoryBeanMetadataMethod(Method method) {
/* 130 */     Class<?> clazz = method.getDeclaringClass();
/*     */ 
/*     */     
/* 133 */     if (clazz.isInterface()) {
/* 134 */       return ((clazz == FactoryBean.class || clazz == SmartFactoryBean.class) && 
/* 135 */         !method.getName().equals("getObject"));
/*     */     }
/*     */ 
/*     */     
/* 139 */     Class<?> factoryBeanType = null;
/* 140 */     if (SmartFactoryBean.class.isAssignableFrom(clazz)) {
/* 141 */       factoryBeanType = SmartFactoryBean.class;
/*     */     }
/* 143 */     else if (FactoryBean.class.isAssignableFrom(clazz)) {
/* 144 */       factoryBeanType = FactoryBean.class;
/*     */     } 
/* 146 */     return (factoryBeanType != null && !method.getName().equals("getObject") && 
/* 147 */       ClassUtils.hasMethod(factoryBeanType, method.getName(), method.getParameterTypes()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?>[] determineValidationGroups(MethodInvocation invocation) {
/* 158 */     Validated validatedAnn = (Validated)AnnotationUtils.findAnnotation(invocation.getMethod(), Validated.class);
/* 159 */     if (validatedAnn == null) {
/* 160 */       validatedAnn = (Validated)AnnotationUtils.findAnnotation(invocation.getThis().getClass(), Validated.class);
/*     */     }
/* 162 */     return (validatedAnn != null) ? validatedAnn.value() : new Class[0];
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/beanvalidation/MethodValidationInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */