/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
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
/*     */ public abstract class AbstractBindingResult
/*     */   extends AbstractErrors
/*     */   implements BindingResult, Serializable
/*     */ {
/*     */   private final String objectName;
/*  51 */   private MessageCodesResolver messageCodesResolver = new DefaultMessageCodesResolver();
/*     */   
/*  53 */   private final List<ObjectError> errors = new LinkedList<>();
/*     */   
/*  55 */   private final Map<String, Class<?>> fieldTypes = new HashMap<>();
/*     */   
/*  57 */   private final Map<String, Object> fieldValues = new HashMap<>();
/*     */   
/*  59 */   private final Set<String> suppressedFields = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractBindingResult(String objectName) {
/*  68 */     this.objectName = objectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageCodesResolver(MessageCodesResolver messageCodesResolver) {
/*  78 */     Assert.notNull(messageCodesResolver, "MessageCodesResolver must not be null");
/*  79 */     this.messageCodesResolver = messageCodesResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageCodesResolver getMessageCodesResolver() {
/*  86 */     return this.messageCodesResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/*  96 */     return this.objectName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
/* 101 */     addError(new ObjectError(getObjectName(), resolveMessageCodes(errorCode), errorArgs, defaultMessage));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
/* 108 */     if ("".equals(getNestedPath()) && !StringUtils.hasLength(field)) {
/*     */ 
/*     */ 
/*     */       
/* 112 */       reject(errorCode, errorArgs, defaultMessage);
/*     */       
/*     */       return;
/*     */     } 
/* 116 */     String fixedField = fixedField(field);
/* 117 */     Object newVal = getActualFieldValue(fixedField);
/*     */     
/* 119 */     FieldError fe = new FieldError(getObjectName(), fixedField, newVal, false, resolveMessageCodes(errorCode, field), errorArgs, defaultMessage);
/* 120 */     addError(fe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAllErrors(Errors errors) {
/* 125 */     if (!errors.getObjectName().equals(getObjectName())) {
/* 126 */       throw new IllegalArgumentException("Errors object needs to have same object name");
/*     */     }
/* 128 */     this.errors.addAll(errors.getAllErrors());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasErrors() {
/* 133 */     return !this.errors.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getErrorCount() {
/* 138 */     return this.errors.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getAllErrors() {
/* 143 */     return Collections.unmodifiableList(this.errors);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getGlobalErrors() {
/* 148 */     List<ObjectError> result = new LinkedList<>();
/* 149 */     for (ObjectError objectError : this.errors) {
/* 150 */       if (!(objectError instanceof FieldError)) {
/* 151 */         result.add(objectError);
/*     */       }
/*     */     } 
/* 154 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ObjectError getGlobalError() {
/* 160 */     for (ObjectError objectError : this.errors) {
/* 161 */       if (!(objectError instanceof FieldError)) {
/* 162 */         return objectError;
/*     */       }
/*     */     } 
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors() {
/* 170 */     List<FieldError> result = new LinkedList<>();
/* 171 */     for (ObjectError objectError : this.errors) {
/* 172 */       if (objectError instanceof FieldError) {
/* 173 */         result.add((FieldError)objectError);
/*     */       }
/*     */     } 
/* 176 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FieldError getFieldError() {
/* 182 */     for (ObjectError objectError : this.errors) {
/* 183 */       if (objectError instanceof FieldError) {
/* 184 */         return (FieldError)objectError;
/*     */       }
/*     */     } 
/* 187 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors(String field) {
/* 192 */     List<FieldError> result = new LinkedList<>();
/* 193 */     String fixedField = fixedField(field);
/* 194 */     for (ObjectError objectError : this.errors) {
/* 195 */       if (objectError instanceof FieldError && isMatchingFieldError(fixedField, (FieldError)objectError)) {
/* 196 */         result.add((FieldError)objectError);
/*     */       }
/*     */     } 
/* 199 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FieldError getFieldError(String field) {
/* 205 */     String fixedField = fixedField(field);
/* 206 */     for (ObjectError objectError : this.errors) {
/* 207 */       if (objectError instanceof FieldError) {
/* 208 */         FieldError fieldError = (FieldError)objectError;
/* 209 */         if (isMatchingFieldError(fixedField, fieldError)) {
/* 210 */           return fieldError;
/*     */         }
/*     */       } 
/*     */     } 
/* 214 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getFieldValue(String field) {
/* 220 */     FieldError fieldError = getFieldError(field);
/*     */     
/* 222 */     if (fieldError != null) {
/* 223 */       Object value = fieldError.getRejectedValue();
/*     */       
/* 225 */       return (fieldError.isBindingFailure() || getTarget() == null) ? value : formatFieldValue(field, value);
/*     */     } 
/* 227 */     if (getTarget() != null) {
/* 228 */       Object value = getActualFieldValue(fixedField(field));
/* 229 */       return formatFieldValue(field, value);
/*     */     } 
/*     */     
/* 232 */     return this.fieldValues.get(field);
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
/*     */   @Nullable
/*     */   public Class<?> getFieldType(@Nullable String field) {
/* 245 */     if (getTarget() != null) {
/* 246 */       Object value = getActualFieldValue(fixedField(field));
/* 247 */       if (value != null) {
/* 248 */         return value.getClass();
/*     */       }
/*     */     } 
/* 251 */     return this.fieldTypes.get(field);
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
/*     */   public Map<String, Object> getModel() {
/* 273 */     Map<String, Object> model = new LinkedHashMap<>(2);
/*     */     
/* 275 */     model.put(getObjectName(), getTarget());
/*     */     
/* 277 */     model.put(MODEL_KEY_PREFIX + getObjectName(), this);
/* 278 */     return model;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getRawFieldValue(String field) {
/* 284 */     return (getTarget() != null) ? getActualFieldValue(fixedField(field)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditor findEditor(@Nullable String field, @Nullable Class<?> valueType) {
/* 295 */     PropertyEditorRegistry editorRegistry = getPropertyEditorRegistry();
/* 296 */     if (editorRegistry != null) {
/* 297 */       Class<?> valueTypeToUse = valueType;
/* 298 */       if (valueTypeToUse == null) {
/* 299 */         valueTypeToUse = getFieldType(field);
/*     */       }
/* 301 */       return editorRegistry.findCustomEditor(valueTypeToUse, fixedField(field));
/*     */     } 
/*     */     
/* 304 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditorRegistry getPropertyEditorRegistry() {
/* 314 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode) {
/* 319 */     return getMessageCodesResolver().resolveMessageCodes(errorCode, getObjectName());
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode, @Nullable String field) {
/* 324 */     return getMessageCodesResolver().resolveMessageCodes(errorCode, 
/* 325 */         getObjectName(), fixedField(field), getFieldType(field));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addError(ObjectError error) {
/* 330 */     this.errors.add(error);
/*     */   }
/*     */ 
/*     */   
/*     */   public void recordFieldValue(String field, Class<?> type, @Nullable Object value) {
/* 335 */     this.fieldTypes.put(field, type);
/* 336 */     this.fieldValues.put(field, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordSuppressedField(String field) {
/* 347 */     this.suppressedFields.add(field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSuppressedFields() {
/* 358 */     return StringUtils.toStringArray(this.suppressedFields);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 364 */     if (this == other) {
/* 365 */       return true;
/*     */     }
/* 367 */     if (!(other instanceof BindingResult)) {
/* 368 */       return false;
/*     */     }
/* 370 */     BindingResult otherResult = (BindingResult)other;
/* 371 */     return (getObjectName().equals(otherResult.getObjectName()) && 
/* 372 */       ObjectUtils.nullSafeEquals(getTarget(), otherResult.getTarget()) && 
/* 373 */       getAllErrors().equals(otherResult.getAllErrors()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 378 */     return getObjectName().hashCode();
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
/*     */   @Nullable
/*     */   public abstract Object getTarget();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract Object getActualFieldValue(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object formatFieldValue(String field, @Nullable Object value) {
/* 411 */     return value;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/AbstractBindingResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */