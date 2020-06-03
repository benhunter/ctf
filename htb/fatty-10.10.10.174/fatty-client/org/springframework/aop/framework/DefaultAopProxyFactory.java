/*    */ package org.springframework.aop.framework;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Proxy;
/*    */ import org.springframework.aop.SpringProxy;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultAopProxyFactory
/*    */   implements AopProxyFactory, Serializable
/*    */ {
/*    */   public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
/* 51 */     if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
/* 52 */       Class<?> targetClass = config.getTargetClass();
/* 53 */       if (targetClass == null) {
/* 54 */         throw new AopConfigException("TargetSource cannot determine target class: Either an interface or a target is required for proxy creation.");
/*    */       }
/*    */       
/* 57 */       if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
/* 58 */         return new JdkDynamicAopProxy(config);
/*    */       }
/* 60 */       return new ObjenesisCglibAopProxy(config);
/*    */     } 
/*    */     
/* 63 */     return new JdkDynamicAopProxy(config);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean hasNoUserSuppliedProxyInterfaces(AdvisedSupport config) {
/* 73 */     Class<?>[] ifcs = config.getProxiedInterfaces();
/* 74 */     return (ifcs.length == 0 || (ifcs.length == 1 && SpringProxy.class.isAssignableFrom(ifcs[0])));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/DefaultAopProxyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */