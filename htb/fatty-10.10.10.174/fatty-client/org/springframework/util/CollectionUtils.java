/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ public abstract class CollectionUtils
/*     */ {
/*     */   public static boolean isEmpty(@Nullable Collection<?> collection) {
/*  54 */     return (collection == null || collection.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(@Nullable Map<?, ?> map) {
/*  64 */     return (map == null || map.isEmpty());
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
/*     */   public static List arrayToList(@Nullable Object source) {
/*  81 */     return Arrays.asList(ObjectUtils.toObjectArray(source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> void mergeArrayIntoCollection(@Nullable Object array, Collection<E> collection) {
/*  91 */     Object[] arr = ObjectUtils.toObjectArray(array);
/*  92 */     for (Object elem : arr) {
/*  93 */       collection.add((E)elem);
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
/*     */   
/*     */   public static <K, V> void mergePropertiesIntoMap(@Nullable Properties props, Map<K, V> map) {
/* 107 */     if (props != null) {
/* 108 */       for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
/* 109 */         String key = (String)en.nextElement();
/* 110 */         Object value = props.get(key);
/* 111 */         if (value == null)
/*     */         {
/* 113 */           value = props.getProperty(key);
/*     */         }
/* 115 */         map.put((K)key, (V)value);
/*     */       } 
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
/*     */   public static boolean contains(@Nullable Iterator<?> iterator, Object element) {
/* 128 */     if (iterator != null) {
/* 129 */       while (iterator.hasNext()) {
/* 130 */         Object candidate = iterator.next();
/* 131 */         if (ObjectUtils.nullSafeEquals(candidate, element)) {
/* 132 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(@Nullable Enumeration<?> enumeration, Object element) {
/* 146 */     if (enumeration != null) {
/* 147 */       while (enumeration.hasMoreElements()) {
/* 148 */         Object candidate = enumeration.nextElement();
/* 149 */         if (ObjectUtils.nullSafeEquals(candidate, element)) {
/* 150 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 154 */     return false;
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
/*     */   public static boolean containsInstance(@Nullable Collection<?> collection, Object element) {
/* 166 */     if (collection != null) {
/* 167 */       for (Object candidate : collection) {
/* 168 */         if (candidate == element) {
/* 169 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
/* 184 */     if (isEmpty(source) || isEmpty(candidates)) {
/* 185 */       return false;
/*     */     }
/* 187 */     for (Object candidate : candidates) {
/* 188 */       if (source.contains(candidate)) {
/* 189 */         return true;
/*     */       }
/*     */     } 
/* 192 */     return false;
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
/*     */   @Nullable
/*     */   public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
/* 207 */     if (isEmpty(source) || isEmpty(candidates)) {
/* 208 */       return null;
/*     */     }
/* 210 */     for (E candidate : candidates) {
/* 211 */       if (source.contains(candidate)) {
/* 212 */         return candidate;
/*     */       }
/*     */     } 
/* 215 */     return null;
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
/*     */   public static <T> T findValueOfType(Collection<?> collection, @Nullable Class<T> type) {
/* 228 */     if (isEmpty(collection)) {
/* 229 */       return null;
/*     */     }
/* 231 */     T value = null;
/* 232 */     for (Object element : collection) {
/* 233 */       if (type == null || type.isInstance(element)) {
/* 234 */         if (value != null)
/*     */         {
/* 236 */           return null;
/*     */         }
/* 238 */         value = (T)element;
/*     */       } 
/*     */     } 
/* 241 */     return value;
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
/*     */   public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
/* 255 */     if (isEmpty(collection) || ObjectUtils.isEmpty((Object[])types)) {
/* 256 */       return null;
/*     */     }
/* 258 */     for (Class<?> type : types) {
/* 259 */       Object value = findValueOfType(collection, type);
/* 260 */       if (value != null) {
/* 261 */         return value;
/*     */       }
/*     */     } 
/* 264 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasUniqueObject(Collection<?> collection) {
/* 274 */     if (isEmpty(collection)) {
/* 275 */       return false;
/*     */     }
/* 277 */     boolean hasCandidate = false;
/* 278 */     Object candidate = null;
/* 279 */     for (Object elem : collection) {
/* 280 */       if (!hasCandidate) {
/* 281 */         hasCandidate = true;
/* 282 */         candidate = elem; continue;
/*     */       } 
/* 284 */       if (candidate != elem) {
/* 285 */         return false;
/*     */       }
/*     */     } 
/* 288 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Class<?> findCommonElementType(Collection<?> collection) {
/* 299 */     if (isEmpty(collection)) {
/* 300 */       return null;
/*     */     }
/* 302 */     Class<?> candidate = null;
/* 303 */     for (Object val : collection) {
/* 304 */       if (val != null) {
/* 305 */         if (candidate == null) {
/* 306 */           candidate = val.getClass(); continue;
/*     */         } 
/* 308 */         if (candidate != val.getClass()) {
/* 309 */           return null;
/*     */         }
/*     */       } 
/*     */     } 
/* 313 */     return candidate;
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
/*     */   @Nullable
/*     */   public static <T> T lastElement(@Nullable Set<T> set) {
/* 328 */     if (isEmpty(set)) {
/* 329 */       return null;
/*     */     }
/* 331 */     if (set instanceof SortedSet) {
/* 332 */       return ((SortedSet<T>)set).last();
/*     */     }
/*     */ 
/*     */     
/* 336 */     Iterator<T> it = set.iterator();
/* 337 */     T last = null;
/* 338 */     while (it.hasNext()) {
/* 339 */       last = it.next();
/*     */     }
/* 341 */     return last;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static <T> T lastElement(@Nullable List<T> list) {
/* 352 */     if (isEmpty(list)) {
/* 353 */       return null;
/*     */     }
/* 355 */     return list.get(list.size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
/* 364 */     ArrayList<A> elements = new ArrayList<>();
/* 365 */     while (enumeration.hasMoreElements()) {
/* 366 */       elements.add((A)enumeration.nextElement());
/*     */     }
/* 368 */     return elements.toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Iterator<E> toIterator(@Nullable Enumeration<E> enumeration) {
/* 377 */     return (enumeration != null) ? new EnumerationIterator<>(enumeration) : Collections.<E>emptyIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, List<V>> map) {
/* 387 */     return new MultiValueMapAdapter<>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> MultiValueMap<K, V> unmodifiableMultiValueMap(MultiValueMap<? extends K, ? extends V> map) {
/* 398 */     Assert.notNull(map, "'map' must not be null");
/* 399 */     Map<K, List<V>> result = new LinkedHashMap<>(map.size());
/* 400 */     map.forEach((key, value) -> {
/*     */           List<? extends V> values = Collections.unmodifiableList(value);
/*     */           result.put(key, values);
/*     */         });
/* 404 */     Map<K, List<V>> unmodifiableMap = Collections.unmodifiableMap(result);
/* 405 */     return toMultiValueMap(unmodifiableMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class EnumerationIterator<E>
/*     */     implements Iterator<E>
/*     */   {
/*     */     private final Enumeration<E> enumeration;
/*     */ 
/*     */     
/*     */     public EnumerationIterator(Enumeration<E> enumeration) {
/* 417 */       this.enumeration = enumeration;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 422 */       return this.enumeration.hasMoreElements();
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 427 */       return this.enumeration.nextElement();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() throws UnsupportedOperationException {
/* 432 */       throw new UnsupportedOperationException("Not supported");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MultiValueMapAdapter<K, V>
/*     */     implements MultiValueMap<K, V>, Serializable
/*     */   {
/*     */     private final Map<K, List<V>> map;
/*     */ 
/*     */ 
/*     */     
/*     */     public MultiValueMapAdapter(Map<K, List<V>> map) {
/* 446 */       Assert.notNull(map, "'map' must not be null");
/* 447 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public V getFirst(K key) {
/* 453 */       List<V> values = this.map.get(key);
/* 454 */       return (values != null) ? values.get(0) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(K key, @Nullable V value) {
/* 459 */       List<V> values = this.map.computeIfAbsent(key, k -> new LinkedList());
/* 460 */       values.add(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addAll(K key, List<? extends V> values) {
/* 465 */       List<V> currentValues = this.map.computeIfAbsent(key, k -> new LinkedList());
/* 466 */       currentValues.addAll(values);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addAll(MultiValueMap<K, V> values) {
/* 471 */       for (Map.Entry<K, List<V>> entry : values.entrySet()) {
/* 472 */         addAll(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(K key, @Nullable V value) {
/* 478 */       List<V> values = new LinkedList<>();
/* 479 */       values.add(value);
/* 480 */       this.map.put(key, values);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setAll(Map<K, V> values) {
/* 485 */       values.forEach(this::set);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<K, V> toSingleValueMap() {
/* 490 */       LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<>(this.map.size());
/* 491 */       this.map.forEach((key, value) -> singleValueMap.put(key, value.get(0)));
/* 492 */       return singleValueMap;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 497 */       return this.map.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 502 */       return this.map.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 507 */       return this.map.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsValue(Object value) {
/* 512 */       return this.map.containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> get(Object key) {
/* 517 */       return this.map.get(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> put(K key, List<V> value) {
/* 522 */       return this.map.put(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> remove(Object key) {
/* 527 */       return this.map.remove(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Map<? extends K, ? extends List<V>> map) {
/* 532 */       this.map.putAll(map);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 537 */       this.map.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> keySet() {
/* 542 */       return this.map.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<List<V>> values() {
/* 547 */       return this.map.values();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Map.Entry<K, List<V>>> entrySet() {
/* 552 */       return this.map.entrySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 557 */       if (this == other) {
/* 558 */         return true;
/*     */       }
/* 560 */       return this.map.equals(other);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 565 */       return this.map.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 570 */       return this.map.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/CollectionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */