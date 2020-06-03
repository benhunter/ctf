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
/*     */ public class CharacterEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private static final String UNICODE_PREFIX = "\\u";
/*     */   private static final int UNICODE_LENGTH = 6;
/*     */   private final boolean allowEmpty;
/*     */   
/*     */   public CharacterEditor(boolean allowEmpty) {
/*  68 */     this.allowEmpty = allowEmpty;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(@Nullable String text) throws IllegalArgumentException {
/*  74 */     if (this.allowEmpty && !StringUtils.hasLength(text)) {
/*     */       
/*  76 */       setValue(null);
/*     */     } else {
/*  78 */       if (text == null) {
/*  79 */         throw new IllegalArgumentException("null String cannot be converted to char type");
/*     */       }
/*  81 */       if (isUnicodeCharacterSequence(text)) {
/*  82 */         setAsUnicode(text);
/*     */       }
/*  84 */       else if (text.length() == 1) {
/*  85 */         setValue(Character.valueOf(text.charAt(0)));
/*     */       } else {
/*     */         
/*  88 */         throw new IllegalArgumentException("String [" + text + "] with length " + text
/*  89 */             .length() + " cannot be converted to char type: neither Unicode nor single character");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getAsText() {
/*  95 */     Object value = getValue();
/*  96 */     return (value != null) ? value.toString() : "";
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isUnicodeCharacterSequence(String sequence) {
/* 101 */     return (sequence.startsWith("\\u") && sequence.length() == 6);
/*     */   }
/*     */   
/*     */   private void setAsUnicode(String text) {
/* 105 */     int code = Integer.parseInt(text.substring("\\u".length()), 16);
/* 106 */     setValue(Character.valueOf((char)code));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/CharacterEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */