/*     */ package org.springframework.web.util.pattern;
/*     */ 
/*     */ import org.springframework.http.server.PathContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SingleCharWildcardedPathElement
/*     */   extends PathElement
/*     */ {
/*     */   private final char[] text;
/*     */   private final int len;
/*     */   private final int questionMarkCount;
/*     */   private final boolean caseSensitive;
/*     */   
/*     */   public SingleCharWildcardedPathElement(int pos, char[] literalText, int questionMarkCount, boolean caseSensitive, char separator) {
/*  44 */     super(pos, separator);
/*  45 */     this.len = literalText.length;
/*  46 */     this.questionMarkCount = questionMarkCount;
/*  47 */     this.caseSensitive = caseSensitive;
/*  48 */     if (caseSensitive) {
/*  49 */       this.text = literalText;
/*     */     } else {
/*     */       
/*  52 */       this.text = new char[literalText.length];
/*  53 */       for (int i = 0; i < this.len; i++) {
/*  54 */         this.text[i] = Character.toLowerCase(literalText[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(int pathIndex, PathPattern.MatchingContext matchingContext) {
/*  62 */     if (pathIndex >= matchingContext.pathLength)
/*     */     {
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     PathContainer.Element element = matchingContext.pathElements.get(pathIndex);
/*  68 */     if (!(element instanceof PathContainer.PathSegment)) {
/*  69 */       return false;
/*     */     }
/*  71 */     String value = ((PathContainer.PathSegment)element).valueToMatch();
/*  72 */     if (value.length() != this.len)
/*     */     {
/*  74 */       return false;
/*     */     }
/*     */     
/*  77 */     char[] data = ((PathContainer.PathSegment)element).valueToMatchAsChars();
/*  78 */     if (this.caseSensitive) {
/*  79 */       for (int i = 0; i < this.len; i++) {
/*  80 */         char ch = this.text[i];
/*  81 */         if (ch != '?' && ch != data[i]) {
/*  82 */           return false;
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/*  87 */       for (int i = 0; i < this.len; i++) {
/*  88 */         char ch = this.text[i];
/*     */         
/*  90 */         if (ch != '?' && ch != Character.toLowerCase(data[i])) {
/*  91 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     pathIndex++;
/*  97 */     if (isNoMorePattern()) {
/*  98 */       if (matchingContext.determineRemainingPath) {
/*  99 */         matchingContext.remainingPathIndex = pathIndex;
/* 100 */         return true;
/*     */       } 
/*     */       
/* 103 */       if (pathIndex == matchingContext.pathLength) {
/* 104 */         return true;
/*     */       }
/*     */       
/* 107 */       return (matchingContext.isMatchOptionalTrailingSeparator() && pathIndex + 1 == matchingContext.pathLength && matchingContext
/*     */         
/* 109 */         .isSeparator(pathIndex));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 114 */     return (this.next != null && this.next.matches(pathIndex, matchingContext));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWildcardCount() {
/* 120 */     return this.questionMarkCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNormalizedLength() {
/* 125 */     return this.len;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 130 */     return "SingleCharWildcarded(" + String.valueOf(this.text) + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] getChars() {
/* 135 */     return this.text;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/SingleCharWildcardedPathElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */