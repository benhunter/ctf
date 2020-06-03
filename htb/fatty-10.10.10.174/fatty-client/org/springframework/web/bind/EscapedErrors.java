/*     */ package org.springframework.web.bind;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.FieldError;
/*     */ import org.springframework.validation.ObjectError;
/*     */ import org.springframework.web.util.HtmlUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EscapedErrors
/*     */   implements Errors
/*     */ {
/*     */   private final Errors source;
/*     */   
/*     */   public EscapedErrors(Errors source) {
/*  52 */     Assert.notNull(source, "Errors source must not be null");
/*  53 */     this.source = source;
/*     */   }
/*     */   
/*     */   public Errors getSource() {
/*  57 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/*  63 */     return this.source.getObjectName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNestedPath(String nestedPath) {
/*  68 */     this.source.setNestedPath(nestedPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNestedPath() {
/*  73 */     return this.source.getNestedPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushNestedPath(String subPath) {
/*  78 */     this.source.pushNestedPath(subPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popNestedPath() throws IllegalStateException {
/*  83 */     this.source.popNestedPath();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reject(String errorCode) {
/*  89 */     this.source.reject(errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, String defaultMessage) {
/*  94 */     this.source.reject(errorCode, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
/*  99 */     this.source.reject(errorCode, errorArgs, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode) {
/* 104 */     this.source.rejectValue(field, errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode, String defaultMessage) {
/* 109 */     this.source.rejectValue(field, errorCode, defaultMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {
/* 116 */     this.source.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAllErrors(Errors errors) {
/* 121 */     this.source.addAllErrors(errors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasErrors() {
/* 127 */     return this.source.hasErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getErrorCount() {
/* 132 */     return this.source.getErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getAllErrors() {
/* 137 */     return escapeObjectErrors(this.source.getAllErrors());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasGlobalErrors() {
/* 142 */     return this.source.hasGlobalErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGlobalErrorCount() {
/* 147 */     return this.source.getGlobalErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getGlobalErrors() {
/* 152 */     return escapeObjectErrors(this.source.getGlobalErrors());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ObjectError getGlobalError() {
/* 158 */     return escapeObjectError(this.source.getGlobalError());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors() {
/* 163 */     return this.source.hasFieldErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount() {
/* 168 */     return this.source.getFieldErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors() {
/* 173 */     return this.source.getFieldErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FieldError getFieldError() {
/* 179 */     return this.source.getFieldError();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors(String field) {
/* 184 */     return this.source.hasFieldErrors(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount(String field) {
/* 189 */     return this.source.getFieldErrorCount(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors(String field) {
/* 194 */     return escapeObjectErrors(this.source.getFieldErrors(field));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FieldError getFieldError(String field) {
/* 200 */     return escapeObjectError(this.source.getFieldError(field));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getFieldValue(String field) {
/* 206 */     Object value = this.source.getFieldValue(field);
/* 207 */     return (value instanceof String) ? HtmlUtils.htmlEscape((String)value) : value;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getFieldType(String field) {
/* 213 */     return this.source.getFieldType(field);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private <T extends ObjectError> T escapeObjectError(@Nullable T source) {
/* 219 */     if (source == null) {
/* 220 */       return null;
/*     */     }
/* 222 */     String defaultMessage = source.getDefaultMessage();
/* 223 */     if (defaultMessage != null) {
/* 224 */       defaultMessage = HtmlUtils.htmlEscape(defaultMessage);
/*     */     }
/* 226 */     if (source instanceof FieldError) {
/* 227 */       FieldError fieldError = (FieldError)source;
/* 228 */       Object value = fieldError.getRejectedValue();
/* 229 */       if (value instanceof String) {
/* 230 */         value = HtmlUtils.htmlEscape((String)value);
/*     */       }
/* 232 */       return (T)new FieldError(fieldError
/* 233 */           .getObjectName(), fieldError.getField(), value, fieldError.isBindingFailure(), fieldError
/* 234 */           .getCodes(), fieldError.getArguments(), defaultMessage);
/*     */     } 
/*     */     
/* 237 */     return (T)new ObjectError(source
/* 238 */         .getObjectName(), source.getCodes(), source.getArguments(), defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends ObjectError> List<T> escapeObjectErrors(List<T> source) {
/* 243 */     List<T> escaped = new ArrayList<>(source.size());
/* 244 */     for (ObjectError objectError : source) {
/* 245 */       escaped.add(escapeObjectError((T)objectError));
/*     */     }
/* 247 */     return escaped;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/EscapedErrors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */