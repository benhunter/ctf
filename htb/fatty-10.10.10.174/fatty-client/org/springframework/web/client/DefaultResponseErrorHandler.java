/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.FileCopyUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultResponseErrorHandler
/*     */   implements ResponseErrorHandler
/*     */ {
/*     */   public boolean hasError(ClientHttpResponse response) throws IOException {
/*  55 */     int rawStatusCode = response.getRawStatusCode();
/*  56 */     HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
/*  57 */     return (statusCode != null) ? hasError(statusCode) : hasError(rawStatusCode);
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
/*     */   protected boolean hasError(HttpStatus statusCode) {
/*  69 */     return statusCode.isError();
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
/*     */   
/*     */   protected boolean hasError(int unknownStatusCode) {
/*  85 */     HttpStatus.Series series = HttpStatus.Series.resolve(unknownStatusCode);
/*  86 */     return (series == HttpStatus.Series.CLIENT_ERROR || series == HttpStatus.Series.SERVER_ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleError(ClientHttpResponse response) throws IOException {
/*  97 */     HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
/*  98 */     if (statusCode == null) {
/*  99 */       throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(), response
/* 100 */           .getHeaders(), getResponseBody(response), getCharset(response));
/*     */     }
/* 102 */     handleError(response, statusCode);
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
/*     */   protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
/* 116 */     String statusText = response.getStatusText();
/* 117 */     HttpHeaders headers = response.getHeaders();
/* 118 */     byte[] body = getResponseBody(response);
/* 119 */     Charset charset = getCharset(response);
/* 120 */     switch (statusCode.series()) {
/*     */       case CLIENT_ERROR:
/* 122 */         throw HttpClientErrorException.create(statusCode, statusText, headers, body, charset);
/*     */       case SERVER_ERROR:
/* 124 */         throw HttpServerErrorException.create(statusCode, statusText, headers, body, charset);
/*     */     } 
/* 126 */     throw new UnknownHttpStatusCodeException(statusCode.value(), statusText, headers, body, charset);
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
/*     */   @Deprecated
/*     */   protected HttpStatus getHttpStatusCode(ClientHttpResponse response) throws IOException {
/* 142 */     HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
/* 143 */     if (statusCode == null) {
/* 144 */       throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(), response
/* 145 */           .getHeaders(), getResponseBody(response), getCharset(response));
/*     */     }
/* 147 */     return statusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] getResponseBody(ClientHttpResponse response) {
/*     */     try {
/* 159 */       return FileCopyUtils.copyToByteArray(response.getBody());
/*     */     }
/* 161 */     catch (IOException iOException) {
/*     */ 
/*     */       
/* 164 */       return new byte[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Charset getCharset(ClientHttpResponse response) {
/* 175 */     HttpHeaders headers = response.getHeaders();
/* 176 */     MediaType contentType = headers.getContentType();
/* 177 */     return (contentType != null) ? contentType.getCharset() : null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/DefaultResponseErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */