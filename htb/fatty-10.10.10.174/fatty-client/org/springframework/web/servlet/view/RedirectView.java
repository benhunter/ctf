/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.SmartView;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ import org.springframework.web.util.UriUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RedirectView
/*     */   extends AbstractUrlBasedView
/*     */   implements SmartView
/*     */ {
/*  88 */   private static final Pattern URI_TEMPLATE_VARIABLE_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
/*     */ 
/*     */   
/*     */   private boolean contextRelative = false;
/*     */ 
/*     */   
/*     */   private boolean http10Compatible = true;
/*     */ 
/*     */   
/*     */   private boolean exposeModelAttributes = true;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String encodingScheme;
/*     */   
/*     */   @Nullable
/*     */   private HttpStatus statusCode;
/*     */   
/*     */   private boolean expandUriTemplateVariables = true;
/*     */   
/*     */   private boolean propagateQueryParams = false;
/*     */   
/*     */   @Nullable
/*     */   private String[] hosts;
/*     */ 
/*     */   
/*     */   public RedirectView() {
/* 115 */     setExposePathVariables(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectView(String url) {
/* 126 */     super(url);
/* 127 */     setExposePathVariables(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectView(String url, boolean contextRelative) {
/* 137 */     super(url);
/* 138 */     this.contextRelative = contextRelative;
/* 139 */     setExposePathVariables(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectView(String url, boolean contextRelative, boolean http10Compatible) {
/* 150 */     super(url);
/* 151 */     this.contextRelative = contextRelative;
/* 152 */     this.http10Compatible = http10Compatible;
/* 153 */     setExposePathVariables(false);
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
/*     */   public RedirectView(String url, boolean contextRelative, boolean http10Compatible, boolean exposeModelAttributes) {
/* 166 */     super(url);
/* 167 */     this.contextRelative = contextRelative;
/* 168 */     this.http10Compatible = http10Compatible;
/* 169 */     this.exposeModelAttributes = exposeModelAttributes;
/* 170 */     setExposePathVariables(false);
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
/*     */   public void setContextRelative(boolean contextRelative) {
/* 184 */     this.contextRelative = contextRelative;
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
/*     */   public void setHttp10Compatible(boolean http10Compatible) {
/* 199 */     this.http10Compatible = http10Compatible;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExposeModelAttributes(boolean exposeModelAttributes) {
/* 208 */     this.exposeModelAttributes = exposeModelAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncodingScheme(String encodingScheme) {
/* 217 */     this.encodingScheme = encodingScheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatusCode(HttpStatus statusCode) {
/* 226 */     this.statusCode = statusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpandUriTemplateVariables(boolean expandUriTemplateVariables) {
/* 237 */     this.expandUriTemplateVariables = expandUriTemplateVariables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropagateQueryParams(boolean propagateQueryParams) {
/* 247 */     this.propagateQueryParams = propagateQueryParams;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPropagateQueryProperties() {
/* 255 */     return this.propagateQueryParams;
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
/*     */   public void setHosts(@Nullable String... hosts) {
/* 269 */     this.hosts = hosts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getHosts() {
/* 278 */     return this.hosts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedirectView() {
/* 286 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isContextRequired() {
/* 294 */     return false;
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
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
/* 307 */     String targetUrl = createTargetUrl(model, request);
/* 308 */     targetUrl = updateTargetUrl(targetUrl, model, request, response);
/*     */ 
/*     */     
/* 311 */     RequestContextUtils.saveOutputFlashMap(targetUrl, request, response);
/*     */ 
/*     */     
/* 314 */     sendRedirect(request, response, targetUrl, this.http10Compatible);
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
/*     */   protected final String createTargetUrl(Map<String, Object> model, HttpServletRequest request) throws UnsupportedEncodingException {
/* 326 */     StringBuilder targetUrl = new StringBuilder();
/* 327 */     String url = getUrl();
/* 328 */     Assert.state((url != null), "'url' not set");
/*     */     
/* 330 */     if (this.contextRelative && getUrl().startsWith("/"))
/*     */     {
/* 332 */       targetUrl.append(getContextPath(request));
/*     */     }
/* 334 */     targetUrl.append(getUrl());
/*     */     
/* 336 */     String enc = this.encodingScheme;
/* 337 */     if (enc == null) {
/* 338 */       enc = request.getCharacterEncoding();
/*     */     }
/* 340 */     if (enc == null) {
/* 341 */       enc = "ISO-8859-1";
/*     */     }
/*     */     
/* 344 */     if (this.expandUriTemplateVariables && StringUtils.hasText(targetUrl)) {
/* 345 */       Map<String, String> variables = getCurrentRequestUriVariables(request);
/* 346 */       targetUrl = replaceUriTemplateVariables(targetUrl.toString(), model, variables, enc);
/*     */     } 
/* 348 */     if (isPropagateQueryProperties()) {
/* 349 */       appendCurrentQueryParams(targetUrl, request);
/*     */     }
/* 351 */     if (this.exposeModelAttributes) {
/* 352 */       appendQueryProperties(targetUrl, model, enc);
/*     */     }
/*     */     
/* 355 */     return targetUrl.toString();
/*     */   }
/*     */   
/*     */   private String getContextPath(HttpServletRequest request) {
/* 359 */     String contextPath = request.getContextPath();
/* 360 */     while (contextPath.startsWith("//")) {
/* 361 */       contextPath = contextPath.substring(1);
/*     */     }
/* 363 */     return contextPath;
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
/*     */   protected StringBuilder replaceUriTemplateVariables(String targetUrl, Map<String, Object> model, Map<String, String> currentUriVariables, String encodingScheme) throws UnsupportedEncodingException {
/* 380 */     StringBuilder result = new StringBuilder();
/* 381 */     Matcher matcher = URI_TEMPLATE_VARIABLE_PATTERN.matcher(targetUrl);
/* 382 */     int endLastMatch = 0;
/* 383 */     while (matcher.find()) {
/* 384 */       String name = matcher.group(1);
/* 385 */       Object value = model.containsKey(name) ? model.remove(name) : currentUriVariables.get(name);
/* 386 */       if (value == null) {
/* 387 */         throw new IllegalArgumentException("Model has no value for key '" + name + "'");
/*     */       }
/* 389 */       result.append(targetUrl.substring(endLastMatch, matcher.start()));
/* 390 */       result.append(UriUtils.encodePathSegment(value.toString(), encodingScheme));
/* 391 */       endLastMatch = matcher.end();
/*     */     } 
/* 393 */     result.append(targetUrl.substring(endLastMatch));
/* 394 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, String> getCurrentRequestUriVariables(HttpServletRequest request) {
/* 399 */     String name = HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;
/* 400 */     Map<String, String> uriVars = (Map<String, String>)request.getAttribute(name);
/* 401 */     return (uriVars != null) ? uriVars : Collections.<String, String>emptyMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void appendCurrentQueryParams(StringBuilder targetUrl, HttpServletRequest request) {
/* 411 */     String query = request.getQueryString();
/* 412 */     if (StringUtils.hasText(query)) {
/*     */       
/* 414 */       String fragment = null;
/* 415 */       int anchorIndex = targetUrl.indexOf("#");
/* 416 */       if (anchorIndex > -1) {
/* 417 */         fragment = targetUrl.substring(anchorIndex);
/* 418 */         targetUrl.delete(anchorIndex, targetUrl.length());
/*     */       } 
/*     */       
/* 421 */       if (targetUrl.toString().indexOf('?') < 0) {
/* 422 */         targetUrl.append('?').append(query);
/*     */       } else {
/*     */         
/* 425 */         targetUrl.append('&').append(query);
/*     */       } 
/*     */       
/* 428 */       if (fragment != null) {
/* 429 */         targetUrl.append(fragment);
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
/*     */   protected void appendQueryProperties(StringBuilder targetUrl, Map<String, Object> model, String encodingScheme) throws UnsupportedEncodingException {
/* 448 */     String fragment = null;
/* 449 */     int anchorIndex = targetUrl.indexOf("#");
/* 450 */     if (anchorIndex > -1) {
/* 451 */       fragment = targetUrl.substring(anchorIndex);
/* 452 */       targetUrl.delete(anchorIndex, targetUrl.length());
/*     */     } 
/*     */ 
/*     */     
/* 456 */     boolean first = (targetUrl.toString().indexOf('?') < 0);
/* 457 */     for (Map.Entry<String, Object> entry : queryProperties(model).entrySet()) {
/* 458 */       Iterator<Object> valueIter; Object rawValue = entry.getValue();
/*     */       
/* 460 */       if (rawValue != null && rawValue.getClass().isArray()) {
/* 461 */         valueIter = Arrays.<Object>asList(ObjectUtils.toObjectArray(rawValue)).iterator();
/*     */       }
/* 463 */       else if (rawValue instanceof Collection) {
/* 464 */         valueIter = ((Collection<Object>)rawValue).iterator();
/*     */       } else {
/*     */         
/* 467 */         valueIter = Collections.<Object>singleton(rawValue).iterator();
/*     */       } 
/* 469 */       while (valueIter.hasNext()) {
/* 470 */         Object value = valueIter.next();
/* 471 */         if (first) {
/* 472 */           targetUrl.append('?');
/* 473 */           first = false;
/*     */         } else {
/*     */           
/* 476 */           targetUrl.append('&');
/*     */         } 
/* 478 */         String encodedKey = urlEncode(entry.getKey(), encodingScheme);
/* 479 */         String encodedValue = (value != null) ? urlEncode(value.toString(), encodingScheme) : "";
/* 480 */         targetUrl.append(encodedKey).append('=').append(encodedValue);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 485 */     if (fragment != null) {
/* 486 */       targetUrl.append(fragment);
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
/*     */   protected Map<String, Object> queryProperties(Map<String, Object> model) {
/* 501 */     Map<String, Object> result = new LinkedHashMap<>();
/* 502 */     model.forEach((name, value) -> {
/*     */           if (isEligibleProperty(name, value)) {
/*     */             result.put(name, value);
/*     */           }
/*     */         });
/* 507 */     return result;
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
/*     */   protected boolean isEligibleProperty(String key, @Nullable Object value) {
/* 521 */     if (value == null) {
/* 522 */       return false;
/*     */     }
/* 524 */     if (isEligibleValue(value)) {
/* 525 */       return true;
/*     */     }
/* 527 */     if (value.getClass().isArray()) {
/* 528 */       int length = Array.getLength(value);
/* 529 */       if (length == 0) {
/* 530 */         return false;
/*     */       }
/* 532 */       for (int i = 0; i < length; i++) {
/* 533 */         Object element = Array.get(value, i);
/* 534 */         if (!isEligibleValue(element)) {
/* 535 */           return false;
/*     */         }
/*     */       } 
/* 538 */       return true;
/*     */     } 
/* 540 */     if (value instanceof Collection) {
/* 541 */       Collection<?> coll = (Collection)value;
/* 542 */       if (coll.isEmpty()) {
/* 543 */         return false;
/*     */       }
/* 545 */       for (Object element : coll) {
/* 546 */         if (!isEligibleValue(element)) {
/* 547 */           return false;
/*     */         }
/*     */       } 
/* 550 */       return true;
/*     */     } 
/* 552 */     return false;
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
/*     */   protected boolean isEligibleValue(@Nullable Object value) {
/* 565 */     return (value != null && BeanUtils.isSimpleValueType(value.getClass()));
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
/*     */   protected String urlEncode(String input, String encodingScheme) throws UnsupportedEncodingException {
/* 578 */     return URLEncoder.encode(input, encodingScheme);
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
/*     */   protected String updateTargetUrl(String targetUrl, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
/* 590 */     WebApplicationContext wac = getWebApplicationContext();
/* 591 */     if (wac == null) {
/* 592 */       wac = RequestContextUtils.findWebApplicationContext(request, getServletContext());
/*     */     }
/*     */     
/* 595 */     if (wac != null && wac.containsBean("requestDataValueProcessor")) {
/* 596 */       RequestDataValueProcessor processor = (RequestDataValueProcessor)wac.getBean("requestDataValueProcessor", RequestDataValueProcessor.class);
/*     */       
/* 598 */       return processor.processUrl(request, targetUrl);
/*     */     } 
/*     */     
/* 601 */     return targetUrl;
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
/*     */   protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String targetUrl, boolean http10Compatible) throws IOException {
/* 615 */     String encodedURL = isRemoteHost(targetUrl) ? targetUrl : response.encodeRedirectURL(targetUrl);
/* 616 */     if (http10Compatible) {
/* 617 */       HttpStatus attributeStatusCode = (HttpStatus)request.getAttribute(View.RESPONSE_STATUS_ATTRIBUTE);
/* 618 */       if (this.statusCode != null) {
/* 619 */         response.setStatus(this.statusCode.value());
/* 620 */         response.setHeader("Location", encodedURL);
/*     */       }
/* 622 */       else if (attributeStatusCode != null) {
/* 623 */         response.setStatus(attributeStatusCode.value());
/* 624 */         response.setHeader("Location", encodedURL);
/*     */       }
/*     */       else {
/*     */         
/* 628 */         response.sendRedirect(encodedURL);
/*     */       } 
/*     */     } else {
/*     */       
/* 632 */       HttpStatus statusCode = getHttp11StatusCode(request, response, targetUrl);
/* 633 */       response.setStatus(statusCode.value());
/* 634 */       response.setHeader("Location", encodedURL);
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
/*     */   protected boolean isRemoteHost(String targetUrl) {
/* 649 */     if (ObjectUtils.isEmpty((Object[])getHosts())) {
/* 650 */       return false;
/*     */     }
/* 652 */     String targetHost = UriComponentsBuilder.fromUriString(targetUrl).build().getHost();
/* 653 */     if (!StringUtils.hasLength(targetHost)) {
/* 654 */       return false;
/*     */     }
/* 656 */     for (String host : getHosts()) {
/* 657 */       if (targetHost.equals(host)) {
/* 658 */         return false;
/*     */       }
/*     */     } 
/* 661 */     return true;
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
/*     */   protected HttpStatus getHttp11StatusCode(HttpServletRequest request, HttpServletResponse response, String targetUrl) {
/* 677 */     if (this.statusCode != null) {
/* 678 */       return this.statusCode;
/*     */     }
/* 680 */     HttpStatus attributeStatusCode = (HttpStatus)request.getAttribute(View.RESPONSE_STATUS_ATTRIBUTE);
/* 681 */     if (attributeStatusCode != null) {
/* 682 */       return attributeStatusCode;
/*     */     }
/* 684 */     return HttpStatus.SEE_OTHER;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/RedirectView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */