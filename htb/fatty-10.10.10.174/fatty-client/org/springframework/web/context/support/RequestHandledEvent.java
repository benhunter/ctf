/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import org.springframework.context.ApplicationEvent;
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
/*     */ public class RequestHandledEvent
/*     */   extends ApplicationEvent
/*     */ {
/*     */   @Nullable
/*     */   private String sessionId;
/*     */   @Nullable
/*     */   private String userName;
/*     */   private final long processingTimeMillis;
/*     */   @Nullable
/*     */   private Throwable failureCause;
/*     */   
/*     */   public RequestHandledEvent(Object source, @Nullable String sessionId, @Nullable String userName, long processingTimeMillis) {
/*  67 */     super(source);
/*  68 */     this.sessionId = sessionId;
/*  69 */     this.userName = userName;
/*  70 */     this.processingTimeMillis = processingTimeMillis;
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
/*     */   public RequestHandledEvent(Object source, @Nullable String sessionId, @Nullable String userName, long processingTimeMillis, @Nullable Throwable failureCause) {
/*  85 */     this(source, sessionId, userName, processingTimeMillis);
/*  86 */     this.failureCause = failureCause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getProcessingTimeMillis() {
/*  94 */     return this.processingTimeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSessionId() {
/* 102 */     return this.sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getUserName() {
/* 112 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean wasFailure() {
/* 119 */     return (this.failureCause != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getFailureCause() {
/* 127 */     return this.failureCause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getShortDescription() {
/* 136 */     StringBuilder sb = new StringBuilder();
/* 137 */     sb.append("session=[").append(this.sessionId).append("]; ");
/* 138 */     sb.append("user=[").append(this.userName).append("]; ");
/* 139 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 147 */     StringBuilder sb = new StringBuilder();
/* 148 */     sb.append("session=[").append(this.sessionId).append("]; ");
/* 149 */     sb.append("user=[").append(this.userName).append("]; ");
/* 150 */     sb.append("time=[").append(this.processingTimeMillis).append("ms]; ");
/* 151 */     sb.append("status=[");
/* 152 */     if (!wasFailure()) {
/* 153 */       sb.append("OK");
/*     */     } else {
/*     */       
/* 156 */       sb.append("failed: ").append(this.failureCause);
/*     */     } 
/* 158 */     sb.append(']');
/* 159 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 164 */     return "RequestHandledEvent: " + getDescription();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/RequestHandledEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */