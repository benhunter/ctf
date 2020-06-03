/*    */ package org.springframework.remoting.support;
/*    */ 
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RemoteAccessor
/*    */   extends RemotingSupport
/*    */ {
/*    */   private Class<?> serviceInterface;
/*    */   
/*    */   public void setServiceInterface(Class<?> serviceInterface) {
/* 51 */     Assert.notNull(serviceInterface, "'serviceInterface' must not be null");
/* 52 */     Assert.isTrue(serviceInterface.isInterface(), "'serviceInterface' must be an interface");
/* 53 */     this.serviceInterface = serviceInterface;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getServiceInterface() {
/* 60 */     return this.serviceInterface;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/RemoteAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */