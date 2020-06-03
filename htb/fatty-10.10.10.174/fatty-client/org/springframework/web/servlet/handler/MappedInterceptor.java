/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.context.request.WebRequestInterceptor;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MappedInterceptor
/*     */   implements HandlerInterceptor
/*     */ {
/*     */   @Nullable
/*     */   private final String[] includePatterns;
/*     */   @Nullable
/*     */   private final String[] excludePatterns;
/*     */   private final HandlerInterceptor interceptor;
/*     */   @Nullable
/*     */   private PathMatcher pathMatcher;
/*     */   
/*     */   public MappedInterceptor(@Nullable String[] includePatterns, HandlerInterceptor interceptor) {
/*  65 */     this(includePatterns, (String[])null, interceptor);
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
/*     */   public MappedInterceptor(@Nullable String[] includePatterns, @Nullable String[] excludePatterns, HandlerInterceptor interceptor) {
/*  77 */     this.includePatterns = includePatterns;
/*  78 */     this.excludePatterns = excludePatterns;
/*  79 */     this.interceptor = interceptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappedInterceptor(@Nullable String[] includePatterns, WebRequestInterceptor interceptor) {
/*  89 */     this(includePatterns, (String[])null, interceptor);
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
/*     */   public MappedInterceptor(@Nullable String[] includePatterns, @Nullable String[] excludePatterns, WebRequestInterceptor interceptor) {
/* 101 */     this(includePatterns, excludePatterns, (HandlerInterceptor)new WebRequestHandlerInterceptorAdapter(interceptor));
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
/*     */   public void setPathMatcher(@Nullable PathMatcher pathMatcher) {
/* 113 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PathMatcher getPathMatcher() {
/* 121 */     return this.pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getPathPatterns() {
/* 129 */     return this.includePatterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerInterceptor getInterceptor() {
/* 136 */     return this.interceptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(String lookupPath, PathMatcher pathMatcher) {
/* 147 */     PathMatcher pathMatcherToUse = (this.pathMatcher != null) ? this.pathMatcher : pathMatcher;
/* 148 */     if (!ObjectUtils.isEmpty((Object[])this.excludePatterns)) {
/* 149 */       for (String pattern : this.excludePatterns) {
/* 150 */         if (pathMatcherToUse.match(pattern, lookupPath)) {
/* 151 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/* 155 */     if (ObjectUtils.isEmpty((Object[])this.includePatterns)) {
/* 156 */       return true;
/*     */     }
/* 158 */     for (String pattern : this.includePatterns) {
/* 159 */       if (pathMatcherToUse.match(pattern, lookupPath)) {
/* 160 */         return true;
/*     */       }
/*     */     } 
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
/* 170 */     return this.interceptor.preHandle(request, response, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
/* 177 */     this.interceptor.postHandle(request, response, handler, modelAndView);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
/* 184 */     this.interceptor.afterCompletion(request, response, handler, ex);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/MappedInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */