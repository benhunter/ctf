/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.core.env.PropertySource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiPropertySource
/*     */   extends PropertySource<JndiLocatorDelegate>
/*     */ {
/*     */   public JndiPropertySource(String name) {
/*  62 */     this(name, JndiLocatorDelegate.createDefaultResourceRefLocator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JndiPropertySource(String name, JndiLocatorDelegate jndiLocator) {
/*  70 */     super(name, jndiLocator);
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
/*     */   @Nullable
/*     */   public Object getProperty(String name) {
/*  83 */     if (((JndiLocatorDelegate)getSource()).isResourceRef() && name.indexOf(':') != -1)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  89 */       return null;
/*     */     }
/*     */     
/*     */     try {
/*  93 */       Object value = ((JndiLocatorDelegate)this.source).lookup(name);
/*  94 */       if (this.logger.isDebugEnabled()) {
/*  95 */         this.logger.debug("JNDI lookup for name [" + name + "] returned: [" + value + "]");
/*     */       }
/*  97 */       return value;
/*     */     }
/*  99 */     catch (NamingException ex) {
/* 100 */       if (this.logger.isDebugEnabled()) {
/* 101 */         this.logger.debug("JNDI lookup for name [" + name + "] threw NamingException with message: " + ex
/* 102 */             .getMessage() + ". Returning null.");
/*     */       }
/* 104 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */