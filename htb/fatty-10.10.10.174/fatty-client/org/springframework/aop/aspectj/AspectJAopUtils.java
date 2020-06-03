/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.Advisor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AspectJAopUtils
/*    */ {
/*    */   public static boolean isBeforeAdvice(Advisor anAdvisor) {
/* 39 */     AspectJPrecedenceInformation precedenceInfo = getAspectJPrecedenceInformationFor(anAdvisor);
/* 40 */     if (precedenceInfo != null) {
/* 41 */       return precedenceInfo.isBeforeAdvice();
/*    */     }
/* 43 */     return anAdvisor.getAdvice() instanceof org.springframework.aop.BeforeAdvice;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isAfterAdvice(Advisor anAdvisor) {
/* 50 */     AspectJPrecedenceInformation precedenceInfo = getAspectJPrecedenceInformationFor(anAdvisor);
/* 51 */     if (precedenceInfo != null) {
/* 52 */       return precedenceInfo.isAfterAdvice();
/*    */     }
/* 54 */     return anAdvisor.getAdvice() instanceof org.springframework.aop.AfterAdvice;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static AspectJPrecedenceInformation getAspectJPrecedenceInformationFor(Advisor anAdvisor) {
/* 64 */     if (anAdvisor instanceof AspectJPrecedenceInformation) {
/* 65 */       return (AspectJPrecedenceInformation)anAdvisor;
/*    */     }
/* 67 */     Advice advice = anAdvisor.getAdvice();
/* 68 */     if (advice instanceof AspectJPrecedenceInformation) {
/* 69 */       return (AspectJPrecedenceInformation)advice;
/*    */     }
/* 71 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJAopUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */