/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.MessageSource;
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
/*     */ public abstract class ApplicationObjectSupport
/*     */   implements ApplicationContextAware
/*     */ {
/*  52 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MessageSourceAccessor messageSourceAccessor;
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setApplicationContext(@Nullable ApplicationContext context) throws BeansException {
/*  65 */     if (context == null && !isContextRequired()) {
/*     */       
/*  67 */       this.applicationContext = null;
/*  68 */       this.messageSourceAccessor = null;
/*     */     }
/*  70 */     else if (this.applicationContext == null) {
/*     */       
/*  72 */       if (!requiredContextClass().isInstance(context)) {
/*  73 */         throw new ApplicationContextException("Invalid application context: needs to be of type [" + 
/*  74 */             requiredContextClass().getName() + "]");
/*     */       }
/*  76 */       this.applicationContext = context;
/*  77 */       this.messageSourceAccessor = new MessageSourceAccessor((MessageSource)context);
/*  78 */       initApplicationContext(context);
/*     */ 
/*     */     
/*     */     }
/*  82 */     else if (this.applicationContext != context) {
/*  83 */       throw new ApplicationContextException("Cannot reinitialize with different application context: current one is [" + this.applicationContext + "], passed-in one is [" + context + "]");
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isContextRequired() {
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> requiredContextClass() {
/* 108 */     return ApplicationContext.class;
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
/*     */   protected void initApplicationContext(ApplicationContext context) throws BeansException {
/* 124 */     initApplicationContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initApplicationContext() throws BeansException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final ApplicationContext getApplicationContext() throws IllegalStateException {
/* 145 */     if (this.applicationContext == null && isContextRequired()) {
/* 146 */       throw new IllegalStateException("ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
/*     */     }
/*     */     
/* 149 */     return this.applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ApplicationContext obtainApplicationContext() {
/* 159 */     ApplicationContext applicationContext = getApplicationContext();
/* 160 */     Assert.state((applicationContext != null), "No ApplicationContext");
/* 161 */     return applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected final MessageSourceAccessor getMessageSourceAccessor() throws IllegalStateException {
/* 171 */     if (this.messageSourceAccessor == null && isContextRequired()) {
/* 172 */       throw new IllegalStateException("ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
/*     */     }
/*     */     
/* 175 */     return this.messageSourceAccessor;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/ApplicationObjectSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */