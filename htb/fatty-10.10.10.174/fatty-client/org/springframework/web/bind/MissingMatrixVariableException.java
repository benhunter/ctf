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
/*    */ public class MissingMatrixVariableException
/*    */   extends ServletRequestBindingException
/*    */ {
/*    */   private final String variableName;
/*    */   private final MethodParameter parameter;
/*    */   
/*    */   public MissingMatrixVariableException(String variableName, MethodParameter parameter) {
/* 44 */     super("");
/* 45 */     this.variableName = variableName;
/* 46 */     this.parameter = parameter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 52 */     return "Missing matrix variable '" + this.variableName + "' for method parameter of type " + this.parameter
/* 53 */       .getNestedParameterType().getSimpleName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getVariableName() {
/* 60 */     return this.variableName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final MethodParameter getParameter() {
/* 67 */     return this.parameter;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/MissingMatrixVariableException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */