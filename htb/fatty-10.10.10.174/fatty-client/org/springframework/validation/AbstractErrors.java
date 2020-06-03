/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public abstract class AbstractErrors
/*     */   implements Errors, Serializable
/*     */ {
/*  42 */   private String nestedPath = "";
/*     */   
/*  44 */   private final Deque<String> nestedPathStack = new ArrayDeque<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNestedPath(@Nullable String nestedPath) {
/*  49 */     doSetNestedPath(nestedPath);
/*  50 */     this.nestedPathStack.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNestedPath() {
/*  55 */     return this.nestedPath;
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushNestedPath(String subPath) {
/*  60 */     this.nestedPathStack.push(getNestedPath());
/*  61 */     doSetNestedPath(getNestedPath() + subPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popNestedPath() throws IllegalStateException {
/*     */     try {
/*  67 */       String formerNestedPath = this.nestedPathStack.pop();
/*  68 */       doSetNestedPath(formerNestedPath);
/*     */     }
/*  70 */     catch (NoSuchElementException ex) {
/*  71 */       throw new IllegalStateException("Cannot pop nested path: no nested path on stack");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doSetNestedPath(@Nullable String nestedPath) {
/*  80 */     if (nestedPath == null) {
/*  81 */       nestedPath = "";
/*     */     }
/*  83 */     nestedPath = canonicalFieldName(nestedPath);
/*  84 */     if (nestedPath.length() > 0 && !nestedPath.endsWith(".")) {
/*  85 */       nestedPath = nestedPath + ".";
/*     */     }
/*  87 */     this.nestedPath = nestedPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String fixedField(@Nullable String field) {
/*  95 */     if (StringUtils.hasLength(field)) {
/*  96 */       return getNestedPath() + canonicalFieldName(field);
/*     */     }
/*     */     
/*  99 */     String path = getNestedPath();
/* 100 */     return path.endsWith(".") ? path
/* 101 */       .substring(0, path.length() - ".".length()) : path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String canonicalFieldName(String field) {
/* 112 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reject(String errorCode) {
/* 118 */     reject(errorCode, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, String defaultMessage) {
/* 123 */     reject(errorCode, null, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode) {
/* 128 */     rejectValue(field, errorCode, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(@Nullable String field, String errorCode, String defaultMessage) {
/* 133 */     rejectValue(field, errorCode, null, defaultMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasErrors() {
/* 139 */     return !getAllErrors().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getErrorCount() {
/* 144 */     return getAllErrors().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getAllErrors() {
/* 149 */     List<ObjectError> result = new LinkedList<>();
/* 150 */     result.addAll(getGlobalErrors());
/* 151 */     result.addAll((Collection)getFieldErrors());
/* 152 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasGlobalErrors() {
/* 157 */     return (getGlobalErrorCount() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGlobalErrorCount() {
/* 162 */     return getGlobalErrors().size();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ObjectError getGlobalError() {
/* 168 */     List<ObjectError> globalErrors = getGlobalErrors();
/* 169 */     return !globalErrors.isEmpty() ? globalErrors.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors() {
/* 174 */     return (getFieldErrorCount() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount() {
/* 179 */     return getFieldErrors().size();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FieldError getFieldError() {
/* 185 */     List<FieldError> fieldErrors = getFieldErrors();
/* 186 */     return !fieldErrors.isEmpty() ? fieldErrors.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors(String field) {
/* 191 */     return (getFieldErrorCount(field) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount(String field) {
/* 196 */     return getFieldErrors(field).size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors(String field) {
/* 201 */     List<FieldError> fieldErrors = getFieldErrors();
/* 202 */     List<FieldError> result = new LinkedList<>();
/* 203 */     String fixedField = fixedField(field);
/* 204 */     for (FieldError error : fieldErrors) {
/* 205 */       if (isMatchingFieldError(fixedField, error)) {
/* 206 */         result.add(error);
/*     */       }
/*     */     } 
/* 209 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FieldError getFieldError(String field) {
/* 215 */     List<FieldError> fieldErrors = getFieldErrors(field);
/* 216 */     return !fieldErrors.isEmpty() ? fieldErrors.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getFieldType(String field) {
/* 222 */     Object value = getFieldValue(field);
/* 223 */     return (value != null) ? value.getClass() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isMatchingFieldError(String field, FieldError fieldError) {
/* 233 */     if (field.equals(fieldError.getField())) {
/* 234 */       return true;
/*     */     }
/*     */     
/* 237 */     int endIndex = field.length() - 1;
/* 238 */     return (endIndex >= 0 && field.charAt(endIndex) == '*' && (endIndex == 0 || field
/* 239 */       .regionMatches(0, fieldError.getField(), 0, endIndex)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 245 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 246 */     sb.append(": ").append(getErrorCount()).append(" errors");
/* 247 */     for (ObjectError error : getAllErrors()) {
/* 248 */       sb.append('\n').append(error);
/*     */     }
/* 250 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/AbstractErrors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */