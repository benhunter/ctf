/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
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
/*     */ public abstract class FileSystemUtils
/*     */ {
/*     */   public static boolean deleteRecursively(@Nullable File root) {
/*  55 */     if (root == null) {
/*  56 */       return false;
/*     */     }
/*     */     
/*     */     try {
/*  60 */       return deleteRecursively(root.toPath());
/*     */     }
/*  62 */     catch (IOException ex) {
/*  63 */       return false;
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
/*     */   
/*     */   public static boolean deleteRecursively(@Nullable Path root) throws IOException {
/*  77 */     if (root == null) {
/*  78 */       return false;
/*     */     }
/*  80 */     if (!Files.exists(root, new java.nio.file.LinkOption[0])) {
/*  81 */       return false;
/*     */     }
/*     */     
/*  84 */     Files.walkFileTree(root, new SimpleFileVisitor<Path>()
/*     */         {
/*     */           public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/*  87 */             Files.delete(file);
/*  88 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */           
/*     */           public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/*  92 */             Files.delete(dir);
/*  93 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */         });
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyRecursively(File src, File dest) throws IOException {
/* 107 */     Assert.notNull(src, "Source File must not be null");
/* 108 */     Assert.notNull(dest, "Destination File must not be null");
/* 109 */     copyRecursively(src.toPath(), dest.toPath());
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
/*     */   public static void copyRecursively(final Path src, final Path dest) throws IOException {
/* 121 */     Assert.notNull(src, "Source Path must not be null");
/* 122 */     Assert.notNull(dest, "Destination Path must not be null");
/* 123 */     BasicFileAttributes srcAttr = Files.readAttributes(src, BasicFileAttributes.class, new java.nio.file.LinkOption[0]);
/*     */     
/* 125 */     if (srcAttr.isDirectory()) {
/* 126 */       Files.walkFileTree(src, new SimpleFileVisitor<Path>()
/*     */           {
/*     */             public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
/* 129 */               Files.createDirectories(dest.resolve(src.relativize(dir)), (FileAttribute<?>[])new FileAttribute[0]);
/* 130 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */             
/*     */             public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 134 */               Files.copy(file, dest.resolve(src.relativize(file)), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 135 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */           });
/*     */     }
/* 139 */     else if (srcAttr.isRegularFile()) {
/* 140 */       Files.copy(src, dest, new CopyOption[0]);
/*     */     } else {
/*     */       
/* 143 */       throw new IllegalArgumentException("Source File must denote a directory or file");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/FileSystemUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */