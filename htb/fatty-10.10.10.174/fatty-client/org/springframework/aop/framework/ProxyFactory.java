/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.Interceptor;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class ProxyFactory
/*     */   extends ProxyCreatorSupport
/*     */ {
/*     */   public ProxyFactory() {}
/*     */   
/*     */   public ProxyFactory(Object target) {
/*  50 */     setTarget(target);
/*  51 */     setInterfaces(ClassUtils.getAllInterfaces(target));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyFactory(Class<?>... proxyInterfaces) {
/*  60 */     setInterfaces(proxyInterfaces);
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
/*     */   public ProxyFactory(Class<?> proxyInterface, Interceptor interceptor) {
/*  72 */     addInterface(proxyInterface);
/*  73 */     addAdvice((Advice)interceptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyFactory(Class<?> proxyInterface, TargetSource targetSource) {
/*  83 */     addInterface(proxyInterface);
/*  84 */     setTargetSource(targetSource);
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
/*     */   public Object getProxy() {
/*  97 */     return createAopProxy().getProxy();
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
/*     */   public Object getProxy(@Nullable ClassLoader classLoader) {
/* 110 */     return createAopProxy().getProxy(classLoader);
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
/*     */   public static <T> T getProxy(Class<T> proxyInterface, Interceptor interceptor) {
/* 126 */     return (T)(new ProxyFactory(proxyInterface, interceptor)).getProxy();
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
/*     */   public static <T> T getProxy(Class<T> proxyInterface, TargetSource targetSource) {
/* 139 */     return (T)(new ProxyFactory(proxyInterface, targetSource)).getProxy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getProxy(TargetSource targetSource) {
/* 149 */     if (targetSource.getTargetClass() == null) {
/* 150 */       throw new IllegalArgumentException("Cannot create class proxy for TargetSource with null target class");
/*     */     }
/* 152 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 153 */     proxyFactory.setTargetSource(targetSource);
/* 154 */     proxyFactory.setProxyTargetClass(true);
/* 155 */     return proxyFactory.getProxy();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/ProxyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */