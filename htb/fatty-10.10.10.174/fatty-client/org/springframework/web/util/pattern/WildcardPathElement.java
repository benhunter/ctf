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
/*     */ class WildcardPathElement
/*     */   extends PathElement
/*     */ {
/*     */   public WildcardPathElement(int pos, char separator) {
/*  34 */     super(pos, separator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(int pathIndex, PathPattern.MatchingContext matchingContext) {
/*  45 */     String segmentData = null;
/*     */     
/*  47 */     if (pathIndex < matchingContext.pathLength) {
/*  48 */       PathContainer.Element element = matchingContext.pathElements.get(pathIndex);
/*  49 */       if (!(element instanceof PathContainer.PathSegment))
/*     */       {
/*  51 */         return false;
/*     */       }
/*  53 */       segmentData = ((PathContainer.PathSegment)element).valueToMatch();
/*  54 */       pathIndex++;
/*     */     } 
/*     */     
/*  57 */     if (isNoMorePattern()) {
/*  58 */       if (matchingContext.determineRemainingPath) {
/*  59 */         matchingContext.remainingPathIndex = pathIndex;
/*  60 */         return true;
/*     */       } 
/*     */       
/*  63 */       if (pathIndex == matchingContext.pathLength)
/*     */       {
/*  65 */         return true;
/*     */       }
/*     */       
/*  68 */       return (matchingContext.isMatchOptionalTrailingSeparator() && segmentData != null && segmentData
/*  69 */         .length() > 0 && pathIndex + 1 == matchingContext.pathLength && matchingContext
/*     */         
/*  71 */         .isSeparator(pathIndex));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     if (segmentData == null || segmentData.length() == 0) {
/*  78 */       return false;
/*     */     }
/*  80 */     return (this.next != null && this.next.matches(pathIndex, matchingContext));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNormalizedLength() {
/*  86 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWildcardCount() {
/*  91 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getScore() {
/*  96 */     return 100;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     return "Wildcard(*)";
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] getChars() {
/* 106 */     return new char[] { '*' };
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/WildcardPathElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */