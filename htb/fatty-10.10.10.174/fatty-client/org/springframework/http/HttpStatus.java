/*     */ package org.springframework.http;
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
/*     */ public enum HttpStatus
/*     */ {
/*  42 */   CONTINUE(100, "Continue"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   SWITCHING_PROTOCOLS(101, "Switching Protocols"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   PROCESSING(102, "Processing"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   CHECKPOINT(103, "Checkpoint"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   OK(200, "OK"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   CREATED(201, "Created"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   ACCEPTED(202, "Accepted"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   NO_CONTENT(204, "No Content"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   RESET_CONTENT(205, "Reset Content"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   PARTIAL_CONTENT(206, "Partial Content"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   MULTI_STATUS(207, "Multi-Status"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   ALREADY_REPORTED(208, "Already Reported"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   IM_USED(226, "IM Used"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   MULTIPLE_CHOICES(300, "Multiple Choices"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   MOVED_PERMANENTLY(301, "Moved Permanently"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   FOUND(302, "Found"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   MOVED_TEMPORARILY(302, "Moved Temporarily"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   SEE_OTHER(303, "See Other"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   NOT_MODIFIED(304, "Not Modified"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   USE_PROXY(305, "Use Proxy"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   TEMPORARY_REDIRECT(307, "Temporary Redirect"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   PERMANENT_REDIRECT(308, "Permanent Redirect"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   BAD_REQUEST(400, "Bad Request"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   UNAUTHORIZED(401, "Unauthorized"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 181 */   PAYMENT_REQUIRED(402, "Payment Required"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   FORBIDDEN(403, "Forbidden"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 191 */   NOT_FOUND(404, "Not Found"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 196 */   METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 201 */   NOT_ACCEPTABLE(406, "Not Acceptable"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   REQUEST_TIMEOUT(408, "Request Timeout"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   CONFLICT(409, "Conflict"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 222 */   GONE(410, "Gone"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   LENGTH_REQUIRED(411, "Length Required"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   PRECONDITION_FAILED(412, "Precondition Failed"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 241 */   PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 248 */   REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 256 */   URI_TOO_LONG(414, "URI Too Long"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 262 */   REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 269 */   UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 274 */   REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 280 */   EXPECTATION_FAILED(417, "Expectation Failed"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 285 */   I_AM_A_TEAPOT(418, "I'm a teapot"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 291 */   INSUFFICIENT_SPACE_ON_RESOURCE(419, "Insufficient Space On Resource"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 298 */   METHOD_FAILURE(420, "Method Failure"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 305 */   DESTINATION_LOCKED(421, "Destination Locked"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 311 */   UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 316 */   LOCKED(423, "Locked"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 321 */   FAILED_DEPENDENCY(424, "Failed Dependency"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 326 */   UPGRADE_REQUIRED(426, "Upgrade Required"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 331 */   PRECONDITION_REQUIRED(428, "Precondition Required"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 336 */   TOO_MANY_REQUESTS(429, "Too Many Requests"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 341 */   REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 348 */   UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 356 */   INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 361 */   NOT_IMPLEMENTED(501, "Not Implemented"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 366 */   BAD_GATEWAY(502, "Bad Gateway"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 371 */   SERVICE_UNAVAILABLE(503, "Service Unavailable"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 376 */   GATEWAY_TIMEOUT(504, "Gateway Timeout"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 381 */   HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 386 */   VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 391 */   INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 396 */   LOOP_DETECTED(508, "Loop Detected"),
/*     */ 
/*     */ 
/*     */   
/* 400 */   BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 405 */   NOT_EXTENDED(510, "Not Extended"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 410 */   NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");
/*     */ 
/*     */   
/*     */   private final int value;
/*     */   
/*     */   private final String reasonPhrase;
/*     */ 
/*     */   
/*     */   HttpStatus(int value, String reasonPhrase) {
/* 419 */     this.value = value;
/* 420 */     this.reasonPhrase = reasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int value() {
/* 428 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReasonPhrase() {
/* 435 */     return this.reasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Series series() {
/* 443 */     return Series.valueOf(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is1xxInformational() {
/* 454 */     return (series() == Series.INFORMATIONAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is2xxSuccessful() {
/* 465 */     return (series() == Series.SUCCESSFUL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is3xxRedirection() {
/* 476 */     return (series() == Series.REDIRECTION);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is4xxClientError() {
/* 487 */     return (series() == Series.CLIENT_ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is5xxServerError() {
/* 498 */     return (series() == Series.SERVER_ERROR);
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
/*     */   public boolean isError() {
/* 511 */     return (is4xxClientError() || is5xxServerError());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 519 */     return this.value + " " + name();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static HttpStatus resolve(int statusCode) {
/* 545 */     for (HttpStatus status : values()) {
/* 546 */       if (status.value == statusCode) {
/* 547 */         return status;
/*     */       }
/*     */     } 
/* 550 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Series
/*     */   {
/* 560 */     INFORMATIONAL(1),
/* 561 */     SUCCESSFUL(2),
/* 562 */     REDIRECTION(3),
/* 563 */     CLIENT_ERROR(4),
/* 564 */     SERVER_ERROR(5);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     Series(int value) {
/* 569 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int value() {
/* 576 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */     public static Series resolve(int statusCode) {
/* 611 */       int seriesCode = statusCode / 100;
/* 612 */       for (Series series : values()) {
/* 613 */         if (series.value == seriesCode) {
/* 614 */           return series;
/*     */         }
/*     */       } 
/* 617 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/HttpStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */