/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.file.Files;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FileCopyUtils
/*     */ {
/*     */   public static final int BUFFER_SIZE = 4096;
/*     */   
/*     */   public static int copy(File in, File out) throws IOException {
/*  64 */     Assert.notNull(in, "No input File specified");
/*  65 */     Assert.notNull(out, "No output File specified");
/*  66 */     return copy(Files.newInputStream(in.toPath(), new java.nio.file.OpenOption[0]), Files.newOutputStream(out.toPath(), new java.nio.file.OpenOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(byte[] in, File out) throws IOException {
/*  76 */     Assert.notNull(in, "No input byte array specified");
/*  77 */     Assert.notNull(out, "No output File specified");
/*  78 */     copy(new ByteArrayInputStream(in), Files.newOutputStream(out.toPath(), new java.nio.file.OpenOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] copyToByteArray(File in) throws IOException {
/*  88 */     Assert.notNull(in, "No input File specified");
/*  89 */     return copyToByteArray(Files.newInputStream(in.toPath(), new java.nio.file.OpenOption[0]));
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
/*     */   public static int copy(InputStream in, OutputStream out) throws IOException {
/* 106 */     Assert.notNull(in, "No InputStream specified");
/* 107 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/*     */     try {
/* 110 */       return StreamUtils.copy(in, out);
/*     */     } finally {
/*     */       
/*     */       try {
/* 114 */         in.close();
/*     */       }
/* 116 */       catch (IOException iOException) {}
/*     */       
/*     */       try {
/* 119 */         out.close();
/*     */       }
/* 121 */       catch (IOException iOException) {}
/*     */     } 
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
/*     */   public static void copy(byte[] in, OutputStream out) throws IOException {
/* 134 */     Assert.notNull(in, "No input byte array specified");
/* 135 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/*     */     try {
/* 138 */       out.write(in);
/*     */     } finally {
/*     */       
/*     */       try {
/* 142 */         out.close();
/*     */       }
/* 144 */       catch (IOException iOException) {}
/*     */     } 
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
/*     */   public static byte[] copyToByteArray(@Nullable InputStream in) throws IOException {
/* 157 */     if (in == null) {
/* 158 */       return new byte[0];
/*     */     }
/*     */     
/* 161 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/* 162 */     copy(in, out);
/* 163 */     return out.toByteArray();
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
/*     */   public static int copy(Reader in, Writer out) throws IOException {
/* 180 */     Assert.notNull(in, "No Reader specified");
/* 181 */     Assert.notNull(out, "No Writer specified");
/*     */     
/*     */     try {
/* 184 */       int byteCount = 0;
/* 185 */       char[] buffer = new char[4096];
/* 186 */       int bytesRead = -1;
/* 187 */       while ((bytesRead = in.read(buffer)) != -1) {
/* 188 */         out.write(buffer, 0, bytesRead);
/* 189 */         byteCount += bytesRead;
/*     */       } 
/* 191 */       out.flush();
/* 192 */       return byteCount;
/*     */     } finally {
/*     */       
/*     */       try {
/* 196 */         in.close();
/*     */       }
/* 198 */       catch (IOException iOException) {}
/*     */       
/*     */       try {
/* 201 */         out.close();
/*     */       }
/* 203 */       catch (IOException iOException) {}
/*     */     } 
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
/*     */   public static void copy(String in, Writer out) throws IOException {
/* 216 */     Assert.notNull(in, "No input String specified");
/* 217 */     Assert.notNull(out, "No Writer specified");
/*     */     
/*     */     try {
/* 220 */       out.write(in);
/*     */     } finally {
/*     */       
/*     */       try {
/* 224 */         out.close();
/*     */       }
/* 226 */       catch (IOException iOException) {}
/*     */     } 
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
/*     */   public static String copyToString(@Nullable Reader in) throws IOException {
/* 239 */     if (in == null) {
/* 240 */       return "";
/*     */     }
/*     */     
/* 243 */     StringWriter out = new StringWriter();
/* 244 */     copy(in, out);
/* 245 */     return out.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/FileCopyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */