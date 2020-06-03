/*     */ package org.springframework.aop.aspectj.autoproxy;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aspectj.util.PartialOrder;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.aspectj.AbstractAspectJAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
/*     */ import org.springframework.aop.aspectj.AspectJProxyUtils;
/*     */ import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
/*     */ import org.springframework.core.Ordered;
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
/*     */ public class AspectJAwareAdvisorAutoProxyCreator
/*     */   extends AbstractAdvisorAutoProxyCreator
/*     */ {
/*  49 */   private static final Comparator<Advisor> DEFAULT_PRECEDENCE_COMPARATOR = new AspectJPrecedenceComparator();
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
/*     */   protected List<Advisor> sortAdvisors(List<Advisor> advisors) {
/*  70 */     List<PartiallyComparableAdvisorHolder> partiallyComparableAdvisors = new ArrayList<>(advisors.size());
/*  71 */     for (Advisor element : advisors) {
/*  72 */       partiallyComparableAdvisors.add(new PartiallyComparableAdvisorHolder(element, DEFAULT_PRECEDENCE_COMPARATOR));
/*     */     }
/*     */     
/*  75 */     List<PartiallyComparableAdvisorHolder> sorted = PartialOrder.sort(partiallyComparableAdvisors);
/*  76 */     if (sorted != null) {
/*  77 */       List<Advisor> result = new ArrayList<>(advisors.size());
/*  78 */       for (PartiallyComparableAdvisorHolder pcAdvisor : sorted) {
/*  79 */         result.add(pcAdvisor.getAdvisor());
/*     */       }
/*  81 */       return result;
/*     */     } 
/*     */     
/*  84 */     return super.sortAdvisors(advisors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendAdvisors(List<Advisor> candidateAdvisors) {
/*  95 */     AspectJProxyUtils.makeAdvisorChainAspectJCapableIfNecessary(candidateAdvisors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldSkip(Class<?> beanClass, String beanName) {
/* 101 */     List<Advisor> candidateAdvisors = findCandidateAdvisors();
/* 102 */     for (Advisor advisor : candidateAdvisors) {
/* 103 */       if (advisor instanceof AspectJPointcutAdvisor && ((AspectJPointcutAdvisor)advisor)
/* 104 */         .getAspectName().equals(beanName)) {
/* 105 */         return true;
/*     */       }
/*     */     } 
/* 108 */     return super.shouldSkip(beanClass, beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PartiallyComparableAdvisorHolder
/*     */     implements PartialOrder.PartialComparable
/*     */   {
/*     */     private final Advisor advisor;
/*     */     
/*     */     private final Comparator<Advisor> comparator;
/*     */ 
/*     */     
/*     */     public PartiallyComparableAdvisorHolder(Advisor advisor, Comparator<Advisor> comparator) {
/* 122 */       this.advisor = advisor;
/* 123 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Object obj) {
/* 128 */       Advisor otherAdvisor = ((PartiallyComparableAdvisorHolder)obj).advisor;
/* 129 */       return this.comparator.compare(this.advisor, otherAdvisor);
/*     */     }
/*     */ 
/*     */     
/*     */     public int fallbackCompareTo(Object obj) {
/* 134 */       return 0;
/*     */     }
/*     */     
/*     */     public Advisor getAdvisor() {
/* 138 */       return this.advisor;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 143 */       StringBuilder sb = new StringBuilder();
/* 144 */       Advice advice = this.advisor.getAdvice();
/* 145 */       sb.append(ClassUtils.getShortName(advice.getClass()));
/* 146 */       sb.append(": ");
/* 147 */       if (this.advisor instanceof Ordered) {
/* 148 */         sb.append("order ").append(((Ordered)this.advisor).getOrder()).append(", ");
/*     */       }
/* 150 */       if (advice instanceof AbstractAspectJAdvice) {
/* 151 */         AbstractAspectJAdvice ajAdvice = (AbstractAspectJAdvice)advice;
/* 152 */         sb.append(ajAdvice.getAspectName());
/* 153 */         sb.append(", declaration order ");
/* 154 */         sb.append(ajAdvice.getDeclarationOrder());
/*     */       } 
/* 156 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/autoproxy/AspectJAwareAdvisorAutoProxyCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */