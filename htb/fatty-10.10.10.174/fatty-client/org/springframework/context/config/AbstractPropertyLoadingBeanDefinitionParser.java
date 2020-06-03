/*    */ package org.springframework.context.config;
/*    */ 
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
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
/*    */ abstract class AbstractPropertyLoadingBeanDefinitionParser
/*    */   extends AbstractSingleBeanDefinitionParser
/*    */ {
/*    */   protected boolean shouldGenerateId() {
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 44 */     String location = element.getAttribute("location");
/* 45 */     if (StringUtils.hasLength(location)) {
/* 46 */       location = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(location);
/* 47 */       String[] locations = StringUtils.commaDelimitedListToStringArray(location);
/* 48 */       builder.addPropertyValue("locations", locations);
/*    */     } 
/*    */     
/* 51 */     String propertiesRef = element.getAttribute("properties-ref");
/* 52 */     if (StringUtils.hasLength(propertiesRef)) {
/* 53 */       builder.addPropertyReference("properties", propertiesRef);
/*    */     }
/*    */     
/* 56 */     String fileEncoding = element.getAttribute("file-encoding");
/* 57 */     if (StringUtils.hasLength(fileEncoding)) {
/* 58 */       builder.addPropertyValue("fileEncoding", fileEncoding);
/*    */     }
/*    */     
/* 61 */     String order = element.getAttribute("order");
/* 62 */     if (StringUtils.hasLength(order)) {
/* 63 */       builder.addPropertyValue("order", Integer.valueOf(order));
/*    */     }
/*    */     
/* 66 */     builder.addPropertyValue("ignoreResourceNotFound", 
/* 67 */         Boolean.valueOf(element.getAttribute("ignore-resource-not-found")));
/*    */     
/* 69 */     builder.addPropertyValue("localOverride", 
/* 70 */         Boolean.valueOf(element.getAttribute("local-override")));
/*    */     
/* 72 */     builder.setRole(2);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/config/AbstractPropertyLoadingBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */