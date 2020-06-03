/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.NoSuchMessageException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindStatus
/*     */ {
/*     */   private final RequestContext requestContext;
/*     */   private final String path;
/*     */   private final boolean htmlEscape;
/*     */   @Nullable
/*     */   private final String expression;
/*     */   @Nullable
/*     */   private final Errors errors;
/*     */   private final String[] errorCodes;
/*     */   @Nullable
/*     */   private String[] errorMessages;
/*     */   @Nullable
/*     */   private List<? extends ObjectError> objectErrors;
/*     */   @Nullable
/*     */   private Object value;
/*     */   @Nullable
/*     */   private Class<?> valueType;
/*     */   @Nullable
/*     */   private Object actualValue;
/*     */   @Nullable
/*     */   private PropertyEditor editor;
/*     */   @Nullable
/*     */   private BindingResult bindingResult;
/*     */   
/*     */   public BindStatus(RequestContext requestContext, String path, boolean htmlEscape) throws IllegalStateException {
/*     */     String beanName;
/*  97 */     this.requestContext = requestContext;
/*  98 */     this.path = path;
/*  99 */     this.htmlEscape = htmlEscape;
/*     */ 
/*     */ 
/*     */     
/* 103 */     int dotPos = path.indexOf('.');
/* 104 */     if (dotPos == -1) {
/*     */       
/* 106 */       beanName = path;
/* 107 */       this.expression = null;
/*     */     } else {
/*     */       
/* 110 */       beanName = path.substring(0, dotPos);
/* 111 */       this.expression = path.substring(dotPos + 1);
/*     */     } 
/*     */     
/* 114 */     this.errors = requestContext.getErrors(beanName, false);
/*     */     
/* 116 */     if (this.errors != null) {
/*     */ 
/*     */ 
/*     */       
/* 120 */       if (this.expression != null) {
/* 121 */         if ("*".equals(this.expression)) {
/* 122 */           this.objectErrors = this.errors.getAllErrors();
/*     */         }
/* 124 */         else if (this.expression.endsWith("*")) {
/* 125 */           this.objectErrors = this.errors.getFieldErrors(this.expression);
/*     */         } else {
/*     */           
/* 128 */           this.objectErrors = this.errors.getFieldErrors(this.expression);
/* 129 */           this.value = this.errors.getFieldValue(this.expression);
/* 130 */           this.valueType = this.errors.getFieldType(this.expression);
/* 131 */           if (this.errors instanceof BindingResult) {
/* 132 */             this.bindingResult = (BindingResult)this.errors;
/* 133 */             this.actualValue = this.bindingResult.getRawFieldValue(this.expression);
/* 134 */             this.editor = this.bindingResult.findEditor(this.expression, null);
/*     */           } else {
/*     */             
/* 137 */             this.actualValue = this.value;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 142 */         this.objectErrors = this.errors.getGlobalErrors();
/*     */       } 
/* 144 */       this.errorCodes = initErrorCodes(this.objectErrors);
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 151 */       Object target = requestContext.getModelObject(beanName);
/* 152 */       if (target == null) {
/* 153 */         throw new IllegalStateException("Neither BindingResult nor plain target object for bean name '" + beanName + "' available as request attribute");
/*     */       }
/*     */       
/* 156 */       if (this.expression != null && !"*".equals(this.expression) && !this.expression.endsWith("*")) {
/* 157 */         BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(target);
/* 158 */         this.value = bw.getPropertyValue(this.expression);
/* 159 */         this.valueType = bw.getPropertyType(this.expression);
/* 160 */         this.actualValue = this.value;
/*     */       } 
/* 162 */       this.errorCodes = new String[0];
/* 163 */       this.errorMessages = new String[0];
/*     */     } 
/*     */     
/* 166 */     if (htmlEscape && this.value instanceof String) {
/* 167 */       this.value = HtmlUtils.htmlEscape((String)this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] initErrorCodes(List<? extends ObjectError> objectErrors) {
/* 175 */     String[] errorCodes = new String[objectErrors.size()];
/* 176 */     for (int i = 0; i < objectErrors.size(); i++) {
/* 177 */       ObjectError error = objectErrors.get(i);
/* 178 */       errorCodes[i] = error.getCode();
/*     */     } 
/* 180 */     return errorCodes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 189 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getExpression() {
/* 201 */     return this.expression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue() {
/* 212 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getValueType() {
/* 222 */     return this.valueType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getActualValue() {
/* 231 */     return this.actualValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDisplayValue() {
/* 242 */     if (this.value instanceof String) {
/* 243 */       return (String)this.value;
/*     */     }
/* 245 */     if (this.value != null) {
/* 246 */       return this.htmlEscape ? HtmlUtils.htmlEscape(this.value.toString()) : this.value.toString();
/*     */     }
/* 248 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isError() {
/* 255 */     return (this.errorCodes.length > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getErrorCodes() {
/* 263 */     return this.errorCodes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorCode() {
/* 270 */     return (this.errorCodes.length > 0) ? this.errorCodes[0] : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getErrorMessages() {
/* 278 */     return initErrorMessages();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorMessage() {
/* 285 */     String[] errorMessages = initErrorMessages();
/* 286 */     return (errorMessages.length > 0) ? errorMessages[0] : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorMessagesAsString(String delimiter) {
/* 296 */     return StringUtils.arrayToDelimitedString((Object[])initErrorMessages(), delimiter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] initErrorMessages() throws NoSuchMessageException {
/* 303 */     if (this.errorMessages == null) {
/* 304 */       if (this.objectErrors != null) {
/* 305 */         this.errorMessages = new String[this.objectErrors.size()];
/* 306 */         for (int i = 0; i < this.objectErrors.size(); i++) {
/* 307 */           ObjectError error = this.objectErrors.get(i);
/* 308 */           this.errorMessages[i] = this.requestContext.getMessage((MessageSourceResolvable)error, this.htmlEscape);
/*     */         } 
/*     */       } else {
/*     */         
/* 312 */         this.errorMessages = new String[0];
/*     */       } 
/*     */     }
/* 315 */     return this.errorMessages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Errors getErrors() {
/* 326 */     return this.errors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditor getEditor() {
/* 336 */     return this.editor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditor findEditor(Class<?> valueClass) {
/* 347 */     return (this.bindingResult != null) ? this.bindingResult.findEditor(this.expression, valueClass) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 353 */     StringBuilder sb = new StringBuilder("BindStatus: ");
/* 354 */     sb.append("expression=[").append(this.expression).append("]; ");
/* 355 */     sb.append("value=[").append(this.value).append("]");
/* 356 */     if (!ObjectUtils.isEmpty((Object[])this.errorCodes)) {
/* 357 */       sb.append("; errorCodes=").append(Arrays.asList(this.errorCodes));
/*     */     }
/* 359 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/BindStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */