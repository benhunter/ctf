/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
/*     */ import org.springframework.aop.scope.ScopedObject;
/*     */ import org.springframework.aop.scope.ScopedProxyUtils;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventListenerMethodProcessor
/*     */   implements SmartInitializingSingleton, ApplicationContextAware, BeanFactoryPostProcessor
/*     */ {
/*  65 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private ConfigurableApplicationContext applicationContext;
/*     */   
/*     */   @Nullable
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*     */   
/*     */   @Nullable
/*     */   private List<EventListenerFactory> eventListenerFactories;
/*     */   
/*  76 */   private final EventExpressionEvaluator evaluator = new EventExpressionEvaluator();
/*     */   
/*  78 */   private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/*  83 */     Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext, "ApplicationContext does not implement ConfigurableApplicationContext");
/*     */     
/*  85 */     this.applicationContext = (ConfigurableApplicationContext)applicationContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/*  90 */     this.beanFactory = beanFactory;
/*     */     
/*  92 */     Map<String, EventListenerFactory> beans = beanFactory.getBeansOfType(EventListenerFactory.class, false, false);
/*  93 */     List<EventListenerFactory> factories = new ArrayList<>(beans.values());
/*  94 */     AnnotationAwareOrderComparator.sort(factories);
/*  95 */     this.eventListenerFactories = factories;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterSingletonsInstantiated() {
/* 101 */     ConfigurableListableBeanFactory beanFactory = this.beanFactory;
/* 102 */     Assert.state((this.beanFactory != null), "No ConfigurableListableBeanFactory set");
/* 103 */     String[] beanNames = beanFactory.getBeanNamesForType(Object.class);
/* 104 */     for (String beanName : beanNames) {
/* 105 */       if (!ScopedProxyUtils.isScopedTarget(beanName)) {
/* 106 */         Class<?> type = null;
/*     */         try {
/* 108 */           type = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
/*     */         }
/* 110 */         catch (Throwable ex) {
/*     */           
/* 112 */           if (this.logger.isDebugEnabled()) {
/* 113 */             this.logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
/*     */           }
/*     */         } 
/* 116 */         if (type != null) {
/* 117 */           if (ScopedObject.class.isAssignableFrom(type)) {
/*     */             try {
/* 119 */               Class<?> targetClass = AutoProxyUtils.determineTargetClass(beanFactory, 
/* 120 */                   ScopedProxyUtils.getTargetBeanName(beanName));
/* 121 */               if (targetClass != null) {
/* 122 */                 type = targetClass;
/*     */               }
/*     */             }
/* 125 */             catch (Throwable ex) {
/*     */               
/* 127 */               if (this.logger.isDebugEnabled()) {
/* 128 */                 this.logger.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex);
/*     */               }
/*     */             } 
/*     */           }
/*     */           try {
/* 133 */             processBean(beanName, type);
/*     */           }
/* 135 */           catch (Throwable ex) {
/* 136 */             throw new BeanInitializationException("Failed to process @EventListener annotation on bean with name '" + beanName + "'", ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void processBean(String beanName, Class<?> targetType) {
/* 145 */     if (!this.nonAnnotatedClasses.contains(targetType) && 
/* 146 */       !targetType.getName().startsWith("java") && 
/* 147 */       !isSpringContainerClass(targetType)) {
/*     */       
/* 149 */       Map<Method, EventListener> annotatedMethods = null;
/*     */       try {
/* 151 */         annotatedMethods = MethodIntrospector.selectMethods(targetType, method -> (EventListener)AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class));
/*     */ 
/*     */       
/*     */       }
/* 155 */       catch (Throwable ex) {
/*     */         
/* 157 */         if (this.logger.isDebugEnabled()) {
/* 158 */           this.logger.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
/*     */         }
/*     */       } 
/*     */       
/* 162 */       if (CollectionUtils.isEmpty(annotatedMethods)) {
/* 163 */         this.nonAnnotatedClasses.add(targetType);
/* 164 */         if (this.logger.isTraceEnabled()) {
/* 165 */           this.logger.trace("No @EventListener annotations found on bean class: " + targetType.getName());
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 170 */         ConfigurableApplicationContext context = this.applicationContext;
/* 171 */         Assert.state((context != null), "No ApplicationContext set");
/* 172 */         List<EventListenerFactory> factories = this.eventListenerFactories;
/* 173 */         Assert.state((factories != null), "EventListenerFactory List not initialized");
/* 174 */         for (Method method : annotatedMethods.keySet()) {
/* 175 */           label40: for (EventListenerFactory factory : factories) {
/* 176 */             if (factory.supportsMethod(method)) {
/* 177 */               Method methodToUse = AopUtils.selectInvocableMethod(method, context.getType(beanName));
/*     */               
/* 179 */               ApplicationListener<?> applicationListener = factory.createApplicationListener(beanName, targetType, methodToUse);
/* 180 */               if (applicationListener instanceof ApplicationListenerMethodAdapter) {
/* 181 */                 ((ApplicationListenerMethodAdapter)applicationListener).init((ApplicationContext)context, this.evaluator); break label40;
/*     */               } 
/* 183 */               context.addApplicationListener(applicationListener);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 188 */         if (this.logger.isDebugEnabled()) {
/* 189 */           this.logger.debug(annotatedMethods.size() + " @EventListener methods processed on bean '" + beanName + "': " + annotatedMethods);
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
/*     */   
/*     */   private static boolean isSpringContainerClass(Class<?> clazz) {
/* 203 */     return (clazz.getName().startsWith("org.springframework.") && 
/* 204 */       !AnnotatedElementUtils.isAnnotated(ClassUtils.getUserClass(clazz), Component.class));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/EventListenerMethodProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */