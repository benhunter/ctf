/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.aop.TargetSource;
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
/*     */ public class HotSwappableTargetSource
/*     */   implements TargetSource, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7497929212653839187L;
/*     */   private Object target;
/*     */   
/*     */   public HotSwappableTargetSource(Object initialTarget) {
/*  53 */     Assert.notNull(initialTarget, "Target object must not be null");
/*  54 */     this.target = initialTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Class<?> getTargetClass() {
/*  64 */     return this.target.getClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStatic() {
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Object getTarget() {
/*  74 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseTarget(Object target) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object swap(Object newTarget) throws IllegalArgumentException {
/*  90 */     Assert.notNull(newTarget, "Target object must not be null");
/*  91 */     Object old = this.target;
/*  92 */     this.target = newTarget;
/*  93 */     return old;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 103 */     return (this == other || (other instanceof HotSwappableTargetSource && this.target
/* 104 */       .equals(((HotSwappableTargetSource)other).target)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 109 */     return HotSwappableTargetSource.class.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 114 */     return "HotSwappableTargetSource for target: " + this.target;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/HotSwappableTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */