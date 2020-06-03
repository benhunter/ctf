/*     */ package org.springframework.remoting.jaxws;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.ws.Endpoint;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.WebServiceProvider;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
/*     */ public abstract class AbstractJaxWsServiceExporter
/*     */   implements BeanFactoryAware, InitializingBean, DisposableBean
/*     */ {
/*     */   @Nullable
/*     */   private Map<String, Object> endpointProperties;
/*     */   @Nullable
/*     */   private Executor executor;
/*     */   @Nullable
/*     */   private String bindingType;
/*     */   @Nullable
/*     */   private WebServiceFeature[] endpointFeatures;
/*     */   @Nullable
/*     */   private ListableBeanFactory beanFactory;
/*  69 */   private final Set<Endpoint> publishedEndpoints = new LinkedHashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndpointProperties(Map<String, Object> endpointProperties) {
/*  80 */     this.endpointProperties = endpointProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutor(Executor executor) {
/*  89 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBindingType(String bindingType) {
/*  97 */     this.bindingType = bindingType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndpointFeatures(WebServiceFeature... endpointFeatures) {
/* 106 */     this.endpointFeatures = endpointFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 114 */     if (!(beanFactory instanceof ListableBeanFactory)) {
/* 115 */       throw new IllegalStateException(getClass().getSimpleName() + " requires a ListableBeanFactory");
/*     */     }
/* 117 */     this.beanFactory = (ListableBeanFactory)beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 127 */     publishEndpoints();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void publishEndpoints() {
/* 136 */     Assert.state((this.beanFactory != null), "No BeanFactory set");
/*     */     
/* 138 */     Set<String> beanNames = new LinkedHashSet<>(this.beanFactory.getBeanDefinitionCount());
/* 139 */     Collections.addAll(beanNames, this.beanFactory.getBeanDefinitionNames());
/* 140 */     if (this.beanFactory instanceof ConfigurableBeanFactory) {
/* 141 */       Collections.addAll(beanNames, ((ConfigurableBeanFactory)this.beanFactory).getSingletonNames());
/*     */     }
/*     */     
/* 144 */     for (String beanName : beanNames) {
/*     */       try {
/* 146 */         Class<?> type = this.beanFactory.getType(beanName);
/* 147 */         if (type != null && !type.isInterface()) {
/* 148 */           WebService wsAnnotation = type.<WebService>getAnnotation(WebService.class);
/* 149 */           WebServiceProvider wsProviderAnnotation = type.<WebServiceProvider>getAnnotation(WebServiceProvider.class);
/* 150 */           if (wsAnnotation != null || wsProviderAnnotation != null) {
/* 151 */             Endpoint endpoint = createEndpoint(this.beanFactory.getBean(beanName));
/* 152 */             if (this.endpointProperties != null) {
/* 153 */               endpoint.setProperties(this.endpointProperties);
/*     */             }
/* 155 */             if (this.executor != null) {
/* 156 */               endpoint.setExecutor(this.executor);
/*     */             }
/* 158 */             if (wsAnnotation != null) {
/* 159 */               publishEndpoint(endpoint, wsAnnotation);
/*     */             } else {
/*     */               
/* 162 */               publishEndpoint(endpoint, wsProviderAnnotation);
/*     */             } 
/* 164 */             this.publishedEndpoints.add(endpoint);
/*     */           }
/*     */         
/*     */         } 
/* 168 */       } catch (CannotLoadBeanClassException cannotLoadBeanClassException) {}
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
/*     */   protected Endpoint createEndpoint(Object bean) {
/* 182 */     return (this.endpointFeatures != null) ? 
/* 183 */       Endpoint.create(this.bindingType, bean, this.endpointFeatures) : 
/* 184 */       Endpoint.create(this.bindingType, bean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void publishEndpoint(Endpoint paramEndpoint, WebService paramWebService);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void publishEndpoint(Endpoint paramEndpoint, WebServiceProvider paramWebServiceProvider);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 208 */     for (Endpoint endpoint : this.publishedEndpoints)
/* 209 */       endpoint.stop(); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/jaxws/AbstractJaxWsServiceExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */