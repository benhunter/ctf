/*     */ package org.springframework.ejb.access;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.ejb.EJBLocalObject;
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalSlsbInvokerInterceptor
/*     */   extends AbstractSlsbInvokerInterceptor
/*     */ {
/*     */   private volatile boolean homeAsComponent = false;
/*     */   
/*     */   @Nullable
/*     */   public Object invokeInContext(MethodInvocation invocation) throws Throwable {
/*  67 */     Object ejb = null;
/*     */     try {
/*  69 */       ejb = getSessionBeanInstance();
/*  70 */       Method method = invocation.getMethod();
/*  71 */       if (method.getDeclaringClass().isInstance(ejb))
/*     */       {
/*  73 */         return method.invoke(ejb, invocation.getArguments());
/*     */       }
/*     */ 
/*     */       
/*  77 */       Method ejbMethod = ejb.getClass().getMethod(method.getName(), method.getParameterTypes());
/*  78 */       return ejbMethod.invoke(ejb, invocation.getArguments());
/*     */     
/*     */     }
/*  81 */     catch (InvocationTargetException ex) {
/*  82 */       Throwable targetEx = ex.getTargetException();
/*  83 */       if (this.logger.isDebugEnabled()) {
/*  84 */         this.logger.debug("Method of local EJB [" + getJndiName() + "] threw exception", targetEx);
/*     */       }
/*  86 */       if (targetEx instanceof javax.ejb.CreateException) {
/*  87 */         throw new EjbAccessException("Could not create local EJB [" + getJndiName() + "]", targetEx);
/*     */       }
/*     */       
/*  90 */       throw targetEx;
/*     */     
/*     */     }
/*  93 */     catch (NamingException ex) {
/*  94 */       throw new EjbAccessException("Failed to locate local EJB [" + getJndiName() + "]", ex);
/*     */     }
/*  96 */     catch (IllegalAccessException ex) {
/*  97 */       throw new EjbAccessException("Could not access method [" + invocation.getMethod().getName() + "] of local EJB [" + 
/*  98 */           getJndiName() + "]", ex);
/*     */     } finally {
/*     */       
/* 101 */       if (ejb instanceof EJBLocalObject) {
/* 102 */         releaseSessionBeanInstance((EJBLocalObject)ejb);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method getCreateMethod(Object home) throws EjbAccessException {
/* 112 */     if (this.homeAsComponent) {
/* 113 */       return null;
/*     */     }
/* 115 */     if (!(home instanceof javax.ejb.EJBLocalHome)) {
/*     */       
/* 117 */       this.homeAsComponent = true;
/* 118 */       return null;
/*     */     } 
/* 120 */     return super.getCreateMethod(home);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getSessionBeanInstance() throws NamingException, InvocationTargetException {
/* 131 */     return newSessionBeanInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void releaseSessionBeanInstance(EJBLocalObject ejb) {
/* 141 */     removeSessionBeanInstance(ejb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object newSessionBeanInstance() throws NamingException, InvocationTargetException {
/* 152 */     if (this.logger.isDebugEnabled()) {
/* 153 */       this.logger.debug("Trying to create reference to local EJB");
/*     */     }
/* 155 */     Object ejbInstance = create();
/* 156 */     if (this.logger.isDebugEnabled()) {
/* 157 */       this.logger.debug("Obtained reference to local EJB: " + ejbInstance);
/*     */     }
/* 159 */     return ejbInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeSessionBeanInstance(@Nullable EJBLocalObject ejb) {
/* 168 */     if (ejb != null && !this.homeAsComponent)
/*     */       try {
/* 170 */         ejb.remove();
/*     */       }
/* 172 */       catch (Throwable ex) {
/* 173 */         this.logger.warn("Could not invoke 'remove' on local EJB proxy", ex);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ejb/access/LocalSlsbInvokerInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */