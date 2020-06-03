/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.Part;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartRequest;
/*     */ import org.springframework.web.multipart.support.MultipartResolutionDelegate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestParamMapMethodArgumentResolver
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/*  64 */     RequestParam requestParam = (RequestParam)parameter.getParameterAnnotation(RequestParam.class);
/*  65 */     return (requestParam != null && Map.class.isAssignableFrom(parameter.getParameterType()) && 
/*  66 */       !StringUtils.hasText(requestParam.name()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/*  73 */     ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
/*     */     
/*  75 */     if (MultiValueMap.class.isAssignableFrom(parameter.getParameterType())) {
/*     */       
/*  77 */       Class<?> clazz = resolvableType.as(MultiValueMap.class).getGeneric(new int[] { 1 }).resolve();
/*  78 */       if (clazz == MultipartFile.class) {
/*  79 */         MultipartRequest multipartRequest = MultipartResolutionDelegate.resolveMultipartRequest(webRequest);
/*  80 */         return (multipartRequest != null) ? multipartRequest.getMultiFileMap() : new LinkedMultiValueMap(0);
/*     */       } 
/*  82 */       if (clazz == Part.class) {
/*  83 */         HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  84 */         if (servletRequest != null && MultipartResolutionDelegate.isMultipartRequest(servletRequest)) {
/*  85 */           Collection<Part> parts = servletRequest.getParts();
/*  86 */           LinkedMultiValueMap<String, Part> linkedMultiValueMap1 = new LinkedMultiValueMap(parts.size());
/*  87 */           for (Part part : parts) {
/*  88 */             linkedMultiValueMap1.add(part.getName(), part);
/*     */           }
/*  90 */           return linkedMultiValueMap1;
/*     */         } 
/*  92 */         return new LinkedMultiValueMap(0);
/*     */       } 
/*     */       
/*  95 */       Map<String, String[]> map = webRequest.getParameterMap();
/*  96 */       LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(map.size());
/*  97 */       map.forEach((key, values) -> {
/*     */             for (String value : values) {
/*     */               result.add(key, value);
/*     */             }
/*     */           });
/* 102 */       return linkedMultiValueMap;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     Class<?> valueType = resolvableType.asMap().getGeneric(new int[] { 1 }).resolve();
/* 109 */     if (valueType == MultipartFile.class) {
/* 110 */       MultipartRequest multipartRequest = MultipartResolutionDelegate.resolveMultipartRequest(webRequest);
/* 111 */       return (multipartRequest != null) ? multipartRequest.getFileMap() : new LinkedHashMap<>(0);
/*     */     } 
/* 113 */     if (valueType == Part.class) {
/* 114 */       HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 115 */       if (servletRequest != null && MultipartResolutionDelegate.isMultipartRequest(servletRequest)) {
/* 116 */         Collection<Part> parts = servletRequest.getParts();
/* 117 */         LinkedHashMap<String, Part> linkedHashMap = new LinkedHashMap<>(parts.size());
/* 118 */         for (Part part : parts) {
/* 119 */           if (!linkedHashMap.containsKey(part.getName())) {
/* 120 */             linkedHashMap.put(part.getName(), part);
/*     */           }
/*     */         } 
/* 123 */         return linkedHashMap;
/*     */       } 
/* 125 */       return new LinkedHashMap<>(0);
/*     */     } 
/*     */     
/* 128 */     Map<String, String[]> parameterMap = webRequest.getParameterMap();
/* 129 */     Map<String, String> result = new LinkedHashMap<>(parameterMap.size());
/* 130 */     parameterMap.forEach((key, values) -> {
/*     */           if (values.length > 0) {
/*     */             result.put(key, values[0]);
/*     */           }
/*     */         });
/* 135 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/RequestParamMapMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */