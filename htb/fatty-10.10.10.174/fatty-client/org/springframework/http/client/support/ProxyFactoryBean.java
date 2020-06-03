/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Proxy;
/*    */ import java.net.SocketAddress;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ public class ProxyFactoryBean
/*    */   implements FactoryBean<Proxy>, InitializingBean
/*    */ {
/* 38 */   private Proxy.Type type = Proxy.Type.HTTP;
/*    */   
/*    */   @Nullable
/*    */   private String hostname;
/*    */   
/* 43 */   private int port = -1;
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   private Proxy proxy;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setType(Proxy.Type type) {
/* 54 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setHostname(String hostname) {
/* 61 */     this.hostname = hostname;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPort(int port) {
/* 68 */     this.port = port;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() throws IllegalArgumentException {
/* 74 */     Assert.notNull(this.type, "Property 'type' is required");
/* 75 */     Assert.notNull(this.hostname, "Property 'hostname' is required");
/* 76 */     if (this.port < 0 || this.port > 65535) {
/* 77 */       throw new IllegalArgumentException("Property 'port' value out of range: " + this.port);
/*    */     }
/*    */     
/* 80 */     SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
/* 81 */     this.proxy = new Proxy(this.type, socketAddress);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Proxy getObject() {
/* 88 */     return this.proxy;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 93 */     return Proxy.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 98 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/support/ProxyFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */