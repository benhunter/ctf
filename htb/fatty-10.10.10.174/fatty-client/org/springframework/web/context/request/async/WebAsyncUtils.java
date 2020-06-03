/*    */ package org.springframework.web.context.request.async;
/*    */ 
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ 
/*    */ public abstract class WebAsyncUtils
/*    */ {
/* 38 */   public static final String WEB_ASYNC_MANAGER_ATTRIBUTE = WebAsyncManager.class
/* 39 */     .getName() + ".WEB_ASYNC_MANAGER";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static WebAsyncManager getAsyncManager(ServletRequest servletRequest) {
/* 47 */     WebAsyncManager asyncManager = null;
/* 48 */     Object asyncManagerAttr = servletRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE);
/* 49 */     if (asyncManagerAttr instanceof WebAsyncManager) {
/* 50 */       asyncManager = (WebAsyncManager)asyncManagerAttr;
/*    */     }
/* 52 */     if (asyncManager == null) {
/* 53 */       asyncManager = new WebAsyncManager();
/* 54 */       servletRequest.setAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, asyncManager);
/*    */     } 
/* 56 */     return asyncManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static WebAsyncManager getAsyncManager(WebRequest webRequest) {
/* 64 */     int scope = 0;
/* 65 */     WebAsyncManager asyncManager = null;
/* 66 */     Object asyncManagerAttr = webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, scope);
/* 67 */     if (asyncManagerAttr instanceof WebAsyncManager) {
/* 68 */       asyncManager = (WebAsyncManager)asyncManagerAttr;
/*    */     }
/* 70 */     if (asyncManager == null) {
/* 71 */       asyncManager = new WebAsyncManager();
/* 72 */       webRequest.setAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, asyncManager, scope);
/*    */     } 
/* 74 */     return asyncManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AsyncWebRequest createAsyncWebRequest(HttpServletRequest request, HttpServletResponse response) {
/* 85 */     return new StandardServletAsyncWebRequest(request, response);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/async/WebAsyncUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */