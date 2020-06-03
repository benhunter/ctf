/*    */ package org.springframework.remoting.jaxws;
/*    */ 
/*    */ import javax.xml.ws.BindingProvider;
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.framework.ProxyFactory;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class JaxWsPortProxyFactoryBean
/*    */   extends JaxWsPortClientInterceptor
/*    */   implements FactoryBean<Object>
/*    */ {
/*    */   @Nullable
/*    */   private Object serviceProxy;
/*    */   
/*    */   public void afterPropertiesSet() {
/* 44 */     super.afterPropertiesSet();
/*    */     
/* 46 */     Class<?> ifc = getServiceInterface();
/* 47 */     Assert.notNull(ifc, "Property 'serviceInterface' is required");
/*    */ 
/*    */     
/* 50 */     ProxyFactory pf = new ProxyFactory();
/* 51 */     pf.addInterface(ifc);
/* 52 */     pf.addInterface(BindingProvider.class);
/* 53 */     pf.addAdvice((Advice)this);
/* 54 */     this.serviceProxy = pf.getProxy(getBeanClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getObject() {
/* 61 */     return this.serviceProxy;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 66 */     return getServiceInterface();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 71 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/jaxws/JaxWsPortProxyFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */