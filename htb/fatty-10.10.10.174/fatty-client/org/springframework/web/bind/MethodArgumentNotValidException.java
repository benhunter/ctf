/*    */ package org.springframework.web.bind;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.validation.BindingResult;
/*    */ import org.springframework.validation.ObjectError;
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
/*    */ public class MethodArgumentNotValidException
/*    */   extends Exception
/*    */ {
/*    */   private final MethodParameter parameter;
/*    */   private final BindingResult bindingResult;
/*    */   
/*    */   public MethodArgumentNotValidException(MethodParameter parameter, BindingResult bindingResult) {
/* 43 */     this.parameter = parameter;
/* 44 */     this.bindingResult = bindingResult;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodParameter getParameter() {
/* 51 */     return this.parameter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BindingResult getBindingResult() {
/* 58 */     return this.bindingResult;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 66 */     StringBuilder sb = (new StringBuilder("Validation failed for argument [")).append(this.parameter.getParameterIndex()).append("] in ").append(this.parameter.getExecutable().toGenericString());
/* 67 */     if (this.bindingResult.getErrorCount() > 1) {
/* 68 */       sb.append(" with ").append(this.bindingResult.getErrorCount()).append(" errors");
/*    */     }
/* 70 */     sb.append(": ");
/* 71 */     for (ObjectError error : this.bindingResult.getAllErrors()) {
/* 72 */       sb.append("[").append(error).append("] ");
/*    */     }
/* 74 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/MethodArgumentNotValidException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */