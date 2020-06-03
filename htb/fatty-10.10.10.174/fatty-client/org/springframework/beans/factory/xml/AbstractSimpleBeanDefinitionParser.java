/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
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
/*     */ public abstract class AbstractSimpleBeanDefinitionParser
/*     */   extends AbstractSingleBeanDefinitionParser
/*     */ {
/*     */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 128 */     NamedNodeMap attributes = element.getAttributes();
/* 129 */     for (int x = 0; x < attributes.getLength(); x++) {
/* 130 */       Attr attribute = (Attr)attributes.item(x);
/* 131 */       if (isEligibleAttribute(attribute, parserContext)) {
/* 132 */         String propertyName = extractPropertyName(attribute.getLocalName());
/* 133 */         Assert.state(StringUtils.hasText(propertyName), "Illegal property name returned from 'extractPropertyName(String)': cannot be null or empty.");
/*     */         
/* 135 */         builder.addPropertyValue(propertyName, attribute.getValue());
/*     */       } 
/*     */     } 
/* 138 */     postProcess(builder, element);
/*     */   }
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
/*     */   protected boolean isEligibleAttribute(Attr attribute, ParserContext parserContext) {
/* 151 */     String fullName = attribute.getName();
/* 152 */     return (!fullName.equals("xmlns") && !fullName.startsWith("xmlns:") && 
/* 153 */       isEligibleAttribute(parserContext.getDelegate().getLocalName(attribute)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleAttribute(String attributeName) {
/* 165 */     return !"id".equals(attributeName);
/*     */   }
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
/*     */   protected String extractPropertyName(String attributeName) {
/* 182 */     return Conventions.attributeNameToPropertyName(attributeName);
/*     */   }
/*     */   
/*     */   protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/AbstractSimpleBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */