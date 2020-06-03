/*    */ package org.springframework.scheduling.concurrent;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import java.util.concurrent.Executor;
/*    */ import javax.naming.NamingException;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.jndi.JndiLocatorDelegate;
/*    */ import org.springframework.jndi.JndiTemplate;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class DefaultManagedTaskExecutor
/*    */   extends ConcurrentTaskExecutor
/*    */   implements InitializingBean
/*    */ {
/* 42 */   private JndiLocatorDelegate jndiLocator = new JndiLocatorDelegate();
/*    */   @Nullable
/* 44 */   private String jndiName = "java:comp/DefaultManagedExecutorService";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJndiTemplate(JndiTemplate jndiTemplate) {
/* 53 */     this.jndiLocator.setJndiTemplate(jndiTemplate);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJndiEnvironment(Properties jndiEnvironment) {
/* 61 */     this.jndiLocator.setJndiEnvironment(jndiEnvironment);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setResourceRef(boolean resourceRef) {
/* 71 */     this.jndiLocator.setResourceRef(resourceRef);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJndiName(String jndiName) {
/* 83 */     this.jndiName = jndiName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() throws NamingException {
/* 88 */     if (this.jndiName != null)
/* 89 */       setConcurrentExecutor((Executor)this.jndiLocator.lookup(this.jndiName, Executor.class)); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/DefaultManagedTaskExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */