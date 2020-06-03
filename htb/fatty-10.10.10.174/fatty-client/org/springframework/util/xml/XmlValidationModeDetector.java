/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
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
/*     */ public class XmlValidationModeDetector
/*     */ {
/*     */   public static final int VALIDATION_NONE = 0;
/*     */   public static final int VALIDATION_AUTO = 1;
/*     */   public static final int VALIDATION_DTD = 2;
/*     */   public static final int VALIDATION_XSD = 3;
/*     */   private static final String DOCTYPE = "DOCTYPE";
/*     */   private static final String START_COMMENT = "<!--";
/*     */   private static final String END_COMMENT = "-->";
/*     */   private boolean inComment;
/*     */   
/*     */   public int detectValidationMode(InputStream inputStream) throws IOException {
/*  92 */     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
/*     */     try {
/*  94 */       boolean isDtdValidated = false;
/*     */       String content;
/*  96 */       while ((content = reader.readLine()) != null) {
/*  97 */         content = consumeCommentTokens(content);
/*  98 */         if (this.inComment || !StringUtils.hasText(content)) {
/*     */           continue;
/*     */         }
/* 101 */         if (hasDoctype(content)) {
/* 102 */           isDtdValidated = true;
/*     */           break;
/*     */         } 
/* 105 */         if (hasOpeningTag(content)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 110 */       return isDtdValidated ? 2 : 3;
/*     */     }
/* 112 */     catch (CharConversionException ex) {
/*     */ 
/*     */       
/* 115 */       return 1;
/*     */     } finally {
/*     */       
/* 118 */       reader.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasDoctype(String content) {
/* 127 */     return content.contains("DOCTYPE");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasOpeningTag(String content) {
/* 136 */     if (this.inComment) {
/* 137 */       return false;
/*     */     }
/* 139 */     int openTagIndex = content.indexOf('<');
/* 140 */     return (openTagIndex > -1 && content.length() > openTagIndex + 1 && 
/* 141 */       Character.isLetter(content.charAt(openTagIndex + 1)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String consumeCommentTokens(String line) {
/* 152 */     if (!line.contains("<!--") && !line.contains("-->")) {
/* 153 */       return line;
/*     */     }
/* 155 */     String currLine = line;
/* 156 */     while ((currLine = consume(currLine)) != null) {
/* 157 */       if (!this.inComment && !currLine.trim().startsWith("<!--")) {
/* 158 */         return currLine;
/*     */       }
/*     */     } 
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String consume(String line) {
/* 170 */     int index = this.inComment ? endComment(line) : startComment(line);
/* 171 */     return (index == -1) ? null : line.substring(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int startComment(String line) {
/* 179 */     return commentToken(line, "<!--", true);
/*     */   }
/*     */   
/*     */   private int endComment(String line) {
/* 183 */     return commentToken(line, "-->", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int commentToken(String line, String token, boolean inCommentIfPresent) {
/* 192 */     int index = line.indexOf(token);
/* 193 */     if (index > -1) {
/* 194 */       this.inComment = inCommentIfPresent;
/*     */     }
/* 196 */     return (index == -1) ? index : (index + token.length());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/XmlValidationModeDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */