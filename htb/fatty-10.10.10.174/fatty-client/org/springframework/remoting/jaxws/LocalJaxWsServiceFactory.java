/*     */ package org.springframework.remoting.jaxws;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.handler.HandlerResolver;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalJaxWsServiceFactory
/*     */ {
/*     */   @Nullable
/*     */   private URL wsdlDocumentUrl;
/*     */   @Nullable
/*     */   private String namespaceUri;
/*     */   @Nullable
/*     */   private String serviceName;
/*     */   @Nullable
/*     */   private WebServiceFeature[] serviceFeatures;
/*     */   @Nullable
/*     */   private Executor executor;
/*     */   @Nullable
/*     */   private HandlerResolver handlerResolver;
/*     */   
/*     */   public void setWsdlDocumentUrl(@Nullable URL wsdlDocumentUrl) {
/*  71 */     this.wsdlDocumentUrl = wsdlDocumentUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWsdlDocumentResource(Resource wsdlDocumentResource) throws IOException {
/*  79 */     Assert.notNull(wsdlDocumentResource, "WSDL Resource must not be null");
/*  80 */     this.wsdlDocumentUrl = wsdlDocumentResource.getURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public URL getWsdlDocumentUrl() {
/*  88 */     return this.wsdlDocumentUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamespaceUri(@Nullable String namespaceUri) {
/*  96 */     this.namespaceUri = (namespaceUri != null) ? namespaceUri.trim() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getNamespaceUri() {
/* 104 */     return this.namespaceUri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceName(@Nullable String serviceName) {
/* 112 */     this.serviceName = serviceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getServiceName() {
/* 120 */     return this.serviceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceFeatures(WebServiceFeature... serviceFeatures) {
/* 130 */     this.serviceFeatures = serviceFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutor(Executor executor) {
/* 139 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerResolver(HandlerResolver handlerResolver) {
/* 148 */     this.handlerResolver = handlerResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Service createJaxWsService() {
/*     */     Service service;
/* 158 */     Assert.notNull(this.serviceName, "No service name specified");
/*     */ 
/*     */     
/* 161 */     if (this.serviceFeatures != null) {
/*     */ 
/*     */       
/* 164 */       service = (this.wsdlDocumentUrl != null) ? Service.create(this.wsdlDocumentUrl, getQName(this.serviceName), this.serviceFeatures) : Service.create(getQName(this.serviceName), this.serviceFeatures);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 169 */       service = (this.wsdlDocumentUrl != null) ? Service.create(this.wsdlDocumentUrl, getQName(this.serviceName)) : Service.create(getQName(this.serviceName));
/*     */     } 
/*     */     
/* 172 */     if (this.executor != null) {
/* 173 */       service.setExecutor(this.executor);
/*     */     }
/* 175 */     if (this.handlerResolver != null) {
/* 176 */       service.setHandlerResolver(this.handlerResolver);
/*     */     }
/*     */     
/* 179 */     return service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName getQName(String name) {
/* 188 */     return (getNamespaceUri() != null) ? new QName(getNamespaceUri(), name) : new QName(name);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/jaxws/LocalJaxWsServiceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */