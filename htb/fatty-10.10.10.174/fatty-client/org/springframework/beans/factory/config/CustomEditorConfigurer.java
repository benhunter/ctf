/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyEditorRegistrar;
/*     */ import org.springframework.core.Ordered;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomEditorConfigurer
/*     */   implements BeanFactoryPostProcessor, Ordered
/*     */ {
/*  98 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 100 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */   @Nullable
/*     */   private PropertyEditorRegistrar[] propertyEditorRegistrars;
/*     */   
/*     */   @Nullable
/*     */   private Map<Class<?>, Class<? extends PropertyEditor>> customEditors;
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/* 110 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 115 */     return this.order;
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
/*     */   public void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars) {
/* 129 */     this.propertyEditorRegistrars = propertyEditorRegistrars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCustomEditors(Map<Class<?>, Class<? extends PropertyEditor>> customEditors) {
/* 139 */     this.customEditors = customEditors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/* 145 */     if (this.propertyEditorRegistrars != null) {
/* 146 */       for (PropertyEditorRegistrar propertyEditorRegistrar : this.propertyEditorRegistrars) {
/* 147 */         beanFactory.addPropertyEditorRegistrar(propertyEditorRegistrar);
/*     */       }
/*     */     }
/* 150 */     if (this.customEditors != null)
/* 151 */       this.customEditors.forEach(beanFactory::registerCustomEditor); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/CustomEditorConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */