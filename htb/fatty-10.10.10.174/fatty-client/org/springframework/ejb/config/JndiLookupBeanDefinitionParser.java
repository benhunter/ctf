/*    */ package org.springframework.ejb.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.jndi.JndiObjectFactoryBean;
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
/*    */ class JndiLookupBeanDefinitionParser
/*    */   extends AbstractJndiLocatingBeanDefinitionParser
/*    */ {
/*    */   public static final String DEFAULT_VALUE = "default-value";
/*    */   public static final String DEFAULT_REF = "default-ref";
/*    */   public static final String DEFAULT_OBJECT = "defaultObject";
/*    */   
/*    */   protected Class<?> getBeanClass(Element element) {
/* 47 */     return JndiObjectFactoryBean.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isEligibleAttribute(String attributeName) {
/* 52 */     return (super.isEligibleAttribute(attributeName) && 
/* 53 */       !"default-value".equals(attributeName) && !"default-ref".equals(attributeName));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 58 */     super.doParse(element, parserContext, builder);
/*    */     
/* 60 */     String defaultValue = element.getAttribute("default-value");
/* 61 */     String defaultRef = element.getAttribute("default-ref");
/* 62 */     if (StringUtils.hasLength(defaultValue)) {
/* 63 */       if (StringUtils.hasLength(defaultRef)) {
/* 64 */         parserContext.getReaderContext().error("<jndi-lookup> element is only allowed to contain either 'default-value' attribute OR 'default-ref' attribute, not both", element);
/*    */       }
/*    */       
/* 67 */       builder.addPropertyValue("defaultObject", defaultValue);
/*    */     }
/* 69 */     else if (StringUtils.hasLength(defaultRef)) {
/* 70 */       builder.addPropertyValue("defaultObject", new RuntimeBeanReference(defaultRef));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ejb/config/JndiLookupBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */