/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.ElementKind;
/*     */ import javax.validation.Path;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.executable.ExecutableValidator;
/*     */ import javax.validation.metadata.BeanDescriptor;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.springframework.beans.NotReadablePropertyException;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.support.DefaultMessageSourceResolvable;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.FieldError;
/*     */ import org.springframework.validation.ObjectError;
/*     */ import org.springframework.validation.SmartValidator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpringValidatorAdapter
/*     */   implements SmartValidator, Validator
/*     */ {
/*  67 */   private static final Set<String> internalAnnotationAttributes = new HashSet<>(4);
/*     */   
/*     */   static {
/*  70 */     internalAnnotationAttributes.add("message");
/*  71 */     internalAnnotationAttributes.add("groups");
/*  72 */     internalAnnotationAttributes.add("payload");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Validator targetValidator;
/*     */ 
/*     */ 
/*     */   
/*     */   public SpringValidatorAdapter(Validator targetValidator) {
/*  84 */     Assert.notNull(targetValidator, "Target Validator must not be null");
/*  85 */     this.targetValidator = targetValidator;
/*     */   }
/*     */ 
/*     */   
/*     */   SpringValidatorAdapter() {}
/*     */   
/*     */   void setTargetValidator(Validator targetValidator) {
/*  92 */     this.targetValidator = targetValidator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supports(Class<?> clazz) {
/* 102 */     return (this.targetValidator != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(Object target, Errors errors) {
/* 107 */     if (this.targetValidator != null) {
/* 108 */       processConstraintViolations(this.targetValidator.validate(target, new Class[0]), errors);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(Object target, Errors errors, Object... validationHints) {
/* 114 */     if (this.targetValidator != null) {
/* 115 */       processConstraintViolations(this.targetValidator
/* 116 */           .validate(target, asValidationGroups(validationHints)), errors);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateValue(Class<?> targetType, String fieldName, @Nullable Object value, Errors errors, Object... validationHints) {
/* 125 */     if (this.targetValidator != null) {
/* 126 */       processConstraintViolations(this.targetValidator.validateValue(targetType, fieldName, value, 
/* 127 */             asValidationGroups(validationHints)), errors);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class<?>[] asValidationGroups(Object... validationHints) {
/* 136 */     Set<Class<?>> groups = new LinkedHashSet<>(4);
/* 137 */     for (Object hint : validationHints) {
/* 138 */       if (hint instanceof Class) {
/* 139 */         groups.add((Class)hint);
/*     */       }
/*     */     } 
/* 142 */     return ClassUtils.toClassArray(groups);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processConstraintViolations(Set<ConstraintViolation<Object>> violations, Errors errors) {
/* 153 */     for (ConstraintViolation<Object> violation : violations) {
/* 154 */       String field = determineField(violation);
/* 155 */       FieldError fieldError = errors.getFieldError(field);
/* 156 */       if (fieldError == null || !fieldError.isBindingFailure()) {
/*     */         try {
/* 158 */           ConstraintDescriptor<?> cd = violation.getConstraintDescriptor();
/* 159 */           String errorCode = determineErrorCode(cd);
/* 160 */           Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), field, cd);
/* 161 */           if (errors instanceof BindingResult) {
/*     */ 
/*     */             
/* 164 */             BindingResult bindingResult = (BindingResult)errors;
/* 165 */             String nestedField = bindingResult.getNestedPath() + field;
/* 166 */             if (nestedField.isEmpty()) {
/* 167 */               String[] arrayOfString = bindingResult.resolveMessageCodes(errorCode);
/*     */               
/* 169 */               ObjectError objectError = new ViolationObjectError(errors.getObjectName(), arrayOfString, errorArgs, violation, this);
/* 170 */               bindingResult.addError(objectError);
/*     */               continue;
/*     */             } 
/* 173 */             Object rejectedValue = getRejectedValue(field, violation, bindingResult);
/* 174 */             String[] errorCodes = bindingResult.resolveMessageCodes(errorCode, field);
/* 175 */             FieldError error = new ViolationFieldError(errors.getObjectName(), nestedField, rejectedValue, errorCodes, errorArgs, violation, this);
/*     */             
/* 177 */             bindingResult.addError((ObjectError)error);
/*     */ 
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 183 */           errors.rejectValue(field, errorCode, errorArgs, violation.getMessage());
/*     */         
/*     */         }
/* 186 */         catch (NotReadablePropertyException ex) {
/* 187 */           throw new IllegalStateException("JSR-303 validated property '" + field + "' does not have a corresponding accessor for Spring data binding - check your DataBinder's configuration (bean property versus direct field access)", ex);
/*     */         } 
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
/*     */   protected String determineField(ConstraintViolation<Object> violation) {
/* 205 */     Path path = violation.getPropertyPath();
/* 206 */     StringBuilder sb = new StringBuilder();
/* 207 */     boolean first = true;
/* 208 */     for (Path.Node node : path) {
/* 209 */       if (node.isInIterable()) {
/* 210 */         sb.append('[');
/* 211 */         Object index = node.getIndex();
/* 212 */         if (index == null) {
/* 213 */           index = node.getKey();
/*     */         }
/* 215 */         if (index != null) {
/* 216 */           sb.append(index);
/*     */         }
/* 218 */         sb.append(']');
/*     */       } 
/* 220 */       String name = node.getName();
/* 221 */       if (name != null && node.getKind() == ElementKind.PROPERTY && !name.startsWith("<")) {
/* 222 */         if (!first) {
/* 223 */           sb.append('.');
/*     */         }
/* 225 */         first = false;
/* 226 */         sb.append(name);
/*     */       } 
/*     */     } 
/* 229 */     return sb.toString();
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
/*     */   protected String determineErrorCode(ConstraintDescriptor<?> descriptor) {
/* 245 */     return descriptor.getAnnotation().annotationType().getSimpleName();
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
/*     */   protected Object[] getArgumentsForConstraint(String objectName, String field, ConstraintDescriptor<?> descriptor) {
/* 265 */     List<Object> arguments = new ArrayList();
/* 266 */     arguments.add(getResolvableField(objectName, field));
/*     */     
/* 268 */     Map<String, Object> attributesToExpose = new TreeMap<>();
/* 269 */     descriptor.getAttributes().forEach((attributeName, attributeValue) -> {
/*     */           if (!internalAnnotationAttributes.contains(attributeName)) {
/*     */             if (attributeValue instanceof String) {
/*     */               attributeValue = new ResolvableAttribute(attributeValue.toString());
/*     */             }
/*     */             attributesToExpose.put(attributeName, attributeValue);
/*     */           } 
/*     */         });
/* 277 */     arguments.addAll(attributesToExpose.values());
/* 278 */     return arguments.toArray();
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
/*     */   protected MessageSourceResolvable getResolvableField(String objectName, String field) {
/* 294 */     String[] codes = { objectName + "." + field, field };
/* 295 */     return (MessageSourceResolvable)new DefaultMessageSourceResolvable(codes, field);
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
/*     */   @Nullable
/*     */   protected Object getRejectedValue(String field, ConstraintViolation<Object> violation, BindingResult bindingResult) {
/* 312 */     Object invalidValue = violation.getInvalidValue();
/* 313 */     if (!"".equals(field) && !field.contains("[]") && (invalidValue == violation
/* 314 */       .getLeafBean() || field.contains("[") || field.contains(".")))
/*     */     {
/*     */       
/* 317 */       invalidValue = bindingResult.getRawFieldValue(field);
/*     */     }
/* 319 */     return invalidValue;
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
/*     */ 
/*     */   
/*     */   protected boolean requiresMessageFormat(ConstraintViolation<?> violation) {
/* 342 */     return containsSpringStylePlaceholder(violation.getMessage());
/*     */   }
/*     */   
/*     */   private static boolean containsSpringStylePlaceholder(@Nullable String message) {
/* 346 */     return (message != null && message.contains("{0}"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
/* 356 */     Assert.state((this.targetValidator != null), "No target Validator set");
/* 357 */     return this.targetValidator.validate(object, groups);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
/* 362 */     Assert.state((this.targetValidator != null), "No target Validator set");
/* 363 */     return this.targetValidator.validateProperty(object, propertyName, groups);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
/* 370 */     Assert.state((this.targetValidator != null), "No target Validator set");
/* 371 */     return this.targetValidator.validateValue(beanType, propertyName, value, groups);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
/* 376 */     Assert.state((this.targetValidator != null), "No target Validator set");
/* 377 */     return this.targetValidator.getConstraintsForClass(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T unwrap(@Nullable Class<T> type) {
/* 383 */     Assert.state((this.targetValidator != null), "No target Validator set");
/*     */     try {
/* 385 */       return (type != null) ? (T)this.targetValidator.unwrap(type) : (T)this.targetValidator;
/*     */     }
/* 387 */     catch (ValidationException ex) {
/*     */       
/* 389 */       if (Validator.class == type) {
/* 390 */         return (T)this.targetValidator;
/*     */       }
/* 392 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ExecutableValidator forExecutables() {
/* 398 */     Assert.state((this.targetValidator != null), "No target Validator set");
/* 399 */     return this.targetValidator.forExecutables();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ResolvableAttribute
/*     */     implements MessageSourceResolvable, Serializable
/*     */   {
/*     */     private final String resolvableString;
/*     */ 
/*     */ 
/*     */     
/*     */     public ResolvableAttribute(String resolvableString) {
/* 413 */       this.resolvableString = resolvableString;
/*     */     }
/*     */ 
/*     */     
/*     */     public String[] getCodes() {
/* 418 */       return new String[] { this.resolvableString };
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object[] getArguments() {
/* 424 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDefaultMessage() {
/* 429 */       return this.resolvableString;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 434 */       return this.resolvableString;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ViolationObjectError
/*     */     extends ObjectError
/*     */     implements Serializable
/*     */   {
/*     */     @Nullable
/*     */     private transient SpringValidatorAdapter adapter;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private transient ConstraintViolation<?> violation;
/*     */ 
/*     */ 
/*     */     
/*     */     public ViolationObjectError(String objectName, String[] codes, Object[] arguments, ConstraintViolation<?> violation, SpringValidatorAdapter adapter) {
/* 454 */       super(objectName, codes, arguments, violation.getMessage());
/* 455 */       this.adapter = adapter;
/* 456 */       this.violation = violation;
/* 457 */       wrap(violation);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldRenderDefaultMessage() {
/* 462 */       return (this.adapter != null && this.violation != null) ? this.adapter
/* 463 */         .requiresMessageFormat(this.violation) : SpringValidatorAdapter
/* 464 */         .containsSpringStylePlaceholder(getDefaultMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ViolationFieldError
/*     */     extends FieldError
/*     */     implements Serializable
/*     */   {
/*     */     @Nullable
/*     */     private transient SpringValidatorAdapter adapter;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private transient ConstraintViolation<?> violation;
/*     */ 
/*     */ 
/*     */     
/*     */     public ViolationFieldError(String objectName, String field, @Nullable Object rejectedValue, String[] codes, Object[] arguments, ConstraintViolation<?> violation, SpringValidatorAdapter adapter) {
/* 484 */       super(objectName, field, rejectedValue, false, codes, arguments, violation.getMessage());
/* 485 */       this.adapter = adapter;
/* 486 */       this.violation = violation;
/* 487 */       wrap(violation);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldRenderDefaultMessage() {
/* 492 */       return (this.adapter != null && this.violation != null) ? this.adapter
/* 493 */         .requiresMessageFormat(this.violation) : SpringValidatorAdapter
/* 494 */         .containsSpringStylePlaceholder(getDefaultMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/beanvalidation/SpringValidatorAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */