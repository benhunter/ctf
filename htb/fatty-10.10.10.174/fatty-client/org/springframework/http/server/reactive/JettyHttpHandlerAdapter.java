/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.server.HttpOutput;
/*     */ import org.eclipse.jetty.server.Request;
/*     */ import org.eclipse.jetty.server.Response;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class JettyHttpHandlerAdapter
/*     */   extends ServletHttpHandlerAdapter
/*     */ {
/*     */   public JettyHttpHandlerAdapter(HttpHandler httpHandler) {
/*  51 */     super(httpHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletServerHttpRequest createRequest(HttpServletRequest request, AsyncContext context) throws IOException, URISyntaxException {
/*  59 */     Assert.notNull(getServletPath(), "Servlet path is not initialized");
/*  60 */     return new JettyServerHttpRequest(request, context, getServletPath(), getDataBufferFactory(), getBufferSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletServerHttpResponse createResponse(HttpServletResponse response, AsyncContext context, ServletServerHttpRequest request) throws IOException {
/*  67 */     return new JettyServerHttpResponse(response, context, 
/*  68 */         getDataBufferFactory(), getBufferSize(), request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class JettyServerHttpRequest
/*     */     extends ServletServerHttpRequest
/*     */   {
/*     */     JettyServerHttpRequest(HttpServletRequest request, AsyncContext asyncContext, String servletPath, DataBufferFactory bufferFactory, int bufferSize) throws IOException, URISyntaxException {
/*  78 */       super(createHeaders(request), request, asyncContext, servletPath, bufferFactory, bufferSize);
/*     */     }
/*     */     
/*     */     private static HttpHeaders createHeaders(HttpServletRequest request) {
/*  82 */       HttpFields fields = ((Request)request).getMetaData().getFields();
/*  83 */       return new HttpHeaders(new JettyHeadersAdapter(fields));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class JettyServerHttpResponse
/*     */     extends ServletServerHttpResponse
/*     */   {
/*     */     JettyServerHttpResponse(HttpServletResponse response, AsyncContext asyncContext, DataBufferFactory bufferFactory, int bufferSize, ServletServerHttpRequest request) throws IOException {
/*  94 */       super(createHeaders(response), response, asyncContext, bufferFactory, bufferSize, request);
/*     */     }
/*     */     
/*     */     private static HttpHeaders createHeaders(HttpServletResponse response) {
/*  98 */       HttpFields fields = ((Response)response).getHttpFields();
/*  99 */       return new HttpHeaders(new JettyHeadersAdapter(fields));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void applyHeaders() {
/* 104 */       MediaType contentType = getHeaders().getContentType();
/* 105 */       HttpServletResponse response = getNativeResponse();
/* 106 */       if (response.getContentType() == null && contentType != null) {
/* 107 */         response.setContentType(contentType.toString());
/*     */       }
/* 109 */       Charset charset = (contentType != null) ? contentType.getCharset() : null;
/* 110 */       if (response.getCharacterEncoding() == null && charset != null) {
/* 111 */         response.setCharacterEncoding(charset.name());
/*     */       }
/* 113 */       long contentLength = getHeaders().getContentLength();
/* 114 */       if (contentLength != -1L) {
/* 115 */         response.setContentLengthLong(contentLength);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected int writeToOutputStream(DataBuffer dataBuffer) throws IOException {
/* 121 */       ByteBuffer input = dataBuffer.asByteBuffer();
/* 122 */       int len = input.remaining();
/* 123 */       ServletResponse response = getNativeResponse();
/* 124 */       ((HttpOutput)response.getOutputStream()).write(input);
/* 125 */       return len;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/JettyHttpHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */