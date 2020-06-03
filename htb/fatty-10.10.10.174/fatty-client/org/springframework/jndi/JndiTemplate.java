/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiTemplate
/*     */ {
/*  44 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Properties environment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JndiTemplate(@Nullable Properties environment) {
/*  60 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(@Nullable Properties environment) {
/*  68 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Properties getEnvironment() {
/*  76 */     return this.environment;
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
/*     */   public <T> T execute(JndiCallback<T> contextCallback) throws NamingException {
/*  89 */     Context ctx = getContext();
/*     */     try {
/*  91 */       return contextCallback.doInContext(ctx);
/*     */     } finally {
/*     */       
/*  94 */       releaseContext(ctx);
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
/*     */   public Context getContext() throws NamingException {
/* 107 */     return createInitialContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseContext(@Nullable Context ctx) {
/* 116 */     if (ctx != null) {
/*     */       try {
/* 118 */         ctx.close();
/*     */       }
/* 120 */       catch (NamingException ex) {
/* 121 */         this.logger.debug("Could not close JNDI InitialContext", ex);
/*     */       } 
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
/*     */   protected Context createInitialContext() throws NamingException {
/* 134 */     Hashtable<?, ?> icEnv = null;
/* 135 */     Properties env = getEnvironment();
/* 136 */     if (env != null) {
/* 137 */       icEnv = new Hashtable<>(env.size());
/* 138 */       CollectionUtils.mergePropertiesIntoMap(env, icEnv);
/*     */     } 
/* 140 */     return new InitialContext(icEnv);
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
/*     */   public Object lookup(String name) throws NamingException {
/* 153 */     if (this.logger.isDebugEnabled()) {
/* 154 */       this.logger.debug("Looking up JNDI object with name [" + name + "]");
/*     */     }
/* 156 */     Object result = execute(ctx -> ctx.lookup(name));
/* 157 */     if (result == null) {
/* 158 */       throw new NameNotFoundException("JNDI object with [" + name + "] not found: JNDI implementation returned null");
/*     */     }
/*     */     
/* 161 */     return result;
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
/*     */   public <T> T lookup(String name, @Nullable Class<T> requiredType) throws NamingException {
/* 178 */     Object jndiObject = lookup(name);
/* 179 */     if (requiredType != null && !requiredType.isInstance(jndiObject)) {
/* 180 */       throw new TypeMismatchNamingException(name, requiredType, jndiObject.getClass());
/*     */     }
/* 182 */     return (T)jndiObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind(String name, Object object) throws NamingException {
/* 192 */     if (this.logger.isDebugEnabled()) {
/* 193 */       this.logger.debug("Binding JNDI object with name [" + name + "]");
/*     */     }
/* 195 */     execute(ctx -> {
/*     */           ctx.bind(name, object);
/*     */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rebind(String name, Object object) throws NamingException {
/* 209 */     if (this.logger.isDebugEnabled()) {
/* 210 */       this.logger.debug("Rebinding JNDI object with name [" + name + "]");
/*     */     }
/* 212 */     execute(ctx -> {
/*     */           ctx.rebind(name, object);
/*     */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unbind(String name) throws NamingException {
/* 224 */     if (this.logger.isDebugEnabled()) {
/* 225 */       this.logger.debug("Unbinding JNDI object with name [" + name + "]");
/*     */     }
/* 227 */     execute(ctx -> {
/*     */           ctx.unbind(name);
/*     */           return null;
/*     */         });
/*     */   }
/*     */   
/*     */   public JndiTemplate() {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */