/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import org.springframework.aop.ClassFilter;
/*    */ import org.springframework.aop.Pointcut;
/*    */ import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeanFactoryCacheOperationSourceAdvisor
/*    */   extends AbstractBeanFactoryPointcutAdvisor
/*    */ {
/*    */   @Nullable
/*    */   private CacheOperationSource cacheOperationSource;
/*    */   
/* 37 */   private final CacheOperationSourcePointcut pointcut = new CacheOperationSourcePointcut()
/*    */     {
/*    */       @Nullable
/*    */       protected CacheOperationSource getCacheOperationSource() {
/* 41 */         return BeanFactoryCacheOperationSourceAdvisor.this.cacheOperationSource;
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCacheOperationSource(CacheOperationSource cacheOperationSource) {
/* 52 */     this.cacheOperationSource = cacheOperationSource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClassFilter(ClassFilter classFilter) {
/* 60 */     this.pointcut.setClassFilter(classFilter);
/*    */   }
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 65 */     return (Pointcut)this.pointcut;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/BeanFactoryCacheOperationSourceAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */