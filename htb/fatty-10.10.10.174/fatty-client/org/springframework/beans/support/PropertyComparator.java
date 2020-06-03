/*     */ package org.springframework.beans.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.BeansException;
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
/*     */ public class PropertyComparator<T>
/*     */   implements Comparator<T>
/*     */ {
/*  43 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final SortDefinition sortDefinition;
/*     */   
/*  47 */   private final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyComparator(SortDefinition sortDefinition) {
/*  55 */     this.sortDefinition = sortDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyComparator(String property, boolean ignoreCase, boolean ascending) {
/*  65 */     this.sortDefinition = new MutableSortDefinition(property, ignoreCase, ascending);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SortDefinition getSortDefinition() {
/*  72 */     return this.sortDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(T o1, T o2) {
/*     */     int result;
/*  79 */     Object v1 = getPropertyValue(o1);
/*  80 */     Object v2 = getPropertyValue(o2);
/*  81 */     if (this.sortDefinition.isIgnoreCase() && v1 instanceof String && v2 instanceof String) {
/*  82 */       v1 = ((String)v1).toLowerCase();
/*  83 */       v2 = ((String)v2).toLowerCase();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  90 */       if (v1 != null) {
/*  91 */         result = (v2 != null) ? ((Comparable<Object>)v1).compareTo(v2) : -1;
/*     */       } else {
/*     */         
/*  94 */         result = (v2 != null) ? 1 : 0;
/*     */       }
/*     */     
/*  97 */     } catch (RuntimeException ex) {
/*  98 */       if (this.logger.isDebugEnabled()) {
/*  99 */         this.logger.debug("Could not sort objects [" + o1 + "] and [" + o2 + "]", ex);
/*     */       }
/* 101 */       return 0;
/*     */     } 
/*     */     
/* 104 */     return this.sortDefinition.isAscending() ? result : -result;
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
/*     */   private Object getPropertyValue(Object obj) {
/*     */     try {
/* 118 */       this.beanWrapper.setWrappedInstance(obj);
/* 119 */       return this.beanWrapper.getPropertyValue(this.sortDefinition.getProperty());
/*     */     }
/* 121 */     catch (BeansException ex) {
/* 122 */       this.logger.debug("PropertyComparator could not access property - treating as null for sorting", (Throwable)ex);
/* 123 */       return null;
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
/*     */   public static void sort(List<?> source, SortDefinition sortDefinition) throws BeansException {
/* 137 */     if (StringUtils.hasText(sortDefinition.getProperty())) {
/* 138 */       source.sort(new PropertyComparator(sortDefinition));
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
/*     */   public static void sort(Object[] source, SortDefinition sortDefinition) throws BeansException {
/* 151 */     if (StringUtils.hasText(sortDefinition.getProperty()))
/* 152 */       Arrays.sort(source, new PropertyComparator(sortDefinition)); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/support/PropertyComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */