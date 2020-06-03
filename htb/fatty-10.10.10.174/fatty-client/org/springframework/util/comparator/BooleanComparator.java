/*    */ package org.springframework.util.comparator;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
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
/*    */ 
/*    */ public class BooleanComparator
/*    */   implements Comparator<Boolean>, Serializable
/*    */ {
/* 36 */   public static final BooleanComparator TRUE_LOW = new BooleanComparator(true);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static final BooleanComparator TRUE_HIGH = new BooleanComparator(false);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean trueLow;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BooleanComparator(boolean trueLow) {
/* 59 */     this.trueLow = trueLow;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(Boolean v1, Boolean v2) {
/* 65 */     return ((v1.booleanValue() ^ v2.booleanValue()) != 0) ? (((v1.booleanValue() ^ this.trueLow) != 0) ? 1 : -1) : 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 71 */     return (this == other || (other instanceof BooleanComparator && this.trueLow == ((BooleanComparator)other).trueLow));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 77 */     return getClass().hashCode() * (this.trueLow ? -1 : 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     return "BooleanComparator: " + (this.trueLow ? "true low" : "true high");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/comparator/BooleanComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */