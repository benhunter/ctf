/*    */ package org.springframework.web.bind;
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
/*    */ public class MissingServletRequestParameterException
/*    */   extends ServletRequestBindingException
/*    */ {
/*    */   private final String parameterName;
/*    */   private final String parameterType;
/*    */   
/*    */   public MissingServletRequestParameterException(String parameterName, String parameterType) {
/* 39 */     super("");
/* 40 */     this.parameterName = parameterName;
/* 41 */     this.parameterType = parameterType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 47 */     return "Required " + this.parameterType + " parameter '" + this.parameterName + "' is not present";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getParameterName() {
/* 54 */     return this.parameterName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getParameterType() {
/* 61 */     return this.parameterType;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/MissingServletRequestParameterException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */