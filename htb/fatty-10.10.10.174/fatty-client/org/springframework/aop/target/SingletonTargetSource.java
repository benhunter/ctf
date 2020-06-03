/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class SingletonTargetSource
/*     */   implements TargetSource, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 9031246629662423738L;
/*     */   private final Object target;
/*     */   
/*     */   public SingletonTargetSource(Object target) {
/*  53 */     Assert.notNull(target, "Target object must not be null");
/*  54 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTargetClass() {
/*  60 */     return this.target.getClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getTarget() {
/*  65 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseTarget(Object target) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  85 */     if (this == other) {
/*  86 */       return true;
/*     */     }
/*  88 */     if (!(other instanceof SingletonTargetSource)) {
/*  89 */       return false;
/*     */     }
/*  91 */     SingletonTargetSource otherTargetSource = (SingletonTargetSource)other;
/*  92 */     return this.target.equals(otherTargetSource.target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 100 */     return this.target.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return "SingletonTargetSource for target object [" + ObjectUtils.identityToString(this.target) + "]";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/SingletonTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */