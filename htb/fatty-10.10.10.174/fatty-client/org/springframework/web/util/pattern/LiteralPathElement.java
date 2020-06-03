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
/*     */ class LiteralPathElement
/*     */   extends PathElement
/*     */ {
/*     */   private char[] text;
/*     */   private int len;
/*     */   private boolean caseSensitive;
/*     */   
/*     */   public LiteralPathElement(int pos, char[] literalText, boolean caseSensitive, char separator) {
/*  40 */     super(pos, separator);
/*  41 */     this.len = literalText.length;
/*  42 */     this.caseSensitive = caseSensitive;
/*  43 */     if (caseSensitive) {
/*  44 */       this.text = literalText;
/*     */     }
/*     */     else {
/*     */       
/*  48 */       this.text = new char[literalText.length];
/*  49 */       for (int i = 0; i < this.len; i++) {
/*  50 */         this.text[i] = Character.toLowerCase(literalText[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(int pathIndex, PathPattern.MatchingContext matchingContext) {
/*  58 */     if (pathIndex >= matchingContext.pathLength)
/*     */     {
/*  60 */       return false;
/*     */     }
/*  62 */     PathContainer.Element element = matchingContext.pathElements.get(pathIndex);
/*  63 */     if (!(element instanceof PathContainer.PathSegment)) {
/*  64 */       return false;
/*     */     }
/*  66 */     String value = ((PathContainer.PathSegment)element).valueToMatch();
/*  67 */     if (value.length() != this.len)
/*     */     {
/*  69 */       return false;
/*     */     }
/*     */     
/*  72 */     char[] data = ((PathContainer.PathSegment)element).valueToMatchAsChars();
/*  73 */     if (this.caseSensitive) {
/*  74 */       for (int i = 0; i < this.len; i++) {
/*  75 */         if (data[i] != this.text[i]) {
/*  76 */           return false;
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/*  81 */       for (int i = 0; i < this.len; i++) {
/*     */         
/*  83 */         if (Character.toLowerCase(data[i]) != this.text[i]) {
/*  84 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     pathIndex++;
/*  90 */     if (isNoMorePattern()) {
/*  91 */       if (matchingContext.determineRemainingPath) {
/*  92 */         matchingContext.remainingPathIndex = pathIndex;
/*  93 */         return true;
/*     */       } 
/*     */       
/*  96 */       if (pathIndex == matchingContext.pathLength) {
/*  97 */         return true;
/*     */       }
/*     */       
/* 100 */       return (matchingContext.isMatchOptionalTrailingSeparator() && pathIndex + 1 == matchingContext.pathLength && matchingContext
/*     */         
/* 102 */         .isSeparator(pathIndex));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 107 */     return (this.next != null && this.next.matches(pathIndex, matchingContext));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNormalizedLength() {
/* 113 */     return this.len;
/*     */   }
/*     */   
/*     */   public char[] getChars() {
/* 117 */     return this.text;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 122 */     return "Literal(" + String.valueOf(this.text) + ")";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/LiteralPathElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */