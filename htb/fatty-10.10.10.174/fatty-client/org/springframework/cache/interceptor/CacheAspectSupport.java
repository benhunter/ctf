/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.CacheManager;
/*     */ import org.springframework.context.expression.AnnotatedElementKey;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.function.SingletonSupplier;
/*     */ import org.springframework.util.function.SupplierUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CacheAspectSupport
/*     */   extends AbstractCacheInvoker
/*     */   implements BeanFactoryAware, InitializingBean, SmartInitializingSingleton
/*     */ {
/*  86 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  88 */   private final Map<CacheOperationCacheKey, CacheOperationMetadata> metadataCache = new ConcurrentHashMap<>(1024);
/*     */   
/*  90 */   private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();
/*     */   
/*     */   @Nullable
/*     */   private CacheOperationSource cacheOperationSource;
/*     */   
/*  95 */   private SingletonSupplier<KeyGenerator> keyGenerator = SingletonSupplier.of(SimpleKeyGenerator::new);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private SingletonSupplier<CacheResolver> cacheResolver;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean initialized = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(@Nullable Supplier<CacheErrorHandler> errorHandler, @Nullable Supplier<KeyGenerator> keyGenerator, @Nullable Supplier<CacheResolver> cacheResolver, @Nullable Supplier<CacheManager> cacheManager) {
/* 115 */     this.errorHandler = new SingletonSupplier(errorHandler, SimpleCacheErrorHandler::new);
/* 116 */     this.keyGenerator = new SingletonSupplier(keyGenerator, SimpleKeyGenerator::new);
/* 117 */     this.cacheResolver = new SingletonSupplier(cacheResolver, () -> SimpleCacheResolver.of((CacheManager)SupplierUtils.resolve(cacheManager)));
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
/*     */   public void setCacheOperationSources(CacheOperationSource... cacheOperationSources) {
/* 129 */     Assert.notEmpty((Object[])cacheOperationSources, "At least 1 CacheOperationSource needs to be specified");
/* 130 */     this.cacheOperationSource = (cacheOperationSources.length > 1) ? new CompositeCacheOperationSource(cacheOperationSources) : cacheOperationSources[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheOperationSource(@Nullable CacheOperationSource cacheOperationSource) {
/* 140 */     this.cacheOperationSource = cacheOperationSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CacheOperationSource getCacheOperationSource() {
/* 148 */     return this.cacheOperationSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyGenerator(KeyGenerator keyGenerator) {
/* 157 */     this.keyGenerator = SingletonSupplier.of(keyGenerator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyGenerator getKeyGenerator() {
/* 164 */     return (KeyGenerator)this.keyGenerator.obtain();
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
/*     */   public void setCacheResolver(@Nullable CacheResolver cacheResolver) {
/* 176 */     this.cacheResolver = SingletonSupplier.ofNullable(cacheResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CacheResolver getCacheResolver() {
/* 184 */     return (CacheResolver)SupplierUtils.resolve((Supplier)this.cacheResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheManager(CacheManager cacheManager) {
/* 194 */     this.cacheResolver = SingletonSupplier.of(new SimpleCacheResolver(cacheManager));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 204 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 210 */     Assert.state((getCacheOperationSource() != null), "The 'cacheOperationSources' property is required: If there are no cacheable methods, then don't use a cache aspect.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterSingletonsInstantiated() {
/* 216 */     if (getCacheResolver() == null) {
/*     */       
/* 218 */       Assert.state((this.beanFactory != null), "CacheResolver or BeanFactory must be set on cache aspect");
/*     */       try {
/* 220 */         setCacheManager((CacheManager)this.beanFactory.getBean(CacheManager.class));
/*     */       }
/* 222 */       catch (NoUniqueBeanDefinitionException ex) {
/* 223 */         throw new IllegalStateException("No CacheResolver specified, and no unique bean of type CacheManager found. Mark one as primary or declare a specific CacheManager to use.");
/*     */       
/*     */       }
/* 226 */       catch (NoSuchBeanDefinitionException ex) {
/* 227 */         throw new IllegalStateException("No CacheResolver specified, and no bean of type CacheManager found. Register a CacheManager bean or remove the @EnableCaching annotation from your configuration.");
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     this.initialized = true;
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
/*     */   protected String methodIdentification(Method method, Class<?> targetClass) {
/* 245 */     Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/* 246 */     return ClassUtils.getQualifiedMethodName(specificMethod);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<? extends Cache> getCaches(CacheOperationInvocationContext<CacheOperation> context, CacheResolver cacheResolver) {
/* 252 */     Collection<? extends Cache> caches = cacheResolver.resolveCaches(context);
/* 253 */     if (caches.isEmpty()) {
/* 254 */       throw new IllegalStateException("No cache could be resolved for '" + context
/* 255 */           .getOperation() + "' using resolver '" + cacheResolver + "'. At least one cache should be provided per cache operation.");
/*     */     }
/*     */     
/* 258 */     return caches;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected CacheOperationContext getOperationContext(CacheOperation operation, Method method, Object[] args, Object target, Class<?> targetClass) {
/* 264 */     CacheOperationMetadata metadata = getCacheOperationMetadata(operation, method, targetClass);
/* 265 */     return new CacheOperationContext(metadata, args, target);
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
/*     */   protected CacheOperationMetadata getCacheOperationMetadata(CacheOperation operation, Method method, Class<?> targetClass) {
/* 280 */     CacheOperationCacheKey cacheKey = new CacheOperationCacheKey(operation, method, targetClass);
/* 281 */     CacheOperationMetadata metadata = this.metadataCache.get(cacheKey);
/* 282 */     if (metadata == null) {
/*     */       KeyGenerator operationKeyGenerator; CacheResolver operationCacheResolver;
/* 284 */       if (StringUtils.hasText(operation.getKeyGenerator())) {
/* 285 */         operationKeyGenerator = getBean(operation.getKeyGenerator(), KeyGenerator.class);
/*     */       } else {
/*     */         
/* 288 */         operationKeyGenerator = getKeyGenerator();
/*     */       } 
/*     */       
/* 291 */       if (StringUtils.hasText(operation.getCacheResolver())) {
/* 292 */         operationCacheResolver = getBean(operation.getCacheResolver(), CacheResolver.class);
/*     */       }
/* 294 */       else if (StringUtils.hasText(operation.getCacheManager())) {
/* 295 */         CacheManager cacheManager = getBean(operation.getCacheManager(), CacheManager.class);
/* 296 */         operationCacheResolver = new SimpleCacheResolver(cacheManager);
/*     */       } else {
/*     */         
/* 299 */         operationCacheResolver = getCacheResolver();
/* 300 */         Assert.state((operationCacheResolver != null), "No CacheResolver/CacheManager set");
/*     */       } 
/* 302 */       metadata = new CacheOperationMetadata(operation, method, targetClass, operationKeyGenerator, operationCacheResolver);
/*     */       
/* 304 */       this.metadataCache.put(cacheKey, metadata);
/*     */     } 
/* 306 */     return metadata;
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
/*     */   protected <T> T getBean(String beanName, Class<T> expectedType) {
/* 321 */     if (this.beanFactory == null) {
/* 322 */       throw new IllegalStateException("BeanFactory must be set on cache aspect for " + expectedType
/* 323 */           .getSimpleName() + " retrieval");
/*     */     }
/* 325 */     return (T)BeanFactoryAnnotationUtils.qualifiedBeanOfType(this.beanFactory, expectedType, beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clearMetadataCache() {
/* 332 */     this.metadataCache.clear();
/* 333 */     this.evaluator.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object execute(CacheOperationInvoker invoker, Object target, Method method, Object[] args) {
/* 339 */     if (this.initialized) {
/* 340 */       Class<?> targetClass = getTargetClass(target);
/* 341 */       CacheOperationSource cacheOperationSource = getCacheOperationSource();
/* 342 */       if (cacheOperationSource != null) {
/* 343 */         Collection<CacheOperation> operations = cacheOperationSource.getCacheOperations(method, targetClass);
/* 344 */         if (!CollectionUtils.isEmpty(operations)) {
/* 345 */           return execute(invoker, method, new CacheOperationContexts(operations, method, args, target, targetClass));
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 351 */     return invoker.invoke();
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
/*     */   protected Object invokeOperation(CacheOperationInvoker invoker) {
/* 365 */     return invoker.invoke();
/*     */   }
/*     */   
/*     */   private Class<?> getTargetClass(Object target) {
/* 369 */     return AopProxyUtils.ultimateTargetClass(target);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object execute(CacheOperationInvoker invoker, Method method, CacheOperationContexts contexts) {
/*     */     Object cacheValue, returnValue;
/* 375 */     if (contexts.isSynchronized()) {
/* 376 */       CacheOperationContext context = contexts.get((Class)CacheableOperation.class).iterator().next();
/* 377 */       if (isConditionPassing(context, CacheOperationExpressionEvaluator.NO_RESULT)) {
/* 378 */         Object key = generateKey(context, CacheOperationExpressionEvaluator.NO_RESULT);
/* 379 */         Cache cache = context.getCaches().iterator().next();
/*     */         try {
/* 381 */           return wrapCacheValue(method, cache.get(key, () -> unwrapReturnValue(invokeOperation(invoker))));
/*     */         }
/* 383 */         catch (org.springframework.cache.Cache.ValueRetrievalException ex) {
/*     */ 
/*     */           
/* 386 */           throw (CacheOperationInvoker.ThrowableWrapper)ex.getCause();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 391 */       return invokeOperation(invoker);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 397 */     processCacheEvicts(contexts.get((Class)CacheEvictOperation.class), true, CacheOperationExpressionEvaluator.NO_RESULT);
/*     */ 
/*     */ 
/*     */     
/* 401 */     Cache.ValueWrapper cacheHit = findCachedItem(contexts.get((Class)CacheableOperation.class));
/*     */ 
/*     */     
/* 404 */     List<CachePutRequest> cachePutRequests = new LinkedList<>();
/* 405 */     if (cacheHit == null) {
/* 406 */       collectPutRequests(contexts.get((Class)CacheableOperation.class), CacheOperationExpressionEvaluator.NO_RESULT, cachePutRequests);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 413 */     if (cacheHit != null && !hasCachePut(contexts)) {
/*     */       
/* 415 */       cacheValue = cacheHit.get();
/* 416 */       returnValue = wrapCacheValue(method, cacheValue);
/*     */     }
/*     */     else {
/*     */       
/* 420 */       returnValue = invokeOperation(invoker);
/* 421 */       cacheValue = unwrapReturnValue(returnValue);
/*     */     } 
/*     */ 
/*     */     
/* 425 */     collectPutRequests(contexts.get((Class)CachePutOperation.class), cacheValue, cachePutRequests);
/*     */ 
/*     */     
/* 428 */     for (CachePutRequest cachePutRequest : cachePutRequests) {
/* 429 */       cachePutRequest.apply(cacheValue);
/*     */     }
/*     */ 
/*     */     
/* 433 */     processCacheEvicts(contexts.get((Class)CacheEvictOperation.class), false, cacheValue);
/*     */     
/* 435 */     return returnValue;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object wrapCacheValue(Method method, @Nullable Object cacheValue) {
/* 440 */     if (method.getReturnType() == Optional.class && (cacheValue == null || cacheValue
/* 441 */       .getClass() != Optional.class)) {
/* 442 */       return Optional.ofNullable(cacheValue);
/*     */     }
/* 444 */     return cacheValue;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object unwrapReturnValue(Object returnValue) {
/* 449 */     return ObjectUtils.unwrapOptional(returnValue);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasCachePut(CacheOperationContexts contexts) {
/* 454 */     Collection<CacheOperationContext> cachePutContexts = contexts.get((Class)CachePutOperation.class);
/* 455 */     Collection<CacheOperationContext> excluded = new ArrayList<>();
/* 456 */     for (CacheOperationContext context : cachePutContexts) {
/*     */       try {
/* 458 */         if (!context.isConditionPassing(CacheOperationExpressionEvaluator.RESULT_UNAVAILABLE)) {
/* 459 */           excluded.add(context);
/*     */         }
/*     */       }
/* 462 */       catch (VariableNotAvailableException variableNotAvailableException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 467 */     return (cachePutContexts.size() != excluded.size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processCacheEvicts(Collection<CacheOperationContext> contexts, boolean beforeInvocation, @Nullable Object result) {
/* 473 */     for (CacheOperationContext context : contexts) {
/* 474 */       CacheEvictOperation operation = (CacheEvictOperation)context.metadata.operation;
/* 475 */       if (beforeInvocation == operation.isBeforeInvocation() && isConditionPassing(context, result)) {
/* 476 */         performCacheEvict(context, operation, result);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void performCacheEvict(CacheOperationContext context, CacheEvictOperation operation, @Nullable Object result) {
/* 484 */     Object key = null;
/* 485 */     for (Cache cache : context.getCaches()) {
/* 486 */       if (operation.isCacheWide()) {
/* 487 */         logInvalidating(context, operation, (Object)null);
/* 488 */         doClear(cache);
/*     */         continue;
/*     */       } 
/* 491 */       if (key == null) {
/* 492 */         key = generateKey(context, result);
/*     */       }
/* 494 */       logInvalidating(context, operation, key);
/* 495 */       doEvict(cache, key);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void logInvalidating(CacheOperationContext context, CacheEvictOperation operation, @Nullable Object key) {
/* 501 */     if (this.logger.isTraceEnabled()) {
/* 502 */       this.logger.trace("Invalidating " + ((key != null) ? ("cache key [" + key + "]") : "entire cache") + " for operation " + operation + " on method " + 
/* 503 */           context.metadata.method);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Cache.ValueWrapper findCachedItem(Collection<CacheOperationContext> contexts) {
/* 515 */     Object result = CacheOperationExpressionEvaluator.NO_RESULT;
/* 516 */     for (CacheOperationContext context : contexts) {
/* 517 */       if (isConditionPassing(context, result)) {
/* 518 */         Object key = generateKey(context, result);
/* 519 */         Cache.ValueWrapper cached = findInCaches(context, key);
/* 520 */         if (cached != null) {
/* 521 */           return cached;
/*     */         }
/*     */         
/* 524 */         if (this.logger.isTraceEnabled()) {
/* 525 */           this.logger.trace("No cache entry for key '" + key + "' in cache(s) " + context.getCacheNames());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 530 */     return null;
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
/*     */   private void collectPutRequests(Collection<CacheOperationContext> contexts, @Nullable Object result, Collection<CachePutRequest> putRequests) {
/* 543 */     for (CacheOperationContext context : contexts) {
/* 544 */       if (isConditionPassing(context, result)) {
/* 545 */         Object key = generateKey(context, result);
/* 546 */         putRequests.add(new CachePutRequest(context, key));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Cache.ValueWrapper findInCaches(CacheOperationContext context, Object key) {
/* 553 */     for (Cache cache : context.getCaches()) {
/* 554 */       Cache.ValueWrapper wrapper = doGet(cache, key);
/* 555 */       if (wrapper != null) {
/* 556 */         if (this.logger.isTraceEnabled()) {
/* 557 */           this.logger.trace("Cache entry for key '" + key + "' found in cache '" + cache.getName() + "'");
/*     */         }
/* 559 */         return wrapper;
/*     */       } 
/*     */     } 
/* 562 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isConditionPassing(CacheOperationContext context, @Nullable Object result) {
/* 566 */     boolean passing = context.isConditionPassing(result);
/* 567 */     if (!passing && this.logger.isTraceEnabled()) {
/* 568 */       this.logger.trace("Cache condition failed on method " + context.metadata.method + " for operation " + 
/* 569 */           context.metadata.operation);
/*     */     }
/* 571 */     return passing;
/*     */   }
/*     */   
/*     */   private Object generateKey(CacheOperationContext context, @Nullable Object result) {
/* 575 */     Object key = context.generateKey(result);
/* 576 */     if (key == null) {
/* 577 */       throw new IllegalArgumentException("Null key returned for cache operation (maybe you are using named params on classes without debug info?) " + 
/* 578 */           context.metadata.operation);
/*     */     }
/* 580 */     if (this.logger.isTraceEnabled()) {
/* 581 */       this.logger.trace("Computed cache key '" + key + "' for operation " + context.metadata.operation);
/*     */     }
/* 583 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class CacheOperationContexts
/*     */   {
/*     */     private final MultiValueMap<Class<? extends CacheOperation>, CacheAspectSupport.CacheOperationContext> contexts;
/*     */     
/*     */     private final boolean sync;
/*     */ 
/*     */     
/*     */     public CacheOperationContexts(Collection<? extends CacheOperation> operations, Method method, Object[] args, Object target, Class<?> targetClass) {
/* 596 */       this.contexts = (MultiValueMap<Class<? extends CacheOperation>, CacheAspectSupport.CacheOperationContext>)new LinkedMultiValueMap(operations.size());
/* 597 */       for (CacheOperation op : operations) {
/* 598 */         this.contexts.add(op.getClass(), CacheAspectSupport.this.getOperationContext(op, method, args, target, targetClass));
/*     */       }
/* 600 */       this.sync = determineSyncFlag(method);
/*     */     }
/*     */     
/*     */     public Collection<CacheAspectSupport.CacheOperationContext> get(Class<? extends CacheOperation> operationClass) {
/* 604 */       Collection<CacheAspectSupport.CacheOperationContext> result = (Collection<CacheAspectSupport.CacheOperationContext>)this.contexts.get(operationClass);
/* 605 */       return (result != null) ? result : Collections.<CacheAspectSupport.CacheOperationContext>emptyList();
/*     */     }
/*     */     
/*     */     public boolean isSynchronized() {
/* 609 */       return this.sync;
/*     */     }
/*     */     
/*     */     private boolean determineSyncFlag(Method method) {
/* 613 */       List<CacheAspectSupport.CacheOperationContext> cacheOperationContexts = (List<CacheAspectSupport.CacheOperationContext>)this.contexts.get(CacheableOperation.class);
/* 614 */       if (cacheOperationContexts == null) {
/* 615 */         return false;
/*     */       }
/* 617 */       boolean syncEnabled = false;
/* 618 */       for (CacheAspectSupport.CacheOperationContext cacheOperationContext : cacheOperationContexts) {
/* 619 */         if (((CacheableOperation)cacheOperationContext.getOperation()).isSync()) {
/* 620 */           syncEnabled = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 624 */       if (syncEnabled) {
/* 625 */         if (this.contexts.size() > 1) {
/* 626 */           throw new IllegalStateException("@Cacheable(sync=true) cannot be combined with other cache operations on '" + method + "'");
/*     */         }
/*     */         
/* 629 */         if (cacheOperationContexts.size() > 1) {
/* 630 */           throw new IllegalStateException("Only one @Cacheable(sync=true) entry is allowed on '" + method + "'");
/*     */         }
/*     */         
/* 633 */         CacheAspectSupport.CacheOperationContext cacheOperationContext = cacheOperationContexts.iterator().next();
/* 634 */         CacheableOperation operation = (CacheableOperation)cacheOperationContext.getOperation();
/* 635 */         if (cacheOperationContext.getCaches().size() > 1) {
/* 636 */           throw new IllegalStateException("@Cacheable(sync=true) only allows a single cache on '" + operation + "'");
/*     */         }
/*     */         
/* 639 */         if (StringUtils.hasText(operation.getUnless())) {
/* 640 */           throw new IllegalStateException("@Cacheable(sync=true) does not support unless attribute on '" + operation + "'");
/*     */         }
/*     */         
/* 643 */         return true;
/*     */       } 
/* 645 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class CacheOperationMetadata
/*     */   {
/*     */     private final CacheOperation operation;
/*     */ 
/*     */     
/*     */     private final Method method;
/*     */ 
/*     */     
/*     */     private final Class<?> targetClass;
/*     */ 
/*     */     
/*     */     private final Method targetMethod;
/*     */ 
/*     */     
/*     */     private final AnnotatedElementKey methodKey;
/*     */     
/*     */     private final KeyGenerator keyGenerator;
/*     */     
/*     */     private final CacheResolver cacheResolver;
/*     */ 
/*     */     
/*     */     public CacheOperationMetadata(CacheOperation operation, Method method, Class<?> targetClass, KeyGenerator keyGenerator, CacheResolver cacheResolver) {
/* 673 */       this.operation = operation;
/* 674 */       this.method = BridgeMethodResolver.findBridgedMethod(method);
/* 675 */       this.targetClass = targetClass;
/* 676 */       this
/* 677 */         .targetMethod = !Proxy.isProxyClass(targetClass) ? AopUtils.getMostSpecificMethod(method, targetClass) : this.method;
/* 678 */       this.methodKey = new AnnotatedElementKey(this.targetMethod, targetClass);
/* 679 */       this.keyGenerator = keyGenerator;
/* 680 */       this.cacheResolver = cacheResolver;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class CacheOperationContext
/*     */     implements CacheOperationInvocationContext<CacheOperation>
/*     */   {
/*     */     private final CacheAspectSupport.CacheOperationMetadata metadata;
/*     */     
/*     */     private final Object[] args;
/*     */     
/*     */     private final Object target;
/*     */     
/*     */     private final Collection<? extends Cache> caches;
/*     */     
/*     */     private final Collection<String> cacheNames;
/*     */     
/*     */     @Nullable
/*     */     private Boolean conditionPassing;
/*     */ 
/*     */     
/*     */     public CacheOperationContext(CacheAspectSupport.CacheOperationMetadata metadata, Object[] args, Object target) {
/* 704 */       this.metadata = metadata;
/* 705 */       this.args = extractArgs(metadata.method, args);
/* 706 */       this.target = target;
/* 707 */       this.caches = CacheAspectSupport.this.getCaches(this, metadata.cacheResolver);
/* 708 */       this.cacheNames = createCacheNames(this.caches);
/*     */     }
/*     */ 
/*     */     
/*     */     public CacheOperation getOperation() {
/* 713 */       return this.metadata.operation;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getTarget() {
/* 718 */       return this.target;
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getMethod() {
/* 723 */       return this.metadata.method;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getArgs() {
/* 728 */       return this.args;
/*     */     }
/*     */     
/*     */     private Object[] extractArgs(Method method, Object[] args) {
/* 732 */       if (!method.isVarArgs()) {
/* 733 */         return args;
/*     */       }
/* 735 */       Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
/* 736 */       Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
/* 737 */       System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
/* 738 */       System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
/* 739 */       return combinedArgs;
/*     */     }
/*     */     
/*     */     protected boolean isConditionPassing(@Nullable Object result) {
/* 743 */       if (this.conditionPassing == null) {
/* 744 */         if (StringUtils.hasText(this.metadata.operation.getCondition())) {
/* 745 */           EvaluationContext evaluationContext = createEvaluationContext(result);
/* 746 */           this.conditionPassing = Boolean.valueOf(CacheAspectSupport.this.evaluator.condition(this.metadata.operation.getCondition(), this.metadata
/* 747 */                 .methodKey, evaluationContext));
/*     */         } else {
/*     */           
/* 750 */           this.conditionPassing = Boolean.valueOf(true);
/*     */         } 
/*     */       }
/* 753 */       return this.conditionPassing.booleanValue();
/*     */     }
/*     */     
/*     */     protected boolean canPutToCache(@Nullable Object value) {
/* 757 */       String unless = "";
/* 758 */       if (this.metadata.operation instanceof CacheableOperation) {
/* 759 */         unless = ((CacheableOperation)this.metadata.operation).getUnless();
/*     */       }
/* 761 */       else if (this.metadata.operation instanceof CachePutOperation) {
/* 762 */         unless = ((CachePutOperation)this.metadata.operation).getUnless();
/*     */       } 
/* 764 */       if (StringUtils.hasText(unless)) {
/* 765 */         EvaluationContext evaluationContext = createEvaluationContext(value);
/* 766 */         return !CacheAspectSupport.this.evaluator.unless(unless, this.metadata.methodKey, evaluationContext);
/*     */       } 
/* 768 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     protected Object generateKey(@Nullable Object result) {
/* 776 */       if (StringUtils.hasText(this.metadata.operation.getKey())) {
/* 777 */         EvaluationContext evaluationContext = createEvaluationContext(result);
/* 778 */         return CacheAspectSupport.this.evaluator.key(this.metadata.operation.getKey(), this.metadata.methodKey, evaluationContext);
/*     */       } 
/* 780 */       return this.metadata.keyGenerator.generate(this.target, this.metadata.method, this.args);
/*     */     }
/*     */     
/*     */     private EvaluationContext createEvaluationContext(@Nullable Object result) {
/* 784 */       return CacheAspectSupport.this.evaluator.createEvaluationContext(this.caches, this.metadata.method, this.args, this.target, this.metadata
/* 785 */           .targetClass, this.metadata.targetMethod, result, CacheAspectSupport.this.beanFactory);
/*     */     }
/*     */     
/*     */     protected Collection<? extends Cache> getCaches() {
/* 789 */       return this.caches;
/*     */     }
/*     */     
/*     */     protected Collection<String> getCacheNames() {
/* 793 */       return this.cacheNames;
/*     */     }
/*     */     
/*     */     private Collection<String> createCacheNames(Collection<? extends Cache> caches) {
/* 797 */       Collection<String> names = new ArrayList<>();
/* 798 */       for (Cache cache : caches) {
/* 799 */         names.add(cache.getName());
/*     */       }
/* 801 */       return names;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class CachePutRequest
/*     */   {
/*     */     private final CacheAspectSupport.CacheOperationContext context;
/*     */     
/*     */     private final Object key;
/*     */     
/*     */     public CachePutRequest(CacheAspectSupport.CacheOperationContext context, Object key) {
/* 813 */       this.context = context;
/* 814 */       this.key = key;
/*     */     }
/*     */     
/*     */     public void apply(@Nullable Object result) {
/* 818 */       if (this.context.canPutToCache(result)) {
/* 819 */         for (Cache cache : this.context.getCaches()) {
/* 820 */           CacheAspectSupport.this.doPut(cache, this.key, result);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class CacheOperationCacheKey
/*     */     implements Comparable<CacheOperationCacheKey>
/*     */   {
/*     */     private final CacheOperation cacheOperation;
/*     */     private final AnnotatedElementKey methodCacheKey;
/*     */     
/*     */     private CacheOperationCacheKey(CacheOperation cacheOperation, Method method, Class<?> targetClass) {
/* 834 */       this.cacheOperation = cacheOperation;
/* 835 */       this.methodCacheKey = new AnnotatedElementKey(method, targetClass);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 840 */       if (this == other) {
/* 841 */         return true;
/*     */       }
/* 843 */       if (!(other instanceof CacheOperationCacheKey)) {
/* 844 */         return false;
/*     */       }
/* 846 */       CacheOperationCacheKey otherKey = (CacheOperationCacheKey)other;
/* 847 */       return (this.cacheOperation.equals(otherKey.cacheOperation) && this.methodCacheKey
/* 848 */         .equals(otherKey.methodCacheKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 853 */       return this.cacheOperation.hashCode() * 31 + this.methodCacheKey.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 858 */       return this.cacheOperation + " on " + this.methodCacheKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(CacheOperationCacheKey other) {
/* 863 */       int result = this.cacheOperation.getName().compareTo(other.cacheOperation.getName());
/* 864 */       if (result == 0) {
/* 865 */         result = this.methodCacheKey.compareTo(other.methodCacheKey);
/*     */       }
/* 867 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheAspectSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */