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
/*     */ public class HttpClientErrorException
/*     */   extends HttpStatusCodeException
/*     */ {
/*     */   private static final long serialVersionUID = 5177019431887513952L;
/*     */   
/*     */   public HttpClientErrorException(HttpStatus statusCode) {
/*  41 */     super(statusCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClientErrorException(HttpStatus statusCode, String statusText) {
/*  48 */     super(statusCode, statusText);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClientErrorException(HttpStatus statusCode, String statusText, @Nullable byte[] body, @Nullable Charset responseCharset) {
/*  57 */     super(statusCode, statusText, body, responseCharset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClientErrorException(HttpStatus statusCode, String statusText, @Nullable HttpHeaders headers, @Nullable byte[] body, @Nullable Charset responseCharset) {
/*  66 */     super(statusCode, statusText, headers, body, responseCharset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpClientErrorException create(HttpStatus statusCode, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/*  77 */     switch (statusCode) {
/*     */       case BAD_REQUEST:
/*  79 */         return new BadRequest(statusText, headers, body, charset);
/*     */       case UNAUTHORIZED:
/*  81 */         return new Unauthorized(statusText, headers, body, charset);
/*     */       case FORBIDDEN:
/*  83 */         return new Forbidden(statusText, headers, body, charset);
/*     */       case NOT_FOUND:
/*  85 */         return new NotFound(statusText, headers, body, charset);
/*     */       case METHOD_NOT_ALLOWED:
/*  87 */         return new MethodNotAllowed(statusText, headers, body, charset);
/*     */       case NOT_ACCEPTABLE:
/*  89 */         return new NotAcceptable(statusText, headers, body, charset);
/*     */       case CONFLICT:
/*  91 */         return new Conflict(statusText, headers, body, charset);
/*     */       case GONE:
/*  93 */         return new Gone(statusText, headers, body, charset);
/*     */       case UNSUPPORTED_MEDIA_TYPE:
/*  95 */         return new UnsupportedMediaType(statusText, headers, body, charset);
/*     */       case TOO_MANY_REQUESTS:
/*  97 */         return new TooManyRequests(statusText, headers, body, charset);
/*     */       case UNPROCESSABLE_ENTITY:
/*  99 */         return new UnprocessableEntity(statusText, headers, body, charset);
/*     */     } 
/* 101 */     return new HttpClientErrorException(statusCode, statusText, headers, body, charset);
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
/*     */   public static class BadRequest
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     BadRequest(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 116 */       super(HttpStatus.BAD_REQUEST, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Unauthorized
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     Unauthorized(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 128 */       super(HttpStatus.UNAUTHORIZED, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Forbidden
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     Forbidden(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 140 */       super(HttpStatus.FORBIDDEN, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NotFound
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     NotFound(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 152 */       super(HttpStatus.NOT_FOUND, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MethodNotAllowed
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     MethodNotAllowed(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 164 */       super(HttpStatus.METHOD_NOT_ALLOWED, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NotAcceptable
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     NotAcceptable(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 176 */       super(HttpStatus.NOT_ACCEPTABLE, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Conflict
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     Conflict(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 188 */       super(HttpStatus.CONFLICT, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Gone
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     Gone(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 200 */       super(HttpStatus.GONE, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class UnsupportedMediaType
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     UnsupportedMediaType(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 212 */       super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class UnprocessableEntity
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     UnprocessableEntity(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 224 */       super(HttpStatus.UNPROCESSABLE_ENTITY, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TooManyRequests
/*     */     extends HttpClientErrorException
/*     */   {
/*     */     TooManyRequests(String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
/* 236 */       super(HttpStatus.TOO_MANY_REQUESTS, statusText, headers, body, charset);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/HttpClientErrorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */