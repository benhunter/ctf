/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindException
/*     */   extends Exception
/*     */   implements BindingResult
/*     */ {
/*     */   private final BindingResult bindingResult;
/*     */   
/*     */   public BindException(BindingResult bindingResult) {
/*  55 */     Assert.notNull(bindingResult, "BindingResult must not be null");
/*  56 */     this.bindingResult = bindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BindException(Object target, String objectName) {
/*  66 */     Assert.notNull(target, "Target object must not be null");
/*  67 */     this.bindingResult = new BeanPropertyBindingResult(target, objectName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BindingResult getBindingResult() {
/*  77 */     return this.bindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/*  83 */     return this.bindingResult.getObjectName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNestedPath(String nestedPath) {
/*  88 */     this.bindingResult.setNestedPath(nestedPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNestedPath() {
/*  93 */     return this.bindingResult.getNestedPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushNestedPath(String subPath) {
/*  98 */     this.bindingResult.pushNestedPath(subPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popNestedPath() throws IllegalStateException {
/* 103 */     this.bindingResult.popNestedPath();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reject(String errorCode) {
/* 109 */     this.bindingResult.reject(errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, String defaultMessage) {
/* 114 */     this.bindingResult.reject(errorCode, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
/* 119 */     this.bindingResult.reject(errorCode, errorArgs, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode) {
/* 124 */     this.bindingResult.rejectValue(field, errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode, String defaultMessage) {
/* 129 */     this.bindingResult.rejectValue(field, errorCode, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
/* 134 */     this.bindingResult.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAllErrors(Errors errors) {
/* 139 */     this.bindingResult.addAllErrors(errors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasErrors() {
/* 145 */     return this.bindingResult.hasErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getErrorCount() {
/* 150 */     return this.bindingResult.getErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getAllErrors() {
/* 155 */     return this.bindingResult.getAllErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasGlobalErrors() {
/* 160 */     return this.bindingResult.hasGlobalErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGlobalErrorCount() {
/* 165 */     return this.bindingResult.getGlobalErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getGlobalErrors() {
/* 170 */     return this.bindingResult.getGlobalErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ObjectError getGlobalError() {
/* 176 */     return this.bindingResult.getGlobalError();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors() {
/* 181 */     return this.bindingResult.hasFieldErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount() {
/* 186 */     return this.bindingResult.getFieldErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors() {
/* 191 */     return this.bindingResult.getFieldErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FieldError getFieldError() {
/* 197 */     return this.bindingResult.getFieldError();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors(String field) {
/* 202 */     return this.bindingResult.hasFieldErrors(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount(String field) {
/* 207 */     return this.bindingResult.getFieldErrorCount(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors(String field) {
/* 212 */     return this.bindingResult.getFieldErrors(field);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FieldError getFieldError(String field) {
/* 218 */     return this.bindingResult.getFieldError(field);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getFieldValue(String field) {
/* 224 */     return this.bindingResult.getFieldValue(field);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getFieldType(String field) {
/* 230 */     return this.bindingResult.getFieldType(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getTarget() {
/* 235 */     return this.bindingResult.getTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getModel() {
/* 240 */     return this.bindingResult.getModel();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getRawFieldValue(String field) {
/* 246 */     return this.bindingResult.getRawFieldValue(field);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditor findEditor(@Nullable String field, @Nullable Class<?> valueType) {
/* 253 */     return this.bindingResult.findEditor(field, valueType);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditorRegistry getPropertyEditorRegistry() {
/* 259 */     return this.bindingResult.getPropertyEditorRegistry();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode) {
/* 264 */     return this.bindingResult.resolveMessageCodes(errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode, String field) {
/* 269 */     return this.bindingResult.resolveMessageCodes(errorCode, field);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addError(ObjectError error) {
/* 274 */     this.bindingResult.addError(error);
/*     */   }
/*     */ 
/*     */   
/*     */   public void recordFieldValue(String field, Class<?> type, @Nullable Object value) {
/* 279 */     this.bindingResult.recordFieldValue(field, type, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void recordSuppressedField(String field) {
/* 284 */     this.bindingResult.recordSuppressedField(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getSuppressedFields() {
/* 289 */     return this.bindingResult.getSuppressedFields();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 298 */     return this.bindingResult.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 303 */     return (this == other || this.bindingResult.equals(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 308 */     return this.bindingResult.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/BindException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */