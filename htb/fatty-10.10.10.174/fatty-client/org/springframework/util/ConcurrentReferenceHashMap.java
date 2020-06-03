/*      */ package org.springframework.util;
/*      */ 
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import org.springframework.lang.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ConcurrentReferenceHashMap<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>
/*      */ {
/*      */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   private static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*      */   private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
/*   70 */   private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAXIMUM_CONCURRENCY_LEVEL = 65536;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAXIMUM_SEGMENT_SIZE = 1073741824;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Segment[] segments;
/*      */ 
/*      */ 
/*      */   
/*      */   private final float loadFactor;
/*      */ 
/*      */ 
/*      */   
/*      */   private final ReferenceType referenceType;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int shift;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private volatile Set<Map.Entry<K, V>> entrySet;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap() {
/*  108 */     this(16, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity) {
/*  116 */     this(initialCapacity, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
/*  126 */     this(initialCapacity, loadFactor, 16, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel) {
/*  136 */     this(initialCapacity, 0.75F, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType referenceType) {
/*  145 */     this(initialCapacity, 0.75F, 16, referenceType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
/*  157 */     this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {
/*  173 */     Assert.isTrue((initialCapacity >= 0), "Initial capacity must not be negative");
/*  174 */     Assert.isTrue((loadFactor > 0.0F), "Load factor must be positive");
/*  175 */     Assert.isTrue((concurrencyLevel > 0), "Concurrency level must be positive");
/*  176 */     Assert.notNull(referenceType, "Reference type must not be null");
/*  177 */     this.loadFactor = loadFactor;
/*  178 */     this.shift = calculateShift(concurrencyLevel, 65536);
/*  179 */     int size = 1 << this.shift;
/*  180 */     this.referenceType = referenceType;
/*  181 */     int roundedUpSegmentCapacity = (int)(((initialCapacity + size) - 1L) / size);
/*  182 */     int initialSize = 1 << calculateShift(roundedUpSegmentCapacity, 1073741824);
/*  183 */     Segment[] arrayOfSegment = (Segment[])Array.newInstance(Segment.class, size);
/*  184 */     int resizeThreshold = (int)(initialSize * getLoadFactor());
/*  185 */     for (int i = 0; i < arrayOfSegment.length; i++) {
/*  186 */       arrayOfSegment[i] = new Segment(initialSize, resizeThreshold);
/*      */     }
/*  188 */     this.segments = (Segment[])arrayOfSegment;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final float getLoadFactor() {
/*  193 */     return this.loadFactor;
/*      */   }
/*      */   
/*      */   protected final int getSegmentsSize() {
/*  197 */     return this.segments.length;
/*      */   }
/*      */   
/*      */   protected final Segment getSegment(int index) {
/*  201 */     return this.segments[index];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ReferenceManager createReferenceManager() {
/*  210 */     return new ReferenceManager();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getHash(@Nullable Object o) {
/*  221 */     int hash = (o != null) ? o.hashCode() : 0;
/*  222 */     hash += hash << 15 ^ 0xFFFFCD7D;
/*  223 */     hash ^= hash >>> 10;
/*  224 */     hash += hash << 3;
/*  225 */     hash ^= hash >>> 6;
/*  226 */     hash += (hash << 2) + (hash << 14);
/*  227 */     hash ^= hash >>> 16;
/*  228 */     return hash;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V get(@Nullable Object key) {
/*  234 */     Entry<K, V> entry = getEntryIfAvailable(key);
/*  235 */     return (entry != null) ? entry.getValue() : null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
/*  241 */     Entry<K, V> entry = getEntryIfAvailable(key);
/*  242 */     return (entry != null) ? entry.getValue() : defaultValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(@Nullable Object key) {
/*  247 */     Entry<K, V> entry = getEntryIfAvailable(key);
/*  248 */     return (entry != null && ObjectUtils.nullSafeEquals(entry.getKey(), key));
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Entry<K, V> getEntryIfAvailable(@Nullable Object key) {
/*  253 */     Reference<K, V> ref = getReference(key, Restructure.WHEN_NECESSARY);
/*  254 */     return (ref != null) ? ref.get() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected final Reference<K, V> getReference(@Nullable Object key, Restructure restructure) {
/*  266 */     int hash = getHash(key);
/*  267 */     return getSegmentForHash(hash).getReference(key, hash, restructure);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V put(@Nullable K key, @Nullable V value) {
/*  273 */     return put(key, value, true);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V putIfAbsent(@Nullable K key, @Nullable V value) {
/*  279 */     return put(key, value, false);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private V put(@Nullable K key, @Nullable final V value, final boolean overwriteExisting) {
/*  284 */     return doTask(key, new Task<V>(new TaskOption[] { TaskOption.RESTRUCTURE_BEFORE, TaskOption.RESIZE })
/*      */         {
/*      */           @Nullable
/*      */           protected V execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry, @Nullable ConcurrentReferenceHashMap<K, V>.Entries entries) {
/*  288 */             if (entry != null) {
/*  289 */               V oldValue = entry.getValue();
/*  290 */               if (overwriteExisting) {
/*  291 */                 entry.setValue((V)value);
/*      */               }
/*  293 */               return oldValue;
/*      */             } 
/*  295 */             Assert.state((entries != null), "No entries segment");
/*  296 */             entries.add((V)value);
/*  297 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V remove(Object key) {
/*  305 */     return doTask(key, new Task<V>(new TaskOption[] { TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           @Nullable
/*      */           protected V execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  309 */             if (entry != null) {
/*  310 */               if (ref != null) {
/*  311 */                 ref.release();
/*      */               }
/*  313 */               return entry.value;
/*      */             } 
/*  315 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean remove(Object key, final Object value) {
/*  322 */     Boolean result = doTask(key, new Task<Boolean>(new TaskOption[] { TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           protected Boolean execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  325 */             if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), value)) {
/*  326 */               if (ref != null) {
/*  327 */                 ref.release();
/*      */               }
/*  329 */               return Boolean.valueOf(true);
/*      */             } 
/*  331 */             return Boolean.valueOf(false);
/*      */           }
/*      */         });
/*  334 */     return (result == Boolean.TRUE);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K key, final V oldValue, final V newValue) {
/*  339 */     Boolean result = doTask(key, new Task<Boolean>(new TaskOption[] { TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           protected Boolean execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  342 */             if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), oldValue)) {
/*  343 */               entry.setValue((V)newValue);
/*  344 */               return Boolean.valueOf(true);
/*      */             } 
/*  346 */             return Boolean.valueOf(false);
/*      */           }
/*      */         });
/*  349 */     return (result == Boolean.TRUE);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V replace(K key, final V value) {
/*  355 */     return doTask(key, new Task<V>(new TaskOption[] { TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           @Nullable
/*      */           protected V execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  359 */             if (entry != null) {
/*  360 */               V oldValue = entry.getValue();
/*  361 */               entry.setValue((V)value);
/*  362 */               return oldValue;
/*      */             } 
/*  364 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/*  371 */     for (Segment segment : this.segments) {
/*  372 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void purgeUnreferencedEntries() {
/*  383 */     for (Segment segment : this.segments) {
/*  384 */       segment.restructureIfNecessary(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  391 */     int size = 0;
/*  392 */     for (Segment segment : this.segments) {
/*  393 */       size += segment.getCount();
/*      */     }
/*  395 */     return size;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  400 */     for (Segment segment : this.segments) {
/*  401 */       if (segment.getCount() > 0) {
/*  402 */         return false;
/*      */       }
/*      */     } 
/*  405 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/*  410 */     Set<Map.Entry<K, V>> entrySet = this.entrySet;
/*  411 */     if (entrySet == null) {
/*  412 */       entrySet = new EntrySet();
/*  413 */       this.entrySet = entrySet;
/*      */     } 
/*  415 */     return entrySet;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private <T> T doTask(@Nullable Object key, Task<T> task) {
/*  420 */     int hash = getHash(key);
/*  421 */     return getSegmentForHash(hash).doTask(hash, key, task);
/*      */   }
/*      */   
/*      */   private Segment getSegmentForHash(int hash) {
/*  425 */     return this.segments[hash >>> 32 - this.shift & this.segments.length - 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static int calculateShift(int minimumValue, int maximumValue) {
/*  436 */     int shift = 0;
/*  437 */     int value = 1;
/*  438 */     while (value < minimumValue && value < maximumValue) {
/*  439 */       value <<= 1;
/*  440 */       shift++;
/*      */     } 
/*  442 */     return shift;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum ReferenceType
/*      */   {
/*  452 */     SOFT,
/*      */ 
/*      */     
/*  455 */     WEAK;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final class Segment
/*      */     extends ReentrantLock
/*      */   {
/*      */     private final ConcurrentReferenceHashMap<K, V>.ReferenceManager referenceManager;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int initialSize;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private volatile ConcurrentReferenceHashMap.Reference<K, V>[] references;
/*      */ 
/*      */ 
/*      */     
/*  479 */     private volatile int count = 0;
/*      */ 
/*      */ 
/*      */     
/*      */     private int resizeThreshold;
/*      */ 
/*      */ 
/*      */     
/*      */     public Segment(int initialSize, int resizeThreshold) {
/*  488 */       this.referenceManager = ConcurrentReferenceHashMap.this.createReferenceManager();
/*  489 */       this.initialSize = initialSize;
/*  490 */       this.references = createReferenceArray(initialSize);
/*  491 */       this.resizeThreshold = resizeThreshold;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> getReference(@Nullable Object key, int hash, ConcurrentReferenceHashMap.Restructure restructure) {
/*  496 */       if (restructure == ConcurrentReferenceHashMap.Restructure.WHEN_NECESSARY) {
/*  497 */         restructureIfNecessary(false);
/*      */       }
/*  499 */       if (this.count == 0) {
/*  500 */         return null;
/*      */       }
/*      */       
/*  503 */       ConcurrentReferenceHashMap.Reference<K, V>[] references = this.references;
/*  504 */       int index = getIndex(hash, references);
/*  505 */       ConcurrentReferenceHashMap.Reference<K, V> head = references[index];
/*  506 */       return findInChain(head, key, hash);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public <T> T doTask(final int hash, @Nullable final Object key, ConcurrentReferenceHashMap<K, V>.Task<T> task) {
/*  519 */       boolean resize = task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESIZE);
/*  520 */       if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_BEFORE)) {
/*  521 */         restructureIfNecessary(resize);
/*      */       }
/*  523 */       if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.SKIP_IF_EMPTY) && this.count == 0) {
/*  524 */         return task.execute((ConcurrentReferenceHashMap.Reference<K, V>)null, (ConcurrentReferenceHashMap.Entry<K, V>)null, (ConcurrentReferenceHashMap<K, V>.Entries)null);
/*      */       }
/*  526 */       lock();
/*      */       try {
/*  528 */         final int index = getIndex(hash, this.references);
/*  529 */         final ConcurrentReferenceHashMap.Reference<K, V> head = this.references[index];
/*  530 */         ConcurrentReferenceHashMap.Reference<K, V> ref = findInChain(head, key, hash);
/*  531 */         ConcurrentReferenceHashMap.Entry<K, V> entry = (ref != null) ? ref.get() : null;
/*  532 */         ConcurrentReferenceHashMap<K, V>.Entries entries = new ConcurrentReferenceHashMap<K, V>.Entries()
/*      */           {
/*      */             public void add(@Nullable V value)
/*      */             {
/*  536 */               ConcurrentReferenceHashMap.Entry<K, V> newEntry = new ConcurrentReferenceHashMap.Entry<>((K)key, value);
/*  537 */               ConcurrentReferenceHashMap.Reference<K, V> newReference = ConcurrentReferenceHashMap.Segment.this.referenceManager.createReference(newEntry, hash, head);
/*  538 */               ConcurrentReferenceHashMap.Segment.this.references[index] = newReference;
/*  539 */               ConcurrentReferenceHashMap.Segment.this.count++;
/*      */             }
/*      */           };
/*  542 */         return task.execute(ref, entry, entries);
/*      */       } finally {
/*      */         
/*  545 */         unlock();
/*  546 */         if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_AFTER)) {
/*  547 */           restructureIfNecessary(resize);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  556 */       if (this.count == 0) {
/*      */         return;
/*      */       }
/*  559 */       lock();
/*      */       try {
/*  561 */         this.references = createReferenceArray(this.initialSize);
/*  562 */         this.resizeThreshold = (int)(this.references.length * ConcurrentReferenceHashMap.this.getLoadFactor());
/*  563 */         this.count = 0;
/*      */       } finally {
/*      */         
/*  566 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final void restructureIfNecessary(boolean allowResize) {
/*  577 */       int currCount = this.count;
/*  578 */       boolean needsResize = (currCount > 0 && currCount >= this.resizeThreshold);
/*  579 */       ConcurrentReferenceHashMap.Reference<K, V> ref = this.referenceManager.pollForPurge();
/*  580 */       if (ref != null || (needsResize && allowResize)) {
/*  581 */         lock();
/*      */         try {
/*  583 */           int countAfterRestructure = this.count;
/*  584 */           Set<ConcurrentReferenceHashMap.Reference<K, V>> toPurge = Collections.emptySet();
/*  585 */           if (ref != null) {
/*  586 */             toPurge = new HashSet<>();
/*  587 */             while (ref != null) {
/*  588 */               toPurge.add(ref);
/*  589 */               ref = this.referenceManager.pollForPurge();
/*      */             } 
/*      */           } 
/*  592 */           countAfterRestructure -= toPurge.size();
/*      */ 
/*      */ 
/*      */           
/*  596 */           needsResize = (countAfterRestructure > 0 && countAfterRestructure >= this.resizeThreshold);
/*  597 */           boolean resizing = false;
/*  598 */           int restructureSize = this.references.length;
/*  599 */           if (allowResize && needsResize && restructureSize < 1073741824) {
/*  600 */             restructureSize <<= 1;
/*  601 */             resizing = true;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  606 */           ConcurrentReferenceHashMap.Reference<K, V>[] restructured = resizing ? createReferenceArray(restructureSize) : this.references;
/*      */ 
/*      */           
/*  609 */           for (int i = 0; i < this.references.length; i++) {
/*  610 */             ref = this.references[i];
/*  611 */             if (!resizing) {
/*  612 */               restructured[i] = null;
/*      */             }
/*  614 */             while (ref != null) {
/*  615 */               if (!toPurge.contains(ref)) {
/*  616 */                 ConcurrentReferenceHashMap.Entry<K, V> entry = ref.get();
/*  617 */                 if (entry != null) {
/*  618 */                   int index = getIndex(ref.getHash(), restructured);
/*  619 */                   restructured[index] = this.referenceManager.createReference(entry, ref
/*  620 */                       .getHash(), restructured[index]);
/*      */                 } 
/*      */               } 
/*  623 */               ref = ref.getNext();
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/*  628 */           if (resizing) {
/*  629 */             this.references = restructured;
/*  630 */             this.resizeThreshold = (int)(this.references.length * ConcurrentReferenceHashMap.this.getLoadFactor());
/*      */           } 
/*  632 */           this.count = Math.max(countAfterRestructure, 0);
/*      */         } finally {
/*      */           
/*  635 */           unlock();
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     private ConcurrentReferenceHashMap.Reference<K, V> findInChain(ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable Object key, int hash) {
/*  642 */       ConcurrentReferenceHashMap.Reference<K, V> currRef = ref;
/*  643 */       while (currRef != null) {
/*  644 */         if (currRef.getHash() == hash) {
/*  645 */           ConcurrentReferenceHashMap.Entry<K, V> entry = currRef.get();
/*  646 */           if (entry != null) {
/*  647 */             K entryKey = entry.getKey();
/*  648 */             if (ObjectUtils.nullSafeEquals(entryKey, key)) {
/*  649 */               return currRef;
/*      */             }
/*      */           } 
/*      */         } 
/*  653 */         currRef = currRef.getNext();
/*      */       } 
/*  655 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     private ConcurrentReferenceHashMap.Reference<K, V>[] createReferenceArray(int size) {
/*  660 */       return (ConcurrentReferenceHashMap.Reference<K, V>[])new ConcurrentReferenceHashMap.Reference[size];
/*      */     }
/*      */     
/*      */     private int getIndex(int hash, ConcurrentReferenceHashMap.Reference<K, V>[] references) {
/*  664 */       return hash & references.length - 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int getSize() {
/*  671 */       return this.references.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int getCount() {
/*  678 */       return this.count;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static interface Reference<K, V>
/*      */   {
/*      */     @Nullable
/*      */     ConcurrentReferenceHashMap.Entry<K, V> get();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int getHash();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     Reference<K, V> getNext();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void release();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final class Entry<K, V>
/*      */     implements Map.Entry<K, V>
/*      */   {
/*      */     @Nullable
/*      */     private final K key;
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private volatile V value;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Entry(@Nullable K key, @Nullable V value) {
/*  730 */       this.key = key;
/*  731 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public K getKey() {
/*  737 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V getValue() {
/*  743 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V setValue(@Nullable V value) {
/*  749 */       V previous = this.value;
/*  750 */       this.value = value;
/*  751 */       return previous;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  756 */       return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final boolean equals(Object other) {
/*  762 */       if (this == other) {
/*  763 */         return true;
/*      */       }
/*  765 */       if (!(other instanceof Map.Entry)) {
/*  766 */         return false;
/*      */       }
/*  768 */       Map.Entry otherEntry = (Map.Entry)other;
/*  769 */       return (ObjectUtils.nullSafeEquals(getKey(), otherEntry.getKey()) && 
/*  770 */         ObjectUtils.nullSafeEquals(getValue(), otherEntry.getValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public final int hashCode() {
/*  775 */       return ObjectUtils.nullSafeHashCode(this.key) ^ ObjectUtils.nullSafeHashCode(this.value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class Task<T>
/*      */   {
/*      */     private final EnumSet<ConcurrentReferenceHashMap.TaskOption> options;
/*      */ 
/*      */ 
/*      */     
/*      */     public Task(ConcurrentReferenceHashMap.TaskOption... options) {
/*  788 */       this.options = (options.length == 0) ? EnumSet.<ConcurrentReferenceHashMap.TaskOption>noneOf(ConcurrentReferenceHashMap.TaskOption.class) : EnumSet.<ConcurrentReferenceHashMap.TaskOption>of(options[0], options);
/*      */     }
/*      */     
/*      */     public boolean hasOption(ConcurrentReferenceHashMap.TaskOption option) {
/*  792 */       return this.options.contains(option);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     protected T execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry, @Nullable ConcurrentReferenceHashMap<K, V>.Entries entries) {
/*  805 */       return execute(ref, entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     protected T execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  817 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private enum TaskOption
/*      */   {
/*  827 */     RESTRUCTURE_BEFORE, RESTRUCTURE_AFTER, SKIP_IF_EMPTY, RESIZE;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class Entries
/*      */   {
/*      */     private Entries() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract void add(@Nullable V param1V);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class EntrySet
/*      */     extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     private EntrySet() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/*  851 */       return new ConcurrentReferenceHashMap.EntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/*  856 */       if (o instanceof Map.Entry) {
/*  857 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  858 */         ConcurrentReferenceHashMap.Reference<K, V> ref = ConcurrentReferenceHashMap.this.getReference(entry.getKey(), ConcurrentReferenceHashMap.Restructure.NEVER);
/*  859 */         ConcurrentReferenceHashMap.Entry<K, V> otherEntry = (ref != null) ? ref.get() : null;
/*  860 */         if (otherEntry != null) {
/*  861 */           return ObjectUtils.nullSafeEquals(otherEntry.getValue(), otherEntry.getValue());
/*      */         }
/*      */       } 
/*  864 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  869 */       if (o instanceof Map.Entry) {
/*  870 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  871 */         return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
/*      */       } 
/*  873 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  878 */       return ConcurrentReferenceHashMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  883 */       ConcurrentReferenceHashMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class EntryIterator
/*      */     implements Iterator<Map.Entry<K, V>>
/*      */   {
/*      */     private int segmentIndex;
/*      */     
/*      */     private int referenceIndex;
/*      */     
/*      */     @Nullable
/*      */     private ConcurrentReferenceHashMap.Reference<K, V>[] references;
/*      */     
/*      */     @Nullable
/*      */     private ConcurrentReferenceHashMap.Reference<K, V> reference;
/*      */     
/*      */     @Nullable
/*      */     private ConcurrentReferenceHashMap.Entry<K, V> next;
/*      */     
/*      */     @Nullable
/*      */     private ConcurrentReferenceHashMap.Entry<K, V> last;
/*      */ 
/*      */     
/*      */     public EntryIterator() {
/*  910 */       moveToNextSegment();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  915 */       getNextIfNecessary();
/*  916 */       return (this.next != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public ConcurrentReferenceHashMap.Entry<K, V> next() {
/*  921 */       getNextIfNecessary();
/*  922 */       if (this.next == null) {
/*  923 */         throw new NoSuchElementException();
/*      */       }
/*  925 */       this.last = this.next;
/*  926 */       this.next = null;
/*  927 */       return this.last;
/*      */     }
/*      */     
/*      */     private void getNextIfNecessary() {
/*  931 */       while (this.next == null) {
/*  932 */         moveToNextReference();
/*  933 */         if (this.reference == null) {
/*      */           return;
/*      */         }
/*  936 */         this.next = this.reference.get();
/*      */       } 
/*      */     }
/*      */     
/*      */     private void moveToNextReference() {
/*  941 */       if (this.reference != null) {
/*  942 */         this.reference = this.reference.getNext();
/*      */       }
/*  944 */       while (this.reference == null && this.references != null) {
/*  945 */         if (this.referenceIndex >= this.references.length) {
/*  946 */           moveToNextSegment();
/*  947 */           this.referenceIndex = 0;
/*      */           continue;
/*      */         } 
/*  950 */         this.reference = this.references[this.referenceIndex];
/*  951 */         this.referenceIndex++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void moveToNextSegment() {
/*  957 */       this.reference = null;
/*  958 */       this.references = null;
/*  959 */       if (this.segmentIndex < ConcurrentReferenceHashMap.this.segments.length) {
/*  960 */         this.references = (ConcurrentReferenceHashMap.this.segments[this.segmentIndex]).references;
/*  961 */         this.segmentIndex++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/*  967 */       Assert.state((this.last != null), "No element to remove");
/*  968 */       ConcurrentReferenceHashMap.this.remove(this.last.getKey());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected enum Restructure
/*      */   {
/*  978 */     WHEN_NECESSARY, NEVER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected class ReferenceManager
/*      */   {
/*  988 */     private final ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> createReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, @Nullable ConcurrentReferenceHashMap.Reference<K, V> next) {
/*  998 */       if (ConcurrentReferenceHashMap.this.referenceType == ConcurrentReferenceHashMap.ReferenceType.WEAK) {
/*  999 */         return new ConcurrentReferenceHashMap.WeakEntryReference<>(entry, hash, next, this.queue);
/*      */       }
/* 1001 */       return new ConcurrentReferenceHashMap.SoftEntryReference<>(entry, hash, next, this.queue);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> pollForPurge() {
/* 1014 */       return (ConcurrentReferenceHashMap.Reference)this.queue.poll();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SoftEntryReference<K, V>
/*      */     extends SoftReference<Entry<K, V>>
/*      */     implements Reference<K, V>
/*      */   {
/*      */     private final int hash;
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private final ConcurrentReferenceHashMap.Reference<K, V> nextReference;
/*      */ 
/*      */     
/*      */     public SoftEntryReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, @Nullable ConcurrentReferenceHashMap.Reference<K, V> next, ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue) {
/* 1032 */       super(entry, queue);
/* 1033 */       this.hash = hash;
/* 1034 */       this.nextReference = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/* 1039 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> getNext() {
/* 1045 */       return this.nextReference;
/*      */     }
/*      */ 
/*      */     
/*      */     public void release() {
/* 1050 */       enqueue();
/* 1051 */       clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class WeakEntryReference<K, V>
/*      */     extends WeakReference<Entry<K, V>>
/*      */     implements Reference<K, V>
/*      */   {
/*      */     private final int hash;
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private final ConcurrentReferenceHashMap.Reference<K, V> nextReference;
/*      */ 
/*      */     
/*      */     public WeakEntryReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, @Nullable ConcurrentReferenceHashMap.Reference<K, V> next, ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue) {
/* 1069 */       super(entry, queue);
/* 1070 */       this.hash = hash;
/* 1071 */       this.nextReference = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/* 1076 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> getNext() {
/* 1082 */       return this.nextReference;
/*      */     }
/*      */ 
/*      */     
/*      */     public void release() {
/* 1087 */       enqueue();
/* 1088 */       clear();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/ConcurrentReferenceHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */