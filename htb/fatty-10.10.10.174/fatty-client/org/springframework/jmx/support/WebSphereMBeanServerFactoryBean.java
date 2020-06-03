/*    */ package org.springframework.jmx.support;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import javax.management.MBeanServer;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.jmx.MBeanServerNotFoundException;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WebSphereMBeanServerFactoryBean
/*    */   implements FactoryBean<MBeanServer>, InitializingBean
/*    */ {
/*    */   private static final String ADMIN_SERVICE_FACTORY_CLASS = "com.ibm.websphere.management.AdminServiceFactory";
/*    */   private static final String GET_MBEAN_FACTORY_METHOD = "getMBeanFactory";
/*    */   private static final String GET_MBEAN_SERVER_METHOD = "getMBeanServer";
/*    */   @Nullable
/*    */   private MBeanServer mbeanServer;
/*    */   
/*    */   public void afterPropertiesSet() throws MBeanServerNotFoundException {
/*    */     try {
/* 66 */       Class<?> adminServiceClass = getClass().getClassLoader().loadClass("com.ibm.websphere.management.AdminServiceFactory");
/* 67 */       Method getMBeanFactoryMethod = adminServiceClass.getMethod("getMBeanFactory", new Class[0]);
/* 68 */       Object mbeanFactory = getMBeanFactoryMethod.invoke(null, new Object[0]);
/* 69 */       Method getMBeanServerMethod = mbeanFactory.getClass().getMethod("getMBeanServer", new Class[0]);
/* 70 */       this.mbeanServer = (MBeanServer)getMBeanServerMethod.invoke(mbeanFactory, new Object[0]);
/*    */     }
/* 72 */     catch (ClassNotFoundException ex) {
/* 73 */       throw new MBeanServerNotFoundException("Could not find WebSphere's AdminServiceFactory class", ex);
/*    */     }
/* 75 */     catch (InvocationTargetException ex) {
/* 76 */       throw new MBeanServerNotFoundException("WebSphere's AdminServiceFactory.getMBeanFactory/getMBeanServer method failed", ex
/* 77 */           .getTargetException());
/*    */     }
/* 79 */     catch (Exception ex) {
/* 80 */       throw new MBeanServerNotFoundException("Could not access WebSphere's AdminServiceFactory.getMBeanFactory/getMBeanServer method", ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public MBeanServer getObject() {
/* 89 */     return this.mbeanServer;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends MBeanServer> getObjectType() {
/* 94 */     return (this.mbeanServer != null) ? (Class)this.mbeanServer.getClass() : MBeanServer.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 99 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/support/WebSphereMBeanServerFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */