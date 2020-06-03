/*     */ package org.springframework.ui;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ public class ModelMap
/*     */   extends LinkedHashMap<String, Object>
/*     */ {
/*     */   public ModelMap() {}
/*     */   
/*     */   public ModelMap(String attributeName, @Nullable Object attributeValue) {
/*  55 */     addAttribute(attributeName, attributeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap(Object attributeValue) {
/*  65 */     addAttribute(attributeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap addAttribute(String attributeName, @Nullable Object attributeValue) {
/*  75 */     Assert.notNull(attributeName, "Model attribute name must not be null");
/*  76 */     put(attributeName, attributeValue);
/*  77 */     return this;
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
/*     */   public ModelMap addAttribute(Object attributeValue) {
/*  90 */     Assert.notNull(attributeValue, "Model object must not be null");
/*  91 */     if (attributeValue instanceof Collection && ((Collection)attributeValue).isEmpty()) {
/*  92 */       return this;
/*     */     }
/*  94 */     return addAttribute(Conventions.getVariableName(attributeValue), attributeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap addAllAttributes(@Nullable Collection<?> attributeValues) {
/* 103 */     if (attributeValues != null) {
/* 104 */       for (Object attributeValue : attributeValues) {
/* 105 */         addAttribute(attributeValue);
/*     */       }
/*     */     }
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap addAllAttributes(@Nullable Map<String, ?> attributes) {
/* 116 */     if (attributes != null) {
/* 117 */       putAll(attributes);
/*     */     }
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap mergeAttributes(@Nullable Map<String, ?> attributes) {
/* 128 */     if (attributes != null) {
/* 129 */       attributes.forEach((key, value) -> {
/*     */             if (!containsKey(key)) {
/*     */               put(key, value);
/*     */             }
/*     */           });
/*     */     }
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAttribute(String attributeName) {
/* 144 */     return containsKey(attributeName);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ui/ModelMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */