/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.http.HttpHeaders;
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
/*     */ public class HttpServerErrorException
/*     */   extends HttpStatusCodeException
/*     */ {
/*     */   private static final long serialVersionUID = -2915754006618138282L;
/*     */   
/*     */   public HttpServerErrorException(HttpStatus statusCode) {
/*  41 */     super(statusCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServerErrorException(HttpStatus statusCode, String statusText) {
/*  48 */     super(statusCode, statusText);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServerErrorException(HttpStatus statusCode, String statusText, @Nullable byte[] body, @Nullable Charset charset) {
/*  57 */     super(statusCode, statusText, body, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServerErrorException(HttpStatus statusCode, String statusText, @Nullable HttpHeaders headers, @Nullable byte[] body, @Nullable Charset charset) {
/*  66 */     super(statusCode, statusText, headers, body, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpServerErrorException create(HttpStatus statusCode, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/*  77 */     switch (statusCode) {
/*     */       case INTERNAL_SERVER_ERROR:
/*  79 */         return new InternalServerError(statusText, headers, body, charset);
/*     */       case NOT_IMPLEMENTED:
/*  81 */         return new NotImplemented(statusText, headers, body, charset);
/*     */       case BAD_GATEWAY:
/*  83 */         return new BadGateway(statusText, headers, body, charset);
/*     */       case SERVICE_UNAVAILABLE:
/*  85 */         return new ServiceUnavailable(statusText, headers, body, charset);
/*     */       case GATEWAY_TIMEOUT:
/*  87 */         return new GatewayTimeout(statusText, headers, body, charset);
/*     */     } 
/*  89 */     return new HttpServerErrorException(statusCode, statusText, headers, body, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class InternalServerError
/*     */     extends HttpServerErrorException
/*     */   {
/*     */     InternalServerError(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 104 */       super(HttpStatus.INTERNAL_SERVER_ERROR, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NotImplemented
/*     */     extends HttpServerErrorException
/*     */   {
/*     */     NotImplemented(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 116 */       super(HttpStatus.NOT_IMPLEMENTED, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BadGateway
/*     */     extends HttpServerErrorException
/*     */   {
/*     */     BadGateway(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 128 */       super(HttpStatus.BAD_GATEWAY, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ServiceUnavailable
/*     */     extends HttpServerErrorException
/*     */   {
/*     */     ServiceUnavailable(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 140 */       super(HttpStatus.SERVICE_UNAVAILABLE, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class GatewayTimeout
/*     */     extends HttpServerErrorException
/*     */   {
/*     */     GatewayTimeout(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 152 */       super(HttpStatus.GATEWAY_TIMEOUT, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/HttpServerErrorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */