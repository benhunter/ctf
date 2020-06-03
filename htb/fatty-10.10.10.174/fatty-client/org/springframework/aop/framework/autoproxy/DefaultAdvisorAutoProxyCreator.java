/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultAdvisorAutoProxyCreator
/*     */   extends AbstractAdvisorAutoProxyCreator
/*     */   implements BeanNameAware
/*     */ {
/*     */   public static final String SEPARATOR = ".";
/*     */   private boolean usePrefix = false;
/*     */   @Nullable
/*     */   private String advisorBeanNamePrefix;
/*     */   
/*     */   public void setUsePrefix(boolean usePrefix) {
/*  57 */     this.usePrefix = usePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUsePrefix() {
/*  64 */     return this.usePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvisorBeanNamePrefix(@Nullable String advisorBeanNamePrefix) {
/*  74 */     this.advisorBeanNamePrefix = advisorBeanNamePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getAdvisorBeanNamePrefix() {
/*  83 */     return this.advisorBeanNamePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanName(String name) {
/*  89 */     if (this.advisorBeanNamePrefix == null) {
/*  90 */       this.advisorBeanNamePrefix = name + ".";
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
/*     */   protected boolean isEligibleAdvisorBean(String beanName) {
/* 102 */     if (!isUsePrefix()) {
/* 103 */       return true;
/*     */     }
/* 105 */     String prefix = getAdvisorBeanNamePrefix();
/* 106 */     return (prefix != null && beanName.startsWith(prefix));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/DefaultAdvisorAutoProxyCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */