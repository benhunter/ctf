/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import org.apache.catalina.connector.CoyoteInputStream;
/*     */ import org.apache.catalina.connector.CoyoteOutputStream;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.RequestFacade;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.catalina.connector.ResponseFacade;
/*     */ import org.apache.coyote.Request;
/*     */ import org.apache.coyote.Response;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class TomcatHttpHandlerAdapter
/*     */   extends ServletHttpHandlerAdapter
/*     */ {
/*     */   public TomcatHttpHandlerAdapter(HttpHandler httpHandler) {
/*  60 */     super(httpHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletServerHttpRequest createRequest(HttpServletRequest request, AsyncContext asyncContext) throws IOException, URISyntaxException {
/*  68 */     Assert.notNull(getServletPath(), "Servlet path is not initialized");
/*  69 */     return new TomcatServerHttpRequest(request, asyncContext, 
/*  70 */         getServletPath(), getDataBufferFactory(), getBufferSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletServerHttpResponse createResponse(HttpServletResponse response, AsyncContext asyncContext, ServletServerHttpRequest request) throws IOException {
/*  77 */     return new TomcatServerHttpResponse(response, asyncContext, 
/*  78 */         getDataBufferFactory(), getBufferSize(), request);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class TomcatServerHttpRequest
/*     */     extends ServletServerHttpRequest
/*     */   {
/*     */     private static final Field COYOTE_REQUEST_FIELD;
/*     */     
/*     */     private final int bufferSize;
/*     */     private final DataBufferFactory factory;
/*     */     
/*     */     static {
/*  91 */       Field field = ReflectionUtils.findField(RequestFacade.class, "request");
/*  92 */       Assert.state((field != null), "Incompatible Tomcat implementation");
/*  93 */       ReflectionUtils.makeAccessible(field);
/*  94 */       COYOTE_REQUEST_FIELD = field;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     TomcatServerHttpRequest(HttpServletRequest request, AsyncContext context, String servletPath, DataBufferFactory factory, int bufferSize) throws IOException, URISyntaxException {
/* 101 */       super(createTomcatHttpHeaders(request), request, context, servletPath, factory, bufferSize);
/* 102 */       this.factory = factory;
/* 103 */       this.bufferSize = bufferSize;
/*     */     }
/*     */     
/*     */     private static HttpHeaders createTomcatHttpHeaders(HttpServletRequest request) {
/* 107 */       RequestFacade requestFacade = getRequestFacade(request);
/*     */       
/* 109 */       Request connectorRequest = (Request)ReflectionUtils.getField(COYOTE_REQUEST_FIELD, requestFacade);
/* 110 */       Assert.state((connectorRequest != null), "No Tomcat connector request");
/* 111 */       Request tomcatRequest = connectorRequest.getCoyoteRequest();
/* 112 */       TomcatHeadersAdapter headers = new TomcatHeadersAdapter(tomcatRequest.getMimeHeaders());
/* 113 */       return new HttpHeaders(headers);
/*     */     }
/*     */     
/*     */     private static RequestFacade getRequestFacade(HttpServletRequest request) {
/* 117 */       if (request instanceof RequestFacade) {
/* 118 */         return (RequestFacade)request;
/*     */       }
/* 120 */       if (request instanceof HttpServletRequestWrapper) {
/* 121 */         HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper)request;
/* 122 */         HttpServletRequest wrappedRequest = (HttpServletRequest)wrapper.getRequest();
/* 123 */         return getRequestFacade(wrappedRequest);
/*     */       } 
/*     */       
/* 126 */       throw new IllegalArgumentException("Cannot convert [" + request.getClass() + "] to org.apache.catalina.connector.RequestFacade");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected DataBuffer readFromInputStream() throws IOException {
/* 133 */       boolean release = true;
/* 134 */       int capacity = this.bufferSize;
/* 135 */       DataBuffer dataBuffer = this.factory.allocateBuffer(capacity);
/*     */       try {
/* 137 */         ByteBuffer byteBuffer = dataBuffer.asByteBuffer(0, capacity);
/* 138 */         ServletRequest request = getNativeRequest();
/* 139 */         int read = ((CoyoteInputStream)request.getInputStream()).read(byteBuffer);
/* 140 */         logBytesRead(read);
/* 141 */         if (read > 0) {
/* 142 */           dataBuffer.writePosition(read);
/* 143 */           release = false;
/* 144 */           return dataBuffer;
/*     */         } 
/* 146 */         if (read == -1) {
/* 147 */           return EOF_BUFFER;
/*     */         }
/*     */         
/* 150 */         return null;
/*     */       }
/*     */       finally {
/*     */         
/* 154 */         if (release) {
/* 155 */           DataBufferUtils.release(dataBuffer);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TomcatServerHttpResponse
/*     */     extends ServletServerHttpResponse
/*     */   {
/*     */     private static final Field COYOTE_RESPONSE_FIELD;
/*     */     
/*     */     static {
/* 167 */       Field field = ReflectionUtils.findField(ResponseFacade.class, "response");
/* 168 */       Assert.state((field != null), "Incompatible Tomcat implementation");
/* 169 */       ReflectionUtils.makeAccessible(field);
/* 170 */       COYOTE_RESPONSE_FIELD = field;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     TomcatServerHttpResponse(HttpServletResponse response, AsyncContext context, DataBufferFactory factory, int bufferSize, ServletServerHttpRequest request) throws IOException {
/* 176 */       super(createTomcatHttpHeaders(response), response, context, factory, bufferSize, request);
/*     */     }
/*     */     
/*     */     private static HttpHeaders createTomcatHttpHeaders(HttpServletResponse response) {
/* 180 */       ResponseFacade responseFacade = getResponseFacade(response);
/*     */       
/* 182 */       Response connectorResponse = (Response)ReflectionUtils.getField(COYOTE_RESPONSE_FIELD, responseFacade);
/* 183 */       Assert.state((connectorResponse != null), "No Tomcat connector response");
/* 184 */       Response tomcatResponse = connectorResponse.getCoyoteResponse();
/* 185 */       TomcatHeadersAdapter headers = new TomcatHeadersAdapter(tomcatResponse.getMimeHeaders());
/* 186 */       return new HttpHeaders(headers);
/*     */     }
/*     */     
/*     */     private static ResponseFacade getResponseFacade(HttpServletResponse response) {
/* 190 */       if (response instanceof ResponseFacade) {
/* 191 */         return (ResponseFacade)response;
/*     */       }
/* 193 */       if (response instanceof HttpServletResponseWrapper) {
/* 194 */         HttpServletResponseWrapper wrapper = (HttpServletResponseWrapper)response;
/* 195 */         HttpServletResponse wrappedResponse = (HttpServletResponse)wrapper.getResponse();
/* 196 */         return getResponseFacade(wrappedResponse);
/*     */       } 
/*     */       
/* 199 */       throw new IllegalArgumentException("Cannot convert [" + response.getClass() + "] to org.apache.catalina.connector.ResponseFacade");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void applyHeaders() {
/* 206 */       HttpServletResponse response = getNativeResponse();
/* 207 */       MediaType contentType = getHeaders().getContentType();
/* 208 */       if (response.getContentType() == null && contentType != null) {
/* 209 */         response.setContentType(contentType.toString());
/*     */       }
/* 211 */       Charset charset = (contentType != null) ? contentType.getCharset() : null;
/* 212 */       if (response.getCharacterEncoding() == null && charset != null) {
/* 213 */         response.setCharacterEncoding(charset.name());
/*     */       }
/* 215 */       long contentLength = getHeaders().getContentLength();
/* 216 */       if (contentLength != -1L) {
/* 217 */         response.setContentLengthLong(contentLength);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected int writeToOutputStream(DataBuffer dataBuffer) throws IOException {
/* 223 */       ByteBuffer input = dataBuffer.asByteBuffer();
/* 224 */       int len = input.remaining();
/* 225 */       ServletResponse response = getNativeResponse();
/* 226 */       ((CoyoteOutputStream)response.getOutputStream()).write(input);
/* 227 */       return len;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/TomcatHttpHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */