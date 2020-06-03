/*    */ package org.springframework.web.util.pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class WildcardTheRestPathElement
/*    */   extends PathElement
/*    */ {
/*    */   WildcardTheRestPathElement(int pos, char separator) {
/* 29 */     super(pos, separator);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(int pathIndex, PathPattern.MatchingContext matchingContext) {
/* 36 */     if (pathIndex < matchingContext.pathLength && !matchingContext.isSeparator(pathIndex)) {
/* 37 */       return false;
/*    */     }
/* 39 */     if (matchingContext.determineRemainingPath) {
/* 40 */       matchingContext.remainingPathIndex = matchingContext.pathLength;
/*    */     }
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getNormalizedLength() {
/* 47 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWildcardCount() {
/* 52 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "WildcardTheRest(" + this.separator + "**)";
/*    */   }
/*    */ 
/*    */   
/*    */   public char[] getChars() {
/* 62 */     return (this.separator + "**").toCharArray();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/WildcardTheRestPathElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */