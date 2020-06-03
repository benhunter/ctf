/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.SmartView;
/*     */ import org.springframework.web.servlet.View;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelAndViewMethodReturnValueHandler
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*     */   @Nullable
/*     */   private String[] redirectPatterns;
/*     */   
/*     */   public void setRedirectPatterns(@Nullable String... redirectPatterns) {
/*  59 */     this.redirectPatterns = redirectPatterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getRedirectPatterns() {
/*  68 */     return this.redirectPatterns;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/*  74 */     return ModelAndView.class.isAssignableFrom(returnType.getParameterType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/*  81 */     if (returnValue == null) {
/*  82 */       mavContainer.setRequestHandled(true);
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     ModelAndView mav = (ModelAndView)returnValue;
/*  87 */     if (mav.isReference()) {
/*  88 */       String viewName = mav.getViewName();
/*  89 */       mavContainer.setViewName(viewName);
/*  90 */       if (viewName != null && isRedirectViewName(viewName)) {
/*  91 */         mavContainer.setRedirectModelScenario(true);
/*     */       }
/*     */     } else {
/*     */       
/*  95 */       View view = mav.getView();
/*  96 */       mavContainer.setView(view);
/*  97 */       if (view instanceof SmartView && ((SmartView)view).isRedirectView()) {
/*  98 */         mavContainer.setRedirectModelScenario(true);
/*     */       }
/*     */     } 
/* 101 */     mavContainer.setStatus(mav.getStatus());
/* 102 */     mavContainer.addAllAttributes(mav.getModel());
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
/*     */   protected boolean isRedirectViewName(String viewName) {
/* 114 */     return (PatternMatchUtils.simpleMatch(this.redirectPatterns, viewName) || viewName.startsWith("redirect:"));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ModelAndViewMethodReturnValueHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */