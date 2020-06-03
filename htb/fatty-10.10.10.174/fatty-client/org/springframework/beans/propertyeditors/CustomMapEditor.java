/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomMapEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final Class<? extends Map> mapType;
/*     */   private final boolean nullAsEmptyMap;
/*     */   
/*     */   public CustomMapEditor(Class<? extends Map> mapType) {
/*  58 */     this(mapType, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomMapEditor(Class<? extends Map> mapType, boolean nullAsEmptyMap) {
/*  80 */     Assert.notNull(mapType, "Map type is required");
/*  81 */     if (!Map.class.isAssignableFrom(mapType)) {
/*  82 */       throw new IllegalArgumentException("Map type [" + mapType
/*  83 */           .getName() + "] does not implement [java.util.Map]");
/*     */     }
/*  85 */     this.mapType = mapType;
/*  86 */     this.nullAsEmptyMap = nullAsEmptyMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/*  95 */     setValue(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(@Nullable Object value) {
/* 103 */     if (value == null && this.nullAsEmptyMap) {
/* 104 */       super.setValue(createMap(this.mapType, 0));
/*     */     }
/* 106 */     else if (value == null || (this.mapType.isInstance(value) && !alwaysCreateNewMap())) {
/*     */       
/* 108 */       super.setValue(value);
/*     */     }
/* 110 */     else if (value instanceof Map) {
/*     */       
/* 112 */       Map<?, ?> source = (Map<?, ?>)value;
/* 113 */       Map<Object, Object> target = createMap(this.mapType, source.size());
/* 114 */       source.forEach((key, val) -> target.put(convertKey(key), convertValue(val)));
/* 115 */       super.setValue(target);
/*     */     } else {
/*     */       
/* 118 */       throw new IllegalArgumentException("Value cannot be converted to Map: " + value);
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
/*     */   protected Map<Object, Object> createMap(Class<? extends Map> mapType, int initialCapacity) {
/* 131 */     if (!mapType.isInterface()) {
/*     */       try {
/* 133 */         return ReflectionUtils.accessibleConstructor(mapType, new Class[0]).newInstance(new Object[0]);
/*     */       }
/* 135 */       catch (Throwable ex) {
/* 136 */         throw new IllegalArgumentException("Could not instantiate map class: " + mapType
/* 137 */             .getName(), ex);
/*     */       } 
/*     */     }
/* 140 */     if (SortedMap.class == mapType) {
/* 141 */       return new TreeMap<>();
/*     */     }
/*     */     
/* 144 */     return new LinkedHashMap<>(initialCapacity);
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
/*     */   protected boolean alwaysCreateNewMap() {
/* 157 */     return false;
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
/*     */   protected Object convertKey(Object key) {
/* 174 */     return key;
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
/*     */   protected Object convertValue(Object value) {
/* 191 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getAsText() {
/* 202 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/CustomMapEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */