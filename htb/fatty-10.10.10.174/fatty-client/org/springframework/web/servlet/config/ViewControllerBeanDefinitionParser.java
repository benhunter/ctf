/*     */ package org.springframework.web.servlet.config;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.support.ManagedMap;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.mvc.ParameterizableViewController;
/*     */ import org.springframework.web.servlet.view.RedirectView;
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
/*     */ class ViewControllerBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String HANDLER_MAPPING_BEAN_NAME = "org.springframework.web.servlet.config.viewControllerHandlerMapping";
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*     */     ManagedMap<String, RootBeanDefinition> managedMap;
/*  65 */     Object source = parserContext.extractSource(element);
/*     */ 
/*     */     
/*  68 */     BeanDefinition hm = registerHandlerMapping(parserContext, source);
/*     */ 
/*     */     
/*  71 */     MvcNamespaceUtils.registerDefaultComponents(parserContext, source);
/*     */ 
/*     */     
/*  74 */     RootBeanDefinition controller = new RootBeanDefinition(ParameterizableViewController.class);
/*  75 */     controller.setSource(source);
/*     */     
/*  77 */     HttpStatus statusCode = null;
/*  78 */     if (element.hasAttribute("status-code")) {
/*  79 */       int statusValue = Integer.parseInt(element.getAttribute("status-code"));
/*  80 */       statusCode = HttpStatus.valueOf(statusValue);
/*     */     } 
/*     */     
/*  83 */     String name = element.getLocalName();
/*  84 */     if (name.equals("view-controller")) {
/*  85 */       if (element.hasAttribute("view-name")) {
/*  86 */         controller.getPropertyValues().add("viewName", element.getAttribute("view-name"));
/*     */       }
/*  88 */       if (statusCode != null) {
/*  89 */         controller.getPropertyValues().add("statusCode", statusCode);
/*     */       }
/*     */     }
/*  92 */     else if (name.equals("redirect-view-controller")) {
/*  93 */       controller.getPropertyValues().add("view", getRedirectView(element, statusCode, source));
/*     */     }
/*  95 */     else if (name.equals("status-controller")) {
/*  96 */       controller.getPropertyValues().add("statusCode", statusCode);
/*  97 */       controller.getPropertyValues().add("statusOnly", Boolean.valueOf(true));
/*     */     }
/*     */     else {
/*     */       
/* 101 */       throw new IllegalStateException("Unexpected tag name: " + name);
/*     */     } 
/*     */     
/* 104 */     Map<String, BeanDefinition> urlMap = (Map<String, BeanDefinition>)hm.getPropertyValues().get("urlMap");
/* 105 */     if (urlMap == null) {
/* 106 */       managedMap = new ManagedMap();
/* 107 */       hm.getPropertyValues().add("urlMap", managedMap);
/*     */     } 
/* 109 */     managedMap.put(element.getAttribute("path"), controller);
/*     */     
/* 111 */     return null;
/*     */   }
/*     */   
/*     */   private BeanDefinition registerHandlerMapping(ParserContext context, @Nullable Object source) {
/* 115 */     if (context.getRegistry().containsBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerMapping")) {
/* 116 */       return context.getRegistry().getBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerMapping");
/*     */     }
/* 118 */     RootBeanDefinition beanDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
/* 119 */     beanDef.setRole(2);
/* 120 */     context.getRegistry().registerBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerMapping", (BeanDefinition)beanDef);
/* 121 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)beanDef, "org.springframework.web.servlet.config.viewControllerHandlerMapping"));
/*     */     
/* 123 */     beanDef.setSource(source);
/* 124 */     beanDef.getPropertyValues().add("order", "1");
/* 125 */     beanDef.getPropertyValues().add("pathMatcher", MvcNamespaceUtils.registerPathMatcher(null, context, source));
/* 126 */     beanDef.getPropertyValues().add("urlPathHelper", MvcNamespaceUtils.registerUrlPathHelper(null, context, source));
/* 127 */     RuntimeBeanReference corsConfigurationsRef = MvcNamespaceUtils.registerCorsConfigurations(null, context, source);
/* 128 */     beanDef.getPropertyValues().add("corsConfigurations", corsConfigurationsRef);
/*     */     
/* 130 */     return (BeanDefinition)beanDef;
/*     */   }
/*     */   
/*     */   private RootBeanDefinition getRedirectView(Element element, @Nullable HttpStatus status, @Nullable Object source) {
/* 134 */     RootBeanDefinition redirectView = new RootBeanDefinition(RedirectView.class);
/* 135 */     redirectView.setSource(source);
/* 136 */     redirectView.getConstructorArgumentValues().addIndexedArgumentValue(0, element.getAttribute("redirect-url"));
/*     */     
/* 138 */     if (status != null) {
/* 139 */       redirectView.getPropertyValues().add("statusCode", status);
/*     */     }
/*     */     
/* 142 */     if (element.hasAttribute("context-relative")) {
/* 143 */       redirectView.getPropertyValues().add("contextRelative", element.getAttribute("context-relative"));
/*     */     } else {
/*     */       
/* 146 */       redirectView.getPropertyValues().add("contextRelative", Boolean.valueOf(true));
/*     */     } 
/*     */     
/* 149 */     if (element.hasAttribute("keep-query-params")) {
/* 150 */       redirectView.getPropertyValues().add("propagateQueryParams", element.getAttribute("keep-query-params"));
/*     */     }
/*     */     
/* 153 */     return redirectView;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/ViewControllerBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */