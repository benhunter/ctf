/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.xml.ResourceEntityResolver;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public abstract class AbstractXmlApplicationContext
/*     */   extends AbstractRefreshableConfigApplicationContext
/*     */ {
/*     */   private boolean validating = true;
/*     */   
/*     */   public AbstractXmlApplicationContext() {}
/*     */   
/*     */   public AbstractXmlApplicationContext(@Nullable ApplicationContext parent) {
/*  62 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidating(boolean validating) {
/*  70 */     this.validating = validating;
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
/*     */   protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
/* 106 */     reader.setValidating(this.validating);
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
/*     */   protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
/* 122 */     Resource[] configResources = getConfigResources();
/* 123 */     if (configResources != null) {
/* 124 */       reader.loadBeanDefinitions(configResources);
/*     */     }
/* 126 */     String[] configLocations = getConfigLocations();
/* 127 */     if (configLocations != null) {
/* 128 */       reader.loadBeanDefinitions(configLocations);
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
/*     */   protected Resource[] getConfigResources() {
/* 142 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/AbstractXmlApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */