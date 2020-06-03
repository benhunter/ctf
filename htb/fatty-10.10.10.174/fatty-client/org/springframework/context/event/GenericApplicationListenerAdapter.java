/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericApplicationListenerAdapter
/*     */   implements GenericApplicationListener, SmartApplicationListener
/*     */ {
/*  41 */   private static final Map<Class<?>, ResolvableType> eventTypeCache = (Map<Class<?>, ResolvableType>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */   
/*     */   private final ApplicationListener<ApplicationEvent> delegate;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final ResolvableType declaredEventType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericApplicationListenerAdapter(ApplicationListener<?> delegate) {
/*  56 */     Assert.notNull(delegate, "Delegate listener must not be null");
/*  57 */     this.delegate = (ApplicationListener)delegate;
/*  58 */     this.declaredEventType = resolveDeclaredEventType(this.delegate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ApplicationEvent event) {
/*  64 */     this.delegate.onApplicationEvent(event);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsEventType(ResolvableType eventType) {
/*  70 */     if (this.delegate instanceof SmartApplicationListener) {
/*  71 */       Class<? extends ApplicationEvent> eventClass = eventType.resolve();
/*  72 */       return (eventClass != null && ((SmartApplicationListener)this.delegate).supportsEventType(eventClass));
/*     */     } 
/*     */     
/*  75 */     return (this.declaredEventType == null || this.declaredEventType.isAssignableFrom(eventType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
/*  81 */     return supportsEventType(ResolvableType.forClass(eventType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSourceType(@Nullable Class<?> sourceType) {
/*  86 */     return (!(this.delegate instanceof SmartApplicationListener) || ((SmartApplicationListener)this.delegate)
/*  87 */       .supportsSourceType(sourceType));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  92 */     return (this.delegate instanceof Ordered) ? ((Ordered)this.delegate).getOrder() : Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static ResolvableType resolveDeclaredEventType(ApplicationListener<ApplicationEvent> listener) {
/*  98 */     ResolvableType declaredEventType = resolveDeclaredEventType(listener.getClass());
/*  99 */     if (declaredEventType == null || declaredEventType.isAssignableFrom(ApplicationEvent.class)) {
/* 100 */       Class<?> targetClass = AopUtils.getTargetClass(listener);
/* 101 */       if (targetClass != listener.getClass()) {
/* 102 */         declaredEventType = resolveDeclaredEventType(targetClass);
/*     */       }
/*     */     } 
/* 105 */     return declaredEventType;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   static ResolvableType resolveDeclaredEventType(Class<?> listenerType) {
/* 110 */     ResolvableType eventType = eventTypeCache.get(listenerType);
/* 111 */     if (eventType == null) {
/* 112 */       eventType = ResolvableType.forClass(listenerType).as(ApplicationListener.class).getGeneric(new int[0]);
/* 113 */       eventTypeCache.put(listenerType, eventType);
/*     */     } 
/* 115 */     return (eventType != ResolvableType.NONE) ? eventType : null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/GenericApplicationListenerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */