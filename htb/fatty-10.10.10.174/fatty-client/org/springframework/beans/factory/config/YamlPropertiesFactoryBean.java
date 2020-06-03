/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.CollectionFactory;
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
/*     */ public class YamlPropertiesFactoryBean
/*     */   extends YamlProcessor
/*     */   implements FactoryBean<Properties>, InitializingBean
/*     */ {
/*     */   private boolean singleton = true;
/*     */   @Nullable
/*     */   private Properties properties;
/*     */   
/*     */   public void setSingleton(boolean singleton) {
/*  97 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 102 */     return this.singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 107 */     if (isSingleton()) {
/* 108 */       this.properties = createProperties();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Properties getObject() {
/* 115 */     return (this.properties != null) ? this.properties : createProperties();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 120 */     return Properties.class;
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
/*     */   protected Properties createProperties() {
/* 134 */     Properties result = CollectionFactory.createStringAdaptingProperties();
/* 135 */     process((properties, map) -> result.putAll(properties));
/* 136 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/YamlPropertiesFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */