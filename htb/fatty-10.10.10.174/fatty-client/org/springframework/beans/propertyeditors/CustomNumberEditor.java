/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.text.NumberFormat;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.NumberUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomNumberEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final Class<? extends Number> numberClass;
/*     */   @Nullable
/*     */   private final NumberFormat numberFormat;
/*     */   private final boolean allowEmpty;
/*     */   
/*     */   public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) throws IllegalArgumentException {
/*  71 */     this(numberClass, null, allowEmpty);
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
/*     */   public CustomNumberEditor(Class<? extends Number> numberClass, @Nullable NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
/*  91 */     if (!Number.class.isAssignableFrom(numberClass)) {
/*  92 */       throw new IllegalArgumentException("Property class must be a subclass of Number");
/*     */     }
/*  94 */     this.numberClass = numberClass;
/*  95 */     this.numberFormat = numberFormat;
/*  96 */     this.allowEmpty = allowEmpty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/* 105 */     if (this.allowEmpty && !StringUtils.hasText(text)) {
/*     */       
/* 107 */       setValue(null);
/*     */     }
/* 109 */     else if (this.numberFormat != null) {
/*     */       
/* 111 */       setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
/*     */     }
/*     */     else {
/*     */       
/* 115 */       setValue(NumberUtils.parseNumber(text, this.numberClass));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(@Nullable Object value) {
/* 124 */     if (value instanceof Number) {
/* 125 */       super.setValue(NumberUtils.convertNumberToTargetClass((Number)value, this.numberClass));
/*     */     } else {
/*     */       
/* 128 */       super.setValue(value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsText() {
/* 137 */     Object value = getValue();
/* 138 */     if (value == null) {
/* 139 */       return "";
/*     */     }
/* 141 */     if (this.numberFormat != null)
/*     */     {
/* 143 */       return this.numberFormat.format(value);
/*     */     }
/*     */ 
/*     */     
/* 147 */     return value.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/CustomNumberEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */