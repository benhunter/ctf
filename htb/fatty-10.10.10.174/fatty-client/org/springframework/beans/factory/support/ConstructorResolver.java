/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.MethodInvoker;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ConstructorResolver
/*     */ {
/*  83 */   private static final Object[] EMPTY_ARGS = new Object[0];
/*     */   
/*  85 */   private static final NamedThreadLocal<InjectionPoint> currentInjectionPoint = new NamedThreadLocal("Current injection point");
/*     */ 
/*     */ 
/*     */   
/*     */   private final AbstractAutowireCapableBeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Log logger;
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorResolver(AbstractAutowireCapableBeanFactory beanFactory) {
/*  98 */     this.beanFactory = beanFactory;
/*  99 */     this.logger = beanFactory.getLogger();
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
/*     */   public BeanWrapper autowireConstructor(String beanName, RootBeanDefinition mbd, @Nullable Constructor<?>[] chosenCtors, @Nullable Object[] explicitArgs) {
/* 120 */     BeanWrapperImpl bw = new BeanWrapperImpl();
/* 121 */     this.beanFactory.initBeanWrapper((BeanWrapper)bw);
/*     */     
/* 123 */     Constructor<?> constructorToUse = null;
/* 124 */     ArgumentsHolder argsHolderToUse = null;
/* 125 */     Object[] argsToUse = null;
/*     */     
/* 127 */     if (explicitArgs != null) {
/* 128 */       argsToUse = explicitArgs;
/*     */     } else {
/*     */       
/* 131 */       Object[] argsToResolve = null;
/* 132 */       synchronized (mbd.constructorArgumentLock) {
/* 133 */         constructorToUse = (Constructor)mbd.resolvedConstructorOrFactoryMethod;
/* 134 */         if (constructorToUse != null && mbd.constructorArgumentsResolved) {
/*     */           
/* 136 */           argsToUse = mbd.resolvedConstructorArguments;
/* 137 */           if (argsToUse == null) {
/* 138 */             argsToResolve = mbd.preparedConstructorArguments;
/*     */           }
/*     */         } 
/*     */       } 
/* 142 */       if (argsToResolve != null) {
/* 143 */         argsToUse = resolvePreparedArguments(beanName, mbd, (BeanWrapper)bw, constructorToUse, argsToResolve, true);
/*     */       }
/*     */     } 
/*     */     
/* 147 */     if (constructorToUse == null || argsToUse == null) {
/*     */       int minNrOfArgs;
/* 149 */       Constructor<?>[] candidates = chosenCtors;
/* 150 */       if (candidates == null) {
/* 151 */         Class<?> beanClass = mbd.getBeanClass();
/*     */         
/*     */         try {
/* 154 */           candidates = mbd.isNonPublicAccessAllowed() ? beanClass.getDeclaredConstructors() : beanClass.getConstructors();
/*     */         }
/* 156 */         catch (Throwable ex) {
/* 157 */           throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Resolution of declared constructors on bean Class [" + beanClass
/* 158 */               .getName() + "] from ClassLoader [" + beanClass
/* 159 */               .getClassLoader() + "] failed", ex);
/*     */         } 
/*     */       } 
/*     */       
/* 163 */       if (candidates.length == 1 && explicitArgs == null && !mbd.hasConstructorArgumentValues()) {
/* 164 */         Constructor<?> uniqueCandidate = candidates[0];
/* 165 */         if (uniqueCandidate.getParameterCount() == 0) {
/* 166 */           synchronized (mbd.constructorArgumentLock) {
/* 167 */             mbd.resolvedConstructorOrFactoryMethod = uniqueCandidate;
/* 168 */             mbd.constructorArgumentsResolved = true;
/* 169 */             mbd.resolvedConstructorArguments = EMPTY_ARGS;
/*     */           } 
/* 171 */           bw.setBeanInstance(instantiate(beanName, mbd, uniqueCandidate, EMPTY_ARGS));
/* 172 */           return (BeanWrapper)bw;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 178 */       boolean autowiring = (chosenCtors != null || mbd.getResolvedAutowireMode() == 3);
/* 179 */       ConstructorArgumentValues resolvedValues = null;
/*     */ 
/*     */       
/* 182 */       if (explicitArgs != null) {
/* 183 */         minNrOfArgs = explicitArgs.length;
/*     */       } else {
/*     */         
/* 186 */         ConstructorArgumentValues cargs = mbd.getConstructorArgumentValues();
/* 187 */         resolvedValues = new ConstructorArgumentValues();
/* 188 */         minNrOfArgs = resolveConstructorArguments(beanName, mbd, (BeanWrapper)bw, cargs, resolvedValues);
/*     */       } 
/*     */       
/* 191 */       AutowireUtils.sortConstructors(candidates);
/* 192 */       int minTypeDiffWeight = Integer.MAX_VALUE;
/* 193 */       Set<Constructor<?>> ambiguousConstructors = null;
/* 194 */       LinkedList<UnsatisfiedDependencyException> causes = null;
/*     */       
/* 196 */       for (Constructor<?> candidate : candidates) {
/* 197 */         ArgumentsHolder argsHolder; Class<?>[] paramTypes = candidate.getParameterTypes();
/*     */         
/* 199 */         if (constructorToUse != null && argsToUse != null && argsToUse.length > paramTypes.length) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 204 */         if (paramTypes.length < minNrOfArgs) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 209 */         if (resolvedValues != null) {
/*     */           try {
/* 211 */             String[] paramNames = ConstructorPropertiesChecker.evaluate(candidate, paramTypes.length);
/* 212 */             if (paramNames == null) {
/* 213 */               ParameterNameDiscoverer pnd = this.beanFactory.getParameterNameDiscoverer();
/* 214 */               if (pnd != null) {
/* 215 */                 paramNames = pnd.getParameterNames(candidate);
/*     */               }
/*     */             } 
/* 218 */             argsHolder = createArgumentArray(beanName, mbd, resolvedValues, (BeanWrapper)bw, paramTypes, paramNames, 
/* 219 */                 getUserDeclaredConstructor(candidate), autowiring, (candidates.length == 1));
/*     */           }
/* 221 */           catch (UnsatisfiedDependencyException ex) {
/* 222 */             if (this.logger.isTraceEnabled()) {
/* 223 */               this.logger.trace("Ignoring constructor [" + candidate + "] of bean '" + beanName + "': " + ex);
/*     */             }
/*     */             
/* 226 */             if (causes == null) {
/* 227 */               causes = new LinkedList<>();
/*     */             }
/* 229 */             causes.add(ex);
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 235 */           if (paramTypes.length != explicitArgs.length) {
/*     */             continue;
/*     */           }
/* 238 */           argsHolder = new ArgumentsHolder(explicitArgs);
/*     */         } 
/*     */ 
/*     */         
/* 242 */         int typeDiffWeight = mbd.isLenientConstructorResolution() ? argsHolder.getTypeDifferenceWeight(paramTypes) : argsHolder.getAssignabilityWeight(paramTypes);
/*     */         
/* 244 */         if (typeDiffWeight < minTypeDiffWeight) {
/* 245 */           constructorToUse = candidate;
/* 246 */           argsHolderToUse = argsHolder;
/* 247 */           argsToUse = argsHolder.arguments;
/* 248 */           minTypeDiffWeight = typeDiffWeight;
/* 249 */           ambiguousConstructors = null;
/*     */         }
/* 251 */         else if (constructorToUse != null && typeDiffWeight == minTypeDiffWeight) {
/* 252 */           if (ambiguousConstructors == null) {
/* 253 */             ambiguousConstructors = new LinkedHashSet<>();
/* 254 */             ambiguousConstructors.add(constructorToUse);
/*     */           } 
/* 256 */           ambiguousConstructors.add(candidate);
/*     */         } 
/*     */         continue;
/*     */       } 
/* 260 */       if (constructorToUse == null) {
/* 261 */         if (causes != null) {
/* 262 */           UnsatisfiedDependencyException ex = causes.removeLast();
/* 263 */           for (Exception cause : causes) {
/* 264 */             this.beanFactory.onSuppressedException(cause);
/*     */           }
/* 266 */           throw ex;
/*     */         } 
/* 268 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Could not resolve matching constructor (hint: specify index/type/name arguments for simple parameters to avoid type ambiguities)");
/*     */       } 
/*     */ 
/*     */       
/* 272 */       if (ambiguousConstructors != null && !mbd.isLenientConstructorResolution()) {
/* 273 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Ambiguous constructor matches found in bean '" + beanName + "' (hint: specify index/type/name arguments for simple parameters to avoid type ambiguities): " + ambiguousConstructors);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 279 */       if (explicitArgs == null && argsHolderToUse != null) {
/* 280 */         argsHolderToUse.storeCache(mbd, constructorToUse);
/*     */       }
/*     */     } 
/*     */     
/* 284 */     Assert.state((argsToUse != null), "Unresolved constructor arguments");
/* 285 */     bw.setBeanInstance(instantiate(beanName, mbd, constructorToUse, argsToUse));
/* 286 */     return (BeanWrapper)bw;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object instantiate(String beanName, RootBeanDefinition mbd, Constructor<?> constructorToUse, Object[] argsToUse) {
/*     */     try {
/* 293 */       InstantiationStrategy strategy = this.beanFactory.getInstantiationStrategy();
/* 294 */       if (System.getSecurityManager() != null) {
/* 295 */         return AccessController.doPrivileged(() -> strategy.instantiate(mbd, beanName, (BeanFactory)this.beanFactory, constructorToUse, argsToUse), this.beanFactory
/*     */             
/* 297 */             .getAccessControlContext());
/*     */       }
/*     */       
/* 300 */       return strategy.instantiate(mbd, beanName, (BeanFactory)this.beanFactory, constructorToUse, argsToUse);
/*     */     
/*     */     }
/* 303 */     catch (Throwable ex) {
/* 304 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean instantiation via constructor failed", ex);
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
/*     */   public void resolveFactoryMethodIfPossible(RootBeanDefinition mbd) {
/*     */     boolean isStatic;
/* 317 */     if (mbd.getFactoryBeanName() != null) {
/* 318 */       factoryClass = this.beanFactory.getType(mbd.getFactoryBeanName());
/* 319 */       isStatic = false;
/*     */     } else {
/*     */       
/* 322 */       factoryClass = mbd.getBeanClass();
/* 323 */       isStatic = true;
/*     */     } 
/* 325 */     Assert.state((factoryClass != null), "Unresolvable factory class");
/* 326 */     Class<?> factoryClass = ClassUtils.getUserClass(factoryClass);
/*     */     
/* 328 */     Method[] candidates = getCandidateMethods(factoryClass, mbd);
/* 329 */     Method uniqueCandidate = null;
/* 330 */     for (Method candidate : candidates) {
/* 331 */       if (Modifier.isStatic(candidate.getModifiers()) == isStatic && mbd.isFactoryMethod(candidate)) {
/* 332 */         if (uniqueCandidate == null) {
/* 333 */           uniqueCandidate = candidate;
/*     */         }
/* 335 */         else if (!Arrays.equals((Object[])uniqueCandidate.getParameterTypes(), (Object[])candidate.getParameterTypes())) {
/* 336 */           uniqueCandidate = null;
/*     */           break;
/*     */         } 
/*     */       }
/*     */     } 
/* 341 */     mbd.factoryMethodToIntrospect = uniqueCandidate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Method[] getCandidateMethods(Class<?> factoryClass, RootBeanDefinition mbd) {
/* 350 */     if (System.getSecurityManager() != null) {
/* 351 */       return AccessController.<Method[]>doPrivileged(() -> mbd.isNonPublicAccessAllowed() ? ReflectionUtils.getAllDeclaredMethods(factoryClass) : factoryClass.getMethods());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 356 */     return mbd.isNonPublicAccessAllowed() ? 
/* 357 */       ReflectionUtils.getAllDeclaredMethods(factoryClass) : factoryClass.getMethods();
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
/*     */   public BeanWrapper instantiateUsingFactoryMethod(String beanName, RootBeanDefinition mbd, @Nullable Object[] explicitArgs) {
/*     */     Object factoryBean;
/*     */     Class<?> factoryClass;
/*     */     boolean isStatic;
/* 379 */     BeanWrapperImpl bw = new BeanWrapperImpl();
/* 380 */     this.beanFactory.initBeanWrapper((BeanWrapper)bw);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 386 */     String factoryBeanName = mbd.getFactoryBeanName();
/* 387 */     if (factoryBeanName != null) {
/* 388 */       if (factoryBeanName.equals(beanName)) {
/* 389 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, "factory-bean reference points back to the same bean definition");
/*     */       }
/*     */       
/* 392 */       factoryBean = this.beanFactory.getBean(factoryBeanName);
/* 393 */       if (mbd.isSingleton() && this.beanFactory.containsSingleton(beanName)) {
/* 394 */         throw new ImplicitlyAppearedSingletonException();
/*     */       }
/* 396 */       factoryClass = factoryBean.getClass();
/* 397 */       isStatic = false;
/*     */     }
/*     */     else {
/*     */       
/* 401 */       if (!mbd.hasBeanClass()) {
/* 402 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, "bean definition declares neither a bean class nor a factory-bean reference");
/*     */       }
/*     */       
/* 405 */       factoryBean = null;
/* 406 */       factoryClass = mbd.getBeanClass();
/* 407 */       isStatic = true;
/*     */     } 
/*     */     
/* 410 */     Method factoryMethodToUse = null;
/* 411 */     ArgumentsHolder argsHolderToUse = null;
/* 412 */     Object[] argsToUse = null;
/*     */     
/* 414 */     if (explicitArgs != null) {
/* 415 */       argsToUse = explicitArgs;
/*     */     } else {
/*     */       
/* 418 */       Object[] argsToResolve = null;
/* 419 */       synchronized (mbd.constructorArgumentLock) {
/* 420 */         factoryMethodToUse = (Method)mbd.resolvedConstructorOrFactoryMethod;
/* 421 */         if (factoryMethodToUse != null && mbd.constructorArgumentsResolved) {
/*     */           
/* 423 */           argsToUse = mbd.resolvedConstructorArguments;
/* 424 */           if (argsToUse == null) {
/* 425 */             argsToResolve = mbd.preparedConstructorArguments;
/*     */           }
/*     */         } 
/*     */       } 
/* 429 */       if (argsToResolve != null) {
/* 430 */         argsToUse = resolvePreparedArguments(beanName, mbd, (BeanWrapper)bw, factoryMethodToUse, argsToResolve, true);
/*     */       }
/*     */     } 
/*     */     
/* 434 */     if (factoryMethodToUse == null || argsToUse == null) {
/*     */       int minNrOfArgs;
/*     */       
/* 437 */       factoryClass = ClassUtils.getUserClass(factoryClass);
/*     */       
/* 439 */       Method[] rawCandidates = getCandidateMethods(factoryClass, mbd);
/* 440 */       List<Method> candidateList = new ArrayList<>();
/* 441 */       for (Method candidate : rawCandidates) {
/* 442 */         if (Modifier.isStatic(candidate.getModifiers()) == isStatic && mbd.isFactoryMethod(candidate)) {
/* 443 */           candidateList.add(candidate);
/*     */         }
/*     */       } 
/*     */       
/* 447 */       if (candidateList.size() == 1 && explicitArgs == null && !mbd.hasConstructorArgumentValues()) {
/* 448 */         Method uniqueCandidate = candidateList.get(0);
/* 449 */         if (uniqueCandidate.getParameterCount() == 0) {
/* 450 */           mbd.factoryMethodToIntrospect = uniqueCandidate;
/* 451 */           synchronized (mbd.constructorArgumentLock) {
/* 452 */             mbd.resolvedConstructorOrFactoryMethod = uniqueCandidate;
/* 453 */             mbd.constructorArgumentsResolved = true;
/* 454 */             mbd.resolvedConstructorArguments = EMPTY_ARGS;
/*     */           } 
/* 456 */           bw.setBeanInstance(instantiate(beanName, mbd, factoryBean, uniqueCandidate, EMPTY_ARGS));
/* 457 */           return (BeanWrapper)bw;
/*     */         } 
/*     */       } 
/*     */       
/* 461 */       Method[] candidates = candidateList.<Method>toArray(new Method[0]);
/* 462 */       AutowireUtils.sortFactoryMethods(candidates);
/*     */       
/* 464 */       ConstructorArgumentValues resolvedValues = null;
/* 465 */       boolean autowiring = (mbd.getResolvedAutowireMode() == 3);
/* 466 */       int minTypeDiffWeight = Integer.MAX_VALUE;
/* 467 */       Set<Method> ambiguousFactoryMethods = null;
/*     */ 
/*     */       
/* 470 */       if (explicitArgs != null) {
/* 471 */         minNrOfArgs = explicitArgs.length;
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 476 */       else if (mbd.hasConstructorArgumentValues()) {
/* 477 */         ConstructorArgumentValues cargs = mbd.getConstructorArgumentValues();
/* 478 */         resolvedValues = new ConstructorArgumentValues();
/* 479 */         minNrOfArgs = resolveConstructorArguments(beanName, mbd, (BeanWrapper)bw, cargs, resolvedValues);
/*     */       } else {
/*     */         
/* 482 */         minNrOfArgs = 0;
/*     */       } 
/*     */ 
/*     */       
/* 486 */       LinkedList<UnsatisfiedDependencyException> causes = null;
/*     */       
/* 488 */       for (Method candidate : candidates) {
/* 489 */         Class<?>[] paramTypes = candidate.getParameterTypes();
/*     */         
/* 491 */         if (paramTypes.length >= minNrOfArgs) {
/*     */           ArgumentsHolder argsHolder;
/*     */           
/* 494 */           if (explicitArgs != null) {
/*     */             
/* 496 */             if (paramTypes.length != explicitArgs.length) {
/*     */               continue;
/*     */             }
/* 499 */             argsHolder = new ArgumentsHolder(explicitArgs);
/*     */           } else {
/*     */ 
/*     */             
/*     */             try {
/* 504 */               String[] paramNames = null;
/* 505 */               ParameterNameDiscoverer pnd = this.beanFactory.getParameterNameDiscoverer();
/* 506 */               if (pnd != null) {
/* 507 */                 paramNames = pnd.getParameterNames(candidate);
/*     */               }
/* 509 */               argsHolder = createArgumentArray(beanName, mbd, resolvedValues, (BeanWrapper)bw, paramTypes, paramNames, candidate, autowiring, (candidates.length == 1));
/*     */             
/*     */             }
/* 512 */             catch (UnsatisfiedDependencyException ex) {
/* 513 */               if (this.logger.isTraceEnabled()) {
/* 514 */                 this.logger.trace("Ignoring factory method [" + candidate + "] of bean '" + beanName + "': " + ex);
/*     */               }
/*     */               
/* 517 */               if (causes == null) {
/* 518 */                 causes = new LinkedList<>();
/*     */               }
/* 520 */               causes.add(ex);
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 526 */           int typeDiffWeight = mbd.isLenientConstructorResolution() ? argsHolder.getTypeDifferenceWeight(paramTypes) : argsHolder.getAssignabilityWeight(paramTypes);
/*     */           
/* 528 */           if (typeDiffWeight < minTypeDiffWeight) {
/* 529 */             factoryMethodToUse = candidate;
/* 530 */             argsHolderToUse = argsHolder;
/* 531 */             argsToUse = argsHolder.arguments;
/* 532 */             minTypeDiffWeight = typeDiffWeight;
/* 533 */             ambiguousFactoryMethods = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 540 */           else if (factoryMethodToUse != null && typeDiffWeight == minTypeDiffWeight && 
/* 541 */             !mbd.isLenientConstructorResolution() && paramTypes.length == factoryMethodToUse
/* 542 */             .getParameterCount() && 
/* 543 */             !Arrays.equals((Object[])paramTypes, (Object[])factoryMethodToUse.getParameterTypes())) {
/* 544 */             if (ambiguousFactoryMethods == null) {
/* 545 */               ambiguousFactoryMethods = new LinkedHashSet<>();
/* 546 */               ambiguousFactoryMethods.add(factoryMethodToUse);
/*     */             } 
/* 548 */             ambiguousFactoryMethods.add(candidate);
/*     */           } 
/*     */         } 
/*     */         continue;
/*     */       } 
/* 553 */       if (factoryMethodToUse == null) {
/* 554 */         if (causes != null) {
/* 555 */           UnsatisfiedDependencyException ex = causes.removeLast();
/* 556 */           for (Exception cause : causes) {
/* 557 */             this.beanFactory.onSuppressedException(cause);
/*     */           }
/* 559 */           throw ex;
/*     */         } 
/* 561 */         List<String> argTypes = new ArrayList<>(minNrOfArgs);
/* 562 */         if (explicitArgs != null) {
/* 563 */           for (Object arg : explicitArgs) {
/* 564 */             argTypes.add((arg != null) ? arg.getClass().getSimpleName() : "null");
/*     */           }
/*     */         }
/* 567 */         else if (resolvedValues != null) {
/* 568 */           Set<ConstructorArgumentValues.ValueHolder> valueHolders = new LinkedHashSet<>(resolvedValues.getArgumentCount());
/* 569 */           valueHolders.addAll(resolvedValues.getIndexedArgumentValues().values());
/* 570 */           valueHolders.addAll(resolvedValues.getGenericArgumentValues());
/* 571 */           for (ConstructorArgumentValues.ValueHolder value : valueHolders) {
/*     */             
/* 573 */             String argType = (value.getType() != null) ? ClassUtils.getShortName(value.getType()) : ((value.getValue() != null) ? value.getValue().getClass().getSimpleName() : "null");
/* 574 */             argTypes.add(argType);
/*     */           } 
/*     */         } 
/* 577 */         String argDesc = StringUtils.collectionToCommaDelimitedString(argTypes);
/* 578 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "No matching factory method found: " + (
/*     */             
/* 580 */             (mbd.getFactoryBeanName() != null) ? ("factory bean '" + mbd
/* 581 */             .getFactoryBeanName() + "'; ") : "") + "factory method '" + mbd
/* 582 */             .getFactoryMethodName() + "(" + argDesc + ")'. Check that a method with the specified name " + ((minNrOfArgs > 0) ? "and arguments " : "") + "exists and that it is " + (isStatic ? "static" : "non-static") + ".");
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 588 */       if (void.class == factoryMethodToUse.getReturnType()) {
/* 589 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid factory method '" + mbd
/* 590 */             .getFactoryMethodName() + "': needs to have a non-void return type!");
/*     */       }
/*     */       
/* 593 */       if (ambiguousFactoryMethods != null) {
/* 594 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Ambiguous factory method matches found in bean '" + beanName + "' (hint: specify index/type/name arguments for simple parameters to avoid type ambiguities): " + ambiguousFactoryMethods);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 600 */       if (explicitArgs == null && argsHolderToUse != null) {
/* 601 */         mbd.factoryMethodToIntrospect = factoryMethodToUse;
/* 602 */         argsHolderToUse.storeCache(mbd, factoryMethodToUse);
/*     */       } 
/*     */     } 
/*     */     
/* 606 */     Assert.state((argsToUse != null), "Unresolved factory method arguments");
/* 607 */     bw.setBeanInstance(instantiate(beanName, mbd, factoryBean, factoryMethodToUse, argsToUse));
/* 608 */     return (BeanWrapper)bw;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object instantiate(String beanName, RootBeanDefinition mbd, @Nullable Object factoryBean, Method factoryMethod, Object[] args) {
/*     */     try {
/* 615 */       if (System.getSecurityManager() != null) {
/* 616 */         return AccessController.doPrivileged(() -> this.beanFactory.getInstantiationStrategy().instantiate(mbd, beanName, (BeanFactory)this.beanFactory, factoryBean, factoryMethod, args), this.beanFactory
/*     */ 
/*     */             
/* 619 */             .getAccessControlContext());
/*     */       }
/*     */       
/* 622 */       return this.beanFactory.getInstantiationStrategy().instantiate(mbd, beanName, (BeanFactory)this.beanFactory, factoryBean, factoryMethod, args);
/*     */ 
/*     */     
/*     */     }
/* 626 */     catch (Throwable ex) {
/* 627 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean instantiation via factory method failed", ex);
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
/*     */   private int resolveConstructorArguments(String beanName, RootBeanDefinition mbd, BeanWrapper bw, ConstructorArgumentValues cargs, ConstructorArgumentValues resolvedValues) {
/* 640 */     TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
/* 641 */     TypeConverter converter = (customConverter != null) ? customConverter : (TypeConverter)bw;
/* 642 */     BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
/*     */ 
/*     */     
/* 645 */     int minNrOfArgs = cargs.getArgumentCount();
/*     */     
/* 647 */     for (Map.Entry<Integer, ConstructorArgumentValues.ValueHolder> entry : (Iterable<Map.Entry<Integer, ConstructorArgumentValues.ValueHolder>>)cargs.getIndexedArgumentValues().entrySet()) {
/* 648 */       int index = ((Integer)entry.getKey()).intValue();
/* 649 */       if (index < 0) {
/* 650 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid constructor argument index: " + index);
/*     */       }
/*     */       
/* 653 */       if (index > minNrOfArgs) {
/* 654 */         minNrOfArgs = index + 1;
/*     */       }
/* 656 */       ConstructorArgumentValues.ValueHolder valueHolder = entry.getValue();
/* 657 */       if (valueHolder.isConverted()) {
/* 658 */         resolvedValues.addIndexedArgumentValue(index, valueHolder);
/*     */         
/*     */         continue;
/*     */       } 
/* 662 */       Object resolvedValue = valueResolver.resolveValueIfNecessary("constructor argument", valueHolder.getValue());
/*     */       
/* 664 */       ConstructorArgumentValues.ValueHolder resolvedValueHolder = new ConstructorArgumentValues.ValueHolder(resolvedValue, valueHolder.getType(), valueHolder.getName());
/* 665 */       resolvedValueHolder.setSource(valueHolder);
/* 666 */       resolvedValues.addIndexedArgumentValue(index, resolvedValueHolder);
/*     */     } 
/*     */ 
/*     */     
/* 670 */     for (ConstructorArgumentValues.ValueHolder valueHolder : cargs.getGenericArgumentValues()) {
/* 671 */       if (valueHolder.isConverted()) {
/* 672 */         resolvedValues.addGenericArgumentValue(valueHolder);
/*     */         
/*     */         continue;
/*     */       } 
/* 676 */       Object resolvedValue = valueResolver.resolveValueIfNecessary("constructor argument", valueHolder.getValue());
/*     */       
/* 678 */       ConstructorArgumentValues.ValueHolder resolvedValueHolder = new ConstructorArgumentValues.ValueHolder(resolvedValue, valueHolder.getType(), valueHolder.getName());
/* 679 */       resolvedValueHolder.setSource(valueHolder);
/* 680 */       resolvedValues.addGenericArgumentValue(resolvedValueHolder);
/*     */     } 
/*     */ 
/*     */     
/* 684 */     return minNrOfArgs;
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
/*     */   private ArgumentsHolder createArgumentArray(String beanName, RootBeanDefinition mbd, @Nullable ConstructorArgumentValues resolvedValues, BeanWrapper bw, Class<?>[] paramTypes, @Nullable String[] paramNames, Executable executable, boolean autowiring, boolean fallback) throws UnsatisfiedDependencyException {
/* 696 */     TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
/* 697 */     TypeConverter converter = (customConverter != null) ? customConverter : (TypeConverter)bw;
/*     */     
/* 699 */     ArgumentsHolder args = new ArgumentsHolder(paramTypes.length);
/* 700 */     Set<ConstructorArgumentValues.ValueHolder> usedValueHolders = new HashSet<>(paramTypes.length);
/* 701 */     Set<String> autowiredBeanNames = new LinkedHashSet<>(4);
/*     */     
/* 703 */     for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
/* 704 */       Class<?> paramType = paramTypes[paramIndex];
/* 705 */       String paramName = (paramNames != null) ? paramNames[paramIndex] : "";
/*     */       
/* 707 */       ConstructorArgumentValues.ValueHolder valueHolder = null;
/* 708 */       if (resolvedValues != null) {
/* 709 */         valueHolder = resolvedValues.getArgumentValue(paramIndex, paramType, paramName, usedValueHolders);
/*     */ 
/*     */ 
/*     */         
/* 713 */         if (valueHolder == null && (!autowiring || paramTypes.length == resolvedValues.getArgumentCount())) {
/* 714 */           valueHolder = resolvedValues.getGenericArgumentValue(null, null, usedValueHolders);
/*     */         }
/*     */       } 
/* 717 */       if (valueHolder != null) {
/*     */         Object convertedValue;
/*     */         
/* 720 */         usedValueHolders.add(valueHolder);
/* 721 */         Object originalValue = valueHolder.getValue();
/*     */         
/* 723 */         if (valueHolder.isConverted()) {
/* 724 */           convertedValue = valueHolder.getConvertedValue();
/* 725 */           args.preparedArguments[paramIndex] = convertedValue;
/*     */         } else {
/*     */           
/* 728 */           MethodParameter methodParam = MethodParameter.forExecutable(executable, paramIndex);
/*     */           try {
/* 730 */             convertedValue = converter.convertIfNecessary(originalValue, paramType, methodParam);
/*     */           }
/* 732 */           catch (TypeMismatchException ex) {
/* 733 */             throw new UnsatisfiedDependencyException(mbd
/* 734 */                 .getResourceDescription(), beanName, new InjectionPoint(methodParam), "Could not convert argument value of type [" + 
/*     */                 
/* 736 */                 ObjectUtils.nullSafeClassName(valueHolder.getValue()) + "] to required type [" + paramType
/* 737 */                 .getName() + "]: " + ex.getMessage());
/*     */           } 
/* 739 */           Object sourceHolder = valueHolder.getSource();
/* 740 */           if (sourceHolder instanceof ConstructorArgumentValues.ValueHolder) {
/* 741 */             Object sourceValue = ((ConstructorArgumentValues.ValueHolder)sourceHolder).getValue();
/* 742 */             args.resolveNecessary = true;
/* 743 */             args.preparedArguments[paramIndex] = sourceValue;
/*     */           } 
/*     */         } 
/* 746 */         args.arguments[paramIndex] = convertedValue;
/* 747 */         args.rawArguments[paramIndex] = originalValue;
/*     */       } else {
/*     */         
/* 750 */         MethodParameter methodParam = MethodParameter.forExecutable(executable, paramIndex);
/*     */ 
/*     */         
/* 753 */         if (!autowiring) {
/* 754 */           throw new UnsatisfiedDependencyException(mbd
/* 755 */               .getResourceDescription(), beanName, new InjectionPoint(methodParam), "Ambiguous argument values for parameter of type [" + paramType
/* 756 */               .getName() + "] - did you specify the correct bean references as arguments?");
/*     */         }
/*     */         
/*     */         try {
/* 760 */           Object autowiredArgument = resolveAutowiredArgument(methodParam, beanName, autowiredBeanNames, converter, fallback);
/*     */           
/* 762 */           args.rawArguments[paramIndex] = autowiredArgument;
/* 763 */           args.arguments[paramIndex] = autowiredArgument;
/* 764 */           args.preparedArguments[paramIndex] = new AutowiredArgumentMarker();
/* 765 */           args.resolveNecessary = true;
/*     */         }
/* 767 */         catch (BeansException ex) {
/* 768 */           Object convertedValue; throw new UnsatisfiedDependencyException(mbd
/* 769 */               .getResourceDescription(), beanName, new InjectionPoint(methodParam), convertedValue);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 774 */     for (String autowiredBeanName : autowiredBeanNames) {
/* 775 */       this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
/* 776 */       if (this.logger.isDebugEnabled()) {
/* 777 */         this.logger.debug("Autowiring by type from bean name '" + beanName + "' via " + ((executable instanceof Constructor) ? "constructor" : "factory method") + " to bean named '" + autowiredBeanName + "'");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 783 */     return args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] resolvePreparedArguments(String beanName, RootBeanDefinition mbd, BeanWrapper bw, Executable executable, Object[] argsToResolve, boolean fallback) {
/* 792 */     TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
/* 793 */     TypeConverter converter = (customConverter != null) ? customConverter : (TypeConverter)bw;
/* 794 */     BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
/*     */     
/* 796 */     Class<?>[] paramTypes = executable.getParameterTypes();
/*     */     
/* 798 */     Object[] resolvedArgs = new Object[argsToResolve.length];
/* 799 */     for (int argIndex = 0; argIndex < argsToResolve.length; argIndex++) {
/* 800 */       Object argValue = argsToResolve[argIndex];
/* 801 */       MethodParameter methodParam = MethodParameter.forExecutable(executable, argIndex);
/* 802 */       GenericTypeResolver.resolveParameterType(methodParam, executable.getDeclaringClass());
/* 803 */       if (argValue instanceof AutowiredArgumentMarker) {
/* 804 */         argValue = resolveAutowiredArgument(methodParam, beanName, null, converter, fallback);
/*     */       }
/* 806 */       else if (argValue instanceof org.springframework.beans.BeanMetadataElement) {
/* 807 */         argValue = valueResolver.resolveValueIfNecessary("constructor argument", argValue);
/*     */       }
/* 809 */       else if (argValue instanceof String) {
/* 810 */         argValue = this.beanFactory.evaluateBeanDefinitionString((String)argValue, mbd);
/*     */       } 
/* 812 */       Class<?> paramType = paramTypes[argIndex];
/*     */       try {
/* 814 */         resolvedArgs[argIndex] = converter.convertIfNecessary(argValue, paramType, methodParam);
/*     */       }
/* 816 */       catch (TypeMismatchException ex) {
/* 817 */         throw new UnsatisfiedDependencyException(mbd
/* 818 */             .getResourceDescription(), beanName, new InjectionPoint(methodParam), "Could not convert argument value of type [" + 
/* 819 */             ObjectUtils.nullSafeClassName(argValue) + "] to required type [" + paramType
/* 820 */             .getName() + "]: " + ex.getMessage());
/*     */       } 
/*     */     } 
/* 823 */     return resolvedArgs;
/*     */   }
/*     */   
/*     */   protected Constructor<?> getUserDeclaredConstructor(Constructor<?> constructor) {
/* 827 */     Class<?> declaringClass = constructor.getDeclaringClass();
/* 828 */     Class<?> userClass = ClassUtils.getUserClass(declaringClass);
/* 829 */     if (userClass != declaringClass) {
/*     */       try {
/* 831 */         return userClass.getDeclaredConstructor(constructor.getParameterTypes());
/*     */       }
/* 833 */       catch (NoSuchMethodException noSuchMethodException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 838 */     return constructor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object resolveAutowiredArgument(MethodParameter param, String beanName, @Nullable Set<String> autowiredBeanNames, TypeConverter typeConverter, boolean fallback) {
/* 848 */     Class<?> paramType = param.getParameterType();
/* 849 */     if (InjectionPoint.class.isAssignableFrom(paramType)) {
/* 850 */       InjectionPoint injectionPoint = (InjectionPoint)currentInjectionPoint.get();
/* 851 */       if (injectionPoint == null) {
/* 852 */         throw new IllegalStateException("No current InjectionPoint available for " + param);
/*     */       }
/* 854 */       return injectionPoint;
/*     */     } 
/*     */     try {
/* 857 */       return this.beanFactory.resolveDependency(new DependencyDescriptor(param, true), beanName, autowiredBeanNames, typeConverter);
/*     */     
/*     */     }
/* 860 */     catch (NoUniqueBeanDefinitionException ex) {
/* 861 */       throw ex;
/*     */     }
/* 863 */     catch (NoSuchBeanDefinitionException ex) {
/* 864 */       if (fallback) {
/*     */ 
/*     */         
/* 867 */         if (paramType.isArray()) {
/* 868 */           return Array.newInstance(paramType.getComponentType(), 0);
/*     */         }
/* 870 */         if (CollectionFactory.isApproximableCollectionType(paramType)) {
/* 871 */           return CollectionFactory.createCollection(paramType, 0);
/*     */         }
/* 873 */         if (CollectionFactory.isApproximableMapType(paramType)) {
/* 874 */           return CollectionFactory.createMap(paramType, 0);
/*     */         }
/*     */       } 
/* 877 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   static InjectionPoint setCurrentInjectionPoint(@Nullable InjectionPoint injectionPoint) {
/* 882 */     InjectionPoint old = (InjectionPoint)currentInjectionPoint.get();
/* 883 */     if (injectionPoint != null) {
/* 884 */       currentInjectionPoint.set(injectionPoint);
/*     */     } else {
/*     */       
/* 887 */       currentInjectionPoint.remove();
/*     */     } 
/* 889 */     return old;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ArgumentsHolder
/*     */   {
/*     */     public final Object[] rawArguments;
/*     */ 
/*     */     
/*     */     public final Object[] arguments;
/*     */     
/*     */     public final Object[] preparedArguments;
/*     */     
/*     */     public boolean resolveNecessary = false;
/*     */ 
/*     */     
/*     */     public ArgumentsHolder(int size) {
/* 907 */       this.rawArguments = new Object[size];
/* 908 */       this.arguments = new Object[size];
/* 909 */       this.preparedArguments = new Object[size];
/*     */     }
/*     */     
/*     */     public ArgumentsHolder(Object[] args) {
/* 913 */       this.rawArguments = args;
/* 914 */       this.arguments = args;
/* 915 */       this.preparedArguments = args;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getTypeDifferenceWeight(Class<?>[] paramTypes) {
/* 923 */       int typeDiffWeight = MethodInvoker.getTypeDifferenceWeight(paramTypes, this.arguments);
/* 924 */       int rawTypeDiffWeight = MethodInvoker.getTypeDifferenceWeight(paramTypes, this.rawArguments) - 1024;
/* 925 */       return (rawTypeDiffWeight < typeDiffWeight) ? rawTypeDiffWeight : typeDiffWeight;
/*     */     }
/*     */     public int getAssignabilityWeight(Class<?>[] paramTypes) {
/*     */       int i;
/* 929 */       for (i = 0; i < paramTypes.length; i++) {
/* 930 */         if (!ClassUtils.isAssignableValue(paramTypes[i], this.arguments[i])) {
/* 931 */           return Integer.MAX_VALUE;
/*     */         }
/*     */       } 
/* 934 */       for (i = 0; i < paramTypes.length; i++) {
/* 935 */         if (!ClassUtils.isAssignableValue(paramTypes[i], this.rawArguments[i])) {
/* 936 */           return 2147483135;
/*     */         }
/*     */       } 
/* 939 */       return 2147482623;
/*     */     }
/*     */     
/*     */     public void storeCache(RootBeanDefinition mbd, Executable constructorOrFactoryMethod) {
/* 943 */       synchronized (mbd.constructorArgumentLock) {
/* 944 */         mbd.resolvedConstructorOrFactoryMethod = constructorOrFactoryMethod;
/* 945 */         mbd.constructorArgumentsResolved = true;
/* 946 */         if (this.resolveNecessary) {
/* 947 */           mbd.preparedConstructorArguments = this.preparedArguments;
/*     */         } else {
/*     */           
/* 950 */           mbd.resolvedConstructorArguments = this.arguments;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AutowiredArgumentMarker
/*     */   {
/*     */     private AutowiredArgumentMarker() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConstructorPropertiesChecker
/*     */   {
/*     */     @Nullable
/*     */     public static String[] evaluate(Constructor<?> candidate, int paramCount) {
/* 971 */       ConstructorProperties cp = candidate.<ConstructorProperties>getAnnotation(ConstructorProperties.class);
/* 972 */       if (cp != null) {
/* 973 */         String[] names = cp.value();
/* 974 */         if (names.length != paramCount) {
/* 975 */           throw new IllegalStateException("Constructor annotated with @ConstructorProperties but not corresponding to actual number of parameters (" + paramCount + "): " + candidate);
/*     */         }
/*     */         
/* 978 */         return names;
/*     */       } 
/*     */       
/* 981 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/ConstructorResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */