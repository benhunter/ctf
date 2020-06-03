/*    */ package org.springframework.web.servlet.theme;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class FixedThemeResolver
/*    */   extends AbstractThemeResolver
/*    */ {
/*    */   public String resolveThemeName(HttpServletRequest request) {
/* 41 */     return getDefaultThemeName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setThemeName(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable String themeName) {
/* 48 */     throw new UnsupportedOperationException("Cannot change theme - use a different theme resolution strategy");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/theme/FixedThemeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */