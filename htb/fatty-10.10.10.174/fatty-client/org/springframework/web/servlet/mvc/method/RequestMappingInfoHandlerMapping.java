/*     */ package org.springframework.web.servlet.mvc.method;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.InvalidMediaTypeException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*     */ import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
/*     */ import org.springframework.web.servlet.mvc.condition.NameValueExpression;
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
/*     */ public abstract class RequestMappingInfoHandlerMapping
/*     */   extends AbstractHandlerMethodMapping<RequestMappingInfo>
/*     */ {
/*     */   private static final Method HTTP_OPTIONS_HANDLE_METHOD;
/*     */   
/*     */   static {
/*     */     try {
/*  63 */       HTTP_OPTIONS_HANDLE_METHOD = HttpOptionsHandler.class.getMethod("handle", new Class[0]);
/*     */     }
/*  65 */     catch (NoSuchMethodException ex) {
/*     */       
/*  67 */       throw new IllegalStateException("Failed to retrieve internal handler method for HTTP OPTIONS", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected RequestMappingInfoHandlerMapping() {
/*  73 */     setHandlerMethodMappingNamingStrategy(new RequestMappingInfoHandlerMethodMappingNamingStrategy());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<String> getMappingPathPatterns(RequestMappingInfo info) {
/*  82 */     return info.getPatternsCondition().getPatterns();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info, HttpServletRequest request) {
/*  93 */     return info.getMatchingCondition(request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Comparator<RequestMappingInfo> getMappingComparator(HttpServletRequest request) {
/* 101 */     return (info1, info2) -> info1.compareTo(info2, request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleMatch(RequestMappingInfo info, String lookupPath, HttpServletRequest request) {
/*     */     String bestPattern;
/*     */     Map<String, String> uriVariables;
/* 112 */     super.handleMatch(info, lookupPath, request);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     Set<String> patterns = info.getPatternsCondition().getPatterns();
/* 118 */     if (patterns.isEmpty()) {
/* 119 */       bestPattern = lookupPath;
/* 120 */       uriVariables = Collections.emptyMap();
/*     */     } else {
/*     */       
/* 123 */       bestPattern = patterns.iterator().next();
/* 124 */       uriVariables = getPathMatcher().extractUriTemplateVariables(bestPattern, lookupPath);
/*     */     } 
/*     */     
/* 127 */     request.setAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE, bestPattern);
/*     */     
/* 129 */     if (isMatrixVariableContentAvailable()) {
/* 130 */       Map<String, MultiValueMap<String, String>> matrixVars = extractMatrixVariables(request, uriVariables);
/* 131 */       request.setAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE, matrixVars);
/*     */     } 
/*     */     
/* 134 */     Map<String, String> decodedUriVariables = getUrlPathHelper().decodePathVariables(request, uriVariables);
/* 135 */     request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, decodedUriVariables);
/*     */     
/* 137 */     if (!info.getProducesCondition().getProducibleMediaTypes().isEmpty()) {
/* 138 */       Set<MediaType> mediaTypes = info.getProducesCondition().getProducibleMediaTypes();
/* 139 */       request.setAttribute(PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMatrixVariableContentAvailable() {
/* 144 */     return !getUrlPathHelper().shouldRemoveSemicolonContent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, MultiValueMap<String, String>> extractMatrixVariables(HttpServletRequest request, Map<String, String> uriVariables) {
/* 150 */     Map<String, MultiValueMap<String, String>> result = new LinkedHashMap<>();
/* 151 */     uriVariables.forEach((uriVarKey, uriVarValue) -> {
/*     */           String matrixVariables;
/*     */           
/*     */           int equalsIndex = uriVarValue.indexOf('=');
/*     */           
/*     */           if (equalsIndex == -1) {
/*     */             return;
/*     */           }
/*     */           
/*     */           int semicolonIndex = uriVarValue.indexOf(';');
/*     */           
/*     */           if (semicolonIndex != -1 && semicolonIndex != 0) {
/*     */             uriVariables.put(uriVarKey, uriVarValue.substring(0, semicolonIndex));
/*     */           }
/*     */           
/*     */           if (semicolonIndex == -1 || semicolonIndex == 0 || equalsIndex < semicolonIndex) {
/*     */             matrixVariables = uriVarValue;
/*     */           } else {
/*     */             matrixVariables = uriVarValue.substring(semicolonIndex + 1);
/*     */           } 
/*     */           MultiValueMap<String, String> vars = WebUtils.parseMatrixVariables(matrixVariables);
/*     */           result.put(uriVarKey, getUrlPathHelper().decodeMatrixVariables(request, vars));
/*     */         });
/* 174 */     return result;
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
/*     */   protected HandlerMethod handleNoMatch(Set<RequestMappingInfo> infos, String lookupPath, HttpServletRequest request) throws ServletException {
/* 189 */     PartialMatchHelper helper = new PartialMatchHelper(infos, request);
/* 190 */     if (helper.isEmpty()) {
/* 191 */       return null;
/*     */     }
/*     */     
/* 194 */     if (helper.hasMethodsMismatch()) {
/* 195 */       Set<String> methods = helper.getAllowedMethods();
/* 196 */       if (HttpMethod.OPTIONS.matches(request.getMethod())) {
/* 197 */         HttpOptionsHandler handler = new HttpOptionsHandler(methods);
/* 198 */         return new HandlerMethod(handler, HTTP_OPTIONS_HANDLE_METHOD);
/*     */       } 
/* 200 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), methods);
/*     */     } 
/*     */     
/* 203 */     if (helper.hasConsumesMismatch()) {
/* 204 */       Set<MediaType> mediaTypes = helper.getConsumableMediaTypes();
/* 205 */       MediaType contentType = null;
/* 206 */       if (StringUtils.hasLength(request.getContentType())) {
/*     */         try {
/* 208 */           contentType = MediaType.parseMediaType(request.getContentType());
/*     */         }
/* 210 */         catch (InvalidMediaTypeException ex) {
/* 211 */           throw new HttpMediaTypeNotSupportedException(ex.getMessage());
/*     */         } 
/*     */       }
/* 214 */       throw new HttpMediaTypeNotSupportedException(contentType, new ArrayList(mediaTypes));
/*     */     } 
/*     */     
/* 217 */     if (helper.hasProducesMismatch()) {
/* 218 */       Set<MediaType> mediaTypes = helper.getProducibleMediaTypes();
/* 219 */       throw new HttpMediaTypeNotAcceptableException(new ArrayList(mediaTypes));
/*     */     } 
/*     */     
/* 222 */     if (helper.hasParamsMismatch()) {
/* 223 */       List<String[]> conditions = helper.getParamConditions();
/* 224 */       throw new UnsatisfiedServletRequestParameterException(conditions, request.getParameterMap());
/*     */     } 
/*     */     
/* 227 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PartialMatchHelper
/*     */   {
/* 236 */     private final List<PartialMatch> partialMatches = new ArrayList<>();
/*     */     
/*     */     public PartialMatchHelper(Set<RequestMappingInfo> infos, HttpServletRequest request) {
/* 239 */       for (RequestMappingInfo info : infos) {
/* 240 */         if (info.getPatternsCondition().getMatchingCondition(request) != null) {
/* 241 */           this.partialMatches.add(new PartialMatch(info, request));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 250 */       return this.partialMatches.isEmpty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasMethodsMismatch() {
/* 257 */       for (PartialMatch match : this.partialMatches) {
/* 258 */         if (match.hasMethodsMatch()) {
/* 259 */           return false;
/*     */         }
/*     */       } 
/* 262 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasConsumesMismatch() {
/* 269 */       for (PartialMatch match : this.partialMatches) {
/* 270 */         if (match.hasConsumesMatch()) {
/* 271 */           return false;
/*     */         }
/*     */       } 
/* 274 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasProducesMismatch() {
/* 281 */       for (PartialMatch match : this.partialMatches) {
/* 282 */         if (match.hasProducesMatch()) {
/* 283 */           return false;
/*     */         }
/*     */       } 
/* 286 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasParamsMismatch() {
/* 293 */       for (PartialMatch match : this.partialMatches) {
/* 294 */         if (match.hasParamsMatch()) {
/* 295 */           return false;
/*     */         }
/*     */       } 
/* 298 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<String> getAllowedMethods() {
/* 305 */       Set<String> result = new LinkedHashSet<>();
/* 306 */       for (PartialMatch match : this.partialMatches) {
/* 307 */         for (RequestMethod method : match.getInfo().getMethodsCondition().getMethods()) {
/* 308 */           result.add(method.name());
/*     */         }
/*     */       } 
/* 311 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<MediaType> getConsumableMediaTypes() {
/* 319 */       Set<MediaType> result = new LinkedHashSet<>();
/* 320 */       for (PartialMatch match : this.partialMatches) {
/* 321 */         if (match.hasMethodsMatch()) {
/* 322 */           result.addAll(match.getInfo().getConsumesCondition().getConsumableMediaTypes());
/*     */         }
/*     */       } 
/* 325 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<MediaType> getProducibleMediaTypes() {
/* 333 */       Set<MediaType> result = new LinkedHashSet<>();
/* 334 */       for (PartialMatch match : this.partialMatches) {
/* 335 */         if (match.hasConsumesMatch()) {
/* 336 */           result.addAll(match.getInfo().getProducesCondition().getProducibleMediaTypes());
/*     */         }
/*     */       } 
/* 339 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<String[]> getParamConditions() {
/* 347 */       List<String[]> result = (List)new ArrayList<>();
/* 348 */       for (PartialMatch match : this.partialMatches) {
/* 349 */         if (match.hasProducesMatch()) {
/* 350 */           Set<NameValueExpression<String>> set = match.getInfo().getParamsCondition().getExpressions();
/* 351 */           if (!CollectionUtils.isEmpty(set)) {
/* 352 */             int i = 0;
/* 353 */             String[] array = new String[set.size()];
/* 354 */             for (NameValueExpression<String> expression : set) {
/* 355 */               array[i++] = expression.toString();
/*     */             }
/* 357 */             result.add(array);
/*     */           } 
/*     */         } 
/*     */       } 
/* 361 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static class PartialMatch
/*     */     {
/*     */       private final RequestMappingInfo info;
/*     */ 
/*     */       
/*     */       private final boolean methodsMatch;
/*     */ 
/*     */       
/*     */       private final boolean consumesMatch;
/*     */ 
/*     */       
/*     */       private final boolean producesMatch;
/*     */ 
/*     */       
/*     */       private final boolean paramsMatch;
/*     */ 
/*     */ 
/*     */       
/*     */       public PartialMatch(RequestMappingInfo info, HttpServletRequest request) {
/* 386 */         this.info = info;
/* 387 */         this.methodsMatch = (info.getMethodsCondition().getMatchingCondition(request) != null);
/* 388 */         this.consumesMatch = (info.getConsumesCondition().getMatchingCondition(request) != null);
/* 389 */         this.producesMatch = (info.getProducesCondition().getMatchingCondition(request) != null);
/* 390 */         this.paramsMatch = (info.getParamsCondition().getMatchingCondition(request) != null);
/*     */       }
/*     */       
/*     */       public RequestMappingInfo getInfo() {
/* 394 */         return this.info;
/*     */       }
/*     */       
/*     */       public boolean hasMethodsMatch() {
/* 398 */         return this.methodsMatch;
/*     */       }
/*     */       
/*     */       public boolean hasConsumesMatch() {
/* 402 */         return (hasMethodsMatch() && this.consumesMatch);
/*     */       }
/*     */       
/*     */       public boolean hasProducesMatch() {
/* 406 */         return (hasConsumesMatch() && this.producesMatch);
/*     */       }
/*     */       
/*     */       public boolean hasParamsMatch() {
/* 410 */         return (hasProducesMatch() && this.paramsMatch);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 415 */         return this.info.toString();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class HttpOptionsHandler
/*     */   {
/* 426 */     private final HttpHeaders headers = new HttpHeaders();
/*     */     
/*     */     public HttpOptionsHandler(Set<String> declaredMethods) {
/* 429 */       this.headers.setAllow(initAllowedHttpMethods(declaredMethods));
/*     */     }
/*     */     
/*     */     private static Set<HttpMethod> initAllowedHttpMethods(Set<String> declaredMethods) {
/* 433 */       Set<HttpMethod> result = new LinkedHashSet<>(declaredMethods.size());
/* 434 */       if (declaredMethods.isEmpty()) {
/* 435 */         for (HttpMethod method : HttpMethod.values()) {
/* 436 */           if (method != HttpMethod.TRACE) {
/* 437 */             result.add(method);
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 442 */         for (String method : declaredMethods) {
/* 443 */           HttpMethod httpMethod = HttpMethod.valueOf(method);
/* 444 */           result.add(httpMethod);
/* 445 */           if (httpMethod == HttpMethod.GET) {
/* 446 */             result.add(HttpMethod.HEAD);
/*     */           }
/*     */         } 
/* 449 */         result.add(HttpMethod.OPTIONS);
/*     */       } 
/* 451 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpHeaders handle() {
/* 456 */       return this.headers;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/RequestMappingInfoHandlerMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */