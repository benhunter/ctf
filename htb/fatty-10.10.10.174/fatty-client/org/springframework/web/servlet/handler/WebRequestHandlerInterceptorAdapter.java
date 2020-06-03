/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.context.request.AsyncWebRequestInterceptor;
/*    */ import org.springframework.web.context.request.WebRequest;
/*    */ import org.springframework.web.context.request.WebRequestInterceptor;
/*    */ import org.springframework.web.servlet.AsyncHandlerInterceptor;
/*    */ import org.springframework.web.servlet.ModelAndView;
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
/*    */ public class WebRequestHandlerInterceptorAdapter
/*    */   implements AsyncHandlerInterceptor
/*    */ {
/*    */   private final WebRequestInterceptor requestInterceptor;
/*    */   
/*    */   public WebRequestHandlerInterceptorAdapter(WebRequestInterceptor requestInterceptor) {
/* 48 */     Assert.notNull(requestInterceptor, "WebRequestInterceptor must not be null");
/* 49 */     this.requestInterceptor = requestInterceptor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
/* 57 */     this.requestInterceptor.preHandle((WebRequest)new DispatcherServletWebRequest(request, response));
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
/* 65 */     this.requestInterceptor.postHandle((WebRequest)new DispatcherServletWebRequest(request, response), (modelAndView != null && 
/* 66 */         !modelAndView.wasCleared()) ? modelAndView.getModelMap() : null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
/* 73 */     this.requestInterceptor.afterCompletion((WebRequest)new DispatcherServletWebRequest(request, response), ex);
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) {
/* 78 */     if (this.requestInterceptor instanceof AsyncWebRequestInterceptor) {
/* 79 */       AsyncWebRequestInterceptor asyncInterceptor = (AsyncWebRequestInterceptor)this.requestInterceptor;
/* 80 */       DispatcherServletWebRequest webRequest = new DispatcherServletWebRequest(request, response);
/* 81 */       asyncInterceptor.afterConcurrentHandlingStarted((WebRequest)webRequest);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/WebRequestHandlerInterceptorAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */