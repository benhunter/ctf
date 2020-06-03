/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*    */ import org.springframework.beans.factory.support.ManagedMap;
/*    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*    */ import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
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
/*    */ 
/*    */ 
/*    */ class DefaultServletHandlerBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   @Nullable
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 50 */     Object source = parserContext.extractSource(element);
/*    */     
/* 52 */     String defaultServletName = element.getAttribute("default-servlet-name");
/* 53 */     RootBeanDefinition defaultServletHandlerDef = new RootBeanDefinition(DefaultServletHttpRequestHandler.class);
/* 54 */     defaultServletHandlerDef.setSource(source);
/* 55 */     defaultServletHandlerDef.setRole(2);
/* 56 */     if (StringUtils.hasText(defaultServletName)) {
/* 57 */       defaultServletHandlerDef.getPropertyValues().add("defaultServletName", defaultServletName);
/*    */     }
/* 59 */     String defaultServletHandlerName = parserContext.getReaderContext().generateBeanName((BeanDefinition)defaultServletHandlerDef);
/* 60 */     parserContext.getRegistry().registerBeanDefinition(defaultServletHandlerName, (BeanDefinition)defaultServletHandlerDef);
/* 61 */     parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)defaultServletHandlerDef, defaultServletHandlerName));
/*    */     
/* 63 */     ManagedMap<String, String> managedMap = new ManagedMap();
/* 64 */     managedMap.put("/**", defaultServletHandlerName);
/*    */     
/* 66 */     RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
/* 67 */     handlerMappingDef.setSource(source);
/* 68 */     handlerMappingDef.setRole(2);
/* 69 */     handlerMappingDef.getPropertyValues().add("urlMap", managedMap);
/*    */     
/* 71 */     String handlerMappingBeanName = parserContext.getReaderContext().generateBeanName((BeanDefinition)handlerMappingDef);
/* 72 */     parserContext.getRegistry().registerBeanDefinition(handlerMappingBeanName, (BeanDefinition)handlerMappingDef);
/* 73 */     parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)handlerMappingDef, handlerMappingBeanName));
/*    */ 
/*    */     
/* 76 */     MvcNamespaceUtils.registerDefaultComponents(parserContext, source);
/*    */     
/* 78 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/DefaultServletHandlerBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */