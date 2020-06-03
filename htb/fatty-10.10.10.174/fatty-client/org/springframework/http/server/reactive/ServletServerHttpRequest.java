/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.AsyncEvent;
/*     */ import javax.servlet.AsyncListener;
/*     */ import javax.servlet.ReadListener;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DefaultDataBufferFactory;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import reactor.core.publisher.Flux;
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
/*     */ class ServletServerHttpRequest
/*     */   extends AbstractServerHttpRequest
/*     */ {
/*  59 */   static final DataBuffer EOF_BUFFER = (DataBuffer)(new DefaultDataBufferFactory()).allocateBuffer(0);
/*     */ 
/*     */   
/*     */   private final HttpServletRequest request;
/*     */   
/*     */   private final RequestBodyPublisher bodyPublisher;
/*     */   
/*  66 */   private final Object cookieLock = new Object();
/*     */ 
/*     */   
/*     */   private final DataBufferFactory bufferFactory;
/*     */ 
/*     */   
/*     */   private final byte[] buffer;
/*     */ 
/*     */   
/*     */   public ServletServerHttpRequest(HttpServletRequest request, AsyncContext asyncContext, String servletPath, DataBufferFactory bufferFactory, int bufferSize) throws IOException, URISyntaxException {
/*  76 */     this(createDefaultHttpHeaders(request), request, asyncContext, servletPath, bufferFactory, bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletServerHttpRequest(HttpHeaders headers, HttpServletRequest request, AsyncContext asyncContext, String servletPath, DataBufferFactory bufferFactory, int bufferSize) throws IOException, URISyntaxException {
/*  83 */     super(initUri(request), request.getContextPath() + servletPath, initHeaders(headers, request));
/*     */     
/*  85 */     Assert.notNull(bufferFactory, "'bufferFactory' must not be null");
/*  86 */     Assert.isTrue((bufferSize > 0), "'bufferSize' must be higher than 0");
/*     */     
/*  88 */     this.request = request;
/*  89 */     this.bufferFactory = bufferFactory;
/*  90 */     this.buffer = new byte[bufferSize];
/*     */     
/*  92 */     asyncContext.addListener(new RequestAsyncListener());
/*     */ 
/*     */     
/*  95 */     ServletInputStream inputStream = request.getInputStream();
/*  96 */     this.bodyPublisher = new RequestBodyPublisher(inputStream);
/*  97 */     this.bodyPublisher.registerReadListener();
/*     */   }
/*     */ 
/*     */   
/*     */   private static HttpHeaders createDefaultHttpHeaders(HttpServletRequest request) {
/* 102 */     HttpHeaders headers = new HttpHeaders();
/* 103 */     for (Enumeration<?> names = request.getHeaderNames(); names.hasMoreElements(); ) {
/* 104 */       String name = (String)names.nextElement();
/* 105 */       for (Enumeration<?> values = request.getHeaders(name); values.hasMoreElements();) {
/* 106 */         headers.add(name, (String)values.nextElement());
/*     */       }
/*     */     } 
/* 109 */     return headers;
/*     */   }
/*     */   
/*     */   private static URI initUri(HttpServletRequest request) throws URISyntaxException {
/* 113 */     Assert.notNull(request, "'request' must not be null");
/* 114 */     StringBuffer url = request.getRequestURL();
/* 115 */     String query = request.getQueryString();
/* 116 */     if (StringUtils.hasText(query)) {
/* 117 */       url.append('?').append(query);
/*     */     }
/* 119 */     return new URI(url.toString());
/*     */   }
/*     */   
/*     */   private static HttpHeaders initHeaders(HttpHeaders headers, HttpServletRequest request) {
/* 123 */     MediaType contentType = headers.getContentType();
/* 124 */     if (contentType == null) {
/* 125 */       String requestContentType = request.getContentType();
/* 126 */       if (StringUtils.hasLength(requestContentType)) {
/* 127 */         contentType = MediaType.parseMediaType(requestContentType);
/* 128 */         headers.setContentType(contentType);
/*     */       } 
/*     */     } 
/* 131 */     if (contentType != null && contentType.getCharset() == null) {
/* 132 */       String encoding = request.getCharacterEncoding();
/* 133 */       if (StringUtils.hasLength(encoding)) {
/* 134 */         Charset charset = Charset.forName(encoding);
/* 135 */         LinkedCaseInsensitiveMap<String, String> linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap();
/* 136 */         linkedCaseInsensitiveMap.putAll(contentType.getParameters());
/* 137 */         linkedCaseInsensitiveMap.put("charset", charset.toString());
/* 138 */         headers.setContentType(new MediaType(contentType
/* 139 */               .getType(), contentType.getSubtype(), (Map)linkedCaseInsensitiveMap));
/*     */       } 
/*     */     } 
/*     */     
/* 143 */     if (headers.getContentLength() == -1L) {
/* 144 */       int contentLength = request.getContentLength();
/* 145 */       if (contentLength != -1) {
/* 146 */         headers.setContentLength(contentLength);
/*     */       }
/*     */     } 
/* 149 */     return headers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/* 155 */     return this.request.getMethod();
/*     */   }
/*     */   
/*     */   protected MultiValueMap<String, HttpCookie> initCookies() {
/*     */     Cookie[] cookies;
/* 160 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/*     */     
/* 162 */     synchronized (this.cookieLock) {
/* 163 */       cookies = this.request.getCookies();
/*     */     } 
/* 165 */     if (cookies != null) {
/* 166 */       for (Cookie cookie : cookies) {
/* 167 */         String name = cookie.getName();
/* 168 */         HttpCookie httpCookie = new HttpCookie(name, cookie.getValue());
/* 169 */         linkedMultiValueMap.add(name, httpCookie);
/*     */       } 
/*     */     }
/* 172 */     return (MultiValueMap<String, HttpCookie>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getRemoteAddress() {
/* 177 */     return new InetSocketAddress(this.request.getRemoteHost(), this.request.getRemotePort());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected SslInfo initSslInfo() {
/* 182 */     X509Certificate[] certificates = getX509Certificates();
/* 183 */     return (certificates != null) ? new DefaultSslInfo(getSslSessionId(), certificates) : null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String getSslSessionId() {
/* 188 */     return (String)this.request.getAttribute("javax.servlet.request.ssl_session_id");
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private X509Certificate[] getX509Certificates() {
/* 193 */     String name = "javax.servlet.request.X509Certificate";
/* 194 */     return (X509Certificate[])this.request.getAttribute(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Flux<DataBuffer> getBody() {
/* 199 */     return Flux.from(this.bodyPublisher);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   DataBuffer readFromInputStream() throws IOException {
/* 210 */     int read = this.request.getInputStream().read(this.buffer);
/* 211 */     logBytesRead(read);
/*     */     
/* 213 */     if (read > 0) {
/* 214 */       DataBuffer dataBuffer = this.bufferFactory.allocateBuffer(read);
/* 215 */       dataBuffer.write(this.buffer, 0, read);
/* 216 */       return dataBuffer;
/*     */     } 
/*     */     
/* 219 */     if (read == -1) {
/* 220 */       return EOF_BUFFER;
/*     */     }
/*     */     
/* 223 */     return null;
/*     */   }
/*     */   
/*     */   protected final void logBytesRead(int read) {
/* 227 */     Log rsReadLogger = AbstractListenerReadPublisher.rsReadLogger;
/* 228 */     if (rsReadLogger.isTraceEnabled()) {
/* 229 */       rsReadLogger.trace(getLogPrefix() + "Read " + read + ((read != -1) ? " bytes" : ""));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getNativeRequest() {
/* 236 */     return (T)this.request;
/*     */   }
/*     */ 
/*     */   
/*     */   private final class RequestAsyncListener
/*     */     implements AsyncListener
/*     */   {
/*     */     private RequestAsyncListener() {}
/*     */     
/*     */     public void onStartAsync(AsyncEvent event) {}
/*     */     
/*     */     public void onTimeout(AsyncEvent event) {
/* 248 */       Throwable ex = event.getThrowable();
/* 249 */       ex = (ex != null) ? ex : new IllegalStateException("Async operation timeout.");
/* 250 */       ServletServerHttpRequest.this.bodyPublisher.onError(ex);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(AsyncEvent event) {
/* 255 */       ServletServerHttpRequest.this.bodyPublisher.onError(event.getThrowable());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete(AsyncEvent event) {
/* 260 */       ServletServerHttpRequest.this.bodyPublisher.onAllDataRead();
/*     */     }
/*     */   }
/*     */   
/*     */   private class RequestBodyPublisher
/*     */     extends AbstractListenerReadPublisher<DataBuffer>
/*     */   {
/*     */     private final ServletInputStream inputStream;
/*     */     
/*     */     public RequestBodyPublisher(ServletInputStream inputStream) {
/* 270 */       super(ServletServerHttpRequest.this.getLogPrefix());
/* 271 */       this.inputStream = inputStream;
/*     */     }
/*     */     
/*     */     public void registerReadListener() throws IOException {
/* 275 */       this.inputStream.setReadListener(new RequestBodyPublisherReadListener());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void checkOnDataAvailable() {
/* 280 */       if (this.inputStream.isReady() && !this.inputStream.isFinished()) {
/* 281 */         onDataAvailable();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     protected DataBuffer read() throws IOException {
/* 288 */       if (this.inputStream.isReady()) {
/* 289 */         DataBuffer dataBuffer = ServletServerHttpRequest.this.readFromInputStream();
/* 290 */         if (dataBuffer == ServletServerHttpRequest.EOF_BUFFER) {
/*     */           
/* 292 */           onAllDataRead();
/* 293 */           dataBuffer = null;
/*     */         } 
/* 295 */         return dataBuffer;
/*     */       } 
/* 297 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void readingPaused() {}
/*     */ 
/*     */     
/*     */     protected void discardData() {}
/*     */ 
/*     */     
/*     */     private class RequestBodyPublisherReadListener
/*     */       implements ReadListener
/*     */     {
/*     */       private RequestBodyPublisherReadListener() {}
/*     */ 
/*     */       
/*     */       public void onDataAvailable() throws IOException {
/* 315 */         ServletServerHttpRequest.RequestBodyPublisher.this.onDataAvailable();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onAllDataRead() throws IOException {
/* 320 */         ServletServerHttpRequest.RequestBodyPublisher.this.onAllDataRead();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable throwable) {
/* 325 */         ServletServerHttpRequest.RequestBodyPublisher.this.onError(throwable);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ServletServerHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */