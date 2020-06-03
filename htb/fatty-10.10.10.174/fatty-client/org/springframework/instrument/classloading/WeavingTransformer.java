/*     */ package org.springframework.instrument.classloading;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.instrument.IllegalClassFormatException;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class WeavingTransformer
/*     */ {
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*  46 */   private final List<ClassFileTransformer> transformers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WeavingTransformer(@Nullable ClassLoader classLoader) {
/*  54 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*  63 */     Assert.notNull(transformer, "Transformer must not be null");
/*  64 */     this.transformers.add(transformer);
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
/*     */   public byte[] transformIfNecessary(String className, byte[] bytes) {
/*  77 */     String internalName = StringUtils.replace(className, ".", "/");
/*  78 */     return transformIfNecessary(className, internalName, bytes, null);
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
/*     */   public byte[] transformIfNecessary(String className, String internalName, byte[] bytes, @Nullable ProtectionDomain pd) {
/*  92 */     byte[] result = bytes;
/*  93 */     for (ClassFileTransformer cft : this.transformers) {
/*     */       try {
/*  95 */         byte[] transformed = cft.transform(this.classLoader, internalName, null, pd, result);
/*  96 */         if (transformed != null) {
/*  97 */           result = transformed;
/*     */         }
/*     */       }
/* 100 */       catch (IllegalClassFormatException ex) {
/* 101 */         throw new IllegalStateException("Class file transformation failed", ex);
/*     */       } 
/*     */     } 
/* 104 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/WeavingTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */