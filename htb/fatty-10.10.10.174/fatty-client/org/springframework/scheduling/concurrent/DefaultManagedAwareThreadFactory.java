/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jndi.JndiLocatorDelegate;
/*     */ import org.springframework.jndi.JndiTemplate;
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
/*     */ public class DefaultManagedAwareThreadFactory
/*     */   extends CustomizableThreadFactory
/*     */   implements InitializingBean
/*     */ {
/*  51 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  53 */   private JndiLocatorDelegate jndiLocator = new JndiLocatorDelegate();
/*     */   @Nullable
/*  55 */   private String jndiName = "java:comp/DefaultManagedThreadFactory";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ThreadFactory threadFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiTemplate(JndiTemplate jndiTemplate) {
/*  67 */     this.jndiLocator.setJndiTemplate(jndiTemplate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiEnvironment(Properties jndiEnvironment) {
/*  75 */     this.jndiLocator.setJndiEnvironment(jndiEnvironment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceRef(boolean resourceRef) {
/*  85 */     this.jndiLocator.setResourceRef(resourceRef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiName(String jndiName) {
/*  96 */     this.jndiName = jndiName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/* 101 */     if (this.jndiName != null) {
/*     */       try {
/* 103 */         this.threadFactory = (ThreadFactory)this.jndiLocator.lookup(this.jndiName, ThreadFactory.class);
/*     */       }
/* 105 */       catch (NamingException ex) {
/* 106 */         if (this.logger.isTraceEnabled()) {
/* 107 */           this.logger.trace("Failed to retrieve [" + this.jndiName + "] from JNDI", ex);
/*     */         }
/* 109 */         this.logger.info("Could not find default managed thread factory in JNDI - proceeding with default local thread factory");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Thread newThread(Runnable runnable) {
/* 118 */     if (this.threadFactory != null) {
/* 119 */       return this.threadFactory.newThread(runnable);
/*     */     }
/*     */     
/* 122 */     return super.newThread(runnable);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/DefaultManagedAwareThreadFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */