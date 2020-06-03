/*     */ package org.springframework.http.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public class ServletServerHttpResponse
/*     */   implements ServerHttpResponse
/*     */ {
/*     */   private final HttpServletResponse servletResponse;
/*     */   private final HttpHeaders headers;
/*     */   private boolean headersWritten = false;
/*     */   private boolean bodyUsed = false;
/*     */   
/*     */   public ServletServerHttpResponse(HttpServletResponse servletResponse) {
/*  55 */     Assert.notNull(servletResponse, "HttpServletResponse must not be null");
/*  56 */     this.servletResponse = servletResponse;
/*  57 */     this.headers = new ServletResponseHttpHeaders();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServletResponse getServletResponse() {
/*  65 */     return this.servletResponse;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatusCode(HttpStatus status) {
/*  70 */     Assert.notNull(status, "HttpStatus must not be null");
/*  71 */     this.servletResponse.setStatus(status.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/*  76 */     return this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream getBody() throws IOException {
/*  81 */     this.bodyUsed = true;
/*  82 */     writeHeaders();
/*  83 */     return (OutputStream)this.servletResponse.getOutputStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  88 */     writeHeaders();
/*  89 */     if (this.bodyUsed) {
/*  90 */       this.servletResponse.flushBuffer();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  96 */     writeHeaders();
/*     */   }
/*     */   
/*     */   private void writeHeaders() {
/* 100 */     if (!this.headersWritten) {
/* 101 */       getHeaders().forEach((headerName, headerValues) -> {
/*     */             for (String headerValue : headerValues) {
/*     */               this.servletResponse.addHeader(headerName, headerValue);
/*     */             }
/*     */           });
/*     */       
/* 107 */       if (this.servletResponse.getContentType() == null && this.headers.getContentType() != null) {
/* 108 */         this.servletResponse.setContentType(this.headers.getContentType().toString());
/*     */       }
/* 110 */       if (this.servletResponse.getCharacterEncoding() == null && this.headers.getContentType() != null && this.headers
/* 111 */         .getContentType().getCharset() != null) {
/* 112 */         this.servletResponse.setCharacterEncoding(this.headers.getContentType().getCharset().name());
/*     */       }
/* 114 */       this.headersWritten = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ServletResponseHttpHeaders
/*     */     extends HttpHeaders
/*     */   {
/*     */     private static final long serialVersionUID = 3410708522401046302L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ServletResponseHttpHeaders() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 136 */       return (super.containsKey(key) || get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getFirst(String headerName) {
/* 142 */       String value = ServletServerHttpResponse.this.servletResponse.getHeader(headerName);
/* 143 */       if (value != null) {
/* 144 */         return value;
/*     */       }
/*     */       
/* 147 */       return super.getFirst(headerName);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public List<String> get(Object key) {
/* 153 */       Assert.isInstanceOf(String.class, key, "Key must be a String-based header name");
/*     */       
/* 155 */       Collection<String> values1 = ServletServerHttpResponse.this.servletResponse.getHeaders((String)key);
/* 156 */       if (ServletServerHttpResponse.this.headersWritten) {
/* 157 */         return new ArrayList<>(values1);
/*     */       }
/* 159 */       boolean isEmpty1 = CollectionUtils.isEmpty(values1);
/*     */       
/* 161 */       List<String> values2 = super.get(key);
/* 162 */       boolean isEmpty2 = CollectionUtils.isEmpty(values2);
/*     */       
/* 164 */       if (isEmpty1 && isEmpty2) {
/* 165 */         return null;
/*     */       }
/*     */       
/* 168 */       List<String> values = new ArrayList<>();
/* 169 */       if (!isEmpty1) {
/* 170 */         values.addAll(values1);
/*     */       }
/* 172 */       if (!isEmpty2) {
/* 173 */         values.addAll(values2);
/*     */       }
/* 175 */       return values;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/ServletServerHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */