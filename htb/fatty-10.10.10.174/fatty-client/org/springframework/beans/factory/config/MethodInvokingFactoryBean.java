/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodInvokingFactoryBean
/*     */   extends MethodInvokingBean
/*     */   implements FactoryBean<Object>
/*     */ {
/*     */   private boolean singleton = true;
/*     */   private boolean initialized = false;
/*     */   @Nullable
/*     */   private Object singletonObject;
/*     */   
/*     */   public void setSingleton(boolean singleton) {
/* 100 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 105 */     prepare();
/* 106 */     if (this.singleton) {
/* 107 */       this.initialized = true;
/* 108 */       this.singletonObject = invokeWithTargetException();
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
/*     */   @Nullable
/*     */   public Object getObject() throws Exception {
/* 121 */     if (this.singleton) {
/* 122 */       if (!this.initialized) {
/* 123 */         throw new FactoryBeanNotInitializedException();
/*     */       }
/*     */       
/* 126 */       return this.singletonObject;
/*     */     } 
/*     */ 
/*     */     
/* 130 */     return invokeWithTargetException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 140 */     if (!isPrepared())
/*     */     {
/* 142 */       return null;
/*     */     }
/* 144 */     return getPreparedMethod().getReturnType();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 149 */     return this.singleton;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/MethodInvokingFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */