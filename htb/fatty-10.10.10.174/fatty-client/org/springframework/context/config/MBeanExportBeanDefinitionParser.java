/*    */ package org.springframework.context.config;
/*    */ 
/*    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
/*    */ import org.springframework.jmx.support.RegistrationPolicy;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class MBeanExportBeanDefinitionParser
/*    */   extends AbstractBeanDefinitionParser
/*    */ {
/*    */   private static final String MBEAN_EXPORTER_BEAN_NAME = "mbeanExporter";
/*    */   private static final String DEFAULT_DOMAIN_ATTRIBUTE = "default-domain";
/*    */   private static final String SERVER_ATTRIBUTE = "server";
/*    */   private static final String REGISTRATION_ATTRIBUTE = "registration";
/*    */   private static final String REGISTRATION_IGNORE_EXISTING = "ignoreExisting";
/*    */   private static final String REGISTRATION_REPLACE_EXISTING = "replaceExisting";
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/* 59 */     return "mbeanExporter";
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
/* 64 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AnnotationMBeanExporter.class);
/*    */ 
/*    */     
/* 67 */     builder.setRole(2);
/* 68 */     builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
/*    */     
/* 70 */     String defaultDomain = element.getAttribute("default-domain");
/* 71 */     if (StringUtils.hasText(defaultDomain)) {
/* 72 */       builder.addPropertyValue("defaultDomain", defaultDomain);
/*    */     }
/*    */     
/* 75 */     String serverBeanName = element.getAttribute("server");
/* 76 */     if (StringUtils.hasText(serverBeanName)) {
/* 77 */       builder.addPropertyReference("server", serverBeanName);
/*    */     } else {
/*    */       
/* 80 */       AbstractBeanDefinition specialServer = MBeanServerBeanDefinitionParser.findServerForSpecialEnvironment();
/* 81 */       if (specialServer != null) {
/* 82 */         builder.addPropertyValue("server", specialServer);
/*    */       }
/*    */     } 
/*    */     
/* 86 */     String registration = element.getAttribute("registration");
/* 87 */     RegistrationPolicy registrationPolicy = RegistrationPolicy.FAIL_ON_EXISTING;
/* 88 */     if ("ignoreExisting".equals(registration)) {
/* 89 */       registrationPolicy = RegistrationPolicy.IGNORE_EXISTING;
/*    */     }
/* 91 */     else if ("replaceExisting".equals(registration)) {
/* 92 */       registrationPolicy = RegistrationPolicy.REPLACE_EXISTING;
/*    */     } 
/* 94 */     builder.addPropertyValue("registrationPolicy", registrationPolicy);
/*    */     
/* 96 */     return builder.getBeanDefinition();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/config/MBeanExportBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */