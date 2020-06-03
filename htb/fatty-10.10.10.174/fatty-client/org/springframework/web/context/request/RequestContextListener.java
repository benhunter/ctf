/*    */ package org.springframework.web.context.request;
/*    */ 
/*    */ import javax.servlet.ServletRequestEvent;
/*    */ import javax.servlet.ServletRequestListener;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.context.i18n.LocaleContextHolder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestContextListener
/*    */   implements ServletRequestListener
/*    */ {
/* 48 */   private static final String REQUEST_ATTRIBUTES_ATTRIBUTE = RequestContextListener.class
/* 49 */     .getName() + ".REQUEST_ATTRIBUTES";
/*    */ 
/*    */ 
/*    */   
/*    */   public void requestInitialized(ServletRequestEvent requestEvent) {
/* 54 */     if (!(requestEvent.getServletRequest() instanceof HttpServletRequest)) {
/* 55 */       throw new IllegalArgumentException("Request is not an HttpServletRequest: " + requestEvent
/* 56 */           .getServletRequest());
/*    */     }
/* 58 */     HttpServletRequest request = (HttpServletRequest)requestEvent.getServletRequest();
/* 59 */     ServletRequestAttributes attributes = new ServletRequestAttributes(request);
/* 60 */     request.setAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE, attributes);
/* 61 */     LocaleContextHolder.setLocale(request.getLocale());
/* 62 */     RequestContextHolder.setRequestAttributes(attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   public void requestDestroyed(ServletRequestEvent requestEvent) {
/* 67 */     ServletRequestAttributes attributes = null;
/* 68 */     Object reqAttr = requestEvent.getServletRequest().getAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE);
/* 69 */     if (reqAttr instanceof ServletRequestAttributes) {
/* 70 */       attributes = (ServletRequestAttributes)reqAttr;
/*    */     }
/* 72 */     RequestAttributes threadAttributes = RequestContextHolder.getRequestAttributes();
/* 73 */     if (threadAttributes != null) {
/*    */       
/* 75 */       LocaleContextHolder.resetLocaleContext();
/* 76 */       RequestContextHolder.resetRequestAttributes();
/* 77 */       if (attributes == null && threadAttributes instanceof ServletRequestAttributes) {
/* 78 */         attributes = (ServletRequestAttributes)threadAttributes;
/*    */       }
/*    */     } 
/* 81 */     if (attributes != null)
/* 82 */       attributes.requestCompleted(); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/RequestContextListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */