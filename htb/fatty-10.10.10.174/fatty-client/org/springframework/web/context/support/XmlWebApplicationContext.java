/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.xml.ResourceEntityResolver;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlWebApplicationContext
/*     */   extends AbstractRefreshableWebApplicationContext
/*     */ {
/*     */   public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
/*     */   public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
/*     */   public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";
/*     */   
/*     */   protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
/*  83 */     XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)beanFactory);
/*     */ 
/*     */ 
/*     */     
/*  87 */     beanDefinitionReader.setEnvironment((Environment)getEnvironment());
/*  88 */     beanDefinitionReader.setResourceLoader((ResourceLoader)this);
/*  89 */     beanDefinitionReader.setEntityResolver((EntityResolver)new ResourceEntityResolver((ResourceLoader)this));
/*     */ 
/*     */ 
/*     */     
/*  93 */     initBeanDefinitionReader(beanDefinitionReader);
/*  94 */     loadBeanDefinitions(beanDefinitionReader);
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
/*     */   protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws IOException {
/* 122 */     String[] configLocations = getConfigLocations();
/* 123 */     if (configLocations != null) {
/* 124 */       for (String configLocation : configLocations) {
/* 125 */         reader.loadBeanDefinitions(configLocation);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getDefaultConfigLocations() {
/* 137 */     if (getNamespace() != null) {
/* 138 */       return new String[] { "/WEB-INF/" + getNamespace() + ".xml" };
/*     */     }
/*     */     
/* 141 */     return new String[] { "/WEB-INF/applicationContext.xml" };
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/XmlWebApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */