/*     */ package org.springframework.beans.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class MutableSortDefinition
/*     */   implements SortDefinition, Serializable
/*     */ {
/*  35 */   private String property = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ignoreCase = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ascending = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean toggleAscendingOnProperty = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutableSortDefinition() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutableSortDefinition(SortDefinition source) {
/*  60 */     this.property = source.getProperty();
/*  61 */     this.ignoreCase = source.isIgnoreCase();
/*  62 */     this.ascending = source.isAscending();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutableSortDefinition(String property, boolean ignoreCase, boolean ascending) {
/*  72 */     this.property = property;
/*  73 */     this.ignoreCase = ignoreCase;
/*  74 */     this.ascending = ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutableSortDefinition(boolean toggleAscendingOnSameProperty) {
/*  84 */     this.toggleAscendingOnProperty = toggleAscendingOnSameProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/*  95 */     if (!StringUtils.hasLength(property)) {
/*  96 */       this.property = "";
/*     */     }
/*     */     else {
/*     */       
/* 100 */       if (isToggleAscendingOnProperty()) {
/* 101 */         this.ascending = (!property.equals(this.property) || !this.ascending);
/*     */       }
/* 103 */       this.property = property;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProperty() {
/* 109 */     return this.property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreCase(boolean ignoreCase) {
/* 116 */     this.ignoreCase = ignoreCase;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnoreCase() {
/* 121 */     return this.ignoreCase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAscending(boolean ascending) {
/* 128 */     this.ascending = ascending;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAscending() {
/* 133 */     return this.ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToggleAscendingOnProperty(boolean toggleAscendingOnProperty) {
/* 144 */     this.toggleAscendingOnProperty = toggleAscendingOnProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isToggleAscendingOnProperty() {
/* 152 */     return this.toggleAscendingOnProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 158 */     if (this == other) {
/* 159 */       return true;
/*     */     }
/* 161 */     if (!(other instanceof SortDefinition)) {
/* 162 */       return false;
/*     */     }
/* 164 */     SortDefinition otherSd = (SortDefinition)other;
/* 165 */     return (getProperty().equals(otherSd.getProperty()) && 
/* 166 */       isAscending() == otherSd.isAscending() && 
/* 167 */       isIgnoreCase() == otherSd.isIgnoreCase());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 172 */     int hashCode = getProperty().hashCode();
/* 173 */     hashCode = 29 * hashCode + (isIgnoreCase() ? 1 : 0);
/* 174 */     hashCode = 29 * hashCode + (isAscending() ? 1 : 0);
/* 175 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/support/MutableSortDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */