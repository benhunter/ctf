/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.WriteListener;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.FastByteArrayOutputStream;
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
/*     */ public class ContentCachingResponseWrapper
/*     */   extends HttpServletResponseWrapper
/*     */ {
/*  46 */   private final FastByteArrayOutputStream content = new FastByteArrayOutputStream(1024);
/*     */   
/*     */   @Nullable
/*     */   private ServletOutputStream outputStream;
/*     */   
/*     */   @Nullable
/*     */   private PrintWriter writer;
/*     */   
/*  54 */   private int statusCode = 200;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Integer contentLength;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentCachingResponseWrapper(HttpServletResponse response) {
/*  65 */     super(response);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(int sc) {
/*  71 */     super.setStatus(sc);
/*  72 */     this.statusCode = sc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(int sc, String sm) {
/*  78 */     super.setStatus(sc, sm);
/*  79 */     this.statusCode = sc;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendError(int sc) throws IOException {
/*  84 */     copyBodyToResponse(false);
/*     */     try {
/*  86 */       super.sendError(sc);
/*     */     }
/*  88 */     catch (IllegalStateException ex) {
/*     */       
/*  90 */       super.setStatus(sc);
/*     */     } 
/*  92 */     this.statusCode = sc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendError(int sc, String msg) throws IOException {
/*  98 */     copyBodyToResponse(false);
/*     */     try {
/* 100 */       super.sendError(sc, msg);
/*     */     }
/* 102 */     catch (IllegalStateException ex) {
/*     */       
/* 104 */       super.setStatus(sc, msg);
/*     */     } 
/* 106 */     this.statusCode = sc;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRedirect(String location) throws IOException {
/* 111 */     copyBodyToResponse(false);
/* 112 */     super.sendRedirect(location);
/*     */   }
/*     */ 
/*     */   
/*     */   public ServletOutputStream getOutputStream() throws IOException {
/* 117 */     if (this.outputStream == null) {
/* 118 */       this.outputStream = new ResponseServletOutputStream(getResponse().getOutputStream());
/*     */     }
/* 120 */     return this.outputStream;
/*     */   }
/*     */ 
/*     */   
/*     */   public PrintWriter getWriter() throws IOException {
/* 125 */     if (this.writer == null) {
/* 126 */       String characterEncoding = getCharacterEncoding();
/* 127 */       this.writer = (characterEncoding != null) ? new ResponsePrintWriter(characterEncoding) : new ResponsePrintWriter("ISO-8859-1");
/*     */     } 
/*     */     
/* 130 */     return this.writer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flushBuffer() throws IOException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentLength(int len) {
/* 140 */     if (len > this.content.size()) {
/* 141 */       this.content.resize(len);
/*     */     }
/* 143 */     this.contentLength = Integer.valueOf(len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContentLengthLong(long len) {
/* 148 */     if (len > 2147483647L) {
/* 149 */       throw new IllegalArgumentException("Content-Length exceeds ContentCachingResponseWrapper's maximum (2147483647): " + len);
/*     */     }
/*     */     
/* 152 */     int lenInt = (int)len;
/* 153 */     if (lenInt > this.content.size()) {
/* 154 */       this.content.resize(lenInt);
/*     */     }
/* 156 */     this.contentLength = Integer.valueOf(lenInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBufferSize(int size) {
/* 161 */     if (size > this.content.size()) {
/* 162 */       this.content.resize(size);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetBuffer() {
/* 168 */     this.content.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 173 */     super.reset();
/* 174 */     this.content.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStatusCode() {
/* 181 */     return this.statusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getContentAsByteArray() {
/* 188 */     return this.content.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getContentInputStream() {
/* 196 */     return this.content.getInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getContentSize() {
/* 204 */     return this.content.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyBodyToResponse() throws IOException {
/* 212 */     copyBodyToResponse(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyBodyToResponse(boolean complete) throws IOException {
/* 222 */     if (this.content.size() > 0) {
/* 223 */       HttpServletResponse rawResponse = (HttpServletResponse)getResponse();
/* 224 */       if ((complete || this.contentLength != null) && !rawResponse.isCommitted()) {
/* 225 */         rawResponse.setContentLength(complete ? this.content.size() : this.contentLength.intValue());
/* 226 */         this.contentLength = null;
/*     */       } 
/* 228 */       this.content.writeTo((OutputStream)rawResponse.getOutputStream());
/* 229 */       this.content.reset();
/* 230 */       if (complete) {
/* 231 */         super.flushBuffer();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private class ResponseServletOutputStream
/*     */     extends ServletOutputStream
/*     */   {
/*     */     private final ServletOutputStream os;
/*     */     
/*     */     public ResponseServletOutputStream(ServletOutputStream os) {
/* 242 */       this.os = os;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) throws IOException {
/* 247 */       ContentCachingResponseWrapper.this.content.write(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 252 */       ContentCachingResponseWrapper.this.content.write(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isReady() {
/* 257 */       return this.os.isReady();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setWriteListener(WriteListener writeListener) {
/* 262 */       this.os.setWriteListener(writeListener);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ResponsePrintWriter
/*     */     extends PrintWriter
/*     */   {
/*     */     public ResponsePrintWriter(String characterEncoding) throws UnsupportedEncodingException {
/* 270 */       super(new OutputStreamWriter((OutputStream)ContentCachingResponseWrapper.this.content, characterEncoding));
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(char[] buf, int off, int len) {
/* 275 */       super.write(buf, off, len);
/* 276 */       flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String s, int off, int len) {
/* 281 */       super.write(s, off, len);
/* 282 */       flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int c) {
/* 287 */       super.write(c);
/* 288 */       flush();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/ContentCachingResponseWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */