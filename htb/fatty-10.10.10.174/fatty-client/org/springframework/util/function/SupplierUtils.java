/*    */ package org.springframework.util.function;
/*    */ 
/*    */ import java.util.function.Supplier;
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
/*    */ public abstract class SupplierUtils
/*    */ {
/*    */   @Nullable
/*    */   public static <T> T resolve(@Nullable Supplier<T> supplier) {
/* 40 */     return (supplier != null) ? supplier.get() : null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/function/SupplierUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */