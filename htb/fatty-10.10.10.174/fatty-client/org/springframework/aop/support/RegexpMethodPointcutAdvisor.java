/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Pointcut;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RegexpMethodPointcutAdvisor
/*     */   extends AbstractGenericPointcutAdvisor
/*     */ {
/*     */   @Nullable
/*     */   private String[] patterns;
/*     */   @Nullable
/*     */   private AbstractRegexpMethodPointcut pointcut;
/*  54 */   private final Object pointcutMonitor = new SerializableMonitor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexpMethodPointcutAdvisor() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexpMethodPointcutAdvisor(Advice advice) {
/*  74 */     setAdvice(advice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexpMethodPointcutAdvisor(String pattern, Advice advice) {
/*  83 */     setPattern(pattern);
/*  84 */     setAdvice(advice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexpMethodPointcutAdvisor(String[] patterns, Advice advice) {
/*  93 */     setPatterns(patterns);
/*  94 */     setAdvice(advice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/* 104 */     setPatterns(new String[] { pattern });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPatterns(String... patterns) {
/* 115 */     this.patterns = patterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pointcut getPointcut() {
/* 124 */     synchronized (this.pointcutMonitor) {
/* 125 */       if (this.pointcut == null) {
/* 126 */         this.pointcut = createPointcut();
/* 127 */         if (this.patterns != null) {
/* 128 */           this.pointcut.setPatterns(this.patterns);
/*     */         }
/*     */       } 
/* 131 */       return this.pointcut;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractRegexpMethodPointcut createPointcut() {
/* 141 */     return new JdkRegexpMethodPointcut();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 146 */     return getClass().getName() + ": advice [" + getAdvice() + "], pointcut patterns " + 
/* 147 */       ObjectUtils.nullSafeToString((Object[])this.patterns);
/*     */   }
/*     */   
/*     */   private static class SerializableMonitor implements Serializable {
/*     */     private SerializableMonitor() {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/RegexpMethodPointcutAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */