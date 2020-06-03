/*     */ package org.springframework.web.bind.support;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.FieldError;
/*     */ import org.springframework.validation.ObjectError;
/*     */ import org.springframework.web.server.ServerWebInputException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebExchangeBindException
/*     */   extends ServerWebInputException
/*     */   implements BindingResult
/*     */ {
/*     */   private final BindingResult bindingResult;
/*     */   
/*     */   public WebExchangeBindException(MethodParameter parameter, BindingResult bindingResult) {
/*  50 */     super("Validation failure", parameter);
/*  51 */     this.bindingResult = bindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BindingResult getBindingResult() {
/*  61 */     return this.bindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/*  67 */     return this.bindingResult.getObjectName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNestedPath(String nestedPath) {
/*  72 */     this.bindingResult.setNestedPath(nestedPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNestedPath() {
/*  77 */     return this.bindingResult.getNestedPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushNestedPath(String subPath) {
/*  82 */     this.bindingResult.pushNestedPath(subPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popNestedPath() throws IllegalStateException {
/*  87 */     this.bindingResult.popNestedPath();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reject(String errorCode) {
/*  93 */     this.bindingResult.reject(errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, String defaultMessage) {
/*  98 */     this.bindingResult.reject(errorCode, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
/* 103 */     this.bindingResult.reject(errorCode, errorArgs, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode) {
/* 108 */     this.bindingResult.rejectValue(field, errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode, String defaultMessage) {
/* 113 */     this.bindingResult.rejectValue(field, errorCode, defaultMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
/* 120 */     this.bindingResult.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAllErrors(Errors errors) {
/* 125 */     this.bindingResult.addAllErrors(errors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasErrors() {
/* 131 */     return this.bindingResult.hasErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getErrorCount() {
/* 136 */     return this.bindingResult.getErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getAllErrors() {
/* 141 */     return this.bindingResult.getAllErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasGlobalErrors() {
/* 146 */     return this.bindingResult.hasGlobalErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGlobalErrorCount() {
/* 151 */     return this.bindingResult.getGlobalErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getGlobalErrors() {
/* 156 */     return this.bindingResult.getGlobalErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ObjectError getGlobalError() {
/* 162 */     return this.bindingResult.getGlobalError();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors() {
/* 167 */     return this.bindingResult.hasFieldErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount() {
/* 172 */     return this.bindingResult.getFieldErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors() {
/* 177 */     return this.bindingResult.getFieldErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FieldError getFieldError() {
/* 183 */     return this.bindingResult.getFieldError();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors(String field) {
/* 188 */     return this.bindingResult.hasFieldErrors(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount(String field) {
/* 193 */     return this.bindingResult.getFieldErrorCount(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors(String field) {
/* 198 */     return this.bindingResult.getFieldErrors(field);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FieldError getFieldError(String field) {
/* 204 */     return this.bindingResult.getFieldError(field);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getFieldValue(String field) {
/* 210 */     return this.bindingResult.getFieldValue(field);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getFieldType(String field) {
/* 216 */     return this.bindingResult.getFieldType(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getTarget() {
/* 221 */     return this.bindingResult.getTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getModel() {
/* 226 */     return this.bindingResult.getModel();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getRawFieldValue(String field) {
/* 232 */     return this.bindingResult.getRawFieldValue(field);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditor findEditor(@Nullable String field, @Nullable Class valueType) {
/* 239 */     return this.bindingResult.findEditor(field, valueType);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditorRegistry getPropertyEditorRegistry() {
/* 245 */     return this.bindingResult.getPropertyEditorRegistry();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode) {
/* 250 */     return this.bindingResult.resolveMessageCodes(errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode, String field) {
/* 255 */     return this.bindingResult.resolveMessageCodes(errorCode, field);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addError(ObjectError error) {
/* 260 */     this.bindingResult.addError(error);
/*     */   }
/*     */ 
/*     */   
/*     */   public void recordFieldValue(String field, Class<?> type, @Nullable Object value) {
/* 265 */     this.bindingResult.recordFieldValue(field, type, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void recordSuppressedField(String field) {
/* 270 */     this.bindingResult.recordSuppressedField(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getSuppressedFields() {
/* 275 */     return this.bindingResult.getSuppressedFields();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 284 */     MethodParameter parameter = getMethodParameter();
/* 285 */     Assert.state((parameter != null), "No MethodParameter");
/*     */ 
/*     */ 
/*     */     
/* 289 */     StringBuilder sb = (new StringBuilder("Validation failed for argument at index ")).append(parameter.getParameterIndex()).append(" in method: ").append(parameter.getExecutable().toGenericString()).append(", with ").append(this.bindingResult.getErrorCount()).append(" error(s): ");
/* 290 */     for (ObjectError error : this.bindingResult.getAllErrors()) {
/* 291 */       sb.append("[").append(error).append("] ");
/*     */     }
/* 293 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 298 */     return (this == other || this.bindingResult.equals(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 303 */     return this.bindingResult.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/support/WebExchangeBindException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */