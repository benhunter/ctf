/*    */ package org.springframework.remoting.support;
/*    */ 
/*    */ import org.aopalliance.intercept.MethodInvocation;
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
/*    */ public class DefaultRemoteInvocationFactory
/*    */   implements RemoteInvocationFactory
/*    */ {
/*    */   public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
/* 32 */     return new RemoteInvocation(methodInvocation);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/DefaultRemoteInvocationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */