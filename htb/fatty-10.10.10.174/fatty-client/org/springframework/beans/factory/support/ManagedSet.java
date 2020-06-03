/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeanMetadataElement;
/*     */ import org.springframework.beans.Mergeable;
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
/*     */ public class ManagedSet<E>
/*     */   extends LinkedHashSet<E>
/*     */   implements Mergeable, BeanMetadataElement
/*     */ {
/*     */   @Nullable
/*     */   private Object source;
/*     */   @Nullable
/*     */   private String elementTypeName;
/*     */   private boolean mergeEnabled;
/*     */   
/*     */   public ManagedSet() {}
/*     */   
/*     */   public ManagedSet(int initialCapacity) {
/*  51 */     super(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(@Nullable Object source) {
/*  60 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/*  66 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElementTypeName(@Nullable String elementTypeName) {
/*  73 */     this.elementTypeName = elementTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getElementTypeName() {
/*  81 */     return this.elementTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMergeEnabled(boolean mergeEnabled) {
/*  89 */     this.mergeEnabled = mergeEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMergeEnabled() {
/*  94 */     return this.mergeEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<E> merge(@Nullable Object parent) {
/* 100 */     if (!this.mergeEnabled) {
/* 101 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*     */     }
/* 103 */     if (parent == null) {
/* 104 */       return this;
/*     */     }
/* 106 */     if (!(parent instanceof Set)) {
/* 107 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*     */     }
/* 109 */     Set<E> merged = new ManagedSet();
/* 110 */     merged.addAll((Set)parent);
/* 111 */     merged.addAll(this);
/* 112 */     return merged;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/ManagedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */