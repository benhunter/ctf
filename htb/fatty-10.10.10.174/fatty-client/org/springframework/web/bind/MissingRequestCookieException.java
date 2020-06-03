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
/*    */ public class MissingRequestCookieException
/*    */   extends ServletRequestBindingException
/*    */ {
/*    */   private final String cookieName;
/*    */   private final MethodParameter parameter;
/*    */   
/*    */   public MissingRequestCookieException(String cookieName, MethodParameter parameter) {
/* 44 */     super("");
/* 45 */     this.cookieName = cookieName;
/* 46 */     this.parameter = parameter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 52 */     return "Missing cookie '" + this.cookieName + "' for method parameter of type " + this.parameter
/* 53 */       .getNestedParameterType().getSimpleName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getCookieName() {
/* 60 */     return this.cookieName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final MethodParameter getParameter() {
/* 67 */     return this.parameter;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/MissingRequestCookieException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */