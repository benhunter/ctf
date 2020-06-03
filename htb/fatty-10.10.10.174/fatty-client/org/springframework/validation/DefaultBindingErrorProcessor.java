/*    */ package org.springframework.validation;
/*    */ 
/*    */ import org.springframework.beans.PropertyAccessException;
/*    */ import org.springframework.context.support.DefaultMessageSourceResolvable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class DefaultBindingErrorProcessor
/*    */   implements BindingErrorProcessor
/*    */ {
/*    */   public static final String MISSING_FIELD_ERROR_CODE = "required";
/*    */   
/*    */   public void processMissingFieldError(String missingField, BindingResult bindingResult) {
/* 59 */     String fixedField = bindingResult.getNestedPath() + missingField;
/* 60 */     String[] codes = bindingResult.resolveMessageCodes("required", missingField);
/* 61 */     Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), fixedField);
/* 62 */     FieldError error = new FieldError(bindingResult.getObjectName(), fixedField, "", true, codes, arguments, "Field '" + fixedField + "' is required");
/*    */     
/* 64 */     bindingResult.addError(error);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void processPropertyAccessException(PropertyAccessException ex, BindingResult bindingResult) {
/* 70 */     String field = ex.getPropertyName();
/* 71 */     Assert.state((field != null), "No field in exception");
/* 72 */     String[] codes = bindingResult.resolveMessageCodes(ex.getErrorCode(), field);
/* 73 */     Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), field);
/* 74 */     Object rejectedValue = ex.getValue();
/* 75 */     if (ObjectUtils.isArray(rejectedValue)) {
/* 76 */       rejectedValue = StringUtils.arrayToCommaDelimitedString(ObjectUtils.toObjectArray(rejectedValue));
/*    */     }
/*    */     
/* 79 */     FieldError error = new FieldError(bindingResult.getObjectName(), field, rejectedValue, true, codes, arguments, ex.getLocalizedMessage());
/* 80 */     error.wrap(ex);
/* 81 */     bindingResult.addError(error);
/*    */   }
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
/*    */   protected Object[] getArgumentsForBindError(String objectName, String field) {
/* 96 */     String[] codes = { objectName + "." + field, field };
/* 97 */     return new Object[] { new DefaultMessageSourceResolvable(codes, field) };
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/DefaultBindingErrorProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */