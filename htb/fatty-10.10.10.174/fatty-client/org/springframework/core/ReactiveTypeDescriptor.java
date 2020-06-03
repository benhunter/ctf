/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReactiveTypeDescriptor
/*     */ {
/*     */   private final Class<?> reactiveType;
/*     */   private final boolean multiValue;
/*     */   private final boolean noValue;
/*     */   @Nullable
/*     */   private final Supplier<?> emptyValueSupplier;
/*     */   
/*     */   private ReactiveTypeDescriptor(Class<?> reactiveType, boolean multiValue, boolean noValue, @Nullable Supplier<?> emptySupplier) {
/*  49 */     Assert.notNull(reactiveType, "'reactiveType' must not be null");
/*  50 */     this.reactiveType = reactiveType;
/*  51 */     this.multiValue = multiValue;
/*  52 */     this.noValue = noValue;
/*  53 */     this.emptyValueSupplier = emptySupplier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getReactiveType() {
/*  61 */     return this.reactiveType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMultiValue() {
/*  71 */     return this.multiValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNoValue() {
/*  79 */     return this.noValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsEmpty() {
/*  86 */     return (this.emptyValueSupplier != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getEmptyValue() {
/*  94 */     Assert.state((this.emptyValueSupplier != null), "Empty values not supported");
/*  95 */     return this.emptyValueSupplier.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 101 */     if (this == other) {
/* 102 */       return true;
/*     */     }
/* 104 */     if (other == null || getClass() != other.getClass()) {
/* 105 */       return false;
/*     */     }
/* 107 */     return this.reactiveType.equals(((ReactiveTypeDescriptor)other).reactiveType);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     return this.reactiveType.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReactiveTypeDescriptor multiValue(Class<?> type, Supplier<?> emptySupplier) {
/* 122 */     return new ReactiveTypeDescriptor(type, true, false, emptySupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReactiveTypeDescriptor singleOptionalValue(Class<?> type, Supplier<?> emptySupplier) {
/* 131 */     return new ReactiveTypeDescriptor(type, false, false, emptySupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReactiveTypeDescriptor singleRequiredValue(Class<?> type) {
/* 139 */     return new ReactiveTypeDescriptor(type, false, false, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReactiveTypeDescriptor noValue(Class<?> type, Supplier<?> emptySupplier) {
/* 148 */     return new ReactiveTypeDescriptor(type, false, true, emptySupplier);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/ReactiveTypeDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */