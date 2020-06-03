/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import javax.naming.NamingException;
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
/*     */ public abstract class JndiLocatorSupport
/*     */   extends JndiAccessor
/*     */ {
/*     */   public static final String CONTAINER_PREFIX = "java:comp/env/";
/*     */   private boolean resourceRef = false;
/*     */   
/*     */   public void setResourceRef(boolean resourceRef) {
/*  57 */     this.resourceRef = resourceRef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResourceRef() {
/*  64 */     return this.resourceRef;
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
/*     */   protected Object lookup(String jndiName) throws NamingException {
/*  78 */     return lookup(jndiName, (Class<?>)null);
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
/*     */   protected <T> T lookup(String jndiName, @Nullable Class<T> requiredType) throws NamingException {
/*     */     T jndiObject;
/*  92 */     Assert.notNull(jndiName, "'jndiName' must not be null");
/*  93 */     String convertedName = convertJndiName(jndiName);
/*     */     
/*     */     try {
/*  96 */       jndiObject = getJndiTemplate().lookup(convertedName, requiredType);
/*     */     }
/*  98 */     catch (NamingException ex) {
/*  99 */       if (!convertedName.equals(jndiName)) {
/*     */         
/* 101 */         if (this.logger.isDebugEnabled()) {
/* 102 */           this.logger.debug("Converted JNDI name [" + convertedName + "] not found - trying original name [" + jndiName + "]. " + ex);
/*     */         }
/*     */         
/* 105 */         jndiObject = getJndiTemplate().lookup(jndiName, requiredType);
/*     */       } else {
/*     */         
/* 108 */         throw ex;
/*     */       } 
/*     */     } 
/* 111 */     if (this.logger.isDebugEnabled()) {
/* 112 */       this.logger.debug("Located object with JNDI name [" + convertedName + "]");
/*     */     }
/* 114 */     return jndiObject;
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
/*     */   protected String convertJndiName(String jndiName) {
/* 128 */     if (isResourceRef() && !jndiName.startsWith("java:comp/env/") && jndiName.indexOf(':') == -1) {
/* 129 */       jndiName = "java:comp/env/" + jndiName;
/*     */     }
/* 131 */     return jndiName;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiLocatorSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */