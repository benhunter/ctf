/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.aop.scope.ScopedProxyUtils;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
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
/*    */ class ScopedProxyBeanDefinitionDecorator
/*    */   implements BeanDefinitionDecorator
/*    */ {
/*    */   private static final String PROXY_TARGET_CLASS = "proxy-target-class";
/*    */   
/*    */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
/* 44 */     boolean proxyTargetClass = true;
/* 45 */     if (node instanceof Element) {
/* 46 */       Element ele = (Element)node;
/* 47 */       if (ele.hasAttribute("proxy-target-class")) {
/* 48 */         proxyTargetClass = Boolean.valueOf(ele.getAttribute("proxy-target-class")).booleanValue();
/*    */       }
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     BeanDefinitionHolder holder = ScopedProxyUtils.createScopedProxy(definition, parserContext.getRegistry(), proxyTargetClass);
/* 56 */     String targetBeanName = ScopedProxyUtils.getTargetBeanName(definition.getBeanName());
/* 57 */     parserContext.getReaderContext().fireComponentRegistered((ComponentDefinition)new BeanComponentDefinition(definition
/* 58 */           .getBeanDefinition(), targetBeanName));
/* 59 */     return holder;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/ScopedProxyBeanDefinitionDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */