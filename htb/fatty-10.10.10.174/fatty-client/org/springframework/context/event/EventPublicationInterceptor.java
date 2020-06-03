/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationEventPublisher;
/*     */ import org.springframework.context.ApplicationEventPublisherAware;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventPublicationInterceptor
/*     */   implements MethodInterceptor, ApplicationEventPublisherAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private Constructor<?> applicationEventClassConstructor;
/*     */   @Nullable
/*     */   private ApplicationEventPublisher applicationEventPublisher;
/*     */   
/*     */   public void setApplicationEventClass(Class<?> applicationEventClass) {
/*  70 */     if (ApplicationEvent.class == applicationEventClass || 
/*  71 */       !ApplicationEvent.class.isAssignableFrom(applicationEventClass)) {
/*  72 */       throw new IllegalArgumentException("'applicationEventClass' needs to extend ApplicationEvent");
/*     */     }
/*     */     try {
/*  75 */       this.applicationEventClassConstructor = applicationEventClass.getConstructor(new Class[] { Object.class });
/*     */     }
/*  77 */     catch (NoSuchMethodException ex) {
/*  78 */       throw new IllegalArgumentException("ApplicationEvent class [" + applicationEventClass
/*  79 */           .getName() + "] does not have the required Object constructor: " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
/*  85 */     this.applicationEventPublisher = applicationEventPublisher;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/*  90 */     if (this.applicationEventClassConstructor == null) {
/*  91 */       throw new IllegalArgumentException("Property 'applicationEventClass' is required");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/*  98 */     Object retVal = invocation.proceed();
/*     */     
/* 100 */     Assert.state((this.applicationEventClassConstructor != null), "No ApplicationEvent class set");
/*     */     
/* 102 */     ApplicationEvent event = (ApplicationEvent)this.applicationEventClassConstructor.newInstance(new Object[] { invocation.getThis() });
/*     */     
/* 104 */     Assert.state((this.applicationEventPublisher != null), "No ApplicationEventPublisher available");
/* 105 */     this.applicationEventPublisher.publishEvent(event);
/*     */     
/* 107 */     return retVal;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/EventPublicationInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */