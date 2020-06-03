/*    */ package org.springframework.web.servlet.theme;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.util.WebUtils;
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
/*    */ 
/*    */ public class SessionThemeResolver
/*    */   extends AbstractThemeResolver
/*    */ {
/* 50 */   public static final String THEME_SESSION_ATTRIBUTE_NAME = SessionThemeResolver.class.getName() + ".THEME";
/*    */ 
/*    */ 
/*    */   
/*    */   public String resolveThemeName(HttpServletRequest request) {
/* 55 */     String themeName = (String)WebUtils.getSessionAttribute(request, THEME_SESSION_ATTRIBUTE_NAME);
/*    */     
/* 57 */     return (themeName != null) ? themeName : getDefaultThemeName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setThemeName(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable String themeName) {
/* 64 */     WebUtils.setSessionAttribute(request, THEME_SESSION_ATTRIBUTE_NAME, 
/* 65 */         StringUtils.hasText(themeName) ? themeName : null);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/theme/SessionThemeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */