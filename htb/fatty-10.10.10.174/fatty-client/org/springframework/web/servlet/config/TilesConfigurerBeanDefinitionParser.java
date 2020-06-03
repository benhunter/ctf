/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.util.xml.DomUtils;
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
/*    */ 
/*    */ public class TilesConfigurerBeanDefinitionParser
/*    */   extends AbstractSingleBeanDefinitionParser
/*    */ {
/*    */   public static final String BEAN_NAME = "mvcTilesConfigurer";
/*    */   
/*    */   protected String getBeanClassName(Element element) {
/* 49 */     return "org.springframework.web.servlet.view.tiles3.TilesConfigurer";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/* 54 */     return "mvcTilesConfigurer";
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 59 */     List<Element> childElements = DomUtils.getChildElementsByTagName(element, "definitions");
/* 60 */     if (!childElements.isEmpty()) {
/* 61 */       List<String> locations = new ArrayList<>(childElements.size());
/* 62 */       for (Element childElement : childElements) {
/* 63 */         locations.add(childElement.getAttribute("location"));
/*    */       }
/* 65 */       builder.addPropertyValue("definitions", StringUtils.toStringArray(locations));
/*    */     } 
/* 67 */     if (element.hasAttribute("check-refresh")) {
/* 68 */       builder.addPropertyValue("checkRefresh", element.getAttribute("check-refresh"));
/*    */     }
/* 70 */     if (element.hasAttribute("validate-definitions")) {
/* 71 */       builder.addPropertyValue("validateDefinitions", element.getAttribute("validate-definitions"));
/*    */     }
/* 73 */     if (element.hasAttribute("definitions-factory")) {
/* 74 */       builder.addPropertyValue("definitionsFactoryClass", element.getAttribute("definitions-factory"));
/*    */     }
/* 76 */     if (element.hasAttribute("preparer-factory"))
/* 77 */       builder.addPropertyValue("preparerFactoryClass", element.getAttribute("preparer-factory")); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/TilesConfigurerBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */