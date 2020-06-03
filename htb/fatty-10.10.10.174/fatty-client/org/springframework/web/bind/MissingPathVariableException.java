/*    */ package org.springframework.web.bind;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
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
/*    */ public class MissingPathVariableException
/*    */   extends ServletRequestBindingException
/*    */ {
/*    */   private final String variableName;
/*    */   private final MethodParameter parameter;
/*    */   
/*    */   public MissingPathVariableException(String variableName, MethodParameter parameter) {
/* 46 */     super("");
/* 47 */     this.variableName = variableName;
/* 48 */     this.parameter = parameter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 54 */     return "Missing URI template variable '" + this.variableName + "' for method parameter of type " + this.parameter
/* 55 */       .getNestedParameterType().getSimpleName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getVariableName() {
/* 62 */     return this.variableName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final MethodParameter getParameter() {
/* 69 */     return this.parameter;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/MissingPathVariableException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */