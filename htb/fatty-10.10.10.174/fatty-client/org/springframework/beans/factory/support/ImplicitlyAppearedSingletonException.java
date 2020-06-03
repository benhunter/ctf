/*    */ package org.springframework.beans.factory.support;
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
/*    */ class ImplicitlyAppearedSingletonException
/*    */   extends IllegalStateException
/*    */ {
/*    */   public ImplicitlyAppearedSingletonException() {
/* 31 */     super("About-to-be-created singleton instance implicitly appeared through the creation of the factory bean that its bean definition points to");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/ImplicitlyAppearedSingletonException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */