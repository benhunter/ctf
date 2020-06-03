/*     */ package org.springframework.ejb.access;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.rmi.RemoteException;
/*     */ import javax.ejb.EJBObject;
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class SimpleRemoteSlsbInvokerInterceptor
/*     */   extends AbstractRemoteSlsbInvokerInterceptor
/*     */   implements DisposableBean
/*     */ {
/*     */   private boolean cacheSessionBean = false;
/*     */   @Nullable
/*     */   private Object beanInstance;
/*  73 */   private final Object beanInstanceMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheSessionBean(boolean cacheSessionBean) {
/*  84 */     this.cacheSessionBean = cacheSessionBean;
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
/*     */   protected Object doInvoke(MethodInvocation invocation) throws Throwable {
/*  98 */     Object ejb = null;
/*     */     try {
/* 100 */       ejb = getSessionBeanInstance();
/* 101 */       return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, ejb);
/*     */     }
/* 103 */     catch (NamingException ex) {
/* 104 */       throw new RemoteLookupFailureException("Failed to locate remote EJB [" + getJndiName() + "]", ex);
/*     */     }
/* 106 */     catch (InvocationTargetException ex) {
/* 107 */       Throwable targetEx = ex.getTargetException();
/* 108 */       if (targetEx instanceof RemoteException) {
/* 109 */         RemoteException rex = (RemoteException)targetEx;
/* 110 */         throw RmiClientInterceptorUtils.convertRmiAccessException(invocation
/* 111 */             .getMethod(), rex, isConnectFailure(rex), getJndiName());
/*     */       } 
/* 113 */       if (targetEx instanceof javax.ejb.CreateException) {
/* 114 */         throw RmiClientInterceptorUtils.convertRmiAccessException(invocation
/* 115 */             .getMethod(), targetEx, "Could not create remote EJB [" + getJndiName() + "]");
/*     */       }
/* 117 */       throw targetEx;
/*     */     } finally {
/*     */       
/* 120 */       if (ejb instanceof EJBObject) {
/* 121 */         releaseSessionBeanInstance((EJBObject)ejb);
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
/*     */   protected Object getSessionBeanInstance() throws NamingException, InvocationTargetException {
/* 135 */     if (this.cacheSessionBean) {
/* 136 */       synchronized (this.beanInstanceMonitor) {
/* 137 */         if (this.beanInstance == null) {
/* 138 */           this.beanInstance = newSessionBeanInstance();
/*     */         }
/* 140 */         return this.beanInstance;
/*     */       } 
/*     */     }
/*     */     
/* 144 */     return newSessionBeanInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void releaseSessionBeanInstance(EJBObject ejb) {
/* 155 */     if (!this.cacheSessionBean) {
/* 156 */       removeSessionBeanInstance(ejb);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void refreshHome() throws NamingException {
/* 165 */     super.refreshHome();
/* 166 */     if (this.cacheSessionBean) {
/* 167 */       synchronized (this.beanInstanceMonitor) {
/* 168 */         this.beanInstance = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 178 */     if (this.cacheSessionBean)
/* 179 */       synchronized (this.beanInstanceMonitor) {
/* 180 */         if (this.beanInstance instanceof EJBObject)
/* 181 */           removeSessionBeanInstance((EJBObject)this.beanInstance); 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ejb/access/SimpleRemoteSlsbInvokerInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */