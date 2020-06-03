/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.security.Principal;
/*     */ import java.time.ZoneId;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.servlet.http.PushBuilder;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.multipart.MultipartRequest;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletRequestMethodArgumentResolver
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*     */   @Nullable
/*     */   private static Class<?> pushBuilder;
/*     */   
/*     */   static {
/*     */     try {
/*  73 */       pushBuilder = ClassUtils.forName("javax.servlet.http.PushBuilder", ServletRequestMethodArgumentResolver.class
/*  74 */           .getClassLoader());
/*     */     }
/*  76 */     catch (ClassNotFoundException ex) {
/*     */       
/*  78 */       pushBuilder = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/*  85 */     Class<?> paramType = parameter.getParameterType();
/*  86 */     return (WebRequest.class.isAssignableFrom(paramType) || ServletRequest.class
/*  87 */       .isAssignableFrom(paramType) || MultipartRequest.class
/*  88 */       .isAssignableFrom(paramType) || HttpSession.class
/*  89 */       .isAssignableFrom(paramType) || (pushBuilder != null && pushBuilder
/*  90 */       .isAssignableFrom(paramType)) || Principal.class
/*  91 */       .isAssignableFrom(paramType) || InputStream.class
/*  92 */       .isAssignableFrom(paramType) || Reader.class
/*  93 */       .isAssignableFrom(paramType) || HttpMethod.class == paramType || Locale.class == paramType || TimeZone.class == paramType || ZoneId.class == paramType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/* 104 */     Class<?> paramType = parameter.getParameterType();
/*     */ 
/*     */     
/* 107 */     if (WebRequest.class.isAssignableFrom(paramType)) {
/* 108 */       if (!paramType.isInstance(webRequest)) {
/* 109 */         throw new IllegalStateException("Current request is not of type [" + paramType
/* 110 */             .getName() + "]: " + webRequest);
/*     */       }
/* 112 */       return webRequest;
/*     */     } 
/*     */ 
/*     */     
/* 116 */     if (ServletRequest.class.isAssignableFrom(paramType) || MultipartRequest.class.isAssignableFrom(paramType)) {
/* 117 */       return resolveNativeRequest(webRequest, paramType);
/*     */     }
/*     */ 
/*     */     
/* 121 */     return resolveArgument(paramType, resolveNativeRequest(webRequest, HttpServletRequest.class));
/*     */   }
/*     */   
/*     */   private <T> T resolveNativeRequest(NativeWebRequest webRequest, Class<T> requiredType) {
/* 125 */     T nativeRequest = (T)webRequest.getNativeRequest(requiredType);
/* 126 */     if (nativeRequest == null) {
/* 127 */       throw new IllegalStateException("Current request is not of type [" + requiredType
/* 128 */           .getName() + "]: " + webRequest);
/*     */     }
/* 130 */     return nativeRequest;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object resolveArgument(Class<?> paramType, HttpServletRequest request) throws IOException {
/* 135 */     if (HttpSession.class.isAssignableFrom(paramType)) {
/* 136 */       HttpSession session = request.getSession();
/* 137 */       if (session != null && !paramType.isInstance(session)) {
/* 138 */         throw new IllegalStateException("Current session is not of type [" + paramType
/* 139 */             .getName() + "]: " + session);
/*     */       }
/* 141 */       return session;
/*     */     } 
/* 143 */     if (pushBuilder != null && pushBuilder.isAssignableFrom(paramType)) {
/* 144 */       return PushBuilderDelegate.resolvePushBuilder(request, paramType);
/*     */     }
/* 146 */     if (InputStream.class.isAssignableFrom(paramType)) {
/* 147 */       ServletInputStream servletInputStream = request.getInputStream();
/* 148 */       if (servletInputStream != null && !paramType.isInstance(servletInputStream)) {
/* 149 */         throw new IllegalStateException("Request input stream is not of type [" + paramType
/* 150 */             .getName() + "]: " + servletInputStream);
/*     */       }
/* 152 */       return servletInputStream;
/*     */     } 
/* 154 */     if (Reader.class.isAssignableFrom(paramType)) {
/* 155 */       Reader reader = request.getReader();
/* 156 */       if (reader != null && !paramType.isInstance(reader)) {
/* 157 */         throw new IllegalStateException("Request body reader is not of type [" + paramType
/* 158 */             .getName() + "]: " + reader);
/*     */       }
/* 160 */       return reader;
/*     */     } 
/* 162 */     if (Principal.class.isAssignableFrom(paramType)) {
/* 163 */       Principal userPrincipal = request.getUserPrincipal();
/* 164 */       if (userPrincipal != null && !paramType.isInstance(userPrincipal)) {
/* 165 */         throw new IllegalStateException("Current user principal is not of type [" + paramType
/* 166 */             .getName() + "]: " + userPrincipal);
/*     */       }
/* 168 */       return userPrincipal;
/*     */     } 
/* 170 */     if (HttpMethod.class == paramType) {
/* 171 */       return HttpMethod.resolve(request.getMethod());
/*     */     }
/* 173 */     if (Locale.class == paramType) {
/* 174 */       return RequestContextUtils.getLocale(request);
/*     */     }
/* 176 */     if (TimeZone.class == paramType) {
/* 177 */       TimeZone timeZone = RequestContextUtils.getTimeZone(request);
/* 178 */       return (timeZone != null) ? timeZone : TimeZone.getDefault();
/*     */     } 
/* 180 */     if (ZoneId.class == paramType) {
/* 181 */       TimeZone timeZone = RequestContextUtils.getTimeZone(request);
/* 182 */       return (timeZone != null) ? timeZone.toZoneId() : ZoneId.systemDefault();
/*     */     } 
/*     */ 
/*     */     
/* 186 */     throw new UnsupportedOperationException("Unknown parameter type: " + paramType.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PushBuilderDelegate
/*     */   {
/*     */     @Nullable
/*     */     public static Object resolvePushBuilder(HttpServletRequest request, Class<?> paramType) {
/* 197 */       PushBuilder pushBuilder = request.newPushBuilder();
/* 198 */       if (pushBuilder != null && !paramType.isInstance(pushBuilder)) {
/* 199 */         throw new IllegalStateException("Current push builder is not of type [" + paramType
/* 200 */             .getName() + "]: " + pushBuilder);
/*     */       }
/* 202 */       return pushBuilder;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ServletRequestMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */