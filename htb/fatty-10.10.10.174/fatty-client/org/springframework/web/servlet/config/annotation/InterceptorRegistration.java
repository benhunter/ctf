/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.handler.MappedInterceptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InterceptorRegistration
/*     */ {
/*     */   private final HandlerInterceptor interceptor;
/*  41 */   private final List<String> includePatterns = new ArrayList<>();
/*     */   
/*  43 */   private final List<String> excludePatterns = new ArrayList<>();
/*     */   
/*     */   @Nullable
/*     */   private PathMatcher pathMatcher;
/*     */   
/*  48 */   private int order = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InterceptorRegistration(HandlerInterceptor interceptor) {
/*  55 */     Assert.notNull(interceptor, "Interceptor is required");
/*  56 */     this.interceptor = interceptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InterceptorRegistration addPathPatterns(String... patterns) {
/*  64 */     return addPathPatterns(Arrays.asList(patterns));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InterceptorRegistration addPathPatterns(List<String> patterns) {
/*  72 */     this.includePatterns.addAll(patterns);
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InterceptorRegistration excludePathPatterns(String... patterns) {
/*  80 */     return excludePathPatterns(Arrays.asList(patterns));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InterceptorRegistration excludePathPatterns(List<String> patterns) {
/*  88 */     this.excludePatterns.addAll(patterns);
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InterceptorRegistration pathMatcher(PathMatcher pathMatcher) {
/*  99 */     this.pathMatcher = pathMatcher;
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InterceptorRegistration order(int order) {
/* 108 */     this.order = order;
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getOrder() {
/* 116 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInterceptor() {
/* 124 */     if (this.includePatterns.isEmpty() && this.excludePatterns.isEmpty()) {
/* 125 */       return this.interceptor;
/*     */     }
/*     */     
/* 128 */     String[] include = StringUtils.toStringArray(this.includePatterns);
/* 129 */     String[] exclude = StringUtils.toStringArray(this.excludePatterns);
/* 130 */     MappedInterceptor mappedInterceptor = new MappedInterceptor(include, exclude, this.interceptor);
/* 131 */     if (this.pathMatcher != null) {
/* 132 */       mappedInterceptor.setPathMatcher(this.pathMatcher);
/*     */     }
/* 134 */     return mappedInterceptor;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/InterceptorRegistration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */