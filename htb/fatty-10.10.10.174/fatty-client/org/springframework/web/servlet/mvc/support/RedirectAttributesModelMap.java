/*     */ package org.springframework.web.servlet.mvc.support;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.Model;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.validation.DataBinder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RedirectAttributesModelMap
/*     */   extends ModelMap
/*     */   implements RedirectAttributes
/*     */ {
/*     */   @Nullable
/*     */   private final DataBinder dataBinder;
/*  41 */   private final ModelMap flashAttributes = new ModelMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectAttributesModelMap() {
/*  49 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectAttributesModelMap(@Nullable DataBinder dataBinder) {
/*  57 */     this.dataBinder = dataBinder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ?> getFlashAttributes() {
/*  66 */     return (Map<String, ?>)this.flashAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectAttributesModelMap addAttribute(String attributeName, @Nullable Object attributeValue) {
/*  75 */     super.addAttribute(attributeName, formatValue(attributeValue));
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String formatValue(@Nullable Object value) {
/*  81 */     if (value == null) {
/*  82 */       return null;
/*     */     }
/*  84 */     return (this.dataBinder != null) ? (String)this.dataBinder.convertIfNecessary(value, String.class) : value.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectAttributesModelMap addAttribute(Object attributeValue) {
/*  93 */     super.addAttribute(attributeValue);
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectAttributesModelMap addAllAttributes(@Nullable Collection<?> attributeValues) {
/* 103 */     super.addAllAttributes(attributeValues);
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectAttributesModelMap addAllAttributes(@Nullable Map<String, ?> attributes) {
/* 113 */     if (attributes != null) {
/* 114 */       attributes.forEach(this::addAttribute);
/*     */     }
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectAttributesModelMap mergeAttributes(@Nullable Map<String, ?> attributes) {
/* 125 */     if (attributes != null) {
/* 126 */       attributes.forEach((key, attribute) -> {
/*     */             if (!containsKey(key)) {
/*     */               addAttribute(key, attribute);
/*     */             }
/*     */           });
/*     */     }
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> asMap() {
/* 137 */     return (Map<String, Object>)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object put(String key, @Nullable Object value) {
/* 146 */     return super.put(key, formatValue(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(@Nullable Map<? extends String, ? extends Object> map) {
/* 155 */     if (map != null) {
/* 156 */       map.forEach((key, value) -> put(key, formatValue(value)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RedirectAttributes addFlashAttribute(String attributeName, @Nullable Object attributeValue) {
/* 162 */     this.flashAttributes.addAttribute(attributeName, attributeValue);
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RedirectAttributes addFlashAttribute(Object attributeValue) {
/* 168 */     this.flashAttributes.addAttribute(attributeValue);
/* 169 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/support/RedirectAttributesModelMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */