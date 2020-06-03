/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.Part;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*     */ import org.springframework.web.multipart.MultipartRequest;
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
/*     */ public abstract class MultipartResolutionDelegate
/*     */ {
/*  46 */   public static final Object UNRESOLVABLE = new Object();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static MultipartRequest resolveMultipartRequest(NativeWebRequest webRequest) {
/*  51 */     MultipartRequest multipartRequest = (MultipartRequest)webRequest.getNativeRequest(MultipartRequest.class);
/*  52 */     if (multipartRequest != null) {
/*  53 */       return multipartRequest;
/*     */     }
/*  55 */     HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  56 */     if (servletRequest != null && isMultipartContent(servletRequest)) {
/*  57 */       return (MultipartRequest)new StandardMultipartHttpServletRequest(servletRequest);
/*     */     }
/*  59 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean isMultipartRequest(HttpServletRequest request) {
/*  63 */     return (WebUtils.getNativeRequest((ServletRequest)request, MultipartHttpServletRequest.class) != null || 
/*  64 */       isMultipartContent(request));
/*     */   }
/*     */   
/*     */   private static boolean isMultipartContent(HttpServletRequest request) {
/*  68 */     String contentType = request.getContentType();
/*  69 */     return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
/*     */   }
/*     */   
/*     */   static MultipartHttpServletRequest asMultipartHttpServletRequest(HttpServletRequest request) {
/*  73 */     MultipartHttpServletRequest unwrapped = (MultipartHttpServletRequest)WebUtils.getNativeRequest((ServletRequest)request, MultipartHttpServletRequest.class);
/*  74 */     if (unwrapped != null) {
/*  75 */       return unwrapped;
/*     */     }
/*  77 */     return new StandardMultipartHttpServletRequest(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isMultipartArgument(MethodParameter parameter) {
/*  82 */     Class<?> paramType = parameter.getNestedParameterType();
/*  83 */     return (MultipartFile.class == paramType || 
/*  84 */       isMultipartFileCollection(parameter) || isMultipartFileArray(parameter) || Part.class == paramType || 
/*  85 */       isPartCollection(parameter) || isPartArray(parameter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Object resolveMultipartArgument(String name, MethodParameter parameter, HttpServletRequest request) throws Exception {
/*  93 */     MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)WebUtils.getNativeRequest((ServletRequest)request, MultipartHttpServletRequest.class);
/*  94 */     boolean isMultipart = (multipartRequest != null || isMultipartContent(request));
/*     */     
/*  96 */     if (MultipartFile.class == parameter.getNestedParameterType()) {
/*  97 */       if (multipartRequest == null && isMultipart) {
/*  98 */         multipartRequest = new StandardMultipartHttpServletRequest(request);
/*     */       }
/* 100 */       return (multipartRequest != null) ? multipartRequest.getFile(name) : null;
/*     */     } 
/* 102 */     if (isMultipartFileCollection(parameter)) {
/* 103 */       if (multipartRequest == null && isMultipart) {
/* 104 */         multipartRequest = new StandardMultipartHttpServletRequest(request);
/*     */       }
/* 106 */       return (multipartRequest != null) ? multipartRequest.getFiles(name) : null;
/*     */     } 
/* 108 */     if (isMultipartFileArray(parameter)) {
/* 109 */       if (multipartRequest == null && isMultipart) {
/* 110 */         multipartRequest = new StandardMultipartHttpServletRequest(request);
/*     */       }
/* 112 */       if (multipartRequest != null) {
/* 113 */         List<MultipartFile> multipartFiles = multipartRequest.getFiles(name);
/* 114 */         return multipartFiles.toArray(new MultipartFile[0]);
/*     */       } 
/*     */       
/* 117 */       return null;
/*     */     } 
/*     */     
/* 120 */     if (Part.class == parameter.getNestedParameterType()) {
/* 121 */       return isMultipart ? request.getPart(name) : null;
/*     */     }
/* 123 */     if (isPartCollection(parameter)) {
/* 124 */       return isMultipart ? resolvePartList(request, name) : null;
/*     */     }
/* 126 */     if (isPartArray(parameter)) {
/* 127 */       return isMultipart ? resolvePartList(request, name).<Part>toArray(new Part[0]) : null;
/*     */     }
/*     */     
/* 130 */     return UNRESOLVABLE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isMultipartFileCollection(MethodParameter methodParam) {
/* 135 */     return (MultipartFile.class == getCollectionParameterType(methodParam));
/*     */   }
/*     */   
/*     */   private static boolean isMultipartFileArray(MethodParameter methodParam) {
/* 139 */     return (MultipartFile.class == methodParam.getNestedParameterType().getComponentType());
/*     */   }
/*     */   
/*     */   private static boolean isPartCollection(MethodParameter methodParam) {
/* 143 */     return (Part.class == getCollectionParameterType(methodParam));
/*     */   }
/*     */   
/*     */   private static boolean isPartArray(MethodParameter methodParam) {
/* 147 */     return (Part.class == methodParam.getNestedParameterType().getComponentType());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Class<?> getCollectionParameterType(MethodParameter methodParam) {
/* 152 */     Class<?> paramType = methodParam.getNestedParameterType();
/* 153 */     if (Collection.class == paramType || List.class.isAssignableFrom(paramType)) {
/* 154 */       Class<?> valueType = ResolvableType.forMethodParameter(methodParam).asCollection().resolveGeneric(new int[0]);
/* 155 */       if (valueType != null) {
/* 156 */         return valueType;
/*     */       }
/*     */     } 
/* 159 */     return null;
/*     */   }
/*     */   
/*     */   private static List<Part> resolvePartList(HttpServletRequest request, String name) throws Exception {
/* 163 */     Collection<Part> parts = request.getParts();
/* 164 */     List<Part> result = new ArrayList<>(parts.size());
/* 165 */     for (Part part : parts) {
/* 166 */       if (part.getName().equals(name)) {
/* 167 */         result.add(part);
/*     */       }
/*     */     } 
/* 170 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/support/MultipartResolutionDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */