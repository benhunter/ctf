/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.AsyncEvent;
/*     */ import javax.servlet.AsyncListener;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRegistration;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DefaultDataBufferFactory;
/*     */ import org.springframework.http.HttpLogging;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ public class ServletHttpHandlerAdapter
/*     */   implements Servlet
/*     */ {
/*  60 */   private static final Log logger = HttpLogging.forLogName(ServletHttpHandlerAdapter.class);
/*     */   
/*     */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   
/*  64 */   private static final String WRITE_ERROR_ATTRIBUTE_NAME = ServletHttpHandlerAdapter.class.getName() + ".ERROR";
/*     */ 
/*     */   
/*     */   private final HttpHandler httpHandler;
/*     */   
/*  69 */   private int bufferSize = 8192;
/*     */   
/*     */   @Nullable
/*     */   private String servletPath;
/*     */   
/*  74 */   private DataBufferFactory dataBufferFactory = (DataBufferFactory)new DefaultDataBufferFactory(false);
/*     */ 
/*     */   
/*     */   public ServletHttpHandlerAdapter(HttpHandler httpHandler) {
/*  78 */     Assert.notNull(httpHandler, "HttpHandler must not be null");
/*  79 */     this.httpHandler = httpHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferSize(int bufferSize) {
/*  88 */     Assert.isTrue((bufferSize > 0), "Buffer size must be larger than zero");
/*  89 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/*  96 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getServletPath() {
/* 108 */     return this.servletPath;
/*     */   }
/*     */   
/*     */   public void setDataBufferFactory(DataBufferFactory dataBufferFactory) {
/* 112 */     Assert.notNull(dataBufferFactory, "DataBufferFactory must not be null");
/* 113 */     this.dataBufferFactory = dataBufferFactory;
/*     */   }
/*     */   
/*     */   public DataBufferFactory getDataBufferFactory() {
/* 117 */     return this.dataBufferFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(ServletConfig config) {
/* 125 */     this.servletPath = getServletPath(config);
/*     */   }
/*     */   
/*     */   private String getServletPath(ServletConfig config) {
/* 129 */     String name = config.getServletName();
/* 130 */     ServletRegistration registration = config.getServletContext().getServletRegistration(name);
/* 131 */     if (registration == null) {
/* 132 */       throw new IllegalStateException("ServletRegistration not found for Servlet '" + name + "'");
/*     */     }
/*     */     
/* 135 */     Collection<String> mappings = registration.getMappings();
/* 136 */     if (mappings.size() == 1) {
/* 137 */       String mapping = mappings.iterator().next();
/* 138 */       if (mapping.equals("/")) {
/* 139 */         return "";
/*     */       }
/* 141 */       if (mapping.endsWith("/*")) {
/* 142 */         String path = mapping.substring(0, mapping.length() - 2);
/* 143 */         if (!path.isEmpty() && logger.isDebugEnabled()) {
/* 144 */           logger.debug("Found servlet mapping prefix '" + path + "' for '" + name + "'");
/*     */         }
/* 146 */         return path;
/*     */       } 
/*     */     } 
/*     */     
/* 150 */     throw new IllegalArgumentException("Expected a single Servlet mapping: either the default Servlet mapping (i.e. '/'), or a path based mapping (e.g. '/*', '/foo/*'). Actual mappings: " + mappings + " for Servlet '" + name + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
/*     */     ServletServerHttpRequest httpRequest;
/* 160 */     if (DispatcherType.ASYNC.equals(request.getDispatcherType())) {
/* 161 */       Throwable ex = (Throwable)request.getAttribute(WRITE_ERROR_ATTRIBUTE_NAME);
/* 162 */       throw new ServletException("Failed to create response content", ex);
/*     */     } 
/*     */ 
/*     */     
/* 166 */     AsyncContext asyncContext = request.startAsync();
/* 167 */     asyncContext.setTimeout(-1L);
/*     */ 
/*     */     
/*     */     try {
/* 171 */       httpRequest = createRequest((HttpServletRequest)request, asyncContext);
/*     */     }
/* 173 */     catch (URISyntaxException ex) {
/* 174 */       if (logger.isDebugEnabled()) {
/* 175 */         logger.debug("Failed to get request  URL: " + ex.getMessage());
/*     */       }
/* 177 */       ((HttpServletResponse)response).setStatus(400);
/* 178 */       asyncContext.complete();
/*     */       
/*     */       return;
/*     */     } 
/* 182 */     ServerHttpResponse httpResponse = createResponse((HttpServletResponse)response, asyncContext, httpRequest);
/* 183 */     if (httpRequest.getMethod() == HttpMethod.HEAD) {
/* 184 */       httpResponse = new HttpHeadResponseDecorator(httpResponse);
/*     */     }
/*     */     
/* 187 */     AtomicBoolean isCompleted = new AtomicBoolean();
/* 188 */     HandlerResultAsyncListener listener = new HandlerResultAsyncListener(isCompleted, httpRequest);
/* 189 */     asyncContext.addListener(listener);
/*     */     
/* 191 */     HandlerResultSubscriber subscriber = new HandlerResultSubscriber(asyncContext, isCompleted, httpRequest);
/* 192 */     this.httpHandler.handle(httpRequest, httpResponse).subscribe(subscriber);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletServerHttpRequest createRequest(HttpServletRequest request, AsyncContext context) throws IOException, URISyntaxException {
/* 198 */     Assert.notNull(this.servletPath, "Servlet path is not initialized");
/* 199 */     return new ServletServerHttpRequest(request, context, this.servletPath, 
/* 200 */         getDataBufferFactory(), getBufferSize());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletServerHttpResponse createResponse(HttpServletResponse response, AsyncContext context, ServletServerHttpRequest request) throws IOException {
/* 206 */     return new ServletServerHttpResponse(response, context, getDataBufferFactory(), getBufferSize(), request);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getServletInfo() {
/* 211 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ServletConfig getServletConfig() {
/* 217 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void runIfAsyncNotComplete(AsyncContext asyncContext, AtomicBoolean isCompleted, Runnable task) {
/*     */     try {
/* 231 */       if (asyncContext.getRequest().isAsyncStarted() && isCompleted.compareAndSet(false, true)) {
/* 232 */         task.run();
/*     */       }
/*     */     }
/* 235 */     catch (IllegalStateException illegalStateException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class HandlerResultAsyncListener
/*     */     implements AsyncListener
/*     */   {
/*     */     private final AtomicBoolean isCompleted;
/*     */     
/*     */     private final String logPrefix;
/*     */ 
/*     */     
/*     */     public HandlerResultAsyncListener(AtomicBoolean isCompleted, ServletServerHttpRequest httpRequest) {
/* 249 */       this.isCompleted = isCompleted;
/* 250 */       this.logPrefix = httpRequest.getLogPrefix();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeout(AsyncEvent event) {
/* 255 */       ServletHttpHandlerAdapter.logger.debug(this.logPrefix + "Timeout notification");
/* 256 */       AsyncContext context = event.getAsyncContext();
/* 257 */       ServletHttpHandlerAdapter.runIfAsyncNotComplete(context, this.isCompleted, context::complete);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(AsyncEvent event) {
/* 262 */       Throwable ex = event.getThrowable();
/* 263 */       ServletHttpHandlerAdapter.logger.debug(this.logPrefix + "Error notification: " + ((ex != null) ? ex : "<no Throwable>"));
/* 264 */       AsyncContext context = event.getAsyncContext();
/* 265 */       ServletHttpHandlerAdapter.runIfAsyncNotComplete(context, this.isCompleted, context::complete);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onStartAsync(AsyncEvent event) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete(AsyncEvent event) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class HandlerResultSubscriber
/*     */     implements Subscriber<Void>
/*     */   {
/*     */     private final AsyncContext asyncContext;
/*     */ 
/*     */     
/*     */     private final AtomicBoolean isCompleted;
/*     */     
/*     */     private final String logPrefix;
/*     */ 
/*     */     
/*     */     public HandlerResultSubscriber(AsyncContext asyncContext, AtomicBoolean isCompleted, ServletServerHttpRequest httpRequest) {
/* 291 */       this.asyncContext = asyncContext;
/* 292 */       this.isCompleted = isCompleted;
/* 293 */       this.logPrefix = httpRequest.getLogPrefix();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription subscription) {
/* 298 */       subscription.request(Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(Void aVoid) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable ex) {
/* 308 */       ServletHttpHandlerAdapter.logger.trace(this.logPrefix + "Failed to complete: " + ex.getMessage());
/* 309 */       ServletHttpHandlerAdapter.runIfAsyncNotComplete(this.asyncContext, this.isCompleted, () -> {
/*     */             if (this.asyncContext.getResponse().isCommitted()) {
/*     */               ServletHttpHandlerAdapter.logger.trace(this.logPrefix + "Dispatch to container, to raise the error on servlet thread");
/*     */               this.asyncContext.getRequest().setAttribute(ServletHttpHandlerAdapter.WRITE_ERROR_ATTRIBUTE_NAME, ex);
/*     */               this.asyncContext.dispatch();
/*     */             } else {
/*     */               try {
/*     */                 ServletHttpHandlerAdapter.logger.trace(this.logPrefix + "Setting ServletResponse status to 500 Server Error");
/*     */                 this.asyncContext.getResponse().resetBuffer();
/*     */                 ((HttpServletResponse)this.asyncContext.getResponse()).setStatus(500);
/*     */               } finally {
/*     */                 this.asyncContext.complete();
/*     */               } 
/*     */             } 
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 330 */       ServletHttpHandlerAdapter.logger.trace(this.logPrefix + "Handling completed");
/* 331 */       ServletHttpHandlerAdapter.runIfAsyncNotComplete(this.asyncContext, this.isCompleted, this.asyncContext::complete);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ServletHttpHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */