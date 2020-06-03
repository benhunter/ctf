/*     */ package org.springframework.remoting.jaxws;
/*     */ 
/*     */ import com.sun.net.httpserver.Authenticator;
/*     */ import com.sun.net.httpserver.Filter;
/*     */ import com.sun.net.httpserver.HttpContext;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.List;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.ws.Endpoint;
/*     */ import javax.xml.ws.WebServiceProvider;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.lang.UsesSunHttpServer;
/*     */ import org.springframework.util.Assert;
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
/*     */ @Deprecated
/*     */ @UsesSunHttpServer
/*     */ public class SimpleHttpServerJaxWsServiceExporter
/*     */   extends AbstractJaxWsServiceExporter
/*     */ {
/*  57 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private HttpServer server;
/*     */   
/*  62 */   private int port = 8080;
/*     */   
/*     */   @Nullable
/*     */   private String hostname;
/*     */   
/*  67 */   private int backlog = -1;
/*     */   
/*  69 */   private int shutdownDelay = 0;
/*     */   
/*  71 */   private String basePath = "/";
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private List<Filter> filters;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Authenticator authenticator;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean localServer = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServer(HttpServer server) {
/*  91 */     this.server = server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/* 100 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHostname(String hostname) {
/* 110 */     this.hostname = hostname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBacklog(int backlog) {
/* 120 */     this.backlog = backlog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShutdownDelay(int shutdownDelay) {
/* 130 */     this.shutdownDelay = shutdownDelay;
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
/*     */   public void setBasePath(String basePath) {
/* 142 */     this.basePath = basePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilters(List<Filter> filters) {
/* 150 */     this.filters = filters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAuthenticator(Authenticator authenticator) {
/* 158 */     this.authenticator = authenticator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 164 */     if (this.server == null) {
/* 165 */       InetSocketAddress address = (this.hostname != null) ? new InetSocketAddress(this.hostname, this.port) : new InetSocketAddress(this.port);
/*     */       
/* 167 */       HttpServer server = HttpServer.create(address, this.backlog);
/* 168 */       if (this.logger.isInfoEnabled()) {
/* 169 */         this.logger.info("Starting HttpServer at address " + address);
/*     */       }
/* 171 */       server.start();
/* 172 */       this.server = server;
/* 173 */       this.localServer = true;
/*     */     } 
/* 175 */     super.afterPropertiesSet();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void publishEndpoint(Endpoint endpoint, WebService annotation) {
/* 180 */     endpoint.publish(buildHttpContext(endpoint, annotation.serviceName()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void publishEndpoint(Endpoint endpoint, WebServiceProvider annotation) {
/* 185 */     endpoint.publish(buildHttpContext(endpoint, annotation.serviceName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpContext buildHttpContext(Endpoint endpoint, String serviceName) {
/* 195 */     Assert.state((this.server != null), "No HttpServer available");
/* 196 */     String fullPath = calculateEndpointPath(endpoint, serviceName);
/* 197 */     HttpContext httpContext = this.server.createContext(fullPath);
/* 198 */     if (this.filters != null) {
/* 199 */       httpContext.getFilters().addAll(this.filters);
/*     */     }
/* 201 */     if (this.authenticator != null) {
/* 202 */       httpContext.setAuthenticator(this.authenticator);
/*     */     }
/* 204 */     return httpContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String calculateEndpointPath(Endpoint endpoint, String serviceName) {
/* 214 */     return this.basePath + serviceName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 220 */     super.destroy();
/* 221 */     if (this.server != null && this.localServer) {
/* 222 */       this.logger.info("Stopping HttpServer");
/* 223 */       this.server.stop(this.shutdownDelay);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/jaxws/SimpleHttpServerJaxWsServiceExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */