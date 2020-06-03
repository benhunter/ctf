/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ public class YamlMapFactoryBean
/*     */   extends YamlProcessor
/*     */   implements FactoryBean<Map<String, Object>>, InitializingBean
/*     */ {
/*     */   private boolean singleton = true;
/*     */   @Nullable
/*     */   private Map<String, Object> map;
/*     */   
/*     */   public void setSingleton(boolean singleton) {
/*  86 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/*  91 */     return this.singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  96 */     if (isSingleton()) {
/*  97 */       this.map = createMap();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map<String, Object> getObject() {
/* 104 */     return (this.map != null) ? this.map : createMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 109 */     return Map.class;
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
/*     */   protected Map<String, Object> createMap() {
/* 123 */     Map<String, Object> result = new LinkedHashMap<>();
/* 124 */     process((properties, map) -> merge(result, map));
/* 125 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private void merge(Map<String, Object> output, Map<String, Object> map) {
/* 130 */     map.forEach((key, value) -> {
/*     */           Object existing = output.get(key);
/*     */           if (value instanceof Map && existing instanceof Map) {
/*     */             Map<String, Object> result = new LinkedHashMap<>((Map<? extends String, ?>)existing);
/*     */             merge(result, (Map<String, Object>)value);
/*     */             output.put(key, result);
/*     */           } else {
/*     */             output.put(key, value);
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/YamlMapFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */