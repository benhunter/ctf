/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractApplicationEventMulticaster
/*     */   implements ApplicationEventMulticaster, BeanClassLoaderAware, BeanFactoryAware
/*     */ {
/*  65 */   private final ListenerRetriever defaultRetriever = new ListenerRetriever(false);
/*     */   
/*  67 */   final Map<ListenerCacheKey, ListenerRetriever> retrieverCache = new ConcurrentHashMap<>(64);
/*     */   
/*     */   @Nullable
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */   
/*  75 */   private Object retrievalMutex = this.defaultRetriever;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  80 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  85 */     this.beanFactory = beanFactory;
/*  86 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/*  87 */       ConfigurableBeanFactory cbf = (ConfigurableBeanFactory)beanFactory;
/*  88 */       if (this.beanClassLoader == null) {
/*  89 */         this.beanClassLoader = cbf.getBeanClassLoader();
/*     */       }
/*  91 */       this.retrievalMutex = cbf.getSingletonMutex();
/*     */     } 
/*     */   }
/*     */   
/*     */   private BeanFactory getBeanFactory() {
/*  96 */     if (this.beanFactory == null) {
/*  97 */       throw new IllegalStateException("ApplicationEventMulticaster cannot retrieve listener beans because it is not associated with a BeanFactory");
/*     */     }
/*     */     
/* 100 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addApplicationListener(ApplicationListener<?> listener) {
/* 106 */     synchronized (this.retrievalMutex) {
/*     */ 
/*     */       
/* 109 */       Object singletonTarget = AopProxyUtils.getSingletonTarget(listener);
/* 110 */       if (singletonTarget instanceof ApplicationListener) {
/* 111 */         this.defaultRetriever.applicationListeners.remove(singletonTarget);
/*     */       }
/* 113 */       this.defaultRetriever.applicationListeners.add(listener);
/* 114 */       this.retrieverCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addApplicationListenerBean(String listenerBeanName) {
/* 120 */     synchronized (this.retrievalMutex) {
/* 121 */       this.defaultRetriever.applicationListenerBeans.add(listenerBeanName);
/* 122 */       this.retrieverCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeApplicationListener(ApplicationListener<?> listener) {
/* 128 */     synchronized (this.retrievalMutex) {
/* 129 */       this.defaultRetriever.applicationListeners.remove(listener);
/* 130 */       this.retrieverCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeApplicationListenerBean(String listenerBeanName) {
/* 136 */     synchronized (this.retrievalMutex) {
/* 137 */       this.defaultRetriever.applicationListenerBeans.remove(listenerBeanName);
/* 138 */       this.retrieverCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAllListeners() {
/* 144 */     synchronized (this.retrievalMutex) {
/* 145 */       this.defaultRetriever.applicationListeners.clear();
/* 146 */       this.defaultRetriever.applicationListenerBeans.clear();
/* 147 */       this.retrieverCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<ApplicationListener<?>> getApplicationListeners() {
/* 158 */     synchronized (this.retrievalMutex) {
/* 159 */       return this.defaultRetriever.getApplicationListeners();
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
/*     */   
/*     */   protected Collection<ApplicationListener<?>> getApplicationListeners(ApplicationEvent event, ResolvableType eventType) {
/* 175 */     Object source = event.getSource();
/* 176 */     Class<?> sourceType = (source != null) ? source.getClass() : null;
/* 177 */     ListenerCacheKey cacheKey = new ListenerCacheKey(eventType, sourceType);
/*     */ 
/*     */     
/* 180 */     ListenerRetriever retriever = this.retrieverCache.get(cacheKey);
/* 181 */     if (retriever != null) {
/* 182 */       return retriever.getApplicationListeners();
/*     */     }
/*     */     
/* 185 */     if (this.beanClassLoader == null || (
/* 186 */       ClassUtils.isCacheSafe(event.getClass(), this.beanClassLoader) && (sourceType == null || 
/* 187 */       ClassUtils.isCacheSafe(sourceType, this.beanClassLoader))))
/*     */     {
/* 189 */       synchronized (this.retrievalMutex) {
/* 190 */         retriever = this.retrieverCache.get(cacheKey);
/* 191 */         if (retriever != null) {
/* 192 */           return retriever.getApplicationListeners();
/*     */         }
/* 194 */         retriever = new ListenerRetriever(true);
/*     */         
/* 196 */         Collection<ApplicationListener<?>> listeners = retrieveApplicationListeners(eventType, sourceType, retriever);
/* 197 */         this.retrieverCache.put(cacheKey, retriever);
/* 198 */         return listeners;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 203 */     return retrieveApplicationListeners(eventType, sourceType, null);
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
/*     */   private Collection<ApplicationListener<?>> retrieveApplicationListeners(ResolvableType eventType, @Nullable Class<?> sourceType, @Nullable ListenerRetriever retriever) {
/*     */     Set<ApplicationListener<?>> listeners;
/*     */     Set<String> listenerBeans;
/* 217 */     List<ApplicationListener<?>> allListeners = new ArrayList<>();
/*     */ 
/*     */     
/* 220 */     synchronized (this.retrievalMutex) {
/* 221 */       listeners = new LinkedHashSet<>(this.defaultRetriever.applicationListeners);
/* 222 */       listenerBeans = new LinkedHashSet<>(this.defaultRetriever.applicationListenerBeans);
/*     */     } 
/* 224 */     for (ApplicationListener<?> listener : listeners) {
/* 225 */       if (supportsEvent(listener, eventType, sourceType)) {
/* 226 */         if (retriever != null) {
/* 227 */           retriever.applicationListeners.add(listener);
/*     */         }
/* 229 */         allListeners.add(listener);
/*     */       } 
/*     */     } 
/* 232 */     if (!listenerBeans.isEmpty()) {
/* 233 */       BeanFactory beanFactory = getBeanFactory();
/* 234 */       for (String listenerBeanName : listenerBeans) {
/*     */         try {
/* 236 */           Class<?> listenerType = beanFactory.getType(listenerBeanName);
/* 237 */           if (listenerType == null || supportsEvent(listenerType, eventType)) {
/*     */             
/* 239 */             ApplicationListener<?> listener = (ApplicationListener)beanFactory.getBean(listenerBeanName, ApplicationListener.class);
/* 240 */             if (!allListeners.contains(listener) && supportsEvent(listener, eventType, sourceType)) {
/* 241 */               if (retriever != null) {
/* 242 */                 if (beanFactory.isSingleton(listenerBeanName)) {
/* 243 */                   retriever.applicationListeners.add(listener);
/*     */                 } else {
/*     */                   
/* 246 */                   retriever.applicationListenerBeans.add(listenerBeanName);
/*     */                 } 
/*     */               }
/* 249 */               allListeners.add(listener);
/*     */             }
/*     */           
/*     */           } 
/* 253 */         } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 259 */     AnnotationAwareOrderComparator.sort(allListeners);
/* 260 */     if (retriever != null && retriever.applicationListenerBeans.isEmpty()) {
/* 261 */       retriever.applicationListeners.clear();
/* 262 */       retriever.applicationListeners.addAll(allListeners);
/*     */     } 
/* 264 */     return allListeners;
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
/*     */   protected boolean supportsEvent(Class<?> listenerType, ResolvableType eventType) {
/* 279 */     if (GenericApplicationListener.class.isAssignableFrom(listenerType) || SmartApplicationListener.class
/* 280 */       .isAssignableFrom(listenerType)) {
/* 281 */       return true;
/*     */     }
/* 283 */     ResolvableType declaredEventType = GenericApplicationListenerAdapter.resolveDeclaredEventType(listenerType);
/* 284 */     return (declaredEventType == null || declaredEventType.isAssignableFrom(eventType));
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
/*     */   protected boolean supportsEvent(ApplicationListener<?> listener, ResolvableType eventType, @Nullable Class<?> sourceType) {
/* 302 */     GenericApplicationListener smartListener = (listener instanceof GenericApplicationListener) ? (GenericApplicationListener)listener : new GenericApplicationListenerAdapter(listener);
/*     */     
/* 304 */     return (smartListener.supportsEventType(eventType) && smartListener.supportsSourceType(sourceType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ListenerCacheKey
/*     */     implements Comparable<ListenerCacheKey>
/*     */   {
/*     */     private final ResolvableType eventType;
/*     */     
/*     */     @Nullable
/*     */     private final Class<?> sourceType;
/*     */ 
/*     */     
/*     */     public ListenerCacheKey(ResolvableType eventType, @Nullable Class<?> sourceType) {
/* 319 */       Assert.notNull(eventType, "Event type must not be null");
/* 320 */       this.eventType = eventType;
/* 321 */       this.sourceType = sourceType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 326 */       if (this == other) {
/* 327 */         return true;
/*     */       }
/* 329 */       ListenerCacheKey otherKey = (ListenerCacheKey)other;
/* 330 */       return (this.eventType.equals(otherKey.eventType) && 
/* 331 */         ObjectUtils.nullSafeEquals(this.sourceType, otherKey.sourceType));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 336 */       return this.eventType.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.sourceType);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 341 */       return "ListenerCacheKey [eventType = " + this.eventType + ", sourceType = " + this.sourceType + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(ListenerCacheKey other) {
/* 346 */       int result = this.eventType.toString().compareTo(other.eventType.toString());
/* 347 */       if (result == 0) {
/* 348 */         if (this.sourceType == null) {
/* 349 */           return (other.sourceType == null) ? 0 : -1;
/*     */         }
/* 351 */         if (other.sourceType == null) {
/* 352 */           return 1;
/*     */         }
/* 354 */         result = this.sourceType.getName().compareTo(other.sourceType.getName());
/*     */       } 
/* 356 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ListenerRetriever
/*     */   {
/* 368 */     public final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();
/*     */     
/* 370 */     public final Set<String> applicationListenerBeans = new LinkedHashSet<>();
/*     */     
/*     */     private final boolean preFiltered;
/*     */     
/*     */     public ListenerRetriever(boolean preFiltered) {
/* 375 */       this.preFiltered = preFiltered;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<ApplicationListener<?>> getApplicationListeners() {
/* 380 */       List<ApplicationListener<?>> allListeners = new ArrayList<>(this.applicationListeners.size() + this.applicationListenerBeans.size());
/* 381 */       allListeners.addAll(this.applicationListeners);
/* 382 */       if (!this.applicationListenerBeans.isEmpty()) {
/* 383 */         BeanFactory beanFactory = AbstractApplicationEventMulticaster.this.getBeanFactory();
/* 384 */         for (String listenerBeanName : this.applicationListenerBeans) {
/*     */           try {
/* 386 */             ApplicationListener<?> listener = (ApplicationListener)beanFactory.getBean(listenerBeanName, ApplicationListener.class);
/* 387 */             if (this.preFiltered || !allListeners.contains(listener)) {
/* 388 */               allListeners.add(listener);
/*     */             }
/*     */           }
/* 391 */           catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 397 */       if (!this.preFiltered || !this.applicationListenerBeans.isEmpty()) {
/* 398 */         AnnotationAwareOrderComparator.sort(allListeners);
/*     */       }
/* 400 */       return allListeners;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/AbstractApplicationEventMulticaster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */