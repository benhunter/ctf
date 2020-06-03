/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.stream.Stream;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MutablePropertyValues
/*     */   implements PropertyValues, Serializable
/*     */ {
/*     */   private final List<PropertyValue> propertyValueList;
/*     */   @Nullable
/*     */   private Set<String> processedProperties;
/*     */   private volatile boolean converted = false;
/*     */   
/*     */   public MutablePropertyValues() {
/*  61 */     this.propertyValueList = new ArrayList<>(0);
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
/*     */   public MutablePropertyValues(@Nullable PropertyValues original) {
/*  74 */     if (original != null) {
/*  75 */       PropertyValue[] pvs = original.getPropertyValues();
/*  76 */       this.propertyValueList = new ArrayList<>(pvs.length);
/*  77 */       for (PropertyValue pv : pvs) {
/*  78 */         this.propertyValueList.add(new PropertyValue(pv));
/*     */       }
/*     */     } else {
/*     */       
/*  82 */       this.propertyValueList = new ArrayList<>(0);
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
/*     */   public MutablePropertyValues(@Nullable Map<?, ?> original) {
/*  94 */     if (original != null) {
/*  95 */       this.propertyValueList = new ArrayList<>(original.size());
/*  96 */       original.forEach((attrName, attrValue) -> this.propertyValueList.add(new PropertyValue(attrName.toString(), attrValue)));
/*     */     }
/*     */     else {
/*     */       
/* 100 */       this.propertyValueList = new ArrayList<>(0);
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
/*     */   public MutablePropertyValues(@Nullable List<PropertyValue> propertyValueList) {
/* 112 */     this.propertyValueList = (propertyValueList != null) ? propertyValueList : new ArrayList<>();
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
/*     */   public List<PropertyValue> getPropertyValueList() {
/* 124 */     return this.propertyValueList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 131 */     return this.propertyValueList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertyValues addPropertyValues(@Nullable PropertyValues other) {
/* 142 */     if (other != null) {
/* 143 */       PropertyValue[] pvs = other.getPropertyValues();
/* 144 */       for (PropertyValue pv : pvs) {
/* 145 */         addPropertyValue(new PropertyValue(pv));
/*     */       }
/*     */     } 
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertyValues addPropertyValues(@Nullable Map<?, ?> other) {
/* 158 */     if (other != null) {
/* 159 */       other.forEach((attrName, attrValue) -> addPropertyValue(new PropertyValue(attrName.toString(), attrValue)));
/*     */     }
/*     */     
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertyValues addPropertyValue(PropertyValue pv) {
/* 172 */     for (int i = 0; i < this.propertyValueList.size(); i++) {
/* 173 */       PropertyValue currentPv = this.propertyValueList.get(i);
/* 174 */       if (currentPv.getName().equals(pv.getName())) {
/* 175 */         pv = mergeIfRequired(pv, currentPv);
/* 176 */         setPropertyValueAt(pv, i);
/* 177 */         return this;
/*     */       } 
/*     */     } 
/* 180 */     this.propertyValueList.add(pv);
/* 181 */     return this;
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
/*     */   public void addPropertyValue(String propertyName, Object propertyValue) {
/* 194 */     addPropertyValue(new PropertyValue(propertyName, propertyValue));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertyValues add(String propertyName, @Nullable Object propertyValue) {
/* 205 */     addPropertyValue(new PropertyValue(propertyName, propertyValue));
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyValueAt(PropertyValue pv, int i) {
/* 214 */     this.propertyValueList.set(i, pv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PropertyValue mergeIfRequired(PropertyValue newPv, PropertyValue currentPv) {
/* 223 */     Object value = newPv.getValue();
/* 224 */     if (value instanceof Mergeable) {
/* 225 */       Mergeable mergeable = (Mergeable)value;
/* 226 */       if (mergeable.isMergeEnabled()) {
/* 227 */         Object merged = mergeable.merge(currentPv.getValue());
/* 228 */         return new PropertyValue(newPv.getName(), merged);
/*     */       } 
/*     */     } 
/* 231 */     return newPv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePropertyValue(PropertyValue pv) {
/* 239 */     this.propertyValueList.remove(pv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePropertyValue(String propertyName) {
/* 248 */     this.propertyValueList.remove(getPropertyValue(propertyName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<PropertyValue> iterator() {
/* 254 */     return Collections.<PropertyValue>unmodifiableList(this.propertyValueList).iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<PropertyValue> spliterator() {
/* 259 */     return Spliterators.spliterator(this.propertyValueList, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<PropertyValue> stream() {
/* 264 */     return this.propertyValueList.stream();
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyValue[] getPropertyValues() {
/* 269 */     return this.propertyValueList.<PropertyValue>toArray(new PropertyValue[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyValue getPropertyValue(String propertyName) {
/* 275 */     for (PropertyValue pv : this.propertyValueList) {
/* 276 */       if (pv.getName().equals(propertyName)) {
/* 277 */         return pv;
/*     */       }
/*     */     } 
/* 280 */     return null;
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
/*     */   public Object get(String propertyName) {
/* 293 */     PropertyValue pv = getPropertyValue(propertyName);
/* 294 */     return (pv != null) ? pv.getValue() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyValues changesSince(PropertyValues old) {
/* 299 */     MutablePropertyValues changes = new MutablePropertyValues();
/* 300 */     if (old == this) {
/* 301 */       return changes;
/*     */     }
/*     */ 
/*     */     
/* 305 */     for (PropertyValue newPv : this.propertyValueList) {
/*     */       
/* 307 */       PropertyValue pvOld = old.getPropertyValue(newPv.getName());
/* 308 */       if (pvOld == null || !pvOld.equals(newPv)) {
/* 309 */         changes.addPropertyValue(newPv);
/*     */       }
/*     */     } 
/* 312 */     return changes;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(String propertyName) {
/* 317 */     return (getPropertyValue(propertyName) != null || (this.processedProperties != null && this.processedProperties
/* 318 */       .contains(propertyName)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 323 */     return this.propertyValueList.isEmpty();
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
/*     */   public void registerProcessedProperty(String propertyName) {
/* 336 */     if (this.processedProperties == null) {
/* 337 */       this.processedProperties = new HashSet<>(4);
/*     */     }
/* 339 */     this.processedProperties.add(propertyName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearProcessedProperty(String propertyName) {
/* 347 */     if (this.processedProperties != null) {
/* 348 */       this.processedProperties.remove(propertyName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConverted() {
/* 357 */     this.converted = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConverted() {
/* 365 */     return this.converted;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 371 */     return (this == other || (other instanceof MutablePropertyValues && this.propertyValueList
/* 372 */       .equals(((MutablePropertyValues)other).propertyValueList)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 377 */     return this.propertyValueList.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 382 */     PropertyValue[] pvs = getPropertyValues();
/* 383 */     StringBuilder sb = (new StringBuilder("PropertyValues: length=")).append(pvs.length);
/* 384 */     if (pvs.length > 0) {
/* 385 */       sb.append("; ").append(StringUtils.arrayToDelimitedString((Object[])pvs, "; "));
/*     */     }
/* 387 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/MutablePropertyValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */