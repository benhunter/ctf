/*     */ package org.springframework.expression.spel;
/*     */ 
/*     */ import org.springframework.core.SpringProperties;
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
/*     */ public class SpelParserConfiguration
/*     */ {
/*     */   private static final SpelCompilerMode defaultCompilerMode;
/*     */   private final SpelCompilerMode compilerMode;
/*     */   @Nullable
/*     */   private final ClassLoader compilerClassLoader;
/*     */   private final boolean autoGrowNullReferences;
/*     */   private final boolean autoGrowCollections;
/*     */   private final int maximumAutoGrowSize;
/*     */   
/*     */   static {
/*  36 */     String compilerMode = SpringProperties.getProperty("spring.expression.compiler.mode");
/*     */     
/*  38 */     defaultCompilerMode = (compilerMode != null) ? SpelCompilerMode.valueOf(compilerMode.toUpperCase()) : SpelCompilerMode.OFF;
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
/*     */   public SpelParserConfiguration() {
/*  58 */     this(null, null, false, false, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelParserConfiguration(@Nullable SpelCompilerMode compilerMode, @Nullable ClassLoader compilerClassLoader) {
/*  67 */     this(compilerMode, compilerClassLoader, false, false, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelParserConfiguration(boolean autoGrowNullReferences, boolean autoGrowCollections) {
/*  77 */     this(null, null, autoGrowNullReferences, autoGrowCollections, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelParserConfiguration(boolean autoGrowNullReferences, boolean autoGrowCollections, int maximumAutoGrowSize) {
/*  87 */     this(null, null, autoGrowNullReferences, autoGrowCollections, maximumAutoGrowSize);
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
/*     */   public SpelParserConfiguration(@Nullable SpelCompilerMode compilerMode, @Nullable ClassLoader compilerClassLoader, boolean autoGrowNullReferences, boolean autoGrowCollections, int maximumAutoGrowSize) {
/* 101 */     this.compilerMode = (compilerMode != null) ? compilerMode : defaultCompilerMode;
/* 102 */     this.compilerClassLoader = compilerClassLoader;
/* 103 */     this.autoGrowNullReferences = autoGrowNullReferences;
/* 104 */     this.autoGrowCollections = autoGrowCollections;
/* 105 */     this.maximumAutoGrowSize = maximumAutoGrowSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelCompilerMode getCompilerMode() {
/* 113 */     return this.compilerMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader getCompilerClassLoader() {
/* 121 */     return this.compilerClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowNullReferences() {
/* 128 */     return this.autoGrowNullReferences;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowCollections() {
/* 135 */     return this.autoGrowCollections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaximumAutoGrowSize() {
/* 142 */     return this.maximumAutoGrowSize;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/SpelParserConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */