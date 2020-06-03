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
/*    */ 
/*    */ 
/*    */ 
/*    */ class SeparatorPathElement
/*    */   extends PathElement
/*    */ {
/*    */   SeparatorPathElement(int pos, char separator) {
/* 32 */     super(pos, separator);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(int pathIndex, PathPattern.MatchingContext matchingContext) {
/* 42 */     if (pathIndex < matchingContext.pathLength && matchingContext.isSeparator(pathIndex)) {
/* 43 */       if (isNoMorePattern()) {
/* 44 */         if (matchingContext.determineRemainingPath) {
/* 45 */           matchingContext.remainingPathIndex = pathIndex + 1;
/* 46 */           return true;
/*    */         } 
/*    */         
/* 49 */         return (pathIndex + 1 == matchingContext.pathLength);
/*    */       } 
/*    */ 
/*    */       
/* 53 */       pathIndex++;
/* 54 */       return (this.next != null && this.next.matches(pathIndex, matchingContext));
/*    */     } 
/*    */     
/* 57 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getNormalizedLength() {
/* 62 */     return 1;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 66 */     return "Separator(" + this.separator + ")";
/*    */   }
/*    */   
/*    */   public char[] getChars() {
/* 70 */     return new char[] { this.separator };
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/SeparatorPathElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */