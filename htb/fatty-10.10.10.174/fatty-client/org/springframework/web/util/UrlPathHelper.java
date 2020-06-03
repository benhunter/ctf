/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlPathHelper
/*     */ {
/*     */   private static final String WEBSPHERE_URI_ATTRIBUTE = "com.ibm.websphere.servlet.uri_non_decoded";
/*  58 */   private static final Log logger = LogFactory.getLog(UrlPathHelper.class);
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static volatile Boolean websphereComplianceFlag;
/*     */   
/*     */   private boolean alwaysUseFullPath = false;
/*     */   
/*     */   private boolean urlDecode = true;
/*     */   
/*     */   private boolean removeSemicolonContent = true;
/*     */   
/*  70 */   private String defaultEncoding = "ISO-8859-1";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
/*  83 */     this.alwaysUseFullPath = alwaysUseFullPath;
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
/*     */   public void setUrlDecode(boolean urlDecode) {
/* 105 */     this.urlDecode = urlDecode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUrlDecode() {
/* 113 */     return this.urlDecode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveSemicolonContent(boolean removeSemicolonContent) {
/* 121 */     this.removeSemicolonContent = removeSemicolonContent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldRemoveSemicolonContent() {
/* 128 */     return this.removeSemicolonContent;
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
/*     */   public void setDefaultEncoding(String defaultEncoding) {
/* 145 */     this.defaultEncoding = defaultEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDefaultEncoding() {
/* 152 */     return this.defaultEncoding;
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
/*     */   public String getLookupPathForRequest(HttpServletRequest request) {
/* 167 */     if (this.alwaysUseFullPath) {
/* 168 */       return getPathWithinApplication(request);
/*     */     }
/*     */     
/* 171 */     String rest = getPathWithinServletMapping(request);
/* 172 */     if (!"".equals(rest)) {
/* 173 */       return rest;
/*     */     }
/*     */     
/* 176 */     return getPathWithinApplication(request);
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
/*     */   public String getPathWithinServletMapping(HttpServletRequest request) {
/* 195 */     String path, pathWithinApp = getPathWithinApplication(request);
/* 196 */     String servletPath = getServletPath(request);
/* 197 */     String sanitizedPathWithinApp = getSanitizedPath(pathWithinApp);
/*     */ 
/*     */ 
/*     */     
/* 201 */     if (servletPath.contains(sanitizedPathWithinApp)) {
/* 202 */       path = getRemainingPath(sanitizedPathWithinApp, servletPath, false);
/*     */     } else {
/*     */       
/* 205 */       path = getRemainingPath(pathWithinApp, servletPath, false);
/*     */     } 
/*     */     
/* 208 */     if (path != null)
/*     */     {
/* 210 */       return path;
/*     */     }
/*     */ 
/*     */     
/* 214 */     String pathInfo = request.getPathInfo();
/* 215 */     if (pathInfo != null)
/*     */     {
/*     */       
/* 218 */       return pathInfo;
/*     */     }
/* 220 */     if (!this.urlDecode) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 225 */       path = getRemainingPath(decodeInternal(request, pathWithinApp), servletPath, false);
/* 226 */       if (path != null) {
/* 227 */         return pathWithinApp;
/*     */       }
/*     */     } 
/*     */     
/* 231 */     return servletPath;
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
/*     */   public String getPathWithinApplication(HttpServletRequest request) {
/* 243 */     String contextPath = getContextPath(request);
/* 244 */     String requestUri = getRequestUri(request);
/* 245 */     String path = getRemainingPath(requestUri, contextPath, true);
/* 246 */     if (path != null)
/*     */     {
/* 248 */       return StringUtils.hasText(path) ? path : "/";
/*     */     }
/*     */     
/* 251 */     return requestUri;
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
/*     */   private String getRemainingPath(String requestUri, String mapping, boolean ignoreCase) {
/* 263 */     int index1 = 0;
/* 264 */     int index2 = 0;
/* 265 */     while (index1 < requestUri.length() && index2 < mapping.length()) {
/* 266 */       char c1 = requestUri.charAt(index1);
/* 267 */       char c2 = mapping.charAt(index2);
/* 268 */       if (c1 == ';') {
/* 269 */         index1 = requestUri.indexOf('/', index1);
/* 270 */         if (index1 == -1) {
/* 271 */           return null;
/*     */         }
/* 273 */         c1 = requestUri.charAt(index1);
/*     */       } 
/* 275 */       if (c1 == c2 || (ignoreCase && Character.toLowerCase(c1) == Character.toLowerCase(c2))) {
/*     */         index1++; index2++; continue;
/*     */       } 
/* 278 */       return null;
/*     */     } 
/* 280 */     if (index2 != mapping.length()) {
/* 281 */       return null;
/*     */     }
/* 283 */     if (index1 == requestUri.length()) {
/* 284 */       return "";
/*     */     }
/* 286 */     if (requestUri.charAt(index1) == ';') {
/* 287 */       index1 = requestUri.indexOf('/', index1);
/*     */     }
/* 289 */     return (index1 != -1) ? requestUri.substring(index1) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getSanitizedPath(String path) {
/* 299 */     String sanitized = path;
/*     */     while (true) {
/* 301 */       int index = sanitized.indexOf("//");
/* 302 */       if (index < 0) {
/*     */         break;
/*     */       }
/*     */       
/* 306 */       sanitized = sanitized.substring(0, index) + sanitized.substring(index + 1);
/*     */     } 
/*     */     
/* 309 */     return sanitized;
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
/*     */   public String getRequestUri(HttpServletRequest request) {
/* 324 */     String uri = (String)request.getAttribute("javax.servlet.include.request_uri");
/* 325 */     if (uri == null) {
/* 326 */       uri = request.getRequestURI();
/*     */     }
/* 328 */     return decodeAndCleanUriString(request, uri);
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
/*     */   public String getContextPath(HttpServletRequest request) {
/* 340 */     String contextPath = (String)request.getAttribute("javax.servlet.include.context_path");
/* 341 */     if (contextPath == null) {
/* 342 */       contextPath = request.getContextPath();
/*     */     }
/* 344 */     if ("/".equals(contextPath))
/*     */     {
/* 346 */       contextPath = "";
/*     */     }
/* 348 */     return decodeRequestString(request, contextPath);
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
/*     */   public String getServletPath(HttpServletRequest request) {
/* 360 */     String servletPath = (String)request.getAttribute("javax.servlet.include.servlet_path");
/* 361 */     if (servletPath == null) {
/* 362 */       servletPath = request.getServletPath();
/*     */     }
/* 364 */     if (servletPath.length() > 1 && servletPath.endsWith("/") && shouldRemoveTrailingServletPathSlash(request))
/*     */     {
/*     */ 
/*     */       
/* 368 */       servletPath = servletPath.substring(0, servletPath.length() - 1);
/*     */     }
/* 370 */     return servletPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOriginatingRequestUri(HttpServletRequest request) {
/* 379 */     String uri = (String)request.getAttribute("com.ibm.websphere.servlet.uri_non_decoded");
/* 380 */     if (uri == null) {
/* 381 */       uri = (String)request.getAttribute("javax.servlet.forward.request_uri");
/* 382 */       if (uri == null) {
/* 383 */         uri = request.getRequestURI();
/*     */       }
/*     */     } 
/* 386 */     return decodeAndCleanUriString(request, uri);
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
/*     */   public String getOriginatingContextPath(HttpServletRequest request) {
/* 398 */     String contextPath = (String)request.getAttribute("javax.servlet.forward.context_path");
/* 399 */     if (contextPath == null) {
/* 400 */       contextPath = request.getContextPath();
/*     */     }
/* 402 */     return decodeRequestString(request, contextPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOriginatingServletPath(HttpServletRequest request) {
/* 412 */     String servletPath = (String)request.getAttribute("javax.servlet.forward.servlet_path");
/* 413 */     if (servletPath == null) {
/* 414 */       servletPath = request.getServletPath();
/*     */     }
/* 416 */     return servletPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOriginatingQueryString(HttpServletRequest request) {
/* 426 */     if (request.getAttribute("javax.servlet.forward.request_uri") != null || request
/* 427 */       .getAttribute("javax.servlet.error.request_uri") != null) {
/* 428 */       return (String)request.getAttribute("javax.servlet.forward.query_string");
/*     */     }
/*     */     
/* 431 */     return request.getQueryString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String decodeAndCleanUriString(HttpServletRequest request, String uri) {
/* 439 */     uri = removeSemicolonContent(uri);
/* 440 */     uri = decodeRequestString(request, uri);
/* 441 */     uri = getSanitizedPath(uri);
/* 442 */     return uri;
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
/*     */   public String decodeRequestString(HttpServletRequest request, String source) {
/* 458 */     if (this.urlDecode) {
/* 459 */       return decodeInternal(request, source);
/*     */     }
/* 461 */     return source;
/*     */   }
/*     */ 
/*     */   
/*     */   private String decodeInternal(HttpServletRequest request, String source) {
/* 466 */     String enc = determineEncoding(request);
/*     */     try {
/* 468 */       return UriUtils.decode(source, enc);
/*     */     }
/* 470 */     catch (UnsupportedCharsetException ex) {
/* 471 */       if (logger.isWarnEnabled()) {
/* 472 */         logger.warn("Could not decode request string [" + source + "] with encoding '" + enc + "': falling back to platform default encoding; exception message: " + ex
/* 473 */             .getMessage());
/*     */       }
/* 475 */       return URLDecoder.decode(source);
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
/*     */   protected String determineEncoding(HttpServletRequest request) {
/* 490 */     String enc = request.getCharacterEncoding();
/* 491 */     if (enc == null) {
/* 492 */       enc = getDefaultEncoding();
/*     */     }
/* 494 */     return enc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String removeSemicolonContent(String requestUri) {
/* 505 */     return this.removeSemicolonContent ? 
/* 506 */       removeSemicolonContentInternal(requestUri) : removeJsessionid(requestUri);
/*     */   }
/*     */   
/*     */   private String removeSemicolonContentInternal(String requestUri) {
/* 510 */     int semicolonIndex = requestUri.indexOf(';');
/* 511 */     while (semicolonIndex != -1) {
/* 512 */       int slashIndex = requestUri.indexOf('/', semicolonIndex);
/* 513 */       String start = requestUri.substring(0, semicolonIndex);
/* 514 */       requestUri = (slashIndex != -1) ? (start + requestUri.substring(slashIndex)) : start;
/* 515 */       semicolonIndex = requestUri.indexOf(';', semicolonIndex);
/*     */     } 
/* 517 */     return requestUri;
/*     */   }
/*     */   
/*     */   private String removeJsessionid(String requestUri) {
/* 521 */     int startIndex = requestUri.toLowerCase().indexOf(";jsessionid=");
/* 522 */     if (startIndex != -1) {
/* 523 */       int endIndex = requestUri.indexOf(';', startIndex + 12);
/* 524 */       String start = requestUri.substring(0, startIndex);
/* 525 */       requestUri = (endIndex != -1) ? (start + requestUri.substring(endIndex)) : start;
/*     */     } 
/* 527 */     return requestUri;
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
/*     */   public Map<String, String> decodePathVariables(HttpServletRequest request, Map<String, String> vars) {
/* 540 */     if (this.urlDecode) {
/* 541 */       return vars;
/*     */     }
/*     */     
/* 544 */     Map<String, String> decodedVars = new LinkedHashMap<>(vars.size());
/* 545 */     vars.forEach((key, value) -> (String)decodedVars.put(key, decodeInternal(request, value)));
/* 546 */     return decodedVars;
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
/*     */   public MultiValueMap<String, String> decodeMatrixVariables(HttpServletRequest request, MultiValueMap<String, String> vars) {
/* 562 */     if (this.urlDecode) {
/* 563 */       return vars;
/*     */     }
/*     */     
/* 566 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(vars.size());
/* 567 */     vars.forEach((key, values) -> {
/*     */           for (String value : values) {
/*     */             decodedVars.add(key, decodeInternal(request, value));
/*     */           }
/*     */         });
/* 572 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean shouldRemoveTrailingServletPathSlash(HttpServletRequest request) {
/* 577 */     if (request.getAttribute("com.ibm.websphere.servlet.uri_non_decoded") == null)
/*     */     {
/*     */ 
/*     */       
/* 581 */       return false;
/*     */     }
/* 583 */     Boolean flagToUse = websphereComplianceFlag;
/* 584 */     if (flagToUse == null) {
/* 585 */       ClassLoader classLoader = UrlPathHelper.class.getClassLoader();
/* 586 */       String className = "com.ibm.ws.webcontainer.WebContainer";
/* 587 */       String methodName = "getWebContainerProperties";
/* 588 */       String propName = "com.ibm.ws.webcontainer.removetrailingservletpathslash";
/* 589 */       boolean flag = false;
/*     */       try {
/* 591 */         Class<?> cl = classLoader.loadClass(className);
/* 592 */         Properties prop = (Properties)cl.getMethod(methodName, new Class[0]).invoke(null, new Object[0]);
/* 593 */         flag = Boolean.parseBoolean(prop.getProperty(propName));
/*     */       }
/* 595 */       catch (Throwable ex) {
/* 596 */         if (logger.isDebugEnabled()) {
/* 597 */           logger.debug("Could not introspect WebSphere web container properties: " + ex);
/*     */         }
/*     */       } 
/* 600 */       flagToUse = Boolean.valueOf(flag);
/* 601 */       websphereComplianceFlag = Boolean.valueOf(flag);
/*     */     } 
/*     */ 
/*     */     
/* 605 */     return !flagToUse.booleanValue();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/UrlPathHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */