/*     */ package org.springframework.web.servlet.config;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.ManagedMap;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.cache.concurrent.ConcurrentMapCache;
/*     */ import org.springframework.http.CacheControl;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.DomUtils;
/*     */ import org.springframework.web.servlet.handler.MappedInterceptor;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.resource.CachingResourceResolver;
/*     */ import org.springframework.web.servlet.resource.CachingResourceTransformer;
/*     */ import org.springframework.web.servlet.resource.ContentVersionStrategy;
/*     */ import org.springframework.web.servlet.resource.CssLinkResourceTransformer;
/*     */ import org.springframework.web.servlet.resource.FixedVersionStrategy;
/*     */ import org.springframework.web.servlet.resource.PathResourceResolver;
/*     */ import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
/*     */ import org.springframework.web.servlet.resource.ResourceUrlProvider;
/*     */ import org.springframework.web.servlet.resource.ResourceUrlProviderExposingInterceptor;
/*     */ import org.springframework.web.servlet.resource.VersionResourceResolver;
/*     */ import org.springframework.web.servlet.resource.WebJarsResourceResolver;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ResourcesBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String RESOURCE_CHAIN_CACHE = "spring-resource-chain-cache";
/*     */   private static final String VERSION_RESOLVER_ELEMENT = "version-resolver";
/*     */   private static final String VERSION_STRATEGY_ELEMENT = "version-strategy";
/*     */   private static final String FIXED_VERSION_STRATEGY_ELEMENT = "fixed-version-strategy";
/*     */   private static final String CONTENT_VERSION_STRATEGY_ELEMENT = "content-version-strategy";
/*     */   private static final String RESOURCE_URL_PROVIDER = "mvcResourceUrlProvider";
/*  84 */   private static final boolean isWebJarsAssetLocatorPresent = ClassUtils.isPresent("org.webjars.WebJarAssetLocator", ResourcesBeanDefinitionParser.class
/*  85 */       .getClassLoader());
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext context) {
/*  90 */     Object source = context.extractSource(element);
/*     */     
/*  92 */     registerUrlProvider(context, source);
/*     */     
/*  94 */     RuntimeBeanReference pathMatcherRef = MvcNamespaceUtils.registerPathMatcher(null, context, source);
/*  95 */     RuntimeBeanReference pathHelperRef = MvcNamespaceUtils.registerUrlPathHelper(null, context, source);
/*     */     
/*  97 */     String resourceHandlerName = registerResourceHandler(context, element, pathHelperRef, source);
/*  98 */     if (resourceHandlerName == null) {
/*  99 */       return null;
/*     */     }
/*     */     
/* 102 */     ManagedMap<String, String> managedMap = new ManagedMap();
/* 103 */     String resourceRequestPath = element.getAttribute("mapping");
/* 104 */     if (!StringUtils.hasText(resourceRequestPath)) {
/* 105 */       context.getReaderContext().error("The 'mapping' attribute is required.", context.extractSource(element));
/* 106 */       return null;
/*     */     } 
/* 108 */     managedMap.put(resourceRequestPath, resourceHandlerName);
/*     */     
/* 110 */     RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
/* 111 */     handlerMappingDef.setSource(source);
/* 112 */     handlerMappingDef.setRole(2);
/* 113 */     handlerMappingDef.getPropertyValues().add("urlMap", managedMap);
/* 114 */     handlerMappingDef.getPropertyValues().add("pathMatcher", pathMatcherRef).add("urlPathHelper", pathHelperRef);
/*     */     
/* 116 */     String orderValue = element.getAttribute("order");
/*     */     
/* 118 */     Object order = StringUtils.hasText(orderValue) ? orderValue : Integer.valueOf(2147483646);
/* 119 */     handlerMappingDef.getPropertyValues().add("order", order);
/*     */     
/* 121 */     RuntimeBeanReference corsRef = MvcNamespaceUtils.registerCorsConfigurations(null, context, source);
/* 122 */     handlerMappingDef.getPropertyValues().add("corsConfigurations", corsRef);
/*     */     
/* 124 */     String beanName = context.getReaderContext().generateBeanName((BeanDefinition)handlerMappingDef);
/* 125 */     context.getRegistry().registerBeanDefinition(beanName, (BeanDefinition)handlerMappingDef);
/* 126 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)handlerMappingDef, beanName));
/*     */ 
/*     */ 
/*     */     
/* 130 */     MvcNamespaceUtils.registerDefaultComponents(context, source);
/*     */     
/* 132 */     return null;
/*     */   }
/*     */   
/*     */   private void registerUrlProvider(ParserContext context, @Nullable Object source) {
/* 136 */     if (!context.getRegistry().containsBeanDefinition("mvcResourceUrlProvider")) {
/* 137 */       RootBeanDefinition urlProvider = new RootBeanDefinition(ResourceUrlProvider.class);
/* 138 */       urlProvider.setSource(source);
/* 139 */       urlProvider.setRole(2);
/* 140 */       context.getRegistry().registerBeanDefinition("mvcResourceUrlProvider", (BeanDefinition)urlProvider);
/* 141 */       context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)urlProvider, "mvcResourceUrlProvider"));
/*     */       
/* 143 */       RootBeanDefinition interceptor = new RootBeanDefinition(ResourceUrlProviderExposingInterceptor.class);
/* 144 */       interceptor.setSource(source);
/* 145 */       interceptor.getConstructorArgumentValues().addIndexedArgumentValue(0, urlProvider);
/*     */       
/* 147 */       RootBeanDefinition mappedInterceptor = new RootBeanDefinition(MappedInterceptor.class);
/* 148 */       mappedInterceptor.setSource(source);
/* 149 */       mappedInterceptor.setRole(2);
/* 150 */       mappedInterceptor.getConstructorArgumentValues().addIndexedArgumentValue(0, null);
/* 151 */       mappedInterceptor.getConstructorArgumentValues().addIndexedArgumentValue(1, interceptor);
/* 152 */       String mappedInterceptorName = context.getReaderContext().registerWithGeneratedName((BeanDefinition)mappedInterceptor);
/* 153 */       context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)mappedInterceptor, mappedInterceptorName));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String registerResourceHandler(ParserContext context, Element element, RuntimeBeanReference pathHelperRef, @Nullable Object source) {
/* 161 */     String locationAttr = element.getAttribute("location");
/* 162 */     if (!StringUtils.hasText(locationAttr)) {
/* 163 */       context.getReaderContext().error("The 'location' attribute is required.", context.extractSource(element));
/* 164 */       return null;
/*     */     } 
/*     */     
/* 167 */     RootBeanDefinition resourceHandlerDef = new RootBeanDefinition(ResourceHttpRequestHandler.class);
/* 168 */     resourceHandlerDef.setSource(source);
/* 169 */     resourceHandlerDef.setRole(2);
/*     */     
/* 171 */     MutablePropertyValues values = resourceHandlerDef.getPropertyValues();
/* 172 */     values.add("urlPathHelper", pathHelperRef);
/* 173 */     values.add("locationValues", StringUtils.commaDelimitedListToStringArray(locationAttr));
/*     */     
/* 175 */     String cacheSeconds = element.getAttribute("cache-period");
/* 176 */     if (StringUtils.hasText(cacheSeconds)) {
/* 177 */       values.add("cacheSeconds", cacheSeconds);
/*     */     }
/*     */     
/* 180 */     Element cacheControlElement = DomUtils.getChildElementByTagName(element, "cache-control");
/* 181 */     if (cacheControlElement != null) {
/* 182 */       CacheControl cacheControl = parseCacheControl(cacheControlElement);
/* 183 */       values.add("cacheControl", cacheControl);
/*     */     } 
/*     */     
/* 186 */     Element resourceChainElement = DomUtils.getChildElementByTagName(element, "resource-chain");
/* 187 */     if (resourceChainElement != null) {
/* 188 */       parseResourceChain(resourceHandlerDef, context, resourceChainElement, source);
/*     */     }
/*     */     
/* 191 */     Object manager = MvcNamespaceUtils.getContentNegotiationManager(context);
/* 192 */     if (manager != null) {
/* 193 */       values.add("contentNegotiationManager", manager);
/*     */     }
/*     */     
/* 196 */     String beanName = context.getReaderContext().generateBeanName((BeanDefinition)resourceHandlerDef);
/* 197 */     context.getRegistry().registerBeanDefinition(beanName, (BeanDefinition)resourceHandlerDef);
/* 198 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)resourceHandlerDef, beanName));
/* 199 */     return beanName;
/*     */   }
/*     */ 
/*     */   
/*     */   private CacheControl parseCacheControl(Element element) {
/*     */     CacheControl cacheControl;
/* 205 */     if ("true".equals(element.getAttribute("no-cache"))) {
/* 206 */       cacheControl = CacheControl.noCache();
/*     */     }
/* 208 */     else if ("true".equals(element.getAttribute("no-store"))) {
/* 209 */       cacheControl = CacheControl.noStore();
/*     */     }
/* 211 */     else if (element.hasAttribute("max-age")) {
/* 212 */       cacheControl = CacheControl.maxAge(Long.parseLong(element.getAttribute("max-age")), TimeUnit.SECONDS);
/*     */     } else {
/*     */       
/* 215 */       cacheControl = CacheControl.empty();
/*     */     } 
/*     */     
/* 218 */     if ("true".equals(element.getAttribute("must-revalidate"))) {
/* 219 */       cacheControl = cacheControl.mustRevalidate();
/*     */     }
/* 221 */     if ("true".equals(element.getAttribute("no-transform"))) {
/* 222 */       cacheControl = cacheControl.noTransform();
/*     */     }
/* 224 */     if ("true".equals(element.getAttribute("cache-public"))) {
/* 225 */       cacheControl = cacheControl.cachePublic();
/*     */     }
/* 227 */     if ("true".equals(element.getAttribute("cache-private"))) {
/* 228 */       cacheControl = cacheControl.cachePrivate();
/*     */     }
/* 230 */     if ("true".equals(element.getAttribute("proxy-revalidate"))) {
/* 231 */       cacheControl = cacheControl.proxyRevalidate();
/*     */     }
/* 233 */     if (element.hasAttribute("s-maxage")) {
/* 234 */       cacheControl = cacheControl.sMaxAge(Long.parseLong(element.getAttribute("s-maxage")), TimeUnit.SECONDS);
/*     */     }
/* 236 */     if (element.hasAttribute("stale-while-revalidate")) {
/* 237 */       cacheControl = cacheControl.staleWhileRevalidate(
/* 238 */           Long.parseLong(element.getAttribute("stale-while-revalidate")), TimeUnit.SECONDS);
/*     */     }
/* 240 */     if (element.hasAttribute("stale-if-error")) {
/* 241 */       cacheControl = cacheControl.staleIfError(
/* 242 */           Long.parseLong(element.getAttribute("stale-if-error")), TimeUnit.SECONDS);
/*     */     }
/* 244 */     return cacheControl;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseResourceChain(RootBeanDefinition resourceHandlerDef, ParserContext context, Element element, @Nullable Object source) {
/* 250 */     String autoRegistration = element.getAttribute("auto-registration");
/* 251 */     boolean isAutoRegistration = (!StringUtils.hasText(autoRegistration) || !"false".equals(autoRegistration));
/*     */     
/* 253 */     ManagedList<Object> resourceResolvers = new ManagedList();
/* 254 */     resourceResolvers.setSource(source);
/* 255 */     ManagedList<Object> resourceTransformers = new ManagedList();
/* 256 */     resourceTransformers.setSource(source);
/*     */     
/* 258 */     parseResourceCache(resourceResolvers, resourceTransformers, element, source);
/* 259 */     parseResourceResolversTransformers(isAutoRegistration, resourceResolvers, resourceTransformers, context, element, source);
/*     */ 
/*     */     
/* 262 */     if (!resourceResolvers.isEmpty()) {
/* 263 */       resourceHandlerDef.getPropertyValues().add("resourceResolvers", resourceResolvers);
/*     */     }
/* 265 */     if (!resourceTransformers.isEmpty()) {
/* 266 */       resourceHandlerDef.getPropertyValues().add("resourceTransformers", resourceTransformers);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseResourceCache(ManagedList<Object> resourceResolvers, ManagedList<Object> resourceTransformers, Element element, @Nullable Object source) {
/* 273 */     String resourceCache = element.getAttribute("resource-cache");
/* 274 */     if ("true".equals(resourceCache)) {
/* 275 */       ConstructorArgumentValues cargs = new ConstructorArgumentValues();
/*     */       
/* 277 */       RootBeanDefinition cachingResolverDef = new RootBeanDefinition(CachingResourceResolver.class);
/* 278 */       cachingResolverDef.setSource(source);
/* 279 */       cachingResolverDef.setRole(2);
/* 280 */       cachingResolverDef.setConstructorArgumentValues(cargs);
/*     */       
/* 282 */       RootBeanDefinition cachingTransformerDef = new RootBeanDefinition(CachingResourceTransformer.class);
/* 283 */       cachingTransformerDef.setSource(source);
/* 284 */       cachingTransformerDef.setRole(2);
/* 285 */       cachingTransformerDef.setConstructorArgumentValues(cargs);
/*     */       
/* 287 */       String cacheManagerName = element.getAttribute("cache-manager");
/* 288 */       String cacheName = element.getAttribute("cache-name");
/* 289 */       if (StringUtils.hasText(cacheManagerName) && StringUtils.hasText(cacheName)) {
/* 290 */         RuntimeBeanReference cacheManagerRef = new RuntimeBeanReference(cacheManagerName);
/* 291 */         cargs.addIndexedArgumentValue(0, cacheManagerRef);
/* 292 */         cargs.addIndexedArgumentValue(1, cacheName);
/*     */       } else {
/*     */         
/* 295 */         ConstructorArgumentValues cacheCavs = new ConstructorArgumentValues();
/* 296 */         cacheCavs.addIndexedArgumentValue(0, "spring-resource-chain-cache");
/* 297 */         RootBeanDefinition cacheDef = new RootBeanDefinition(ConcurrentMapCache.class);
/* 298 */         cacheDef.setSource(source);
/* 299 */         cacheDef.setRole(2);
/* 300 */         cacheDef.setConstructorArgumentValues(cacheCavs);
/* 301 */         cargs.addIndexedArgumentValue(0, cacheDef);
/*     */       } 
/* 303 */       resourceResolvers.add(cachingResolverDef);
/* 304 */       resourceTransformers.add(cachingTransformerDef);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseResourceResolversTransformers(boolean isAutoRegistration, ManagedList<Object> resourceResolvers, ManagedList<Object> resourceTransformers, ParserContext context, Element element, @Nullable Object source) {
/* 312 */     Element resolversElement = DomUtils.getChildElementByTagName(element, "resolvers");
/* 313 */     if (resolversElement != null) {
/* 314 */       for (Element beanElement : DomUtils.getChildElements(resolversElement)) {
/* 315 */         if ("version-resolver".equals(beanElement.getLocalName())) {
/* 316 */           RootBeanDefinition versionResolverDef = parseVersionResolver(context, beanElement, source);
/* 317 */           versionResolverDef.setSource(source);
/* 318 */           resourceResolvers.add(versionResolverDef);
/* 319 */           if (isAutoRegistration) {
/* 320 */             RootBeanDefinition cssLinkTransformerDef = new RootBeanDefinition(CssLinkResourceTransformer.class);
/* 321 */             cssLinkTransformerDef.setSource(source);
/* 322 */             cssLinkTransformerDef.setRole(2);
/* 323 */             resourceTransformers.add(cssLinkTransformerDef);
/*     */           } 
/*     */           continue;
/*     */         } 
/* 327 */         Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
/* 328 */         resourceResolvers.add(object);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 333 */     if (isAutoRegistration) {
/* 334 */       if (isWebJarsAssetLocatorPresent) {
/* 335 */         RootBeanDefinition webJarsResolverDef = new RootBeanDefinition(WebJarsResourceResolver.class);
/* 336 */         webJarsResolverDef.setSource(source);
/* 337 */         webJarsResolverDef.setRole(2);
/* 338 */         resourceResolvers.add(webJarsResolverDef);
/*     */       } 
/* 340 */       RootBeanDefinition pathResolverDef = new RootBeanDefinition(PathResourceResolver.class);
/* 341 */       pathResolverDef.setSource(source);
/* 342 */       pathResolverDef.setRole(2);
/* 343 */       resourceResolvers.add(pathResolverDef);
/*     */     } 
/*     */     
/* 346 */     Element transformersElement = DomUtils.getChildElementByTagName(element, "transformers");
/* 347 */     if (transformersElement != null) {
/* 348 */       for (Element beanElement : DomUtils.getChildElementsByTagName(transformersElement, new String[] { "bean", "ref" })) {
/* 349 */         Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
/* 350 */         resourceTransformers.add(object);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private RootBeanDefinition parseVersionResolver(ParserContext context, Element element, @Nullable Object source) {
/* 356 */     ManagedMap<String, Object> strategyMap = new ManagedMap();
/* 357 */     strategyMap.setSource(source);
/* 358 */     RootBeanDefinition versionResolverDef = new RootBeanDefinition(VersionResourceResolver.class);
/* 359 */     versionResolverDef.setSource(source);
/* 360 */     versionResolverDef.setRole(2);
/* 361 */     versionResolverDef.getPropertyValues().addPropertyValue("strategyMap", strategyMap);
/*     */     
/* 363 */     for (Element beanElement : DomUtils.getChildElements(element)) {
/* 364 */       String[] patterns = StringUtils.commaDelimitedListToStringArray(beanElement.getAttribute("patterns"));
/* 365 */       Object strategy = null;
/* 366 */       if ("fixed-version-strategy".equals(beanElement.getLocalName())) {
/* 367 */         ConstructorArgumentValues cargs = new ConstructorArgumentValues();
/* 368 */         cargs.addIndexedArgumentValue(0, beanElement.getAttribute("version"));
/* 369 */         RootBeanDefinition strategyDef = new RootBeanDefinition(FixedVersionStrategy.class);
/* 370 */         strategyDef.setSource(source);
/* 371 */         strategyDef.setRole(2);
/* 372 */         strategyDef.setConstructorArgumentValues(cargs);
/* 373 */         strategy = strategyDef;
/*     */       }
/* 375 */       else if ("content-version-strategy".equals(beanElement.getLocalName())) {
/* 376 */         RootBeanDefinition strategyDef = new RootBeanDefinition(ContentVersionStrategy.class);
/* 377 */         strategyDef.setSource(source);
/* 378 */         strategyDef.setRole(2);
/* 379 */         strategy = strategyDef;
/*     */       }
/* 381 */       else if ("version-strategy".equals(beanElement.getLocalName())) {
/* 382 */         Element childElement = DomUtils.getChildElementsByTagName(beanElement, new String[] { "bean", "ref" }).get(0);
/* 383 */         strategy = context.getDelegate().parsePropertySubElement(childElement, null);
/*     */       } 
/* 385 */       for (String pattern : patterns) {
/* 386 */         strategyMap.put(pattern, strategy);
/*     */       }
/*     */     } 
/*     */     
/* 390 */     return versionResolverDef;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/ResourcesBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */