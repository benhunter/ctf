/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.util.Hashtable;
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
/*    */ public final class ThreadLocalMap
/*    */   extends InheritableThreadLocal
/*    */ {
/*    */   public final Object childValue(Object parentValue) {
/* 35 */     Hashtable ht = (Hashtable)parentValue;
/* 36 */     if (ht != null) {
/* 37 */       return ht.clone();
/*    */     }
/* 39 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/helpers/ThreadLocalMap.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */