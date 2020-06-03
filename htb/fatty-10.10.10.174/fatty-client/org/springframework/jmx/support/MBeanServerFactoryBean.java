/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MBeanServerFactoryBean
/*     */   implements FactoryBean<MBeanServer>, InitializingBean, DisposableBean
/*     */ {
/*  57 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private boolean locateExistingServerIfPossible = false;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String agentId;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String defaultDomain;
/*     */ 
/*     */   
/*     */   private boolean registerWithFactory = true;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MBeanServer server;
/*     */   
/*     */   private boolean newlyRegistered = false;
/*     */ 
/*     */   
/*     */   public void setLocateExistingServerIfPossible(boolean locateExistingServerIfPossible) {
/*  81 */     this.locateExistingServerIfPossible = locateExistingServerIfPossible;
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
/*     */   public void setAgentId(String agentId) {
/*  95 */     this.agentId = agentId;
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
/*     */   public void setDefaultDomain(String defaultDomain) {
/* 107 */     this.defaultDomain = defaultDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegisterWithFactory(boolean registerWithFactory) {
/* 118 */     this.registerWithFactory = registerWithFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws MBeanServerNotFoundException {
/* 128 */     if (this.locateExistingServerIfPossible || this.agentId != null) {
/*     */       try {
/* 130 */         this.server = locateMBeanServer(this.agentId);
/*     */       }
/* 132 */       catch (MBeanServerNotFoundException ex) {
/*     */ 
/*     */         
/* 135 */         if (this.agentId != null) {
/* 136 */           throw ex;
/*     */         }
/* 138 */         this.logger.debug("No existing MBeanServer found - creating new one");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 143 */     if (this.server == null) {
/* 144 */       this.server = createMBeanServer(this.defaultDomain, this.registerWithFactory);
/* 145 */       this.newlyRegistered = this.registerWithFactory;
/*     */     } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected MBeanServer locateMBeanServer(@Nullable String agentId) throws MBeanServerNotFoundException {
/* 165 */     return JmxUtils.locateMBeanServer(agentId);
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
/*     */   protected MBeanServer createMBeanServer(@Nullable String defaultDomain, boolean registerWithFactory) {
/* 178 */     if (registerWithFactory) {
/* 179 */       return MBeanServerFactory.createMBeanServer(defaultDomain);
/*     */     }
/*     */     
/* 182 */     return MBeanServerFactory.newMBeanServer(defaultDomain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MBeanServer getObject() {
/* 190 */     return this.server;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends MBeanServer> getObjectType() {
/* 195 */     return (this.server != null) ? (Class)this.server.getClass() : MBeanServer.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 200 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 209 */     if (this.newlyRegistered)
/* 210 */       MBeanServerFactory.releaseMBeanServer(this.server); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/support/MBeanServerFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */