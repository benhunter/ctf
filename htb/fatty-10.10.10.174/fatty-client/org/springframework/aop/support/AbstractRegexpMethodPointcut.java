/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRegexpMethodPointcut
/*     */   extends StaticMethodMatcherPointcut
/*     */   implements Serializable
/*     */ {
/*  57 */   private String[] patterns = new String[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private String[] excludedPatterns = new String[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/*  71 */     setPatterns(new String[] { pattern });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPatterns(String... patterns) {
/*  80 */     Assert.notEmpty((Object[])patterns, "'patterns' must not be empty");
/*  81 */     this.patterns = new String[patterns.length];
/*  82 */     for (int i = 0; i < patterns.length; i++) {
/*  83 */       this.patterns[i] = StringUtils.trimWhitespace(patterns[i]);
/*     */     }
/*  85 */     initPatternRepresentation(this.patterns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getPatterns() {
/*  92 */     return this.patterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludedPattern(String excludedPattern) {
/* 101 */     setExcludedPatterns(new String[] { excludedPattern });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludedPatterns(String... excludedPatterns) {
/* 110 */     Assert.notEmpty((Object[])excludedPatterns, "'excludedPatterns' must not be empty");
/* 111 */     this.excludedPatterns = new String[excludedPatterns.length];
/* 112 */     for (int i = 0; i < excludedPatterns.length; i++) {
/* 113 */       this.excludedPatterns[i] = StringUtils.trimWhitespace(excludedPatterns[i]);
/*     */     }
/* 115 */     initExcludedPatternRepresentation(this.excludedPatterns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExcludedPatterns() {
/* 122 */     return this.excludedPatterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass) {
/* 133 */     return (matchesPattern(ClassUtils.getQualifiedMethodName(method, targetClass)) || (targetClass != method
/* 134 */       .getDeclaringClass() && 
/* 135 */       matchesPattern(ClassUtils.getQualifiedMethodName(method, method.getDeclaringClass()))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean matchesPattern(String signatureString) {
/* 144 */     for (int i = 0; i < this.patterns.length; i++) {
/* 145 */       boolean matched = matches(signatureString, i);
/* 146 */       if (matched) {
/* 147 */         for (int j = 0; j < this.excludedPatterns.length; j++) {
/* 148 */           boolean excluded = matchesExclusion(signatureString, j);
/* 149 */           if (excluded) {
/* 150 */             return false;
/*     */           }
/*     */         } 
/* 153 */         return true;
/*     */       } 
/*     */     } 
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void initPatternRepresentation(String[] paramArrayOfString) throws IllegalArgumentException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void initExcludedPatternRepresentation(String[] paramArrayOfString) throws IllegalArgumentException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean matches(String paramString, int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean matchesExclusion(String paramString, int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 199 */     if (this == other) {
/* 200 */       return true;
/*     */     }
/* 202 */     if (!(other instanceof AbstractRegexpMethodPointcut)) {
/* 203 */       return false;
/*     */     }
/* 205 */     AbstractRegexpMethodPointcut otherPointcut = (AbstractRegexpMethodPointcut)other;
/* 206 */     return (Arrays.equals((Object[])this.patterns, (Object[])otherPointcut.patterns) && 
/* 207 */       Arrays.equals((Object[])this.excludedPatterns, (Object[])otherPointcut.excludedPatterns));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 212 */     int result = 27;
/* 213 */     for (String pattern : this.patterns) {
/* 214 */       result = 13 * result + pattern.hashCode();
/*     */     }
/* 216 */     for (String excludedPattern : this.excludedPatterns) {
/* 217 */       result = 13 * result + excludedPattern.hashCode();
/*     */     }
/* 219 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 224 */     return getClass().getName() + ": patterns " + ObjectUtils.nullSafeToString((Object[])this.patterns) + ", excluded patterns " + 
/* 225 */       ObjectUtils.nullSafeToString((Object[])this.excludedPatterns);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/AbstractRegexpMethodPointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */