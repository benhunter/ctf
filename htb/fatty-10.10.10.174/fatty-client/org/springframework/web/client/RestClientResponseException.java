/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.springframework.http.HttpHeaders;
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
/*     */ public class RestClientResponseException
/*     */   extends RestClientException
/*     */ {
/*     */   private static final long serialVersionUID = -8803556342728481792L;
/*  36 */   private static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int rawStatusCode;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String statusText;
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] responseBody;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final HttpHeaders responseHeaders;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final String responseCharset;
/*     */ 
/*     */ 
/*     */   
/*     */   public RestClientResponseException(String message, int statusCode, String statusText, @Nullable HttpHeaders responseHeaders, @Nullable byte[] responseBody, @Nullable Charset responseCharset) {
/*  63 */     super(message);
/*  64 */     this.rawStatusCode = statusCode;
/*  65 */     this.statusText = statusText;
/*  66 */     this.responseHeaders = responseHeaders;
/*  67 */     this.responseBody = (responseBody != null) ? responseBody : new byte[0];
/*  68 */     this.responseCharset = (responseCharset != null) ? responseCharset.name() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRawStatusCode() {
/*  76 */     return this.rawStatusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatusText() {
/*  83 */     return this.statusText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HttpHeaders getResponseHeaders() {
/*  91 */     return this.responseHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getResponseBodyAsByteArray() {
/*  98 */     return this.responseBody;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResponseBodyAsString() {
/* 105 */     if (this.responseCharset == null) {
/* 106 */       return new String(this.responseBody, DEFAULT_CHARSET);
/*     */     }
/*     */     try {
/* 109 */       return new String(this.responseBody, this.responseCharset);
/*     */     }
/* 111 */     catch (UnsupportedEncodingException ex) {
/*     */       
/* 113 */       throw new IllegalStateException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/RestClientResponseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */