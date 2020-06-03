/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.bind.support.WebArgumentResolver;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractWebArgumentResolverAdapter
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*  52 */   private final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   private final WebArgumentResolver adaptee;
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractWebArgumentResolverAdapter(WebArgumentResolver adaptee) {
/*  61 */     Assert.notNull(adaptee, "'adaptee' must not be null");
/*  62 */     this.adaptee = adaptee;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/*     */     try {
/*  73 */       NativeWebRequest webRequest = getWebRequest();
/*  74 */       Object result = this.adaptee.resolveArgument(parameter, webRequest);
/*  75 */       if (result == WebArgumentResolver.UNRESOLVED) {
/*  76 */         return false;
/*     */       }
/*     */       
/*  79 */       return ClassUtils.isAssignableValue(parameter.getParameterType(), result);
/*     */     
/*     */     }
/*  82 */     catch (Exception ex) {
/*     */       
/*  84 */       if (this.logger.isDebugEnabled()) {
/*  85 */         this.logger.debug("Error in checking support for parameter [" + parameter + "]: " + ex.getMessage());
/*     */       }
/*  87 */       return false;
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
/*     */   @Nullable
/*     */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/* 101 */     Class<?> paramType = parameter.getParameterType();
/* 102 */     Object result = this.adaptee.resolveArgument(parameter, webRequest);
/* 103 */     if (result == WebArgumentResolver.UNRESOLVED || !ClassUtils.isAssignableValue(paramType, result)) {
/* 104 */       throw new IllegalStateException("Standard argument type [" + paramType
/* 105 */           .getName() + "] in method " + parameter.getMethod() + "resolved to incompatible value of type [" + ((result != null) ? result
/* 106 */           .getClass() : null) + "]. Consider declaring the argument type in a less specific fashion.");
/*     */     }
/*     */     
/* 109 */     return result;
/*     */   }
/*     */   
/*     */   protected abstract NativeWebRequest getWebRequest();
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/AbstractWebArgumentResolverAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */