/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.remote.JMXConnectorServer;
/*     */ import javax.management.remote.JMXConnectorServerFactory;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import javax.management.remote.MBeanServerForwarder;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.JmxException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConnectorServerFactoryBean
/*     */   extends MBeanRegistrationSupport
/*     */   implements FactoryBean<JMXConnectorServer>, InitializingBean, DisposableBean
/*     */ {
/*     */   public static final String DEFAULT_SERVICE_URL = "service:jmx:jmxmp://localhost:9875";
/*  63 */   private String serviceUrl = "service:jmx:jmxmp://localhost:9875";
/*     */   
/*  65 */   private Map<String, Object> environment = new HashMap<>();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MBeanServerForwarder forwarder;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ObjectName objectName;
/*     */ 
/*     */   
/*     */   private boolean threaded = false;
/*     */   
/*     */   private boolean daemon = false;
/*     */   
/*     */   @Nullable
/*     */   private JMXConnectorServer connectorServer;
/*     */ 
/*     */   
/*     */   public void setServiceUrl(String serviceUrl) {
/*  85 */     this.serviceUrl = serviceUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(@Nullable Properties environment) {
/*  93 */     CollectionUtils.mergePropertiesIntoMap(environment, this.environment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironmentMap(@Nullable Map<String, ?> environment) {
/* 101 */     if (environment != null) {
/* 102 */       this.environment.putAll(environment);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForwarder(MBeanServerForwarder forwarder) {
/* 110 */     this.forwarder = forwarder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjectName(Object objectName) throws MalformedObjectNameException {
/* 120 */     this.objectName = ObjectNameManager.getInstance(objectName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreaded(boolean threaded) {
/* 127 */     this.threaded = threaded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDaemon(boolean daemon) {
/* 135 */     this.daemon = daemon;
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
/*     */   public void afterPropertiesSet() throws JMException, IOException {
/* 150 */     if (this.server == null) {
/* 151 */       this.server = JmxUtils.locateMBeanServer();
/*     */     }
/*     */ 
/*     */     
/* 155 */     JMXServiceURL url = new JMXServiceURL(this.serviceUrl);
/*     */ 
/*     */     
/* 158 */     this.connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, this.environment, this.server);
/*     */ 
/*     */     
/* 161 */     if (this.forwarder != null) {
/* 162 */       this.connectorServer.setMBeanServerForwarder(this.forwarder);
/*     */     }
/*     */ 
/*     */     
/* 166 */     if (this.objectName != null) {
/* 167 */       doRegister(this.connectorServer, this.objectName);
/*     */     }
/*     */     
/*     */     try {
/* 171 */       if (this.threaded) {
/*     */         
/* 173 */         final JMXConnectorServer serverToStart = this.connectorServer;
/* 174 */         Thread connectorThread = new Thread()
/*     */           {
/*     */             public void run() {
/*     */               try {
/* 178 */                 serverToStart.start();
/*     */               }
/* 180 */               catch (IOException ex) {
/* 181 */                 throw new JmxException("Could not start JMX connector server after delay", ex);
/*     */               } 
/*     */             }
/*     */           };
/*     */         
/* 186 */         connectorThread.setName("JMX Connector Thread [" + this.serviceUrl + "]");
/* 187 */         connectorThread.setDaemon(this.daemon);
/* 188 */         connectorThread.start();
/*     */       }
/*     */       else {
/*     */         
/* 192 */         this.connectorServer.start();
/*     */       } 
/*     */       
/* 195 */       if (this.logger.isInfoEnabled()) {
/* 196 */         this.logger.info("JMX connector server started: " + this.connectorServer);
/*     */       
/*     */       }
/*     */     }
/* 200 */     catch (IOException ex) {
/*     */       
/* 202 */       unregisterBeans();
/* 203 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public JMXConnectorServer getObject() {
/* 211 */     return this.connectorServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends JMXConnectorServer> getObjectType() {
/* 216 */     return (this.connectorServer != null) ? (Class)this.connectorServer.getClass() : JMXConnectorServer.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 221 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws IOException {
/*     */     try {
/* 233 */       if (this.connectorServer != null) {
/* 234 */         if (this.logger.isInfoEnabled()) {
/* 235 */           this.logger.info("Stopping JMX connector server: " + this.connectorServer);
/*     */         }
/* 237 */         this.connectorServer.stop();
/*     */       } 
/*     */     } finally {
/*     */       
/* 241 */       unregisterBeans();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/support/ConnectorServerFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */