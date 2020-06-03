/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aspectj.lang.annotation.After;
/*     */ import org.aspectj.lang.annotation.AfterReturning;
/*     */ import org.aspectj.lang.annotation.AfterThrowing;
/*     */ import org.aspectj.lang.annotation.Around;
/*     */ import org.aspectj.lang.annotation.Before;
/*     */ import org.aspectj.lang.annotation.DeclareParents;
/*     */ import org.aspectj.lang.annotation.Pointcut;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.aspectj.AspectJAfterAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAroundAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJExpressionPointcut;
/*     */ import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
/*     */ import org.springframework.aop.aspectj.DeclareParentsAdvisor;
/*     */ import org.springframework.aop.framework.AopConfigException;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.convert.converter.ConvertingComparator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.comparator.InstanceComparator;
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
/*     */ public class ReflectiveAspectJAdvisorFactory
/*     */   extends AbstractAspectJAdvisorFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static final Comparator<Method> METHOD_COMPARATOR;
/*     */   @Nullable
/*     */   private final BeanFactory beanFactory;
/*     */   
/*     */   static {
/*  75 */     ConvertingComparator<Method> convertingComparator = new ConvertingComparator((Comparator)new InstanceComparator(new Class[] { Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class }, ), method -> {
/*     */           AbstractAspectJAdvisorFactory.AspectJAnnotation<?> annotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(method);
/*     */ 
/*     */           
/*     */           return (annotation != null) ? (Annotation)annotation.getAnnotation() : null;
/*     */         });
/*     */ 
/*     */     
/*  83 */     ConvertingComparator convertingComparator1 = new ConvertingComparator(Method::getName);
/*  84 */     METHOD_COMPARATOR = convertingComparator.thenComparing((Comparator<? super Method>)convertingComparator1);
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
/*     */   public ReflectiveAspectJAdvisorFactory() {
/*  96 */     this((BeanFactory)null);
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
/*     */   public ReflectiveAspectJAdvisorFactory(@Nullable BeanFactory beanFactory) {
/* 109 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory aspectInstanceFactory) {
/* 115 */     Class<?> aspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
/* 116 */     String aspectName = aspectInstanceFactory.getAspectMetadata().getAspectName();
/* 117 */     validate(aspectClass);
/*     */ 
/*     */ 
/*     */     
/* 121 */     MetadataAwareAspectInstanceFactory lazySingletonAspectInstanceFactory = new LazySingletonAspectInstanceFactoryDecorator(aspectInstanceFactory);
/*     */ 
/*     */     
/* 124 */     List<Advisor> advisors = new ArrayList<>();
/* 125 */     for (Method method : getAdvisorMethods(aspectClass)) {
/* 126 */       Advisor advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, advisors.size(), aspectName);
/* 127 */       if (advisor != null) {
/* 128 */         advisors.add(advisor);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 133 */     if (!advisors.isEmpty() && lazySingletonAspectInstanceFactory.getAspectMetadata().isLazilyInstantiated()) {
/* 134 */       SyntheticInstantiationAdvisor syntheticInstantiationAdvisor = new SyntheticInstantiationAdvisor(lazySingletonAspectInstanceFactory);
/* 135 */       advisors.add(0, syntheticInstantiationAdvisor);
/*     */     } 
/*     */ 
/*     */     
/* 139 */     for (Field field : aspectClass.getDeclaredFields()) {
/* 140 */       Advisor advisor = getDeclareParentsAdvisor(field);
/* 141 */       if (advisor != null) {
/* 142 */         advisors.add(advisor);
/*     */       }
/*     */     } 
/*     */     
/* 146 */     return advisors;
/*     */   }
/*     */   
/*     */   private List<Method> getAdvisorMethods(Class<?> aspectClass) {
/* 150 */     List<Method> methods = new ArrayList<>();
/* 151 */     ReflectionUtils.doWithMethods(aspectClass, method -> {
/*     */           if (AnnotationUtils.getAnnotation(method, Pointcut.class) == null) {
/*     */             methods.add(method);
/*     */           }
/*     */         });
/*     */     
/* 157 */     methods.sort(METHOD_COMPARATOR);
/* 158 */     return methods;
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
/*     */   private Advisor getDeclareParentsAdvisor(Field introductionField) {
/* 170 */     DeclareParents declareParents = introductionField.<DeclareParents>getAnnotation(DeclareParents.class);
/* 171 */     if (declareParents == null)
/*     */     {
/* 173 */       return null;
/*     */     }
/*     */     
/* 176 */     if (DeclareParents.class == declareParents.defaultImpl()) {
/* 177 */       throw new IllegalStateException("'defaultImpl' attribute must be set on DeclareParents");
/*     */     }
/*     */     
/* 180 */     return (Advisor)new DeclareParentsAdvisor(introductionField
/* 181 */         .getType(), declareParents.value(), declareParents.defaultImpl());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Advisor getAdvisor(Method candidateAdviceMethod, MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrderInAspect, String aspectName) {
/* 190 */     validate(aspectInstanceFactory.getAspectMetadata().getAspectClass());
/*     */     
/* 192 */     AspectJExpressionPointcut expressionPointcut = getPointcut(candidateAdviceMethod, aspectInstanceFactory
/* 193 */         .getAspectMetadata().getAspectClass());
/* 194 */     if (expressionPointcut == null) {
/* 195 */       return null;
/*     */     }
/*     */     
/* 198 */     return (Advisor)new InstantiationModelAwarePointcutAdvisorImpl(expressionPointcut, candidateAdviceMethod, this, aspectInstanceFactory, declarationOrderInAspect, aspectName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private AspectJExpressionPointcut getPointcut(Method candidateAdviceMethod, Class<?> candidateAspectClass) {
/* 205 */     AbstractAspectJAdvisorFactory.AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
/* 206 */     if (aspectJAnnotation == null) {
/* 207 */       return null;
/*     */     }
/*     */     
/* 210 */     AspectJExpressionPointcut ajexp = new AspectJExpressionPointcut(candidateAspectClass, new String[0], new Class[0]);
/*     */     
/* 212 */     ajexp.setExpression(aspectJAnnotation.getPointcutExpression());
/* 213 */     if (this.beanFactory != null) {
/* 214 */       ajexp.setBeanFactory(this.beanFactory);
/*     */     }
/* 216 */     return ajexp; } @Nullable
/*     */   public Advice getAdvice(Method candidateAdviceMethod, AspectJExpressionPointcut expressionPointcut, MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrder, String aspectName) {
/*     */     AspectJAroundAdvice aspectJAroundAdvice;
/*     */     AspectJMethodBeforeAdvice aspectJMethodBeforeAdvice;
/*     */     AspectJAfterAdvice aspectJAfterAdvice;
/*     */     AspectJAfterReturningAdvice aspectJAfterReturningAdvice;
/*     */     AspectJAfterThrowingAdvice aspectJAfterThrowingAdvice;
/*     */     AfterReturning afterReturningAnnotation;
/*     */     AfterThrowing afterThrowingAnnotation;
/* 225 */     Class<?> candidateAspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
/* 226 */     validate(candidateAspectClass);
/*     */ 
/*     */     
/* 229 */     AbstractAspectJAdvisorFactory.AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
/* 230 */     if (aspectJAnnotation == null) {
/* 231 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 236 */     if (!isAspect(candidateAspectClass)) {
/* 237 */       throw new AopConfigException("Advice must be declared inside an aspect type: Offending method '" + candidateAdviceMethod + "' in class [" + candidateAspectClass
/*     */           
/* 239 */           .getName() + "]");
/*     */     }
/*     */     
/* 242 */     if (this.logger.isDebugEnabled()) {
/* 243 */       this.logger.debug("Found AspectJ method: " + candidateAdviceMethod);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 248 */     switch (aspectJAnnotation.getAnnotationType()) {
/*     */       case AtPointcut:
/* 250 */         if (this.logger.isDebugEnabled()) {
/* 251 */           this.logger.debug("Processing pointcut '" + candidateAdviceMethod.getName() + "'");
/*     */         }
/* 253 */         return null;
/*     */       case AtAround:
/* 255 */         aspectJAroundAdvice = new AspectJAroundAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */         break;
/*     */       
/*     */       case AtBefore:
/* 259 */         aspectJMethodBeforeAdvice = new AspectJMethodBeforeAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */         break;
/*     */       
/*     */       case AtAfter:
/* 263 */         aspectJAfterAdvice = new AspectJAfterAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */         break;
/*     */       
/*     */       case AtAfterReturning:
/* 267 */         aspectJAfterReturningAdvice = new AspectJAfterReturningAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */         
/* 269 */         afterReturningAnnotation = (AfterReturning)aspectJAnnotation.getAnnotation();
/* 270 */         if (StringUtils.hasText(afterReturningAnnotation.returning())) {
/* 271 */           aspectJAfterReturningAdvice.setReturningName(afterReturningAnnotation.returning());
/*     */         }
/*     */         break;
/*     */       case AtAfterThrowing:
/* 275 */         aspectJAfterThrowingAdvice = new AspectJAfterThrowingAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */         
/* 277 */         afterThrowingAnnotation = (AfterThrowing)aspectJAnnotation.getAnnotation();
/* 278 */         if (StringUtils.hasText(afterThrowingAnnotation.throwing())) {
/* 279 */           aspectJAfterThrowingAdvice.setThrowingName(afterThrowingAnnotation.throwing());
/*     */         }
/*     */         break;
/*     */       default:
/* 283 */         throw new UnsupportedOperationException("Unsupported advice type on method: " + candidateAdviceMethod);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 288 */     aspectJAfterThrowingAdvice.setAspectName(aspectName);
/* 289 */     aspectJAfterThrowingAdvice.setDeclarationOrder(declarationOrder);
/* 290 */     String[] argNames = this.parameterNameDiscoverer.getParameterNames(candidateAdviceMethod);
/* 291 */     if (argNames != null) {
/* 292 */       aspectJAfterThrowingAdvice.setArgumentNamesFromStringArray(argNames);
/*     */     }
/* 294 */     aspectJAfterThrowingAdvice.calculateArgumentBindings();
/*     */     
/* 296 */     return (Advice)aspectJAfterThrowingAdvice;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class SyntheticInstantiationAdvisor
/*     */     extends DefaultPointcutAdvisor
/*     */   {
/*     */     public SyntheticInstantiationAdvisor(MetadataAwareAspectInstanceFactory aif) {
/* 309 */       super(aif.getAspectMetadata().getPerClausePointcut(), (Advice)((method, args, target) -> aif.getAspectInstance()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/ReflectiveAspectJAdvisorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */