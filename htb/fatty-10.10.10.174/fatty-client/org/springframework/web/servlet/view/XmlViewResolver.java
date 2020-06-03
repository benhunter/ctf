/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.xml.ResourceEntityResolver;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.context.support.GenericWebApplicationContext;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.xml.sax.EntityResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlViewResolver
/*     */   extends AbstractCachingViewResolver
/*     */   implements Ordered, InitializingBean, DisposableBean
/*     */ {
/*     */   public static final String DEFAULT_LOCATION = "/WEB-INF/views.xml";
/*     */   @Nullable
/*     */   private Resource location;
/*     */   @Nullable
/*     */   private ConfigurableApplicationContext cachedFactory;
/*  71 */   private int order = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(Resource location) {
/*  80 */     this.location = location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/*  89 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  94 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws BeansException {
/* 103 */     if (isCache()) {
/* 104 */       initFactory();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getCacheKey(String viewName, Locale locale) {
/* 115 */     return viewName;
/*     */   }
/*     */ 
/*     */   
/*     */   protected View loadView(String viewName, Locale locale) throws BeansException {
/* 120 */     BeanFactory factory = initFactory();
/*     */     try {
/* 122 */       return (View)factory.getBean(viewName, View.class);
/*     */     }
/* 124 */     catch (NoSuchBeanDefinitionException ex) {
/*     */       
/* 126 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized BeanFactory initFactory() throws BeansException {
/* 136 */     if (this.cachedFactory != null) {
/* 137 */       return (BeanFactory)this.cachedFactory;
/*     */     }
/*     */     
/* 140 */     ApplicationContext applicationContext = obtainApplicationContext();
/*     */     
/* 142 */     Resource actualLocation = this.location;
/* 143 */     if (actualLocation == null) {
/* 144 */       actualLocation = applicationContext.getResource("/WEB-INF/views.xml");
/*     */     }
/*     */ 
/*     */     
/* 148 */     GenericWebApplicationContext factory = new GenericWebApplicationContext();
/* 149 */     factory.setParent(applicationContext);
/* 150 */     factory.setServletContext(getServletContext());
/*     */ 
/*     */     
/* 153 */     XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)factory);
/* 154 */     reader.setEnvironment(applicationContext.getEnvironment());
/* 155 */     reader.setEntityResolver((EntityResolver)new ResourceEntityResolver((ResourceLoader)applicationContext));
/* 156 */     reader.loadBeanDefinitions(actualLocation);
/*     */     
/* 158 */     factory.refresh();
/*     */     
/* 160 */     if (isCache()) {
/* 161 */       this.cachedFactory = (ConfigurableApplicationContext)factory;
/*     */     }
/* 163 */     return (BeanFactory)factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws BeansException {
/* 172 */     if (this.cachedFactory != null)
/* 173 */       this.cachedFactory.close(); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/XmlViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */