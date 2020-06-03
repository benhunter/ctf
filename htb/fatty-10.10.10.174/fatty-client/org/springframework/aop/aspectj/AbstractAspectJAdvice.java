/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.aspectj.lang.JoinPoint;
/*     */ import org.aspectj.lang.ProceedingJoinPoint;
/*     */ import org.aspectj.weaver.tools.JoinPointMatch;
/*     */ import org.aspectj.weaver.tools.PointcutParameter;
/*     */ import org.springframework.aop.AopInvocationException;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
/*     */ import org.springframework.aop.support.ComposablePointcut;
/*     */ import org.springframework.aop.support.MethodMatchers;
/*     */ import org.springframework.aop.support.StaticMethodMatcher;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public abstract class AbstractAspectJAdvice
/*     */   implements Advice, AspectJPrecedenceInformation, Serializable
/*     */ {
/*  68 */   protected static final String JOIN_POINT_KEY = JoinPoint.class.getName();
/*     */   
/*     */   private final Class<?> declaringClass;
/*     */   
/*     */   private final String methodName;
/*     */   private final Class<?>[] parameterTypes;
/*     */   protected transient Method aspectJAdviceMethod;
/*     */   private final AspectJExpressionPointcut pointcut;
/*     */   private final AspectInstanceFactory aspectInstanceFactory;
/*     */   
/*     */   public static JoinPoint currentJoinPoint() {
/*     */     MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint;
/*  80 */     MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
/*  81 */     if (!(mi instanceof ProxyMethodInvocation)) {
/*  82 */       throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */     }
/*  84 */     ProxyMethodInvocation pmi = (ProxyMethodInvocation)mi;
/*  85 */     JoinPoint jp = (JoinPoint)pmi.getUserAttribute(JOIN_POINT_KEY);
/*  86 */     if (jp == null) {
/*  87 */       methodInvocationProceedingJoinPoint = new MethodInvocationProceedingJoinPoint(pmi);
/*  88 */       pmi.setUserAttribute(JOIN_POINT_KEY, methodInvocationProceedingJoinPoint);
/*     */     } 
/*  90 */     return (JoinPoint)methodInvocationProceedingJoinPoint;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private String aspectName = "";
/*     */ 
/*     */ 
/*     */   
/*     */   private int declarationOrder;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String[] argumentNames;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String throwingName;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String returningName;
/*     */ 
/*     */   
/* 133 */   private Class<?> discoveredReturningType = Object.class;
/*     */   
/* 135 */   private Class<?> discoveredThrowingType = Object.class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   private int joinPointArgumentIndex = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   private int joinPointStaticPartArgumentIndex = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Map<String, Integer> argumentBindings;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean argumentsIntrospected = false;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Type discoveredReturningGenericType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractAspectJAdvice(Method aspectJAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aspectInstanceFactory) {
/* 169 */     Assert.notNull(aspectJAdviceMethod, "Advice method must not be null");
/* 170 */     this.declaringClass = aspectJAdviceMethod.getDeclaringClass();
/* 171 */     this.methodName = aspectJAdviceMethod.getName();
/* 172 */     this.parameterTypes = aspectJAdviceMethod.getParameterTypes();
/* 173 */     this.aspectJAdviceMethod = aspectJAdviceMethod;
/* 174 */     this.pointcut = pointcut;
/* 175 */     this.aspectInstanceFactory = aspectInstanceFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method getAspectJAdviceMethod() {
/* 183 */     return this.aspectJAdviceMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AspectJExpressionPointcut getPointcut() {
/* 190 */     calculateArgumentBindings();
/* 191 */     return this.pointcut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Pointcut buildSafePointcut() {
/* 200 */     AspectJExpressionPointcut aspectJExpressionPointcut = getPointcut();
/* 201 */     MethodMatcher safeMethodMatcher = MethodMatchers.intersection((MethodMatcher)new AdviceExcludingMethodMatcher(this.aspectJAdviceMethod), aspectJExpressionPointcut
/* 202 */         .getMethodMatcher());
/* 203 */     return (Pointcut)new ComposablePointcut(aspectJExpressionPointcut.getClassFilter(), safeMethodMatcher);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AspectInstanceFactory getAspectInstanceFactory() {
/* 210 */     return this.aspectInstanceFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final ClassLoader getAspectClassLoader() {
/* 218 */     return this.aspectInstanceFactory.getAspectClassLoader();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 223 */     return this.aspectInstanceFactory.getOrder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAspectName(String name) {
/* 231 */     this.aspectName = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAspectName() {
/* 236 */     return this.aspectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeclarationOrder(int order) {
/* 243 */     this.declarationOrder = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeclarationOrder() {
/* 248 */     return this.declarationOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArgumentNames(String argNames) {
/* 258 */     String[] tokens = StringUtils.commaDelimitedListToStringArray(argNames);
/* 259 */     setArgumentNamesFromStringArray(tokens);
/*     */   }
/*     */   
/*     */   public void setArgumentNamesFromStringArray(String... args) {
/* 263 */     this.argumentNames = new String[args.length];
/* 264 */     for (int i = 0; i < args.length; i++) {
/* 265 */       this.argumentNames[i] = StringUtils.trimWhitespace(args[i]);
/* 266 */       if (!isVariableName(this.argumentNames[i])) {
/* 267 */         throw new IllegalArgumentException("'argumentNames' property of AbstractAspectJAdvice contains an argument name '" + this.argumentNames[i] + "' that is not a valid Java identifier");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 272 */     if (this.argumentNames != null && 
/* 273 */       this.aspectJAdviceMethod.getParameterCount() == this.argumentNames.length + 1) {
/*     */       
/* 275 */       Class<?> firstArgType = this.aspectJAdviceMethod.getParameterTypes()[0];
/* 276 */       if (firstArgType == JoinPoint.class || firstArgType == ProceedingJoinPoint.class || firstArgType == JoinPoint.StaticPart.class) {
/*     */ 
/*     */         
/* 279 */         String[] oldNames = this.argumentNames;
/* 280 */         this.argumentNames = new String[oldNames.length + 1];
/* 281 */         this.argumentNames[0] = "THIS_JOIN_POINT";
/* 282 */         System.arraycopy(oldNames, 0, this.argumentNames, 1, oldNames.length);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReturningName(String name) {
/* 289 */     throw new UnsupportedOperationException("Only afterReturning advice can be used to bind a return value");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setReturningNameNoCheck(String name) {
/* 298 */     if (isVariableName(name)) {
/* 299 */       this.returningName = name;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 304 */         this.discoveredReturningType = ClassUtils.forName(name, getAspectClassLoader());
/*     */       }
/* 306 */       catch (Throwable ex) {
/* 307 */         throw new IllegalArgumentException("Returning name '" + name + "' is neither a valid argument name nor the fully-qualified name of a Java type on the classpath. Root cause: " + ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getDiscoveredReturningType() {
/* 315 */     return this.discoveredReturningType;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Type getDiscoveredReturningGenericType() {
/* 320 */     return this.discoveredReturningGenericType;
/*     */   }
/*     */   
/*     */   public void setThrowingName(String name) {
/* 324 */     throw new UnsupportedOperationException("Only afterThrowing advice can be used to bind a thrown exception");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setThrowingNameNoCheck(String name) {
/* 333 */     if (isVariableName(name)) {
/* 334 */       this.throwingName = name;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 339 */         this.discoveredThrowingType = ClassUtils.forName(name, getAspectClassLoader());
/*     */       }
/* 341 */       catch (Throwable ex) {
/* 342 */         throw new IllegalArgumentException("Throwing name '" + name + "' is neither a valid argument name nor the fully-qualified name of a Java type on the classpath. Root cause: " + ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getDiscoveredThrowingType() {
/* 350 */     return this.discoveredThrowingType;
/*     */   }
/*     */   
/*     */   private boolean isVariableName(String name) {
/* 354 */     char[] chars = name.toCharArray();
/* 355 */     if (!Character.isJavaIdentifierStart(chars[0])) {
/* 356 */       return false;
/*     */     }
/* 358 */     for (int i = 1; i < chars.length; i++) {
/* 359 */       if (!Character.isJavaIdentifierPart(chars[i])) {
/* 360 */         return false;
/*     */       }
/*     */     } 
/* 363 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void calculateArgumentBindings() {
/* 382 */     if (this.argumentsIntrospected || this.parameterTypes.length == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 386 */     int numUnboundArgs = this.parameterTypes.length;
/* 387 */     Class<?>[] parameterTypes = this.aspectJAdviceMethod.getParameterTypes();
/* 388 */     if (maybeBindJoinPoint(parameterTypes[0]) || maybeBindProceedingJoinPoint(parameterTypes[0]) || 
/* 389 */       maybeBindJoinPointStaticPart(parameterTypes[0])) {
/* 390 */       numUnboundArgs--;
/*     */     }
/*     */     
/* 393 */     if (numUnboundArgs > 0)
/*     */     {
/* 395 */       bindArgumentsByName(numUnboundArgs);
/*     */     }
/*     */     
/* 398 */     this.argumentsIntrospected = true;
/*     */   }
/*     */   
/*     */   private boolean maybeBindJoinPoint(Class<?> candidateParameterType) {
/* 402 */     if (JoinPoint.class == candidateParameterType) {
/* 403 */       this.joinPointArgumentIndex = 0;
/* 404 */       return true;
/*     */     } 
/*     */     
/* 407 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean maybeBindProceedingJoinPoint(Class<?> candidateParameterType) {
/* 412 */     if (ProceedingJoinPoint.class == candidateParameterType) {
/* 413 */       if (!supportsProceedingJoinPoint()) {
/* 414 */         throw new IllegalArgumentException("ProceedingJoinPoint is only supported for around advice");
/*     */       }
/* 416 */       this.joinPointArgumentIndex = 0;
/* 417 */       return true;
/*     */     } 
/*     */     
/* 420 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean supportsProceedingJoinPoint() {
/* 425 */     return false;
/*     */   }
/*     */   
/*     */   private boolean maybeBindJoinPointStaticPart(Class<?> candidateParameterType) {
/* 429 */     if (JoinPoint.StaticPart.class == candidateParameterType) {
/* 430 */       this.joinPointStaticPartArgumentIndex = 0;
/* 431 */       return true;
/*     */     } 
/*     */     
/* 434 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void bindArgumentsByName(int numArgumentsExpectingToBind) {
/* 439 */     if (this.argumentNames == null) {
/* 440 */       this.argumentNames = createParameterNameDiscoverer().getParameterNames(this.aspectJAdviceMethod);
/*     */     }
/* 442 */     if (this.argumentNames != null) {
/*     */       
/* 444 */       bindExplicitArguments(numArgumentsExpectingToBind);
/*     */     } else {
/*     */       
/* 447 */       throw new IllegalStateException("Advice method [" + this.aspectJAdviceMethod.getName() + "] requires " + numArgumentsExpectingToBind + " arguments to be bound by name, but the argument names were not specified and could not be discovered.");
/*     */     } 
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
/*     */   protected ParameterNameDiscoverer createParameterNameDiscoverer() {
/* 461 */     DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
/*     */     
/* 463 */     AspectJAdviceParameterNameDiscoverer adviceParameterNameDiscoverer = new AspectJAdviceParameterNameDiscoverer(this.pointcut.getExpression());
/* 464 */     adviceParameterNameDiscoverer.setReturningName(this.returningName);
/* 465 */     adviceParameterNameDiscoverer.setThrowingName(this.throwingName);
/*     */     
/* 467 */     adviceParameterNameDiscoverer.setRaiseExceptions(true);
/* 468 */     discoverer.addDiscoverer(adviceParameterNameDiscoverer);
/* 469 */     return (ParameterNameDiscoverer)discoverer;
/*     */   }
/*     */   
/*     */   private void bindExplicitArguments(int numArgumentsLeftToBind) {
/* 473 */     Assert.state((this.argumentNames != null), "No argument names available");
/* 474 */     this.argumentBindings = new HashMap<>();
/*     */     
/* 476 */     int numExpectedArgumentNames = this.aspectJAdviceMethod.getParameterCount();
/* 477 */     if (this.argumentNames.length != numExpectedArgumentNames) {
/* 478 */       throw new IllegalStateException("Expecting to find " + numExpectedArgumentNames + " arguments to bind by name in advice, but actually found " + this.argumentNames.length + " arguments.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 484 */     int argumentIndexOffset = this.parameterTypes.length - numArgumentsLeftToBind;
/* 485 */     for (int i = argumentIndexOffset; i < this.argumentNames.length; i++) {
/* 486 */       this.argumentBindings.put(this.argumentNames[i], Integer.valueOf(i));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 491 */     if (this.returningName != null) {
/* 492 */       if (!this.argumentBindings.containsKey(this.returningName)) {
/* 493 */         throw new IllegalStateException("Returning argument name '" + this.returningName + "' was not bound in advice arguments");
/*     */       }
/*     */ 
/*     */       
/* 497 */       Integer index = this.argumentBindings.get(this.returningName);
/* 498 */       this.discoveredReturningType = this.aspectJAdviceMethod.getParameterTypes()[index.intValue()];
/* 499 */       this.discoveredReturningGenericType = this.aspectJAdviceMethod.getGenericParameterTypes()[index.intValue()];
/*     */     } 
/*     */     
/* 502 */     if (this.throwingName != null) {
/* 503 */       if (!this.argumentBindings.containsKey(this.throwingName)) {
/* 504 */         throw new IllegalStateException("Throwing argument name '" + this.throwingName + "' was not bound in advice arguments");
/*     */       }
/*     */ 
/*     */       
/* 508 */       Integer index = this.argumentBindings.get(this.throwingName);
/* 509 */       this.discoveredThrowingType = this.aspectJAdviceMethod.getParameterTypes()[index.intValue()];
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 514 */     configurePointcutParameters(this.argumentNames, argumentIndexOffset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void configurePointcutParameters(String[] argumentNames, int argumentIndexOffset) {
/* 523 */     int numParametersToRemove = argumentIndexOffset;
/* 524 */     if (this.returningName != null) {
/* 525 */       numParametersToRemove++;
/*     */     }
/* 527 */     if (this.throwingName != null) {
/* 528 */       numParametersToRemove++;
/*     */     }
/* 530 */     String[] pointcutParameterNames = new String[argumentNames.length - numParametersToRemove];
/* 531 */     Class<?>[] pointcutParameterTypes = new Class[pointcutParameterNames.length];
/* 532 */     Class<?>[] methodParameterTypes = this.aspectJAdviceMethod.getParameterTypes();
/*     */     
/* 534 */     int index = 0;
/* 535 */     for (int i = 0; i < argumentNames.length; i++) {
/* 536 */       if (i >= argumentIndexOffset)
/*     */       {
/*     */         
/* 539 */         if (!argumentNames[i].equals(this.returningName) && 
/* 540 */           !argumentNames[i].equals(this.throwingName)) {
/*     */ 
/*     */           
/* 543 */           pointcutParameterNames[index] = argumentNames[i];
/* 544 */           pointcutParameterTypes[index] = methodParameterTypes[i];
/* 545 */           index++;
/*     */         }  } 
/*     */     } 
/* 548 */     this.pointcut.setParameterNames(pointcutParameterNames);
/* 549 */     this.pointcut.setParameterTypes(pointcutParameterTypes);
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
/*     */ 
/*     */   
/*     */   protected Object[] argBinding(JoinPoint jp, @Nullable JoinPointMatch jpMatch, @Nullable Object returnValue, @Nullable Throwable ex) {
/* 564 */     calculateArgumentBindings();
/*     */ 
/*     */     
/* 567 */     Object[] adviceInvocationArgs = new Object[this.parameterTypes.length];
/* 568 */     int numBound = 0;
/*     */     
/* 570 */     if (this.joinPointArgumentIndex != -1) {
/* 571 */       adviceInvocationArgs[this.joinPointArgumentIndex] = jp;
/* 572 */       numBound++;
/*     */     }
/* 574 */     else if (this.joinPointStaticPartArgumentIndex != -1) {
/* 575 */       adviceInvocationArgs[this.joinPointStaticPartArgumentIndex] = jp.getStaticPart();
/* 576 */       numBound++;
/*     */     } 
/*     */     
/* 579 */     if (!CollectionUtils.isEmpty(this.argumentBindings)) {
/*     */       
/* 581 */       if (jpMatch != null) {
/* 582 */         PointcutParameter[] parameterBindings = jpMatch.getParameterBindings();
/* 583 */         for (PointcutParameter parameter : parameterBindings) {
/* 584 */           String name = parameter.getName();
/* 585 */           Integer index = this.argumentBindings.get(name);
/* 586 */           adviceInvocationArgs[index.intValue()] = parameter.getBinding();
/* 587 */           numBound++;
/*     */         } 
/*     */       } 
/*     */       
/* 591 */       if (this.returningName != null) {
/* 592 */         Integer index = this.argumentBindings.get(this.returningName);
/* 593 */         adviceInvocationArgs[index.intValue()] = returnValue;
/* 594 */         numBound++;
/*     */       } 
/*     */       
/* 597 */       if (this.throwingName != null) {
/* 598 */         Integer index = this.argumentBindings.get(this.throwingName);
/* 599 */         adviceInvocationArgs[index.intValue()] = ex;
/* 600 */         numBound++;
/*     */       } 
/*     */     } 
/*     */     
/* 604 */     if (numBound != this.parameterTypes.length) {
/* 605 */       throw new IllegalStateException("Required to bind " + this.parameterTypes.length + " arguments, but only bound " + numBound + " (JoinPointMatch " + ((jpMatch == null) ? "was NOT" : "WAS") + " bound in invocation)");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 610 */     return adviceInvocationArgs;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object invokeAdviceMethod(@Nullable JoinPointMatch jpMatch, @Nullable Object returnValue, @Nullable Throwable ex) throws Throwable {
/* 626 */     return invokeAdviceMethodWithGivenArgs(argBinding(getJoinPoint(), jpMatch, returnValue, ex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object invokeAdviceMethod(JoinPoint jp, @Nullable JoinPointMatch jpMatch, @Nullable Object returnValue, @Nullable Throwable t) throws Throwable {
/* 633 */     return invokeAdviceMethodWithGivenArgs(argBinding(jp, jpMatch, returnValue, t));
/*     */   }
/*     */   
/*     */   protected Object invokeAdviceMethodWithGivenArgs(Object[] args) throws Throwable {
/* 637 */     Object[] actualArgs = args;
/* 638 */     if (this.aspectJAdviceMethod.getParameterCount() == 0) {
/* 639 */       actualArgs = null;
/*     */     }
/*     */     try {
/* 642 */       ReflectionUtils.makeAccessible(this.aspectJAdviceMethod);
/*     */       
/* 644 */       return this.aspectJAdviceMethod.invoke(this.aspectInstanceFactory.getAspectInstance(), actualArgs);
/*     */     }
/* 646 */     catch (IllegalArgumentException ex) {
/* 647 */       throw new AopInvocationException("Mismatch on arguments to advice method [" + this.aspectJAdviceMethod + "]; pointcut expression [" + this.pointcut
/*     */           
/* 649 */           .getPointcutExpression() + "]", ex);
/*     */     }
/* 651 */     catch (InvocationTargetException ex) {
/* 652 */       throw ex.getTargetException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JoinPoint getJoinPoint() {
/* 660 */     return currentJoinPoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected JoinPointMatch getJoinPointMatch() {
/* 668 */     MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
/* 669 */     if (!(mi instanceof ProxyMethodInvocation)) {
/* 670 */       throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */     }
/* 672 */     return getJoinPointMatch((ProxyMethodInvocation)mi);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected JoinPointMatch getJoinPointMatch(ProxyMethodInvocation pmi) {
/* 683 */     String expression = this.pointcut.getExpression();
/* 684 */     return (expression != null) ? (JoinPointMatch)pmi.getUserAttribute(expression) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 690 */     return getClass().getName() + ": advice method [" + this.aspectJAdviceMethod + "]; aspect name '" + this.aspectName + "'";
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 695 */     inputStream.defaultReadObject();
/*     */     try {
/* 697 */       this.aspectJAdviceMethod = this.declaringClass.getMethod(this.methodName, this.parameterTypes);
/*     */     }
/* 699 */     catch (NoSuchMethodException ex) {
/* 700 */       throw new IllegalStateException("Failed to find advice method on deserialization", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AdviceExcludingMethodMatcher
/*     */     extends StaticMethodMatcher
/*     */   {
/*     */     private final Method adviceMethod;
/*     */ 
/*     */ 
/*     */     
/*     */     public AdviceExcludingMethodMatcher(Method adviceMethod) {
/* 714 */       this.adviceMethod = adviceMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass) {
/* 719 */       return !this.adviceMethod.equals(method);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 724 */       if (this == other) {
/* 725 */         return true;
/*     */       }
/* 727 */       if (!(other instanceof AdviceExcludingMethodMatcher)) {
/* 728 */         return false;
/*     */       }
/* 730 */       AdviceExcludingMethodMatcher otherMm = (AdviceExcludingMethodMatcher)other;
/* 731 */       return this.adviceMethod.equals(otherMm.adviceMethod);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 736 */       return this.adviceMethod.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AbstractAspectJAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */