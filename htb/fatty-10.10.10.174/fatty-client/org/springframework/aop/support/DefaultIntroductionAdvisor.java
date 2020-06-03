/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.DynamicIntroductionAdvice;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.IntroductionInfo;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class DefaultIntroductionAdvisor
/*     */   implements IntroductionAdvisor, ClassFilter, Ordered, Serializable
/*     */ {
/*     */   private final Advice advice;
/*  47 */   private final Set<Class<?>> interfaces = new LinkedHashSet<>();
/*     */   
/*  49 */   private int order = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIntroductionAdvisor(Advice advice) {
/*  59 */     this(advice, (advice instanceof IntroductionInfo) ? (IntroductionInfo)advice : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIntroductionAdvisor(Advice advice, @Nullable IntroductionInfo introductionInfo) {
/*  69 */     Assert.notNull(advice, "Advice must not be null");
/*  70 */     this.advice = advice;
/*  71 */     if (introductionInfo != null) {
/*  72 */       Class<?>[] introducedInterfaces = introductionInfo.getInterfaces();
/*  73 */       if (introducedInterfaces.length == 0) {
/*  74 */         throw new IllegalArgumentException("IntroductionAdviceSupport implements no interfaces");
/*     */       }
/*  76 */       for (Class<?> ifc : introducedInterfaces) {
/*  77 */         addInterface(ifc);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIntroductionAdvisor(DynamicIntroductionAdvice advice, Class<?> intf) {
/*  88 */     Assert.notNull(advice, "Advice must not be null");
/*  89 */     this.advice = (Advice)advice;
/*  90 */     addInterface(intf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInterface(Class<?> intf) {
/*  99 */     Assert.notNull(intf, "Interface must not be null");
/* 100 */     if (!intf.isInterface()) {
/* 101 */       throw new IllegalArgumentException("Specified class [" + intf.getName() + "] must be an interface");
/*     */     }
/* 103 */     this.interfaces.add(intf);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getInterfaces() {
/* 108 */     return ClassUtils.toClassArray(this.interfaces);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateInterfaces() throws IllegalArgumentException {
/* 113 */     for (Class<?> ifc : this.interfaces) {
/* 114 */       if (this.advice instanceof DynamicIntroductionAdvice && 
/* 115 */         !((DynamicIntroductionAdvice)this.advice).implementsInterface(ifc)) {
/* 116 */         throw new IllegalArgumentException("DynamicIntroductionAdvice [" + this.advice + "] does not implement interface [" + ifc
/* 117 */             .getName() + "] specified for introduction");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 123 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 128 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public Advice getAdvice() {
/* 133 */     return this.advice;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPerInstance() {
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassFilter getClassFilter() {
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Class<?> clazz) {
/* 148 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 154 */     if (this == other) {
/* 155 */       return true;
/*     */     }
/* 157 */     if (!(other instanceof DefaultIntroductionAdvisor)) {
/* 158 */       return false;
/*     */     }
/* 160 */     DefaultIntroductionAdvisor otherAdvisor = (DefaultIntroductionAdvisor)other;
/* 161 */     return (this.advice.equals(otherAdvisor.advice) && this.interfaces.equals(otherAdvisor.interfaces));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 166 */     return this.advice.hashCode() * 13 + this.interfaces.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 171 */     return ClassUtils.getShortName(getClass()) + ": advice [" + this.advice + "]; interfaces " + 
/* 172 */       ClassUtils.classNamesToString(this.interfaces);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/DefaultIntroductionAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */