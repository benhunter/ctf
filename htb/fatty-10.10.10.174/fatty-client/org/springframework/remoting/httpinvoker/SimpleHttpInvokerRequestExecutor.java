/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Locale;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
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
/*     */ public class SimpleHttpInvokerRequestExecutor
/*     */   extends AbstractHttpInvokerRequestExecutor
/*     */ {
/*  47 */   private int connectTimeout = -1;
/*     */   
/*  49 */   private int readTimeout = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int connectTimeout) {
/*  59 */     this.connectTimeout = connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadTimeout(int readTimeout) {
/*  69 */     this.readTimeout = readTimeout;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws IOException, ClassNotFoundException {
/*  88 */     HttpURLConnection con = openConnection(config);
/*  89 */     prepareConnection(con, baos.size());
/*  90 */     writeRequestBody(config, con, baos);
/*  91 */     validateResponse(config, con);
/*  92 */     InputStream responseBody = readResponseBody(config, con);
/*     */     
/*  94 */     return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
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
/*     */   protected HttpURLConnection openConnection(HttpInvokerClientConfiguration config) throws IOException {
/* 106 */     URLConnection con = (new URL(config.getServiceUrl())).openConnection();
/* 107 */     if (!(con instanceof HttpURLConnection)) {
/* 108 */       throw new IOException("Service URL [" + config
/* 109 */           .getServiceUrl() + "] does not resolve to an HTTP connection");
/*     */     }
/* 111 */     return (HttpURLConnection)con;
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
/*     */   protected void prepareConnection(HttpURLConnection connection, int contentLength) throws IOException {
/* 126 */     if (this.connectTimeout >= 0) {
/* 127 */       connection.setConnectTimeout(this.connectTimeout);
/*     */     }
/* 129 */     if (this.readTimeout >= 0) {
/* 130 */       connection.setReadTimeout(this.readTimeout);
/*     */     }
/*     */     
/* 133 */     connection.setDoOutput(true);
/* 134 */     connection.setRequestMethod("POST");
/* 135 */     connection.setRequestProperty("Content-Type", getContentType());
/* 136 */     connection.setRequestProperty("Content-Length", Integer.toString(contentLength));
/*     */     
/* 138 */     LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
/* 139 */     if (localeContext != null) {
/* 140 */       Locale locale = localeContext.getLocale();
/* 141 */       if (locale != null) {
/* 142 */         connection.setRequestProperty("Accept-Language", locale.toLanguageTag());
/*     */       }
/*     */     } 
/*     */     
/* 146 */     if (isAcceptGzipEncoding()) {
/* 147 */       connection.setRequestProperty("Accept-Encoding", "gzip");
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeRequestBody(HttpInvokerClientConfiguration config, HttpURLConnection con, ByteArrayOutputStream baos) throws IOException {
/* 168 */     baos.writeTo(con.getOutputStream());
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
/*     */   protected void validateResponse(HttpInvokerClientConfiguration config, HttpURLConnection con) throws IOException {
/* 184 */     if (con.getResponseCode() >= 300) {
/* 185 */       throw new IOException("Did not receive successful HTTP response: status code = " + con
/* 186 */           .getResponseCode() + ", status message = [" + con
/* 187 */           .getResponseMessage() + "]");
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream readResponseBody(HttpInvokerClientConfiguration config, HttpURLConnection con) throws IOException {
/* 210 */     if (isGzipResponse(con))
/*     */     {
/* 212 */       return new GZIPInputStream(con.getInputStream());
/*     */     }
/*     */ 
/*     */     
/* 216 */     return con.getInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isGzipResponse(HttpURLConnection con) {
/* 227 */     String encodingHeader = con.getHeaderField("Content-Encoding");
/* 228 */     return (encodingHeader != null && encodingHeader.toLowerCase().contains("gzip"));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/httpinvoker/SimpleHttpInvokerRequestExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */