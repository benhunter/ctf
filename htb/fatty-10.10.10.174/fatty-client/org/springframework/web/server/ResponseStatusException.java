/*    */ package org.springframework.web.server;
/*    */ 
/*    */ import org.springframework.core.NestedExceptionUtils;
/*    */ import org.springframework.core.NestedRuntimeException;
/*    */ import org.springframework.http.HttpStatus;
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
/*    */ public class ResponseStatusException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   private final HttpStatus status;
/*    */   @Nullable
/*    */   private final String reason;
/*    */   
/*    */   public ResponseStatusException(HttpStatus status) {
/* 46 */     this(status, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResponseStatusException(HttpStatus status, @Nullable String reason) {
/* 56 */     this(status, reason, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResponseStatusException(HttpStatus status, @Nullable String reason, @Nullable Throwable cause) {
/* 67 */     super(null, cause);
/* 68 */     Assert.notNull(status, "HttpStatus is required");
/* 69 */     this.status = status;
/* 70 */     this.reason = reason;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpStatus getStatus() {
/* 78 */     return this.status;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getReason() {
/* 86 */     return this.reason;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 91 */     String msg = this.status + ((this.reason != null) ? (" \"" + this.reason + "\"") : "");
/* 92 */     return NestedExceptionUtils.buildMessage(msg, getCause());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/ResponseStatusException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */