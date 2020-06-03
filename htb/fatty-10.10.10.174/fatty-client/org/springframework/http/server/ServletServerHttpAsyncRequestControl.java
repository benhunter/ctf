/*     */ package org.springframework.http.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.AsyncEvent;
/*     */ import javax.servlet.AsyncListener;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ public class ServletServerHttpAsyncRequestControl
/*     */   implements ServerHttpAsyncRequestControl, AsyncListener
/*     */ {
/*     */   private static final long NO_TIMEOUT_VALUE = -9223372036854775808L;
/*     */   private final ServletServerHttpRequest request;
/*     */   private final ServletServerHttpResponse response;
/*     */   @Nullable
/*     */   private AsyncContext asyncContext;
/*  48 */   private AtomicBoolean asyncCompleted = new AtomicBoolean(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletServerHttpAsyncRequestControl(ServletServerHttpRequest request, ServletServerHttpResponse response) {
/*  57 */     Assert.notNull(request, "request is required");
/*  58 */     Assert.notNull(response, "response is required");
/*     */     
/*  60 */     Assert.isTrue(request.getServletRequest().isAsyncSupported(), "Async support must be enabled on a servlet and for all filters involved in async request processing. This is done in Java code using the Servlet API or by adding \"<async-supported>true</async-supported>\" to servlet and filter declarations in web.xml. Also you must use a Servlet 3.0+ container");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     this.request = request;
/*  67 */     this.response = response;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/*  73 */     return (this.asyncContext != null && this.request.getServletRequest().isAsyncStarted());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompleted() {
/*  78 */     return this.asyncCompleted.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  83 */     start(Long.MIN_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start(long timeout) {
/*  88 */     Assert.state(!isCompleted(), "Async processing has already completed");
/*  89 */     if (isStarted()) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     HttpServletRequest servletRequest = this.request.getServletRequest();
/*  94 */     HttpServletResponse servletResponse = this.response.getServletResponse();
/*     */     
/*  96 */     this.asyncContext = servletRequest.startAsync((ServletRequest)servletRequest, (ServletResponse)servletResponse);
/*  97 */     this.asyncContext.addListener(this);
/*     */     
/*  99 */     if (timeout != Long.MIN_VALUE) {
/* 100 */       this.asyncContext.setTimeout(timeout);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void complete() {
/* 106 */     if (this.asyncContext != null && isStarted() && !isCompleted()) {
/* 107 */       this.asyncContext.complete();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete(AsyncEvent event) throws IOException {
/* 118 */     this.asyncContext = null;
/* 119 */     this.asyncCompleted.set(true);
/*     */   }
/*     */   
/*     */   public void onStartAsync(AsyncEvent event) throws IOException {}
/*     */   
/*     */   public void onError(AsyncEvent event) throws IOException {}
/*     */   
/*     */   public void onTimeout(AsyncEvent event) throws IOException {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/ServletServerHttpAsyncRequestControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */