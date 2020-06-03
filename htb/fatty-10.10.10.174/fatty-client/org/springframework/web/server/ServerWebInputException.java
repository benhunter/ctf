/*    */ package org.springframework.web.server;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class ServerWebInputException
/*    */   extends ResponseStatusException
/*    */ {
/*    */   @Nullable
/*    */   private final MethodParameter parameter;
/*    */   
/*    */   public ServerWebInputException(String reason) {
/* 42 */     this(reason, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ServerWebInputException(String reason, @Nullable MethodParameter parameter) {
/* 49 */     this(reason, parameter, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ServerWebInputException(String reason, @Nullable MethodParameter parameter, @Nullable Throwable cause) {
/* 56 */     super(HttpStatus.BAD_REQUEST, reason, cause);
/* 57 */     this.parameter = parameter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public MethodParameter getMethodParameter() {
/* 66 */     return this.parameter;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/ServerWebInputException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */