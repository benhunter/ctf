/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NameMatchMethodPointcut
/*     */   extends StaticMethodMatcherPointcut
/*     */   implements Serializable
/*     */ {
/*  40 */   private List<String> mappedNames = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappedName(String mappedName) {
/*  49 */     setMappedNames(new String[] { mappedName });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappedNames(String... mappedNames) {
/*  58 */     this.mappedNames = new ArrayList<>(Arrays.asList(mappedNames));
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
/*     */   public NameMatchMethodPointcut addMethodName(String name) {
/*  71 */     this.mappedNames.add(name);
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass) {
/*  78 */     for (String mappedName : this.mappedNames) {
/*  79 */       if (mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName)) {
/*  80 */         return true;
/*     */       }
/*     */     } 
/*  83 */     return false;
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
/*     */   protected boolean isMatch(String methodName, String mappedName) {
/*  96 */     return PatternMatchUtils.simpleMatch(mappedName, methodName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 102 */     return (this == other || (other instanceof NameMatchMethodPointcut && this.mappedNames
/* 103 */       .equals(((NameMatchMethodPointcut)other).mappedNames)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 108 */     return this.mappedNames.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/NameMatchMethodPointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */