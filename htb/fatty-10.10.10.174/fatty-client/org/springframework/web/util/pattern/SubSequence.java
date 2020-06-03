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
/*    */ 
/*    */ class SubSequence
/*    */   implements CharSequence
/*    */ {
/*    */   private final char[] chars;
/*    */   private final int start;
/*    */   private final int end;
/*    */   
/*    */   SubSequence(char[] chars, int start, int end) {
/* 37 */     this.chars = chars;
/* 38 */     this.start = start;
/* 39 */     this.end = end;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int length() {
/* 45 */     return this.end - this.start;
/*    */   }
/*    */ 
/*    */   
/*    */   public char charAt(int index) {
/* 50 */     return this.chars[this.start + index];
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence subSequence(int start, int end) {
/* 55 */     return new SubSequence(this.chars, this.start + start, this.start + end);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return new String(this.chars, this.start, this.end - this.start);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/SubSequence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */