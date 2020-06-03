/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.core.OrderComparator;
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
/*     */ public class AnnotationAwareOrderComparator
/*     */   extends OrderComparator
/*     */ {
/*  52 */   public static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();
/*     */ 
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
/*     */   protected Integer findOrder(Object obj) {
/*  65 */     Integer order = super.findOrder(obj);
/*  66 */     if (order != null) {
/*  67 */       return order;
/*     */     }
/*     */ 
/*     */     
/*  71 */     if (obj instanceof Class) {
/*  72 */       return OrderUtils.getOrder((Class)obj);
/*     */     }
/*  74 */     if (obj instanceof Method) {
/*  75 */       Order ann = AnnotationUtils.<Order>findAnnotation((Method)obj, Order.class);
/*  76 */       if (ann != null) {
/*  77 */         return Integer.valueOf(ann.value());
/*     */       }
/*     */     }
/*  80 */     else if (obj instanceof AnnotatedElement) {
/*  81 */       Order ann = AnnotationUtils.<Order>getAnnotation((AnnotatedElement)obj, Order.class);
/*  82 */       if (ann != null) {
/*  83 */         return Integer.valueOf(ann.value());
/*     */       }
/*     */     } else {
/*     */       
/*  87 */       order = OrderUtils.getOrder(obj.getClass());
/*  88 */       if (order == null && obj instanceof DecoratingProxy) {
/*  89 */         order = OrderUtils.getOrder(((DecoratingProxy)obj).getDecoratedClass());
/*     */       }
/*     */     } 
/*     */     
/*  93 */     return order;
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
/*     */   public Integer getPriority(Object obj) {
/* 105 */     if (obj instanceof Class) {
/* 106 */       return OrderUtils.getPriority((Class)obj);
/*     */     }
/* 108 */     Integer priority = OrderUtils.getPriority(obj.getClass());
/* 109 */     if (priority == null && obj instanceof DecoratingProxy) {
/* 110 */       priority = OrderUtils.getPriority(((DecoratingProxy)obj).getDecoratedClass());
/*     */     }
/* 112 */     return priority;
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
/* 124 */     if (list.size() > 1) {
/* 125 */       list.sort((Comparator<?>)INSTANCE);
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
/* 137 */     if (array.length > 1) {
/* 138 */       Arrays.sort(array, (Comparator<? super Object>)INSTANCE);
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
/* 151 */     if (value instanceof Object[]) {
/* 152 */       sort((Object[])value);
/*     */     }
/* 154 */     else if (value instanceof List) {
/* 155 */       sort((List)value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/AnnotationAwareOrderComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */