/*    */ package org.springframework.context.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PropertyPlaceholderBeanDefinitionParser
/*    */   extends AbstractPropertyLoadingBeanDefinitionParser
/*    */ {
/*    */   private static final String SYSTEM_PROPERTIES_MODE_ATTRIBUTE = "system-properties-mode";
/*    */   private static final String SYSTEM_PROPERTIES_MODE_DEFAULT = "ENVIRONMENT";
/*    */   
/*    */   protected Class<?> getBeanClass(Element element) {
/* 48 */     if ("ENVIRONMENT".equals(element.getAttribute("system-properties-mode"))) {
/* 49 */       return PropertySourcesPlaceholderConfigurer.class;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 54 */     return PropertyPlaceholderConfigurer.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 59 */     super.doParse(element, parserContext, builder);
/*    */     
/* 61 */     builder.addPropertyValue("ignoreUnresolvablePlaceholders", 
/* 62 */         Boolean.valueOf(element.getAttribute("ignore-unresolvable")));
/*    */     
/* 64 */     String systemPropertiesModeName = element.getAttribute("system-properties-mode");
/* 65 */     if (StringUtils.hasLength(systemPropertiesModeName) && 
/* 66 */       !systemPropertiesModeName.equals("ENVIRONMENT")) {
/* 67 */       builder.addPropertyValue("systemPropertiesModeName", "SYSTEM_PROPERTIES_MODE_" + systemPropertiesModeName);
/*    */     }
/*    */     
/* 70 */     if (element.hasAttribute("value-separator")) {
/* 71 */       builder.addPropertyValue("valueSeparator", element.getAttribute("value-separator"));
/*    */     }
/* 73 */     if (element.hasAttribute("trim-values")) {
/* 74 */       builder.addPropertyValue("trimValues", element.getAttribute("trim-values"));
/*    */     }
/* 76 */     if (element.hasAttribute("null-value"))
/* 77 */       builder.addPropertyValue("nullValue", element.getAttribute("null-value")); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/config/PropertyPlaceholderBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */