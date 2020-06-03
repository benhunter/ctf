/*    */ package org.springframework.aop.framework.adapter;
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
/*    */ public final class GlobalAdvisorAdapterRegistry
/*    */ {
/* 36 */   private static AdvisorAdapterRegistry instance = new DefaultAdvisorAdapterRegistry();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AdvisorAdapterRegistry getInstance() {
/* 42 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void reset() {
/* 51 */     instance = new DefaultAdvisorAdapterRegistry();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/adapter/GlobalAdvisorAdapterRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */