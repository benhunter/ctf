/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InitDestroyAnnotationBeanPostProcessor
/*     */   implements DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, PriorityOrdered, Serializable
/*     */ {
/*  80 */   protected transient Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private Class<? extends Annotation> initAnnotationType;
/*     */   
/*     */   @Nullable
/*     */   private Class<? extends Annotation> destroyAnnotationType;
/*     */   
/*  88 */   private int order = Integer.MAX_VALUE;
/*     */   @Nullable
/*  90 */   private final transient Map<Class<?>, LifecycleMetadata> lifecycleMetadataCache = new ConcurrentHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitAnnotationType(Class<? extends Annotation> initAnnotationType) {
/* 102 */     this.initAnnotationType = initAnnotationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestroyAnnotationType(Class<? extends Annotation> destroyAnnotationType) {
/* 113 */     this.destroyAnnotationType = destroyAnnotationType;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 117 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 122 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
/* 128 */     LifecycleMetadata metadata = findLifecycleMetadata(beanType);
/* 129 */     metadata.checkConfigMembers(beanDefinition);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/* 134 */     LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
/*     */     try {
/* 136 */       metadata.invokeInitMethods(bean, beanName);
/*     */     }
/* 138 */     catch (InvocationTargetException ex) {
/* 139 */       throw new BeanCreationException(beanName, "Invocation of init method failed", ex.getTargetException());
/*     */     }
/* 141 */     catch (Throwable ex) {
/* 142 */       throw new BeanCreationException(beanName, "Failed to invoke init method", ex);
/*     */     } 
/* 144 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
/* 149 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
/* 154 */     LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
/*     */     try {
/* 156 */       metadata.invokeDestroyMethods(bean, beanName);
/*     */     }
/* 158 */     catch (InvocationTargetException ex) {
/* 159 */       String msg = "Destroy method on bean with name '" + beanName + "' threw an exception";
/* 160 */       if (this.logger.isDebugEnabled()) {
/* 161 */         this.logger.warn(msg, ex.getTargetException());
/*     */       } else {
/*     */         
/* 164 */         this.logger.warn(msg + ": " + ex.getTargetException());
/*     */       }
/*     */     
/* 167 */     } catch (Throwable ex) {
/* 168 */       this.logger.warn("Failed to invoke destroy method on bean with name '" + beanName + "'", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresDestruction(Object bean) {
/* 174 */     return findLifecycleMetadata(bean.getClass()).hasDestroyMethods();
/*     */   }
/*     */ 
/*     */   
/*     */   private LifecycleMetadata findLifecycleMetadata(Class<?> clazz) {
/* 179 */     if (this.lifecycleMetadataCache == null)
/*     */     {
/* 181 */       return buildLifecycleMetadata(clazz);
/*     */     }
/*     */     
/* 184 */     LifecycleMetadata metadata = this.lifecycleMetadataCache.get(clazz);
/* 185 */     if (metadata == null) {
/* 186 */       synchronized (this.lifecycleMetadataCache) {
/* 187 */         metadata = this.lifecycleMetadataCache.get(clazz);
/* 188 */         if (metadata == null) {
/* 189 */           metadata = buildLifecycleMetadata(clazz);
/* 190 */           this.lifecycleMetadataCache.put(clazz, metadata);
/*     */         } 
/* 192 */         return metadata;
/*     */       } 
/*     */     }
/* 195 */     return metadata;
/*     */   }
/*     */   
/*     */   private LifecycleMetadata buildLifecycleMetadata(Class<?> clazz) {
/* 199 */     List<LifecycleElement> initMethods = new ArrayList<>();
/* 200 */     List<LifecycleElement> destroyMethods = new ArrayList<>();
/* 201 */     Class<?> targetClass = clazz;
/*     */     
/*     */     do {
/* 204 */       List<LifecycleElement> currInitMethods = new ArrayList<>();
/* 205 */       List<LifecycleElement> currDestroyMethods = new ArrayList<>();
/*     */       
/* 207 */       ReflectionUtils.doWithLocalMethods(targetClass, method -> {
/*     */             if (this.initAnnotationType != null && method.isAnnotationPresent(this.initAnnotationType)) {
/*     */               LifecycleElement element = new LifecycleElement(method);
/*     */               
/*     */               currInitMethods.add(element);
/*     */               if (this.logger.isTraceEnabled()) {
/*     */                 this.logger.trace("Found init method on class [" + clazz.getName() + "]: " + method);
/*     */               }
/*     */             } 
/*     */             if (this.destroyAnnotationType != null && method.isAnnotationPresent(this.destroyAnnotationType)) {
/*     */               currDestroyMethods.add(new LifecycleElement(method));
/*     */               if (this.logger.isTraceEnabled()) {
/*     */                 this.logger.trace("Found destroy method on class [" + clazz.getName() + "]: " + method);
/*     */               }
/*     */             } 
/*     */           });
/* 223 */       initMethods.addAll(0, currInitMethods);
/* 224 */       destroyMethods.addAll(currDestroyMethods);
/* 225 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 227 */     while (targetClass != null && targetClass != Object.class);
/*     */     
/* 229 */     return new LifecycleMetadata(clazz, initMethods, destroyMethods);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 239 */     ois.defaultReadObject();
/*     */ 
/*     */     
/* 242 */     this.logger = LogFactory.getLog(getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class LifecycleMetadata
/*     */   {
/*     */     private final Class<?> targetClass;
/*     */ 
/*     */     
/*     */     private final Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> initMethods;
/*     */ 
/*     */     
/*     */     private final Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> destroyMethods;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private volatile Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedInitMethods;
/*     */     
/*     */     @Nullable
/*     */     private volatile Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedDestroyMethods;
/*     */ 
/*     */     
/*     */     public LifecycleMetadata(Class<?> targetClass, Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> initMethods, Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> destroyMethods) {
/* 266 */       this.targetClass = targetClass;
/* 267 */       this.initMethods = initMethods;
/* 268 */       this.destroyMethods = destroyMethods;
/*     */     }
/*     */     
/*     */     public void checkConfigMembers(RootBeanDefinition beanDefinition) {
/* 272 */       Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedInitMethods = new LinkedHashSet<>(this.initMethods.size());
/* 273 */       for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : this.initMethods) {
/* 274 */         String methodIdentifier = element.getIdentifier();
/* 275 */         if (!beanDefinition.isExternallyManagedInitMethod(methodIdentifier)) {
/* 276 */           beanDefinition.registerExternallyManagedInitMethod(methodIdentifier);
/* 277 */           checkedInitMethods.add(element);
/* 278 */           if (InitDestroyAnnotationBeanPostProcessor.this.logger.isTraceEnabled()) {
/* 279 */             InitDestroyAnnotationBeanPostProcessor.this.logger.trace("Registered init method on class [" + this.targetClass.getName() + "]: " + element);
/*     */           }
/*     */         } 
/*     */       } 
/* 283 */       Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedDestroyMethods = new LinkedHashSet<>(this.destroyMethods.size());
/* 284 */       for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : this.destroyMethods) {
/* 285 */         String methodIdentifier = element.getIdentifier();
/* 286 */         if (!beanDefinition.isExternallyManagedDestroyMethod(methodIdentifier)) {
/* 287 */           beanDefinition.registerExternallyManagedDestroyMethod(methodIdentifier);
/* 288 */           checkedDestroyMethods.add(element);
/* 289 */           if (InitDestroyAnnotationBeanPostProcessor.this.logger.isTraceEnabled()) {
/* 290 */             InitDestroyAnnotationBeanPostProcessor.this.logger.trace("Registered destroy method on class [" + this.targetClass.getName() + "]: " + element);
/*     */           }
/*     */         } 
/*     */       } 
/* 294 */       this.checkedInitMethods = checkedInitMethods;
/* 295 */       this.checkedDestroyMethods = checkedDestroyMethods;
/*     */     }
/*     */     
/*     */     public void invokeInitMethods(Object target, String beanName) throws Throwable {
/* 299 */       Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedInitMethods = this.checkedInitMethods;
/* 300 */       Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> initMethodsToIterate = (checkedInitMethods != null) ? checkedInitMethods : this.initMethods;
/*     */       
/* 302 */       if (!initMethodsToIterate.isEmpty()) {
/* 303 */         for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : initMethodsToIterate) {
/* 304 */           if (InitDestroyAnnotationBeanPostProcessor.this.logger.isTraceEnabled()) {
/* 305 */             InitDestroyAnnotationBeanPostProcessor.this.logger.trace("Invoking init method on bean '" + beanName + "': " + element.getMethod());
/*     */           }
/* 307 */           element.invoke(target);
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     public void invokeDestroyMethods(Object target, String beanName) throws Throwable {
/* 313 */       Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedDestroyMethods = this.checkedDestroyMethods;
/* 314 */       Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> destroyMethodsToUse = (checkedDestroyMethods != null) ? checkedDestroyMethods : this.destroyMethods;
/*     */       
/* 316 */       if (!destroyMethodsToUse.isEmpty()) {
/* 317 */         for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : destroyMethodsToUse) {
/* 318 */           if (InitDestroyAnnotationBeanPostProcessor.this.logger.isTraceEnabled()) {
/* 319 */             InitDestroyAnnotationBeanPostProcessor.this.logger.trace("Invoking destroy method on bean '" + beanName + "': " + element.getMethod());
/*     */           }
/* 321 */           element.invoke(target);
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean hasDestroyMethods() {
/* 327 */       Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedDestroyMethods = this.checkedDestroyMethods;
/* 328 */       Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> destroyMethodsToUse = (checkedDestroyMethods != null) ? checkedDestroyMethods : this.destroyMethods;
/*     */       
/* 330 */       return !destroyMethodsToUse.isEmpty();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LifecycleElement
/*     */   {
/*     */     private final Method method;
/*     */ 
/*     */     
/*     */     private final String identifier;
/*     */ 
/*     */     
/*     */     public LifecycleElement(Method method) {
/* 345 */       if (method.getParameterCount() != 0) {
/* 346 */         throw new IllegalStateException("Lifecycle method annotation requires a no-arg method: " + method);
/*     */       }
/* 348 */       this.method = method;
/* 349 */       this
/* 350 */         .identifier = Modifier.isPrivate(method.getModifiers()) ? ClassUtils.getQualifiedMethodName(method) : method.getName();
/*     */     }
/*     */     
/*     */     public Method getMethod() {
/* 354 */       return this.method;
/*     */     }
/*     */     
/*     */     public String getIdentifier() {
/* 358 */       return this.identifier;
/*     */     }
/*     */     
/*     */     public void invoke(Object target) throws Throwable {
/* 362 */       ReflectionUtils.makeAccessible(this.method);
/* 363 */       this.method.invoke(target, (Object[])null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 368 */       if (this == other) {
/* 369 */         return true;
/*     */       }
/* 371 */       if (!(other instanceof LifecycleElement)) {
/* 372 */         return false;
/*     */       }
/* 374 */       LifecycleElement otherElement = (LifecycleElement)other;
/* 375 */       return this.identifier.equals(otherElement.identifier);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 380 */       return this.identifier.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/annotation/InitDestroyAnnotationBeanPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */