/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import java.security.AccessControlContext;
/*    */ import java.security.AccessController;
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
/*    */ public class SimpleSecurityContextProvider
/*    */   implements SecurityContextProvider
/*    */ {
/*    */   @Nullable
/*    */   private final AccessControlContext acc;
/*    */   
/*    */   public SimpleSecurityContextProvider() {
/* 42 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleSecurityContextProvider(@Nullable AccessControlContext acc) {
/* 53 */     this.acc = acc;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AccessControlContext getAccessControlContext() {
/* 59 */     return (this.acc != null) ? this.acc : AccessController.getContext();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/SimpleSecurityContextProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */