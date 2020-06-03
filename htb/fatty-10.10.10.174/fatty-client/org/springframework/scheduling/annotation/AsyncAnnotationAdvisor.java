/*     */ package org.springframework.scheduling.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
/*     */ import org.springframework.aop.support.AbstractPointcutAdvisor;
/*     */ import org.springframework.aop.support.ComposablePointcut;
/*     */ import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class AsyncAnnotationAdvisor
/*     */   extends AbstractPointcutAdvisor
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   private Advice advice;
/*     */   private Pointcut pointcut;
/*     */   
/*     */   public AsyncAnnotationAdvisor() {
/*  67 */     this((Supplier<Executor>)null, (Supplier<AsyncUncaughtExceptionHandler>)null);
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
/*     */   public AsyncAnnotationAdvisor(@Nullable Executor executor, @Nullable AsyncUncaughtExceptionHandler exceptionHandler) {
/*  82 */     this((Supplier<Executor>)SingletonSupplier.ofNullable(executor), (Supplier<AsyncUncaughtExceptionHandler>)SingletonSupplier.ofNullable(exceptionHandler));
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
/*     */   public AsyncAnnotationAdvisor(@Nullable Supplier<Executor> executor, @Nullable Supplier<AsyncUncaughtExceptionHandler> exceptionHandler) {
/*  98 */     Set<Class<? extends Annotation>> asyncAnnotationTypes = new LinkedHashSet<>(2);
/*  99 */     asyncAnnotationTypes.add(Async.class);
/*     */     try {
/* 101 */       asyncAnnotationTypes.add(
/* 102 */           ClassUtils.forName("javax.ejb.Asynchronous", AsyncAnnotationAdvisor.class.getClassLoader()));
/*     */     }
/* 104 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */     
/* 107 */     this.advice = buildAdvice(executor, exceptionHandler);
/* 108 */     this.pointcut = buildPointcut(asyncAnnotationTypes);
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
/*     */   public void setAsyncAnnotationType(Class<? extends Annotation> asyncAnnotationType) {
/* 122 */     Assert.notNull(asyncAnnotationType, "'asyncAnnotationType' must not be null");
/* 123 */     Set<Class<? extends Annotation>> asyncAnnotationTypes = new HashSet<>();
/* 124 */     asyncAnnotationTypes.add(asyncAnnotationType);
/* 125 */     this.pointcut = buildPointcut(asyncAnnotationTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 133 */     if (this.advice instanceof BeanFactoryAware) {
/* 134 */       ((BeanFactoryAware)this.advice).setBeanFactory(beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Advice getAdvice() {
/* 141 */     return this.advice;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pointcut getPointcut() {
/* 146 */     return this.pointcut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Advice buildAdvice(@Nullable Supplier<Executor> executor, @Nullable Supplier<AsyncUncaughtExceptionHandler> exceptionHandler) {
/* 153 */     AnnotationAsyncExecutionInterceptor interceptor = new AnnotationAsyncExecutionInterceptor(null);
/* 154 */     interceptor.configure(executor, exceptionHandler);
/* 155 */     return (Advice)interceptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Pointcut buildPointcut(Set<Class<? extends Annotation>> asyncAnnotationTypes) {
/* 164 */     ComposablePointcut result = null;
/* 165 */     for (Class<? extends Annotation> asyncAnnotationType : asyncAnnotationTypes) {
/* 166 */       AnnotationMatchingPointcut annotationMatchingPointcut1 = new AnnotationMatchingPointcut(asyncAnnotationType, true);
/* 167 */       AnnotationMatchingPointcut annotationMatchingPointcut2 = new AnnotationMatchingPointcut(null, asyncAnnotationType, true);
/* 168 */       if (result == null) {
/* 169 */         result = new ComposablePointcut((Pointcut)annotationMatchingPointcut1);
/*     */       } else {
/*     */         
/* 172 */         result.union((Pointcut)annotationMatchingPointcut1);
/*     */       } 
/* 174 */       result = result.union((Pointcut)annotationMatchingPointcut2);
/*     */     } 
/* 176 */     return (result != null) ? (Pointcut)result : Pointcut.TRUE;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/annotation/AsyncAnnotationAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */