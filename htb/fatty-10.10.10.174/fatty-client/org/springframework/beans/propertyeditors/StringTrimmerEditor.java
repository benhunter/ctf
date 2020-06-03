/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class StringTrimmerEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   @Nullable
/*    */   private final String charsToDelete;
/*    */   private final boolean emptyAsNull;
/*    */   
/*    */   public StringTrimmerEditor(boolean emptyAsNull) {
/* 47 */     this.charsToDelete = null;
/* 48 */     this.emptyAsNull = emptyAsNull;
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
/*    */   public StringTrimmerEditor(String charsToDelete, boolean emptyAsNull) {
/* 60 */     this.charsToDelete = charsToDelete;
/* 61 */     this.emptyAsNull = emptyAsNull;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(@Nullable String text) {
/* 67 */     if (text == null) {
/* 68 */       setValue(null);
/*    */     } else {
/*    */       
/* 71 */       String value = text.trim();
/* 72 */       if (this.charsToDelete != null) {
/* 73 */         value = StringUtils.deleteAny(value, this.charsToDelete);
/*    */       }
/* 75 */       if (this.emptyAsNull && "".equals(value)) {
/* 76 */         setValue(null);
/*    */       } else {
/*    */         
/* 79 */         setValue(value);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 86 */     Object value = getValue();
/* 87 */     return (value != null) ? value.toString() : "";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/StringTrimmerEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */