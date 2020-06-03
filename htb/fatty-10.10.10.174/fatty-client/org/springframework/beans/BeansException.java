/*    */ package org.springframework.beans;
/*    */ 
/*    */ import org.springframework.core.NestedRuntimeException;
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
/*    */ public abstract class BeansException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public BeansException(String msg) {
/* 40 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BeansException(@Nullable String msg, @Nullable Throwable cause) {
/* 50 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/BeansException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */