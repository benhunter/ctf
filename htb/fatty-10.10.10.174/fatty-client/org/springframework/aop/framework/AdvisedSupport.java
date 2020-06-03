/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.IntroductionInfo;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.support.DefaultIntroductionAdvisor;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.aop.target.EmptyTargetSource;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AdvisedSupport
/*     */   extends ProxyConfig
/*     */   implements Advised
/*     */ {
/*     */   private static final long serialVersionUID = 2651364800145442165L;
/*  71 */   public static final TargetSource EMPTY_TARGET_SOURCE = (TargetSource)EmptyTargetSource.INSTANCE;
/*     */ 
/*     */ 
/*     */   
/*  75 */   TargetSource targetSource = EMPTY_TARGET_SOURCE;
/*     */ 
/*     */   
/*     */   private boolean preFiltered = false;
/*     */ 
/*     */   
/*  81 */   AdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient Map<MethodCacheKey, List<Object>> methodCache;
/*     */ 
/*     */ 
/*     */   
/*  90 */   private List<Class<?>> interfaces = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   private List<Advisor> advisors = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   private Advisor[] advisorArray = new Advisor[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AdvisedSupport() {
/* 109 */     this.methodCache = new ConcurrentHashMap<>(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AdvisedSupport(Class<?>... interfaces) {
/* 117 */     this();
/* 118 */     setInterfaces(interfaces);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(Object target) {
/* 129 */     setTargetSource((TargetSource)new SingletonTargetSource(target));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTargetSource(@Nullable TargetSource targetSource) {
/* 134 */     this.targetSource = (targetSource != null) ? targetSource : EMPTY_TARGET_SOURCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public TargetSource getTargetSource() {
/* 139 */     return this.targetSource;
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
/*     */   public void setTargetClass(@Nullable Class<?> targetClass) {
/* 156 */     this.targetSource = (TargetSource)EmptyTargetSource.forClass(targetClass);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getTargetClass() {
/* 162 */     return this.targetSource.getTargetClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPreFiltered(boolean preFiltered) {
/* 167 */     this.preFiltered = preFiltered;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPreFiltered() {
/* 172 */     return this.preFiltered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvisorChainFactory(AdvisorChainFactory advisorChainFactory) {
/* 180 */     Assert.notNull(advisorChainFactory, "AdvisorChainFactory must not be null");
/* 181 */     this.advisorChainFactory = advisorChainFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AdvisorChainFactory getAdvisorChainFactory() {
/* 188 */     return this.advisorChainFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterfaces(Class<?>... interfaces) {
/* 196 */     Assert.notNull(interfaces, "Interfaces must not be null");
/* 197 */     this.interfaces.clear();
/* 198 */     for (Class<?> ifc : interfaces) {
/* 199 */       addInterface(ifc);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInterface(Class<?> intf) {
/* 208 */     Assert.notNull(intf, "Interface must not be null");
/* 209 */     if (!intf.isInterface()) {
/* 210 */       throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
/*     */     }
/* 212 */     if (!this.interfaces.contains(intf)) {
/* 213 */       this.interfaces.add(intf);
/* 214 */       adviceChanged();
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
/*     */   public boolean removeInterface(Class<?> intf) {
/* 226 */     return this.interfaces.remove(intf);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getProxiedInterfaces() {
/* 231 */     return ClassUtils.toClassArray(this.interfaces);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterfaceProxied(Class<?> intf) {
/* 236 */     for (Class<?> proxyIntf : this.interfaces) {
/* 237 */       if (intf.isAssignableFrom(proxyIntf)) {
/* 238 */         return true;
/*     */       }
/*     */     } 
/* 241 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Advisor[] getAdvisors() {
/* 247 */     return this.advisorArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdvisor(Advisor advisor) {
/* 252 */     int pos = this.advisors.size();
/* 253 */     addAdvisor(pos, advisor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdvisor(int pos, Advisor advisor) throws AopConfigException {
/* 258 */     if (advisor instanceof IntroductionAdvisor) {
/* 259 */       validateIntroductionAdvisor((IntroductionAdvisor)advisor);
/*     */     }
/* 261 */     addAdvisorInternal(pos, advisor);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAdvisor(Advisor advisor) {
/* 266 */     int index = indexOf(advisor);
/* 267 */     if (index == -1) {
/* 268 */       return false;
/*     */     }
/*     */     
/* 271 */     removeAdvisor(index);
/* 272 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAdvisor(int index) throws AopConfigException {
/* 278 */     if (isFrozen()) {
/* 279 */       throw new AopConfigException("Cannot remove Advisor: Configuration is frozen.");
/*     */     }
/* 281 */     if (index < 0 || index > this.advisors.size() - 1) {
/* 282 */       throw new AopConfigException("Advisor index " + index + " is out of bounds: This configuration only has " + this.advisors
/* 283 */           .size() + " advisors.");
/*     */     }
/*     */     
/* 286 */     Advisor advisor = this.advisors.get(index);
/* 287 */     if (advisor instanceof IntroductionAdvisor) {
/* 288 */       IntroductionAdvisor ia = (IntroductionAdvisor)advisor;
/*     */       
/* 290 */       for (int j = 0; j < (ia.getInterfaces()).length; j++) {
/* 291 */         removeInterface(ia.getInterfaces()[j]);
/*     */       }
/*     */     } 
/*     */     
/* 295 */     this.advisors.remove(index);
/* 296 */     updateAdvisorArray();
/* 297 */     adviceChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Advisor advisor) {
/* 302 */     Assert.notNull(advisor, "Advisor must not be null");
/* 303 */     return this.advisors.indexOf(advisor);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean replaceAdvisor(Advisor a, Advisor b) throws AopConfigException {
/* 308 */     Assert.notNull(a, "Advisor a must not be null");
/* 309 */     Assert.notNull(b, "Advisor b must not be null");
/* 310 */     int index = indexOf(a);
/* 311 */     if (index == -1) {
/* 312 */       return false;
/*     */     }
/* 314 */     removeAdvisor(index);
/* 315 */     addAdvisor(index, b);
/* 316 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdvisors(Advisor... advisors) {
/* 324 */     addAdvisors(Arrays.asList(advisors));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdvisors(Collection<Advisor> advisors) {
/* 332 */     if (isFrozen()) {
/* 333 */       throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
/*     */     }
/* 335 */     if (!CollectionUtils.isEmpty(advisors)) {
/* 336 */       for (Advisor advisor : advisors) {
/* 337 */         if (advisor instanceof IntroductionAdvisor) {
/* 338 */           validateIntroductionAdvisor((IntroductionAdvisor)advisor);
/*     */         }
/* 340 */         Assert.notNull(advisor, "Advisor must not be null");
/* 341 */         this.advisors.add(advisor);
/*     */       } 
/* 343 */       updateAdvisorArray();
/* 344 */       adviceChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validateIntroductionAdvisor(IntroductionAdvisor advisor) {
/* 349 */     advisor.validateInterfaces();
/*     */     
/* 351 */     Class<?>[] ifcs = advisor.getInterfaces();
/* 352 */     for (Class<?> ifc : ifcs) {
/* 353 */       addInterface(ifc);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addAdvisorInternal(int pos, Advisor advisor) throws AopConfigException {
/* 358 */     Assert.notNull(advisor, "Advisor must not be null");
/* 359 */     if (isFrozen()) {
/* 360 */       throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
/*     */     }
/* 362 */     if (pos > this.advisors.size()) {
/* 363 */       throw new IllegalArgumentException("Illegal position " + pos + " in advisor list with size " + this.advisors
/* 364 */           .size());
/*     */     }
/* 366 */     this.advisors.add(pos, advisor);
/* 367 */     updateAdvisorArray();
/* 368 */     adviceChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void updateAdvisorArray() {
/* 375 */     this.advisorArray = this.advisors.<Advisor>toArray(new Advisor[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final List<Advisor> getAdvisorsInternal() {
/* 384 */     return this.advisors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdvice(Advice advice) throws AopConfigException {
/* 390 */     int pos = this.advisors.size();
/* 391 */     addAdvice(pos, advice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdvice(int pos, Advice advice) throws AopConfigException {
/* 399 */     Assert.notNull(advice, "Advice must not be null");
/* 400 */     if (advice instanceof IntroductionInfo) {
/*     */ 
/*     */       
/* 403 */       addAdvisor(pos, (Advisor)new DefaultIntroductionAdvisor(advice, (IntroductionInfo)advice));
/*     */     } else {
/* 405 */       if (advice instanceof org.springframework.aop.DynamicIntroductionAdvice)
/*     */       {
/* 407 */         throw new AopConfigException("DynamicIntroductionAdvice may only be added as part of IntroductionAdvisor");
/*     */       }
/*     */       
/* 410 */       addAdvisor(pos, (Advisor)new DefaultPointcutAdvisor(advice));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAdvice(Advice advice) throws AopConfigException {
/* 416 */     int index = indexOf(advice);
/* 417 */     if (index == -1) {
/* 418 */       return false;
/*     */     }
/*     */     
/* 421 */     removeAdvisor(index);
/* 422 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(Advice advice) {
/* 428 */     Assert.notNull(advice, "Advice must not be null");
/* 429 */     for (int i = 0; i < this.advisors.size(); i++) {
/* 430 */       Advisor advisor = this.advisors.get(i);
/* 431 */       if (advisor.getAdvice() == advice) {
/* 432 */         return i;
/*     */       }
/*     */     } 
/* 435 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean adviceIncluded(@Nullable Advice advice) {
/* 444 */     if (advice != null) {
/* 445 */       for (Advisor advisor : this.advisors) {
/* 446 */         if (advisor.getAdvice() == advice) {
/* 447 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 451 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int countAdvicesOfType(@Nullable Class<?> adviceClass) {
/* 460 */     int count = 0;
/* 461 */     if (adviceClass != null) {
/* 462 */       for (Advisor advisor : this.advisors) {
/* 463 */         if (adviceClass.isInstance(advisor.getAdvice())) {
/* 464 */           count++;
/*     */         }
/*     */       } 
/*     */     }
/* 468 */     return count;
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
/*     */   public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, @Nullable Class<?> targetClass) {
/* 480 */     MethodCacheKey cacheKey = new MethodCacheKey(method);
/* 481 */     List<Object> cached = this.methodCache.get(cacheKey);
/* 482 */     if (cached == null) {
/* 483 */       cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this, method, targetClass);
/*     */       
/* 485 */       this.methodCache.put(cacheKey, cached);
/*     */     } 
/* 487 */     return cached;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void adviceChanged() {
/* 494 */     this.methodCache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyConfigurationFrom(AdvisedSupport other) {
/* 503 */     copyConfigurationFrom(other, other.targetSource, new ArrayList<>(other.advisors));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyConfigurationFrom(AdvisedSupport other, TargetSource targetSource, List<Advisor> advisors) {
/* 514 */     copyFrom(other);
/* 515 */     this.targetSource = targetSource;
/* 516 */     this.advisorChainFactory = other.advisorChainFactory;
/* 517 */     this.interfaces = new ArrayList<>(other.interfaces);
/* 518 */     for (Advisor advisor : advisors) {
/* 519 */       if (advisor instanceof IntroductionAdvisor) {
/* 520 */         validateIntroductionAdvisor((IntroductionAdvisor)advisor);
/*     */       }
/* 522 */       Assert.notNull(advisor, "Advisor must not be null");
/* 523 */       this.advisors.add(advisor);
/*     */     } 
/* 525 */     updateAdvisorArray();
/* 526 */     adviceChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AdvisedSupport getConfigurationOnlyCopy() {
/* 534 */     AdvisedSupport copy = new AdvisedSupport();
/* 535 */     copy.copyFrom(this);
/* 536 */     copy.targetSource = (TargetSource)EmptyTargetSource.forClass(getTargetClass(), getTargetSource().isStatic());
/* 537 */     copy.advisorChainFactory = this.advisorChainFactory;
/* 538 */     copy.interfaces = this.interfaces;
/* 539 */     copy.advisors = this.advisors;
/* 540 */     copy.updateAdvisorArray();
/* 541 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 551 */     ois.defaultReadObject();
/*     */ 
/*     */     
/* 554 */     this.methodCache = new ConcurrentHashMap<>(32);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toProxyConfigString() {
/* 560 */     return toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 568 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 569 */     sb.append(": ").append(this.interfaces.size()).append(" interfaces ");
/* 570 */     sb.append(ClassUtils.classNamesToString(this.interfaces)).append("; ");
/* 571 */     sb.append(this.advisors.size()).append(" advisors ");
/* 572 */     sb.append(this.advisors).append("; ");
/* 573 */     sb.append("targetSource [").append(this.targetSource).append("]; ");
/* 574 */     sb.append(super.toString());
/* 575 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class MethodCacheKey
/*     */     implements Comparable<MethodCacheKey>
/*     */   {
/*     */     private final Method method;
/*     */ 
/*     */     
/*     */     private final int hashCode;
/*     */ 
/*     */     
/*     */     public MethodCacheKey(Method method) {
/* 590 */       this.method = method;
/* 591 */       this.hashCode = method.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 596 */       return (this == other || (other instanceof MethodCacheKey && this.method == ((MethodCacheKey)other).method));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 602 */       return this.hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 607 */       return this.method.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(MethodCacheKey other) {
/* 612 */       int result = this.method.getName().compareTo(other.method.getName());
/* 613 */       if (result == 0) {
/* 614 */         result = this.method.toString().compareTo(other.method.toString());
/*     */       }
/* 616 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/AdvisedSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */