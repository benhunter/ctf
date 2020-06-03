/*     */ package org.springframework.util.comparator;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class CompoundComparator<T>
/*     */   implements Comparator<T>, Serializable
/*     */ {
/*     */   private final List<InvertibleComparator> comparators;
/*     */   
/*     */   public CompoundComparator() {
/*  56 */     this.comparators = new ArrayList<>();
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
/*     */   public CompoundComparator(Comparator... comparators) {
/*  68 */     Assert.notNull(comparators, "Comparators must not be null");
/*  69 */     this.comparators = new ArrayList<>(comparators.length);
/*  70 */     for (Comparator<? extends T> comparator : comparators) {
/*  71 */       addComparator(comparator);
/*     */     }
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
/*     */   public void addComparator(Comparator<? extends T> comparator) {
/*  85 */     if (comparator instanceof InvertibleComparator) {
/*  86 */       this.comparators.add((InvertibleComparator)comparator);
/*     */     } else {
/*     */       
/*  89 */       this.comparators.add(new InvertibleComparator<>(comparator));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComparator(Comparator<? extends T> comparator, boolean ascending) {
/* 100 */     this.comparators.add(new InvertibleComparator<>(comparator, ascending));
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
/*     */   public void setComparator(int index, Comparator<? extends T> comparator) {
/* 113 */     if (comparator instanceof InvertibleComparator) {
/* 114 */       this.comparators.set(index, (InvertibleComparator)comparator);
/*     */     } else {
/*     */       
/* 117 */       this.comparators.set(index, new InvertibleComparator<>(comparator));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComparator(int index, Comparator<T> comparator, boolean ascending) {
/* 128 */     this.comparators.set(index, new InvertibleComparator<>(comparator, ascending));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invertOrder() {
/* 136 */     for (InvertibleComparator comparator : this.comparators) {
/* 137 */       comparator.invertOrder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invertOrder(int index) {
/* 146 */     ((InvertibleComparator)this.comparators.get(index)).invertOrder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAscendingOrder(int index) {
/* 154 */     ((InvertibleComparator)this.comparators.get(index)).setAscending(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescendingOrder(int index) {
/* 162 */     ((InvertibleComparator)this.comparators.get(index)).setAscending(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getComparatorCount() {
/* 169 */     return this.comparators.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(T o1, T o2) {
/* 176 */     Assert.state(!this.comparators.isEmpty(), "No sort definitions have been added to this CompoundComparator to compare");
/*     */     
/* 178 */     for (InvertibleComparator<T> comparator : this.comparators) {
/* 179 */       int result = comparator.compare(o1, o2);
/* 180 */       if (result != 0) {
/* 181 */         return result;
/*     */       }
/*     */     } 
/* 184 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 191 */     return (this == other || (other instanceof CompoundComparator && this.comparators
/* 192 */       .equals(((CompoundComparator)other).comparators)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 197 */     return this.comparators.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 202 */     return "CompoundComparator: " + this.comparators;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/comparator/CompoundComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */