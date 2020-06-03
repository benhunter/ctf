/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.remoting.RemoteAccessException;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteInvocationFailureException;
/*     */ import org.springframework.remoting.support.RemoteInvocation;
/*     */ import org.springframework.remoting.support.RemoteInvocationBasedAccessor;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpInvokerClientInterceptor
/*     */   extends RemoteInvocationBasedAccessor
/*     */   implements MethodInterceptor, HttpInvokerClientConfiguration
/*     */ {
/*     */   @Nullable
/*     */   private String codebaseUrl;
/*     */   @Nullable
/*     */   private HttpInvokerRequestExecutor httpInvokerRequestExecutor;
/*     */   
/*     */   public void setCodebaseUrl(@Nullable String codebaseUrl) {
/*  96 */     this.codebaseUrl = codebaseUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getCodebaseUrl() {
/* 105 */     return this.codebaseUrl;
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
/*     */   public void setHttpInvokerRequestExecutor(HttpInvokerRequestExecutor httpInvokerRequestExecutor) {
/* 118 */     this.httpInvokerRequestExecutor = httpInvokerRequestExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpInvokerRequestExecutor getHttpInvokerRequestExecutor() {
/* 127 */     if (this.httpInvokerRequestExecutor == null) {
/* 128 */       SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();
/* 129 */       executor.setBeanClassLoader(getBeanClassLoader());
/* 130 */       this.httpInvokerRequestExecutor = executor;
/*     */     } 
/* 132 */     return this.httpInvokerRequestExecutor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 137 */     super.afterPropertiesSet();
/*     */ 
/*     */     
/* 140 */     getHttpInvokerRequestExecutor();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation methodInvocation) throws Throwable {
/*     */     RemoteInvocationResult result;
/* 146 */     if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
/* 147 */       return "HTTP invoker proxy for service URL [" + getServiceUrl() + "]";
/*     */     }
/*     */     
/* 150 */     RemoteInvocation invocation = createRemoteInvocation(methodInvocation);
/*     */ 
/*     */     
/*     */     try {
/* 154 */       result = executeRequest(invocation, methodInvocation);
/*     */     }
/* 156 */     catch (Throwable ex) {
/* 157 */       RemoteAccessException rae = convertHttpInvokerAccessException(ex);
/* 158 */       throw (rae != null) ? rae : ex;
/*     */     } 
/*     */     
/*     */     try {
/* 162 */       return recreateRemoteInvocationResult(result);
/*     */     }
/* 164 */     catch (Throwable ex) {
/* 165 */       if (result.hasInvocationTargetException()) {
/* 166 */         throw ex;
/*     */       }
/*     */       
/* 169 */       throw new RemoteInvocationFailureException("Invocation of method [" + methodInvocation.getMethod() + "] failed in HTTP invoker remote service at [" + 
/* 170 */           getServiceUrl() + "]", ex);
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
/*     */   protected RemoteInvocationResult executeRequest(RemoteInvocation invocation, MethodInvocation originalInvocation) throws Exception {
/* 188 */     return executeRequest(invocation);
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
/*     */   protected RemoteInvocationResult executeRequest(RemoteInvocation invocation) throws Exception {
/* 206 */     return getHttpInvokerRequestExecutor().executeRequest(this, invocation);
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
/*     */   protected RemoteAccessException convertHttpInvokerAccessException(Throwable ex) {
/* 218 */     if (ex instanceof java.net.ConnectException) {
/* 219 */       return (RemoteAccessException)new RemoteConnectFailureException("Could not connect to HTTP invoker remote service at [" + 
/* 220 */           getServiceUrl() + "]", ex);
/*     */     }
/*     */     
/* 223 */     if (ex instanceof ClassNotFoundException || ex instanceof NoClassDefFoundError || ex instanceof java.io.InvalidClassException)
/*     */     {
/* 225 */       return new RemoteAccessException("Could not deserialize result from HTTP invoker remote service [" + 
/* 226 */           getServiceUrl() + "]", ex);
/*     */     }
/*     */     
/* 229 */     if (ex instanceof Exception) {
/* 230 */       return new RemoteAccessException("Could not access HTTP invoker remote service at [" + 
/* 231 */           getServiceUrl() + "]", ex);
/*     */     }
/*     */ 
/*     */     
/* 235 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/httpinvoker/HttpInvokerClientInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */