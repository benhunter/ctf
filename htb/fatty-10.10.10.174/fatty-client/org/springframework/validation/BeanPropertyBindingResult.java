/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanPropertyBindingResult
/*     */   extends AbstractPropertyBindingResult
/*     */   implements Serializable
/*     */ {
/*     */   @Nullable
/*     */   private final Object target;
/*     */   private final boolean autoGrowNestedPaths;
/*     */   private final int autoGrowCollectionLimit;
/*     */   @Nullable
/*     */   private transient BeanWrapper beanWrapper;
/*     */   
/*     */   public BeanPropertyBindingResult(@Nullable Object target, String objectName) {
/*  63 */     this(target, objectName, true, 2147483647);
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
/*     */   public BeanPropertyBindingResult(@Nullable Object target, String objectName, boolean autoGrowNestedPaths, int autoGrowCollectionLimit) {
/*  76 */     super(objectName);
/*  77 */     this.target = target;
/*  78 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*  79 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Object getTarget() {
/*  86 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ConfigurablePropertyAccessor getPropertyAccessor() {
/*  96 */     if (this.beanWrapper == null) {
/*  97 */       this.beanWrapper = createBeanWrapper();
/*  98 */       this.beanWrapper.setExtractOldValueForEditor(true);
/*  99 */       this.beanWrapper.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
/* 100 */       this.beanWrapper.setAutoGrowCollectionLimit(this.autoGrowCollectionLimit);
/*     */     } 
/* 102 */     return (ConfigurablePropertyAccessor)this.beanWrapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanWrapper createBeanWrapper() {
/* 110 */     if (this.target == null) {
/* 111 */       throw new IllegalStateException("Cannot access properties on null bean instance '" + getObjectName() + "'");
/*     */     }
/* 113 */     return PropertyAccessorFactory.forBeanPropertyAccess(this.target);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/BeanPropertyBindingResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */