/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.aspectj.AspectJProxyUtils;
/*     */ import org.springframework.aop.aspectj.SimpleAspectInstanceFactory;
/*     */ import org.springframework.aop.framework.ProxyCreatorSupport;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AspectJProxyFactory
/*     */   extends ProxyCreatorSupport
/*     */ {
/*  53 */   private static final Map<Class<?>, Object> aspectCache = new ConcurrentHashMap<>();
/*     */   
/*  55 */   private final AspectJAdvisorFactory aspectFactory = new ReflectiveAspectJAdvisorFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AspectJProxyFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AspectJProxyFactory(Object target) {
/*  70 */     Assert.notNull(target, "Target object must not be null");
/*  71 */     setInterfaces(ClassUtils.getAllInterfaces(target));
/*  72 */     setTarget(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AspectJProxyFactory(Class<?>... interfaces) {
/*  80 */     setInterfaces(interfaces);
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
/*     */   public void addAspect(Object aspectInstance) {
/*  92 */     Class<?> aspectClass = aspectInstance.getClass();
/*  93 */     String aspectName = aspectClass.getName();
/*  94 */     AspectMetadata am = createAspectMetadata(aspectClass, aspectName);
/*  95 */     if (am.getAjType().getPerClause().getKind() != PerClauseKind.SINGLETON) {
/*  96 */       throw new IllegalArgumentException("Aspect class [" + aspectClass
/*  97 */           .getName() + "] does not define a singleton aspect");
/*     */     }
/*  99 */     addAdvisorsFromAspectInstanceFactory(new SingletonMetadataAwareAspectInstanceFactory(aspectInstance, aspectName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAspect(Class<?> aspectClass) {
/* 108 */     String aspectName = aspectClass.getName();
/* 109 */     AspectMetadata am = createAspectMetadata(aspectClass, aspectName);
/* 110 */     MetadataAwareAspectInstanceFactory instanceFactory = createAspectInstanceFactory(am, aspectClass, aspectName);
/* 111 */     addAdvisorsFromAspectInstanceFactory(instanceFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAdvisorsFromAspectInstanceFactory(MetadataAwareAspectInstanceFactory instanceFactory) {
/* 121 */     List<Advisor> advisors = this.aspectFactory.getAdvisors(instanceFactory);
/* 122 */     Class<?> targetClass = getTargetClass();
/* 123 */     Assert.state((targetClass != null), "Unresolvable target class");
/* 124 */     advisors = AopUtils.findAdvisorsThatCanApply(advisors, targetClass);
/* 125 */     AspectJProxyUtils.makeAdvisorChainAspectJCapableIfNecessary(advisors);
/* 126 */     AnnotationAwareOrderComparator.sort(advisors);
/* 127 */     addAdvisors(advisors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AspectMetadata createAspectMetadata(Class<?> aspectClass, String aspectName) {
/* 134 */     AspectMetadata am = new AspectMetadata(aspectClass, aspectName);
/* 135 */     if (!am.getAjType().isAspect()) {
/* 136 */       throw new IllegalArgumentException("Class [" + aspectClass.getName() + "] is not a valid aspect type");
/*     */     }
/* 138 */     return am;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataAwareAspectInstanceFactory createAspectInstanceFactory(AspectMetadata am, Class<?> aspectClass, String aspectName) {
/*     */     MetadataAwareAspectInstanceFactory instanceFactory;
/* 150 */     if (am.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
/*     */       
/* 152 */       Object instance = getSingletonAspectInstance(aspectClass);
/* 153 */       instanceFactory = new SingletonMetadataAwareAspectInstanceFactory(instance, aspectName);
/*     */     }
/*     */     else {
/*     */       
/* 157 */       instanceFactory = new SimpleMetadataAwareAspectInstanceFactory(aspectClass, aspectName);
/*     */     } 
/* 159 */     return instanceFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object getSingletonAspectInstance(Class<?> aspectClass) {
/* 168 */     Object instance = aspectCache.get(aspectClass);
/* 169 */     if (instance == null) {
/* 170 */       synchronized (aspectCache) {
/*     */         
/* 172 */         instance = aspectCache.get(aspectClass);
/* 173 */         if (instance == null) {
/* 174 */           instance = (new SimpleAspectInstanceFactory(aspectClass)).getAspectInstance();
/* 175 */           aspectCache.put(aspectClass, instance);
/*     */         } 
/*     */       } 
/*     */     }
/* 179 */     return instance;
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
/*     */   public <T> T getProxy() {
/* 193 */     return (T)createAopProxy().getProxy();
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
/*     */   public <T> T getProxy(ClassLoader classLoader) {
/* 206 */     return (T)createAopProxy().getProxy(classLoader);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/AspectJProxyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */