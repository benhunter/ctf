/*     */ package org.springframework.scheduling.config;
/*     */ 
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.task.TaskExecutor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
/*     */ public class TaskExecutorFactoryBean
/*     */   implements FactoryBean<TaskExecutor>, BeanNameAware, InitializingBean, DisposableBean
/*     */ {
/*     */   @Nullable
/*     */   private String poolSize;
/*     */   @Nullable
/*     */   private Integer queueCapacity;
/*     */   @Nullable
/*     */   private RejectedExecutionHandler rejectedExecutionHandler;
/*     */   @Nullable
/*     */   private Integer keepAliveSeconds;
/*     */   @Nullable
/*     */   private String beanName;
/*     */   @Nullable
/*     */   private ThreadPoolTaskExecutor target;
/*     */   
/*     */   public void setPoolSize(String poolSize) {
/*  61 */     this.poolSize = poolSize;
/*     */   }
/*     */   
/*     */   public void setQueueCapacity(int queueCapacity) {
/*  65 */     this.queueCapacity = Integer.valueOf(queueCapacity);
/*     */   }
/*     */   
/*     */   public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
/*  69 */     this.rejectedExecutionHandler = rejectedExecutionHandler;
/*     */   }
/*     */   
/*     */   public void setKeepAliveSeconds(int keepAliveSeconds) {
/*  73 */     this.keepAliveSeconds = Integer.valueOf(keepAliveSeconds);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String beanName) {
/*  78 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  84 */     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
/*  85 */     determinePoolSizeRange(executor);
/*  86 */     if (this.queueCapacity != null) {
/*  87 */       executor.setQueueCapacity(this.queueCapacity.intValue());
/*     */     }
/*  89 */     if (this.keepAliveSeconds != null) {
/*  90 */       executor.setKeepAliveSeconds(this.keepAliveSeconds.intValue());
/*     */     }
/*  92 */     if (this.rejectedExecutionHandler != null) {
/*  93 */       executor.setRejectedExecutionHandler(this.rejectedExecutionHandler);
/*     */     }
/*  95 */     if (this.beanName != null) {
/*  96 */       executor.setThreadNamePrefix(this.beanName + "-");
/*     */     }
/*  98 */     executor.afterPropertiesSet();
/*  99 */     this.target = executor;
/*     */   }
/*     */   
/*     */   private void determinePoolSizeRange(ThreadPoolTaskExecutor executor) {
/* 103 */     if (StringUtils.hasText(this.poolSize)) {
/*     */       
/*     */       try {
/*     */         
/* 107 */         int corePoolSize, maxPoolSize, separatorIndex = this.poolSize.indexOf('-');
/* 108 */         if (separatorIndex != -1) {
/* 109 */           corePoolSize = Integer.valueOf(this.poolSize.substring(0, separatorIndex)).intValue();
/* 110 */           maxPoolSize = Integer.valueOf(this.poolSize.substring(separatorIndex + 1, this.poolSize.length())).intValue();
/* 111 */           if (corePoolSize > maxPoolSize) {
/* 112 */             throw new IllegalArgumentException("Lower bound of pool-size range must not exceed the upper bound");
/*     */           }
/*     */           
/* 115 */           if (this.queueCapacity == null)
/*     */           {
/* 117 */             if (corePoolSize == 0)
/*     */             {
/*     */               
/* 120 */               executor.setAllowCoreThreadTimeOut(true);
/* 121 */               corePoolSize = maxPoolSize;
/*     */             }
/*     */             else
/*     */             {
/* 125 */               throw new IllegalArgumentException("A non-zero lower bound for the size range requires a queue-capacity value");
/*     */             }
/*     */           
/*     */           }
/*     */         } else {
/*     */           
/* 131 */           Integer value = Integer.valueOf(this.poolSize);
/* 132 */           corePoolSize = value.intValue();
/* 133 */           maxPoolSize = value.intValue();
/*     */         } 
/* 135 */         executor.setCorePoolSize(corePoolSize);
/* 136 */         executor.setMaxPoolSize(maxPoolSize);
/*     */       }
/* 138 */       catch (NumberFormatException ex) {
/* 139 */         throw new IllegalArgumentException("Invalid pool-size value [" + this.poolSize + "]: only single maximum integer (e.g. \"5\") and minimum-maximum range (e.g. \"3-5\") are supported", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TaskExecutor getObject() {
/* 149 */     return (TaskExecutor)this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends TaskExecutor> getObjectType() {
/* 154 */     return (this.target != null) ? (Class)this.target.getClass() : (Class)ThreadPoolTaskExecutor.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 159 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 165 */     if (this.target != null)
/* 166 */       this.target.destroy(); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/config/TaskExecutorFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */