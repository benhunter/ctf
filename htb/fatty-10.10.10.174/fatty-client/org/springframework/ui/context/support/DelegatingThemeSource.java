/*    */ package org.springframework.ui.context.support;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.ui.context.HierarchicalThemeSource;
/*    */ import org.springframework.ui.context.Theme;
/*    */ import org.springframework.ui.context.ThemeSource;
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
/*    */ public class DelegatingThemeSource
/*    */   implements HierarchicalThemeSource
/*    */ {
/*    */   @Nullable
/*    */   private ThemeSource parentThemeSource;
/*    */   
/*    */   public void setParentThemeSource(@Nullable ThemeSource parentThemeSource) {
/* 43 */     this.parentThemeSource = parentThemeSource;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ThemeSource getParentThemeSource() {
/* 49 */     return this.parentThemeSource;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Theme getTheme(String themeName) {
/* 56 */     if (this.parentThemeSource != null) {
/* 57 */       return this.parentThemeSource.getTheme(themeName);
/*    */     }
/*    */     
/* 60 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ui/context/support/DelegatingThemeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */