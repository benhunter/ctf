/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import org.springframework.beans.BeanMetadataElement;
/*    */ import org.springframework.beans.Mergeable;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ManagedProperties
/*    */   extends Properties
/*    */   implements Mergeable, BeanMetadataElement
/*    */ {
/*    */   @Nullable
/*    */   private Object source;
/*    */   private boolean mergeEnabled;
/*    */   
/*    */   public void setSource(@Nullable Object source) {
/* 47 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getSource() {
/* 53 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMergeEnabled(boolean mergeEnabled) {
/* 61 */     this.mergeEnabled = mergeEnabled;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMergeEnabled() {
/* 66 */     return this.mergeEnabled;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object merge(@Nullable Object parent) {
/* 72 */     if (!this.mergeEnabled) {
/* 73 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*    */     }
/* 75 */     if (parent == null) {
/* 76 */       return this;
/*    */     }
/* 78 */     if (!(parent instanceof Properties)) {
/* 79 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*    */     }
/* 81 */     Properties merged = new ManagedProperties();
/* 82 */     merged.putAll((Properties)parent);
/* 83 */     merged.putAll(this);
/* 84 */     return merged;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/ManagedProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */