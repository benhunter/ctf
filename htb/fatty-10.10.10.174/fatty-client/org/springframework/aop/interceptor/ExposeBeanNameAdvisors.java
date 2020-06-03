/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.aop.support.DefaultIntroductionAdvisor;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ import org.springframework.beans.factory.NamedBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ExposeBeanNameAdvisors
/*     */ {
/*  48 */   private static final String BEAN_NAME_ATTRIBUTE = ExposeBeanNameAdvisors.class.getName() + ".BEAN_NAME";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getBeanName() throws IllegalStateException {
/*  59 */     return getBeanName(ExposeInvocationInterceptor.currentInvocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getBeanName(MethodInvocation mi) throws IllegalStateException {
/*  70 */     if (!(mi instanceof ProxyMethodInvocation)) {
/*  71 */       throw new IllegalArgumentException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */     }
/*  73 */     ProxyMethodInvocation pmi = (ProxyMethodInvocation)mi;
/*  74 */     String beanName = (String)pmi.getUserAttribute(BEAN_NAME_ATTRIBUTE);
/*  75 */     if (beanName == null) {
/*  76 */       throw new IllegalStateException("Cannot get bean name; not set on MethodInvocation: " + mi);
/*     */     }
/*  78 */     return beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Advisor createAdvisorWithoutIntroduction(String beanName) {
/*  87 */     return (Advisor)new DefaultPointcutAdvisor((Advice)new ExposeBeanNameInterceptor(beanName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Advisor createAdvisorIntroducingNamedBean(String beanName) {
/*  97 */     return (Advisor)new DefaultIntroductionAdvisor((Advice)new ExposeBeanNameIntroduction(beanName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ExposeBeanNameInterceptor
/*     */     implements MethodInterceptor
/*     */   {
/*     */     private final String beanName;
/*     */ 
/*     */     
/*     */     public ExposeBeanNameInterceptor(String beanName) {
/* 109 */       this.beanName = beanName;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(MethodInvocation mi) throws Throwable {
/* 114 */       if (!(mi instanceof ProxyMethodInvocation)) {
/* 115 */         throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */       }
/* 117 */       ProxyMethodInvocation pmi = (ProxyMethodInvocation)mi;
/* 118 */       pmi.setUserAttribute(ExposeBeanNameAdvisors.BEAN_NAME_ATTRIBUTE, this.beanName);
/* 119 */       return mi.proceed();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ExposeBeanNameIntroduction
/*     */     extends DelegatingIntroductionInterceptor
/*     */     implements NamedBean
/*     */   {
/*     */     private final String beanName;
/*     */ 
/*     */     
/*     */     public ExposeBeanNameIntroduction(String beanName) {
/* 133 */       this.beanName = beanName;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(MethodInvocation mi) throws Throwable {
/* 138 */       if (!(mi instanceof ProxyMethodInvocation)) {
/* 139 */         throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */       }
/* 141 */       ProxyMethodInvocation pmi = (ProxyMethodInvocation)mi;
/* 142 */       pmi.setUserAttribute(ExposeBeanNameAdvisors.BEAN_NAME_ATTRIBUTE, this.beanName);
/* 143 */       return super.invoke(mi);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getBeanName() {
/* 148 */       return this.beanName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/interceptor/ExposeBeanNameAdvisors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */