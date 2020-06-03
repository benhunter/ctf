/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.aspectj.AspectJExpressionPointcut;
/*     */ import org.springframework.aop.aspectj.AspectJPrecedenceInformation;
/*     */ import org.springframework.aop.aspectj.InstantiationModelAwarePointcutAdvisor;
/*     */ import org.springframework.aop.support.DynamicMethodMatcherPointcut;
/*     */ import org.springframework.aop.support.Pointcuts;
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
/*     */ final class InstantiationModelAwarePointcutAdvisorImpl
/*     */   implements InstantiationModelAwarePointcutAdvisor, AspectJPrecedenceInformation, Serializable
/*     */ {
/*  48 */   private static final Advice EMPTY_ADVICE = new Advice()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*     */   private final AspectJExpressionPointcut declaredPointcut;
/*     */   
/*     */   private final Class<?> declaringClass;
/*     */   
/*     */   private final String methodName;
/*     */   
/*     */   private final Class<?>[] parameterTypes;
/*     */   
/*     */   private transient Method aspectJAdviceMethod;
/*     */   
/*     */   private final AspectJAdvisorFactory aspectJAdvisorFactory;
/*     */   
/*     */   private final MetadataAwareAspectInstanceFactory aspectInstanceFactory;
/*     */   
/*     */   private final int declarationOrder;
/*     */   
/*     */   private final String aspectName;
/*     */   
/*     */   private final Pointcut pointcut;
/*     */   
/*     */   private final boolean lazy;
/*     */   
/*     */   @Nullable
/*     */   private Advice instantiatedAdvice;
/*     */   
/*     */   @Nullable
/*     */   private Boolean isBeforeAdvice;
/*     */   
/*     */   @Nullable
/*     */   private Boolean isAfterAdvice;
/*     */ 
/*     */   
/*     */   public InstantiationModelAwarePointcutAdvisorImpl(AspectJExpressionPointcut declaredPointcut, Method aspectJAdviceMethod, AspectJAdvisorFactory aspectJAdvisorFactory, MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrder, String aspectName) {
/*  87 */     this.declaredPointcut = declaredPointcut;
/*  88 */     this.declaringClass = aspectJAdviceMethod.getDeclaringClass();
/*  89 */     this.methodName = aspectJAdviceMethod.getName();
/*  90 */     this.parameterTypes = aspectJAdviceMethod.getParameterTypes();
/*  91 */     this.aspectJAdviceMethod = aspectJAdviceMethod;
/*  92 */     this.aspectJAdvisorFactory = aspectJAdvisorFactory;
/*  93 */     this.aspectInstanceFactory = aspectInstanceFactory;
/*  94 */     this.declarationOrder = declarationOrder;
/*  95 */     this.aspectName = aspectName;
/*     */     
/*  97 */     if (aspectInstanceFactory.getAspectMetadata().isLazilyInstantiated()) {
/*     */       
/*  99 */       Pointcut preInstantiationPointcut = Pointcuts.union(aspectInstanceFactory
/* 100 */           .getAspectMetadata().getPerClausePointcut(), (Pointcut)this.declaredPointcut);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 105 */       this.pointcut = (Pointcut)new PerTargetInstantiationModelPointcut(this.declaredPointcut, preInstantiationPointcut, aspectInstanceFactory);
/*     */       
/* 107 */       this.lazy = true;
/*     */     }
/*     */     else {
/*     */       
/* 111 */       this.pointcut = (Pointcut)this.declaredPointcut;
/* 112 */       this.lazy = false;
/* 113 */       this.instantiatedAdvice = instantiateAdvice(this.declaredPointcut);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pointcut getPointcut() {
/* 124 */     return this.pointcut;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLazy() {
/* 129 */     return this.lazy;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isAdviceInstantiated() {
/* 134 */     return (this.instantiatedAdvice != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Advice getAdvice() {
/* 142 */     if (this.instantiatedAdvice == null) {
/* 143 */       this.instantiatedAdvice = instantiateAdvice(this.declaredPointcut);
/*     */     }
/* 145 */     return this.instantiatedAdvice;
/*     */   }
/*     */   
/*     */   private Advice instantiateAdvice(AspectJExpressionPointcut pointcut) {
/* 149 */     Advice advice = this.aspectJAdvisorFactory.getAdvice(this.aspectJAdviceMethod, pointcut, this.aspectInstanceFactory, this.declarationOrder, this.aspectName);
/*     */     
/* 151 */     return (advice != null) ? advice : EMPTY_ADVICE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPerInstance() {
/* 161 */     return (getAspectMetadata().getAjType().getPerClause().getKind() != PerClauseKind.SINGLETON);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AspectMetadata getAspectMetadata() {
/* 168 */     return this.aspectInstanceFactory.getAspectMetadata();
/*     */   }
/*     */   
/*     */   public MetadataAwareAspectInstanceFactory getAspectInstanceFactory() {
/* 172 */     return this.aspectInstanceFactory;
/*     */   }
/*     */   
/*     */   public AspectJExpressionPointcut getDeclaredPointcut() {
/* 176 */     return this.declaredPointcut;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 181 */     return this.aspectInstanceFactory.getOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAspectName() {
/* 186 */     return this.aspectName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeclarationOrder() {
/* 191 */     return this.declarationOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBeforeAdvice() {
/* 196 */     if (this.isBeforeAdvice == null) {
/* 197 */       determineAdviceType();
/*     */     }
/* 199 */     return this.isBeforeAdvice.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAfterAdvice() {
/* 204 */     if (this.isAfterAdvice == null) {
/* 205 */       determineAdviceType();
/*     */     }
/* 207 */     return this.isAfterAdvice.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void determineAdviceType() {
/* 216 */     AbstractAspectJAdvisorFactory.AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(this.aspectJAdviceMethod);
/* 217 */     if (aspectJAnnotation == null) {
/* 218 */       this.isBeforeAdvice = Boolean.valueOf(false);
/* 219 */       this.isAfterAdvice = Boolean.valueOf(false);
/*     */     } else {
/*     */       
/* 222 */       switch (aspectJAnnotation.getAnnotationType()) {
/*     */         case AtPointcut:
/*     */         case AtAround:
/* 225 */           this.isBeforeAdvice = Boolean.valueOf(false);
/* 226 */           this.isAfterAdvice = Boolean.valueOf(false);
/*     */           break;
/*     */         case AtBefore:
/* 229 */           this.isBeforeAdvice = Boolean.valueOf(true);
/* 230 */           this.isAfterAdvice = Boolean.valueOf(false);
/*     */           break;
/*     */         case AtAfter:
/*     */         case AtAfterReturning:
/*     */         case AtAfterThrowing:
/* 235 */           this.isBeforeAdvice = Boolean.valueOf(false);
/* 236 */           this.isAfterAdvice = Boolean.valueOf(true);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 244 */     inputStream.defaultReadObject();
/*     */     try {
/* 246 */       this.aspectJAdviceMethod = this.declaringClass.getMethod(this.methodName, this.parameterTypes);
/*     */     }
/* 248 */     catch (NoSuchMethodException ex) {
/* 249 */       throw new IllegalStateException("Failed to find advice method on deserialization", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 255 */     return "InstantiationModelAwarePointcutAdvisor: expression [" + getDeclaredPointcut().getExpression() + "]; advice method [" + this.aspectJAdviceMethod + "]; perClauseKind=" + this.aspectInstanceFactory
/*     */       
/* 257 */       .getAspectMetadata().getAjType().getPerClause().getKind();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class PerTargetInstantiationModelPointcut
/*     */     extends DynamicMethodMatcherPointcut
/*     */   {
/*     */     private final AspectJExpressionPointcut declaredPointcut;
/*     */ 
/*     */     
/*     */     private final Pointcut preInstantiationPointcut;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private LazySingletonAspectInstanceFactoryDecorator aspectInstanceFactory;
/*     */ 
/*     */ 
/*     */     
/*     */     public PerTargetInstantiationModelPointcut(AspectJExpressionPointcut declaredPointcut, Pointcut preInstantiationPointcut, MetadataAwareAspectInstanceFactory aspectInstanceFactory) {
/* 278 */       this.declaredPointcut = declaredPointcut;
/* 279 */       this.preInstantiationPointcut = preInstantiationPointcut;
/* 280 */       if (aspectInstanceFactory instanceof LazySingletonAspectInstanceFactoryDecorator) {
/* 281 */         this.aspectInstanceFactory = (LazySingletonAspectInstanceFactoryDecorator)aspectInstanceFactory;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass) {
/* 289 */       return ((isAspectMaterialized() && this.declaredPointcut.matches(method, targetClass)) || this.preInstantiationPointcut
/* 290 */         .getMethodMatcher().matches(method, targetClass));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, Object... args) {
/* 296 */       return (isAspectMaterialized() && this.declaredPointcut.matches(method, targetClass));
/*     */     }
/*     */     
/*     */     private boolean isAspectMaterialized() {
/* 300 */       return (this.aspectInstanceFactory == null || this.aspectInstanceFactory.isMaterialized());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/InstantiationModelAwarePointcutAdvisorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */