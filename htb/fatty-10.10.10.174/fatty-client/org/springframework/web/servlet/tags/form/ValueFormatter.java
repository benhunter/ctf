/*    */ package org.springframework.web.servlet.tags.form;
/*    */ 
/*    */ import java.beans.PropertyEditor;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ import org.springframework.web.util.HtmlUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class ValueFormatter
/*    */ {
/*    */   public static String getDisplayString(@Nullable Object value, boolean htmlEscape) {
/* 48 */     String displayValue = ObjectUtils.getDisplayString(value);
/* 49 */     return htmlEscape ? HtmlUtils.htmlEscape(displayValue) : displayValue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getDisplayString(@Nullable Object value, @Nullable PropertyEditor propertyEditor, boolean htmlEscape) {
/* 62 */     if (propertyEditor != null && !(value instanceof String)) {
/*    */       try {
/* 64 */         propertyEditor.setValue(value);
/* 65 */         String text = propertyEditor.getAsText();
/* 66 */         if (text != null) {
/* 67 */           return getDisplayString(text, htmlEscape);
/*    */         }
/*    */       }
/* 70 */       catch (Throwable throwable) {}
/*    */     }
/*    */ 
/*    */     
/* 74 */     return getDisplayString(value, htmlEscape);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/ValueFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */