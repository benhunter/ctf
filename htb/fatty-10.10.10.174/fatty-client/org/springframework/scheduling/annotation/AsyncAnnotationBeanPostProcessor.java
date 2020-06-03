/*     */ package org.springframework.scheduling.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
/*     */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncAnnotationBeanPostProcessor
/*     */   extends AbstractBeanFactoryAwareAdvisingPostProcessor
/*     */ {
/*     */   public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME = "taskExecutor";
/*  78 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Supplier<Executor> executor;
/*     */   
/*     */   @Nullable
/*     */   private Supplier<AsyncUncaughtExceptionHandler> exceptionHandler;
/*     */   
/*     */   @Nullable
/*     */   private Class<? extends Annotation> asyncAnnotationType;
/*     */ 
/*     */   
/*     */   public AsyncAnnotationBeanPostProcessor() {
/*  92 */     setBeforeExistingAdvisors(true);
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
/*     */   public void configure(@Nullable Supplier<Executor> executor, @Nullable Supplier<AsyncUncaughtExceptionHandler> exceptionHandler) {
/* 104 */     this.executor = executor;
/* 105 */     this.exceptionHandler = exceptionHandler;
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
/*     */   public void setExecutor(Executor executor) {
/* 118 */     this.executor = (Supplier<Executor>)SingletonSupplier.of(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionHandler(AsyncUncaughtExceptionHandler exceptionHandler) {
/* 127 */     this.exceptionHandler = (Supplier<AsyncUncaughtExceptionHandler>)SingletonSupplier.of(exceptionHandler);
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
/*     */   public void setAsyncAnnotationType(Class<? extends Annotation> asyncAnnotationType) {
/* 140 */     Assert.notNull(asyncAnnotationType, "'asyncAnnotationType' must not be null");
/* 141 */     this.asyncAnnotationType = asyncAnnotationType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 147 */     super.setBeanFactory(beanFactory);
/*     */     
/* 149 */     AsyncAnnotationAdvisor advisor = new AsyncAnnotationAdvisor(this.executor, this.exceptionHandler);
/* 150 */     if (this.asyncAnnotationType != null) {
/* 151 */       advisor.setAsyncAnnotationType(this.asyncAnnotationType);
/*     */     }
/* 153 */     advisor.setBeanFactory(beanFactory);
/* 154 */     this.advisor = (Advisor)advisor;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/annotation/AsyncAnnotationBeanPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */