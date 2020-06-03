/*    */ package org.springframework.aop.framework.adapter;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.springframework.aop.Advisor;
/*    */ import org.springframework.aop.support.DefaultPointcutAdvisor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultAdvisorAdapterRegistry
/*    */   implements AdvisorAdapterRegistry, Serializable
/*    */ {
/* 43 */   private final List<AdvisorAdapter> adapters = new ArrayList<>(3);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultAdvisorAdapterRegistry() {
/* 50 */     registerAdvisorAdapter(new MethodBeforeAdviceAdapter());
/* 51 */     registerAdvisorAdapter(new AfterReturningAdviceAdapter());
/* 52 */     registerAdvisorAdapter(new ThrowsAdviceAdapter());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Advisor wrap(Object adviceObject) throws UnknownAdviceTypeException {
/* 58 */     if (adviceObject instanceof Advisor) {
/* 59 */       return (Advisor)adviceObject;
/*    */     }
/* 61 */     if (!(adviceObject instanceof Advice)) {
/* 62 */       throw new UnknownAdviceTypeException(adviceObject);
/*    */     }
/* 64 */     Advice advice = (Advice)adviceObject;
/* 65 */     if (advice instanceof MethodInterceptor)
/*    */     {
/* 67 */       return (Advisor)new DefaultPointcutAdvisor(advice);
/*    */     }
/* 69 */     for (AdvisorAdapter adapter : this.adapters) {
/*    */       
/* 71 */       if (adapter.supportsAdvice(advice)) {
/* 72 */         return (Advisor)new DefaultPointcutAdvisor(advice);
/*    */       }
/*    */     } 
/* 75 */     throw new UnknownAdviceTypeException(advice);
/*    */   }
/*    */ 
/*    */   
/*    */   public MethodInterceptor[] getInterceptors(Advisor advisor) throws UnknownAdviceTypeException {
/* 80 */     List<MethodInterceptor> interceptors = new ArrayList<>(3);
/* 81 */     Advice advice = advisor.getAdvice();
/* 82 */     if (advice instanceof MethodInterceptor) {
/* 83 */       interceptors.add((MethodInterceptor)advice);
/*    */     }
/* 85 */     for (AdvisorAdapter adapter : this.adapters) {
/* 86 */       if (adapter.supportsAdvice(advice)) {
/* 87 */         interceptors.add(adapter.getInterceptor(advisor));
/*    */       }
/*    */     } 
/* 90 */     if (interceptors.isEmpty()) {
/* 91 */       throw new UnknownAdviceTypeException(advisor.getAdvice());
/*    */     }
/* 93 */     return interceptors.<MethodInterceptor>toArray(new MethodInterceptor[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerAdvisorAdapter(AdvisorAdapter adapter) {
/* 98 */     this.adapters.add(adapter);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/adapter/DefaultAdvisorAdapterRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */