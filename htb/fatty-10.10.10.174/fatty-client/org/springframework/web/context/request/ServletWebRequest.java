/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletWebRequest
/*     */   extends ServletRequestAttributes
/*     */   implements NativeWebRequest
/*     */ {
/*     */   private static final String ETAG = "ETag";
/*     */   private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
/*     */   private static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
/*     */   private static final String IF_NONE_MATCH = "If-None-Match";
/*     */   private static final String LAST_MODIFIED = "Last-Modified";
/*  63 */   private static final List<String> SAFE_METHODS = Arrays.asList(new String[] { "GET", "HEAD" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private static final String[] DATE_FORMATS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean notModified = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletWebRequest(HttpServletRequest request) {
/*  91 */     super(request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletWebRequest(HttpServletRequest request, @Nullable HttpServletResponse response) {
/* 100 */     super(request, response);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getNativeRequest() {
/* 106 */     return getRequest();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getNativeResponse() {
/* 111 */     return getResponse();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getNativeRequest(@Nullable Class<T> requiredType) {
/* 116 */     return (T)WebUtils.getNativeRequest((ServletRequest)getRequest(), requiredType);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getNativeResponse(@Nullable Class<T> requiredType) {
/* 121 */     HttpServletResponse response = getResponse();
/* 122 */     return (response != null) ? (T)WebUtils.getNativeResponse((ServletResponse)response, requiredType) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HttpMethod getHttpMethod() {
/* 131 */     return HttpMethod.resolve(getRequest().getMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getHeader(String headerName) {
/* 137 */     return getRequest().getHeader(headerName);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getHeaderValues(String headerName) {
/* 143 */     String[] headerValues = StringUtils.toStringArray(getRequest().getHeaders(headerName));
/* 144 */     return !ObjectUtils.isEmpty((Object[])headerValues) ? headerValues : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getHeaderNames() {
/* 149 */     return CollectionUtils.toIterator(getRequest().getHeaderNames());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getParameter(String paramName) {
/* 155 */     return getRequest().getParameter(paramName);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getParameterValues(String paramName) {
/* 161 */     return getRequest().getParameterValues(paramName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getParameterNames() {
/* 166 */     return CollectionUtils.toIterator(getRequest().getParameterNames());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 171 */     return getRequest().getParameterMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 176 */     return getRequest().getLocale();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContextPath() {
/* 181 */     return getRequest().getContextPath();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getRemoteUser() {
/* 187 */     return getRequest().getRemoteUser();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Principal getUserPrincipal() {
/* 193 */     return getRequest().getUserPrincipal();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUserInRole(String role) {
/* 198 */     return getRequest().isUserInRole(role);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 203 */     return getRequest().isSecure();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(long lastModifiedTimestamp) {
/* 209 */     return checkNotModified((String)null, lastModifiedTimestamp);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(String etag) {
/* 214 */     return checkNotModified(etag, -1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(@Nullable String etag, long lastModifiedTimestamp) {
/* 219 */     HttpServletResponse response = getResponse();
/* 220 */     if (this.notModified || (response != null && HttpStatus.OK.value() != response.getStatus())) {
/* 221 */       return this.notModified;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 227 */     if (validateIfUnmodifiedSince(lastModifiedTimestamp)) {
/* 228 */       if (this.notModified && response != null) {
/* 229 */         response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
/*     */       }
/* 231 */       return this.notModified;
/*     */     } 
/*     */     
/* 234 */     boolean validated = validateIfNoneMatch(etag);
/* 235 */     if (!validated) {
/* 236 */       validateIfModifiedSince(lastModifiedTimestamp);
/*     */     }
/*     */ 
/*     */     
/* 240 */     if (response != null) {
/* 241 */       boolean isHttpGetOrHead = SAFE_METHODS.contains(getRequest().getMethod());
/* 242 */       if (this.notModified) {
/* 243 */         response.setStatus(isHttpGetOrHead ? HttpStatus.NOT_MODIFIED
/* 244 */             .value() : HttpStatus.PRECONDITION_FAILED.value());
/*     */       }
/* 246 */       if (isHttpGetOrHead) {
/* 247 */         if (lastModifiedTimestamp > 0L && parseDateValue(response.getHeader("Last-Modified")) == -1L) {
/* 248 */           response.setDateHeader("Last-Modified", lastModifiedTimestamp);
/*     */         }
/* 250 */         if (StringUtils.hasLength(etag) && response.getHeader("ETag") == null) {
/* 251 */           response.setHeader("ETag", padEtagIfNecessary(etag));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 256 */     return this.notModified;
/*     */   }
/*     */   
/*     */   private boolean validateIfUnmodifiedSince(long lastModifiedTimestamp) {
/* 260 */     if (lastModifiedTimestamp < 0L) {
/* 261 */       return false;
/*     */     }
/* 263 */     long ifUnmodifiedSince = parseDateHeader("If-Unmodified-Since");
/* 264 */     if (ifUnmodifiedSince == -1L) {
/* 265 */       return false;
/*     */     }
/*     */     
/* 268 */     this.notModified = (ifUnmodifiedSince < lastModifiedTimestamp / 1000L * 1000L);
/* 269 */     return true;
/*     */   }
/*     */   private boolean validateIfNoneMatch(@Nullable String etag) {
/*     */     Enumeration<String> ifNoneMatch;
/* 273 */     if (!StringUtils.hasLength(etag)) {
/* 274 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 279 */       ifNoneMatch = getRequest().getHeaders("If-None-Match");
/*     */     }
/* 281 */     catch (IllegalArgumentException ex) {
/* 282 */       return false;
/*     */     } 
/* 284 */     if (!ifNoneMatch.hasMoreElements()) {
/* 285 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 289 */     etag = padEtagIfNecessary(etag);
/* 290 */     if (etag.startsWith("W/")) {
/* 291 */       etag = etag.substring(2);
/*     */     }
/* 293 */     while (ifNoneMatch.hasMoreElements()) {
/* 294 */       String clientETags = ifNoneMatch.nextElement();
/* 295 */       Matcher etagMatcher = ETAG_HEADER_VALUE_PATTERN.matcher(clientETags);
/*     */       
/* 297 */       while (etagMatcher.find()) {
/* 298 */         if (StringUtils.hasLength(etagMatcher.group()) && etag.equals(etagMatcher.group(3))) {
/* 299 */           this.notModified = true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 305 */     return true;
/*     */   }
/*     */   
/*     */   private String padEtagIfNecessary(String etag) {
/* 309 */     if (!StringUtils.hasLength(etag)) {
/* 310 */       return etag;
/*     */     }
/* 312 */     if ((etag.startsWith("\"") || etag.startsWith("W/\"")) && etag.endsWith("\"")) {
/* 313 */       return etag;
/*     */     }
/* 315 */     return "\"" + etag + "\"";
/*     */   }
/*     */   
/*     */   private boolean validateIfModifiedSince(long lastModifiedTimestamp) {
/* 319 */     if (lastModifiedTimestamp < 0L) {
/* 320 */       return false;
/*     */     }
/* 322 */     long ifModifiedSince = parseDateHeader("If-Modified-Since");
/* 323 */     if (ifModifiedSince == -1L) {
/* 324 */       return false;
/*     */     }
/*     */     
/* 327 */     this.notModified = (ifModifiedSince >= lastModifiedTimestamp / 1000L * 1000L);
/* 328 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isNotModified() {
/* 332 */     return this.notModified;
/*     */   }
/*     */   
/*     */   private long parseDateHeader(String headerName) {
/* 336 */     long dateValue = -1L;
/*     */     try {
/* 338 */       dateValue = getRequest().getDateHeader(headerName);
/*     */     }
/* 340 */     catch (IllegalArgumentException ex) {
/* 341 */       String headerValue = getHeader(headerName);
/*     */       
/* 343 */       if (headerValue != null) {
/* 344 */         int separatorIndex = headerValue.indexOf(';');
/* 345 */         if (separatorIndex != -1) {
/* 346 */           String datePart = headerValue.substring(0, separatorIndex);
/* 347 */           dateValue = parseDateValue(datePart);
/*     */         } 
/*     */       } 
/*     */     } 
/* 351 */     return dateValue;
/*     */   }
/*     */   
/*     */   private long parseDateValue(@Nullable String headerValue) {
/* 355 */     if (headerValue == null)
/*     */     {
/* 357 */       return -1L;
/*     */     }
/* 359 */     if (headerValue.length() >= 3)
/*     */     {
/*     */       
/* 362 */       for (String dateFormat : DATE_FORMATS) {
/* 363 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
/* 364 */         simpleDateFormat.setTimeZone(GMT);
/*     */         try {
/* 366 */           return simpleDateFormat.parse(headerValue).getTime();
/*     */         }
/* 368 */         catch (ParseException parseException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 373 */     return -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription(boolean includeClientInfo) {
/* 378 */     HttpServletRequest request = getRequest();
/* 379 */     StringBuilder sb = new StringBuilder();
/* 380 */     sb.append("uri=").append(request.getRequestURI());
/* 381 */     if (includeClientInfo) {
/* 382 */       String client = request.getRemoteAddr();
/* 383 */       if (StringUtils.hasLength(client)) {
/* 384 */         sb.append(";client=").append(client);
/*     */       }
/* 386 */       HttpSession session = request.getSession(false);
/* 387 */       if (session != null) {
/* 388 */         sb.append(";session=").append(session.getId());
/*     */       }
/* 390 */       String user = request.getRemoteUser();
/* 391 */       if (StringUtils.hasLength(user)) {
/* 392 */         sb.append(";user=").append(user);
/*     */       }
/*     */     } 
/* 395 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 401 */     return "ServletWebRequest: " + getDescription(true);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/ServletWebRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */