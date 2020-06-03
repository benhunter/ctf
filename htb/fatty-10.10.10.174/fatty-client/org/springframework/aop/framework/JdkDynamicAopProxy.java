/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.AopInvocationException;
/*     */ import org.springframework.aop.RawTargetAccess;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JdkDynamicAopProxy
/*     */   implements AopProxy, InvocationHandler, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5531744639992436476L;
/*  80 */   private static final Log logger = LogFactory.getLog(JdkDynamicAopProxy.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AdvisedSupport advised;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean equalsDefined;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hashCodeDefined;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JdkDynamicAopProxy(AdvisedSupport config) throws AopConfigException {
/* 103 */     Assert.notNull(config, "AdvisedSupport must not be null");
/* 104 */     if ((config.getAdvisors()).length == 0 && config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE) {
/* 105 */       throw new AopConfigException("No advisors and no TargetSource specified");
/*     */     }
/* 107 */     this.advised = config;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProxy() {
/* 113 */     return getProxy(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getProxy(@Nullable ClassLoader classLoader) {
/* 118 */     if (logger.isTraceEnabled()) {
/* 119 */       logger.trace("Creating JDK dynamic proxy: " + this.advised.getTargetSource());
/*     */     }
/* 121 */     Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised, true);
/* 122 */     findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
/* 123 */     return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findDefinedEqualsAndHashCodeMethods(Class<?>[] proxiedInterfaces) {
/* 132 */     for (Class<?> proxiedInterface : proxiedInterfaces) {
/* 133 */       Method[] methods = proxiedInterface.getDeclaredMethods();
/* 134 */       for (Method method : methods) {
/* 135 */         if (AopUtils.isEqualsMethod(method)) {
/* 136 */           this.equalsDefined = true;
/*     */         }
/* 138 */         if (AopUtils.isHashCodeMethod(method)) {
/* 139 */           this.hashCodeDefined = true;
/*     */         }
/* 141 */         if (this.equalsDefined && this.hashCodeDefined) {
/*     */           return;
/*     */         }
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
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 157 */     Object oldProxy = null;
/* 158 */     boolean setProxyContext = false;
/*     */     
/* 160 */     TargetSource targetSource = this.advised.targetSource;
/* 161 */     Object target = null;
/*     */     try {
/*     */       Object retVal;
/* 164 */       if (!this.equalsDefined && AopUtils.isEqualsMethod(method)) {
/*     */         
/* 166 */         retVal = Boolean.valueOf(equals(args[0])); return retVal;
/*     */       } 
/* 168 */       if (!this.hashCodeDefined && AopUtils.isHashCodeMethod(method)) {
/*     */         
/* 170 */         retVal = Integer.valueOf(hashCode()); return retVal;
/*     */       } 
/* 172 */       if (method.getDeclaringClass() == DecoratingProxy.class) {
/*     */         
/* 174 */         retVal = AopProxyUtils.ultimateTargetClass(this.advised); return retVal;
/*     */       } 
/* 176 */       if (!this.advised.opaque && method.getDeclaringClass().isInterface() && method
/* 177 */         .getDeclaringClass().isAssignableFrom(Advised.class)) {
/*     */         
/* 179 */         retVal = AopUtils.invokeJoinpointUsingReflection(this.advised, method, args); return retVal;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 184 */       if (this.advised.exposeProxy) {
/*     */         
/* 186 */         oldProxy = AopContext.setCurrentProxy(proxy);
/* 187 */         setProxyContext = true;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 192 */       target = targetSource.getTarget();
/* 193 */       Class<?> targetClass = (target != null) ? target.getClass() : null;
/*     */ 
/*     */       
/* 196 */       List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
/*     */ 
/*     */ 
/*     */       
/* 200 */       if (chain.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/* 204 */         Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
/* 205 */         retVal = AopUtils.invokeJoinpointUsingReflection(target, method, argsToUse);
/*     */       }
/*     */       else {
/*     */         
/* 209 */         ReflectiveMethodInvocation reflectiveMethodInvocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
/*     */ 
/*     */         
/* 212 */         retVal = reflectiveMethodInvocation.proceed();
/*     */       } 
/*     */ 
/*     */       
/* 216 */       Class<?> returnType = method.getReturnType();
/* 217 */       if (retVal != null && retVal == target && returnType != Object.class && returnType
/* 218 */         .isInstance(proxy) && 
/* 219 */         !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {
/*     */ 
/*     */ 
/*     */         
/* 223 */         retVal = proxy;
/*     */       }
/* 225 */       else if (retVal == null && returnType != void.class && returnType.isPrimitive()) {
/* 226 */         throw new AopInvocationException("Null return value from advice does not match primitive return type for: " + method);
/*     */       } 
/*     */       
/* 229 */       return retVal;
/*     */     } finally {
/*     */       
/* 232 */       if (target != null && !targetSource.isStatic())
/*     */       {
/* 234 */         targetSource.releaseTarget(target);
/*     */       }
/* 236 */       if (setProxyContext)
/*     */       {
/* 238 */         AopContext.setCurrentProxy(oldProxy);
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
/*     */   public boolean equals(@Nullable Object other) {
/*     */     JdkDynamicAopProxy otherProxy;
/* 251 */     if (other == this) {
/* 252 */       return true;
/*     */     }
/* 254 */     if (other == null) {
/* 255 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 259 */     if (other instanceof JdkDynamicAopProxy) {
/* 260 */       otherProxy = (JdkDynamicAopProxy)other;
/*     */     }
/* 262 */     else if (Proxy.isProxyClass(other.getClass())) {
/* 263 */       InvocationHandler ih = Proxy.getInvocationHandler(other);
/* 264 */       if (!(ih instanceof JdkDynamicAopProxy)) {
/* 265 */         return false;
/*     */       }
/* 267 */       otherProxy = (JdkDynamicAopProxy)ih;
/*     */     }
/*     */     else {
/*     */       
/* 271 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 275 */     return AopProxyUtils.equalsInProxy(this.advised, otherProxy.advised);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 283 */     return JdkDynamicAopProxy.class.hashCode() * 13 + this.advised.getTargetSource().hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/JdkDynamicAopProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */