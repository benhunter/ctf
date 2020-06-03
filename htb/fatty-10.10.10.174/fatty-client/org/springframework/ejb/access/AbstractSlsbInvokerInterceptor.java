/*     */ package org.springframework.ejb.access;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.jndi.JndiObjectLocator;
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
/*     */ public abstract class AbstractSlsbInvokerInterceptor
/*     */   extends JndiObjectLocator
/*     */   implements MethodInterceptor
/*     */ {
/*     */   private boolean lookupHomeOnStartup = true;
/*     */   private boolean cacheHome = true;
/*     */   private boolean exposeAccessContext = false;
/*     */   @Nullable
/*     */   private Object cachedHome;
/*     */   @Nullable
/*     */   private Method createMethod;
/*  63 */   private final Object homeMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLookupHomeOnStartup(boolean lookupHomeOnStartup) {
/*  74 */     this.lookupHomeOnStartup = lookupHomeOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheHome(boolean cacheHome) {
/*  85 */     this.cacheHome = cacheHome;
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
/*  97 */     this.exposeAccessContext = exposeAccessContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/* 108 */     super.afterPropertiesSet();
/* 109 */     if (this.lookupHomeOnStartup)
/*     */     {
/* 111 */       refreshHome();
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
/*     */   protected void refreshHome() throws NamingException {
/* 123 */     synchronized (this.homeMonitor) {
/* 124 */       Object home = lookup();
/* 125 */       if (this.cacheHome) {
/* 126 */         this.cachedHome = home;
/* 127 */         this.createMethod = getCreateMethod(home);
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
/*     */   @Nullable
/*     */   protected Method getCreateMethod(Object home) throws EjbAccessException {
/*     */     try {
/* 142 */       return home.getClass().getMethod("create", new Class[0]);
/*     */     }
/* 144 */     catch (NoSuchMethodException ex) {
/* 145 */       throw new EjbAccessException("EJB home [" + home + "] has no no-arg create() method");
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
/*     */   protected Object getHome() throws NamingException {
/* 162 */     if (!this.cacheHome || (this.lookupHomeOnStartup && !isHomeRefreshable())) {
/* 163 */       return (this.cachedHome != null) ? this.cachedHome : lookup();
/*     */     }
/*     */     
/* 166 */     synchronized (this.homeMonitor) {
/* 167 */       if (this.cachedHome == null) {
/* 168 */         this.cachedHome = lookup();
/* 169 */         this.createMethod = getCreateMethod(this.cachedHome);
/*     */       } 
/* 171 */       return this.cachedHome;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isHomeRefreshable() {
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 192 */     Context ctx = this.exposeAccessContext ? getJndiTemplate().getContext() : null;
/*     */     try {
/* 194 */       return invokeInContext(invocation);
/*     */     } finally {
/*     */       
/* 197 */       getJndiTemplate().releaseContext(ctx);
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
/*     */   @Nullable
/*     */   protected abstract Object invokeInContext(MethodInvocation paramMethodInvocation) throws Throwable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object create() throws NamingException, InvocationTargetException {
/*     */     try {
/* 221 */       Object home = getHome();
/* 222 */       Method createMethodToUse = this.createMethod;
/* 223 */       if (createMethodToUse == null) {
/* 224 */         createMethodToUse = getCreateMethod(home);
/*     */       }
/* 226 */       if (createMethodToUse == null) {
/* 227 */         return home;
/*     */       }
/*     */       
/* 230 */       return createMethodToUse.invoke(home, (Object[])null);
/*     */     }
/* 232 */     catch (IllegalAccessException ex) {
/* 233 */       throw new EjbAccessException("Could not access EJB home create() method", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ejb/access/AbstractSlsbInvokerInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */