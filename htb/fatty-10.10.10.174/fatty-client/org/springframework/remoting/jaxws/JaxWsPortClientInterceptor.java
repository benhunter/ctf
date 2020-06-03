/*     */ package org.springframework.remoting.jaxws;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.BindingProvider;
/*     */ import javax.xml.ws.ProtocolException;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.soap.SOAPFaultException;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.remoting.RemoteAccessException;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.RemoteProxyFailureException;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class JaxWsPortClientInterceptor
/*     */   extends LocalJaxWsServiceFactory
/*     */   implements MethodInterceptor, BeanClassLoaderAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private Service jaxWsService;
/*     */   @Nullable
/*     */   private String portName;
/*     */   @Nullable
/*     */   private String username;
/*     */   @Nullable
/*     */   private String password;
/*     */   @Nullable
/*     */   private String endpointAddress;
/*     */   private boolean maintainSession;
/*     */   private boolean useSoapAction;
/*     */   @Nullable
/*     */   private String soapActionUri;
/*     */   @Nullable
/*     */   private Map<String, Object> customProperties;
/*     */   @Nullable
/*     */   private WebServiceFeature[] portFeatures;
/*     */   @Nullable
/*     */   private Class<?> serviceInterface;
/*     */   private boolean lookupServiceOnStartup = true;
/*     */   @Nullable
/* 102 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/*     */   @Nullable
/*     */   private QName portQName;
/*     */   
/*     */   @Nullable
/*     */   private Object portStub;
/*     */   
/* 110 */   private final Object preparationMonitor = new Object();
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
/*     */   public void setJaxWsService(@Nullable Service jaxWsService) {
/* 123 */     this.jaxWsService = jaxWsService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Service getJaxWsService() {
/* 131 */     return this.jaxWsService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPortName(@Nullable String portName) {
/* 139 */     this.portName = portName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPortName() {
/* 147 */     return this.portName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUsername(@Nullable String username) {
/* 155 */     this.username = username;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getUsername() {
/* 163 */     return this.username;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(@Nullable String password) {
/* 171 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPassword() {
/* 179 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndpointAddress(@Nullable String endpointAddress) {
/* 187 */     this.endpointAddress = endpointAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEndpointAddress() {
/* 195 */     return this.endpointAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaintainSession(boolean maintainSession) {
/* 203 */     this.maintainSession = maintainSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMaintainSession() {
/* 210 */     return this.maintainSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseSoapAction(boolean useSoapAction) {
/* 218 */     this.useSoapAction = useSoapAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseSoapAction() {
/* 225 */     return this.useSoapAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSoapActionUri(@Nullable String soapActionUri) {
/* 233 */     this.soapActionUri = soapActionUri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSoapActionUri() {
/* 241 */     return this.soapActionUri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCustomProperties(Map<String, Object> customProperties) {
/* 251 */     this.customProperties = customProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getCustomProperties() {
/* 262 */     if (this.customProperties == null) {
/* 263 */       this.customProperties = new HashMap<>();
/*     */     }
/* 265 */     return this.customProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCustomProperty(String name, Object value) {
/* 275 */     getCustomProperties().put(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPortFeatures(WebServiceFeature... features) {
/* 286 */     this.portFeatures = features;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceInterface(@Nullable Class<?> serviceInterface) {
/* 293 */     if (serviceInterface != null) {
/* 294 */       Assert.isTrue(serviceInterface.isInterface(), "'serviceInterface' must be an interface");
/*     */     }
/* 296 */     this.serviceInterface = serviceInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getServiceInterface() {
/* 304 */     return this.serviceInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLookupServiceOnStartup(boolean lookupServiceOnStartup) {
/* 314 */     this.lookupServiceOnStartup = lookupServiceOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(@Nullable ClassLoader classLoader) {
/* 323 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ClassLoader getBeanClassLoader() {
/* 331 */     return this.beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 337 */     if (this.lookupServiceOnStartup) {
/* 338 */       prepare();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 346 */     Class<?> ifc = getServiceInterface();
/* 347 */     Assert.notNull(ifc, "Property 'serviceInterface' is required");
/*     */     
/* 349 */     WebService ann = ifc.<WebService>getAnnotation(WebService.class);
/* 350 */     if (ann != null) {
/* 351 */       applyDefaultsFromAnnotation(ann);
/*     */     }
/*     */     
/* 354 */     Service serviceToUse = getJaxWsService();
/* 355 */     if (serviceToUse == null) {
/* 356 */       serviceToUse = createJaxWsService();
/*     */     }
/*     */     
/* 359 */     this.portQName = getQName((getPortName() != null) ? getPortName() : ifc.getName());
/* 360 */     Object stub = getPortStub(serviceToUse, (getPortName() != null) ? this.portQName : null);
/* 361 */     preparePortStub(stub);
/* 362 */     this.portStub = stub;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyDefaultsFromAnnotation(WebService ann) {
/* 373 */     if (getWsdlDocumentUrl() == null) {
/* 374 */       String wsdl = ann.wsdlLocation();
/* 375 */       if (StringUtils.hasText(wsdl)) {
/*     */         try {
/* 377 */           setWsdlDocumentUrl(new URL(wsdl));
/*     */         }
/* 379 */         catch (MalformedURLException ex) {
/* 380 */           throw new IllegalStateException("Encountered invalid @Service wsdlLocation value [" + wsdl + "]", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 385 */     if (getNamespaceUri() == null) {
/* 386 */       String ns = ann.targetNamespace();
/* 387 */       if (StringUtils.hasText(ns)) {
/* 388 */         setNamespaceUri(ns);
/*     */       }
/*     */     } 
/* 391 */     if (getServiceName() == null) {
/* 392 */       String sn = ann.serviceName();
/* 393 */       if (StringUtils.hasText(sn)) {
/* 394 */         setServiceName(sn);
/*     */       }
/*     */     } 
/* 397 */     if (getPortName() == null) {
/* 398 */       String pn = ann.portName();
/* 399 */       if (StringUtils.hasText(pn)) {
/* 400 */         setPortName(pn);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPrepared() {
/* 410 */     synchronized (this.preparationMonitor) {
/* 411 */       return (this.portStub != null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected final QName getPortQName() {
/* 422 */     return this.portQName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getPortStub(Service service, @Nullable QName portQName) {
/* 433 */     if (this.portFeatures != null) {
/* 434 */       return (portQName != null) ? service.getPort(portQName, getServiceInterface(), this.portFeatures) : service
/* 435 */         .getPort(getServiceInterface(), this.portFeatures);
/*     */     }
/*     */     
/* 438 */     return (portQName != null) ? service.getPort(portQName, getServiceInterface()) : service
/* 439 */       .getPort(getServiceInterface());
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
/*     */   protected void preparePortStub(Object stub) {
/* 454 */     Map<String, Object> stubProperties = new HashMap<>();
/* 455 */     String username = getUsername();
/* 456 */     if (username != null) {
/* 457 */       stubProperties.put("javax.xml.ws.security.auth.username", username);
/*     */     }
/* 459 */     String password = getPassword();
/* 460 */     if (password != null) {
/* 461 */       stubProperties.put("javax.xml.ws.security.auth.password", password);
/*     */     }
/* 463 */     String endpointAddress = getEndpointAddress();
/* 464 */     if (endpointAddress != null) {
/* 465 */       stubProperties.put("javax.xml.ws.service.endpoint.address", endpointAddress);
/*     */     }
/* 467 */     if (isMaintainSession()) {
/* 468 */       stubProperties.put("javax.xml.ws.session.maintain", Boolean.TRUE);
/*     */     }
/* 470 */     if (isUseSoapAction()) {
/* 471 */       stubProperties.put("javax.xml.ws.soap.http.soapaction.use", Boolean.TRUE);
/*     */     }
/* 473 */     String soapActionUri = getSoapActionUri();
/* 474 */     if (soapActionUri != null) {
/* 475 */       stubProperties.put("javax.xml.ws.soap.http.soapaction.uri", soapActionUri);
/*     */     }
/* 477 */     stubProperties.putAll(getCustomProperties());
/* 478 */     if (!stubProperties.isEmpty()) {
/* 479 */       if (!(stub instanceof BindingProvider)) {
/* 480 */         throw new RemoteLookupFailureException("Port stub of class [" + stub.getClass().getName() + "] is not a customizable JAX-WS stub: it does not implement interface [javax.xml.ws.BindingProvider]");
/*     */       }
/*     */       
/* 483 */       ((BindingProvider)stub).getRequestContext().putAll(stubProperties);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getPortStub() {
/* 493 */     return this.portStub;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 500 */     if (AopUtils.isToStringMethod(invocation.getMethod())) {
/* 501 */       return "JAX-WS proxy for port [" + getPortName() + "] of service [" + getServiceName() + "]";
/*     */     }
/*     */     
/* 504 */     synchronized (this.preparationMonitor) {
/* 505 */       if (!isPrepared()) {
/* 506 */         prepare();
/*     */       }
/*     */     } 
/* 509 */     return doInvoke(invocation);
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
/*     */   @Nullable
/*     */   protected Object doInvoke(MethodInvocation invocation) throws Throwable {
/*     */     try {
/* 523 */       return doInvoke(invocation, getPortStub());
/*     */     }
/* 525 */     catch (SOAPFaultException ex) {
/* 526 */       throw new JaxWsSoapFaultException(ex);
/*     */     }
/* 528 */     catch (ProtocolException ex) {
/* 529 */       throw new RemoteConnectFailureException("Could not connect to remote service [" + 
/* 530 */           getEndpointAddress() + "]", ex);
/*     */     }
/* 532 */     catch (WebServiceException ex) {
/* 533 */       throw new RemoteAccessException("Could not access remote service at [" + 
/* 534 */           getEndpointAddress() + "]", ex);
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
/*     */   @Nullable
/*     */   protected Object doInvoke(MethodInvocation invocation, @Nullable Object portStub) throws Throwable {
/* 548 */     Method method = invocation.getMethod();
/*     */     try {
/* 550 */       return method.invoke(portStub, invocation.getArguments());
/*     */     }
/* 552 */     catch (InvocationTargetException ex) {
/* 553 */       throw ex.getTargetException();
/*     */     }
/* 555 */     catch (Throwable ex) {
/* 556 */       throw new RemoteProxyFailureException("Invocation of stub method failed: " + method, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/jaxws/JaxWsPortClientInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */