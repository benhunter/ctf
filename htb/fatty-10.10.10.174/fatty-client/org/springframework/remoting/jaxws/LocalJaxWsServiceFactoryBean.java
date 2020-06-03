/*    */ package org.springframework.remoting.jaxws;
/*    */ 
/*    */ import javax.xml.ws.Service;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ public class LocalJaxWsServiceFactoryBean
/*    */   extends LocalJaxWsServiceFactory
/*    */   implements FactoryBean<Service>, InitializingBean
/*    */ {
/*    */   @Nullable
/*    */   private Service service;
/*    */   
/*    */   public void afterPropertiesSet() {
/* 48 */     this.service = createJaxWsService();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Service getObject() {
/* 54 */     return this.service;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends Service> getObjectType() {
/* 59 */     return (this.service != null) ? (Class)this.service.getClass() : Service.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 64 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/jaxws/LocalJaxWsServiceFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */