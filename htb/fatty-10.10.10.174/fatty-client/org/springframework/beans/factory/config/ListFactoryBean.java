/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.core.ResolvableType;
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
/*     */ public class ListFactoryBean
/*     */   extends AbstractFactoryBean<List<Object>>
/*     */ {
/*     */   @Nullable
/*     */   private List<?> sourceList;
/*     */   @Nullable
/*     */   private Class<? extends List> targetListClass;
/*     */   
/*     */   public void setSourceList(List<?> sourceList) {
/*  50 */     this.sourceList = sourceList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetListClass(@Nullable Class<? extends List> targetListClass) {
/*  61 */     if (targetListClass == null) {
/*  62 */       throw new IllegalArgumentException("'targetListClass' must not be null");
/*     */     }
/*  64 */     if (!List.class.isAssignableFrom(targetListClass)) {
/*  65 */       throw new IllegalArgumentException("'targetListClass' must implement [java.util.List]");
/*     */     }
/*  67 */     this.targetListClass = targetListClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<List> getObjectType() {
/*  74 */     return List.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Object> createInstance() {
/*  80 */     if (this.sourceList == null) {
/*  81 */       throw new IllegalArgumentException("'sourceList' is required");
/*     */     }
/*  83 */     List<Object> result = null;
/*  84 */     if (this.targetListClass != null) {
/*  85 */       result = (List<Object>)BeanUtils.instantiateClass(this.targetListClass);
/*     */     } else {
/*     */       
/*  88 */       result = new ArrayList(this.sourceList.size());
/*     */     } 
/*  90 */     Class<?> valueType = null;
/*  91 */     if (this.targetListClass != null) {
/*  92 */       valueType = ResolvableType.forClass(this.targetListClass).asCollection().resolveGeneric(new int[0]);
/*     */     }
/*  94 */     if (valueType != null) {
/*  95 */       TypeConverter converter = getBeanTypeConverter();
/*  96 */       for (Object elem : this.sourceList) {
/*  97 */         result.add(converter.convertIfNecessary(elem, valueType));
/*     */       }
/*     */     } else {
/*     */       
/* 101 */       result.addAll(this.sourceList);
/*     */     } 
/* 103 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/ListFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */