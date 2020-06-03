/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class OrderUtils
/*     */ {
/*  40 */   private static final Object NOT_ANNOTATED = new Object();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Class<? extends Annotation> priorityAnnotationType;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  49 */       priorityAnnotationType = ClassUtils.forName("javax.annotation.Priority", OrderUtils.class.getClassLoader());
/*     */     }
/*  51 */     catch (Throwable ex) {
/*     */       
/*  53 */       priorityAnnotationType = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static final Map<Class<?>, Object> orderCache = (Map<Class<?>, Object>)new ConcurrentReferenceHashMap(64);
/*     */ 
/*     */   
/*  62 */   private static final Map<Class<?>, Object> priorityCache = (Map<Class<?>, Object>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getOrder(Class<?> type, int defaultOrder) {
/*  75 */     Integer order = getOrder(type);
/*  76 */     return (order != null) ? order.intValue() : defaultOrder;
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
/*     */   public static Integer getOrder(Class<?> type, @Nullable Integer defaultOrder) {
/*  89 */     Integer order = getOrder(type);
/*  90 */     return (order != null) ? order : defaultOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Integer getOrder(Class<?> type) {
/*     */     Integer result;
/* 102 */     Object cached = orderCache.get(type);
/* 103 */     if (cached != null) {
/* 104 */       return (cached instanceof Integer) ? (Integer)cached : null;
/*     */     }
/* 106 */     Order order = AnnotationUtils.<Order>findAnnotation(type, Order.class);
/*     */     
/* 108 */     if (order != null) {
/* 109 */       result = Integer.valueOf(order.value());
/*     */     } else {
/*     */       
/* 112 */       result = getPriority(type);
/*     */     } 
/* 114 */     orderCache.put(type, (result != null) ? result : NOT_ANNOTATED);
/* 115 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Integer getPriority(Class<?> type) {
/* 126 */     if (priorityAnnotationType == null) {
/* 127 */       return null;
/*     */     }
/* 129 */     Object cached = priorityCache.get(type);
/* 130 */     if (cached != null) {
/* 131 */       return (cached instanceof Integer) ? (Integer)cached : null;
/*     */     }
/* 133 */     Annotation priority = AnnotationUtils.findAnnotation(type, (Class)priorityAnnotationType);
/* 134 */     Integer result = null;
/* 135 */     if (priority != null) {
/* 136 */       result = (Integer)AnnotationUtils.getValue(priority);
/*     */     }
/* 138 */     priorityCache.put(type, (result != null) ? result : NOT_ANNOTATED);
/* 139 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/OrderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */