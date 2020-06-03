/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.client.ClientHttpResponse;
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
/*     */ class MessageBodyClientHttpResponseWrapper
/*     */   implements ClientHttpResponse
/*     */ {
/*     */   private final ClientHttpResponse response;
/*     */   @Nullable
/*     */   private PushbackInputStream pushbackInputStream;
/*     */   
/*     */   public MessageBodyClientHttpResponseWrapper(ClientHttpResponse response) throws IOException {
/*  46 */     this.response = response;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMessageBody() throws IOException {
/*  61 */     HttpStatus status = HttpStatus.resolve(getRawStatusCode());
/*  62 */     if (status != null && (status.is1xxInformational() || status == HttpStatus.NO_CONTENT || status == HttpStatus.NOT_MODIFIED))
/*     */     {
/*  64 */       return false;
/*     */     }
/*  66 */     if (getHeaders().getContentLength() == 0L) {
/*  67 */       return false;
/*     */     }
/*  69 */     return true;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasEmptyMessageBody() throws IOException {
/*  84 */     InputStream body = this.response.getBody();
/*     */     
/*  86 */     if (body == null) {
/*  87 */       return true;
/*     */     }
/*  89 */     if (body.markSupported()) {
/*  90 */       body.mark(1);
/*  91 */       if (body.read() == -1) {
/*  92 */         return true;
/*     */       }
/*     */       
/*  95 */       body.reset();
/*  96 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 100 */     this.pushbackInputStream = new PushbackInputStream(body);
/* 101 */     int b = this.pushbackInputStream.read();
/* 102 */     if (b == -1) {
/* 103 */       return true;
/*     */     }
/*     */     
/* 106 */     this.pushbackInputStream.unread(b);
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/* 115 */     return this.response.getHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getBody() throws IOException {
/* 120 */     return (this.pushbackInputStream != null) ? this.pushbackInputStream : this.response.getBody();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpStatus getStatusCode() throws IOException {
/* 125 */     return this.response.getStatusCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRawStatusCode() throws IOException {
/* 130 */     return this.response.getRawStatusCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStatusText() throws IOException {
/* 135 */     return this.response.getStatusText();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 140 */     this.response.close();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/MessageBodyClientHttpResponseWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */