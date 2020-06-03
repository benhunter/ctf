/*    */ package org.springframework.format.support;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.context.i18n.LocaleContextHolder;
/*    */ import org.springframework.format.Formatter;
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
/*    */ public class FormatterPropertyEditorAdapter
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final Formatter<Object> formatter;
/*    */   
/*    */   public FormatterPropertyEditorAdapter(Formatter<?> formatter) {
/* 44 */     Assert.notNull(formatter, "Formatter must not be null");
/* 45 */     this.formatter = (Formatter)formatter;
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
/*    */   public Class<?> getFieldType() {
/* 57 */     return FormattingConversionService.getFieldType(this.formatter);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 63 */     if (StringUtils.hasText(text)) {
/*    */       try {
/* 65 */         setValue(this.formatter.parse(text, LocaleContextHolder.getLocale()));
/*    */       }
/* 67 */       catch (IllegalArgumentException ex) {
/* 68 */         throw ex;
/*    */       }
/* 70 */       catch (Throwable ex) {
/* 71 */         throw new IllegalArgumentException("Parse attempt failed for value [" + text + "]", ex);
/*    */       } 
/*    */     } else {
/*    */       
/* 75 */       setValue(null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 81 */     Object value = getValue();
/* 82 */     return (value != null) ? this.formatter.print(value, LocaleContextHolder.getLocale()) : "";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/support/FormatterPropertyEditorAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */