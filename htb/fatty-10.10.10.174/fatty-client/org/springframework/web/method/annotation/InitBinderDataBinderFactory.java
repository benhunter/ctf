/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ import org.springframework.web.bind.WebDataBinder;
/*    */ import org.springframework.web.bind.annotation.InitBinder;
/*    */ import org.springframework.web.bind.support.DefaultDataBinderFactory;
/*    */ import org.springframework.web.bind.support.WebBindingInitializer;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.HandlerMethod;
/*    */ import org.springframework.web.method.support.InvocableHandlerMethod;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InitBinderDataBinderFactory
/*    */   extends DefaultDataBinderFactory
/*    */ {
/*    */   private final List<InvocableHandlerMethod> binderMethods;
/*    */   
/*    */   public InitBinderDataBinderFactory(@Nullable List<InvocableHandlerMethod> binderMethods, @Nullable WebBindingInitializer initializer) {
/* 52 */     super(initializer);
/* 53 */     this.binderMethods = (binderMethods != null) ? binderMethods : Collections.<InvocableHandlerMethod>emptyList();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initBinder(WebDataBinder dataBinder, NativeWebRequest request) throws Exception {
/* 66 */     for (InvocableHandlerMethod binderMethod : this.binderMethods) {
/* 67 */       if (isBinderMethodApplicable((HandlerMethod)binderMethod, dataBinder)) {
/* 68 */         Object returnValue = binderMethod.invokeForRequest(request, null, new Object[] { dataBinder });
/* 69 */         if (returnValue != null) {
/* 70 */           throw new IllegalStateException("@InitBinder methods must not return a value (should be void): " + binderMethod);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isBinderMethodApplicable(HandlerMethod initBinderMethod, WebDataBinder dataBinder) {
/* 83 */     InitBinder ann = (InitBinder)initBinderMethod.getMethodAnnotation(InitBinder.class);
/* 84 */     Assert.state((ann != null), "No InitBinder annotation");
/* 85 */     String[] names = ann.value();
/* 86 */     return (ObjectUtils.isEmpty((Object[])names) || ObjectUtils.containsElement((Object[])names, dataBinder.getObjectName()));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/InitBinderDataBinderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */