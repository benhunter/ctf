/*    */ package org.springframework.web.servlet.view.tiles3;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.tiles.locale.impl.DefaultLocaleResolver;
/*    */ import org.apache.tiles.request.Request;
/*    */ import org.apache.tiles.request.servlet.NotAServletEnvironmentException;
/*    */ import org.apache.tiles.request.servlet.ServletUtil;
/*    */ import org.springframework.web.servlet.support.RequestContextUtils;
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
/*    */ public class SpringLocaleResolver
/*    */   extends DefaultLocaleResolver
/*    */ {
/*    */   public Locale resolveLocale(Request request) {
/*    */     try {
/* 45 */       HttpServletRequest servletRequest = ServletUtil.getServletRequest(request).getRequest();
/* 46 */       if (servletRequest != null) {
/* 47 */         return RequestContextUtils.getLocale(servletRequest);
/*    */       }
/*    */     }
/* 50 */     catch (NotAServletEnvironmentException notAServletEnvironmentException) {}
/*    */ 
/*    */     
/* 53 */     return super.resolveLocale(request);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/tiles3/SpringLocaleResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */