/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.aop.Advisor;
/*    */ import org.springframework.aop.PointcutAdvisor;
/*    */ import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AspectJProxyUtils
/*    */ {
/*    */   public static boolean makeAdvisorChainAspectJCapableIfNecessary(List<Advisor> advisors) {
/* 47 */     if (!advisors.isEmpty()) {
/* 48 */       boolean foundAspectJAdvice = false;
/* 49 */       for (Advisor advisor : advisors) {
/*    */ 
/*    */         
/* 52 */         if (isAspectJAdvice(advisor)) {
/* 53 */           foundAspectJAdvice = true;
/*    */           break;
/*    */         } 
/*    */       } 
/* 57 */       if (foundAspectJAdvice && !advisors.contains(ExposeInvocationInterceptor.ADVISOR)) {
/* 58 */         advisors.add(0, ExposeInvocationInterceptor.ADVISOR);
/* 59 */         return true;
/*    */       } 
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isAspectJAdvice(Advisor advisor) {
/* 70 */     return (advisor instanceof InstantiationModelAwarePointcutAdvisor || advisor
/* 71 */       .getAdvice() instanceof AbstractAspectJAdvice || (advisor instanceof PointcutAdvisor && ((PointcutAdvisor)advisor)
/*    */       
/* 73 */       .getPointcut() instanceof AspectJExpressionPointcut));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJProxyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */