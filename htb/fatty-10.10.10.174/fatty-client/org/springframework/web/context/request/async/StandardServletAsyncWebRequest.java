/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.Consumer;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.AsyncEvent;
/*     */ import javax.servlet.AsyncListener;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
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
/*     */ public class StandardServletAsyncWebRequest
/*     */   extends ServletWebRequest
/*     */   implements AsyncWebRequest, AsyncListener
/*     */ {
/*     */   private Long timeout;
/*     */   private AsyncContext asyncContext;
/*  50 */   private AtomicBoolean asyncCompleted = new AtomicBoolean(false);
/*     */   
/*  52 */   private final List<Runnable> timeoutHandlers = new ArrayList<>();
/*     */   
/*  54 */   private final List<Consumer<Throwable>> exceptionHandlers = new ArrayList<>();
/*     */   
/*  56 */   private final List<Runnable> completionHandlers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardServletAsyncWebRequest(HttpServletRequest request, HttpServletResponse response) {
/*  65 */     super(request, response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeout(Long timeout) {
/*  75 */     Assert.state(!isAsyncStarted(), "Cannot change the timeout with concurrent handling in progress");
/*  76 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTimeoutHandler(Runnable timeoutHandler) {
/*  81 */     this.timeoutHandlers.add(timeoutHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addErrorHandler(Consumer<Throwable> exceptionHandler) {
/*  86 */     this.exceptionHandlers.add(exceptionHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCompletionHandler(Runnable runnable) {
/*  91 */     this.completionHandlers.add(runnable);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAsyncStarted() {
/*  96 */     return (this.asyncContext != null && getRequest().isAsyncStarted());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAsyncComplete() {
/* 106 */     return this.asyncCompleted.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void startAsync() {
/* 111 */     Assert.state(getRequest().isAsyncSupported(), "Async support must be enabled on a servlet and for all filters involved in async request processing. This is done in Java code using the Servlet API or by adding \"<async-supported>true</async-supported>\" to servlet and filter declarations in web.xml.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     Assert.state(!isAsyncComplete(), "Async processing has already completed");
/*     */     
/* 118 */     if (isAsyncStarted()) {
/*     */       return;
/*     */     }
/* 121 */     this.asyncContext = getRequest().startAsync((ServletRequest)getRequest(), (ServletResponse)getResponse());
/* 122 */     this.asyncContext.addListener(this);
/* 123 */     if (this.timeout != null) {
/* 124 */       this.asyncContext.setTimeout(this.timeout.longValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispatch() {
/* 130 */     Assert.notNull(this.asyncContext, "Cannot dispatch without an AsyncContext");
/* 131 */     this.asyncContext.dispatch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onStartAsync(AsyncEvent event) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(AsyncEvent event) throws IOException {
/* 145 */     this.exceptionHandlers.forEach(consumer -> consumer.accept(event.getThrowable()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTimeout(AsyncEvent event) throws IOException {
/* 150 */     this.timeoutHandlers.forEach(Runnable::run);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete(AsyncEvent event) throws IOException {
/* 155 */     this.completionHandlers.forEach(Runnable::run);
/* 156 */     this.asyncContext = null;
/* 157 */     this.asyncCompleted.set(true);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/async/StandardServletAsyncWebRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */