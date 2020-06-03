/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.slf4j.Marker;
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
/*     */ public class BasicMarker
/*     */   implements Marker
/*     */ {
/*     */   private static final long serialVersionUID = -2849567615646933777L;
/*     */   private final String name;
/*  43 */   private List<Marker> referenceList = new CopyOnWriteArrayList<Marker>();
/*     */   
/*     */   BasicMarker(String name) {
/*  46 */     if (name == null) {
/*  47 */       throw new IllegalArgumentException("A marker name cannot be null");
/*     */     }
/*  49 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  53 */     return this.name;
/*     */   }
/*     */   
/*     */   public void add(Marker reference) {
/*  57 */     if (reference == null) {
/*  58 */       throw new IllegalArgumentException("A null value cannot be added to a Marker as reference.");
/*     */     }
/*     */ 
/*     */     
/*  62 */     if (contains(reference)) {
/*     */       return;
/*     */     }
/*  65 */     if (reference.contains(this)) {
/*     */       return;
/*     */     }
/*     */     
/*  69 */     this.referenceList.add(reference);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasReferences() {
/*  74 */     return (this.referenceList.size() > 0);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean hasChildren() {
/*  79 */     return hasReferences();
/*     */   }
/*     */   
/*     */   public Iterator<Marker> iterator() {
/*  83 */     return this.referenceList.iterator();
/*     */   }
/*     */   
/*     */   public boolean remove(Marker referenceToRemove) {
/*  87 */     return this.referenceList.remove(referenceToRemove);
/*     */   }
/*     */   
/*     */   public boolean contains(Marker other) {
/*  91 */     if (other == null) {
/*  92 */       throw new IllegalArgumentException("Other cannot be null");
/*     */     }
/*     */     
/*  95 */     if (equals(other)) {
/*  96 */       return true;
/*     */     }
/*     */     
/*  99 */     if (hasReferences()) {
/* 100 */       for (Marker ref : this.referenceList) {
/* 101 */         if (ref.contains(other)) {
/* 102 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(String name) {
/* 113 */     if (name == null) {
/* 114 */       throw new IllegalArgumentException("Other cannot be null");
/*     */     }
/*     */     
/* 117 */     if (this.name.equals(name)) {
/* 118 */       return true;
/*     */     }
/*     */     
/* 121 */     if (hasReferences()) {
/* 122 */       for (Marker ref : this.referenceList) {
/* 123 */         if (ref.contains(name)) {
/* 124 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 128 */     return false;
/*     */   }
/*     */   
/* 131 */   private static String OPEN = "[ ";
/* 132 */   private static String CLOSE = " ]";
/* 133 */   private static String SEP = ", ";
/*     */   
/*     */   public boolean equals(Object obj) {
/* 136 */     if (this == obj)
/* 137 */       return true; 
/* 138 */     if (obj == null)
/* 139 */       return false; 
/* 140 */     if (!(obj instanceof Marker)) {
/* 141 */       return false;
/*     */     }
/* 143 */     Marker other = (Marker)obj;
/* 144 */     return this.name.equals(other.getName());
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 148 */     return this.name.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 152 */     if (!hasReferences()) {
/* 153 */       return getName();
/*     */     }
/* 155 */     Iterator<Marker> it = iterator();
/*     */     
/* 157 */     StringBuilder sb = new StringBuilder(getName());
/* 158 */     sb.append(' ').append(OPEN);
/* 159 */     while (it.hasNext()) {
/* 160 */       Marker reference = it.next();
/* 161 */       sb.append(reference.getName());
/* 162 */       if (it.hasNext()) {
/* 163 */         sb.append(SEP);
/*     */       }
/*     */     } 
/* 166 */     sb.append(CLOSE);
/*     */     
/* 168 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/helpers/BasicMarker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */