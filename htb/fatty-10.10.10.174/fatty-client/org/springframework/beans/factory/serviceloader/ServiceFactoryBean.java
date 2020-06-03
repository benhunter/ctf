/*    */ package org.springframework.beans.factory.serviceloader;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.ServiceLoader;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
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
/*    */ public class ServiceFactoryBean
/*    */   extends AbstractServiceLoaderBasedFactoryBean
/*    */   implements BeanClassLoaderAware
/*    */ {
/*    */   protected Object getObjectToExpose(ServiceLoader<?> serviceLoader) {
/* 38 */     Iterator<?> it = serviceLoader.iterator();
/* 39 */     if (!it.hasNext()) {
/* 40 */       throw new IllegalStateException("ServiceLoader could not find service for type [" + 
/* 41 */           getServiceType() + "]");
/*    */     }
/* 43 */     return it.next();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Class<?> getObjectType() {
/* 49 */     return getServiceType();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/serviceloader/ServiceFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */