/*    */ package org.springframework.cache.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CacheNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   static final String CACHE_MANAGER_ATTRIBUTE = "cache-manager";
/*    */   static final String DEFAULT_CACHE_MANAGER_BEAN_NAME = "cacheManager";
/*    */   
/*    */   static String extractCacheManager(Element element) {
/* 44 */     return element.hasAttribute("cache-manager") ? element
/* 45 */       .getAttribute("cache-manager") : "cacheManager";
/*    */   }
/*    */ 
/*    */   
/*    */   static BeanDefinition parseKeyGenerator(Element element, BeanDefinition def) {
/* 50 */     String name = element.getAttribute("key-generator");
/* 51 */     if (StringUtils.hasText(name)) {
/* 52 */       def.getPropertyValues().add("keyGenerator", new RuntimeBeanReference(name.trim()));
/*    */     }
/* 54 */     return def;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void init() {
/* 60 */     registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenCacheBeanDefinitionParser());
/* 61 */     registerBeanDefinitionParser("advice", (BeanDefinitionParser)new CacheAdviceParser());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/config/CacheNamespaceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */