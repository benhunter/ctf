/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.aop.TargetSource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiObjectTargetSource
/*     */   extends JndiObjectLocator
/*     */   implements TargetSource
/*     */ {
/*     */   private boolean lookupOnStartup = true;
/*     */   private boolean cache = true;
/*     */   @Nullable
/*     */   private Object cachedObject;
/*     */   @Nullable
/*     */   private Class<?> targetClass;
/*     */   
/*     */   public void setLookupOnStartup(boolean lookupOnStartup) {
/*  82 */     this.lookupOnStartup = lookupOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCache(boolean cache) {
/*  93 */     this.cache = cache;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/*  98 */     super.afterPropertiesSet();
/*  99 */     if (this.lookupOnStartup) {
/* 100 */       Object object = lookup();
/* 101 */       if (this.cache) {
/* 102 */         this.cachedObject = object;
/*     */       } else {
/*     */         
/* 105 */         this.targetClass = object.getClass();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getTargetClass() {
/* 114 */     if (this.cachedObject != null) {
/* 115 */       return this.cachedObject.getClass();
/*     */     }
/* 117 */     if (this.targetClass != null) {
/* 118 */       return this.targetClass;
/*     */     }
/*     */     
/* 121 */     return getExpectedType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 127 */     return (this.cachedObject != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getTarget() {
/*     */     try {
/* 134 */       if (this.lookupOnStartup || !this.cache) {
/* 135 */         return (this.cachedObject != null) ? this.cachedObject : lookup();
/*     */       }
/*     */       
/* 138 */       synchronized (this) {
/* 139 */         if (this.cachedObject == null) {
/* 140 */           this.cachedObject = lookup();
/*     */         }
/* 142 */         return this.cachedObject;
/*     */       }
/*     */     
/*     */     }
/* 146 */     catch (NamingException ex) {
/* 147 */       throw new JndiLookupFailureException("JndiObjectTargetSource failed to obtain new target object", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseTarget(Object target) {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiObjectTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */