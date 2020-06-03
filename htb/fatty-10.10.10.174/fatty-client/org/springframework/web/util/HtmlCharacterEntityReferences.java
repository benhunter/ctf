/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ class HtmlCharacterEntityReferences
/*     */ {
/*     */   private static final String PROPERTIES_FILE = "HtmlCharacterEntityReferences.properties";
/*     */   static final char REFERENCE_START = '&';
/*     */   static final String DECIMAL_REFERENCE_START = "&#";
/*     */   static final String HEX_REFERENCE_START = "&#x";
/*     */   static final char REFERENCE_END = ';';
/*     */   static final char CHAR_NULL = '￿';
/*  56 */   private final String[] characterToEntityReferenceMap = new String[3000];
/*     */   
/*  58 */   private final Map<String, Character> entityReferenceToCharacterMap = new HashMap<>(512);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HtmlCharacterEntityReferences() {
/*  65 */     Properties entityReferences = new Properties();
/*     */ 
/*     */     
/*  68 */     InputStream is = HtmlCharacterEntityReferences.class.getResourceAsStream("HtmlCharacterEntityReferences.properties");
/*  69 */     if (is == null) {
/*  70 */       throw new IllegalStateException("Cannot find reference definition file [HtmlCharacterEntityReferences.properties] as class path resource");
/*     */     }
/*     */     
/*     */     try {
/*     */       try {
/*  75 */         entityReferences.load(is);
/*     */       } finally {
/*     */         
/*  78 */         is.close();
/*     */       }
/*     */     
/*  81 */     } catch (IOException ex) {
/*  82 */       throw new IllegalStateException("Failed to parse reference definition file [HtmlCharacterEntityReferences.properties]: " + ex
/*  83 */           .getMessage());
/*     */     } 
/*     */ 
/*     */     
/*  87 */     Enumeration<?> keys = entityReferences.propertyNames();
/*  88 */     while (keys.hasMoreElements()) {
/*  89 */       String key = (String)keys.nextElement();
/*  90 */       int referredChar = Integer.parseInt(key);
/*  91 */       Assert.isTrue((referredChar < 1000 || (referredChar >= 8000 && referredChar < 10000)), () -> "Invalid reference to special HTML entity: " + referredChar);
/*     */       
/*  93 */       int index = (referredChar < 1000) ? referredChar : (referredChar - 7000);
/*  94 */       String reference = entityReferences.getProperty(key);
/*  95 */       this.characterToEntityReferenceMap[index] = '&' + reference + ';';
/*  96 */       this.entityReferenceToCharacterMap.put(reference, Character.valueOf((char)referredChar));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSupportedReferenceCount() {
/* 105 */     return this.entityReferenceToCharacterMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMappedToReference(char character) {
/* 112 */     return isMappedToReference(character, "ISO-8859-1");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMappedToReference(char character, String encoding) {
/* 119 */     return (convertToReference(character, encoding) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String convertToReference(char character) {
/* 127 */     return convertToReference(character, "ISO-8859-1");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String convertToReference(char character, String encoding) {
/* 136 */     if (encoding.startsWith("UTF-")) {
/* 137 */       switch (character) {
/*     */         case '<':
/* 139 */           return "&lt;";
/*     */         case '>':
/* 141 */           return "&gt;";
/*     */         case '"':
/* 143 */           return "&quot;";
/*     */         case '&':
/* 145 */           return "&amp;";
/*     */         case '\'':
/* 147 */           return "&#39;";
/*     */       } 
/*     */     
/* 150 */     } else if (character < 'Ϩ' || (character >= 'ὀ' && character < '✐')) {
/* 151 */       int index = (character < 'Ϩ') ? character : (character - 7000);
/* 152 */       String entityReference = this.characterToEntityReferenceMap[index];
/* 153 */       if (entityReference != null) {
/* 154 */         return entityReference;
/*     */       }
/*     */     } 
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char convertToCharacter(String entityReference) {
/* 164 */     Character referredCharacter = this.entityReferenceToCharacterMap.get(entityReference);
/* 165 */     if (referredCharacter != null) {
/* 166 */       return referredCharacter.charValue();
/*     */     }
/* 168 */     return Character.MAX_VALUE;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/HtmlCharacterEntityReferences.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */