/*     */ package org.springframework.web.context.support;
/*     */ 
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
/*     */ 
/*     */ public class ServletRequestHandledEvent
/*     */   extends RequestHandledEvent
/*     */ {
/*     */   private final String requestUrl;
/*     */   private final String clientAddress;
/*     */   private final String method;
/*     */   private final String servletName;
/*     */   private final int statusCode;
/*     */   
/*     */   public ServletRequestHandledEvent(Object source, String requestUrl, String clientAddress, String method, String servletName, @Nullable String sessionId, @Nullable String userName, long processingTimeMillis) {
/*  65 */     super(source, sessionId, userName, processingTimeMillis);
/*  66 */     this.requestUrl = requestUrl;
/*  67 */     this.clientAddress = clientAddress;
/*  68 */     this.method = method;
/*  69 */     this.servletName = servletName;
/*  70 */     this.statusCode = -1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletRequestHandledEvent(Object source, String requestUrl, String clientAddress, String method, String servletName, @Nullable String sessionId, @Nullable String userName, long processingTimeMillis, @Nullable Throwable failureCause) {
/*  90 */     super(source, sessionId, userName, processingTimeMillis, failureCause);
/*  91 */     this.requestUrl = requestUrl;
/*  92 */     this.clientAddress = clientAddress;
/*  93 */     this.method = method;
/*  94 */     this.servletName = servletName;
/*  95 */     this.statusCode = -1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletRequestHandledEvent(Object source, String requestUrl, String clientAddress, String method, String servletName, @Nullable String sessionId, @Nullable String userName, long processingTimeMillis, @Nullable Throwable failureCause, int statusCode) {
/* 116 */     super(source, sessionId, userName, processingTimeMillis, failureCause);
/* 117 */     this.requestUrl = requestUrl;
/* 118 */     this.clientAddress = clientAddress;
/* 119 */     this.method = method;
/* 120 */     this.servletName = servletName;
/* 121 */     this.statusCode = statusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRequestUrl() {
/* 129 */     return this.requestUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientAddress() {
/* 136 */     return this.clientAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethod() {
/* 143 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServletName() {
/* 150 */     return this.servletName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStatusCode() {
/* 159 */     return this.statusCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getShortDescription() {
/* 164 */     StringBuilder sb = new StringBuilder();
/* 165 */     sb.append("url=[").append(getRequestUrl()).append("]; ");
/* 166 */     sb.append("client=[").append(getClientAddress()).append("]; ");
/* 167 */     sb.append(super.getShortDescription());
/* 168 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 173 */     StringBuilder sb = new StringBuilder();
/* 174 */     sb.append("url=[").append(getRequestUrl()).append("]; ");
/* 175 */     sb.append("client=[").append(getClientAddress()).append("]; ");
/* 176 */     sb.append("method=[").append(getMethod()).append("]; ");
/* 177 */     sb.append("servlet=[").append(getServletName()).append("]; ");
/* 178 */     sb.append(super.getDescription());
/* 179 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 184 */     return "ServletRequestHandledEvent: " + getDescription();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/ServletRequestHandledEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */