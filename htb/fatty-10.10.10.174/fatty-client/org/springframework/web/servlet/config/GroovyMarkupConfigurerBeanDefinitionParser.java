/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*    */ import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
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
/*    */ public class GroovyMarkupConfigurerBeanDefinitionParser
/*    */   extends AbstractSimpleBeanDefinitionParser
/*    */ {
/*    */   public static final String BEAN_NAME = "mvcGroovyMarkupConfigurer";
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/* 42 */     return "mvcGroovyMarkupConfigurer";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getBeanClassName(Element element) {
/* 47 */     return "org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer";
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isEligibleAttribute(String name) {
/* 52 */     return (name.equals("auto-indent") || name.equals("cache-templates") || name.equals("resource-loader-path"));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/GroovyMarkupConfigurerBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */