/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.Locale;
/*    */ import java.util.ResourceBundle;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class ResourceBundleEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public static final String BASE_NAME_SEPARATOR = "_";
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 84 */     Assert.hasText(text, "'text' must not be empty");
/* 85 */     String name = text.trim();
/*    */     
/* 87 */     int separator = name.indexOf("_");
/* 88 */     if (separator == -1) {
/* 89 */       setValue(ResourceBundle.getBundle(name));
/*    */     }
/*    */     else {
/*    */       
/* 93 */       String baseName = name.substring(0, separator);
/* 94 */       if (!StringUtils.hasText(baseName)) {
/* 95 */         throw new IllegalArgumentException("Invalid ResourceBundle name: '" + text + "'");
/*    */       }
/* 97 */       String localeString = name.substring(separator + 1);
/* 98 */       Locale locale = StringUtils.parseLocaleString(localeString);
/* 99 */       setValue((locale != null) ? ResourceBundle.getBundle(baseName, locale) : ResourceBundle.getBundle(baseName));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/ResourceBundleEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */