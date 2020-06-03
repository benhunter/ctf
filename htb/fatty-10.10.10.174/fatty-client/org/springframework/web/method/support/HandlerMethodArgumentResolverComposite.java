/*     */ package org.springframework.web.method.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HandlerMethodArgumentResolverComposite
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*     */   @Deprecated
/*  45 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  47 */   private final List<HandlerMethodArgumentResolver> argumentResolvers = new LinkedList<>();
/*     */   
/*  49 */   private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver resolver) {
/*  57 */     this.argumentResolvers.add(resolver);
/*  58 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodArgumentResolverComposite addResolvers(@Nullable HandlerMethodArgumentResolver... resolvers) {
/*  68 */     if (resolvers != null) {
/*  69 */       Collections.addAll(this.argumentResolvers, resolvers);
/*     */     }
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodArgumentResolverComposite addResolvers(@Nullable List<? extends HandlerMethodArgumentResolver> resolvers) {
/*  80 */     if (resolvers != null) {
/*  81 */       this.argumentResolvers.addAll(resolvers);
/*     */     }
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HandlerMethodArgumentResolver> getResolvers() {
/*  90 */     return Collections.unmodifiableList(this.argumentResolvers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  98 */     this.argumentResolvers.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/* 108 */     return (getArgumentResolver(parameter) != null);
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
/*     */   @Nullable
/*     */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/* 122 */     HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
/* 123 */     if (resolver == null) {
/* 124 */       throw new IllegalArgumentException("Unsupported parameter type [" + parameter
/* 125 */           .getParameterType().getName() + "]. supportsParameter should be called first.");
/*     */     }
/* 127 */     return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
/* 136 */     HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
/* 137 */     if (result == null) {
/* 138 */       for (HandlerMethodArgumentResolver resolver : this.argumentResolvers) {
/* 139 */         if (resolver.supportsParameter(parameter)) {
/* 140 */           result = resolver;
/* 141 */           this.argumentResolverCache.put(parameter, result);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 146 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/support/HandlerMethodArgumentResolverComposite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */