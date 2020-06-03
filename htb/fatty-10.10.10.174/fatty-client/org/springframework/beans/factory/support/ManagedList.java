/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ public class ManagedList<E>
/*     */   extends ArrayList<E>
/*     */   implements Mergeable, BeanMetadataElement
/*     */ {
/*     */   @Nullable
/*     */   private Object source;
/*     */   @Nullable
/*     */   private String elementTypeName;
/*     */   private boolean mergeEnabled;
/*     */   
/*     */   public ManagedList() {}
/*     */   
/*     */   public ManagedList(int initialCapacity) {
/*  52 */     super(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(@Nullable Object source) {
/*  61 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/*  67 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElementTypeName(String elementTypeName) {
/*  74 */     this.elementTypeName = elementTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getElementTypeName() {
/*  82 */     return this.elementTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMergeEnabled(boolean mergeEnabled) {
/*  90 */     this.mergeEnabled = mergeEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMergeEnabled() {
/*  95 */     return this.mergeEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<E> merge(@Nullable Object parent) {
/* 101 */     if (!this.mergeEnabled) {
/* 102 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*     */     }
/* 104 */     if (parent == null) {
/* 105 */       return this;
/*     */     }
/* 107 */     if (!(parent instanceof List)) {
/* 108 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*     */     }
/* 110 */     List<E> merged = new ManagedList();
/* 111 */     merged.addAll((List)parent);
/* 112 */     merged.addAll(this);
/* 113 */     return merged;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/ManagedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */