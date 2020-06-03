/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SourceFilteringListener
/*     */   implements GenericApplicationListener, SmartApplicationListener
/*     */ {
/*     */   private final Object source;
/*     */   @Nullable
/*     */   private GenericApplicationListener delegate;
/*     */   
/*     */   public SourceFilteringListener(Object source, ApplicationListener<?> delegate) {
/*  53 */     this.source = source;
/*  54 */     this.delegate = (delegate instanceof GenericApplicationListener) ? (GenericApplicationListener)delegate : new GenericApplicationListenerAdapter(delegate);
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
/*     */   protected SourceFilteringListener(Object source) {
/*  66 */     this.source = source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ApplicationEvent event) {
/*  72 */     if (event.getSource() == this.source) {
/*  73 */       onApplicationEventInternal(event);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsEventType(ResolvableType eventType) {
/*  79 */     return (this.delegate == null || this.delegate.supportsEventType(eventType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
/*  84 */     return supportsEventType(ResolvableType.forType(eventType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSourceType(@Nullable Class<?> sourceType) {
/*  89 */     return (sourceType != null && sourceType.isInstance(this.source));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  94 */     return (this.delegate != null) ? this.delegate.getOrder() : Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onApplicationEventInternal(ApplicationEvent event) {
/* 105 */     if (this.delegate == null) {
/* 106 */       throw new IllegalStateException("Must specify a delegate object or override the onApplicationEventInternal method");
/*     */     }
/*     */     
/* 109 */     this.delegate.onApplicationEvent(event);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/SourceFilteringListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */