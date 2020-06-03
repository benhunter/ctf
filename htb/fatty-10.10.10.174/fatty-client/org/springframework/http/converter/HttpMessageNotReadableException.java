/*    */ package org.springframework.http.converter;
/*    */ 
/*    */ import org.springframework.http.HttpInputMessage;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class HttpMessageNotReadableException
/*    */   extends HttpMessageConversionException
/*    */ {
/*    */   @Nullable
/*    */   private final HttpInputMessage httpInputMessage;
/*    */   
/*    */   @Deprecated
/*    */   public HttpMessageNotReadableException(String msg) {
/* 45 */     super(msg);
/* 46 */     this.httpInputMessage = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public HttpMessageNotReadableException(String msg, @Nullable Throwable cause) {
/* 57 */     super(msg, cause);
/* 58 */     this.httpInputMessage = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpMessageNotReadableException(String msg, HttpInputMessage httpInputMessage) {
/* 68 */     super(msg);
/* 69 */     this.httpInputMessage = httpInputMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpMessageNotReadableException(String msg, @Nullable Throwable cause, HttpInputMessage httpInputMessage) {
/* 80 */     super(msg, cause);
/* 81 */     this.httpInputMessage = httpInputMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpInputMessage getHttpInputMessage() {
/* 90 */     Assert.state((this.httpInputMessage != null), "No HttpInputMessage available - use non-deprecated constructors");
/* 91 */     return this.httpInputMessage;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/HttpMessageNotReadableException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */