/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ViewNameMethodReturnValueHandler
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
/*     */   @Nullable
/*     */   public String[] getRedirectPatterns() {
/*  67 */     return this.redirectPatterns;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/*  73 */     Class<?> paramType = returnType.getParameterType();
/*  74 */     return (void.class == paramType || CharSequence.class.isAssignableFrom(paramType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/*  81 */     if (returnValue instanceof CharSequence) {
/*  82 */       String viewName = returnValue.toString();
/*  83 */       mavContainer.setViewName(viewName);
/*  84 */       if (isRedirectViewName(viewName)) {
/*  85 */         mavContainer.setRedirectModelScenario(true);
/*     */       }
/*     */     }
/*  88 */     else if (returnValue != null) {
/*     */       
/*  90 */       throw new UnsupportedOperationException("Unexpected return type: " + returnType
/*  91 */           .getParameterType().getName() + " in method: " + returnType.getMethod());
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
/*     */   protected boolean isRedirectViewName(String viewName) {
/* 104 */     return (PatternMatchUtils.simpleMatch(this.redirectPatterns, viewName) || viewName.startsWith("redirect:"));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ViewNameMethodReturnValueHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */