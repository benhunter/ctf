/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ public class SetFactoryBean
/*     */   extends AbstractFactoryBean<Set<Object>>
/*     */ {
/*     */   @Nullable
/*     */   private Set<?> sourceSet;
/*     */   @Nullable
/*     */   private Class<? extends Set> targetSetClass;
/*     */   
/*     */   public void setSourceSet(Set<?> sourceSet) {
/*  50 */     this.sourceSet = sourceSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetSetClass(@Nullable Class<? extends Set> targetSetClass) {
/*  61 */     if (targetSetClass == null) {
/*  62 */       throw new IllegalArgumentException("'targetSetClass' must not be null");
/*     */     }
/*  64 */     if (!Set.class.isAssignableFrom(targetSetClass)) {
/*  65 */       throw new IllegalArgumentException("'targetSetClass' must implement [java.util.Set]");
/*     */     }
/*  67 */     this.targetSetClass = targetSetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<Set> getObjectType() {
/*  74 */     return Set.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<Object> createInstance() {
/*  80 */     if (this.sourceSet == null) {
/*  81 */       throw new IllegalArgumentException("'sourceSet' is required");
/*     */     }
/*  83 */     Set<Object> result = null;
/*  84 */     if (this.targetSetClass != null) {
/*  85 */       result = (Set<Object>)BeanUtils.instantiateClass(this.targetSetClass);
/*     */     } else {
/*     */       
/*  88 */       result = new LinkedHashSet(this.sourceSet.size());
/*     */     } 
/*  90 */     Class<?> valueType = null;
/*  91 */     if (this.targetSetClass != null) {
/*  92 */       valueType = ResolvableType.forClass(this.targetSetClass).asCollection().resolveGeneric(new int[0]);
/*     */     }
/*  94 */     if (valueType != null) {
/*  95 */       TypeConverter converter = getBeanTypeConverter();
/*  96 */       for (Object elem : this.sourceSet) {
/*  97 */         result.add(converter.convertIfNecessary(elem, valueType));
/*     */       }
/*     */     } else {
/*     */       
/* 101 */       result.addAll(this.sourceSet);
/*     */     } 
/* 103 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/SetFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */