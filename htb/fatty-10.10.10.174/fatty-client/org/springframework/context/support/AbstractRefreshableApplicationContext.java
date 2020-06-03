/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
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
/*     */ public abstract class AbstractRefreshableApplicationContext
/*     */   extends AbstractApplicationContext
/*     */ {
/*     */   @Nullable
/*     */   private Boolean allowBeanDefinitionOverriding;
/*     */   @Nullable
/*     */   private Boolean allowCircularReferences;
/*     */   @Nullable
/*     */   private DefaultListableBeanFactory beanFactory;
/*  78 */   private final Object beanFactoryMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractRefreshableApplicationContext() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractRefreshableApplicationContext(@Nullable ApplicationContext parent) {
/*  92 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding) {
/* 103 */     this.allowBeanDefinitionOverriding = Boolean.valueOf(allowBeanDefinitionOverriding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowCircularReferences(boolean allowCircularReferences) {
/* 114 */     this.allowCircularReferences = Boolean.valueOf(allowCircularReferences);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void refreshBeanFactory() throws BeansException {
/* 125 */     if (hasBeanFactory()) {
/* 126 */       destroyBeans();
/* 127 */       closeBeanFactory();
/*     */     } 
/*     */     try {
/* 130 */       DefaultListableBeanFactory beanFactory = createBeanFactory();
/* 131 */       beanFactory.setSerializationId(getId());
/* 132 */       customizeBeanFactory(beanFactory);
/* 133 */       loadBeanDefinitions(beanFactory);
/* 134 */       synchronized (this.beanFactoryMonitor) {
/* 135 */         this.beanFactory = beanFactory;
/*     */       }
/*     */     
/* 138 */     } catch (IOException ex) {
/* 139 */       throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cancelRefresh(BeansException ex) {
/* 145 */     synchronized (this.beanFactoryMonitor) {
/* 146 */       if (this.beanFactory != null) {
/* 147 */         this.beanFactory.setSerializationId(null);
/*     */       }
/*     */     } 
/* 150 */     super.cancelRefresh(ex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void closeBeanFactory() {
/* 155 */     synchronized (this.beanFactoryMonitor) {
/* 156 */       if (this.beanFactory != null) {
/* 157 */         this.beanFactory.setSerializationId(null);
/* 158 */         this.beanFactory = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean hasBeanFactory() {
/* 168 */     synchronized (this.beanFactoryMonitor) {
/* 169 */       return (this.beanFactory != null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final ConfigurableListableBeanFactory getBeanFactory() {
/* 175 */     synchronized (this.beanFactoryMonitor) {
/* 176 */       if (this.beanFactory == null) {
/* 177 */         throw new IllegalStateException("BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext");
/*     */       }
/*     */       
/* 180 */       return (ConfigurableListableBeanFactory)this.beanFactory;
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
/*     */   
/*     */   protected void assertBeanFactoryActive() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultListableBeanFactory createBeanFactory() {
/* 207 */     return new DefaultListableBeanFactory(getInternalParentBeanFactory());
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
/*     */ 
/*     */   
/*     */   protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
/* 225 */     if (this.allowBeanDefinitionOverriding != null) {
/* 226 */       beanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding.booleanValue());
/*     */     }
/* 228 */     if (this.allowCircularReferences != null)
/* 229 */       beanFactory.setAllowCircularReferences(this.allowCircularReferences.booleanValue()); 
/*     */   }
/*     */   
/*     */   protected abstract void loadBeanDefinitions(DefaultListableBeanFactory paramDefaultListableBeanFactory) throws BeansException, IOException;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/AbstractRefreshableApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */