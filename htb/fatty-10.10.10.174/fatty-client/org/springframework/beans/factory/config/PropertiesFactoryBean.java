/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.io.support.PropertiesLoaderSupport;
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
/*     */ public class PropertiesFactoryBean
/*     */   extends PropertiesLoaderSupport
/*     */   implements FactoryBean<Properties>, InitializingBean
/*     */ {
/*     */   private boolean singleton = true;
/*     */   @Nullable
/*     */   private Properties singletonInstance;
/*     */   
/*     */   public final void setSingleton(boolean singleton) {
/*  61 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSingleton() {
/*  66 */     return this.singleton;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void afterPropertiesSet() throws IOException {
/*  72 */     if (this.singleton) {
/*  73 */       this.singletonInstance = createProperties();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Properties getObject() throws IOException {
/*  80 */     if (this.singleton) {
/*  81 */       return this.singletonInstance;
/*     */     }
/*     */     
/*  84 */     return createProperties();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<Properties> getObjectType() {
/*  90 */     return Properties.class;
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
/*     */   protected Properties createProperties() throws IOException {
/* 105 */     return mergeProperties();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/PropertiesFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */