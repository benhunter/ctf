/*    */ package org.springframework.web.servlet.theme;
/*    */ 
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.servlet.ThemeResolver;
/*    */ import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
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
/*    */ public class ThemeChangeInterceptor
/*    */   extends HandlerInterceptorAdapter
/*    */ {
/*    */   public static final String DEFAULT_PARAM_NAME = "theme";
/* 42 */   private String paramName = "theme";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setParamName(String paramName) {
/* 50 */     this.paramName = paramName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getParamName() {
/* 58 */     return this.paramName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
/* 66 */     String newTheme = request.getParameter(this.paramName);
/* 67 */     if (newTheme != null) {
/* 68 */       ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(request);
/* 69 */       if (themeResolver == null) {
/* 70 */         throw new IllegalStateException("No ThemeResolver found: not in a DispatcherServlet request?");
/*    */       }
/* 72 */       themeResolver.setThemeName(request, response, newTheme);
/*    */     } 
/*    */     
/* 75 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/theme/ThemeChangeInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */