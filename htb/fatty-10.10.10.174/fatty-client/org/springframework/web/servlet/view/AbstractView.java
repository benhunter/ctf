/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.support.ContextExposingHttpServletRequest;
/*     */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractView
/*     */   extends WebApplicationObjectSupport
/*     */   implements View, BeanNameAware
/*     */ {
/*     */   public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=ISO-8859-1";
/*     */   private static final int OUTPUT_BYTE_ARRAY_INITIAL_SIZE = 4096;
/*     */   @Nullable
/*  72 */   private String contentType = "text/html;charset=ISO-8859-1";
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String requestContextAttribute;
/*     */   
/*  78 */   private final Map<String, Object> staticAttributes = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean exposePathVariables = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean exposeContextBeansAsAttributes = false;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Set<String> exposedContextBeanNames;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String beanName;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentType(@Nullable String contentType) {
/*  99 */     this.contentType = contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getContentType() {
/* 108 */     return this.contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequestContextAttribute(@Nullable String requestContextAttribute) {
/* 116 */     this.requestContextAttribute = requestContextAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getRequestContextAttribute() {
/* 124 */     return this.requestContextAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributesCSV(@Nullable String propString) throws IllegalArgumentException {
/* 135 */     if (propString != null) {
/* 136 */       StringTokenizer st = new StringTokenizer(propString, ",");
/* 137 */       while (st.hasMoreTokens()) {
/* 138 */         String tok = st.nextToken();
/* 139 */         int eqIdx = tok.indexOf('=');
/* 140 */         if (eqIdx == -1) {
/* 141 */           throw new IllegalArgumentException("Expected '=' in attributes CSV string '" + propString + "'");
/*     */         }
/*     */         
/* 144 */         if (eqIdx >= tok.length() - 2) {
/* 145 */           throw new IllegalArgumentException("At least 2 characters ([]) required in attributes CSV string '" + propString + "'");
/*     */         }
/*     */         
/* 148 */         String name = tok.substring(0, eqIdx);
/* 149 */         String value = tok.substring(eqIdx + 1);
/*     */ 
/*     */         
/* 152 */         value = value.substring(1);
/* 153 */         value = value.substring(0, value.length() - 1);
/*     */         
/* 155 */         addStaticAttribute(name, value);
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
/*     */   public void setAttributes(Properties attributes) {
/* 174 */     CollectionUtils.mergePropertiesIntoMap(attributes, this.staticAttributes);
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
/*     */   public void setAttributesMap(@Nullable Map<String, ?> attributes) {
/* 187 */     if (attributes != null) {
/* 188 */       attributes.forEach(this::addStaticAttribute);
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
/*     */   public Map<String, Object> getAttributesMap() {
/* 200 */     return this.staticAttributes;
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
/*     */   public void addStaticAttribute(String name, Object value) {
/* 214 */     this.staticAttributes.put(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getStaticAttributes() {
/* 224 */     return Collections.unmodifiableMap(this.staticAttributes);
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
/*     */   public void setExposePathVariables(boolean exposePathVariables) {
/* 239 */     this.exposePathVariables = exposePathVariables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExposePathVariables() {
/* 246 */     return this.exposePathVariables;
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
/*     */   public void setExposeContextBeansAsAttributes(boolean exposeContextBeansAsAttributes) {
/* 264 */     this.exposeContextBeansAsAttributes = exposeContextBeansAsAttributes;
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
/*     */   public void setExposedContextBeanNames(String... exposedContextBeanNames) {
/* 276 */     this.exposedContextBeanNames = new HashSet<>(Arrays.asList(exposedContextBeanNames));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanName(@Nullable String beanName) {
/* 285 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getBeanName() {
/* 294 */     return this.beanName;
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
/*     */   public void render(@Nullable Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 308 */     if (this.logger.isDebugEnabled()) {
/* 309 */       this.logger.debug("View " + formatViewName() + ", model " + ((model != null) ? model : 
/* 310 */           Collections.emptyMap()) + (
/* 311 */           this.staticAttributes.isEmpty() ? "" : (", static attributes " + this.staticAttributes)));
/*     */     }
/*     */     
/* 314 */     Map<String, Object> mergedModel = createMergedOutputModel(model, request, response);
/* 315 */     prepareResponse(request, response);
/* 316 */     renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
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
/*     */   protected Map<String, Object> createMergedOutputModel(@Nullable Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
/* 328 */     Map<String, Object> pathVars = this.exposePathVariables ? (Map<String, Object>)request.getAttribute(View.PATH_VARIABLES) : null;
/*     */ 
/*     */     
/* 331 */     int size = this.staticAttributes.size();
/* 332 */     size += (model != null) ? model.size() : 0;
/* 333 */     size += (pathVars != null) ? pathVars.size() : 0;
/*     */     
/* 335 */     Map<String, Object> mergedModel = new LinkedHashMap<>(size);
/* 336 */     mergedModel.putAll(this.staticAttributes);
/* 337 */     if (pathVars != null) {
/* 338 */       mergedModel.putAll(pathVars);
/*     */     }
/* 340 */     if (model != null) {
/* 341 */       mergedModel.putAll(model);
/*     */     }
/*     */ 
/*     */     
/* 345 */     if (this.requestContextAttribute != null) {
/* 346 */       mergedModel.put(this.requestContextAttribute, createRequestContext(request, response, mergedModel));
/*     */     }
/*     */     
/* 349 */     return mergedModel;
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
/*     */   protected RequestContext createRequestContext(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
/* 366 */     return new RequestContext(request, response, getServletContext(), model);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
/* 377 */     if (generatesDownloadContent()) {
/* 378 */       response.setHeader("Pragma", "private");
/* 379 */       response.setHeader("Cache-Control", "private, must-revalidate");
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
/*     */   protected boolean generatesDownloadContent() {
/* 394 */     return false;
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
/*     */   protected HttpServletRequest getRequestToExpose(HttpServletRequest originalRequest) {
/* 408 */     if (this.exposeContextBeansAsAttributes || this.exposedContextBeanNames != null) {
/* 409 */       WebApplicationContext wac = getWebApplicationContext();
/* 410 */       Assert.state((wac != null), "No WebApplicationContext");
/* 411 */       return (HttpServletRequest)new ContextExposingHttpServletRequest(originalRequest, wac, this.exposedContextBeanNames);
/*     */     } 
/* 413 */     return originalRequest;
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
/*     */   
/*     */   protected void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request) throws Exception {
/* 442 */     model.forEach((name, value) -> {
/*     */           if (value != null) {
/*     */             request.setAttribute(name, value);
/*     */           } else {
/*     */             request.removeAttribute(name);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteArrayOutputStream createTemporaryOutputStream() {
/* 458 */     return new ByteArrayOutputStream(4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeToResponse(HttpServletResponse response, ByteArrayOutputStream baos) throws IOException {
/* 469 */     response.setContentType(getContentType());
/* 470 */     response.setContentLength(baos.size());
/*     */ 
/*     */     
/* 473 */     ServletOutputStream out = response.getOutputStream();
/* 474 */     baos.writeTo((OutputStream)out);
/* 475 */     out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setResponseContentType(HttpServletRequest request, HttpServletResponse response) {
/* 485 */     MediaType mediaType = (MediaType)request.getAttribute(View.SELECTED_CONTENT_TYPE);
/* 486 */     if (mediaType != null && mediaType.isConcrete()) {
/* 487 */       response.setContentType(mediaType.toString());
/*     */     } else {
/*     */       
/* 490 */       response.setContentType(getContentType());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 496 */     return getClass().getName() + ": " + formatViewName();
/*     */   }
/*     */   
/*     */   protected String formatViewName() {
/* 500 */     return (getBeanName() != null) ? ("name '" + getBeanName() + "'") : ("[" + getClass().getSimpleName() + "]");
/*     */   }
/*     */   
/*     */   protected abstract void renderMergedOutputModel(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws Exception;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/AbstractView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */