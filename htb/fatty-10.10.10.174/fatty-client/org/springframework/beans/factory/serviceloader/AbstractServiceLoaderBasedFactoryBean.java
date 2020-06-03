/*    */ package org.springframework.beans.factory.serviceloader;
/*    */ 
/*    */ import java.util.ServiceLoader;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.beans.factory.config.AbstractFactoryBean;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public abstract class AbstractServiceLoaderBasedFactoryBean
/*    */   extends AbstractFactoryBean<Object>
/*    */   implements BeanClassLoaderAware
/*    */ {
/*    */   @Nullable
/*    */   private Class<?> serviceType;
/*    */   @Nullable
/* 42 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setServiceType(@Nullable Class<?> serviceType) {
/* 49 */     this.serviceType = serviceType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Class<?> getServiceType() {
/* 57 */     return this.serviceType;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBeanClassLoader(@Nullable ClassLoader beanClassLoader) {
/* 62 */     this.beanClassLoader = beanClassLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object createInstance() {
/* 72 */     Assert.notNull(getServiceType(), "Property 'serviceType' is required");
/* 73 */     return getObjectToExpose(ServiceLoader.load(getServiceType(), this.beanClassLoader));
/*    */   }
/*    */   
/*    */   protected abstract Object getObjectToExpose(ServiceLoader<?> paramServiceLoader);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/serviceloader/AbstractServiceLoaderBasedFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */