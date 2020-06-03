/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.remote.JMXConnector;
/*     */ import javax.management.remote.JMXConnectorFactory;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.target.AbstractLazyCreationTargetSource;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class MBeanServerConnectionFactoryBean
/*     */   implements FactoryBean<MBeanServerConnection>, BeanClassLoaderAware, InitializingBean, DisposableBean
/*     */ {
/*     */   @Nullable
/*     */   private JMXServiceURL serviceUrl;
/*  60 */   private Map<String, Object> environment = new HashMap<>();
/*     */   
/*     */   private boolean connectOnStartup = true;
/*     */   
/*     */   @Nullable
/*  65 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private JMXConnector connector;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MBeanServerConnection connection;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private JMXConnectorLazyInitTargetSource connectorTargetSource;
/*     */ 
/*     */   
/*     */   public void setServiceUrl(String url) throws MalformedURLException {
/*  81 */     this.serviceUrl = new JMXServiceURL(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Properties environment) {
/*  89 */     CollectionUtils.mergePropertiesIntoMap(environment, this.environment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironmentMap(@Nullable Map<String, ?> environment) {
/*  97 */     if (environment != null) {
/*  98 */       this.environment.putAll(environment);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectOnStartup(boolean connectOnStartup) {
/* 108 */     this.connectOnStartup = connectOnStartup;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 113 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws IOException {
/* 123 */     if (this.serviceUrl == null) {
/* 124 */       throw new IllegalArgumentException("Property 'serviceUrl' is required");
/*     */     }
/*     */     
/* 127 */     if (this.connectOnStartup) {
/* 128 */       connect();
/*     */     } else {
/*     */       
/* 131 */       createLazyConnection();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void connect() throws IOException {
/* 140 */     Assert.state((this.serviceUrl != null), "No JMXServiceURL set");
/* 141 */     this.connector = JMXConnectorFactory.connect(this.serviceUrl, this.environment);
/* 142 */     this.connection = this.connector.getMBeanServerConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createLazyConnection() {
/* 149 */     this.connectorTargetSource = new JMXConnectorLazyInitTargetSource();
/* 150 */     MBeanServerConnectionLazyInitTargetSource mBeanServerConnectionLazyInitTargetSource = new MBeanServerConnectionLazyInitTargetSource();
/*     */     
/* 152 */     this
/* 153 */       .connector = (JMXConnector)(new ProxyFactory(JMXConnector.class, (TargetSource)this.connectorTargetSource)).getProxy(this.beanClassLoader);
/* 154 */     this
/* 155 */       .connection = (MBeanServerConnection)(new ProxyFactory(MBeanServerConnection.class, (TargetSource)mBeanServerConnectionLazyInitTargetSource)).getProxy(this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MBeanServerConnection getObject() {
/* 162 */     return this.connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends MBeanServerConnection> getObjectType() {
/* 167 */     return (this.connection != null) ? (Class)this.connection.getClass() : MBeanServerConnection.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws IOException {
/* 181 */     if (this.connector != null && (this.connectorTargetSource == null || this.connectorTargetSource
/* 182 */       .isInitialized())) {
/* 183 */       this.connector.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class JMXConnectorLazyInitTargetSource
/*     */     extends AbstractLazyCreationTargetSource
/*     */   {
/*     */     private JMXConnectorLazyInitTargetSource() {}
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object createObject() throws Exception {
/* 198 */       Assert.state((MBeanServerConnectionFactoryBean.this.serviceUrl != null), "No JMXServiceURL set");
/* 199 */       return JMXConnectorFactory.connect(MBeanServerConnectionFactoryBean.this.serviceUrl, MBeanServerConnectionFactoryBean.this.environment);
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getTargetClass() {
/* 204 */       return JMXConnector.class;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class MBeanServerConnectionLazyInitTargetSource
/*     */     extends AbstractLazyCreationTargetSource
/*     */   {
/*     */     private MBeanServerConnectionLazyInitTargetSource() {}
/*     */ 
/*     */     
/*     */     protected Object createObject() throws Exception {
/* 216 */       Assert.state((MBeanServerConnectionFactoryBean.this.connector != null), "JMXConnector not initialized");
/* 217 */       return MBeanServerConnectionFactoryBean.this.connector.getMBeanServerConnection();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getTargetClass() {
/* 222 */       return MBeanServerConnection.class;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/support/MBeanServerConnectionFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */