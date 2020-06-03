/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.core.ResolvableType;
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
/*     */ public class MapFactoryBean
/*     */   extends AbstractFactoryBean<Map<Object, Object>>
/*     */ {
/*     */   @Nullable
/*     */   private Map<?, ?> sourceMap;
/*     */   @Nullable
/*     */   private Class<? extends Map> targetMapClass;
/*     */   
/*     */   public void setSourceMap(Map<?, ?> sourceMap) {
/*  50 */     this.sourceMap = sourceMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetMapClass(@Nullable Class<? extends Map> targetMapClass) {
/*  61 */     if (targetMapClass == null) {
/*  62 */       throw new IllegalArgumentException("'targetMapClass' must not be null");
/*     */     }
/*  64 */     if (!Map.class.isAssignableFrom(targetMapClass)) {
/*  65 */       throw new IllegalArgumentException("'targetMapClass' must implement [java.util.Map]");
/*     */     }
/*  67 */     this.targetMapClass = targetMapClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<Map> getObjectType() {
/*  74 */     return Map.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> createInstance() {
/*  80 */     if (this.sourceMap == null) {
/*  81 */       throw new IllegalArgumentException("'sourceMap' is required");
/*     */     }
/*  83 */     Map<Object, Object> result = null;
/*  84 */     if (this.targetMapClass != null) {
/*  85 */       result = (Map<Object, Object>)BeanUtils.instantiateClass(this.targetMapClass);
/*     */     } else {
/*     */       
/*  88 */       result = new LinkedHashMap<>(this.sourceMap.size());
/*     */     } 
/*  90 */     Class<?> keyType = null;
/*  91 */     Class<?> valueType = null;
/*  92 */     if (this.targetMapClass != null) {
/*  93 */       ResolvableType mapType = ResolvableType.forClass(this.targetMapClass).asMap();
/*  94 */       keyType = mapType.resolveGeneric(new int[] { 0 });
/*  95 */       valueType = mapType.resolveGeneric(new int[] { 1 });
/*     */     } 
/*  97 */     if (keyType != null || valueType != null) {
/*  98 */       TypeConverter converter = getBeanTypeConverter();
/*  99 */       for (Map.Entry<?, ?> entry : this.sourceMap.entrySet()) {
/* 100 */         Object convertedKey = converter.convertIfNecessary(entry.getKey(), keyType);
/* 101 */         Object convertedValue = converter.convertIfNecessary(entry.getValue(), valueType);
/* 102 */         result.put(convertedKey, convertedValue);
/*     */       } 
/*     */     } else {
/*     */       
/* 106 */       result.putAll(this.sourceMap);
/*     */     } 
/* 108 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/MapFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */