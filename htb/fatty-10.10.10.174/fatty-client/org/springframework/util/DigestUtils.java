/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DigestUtils
/*     */ {
/*     */   private static final String MD5_ALGORITHM_NAME = "MD5";
/*  40 */   private static final char[] HEX_CHARS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] md5Digest(byte[] bytes) {
/*  50 */     return digest("MD5", bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] md5Digest(InputStream inputStream) throws IOException {
/*  60 */     return digest("MD5", inputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String md5DigestAsHex(byte[] bytes) {
/*  69 */     return digestAsHexString("MD5", bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String md5DigestAsHex(InputStream inputStream) throws IOException {
/*  79 */     return digestAsHexString("MD5", inputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringBuilder appendMd5DigestAsHex(byte[] bytes, StringBuilder builder) {
/*  90 */     return appendDigestAsHex("MD5", bytes, builder);
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
/*     */   public static StringBuilder appendMd5DigestAsHex(InputStream inputStream, StringBuilder builder) throws IOException {
/* 102 */     return appendDigestAsHex("MD5", inputStream, builder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MessageDigest getDigest(String algorithm) {
/*     */     try {
/* 112 */       return MessageDigest.getInstance(algorithm);
/*     */     }
/* 114 */     catch (NoSuchAlgorithmException ex) {
/* 115 */       throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static byte[] digest(String algorithm, byte[] bytes) {
/* 120 */     return getDigest(algorithm).digest(bytes);
/*     */   }
/*     */   
/*     */   private static byte[] digest(String algorithm, InputStream inputStream) throws IOException {
/* 124 */     MessageDigest messageDigest = getDigest(algorithm);
/* 125 */     if (inputStream instanceof UpdateMessageDigestInputStream) {
/* 126 */       ((UpdateMessageDigestInputStream)inputStream).updateMessageDigest(messageDigest);
/* 127 */       return messageDigest.digest();
/*     */     } 
/*     */     
/* 130 */     byte[] buffer = new byte[4096];
/* 131 */     int bytesRead = -1;
/* 132 */     while ((bytesRead = inputStream.read(buffer)) != -1) {
/* 133 */       messageDigest.update(buffer, 0, bytesRead);
/*     */     }
/* 135 */     return messageDigest.digest();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String digestAsHexString(String algorithm, byte[] bytes) {
/* 140 */     char[] hexDigest = digestAsHexChars(algorithm, bytes);
/* 141 */     return new String(hexDigest);
/*     */   }
/*     */   
/*     */   private static String digestAsHexString(String algorithm, InputStream inputStream) throws IOException {
/* 145 */     char[] hexDigest = digestAsHexChars(algorithm, inputStream);
/* 146 */     return new String(hexDigest);
/*     */   }
/*     */   
/*     */   private static StringBuilder appendDigestAsHex(String algorithm, byte[] bytes, StringBuilder builder) {
/* 150 */     char[] hexDigest = digestAsHexChars(algorithm, bytes);
/* 151 */     return builder.append(hexDigest);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder appendDigestAsHex(String algorithm, InputStream inputStream, StringBuilder builder) throws IOException {
/* 157 */     char[] hexDigest = digestAsHexChars(algorithm, inputStream);
/* 158 */     return builder.append(hexDigest);
/*     */   }
/*     */   
/*     */   private static char[] digestAsHexChars(String algorithm, byte[] bytes) {
/* 162 */     byte[] digest = digest(algorithm, bytes);
/* 163 */     return encodeHex(digest);
/*     */   }
/*     */   
/*     */   private static char[] digestAsHexChars(String algorithm, InputStream inputStream) throws IOException {
/* 167 */     byte[] digest = digest(algorithm, inputStream);
/* 168 */     return encodeHex(digest);
/*     */   }
/*     */   
/*     */   private static char[] encodeHex(byte[] bytes) {
/* 172 */     char[] chars = new char[32];
/* 173 */     for (int i = 0; i < chars.length; i += 2) {
/* 174 */       byte b = bytes[i / 2];
/* 175 */       chars[i] = HEX_CHARS[b >>> 4 & 0xF];
/* 176 */       chars[i + 1] = HEX_CHARS[b & 0xF];
/*     */     } 
/* 178 */     return chars;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/DigestUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */