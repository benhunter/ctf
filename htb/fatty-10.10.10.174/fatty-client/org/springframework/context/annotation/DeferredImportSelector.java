/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import org.springframework.core.type.AnnotationMetadata;
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
/*     */ public interface DeferredImportSelector
/*     */   extends ImportSelector
/*     */ {
/*     */   @Nullable
/*     */   default Class<? extends Group> getImportGroup() {
/*  50 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface Group
/*     */   {
/*     */     void process(AnnotationMetadata param1AnnotationMetadata, DeferredImportSelector param1DeferredImportSelector);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterable<Entry> selectImports();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static class Entry
/*     */     {
/*     */       private final AnnotationMetadata metadata;
/*     */ 
/*     */ 
/*     */       
/*     */       private final String importClassName;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public Entry(AnnotationMetadata metadata, String importClassName) {
/*  83 */         this.metadata = metadata;
/*  84 */         this.importClassName = importClassName;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public AnnotationMetadata getMetadata() {
/*  92 */         return this.metadata;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public String getImportClassName() {
/*  99 */         return this.importClassName;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean equals(Object other) {
/* 104 */         if (this == other) {
/* 105 */           return true;
/*     */         }
/* 107 */         if (other == null || getClass() != other.getClass()) {
/* 108 */           return false;
/*     */         }
/* 110 */         Entry entry = (Entry)other;
/* 111 */         return (Objects.equals(this.metadata, entry.metadata) && 
/* 112 */           Objects.equals(this.importClassName, entry.importClassName));
/*     */       }
/*     */ 
/*     */       
/*     */       public int hashCode() {
/* 117 */         return Objects.hash(new Object[] { this.metadata, this.importClassName });
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/DeferredImportSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */