/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Supplier;
/*    */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.ImportAware;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ public abstract class AbstractAsyncConfiguration
/*    */   implements ImportAware
/*    */ {
/*    */   @Nullable
/*    */   protected AnnotationAttributes enableAsync;
/*    */   @Nullable
/*    */   protected Supplier<Executor> executor;
/*    */   @Nullable
/*    */   protected Supplier<AsyncUncaughtExceptionHandler> exceptionHandler;
/*    */   
/*    */   public void setImportMetadata(AnnotationMetadata importMetadata) {
/* 57 */     this.enableAsync = AnnotationAttributes.fromMap(importMetadata
/* 58 */         .getAnnotationAttributes(EnableAsync.class.getName(), false));
/* 59 */     if (this.enableAsync == null) {
/* 60 */       throw new IllegalArgumentException("@EnableAsync is not present on importing class " + importMetadata
/* 61 */           .getClassName());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired(required = false)
/*    */   void setConfigurers(Collection<AsyncConfigurer> configurers) {
/* 70 */     if (CollectionUtils.isEmpty(configurers)) {
/*    */       return;
/*    */     }
/* 73 */     if (configurers.size() > 1) {
/* 74 */       throw new IllegalStateException("Only one AsyncConfigurer may exist");
/*    */     }
/* 76 */     AsyncConfigurer configurer = configurers.iterator().next();
/* 77 */     this.executor = configurer::getAsyncExecutor;
/* 78 */     this.exceptionHandler = configurer::getAsyncUncaughtExceptionHandler;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/annotation/AbstractAsyncConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */