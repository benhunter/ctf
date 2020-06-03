/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HtmlUtils
/*     */ {
/*  46 */   private static final HtmlCharacterEntityReferences characterEntityReferences = new HtmlCharacterEntityReferences();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String htmlEscape(String input) {
/*  63 */     return htmlEscape(input, "ISO-8859-1");
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
/*     */   public static String htmlEscape(String input, String encoding) {
/*  83 */     Assert.notNull(input, "Input is required");
/*  84 */     Assert.notNull(encoding, "Encoding is required");
/*  85 */     StringBuilder escaped = new StringBuilder(input.length() * 2);
/*  86 */     for (int i = 0; i < input.length(); i++) {
/*  87 */       char character = input.charAt(i);
/*  88 */       String reference = characterEntityReferences.convertToReference(character, encoding);
/*  89 */       if (reference != null) {
/*  90 */         escaped.append(reference);
/*     */       } else {
/*     */         
/*  93 */         escaped.append(character);
/*     */       } 
/*     */     } 
/*  96 */     return escaped.toString();
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
/*     */   public static String htmlEscapeDecimal(String input) {
/* 112 */     return htmlEscapeDecimal(input, "ISO-8859-1");
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
/*     */   public static String htmlEscapeDecimal(String input, String encoding) {
/* 132 */     Assert.notNull(input, "Input is required");
/* 133 */     Assert.notNull(encoding, "Encoding is required");
/* 134 */     StringBuilder escaped = new StringBuilder(input.length() * 2);
/* 135 */     for (int i = 0; i < input.length(); i++) {
/* 136 */       char character = input.charAt(i);
/* 137 */       if (characterEntityReferences.isMappedToReference(character, encoding)) {
/* 138 */         escaped.append("&#");
/* 139 */         escaped.append(character);
/* 140 */         escaped.append(';');
/*     */       } else {
/*     */         
/* 143 */         escaped.append(character);
/*     */       } 
/*     */     } 
/* 146 */     return escaped.toString();
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
/*     */   public static String htmlEscapeHex(String input) {
/* 162 */     return htmlEscapeHex(input, "ISO-8859-1");
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
/*     */   public static String htmlEscapeHex(String input, String encoding) {
/* 182 */     Assert.notNull(input, "Input is required");
/* 183 */     Assert.notNull(encoding, "Encoding is required");
/* 184 */     StringBuilder escaped = new StringBuilder(input.length() * 2);
/* 185 */     for (int i = 0; i < input.length(); i++) {
/* 186 */       char character = input.charAt(i);
/* 187 */       if (characterEntityReferences.isMappedToReference(character, encoding)) {
/* 188 */         escaped.append("&#x");
/* 189 */         escaped.append(Integer.toString(character, 16));
/* 190 */         escaped.append(';');
/*     */       } else {
/*     */         
/* 193 */         escaped.append(character);
/*     */       } 
/*     */     } 
/* 196 */     return escaped.toString();
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
/*     */   public static String htmlUnescape(String input) {
/* 219 */     return (new HtmlCharacterEntityDecoder(characterEntityReferences, input)).decode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/HtmlUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */