/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.registry.LocateRegistry;
/*     */ import java.rmi.registry.Registry;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RmiRegistryFactoryBean
/*     */   implements FactoryBean<Registry>, InitializingBean, DisposableBean
/*     */ {
/*  66 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private String host;
/*     */   
/*  70 */   private int port = 1099;
/*     */ 
/*     */   
/*     */   private RMIClientSocketFactory clientSocketFactory;
/*     */ 
/*     */   
/*     */   private RMIServerSocketFactory serverSocketFactory;
/*     */ 
/*     */   
/*     */   private Registry registry;
/*     */ 
/*     */   
/*     */   private boolean alwaysCreate = false;
/*     */ 
/*     */   
/*     */   private boolean created = false;
/*     */ 
/*     */   
/*     */   public void setHost(String host) {
/*  89 */     this.host = host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHost() {
/*  96 */     return this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/* 105 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 112 */     return this.port;
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
/*     */   public void setClientSocketFactory(RMIClientSocketFactory clientSocketFactory) {
/* 125 */     this.clientSocketFactory = clientSocketFactory;
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
/*     */   public void setServerSocketFactory(RMIServerSocketFactory serverSocketFactory) {
/* 138 */     this.serverSocketFactory = serverSocketFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysCreate(boolean alwaysCreate) {
/* 149 */     this.alwaysCreate = alwaysCreate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 156 */     if (this.clientSocketFactory instanceof RMIServerSocketFactory) {
/* 157 */       this.serverSocketFactory = (RMIServerSocketFactory)this.clientSocketFactory;
/*     */     }
/* 159 */     if ((this.clientSocketFactory != null && this.serverSocketFactory == null) || (this.clientSocketFactory == null && this.serverSocketFactory != null))
/*     */     {
/* 161 */       throw new IllegalArgumentException("Both RMIClientSocketFactory and RMIServerSocketFactory or none required");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 166 */     this.registry = getRegistry(this.host, this.port, this.clientSocketFactory, this.serverSocketFactory);
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
/*     */   protected Registry getRegistry(String registryHost, int registryPort, @Nullable RMIClientSocketFactory clientSocketFactory, @Nullable RMIServerSocketFactory serverSocketFactory) throws RemoteException {
/* 184 */     if (registryHost != null) {
/*     */       
/* 186 */       if (this.logger.isDebugEnabled()) {
/* 187 */         this.logger.debug("Looking for RMI registry at port '" + registryPort + "' of host [" + registryHost + "]");
/*     */       }
/* 189 */       Registry reg = LocateRegistry.getRegistry(registryHost, registryPort, clientSocketFactory);
/* 190 */       testRegistry(reg);
/* 191 */       return reg;
/*     */     } 
/*     */ 
/*     */     
/* 195 */     return getRegistry(registryPort, clientSocketFactory, serverSocketFactory);
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
/*     */   protected Registry getRegistry(int registryPort, @Nullable RMIClientSocketFactory clientSocketFactory, @Nullable RMIServerSocketFactory serverSocketFactory) throws RemoteException {
/* 211 */     if (clientSocketFactory != null) {
/* 212 */       if (this.alwaysCreate) {
/* 213 */         this.logger.debug("Creating new RMI registry");
/* 214 */         this.created = true;
/* 215 */         return LocateRegistry.createRegistry(registryPort, clientSocketFactory, serverSocketFactory);
/*     */       } 
/* 217 */       if (this.logger.isDebugEnabled()) {
/* 218 */         this.logger.debug("Looking for RMI registry at port '" + registryPort + "', using custom socket factory");
/*     */       }
/* 220 */       synchronized (LocateRegistry.class) {
/*     */ 
/*     */         
/* 223 */         Registry reg = LocateRegistry.getRegistry(null, registryPort, clientSocketFactory);
/* 224 */         testRegistry(reg);
/* 225 */         return reg;
/*     */       } 
/*     */     } 
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
/* 238 */     return getRegistry(registryPort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Registry getRegistry(int registryPort) throws RemoteException {
/* 249 */     if (this.alwaysCreate) {
/* 250 */       this.logger.debug("Creating new RMI registry");
/* 251 */       this.created = true;
/* 252 */       return LocateRegistry.createRegistry(registryPort);
/*     */     } 
/* 254 */     if (this.logger.isDebugEnabled()) {
/* 255 */       this.logger.debug("Looking for RMI registry at port '" + registryPort + "'");
/*     */     }
/* 257 */     synchronized (LocateRegistry.class) {
/*     */ 
/*     */       
/* 260 */       Registry reg = LocateRegistry.getRegistry(registryPort);
/* 261 */       testRegistry(reg);
/* 262 */       return reg;
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
/*     */   
/*     */   protected void testRegistry(Registry registry) throws RemoteException {
/* 283 */     registry.list();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Registry getObject() throws Exception {
/* 289 */     return this.registry;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends Registry> getObjectType() {
/* 294 */     return (this.registry != null) ? (Class)this.registry.getClass() : Registry.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws RemoteException {
/* 309 */     if (this.created) {
/* 310 */       this.logger.debug("Unexporting RMI registry");
/* 311 */       UnicastRemoteObject.unexportObject(this.registry, true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/rmi/RmiRegistryFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */