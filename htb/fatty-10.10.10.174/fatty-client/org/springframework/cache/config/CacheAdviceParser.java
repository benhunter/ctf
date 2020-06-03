/*     */ package org.springframework.cache.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.TypedStringValue;
/*     */ import org.springframework.beans.factory.parsing.ReaderContext;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.ManagedMap;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.cache.interceptor.CacheEvictOperation;
/*     */ import org.springframework.cache.interceptor.CacheInterceptor;
/*     */ import org.springframework.cache.interceptor.CacheOperation;
/*     */ import org.springframework.cache.interceptor.CachePutOperation;
/*     */ import org.springframework.cache.interceptor.CacheableOperation;
/*     */ import org.springframework.cache.interceptor.NameMatchCacheOperationSource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.DomUtils;
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
/*     */ class CacheAdviceParser
/*     */   extends AbstractSingleBeanDefinitionParser
/*     */ {
/*     */   private static final String CACHEABLE_ELEMENT = "cacheable";
/*     */   private static final String CACHE_EVICT_ELEMENT = "cache-evict";
/*     */   private static final String CACHE_PUT_ELEMENT = "cache-put";
/*     */   private static final String METHOD_ATTRIBUTE = "method";
/*     */   private static final String DEFS_ELEMENT = "caching";
/*     */   
/*     */   protected Class<?> getBeanClass(Element element) {
/*  66 */     return CacheInterceptor.class;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/*  71 */     builder.addPropertyReference("cacheManager", CacheNamespaceHandler.extractCacheManager(element));
/*  72 */     CacheNamespaceHandler.parseKeyGenerator(element, (BeanDefinition)builder.getBeanDefinition());
/*     */     
/*  74 */     List<Element> cacheDefs = DomUtils.getChildElementsByTagName(element, "caching");
/*  75 */     if (!cacheDefs.isEmpty()) {
/*     */       
/*  77 */       List<RootBeanDefinition> attributeSourceDefinitions = parseDefinitionsSources(cacheDefs, parserContext);
/*  78 */       builder.addPropertyValue("cacheOperationSources", attributeSourceDefinitions);
/*     */     }
/*     */     else {
/*     */       
/*  82 */       builder.addPropertyValue("cacheOperationSources", new RootBeanDefinition("org.springframework.cache.annotation.AnnotationCacheOperationSource"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private List<RootBeanDefinition> parseDefinitionsSources(List<Element> definitions, ParserContext parserContext) {
/*  88 */     ManagedList<RootBeanDefinition> defs = new ManagedList(definitions.size());
/*     */ 
/*     */     
/*  91 */     for (Element element : definitions) {
/*  92 */       defs.add(parseDefinitionSource(element, parserContext));
/*     */     }
/*     */     
/*  95 */     return (List<RootBeanDefinition>)defs;
/*     */   }
/*     */   
/*     */   private RootBeanDefinition parseDefinitionSource(Element definition, ParserContext parserContext) {
/*  99 */     Props prop = new Props(definition);
/*     */ 
/*     */     
/* 102 */     ManagedMap<TypedStringValue, Collection<CacheOperation>> cacheOpMap = new ManagedMap();
/* 103 */     cacheOpMap.setSource(parserContext.extractSource(definition));
/*     */     
/* 105 */     List<Element> cacheableCacheMethods = DomUtils.getChildElementsByTagName(definition, "cacheable");
/*     */     
/* 107 */     for (Element opElement : cacheableCacheMethods) {
/* 108 */       String name = prop.merge(opElement, (ReaderContext)parserContext.getReaderContext());
/* 109 */       TypedStringValue nameHolder = new TypedStringValue(name);
/* 110 */       nameHolder.setSource(parserContext.extractSource(opElement));
/* 111 */       CacheableOperation.Builder builder = prop.<CacheableOperation.Builder>merge(opElement, (ReaderContext)parserContext
/* 112 */           .getReaderContext(), new CacheableOperation.Builder());
/* 113 */       builder.setUnless(getAttributeValue(opElement, "unless", ""));
/* 114 */       builder.setSync(Boolean.valueOf(getAttributeValue(opElement, "sync", "false")).booleanValue());
/*     */       
/* 116 */       Collection<CacheOperation> col = (Collection<CacheOperation>)cacheOpMap.get(nameHolder);
/* 117 */       if (col == null) {
/* 118 */         col = new ArrayList<>(2);
/* 119 */         cacheOpMap.put(nameHolder, col);
/*     */       } 
/* 121 */       col.add(builder.build());
/*     */     } 
/*     */     
/* 124 */     List<Element> evictCacheMethods = DomUtils.getChildElementsByTagName(definition, "cache-evict");
/*     */     
/* 126 */     for (Element opElement : evictCacheMethods) {
/* 127 */       String name = prop.merge(opElement, (ReaderContext)parserContext.getReaderContext());
/* 128 */       TypedStringValue nameHolder = new TypedStringValue(name);
/* 129 */       nameHolder.setSource(parserContext.extractSource(opElement));
/* 130 */       CacheEvictOperation.Builder builder = prop.<CacheEvictOperation.Builder>merge(opElement, (ReaderContext)parserContext
/* 131 */           .getReaderContext(), new CacheEvictOperation.Builder());
/*     */       
/* 133 */       String wide = opElement.getAttribute("all-entries");
/* 134 */       if (StringUtils.hasText(wide)) {
/* 135 */         builder.setCacheWide(Boolean.valueOf(wide.trim()).booleanValue());
/*     */       }
/*     */       
/* 138 */       String after = opElement.getAttribute("before-invocation");
/* 139 */       if (StringUtils.hasText(after)) {
/* 140 */         builder.setBeforeInvocation(Boolean.valueOf(after.trim()).booleanValue());
/*     */       }
/*     */       
/* 143 */       Collection<CacheOperation> col = (Collection<CacheOperation>)cacheOpMap.get(nameHolder);
/* 144 */       if (col == null) {
/* 145 */         col = new ArrayList<>(2);
/* 146 */         cacheOpMap.put(nameHolder, col);
/*     */       } 
/* 148 */       col.add(builder.build());
/*     */     } 
/*     */     
/* 151 */     List<Element> putCacheMethods = DomUtils.getChildElementsByTagName(definition, "cache-put");
/*     */     
/* 153 */     for (Element opElement : putCacheMethods) {
/* 154 */       String name = prop.merge(opElement, (ReaderContext)parserContext.getReaderContext());
/* 155 */       TypedStringValue nameHolder = new TypedStringValue(name);
/* 156 */       nameHolder.setSource(parserContext.extractSource(opElement));
/* 157 */       CachePutOperation.Builder builder = prop.<CachePutOperation.Builder>merge(opElement, (ReaderContext)parserContext
/* 158 */           .getReaderContext(), new CachePutOperation.Builder());
/* 159 */       builder.setUnless(getAttributeValue(opElement, "unless", ""));
/*     */       
/* 161 */       Collection<CacheOperation> col = (Collection<CacheOperation>)cacheOpMap.get(nameHolder);
/* 162 */       if (col == null) {
/* 163 */         col = new ArrayList<>(2);
/* 164 */         cacheOpMap.put(nameHolder, col);
/*     */       } 
/* 166 */       col.add(builder.build());
/*     */     } 
/*     */     
/* 169 */     RootBeanDefinition attributeSourceDefinition = new RootBeanDefinition(NameMatchCacheOperationSource.class);
/* 170 */     attributeSourceDefinition.setSource(parserContext.extractSource(definition));
/* 171 */     attributeSourceDefinition.getPropertyValues().add("nameMap", cacheOpMap);
/* 172 */     return attributeSourceDefinition;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getAttributeValue(Element element, String attributeName, String defaultValue) {
/* 177 */     String attribute = element.getAttribute(attributeName);
/* 178 */     if (StringUtils.hasText(attribute)) {
/* 179 */       return attribute.trim();
/*     */     }
/* 181 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Props
/*     */   {
/*     */     private String key;
/*     */ 
/*     */     
/*     */     private String keyGenerator;
/*     */     
/*     */     private String cacheManager;
/*     */     
/*     */     private String condition;
/*     */     
/*     */     private String method;
/*     */     
/*     */     @Nullable
/*     */     private String[] caches;
/*     */ 
/*     */     
/*     */     Props(Element root) {
/* 204 */       String defaultCache = root.getAttribute("cache");
/* 205 */       this.key = root.getAttribute("key");
/* 206 */       this.keyGenerator = root.getAttribute("key-generator");
/* 207 */       this.cacheManager = root.getAttribute("cache-manager");
/* 208 */       this.condition = root.getAttribute("condition");
/* 209 */       this.method = root.getAttribute("method");
/*     */       
/* 211 */       if (StringUtils.hasText(defaultCache)) {
/* 212 */         this.caches = StringUtils.commaDelimitedListToStringArray(defaultCache.trim());
/*     */       }
/*     */     }
/*     */     
/*     */     <T extends CacheOperation.Builder> T merge(Element element, ReaderContext readerCtx, T builder) {
/* 217 */       String cache = element.getAttribute("cache");
/*     */ 
/*     */       
/* 220 */       String[] localCaches = this.caches;
/* 221 */       if (StringUtils.hasText(cache)) {
/* 222 */         localCaches = StringUtils.commaDelimitedListToStringArray(cache.trim());
/*     */       }
/* 224 */       if (localCaches != null) {
/* 225 */         builder.setCacheNames(localCaches);
/*     */       } else {
/*     */         
/* 228 */         readerCtx.error("No cache specified for " + element.getNodeName(), element);
/*     */       } 
/*     */       
/* 231 */       builder.setKey(CacheAdviceParser.getAttributeValue(element, "key", this.key));
/* 232 */       builder.setKeyGenerator(CacheAdviceParser.getAttributeValue(element, "key-generator", this.keyGenerator));
/* 233 */       builder.setCacheManager(CacheAdviceParser.getAttributeValue(element, "cache-manager", this.cacheManager));
/* 234 */       builder.setCondition(CacheAdviceParser.getAttributeValue(element, "condition", this.condition));
/*     */       
/* 236 */       if (StringUtils.hasText(builder.getKey()) && StringUtils.hasText(builder.getKeyGenerator())) {
/* 237 */         throw new IllegalStateException("Invalid cache advice configuration on '" + element
/* 238 */             .toString() + "'. Both 'key' and 'keyGenerator' attributes have been set. These attributes are mutually exclusive: either set the SpEL expression used tocompute the key at runtime or set the name of the KeyGenerator bean to use.");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 243 */       return builder;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     String merge(Element element, ReaderContext readerCtx) {
/* 248 */       String method = element.getAttribute("method");
/* 249 */       if (StringUtils.hasText(method)) {
/* 250 */         return method.trim();
/*     */       }
/* 252 */       if (StringUtils.hasText(this.method)) {
/* 253 */         return this.method;
/*     */       }
/* 255 */       readerCtx.error("No method specified for " + element.getNodeName(), element);
/* 256 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/config/CacheAdviceParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */