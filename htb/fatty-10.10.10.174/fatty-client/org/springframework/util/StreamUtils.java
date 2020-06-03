/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
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
/*     */ public abstract class StreamUtils
/*     */ {
/*     */   public static final int BUFFER_SIZE = 4096;
/*  53 */   private static final byte[] EMPTY_CONTENT = new byte[0];
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
/*  64 */     if (in == null) {
/*  65 */       return new byte[0];
/*     */     }
/*     */     
/*  68 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/*  69 */     copy(in, out);
/*  70 */     return out.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String copyToString(@Nullable InputStream in, Charset charset) throws IOException {
/*  81 */     if (in == null) {
/*  82 */       return "";
/*     */     }
/*     */     
/*  85 */     StringBuilder out = new StringBuilder();
/*  86 */     InputStreamReader reader = new InputStreamReader(in, charset);
/*  87 */     char[] buffer = new char[4096];
/*  88 */     int bytesRead = -1;
/*  89 */     while ((bytesRead = reader.read(buffer)) != -1) {
/*  90 */       out.append(buffer, 0, bytesRead);
/*     */     }
/*  92 */     return out.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(byte[] in, OutputStream out) throws IOException {
/* 103 */     Assert.notNull(in, "No input byte array specified");
/* 104 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 106 */     out.write(in);
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
/*     */   public static void copy(String in, Charset charset, OutputStream out) throws IOException {
/* 118 */     Assert.notNull(in, "No input String specified");
/* 119 */     Assert.notNull(charset, "No charset specified");
/* 120 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 122 */     Writer writer = new OutputStreamWriter(out, charset);
/* 123 */     writer.write(in);
/* 124 */     writer.flush();
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
/*     */   public static int copy(InputStream in, OutputStream out) throws IOException {
/* 136 */     Assert.notNull(in, "No InputStream specified");
/* 137 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 139 */     int byteCount = 0;
/* 140 */     byte[] buffer = new byte[4096];
/* 141 */     int bytesRead = -1;
/* 142 */     while ((bytesRead = in.read(buffer)) != -1) {
/* 143 */       out.write(buffer, 0, bytesRead);
/* 144 */       byteCount += bytesRead;
/*     */     } 
/* 146 */     out.flush();
/* 147 */     return byteCount;
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
/*     */   public static long copyRange(InputStream in, OutputStream out, long start, long end) throws IOException {
/* 164 */     Assert.notNull(in, "No InputStream specified");
/* 165 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 167 */     long skipped = in.skip(start);
/* 168 */     if (skipped < start) {
/* 169 */       throw new IOException("Skipped only " + skipped + " bytes out of " + start + " required");
/*     */     }
/*     */     
/* 172 */     long bytesToCopy = end - start + 1L;
/* 173 */     byte[] buffer = new byte[4096];
/* 174 */     while (bytesToCopy > 0L) {
/* 175 */       int bytesRead = in.read(buffer);
/* 176 */       if (bytesRead == -1) {
/*     */         break;
/*     */       }
/* 179 */       if (bytesRead <= bytesToCopy) {
/* 180 */         out.write(buffer, 0, bytesRead);
/* 181 */         bytesToCopy -= bytesRead;
/*     */         continue;
/*     */       } 
/* 184 */       out.write(buffer, 0, (int)bytesToCopy);
/* 185 */       bytesToCopy = 0L;
/*     */     } 
/*     */     
/* 188 */     return end - start + 1L - bytesToCopy;
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
/*     */   public static int drain(InputStream in) throws IOException {
/* 200 */     Assert.notNull(in, "No InputStream specified");
/* 201 */     byte[] buffer = new byte[4096];
/* 202 */     int bytesRead = -1;
/* 203 */     int byteCount = 0;
/* 204 */     while ((bytesRead = in.read(buffer)) != -1) {
/* 205 */       byteCount += bytesRead;
/*     */     }
/* 207 */     return byteCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream emptyInput() {
/* 216 */     return new ByteArrayInputStream(EMPTY_CONTENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream nonClosing(InputStream in) {
/* 226 */     Assert.notNull(in, "No InputStream specified");
/* 227 */     return new NonClosingInputStream(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OutputStream nonClosing(OutputStream out) {
/* 237 */     Assert.notNull(out, "No OutputStream specified");
/* 238 */     return new NonClosingOutputStream(out);
/*     */   }
/*     */   
/*     */   private static class NonClosingInputStream
/*     */     extends FilterInputStream
/*     */   {
/*     */     public NonClosingInputStream(InputStream in) {
/* 245 */       super(in);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class NonClosingOutputStream
/*     */     extends FilterOutputStream
/*     */   {
/*     */     public NonClosingOutputStream(OutputStream out) {
/* 257 */       super(out);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int let) throws IOException {
/* 263 */       this.out.write(b, off, let);
/*     */     }
/*     */     
/*     */     public void close() throws IOException {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/StreamUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */