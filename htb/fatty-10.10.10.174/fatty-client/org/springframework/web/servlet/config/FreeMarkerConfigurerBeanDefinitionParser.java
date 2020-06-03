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
/*    */ public class FreeMarkerConfigurerBeanDefinitionParser
/*    */   extends AbstractSingleBeanDefinitionParser
/*    */ {
/*    */   public static final String BEAN_NAME = "mvcFreeMarkerConfigurer";
/*    */   
/*    */   protected String getBeanClassName(Element element) {
/* 48 */     return "org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/* 53 */     return "mvcFreeMarkerConfigurer";
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 58 */     List<Element> childElements = DomUtils.getChildElementsByTagName(element, "template-loader-path");
/* 59 */     if (!childElements.isEmpty()) {
/* 60 */       List<String> locations = new ArrayList<>(childElements.size());
/* 61 */       for (Element childElement : childElements) {
/* 62 */         locations.add(childElement.getAttribute("location"));
/*    */       }
/* 64 */       if (locations.isEmpty()) {
/* 65 */         locations.add("/WEB-INF/");
/*    */       }
/* 67 */       builder.addPropertyValue("templateLoaderPaths", StringUtils.toStringArray(locations));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/FreeMarkerConfigurerBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */