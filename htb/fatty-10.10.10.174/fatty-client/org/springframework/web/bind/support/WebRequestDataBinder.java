/*     */ package org.springframework.web.bind.support;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.Part;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebRequestDataBinder
/*     */   extends WebDataBinder
/*     */ {
/*     */   public WebRequestDataBinder(@Nullable Object target) {
/*  78 */     super(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebRequestDataBinder(@Nullable Object target, String objectName) {
/*  88 */     super(target, objectName);
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
/*     */   public void bind(WebRequest request) {
/* 111 */     MutablePropertyValues mpvs = new MutablePropertyValues(request.getParameterMap());
/* 112 */     if (isMultipartRequest(request) && request instanceof NativeWebRequest) {
/* 113 */       MultipartRequest multipartRequest = (MultipartRequest)((NativeWebRequest)request).getNativeRequest(MultipartRequest.class);
/* 114 */       if (multipartRequest != null) {
/* 115 */         bindMultipart((Map)multipartRequest.getMultiFileMap(), mpvs);
/*     */       } else {
/*     */         
/* 118 */         HttpServletRequest servletRequest = (HttpServletRequest)((NativeWebRequest)request).getNativeRequest(HttpServletRequest.class);
/* 119 */         if (servletRequest != null) {
/* 120 */           bindParts(servletRequest, mpvs);
/*     */         }
/*     */       } 
/*     */     } 
/* 124 */     doBind(mpvs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isMultipartRequest(WebRequest request) {
/* 132 */     String contentType = request.getHeader("Content-Type");
/* 133 */     return (contentType != null && StringUtils.startsWithIgnoreCase(contentType, "multipart"));
/*     */   }
/*     */   
/*     */   private void bindParts(HttpServletRequest request, MutablePropertyValues mpvs) {
/*     */     try {
/* 138 */       LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 139 */       for (Part part : request.getParts()) {
/* 140 */         linkedMultiValueMap.add(part.getName(), part);
/*     */       }
/* 142 */       linkedMultiValueMap.forEach((key, values) -> {
/*     */             if (values.size() == 1) {
/*     */               Part part = values.get(0);
/*     */ 
/*     */               
/*     */               if (isBindEmptyMultipartFiles() || part.getSize() > 0L) {
/*     */                 mpvs.add(key, part);
/*     */               }
/*     */             } else {
/*     */               mpvs.add(key, values);
/*     */             } 
/*     */           });
/* 154 */     } catch (Exception ex) {
/* 155 */       throw new MultipartException("Failed to get request parts", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeNoCatch() throws BindException {
/* 166 */     if (getBindingResult().hasErrors())
/* 167 */       throw new BindException(getBindingResult()); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/support/WebRequestDataBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */