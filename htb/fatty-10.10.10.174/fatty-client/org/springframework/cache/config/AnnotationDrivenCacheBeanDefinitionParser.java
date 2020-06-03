/*     */ package org.springframework.cache.config;
/*     */ 
/*     */ import org.springframework.aop.config.AopNamespaceUtils;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
/*     */ import org.springframework.cache.interceptor.CacheInterceptor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ 
/*     */ 
/*     */ class AnnotationDrivenCacheBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String CACHE_ASPECT_CLASS_NAME = "org.springframework.cache.aspectj.AnnotationCacheAspect";
/*     */   private static final String JCACHE_ASPECT_CLASS_NAME = "org.springframework.cache.aspectj.JCacheCacheAspect";
/*     */   private static final boolean jsr107Present;
/*     */   private static final boolean jcacheImplPresent;
/*     */   
/*     */   static {
/*  69 */     ClassLoader classLoader = AnnotationDrivenCacheBeanDefinitionParser.class.getClassLoader();
/*  70 */     jsr107Present = ClassUtils.isPresent("javax.cache.Cache", classLoader);
/*  71 */     jcacheImplPresent = ClassUtils.isPresent("org.springframework.cache.jcache.interceptor.DefaultJCacheOperationSource", classLoader);
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
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*  84 */     String mode = element.getAttribute("mode");
/*  85 */     if ("aspectj".equals(mode)) {
/*     */       
/*  87 */       registerCacheAspect(element, parserContext);
/*     */     }
/*     */     else {
/*     */       
/*  91 */       registerCacheAdvisor(element, parserContext);
/*     */     } 
/*     */     
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   private void registerCacheAspect(Element element, ParserContext parserContext) {
/*  98 */     SpringCachingConfigurer.registerCacheAspect(element, parserContext);
/*  99 */     if (jsr107Present && jcacheImplPresent) {
/* 100 */       JCacheCachingConfigurer.registerCacheAspect(element, parserContext);
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerCacheAdvisor(Element element, ParserContext parserContext) {
/* 105 */     AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);
/* 106 */     SpringCachingConfigurer.registerCacheAdvisor(element, parserContext);
/* 107 */     if (jsr107Present && jcacheImplPresent) {
/* 108 */       JCacheCachingConfigurer.registerCacheAdvisor(element, parserContext);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void parseCacheResolution(Element element, BeanDefinition def, boolean setBoth) {
/* 118 */     String name = element.getAttribute("cache-resolver");
/* 119 */     boolean hasText = StringUtils.hasText(name);
/* 120 */     if (hasText) {
/* 121 */       def.getPropertyValues().add("cacheResolver", new RuntimeBeanReference(name.trim()));
/*     */     }
/* 123 */     if (!hasText || setBoth) {
/* 124 */       def.getPropertyValues().add("cacheManager", new RuntimeBeanReference(
/* 125 */             CacheNamespaceHandler.extractCacheManager(element)));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void parseErrorHandler(Element element, BeanDefinition def) {
/* 130 */     String name = element.getAttribute("error-handler");
/* 131 */     if (StringUtils.hasText(name)) {
/* 132 */       def.getPropertyValues().add("errorHandler", new RuntimeBeanReference(name.trim()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SpringCachingConfigurer
/*     */   {
/*     */     private static void registerCacheAdvisor(Element element, ParserContext parserContext) {
/* 143 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.cache.config.internalCacheAdvisor")) {
/* 144 */         Object eleSource = parserContext.extractSource(element);
/*     */ 
/*     */         
/* 147 */         RootBeanDefinition sourceDef = new RootBeanDefinition("org.springframework.cache.annotation.AnnotationCacheOperationSource");
/* 148 */         sourceDef.setSource(eleSource);
/* 149 */         sourceDef.setRole(2);
/* 150 */         String sourceName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)sourceDef);
/*     */ 
/*     */         
/* 153 */         RootBeanDefinition interceptorDef = new RootBeanDefinition(CacheInterceptor.class);
/* 154 */         interceptorDef.setSource(eleSource);
/* 155 */         interceptorDef.setRole(2);
/* 156 */         AnnotationDrivenCacheBeanDefinitionParser.parseCacheResolution(element, (BeanDefinition)interceptorDef, false);
/* 157 */         AnnotationDrivenCacheBeanDefinitionParser.parseErrorHandler(element, (BeanDefinition)interceptorDef);
/* 158 */         CacheNamespaceHandler.parseKeyGenerator(element, (BeanDefinition)interceptorDef);
/* 159 */         interceptorDef.getPropertyValues().add("cacheOperationSources", new RuntimeBeanReference(sourceName));
/* 160 */         String interceptorName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)interceptorDef);
/*     */ 
/*     */         
/* 163 */         RootBeanDefinition advisorDef = new RootBeanDefinition(BeanFactoryCacheOperationSourceAdvisor.class);
/* 164 */         advisorDef.setSource(eleSource);
/* 165 */         advisorDef.setRole(2);
/* 166 */         advisorDef.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
/* 167 */         advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
/* 168 */         if (element.hasAttribute("order")) {
/* 169 */           advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
/*     */         }
/* 171 */         parserContext.getRegistry().registerBeanDefinition("org.springframework.cache.config.internalCacheAdvisor", (BeanDefinition)advisorDef);
/*     */         
/* 173 */         CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), eleSource);
/* 174 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)sourceDef, sourceName));
/* 175 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)interceptorDef, interceptorName));
/* 176 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)advisorDef, "org.springframework.cache.config.internalCacheAdvisor"));
/* 177 */         parserContext.registerComponent((ComponentDefinition)compositeDef);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static void registerCacheAspect(Element element, ParserContext parserContext) {
/* 191 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.cache.config.internalCacheAspect")) {
/* 192 */         RootBeanDefinition def = new RootBeanDefinition();
/* 193 */         def.setBeanClassName("org.springframework.cache.aspectj.AnnotationCacheAspect");
/* 194 */         def.setFactoryMethodName("aspectOf");
/* 195 */         AnnotationDrivenCacheBeanDefinitionParser.parseCacheResolution(element, (BeanDefinition)def, false);
/* 196 */         CacheNamespaceHandler.parseKeyGenerator(element, (BeanDefinition)def);
/* 197 */         parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)def, "org.springframework.cache.config.internalCacheAspect"));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JCacheCachingConfigurer
/*     */   {
/*     */     private static void registerCacheAdvisor(Element element, ParserContext parserContext) {
/* 209 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.cache.config.internalJCacheAdvisor")) {
/* 210 */         Object source = parserContext.extractSource(element);
/*     */ 
/*     */         
/* 213 */         RootBeanDefinition rootBeanDefinition1 = createJCacheOperationSourceBeanDefinition(element, source);
/* 214 */         String sourceName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)rootBeanDefinition1);
/*     */ 
/*     */         
/* 217 */         RootBeanDefinition interceptorDef = new RootBeanDefinition("org.springframework.cache.jcache.interceptor.JCacheInterceptor");
/*     */         
/* 219 */         interceptorDef.setSource(source);
/* 220 */         interceptorDef.setRole(2);
/* 221 */         interceptorDef.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
/* 222 */         AnnotationDrivenCacheBeanDefinitionParser.parseErrorHandler(element, (BeanDefinition)interceptorDef);
/* 223 */         String interceptorName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)interceptorDef);
/*     */ 
/*     */         
/* 226 */         RootBeanDefinition advisorDef = new RootBeanDefinition("org.springframework.cache.jcache.interceptor.BeanFactoryJCacheOperationSourceAdvisor");
/*     */         
/* 228 */         advisorDef.setSource(source);
/* 229 */         advisorDef.setRole(2);
/* 230 */         advisorDef.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
/* 231 */         advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
/* 232 */         if (element.hasAttribute("order")) {
/* 233 */           advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
/*     */         }
/* 235 */         parserContext.getRegistry().registerBeanDefinition("org.springframework.cache.config.internalJCacheAdvisor", (BeanDefinition)advisorDef);
/*     */         
/* 237 */         CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), source);
/* 238 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)rootBeanDefinition1, sourceName));
/* 239 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)interceptorDef, interceptorName));
/* 240 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)advisorDef, "org.springframework.cache.config.internalJCacheAdvisor"));
/* 241 */         parserContext.registerComponent((ComponentDefinition)compositeDef);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void registerCacheAspect(Element element, ParserContext parserContext) {
/* 246 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.cache.config.internalJCacheAspect")) {
/* 247 */         Object eleSource = parserContext.extractSource(element);
/* 248 */         RootBeanDefinition def = new RootBeanDefinition();
/* 249 */         def.setBeanClassName("org.springframework.cache.aspectj.JCacheCacheAspect");
/* 250 */         def.setFactoryMethodName("aspectOf");
/* 251 */         RootBeanDefinition rootBeanDefinition1 = createJCacheOperationSourceBeanDefinition(element, eleSource);
/*     */         
/* 253 */         String sourceName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)rootBeanDefinition1);
/* 254 */         def.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
/*     */         
/* 256 */         parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)rootBeanDefinition1, sourceName));
/* 257 */         parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)def, "org.springframework.cache.config.internalJCacheAspect"));
/*     */       } 
/*     */     }
/*     */     
/*     */     private static RootBeanDefinition createJCacheOperationSourceBeanDefinition(Element element, @Nullable Object eleSource) {
/* 262 */       RootBeanDefinition sourceDef = new RootBeanDefinition("org.springframework.cache.jcache.interceptor.DefaultJCacheOperationSource");
/*     */       
/* 264 */       sourceDef.setSource(eleSource);
/* 265 */       sourceDef.setRole(2);
/*     */ 
/*     */       
/* 268 */       AnnotationDrivenCacheBeanDefinitionParser.parseCacheResolution(element, (BeanDefinition)sourceDef, true);
/* 269 */       CacheNamespaceHandler.parseKeyGenerator(element, (BeanDefinition)sourceDef);
/* 270 */       return sourceDef;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/config/AnnotationDrivenCacheBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */