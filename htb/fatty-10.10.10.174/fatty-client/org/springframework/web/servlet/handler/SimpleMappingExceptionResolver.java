/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.servlet.ModelAndView;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleMappingExceptionResolver
/*     */   extends AbstractHandlerExceptionResolver
/*     */ {
/*     */   public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";
/*     */   @Nullable
/*     */   private Properties exceptionMappings;
/*     */   @Nullable
/*     */   private Class<?>[] excludedExceptions;
/*     */   @Nullable
/*     */   private String defaultErrorView;
/*     */   @Nullable
/*     */   private Integer defaultStatusCode;
/*  63 */   private Map<String, Integer> statusCodes = new HashMap<>();
/*     */   @Nullable
/*  65 */   private String exceptionAttribute = "exception";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionMappings(Properties mappings) {
/*  84 */     this.exceptionMappings = mappings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludedExceptions(Class<?>... excludedExceptions) {
/*  94 */     this.excludedExceptions = excludedExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultErrorView(String defaultErrorView) {
/* 103 */     this.defaultErrorView = defaultErrorView;
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
/*     */   public void setStatusCodes(Properties statusCodes) {
/* 116 */     for (Enumeration<?> enumeration = statusCodes.propertyNames(); enumeration.hasMoreElements(); ) {
/* 117 */       String viewName = (String)enumeration.nextElement();
/* 118 */       Integer statusCode = Integer.valueOf(statusCodes.getProperty(viewName));
/* 119 */       this.statusCodes.put(viewName, statusCode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStatusCode(String viewName, int statusCode) {
/* 128 */     this.statusCodes.put(viewName, Integer.valueOf(statusCode));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Integer> getStatusCodesAsMap() {
/* 136 */     return Collections.unmodifiableMap(this.statusCodes);
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
/*     */   public void setDefaultStatusCode(int defaultStatusCode) {
/* 152 */     this.defaultStatusCode = Integer.valueOf(defaultStatusCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionAttribute(@Nullable String exceptionAttribute) {
/* 163 */     this.exceptionAttribute = exceptionAttribute;
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
/*     */   @Nullable
/*     */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
/* 188 */     String viewName = determineViewName(ex, request);
/* 189 */     if (viewName != null) {
/*     */ 
/*     */       
/* 192 */       Integer statusCode = determineStatusCode(request, viewName);
/* 193 */       if (statusCode != null) {
/* 194 */         applyStatusCodeIfPossible(request, response, statusCode.intValue());
/*     */       }
/* 196 */       return getModelAndView(viewName, ex, request);
/*     */     } 
/*     */     
/* 199 */     return null;
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
/*     */   protected String determineViewName(Exception ex, HttpServletRequest request) {
/* 214 */     String viewName = null;
/* 215 */     if (this.excludedExceptions != null) {
/* 216 */       for (Class<?> excludedEx : this.excludedExceptions) {
/* 217 */         if (excludedEx.equals(ex.getClass())) {
/* 218 */           return null;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 223 */     if (this.exceptionMappings != null) {
/* 224 */       viewName = findMatchingViewName(this.exceptionMappings, ex);
/*     */     }
/*     */     
/* 227 */     if (viewName == null && this.defaultErrorView != null) {
/* 228 */       if (this.logger.isDebugEnabled()) {
/* 229 */         this.logger.debug("Resolving to default view '" + this.defaultErrorView + "'");
/*     */       }
/* 231 */       viewName = this.defaultErrorView;
/*     */     } 
/* 233 */     return viewName;
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
/*     */   protected String findMatchingViewName(Properties exceptionMappings, Exception ex) {
/* 245 */     String viewName = null;
/* 246 */     String dominantMapping = null;
/* 247 */     int deepest = Integer.MAX_VALUE;
/* 248 */     for (Enumeration<?> names = exceptionMappings.propertyNames(); names.hasMoreElements(); ) {
/* 249 */       String exceptionMapping = (String)names.nextElement();
/* 250 */       int depth = getDepth(exceptionMapping, ex);
/* 251 */       if (depth >= 0 && (depth < deepest || (depth == deepest && dominantMapping != null && exceptionMapping
/* 252 */         .length() > dominantMapping.length()))) {
/* 253 */         deepest = depth;
/* 254 */         dominantMapping = exceptionMapping;
/* 255 */         viewName = exceptionMappings.getProperty(exceptionMapping);
/*     */       } 
/*     */     } 
/* 258 */     if (viewName != null && this.logger.isDebugEnabled()) {
/* 259 */       this.logger.debug("Resolving to view '" + viewName + "' based on mapping [" + dominantMapping + "]");
/*     */     }
/* 261 */     return viewName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDepth(String exceptionMapping, Exception ex) {
/* 270 */     return getDepth(exceptionMapping, ex.getClass(), 0);
/*     */   }
/*     */   
/*     */   private int getDepth(String exceptionMapping, Class<?> exceptionClass, int depth) {
/* 274 */     if (exceptionClass.getName().contains(exceptionMapping))
/*     */     {
/* 276 */       return depth;
/*     */     }
/*     */     
/* 279 */     if (exceptionClass == Throwable.class) {
/* 280 */       return -1;
/*     */     }
/* 282 */     return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
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
/*     */   @Nullable
/*     */   protected Integer determineStatusCode(HttpServletRequest request, String viewName) {
/* 300 */     if (this.statusCodes.containsKey(viewName)) {
/* 301 */       return this.statusCodes.get(viewName);
/*     */     }
/* 303 */     return this.defaultStatusCode;
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
/*     */   protected void applyStatusCodeIfPossible(HttpServletRequest request, HttpServletResponse response, int statusCode) {
/* 317 */     if (!WebUtils.isIncludeRequest((ServletRequest)request)) {
/* 318 */       if (this.logger.isDebugEnabled()) {
/* 319 */         this.logger.debug("Applying HTTP status " + statusCode);
/*     */       }
/* 321 */       response.setStatus(statusCode);
/* 322 */       request.setAttribute("javax.servlet.error.status_code", Integer.valueOf(statusCode));
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
/*     */   protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request) {
/* 335 */     return getModelAndView(viewName, ex);
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
/*     */   protected ModelAndView getModelAndView(String viewName, Exception ex) {
/* 348 */     ModelAndView mv = new ModelAndView(viewName);
/* 349 */     if (this.exceptionAttribute != null) {
/* 350 */       mv.addObject(this.exceptionAttribute, ex);
/*     */     }
/* 352 */     return mv;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/SimpleMappingExceptionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */