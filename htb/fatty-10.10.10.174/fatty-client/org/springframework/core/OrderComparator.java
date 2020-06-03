/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OrderComparator
/*     */   implements Comparator<Object>
/*     */ {
/*  53 */   public static final OrderComparator INSTANCE = new OrderComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<Object> withSourceProvider(OrderSourceProvider sourceProvider) {
/*  63 */     return (o1, o2) -> doCompare(o1, o2, sourceProvider);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compare(@Nullable Object o1, @Nullable Object o2) {
/*  68 */     return doCompare(o1, o2, null);
/*     */   }
/*     */   
/*     */   private int doCompare(@Nullable Object o1, @Nullable Object o2, @Nullable OrderSourceProvider sourceProvider) {
/*  72 */     boolean p1 = o1 instanceof PriorityOrdered;
/*  73 */     boolean p2 = o2 instanceof PriorityOrdered;
/*  74 */     if (p1 && !p2) {
/*  75 */       return -1;
/*     */     }
/*  77 */     if (p2 && !p1) {
/*  78 */       return 1;
/*     */     }
/*     */     
/*  81 */     int i1 = getOrder(o1, sourceProvider);
/*  82 */     int i2 = getOrder(o2, sourceProvider);
/*  83 */     return Integer.compare(i1, i2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getOrder(@Nullable Object obj, @Nullable OrderSourceProvider sourceProvider) {
/*  94 */     Integer order = null;
/*  95 */     if (obj != null && sourceProvider != null) {
/*  96 */       Object orderSource = sourceProvider.getOrderSource(obj);
/*  97 */       if (orderSource != null) {
/*  98 */         if (orderSource.getClass().isArray()) {
/*  99 */           Object[] sources = ObjectUtils.toObjectArray(orderSource);
/* 100 */           for (Object source : sources) {
/* 101 */             order = findOrder(source);
/* 102 */             if (order != null) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } else {
/*     */           
/* 108 */           order = findOrder(orderSource);
/*     */         } 
/*     */       }
/*     */     } 
/* 112 */     return (order != null) ? order.intValue() : getOrder(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getOrder(@Nullable Object obj) {
/* 123 */     if (obj != null) {
/* 124 */       Integer order = findOrder(obj);
/* 125 */       if (order != null) {
/* 126 */         return order.intValue();
/*     */       }
/*     */     } 
/* 129 */     return Integer.MAX_VALUE;
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
/*     */   protected Integer findOrder(Object obj) {
/* 141 */     return (obj instanceof Ordered) ? Integer.valueOf(((Ordered)obj).getOrder()) : null;
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
/*     */   @Nullable
/*     */   public Integer getPriority(Object obj) {
/* 157 */     return null;
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
/*     */   public static void sort(List<?> list) {
/* 169 */     if (list.size() > 1) {
/* 170 */       list.sort(INSTANCE);
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
/*     */   public static void sort(Object[] array) {
/* 182 */     if (array.length > 1) {
/* 183 */       Arrays.sort(array, INSTANCE);
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
/*     */   public static void sortIfNecessary(Object value) {
/* 196 */     if (value instanceof Object[]) {
/* 197 */       sort((Object[])value);
/*     */     }
/* 199 */     else if (value instanceof List) {
/* 200 */       sort((List)value);
/*     */     } 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OrderSourceProvider {
/*     */     @Nullable
/*     */     Object getOrderSource(Object param1Object);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/OrderComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */