/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.aspectj.util.FuzzyBoolean;
/*     */ import org.aspectj.weaver.patterns.NamePattern;
/*     */ import org.aspectj.weaver.reflect.ReflectionWorld;
/*     */ import org.aspectj.weaver.reflect.ShadowMatchImpl;
/*     */ import org.aspectj.weaver.tools.ContextBasedMatcher;
/*     */ import org.aspectj.weaver.tools.FuzzyBoolean;
/*     */ import org.aspectj.weaver.tools.JoinPointMatch;
/*     */ import org.aspectj.weaver.tools.MatchingContext;
/*     */ import org.aspectj.weaver.tools.PointcutDesignatorHandler;
/*     */ import org.aspectj.weaver.tools.PointcutExpression;
/*     */ import org.aspectj.weaver.tools.PointcutParameter;
/*     */ import org.aspectj.weaver.tools.PointcutParser;
/*     */ import org.aspectj.weaver.tools.PointcutPrimitive;
/*     */ import org.aspectj.weaver.tools.ShadowMatch;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.IntroductionAwareMethodMatcher;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.aop.framework.autoproxy.ProxyCreationContext;
/*     */ import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
/*     */ import org.springframework.aop.support.AbstractExpressionPointcut;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class AspectJExpressionPointcut
/*     */   extends AbstractExpressionPointcut
/*     */   implements ClassFilter, IntroductionAwareMethodMatcher, BeanFactoryAware
/*     */ {
/*  88 */   private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();
/*     */   
/*     */   static {
/*  91 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
/*  92 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
/*  93 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
/*  94 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
/*  95 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
/*  96 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
/*  97 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
/*  98 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
/*  99 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
/* 100 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
/*     */   }
/*     */ 
/*     */   
/* 104 */   private static final Log logger = LogFactory.getLog(AspectJExpressionPointcut.class);
/*     */   
/*     */   @Nullable
/*     */   private Class<?> pointcutDeclarationScope;
/*     */   
/* 109 */   private String[] pointcutParameterNames = new String[0];
/*     */   
/* 111 */   private Class<?>[] pointcutParameterTypes = new Class[0];
/*     */   
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   @Nullable
/*     */   private transient ClassLoader pointcutClassLoader;
/*     */   
/*     */   @Nullable
/*     */   private transient PointcutExpression pointcutExpression;
/*     */   
/* 122 */   private transient Map<Method, ShadowMatch> shadowMatchCache = new ConcurrentHashMap<>(32);
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
/*     */   public AspectJExpressionPointcut(Class<?> declarationScope, String[] paramNames, Class<?>[] paramTypes) {
/* 138 */     this.pointcutDeclarationScope = declarationScope;
/* 139 */     if (paramNames.length != paramTypes.length) {
/* 140 */       throw new IllegalStateException("Number of pointcut parameter names must match number of pointcut parameter types");
/*     */     }
/*     */     
/* 143 */     this.pointcutParameterNames = paramNames;
/* 144 */     this.pointcutParameterTypes = paramTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPointcutDeclarationScope(Class<?> pointcutDeclarationScope) {
/* 152 */     this.pointcutDeclarationScope = pointcutDeclarationScope;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterNames(String... names) {
/* 159 */     this.pointcutParameterNames = names;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterTypes(Class<?>... types) {
/* 166 */     this.pointcutParameterTypes = types;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 171 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFilter getClassFilter() {
/* 177 */     obtainPointcutExpression();
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodMatcher getMethodMatcher() {
/* 183 */     obtainPointcutExpression();
/* 184 */     return (MethodMatcher)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PointcutExpression obtainPointcutExpression() {
/* 193 */     if (getExpression() == null) {
/* 194 */       throw new IllegalStateException("Must set property 'expression' before attempting to match");
/*     */     }
/* 196 */     if (this.pointcutExpression == null) {
/* 197 */       this.pointcutClassLoader = determinePointcutClassLoader();
/* 198 */       this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
/*     */     } 
/* 200 */     return this.pointcutExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ClassLoader determinePointcutClassLoader() {
/* 208 */     if (this.beanFactory instanceof ConfigurableBeanFactory) {
/* 209 */       return ((ConfigurableBeanFactory)this.beanFactory).getBeanClassLoader();
/*     */     }
/* 211 */     if (this.pointcutDeclarationScope != null) {
/* 212 */       return this.pointcutDeclarationScope.getClassLoader();
/*     */     }
/* 214 */     return ClassUtils.getDefaultClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PointcutExpression buildPointcutExpression(@Nullable ClassLoader classLoader) {
/* 221 */     PointcutParser parser = initializePointcutParser(classLoader);
/* 222 */     PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
/* 223 */     for (int i = 0; i < pointcutParameters.length; i++) {
/* 224 */       pointcutParameters[i] = parser.createPointcutParameter(this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
/*     */     }
/*     */     
/* 227 */     return parser.parsePointcutExpression(replaceBooleanOperators(resolveExpression()), this.pointcutDeclarationScope, pointcutParameters);
/*     */   }
/*     */ 
/*     */   
/*     */   private String resolveExpression() {
/* 232 */     String expression = getExpression();
/* 233 */     Assert.state((expression != null), "No expression set");
/* 234 */     return expression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PointcutParser initializePointcutParser(@Nullable ClassLoader classLoader) {
/* 242 */     PointcutParser parser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, classLoader);
/*     */     
/* 244 */     parser.registerPointcutDesignatorHandler(new BeanPointcutDesignatorHandler());
/* 245 */     return parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String replaceBooleanOperators(String pcExpr) {
/* 256 */     String result = StringUtils.replace(pcExpr, " and ", " && ");
/* 257 */     result = StringUtils.replace(result, " or ", " || ");
/* 258 */     result = StringUtils.replace(result, " not ", " ! ");
/* 259 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PointcutExpression getPointcutExpression() {
/* 267 */     return obtainPointcutExpression();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Class<?> targetClass) {
/* 272 */     PointcutExpression pointcutExpression = obtainPointcutExpression();
/*     */     
/*     */     try {
/* 275 */       return pointcutExpression.couldMatchJoinPointsInType(targetClass);
/*     */     }
/* 277 */     catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex) {
/* 278 */       logger.debug("PointcutExpression matching rejected target class - trying fallback expression", (Throwable)ex);
/*     */       
/* 280 */       PointcutExpression fallbackExpression = getFallbackPointcutExpression(targetClass);
/* 281 */       if (fallbackExpression != null) {
/* 282 */         return fallbackExpression.couldMatchJoinPointsInType(targetClass);
/*     */       
/*     */       }
/*     */     }
/* 286 */     catch (Throwable ex) {
/* 287 */       logger.debug("PointcutExpression matching rejected target class", ex);
/*     */     } 
/* 289 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass, boolean hasIntroductions) {
/* 294 */     obtainPointcutExpression();
/* 295 */     ShadowMatch shadowMatch = getTargetShadowMatch(method, targetClass);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     if (shadowMatch.alwaysMatches()) {
/* 301 */       return true;
/*     */     }
/* 303 */     if (shadowMatch.neverMatches()) {
/* 304 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 308 */     if (hasIntroductions) {
/* 309 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     RuntimeTestWalker walker = getRuntimeTestWalker(shadowMatch);
/* 316 */     return (!walker.testsSubtypeSensitiveVars() || walker.testTargetInstanceOfResidue(targetClass));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass) {
/* 322 */     return matches(method, targetClass, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRuntime() {
/* 327 */     return obtainPointcutExpression().mayNeedDynamicTest();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass, Object... args) {
/* 332 */     obtainPointcutExpression();
/* 333 */     ShadowMatch shadowMatch = getTargetShadowMatch(method, targetClass);
/*     */ 
/*     */ 
/*     */     
/* 337 */     ProxyMethodInvocation pmi = null;
/* 338 */     Object targetObject = null;
/* 339 */     Object thisObject = null;
/*     */     try {
/* 341 */       MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
/* 342 */       targetObject = mi.getThis();
/* 343 */       if (!(mi instanceof ProxyMethodInvocation)) {
/* 344 */         throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */       }
/* 346 */       pmi = (ProxyMethodInvocation)mi;
/* 347 */       thisObject = pmi.getProxy();
/*     */     }
/* 349 */     catch (IllegalStateException ex) {
/*     */       
/* 351 */       if (logger.isDebugEnabled()) {
/* 352 */         logger.debug("Could not access current invocation - matching with limited context: " + ex);
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 357 */       JoinPointMatch joinPointMatch = shadowMatch.matchesJoinPoint(thisObject, targetObject, args);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 367 */       if (pmi != null && thisObject != null) {
/* 368 */         RuntimeTestWalker originalMethodResidueTest = getRuntimeTestWalker(getShadowMatch(method, method));
/* 369 */         if (!originalMethodResidueTest.testThisInstanceOfResidue(thisObject.getClass())) {
/* 370 */           return false;
/*     */         }
/* 372 */         if (joinPointMatch.matches()) {
/* 373 */           bindParameters(pmi, joinPointMatch);
/*     */         }
/*     */       } 
/*     */       
/* 377 */       return joinPointMatch.matches();
/*     */     }
/* 379 */     catch (Throwable ex) {
/* 380 */       if (logger.isDebugEnabled()) {
/* 381 */         logger.debug("Failed to evaluate join point for arguments " + Arrays.<Object>asList(args) + " - falling back to non-match", ex);
/*     */       }
/*     */       
/* 384 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected String getCurrentProxiedBeanName() {
/* 390 */     return ProxyCreationContext.getCurrentProxiedBeanName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private PointcutExpression getFallbackPointcutExpression(Class<?> targetClass) {
/*     */     try {
/* 400 */       ClassLoader classLoader = targetClass.getClassLoader();
/* 401 */       if (classLoader != null && classLoader != this.pointcutClassLoader) {
/* 402 */         return buildPointcutExpression(classLoader);
/*     */       }
/*     */     }
/* 405 */     catch (Throwable ex) {
/* 406 */       logger.debug("Failed to create fallback PointcutExpression", ex);
/*     */     } 
/* 408 */     return null;
/*     */   }
/*     */   
/*     */   private RuntimeTestWalker getRuntimeTestWalker(ShadowMatch shadowMatch) {
/* 412 */     if (shadowMatch instanceof DefensiveShadowMatch) {
/* 413 */       return new RuntimeTestWalker(((DefensiveShadowMatch)shadowMatch).primary);
/*     */     }
/* 415 */     return new RuntimeTestWalker(shadowMatch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void bindParameters(ProxyMethodInvocation invocation, JoinPointMatch jpm) {
/* 425 */     invocation.setUserAttribute(resolveExpression(), jpm);
/*     */   }
/*     */   
/*     */   private ShadowMatch getTargetShadowMatch(Method method, Class<?> targetClass) {
/* 429 */     Method targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
/* 430 */     if (targetMethod.getDeclaringClass().isInterface()) {
/*     */ 
/*     */ 
/*     */       
/* 434 */       Set<Class<?>> ifcs = ClassUtils.getAllInterfacesForClassAsSet(targetClass);
/* 435 */       if (ifcs.size() > 1) {
/*     */         try {
/* 437 */           Class<?> compositeInterface = ClassUtils.createCompositeInterface(
/* 438 */               ClassUtils.toClassArray(ifcs), targetClass.getClassLoader());
/* 439 */           targetMethod = ClassUtils.getMostSpecificMethod(targetMethod, compositeInterface);
/*     */         }
/* 441 */         catch (IllegalArgumentException illegalArgumentException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 447 */     return getShadowMatch(targetMethod, method);
/*     */   }
/*     */ 
/*     */   
/*     */   private ShadowMatch getShadowMatch(Method targetMethod, Method originalMethod) {
/* 452 */     ShadowMatch shadowMatch = this.shadowMatchCache.get(targetMethod);
/* 453 */     if (shadowMatch == null) {
/* 454 */       synchronized (this.shadowMatchCache) {
/*     */         
/* 456 */         PointcutExpression fallbackExpression = null;
/* 457 */         shadowMatch = this.shadowMatchCache.get(targetMethod);
/* 458 */         if (shadowMatch == null) {
/* 459 */           ShadowMatchImpl shadowMatchImpl; Method methodToMatch = targetMethod;
/*     */           try {
/*     */             try {
/* 462 */               shadowMatch = obtainPointcutExpression().matchesMethodExecution(methodToMatch);
/*     */             }
/* 464 */             catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex) {
/*     */ 
/*     */               
/*     */               try {
/* 468 */                 fallbackExpression = getFallbackPointcutExpression(methodToMatch.getDeclaringClass());
/* 469 */                 if (fallbackExpression != null) {
/* 470 */                   shadowMatch = fallbackExpression.matchesMethodExecution(methodToMatch);
/*     */                 }
/*     */               }
/* 473 */               catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex2) {
/* 474 */                 fallbackExpression = null;
/*     */               } 
/*     */             } 
/* 477 */             if (targetMethod != originalMethod && (shadowMatch == null || (shadowMatch
/* 478 */               .neverMatches() && Proxy.isProxyClass(targetMethod.getDeclaringClass())))) {
/*     */ 
/*     */ 
/*     */               
/* 482 */               methodToMatch = originalMethod;
/*     */               try {
/* 484 */                 shadowMatch = obtainPointcutExpression().matchesMethodExecution(methodToMatch);
/*     */               }
/* 486 */               catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex) {
/*     */ 
/*     */                 
/*     */                 try {
/* 490 */                   fallbackExpression = getFallbackPointcutExpression(methodToMatch.getDeclaringClass());
/* 491 */                   if (fallbackExpression != null) {
/* 492 */                     shadowMatch = fallbackExpression.matchesMethodExecution(methodToMatch);
/*     */                   }
/*     */                 }
/* 495 */                 catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex2) {
/* 496 */                   fallbackExpression = null;
/*     */                 }
/*     */               
/*     */               } 
/*     */             } 
/* 501 */           } catch (Throwable ex) {
/*     */             
/* 503 */             logger.debug("PointcutExpression matching rejected target method", ex);
/* 504 */             fallbackExpression = null;
/*     */           } 
/* 506 */           if (shadowMatch == null) {
/* 507 */             shadowMatchImpl = new ShadowMatchImpl(FuzzyBoolean.NO, null, null, null);
/*     */           }
/* 509 */           else if (shadowMatchImpl.maybeMatches() && fallbackExpression != null) {
/*     */             
/* 511 */             shadowMatch = new DefensiveShadowMatch((ShadowMatch)shadowMatchImpl, fallbackExpression.matchesMethodExecution(methodToMatch));
/*     */           } 
/* 513 */           this.shadowMatchCache.put(targetMethod, shadowMatch);
/*     */         } 
/*     */       } 
/*     */     }
/* 517 */     return shadowMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 523 */     if (this == other) {
/* 524 */       return true;
/*     */     }
/* 526 */     if (!(other instanceof AspectJExpressionPointcut)) {
/* 527 */       return false;
/*     */     }
/* 529 */     AspectJExpressionPointcut otherPc = (AspectJExpressionPointcut)other;
/* 530 */     return (ObjectUtils.nullSafeEquals(getExpression(), otherPc.getExpression()) && 
/* 531 */       ObjectUtils.nullSafeEquals(this.pointcutDeclarationScope, otherPc.pointcutDeclarationScope) && 
/* 532 */       ObjectUtils.nullSafeEquals(this.pointcutParameterNames, otherPc.pointcutParameterNames) && 
/* 533 */       ObjectUtils.nullSafeEquals(this.pointcutParameterTypes, otherPc.pointcutParameterTypes));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 538 */     int hashCode = ObjectUtils.nullSafeHashCode(getExpression());
/* 539 */     hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.pointcutDeclarationScope);
/* 540 */     hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode((Object[])this.pointcutParameterNames);
/* 541 */     hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode((Object[])this.pointcutParameterTypes);
/* 542 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 547 */     StringBuilder sb = new StringBuilder();
/* 548 */     sb.append("AspectJExpressionPointcut: ");
/* 549 */     sb.append("(");
/* 550 */     for (int i = 0; i < this.pointcutParameterTypes.length; i++) {
/* 551 */       sb.append(this.pointcutParameterTypes[i].getName());
/* 552 */       sb.append(" ");
/* 553 */       sb.append(this.pointcutParameterNames[i]);
/* 554 */       if (i + 1 < this.pointcutParameterTypes.length) {
/* 555 */         sb.append(", ");
/*     */       }
/*     */     } 
/* 558 */     sb.append(")");
/* 559 */     sb.append(" ");
/* 560 */     if (getExpression() != null) {
/* 561 */       sb.append(getExpression());
/*     */     } else {
/*     */       
/* 564 */       sb.append("<pointcut expression not set>");
/*     */     } 
/* 566 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 575 */     ois.defaultReadObject();
/*     */ 
/*     */ 
/*     */     
/* 579 */     this.shadowMatchCache = new ConcurrentHashMap<>(32);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AspectJExpressionPointcut() {}
/*     */ 
/*     */   
/*     */   private class BeanPointcutDesignatorHandler
/*     */     implements PointcutDesignatorHandler
/*     */   {
/*     */     private static final String BEAN_DESIGNATOR_NAME = "bean";
/*     */ 
/*     */     
/*     */     private BeanPointcutDesignatorHandler() {}
/*     */ 
/*     */     
/*     */     public String getDesignatorName() {
/* 597 */       return "bean";
/*     */     }
/*     */ 
/*     */     
/*     */     public ContextBasedMatcher parse(String expression) {
/* 602 */       return new AspectJExpressionPointcut.BeanContextMatcher(expression);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class BeanContextMatcher
/*     */     implements ContextBasedMatcher
/*     */   {
/*     */     private final NamePattern expressionPattern;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BeanContextMatcher(String expression) {
/* 619 */       this.expressionPattern = new NamePattern(expression);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public boolean couldMatchJoinPointsInType(Class<?> someClass) {
/* 626 */       return (contextMatch(someClass) == FuzzyBoolean.YES);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public boolean couldMatchJoinPointsInType(Class<?> someClass, MatchingContext context) {
/* 633 */       return (contextMatch(someClass) == FuzzyBoolean.YES);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matchesDynamically(MatchingContext context) {
/* 638 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public FuzzyBoolean matchesStatically(MatchingContext context) {
/* 643 */       return contextMatch(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mayNeedDynamicTest() {
/* 648 */       return false;
/*     */     }
/*     */     
/*     */     private FuzzyBoolean contextMatch(@Nullable Class<?> targetType) {
/* 652 */       String advisedBeanName = AspectJExpressionPointcut.this.getCurrentProxiedBeanName();
/* 653 */       if (advisedBeanName == null)
/*     */       {
/* 655 */         return FuzzyBoolean.MAYBE;
/*     */       }
/* 657 */       if (BeanFactoryUtils.isGeneratedBeanName(advisedBeanName)) {
/* 658 */         return FuzzyBoolean.NO;
/*     */       }
/* 660 */       if (targetType != null) {
/* 661 */         boolean isFactory = FactoryBean.class.isAssignableFrom(targetType);
/* 662 */         return FuzzyBoolean.fromBoolean(
/* 663 */             matchesBean(isFactory ? ("&" + advisedBeanName) : advisedBeanName));
/*     */       } 
/*     */       
/* 666 */       return FuzzyBoolean.fromBoolean((matchesBean(advisedBeanName) || 
/* 667 */           matchesBean("&" + advisedBeanName)));
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean matchesBean(String advisedBeanName) {
/* 672 */       return BeanFactoryAnnotationUtils.isQualifierMatch(this.expressionPattern::matches, advisedBeanName, AspectJExpressionPointcut.this
/* 673 */           .beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class DefensiveShadowMatch
/*     */     implements ShadowMatch
/*     */   {
/*     */     private final ShadowMatch primary;
/*     */     private final ShadowMatch other;
/*     */     
/*     */     public DefensiveShadowMatch(ShadowMatch primary, ShadowMatch other) {
/* 685 */       this.primary = primary;
/* 686 */       this.other = other;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean alwaysMatches() {
/* 691 */       return this.primary.alwaysMatches();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean maybeMatches() {
/* 696 */       return this.primary.maybeMatches();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean neverMatches() {
/* 701 */       return this.primary.neverMatches();
/*     */     }
/*     */ 
/*     */     
/*     */     public JoinPointMatch matchesJoinPoint(Object thisObject, Object targetObject, Object[] args) {
/*     */       try {
/* 707 */         return this.primary.matchesJoinPoint(thisObject, targetObject, args);
/*     */       }
/* 709 */       catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex) {
/* 710 */         return this.other.matchesJoinPoint(thisObject, targetObject, args);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setMatchingContext(MatchingContext aMatchContext) {
/* 716 */       this.primary.setMatchingContext(aMatchContext);
/* 717 */       this.other.setMatchingContext(aMatchContext);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJExpressionPointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */