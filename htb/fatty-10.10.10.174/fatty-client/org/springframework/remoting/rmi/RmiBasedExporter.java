/*    */ package org.springframework.remoting.rmi;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.rmi.Remote;
/*    */ import org.springframework.remoting.support.RemoteInvocation;
/*    */ import org.springframework.remoting.support.RemoteInvocationBasedExporter;
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
/*    */ public abstract class RmiBasedExporter
/*    */   extends RemoteInvocationBasedExporter
/*    */ {
/*    */   protected Remote getObjectToExport() {
/* 51 */     if (getService() instanceof Remote && (
/* 52 */       getServiceInterface() == null || Remote.class.isAssignableFrom(getServiceInterface())))
/*    */     {
/* 54 */       return (Remote)getService();
/*    */     }
/*    */ 
/*    */     
/* 58 */     if (this.logger.isDebugEnabled()) {
/* 59 */       this.logger.debug("RMI service [" + getService() + "] is an RMI invoker");
/*    */     }
/* 61 */     return new RmiInvocationWrapper(getProxyForService(), this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object invoke(RemoteInvocation invocation, Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 73 */     return super.invoke(invocation, targetObject);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/rmi/RmiBasedExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */