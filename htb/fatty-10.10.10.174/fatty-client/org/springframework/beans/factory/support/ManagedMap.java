/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public class ManagedMap<K, V>
/*     */   extends LinkedHashMap<K, V>
/*     */   implements Mergeable, BeanMetadataElement
/*     */ {
/*     */   @Nullable
/*     */   private Object source;
/*     */   @Nullable
/*     */   private String keyTypeName;
/*     */   @Nullable
/*     */   private String valueTypeName;
/*     */   private boolean mergeEnabled;
/*     */   
/*     */   public ManagedMap() {}
/*     */   
/*     */   public ManagedMap(int initialCapacity) {
/*  55 */     super(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(@Nullable Object source) {
/*  64 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/*  70 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyTypeName(@Nullable String keyTypeName) {
/*  77 */     this.keyTypeName = keyTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getKeyTypeName() {
/*  85 */     return this.keyTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValueTypeName(@Nullable String valueTypeName) {
/*  92 */     this.valueTypeName = valueTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getValueTypeName() {
/* 100 */     return this.valueTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMergeEnabled(boolean mergeEnabled) {
/* 108 */     this.mergeEnabled = mergeEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMergeEnabled() {
/* 113 */     return this.mergeEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object merge(@Nullable Object parent) {
/* 119 */     if (!this.mergeEnabled) {
/* 120 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*     */     }
/* 122 */     if (parent == null) {
/* 123 */       return this;
/*     */     }
/* 125 */     if (!(parent instanceof Map)) {
/* 126 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*     */     }
/* 128 */     Map<K, V> merged = new ManagedMap();
/* 129 */     merged.putAll((Map<? extends K, ? extends V>)parent);
/* 130 */     merged.putAll(this);
/* 131 */     return merged;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/ManagedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */