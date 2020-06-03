/*    */ package org.springframework.web.bind.support;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.bind.WebDataBinder;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.context.request.WebRequest;
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
/*    */ public class DefaultDataBinderFactory
/*    */   implements WebDataBinderFactory
/*    */ {
/*    */   @Nullable
/*    */   private final WebBindingInitializer initializer;
/*    */   
/*    */   public DefaultDataBinderFactory(@Nullable WebBindingInitializer initializer) {
/* 42 */     this.initializer = initializer;
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
/*    */   
/*    */   public final WebDataBinder createBinder(NativeWebRequest webRequest, @Nullable Object target, String objectName) throws Exception {
/* 56 */     WebDataBinder dataBinder = createBinderInstance(target, objectName, webRequest);
/* 57 */     if (this.initializer != null) {
/* 58 */       this.initializer.initBinder(dataBinder, (WebRequest)webRequest);
/*    */     }
/* 60 */     initBinder(dataBinder, webRequest);
/* 61 */     return dataBinder;
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
/*    */   
/*    */   protected WebDataBinder createBinderInstance(@Nullable Object target, String objectName, NativeWebRequest webRequest) throws Exception {
/* 75 */     return new WebRequestDataBinder(target, objectName);
/*    */   }
/*    */   
/*    */   protected void initBinder(WebDataBinder dataBinder, NativeWebRequest webRequest) throws Exception {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/support/DefaultDataBinderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */