/*     */ package org.springframework.http.codec;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public final class ServerSentEvent<T>
/*     */ {
/*     */   @Nullable
/*     */   private final String id;
/*     */   @Nullable
/*     */   private final String event;
/*     */   @Nullable
/*     */   private final Duration retry;
/*     */   @Nullable
/*     */   private final String comment;
/*     */   @Nullable
/*     */   private final T data;
/*     */   
/*     */   private ServerSentEvent(@Nullable String id, @Nullable String event, @Nullable Duration retry, @Nullable String comment, @Nullable T data) {
/*  56 */     this.id = id;
/*  57 */     this.event = event;
/*  58 */     this.retry = retry;
/*  59 */     this.comment = comment;
/*  60 */     this.data = data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String id() {
/*  69 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String event() {
/*  77 */     return this.event;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Duration retry() {
/*  85 */     return this.retry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String comment() {
/*  93 */     return this.comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T data() {
/* 101 */     return this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return "ServerSentEvent [id = '" + this.id + "', event='" + this.event + "', retry=" + this.retry + ", comment='" + this.comment + "', data=" + this.data + ']';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Builder<T> builder() {
/* 118 */     return new BuilderImpl<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Builder<T> builder(T data) {
/* 127 */     return new BuilderImpl<>(data);
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
/*     */   private static class BuilderImpl<T>
/*     */     implements Builder<T>
/*     */   {
/*     */     @Nullable
/*     */     private String id;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private String event;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Duration retry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private String comment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private T data;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BuilderImpl() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BuilderImpl(T data) {
/* 206 */       this.data = data;
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerSentEvent.Builder<T> id(String id) {
/* 211 */       this.id = id;
/* 212 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerSentEvent.Builder<T> event(String event) {
/* 217 */       this.event = event;
/* 218 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerSentEvent.Builder<T> retry(Duration retry) {
/* 223 */       this.retry = retry;
/* 224 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerSentEvent.Builder<T> comment(String comment) {
/* 229 */       this.comment = comment;
/* 230 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerSentEvent.Builder<T> data(@Nullable T data) {
/* 235 */       this.data = data;
/* 236 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerSentEvent<T> build() {
/* 241 */       return new ServerSentEvent<>(this.id, this.event, this.retry, this.comment, this.data);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Builder<T> {
/*     */     Builder<T> id(String param1String);
/*     */     
/*     */     Builder<T> event(String param1String);
/*     */     
/*     */     Builder<T> retry(Duration param1Duration);
/*     */     
/*     */     Builder<T> comment(String param1String);
/*     */     
/*     */     Builder<T> data(@Nullable T param1T);
/*     */     
/*     */     ServerSentEvent<T> build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/ServerSentEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */