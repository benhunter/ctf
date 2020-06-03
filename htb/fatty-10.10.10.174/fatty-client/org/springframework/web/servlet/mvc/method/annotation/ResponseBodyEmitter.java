/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class ResponseBodyEmitter
/*     */ {
/*     */   @Nullable
/*     */   private final Long timeout;
/*     */   @Nullable
/*     */   private Handler handler;
/*  73 */   private final Set<DataWithMediaType> earlySendAttempts = new LinkedHashSet<>(8);
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean complete;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Throwable failure;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sendFailed;
/*     */ 
/*     */   
/*  89 */   private final DefaultCallback timeoutCallback = new DefaultCallback();
/*     */   
/*  91 */   private final ErrorCallback errorCallback = new ErrorCallback();
/*     */   
/*  93 */   private final DefaultCallback completionCallback = new DefaultCallback();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseBodyEmitter() {
/* 100 */     this.timeout = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseBodyEmitter(Long timeout) {
/* 111 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Long getTimeout() {
/* 120 */     return this.timeout;
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void initialize(Handler handler) throws IOException {
/* 125 */     this.handler = handler;
/*     */     
/* 127 */     for (DataWithMediaType sendAttempt : this.earlySendAttempts) {
/* 128 */       sendInternal(sendAttempt.getData(), sendAttempt.getMediaType());
/*     */     }
/* 130 */     this.earlySendAttempts.clear();
/*     */     
/* 132 */     if (this.complete) {
/* 133 */       if (this.failure != null) {
/* 134 */         this.handler.completeWithError(this.failure);
/*     */       } else {
/*     */         
/* 137 */         this.handler.complete();
/*     */       } 
/*     */     } else {
/*     */       
/* 141 */       this.handler.onTimeout(this.timeoutCallback);
/* 142 */       this.handler.onError(this.errorCallback);
/* 143 */       this.handler.onCompletion(this.completionCallback);
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
/*     */   protected void extendResponse(ServerHttpResponse outputMessage) {}
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
/*     */   public void send(Object object) throws IOException {
/* 170 */     send(object, null);
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
/*     */   public synchronized void send(Object object, @Nullable MediaType mediaType) throws IOException {
/* 182 */     Assert.state(!this.complete, "ResponseBodyEmitter is already set complete");
/* 183 */     sendInternal(object, mediaType);
/*     */   }
/*     */   
/*     */   private void sendInternal(Object object, @Nullable MediaType mediaType) throws IOException {
/* 187 */     if (this.handler != null) {
/*     */       try {
/* 189 */         this.handler.send(object, mediaType);
/*     */       }
/* 191 */       catch (IOException ex) {
/* 192 */         this.sendFailed = true;
/* 193 */         throw ex;
/*     */       }
/* 195 */       catch (Throwable ex) {
/* 196 */         this.sendFailed = true;
/* 197 */         throw new IllegalStateException("Failed to send " + object, ex);
/*     */       } 
/*     */     } else {
/*     */       
/* 201 */       this.earlySendAttempts.add(new DataWithMediaType(object, mediaType));
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
/*     */   public synchronized void complete() {
/* 215 */     if (this.sendFailed) {
/*     */       return;
/*     */     }
/* 218 */     this.complete = true;
/* 219 */     if (this.handler != null) {
/* 220 */       this.handler.complete();
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
/*     */   public synchronized void completeWithError(Throwable ex) {
/* 237 */     if (this.sendFailed) {
/*     */       return;
/*     */     }
/* 240 */     this.complete = true;
/* 241 */     this.failure = ex;
/* 242 */     if (this.handler != null) {
/* 243 */       this.handler.completeWithError(ex);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void onTimeout(Runnable callback) {
/* 252 */     this.timeoutCallback.setDelegate(callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void onError(Consumer<Throwable> callback) {
/* 262 */     this.errorCallback.setDelegate(callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void onCompletion(Runnable callback) {
/* 272 */     this.completionCallback.setDelegate(callback);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 278 */     return "ResponseBodyEmitter@" + ObjectUtils.getIdentityHexString(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static interface Handler
/*     */   {
/*     */     void send(Object param1Object, @Nullable MediaType param1MediaType) throws IOException;
/*     */ 
/*     */     
/*     */     void complete();
/*     */ 
/*     */     
/*     */     void completeWithError(Throwable param1Throwable);
/*     */ 
/*     */     
/*     */     void onTimeout(Runnable param1Runnable);
/*     */ 
/*     */     
/*     */     void onError(Consumer<Throwable> param1Consumer);
/*     */ 
/*     */     
/*     */     void onCompletion(Runnable param1Runnable);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class DataWithMediaType
/*     */   {
/*     */     private final Object data;
/*     */     
/*     */     @Nullable
/*     */     private final MediaType mediaType;
/*     */ 
/*     */     
/*     */     public DataWithMediaType(Object data, @Nullable MediaType mediaType) {
/* 313 */       this.data = data;
/* 314 */       this.mediaType = mediaType;
/*     */     }
/*     */     
/*     */     public Object getData() {
/* 318 */       return this.data;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public MediaType getMediaType() {
/* 323 */       return this.mediaType;
/*     */     }
/*     */   }
/*     */   
/*     */   private class DefaultCallback implements Runnable {
/*     */     @Nullable
/*     */     private Runnable delegate;
/*     */     
/*     */     private DefaultCallback() {}
/*     */     
/*     */     public void setDelegate(Runnable delegate) {
/* 334 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 339 */       ResponseBodyEmitter.this.complete = true;
/* 340 */       if (this.delegate != null)
/* 341 */         this.delegate.run(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class ErrorCallback
/*     */     implements Consumer<Throwable> {
/*     */     @Nullable
/*     */     private Consumer<Throwable> delegate;
/*     */     
/*     */     private ErrorCallback() {}
/*     */     
/*     */     public void setDelegate(Consumer<Throwable> callback) {
/* 353 */       this.delegate = callback;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Throwable t) {
/* 358 */       ResponseBodyEmitter.this.complete = true;
/* 359 */       if (this.delegate != null)
/* 360 */         this.delegate.accept(t); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */