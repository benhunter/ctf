/*     */ package org.springframework.jmx.access;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.JmxException;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
/*     */ import org.springframework.jmx.support.NotificationListenerHolder;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public class NotificationListenerRegistrar
/*     */   extends NotificationListenerHolder
/*     */   implements InitializingBean, DisposableBean
/*     */ {
/*  53 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  55 */   private final ConnectorDelegate connector = new ConnectorDelegate();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MBeanServerConnection server;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private JMXServiceURL serviceUrl;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Map<String, ?> environment;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String agentId;
/*     */   
/*     */   @Nullable
/*     */   private ObjectName[] actualObjectNames;
/*     */ 
/*     */   
/*     */   public void setServer(MBeanServerConnection server) {
/*  78 */     this.server = server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(@Nullable Map<String, ?> environment) {
/*  86 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map<String, ?> getEnvironment() {
/*  98 */     return this.environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceUrl(String url) throws MalformedURLException {
/* 105 */     this.serviceUrl = new JMXServiceURL(url);
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
/*     */   public void setAgentId(String agentId) {
/* 117 */     this.agentId = agentId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 123 */     if (getNotificationListener() == null) {
/* 124 */       throw new IllegalArgumentException("Property 'notificationListener' is required");
/*     */     }
/* 126 */     if (CollectionUtils.isEmpty(this.mappedObjectNames)) {
/* 127 */       throw new IllegalArgumentException("Property 'mappedObjectName' is required");
/*     */     }
/* 129 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 138 */     if (this.server == null) {
/* 139 */       this.server = this.connector.connect(this.serviceUrl, this.environment, this.agentId);
/*     */     }
/*     */     try {
/* 142 */       this.actualObjectNames = getResolvedObjectNames();
/* 143 */       if (this.actualObjectNames != null) {
/* 144 */         if (this.logger.isDebugEnabled()) {
/* 145 */           this.logger.debug("Registering NotificationListener for MBeans " + Arrays.<ObjectName>asList(this.actualObjectNames));
/*     */         }
/* 147 */         for (ObjectName actualObjectName : this.actualObjectNames) {
/* 148 */           this.server.addNotificationListener(actualObjectName, 
/* 149 */               getNotificationListener(), getNotificationFilter(), getHandback());
/*     */         }
/*     */       }
/*     */     
/* 153 */     } catch (IOException ex) {
/* 154 */       throw new MBeanServerNotFoundException("Could not connect to remote MBeanServer at URL [" + this.serviceUrl + "]", ex);
/*     */     
/*     */     }
/* 157 */     catch (Exception ex) {
/* 158 */       throw new JmxException("Unable to register NotificationListener", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/*     */     try {
/* 168 */       if (this.server != null && this.actualObjectNames != null) {
/* 169 */         for (ObjectName actualObjectName : this.actualObjectNames) {
/*     */           try {
/* 171 */             this.server.removeNotificationListener(actualObjectName, 
/* 172 */                 getNotificationListener(), getNotificationFilter(), getHandback());
/*     */           }
/* 174 */           catch (Exception ex) {
/* 175 */             if (this.logger.isDebugEnabled()) {
/* 176 */               this.logger.debug("Unable to unregister NotificationListener", ex);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } finally {
/*     */       
/* 183 */       this.connector.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/access/NotificationListenerRegistrar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */