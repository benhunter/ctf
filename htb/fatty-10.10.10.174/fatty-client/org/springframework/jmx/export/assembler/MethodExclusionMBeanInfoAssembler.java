/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodExclusionMBeanInfoAssembler
/*     */   extends AbstractConfigurableMBeanInfoAssembler
/*     */ {
/*     */   @Nullable
/*     */   private Set<String> ignoredMethods;
/*     */   @Nullable
/*     */   private Map<String, Set<String>> ignoredMethodMappings;
/*     */   
/*     */   public void setIgnoredMethods(String... ignoredMethodNames) {
/*  75 */     this.ignoredMethods = new HashSet<>(Arrays.asList(ignoredMethodNames));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoredMethodMappings(Properties mappings) {
/*  86 */     this.ignoredMethodMappings = new HashMap<>();
/*  87 */     for (Enumeration<?> en = mappings.keys(); en.hasMoreElements(); ) {
/*  88 */       String beanKey = (String)en.nextElement();
/*  89 */       String[] methodNames = StringUtils.commaDelimitedListToStringArray(mappings.getProperty(beanKey));
/*  90 */       this.ignoredMethodMappings.put(beanKey, new HashSet<>(Arrays.asList(methodNames)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean includeReadAttribute(Method method, String beanKey) {
/*  97 */     return isNotIgnored(method, beanKey);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean includeWriteAttribute(Method method, String beanKey) {
/* 102 */     return isNotIgnored(method, beanKey);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean includeOperation(Method method, String beanKey) {
/* 107 */     return isNotIgnored(method, beanKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isNotIgnored(Method method, String beanKey) {
/* 118 */     if (this.ignoredMethodMappings != null) {
/* 119 */       Set<String> methodNames = this.ignoredMethodMappings.get(beanKey);
/* 120 */       if (methodNames != null) {
/* 121 */         return !methodNames.contains(method.getName());
/*     */       }
/*     */     } 
/* 124 */     if (this.ignoredMethods != null) {
/* 125 */       return !this.ignoredMethods.contains(method.getName());
/*     */     }
/* 127 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/assembler/MethodExclusionMBeanInfoAssembler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */