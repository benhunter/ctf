/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class LinkedMultiValueMap<K, V>
/*     */   implements MultiValueMap<K, V>, Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 3801124242820219131L;
/*     */   private final Map<K, List<V>> targetMap;
/*     */   
/*     */   public LinkedMultiValueMap() {
/*  53 */     this.targetMap = new LinkedHashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedMultiValueMap(int initialCapacity) {
/*  62 */     this.targetMap = new LinkedHashMap<>(initialCapacity);
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
/*     */   public LinkedMultiValueMap(Map<K, List<V>> otherMap) {
/*  74 */     this.targetMap = new LinkedHashMap<>(otherMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V getFirst(K key) {
/*  83 */     List<V> values = this.targetMap.get(key);
/*  84 */     return (values != null && !values.isEmpty()) ? values.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(K key, @Nullable V value) {
/*  89 */     List<V> values = this.targetMap.computeIfAbsent(key, k -> new LinkedList());
/*  90 */     values.add(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(K key, List<? extends V> values) {
/*  95 */     List<V> currentValues = this.targetMap.computeIfAbsent(key, k -> new LinkedList());
/*  96 */     currentValues.addAll(values);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(MultiValueMap<K, V> values) {
/* 101 */     for (Map.Entry<K, List<V>> entry : values.entrySet()) {
/* 102 */       addAll(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(K key, @Nullable V value) {
/* 108 */     List<V> values = new LinkedList<>();
/* 109 */     values.add(value);
/* 110 */     this.targetMap.put(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAll(Map<K, V> values) {
/* 115 */     values.forEach(this::set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<K, V> toSingleValueMap() {
/* 120 */     LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<>(this.targetMap.size());
/* 121 */     this.targetMap.forEach((key, values) -> {
/*     */           if (values != null && !values.isEmpty()) {
/*     */             singleValueMap.put(key, values.get(0));
/*     */           }
/*     */         });
/* 126 */     return singleValueMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 134 */     return this.targetMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 139 */     return this.targetMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 144 */     return this.targetMap.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 149 */     return this.targetMap.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<V> get(Object key) {
/* 155 */     return this.targetMap.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<V> put(K key, List<V> value) {
/* 161 */     return this.targetMap.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<V> remove(Object key) {
/* 167 */     return this.targetMap.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends List<V>> map) {
/* 172 */     this.targetMap.putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 177 */     this.targetMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 182 */     return this.targetMap.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<List<V>> values() {
/* 187 */     return this.targetMap.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, List<V>>> entrySet() {
/* 192 */     return this.targetMap.entrySet();
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
/*     */   public LinkedMultiValueMap<K, V> deepCopy() {
/* 206 */     LinkedMultiValueMap<K, V> copy = new LinkedMultiValueMap(this.targetMap.size());
/* 207 */     this.targetMap.forEach((key, value) -> copy.put(key, new LinkedList(value)));
/* 208 */     return copy;
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
/*     */   public LinkedMultiValueMap<K, V> clone() {
/* 224 */     return new LinkedMultiValueMap(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 229 */     return this.targetMap.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 234 */     return this.targetMap.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 239 */     return this.targetMap.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/LinkedMultiValueMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */