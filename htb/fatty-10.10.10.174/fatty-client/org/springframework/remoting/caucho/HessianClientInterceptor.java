/*     */ package org.springframework.remoting.caucho;
/*     */ 
/*     */ import com.caucho.hessian.client.HessianConnectionFactory;
/*     */ import com.caucho.hessian.client.HessianProxyFactory;
/*     */ import com.caucho.hessian.io.SerializerFactory;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.net.MalformedURLException;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.remoting.RemoteAccessException;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.RemoteProxyFailureException;
/*     */ import org.springframework.remoting.support.UrlBasedRemoteAccessor;
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
/*     */ public class HessianClientInterceptor
/*     */   extends UrlBasedRemoteAccessor
/*     */   implements MethodInterceptor
/*     */ {
/*  69 */   private HessianProxyFactory proxyFactory = new HessianProxyFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object hessianProxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyFactory(@Nullable HessianProxyFactory proxyFactory) {
/*  82 */     this.proxyFactory = (proxyFactory != null) ? proxyFactory : new HessianProxyFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSerializerFactory(SerializerFactory serializerFactory) {
/*  92 */     this.proxyFactory.setSerializerFactory(serializerFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSendCollectionType(boolean sendCollectionType) {
/* 100 */     this.proxyFactory.getSerializerFactory().setSendCollectionType(sendCollectionType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowNonSerializable(boolean allowNonSerializable) {
/* 108 */     this.proxyFactory.getSerializerFactory().setAllowNonSerializable(allowNonSerializable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverloadEnabled(boolean overloadEnabled) {
/* 117 */     this.proxyFactory.setOverloadEnabled(overloadEnabled);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUsername(String username) {
/* 127 */     this.proxyFactory.setUser(username);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 137 */     this.proxyFactory.setPassword(password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/* 146 */     this.proxyFactory.setDebug(debug);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChunkedPost(boolean chunkedPost) {
/* 154 */     this.proxyFactory.setChunkedPost(chunkedPost);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectionFactory(HessianConnectionFactory connectionFactory) {
/* 161 */     this.proxyFactory.setConnectionFactory(connectionFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(long timeout) {
/* 169 */     this.proxyFactory.setConnectTimeout(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadTimeout(long timeout) {
/* 177 */     this.proxyFactory.setReadTimeout(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHessian2(boolean hessian2) {
/* 186 */     this.proxyFactory.setHessian2Request(hessian2);
/* 187 */     this.proxyFactory.setHessian2Reply(hessian2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHessian2Request(boolean hessian2) {
/* 196 */     this.proxyFactory.setHessian2Request(hessian2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHessian2Reply(boolean hessian2) {
/* 205 */     this.proxyFactory.setHessian2Reply(hessian2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 211 */     super.afterPropertiesSet();
/* 212 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws RemoteLookupFailureException {
/*     */     try {
/* 221 */       this.hessianProxy = createHessianProxy(this.proxyFactory);
/*     */     }
/* 223 */     catch (MalformedURLException ex) {
/* 224 */       throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
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
/*     */   protected Object createHessianProxy(HessianProxyFactory proxyFactory) throws MalformedURLException {
/* 236 */     Assert.notNull(getServiceInterface(), "'serviceInterface' is required");
/* 237 */     return proxyFactory.create(getServiceInterface(), getServiceUrl(), getBeanClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 244 */     if (this.hessianProxy == null) {
/* 245 */       throw new IllegalStateException("HessianClientInterceptor is not properly initialized - invoke 'prepare' before attempting any operations");
/*     */     }
/*     */ 
/*     */     
/* 249 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader();
/*     */     try {
/* 251 */       return invocation.getMethod().invoke(this.hessianProxy, invocation.getArguments());
/*     */     }
/* 253 */     catch (InvocationTargetException ex) {
/* 254 */       Throwable targetEx = ex.getTargetException();
/*     */       
/* 256 */       if (targetEx instanceof InvocationTargetException) {
/* 257 */         targetEx = ((InvocationTargetException)targetEx).getTargetException();
/*     */       }
/* 259 */       if (targetEx instanceof com.caucho.hessian.client.HessianConnectionException) {
/* 260 */         throw convertHessianAccessException(targetEx);
/*     */       }
/* 262 */       if (targetEx instanceof com.caucho.hessian.HessianException || targetEx instanceof com.caucho.hessian.client.HessianRuntimeException) {
/* 263 */         Throwable cause = targetEx.getCause();
/* 264 */         throw convertHessianAccessException((cause != null) ? cause : targetEx);
/*     */       } 
/* 266 */       if (targetEx instanceof UndeclaredThrowableException) {
/* 267 */         UndeclaredThrowableException utex = (UndeclaredThrowableException)targetEx;
/* 268 */         throw convertHessianAccessException(utex.getUndeclaredThrowable());
/*     */       } 
/*     */       
/* 271 */       throw targetEx;
/*     */     
/*     */     }
/* 274 */     catch (Throwable ex) {
/* 275 */       throw new RemoteProxyFailureException("Failed to invoke Hessian proxy for remote service [" + 
/* 276 */           getServiceUrl() + "]", ex);
/*     */     } finally {
/*     */       
/* 279 */       resetThreadContextClassLoader(originalClassLoader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RemoteAccessException convertHessianAccessException(Throwable ex) {
/* 290 */     if (ex instanceof com.caucho.hessian.client.HessianConnectionException || ex instanceof java.net.ConnectException) {
/* 291 */       return (RemoteAccessException)new RemoteConnectFailureException("Cannot connect to Hessian remote service at [" + 
/* 292 */           getServiceUrl() + "]", ex);
/*     */     }
/*     */     
/* 295 */     return new RemoteAccessException("Cannot access Hessian remote service at [" + 
/* 296 */         getServiceUrl() + "]", ex);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/caucho/HessianClientInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */