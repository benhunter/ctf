/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public final class EmptyTargetSource
/*     */   implements TargetSource, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3680494563553489691L;
/*  46 */   public static final EmptyTargetSource INSTANCE = new EmptyTargetSource(null, true);
/*     */ 
/*     */   
/*     */   private final Class<?> targetClass;
/*     */   
/*     */   private final boolean isStatic;
/*     */ 
/*     */   
/*     */   public static EmptyTargetSource forClass(@Nullable Class<?> targetClass) {
/*  55 */     return forClass(targetClass, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EmptyTargetSource forClass(@Nullable Class<?> targetClass, boolean isStatic) {
/*  65 */     return (targetClass == null && isStatic) ? INSTANCE : new EmptyTargetSource(targetClass, isStatic);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EmptyTargetSource(@Nullable Class<?> targetClass, boolean isStatic) {
/*  86 */     this.targetClass = targetClass;
/*  87 */     this.isStatic = isStatic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getTargetClass() {
/*  97 */     return this.targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 105 */     return this.isStatic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getTarget() {
/* 114 */     return null;
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
/*     */   private Object readResolve() {
/* 130 */     return (this.targetClass == null && this.isStatic) ? INSTANCE : this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 135 */     if (this == other) {
/* 136 */       return true;
/*     */     }
/* 138 */     if (!(other instanceof EmptyTargetSource)) {
/* 139 */       return false;
/*     */     }
/* 141 */     EmptyTargetSource otherTs = (EmptyTargetSource)other;
/* 142 */     return (ObjectUtils.nullSafeEquals(this.targetClass, otherTs.targetClass) && this.isStatic == otherTs.isStatic);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 147 */     return EmptyTargetSource.class.hashCode() * 13 + ObjectUtils.nullSafeHashCode(this.targetClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 152 */     return "EmptyTargetSource: " + ((this.targetClass != null) ? ("target class [" + this.targetClass
/* 153 */       .getName() + "]") : "no target class") + ", " + (this.isStatic ? "static" : "dynamic");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/EmptyTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */