/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomScopeConfigurer
/*     */   implements BeanFactoryPostProcessor, BeanClassLoaderAware, Ordered
/*     */ {
/*     */   @Nullable
/*     */   private Map<String, Object> scopes;
/*  52 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */   @Nullable
/*  55 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopes(Map<String, Object> scopes) {
/*  65 */     this.scopes = scopes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addScope(String scopeName, Scope scope) {
/*  75 */     if (this.scopes == null) {
/*  76 */       this.scopes = new LinkedHashMap<>(1);
/*     */     }
/*  78 */     this.scopes.put(scopeName, scope);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/*  83 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  88 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(@Nullable ClassLoader beanClassLoader) {
/*  93 */     this.beanClassLoader = beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/*  99 */     if (this.scopes != null)
/* 100 */       this.scopes.forEach((scopeKey, value) -> {
/*     */             if (value instanceof Scope) {
/*     */               beanFactory.registerScope(scopeKey, (Scope)value);
/*     */             } else if (value instanceof Class) {
/*     */               Class<?> scopeClass = (Class)value;
/*     */               Assert.isAssignable(Scope.class, scopeClass, "Invalid scope class");
/*     */               beanFactory.registerScope(scopeKey, (Scope)BeanUtils.instantiateClass(scopeClass));
/*     */             } else if (value instanceof String) {
/*     */               Class<?> scopeClass = ClassUtils.resolveClassName((String)value, this.beanClassLoader);
/*     */               Assert.isAssignable(Scope.class, scopeClass, "Invalid scope class");
/*     */               beanFactory.registerScope(scopeKey, (Scope)BeanUtils.instantiateClass(scopeClass));
/*     */             } else {
/*     */               throw new IllegalArgumentException("Mapped value [" + value + "] for scope key [" + scopeKey + "] is not an instance of required type [" + Scope.class.getName() + "] or a corresponding Class or String value indicating a Scope implementation");
/*     */             } 
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/CustomScopeConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */