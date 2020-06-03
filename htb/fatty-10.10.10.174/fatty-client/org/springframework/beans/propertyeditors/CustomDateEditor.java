/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Date;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomDateEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final DateFormat dateFormat;
/*     */   private final boolean allowEmpty;
/*     */   private final int exactDateLength;
/*     */   
/*     */   public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty) {
/*  64 */     this.dateFormat = dateFormat;
/*  65 */     this.allowEmpty = allowEmpty;
/*  66 */     this.exactDateLength = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
/*  88 */     this.dateFormat = dateFormat;
/*  89 */     this.allowEmpty = allowEmpty;
/*  90 */     this.exactDateLength = exactDateLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(@Nullable String text) throws IllegalArgumentException {
/*  99 */     if (this.allowEmpty && !StringUtils.hasText(text)) {
/*     */       
/* 101 */       setValue(null);
/*     */     } else {
/* 103 */       if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
/* 104 */         throw new IllegalArgumentException("Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 109 */         setValue(this.dateFormat.parse(text));
/*     */       }
/* 111 */       catch (ParseException ex) {
/* 112 */         throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsText() {
/* 122 */     Date value = (Date)getValue();
/* 123 */     return (value != null) ? this.dateFormat.format(value) : "";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/CustomDateEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */