/*     */ package org.springframework.util.comparator;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class InvertibleComparator<T>
/*     */   implements Comparator<T>, Serializable
/*     */ {
/*     */   private final Comparator<T> comparator;
/*     */   private boolean ascending = true;
/*     */   
/*     */   public InvertibleComparator(Comparator<T> comparator) {
/*  51 */     Assert.notNull(comparator, "Comparator must not be null");
/*  52 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InvertibleComparator(Comparator<T> comparator, boolean ascending) {
/*  62 */     Assert.notNull(comparator, "Comparator must not be null");
/*  63 */     this.comparator = comparator;
/*  64 */     setAscending(ascending);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAscending(boolean ascending) {
/*  72 */     this.ascending = ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAscending() {
/*  79 */     return this.ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invertOrder() {
/*  87 */     this.ascending = !this.ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(T o1, T o2) {
/*  93 */     int result = this.comparator.compare(o1, o2);
/*  94 */     if (result != 0) {
/*     */       
/*  96 */       if (!this.ascending) {
/*  97 */         if (Integer.MIN_VALUE == result) {
/*  98 */           result = Integer.MAX_VALUE;
/*     */         } else {
/*     */           
/* 101 */           result *= -1;
/*     */         } 
/*     */       }
/* 104 */       return result;
/*     */     } 
/* 106 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 112 */     if (this == other) {
/* 113 */       return true;
/*     */     }
/* 115 */     if (!(other instanceof InvertibleComparator)) {
/* 116 */       return false;
/*     */     }
/* 118 */     InvertibleComparator<T> otherComp = (InvertibleComparator<T>)other;
/* 119 */     return (this.comparator.equals(otherComp.comparator) && this.ascending == otherComp.ascending);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 124 */     return this.comparator.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 129 */     return "InvertibleComparator: [" + this.comparator + "]; ascending=" + this.ascending;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/comparator/InvertibleComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */