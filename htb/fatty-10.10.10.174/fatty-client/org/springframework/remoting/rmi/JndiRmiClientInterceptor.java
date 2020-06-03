/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.RemoteException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jndi.JndiObjectLocator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteInvocationFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.support.DefaultRemoteInvocationFactory;
/*     */ import org.springframework.remoting.support.RemoteInvocation;
/*     */ import org.springframework.remoting.support.RemoteInvocationFactory;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiRmiClientInterceptor
/*     */   extends JndiObjectLocator
/*     */   implements MethodInterceptor, InitializingBean
/*     */ {
/*     */   private Class<?> serviceInterface;
/*  79 */   private RemoteInvocationFactory remoteInvocationFactory = (RemoteInvocationFactory)new DefaultRemoteInvocationFactory();
/*     */   
/*     */   private boolean lookupStubOnStartup = true;
/*     */   
/*     */   private boolean cacheStub = true;
/*     */   
/*     */   private boolean refreshStubOnConnectFailure = false;
/*     */   
/*     */   private boolean exposeAccessContext = false;
/*     */   
/*     */   private Object cachedStub;
/*     */   
/*  91 */   private final Object stubMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceInterface(Class<?> serviceInterface) {
/* 101 */     Assert.notNull(serviceInterface, "'serviceInterface' must not be null");
/* 102 */     Assert.isTrue(serviceInterface.isInterface(), "'serviceInterface' must be an interface");
/* 103 */     this.serviceInterface = serviceInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getServiceInterface() {
/* 110 */     return this.serviceInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteInvocationFactory(RemoteInvocationFactory remoteInvocationFactory) {
/* 120 */     this.remoteInvocationFactory = remoteInvocationFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteInvocationFactory getRemoteInvocationFactory() {
/* 127 */     return this.remoteInvocationFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
/* 137 */     this.lookupStubOnStartup = lookupStubOnStartup;
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
/* 148 */     this.cacheStub = cacheStub;
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
/* 163 */     this.refreshStubOnConnectFailure = refreshStubOnConnectFailure;
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
/*     */   public void setExposeAccessContext(boolean exposeAccessContext) {
/* 175 */     this.exposeAccessContext = exposeAccessContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/* 181 */     super.afterPropertiesSet();
/* 182 */     prepare();
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
/* 193 */     if (this.lookupStubOnStartup) {
/* 194 */       Object remoteObj = lookupStub();
/* 195 */       if (this.logger.isDebugEnabled()) {
/* 196 */         if (remoteObj instanceof RmiInvocationHandler) {
/* 197 */           this.logger.debug("JNDI RMI object [" + getJndiName() + "] is an RMI invoker");
/*     */         }
/* 199 */         else if (getServiceInterface() != null) {
/* 200 */           boolean isImpl = getServiceInterface().isInstance(remoteObj);
/* 201 */           this.logger.debug("Using service interface [" + getServiceInterface().getName() + "] for JNDI RMI object [" + 
/* 202 */               getJndiName() + "] - " + (!isImpl ? "not " : "") + "directly implemented");
/*     */         } 
/*     */       }
/*     */       
/* 206 */       if (this.cacheStub) {
/* 207 */         this.cachedStub = remoteObj;
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
/*     */   protected Object lookupStub() throws RemoteLookupFailureException {
/*     */     try {
/* 225 */       return lookup();
/*     */     }
/* 227 */     catch (NamingException ex) {
/* 228 */       throw new RemoteLookupFailureException("JNDI lookup for RMI service [" + getJndiName() + "] failed", ex);
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
/*     */   protected Object getStub() throws NamingException, RemoteLookupFailureException {
/* 244 */     if (!this.cacheStub || (this.lookupStubOnStartup && !this.refreshStubOnConnectFailure)) {
/* 245 */       return (this.cachedStub != null) ? this.cachedStub : lookupStub();
/*     */     }
/*     */     
/* 248 */     synchronized (this.stubMonitor) {
/* 249 */       if (this.cachedStub == null) {
/* 250 */         this.cachedStub = lookupStub();
/*     */       }
/* 252 */       return this.cachedStub;
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
/*     */     Object stub;
/*     */     try {
/* 273 */       stub = getStub();
/*     */     }
/* 275 */     catch (NamingException ex) {
/* 276 */       throw new RemoteLookupFailureException("JNDI lookup for RMI service [" + getJndiName() + "] failed", ex);
/*     */     } 
/*     */     
/* 279 */     Context ctx = this.exposeAccessContext ? getJndiTemplate().getContext() : null;
/*     */     try {
/* 281 */       return doInvoke(invocation, stub);
/*     */     }
/* 283 */     catch (RemoteConnectFailureException ex) {
/* 284 */       return handleRemoteConnectFailure(invocation, (Exception)ex);
/*     */     }
/* 286 */     catch (RemoteException ex) {
/* 287 */       if (isConnectFailure(ex)) {
/* 288 */         return handleRemoteConnectFailure(invocation, ex);
/*     */       }
/*     */       
/* 291 */       throw ex;
/*     */     }
/*     */     finally {
/*     */       
/* 295 */       getJndiTemplate().releaseContext(ctx);
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
/*     */   protected boolean isConnectFailure(RemoteException ex) {
/* 307 */     return RmiClientInterceptorUtils.isConnectFailure(ex);
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
/*     */   private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex) throws Throwable {
/* 320 */     if (this.refreshStubOnConnectFailure) {
/* 321 */       if (this.logger.isDebugEnabled()) {
/* 322 */         this.logger.debug("Could not connect to RMI service [" + getJndiName() + "] - retrying", ex);
/*     */       }
/* 324 */       else if (this.logger.isInfoEnabled()) {
/* 325 */         this.logger.info("Could not connect to RMI service [" + getJndiName() + "] - retrying");
/*     */       } 
/* 327 */       return refreshAndRetry(invocation);
/*     */     } 
/*     */     
/* 330 */     throw ex;
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
/*     */     Object freshStub;
/* 345 */     synchronized (this.stubMonitor) {
/* 346 */       this.cachedStub = null;
/* 347 */       freshStub = lookupStub();
/* 348 */       if (this.cacheStub) {
/* 349 */         this.cachedStub = freshStub;
/*     */       }
/*     */     } 
/* 352 */     return doInvoke(invocation, freshStub);
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
/*     */   @Nullable
/*     */   protected Object doInvoke(MethodInvocation invocation, Object stub) throws Throwable {
/* 365 */     if (stub instanceof RmiInvocationHandler) {
/*     */       
/*     */       try {
/* 368 */         return doInvoke(invocation, (RmiInvocationHandler)stub);
/*     */       }
/* 370 */       catch (RemoteException ex) {
/* 371 */         throw convertRmiAccessException(ex, invocation.getMethod());
/*     */       }
/* 373 */       catch (InvocationTargetException ex) {
/* 374 */         throw ex.getTargetException();
/*     */       }
/* 376 */       catch (Throwable ex) {
/* 377 */         throw new RemoteInvocationFailureException("Invocation of method [" + invocation.getMethod() + "] failed in RMI service [" + 
/* 378 */             getJndiName() + "]", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 384 */       return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, stub);
/*     */     }
/* 386 */     catch (InvocationTargetException ex) {
/* 387 */       Throwable targetEx = ex.getTargetException();
/* 388 */       if (targetEx instanceof RemoteException) {
/* 389 */         throw convertRmiAccessException((RemoteException)targetEx, invocation.getMethod());
/*     */       }
/*     */       
/* 392 */       throw targetEx;
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
/*     */   protected Object doInvoke(MethodInvocation methodInvocation, RmiInvocationHandler invocationHandler) throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 413 */     if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
/* 414 */       return "RMI invoker proxy for service URL [" + getJndiName() + "]";
/*     */     }
/*     */     
/* 417 */     return invocationHandler.invoke(createRemoteInvocation(methodInvocation));
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
/*     */   protected RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
/* 433 */     return getRemoteInvocationFactory().createRemoteInvocation(methodInvocation);
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
/*     */   private Exception convertRmiAccessException(RemoteException ex, Method method) {
/* 445 */     return RmiClientInterceptorUtils.convertRmiAccessException(method, ex, isConnectFailure(ex), getJndiName());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/rmi/JndiRmiClientInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */