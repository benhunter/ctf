/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
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
/*     */ public class LinkedCaseInsensitiveMap<V>
/*     */   implements Map<String, V>, Serializable, Cloneable
/*     */ {
/*     */   private final LinkedHashMap<String, V> targetMap;
/*     */   private final HashMap<String, String> caseInsensitiveKeys;
/*     */   private final Locale locale;
/*     */   
/*     */   public LinkedCaseInsensitiveMap() {
/*  59 */     this((Locale)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedCaseInsensitiveMap(@Nullable Locale locale) {
/*  69 */     this(16, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedCaseInsensitiveMap(int initialCapacity) {
/*  80 */     this(initialCapacity, null);
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
/*     */   public LinkedCaseInsensitiveMap(int initialCapacity, @Nullable Locale locale) {
/*  92 */     this.targetMap = new LinkedHashMap<String, V>(initialCapacity)
/*     */       {
/*     */         public boolean containsKey(Object key) {
/*  95 */           return LinkedCaseInsensitiveMap.this.containsKey(key);
/*     */         }
/*     */         
/*     */         protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
/*  99 */           boolean doRemove = LinkedCaseInsensitiveMap.this.removeEldestEntry(eldest);
/* 100 */           if (doRemove) {
/* 101 */             LinkedCaseInsensitiveMap.this.caseInsensitiveKeys.remove(LinkedCaseInsensitiveMap.this.convertKey(eldest.getKey()));
/*     */           }
/* 103 */           return doRemove;
/*     */         }
/*     */       };
/* 106 */     this.caseInsensitiveKeys = new HashMap<>(initialCapacity);
/* 107 */     this.locale = (locale != null) ? locale : Locale.getDefault();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LinkedCaseInsensitiveMap(LinkedCaseInsensitiveMap<V> other) {
/* 115 */     this.targetMap = (LinkedHashMap<String, V>)other.targetMap.clone();
/* 116 */     this.caseInsensitiveKeys = (HashMap<String, String>)other.caseInsensitiveKeys.clone();
/* 117 */     this.locale = other.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 125 */     return this.targetMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 130 */     return this.targetMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 135 */     return (key instanceof String && this.caseInsensitiveKeys.containsKey(convertKey((String)key)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 140 */     return this.targetMap.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(Object key) {
/* 146 */     if (key instanceof String) {
/* 147 */       String caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String)key));
/* 148 */       if (caseInsensitiveKey != null) {
/* 149 */         return this.targetMap.get(caseInsensitiveKey);
/*     */       }
/*     */     } 
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V getOrDefault(Object key, V defaultValue) {
/* 158 */     if (key instanceof String) {
/* 159 */       String caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String)key));
/* 160 */       if (caseInsensitiveKey != null) {
/* 161 */         return this.targetMap.get(caseInsensitiveKey);
/*     */       }
/*     */     } 
/* 164 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V put(String key, @Nullable V value) {
/* 170 */     String oldKey = this.caseInsensitiveKeys.put(convertKey(key), key);
/* 171 */     V oldKeyValue = null;
/* 172 */     if (oldKey != null && !oldKey.equals(key)) {
/* 173 */       oldKeyValue = this.targetMap.remove(oldKey);
/*     */     }
/* 175 */     V oldValue = this.targetMap.put(key, value);
/* 176 */     return (oldKeyValue != null) ? oldKeyValue : oldValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends V> map) {
/* 181 */     if (map.isEmpty()) {
/*     */       return;
/*     */     }
/* 184 */     map.forEach(this::put);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V putIfAbsent(String key, @Nullable V value) {
/* 190 */     String oldKey = this.caseInsensitiveKeys.putIfAbsent(convertKey(key), key);
/* 191 */     if (oldKey != null) {
/* 192 */       return this.targetMap.get(oldKey);
/*     */     }
/* 194 */     return this.targetMap.putIfAbsent(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V computeIfAbsent(String key, Function<? super String, ? extends V> mappingFunction) {
/* 200 */     String oldKey = this.caseInsensitiveKeys.putIfAbsent(convertKey(key), key);
/* 201 */     if (oldKey != null) {
/* 202 */       return this.targetMap.get(oldKey);
/*     */     }
/* 204 */     return this.targetMap.computeIfAbsent(key, mappingFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V remove(Object key) {
/* 210 */     if (key instanceof String) {
/* 211 */       String caseInsensitiveKey = this.caseInsensitiveKeys.remove(convertKey((String)key));
/* 212 */       if (caseInsensitiveKey != null) {
/* 213 */         return this.targetMap.remove(caseInsensitiveKey);
/*     */       }
/*     */     } 
/* 216 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 221 */     this.caseInsensitiveKeys.clear();
/* 222 */     this.targetMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/* 227 */     return this.targetMap.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 232 */     return this.targetMap.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, V>> entrySet() {
/* 237 */     return this.targetMap.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkedCaseInsensitiveMap<V> clone() {
/* 242 */     return new LinkedCaseInsensitiveMap(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 247 */     return this.targetMap.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 252 */     return this.targetMap.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 257 */     return this.targetMap.toString();
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
/*     */   public Locale getLocale() {
/* 271 */     return this.locale;
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
/*     */   protected String convertKey(String key) {
/* 283 */     return key.toLowerCase(getLocale());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
/* 293 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/LinkedCaseInsensitiveMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */