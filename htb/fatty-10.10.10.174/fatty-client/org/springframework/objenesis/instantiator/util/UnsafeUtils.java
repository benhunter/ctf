/*    */ package org.springframework.objenesis.instantiator.util;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import sun.misc.Unsafe;
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
/*    */ public final class UnsafeUtils
/*    */ {
/*    */   private static final Unsafe unsafe;
/*    */   
/*    */   static {
/*    */     Field f;
/*    */     try {
/* 36 */       f = Unsafe.class.getDeclaredField("theUnsafe");
/* 37 */     } catch (NoSuchFieldException e) {
/* 38 */       throw new ObjenesisException(e);
/*    */     } 
/* 40 */     f.setAccessible(true);
/*    */     try {
/* 42 */       unsafe = (Unsafe)f.get(null);
/* 43 */     } catch (IllegalAccessException e) {
/* 44 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Unsafe getUnsafe() {
/* 51 */     return unsafe;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/objenesis/instantiator/util/UnsafeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */