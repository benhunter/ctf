/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class StringArrayPropertyEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   public static final String DEFAULT_SEPARATOR = ",";
/*     */   private final String separator;
/*     */   @Nullable
/*     */   private final String charsToDelete;
/*     */   private final boolean emptyArrayAsNull;
/*     */   private final boolean trimValues;
/*     */   
/*     */   public StringArrayPropertyEditor() {
/*  61 */     this(",", (String)null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringArrayPropertyEditor(String separator) {
/*  70 */     this(separator, (String)null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringArrayPropertyEditor(String separator, boolean emptyArrayAsNull) {
/*  80 */     this(separator, (String)null, emptyArrayAsNull);
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
/*     */   public StringArrayPropertyEditor(String separator, boolean emptyArrayAsNull, boolean trimValues) {
/*  92 */     this(separator, (String)null, emptyArrayAsNull, trimValues);
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
/*     */   public StringArrayPropertyEditor(String separator, @Nullable String charsToDelete, boolean emptyArrayAsNull) {
/* 105 */     this(separator, charsToDelete, emptyArrayAsNull, true);
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
/*     */   public StringArrayPropertyEditor(String separator, @Nullable String charsToDelete, boolean emptyArrayAsNull, boolean trimValues) {
/* 122 */     this.separator = separator;
/* 123 */     this.charsToDelete = charsToDelete;
/* 124 */     this.emptyArrayAsNull = emptyArrayAsNull;
/* 125 */     this.trimValues = trimValues;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/* 130 */     String[] array = StringUtils.delimitedListToStringArray(text, this.separator, this.charsToDelete);
/* 131 */     if (this.trimValues) {
/* 132 */       array = StringUtils.trimArrayElements(array);
/*     */     }
/* 134 */     if (this.emptyArrayAsNull && array.length == 0) {
/* 135 */       setValue(null);
/*     */     } else {
/*     */       
/* 138 */       setValue(array);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAsText() {
/* 144 */     return StringUtils.arrayToDelimitedString(ObjectUtils.toObjectArray(getValue()), this.separator);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/StringArrayPropertyEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */