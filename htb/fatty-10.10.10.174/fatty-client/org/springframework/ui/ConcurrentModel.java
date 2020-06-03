/*     */ package org.springframework.ui;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.core.Conventions;
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
/*     */ public class ConcurrentModel
/*     */   extends ConcurrentHashMap<String, Object>
/*     */   implements Model
/*     */ {
/*     */   public ConcurrentModel() {}
/*     */   
/*     */   public ConcurrentModel(String attributeName, Object attributeValue) {
/*  54 */     addAttribute(attributeName, attributeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentModel(Object attributeValue) {
/*  64 */     addAttribute(attributeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object put(String key, Object value) {
/*  70 */     if (value != null) {
/*  71 */       return super.put(key, value);
/*     */     }
/*     */     
/*  74 */     return remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ?> map) {
/*  80 */     for (Map.Entry<? extends String, ?> entry : map.entrySet()) {
/*  81 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentModel addAttribute(String attributeName, @Nullable Object attributeValue) {
/*  92 */     Assert.notNull(attributeName, "Model attribute name must not be null");
/*  93 */     put(attributeName, attributeValue);
/*  94 */     return this;
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
/*     */   public ConcurrentModel addAttribute(Object attributeValue) {
/* 107 */     Assert.notNull(attributeValue, "Model attribute value must not be null");
/* 108 */     if (attributeValue instanceof Collection && ((Collection)attributeValue).isEmpty()) {
/* 109 */       return this;
/*     */     }
/* 111 */     return addAttribute(Conventions.getVariableName(attributeValue), attributeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentModel addAllAttributes(@Nullable Collection<?> attributeValues) {
/* 120 */     if (attributeValues != null) {
/* 121 */       for (Object attributeValue : attributeValues) {
/* 122 */         addAttribute(attributeValue);
/*     */       }
/*     */     }
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentModel addAllAttributes(@Nullable Map<String, ?> attributes) {
/* 133 */     if (attributes != null) {
/* 134 */       putAll(attributes);
/*     */     }
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentModel mergeAttributes(@Nullable Map<String, ?> attributes) {
/* 145 */     if (attributes != null) {
/* 146 */       attributes.forEach((key, value) -> {
/*     */             if (!containsKey(key)) {
/*     */               put(key, value);
/*     */             }
/*     */           });
/*     */     }
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAttribute(String attributeName) {
/* 161 */     return containsKey(attributeName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> asMap() {
/* 166 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ui/ConcurrentModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */