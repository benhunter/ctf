/*    */ package org.springframework.remoting.support;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
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
/*    */ public class DefaultRemoteInvocationExecutor
/*    */   implements RemoteInvocationExecutor
/*    */ {
/*    */   public Object invoke(RemoteInvocation invocation, Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 37 */     Assert.notNull(invocation, "RemoteInvocation must not be null");
/* 38 */     Assert.notNull(targetObject, "Target object must not be null");
/* 39 */     return invocation.invoke(targetObject);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/DefaultRemoteInvocationExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */