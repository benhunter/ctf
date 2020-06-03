/*     */ package org.springframework.scheduling.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.NamedBeanHolder;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.context.event.ContextRefreshedEvent;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scheduling.TaskScheduler;
/*     */ import org.springframework.scheduling.config.CronTask;
/*     */ import org.springframework.scheduling.config.FixedDelayTask;
/*     */ import org.springframework.scheduling.config.FixedRateTask;
/*     */ import org.springframework.scheduling.config.ScheduledTask;
/*     */ import org.springframework.scheduling.config.ScheduledTaskHolder;
/*     */ import org.springframework.scheduling.config.ScheduledTaskRegistrar;
/*     */ import org.springframework.scheduling.support.CronTrigger;
/*     */ import org.springframework.scheduling.support.ScheduledMethodRunnable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScheduledAnnotationBeanPostProcessor
/*     */   implements ScheduledTaskHolder, MergedBeanDefinitionPostProcessor, DestructionAwareBeanPostProcessor, Ordered, EmbeddedValueResolverAware, BeanNameAware, BeanFactoryAware, ApplicationContextAware, SmartInitializingSingleton, ApplicationListener<ContextRefreshedEvent>, DisposableBean
/*     */ {
/*     */   public static final String DEFAULT_TASK_SCHEDULER_BEAN_NAME = "taskScheduler";
/* 117 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final ScheduledTaskRegistrar registrar;
/*     */   
/*     */   @Nullable
/*     */   private Object scheduler;
/*     */   
/*     */   @Nullable
/*     */   private StringValueResolver embeddedValueResolver;
/*     */   
/*     */   @Nullable
/*     */   private String beanName;
/*     */   
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */   
/* 136 */   private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));
/*     */   
/* 138 */   private final Map<Object, Set<ScheduledTask>> scheduledTasks = new IdentityHashMap<>(16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledAnnotationBeanPostProcessor() {
/* 145 */     this.registrar = new ScheduledTaskRegistrar();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledAnnotationBeanPostProcessor(ScheduledTaskRegistrar registrar) {
/* 155 */     Assert.notNull(registrar, "ScheduledTaskRegistrar is required");
/* 156 */     this.registrar = registrar;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 162 */     return Integer.MAX_VALUE;
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
/*     */   public void setScheduler(Object scheduler) {
/* 177 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEmbeddedValueResolver(StringValueResolver resolver) {
/* 182 */     this.embeddedValueResolver = resolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String beanName) {
/* 187 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 197 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 207 */     this.applicationContext = applicationContext;
/* 208 */     if (this.beanFactory == null) {
/* 209 */       this.beanFactory = (BeanFactory)applicationContext;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterSingletonsInstantiated() {
/* 217 */     this.nonAnnotatedClasses.clear();
/*     */     
/* 219 */     if (this.applicationContext == null)
/*     */     {
/* 221 */       finishRegistration();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ContextRefreshedEvent event) {
/* 227 */     if (event.getApplicationContext() == this.applicationContext)
/*     */     {
/*     */ 
/*     */       
/* 231 */       finishRegistration();
/*     */     }
/*     */   }
/*     */   
/*     */   private void finishRegistration() {
/* 236 */     if (this.scheduler != null) {
/* 237 */       this.registrar.setScheduler(this.scheduler);
/*     */     }
/*     */     
/* 240 */     if (this.beanFactory instanceof ListableBeanFactory) {
/*     */       
/* 242 */       Map<String, SchedulingConfigurer> beans = ((ListableBeanFactory)this.beanFactory).getBeansOfType(SchedulingConfigurer.class);
/* 243 */       List<SchedulingConfigurer> configurers = new ArrayList<>(beans.values());
/* 244 */       AnnotationAwareOrderComparator.sort(configurers);
/* 245 */       for (SchedulingConfigurer configurer : configurers) {
/* 246 */         configurer.configureTasks(this.registrar);
/*     */       }
/*     */     } 
/*     */     
/* 250 */     if (this.registrar.hasTasks() && this.registrar.getScheduler() == null) {
/* 251 */       Assert.state((this.beanFactory != null), "BeanFactory must be set to find scheduler by type");
/*     */       
/*     */       try {
/* 254 */         this.registrar.setTaskScheduler(resolveSchedulerBean(this.beanFactory, TaskScheduler.class, false));
/*     */       }
/* 256 */       catch (NoUniqueBeanDefinitionException ex) {
/* 257 */         this.logger.trace("Could not find unique TaskScheduler bean", (Throwable)ex);
/*     */         try {
/* 259 */           this.registrar.setTaskScheduler(resolveSchedulerBean(this.beanFactory, TaskScheduler.class, true));
/*     */         }
/* 261 */         catch (NoSuchBeanDefinitionException ex2) {
/* 262 */           if (this.logger.isInfoEnabled()) {
/* 263 */             this.logger.info("More than one TaskScheduler bean exists within the context, and none is named 'taskScheduler'. Mark one of them as primary or name it 'taskScheduler' (possibly as an alias); or implement the SchedulingConfigurer interface and call ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback: " + ex
/*     */ 
/*     */ 
/*     */                 
/* 267 */                 .getBeanNamesFound());
/*     */           }
/*     */         }
/*     */       
/* 271 */       } catch (NoSuchBeanDefinitionException ex) {
/* 272 */         this.logger.trace("Could not find default TaskScheduler bean", (Throwable)ex);
/*     */         
/*     */         try {
/* 275 */           this.registrar.setScheduler(resolveSchedulerBean(this.beanFactory, ScheduledExecutorService.class, false));
/*     */         }
/* 277 */         catch (NoUniqueBeanDefinitionException ex2) {
/* 278 */           this.logger.trace("Could not find unique ScheduledExecutorService bean", (Throwable)ex2);
/*     */           try {
/* 280 */             this.registrar.setScheduler(resolveSchedulerBean(this.beanFactory, ScheduledExecutorService.class, true));
/*     */           }
/* 282 */           catch (NoSuchBeanDefinitionException ex3) {
/* 283 */             if (this.logger.isInfoEnabled()) {
/* 284 */               this.logger.info("More than one ScheduledExecutorService bean exists within the context, and none is named 'taskScheduler'. Mark one of them as primary or name it 'taskScheduler' (possibly as an alias); or implement the SchedulingConfigurer interface and call ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback: " + ex2
/*     */ 
/*     */ 
/*     */                   
/* 288 */                   .getBeanNamesFound());
/*     */             }
/*     */           }
/*     */         
/* 292 */         } catch (NoSuchBeanDefinitionException ex2) {
/* 293 */           this.logger.trace("Could not find default ScheduledExecutorService bean", (Throwable)ex2);
/*     */           
/* 295 */           this.logger.info("No TaskScheduler/ScheduledExecutorService bean found for scheduled processing");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 300 */     this.registrar.afterPropertiesSet();
/*     */   }
/*     */   
/*     */   private <T> T resolveSchedulerBean(BeanFactory beanFactory, Class<T> schedulerType, boolean byName) {
/* 304 */     if (byName) {
/* 305 */       T scheduler = (T)beanFactory.getBean("taskScheduler", schedulerType);
/* 306 */       if (this.beanName != null && this.beanFactory instanceof ConfigurableBeanFactory) {
/* 307 */         ((ConfigurableBeanFactory)this.beanFactory).registerDependentBean("taskScheduler", this.beanName);
/*     */       }
/*     */       
/* 310 */       return scheduler;
/*     */     } 
/* 312 */     if (beanFactory instanceof AutowireCapableBeanFactory) {
/* 313 */       NamedBeanHolder<T> holder = ((AutowireCapableBeanFactory)beanFactory).resolveNamedBean(schedulerType);
/* 314 */       if (this.beanName != null && beanFactory instanceof ConfigurableBeanFactory) {
/* 315 */         ((ConfigurableBeanFactory)beanFactory).registerDependentBean(holder.getBeanName(), this.beanName);
/*     */       }
/* 317 */       return (T)holder.getBeanInstance();
/*     */     } 
/*     */     
/* 320 */     return (T)beanFactory.getBean(schedulerType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) {
/* 331 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) {
/* 336 */     if (bean instanceof org.springframework.aop.framework.AopInfrastructureBean || bean instanceof TaskScheduler || bean instanceof ScheduledExecutorService)
/*     */     {
/*     */       
/* 339 */       return bean;
/*     */     }
/*     */     
/* 342 */     Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
/* 343 */     if (!this.nonAnnotatedClasses.contains(targetClass)) {
/* 344 */       Map<Method, Set<Scheduled>> annotatedMethods = MethodIntrospector.selectMethods(targetClass, method -> {
/*     */             Set<Scheduled> scheduledMethods = AnnotatedElementUtils.getMergedRepeatableAnnotations(method, Scheduled.class, Schedules.class);
/*     */             
/*     */             return !scheduledMethods.isEmpty() ? scheduledMethods : null;
/*     */           });
/*     */       
/* 350 */       if (annotatedMethods.isEmpty()) {
/* 351 */         this.nonAnnotatedClasses.add(targetClass);
/* 352 */         if (this.logger.isTraceEnabled()) {
/* 353 */           this.logger.trace("No @Scheduled annotations found on bean class: " + targetClass);
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 358 */         annotatedMethods.forEach((method, scheduledMethods) -> scheduledMethods.forEach(()));
/*     */         
/* 360 */         if (this.logger.isTraceEnabled()) {
/* 361 */           this.logger.trace(annotatedMethods.size() + " @Scheduled methods processed on bean '" + beanName + "': " + annotatedMethods);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 366 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processScheduled(Scheduled scheduled, Method method, Object bean) {
/*     */     try {
/* 378 */       Runnable runnable = createRunnable(bean, method);
/* 379 */       boolean processedSchedule = false;
/* 380 */       String errorMessage = "Exactly one of the 'cron', 'fixedDelay(String)', or 'fixedRate(String)' attributes is required";
/*     */ 
/*     */       
/* 383 */       Set<ScheduledTask> tasks = new LinkedHashSet<>(4);
/*     */ 
/*     */       
/* 386 */       long initialDelay = scheduled.initialDelay();
/* 387 */       String initialDelayString = scheduled.initialDelayString();
/* 388 */       if (StringUtils.hasText(initialDelayString)) {
/* 389 */         Assert.isTrue((initialDelay < 0L), "Specify 'initialDelay' or 'initialDelayString', not both");
/* 390 */         if (this.embeddedValueResolver != null) {
/* 391 */           initialDelayString = this.embeddedValueResolver.resolveStringValue(initialDelayString);
/*     */         }
/* 393 */         if (StringUtils.hasLength(initialDelayString)) {
/*     */           try {
/* 395 */             initialDelay = parseDelayAsLong(initialDelayString);
/*     */           }
/* 397 */           catch (RuntimeException ex) {
/* 398 */             throw new IllegalArgumentException("Invalid initialDelayString value \"" + initialDelayString + "\" - cannot parse into long");
/*     */           } 
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 405 */       String cron = scheduled.cron();
/* 406 */       if (StringUtils.hasText(cron)) {
/* 407 */         String zone = scheduled.zone();
/* 408 */         if (this.embeddedValueResolver != null) {
/* 409 */           cron = this.embeddedValueResolver.resolveStringValue(cron);
/* 410 */           zone = this.embeddedValueResolver.resolveStringValue(zone);
/*     */         } 
/* 412 */         if (StringUtils.hasLength(cron)) {
/* 413 */           Assert.isTrue((initialDelay == -1L), "'initialDelay' not supported for cron triggers");
/* 414 */           processedSchedule = true;
/* 415 */           if (!"-".equals(cron)) {
/*     */             TimeZone timeZone;
/* 417 */             if (StringUtils.hasText(zone)) {
/* 418 */               timeZone = StringUtils.parseTimeZoneString(zone);
/*     */             } else {
/*     */               
/* 421 */               timeZone = TimeZone.getDefault();
/*     */             } 
/* 423 */             tasks.add(this.registrar.scheduleCronTask(new CronTask(runnable, new CronTrigger(cron, timeZone))));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 429 */       if (initialDelay < 0L) {
/* 430 */         initialDelay = 0L;
/*     */       }
/*     */ 
/*     */       
/* 434 */       long fixedDelay = scheduled.fixedDelay();
/* 435 */       if (fixedDelay >= 0L) {
/* 436 */         Assert.isTrue(!processedSchedule, errorMessage);
/* 437 */         processedSchedule = true;
/* 438 */         tasks.add(this.registrar.scheduleFixedDelayTask(new FixedDelayTask(runnable, fixedDelay, initialDelay)));
/*     */       } 
/* 440 */       String fixedDelayString = scheduled.fixedDelayString();
/* 441 */       if (StringUtils.hasText(fixedDelayString)) {
/* 442 */         if (this.embeddedValueResolver != null) {
/* 443 */           fixedDelayString = this.embeddedValueResolver.resolveStringValue(fixedDelayString);
/*     */         }
/* 445 */         if (StringUtils.hasLength(fixedDelayString)) {
/* 446 */           Assert.isTrue(!processedSchedule, errorMessage);
/* 447 */           processedSchedule = true;
/*     */           try {
/* 449 */             fixedDelay = parseDelayAsLong(fixedDelayString);
/*     */           }
/* 451 */           catch (RuntimeException ex) {
/* 452 */             throw new IllegalArgumentException("Invalid fixedDelayString value \"" + fixedDelayString + "\" - cannot parse into long");
/*     */           } 
/*     */           
/* 455 */           tasks.add(this.registrar.scheduleFixedDelayTask(new FixedDelayTask(runnable, fixedDelay, initialDelay)));
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 460 */       long fixedRate = scheduled.fixedRate();
/* 461 */       if (fixedRate >= 0L) {
/* 462 */         Assert.isTrue(!processedSchedule, errorMessage);
/* 463 */         processedSchedule = true;
/* 464 */         tasks.add(this.registrar.scheduleFixedRateTask(new FixedRateTask(runnable, fixedRate, initialDelay)));
/*     */       } 
/* 466 */       String fixedRateString = scheduled.fixedRateString();
/* 467 */       if (StringUtils.hasText(fixedRateString)) {
/* 468 */         if (this.embeddedValueResolver != null) {
/* 469 */           fixedRateString = this.embeddedValueResolver.resolveStringValue(fixedRateString);
/*     */         }
/* 471 */         if (StringUtils.hasLength(fixedRateString)) {
/* 472 */           Assert.isTrue(!processedSchedule, errorMessage);
/* 473 */           processedSchedule = true;
/*     */           try {
/* 475 */             fixedRate = parseDelayAsLong(fixedRateString);
/*     */           }
/* 477 */           catch (RuntimeException ex) {
/* 478 */             throw new IllegalArgumentException("Invalid fixedRateString value \"" + fixedRateString + "\" - cannot parse into long");
/*     */           } 
/*     */           
/* 481 */           tasks.add(this.registrar.scheduleFixedRateTask(new FixedRateTask(runnable, fixedRate, initialDelay)));
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 486 */       Assert.isTrue(processedSchedule, errorMessage);
/*     */ 
/*     */       
/* 489 */       synchronized (this.scheduledTasks) {
/* 490 */         Set<ScheduledTask> regTasks = this.scheduledTasks.computeIfAbsent(bean, key -> new LinkedHashSet(4));
/* 491 */         regTasks.addAll(tasks);
/*     */       }
/*     */     
/* 494 */     } catch (IllegalArgumentException ex) {
/* 495 */       throw new IllegalStateException("Encountered invalid @Scheduled method '" + method
/* 496 */           .getName() + "': " + ex.getMessage());
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
/*     */   protected Runnable createRunnable(Object target, Method method) {
/* 510 */     Assert.isTrue((method.getParameterCount() == 0), "Only no-arg methods may be annotated with @Scheduled");
/* 511 */     Method invocableMethod = AopUtils.selectInvocableMethod(method, target.getClass());
/* 512 */     return (Runnable)new ScheduledMethodRunnable(target, invocableMethod);
/*     */   }
/*     */   
/*     */   private static long parseDelayAsLong(String value) throws RuntimeException {
/* 516 */     if (value.length() > 1 && (isP(value.charAt(0)) || isP(value.charAt(1)))) {
/* 517 */       return Duration.parse(value).toMillis();
/*     */     }
/* 519 */     return Long.parseLong(value);
/*     */   }
/*     */   
/*     */   private static boolean isP(char ch) {
/* 523 */     return (ch == 'P' || ch == 'p');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<ScheduledTask> getScheduledTasks() {
/* 534 */     Set<ScheduledTask> result = new LinkedHashSet<>();
/* 535 */     synchronized (this.scheduledTasks) {
/* 536 */       Collection<Set<ScheduledTask>> allTasks = this.scheduledTasks.values();
/* 537 */       for (Set<ScheduledTask> tasks : allTasks) {
/* 538 */         result.addAll(tasks);
/*     */       }
/*     */     } 
/* 541 */     result.addAll(this.registrar.getScheduledTasks());
/* 542 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcessBeforeDestruction(Object bean, String beanName) {
/*     */     Set<ScheduledTask> tasks;
/* 548 */     synchronized (this.scheduledTasks) {
/* 549 */       tasks = this.scheduledTasks.remove(bean);
/*     */     } 
/* 551 */     if (tasks != null) {
/* 552 */       for (ScheduledTask task : tasks) {
/* 553 */         task.cancel();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresDestruction(Object bean) {
/* 560 */     synchronized (this.scheduledTasks) {
/* 561 */       return this.scheduledTasks.containsKey(bean);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 567 */     synchronized (this.scheduledTasks) {
/* 568 */       Collection<Set<ScheduledTask>> allTasks = this.scheduledTasks.values();
/* 569 */       for (Set<ScheduledTask> tasks : allTasks) {
/* 570 */         for (ScheduledTask task : tasks) {
/* 571 */           task.cancel();
/*     */         }
/*     */       } 
/* 574 */       this.scheduledTasks.clear();
/*     */     } 
/* 576 */     this.registrar.destroy();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/annotation/ScheduledAnnotationBeanPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */