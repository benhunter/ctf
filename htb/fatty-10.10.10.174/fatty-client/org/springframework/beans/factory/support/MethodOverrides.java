/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ public class MethodOverrides
/*     */ {
/*  40 */   private final Set<MethodOverride> overrides = Collections.synchronizedSet(new LinkedHashSet<>(2));
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean modified = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodOverrides() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodOverrides(MethodOverrides other) {
/*  55 */     addOverrides(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOverrides(@Nullable MethodOverrides other) {
/*  63 */     if (other != null) {
/*  64 */       this.modified = true;
/*  65 */       this.overrides.addAll(other.overrides);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOverride(MethodOverride override) {
/*  73 */     this.modified = true;
/*  74 */     this.overrides.add(override);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<MethodOverride> getOverrides() {
/*  83 */     this.modified = true;
/*  84 */     return this.overrides;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  91 */     return (!this.modified || this.overrides.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MethodOverride getOverride(Method method) {
/* 101 */     if (!this.modified) {
/* 102 */       return null;
/*     */     }
/* 104 */     synchronized (this.overrides) {
/* 105 */       MethodOverride match = null;
/* 106 */       for (MethodOverride candidate : this.overrides) {
/* 107 */         if (candidate.matches(method)) {
/* 108 */           match = candidate;
/*     */         }
/*     */       } 
/* 111 */       return match;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 118 */     if (this == other) {
/* 119 */       return true;
/*     */     }
/* 121 */     if (!(other instanceof MethodOverrides)) {
/* 122 */       return false;
/*     */     }
/* 124 */     MethodOverrides that = (MethodOverrides)other;
/* 125 */     return this.overrides.equals(that.overrides);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 131 */     return this.overrides.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/MethodOverrides.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */