/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyPathFactoryBean
/*     */   implements FactoryBean<Object>, BeanNameAware, BeanFactoryAware
/*     */ {
/*  88 */   private static final Log logger = LogFactory.getLog(PropertyPathFactoryBean.class);
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BeanWrapper targetBeanWrapper;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String targetBeanName;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String propertyPath;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Class<?> resultType;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String beanName;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetObject(Object targetObject) {
/* 117 */     this.targetBeanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(targetObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetBeanName(String targetBeanName) {
/* 128 */     this.targetBeanName = StringUtils.trimAllWhitespace(targetBeanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyPath(String propertyPath) {
/* 137 */     this.propertyPath = StringUtils.trimAllWhitespace(propertyPath);
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
/*     */   public void setResultType(Class<?> resultType) {
/* 149 */     this.resultType = resultType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanName(String beanName) {
/* 160 */     this.beanName = StringUtils.trimAllWhitespace(BeanFactoryUtils.originalBeanName(beanName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 166 */     this.beanFactory = beanFactory;
/*     */     
/* 168 */     if (this.targetBeanWrapper != null && this.targetBeanName != null) {
/* 169 */       throw new IllegalArgumentException("Specify either 'targetObject' or 'targetBeanName', not both");
/*     */     }
/*     */     
/* 172 */     if (this.targetBeanWrapper == null && this.targetBeanName == null) {
/* 173 */       if (this.propertyPath != null) {
/* 174 */         throw new IllegalArgumentException("Specify 'targetObject' or 'targetBeanName' in combination with 'propertyPath'");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 179 */       int dotIndex = (this.beanName != null) ? this.beanName.indexOf('.') : -1;
/* 180 */       if (dotIndex == -1) {
/* 181 */         throw new IllegalArgumentException("Neither 'targetObject' nor 'targetBeanName' specified, and PropertyPathFactoryBean bean name '" + this.beanName + "' does not follow 'beanName.property' syntax");
/*     */       }
/*     */ 
/*     */       
/* 185 */       this.targetBeanName = this.beanName.substring(0, dotIndex);
/* 186 */       this.propertyPath = this.beanName.substring(dotIndex + 1);
/*     */     
/*     */     }
/* 189 */     else if (this.propertyPath == null) {
/*     */       
/* 191 */       throw new IllegalArgumentException("'propertyPath' is required");
/*     */     } 
/*     */     
/* 194 */     if (this.targetBeanWrapper == null && this.beanFactory.isSingleton(this.targetBeanName)) {
/*     */       
/* 196 */       Object bean = this.beanFactory.getBean(this.targetBeanName);
/* 197 */       this.targetBeanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
/* 198 */       this.resultType = this.targetBeanWrapper.getPropertyType(this.propertyPath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getObject() throws BeansException {
/* 206 */     BeanWrapper target = this.targetBeanWrapper;
/* 207 */     if (target != null) {
/* 208 */       if (logger.isWarnEnabled() && this.targetBeanName != null && this.beanFactory instanceof ConfigurableBeanFactory && ((ConfigurableBeanFactory)this.beanFactory)
/*     */         
/* 210 */         .isCurrentlyInCreation(this.targetBeanName)) {
/* 211 */         logger.warn("Target bean '" + this.targetBeanName + "' is still in creation due to a circular reference - obtained value for property '" + this.propertyPath + "' may be outdated!");
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 217 */       Assert.state((this.beanFactory != null), "No BeanFactory available");
/* 218 */       Assert.state((this.targetBeanName != null), "No target bean name specified");
/* 219 */       Object bean = this.beanFactory.getBean(this.targetBeanName);
/* 220 */       target = PropertyAccessorFactory.forBeanPropertyAccess(bean);
/*     */     } 
/* 222 */     Assert.state((this.propertyPath != null), "No property path specified");
/* 223 */     return target.getPropertyValue(this.propertyPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 228 */     return this.resultType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 239 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/PropertyPathFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */