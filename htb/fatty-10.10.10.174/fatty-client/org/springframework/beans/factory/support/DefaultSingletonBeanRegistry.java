/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanCreationNotAllowedException;
/*     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.beans.factory.config.SingletonBeanRegistry;
/*     */ import org.springframework.core.SimpleAliasRegistry;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class DefaultSingletonBeanRegistry
/*     */   extends SimpleAliasRegistry
/*     */   implements SingletonBeanRegistry
/*     */ {
/*  74 */   private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
/*     */ 
/*     */   
/*  77 */   private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
/*     */ 
/*     */   
/*  80 */   private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
/*     */ 
/*     */   
/*  83 */   private final Set<String> registeredSingletons = new LinkedHashSet<>(256);
/*     */ 
/*     */ 
/*     */   
/*  87 */   private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
/*     */ 
/*     */ 
/*     */   
/*  91 */   private final Set<String> inCreationCheckExclusions = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Set<Exception> suppressedExceptions;
/*     */ 
/*     */   
/*     */   private boolean singletonsCurrentlyInDestruction = false;
/*     */ 
/*     */   
/* 101 */   private final Map<String, Object> disposableBeans = new LinkedHashMap<>();
/*     */ 
/*     */   
/* 104 */   private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<>(16);
/*     */ 
/*     */   
/* 107 */   private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
/*     */ 
/*     */   
/* 110 */   private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
/* 115 */     Assert.notNull(beanName, "Bean name must not be null");
/* 116 */     Assert.notNull(singletonObject, "Singleton object must not be null");
/* 117 */     synchronized (this.singletonObjects) {
/* 118 */       Object oldObject = this.singletonObjects.get(beanName);
/* 119 */       if (oldObject != null) {
/* 120 */         throw new IllegalStateException("Could not register object [" + singletonObject + "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
/*     */       }
/*     */       
/* 123 */       addSingleton(beanName, singletonObject);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSingleton(String beanName, Object singletonObject) {
/* 134 */     synchronized (this.singletonObjects) {
/* 135 */       this.singletonObjects.put(beanName, singletonObject);
/* 136 */       this.singletonFactories.remove(beanName);
/* 137 */       this.earlySingletonObjects.remove(beanName);
/* 138 */       this.registeredSingletons.add(beanName);
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
/*     */   protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
/* 151 */     Assert.notNull(singletonFactory, "Singleton factory must not be null");
/* 152 */     synchronized (this.singletonObjects) {
/* 153 */       if (!this.singletonObjects.containsKey(beanName)) {
/* 154 */         this.singletonFactories.put(beanName, singletonFactory);
/* 155 */         this.earlySingletonObjects.remove(beanName);
/* 156 */         this.registeredSingletons.add(beanName);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSingleton(String beanName) {
/* 164 */     return getSingleton(beanName, true);
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
/*     */   @Nullable
/*     */   protected Object getSingleton(String beanName, boolean allowEarlyReference) {
/* 177 */     Object singletonObject = this.singletonObjects.get(beanName);
/* 178 */     if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
/* 179 */       synchronized (this.singletonObjects) {
/* 180 */         singletonObject = this.earlySingletonObjects.get(beanName);
/* 181 */         if (singletonObject == null && allowEarlyReference) {
/* 182 */           ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
/* 183 */           if (singletonFactory != null) {
/* 184 */             singletonObject = singletonFactory.getObject();
/* 185 */             this.earlySingletonObjects.put(beanName, singletonObject);
/* 186 */             this.singletonFactories.remove(beanName);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 191 */     return singletonObject;
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
/*     */   public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
/* 203 */     Assert.notNull(beanName, "Bean name must not be null");
/* 204 */     synchronized (this.singletonObjects) {
/* 205 */       Object singletonObject = this.singletonObjects.get(beanName);
/* 206 */       if (singletonObject == null) {
/* 207 */         if (this.singletonsCurrentlyInDestruction) {
/* 208 */           throw new BeanCreationNotAllowedException(beanName, "Singleton bean creation not allowed while singletons of this factory are in destruction (Do not request a bean from a BeanFactory in a destroy method implementation!)");
/*     */         }
/*     */ 
/*     */         
/* 212 */         if (this.logger.isDebugEnabled()) {
/* 213 */           this.logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
/*     */         }
/* 215 */         beforeSingletonCreation(beanName);
/* 216 */         boolean newSingleton = false;
/* 217 */         boolean recordSuppressedExceptions = (this.suppressedExceptions == null);
/* 218 */         if (recordSuppressedExceptions) {
/* 219 */           this.suppressedExceptions = new LinkedHashSet<>();
/*     */         }
/*     */         try {
/* 222 */           singletonObject = singletonFactory.getObject();
/* 223 */           newSingleton = true;
/*     */         }
/* 225 */         catch (IllegalStateException ex) {
/*     */ 
/*     */           
/* 228 */           singletonObject = this.singletonObjects.get(beanName);
/* 229 */           if (singletonObject == null) {
/* 230 */             throw ex;
/*     */           }
/*     */         }
/* 233 */         catch (BeanCreationException ex) {
/* 234 */           if (recordSuppressedExceptions) {
/* 235 */             for (Exception suppressedException : this.suppressedExceptions) {
/* 236 */               ex.addRelatedCause(suppressedException);
/*     */             }
/*     */           }
/* 239 */           throw ex;
/*     */         } finally {
/*     */           
/* 242 */           if (recordSuppressedExceptions) {
/* 243 */             this.suppressedExceptions = null;
/*     */           }
/* 245 */           afterSingletonCreation(beanName);
/*     */         } 
/* 247 */         if (newSingleton) {
/* 248 */           addSingleton(beanName, singletonObject);
/*     */         }
/*     */       } 
/* 251 */       return singletonObject;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onSuppressedException(Exception ex) {
/* 261 */     synchronized (this.singletonObjects) {
/* 262 */       if (this.suppressedExceptions != null) {
/* 263 */         this.suppressedExceptions.add(ex);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeSingleton(String beanName) {
/* 275 */     synchronized (this.singletonObjects) {
/* 276 */       this.singletonObjects.remove(beanName);
/* 277 */       this.singletonFactories.remove(beanName);
/* 278 */       this.earlySingletonObjects.remove(beanName);
/* 279 */       this.registeredSingletons.remove(beanName);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsSingleton(String beanName) {
/* 285 */     return this.singletonObjects.containsKey(beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getSingletonNames() {
/* 290 */     synchronized (this.singletonObjects) {
/* 291 */       return StringUtils.toStringArray(this.registeredSingletons);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSingletonCount() {
/* 297 */     synchronized (this.singletonObjects) {
/* 298 */       return this.registeredSingletons.size();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentlyInCreation(String beanName, boolean inCreation) {
/* 304 */     Assert.notNull(beanName, "Bean name must not be null");
/* 305 */     if (!inCreation) {
/* 306 */       this.inCreationCheckExclusions.add(beanName);
/*     */     } else {
/*     */       
/* 309 */       this.inCreationCheckExclusions.remove(beanName);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCurrentlyInCreation(String beanName) {
/* 314 */     Assert.notNull(beanName, "Bean name must not be null");
/* 315 */     return (!this.inCreationCheckExclusions.contains(beanName) && isActuallyInCreation(beanName));
/*     */   }
/*     */   
/*     */   protected boolean isActuallyInCreation(String beanName) {
/* 319 */     return isSingletonCurrentlyInCreation(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingletonCurrentlyInCreation(String beanName) {
/* 328 */     return this.singletonsCurrentlyInCreation.contains(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beforeSingletonCreation(String beanName) {
/* 338 */     if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.add(beanName)) {
/* 339 */       throw new BeanCurrentlyInCreationException(beanName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterSingletonCreation(String beanName) {
/* 350 */     if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.remove(beanName)) {
/* 351 */       throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
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
/*     */   
/*     */   public void registerDisposableBean(String beanName, DisposableBean bean) {
/* 366 */     synchronized (this.disposableBeans) {
/* 367 */       this.disposableBeans.put(beanName, bean);
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
/*     */   public void registerContainedBean(String containedBeanName, String containingBeanName) {
/* 381 */     synchronized (this.containedBeanMap) {
/*     */       
/* 383 */       Set<String> containedBeans = this.containedBeanMap.computeIfAbsent(containingBeanName, k -> new LinkedHashSet(8));
/* 384 */       if (!containedBeans.add(containedBeanName)) {
/*     */         return;
/*     */       }
/*     */     } 
/* 388 */     registerDependentBean(containedBeanName, containingBeanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDependentBean(String beanName, String dependentBeanName) {
/* 398 */     String canonicalName = canonicalName(beanName);
/*     */     
/* 400 */     synchronized (this.dependentBeanMap) {
/*     */       
/* 402 */       Set<String> dependentBeans = this.dependentBeanMap.computeIfAbsent(canonicalName, k -> new LinkedHashSet(8));
/* 403 */       if (!dependentBeans.add(dependentBeanName)) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 408 */     synchronized (this.dependenciesForBeanMap) {
/*     */       
/* 410 */       Set<String> dependenciesForBean = this.dependenciesForBeanMap.computeIfAbsent(dependentBeanName, k -> new LinkedHashSet(8));
/* 411 */       dependenciesForBean.add(canonicalName);
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
/*     */   protected boolean isDependent(String beanName, String dependentBeanName) {
/* 423 */     synchronized (this.dependentBeanMap) {
/* 424 */       return isDependent(beanName, dependentBeanName, (Set<String>)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isDependent(String beanName, String dependentBeanName, @Nullable Set<String> alreadySeen) {
/* 429 */     if (alreadySeen != null && alreadySeen.contains(beanName)) {
/* 430 */       return false;
/*     */     }
/* 432 */     String canonicalName = canonicalName(beanName);
/* 433 */     Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
/* 434 */     if (dependentBeans == null) {
/* 435 */       return false;
/*     */     }
/* 437 */     if (dependentBeans.contains(dependentBeanName)) {
/* 438 */       return true;
/*     */     }
/* 440 */     for (String transitiveDependency : dependentBeans) {
/* 441 */       if (alreadySeen == null) {
/* 442 */         alreadySeen = new HashSet<>();
/*     */       }
/* 444 */       alreadySeen.add(beanName);
/* 445 */       if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
/* 446 */         return true;
/*     */       }
/*     */     } 
/* 449 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasDependentBean(String beanName) {
/* 457 */     return this.dependentBeanMap.containsKey(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDependentBeans(String beanName) {
/* 466 */     Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
/* 467 */     if (dependentBeans == null) {
/* 468 */       return new String[0];
/*     */     }
/* 470 */     synchronized (this.dependentBeanMap) {
/* 471 */       return StringUtils.toStringArray(dependentBeans);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDependenciesForBean(String beanName) {
/* 482 */     Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
/* 483 */     if (dependenciesForBean == null) {
/* 484 */       return new String[0];
/*     */     }
/* 486 */     synchronized (this.dependenciesForBeanMap) {
/* 487 */       return StringUtils.toStringArray(dependenciesForBean);
/*     */     } 
/*     */   }
/*     */   public void destroySingletons() {
/*     */     String[] disposableBeanNames;
/* 492 */     if (this.logger.isTraceEnabled()) {
/* 493 */       this.logger.trace("Destroying singletons in " + this);
/*     */     }
/* 495 */     synchronized (this.singletonObjects) {
/* 496 */       this.singletonsCurrentlyInDestruction = true;
/*     */     } 
/*     */ 
/*     */     
/* 500 */     synchronized (this.disposableBeans) {
/* 501 */       disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
/*     */     } 
/* 503 */     for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
/* 504 */       destroySingleton(disposableBeanNames[i]);
/*     */     }
/*     */     
/* 507 */     this.containedBeanMap.clear();
/* 508 */     this.dependentBeanMap.clear();
/* 509 */     this.dependenciesForBeanMap.clear();
/*     */     
/* 511 */     clearSingletonCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clearSingletonCache() {
/* 519 */     synchronized (this.singletonObjects) {
/* 520 */       this.singletonObjects.clear();
/* 521 */       this.singletonFactories.clear();
/* 522 */       this.earlySingletonObjects.clear();
/* 523 */       this.registeredSingletons.clear();
/* 524 */       this.singletonsCurrentlyInDestruction = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroySingleton(String beanName) {
/*     */     DisposableBean disposableBean;
/* 536 */     removeSingleton(beanName);
/*     */ 
/*     */ 
/*     */     
/* 540 */     synchronized (this.disposableBeans) {
/* 541 */       disposableBean = (DisposableBean)this.disposableBeans.remove(beanName);
/*     */     } 
/* 543 */     destroyBean(beanName, disposableBean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroyBean(String beanName, @Nullable DisposableBean bean) {
/*     */     Set<String> dependencies, containedBeans;
/* 555 */     synchronized (this.dependentBeanMap) {
/*     */       
/* 557 */       dependencies = this.dependentBeanMap.remove(beanName);
/*     */     } 
/* 559 */     if (dependencies != null) {
/* 560 */       if (this.logger.isTraceEnabled()) {
/* 561 */         this.logger.trace("Retrieved dependent beans for bean '" + beanName + "': " + dependencies);
/*     */       }
/* 563 */       for (String dependentBeanName : dependencies) {
/* 564 */         destroySingleton(dependentBeanName);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 569 */     if (bean != null) {
/*     */       try {
/* 571 */         bean.destroy();
/*     */       }
/* 573 */       catch (Throwable ex) {
/* 574 */         if (this.logger.isWarnEnabled()) {
/* 575 */           this.logger.warn("Destruction of bean with name '" + beanName + "' threw an exception", ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 582 */     synchronized (this.containedBeanMap) {
/*     */       
/* 584 */       containedBeans = this.containedBeanMap.remove(beanName);
/*     */     } 
/* 586 */     if (containedBeans != null) {
/* 587 */       for (String containedBeanName : containedBeans) {
/* 588 */         destroySingleton(containedBeanName);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 593 */     synchronized (this.dependentBeanMap) {
/* 594 */       for (Iterator<Map.Entry<String, Set<String>>> it = this.dependentBeanMap.entrySet().iterator(); it.hasNext(); ) {
/* 595 */         Map.Entry<String, Set<String>> entry = it.next();
/* 596 */         Set<String> dependenciesToClean = entry.getValue();
/* 597 */         dependenciesToClean.remove(beanName);
/* 598 */         if (dependenciesToClean.isEmpty()) {
/* 599 */           it.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 605 */     this.dependenciesForBeanMap.remove(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getSingletonMutex() {
/* 616 */     return this.singletonObjects;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/DefaultSingletonBeanRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */