/*    */ package org.springframework.util.comparator;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ComparableComparator<T extends Comparable<T>>
/*    */   implements Comparator<T>
/*    */ {
/* 38 */   public static final ComparableComparator INSTANCE = new ComparableComparator();
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(T o1, T o2) {
/* 43 */     return o1.compareTo(o2);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/comparator/ComparableComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */