/*    */ package org.springframework.context.config;
/*    */ 
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*    */ import org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser;
/*    */ import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
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
/*    */ public class ContextNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   public void init() {
/* 35 */     registerBeanDefinitionParser("property-placeholder", (BeanDefinitionParser)new PropertyPlaceholderBeanDefinitionParser());
/* 36 */     registerBeanDefinitionParser("property-override", (BeanDefinitionParser)new PropertyOverrideBeanDefinitionParser());
/* 37 */     registerBeanDefinitionParser("annotation-config", (BeanDefinitionParser)new AnnotationConfigBeanDefinitionParser());
/* 38 */     registerBeanDefinitionParser("component-scan", (BeanDefinitionParser)new ComponentScanBeanDefinitionParser());
/* 39 */     registerBeanDefinitionParser("load-time-weaver", (BeanDefinitionParser)new LoadTimeWeaverBeanDefinitionParser());
/* 40 */     registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
/* 41 */     registerBeanDefinitionParser("mbean-export", (BeanDefinitionParser)new MBeanExportBeanDefinitionParser());
/* 42 */     registerBeanDefinitionParser("mbean-server", (BeanDefinitionParser)new MBeanServerBeanDefinitionParser());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/config/ContextNamespaceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */