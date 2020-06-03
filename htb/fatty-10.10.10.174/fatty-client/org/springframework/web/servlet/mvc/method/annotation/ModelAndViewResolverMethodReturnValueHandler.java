/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.ExtendedModelMap;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
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
/*     */ public class ModelAndViewResolverMethodReturnValueHandler
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*     */   @Nullable
/*     */   private final List<ModelAndViewResolver> mavResolvers;
/*  61 */   private final ModelAttributeMethodProcessor modelAttributeProcessor = new ModelAttributeMethodProcessor(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewResolverMethodReturnValueHandler(@Nullable List<ModelAndViewResolver> mavResolvers) {
/*  68 */     this.mavResolvers = mavResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/*  84 */     if (this.mavResolvers != null) {
/*  85 */       for (ModelAndViewResolver mavResolver : this.mavResolvers) {
/*  86 */         Class<?> handlerType = returnType.getContainingClass();
/*  87 */         Method method = returnType.getMethod();
/*  88 */         Assert.state((method != null), "No handler method");
/*  89 */         ExtendedModelMap model = (ExtendedModelMap)mavContainer.getModel();
/*  90 */         ModelAndView mav = mavResolver.resolveModelAndView(method, handlerType, returnValue, model, webRequest);
/*  91 */         if (mav != ModelAndViewResolver.UNRESOLVED) {
/*  92 */           mavContainer.addAllAttributes(mav.getModel());
/*  93 */           mavContainer.setViewName(mav.getViewName());
/*  94 */           if (!mav.isReference()) {
/*  95 */             mavContainer.setView(mav.getView());
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 103 */     if (this.modelAttributeProcessor.supportsReturnType(returnType)) {
/* 104 */       this.modelAttributeProcessor.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
/*     */     } else {
/*     */       
/* 107 */       throw new UnsupportedOperationException("Unexpected return type: " + returnType
/* 108 */           .getParameterType().getName() + " in method: " + returnType.getMethod());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ModelAndViewResolverMethodReturnValueHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */