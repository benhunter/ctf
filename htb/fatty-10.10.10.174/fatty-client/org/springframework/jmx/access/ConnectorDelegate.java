/*    */ package org.springframework.jmx.access;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import javax.management.MBeanServerConnection;
/*    */ import javax.management.remote.JMXConnector;
/*    */ import javax.management.remote.JMXConnectorFactory;
/*    */ import javax.management.remote.JMXServiceURL;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.jmx.MBeanServerNotFoundException;
/*    */ import org.springframework.jmx.support.JmxUtils;
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
/*    */ class ConnectorDelegate
/*    */ {
/* 41 */   private static final Log logger = LogFactory.getLog(ConnectorDelegate.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   private JMXConnector connector;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MBeanServerConnection connect(@Nullable JMXServiceURL serviceUrl, @Nullable Map<String, ?> environment, @Nullable String agentId) throws MBeanServerNotFoundException {
/* 57 */     if (serviceUrl != null) {
/* 58 */       if (logger.isDebugEnabled()) {
/* 59 */         logger.debug("Connecting to remote MBeanServer at URL [" + serviceUrl + "]");
/*    */       }
/*    */       try {
/* 62 */         this.connector = JMXConnectorFactory.connect(serviceUrl, environment);
/* 63 */         return this.connector.getMBeanServerConnection();
/*    */       }
/* 65 */       catch (IOException ex) {
/* 66 */         throw new MBeanServerNotFoundException("Could not connect to remote MBeanServer [" + serviceUrl + "]", ex);
/*    */       } 
/*    */     } 
/*    */     
/* 70 */     logger.debug("Attempting to locate local MBeanServer");
/* 71 */     return JmxUtils.locateMBeanServer(agentId);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 79 */     if (this.connector != null)
/*    */       try {
/* 81 */         this.connector.close();
/*    */       }
/* 83 */       catch (IOException ex) {
/* 84 */         logger.debug("Could not close JMX connector", ex);
/*    */       }  
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/access/ConnectorDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */