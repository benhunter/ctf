/*    */ package org.springframework.web.servlet.theme;
/*    */ 
/*    */ import org.springframework.web.servlet.ThemeResolver;
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
/*    */ public abstract class AbstractThemeResolver
/*    */   implements ThemeResolver
/*    */ {
/*    */   public static final String ORIGINAL_DEFAULT_THEME_NAME = "theme";
/* 36 */   private String defaultThemeName = "theme";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDefaultThemeName(String defaultThemeName) {
/* 44 */     this.defaultThemeName = defaultThemeName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDefaultThemeName() {
/* 51 */     return this.defaultThemeName;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/theme/AbstractThemeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */