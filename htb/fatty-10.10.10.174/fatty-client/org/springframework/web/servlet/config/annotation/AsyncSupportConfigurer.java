/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.context.request.async.CallableProcessingInterceptor;
/*     */ import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
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
/*     */ public class AsyncSupportConfigurer
/*     */ {
/*     */   @Nullable
/*     */   private AsyncTaskExecutor taskExecutor;
/*     */   @Nullable
/*     */   private Long timeout;
/*  46 */   private final List<CallableProcessingInterceptor> callableInterceptors = new ArrayList<>();
/*     */   
/*  48 */   private final List<DeferredResultProcessingInterceptor> deferredResultInterceptors = new ArrayList<>();
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
/*     */   public AsyncSupportConfigurer setTaskExecutor(AsyncTaskExecutor taskExecutor) {
/*  64 */     this.taskExecutor = taskExecutor;
/*  65 */     return this;
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
/*     */   public AsyncSupportConfigurer setDefaultTimeout(long timeout) {
/*  78 */     this.timeout = Long.valueOf(timeout);
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncSupportConfigurer registerCallableInterceptors(CallableProcessingInterceptor... interceptors) {
/*  89 */     this.callableInterceptors.addAll(Arrays.asList(interceptors));
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncSupportConfigurer registerDeferredResultInterceptors(DeferredResultProcessingInterceptor... interceptors) {
/* 101 */     this.deferredResultInterceptors.addAll(Arrays.asList(interceptors));
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected AsyncTaskExecutor getTaskExecutor() {
/* 108 */     return this.taskExecutor;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Long getTimeout() {
/* 113 */     return this.timeout;
/*     */   }
/*     */   
/*     */   protected List<CallableProcessingInterceptor> getCallableInterceptors() {
/* 117 */     return this.callableInterceptors;
/*     */   }
/*     */   
/*     */   protected List<DeferredResultProcessingInterceptor> getDeferredResultInterceptors() {
/* 121 */     return this.deferredResultInterceptors;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/AsyncSupportConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */