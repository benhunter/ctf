/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
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
/*     */ public class CustomBooleanEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   public static final String VALUE_TRUE = "true";
/*     */   public static final String VALUE_FALSE = "false";
/*     */   public static final String VALUE_ON = "on";
/*     */   public static final String VALUE_OFF = "off";
/*     */   public static final String VALUE_YES = "yes";
/*     */   public static final String VALUE_NO = "no";
/*     */   public static final String VALUE_1 = "1";
/*     */   public static final String VALUE_0 = "0";
/*     */   @Nullable
/*     */   private final String trueString;
/*     */   @Nullable
/*     */   private final String falseString;
/*     */   private final boolean allowEmpty;
/*     */   
/*     */   public CustomBooleanEditor(boolean allowEmpty) {
/* 100 */     this((String)null, (String)null, allowEmpty);
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
/*     */ 
/*     */   
/*     */   public CustomBooleanEditor(@Nullable String trueString, @Nullable String falseString, boolean allowEmpty) {
/* 124 */     this.trueString = trueString;
/* 125 */     this.falseString = falseString;
/* 126 */     this.allowEmpty = allowEmpty;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(@Nullable String text) throws IllegalArgumentException {
/* 132 */     String input = (text != null) ? text.trim() : null;
/* 133 */     if (this.allowEmpty && !StringUtils.hasLength(input)) {
/*     */       
/* 135 */       setValue(null);
/*     */     }
/* 137 */     else if (this.trueString != null && this.trueString.equalsIgnoreCase(input)) {
/* 138 */       setValue(Boolean.TRUE);
/*     */     }
/* 140 */     else if (this.falseString != null && this.falseString.equalsIgnoreCase(input)) {
/* 141 */       setValue(Boolean.FALSE);
/*     */     }
/* 143 */     else if (this.trueString == null && ("true"
/* 144 */       .equalsIgnoreCase(input) || "on".equalsIgnoreCase(input) || "yes"
/* 145 */       .equalsIgnoreCase(input) || "1".equals(input))) {
/* 146 */       setValue(Boolean.TRUE);
/*     */     }
/* 148 */     else if (this.falseString == null && ("false"
/* 149 */       .equalsIgnoreCase(input) || "off".equalsIgnoreCase(input) || "no"
/* 150 */       .equalsIgnoreCase(input) || "0".equals(input))) {
/* 151 */       setValue(Boolean.FALSE);
/*     */     } else {
/*     */       
/* 154 */       throw new IllegalArgumentException("Invalid boolean value [" + text + "]");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAsText() {
/* 160 */     if (Boolean.TRUE.equals(getValue())) {
/* 161 */       return (this.trueString != null) ? this.trueString : "true";
/*     */     }
/* 163 */     if (Boolean.FALSE.equals(getValue())) {
/* 164 */       return (this.falseString != null) ? this.falseString : "false";
/*     */     }
/*     */     
/* 167 */     return "";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/CustomBooleanEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */