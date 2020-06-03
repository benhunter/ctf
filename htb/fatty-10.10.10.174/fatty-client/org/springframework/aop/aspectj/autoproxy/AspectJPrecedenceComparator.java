/*     */ package org.springframework.aop.aspectj.autoproxy;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.aspectj.AspectJAopUtils;
/*     */ import org.springframework.aop.aspectj.AspectJPrecedenceInformation;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.util.Assert;
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
/*     */ class AspectJPrecedenceComparator
/*     */   implements Comparator<Advisor>
/*     */ {
/*     */   private static final int HIGHER_PRECEDENCE = -1;
/*     */   private static final int SAME_PRECEDENCE = 0;
/*     */   private static final int LOWER_PRECEDENCE = 1;
/*     */   private final Comparator<? super Advisor> advisorComparator;
/*     */   
/*     */   public AspectJPrecedenceComparator() {
/*  65 */     this.advisorComparator = (Comparator<? super Advisor>)AnnotationAwareOrderComparator.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AspectJPrecedenceComparator(Comparator<? super Advisor> advisorComparator) {
/*  74 */     Assert.notNull(advisorComparator, "Advisor comparator must not be null");
/*  75 */     this.advisorComparator = advisorComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(Advisor o1, Advisor o2) {
/*  81 */     int advisorPrecedence = this.advisorComparator.compare(o1, o2);
/*  82 */     if (advisorPrecedence == 0 && declaredInSameAspect(o1, o2)) {
/*  83 */       advisorPrecedence = comparePrecedenceWithinAspect(o1, o2);
/*     */     }
/*  85 */     return advisorPrecedence;
/*     */   }
/*     */ 
/*     */   
/*     */   private int comparePrecedenceWithinAspect(Advisor advisor1, Advisor advisor2) {
/*  90 */     boolean oneOrOtherIsAfterAdvice = (AspectJAopUtils.isAfterAdvice(advisor1) || AspectJAopUtils.isAfterAdvice(advisor2));
/*  91 */     int adviceDeclarationOrderDelta = getAspectDeclarationOrder(advisor1) - getAspectDeclarationOrder(advisor2);
/*     */     
/*  93 */     if (oneOrOtherIsAfterAdvice) {
/*     */       
/*  95 */       if (adviceDeclarationOrderDelta < 0)
/*     */       {
/*     */         
/*  98 */         return 1;
/*     */       }
/* 100 */       if (adviceDeclarationOrderDelta == 0) {
/* 101 */         return 0;
/*     */       }
/*     */       
/* 104 */       return -1;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 109 */     if (adviceDeclarationOrderDelta < 0)
/*     */     {
/*     */       
/* 112 */       return -1;
/*     */     }
/* 114 */     if (adviceDeclarationOrderDelta == 0) {
/* 115 */       return 0;
/*     */     }
/*     */     
/* 118 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean declaredInSameAspect(Advisor advisor1, Advisor advisor2) {
/* 124 */     return (hasAspectName(advisor1) && hasAspectName(advisor2) && 
/* 125 */       getAspectName(advisor1).equals(getAspectName(advisor2)));
/*     */   }
/*     */   
/*     */   private boolean hasAspectName(Advisor anAdvisor) {
/* 129 */     return (anAdvisor instanceof AspectJPrecedenceInformation || anAdvisor
/* 130 */       .getAdvice() instanceof AspectJPrecedenceInformation);
/*     */   }
/*     */ 
/*     */   
/*     */   private String getAspectName(Advisor anAdvisor) {
/* 135 */     AspectJPrecedenceInformation pi = AspectJAopUtils.getAspectJPrecedenceInformationFor(anAdvisor);
/* 136 */     Assert.state((pi != null), "Unresolvable precedence information");
/* 137 */     return pi.getAspectName();
/*     */   }
/*     */ 
/*     */   
/*     */   private int getAspectDeclarationOrder(Advisor anAdvisor) {
/* 142 */     AspectJPrecedenceInformation precedenceInfo = AspectJAopUtils.getAspectJPrecedenceInformationFor(anAdvisor);
/* 143 */     if (precedenceInfo != null) {
/* 144 */       return precedenceInfo.getDeclarationOrder();
/*     */     }
/*     */     
/* 147 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/autoproxy/AspectJPrecedenceComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */