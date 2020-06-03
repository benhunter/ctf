/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ErrorHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleApplicationEventMulticaster
/*     */   extends AbstractApplicationEventMulticaster
/*     */ {
/*     */   @Nullable
/*     */   private Executor taskExecutor;
/*     */   @Nullable
/*     */   private ErrorHandler errorHandler;
/*     */   
/*     */   public SimpleApplicationEventMulticaster() {}
/*     */   
/*     */   public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
/*  68 */     setBeanFactory(beanFactory);
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
/*     */   public void setTaskExecutor(@Nullable Executor taskExecutor) {
/*  85 */     this.taskExecutor = taskExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Executor getTaskExecutor() {
/*  93 */     return this.taskExecutor;
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
/*     */   public void setErrorHandler(@Nullable ErrorHandler errorHandler) {
/* 112 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ErrorHandler getErrorHandler() {
/* 121 */     return this.errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void multicastEvent(ApplicationEvent event) {
/* 127 */     multicastEvent(event, resolveDefaultEventType(event));
/*     */   }
/*     */ 
/*     */   
/*     */   public void multicastEvent(ApplicationEvent event, @Nullable ResolvableType eventType) {
/* 132 */     ResolvableType type = (eventType != null) ? eventType : resolveDefaultEventType(event);
/* 133 */     Executor executor = getTaskExecutor();
/* 134 */     for (ApplicationListener<?> listener : getApplicationListeners(event, type)) {
/* 135 */       if (executor != null) {
/* 136 */         executor.execute(() -> invokeListener(listener, event));
/*     */         continue;
/*     */       } 
/* 139 */       invokeListener(listener, event);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private ResolvableType resolveDefaultEventType(ApplicationEvent event) {
/* 145 */     return ResolvableType.forInstance(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void invokeListener(ApplicationListener<?> listener, ApplicationEvent event) {
/* 155 */     ErrorHandler errorHandler = getErrorHandler();
/* 156 */     if (errorHandler != null) {
/*     */       try {
/* 158 */         doInvokeListener(listener, event);
/*     */       }
/* 160 */       catch (Throwable err) {
/* 161 */         errorHandler.handleError(err);
/*     */       } 
/*     */     } else {
/*     */       
/* 165 */       doInvokeListener(listener, event);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doInvokeListener(ApplicationListener listener, ApplicationEvent event) {
/*     */     try {
/* 172 */       listener.onApplicationEvent(event);
/*     */     }
/* 174 */     catch (ClassCastException ex) {
/* 175 */       String msg = ex.getMessage();
/* 176 */       if (msg == null || matchesClassCastMessage(msg, event.getClass())) {
/*     */ 
/*     */         
/* 179 */         Log logger = LogFactory.getLog(getClass());
/* 180 */         if (logger.isTraceEnabled()) {
/* 181 */           logger.trace("Non-matching event type for listener: " + listener, ex);
/*     */         }
/*     */       } else {
/*     */         
/* 185 */         throw ex;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean matchesClassCastMessage(String classCastMessage, Class<?> eventClass) {
/* 192 */     if (classCastMessage.startsWith(eventClass.getName())) {
/* 193 */       return true;
/*     */     }
/*     */     
/* 196 */     if (classCastMessage.startsWith(eventClass.toString())) {
/* 197 */       return true;
/*     */     }
/*     */     
/* 200 */     int moduleSeparatorIndex = classCastMessage.indexOf('/');
/* 201 */     if (moduleSeparatorIndex != -1 && classCastMessage.startsWith(eventClass.getName(), moduleSeparatorIndex + 1)) {
/* 202 */       return true;
/*     */     }
/*     */     
/* 205 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/SimpleApplicationEventMulticaster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */