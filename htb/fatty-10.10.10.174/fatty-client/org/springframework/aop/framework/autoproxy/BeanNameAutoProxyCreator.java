/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.PatternMatchUtils;
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
/*     */ public class BeanNameAutoProxyCreator
/*     */   extends AbstractAutoProxyCreator
/*     */ {
/*     */   @Nullable
/*     */   private List<String> beanNames;
/*     */   
/*     */   public void setBeanNames(String... beanNames) {
/*  66 */     Assert.notEmpty((Object[])beanNames, "'beanNames' must not be empty");
/*  67 */     this.beanNames = new ArrayList<>(beanNames.length);
/*  68 */     for (String mappedName : beanNames) {
/*  69 */       this.beanNames.add(StringUtils.trimWhitespace(mappedName));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, @Nullable TargetSource targetSource) {
/*  82 */     if (this.beanNames != null) {
/*  83 */       for (String mappedName : this.beanNames) {
/*  84 */         if (FactoryBean.class.isAssignableFrom(beanClass)) {
/*  85 */           if (!mappedName.startsWith("&")) {
/*     */             continue;
/*     */           }
/*  88 */           mappedName = mappedName.substring("&".length());
/*     */         } 
/*  90 */         if (isMatch(beanName, mappedName)) {
/*  91 */           return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
/*     */         }
/*  93 */         BeanFactory beanFactory = getBeanFactory();
/*  94 */         if (beanFactory != null) {
/*  95 */           String[] aliases = beanFactory.getAliases(beanName);
/*  96 */           for (String alias : aliases) {
/*  97 */             if (isMatch(alias, mappedName)) {
/*  98 */               return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 104 */     return DO_NOT_PROXY;
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
/*     */   protected boolean isMatch(String beanName, String mappedName) {
/* 117 */     return PatternMatchUtils.simpleMatch(mappedName, beanName);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/BeanNameAutoProxyCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */