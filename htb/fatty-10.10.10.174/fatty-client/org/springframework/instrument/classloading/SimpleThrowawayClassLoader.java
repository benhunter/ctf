/*    */ package org.springframework.instrument.classloading;
/*    */ 
/*    */ import org.springframework.core.OverridingClassLoader;
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
/*    */ public class SimpleThrowawayClassLoader
/*    */   extends OverridingClassLoader
/*    */ {
/*    */   static {
/* 33 */     ClassLoader.registerAsParallelCapable();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleThrowawayClassLoader(@Nullable ClassLoader parent) {
/* 42 */     super(parent);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/SimpleThrowawayClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */