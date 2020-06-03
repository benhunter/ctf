/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public final class NullValue
/*    */   implements Serializable
/*    */ {
/* 39 */   public static final Object INSTANCE = new NullValue();
/*    */ 
/*    */ 
/*    */   
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 48 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object obj) {
/* 54 */     return (this == obj || obj == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 59 */     return NullValue.class.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 64 */     return "null";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/support/NullValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */