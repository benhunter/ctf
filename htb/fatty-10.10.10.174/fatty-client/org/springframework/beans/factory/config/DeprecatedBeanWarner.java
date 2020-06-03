/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
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
/*     */ public class DeprecatedBeanWarner
/*     */   implements BeanFactoryPostProcessor
/*     */ {
/*  38 */   protected transient Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoggerName(String loggerName) {
/*  50 */     this.logger = LogFactory.getLog(loggerName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/*  56 */     if (isLogEnabled()) {
/*  57 */       String[] beanNames = beanFactory.getBeanDefinitionNames();
/*  58 */       for (String beanName : beanNames) {
/*  59 */         String nameToLookup = beanName;
/*  60 */         if (beanFactory.isFactoryBean(beanName)) {
/*  61 */           nameToLookup = "&" + beanName;
/*     */         }
/*  63 */         Class<?> beanType = beanFactory.getType(nameToLookup);
/*  64 */         if (beanType != null) {
/*  65 */           Class<?> userClass = ClassUtils.getUserClass(beanType);
/*  66 */           if (userClass.isAnnotationPresent((Class)Deprecated.class)) {
/*  67 */             BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
/*  68 */             logDeprecatedBean(beanName, beanType, beanDefinition);
/*     */           } 
/*     */         } 
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
/*     */   protected void logDeprecatedBean(String beanName, Class<?> beanType, BeanDefinition beanDefinition) {
/*  82 */     StringBuilder builder = new StringBuilder();
/*  83 */     builder.append(beanType);
/*  84 */     builder.append(" ['");
/*  85 */     builder.append(beanName);
/*  86 */     builder.append('\'');
/*  87 */     String resourceDescription = beanDefinition.getResourceDescription();
/*  88 */     if (StringUtils.hasLength(resourceDescription)) {
/*  89 */       builder.append(" in ");
/*  90 */       builder.append(resourceDescription);
/*     */     } 
/*  92 */     builder.append("] has been deprecated");
/*  93 */     writeToLog(builder.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeToLog(String message) {
/* 102 */     this.logger.warn(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isLogEnabled() {
/* 111 */     return this.logger.isWarnEnabled();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/DeprecatedBeanWarner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */