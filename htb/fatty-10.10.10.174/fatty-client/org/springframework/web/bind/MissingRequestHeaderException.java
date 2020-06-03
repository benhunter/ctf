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
/*    */ public class MissingRequestHeaderException
/*    */   extends ServletRequestBindingException
/*    */ {
/*    */   private final String headerName;
/*    */   private final MethodParameter parameter;
/*    */   
/*    */   public MissingRequestHeaderException(String headerName, MethodParameter parameter) {
/* 44 */     super("");
/* 45 */     this.headerName = headerName;
/* 46 */     this.parameter = parameter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 52 */     return "Missing request header '" + this.headerName + "' for method parameter of type " + this.parameter
/* 53 */       .getNestedParameterType().getSimpleName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getHeaderName() {
/* 60 */     return this.headerName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final MethodParameter getParameter() {
/* 67 */     return this.parameter;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/MissingRequestHeaderException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */