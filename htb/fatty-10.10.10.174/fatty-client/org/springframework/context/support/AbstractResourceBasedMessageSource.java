/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractResourceBasedMessageSource
/*     */   extends AbstractMessageSource
/*     */ {
/*  39 */   private final Set<String> basenameSet = new LinkedHashSet<>(4);
/*     */   
/*     */   @Nullable
/*     */   private String defaultEncoding;
/*     */   
/*     */   private boolean fallbackToSystemLocale = true;
/*     */   
/*  46 */   private long cacheMillis = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBasename(String basename) {
/*  62 */     setBasenames(new String[] { basename });
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
/*     */   public void setBasenames(String... basenames) {
/*  82 */     this.basenameSet.clear();
/*  83 */     addBasenames(basenames);
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
/*     */   public void addBasenames(String... basenames) {
/*  96 */     if (!ObjectUtils.isEmpty((Object[])basenames)) {
/*  97 */       for (String basename : basenames) {
/*  98 */         Assert.hasText(basename, "Basename must not be empty");
/*  99 */         this.basenameSet.add(basename.trim());
/*     */       } 
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
/*     */   public Set<String> getBasenameSet() {
/* 112 */     return this.basenameSet;
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
/*     */   public void setDefaultEncoding(@Nullable String defaultEncoding) {
/* 125 */     this.defaultEncoding = defaultEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getDefaultEncoding() {
/* 134 */     return this.defaultEncoding;
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
/*     */   public void setFallbackToSystemLocale(boolean fallbackToSystemLocale) {
/* 148 */     this.fallbackToSystemLocale = fallbackToSystemLocale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isFallbackToSystemLocale() {
/* 157 */     return this.fallbackToSystemLocale;
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
/*     */   public void setCacheSeconds(int cacheSeconds) {
/* 180 */     this.cacheMillis = (cacheSeconds * 1000);
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
/*     */   public void setCacheMillis(long cacheMillis) {
/* 201 */     this.cacheMillis = cacheMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getCacheMillis() {
/* 209 */     return this.cacheMillis;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/AbstractResourceBasedMessageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */