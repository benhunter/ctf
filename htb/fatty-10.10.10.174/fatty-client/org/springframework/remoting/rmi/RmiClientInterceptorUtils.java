/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.RemoteException;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.remoting.RemoteAccessException;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteProxyFailureException;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RmiClientInterceptorUtils
/*     */ {
/*  50 */   private static final Log logger = LogFactory.getLog(RmiClientInterceptorUtils.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Object invokeRemoteMethod(MethodInvocation invocation, Object stub) throws InvocationTargetException {
/*  65 */     Method method = invocation.getMethod();
/*     */     try {
/*  67 */       if (method.getDeclaringClass().isInstance(stub))
/*     */       {
/*  69 */         return method.invoke(stub, invocation.getArguments());
/*     */       }
/*     */ 
/*     */       
/*  73 */       Method stubMethod = stub.getClass().getMethod(method.getName(), method.getParameterTypes());
/*  74 */       return stubMethod.invoke(stub, invocation.getArguments());
/*     */     
/*     */     }
/*  77 */     catch (InvocationTargetException ex) {
/*  78 */       throw ex;
/*     */     }
/*  80 */     catch (NoSuchMethodException ex) {
/*  81 */       throw new RemoteProxyFailureException("No matching RMI stub method found for: " + method, ex);
/*     */     }
/*  83 */     catch (Throwable ex) {
/*  84 */       throw new RemoteProxyFailureException("Invocation of RMI stub method failed: " + method, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Exception convertRmiAccessException(Method method, Throwable ex, String message) {
/* 102 */     if (logger.isDebugEnabled()) {
/* 103 */       logger.debug(message, ex);
/*     */     }
/* 105 */     if (ReflectionUtils.declaresException(method, RemoteException.class)) {
/* 106 */       return new RemoteException(message, ex);
/*     */     }
/*     */     
/* 109 */     return (Exception)new RemoteAccessException(message, ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Exception convertRmiAccessException(Method method, RemoteException ex, String serviceName) {
/* 123 */     return convertRmiAccessException(method, ex, isConnectFailure(ex), serviceName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Exception convertRmiAccessException(Method method, RemoteException ex, boolean isConnectFailure, String serviceName) {
/* 140 */     if (logger.isDebugEnabled()) {
/* 141 */       logger.debug("Remote service [" + serviceName + "] threw exception", ex);
/*     */     }
/* 143 */     if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 144 */       return ex;
/*     */     }
/*     */     
/* 147 */     if (isConnectFailure) {
/* 148 */       return (Exception)new RemoteConnectFailureException("Could not connect to remote service [" + serviceName + "]", ex);
/*     */     }
/*     */     
/* 151 */     return (Exception)new RemoteAccessException("Could not access remote service [" + serviceName + "]", ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isConnectFailure(RemoteException ex) {
/* 169 */     return (ex instanceof java.rmi.ConnectException || ex instanceof java.rmi.ConnectIOException || ex instanceof java.rmi.UnknownHostException || ex instanceof java.rmi.NoSuchObjectException || ex instanceof java.rmi.StubNotFoundException || ex
/*     */       
/* 171 */       .getCause() instanceof java.net.SocketException);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/rmi/RmiClientInterceptorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */