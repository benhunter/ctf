/*     */ package org.springframework.ejb.access;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.RemoteException;
/*     */ import javax.ejb.EJBObject;
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.rmi.RmiClientInterceptorUtils;
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
/*     */ public abstract class AbstractRemoteSlsbInvokerInterceptor
/*     */   extends AbstractSlsbInvokerInterceptor
/*     */ {
/*     */   private boolean refreshHomeOnConnectFailure = false;
/*     */   private volatile boolean homeAsComponent = false;
/*     */   
/*     */   public void setRefreshHomeOnConnectFailure(boolean refreshHomeOnConnectFailure) {
/*  63 */     this.refreshHomeOnConnectFailure = refreshHomeOnConnectFailure;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isHomeRefreshable() {
/*  68 */     return this.refreshHomeOnConnectFailure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method getCreateMethod(Object home) throws EjbAccessException {
/*  77 */     if (this.homeAsComponent) {
/*  78 */       return null;
/*     */     }
/*  80 */     if (!(home instanceof javax.ejb.EJBHome)) {
/*     */       
/*  82 */       this.homeAsComponent = true;
/*  83 */       return null;
/*     */     } 
/*  85 */     return super.getCreateMethod(home);
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
/*     */   @Nullable
/*     */   public Object invokeInContext(MethodInvocation invocation) throws Throwable {
/*     */     try {
/* 101 */       return doInvoke(invocation);
/*     */     }
/* 103 */     catch (RemoteConnectFailureException ex) {
/* 104 */       return handleRemoteConnectFailure(invocation, (Exception)ex);
/*     */     }
/* 106 */     catch (RemoteException ex) {
/* 107 */       if (isConnectFailure(ex)) {
/* 108 */         return handleRemoteConnectFailure(invocation, ex);
/*     */       }
/*     */       
/* 111 */       throw ex;
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
/* 124 */     return RmiClientInterceptorUtils.isConnectFailure(ex);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex) throws Throwable {
/* 129 */     if (this.refreshHomeOnConnectFailure) {
/* 130 */       if (this.logger.isDebugEnabled()) {
/* 131 */         this.logger.debug("Could not connect to remote EJB [" + getJndiName() + "] - retrying", ex);
/*     */       }
/* 133 */       else if (this.logger.isWarnEnabled()) {
/* 134 */         this.logger.warn("Could not connect to remote EJB [" + getJndiName() + "] - retrying");
/*     */       } 
/* 136 */       return refreshAndRetry(invocation);
/*     */     } 
/*     */     
/* 139 */     throw ex;
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
/*     */     try {
/* 154 */       refreshHome();
/*     */     }
/* 156 */     catch (NamingException ex) {
/* 157 */       throw new RemoteLookupFailureException("Failed to locate remote EJB [" + getJndiName() + "]", ex);
/*     */     } 
/* 159 */     return doInvoke(invocation);
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
/*     */   @Nullable
/*     */   protected abstract Object doInvoke(MethodInvocation paramMethodInvocation) throws Throwable;
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
/*     */   protected Object newSessionBeanInstance() throws NamingException, InvocationTargetException {
/* 185 */     if (this.logger.isDebugEnabled()) {
/* 186 */       this.logger.debug("Trying to create reference to remote EJB");
/*     */     }
/* 188 */     Object ejbInstance = create();
/* 189 */     if (this.logger.isDebugEnabled()) {
/* 190 */       this.logger.debug("Obtained reference to remote EJB: " + ejbInstance);
/*     */     }
/* 192 */     return ejbInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeSessionBeanInstance(@Nullable EJBObject ejb) {
/* 202 */     if (ejb != null && !this.homeAsComponent)
/*     */       try {
/* 204 */         ejb.remove();
/*     */       }
/* 206 */       catch (Throwable ex) {
/* 207 */         this.logger.warn("Could not invoke 'remove' on remote EJB proxy", ex);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ejb/access/AbstractRemoteSlsbInvokerInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */