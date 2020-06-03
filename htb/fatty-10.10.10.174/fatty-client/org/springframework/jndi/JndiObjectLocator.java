/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JndiObjectLocator
/*     */   extends JndiLocatorSupport
/*     */   implements InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private String jndiName;
/*     */   @Nullable
/*     */   private Class<?> expectedType;
/*     */   
/*     */   public void setJndiName(@Nullable String jndiName) {
/*  66 */     this.jndiName = jndiName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getJndiName() {
/*  74 */     return this.jndiName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpectedType(@Nullable Class<?> expectedType) {
/*  82 */     this.expectedType = expectedType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getExpectedType() {
/*  91 */     return this.expectedType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws IllegalArgumentException, NamingException {
/*  96 */     if (!StringUtils.hasLength(getJndiName())) {
/*  97 */       throw new IllegalArgumentException("Property 'jndiName' is required");
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
/*     */   protected Object lookup() throws NamingException {
/* 112 */     String jndiName = getJndiName();
/* 113 */     Assert.state((jndiName != null), "No JNDI name specified");
/* 114 */     return lookup(jndiName, getExpectedType());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiObjectLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */