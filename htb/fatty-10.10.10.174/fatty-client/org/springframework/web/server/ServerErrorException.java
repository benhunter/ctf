/*     */ package org.springframework.web.server;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerErrorException
/*     */   extends ResponseStatusException
/*     */ {
/*     */   @Nullable
/*     */   private final Method handlerMethod;
/*     */   @Nullable
/*     */   private final MethodParameter parameter;
/*     */   
/*     */   public ServerErrorException(String reason, @Nullable Throwable cause) {
/*  48 */     super(HttpStatus.INTERNAL_SERVER_ERROR, reason, cause);
/*  49 */     this.handlerMethod = null;
/*  50 */     this.parameter = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerErrorException(String reason, Method handlerMethod, @Nullable Throwable cause) {
/*  58 */     super(HttpStatus.INTERNAL_SERVER_ERROR, reason, cause);
/*  59 */     this.handlerMethod = handlerMethod;
/*  60 */     this.parameter = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerErrorException(String reason, MethodParameter parameter, @Nullable Throwable cause) {
/*  67 */     super(HttpStatus.INTERNAL_SERVER_ERROR, reason, cause);
/*  68 */     this.handlerMethod = parameter.getMethod();
/*  69 */     this.parameter = parameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ServerErrorException(String reason, MethodParameter parameter) {
/*  78 */     this(reason, parameter, (Throwable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ServerErrorException(String reason) {
/*  87 */     super(HttpStatus.INTERNAL_SERVER_ERROR, reason, null);
/*  88 */     this.handlerMethod = null;
/*  89 */     this.parameter = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getHandlerMethod() {
/*  99 */     return this.handlerMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MethodParameter getMethodParameter() {
/* 107 */     return this.parameter;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/ServerErrorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */