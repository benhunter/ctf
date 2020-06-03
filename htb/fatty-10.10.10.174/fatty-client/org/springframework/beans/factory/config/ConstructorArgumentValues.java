/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeanMetadataElement;
/*     */ import org.springframework.beans.Mergeable;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstructorArgumentValues
/*     */ {
/*  46 */   private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<>();
/*     */   
/*  48 */   private final List<ValueHolder> genericArgumentValues = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorArgumentValues(ConstructorArgumentValues original) {
/*  62 */     addArgumentValues(original);
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
/*     */   public void addArgumentValues(@Nullable ConstructorArgumentValues other) {
/*  74 */     if (other != null) {
/*  75 */       other.indexedArgumentValues.forEach((index, argValue) -> addOrMergeIndexedArgumentValue(index, argValue.copy()));
/*     */ 
/*     */       
/*  78 */       other.genericArgumentValues.stream()
/*  79 */         .filter(valueHolder -> !this.genericArgumentValues.contains(valueHolder))
/*  80 */         .forEach(valueHolder -> addOrMergeGenericArgumentValue(valueHolder.copy()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIndexedArgumentValue(int index, @Nullable Object value) {
/*  91 */     addIndexedArgumentValue(index, new ValueHolder(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIndexedArgumentValue(int index, @Nullable Object value, String type) {
/* 101 */     addIndexedArgumentValue(index, new ValueHolder(value, type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIndexedArgumentValue(int index, ValueHolder newValue) {
/* 110 */     Assert.isTrue((index >= 0), "Index must not be negative");
/* 111 */     Assert.notNull(newValue, "ValueHolder must not be null");
/* 112 */     addOrMergeIndexedArgumentValue(Integer.valueOf(index), newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addOrMergeIndexedArgumentValue(Integer key, ValueHolder newValue) {
/* 123 */     ValueHolder currentValue = this.indexedArgumentValues.get(key);
/* 124 */     if (currentValue != null && newValue.getValue() instanceof Mergeable) {
/* 125 */       Mergeable mergeable = (Mergeable)newValue.getValue();
/* 126 */       if (mergeable.isMergeEnabled()) {
/* 127 */         newValue.setValue(mergeable.merge(currentValue.getValue()));
/*     */       }
/*     */     } 
/* 130 */     this.indexedArgumentValues.put(key, newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasIndexedArgumentValue(int index) {
/* 138 */     return this.indexedArgumentValues.containsKey(Integer.valueOf(index));
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
/*     */   public ValueHolder getIndexedArgumentValue(int index, @Nullable Class<?> requiredType) {
/* 150 */     return getIndexedArgumentValue(index, requiredType, null);
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
/*     */   @Nullable
/*     */   public ValueHolder getIndexedArgumentValue(int index, @Nullable Class<?> requiredType, @Nullable String requiredName) {
/* 164 */     Assert.isTrue((index >= 0), "Index must not be negative");
/* 165 */     ValueHolder valueHolder = this.indexedArgumentValues.get(Integer.valueOf(index));
/* 166 */     if (valueHolder != null && (valueHolder
/* 167 */       .getType() == null || (requiredType != null && 
/* 168 */       ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) && (valueHolder
/* 169 */       .getName() == null || "".equals(requiredName) || (requiredName != null && requiredName
/* 170 */       .equals(valueHolder.getName())))) {
/* 171 */       return valueHolder;
/*     */     }
/* 173 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Integer, ValueHolder> getIndexedArgumentValues() {
/* 182 */     return Collections.unmodifiableMap(this.indexedArgumentValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGenericArgumentValue(Object value) {
/* 193 */     this.genericArgumentValues.add(new ValueHolder(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGenericArgumentValue(Object value, String type) {
/* 204 */     this.genericArgumentValues.add(new ValueHolder(value, type));
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
/*     */   public void addGenericArgumentValue(ValueHolder newValue) {
/* 217 */     Assert.notNull(newValue, "ValueHolder must not be null");
/* 218 */     if (!this.genericArgumentValues.contains(newValue)) {
/* 219 */       addOrMergeGenericArgumentValue(newValue);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addOrMergeGenericArgumentValue(ValueHolder newValue) {
/* 229 */     if (newValue.getName() != null) {
/* 230 */       for (Iterator<ValueHolder> it = this.genericArgumentValues.iterator(); it.hasNext(); ) {
/* 231 */         ValueHolder currentValue = it.next();
/* 232 */         if (newValue.getName().equals(currentValue.getName())) {
/* 233 */           if (newValue.getValue() instanceof Mergeable) {
/* 234 */             Mergeable mergeable = (Mergeable)newValue.getValue();
/* 235 */             if (mergeable.isMergeEnabled()) {
/* 236 */               newValue.setValue(mergeable.merge(currentValue.getValue()));
/*     */             }
/*     */           } 
/* 239 */           it.remove();
/*     */         } 
/*     */       } 
/*     */     }
/* 243 */     this.genericArgumentValues.add(newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ValueHolder getGenericArgumentValue(Class<?> requiredType) {
/* 253 */     return getGenericArgumentValue(requiredType, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ValueHolder getGenericArgumentValue(Class<?> requiredType, String requiredName) {
/* 264 */     return getGenericArgumentValue(requiredType, requiredName, null);
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
/*     */   @Nullable
/*     */   public ValueHolder getGenericArgumentValue(@Nullable Class<?> requiredType, @Nullable String requiredName, @Nullable Set<ValueHolder> usedValueHolders) {
/* 281 */     for (ValueHolder valueHolder : this.genericArgumentValues) {
/* 282 */       if (usedValueHolders != null && usedValueHolders.contains(valueHolder)) {
/*     */         continue;
/*     */       }
/* 285 */       if (valueHolder.getName() != null && !"".equals(requiredName) && (requiredName == null || 
/* 286 */         !valueHolder.getName().equals(requiredName))) {
/*     */         continue;
/*     */       }
/* 289 */       if (valueHolder.getType() != null && (requiredType == null || 
/* 290 */         !ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) {
/*     */         continue;
/*     */       }
/* 293 */       if (requiredType != null && valueHolder.getType() == null && valueHolder.getName() == null && 
/* 294 */         !ClassUtils.isAssignableValue(requiredType, valueHolder.getValue())) {
/*     */         continue;
/*     */       }
/* 297 */       return valueHolder;
/*     */     } 
/* 299 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ValueHolder> getGenericArgumentValues() {
/* 308 */     return Collections.unmodifiableList(this.genericArgumentValues);
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
/*     */   @Nullable
/*     */   public ValueHolder getArgumentValue(int index, Class<?> requiredType) {
/* 321 */     return getArgumentValue(index, requiredType, null, null);
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
/*     */   @Nullable
/*     */   public ValueHolder getArgumentValue(int index, Class<?> requiredType, String requiredName) {
/* 334 */     return getArgumentValue(index, requiredType, requiredName, null);
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
/*     */   @Nullable
/*     */   public ValueHolder getArgumentValue(int index, @Nullable Class<?> requiredType, @Nullable String requiredName, @Nullable Set<ValueHolder> usedValueHolders) {
/* 353 */     Assert.isTrue((index >= 0), "Index must not be negative");
/* 354 */     ValueHolder valueHolder = getIndexedArgumentValue(index, requiredType, requiredName);
/* 355 */     if (valueHolder == null) {
/* 356 */       valueHolder = getGenericArgumentValue(requiredType, requiredName, usedValueHolders);
/*     */     }
/* 358 */     return valueHolder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArgumentCount() {
/* 366 */     return this.indexedArgumentValues.size() + this.genericArgumentValues.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 374 */     return (this.indexedArgumentValues.isEmpty() && this.genericArgumentValues.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 381 */     this.indexedArgumentValues.clear();
/* 382 */     this.genericArgumentValues.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 388 */     if (this == other) {
/* 389 */       return true;
/*     */     }
/* 391 */     if (!(other instanceof ConstructorArgumentValues)) {
/* 392 */       return false;
/*     */     }
/* 394 */     ConstructorArgumentValues that = (ConstructorArgumentValues)other;
/* 395 */     if (this.genericArgumentValues.size() != that.genericArgumentValues.size() || this.indexedArgumentValues
/* 396 */       .size() != that.indexedArgumentValues.size()) {
/* 397 */       return false;
/*     */     }
/* 399 */     Iterator<ValueHolder> it1 = this.genericArgumentValues.iterator();
/* 400 */     Iterator<ValueHolder> it2 = that.genericArgumentValues.iterator();
/* 401 */     while (it1.hasNext() && it2.hasNext()) {
/* 402 */       ValueHolder vh1 = it1.next();
/* 403 */       ValueHolder vh2 = it2.next();
/* 404 */       if (!vh1.contentEquals(vh2)) {
/* 405 */         return false;
/*     */       }
/*     */     } 
/* 408 */     for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentValues.entrySet()) {
/* 409 */       ValueHolder vh1 = entry.getValue();
/* 410 */       ValueHolder vh2 = that.indexedArgumentValues.get(entry.getKey());
/* 411 */       if (!vh1.contentEquals(vh2)) {
/* 412 */         return false;
/*     */       }
/*     */     } 
/* 415 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 420 */     int hashCode = 7;
/* 421 */     for (ValueHolder valueHolder : this.genericArgumentValues) {
/* 422 */       hashCode = 31 * hashCode + valueHolder.contentHashCode();
/*     */     }
/* 424 */     hashCode = 29 * hashCode;
/* 425 */     for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentValues.entrySet()) {
/* 426 */       hashCode = 31 * hashCode + (((ValueHolder)entry.getValue()).contentHashCode() ^ ((Integer)entry.getKey()).hashCode());
/*     */     }
/* 428 */     return hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorArgumentValues() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ValueHolder
/*     */     implements BeanMetadataElement
/*     */   {
/*     */     @Nullable
/*     */     private Object value;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private String type;
/*     */     
/*     */     @Nullable
/*     */     private String name;
/*     */     
/*     */     @Nullable
/*     */     private Object source;
/*     */     
/*     */     private boolean converted = false;
/*     */     
/*     */     @Nullable
/*     */     private Object convertedValue;
/*     */ 
/*     */     
/*     */     public ValueHolder(@Nullable Object value) {
/* 460 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ValueHolder(@Nullable Object value, @Nullable String type) {
/* 469 */       this.value = value;
/* 470 */       this.type = type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ValueHolder(@Nullable Object value, @Nullable String type, @Nullable String name) {
/* 480 */       this.value = value;
/* 481 */       this.type = type;
/* 482 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object value) {
/* 490 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object getValue() {
/* 498 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setType(@Nullable String type) {
/* 505 */       this.type = type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getType() {
/* 513 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(@Nullable String name) {
/* 520 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getName() {
/* 528 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setSource(@Nullable Object source) {
/* 536 */       this.source = source;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object getSource() {
/* 542 */       return this.source;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized boolean isConverted() {
/* 550 */       return this.converted;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void setConvertedValue(@Nullable Object value) {
/* 558 */       this.converted = (value != null);
/* 559 */       this.convertedValue = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public synchronized Object getConvertedValue() {
/* 568 */       return this.convertedValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean contentEquals(ValueHolder other) {
/* 579 */       return (this == other || (
/* 580 */         ObjectUtils.nullSafeEquals(this.value, other.value) && ObjectUtils.nullSafeEquals(this.type, other.type)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int contentHashCode() {
/* 590 */       return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.type);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ValueHolder copy() {
/* 598 */       ValueHolder copy = new ValueHolder(this.value, this.type, this.name);
/* 599 */       copy.setSource(this.source);
/* 600 */       return copy;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/ConstructorArgumentValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */