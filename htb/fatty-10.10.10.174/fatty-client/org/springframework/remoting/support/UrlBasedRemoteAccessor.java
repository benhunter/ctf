/*    */ package org.springframework.remoting.support;
/*    */ 
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ public abstract class UrlBasedRemoteAccessor
/*    */   extends RemoteAccessor
/*    */   implements InitializingBean
/*    */ {
/*    */   private String serviceUrl;
/*    */   
/*    */   public void setServiceUrl(String serviceUrl) {
/* 38 */     this.serviceUrl = serviceUrl;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getServiceUrl() {
/* 45 */     return this.serviceUrl;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() {
/* 51 */     if (getServiceUrl() == null)
/* 52 */       throw new IllegalArgumentException("Property 'serviceUrl' is required"); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/UrlBasedRemoteAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */