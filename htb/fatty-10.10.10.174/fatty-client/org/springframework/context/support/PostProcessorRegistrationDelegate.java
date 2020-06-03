/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.core.OrderComparator;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.PriorityOrdered;
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
/*     */ final class PostProcessorRegistrationDelegate
/*     */ {
/*     */   public static void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {
/*  59 */     Set<String> processedBeans = new HashSet<>();
/*     */     
/*  61 */     if (beanFactory instanceof BeanDefinitionRegistry) {
/*  62 */       BeanDefinitionRegistry registry = (BeanDefinitionRegistry)beanFactory;
/*  63 */       List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
/*  64 */       List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();
/*     */       
/*  66 */       for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
/*  67 */         if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
/*  68 */           BeanDefinitionRegistryPostProcessor registryProcessor = (BeanDefinitionRegistryPostProcessor)postProcessor;
/*     */           
/*  70 */           registryProcessor.postProcessBeanDefinitionRegistry(registry);
/*  71 */           registryProcessors.add(registryProcessor);
/*     */           continue;
/*     */         } 
/*  74 */         regularPostProcessors.add(postProcessor);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();
/*     */ 
/*     */ 
/*     */       
/*  86 */       String[] arrayOfString = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
/*  87 */       for (String ppName : arrayOfString) {
/*  88 */         if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
/*  89 */           currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
/*  90 */           processedBeans.add(ppName);
/*     */         } 
/*     */       } 
/*  93 */       sortPostProcessors(currentRegistryProcessors, beanFactory);
/*  94 */       registryProcessors.addAll(currentRegistryProcessors);
/*  95 */       invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
/*  96 */       currentRegistryProcessors.clear();
/*     */ 
/*     */       
/*  99 */       arrayOfString = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
/* 100 */       for (String ppName : arrayOfString) {
/* 101 */         if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
/* 102 */           currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
/* 103 */           processedBeans.add(ppName);
/*     */         } 
/*     */       } 
/* 106 */       sortPostProcessors(currentRegistryProcessors, beanFactory);
/* 107 */       registryProcessors.addAll(currentRegistryProcessors);
/* 108 */       invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
/* 109 */       currentRegistryProcessors.clear();
/*     */ 
/*     */       
/* 112 */       boolean reiterate = true;
/* 113 */       while (reiterate) {
/* 114 */         reiterate = false;
/* 115 */         arrayOfString = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
/* 116 */         for (String ppName : arrayOfString) {
/* 117 */           if (!processedBeans.contains(ppName)) {
/* 118 */             currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
/* 119 */             processedBeans.add(ppName);
/* 120 */             reiterate = true;
/*     */           } 
/*     */         } 
/* 123 */         sortPostProcessors(currentRegistryProcessors, beanFactory);
/* 124 */         registryProcessors.addAll(currentRegistryProcessors);
/* 125 */         invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
/* 126 */         currentRegistryProcessors.clear();
/*     */       } 
/*     */ 
/*     */       
/* 130 */       invokeBeanFactoryPostProcessors((Collection)registryProcessors, beanFactory);
/* 131 */       invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 136 */       invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
/*     */ 
/*     */ 
/*     */     
/* 146 */     List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
/* 147 */     List<String> orderedPostProcessorNames = new ArrayList<>();
/* 148 */     List<String> nonOrderedPostProcessorNames = new ArrayList<>();
/* 149 */     for (String ppName : postProcessorNames) {
/* 150 */       if (!processedBeans.contains(ppName))
/*     */       {
/*     */         
/* 153 */         if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
/* 154 */           priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
/*     */         }
/* 156 */         else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
/* 157 */           orderedPostProcessorNames.add(ppName);
/*     */         } else {
/*     */           
/* 160 */           nonOrderedPostProcessorNames.add(ppName);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 165 */     sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
/* 166 */     invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);
/*     */ 
/*     */     
/* 169 */     List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>();
/* 170 */     for (String postProcessorName : orderedPostProcessorNames) {
/* 171 */       orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
/*     */     }
/* 173 */     sortPostProcessors(orderedPostProcessors, beanFactory);
/* 174 */     invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);
/*     */ 
/*     */     
/* 177 */     List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
/* 178 */     for (String postProcessorName : nonOrderedPostProcessorNames) {
/* 179 */       nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
/*     */     }
/* 181 */     invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);
/*     */ 
/*     */ 
/*     */     
/* 185 */     beanFactory.clearMetadataCache();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {
/* 191 */     String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
/* 197 */     beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));
/*     */ 
/*     */ 
/*     */     
/* 201 */     List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
/* 202 */     List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
/* 203 */     List<String> orderedPostProcessorNames = new ArrayList<>();
/* 204 */     List<String> nonOrderedPostProcessorNames = new ArrayList<>();
/* 205 */     for (String ppName : postProcessorNames) {
/* 206 */       if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
/* 207 */         BeanPostProcessor pp = (BeanPostProcessor)beanFactory.getBean(ppName, BeanPostProcessor.class);
/* 208 */         priorityOrderedPostProcessors.add(pp);
/* 209 */         if (pp instanceof org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor) {
/* 210 */           internalPostProcessors.add(pp);
/*     */         }
/*     */       }
/* 213 */       else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
/* 214 */         orderedPostProcessorNames.add(ppName);
/*     */       } else {
/*     */         
/* 217 */         nonOrderedPostProcessorNames.add(ppName);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 222 */     sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
/* 223 */     registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);
/*     */ 
/*     */     
/* 226 */     List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>();
/* 227 */     for (String ppName : orderedPostProcessorNames) {
/* 228 */       BeanPostProcessor pp = (BeanPostProcessor)beanFactory.getBean(ppName, BeanPostProcessor.class);
/* 229 */       orderedPostProcessors.add(pp);
/* 230 */       if (pp instanceof org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor) {
/* 231 */         internalPostProcessors.add(pp);
/*     */       }
/*     */     } 
/* 234 */     sortPostProcessors(orderedPostProcessors, beanFactory);
/* 235 */     registerBeanPostProcessors(beanFactory, orderedPostProcessors);
/*     */ 
/*     */     
/* 238 */     List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
/* 239 */     for (String ppName : nonOrderedPostProcessorNames) {
/* 240 */       BeanPostProcessor pp = (BeanPostProcessor)beanFactory.getBean(ppName, BeanPostProcessor.class);
/* 241 */       nonOrderedPostProcessors.add(pp);
/* 242 */       if (pp instanceof org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor) {
/* 243 */         internalPostProcessors.add(pp);
/*     */       }
/*     */     } 
/* 246 */     registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);
/*     */ 
/*     */     
/* 249 */     sortPostProcessors(internalPostProcessors, beanFactory);
/* 250 */     registerBeanPostProcessors(beanFactory, internalPostProcessors);
/*     */ 
/*     */ 
/*     */     
/* 254 */     beanFactory.addBeanPostProcessor((BeanPostProcessor)new ApplicationListenerDetector(applicationContext));
/*     */   }
/*     */   private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
/*     */     OrderComparator orderComparator;
/* 258 */     Comparator<Object> comparatorToUse = null;
/* 259 */     if (beanFactory instanceof DefaultListableBeanFactory) {
/* 260 */       comparatorToUse = ((DefaultListableBeanFactory)beanFactory).getDependencyComparator();
/*     */     }
/* 262 */     if (comparatorToUse == null) {
/* 263 */       orderComparator = OrderComparator.INSTANCE;
/*     */     }
/* 265 */     postProcessors.sort((Comparator<?>)orderComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void invokeBeanDefinitionRegistryPostProcessors(Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {
/* 274 */     for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
/* 275 */       postProcessor.postProcessBeanDefinitionRegistry(registry);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void invokeBeanFactoryPostProcessors(Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {
/* 285 */     for (BeanFactoryPostProcessor postProcessor : postProcessors) {
/* 286 */       postProcessor.postProcessBeanFactory(beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {
/* 296 */     for (BeanPostProcessor postProcessor : postProcessors) {
/* 297 */       beanFactory.addBeanPostProcessor(postProcessor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class BeanPostProcessorChecker
/*     */     implements BeanPostProcessor
/*     */   {
/* 309 */     private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);
/*     */     
/*     */     private final ConfigurableListableBeanFactory beanFactory;
/*     */     
/*     */     private final int beanPostProcessorTargetCount;
/*     */     
/*     */     public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
/* 316 */       this.beanFactory = beanFactory;
/* 317 */       this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object postProcessBeforeInitialization(Object bean, String beanName) {
/* 322 */       return bean;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object postProcessAfterInitialization(Object bean, String beanName) {
/* 327 */       if (!(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) && this.beanFactory
/* 328 */         .getBeanPostProcessorCount() < this.beanPostProcessorTargetCount && 
/* 329 */         logger.isInfoEnabled()) {
/* 330 */         logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() + "] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 335 */       return bean;
/*     */     }
/*     */     
/*     */     private boolean isInfrastructureBean(@Nullable String beanName) {
/* 339 */       if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
/* 340 */         BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
/* 341 */         return (bd.getRole() == 2);
/*     */       } 
/* 343 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/PostProcessorRegistrationDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */