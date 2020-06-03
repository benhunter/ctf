/*    */ package org.springframework.aop.framework;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.cglib.proxy.Callback;
/*    */ import org.springframework.cglib.proxy.Enhancer;
/*    */ import org.springframework.cglib.proxy.Factory;
/*    */ import org.springframework.objenesis.SpringObjenesis;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ class ObjenesisCglibAopProxy
/*    */   extends CglibAopProxy
/*    */ {
/* 41 */   private static final Log logger = LogFactory.getLog(ObjenesisCglibAopProxy.class);
/*    */   
/* 43 */   private static final SpringObjenesis objenesis = new SpringObjenesis();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjenesisCglibAopProxy(AdvisedSupport config) {
/* 51 */     super(config);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
/* 57 */     Class<?> proxyClass = enhancer.createClass();
/* 58 */     Object proxyInstance = null;
/*    */     
/* 60 */     if (objenesis.isWorthTrying()) {
/*    */       try {
/* 62 */         proxyInstance = objenesis.newInstance(proxyClass, enhancer.getUseCache());
/*    */       }
/* 64 */       catch (Throwable ex) {
/* 65 */         logger.debug("Unable to instantiate proxy using Objenesis, falling back to regular proxy construction", ex);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 70 */     if (proxyInstance == null) {
/*    */       
/*    */       try {
/*    */ 
/*    */         
/* 75 */         Constructor<?> ctor = (this.constructorArgs != null) ? proxyClass.getDeclaredConstructor(this.constructorArgTypes) : proxyClass.getDeclaredConstructor(new Class[0]);
/* 76 */         ReflectionUtils.makeAccessible(ctor);
/*    */         
/* 78 */         proxyInstance = (this.constructorArgs != null) ? ctor.newInstance(this.constructorArgs) : ctor.newInstance(new Object[0]);
/*    */       }
/* 80 */       catch (Throwable ex) {
/* 81 */         throw new AopConfigException("Unable to instantiate proxy using Objenesis, and regular proxy instantiation via default constructor fails as well", ex);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 86 */     ((Factory)proxyInstance).setCallbacks(callbacks);
/* 87 */     return proxyInstance;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/ObjenesisCglibAopProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */