/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.Lifecycle;
/*     */ import org.springframework.context.LifecycleProcessor;
/*     */ import org.springframework.context.Phased;
/*     */ import org.springframework.context.SmartLifecycle;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultLifecycleProcessor
/*     */   implements LifecycleProcessor, BeanFactoryAware
/*     */ {
/*  55 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  57 */   private volatile long timeoutPerShutdownPhase = 30000L;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean running;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile ConfigurableListableBeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeoutPerShutdownPhase(long timeoutPerShutdownPhase) {
/*  71 */     this.timeoutPerShutdownPhase = timeoutPerShutdownPhase;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  76 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  77 */       throw new IllegalArgumentException("DefaultLifecycleProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
/*     */     }
/*     */     
/*  80 */     this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
/*     */   }
/*     */   
/*     */   private ConfigurableListableBeanFactory getBeanFactory() {
/*  84 */     ConfigurableListableBeanFactory beanFactory = this.beanFactory;
/*  85 */     Assert.state((beanFactory != null), "No BeanFactory available");
/*  86 */     return beanFactory;
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
/*     */   public void start() {
/* 102 */     startBeans(false);
/* 103 */     this.running = true;
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
/*     */   public void stop() {
/* 116 */     stopBeans();
/* 117 */     this.running = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRefresh() {
/* 122 */     startBeans(true);
/* 123 */     this.running = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 128 */     stopBeans();
/* 129 */     this.running = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 134 */     return this.running;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startBeans(boolean autoStartupOnly) {
/* 141 */     Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
/* 142 */     Map<Integer, LifecycleGroup> phases = new HashMap<>();
/* 143 */     lifecycleBeans.forEach((beanName, bean) -> {
/*     */           if (!autoStartupOnly || (bean instanceof SmartLifecycle && ((SmartLifecycle)bean).isAutoStartup())) {
/*     */             int phase = getPhase(bean);
/*     */             LifecycleGroup group = (LifecycleGroup)phases.get(Integer.valueOf(phase));
/*     */             if (group == null) {
/*     */               group = new LifecycleGroup(phase, this.timeoutPerShutdownPhase, lifecycleBeans, autoStartupOnly);
/*     */               phases.put(Integer.valueOf(phase), group);
/*     */             } 
/*     */             group.add(beanName, bean);
/*     */           } 
/*     */         });
/* 154 */     if (!phases.isEmpty()) {
/* 155 */       List<Integer> keys = new ArrayList<>(phases.keySet());
/* 156 */       Collections.sort(keys);
/* 157 */       for (Integer key : keys) {
/* 158 */         ((LifecycleGroup)phases.get(key)).start();
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
/*     */   private void doStart(Map<String, ? extends Lifecycle> lifecycleBeans, String beanName, boolean autoStartupOnly) {
/* 170 */     Lifecycle bean = lifecycleBeans.remove(beanName);
/* 171 */     if (bean != null && bean != this) {
/* 172 */       String[] dependenciesForBean = getBeanFactory().getDependenciesForBean(beanName);
/* 173 */       for (String dependency : dependenciesForBean) {
/* 174 */         doStart(lifecycleBeans, dependency, autoStartupOnly);
/*     */       }
/* 176 */       if (!bean.isRunning() && (!autoStartupOnly || !(bean instanceof SmartLifecycle) || ((SmartLifecycle)bean)
/* 177 */         .isAutoStartup())) {
/* 178 */         if (this.logger.isTraceEnabled()) {
/* 179 */           this.logger.trace("Starting bean '" + beanName + "' of type [" + bean.getClass().getName() + "]");
/*     */         }
/*     */         try {
/* 182 */           bean.start();
/*     */         }
/* 184 */         catch (Throwable ex) {
/* 185 */           throw new ApplicationContextException("Failed to start bean '" + beanName + "'", ex);
/*     */         } 
/* 187 */         if (this.logger.isDebugEnabled()) {
/* 188 */           this.logger.debug("Successfully started bean '" + beanName + "'");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stopBeans() {
/* 195 */     Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
/* 196 */     Map<Integer, LifecycleGroup> phases = new HashMap<>();
/* 197 */     lifecycleBeans.forEach((beanName, bean) -> {
/*     */           int shutdownPhase = getPhase(bean);
/*     */           LifecycleGroup group = (LifecycleGroup)phases.get(Integer.valueOf(shutdownPhase));
/*     */           if (group == null) {
/*     */             group = new LifecycleGroup(shutdownPhase, this.timeoutPerShutdownPhase, lifecycleBeans, false);
/*     */             phases.put(Integer.valueOf(shutdownPhase), group);
/*     */           } 
/*     */           group.add(beanName, bean);
/*     */         });
/* 206 */     if (!phases.isEmpty()) {
/* 207 */       List<Integer> keys = new ArrayList<>(phases.keySet());
/* 208 */       keys.sort(Collections.reverseOrder());
/* 209 */       for (Integer key : keys) {
/* 210 */         ((LifecycleGroup)phases.get(key)).stop();
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
/*     */   
/*     */   private void doStop(Map<String, ? extends Lifecycle> lifecycleBeans, String beanName, CountDownLatch latch, Set<String> countDownBeanNames) {
/* 224 */     Lifecycle bean = lifecycleBeans.remove(beanName);
/* 225 */     if (bean != null) {
/* 226 */       String[] dependentBeans = getBeanFactory().getDependentBeans(beanName);
/* 227 */       for (String dependentBean : dependentBeans) {
/* 228 */         doStop(lifecycleBeans, dependentBean, latch, countDownBeanNames);
/*     */       }
/*     */       try {
/* 231 */         if (bean.isRunning()) {
/* 232 */           if (bean instanceof SmartLifecycle) {
/* 233 */             if (this.logger.isTraceEnabled()) {
/* 234 */               this.logger.trace("Asking bean '" + beanName + "' of type [" + bean
/* 235 */                   .getClass().getName() + "] to stop");
/*     */             }
/* 237 */             countDownBeanNames.add(beanName);
/* 238 */             ((SmartLifecycle)bean).stop(() -> {
/*     */                   latch.countDown();
/*     */                   
/*     */                   countDownBeanNames.remove(beanName);
/*     */                   if (this.logger.isDebugEnabled()) {
/*     */                     this.logger.debug("Bean '" + beanName + "' completed its stop procedure");
/*     */                   }
/*     */                 });
/*     */           } else {
/* 247 */             if (this.logger.isTraceEnabled()) {
/* 248 */               this.logger.trace("Stopping bean '" + beanName + "' of type [" + bean
/* 249 */                   .getClass().getName() + "]");
/*     */             }
/* 251 */             bean.stop();
/* 252 */             if (this.logger.isDebugEnabled()) {
/* 253 */               this.logger.debug("Successfully stopped bean '" + beanName + "'");
/*     */             }
/*     */           }
/*     */         
/* 257 */         } else if (bean instanceof SmartLifecycle) {
/*     */           
/* 259 */           latch.countDown();
/*     */         }
/*     */       
/* 262 */       } catch (Throwable ex) {
/* 263 */         if (this.logger.isWarnEnabled()) {
/* 264 */           this.logger.warn("Failed to stop bean '" + beanName + "'", ex);
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
/*     */   
/*     */   protected Map<String, Lifecycle> getLifecycleBeans() {
/* 279 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/* 280 */     Map<String, Lifecycle> beans = new LinkedHashMap<>();
/* 281 */     String[] beanNames = beanFactory.getBeanNamesForType(Lifecycle.class, false, false);
/* 282 */     for (String beanName : beanNames) {
/* 283 */       String beanNameToRegister = BeanFactoryUtils.transformedBeanName(beanName);
/* 284 */       boolean isFactoryBean = beanFactory.isFactoryBean(beanNameToRegister);
/* 285 */       String beanNameToCheck = isFactoryBean ? ("&" + beanName) : beanName;
/* 286 */       if ((beanFactory.containsSingleton(beanNameToRegister) && (!isFactoryBean || 
/* 287 */         matchesBeanType(Lifecycle.class, beanNameToCheck, (BeanFactory)beanFactory))) || 
/* 288 */         matchesBeanType(SmartLifecycle.class, beanNameToCheck, (BeanFactory)beanFactory)) {
/* 289 */         Object bean = beanFactory.getBean(beanNameToCheck);
/* 290 */         if (bean != this && bean instanceof Lifecycle) {
/* 291 */           beans.put(beanNameToRegister, (Lifecycle)bean);
/*     */         }
/*     */       } 
/*     */     } 
/* 295 */     return beans;
/*     */   }
/*     */   
/*     */   private boolean matchesBeanType(Class<?> targetType, String beanName, BeanFactory beanFactory) {
/* 299 */     Class<?> beanType = beanFactory.getType(beanName);
/* 300 */     return (beanType != null && targetType.isAssignableFrom(beanType));
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
/*     */   protected int getPhase(Lifecycle bean) {
/* 313 */     return (bean instanceof Phased) ? ((Phased)bean).getPhase() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class LifecycleGroup
/*     */   {
/*     */     private final int phase;
/*     */ 
/*     */     
/*     */     private final long timeout;
/*     */ 
/*     */     
/*     */     private final Map<String, ? extends Lifecycle> lifecycleBeans;
/*     */ 
/*     */     
/*     */     private final boolean autoStartupOnly;
/*     */     
/* 331 */     private final List<DefaultLifecycleProcessor.LifecycleGroupMember> members = new ArrayList<>();
/*     */ 
/*     */     
/*     */     private int smartMemberCount;
/*     */ 
/*     */     
/*     */     public LifecycleGroup(int phase, long timeout, Map<String, ? extends Lifecycle> lifecycleBeans, boolean autoStartupOnly) {
/* 338 */       this.phase = phase;
/* 339 */       this.timeout = timeout;
/* 340 */       this.lifecycleBeans = lifecycleBeans;
/* 341 */       this.autoStartupOnly = autoStartupOnly;
/*     */     }
/*     */     
/*     */     public void add(String name, Lifecycle bean) {
/* 345 */       this.members.add(new DefaultLifecycleProcessor.LifecycleGroupMember(name, bean));
/* 346 */       if (bean instanceof SmartLifecycle) {
/* 347 */         this.smartMemberCount++;
/*     */       }
/*     */     }
/*     */     
/*     */     public void start() {
/* 352 */       if (this.members.isEmpty()) {
/*     */         return;
/*     */       }
/* 355 */       if (DefaultLifecycleProcessor.this.logger.isDebugEnabled()) {
/* 356 */         DefaultLifecycleProcessor.this.logger.debug("Starting beans in phase " + this.phase);
/*     */       }
/* 358 */       Collections.sort(this.members);
/* 359 */       for (DefaultLifecycleProcessor.LifecycleGroupMember member : this.members) {
/* 360 */         DefaultLifecycleProcessor.this.doStart(this.lifecycleBeans, member.name, this.autoStartupOnly);
/*     */       }
/*     */     }
/*     */     
/*     */     public void stop() {
/* 365 */       if (this.members.isEmpty()) {
/*     */         return;
/*     */       }
/* 368 */       if (DefaultLifecycleProcessor.this.logger.isDebugEnabled()) {
/* 369 */         DefaultLifecycleProcessor.this.logger.debug("Stopping beans in phase " + this.phase);
/*     */       }
/* 371 */       this.members.sort(Collections.reverseOrder());
/* 372 */       CountDownLatch latch = new CountDownLatch(this.smartMemberCount);
/* 373 */       Set<String> countDownBeanNames = Collections.synchronizedSet(new LinkedHashSet<>());
/* 374 */       Set<String> lifecycleBeanNames = new HashSet<>(this.lifecycleBeans.keySet());
/* 375 */       for (DefaultLifecycleProcessor.LifecycleGroupMember member : this.members) {
/* 376 */         if (lifecycleBeanNames.contains(member.name)) {
/* 377 */           DefaultLifecycleProcessor.this.doStop(this.lifecycleBeans, member.name, latch, countDownBeanNames); continue;
/*     */         } 
/* 379 */         if (member.bean instanceof SmartLifecycle)
/*     */         {
/* 381 */           latch.countDown();
/*     */         }
/*     */       } 
/*     */       try {
/* 385 */         latch.await(this.timeout, TimeUnit.MILLISECONDS);
/* 386 */         if (latch.getCount() > 0L && !countDownBeanNames.isEmpty() && DefaultLifecycleProcessor.this.logger.isInfoEnabled()) {
/* 387 */           DefaultLifecycleProcessor.this.logger.info("Failed to shut down " + countDownBeanNames.size() + " bean" + (
/* 388 */               (countDownBeanNames.size() > 1) ? "s" : "") + " with phase value " + this.phase + " within timeout of " + this.timeout + ": " + countDownBeanNames);
/*     */         
/*     */         }
/*     */       }
/* 392 */       catch (InterruptedException ex) {
/* 393 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class LifecycleGroupMember
/*     */     implements Comparable<LifecycleGroupMember>
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final Lifecycle bean;
/*     */ 
/*     */     
/*     */     LifecycleGroupMember(String name, Lifecycle bean) {
/* 409 */       this.name = name;
/* 410 */       this.bean = bean;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(LifecycleGroupMember other) {
/* 415 */       int thisPhase = DefaultLifecycleProcessor.this.getPhase(this.bean);
/* 416 */       int otherPhase = DefaultLifecycleProcessor.this.getPhase(other.bean);
/* 417 */       return Integer.compare(thisPhase, otherPhase);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/DefaultLifecycleProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */