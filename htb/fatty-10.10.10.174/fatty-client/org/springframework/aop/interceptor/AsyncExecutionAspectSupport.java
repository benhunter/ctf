/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.core.task.TaskExecutor;
/*     */ import org.springframework.core.task.support.TaskExecutorAdapter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.function.SingletonSupplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AsyncExecutionAspectSupport
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME = "taskExecutor";
/*  73 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  75 */   private final Map<Method, AsyncTaskExecutor> executors = new ConcurrentHashMap<>(16);
/*     */ 
/*     */ 
/*     */   
/*     */   private SingletonSupplier<Executor> defaultExecutor;
/*     */ 
/*     */ 
/*     */   
/*     */   private SingletonSupplier<AsyncUncaughtExceptionHandler> exceptionHandler;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncExecutionAspectSupport(@Nullable Executor defaultExecutor) {
/*  93 */     this.defaultExecutor = new SingletonSupplier(defaultExecutor, () -> getDefaultExecutor(this.beanFactory));
/*  94 */     this.exceptionHandler = SingletonSupplier.of(SimpleAsyncUncaughtExceptionHandler::new);
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
/*     */   public AsyncExecutionAspectSupport(@Nullable Executor defaultExecutor, AsyncUncaughtExceptionHandler exceptionHandler) {
/* 106 */     this.defaultExecutor = new SingletonSupplier(defaultExecutor, () -> getDefaultExecutor(this.beanFactory));
/* 107 */     this.exceptionHandler = SingletonSupplier.of(exceptionHandler);
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
/*     */   public void configure(@Nullable Supplier<Executor> defaultExecutor, @Nullable Supplier<AsyncUncaughtExceptionHandler> exceptionHandler) {
/* 119 */     this.defaultExecutor = new SingletonSupplier(defaultExecutor, () -> getDefaultExecutor(this.beanFactory));
/* 120 */     this.exceptionHandler = new SingletonSupplier(exceptionHandler, SimpleAsyncUncaughtExceptionHandler::new);
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
/*     */   public void setExecutor(Executor defaultExecutor) {
/* 134 */     this.defaultExecutor = SingletonSupplier.of(defaultExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionHandler(AsyncUncaughtExceptionHandler exceptionHandler) {
/* 142 */     this.exceptionHandler = SingletonSupplier.of(exceptionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 153 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected AsyncTaskExecutor determineAsyncExecutor(Method method) {
/* 164 */     AsyncTaskExecutor executor = this.executors.get(method);
/* 165 */     if (executor == null) {
/*     */       Executor targetExecutor;
/* 167 */       String qualifier = getExecutorQualifier(method);
/* 168 */       if (StringUtils.hasLength(qualifier)) {
/* 169 */         targetExecutor = findQualifiedExecutor(this.beanFactory, qualifier);
/*     */       } else {
/*     */         
/* 172 */         targetExecutor = (Executor)this.defaultExecutor.get();
/*     */       } 
/* 174 */       if (targetExecutor == null) {
/* 175 */         return null;
/*     */       }
/* 177 */       executor = (targetExecutor instanceof AsyncListenableTaskExecutor) ? (AsyncTaskExecutor)targetExecutor : (AsyncTaskExecutor)new TaskExecutorAdapter(targetExecutor);
/*     */       
/* 179 */       this.executors.put(method, executor);
/*     */     } 
/* 181 */     return executor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Executor findQualifiedExecutor(@Nullable BeanFactory beanFactory, String qualifier) {
/* 207 */     if (beanFactory == null) {
/* 208 */       throw new IllegalStateException("BeanFactory must be set on " + getClass().getSimpleName() + " to access qualified executor '" + qualifier + "'");
/*     */     }
/*     */     
/* 211 */     return (Executor)BeanFactoryAnnotationUtils.qualifiedBeanOfType(beanFactory, Executor.class, qualifier);
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
/*     */   @Nullable
/*     */   protected Executor getDefaultExecutor(@Nullable BeanFactory beanFactory) {
/* 228 */     if (beanFactory != null) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 233 */         return (Executor)beanFactory.getBean(TaskExecutor.class);
/*     */       }
/* 235 */       catch (NoUniqueBeanDefinitionException ex) {
/* 236 */         this.logger.debug("Could not find unique TaskExecutor bean", (Throwable)ex);
/*     */         try {
/* 238 */           return (Executor)beanFactory.getBean("taskExecutor", Executor.class);
/*     */         }
/* 240 */         catch (NoSuchBeanDefinitionException ex2) {
/* 241 */           if (this.logger.isInfoEnabled()) {
/* 242 */             this.logger.info("More than one TaskExecutor bean found within the context, and none is named 'taskExecutor'. Mark one of them as primary or name it 'taskExecutor' (possibly as an alias) in order to use it for async processing: " + ex
/*     */                 
/* 244 */                 .getBeanNamesFound());
/*     */           }
/*     */         }
/*     */       
/* 248 */       } catch (NoSuchBeanDefinitionException ex) {
/* 249 */         this.logger.debug("Could not find default TaskExecutor bean", (Throwable)ex);
/*     */         try {
/* 251 */           return (Executor)beanFactory.getBean("taskExecutor", Executor.class);
/*     */         }
/* 253 */         catch (NoSuchBeanDefinitionException ex2) {
/* 254 */           this.logger.info("No task executor bean found for async processing: no bean of type TaskExecutor and no bean named 'taskExecutor' either");
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 260 */     return null;
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
/*     */   @Nullable
/*     */   protected Object doSubmit(Callable<Object> task, AsyncTaskExecutor executor, Class<?> returnType) {
/* 273 */     if (CompletableFuture.class.isAssignableFrom(returnType)) {
/* 274 */       return CompletableFuture.supplyAsync(() -> {
/*     */             
/*     */             try {
/*     */               return task.call();
/* 278 */             } catch (Throwable ex) {
/*     */               throw new CompletionException(ex);
/*     */             } 
/*     */           }(Executor)executor);
/*     */     }
/* 283 */     if (ListenableFuture.class.isAssignableFrom(returnType)) {
/* 284 */       return ((AsyncListenableTaskExecutor)executor).submitListenable(task);
/*     */     }
/* 286 */     if (Future.class.isAssignableFrom(returnType)) {
/* 287 */       return executor.submit(task);
/*     */     }
/*     */     
/* 290 */     executor.submit(task);
/* 291 */     return null;
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
/*     */   protected void handleError(Throwable ex, Method method, Object... params) throws Exception {
/* 308 */     if (Future.class.isAssignableFrom(method.getReturnType())) {
/* 309 */       ReflectionUtils.rethrowException(ex);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 314 */         ((AsyncUncaughtExceptionHandler)this.exceptionHandler.obtain()).handleUncaughtException(ex, method, params);
/*     */       }
/* 316 */       catch (Throwable ex2) {
/* 317 */         this.logger.warn("Exception handler for async method '" + method.toGenericString() + "' threw unexpected exception itself", ex2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract String getExecutorQualifier(Method paramMethod);
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/interceptor/AsyncExecutionAspectSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */