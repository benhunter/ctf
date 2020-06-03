/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.rmi.Naming;
/*     */ import java.rmi.NotBoundException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.registry.LocateRegistry;
/*     */ import java.rmi.registry.Registry;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteInvocationFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.support.RemoteInvocationBasedAccessor;
/*     */ import org.springframework.remoting.support.RemoteInvocationUtils;
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
/*     */ public class RmiClientInterceptor
/*     */   extends RemoteInvocationBasedAccessor
/*     */   implements MethodInterceptor
/*     */ {
/*     */   private boolean lookupStubOnStartup = true;
/*     */   private boolean cacheStub = true;
/*     */   private boolean refreshStubOnConnectFailure = false;
/*     */   private RMIClientSocketFactory registryClientSocketFactory;
/*     */   private Remote cachedStub;
/*  84 */   private final Object stubMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
/*  94 */     this.lookupStubOnStartup = lookupStubOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheStub(boolean cacheStub) {
/* 105 */     this.cacheStub = cacheStub;
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
/*     */   public void setRefreshStubOnConnectFailure(boolean refreshStubOnConnectFailure) {
/* 120 */     this.refreshStubOnConnectFailure = refreshStubOnConnectFailure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegistryClientSocketFactory(RMIClientSocketFactory registryClientSocketFactory) {
/* 129 */     this.registryClientSocketFactory = registryClientSocketFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 135 */     super.afterPropertiesSet();
/* 136 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws RemoteLookupFailureException {
/* 147 */     if (this.lookupStubOnStartup) {
/* 148 */       Remote remoteObj = lookupStub();
/* 149 */       if (this.logger.isDebugEnabled()) {
/* 150 */         if (remoteObj instanceof RmiInvocationHandler) {
/* 151 */           this.logger.debug("RMI stub [" + getServiceUrl() + "] is an RMI invoker");
/*     */         }
/* 153 */         else if (getServiceInterface() != null) {
/* 154 */           boolean isImpl = getServiceInterface().isInstance(remoteObj);
/* 155 */           this.logger.debug("Using service interface [" + getServiceInterface().getName() + "] for RMI stub [" + 
/* 156 */               getServiceUrl() + "] - " + (!isImpl ? "not " : "") + "directly implemented");
/*     */         } 
/*     */       }
/*     */       
/* 160 */       if (this.cacheStub) {
/* 161 */         this.cachedStub = remoteObj;
/*     */       }
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
/*     */   protected Remote lookupStub() throws RemoteLookupFailureException {
/*     */     try {
/* 179 */       Remote stub = null;
/* 180 */       if (this.registryClientSocketFactory != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 185 */         URL url = new URL(null, getServiceUrl(), new DummyURLStreamHandler());
/* 186 */         String protocol = url.getProtocol();
/* 187 */         if (protocol != null && !"rmi".equals(protocol)) {
/* 188 */           throw new MalformedURLException("Invalid URL scheme '" + protocol + "'");
/*     */         }
/* 190 */         String host = url.getHost();
/* 191 */         int port = url.getPort();
/* 192 */         String name = url.getPath();
/* 193 */         if (name != null && name.startsWith("/")) {
/* 194 */           name = name.substring(1);
/*     */         }
/* 196 */         Registry registry = LocateRegistry.getRegistry(host, port, this.registryClientSocketFactory);
/* 197 */         stub = registry.lookup(name);
/*     */       }
/*     */       else {
/*     */         
/* 201 */         stub = Naming.lookup(getServiceUrl());
/*     */       } 
/* 203 */       if (this.logger.isDebugEnabled()) {
/* 204 */         this.logger.debug("Located RMI stub with URL [" + getServiceUrl() + "]");
/*     */       }
/* 206 */       return stub;
/*     */     }
/* 208 */     catch (MalformedURLException ex) {
/* 209 */       throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
/*     */     }
/* 211 */     catch (NotBoundException ex) {
/* 212 */       throw new RemoteLookupFailureException("Could not find RMI service [" + 
/* 213 */           getServiceUrl() + "] in RMI registry", ex);
/*     */     }
/* 215 */     catch (RemoteException ex) {
/* 216 */       throw new RemoteLookupFailureException("Lookup of RMI stub failed", ex);
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
/*     */   protected Remote getStub() throws RemoteLookupFailureException {
/* 232 */     if (!this.cacheStub || (this.lookupStubOnStartup && !this.refreshStubOnConnectFailure)) {
/* 233 */       return (this.cachedStub != null) ? this.cachedStub : lookupStub();
/*     */     }
/*     */     
/* 236 */     synchronized (this.stubMonitor) {
/* 237 */       if (this.cachedStub == null) {
/* 238 */         this.cachedStub = lookupStub();
/*     */       }
/* 240 */       return this.cachedStub;
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
/*     */   
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 259 */     Remote stub = getStub();
/*     */     try {
/* 261 */       return doInvoke(invocation, stub);
/*     */     }
/* 263 */     catch (RemoteConnectFailureException ex) {
/* 264 */       return handleRemoteConnectFailure(invocation, (Exception)ex);
/*     */     }
/* 266 */     catch (RemoteException ex) {
/* 267 */       if (isConnectFailure(ex)) {
/* 268 */         return handleRemoteConnectFailure(invocation, ex);
/*     */       }
/*     */       
/* 271 */       throw ex;
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
/*     */   protected boolean isConnectFailure(RemoteException ex) {
/* 284 */     return RmiClientInterceptorUtils.isConnectFailure(ex);
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
/*     */   @Nullable
/*     */   private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex) throws Throwable {
/* 301 */     if (this.refreshStubOnConnectFailure) {
/* 302 */       String msg = "Could not connect to RMI service [" + getServiceUrl() + "] - retrying";
/* 303 */       if (this.logger.isDebugEnabled()) {
/* 304 */         this.logger.warn(msg, ex);
/*     */       }
/* 306 */       else if (this.logger.isWarnEnabled()) {
/* 307 */         this.logger.warn(msg);
/*     */       } 
/* 309 */       return refreshAndRetry(invocation);
/*     */     } 
/*     */     
/* 312 */     throw ex;
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
/*     */   @Nullable
/*     */   protected Object refreshAndRetry(MethodInvocation invocation) throws Throwable {
/* 326 */     Remote freshStub = null;
/* 327 */     synchronized (this.stubMonitor) {
/* 328 */       this.cachedStub = null;
/* 329 */       freshStub = lookupStub();
/* 330 */       if (this.cacheStub) {
/* 331 */         this.cachedStub = freshStub;
/*     */       }
/*     */     } 
/* 334 */     return doInvoke(invocation, freshStub);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object doInvoke(MethodInvocation invocation, Remote stub) throws Throwable {
/* 346 */     if (stub instanceof RmiInvocationHandler) {
/*     */       
/*     */       try {
/* 349 */         return doInvoke(invocation, (RmiInvocationHandler)stub);
/*     */       }
/* 351 */       catch (RemoteException ex) {
/* 352 */         throw RmiClientInterceptorUtils.convertRmiAccessException(invocation
/* 353 */             .getMethod(), ex, isConnectFailure(ex), getServiceUrl());
/*     */       }
/* 355 */       catch (InvocationTargetException ex) {
/* 356 */         Throwable exToThrow = ex.getTargetException();
/* 357 */         RemoteInvocationUtils.fillInClientStackTraceIfPossible(exToThrow);
/* 358 */         throw exToThrow;
/*     */       }
/* 360 */       catch (Throwable ex) {
/* 361 */         throw new RemoteInvocationFailureException("Invocation of method [" + invocation.getMethod() + "] failed in RMI service [" + 
/* 362 */             getServiceUrl() + "]", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 368 */       return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, stub);
/*     */     }
/* 370 */     catch (InvocationTargetException ex) {
/* 371 */       Throwable targetEx = ex.getTargetException();
/* 372 */       if (targetEx instanceof RemoteException) {
/* 373 */         RemoteException rex = (RemoteException)targetEx;
/* 374 */         throw RmiClientInterceptorUtils.convertRmiAccessException(invocation
/* 375 */             .getMethod(), rex, isConnectFailure(rex), getServiceUrl());
/*     */       } 
/*     */       
/* 378 */       throw targetEx;
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object doInvoke(MethodInvocation methodInvocation, RmiInvocationHandler invocationHandler) throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 400 */     if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
/* 401 */       return "RMI invoker proxy for service URL [" + getServiceUrl() + "]";
/*     */     }
/*     */     
/* 404 */     return invocationHandler.invoke(createRemoteInvocation(methodInvocation));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DummyURLStreamHandler
/*     */     extends URLStreamHandler
/*     */   {
/*     */     private DummyURLStreamHandler() {}
/*     */ 
/*     */ 
/*     */     
/*     */     protected URLConnection openConnection(URL url) throws IOException {
/* 417 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/rmi/RmiClientInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */