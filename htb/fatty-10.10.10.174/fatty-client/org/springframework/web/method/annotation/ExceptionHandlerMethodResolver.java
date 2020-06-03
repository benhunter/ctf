/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.ExceptionDepthComparator;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExceptionHandlerMethodResolver
/*     */ {
/*     */   public static final ReflectionUtils.MethodFilter EXCEPTION_HANDLER_METHODS;
/*     */   
/*     */   static {
/*  49 */     EXCEPTION_HANDLER_METHODS = (method -> AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class));
/*     */   }
/*     */ 
/*     */   
/*  53 */   private final Map<Class<? extends Throwable>, Method> mappedMethods = new HashMap<>(16);
/*     */   
/*  55 */   private final Map<Class<? extends Throwable>, Method> exceptionLookupCache = (Map<Class<? extends Throwable>, Method>)new ConcurrentReferenceHashMap(16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionHandlerMethodResolver(Class<?> handlerType) {
/*  63 */     for (Method method : MethodIntrospector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS)) {
/*  64 */       for (Class<? extends Throwable> exceptionType : detectExceptionMappings(method)) {
/*  65 */         addExceptionMapping(exceptionType, method);
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
/*     */   private List<Class<? extends Throwable>> detectExceptionMappings(Method method) {
/*  77 */     List<Class<? extends Throwable>> result = new ArrayList<>();
/*  78 */     detectAnnotationExceptionMappings(method, result);
/*  79 */     if (result.isEmpty()) {
/*  80 */       for (Class<?> paramType : method.getParameterTypes()) {
/*  81 */         if (Throwable.class.isAssignableFrom(paramType)) {
/*  82 */           result.add(paramType);
/*     */         }
/*     */       } 
/*     */     }
/*  86 */     if (result.isEmpty()) {
/*  87 */       throw new IllegalStateException("No exception types mapped to " + method);
/*     */     }
/*  89 */     return result;
/*     */   }
/*     */   
/*     */   private void detectAnnotationExceptionMappings(Method method, List<Class<? extends Throwable>> result) {
/*  93 */     ExceptionHandler ann = (ExceptionHandler)AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
/*  94 */     Assert.state((ann != null), "No ExceptionHandler annotation");
/*  95 */     result.addAll(Arrays.asList((Class<? extends Throwable>[])ann.value()));
/*     */   }
/*     */   
/*     */   private void addExceptionMapping(Class<? extends Throwable> exceptionType, Method method) {
/*  99 */     Method oldMethod = this.mappedMethods.put(exceptionType, method);
/* 100 */     if (oldMethod != null && !oldMethod.equals(method)) {
/* 101 */       throw new IllegalStateException("Ambiguous @ExceptionHandler method mapped for [" + exceptionType + "]: {" + oldMethod + ", " + method + "}");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasExceptionMappings() {
/* 110 */     return !this.mappedMethods.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method resolveMethod(Exception exception) {
/* 121 */     return resolveMethodByThrowable(exception);
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
/*     */   public Method resolveMethodByThrowable(Throwable exception) {
/* 133 */     Method method = resolveMethodByExceptionType((Class)exception.getClass());
/* 134 */     if (method == null) {
/* 135 */       Throwable cause = exception.getCause();
/* 136 */       if (cause != null) {
/* 137 */         method = resolveMethodByExceptionType((Class)cause.getClass());
/*     */       }
/*     */     } 
/* 140 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method resolveMethodByExceptionType(Class<? extends Throwable> exceptionType) {
/* 151 */     Method method = this.exceptionLookupCache.get(exceptionType);
/* 152 */     if (method == null) {
/* 153 */       method = getMappedMethod(exceptionType);
/* 154 */       this.exceptionLookupCache.put(exceptionType, method);
/*     */     } 
/* 156 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Method getMappedMethod(Class<? extends Throwable> exceptionType) {
/* 164 */     List<Class<? extends Throwable>> matches = new ArrayList<>();
/* 165 */     for (Class<? extends Throwable> mappedException : this.mappedMethods.keySet()) {
/* 166 */       if (mappedException.isAssignableFrom(exceptionType)) {
/* 167 */         matches.add(mappedException);
/*     */       }
/*     */     } 
/* 170 */     if (!matches.isEmpty()) {
/* 171 */       matches.sort((Comparator<? super Class<? extends Throwable>>)new ExceptionDepthComparator(exceptionType));
/* 172 */       return this.mappedMethods.get(matches.get(0));
/*     */     } 
/*     */     
/* 175 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/ExceptionHandlerMethodResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */