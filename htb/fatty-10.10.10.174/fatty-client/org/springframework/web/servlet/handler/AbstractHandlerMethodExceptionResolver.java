/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.method.HandlerMethod;
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
/*    */ 
/*    */ public abstract class AbstractHandlerMethodExceptionResolver
/*    */   extends AbstractHandlerExceptionResolver
/*    */ {
/*    */   protected boolean shouldApplyTo(HttpServletRequest request, @Nullable Object handler) {
/* 43 */     if (handler == null) {
/* 44 */       return super.shouldApplyTo(request, null);
/*    */     }
/* 46 */     if (handler instanceof HandlerMethod) {
/* 47 */       HandlerMethod handlerMethod = (HandlerMethod)handler;
/* 48 */       handler = handlerMethod.getBean();
/* 49 */       return super.shouldApplyTo(request, handler);
/*    */     } 
/*    */     
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected final ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
/* 61 */     return doResolveHandlerMethodException(request, response, (HandlerMethod)handler, ex);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   protected abstract ModelAndView doResolveHandlerMethodException(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, @Nullable HandlerMethod paramHandlerMethod, Exception paramException);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/AbstractHandlerMethodExceptionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */