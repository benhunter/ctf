/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
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
/*    */ public class MvcNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   public void init() {
/* 34 */     registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
/* 35 */     registerBeanDefinitionParser("default-servlet-handler", new DefaultServletHandlerBeanDefinitionParser());
/* 36 */     registerBeanDefinitionParser("interceptors", new InterceptorsBeanDefinitionParser());
/* 37 */     registerBeanDefinitionParser("resources", new ResourcesBeanDefinitionParser());
/* 38 */     registerBeanDefinitionParser("view-controller", new ViewControllerBeanDefinitionParser());
/* 39 */     registerBeanDefinitionParser("redirect-view-controller", new ViewControllerBeanDefinitionParser());
/* 40 */     registerBeanDefinitionParser("status-controller", new ViewControllerBeanDefinitionParser());
/* 41 */     registerBeanDefinitionParser("view-resolvers", new ViewResolversBeanDefinitionParser());
/* 42 */     registerBeanDefinitionParser("tiles-configurer", (BeanDefinitionParser)new TilesConfigurerBeanDefinitionParser());
/* 43 */     registerBeanDefinitionParser("freemarker-configurer", (BeanDefinitionParser)new FreeMarkerConfigurerBeanDefinitionParser());
/* 44 */     registerBeanDefinitionParser("groovy-configurer", (BeanDefinitionParser)new GroovyMarkupConfigurerBeanDefinitionParser());
/* 45 */     registerBeanDefinitionParser("script-template-configurer", (BeanDefinitionParser)new ScriptTemplateConfigurerBeanDefinitionParser());
/* 46 */     registerBeanDefinitionParser("cors", new CorsBeanDefinitionParser());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/MvcNamespaceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */