/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
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
/*    */ final class NullBean
/*    */ {
/*    */   public boolean equals(@Nullable Object obj) {
/* 44 */     return (this == obj || obj == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 49 */     return NullBean.class.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "null";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/NullBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */