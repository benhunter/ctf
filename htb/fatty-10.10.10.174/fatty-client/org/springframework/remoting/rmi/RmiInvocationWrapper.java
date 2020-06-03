/*    */ package org.springframework.remoting.rmi;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.rmi.RemoteException;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.remoting.support.RemoteInvocation;
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
/*    */ class RmiInvocationWrapper
/*    */   implements RmiInvocationHandler
/*    */ {
/*    */   private final Object wrappedObject;
/*    */   private final RmiBasedExporter rmiExporter;
/*    */   
/*    */   public RmiInvocationWrapper(Object wrappedObject, RmiBasedExporter rmiExporter) {
/* 50 */     Assert.notNull(wrappedObject, "Object to wrap is required");
/* 51 */     Assert.notNull(rmiExporter, "RMI exporter is required");
/* 52 */     this.wrappedObject = wrappedObject;
/* 53 */     this.rmiExporter = rmiExporter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getTargetInterfaceName() {
/* 64 */     Class<?> ifc = this.rmiExporter.getServiceInterface();
/* 65 */     return (ifc != null) ? ifc.getName() : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object invoke(RemoteInvocation invocation) throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 77 */     return this.rmiExporter.invoke(invocation, this.wrappedObject);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/rmi/RmiInvocationWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */