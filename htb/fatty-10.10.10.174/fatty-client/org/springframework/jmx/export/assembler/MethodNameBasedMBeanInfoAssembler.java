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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodNameBasedMBeanInfoAssembler
/*     */   extends AbstractConfigurableMBeanInfoAssembler
/*     */ {
/*     */   @Nullable
/*     */   private Set<String> managedMethods;
/*     */   @Nullable
/*     */   private Map<String, Set<String>> methodMappings;
/*     */   
/*     */   public void setManagedMethods(String... methodNames) {
/*  79 */     this.managedMethods = new HashSet<>(Arrays.asList(methodNames));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethodMappings(Properties mappings) {
/*  90 */     this.methodMappings = new HashMap<>();
/*  91 */     for (Enumeration<?> en = mappings.keys(); en.hasMoreElements(); ) {
/*  92 */       String beanKey = (String)en.nextElement();
/*  93 */       String[] methodNames = StringUtils.commaDelimitedListToStringArray(mappings.getProperty(beanKey));
/*  94 */       this.methodMappings.put(beanKey, new HashSet<>(Arrays.asList(methodNames)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean includeReadAttribute(Method method, String beanKey) {
/* 101 */     return isMatch(method, beanKey);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean includeWriteAttribute(Method method, String beanKey) {
/* 106 */     return isMatch(method, beanKey);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean includeOperation(Method method, String beanKey) {
/* 111 */     return isMatch(method, beanKey);
/*     */   }
/*     */   
/*     */   protected boolean isMatch(Method method, String beanKey) {
/* 115 */     if (this.methodMappings != null) {
/* 116 */       Set<String> methodNames = this.methodMappings.get(beanKey);
/* 117 */       if (methodNames != null) {
/* 118 */         return methodNames.contains(method.getName());
/*     */       }
/*     */     } 
/* 121 */     return (this.managedMethods != null && this.managedMethods.contains(method.getName()));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/assembler/MethodNameBasedMBeanInfoAssembler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */