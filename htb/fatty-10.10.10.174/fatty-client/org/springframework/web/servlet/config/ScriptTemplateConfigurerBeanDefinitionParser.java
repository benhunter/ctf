/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
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
/*    */ public class ScriptTemplateConfigurerBeanDefinitionParser
/*    */   extends AbstractSimpleBeanDefinitionParser
/*    */ {
/*    */   public static final String BEAN_NAME = "mvcScriptTemplateConfigurer";
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/* 49 */     return "mvcScriptTemplateConfigurer";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getBeanClassName(Element element) {
/* 54 */     return "org.springframework.web.servlet.view.script.ScriptTemplateConfigurer";
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 59 */     List<Element> childElements = DomUtils.getChildElementsByTagName(element, "script");
/* 60 */     if (!childElements.isEmpty()) {
/* 61 */       List<String> locations = new ArrayList<>(childElements.size());
/* 62 */       for (Element childElement : childElements) {
/* 63 */         locations.add(childElement.getAttribute("location"));
/*    */       }
/* 65 */       builder.addPropertyValue("scripts", StringUtils.toStringArray(locations));
/*    */     } 
/* 67 */     builder.addPropertyValue("engineName", element.getAttribute("engine-name"));
/* 68 */     if (element.hasAttribute("render-object")) {
/* 69 */       builder.addPropertyValue("renderObject", element.getAttribute("render-object"));
/*    */     }
/* 71 */     if (element.hasAttribute("render-function")) {
/* 72 */       builder.addPropertyValue("renderFunction", element.getAttribute("render-function"));
/*    */     }
/* 74 */     if (element.hasAttribute("content-type")) {
/* 75 */       builder.addPropertyValue("contentType", element.getAttribute("content-type"));
/*    */     }
/* 77 */     if (element.hasAttribute("charset")) {
/* 78 */       builder.addPropertyValue("charset", Charset.forName(element.getAttribute("charset")));
/*    */     }
/* 80 */     if (element.hasAttribute("resource-loader-path")) {
/* 81 */       builder.addPropertyValue("resourceLoaderPath", element.getAttribute("resource-loader-path"));
/*    */     }
/* 83 */     if (element.hasAttribute("shared-engine")) {
/* 84 */       builder.addPropertyValue("sharedEngine", element.getAttribute("shared-engine"));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isEligibleAttribute(String name) {
/* 90 */     return (name.equals("engine-name") || name.equals("scripts") || name.equals("render-object") || name
/* 91 */       .equals("render-function") || name.equals("content-type") || name
/* 92 */       .equals("charset") || name.equals("resource-loader-path"));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/ScriptTemplateConfigurerBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */