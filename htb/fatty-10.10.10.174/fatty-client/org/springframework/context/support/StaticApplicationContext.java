/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.context.ApplicationContext;
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
/*     */ public class StaticApplicationContext
/*     */   extends GenericApplicationContext
/*     */ {
/*     */   private final StaticMessageSource staticMessageSource;
/*     */   
/*     */   public StaticApplicationContext() throws BeansException {
/*  53 */     this((ApplicationContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaticApplicationContext(@Nullable ApplicationContext parent) throws BeansException {
/*  64 */     super(parent);
/*     */ 
/*     */     
/*  67 */     this.staticMessageSource = new StaticMessageSource();
/*  68 */     getBeanFactory().registerSingleton("messageSource", this.staticMessageSource);
/*     */   }
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
/*     */   public final StaticMessageSource getStaticMessageSource() {
/*  85 */     return this.staticMessageSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerSingleton(String name, Class<?> clazz) throws BeansException {
/*  94 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/*  95 */     bd.setBeanClass(clazz);
/*  96 */     getDefaultListableBeanFactory().registerBeanDefinition(name, (BeanDefinition)bd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerSingleton(String name, Class<?> clazz, MutablePropertyValues pvs) throws BeansException {
/* 105 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/* 106 */     bd.setBeanClass(clazz);
/* 107 */     bd.setPropertyValues(pvs);
/* 108 */     getDefaultListableBeanFactory().registerBeanDefinition(name, (BeanDefinition)bd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerPrototype(String name, Class<?> clazz) throws BeansException {
/* 117 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/* 118 */     bd.setScope("prototype");
/* 119 */     bd.setBeanClass(clazz);
/* 120 */     getDefaultListableBeanFactory().registerBeanDefinition(name, (BeanDefinition)bd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerPrototype(String name, Class<?> clazz, MutablePropertyValues pvs) throws BeansException {
/* 129 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/* 130 */     bd.setScope("prototype");
/* 131 */     bd.setBeanClass(clazz);
/* 132 */     bd.setPropertyValues(pvs);
/* 133 */     getDefaultListableBeanFactory().registerBeanDefinition(name, (BeanDefinition)bd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMessage(String code, Locale locale, String defaultMessage) {
/* 144 */     getStaticMessageSource().addMessage(code, locale, defaultMessage);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/StaticApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */