/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.cors.CorsUtils;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHandlerMethodMapping<T>
/*     */   extends AbstractHandlerMapping
/*     */   implements InitializingBean
/*     */ {
/*     */   private static final String SCOPED_TARGET_NAME_PREFIX = "scopedTarget.";
/*  79 */   private static final HandlerMethod PREFLIGHT_AMBIGUOUS_MATCH = new HandlerMethod(new EmptyHandler(), 
/*  80 */       ClassUtils.getMethod(EmptyHandler.class, "handle", new Class[0]));
/*     */   
/*  82 */   private static final CorsConfiguration ALLOW_CORS_CONFIG = new CorsConfiguration();
/*     */   
/*     */   static {
/*  85 */     ALLOW_CORS_CONFIG.addAllowedOrigin("*");
/*  86 */     ALLOW_CORS_CONFIG.addAllowedMethod("*");
/*  87 */     ALLOW_CORS_CONFIG.addAllowedHeader("*");
/*  88 */     ALLOW_CORS_CONFIG.setAllowCredentials(Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean detectHandlerMethodsInAncestorContexts = false;
/*     */   
/*     */   @Nullable
/*     */   private HandlerMethodMappingNamingStrategy<T> namingStrategy;
/*     */   
/*  97 */   private final MappingRegistry mappingRegistry = new MappingRegistry();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDetectHandlerMethodsInAncestorContexts(boolean detectHandlerMethodsInAncestorContexts) {
/* 110 */     this.detectHandlerMethodsInAncestorContexts = detectHandlerMethodsInAncestorContexts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerMethodMappingNamingStrategy(HandlerMethodMappingNamingStrategy<T> namingStrategy) {
/* 121 */     this.namingStrategy = namingStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HandlerMethodMappingNamingStrategy<T> getNamingStrategy() {
/* 129 */     return this.namingStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<T, HandlerMethod> getHandlerMethods() {
/* 136 */     this.mappingRegistry.acquireReadLock();
/*     */     try {
/* 138 */       return Collections.unmodifiableMap(this.mappingRegistry.getMappings());
/*     */     } finally {
/*     */       
/* 141 */       this.mappingRegistry.releaseReadLock();
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
/*     */   @Nullable
/*     */   public List<HandlerMethod> getHandlerMethodsForMappingName(String mappingName) {
/* 154 */     return this.mappingRegistry.getHandlerMethodsByMappingName(mappingName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MappingRegistry getMappingRegistry() {
/* 161 */     return this.mappingRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerMapping(T mapping, Object handler, Method method) {
/* 172 */     if (this.logger.isTraceEnabled()) {
/* 173 */       this.logger.trace("Register \"" + mapping + "\" to " + method.toGenericString());
/*     */     }
/* 175 */     this.mappingRegistry.register(mapping, handler, method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregisterMapping(T mapping) {
/* 184 */     if (this.logger.isTraceEnabled()) {
/* 185 */       this.logger.trace("Unregister mapping \"" + mapping + "\"");
/*     */     }
/* 187 */     this.mappingRegistry.unregister(mapping);
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
/*     */   public void afterPropertiesSet() {
/* 199 */     initHandlerMethods();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initHandlerMethods() {
/* 209 */     for (String beanName : getCandidateBeanNames()) {
/* 210 */       if (!beanName.startsWith("scopedTarget.")) {
/* 211 */         processCandidateBean(beanName);
/*     */       }
/*     */     } 
/* 214 */     handlerMethodsInitialized(getHandlerMethods());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getCandidateBeanNames() {
/* 224 */     return this.detectHandlerMethodsInAncestorContexts ? 
/* 225 */       BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)obtainApplicationContext(), Object.class) : 
/* 226 */       obtainApplicationContext().getBeanNamesForType(Object.class);
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
/*     */   protected void processCandidateBean(String beanName) {
/* 241 */     Class<?> beanType = null;
/*     */     try {
/* 243 */       beanType = obtainApplicationContext().getType(beanName);
/*     */     }
/* 245 */     catch (Throwable ex) {
/*     */       
/* 247 */       if (this.logger.isTraceEnabled()) {
/* 248 */         this.logger.trace("Could not resolve type for bean '" + beanName + "'", ex);
/*     */       }
/*     */     } 
/* 251 */     if (beanType != null && isHandler(beanType)) {
/* 252 */       detectHandlerMethods(beanName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void detectHandlerMethods(Object handler) {
/* 263 */     Class<?> handlerType = (handler instanceof String) ? obtainApplicationContext().getType((String)handler) : handler.getClass();
/*     */     
/* 265 */     if (handlerType != null) {
/* 266 */       Class<?> userType = ClassUtils.getUserClass(handlerType);
/* 267 */       Map<Method, T> methods = MethodIntrospector.selectMethods(userType, method -> {
/*     */ 
/*     */             
/*     */             try {
/*     */               return (MethodIntrospector.MetadataLookup)getMappingForMethod(method, userType);
/* 272 */             } catch (Throwable ex) {
/*     */               throw new IllegalStateException("Invalid mapping on handler class [" + userType.getName() + "]: " + method, ex);
/*     */             } 
/*     */           });
/*     */       
/* 277 */       if (this.logger.isTraceEnabled()) {
/* 278 */         this.logger.trace(formatMappings(userType, methods));
/*     */       }
/* 280 */       methods.forEach((method, mapping) -> {
/*     */             Method invocableMethod = AopUtils.selectInvocableMethod(method, userType);
/*     */             registerHandlerMethod(handler, invocableMethod, (T)mapping);
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String formatMappings(Class<?> userType, Map<Method, T> methods) {
/* 290 */     String formattedType = (String)Arrays.<String>stream(ClassUtils.getPackageName(userType).split("\\.")).map(p -> p.substring(0, 1)).collect(Collectors.joining(".", "", ".")) + userType.getSimpleName();
/* 291 */     Function<Method, String> methodFormatter = method -> (String)Arrays.<Class<?>>stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(",", "(", ")"));
/*     */ 
/*     */     
/* 294 */     return methods.entrySet().stream()
/* 295 */       .map(e -> {
/*     */           Method method = (Method)e.getKey();
/*     */           
/*     */           return (new StringBuilder()).append(e.getValue()).append(": ").append(method.getName()).append(methodFormatter.apply(method)).toString();
/* 299 */         }).collect(Collectors.joining("\n\t", "\n\t" + formattedType + ":\n\t", ""));
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
/*     */   protected void registerHandlerMethod(Object handler, Method method, T mapping) {
/* 312 */     this.mappingRegistry.register(mapping, handler, method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HandlerMethod createHandlerMethod(Object handler, Method method) {
/*     */     HandlerMethod handlerMethod;
/* 323 */     if (handler instanceof String) {
/* 324 */       String beanName = (String)handler;
/*     */       
/* 326 */       handlerMethod = new HandlerMethod(beanName, (BeanFactory)obtainApplicationContext().getAutowireCapableBeanFactory(), method);
/*     */     } else {
/*     */       
/* 329 */       handlerMethod = new HandlerMethod(handler, method);
/*     */     } 
/* 331 */     return handlerMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected CorsConfiguration initCorsConfiguration(Object handler, Method method, T mapping) {
/* 339 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handlerMethodsInitialized(Map<T, HandlerMethod> handlerMethods) {
/* 348 */     int total = handlerMethods.size();
/* 349 */     if ((this.logger.isTraceEnabled() && total == 0) || (this.logger.isDebugEnabled() && total > 0)) {
/* 350 */       this.logger.debug(total + " mappings in " + formatMappingName());
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
/*     */   protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
/* 362 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 363 */     this.mappingRegistry.acquireReadLock();
/*     */     try {
/* 365 */       HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
/* 366 */       return (handlerMethod != null) ? handlerMethod.createWithResolvedBean() : null;
/*     */     } finally {
/*     */       
/* 369 */       this.mappingRegistry.releaseReadLock();
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
/*     */   @Nullable
/*     */   protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
/* 384 */     List<Match> matches = new ArrayList<>();
/* 385 */     List<T> directPathMatches = this.mappingRegistry.getMappingsByUrl(lookupPath);
/* 386 */     if (directPathMatches != null) {
/* 387 */       addMatchingMappings(directPathMatches, matches, request);
/*     */     }
/* 389 */     if (matches.isEmpty())
/*     */     {
/* 391 */       addMatchingMappings(this.mappingRegistry.getMappings().keySet(), matches, request);
/*     */     }
/*     */     
/* 394 */     if (!matches.isEmpty()) {
/* 395 */       Comparator<Match> comparator = new MatchComparator(getMappingComparator(request));
/* 396 */       matches.sort(comparator);
/* 397 */       Match bestMatch = matches.get(0);
/* 398 */       if (matches.size() > 1) {
/* 399 */         if (this.logger.isTraceEnabled()) {
/* 400 */           this.logger.trace(matches.size() + " matching mappings: " + matches);
/*     */         }
/* 402 */         if (CorsUtils.isPreFlightRequest(request)) {
/* 403 */           return PREFLIGHT_AMBIGUOUS_MATCH;
/*     */         }
/* 405 */         Match secondBestMatch = matches.get(1);
/* 406 */         if (comparator.compare(bestMatch, secondBestMatch) == 0) {
/* 407 */           Method m1 = bestMatch.handlerMethod.getMethod();
/* 408 */           Method m2 = secondBestMatch.handlerMethod.getMethod();
/* 409 */           String uri = request.getRequestURI();
/* 410 */           throw new IllegalStateException("Ambiguous handler methods mapped for '" + uri + "': {" + m1 + ", " + m2 + "}");
/*     */         } 
/*     */       } 
/*     */       
/* 414 */       request.setAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE, bestMatch.handlerMethod);
/* 415 */       handleMatch(bestMatch.mapping, lookupPath, request);
/* 416 */       return bestMatch.handlerMethod;
/*     */     } 
/*     */     
/* 419 */     return handleNoMatch(this.mappingRegistry.getMappings().keySet(), lookupPath, request);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addMatchingMappings(Collection<T> mappings, List<Match> matches, HttpServletRequest request) {
/* 424 */     for (T mapping : mappings) {
/* 425 */       T match = getMatchingMapping(mapping, request);
/* 426 */       if (match != null) {
/* 427 */         matches.add(new Match(match, this.mappingRegistry.getMappings().get(mapping)));
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
/*     */   protected void handleMatch(T mapping, String lookupPath, HttpServletRequest request) {
/* 439 */     request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, lookupPath);
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
/*     */   @Nullable
/*     */   protected HandlerMethod handleNoMatch(Set<T> mappings, String lookupPath, HttpServletRequest request) throws Exception {
/* 453 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected CorsConfiguration getCorsConfiguration(Object handler, HttpServletRequest request) {
/* 458 */     CorsConfiguration corsConfig = super.getCorsConfiguration(handler, request);
/* 459 */     if (handler instanceof HandlerMethod) {
/* 460 */       HandlerMethod handlerMethod = (HandlerMethod)handler;
/* 461 */       if (handlerMethod.equals(PREFLIGHT_AMBIGUOUS_MATCH)) {
/* 462 */         return ALLOW_CORS_CONFIG;
/*     */       }
/*     */       
/* 465 */       CorsConfiguration corsConfigFromMethod = this.mappingRegistry.getCorsConfiguration(handlerMethod);
/* 466 */       corsConfig = (corsConfig != null) ? corsConfig.combine(corsConfigFromMethod) : corsConfigFromMethod;
/*     */     } 
/*     */     
/* 469 */     return corsConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isHandler(Class<?> paramClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract T getMappingForMethod(Method paramMethod, Class<?> paramClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Set<String> getMappingPathPatterns(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract T getMatchingMapping(T paramT, HttpServletRequest paramHttpServletRequest);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Comparator<T> getMappingComparator(HttpServletRequest paramHttpServletRequest);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class MappingRegistry
/*     */   {
/* 524 */     private final Map<T, AbstractHandlerMethodMapping.MappingRegistration<T>> registry = new HashMap<>();
/*     */     
/* 526 */     private final Map<T, HandlerMethod> mappingLookup = new LinkedHashMap<>();
/*     */     
/* 528 */     private final MultiValueMap<String, T> urlLookup = (MultiValueMap<String, T>)new LinkedMultiValueMap();
/*     */     
/* 530 */     private final Map<String, List<HandlerMethod>> nameLookup = new ConcurrentHashMap<>();
/*     */     
/* 532 */     private final Map<HandlerMethod, CorsConfiguration> corsLookup = new ConcurrentHashMap<>();
/*     */     
/* 534 */     private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<T, HandlerMethod> getMappings() {
/* 541 */       return this.mappingLookup;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public List<T> getMappingsByUrl(String urlPath) {
/* 550 */       return (List<T>)this.urlLookup.get(urlPath);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<HandlerMethod> getHandlerMethodsByMappingName(String mappingName) {
/* 557 */       return this.nameLookup.get(mappingName);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CorsConfiguration getCorsConfiguration(HandlerMethod handlerMethod) {
/* 564 */       HandlerMethod original = handlerMethod.getResolvedFromHandlerMethod();
/* 565 */       return this.corsLookup.get((original != null) ? original : handlerMethod);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void acquireReadLock() {
/* 572 */       this.readWriteLock.readLock().lock();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void releaseReadLock() {
/* 579 */       this.readWriteLock.readLock().unlock();
/*     */     }
/*     */     
/*     */     public void register(T mapping, Object handler, Method method) {
/* 583 */       this.readWriteLock.writeLock().lock();
/*     */       try {
/* 585 */         HandlerMethod handlerMethod = AbstractHandlerMethodMapping.this.createHandlerMethod(handler, method);
/* 586 */         assertUniqueMethodMapping(handlerMethod, mapping);
/* 587 */         this.mappingLookup.put(mapping, handlerMethod);
/*     */         
/* 589 */         List<String> directUrls = getDirectUrls(mapping);
/* 590 */         for (String url : directUrls) {
/* 591 */           this.urlLookup.add(url, mapping);
/*     */         }
/*     */         
/* 594 */         String name = null;
/* 595 */         if (AbstractHandlerMethodMapping.this.getNamingStrategy() != null) {
/* 596 */           name = AbstractHandlerMethodMapping.this.getNamingStrategy().getName(handlerMethod, mapping);
/* 597 */           addMappingName(name, handlerMethod);
/*     */         } 
/*     */         
/* 600 */         CorsConfiguration corsConfig = AbstractHandlerMethodMapping.this.initCorsConfiguration(handler, method, mapping);
/* 601 */         if (corsConfig != null) {
/* 602 */           this.corsLookup.put(handlerMethod, corsConfig);
/*     */         }
/*     */         
/* 605 */         this.registry.put(mapping, new AbstractHandlerMethodMapping.MappingRegistration<>(mapping, handlerMethod, directUrls, name));
/*     */       } finally {
/*     */         
/* 608 */         this.readWriteLock.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void assertUniqueMethodMapping(HandlerMethod newHandlerMethod, T mapping) {
/* 613 */       HandlerMethod handlerMethod = this.mappingLookup.get(mapping);
/* 614 */       if (handlerMethod != null && !handlerMethod.equals(newHandlerMethod)) {
/* 615 */         throw new IllegalStateException("Ambiguous mapping. Cannot map '" + newHandlerMethod
/* 616 */             .getBean() + "' method \n" + newHandlerMethod + "\nto " + mapping + ": There is already '" + handlerMethod
/*     */             
/* 618 */             .getBean() + "' bean method\n" + handlerMethod + " mapped.");
/*     */       }
/*     */     }
/*     */     
/*     */     private List<String> getDirectUrls(T mapping) {
/* 623 */       List<String> urls = new ArrayList<>(1);
/* 624 */       for (String path : AbstractHandlerMethodMapping.this.getMappingPathPatterns(mapping)) {
/* 625 */         if (!AbstractHandlerMethodMapping.this.getPathMatcher().isPattern(path)) {
/* 626 */           urls.add(path);
/*     */         }
/*     */       } 
/* 629 */       return urls;
/*     */     }
/*     */     
/*     */     private void addMappingName(String name, HandlerMethod handlerMethod) {
/* 633 */       List<HandlerMethod> oldList = this.nameLookup.get(name);
/* 634 */       if (oldList == null) {
/* 635 */         oldList = Collections.emptyList();
/*     */       }
/*     */       
/* 638 */       for (HandlerMethod current : oldList) {
/* 639 */         if (handlerMethod.equals(current)) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */       
/* 644 */       List<HandlerMethod> newList = new ArrayList<>(oldList.size() + 1);
/* 645 */       newList.addAll(oldList);
/* 646 */       newList.add(handlerMethod);
/* 647 */       this.nameLookup.put(name, newList);
/*     */     }
/*     */     
/*     */     public void unregister(T mapping) {
/* 651 */       this.readWriteLock.writeLock().lock();
/*     */       try {
/* 653 */         AbstractHandlerMethodMapping.MappingRegistration<T> definition = this.registry.remove(mapping);
/* 654 */         if (definition == null) {
/*     */           return;
/*     */         }
/*     */         
/* 658 */         this.mappingLookup.remove(definition.getMapping());
/*     */         
/* 660 */         for (String url : definition.getDirectUrls()) {
/* 661 */           List<T> list = (List<T>)this.urlLookup.get(url);
/* 662 */           if (list != null) {
/* 663 */             list.remove(definition.getMapping());
/* 664 */             if (list.isEmpty()) {
/* 665 */               this.urlLookup.remove(url);
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 670 */         removeMappingName(definition);
/*     */         
/* 672 */         this.corsLookup.remove(definition.getHandlerMethod());
/*     */       } finally {
/*     */         
/* 675 */         this.readWriteLock.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void removeMappingName(AbstractHandlerMethodMapping.MappingRegistration<T> definition) {
/* 680 */       String name = definition.getMappingName();
/* 681 */       if (name == null) {
/*     */         return;
/*     */       }
/* 684 */       HandlerMethod handlerMethod = definition.getHandlerMethod();
/* 685 */       List<HandlerMethod> oldList = this.nameLookup.get(name);
/* 686 */       if (oldList == null) {
/*     */         return;
/*     */       }
/* 689 */       if (oldList.size() <= 1) {
/* 690 */         this.nameLookup.remove(name);
/*     */         return;
/*     */       } 
/* 693 */       List<HandlerMethod> newList = new ArrayList<>(oldList.size() - 1);
/* 694 */       for (HandlerMethod current : oldList) {
/* 695 */         if (!current.equals(handlerMethod)) {
/* 696 */           newList.add(current);
/*     */         }
/*     */       } 
/* 699 */       this.nameLookup.put(name, newList);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MappingRegistration<T>
/*     */   {
/*     */     private final T mapping;
/*     */     
/*     */     private final HandlerMethod handlerMethod;
/*     */     
/*     */     private final List<String> directUrls;
/*     */     
/*     */     @Nullable
/*     */     private final String mappingName;
/*     */ 
/*     */     
/*     */     public MappingRegistration(T mapping, HandlerMethod handlerMethod, @Nullable List<String> directUrls, @Nullable String mappingName) {
/* 718 */       Assert.notNull(mapping, "Mapping must not be null");
/* 719 */       Assert.notNull(handlerMethod, "HandlerMethod must not be null");
/* 720 */       this.mapping = mapping;
/* 721 */       this.handlerMethod = handlerMethod;
/* 722 */       this.directUrls = (directUrls != null) ? directUrls : Collections.<String>emptyList();
/* 723 */       this.mappingName = mappingName;
/*     */     }
/*     */     
/*     */     public T getMapping() {
/* 727 */       return this.mapping;
/*     */     }
/*     */     
/*     */     public HandlerMethod getHandlerMethod() {
/* 731 */       return this.handlerMethod;
/*     */     }
/*     */     
/*     */     public List<String> getDirectUrls() {
/* 735 */       return this.directUrls;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public String getMappingName() {
/* 740 */       return this.mappingName;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Match
/*     */   {
/*     */     private final T mapping;
/*     */ 
/*     */     
/*     */     private final HandlerMethod handlerMethod;
/*     */ 
/*     */ 
/*     */     
/*     */     public Match(T mapping, HandlerMethod handlerMethod) {
/* 756 */       this.mapping = mapping;
/* 757 */       this.handlerMethod = handlerMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 762 */       return this.mapping.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private class MatchComparator
/*     */     implements Comparator<Match>
/*     */   {
/*     */     private final Comparator<T> comparator;
/*     */     
/*     */     public MatchComparator(Comparator<T> comparator) {
/* 772 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compare(AbstractHandlerMethodMapping<T>.Match match1, AbstractHandlerMethodMapping<T>.Match match2) {
/* 777 */       return this.comparator.compare(match1.mapping, match2.mapping);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EmptyHandler
/*     */   {
/*     */     private EmptyHandler() {}
/*     */     
/*     */     public void handle() {
/* 786 */       throw new UnsupportedOperationException("Not implemented");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/AbstractHandlerMethodMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */