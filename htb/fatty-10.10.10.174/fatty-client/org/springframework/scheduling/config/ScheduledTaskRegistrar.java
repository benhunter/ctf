/*     */ package org.springframework.scheduling.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scheduling.TaskScheduler;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScheduledTaskRegistrar
/*     */   implements ScheduledTaskHolder, InitializingBean, DisposableBean
/*     */ {
/*     */   @Nullable
/*     */   private TaskScheduler taskScheduler;
/*     */   @Nullable
/*     */   private ScheduledExecutorService localExecutor;
/*     */   @Nullable
/*     */   private List<TriggerTask> triggerTasks;
/*     */   @Nullable
/*     */   private List<CronTask> cronTasks;
/*     */   @Nullable
/*     */   private List<IntervalTask> fixedRateTasks;
/*     */   @Nullable
/*     */   private List<IntervalTask> fixedDelayTasks;
/*  77 */   private final Map<Task, ScheduledTask> unresolvedTasks = new HashMap<>(16);
/*     */   
/*  79 */   private final Set<ScheduledTask> scheduledTasks = new LinkedHashSet<>(16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskScheduler(TaskScheduler taskScheduler) {
/*  86 */     Assert.notNull(taskScheduler, "TaskScheduler must not be null");
/*  87 */     this.taskScheduler = taskScheduler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScheduler(@Nullable Object scheduler) {
/*  96 */     if (scheduler == null) {
/*  97 */       this.taskScheduler = null;
/*     */     }
/*  99 */     else if (scheduler instanceof TaskScheduler) {
/* 100 */       this.taskScheduler = (TaskScheduler)scheduler;
/*     */     }
/* 102 */     else if (scheduler instanceof ScheduledExecutorService) {
/* 103 */       this.taskScheduler = (TaskScheduler)new ConcurrentTaskScheduler((ScheduledExecutorService)scheduler);
/*     */     } else {
/*     */       
/* 106 */       throw new IllegalArgumentException("Unsupported scheduler type: " + scheduler.getClass());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TaskScheduler getScheduler() {
/* 115 */     return this.taskScheduler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTriggerTasks(Map<Runnable, Trigger> triggerTasks) {
/* 124 */     this.triggerTasks = new ArrayList<>();
/* 125 */     triggerTasks.forEach((task, trigger) -> addTriggerTask(new TriggerTask(task, trigger)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTriggerTasksList(List<TriggerTask> triggerTasks) {
/* 135 */     this.triggerTasks = triggerTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<TriggerTask> getTriggerTaskList() {
/* 144 */     return (this.triggerTasks != null) ? Collections.<TriggerTask>unmodifiableList(this.triggerTasks) : 
/* 145 */       Collections.<TriggerTask>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCronTasks(Map<Runnable, String> cronTasks) {
/* 153 */     this.cronTasks = new ArrayList<>();
/* 154 */     cronTasks.forEach(this::addCronTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCronTasksList(List<CronTask> cronTasks) {
/* 164 */     this.cronTasks = cronTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CronTask> getCronTaskList() {
/* 173 */     return (this.cronTasks != null) ? Collections.<CronTask>unmodifiableList(this.cronTasks) : 
/* 174 */       Collections.<CronTask>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedRateTasks(Map<Runnable, Long> fixedRateTasks) {
/* 182 */     this.fixedRateTasks = new ArrayList<>();
/* 183 */     fixedRateTasks.forEach(this::addFixedRateTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedRateTasksList(List<IntervalTask> fixedRateTasks) {
/* 193 */     this.fixedRateTasks = fixedRateTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IntervalTask> getFixedRateTaskList() {
/* 202 */     return (this.fixedRateTasks != null) ? Collections.<IntervalTask>unmodifiableList(this.fixedRateTasks) : 
/* 203 */       Collections.<IntervalTask>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedDelayTasks(Map<Runnable, Long> fixedDelayTasks) {
/* 211 */     this.fixedDelayTasks = new ArrayList<>();
/* 212 */     fixedDelayTasks.forEach(this::addFixedDelayTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedDelayTasksList(List<IntervalTask> fixedDelayTasks) {
/* 222 */     this.fixedDelayTasks = fixedDelayTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IntervalTask> getFixedDelayTaskList() {
/* 231 */     return (this.fixedDelayTasks != null) ? Collections.<IntervalTask>unmodifiableList(this.fixedDelayTasks) : 
/* 232 */       Collections.<IntervalTask>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTriggerTask(Runnable task, Trigger trigger) {
/* 241 */     addTriggerTask(new TriggerTask(task, trigger));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTriggerTask(TriggerTask task) {
/* 250 */     if (this.triggerTasks == null) {
/* 251 */       this.triggerTasks = new ArrayList<>();
/*     */     }
/* 253 */     this.triggerTasks.add(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCronTask(Runnable task, String expression) {
/* 260 */     addCronTask(new CronTask(task, expression));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCronTask(CronTask task) {
/* 268 */     if (this.cronTasks == null) {
/* 269 */       this.cronTasks = new ArrayList<>();
/*     */     }
/* 271 */     this.cronTasks.add(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFixedRateTask(Runnable task, long interval) {
/* 279 */     addFixedRateTask(new IntervalTask(task, interval, 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFixedRateTask(IntervalTask task) {
/* 288 */     if (this.fixedRateTasks == null) {
/* 289 */       this.fixedRateTasks = new ArrayList<>();
/*     */     }
/* 291 */     this.fixedRateTasks.add(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFixedDelayTask(Runnable task, long delay) {
/* 299 */     addFixedDelayTask(new IntervalTask(task, delay, 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFixedDelayTask(IntervalTask task) {
/* 308 */     if (this.fixedDelayTasks == null) {
/* 309 */       this.fixedDelayTasks = new ArrayList<>();
/*     */     }
/* 311 */     this.fixedDelayTasks.add(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTasks() {
/* 320 */     return (!CollectionUtils.isEmpty(this.triggerTasks) || 
/* 321 */       !CollectionUtils.isEmpty(this.cronTasks) || 
/* 322 */       !CollectionUtils.isEmpty(this.fixedRateTasks) || 
/* 323 */       !CollectionUtils.isEmpty(this.fixedDelayTasks));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 332 */     scheduleTasks();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void scheduleTasks() {
/* 341 */     if (this.taskScheduler == null) {
/* 342 */       this.localExecutor = Executors.newSingleThreadScheduledExecutor();
/* 343 */       this.taskScheduler = (TaskScheduler)new ConcurrentTaskScheduler(this.localExecutor);
/*     */     } 
/* 345 */     if (this.triggerTasks != null) {
/* 346 */       for (TriggerTask task : this.triggerTasks) {
/* 347 */         addScheduledTask(scheduleTriggerTask(task));
/*     */       }
/*     */     }
/* 350 */     if (this.cronTasks != null) {
/* 351 */       for (CronTask task : this.cronTasks) {
/* 352 */         addScheduledTask(scheduleCronTask(task));
/*     */       }
/*     */     }
/* 355 */     if (this.fixedRateTasks != null) {
/* 356 */       for (IntervalTask task : this.fixedRateTasks) {
/* 357 */         addScheduledTask(scheduleFixedRateTask(task));
/*     */       }
/*     */     }
/* 360 */     if (this.fixedDelayTasks != null) {
/* 361 */       for (IntervalTask task : this.fixedDelayTasks) {
/* 362 */         addScheduledTask(scheduleFixedDelayTask(task));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void addScheduledTask(@Nullable ScheduledTask task) {
/* 368 */     if (task != null) {
/* 369 */       this.scheduledTasks.add(task);
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
/*     */   @Nullable
/*     */   public ScheduledTask scheduleTriggerTask(TriggerTask task) {
/* 382 */     ScheduledTask scheduledTask = this.unresolvedTasks.remove(task);
/* 383 */     boolean newTask = false;
/* 384 */     if (scheduledTask == null) {
/* 385 */       scheduledTask = new ScheduledTask(task);
/* 386 */       newTask = true;
/*     */     } 
/* 388 */     if (this.taskScheduler != null) {
/* 389 */       scheduledTask.future = this.taskScheduler.schedule(task.getRunnable(), task.getTrigger());
/*     */     } else {
/*     */       
/* 392 */       addTriggerTask(task);
/* 393 */       this.unresolvedTasks.put(task, scheduledTask);
/*     */     } 
/* 395 */     return newTask ? scheduledTask : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ScheduledTask scheduleCronTask(CronTask task) {
/* 407 */     ScheduledTask scheduledTask = this.unresolvedTasks.remove(task);
/* 408 */     boolean newTask = false;
/* 409 */     if (scheduledTask == null) {
/* 410 */       scheduledTask = new ScheduledTask(task);
/* 411 */       newTask = true;
/*     */     } 
/* 413 */     if (this.taskScheduler != null) {
/* 414 */       scheduledTask.future = this.taskScheduler.schedule(task.getRunnable(), task.getTrigger());
/*     */     } else {
/*     */       
/* 417 */       addCronTask(task);
/* 418 */       this.unresolvedTasks.put(task, scheduledTask);
/*     */     } 
/* 420 */     return newTask ? scheduledTask : null;
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
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public ScheduledTask scheduleFixedRateTask(IntervalTask task) {
/* 435 */     FixedRateTask taskToUse = (task instanceof FixedRateTask) ? (FixedRateTask)task : new FixedRateTask(task.getRunnable(), task.getInterval(), task.getInitialDelay());
/* 436 */     return scheduleFixedRateTask(taskToUse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ScheduledTask scheduleFixedRateTask(FixedRateTask task) {
/* 448 */     ScheduledTask scheduledTask = this.unresolvedTasks.remove(task);
/* 449 */     boolean newTask = false;
/* 450 */     if (scheduledTask == null) {
/* 451 */       scheduledTask = new ScheduledTask(task);
/* 452 */       newTask = true;
/*     */     } 
/* 454 */     if (this.taskScheduler != null) {
/* 455 */       if (task.getInitialDelay() > 0L) {
/* 456 */         Date startTime = new Date(System.currentTimeMillis() + task.getInitialDelay());
/* 457 */         scheduledTask
/* 458 */           .future = this.taskScheduler.scheduleAtFixedRate(task.getRunnable(), startTime, task.getInterval());
/*     */       } else {
/*     */         
/* 461 */         scheduledTask
/* 462 */           .future = this.taskScheduler.scheduleAtFixedRate(task.getRunnable(), task.getInterval());
/*     */       } 
/*     */     } else {
/*     */       
/* 466 */       addFixedRateTask(task);
/* 467 */       this.unresolvedTasks.put(task, scheduledTask);
/*     */     } 
/* 469 */     return newTask ? scheduledTask : null;
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
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public ScheduledTask scheduleFixedDelayTask(IntervalTask task) {
/* 484 */     FixedDelayTask taskToUse = (task instanceof FixedDelayTask) ? (FixedDelayTask)task : new FixedDelayTask(task.getRunnable(), task.getInterval(), task.getInitialDelay());
/* 485 */     return scheduleFixedDelayTask(taskToUse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ScheduledTask scheduleFixedDelayTask(FixedDelayTask task) {
/* 497 */     ScheduledTask scheduledTask = this.unresolvedTasks.remove(task);
/* 498 */     boolean newTask = false;
/* 499 */     if (scheduledTask == null) {
/* 500 */       scheduledTask = new ScheduledTask(task);
/* 501 */       newTask = true;
/*     */     } 
/* 503 */     if (this.taskScheduler != null) {
/* 504 */       if (task.getInitialDelay() > 0L) {
/* 505 */         Date startTime = new Date(System.currentTimeMillis() + task.getInitialDelay());
/* 506 */         scheduledTask
/* 507 */           .future = this.taskScheduler.scheduleWithFixedDelay(task.getRunnable(), startTime, task.getInterval());
/*     */       } else {
/*     */         
/* 510 */         scheduledTask
/* 511 */           .future = this.taskScheduler.scheduleWithFixedDelay(task.getRunnable(), task.getInterval());
/*     */       } 
/*     */     } else {
/*     */       
/* 515 */       addFixedDelayTask(task);
/* 516 */       this.unresolvedTasks.put(task, scheduledTask);
/*     */     } 
/* 518 */     return newTask ? scheduledTask : null;
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
/*     */   public Set<ScheduledTask> getScheduledTasks() {
/* 532 */     return Collections.unmodifiableSet(this.scheduledTasks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 537 */     for (ScheduledTask task : this.scheduledTasks) {
/* 538 */       task.cancel();
/*     */     }
/* 540 */     if (this.localExecutor != null)
/* 541 */       this.localExecutor.shutdownNow(); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/config/ScheduledTaskRegistrar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */