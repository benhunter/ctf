/*    */ package org.springframework.util.comparator;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InstanceComparator<T>
/*    */   implements Comparator<T>
/*    */ {
/*    */   private final Class<?>[] instanceOrder;
/*    */   
/*    */   public InstanceComparator(Class<?>... instanceOrder) {
/* 50 */     Assert.notNull(instanceOrder, "'instanceOrder' array must not be null");
/* 51 */     this.instanceOrder = instanceOrder;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(T o1, T o2) {
/* 57 */     int i1 = getOrder(o1);
/* 58 */     int i2 = getOrder(o2);
/* 59 */     return (i1 < i2) ? -1 : ((i1 == i2) ? 0 : 1);
/*    */   }
/*    */   
/*    */   private int getOrder(@Nullable T object) {
/* 63 */     if (object != null) {
/* 64 */       for (int i = 0; i < this.instanceOrder.length; i++) {
/* 65 */         if (this.instanceOrder[i].isInstance(object)) {
/* 66 */           return i;
/*    */         }
/*    */       } 
/*    */     }
/* 70 */     return this.instanceOrder.length;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/comparator/InstanceComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */