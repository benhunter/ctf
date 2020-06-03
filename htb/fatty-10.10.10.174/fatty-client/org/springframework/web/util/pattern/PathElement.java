/*     */ package org.springframework.web.util.pattern;
/*     */ 
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class PathElement
/*     */ {
/*     */   protected static final int WILDCARD_WEIGHT = 100;
/*     */   protected static final int CAPTURE_VARIABLE_WEIGHT = 1;
/*  37 */   protected static final MultiValueMap<String, String> NO_PARAMETERS = (MultiValueMap<String, String>)new LinkedMultiValueMap();
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int pos;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final char separator;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected PathElement next;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected PathElement prev;
/*     */ 
/*     */ 
/*     */   
/*     */   PathElement(int pos, char separator) {
/*  60 */     this.pos = pos;
/*  61 */     this.separator = separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean matches(int paramInt, PathPattern.MatchingContext paramMatchingContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getNormalizedLength();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract char[] getChars();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCaptureCount() {
/*  85 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWildcardCount() {
/*  92 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScore() {
/*  99 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean isNoMorePattern() {
/* 107 */     return (this.next == null);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/PathElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */