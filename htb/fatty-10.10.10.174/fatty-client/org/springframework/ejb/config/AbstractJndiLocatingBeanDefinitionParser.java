/*    */ package org.springframework.ejb.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractJndiLocatingBeanDefinitionParser
/*    */   extends AbstractSimpleBeanDefinitionParser
/*    */ {
/*    */   public static final String ENVIRONMENT = "environment";
/*    */   public static final String ENVIRONMENT_REF = "environment-ref";
/*    */   public static final String JNDI_ENVIRONMENT = "jndiEnvironment";
/*    */   
/*    */   protected boolean isEligibleAttribute(String attributeName) {
/* 52 */     return (super.isEligibleAttribute(attributeName) && !"environment-ref".equals(attributeName) && 
/* 53 */       !"lazy-init".equals(attributeName));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void postProcess(BeanDefinitionBuilder definitionBuilder, Element element) {
/* 58 */     Object envValue = DomUtils.getChildElementValueByTagName(element, "environment");
/* 59 */     if (envValue != null) {
/*    */       
/* 61 */       definitionBuilder.addPropertyValue("jndiEnvironment", envValue);
/*    */     }
/*    */     else {
/*    */       
/* 65 */       String envRef = element.getAttribute("environment-ref");
/* 66 */       if (StringUtils.hasLength(envRef)) {
/* 67 */         definitionBuilder.addPropertyValue("jndiEnvironment", new RuntimeBeanReference(envRef));
/*    */       }
/*    */     } 
/*    */     
/* 71 */     String lazyInit = element.getAttribute("lazy-init");
/* 72 */     if (StringUtils.hasText(lazyInit) && !"default".equals(lazyInit))
/* 73 */       definitionBuilder.setLazyInit("true".equals(lazyInit)); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ejb/config/AbstractJndiLocatingBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */