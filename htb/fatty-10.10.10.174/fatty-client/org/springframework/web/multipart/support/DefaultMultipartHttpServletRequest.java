/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultMultipartHttpServletRequest
/*     */   extends AbstractMultipartHttpServletRequest
/*     */ {
/*     */   private static final String CONTENT_TYPE = "Content-Type";
/*     */   @Nullable
/*     */   private Map<String, String[]> multipartParameters;
/*     */   @Nullable
/*     */   private Map<String, String> multipartParameterContentTypes;
/*     */   
/*     */   public DefaultMultipartHttpServletRequest(HttpServletRequest request, MultiValueMap<String, MultipartFile> mpFiles, Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes) {
/*  66 */     super(request);
/*  67 */     setMultipartFiles(mpFiles);
/*  68 */     setMultipartParameters(mpParams);
/*  69 */     setMultipartParameterContentTypes(mpParamContentTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMultipartHttpServletRequest(HttpServletRequest request) {
/*  77 */     super(request);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getParameter(String name) {
/*  84 */     String[] values = getMultipartParameters().get(name);
/*  85 */     if (values != null) {
/*  86 */       return (values.length > 0) ? values[0] : null;
/*     */     }
/*  88 */     return super.getParameter(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getParameterValues(String name) {
/*  93 */     String[] parameterValues = super.getParameterValues(name);
/*  94 */     String[] mpValues = getMultipartParameters().get(name);
/*  95 */     if (mpValues == null) {
/*  96 */       return parameterValues;
/*     */     }
/*  98 */     if (parameterValues == null || getQueryString() == null) {
/*  99 */       return mpValues;
/*     */     }
/*     */     
/* 102 */     String[] result = new String[mpValues.length + parameterValues.length];
/* 103 */     System.arraycopy(mpValues, 0, result, 0, mpValues.length);
/* 104 */     System.arraycopy(parameterValues, 0, result, mpValues.length, parameterValues.length);
/* 105 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<String> getParameterNames() {
/* 111 */     Map<String, String[]> multipartParameters = getMultipartParameters();
/* 112 */     if (multipartParameters.isEmpty()) {
/* 113 */       return super.getParameterNames();
/*     */     }
/*     */     
/* 116 */     Set<String> paramNames = new LinkedHashSet<>();
/* 117 */     paramNames.addAll(Collections.list(super.getParameterNames()));
/* 118 */     paramNames.addAll(multipartParameters.keySet());
/* 119 */     return Collections.enumeration(paramNames);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 124 */     Map<String, String[]> result = (Map)new LinkedHashMap<>();
/* 125 */     Enumeration<String> names = getParameterNames();
/* 126 */     while (names.hasMoreElements()) {
/* 127 */       String name = names.nextElement();
/* 128 */       result.put(name, getParameterValues(name));
/*     */     } 
/* 130 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMultipartContentType(String paramOrFileName) {
/* 135 */     MultipartFile file = getFile(paramOrFileName);
/* 136 */     if (file != null) {
/* 137 */       return file.getContentType();
/*     */     }
/*     */     
/* 140 */     return getMultipartParameterContentTypes().get(paramOrFileName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders getMultipartHeaders(String paramOrFileName) {
/* 146 */     String contentType = getMultipartContentType(paramOrFileName);
/* 147 */     if (contentType != null) {
/* 148 */       HttpHeaders headers = new HttpHeaders();
/* 149 */       headers.add("Content-Type", contentType);
/* 150 */       return headers;
/*     */     } 
/*     */     
/* 153 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setMultipartParameters(Map<String, String[]> multipartParameters) {
/* 163 */     this.multipartParameters = multipartParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, String[]> getMultipartParameters() {
/* 172 */     if (this.multipartParameters == null) {
/* 173 */       initializeMultipart();
/*     */     }
/* 175 */     return this.multipartParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setMultipartParameterContentTypes(Map<String, String> multipartParameterContentTypes) {
/* 183 */     this.multipartParameterContentTypes = multipartParameterContentTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, String> getMultipartParameterContentTypes() {
/* 192 */     if (this.multipartParameterContentTypes == null) {
/* 193 */       initializeMultipart();
/*     */     }
/* 195 */     return this.multipartParameterContentTypes;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/support/DefaultMultipartHttpServletRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */