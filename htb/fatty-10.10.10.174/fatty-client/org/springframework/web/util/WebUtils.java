/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.net.URI;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeMap;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletRequestWrapper;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.ServletResponseWrapper;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WebUtils
/*     */ {
/*     */   public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
/*     */   public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
/*     */   public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
/*     */   public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
/*     */   public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";
/*     */   public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
/*     */   public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
/*     */   public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
/*     */   public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
/*     */   public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";
/*     */   public static final String ERROR_STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";
/*     */   public static final String ERROR_EXCEPTION_TYPE_ATTRIBUTE = "javax.servlet.error.exception_type";
/*     */   public static final String ERROR_MESSAGE_ATTRIBUTE = "javax.servlet.error.message";
/*     */   public static final String ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
/*     */   public static final String ERROR_REQUEST_URI_ATTRIBUTE = "javax.servlet.error.request_uri";
/*     */   public static final String ERROR_SERVLET_NAME_ATTRIBUTE = "javax.servlet.error.servlet_name";
/*     */   public static final String CONTENT_TYPE_CHARSET_PREFIX = ";charset=";
/*     */   public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
/*     */   public static final String TEMP_DIR_CONTEXT_ATTRIBUTE = "javax.servlet.context.tempdir";
/*     */   public static final String HTML_ESCAPE_CONTEXT_PARAM = "defaultHtmlEscape";
/*     */   public static final String RESPONSE_ENCODED_HTML_ESCAPE_CONTEXT_PARAM = "responseEncodedHtmlEscape";
/*     */   public static final String WEB_APP_ROOT_KEY_PARAM = "webAppRootKey";
/*     */   public static final String DEFAULT_WEB_APP_ROOT_KEY = "webapp.root";
/* 217 */   public static final String[] SUBMIT_IMAGE_SUFFIXES = new String[] { ".x", ".y" };
/*     */ 
/*     */   
/* 220 */   public static final String SESSION_MUTEX_ATTRIBUTE = WebUtils.class.getName() + ".MUTEX";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setWebAppRootSystemProperty(ServletContext servletContext) throws IllegalStateException {
/* 237 */     Assert.notNull(servletContext, "ServletContext must not be null");
/* 238 */     String root = servletContext.getRealPath("/");
/* 239 */     if (root == null) {
/* 240 */       throw new IllegalStateException("Cannot set web app root system property when WAR file is not expanded");
/*     */     }
/*     */     
/* 243 */     String param = servletContext.getInitParameter("webAppRootKey");
/* 244 */     String key = (param != null) ? param : "webapp.root";
/* 245 */     String oldValue = System.getProperty(key);
/* 246 */     if (oldValue != null && !StringUtils.pathEquals(oldValue, root)) {
/* 247 */       throw new IllegalStateException("Web app root system property already set to different value: '" + key + "' = [" + oldValue + "] instead of [" + root + "] - Choose unique values for the 'webAppRootKey' context-param in your web.xml files!");
/*     */     }
/*     */ 
/*     */     
/* 251 */     System.setProperty(key, root);
/* 252 */     servletContext.log("Set web app root system property: '" + key + "' = [" + root + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeWebAppRootSystemProperty(ServletContext servletContext) {
/* 262 */     Assert.notNull(servletContext, "ServletContext must not be null");
/* 263 */     String param = servletContext.getInitParameter("webAppRootKey");
/* 264 */     String key = (param != null) ? param : "webapp.root";
/* 265 */     System.getProperties().remove(key);
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
/*     */   @Nullable
/*     */   public static Boolean getDefaultHtmlEscape(@Nullable ServletContext servletContext) {
/* 281 */     if (servletContext == null) {
/* 282 */       return null;
/*     */     }
/* 284 */     String param = servletContext.getInitParameter("defaultHtmlEscape");
/* 285 */     return StringUtils.hasText(param) ? Boolean.valueOf(param) : null;
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
/*     */   @Nullable
/*     */   public static Boolean getResponseEncodedHtmlEscape(@Nullable ServletContext servletContext) {
/* 304 */     if (servletContext == null) {
/* 305 */       return null;
/*     */     }
/* 307 */     String param = servletContext.getInitParameter("responseEncodedHtmlEscape");
/* 308 */     return StringUtils.hasText(param) ? Boolean.valueOf(param) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getTempDir(ServletContext servletContext) {
/* 318 */     Assert.notNull(servletContext, "ServletContext must not be null");
/* 319 */     return (File)servletContext.getAttribute("javax.servlet.context.tempdir");
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
/*     */   public static String getRealPath(ServletContext servletContext, String path) throws FileNotFoundException {
/* 336 */     Assert.notNull(servletContext, "ServletContext must not be null");
/*     */     
/* 338 */     if (!path.startsWith("/")) {
/* 339 */       path = "/" + path;
/*     */     }
/* 341 */     String realPath = servletContext.getRealPath(path);
/* 342 */     if (realPath == null) {
/* 343 */       throw new FileNotFoundException("ServletContext resource [" + path + "] cannot be resolved to absolute file path - web application archive not expanded?");
/*     */     }
/*     */ 
/*     */     
/* 347 */     return realPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String getSessionId(HttpServletRequest request) {
/* 357 */     Assert.notNull(request, "Request must not be null");
/* 358 */     HttpSession session = request.getSession(false);
/* 359 */     return (session != null) ? session.getId() : null;
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
/*     */   @Nullable
/*     */   public static Object getSessionAttribute(HttpServletRequest request, String name) {
/* 372 */     Assert.notNull(request, "Request must not be null");
/* 373 */     HttpSession session = request.getSession(false);
/* 374 */     return (session != null) ? session.getAttribute(name) : null;
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
/*     */   public static Object getRequiredSessionAttribute(HttpServletRequest request, String name) throws IllegalStateException {
/* 389 */     Object attr = getSessionAttribute(request, name);
/* 390 */     if (attr == null) {
/* 391 */       throw new IllegalStateException("No session attribute '" + name + "' found");
/*     */     }
/* 393 */     return attr;
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
/*     */   public static void setSessionAttribute(HttpServletRequest request, String name, @Nullable Object value) {
/* 405 */     Assert.notNull(request, "Request must not be null");
/* 406 */     if (value != null) {
/* 407 */       request.getSession().setAttribute(name, value);
/*     */     } else {
/*     */       
/* 410 */       HttpSession session = request.getSession(false);
/* 411 */       if (session != null) {
/* 412 */         session.removeAttribute(name);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getSessionMutex(HttpSession session) {
/* 438 */     Assert.notNull(session, "Session must not be null");
/* 439 */     Object mutex = session.getAttribute(SESSION_MUTEX_ATTRIBUTE);
/* 440 */     if (mutex == null) {
/* 441 */       mutex = session;
/*     */     }
/* 443 */     return mutex;
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
/*     */   @Nullable
/*     */   public static <T> T getNativeRequest(ServletRequest request, @Nullable Class<T> requiredType) {
/* 458 */     if (requiredType != null) {
/* 459 */       if (requiredType.isInstance(request)) {
/* 460 */         return (T)request;
/*     */       }
/* 462 */       if (request instanceof ServletRequestWrapper) {
/* 463 */         return getNativeRequest(((ServletRequestWrapper)request).getRequest(), requiredType);
/*     */       }
/*     */     } 
/* 466 */     return null;
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
/*     */   @Nullable
/*     */   public static <T> T getNativeResponse(ServletResponse response, @Nullable Class<T> requiredType) {
/* 480 */     if (requiredType != null) {
/* 481 */       if (requiredType.isInstance(response)) {
/* 482 */         return (T)response;
/*     */       }
/* 484 */       if (response instanceof ServletResponseWrapper) {
/* 485 */         return getNativeResponse(((ServletResponseWrapper)response).getResponse(), requiredType);
/*     */       }
/*     */     } 
/* 488 */     return null;
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
/*     */   public static boolean isIncludeRequest(ServletRequest request) {
/* 501 */     return (request.getAttribute("javax.servlet.include.request_uri") != null);
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
/*     */   public static void exposeErrorRequestAttributes(HttpServletRequest request, Throwable ex, @Nullable String servletName) {
/* 525 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.status_code", Integer.valueOf(200));
/* 526 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.exception_type", ex.getClass());
/* 527 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.message", ex.getMessage());
/* 528 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.exception", ex);
/* 529 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.request_uri", request.getRequestURI());
/* 530 */     if (servletName != null) {
/* 531 */       exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.servlet_name", servletName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void exposeRequestAttributeIfNotPresent(ServletRequest request, String name, Object value) {
/* 542 */     if (request.getAttribute(name) == null) {
/* 543 */       request.setAttribute(name, value);
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
/*     */   public static void clearErrorRequestAttributes(HttpServletRequest request) {
/* 559 */     request.removeAttribute("javax.servlet.error.status_code");
/* 560 */     request.removeAttribute("javax.servlet.error.exception_type");
/* 561 */     request.removeAttribute("javax.servlet.error.message");
/* 562 */     request.removeAttribute("javax.servlet.error.exception");
/* 563 */     request.removeAttribute("javax.servlet.error.request_uri");
/* 564 */     request.removeAttribute("javax.servlet.error.servlet_name");
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
/*     */   public static Cookie getCookie(HttpServletRequest request, String name) {
/* 576 */     Assert.notNull(request, "Request must not be null");
/* 577 */     Cookie[] cookies = request.getCookies();
/* 578 */     if (cookies != null) {
/* 579 */       for (Cookie cookie : cookies) {
/* 580 */         if (name.equals(cookie.getName())) {
/* 581 */           return cookie;
/*     */         }
/*     */       } 
/*     */     }
/* 585 */     return null;
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
/*     */   public static boolean hasSubmitParameter(ServletRequest request, String name) {
/* 598 */     Assert.notNull(request, "Request must not be null");
/* 599 */     if (request.getParameter(name) != null) {
/* 600 */       return true;
/*     */     }
/* 602 */     for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
/* 603 */       if (request.getParameter(name + suffix) != null) {
/* 604 */         return true;
/*     */       }
/*     */     } 
/* 607 */     return false;
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
/*     */   @Nullable
/*     */   public static String findParameterValue(ServletRequest request, String name) {
/* 621 */     return findParameterValue(request.getParameterMap(), name);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String findParameterValue(Map<String, ?> parameters, String name) {
/* 650 */     Object value = parameters.get(name);
/* 651 */     if (value instanceof String[]) {
/* 652 */       String[] values = (String[])value;
/* 653 */       return (values.length > 0) ? values[0] : null;
/*     */     } 
/* 655 */     if (value != null) {
/* 656 */       return value.toString();
/*     */     }
/*     */     
/* 659 */     String prefix = name + "_";
/* 660 */     for (String paramName : parameters.keySet()) {
/* 661 */       if (paramName.startsWith(prefix)) {
/*     */         
/* 663 */         for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
/* 664 */           if (paramName.endsWith(suffix)) {
/* 665 */             return paramName.substring(prefix.length(), paramName.length() - suffix.length());
/*     */           }
/*     */         } 
/* 668 */         return paramName.substring(prefix.length());
/*     */       } 
/*     */     } 
/*     */     
/* 672 */     return null;
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
/*     */   public static Map<String, Object> getParametersStartingWith(ServletRequest request, @Nullable String prefix) {
/* 690 */     Assert.notNull(request, "Request must not be null");
/* 691 */     Enumeration<String> paramNames = request.getParameterNames();
/* 692 */     Map<String, Object> params = new TreeMap<>();
/* 693 */     if (prefix == null) {
/* 694 */       prefix = "";
/*     */     }
/* 696 */     while (paramNames != null && paramNames.hasMoreElements()) {
/* 697 */       String paramName = paramNames.nextElement();
/* 698 */       if ("".equals(prefix) || paramName.startsWith(prefix)) {
/* 699 */         String unprefixed = paramName.substring(prefix.length());
/* 700 */         String[] values = request.getParameterValues(paramName);
/* 701 */         if (values == null || values.length == 0) {
/*     */           continue;
/*     */         }
/* 704 */         if (values.length > 1) {
/* 705 */           params.put(unprefixed, values);
/*     */           continue;
/*     */         } 
/* 708 */         params.put(unprefixed, values[0]);
/*     */       } 
/*     */     } 
/*     */     
/* 712 */     return params;
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
/*     */   public static MultiValueMap<String, String> parseMatrixVariables(String matrixVariables) {
/* 725 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 726 */     if (!StringUtils.hasText(matrixVariables)) {
/* 727 */       return (MultiValueMap<String, String>)linkedMultiValueMap;
/*     */     }
/* 729 */     StringTokenizer pairs = new StringTokenizer(matrixVariables, ";");
/* 730 */     while (pairs.hasMoreTokens()) {
/* 731 */       String pair = pairs.nextToken();
/* 732 */       int index = pair.indexOf('=');
/* 733 */       if (index != -1) {
/* 734 */         String name = pair.substring(0, index);
/* 735 */         String rawValue = pair.substring(index + 1);
/* 736 */         for (String value : StringUtils.commaDelimitedListToStringArray(rawValue)) {
/* 737 */           linkedMultiValueMap.add(name, value);
/*     */         }
/*     */         continue;
/*     */       } 
/* 741 */       linkedMultiValueMap.add(pair, "");
/*     */     } 
/*     */     
/* 744 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
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
/*     */   public static boolean isValidOrigin(HttpRequest request, Collection<String> allowedOrigins) {
/* 762 */     Assert.notNull(request, "Request must not be null");
/* 763 */     Assert.notNull(allowedOrigins, "Allowed origins must not be null");
/*     */     
/* 765 */     String origin = request.getHeaders().getOrigin();
/* 766 */     if (origin == null || allowedOrigins.contains("*")) {
/* 767 */       return true;
/*     */     }
/* 769 */     if (CollectionUtils.isEmpty(allowedOrigins)) {
/* 770 */       return isSameOrigin(request);
/*     */     }
/*     */     
/* 773 */     return allowedOrigins.contains(origin);
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
/*     */   public static boolean isSameOrigin(HttpRequest request) {
/*     */     String scheme, host;
/*     */     int port;
/* 792 */     HttpHeaders headers = request.getHeaders();
/* 793 */     String origin = headers.getOrigin();
/* 794 */     if (origin == null) {
/* 795 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 801 */     if (request instanceof ServletServerHttpRequest) {
/*     */       
/* 803 */       HttpServletRequest servletRequest = ((ServletServerHttpRequest)request).getServletRequest();
/* 804 */       scheme = servletRequest.getScheme();
/* 805 */       host = servletRequest.getServerName();
/* 806 */       port = servletRequest.getServerPort();
/*     */     } else {
/*     */       
/* 809 */       URI uri = request.getURI();
/* 810 */       scheme = uri.getScheme();
/* 811 */       host = uri.getHost();
/* 812 */       port = uri.getPort();
/*     */     } 
/*     */     
/* 815 */     UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
/* 816 */     return (ObjectUtils.nullSafeEquals(scheme, originUrl.getScheme()) && 
/* 817 */       ObjectUtils.nullSafeEquals(host, originUrl.getHost()) && 
/* 818 */       getPort(scheme, port) == getPort(originUrl.getScheme(), originUrl.getPort()));
/*     */   }
/*     */   
/*     */   private static int getPort(@Nullable String scheme, int port) {
/* 822 */     if (port == -1) {
/* 823 */       if ("http".equals(scheme) || "ws".equals(scheme)) {
/* 824 */         port = 80;
/*     */       }
/* 826 */       else if ("https".equals(scheme) || "wss".equals(scheme)) {
/* 827 */         port = 443;
/*     */       } 
/*     */     }
/* 830 */     return port;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/WebUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */