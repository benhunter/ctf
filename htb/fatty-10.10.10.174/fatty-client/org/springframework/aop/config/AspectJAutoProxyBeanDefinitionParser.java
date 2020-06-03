/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.TypedStringValue;
/*    */ import org.springframework.beans.factory.support.ManagedList;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
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
/*    */ class AspectJAutoProxyBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   @Nullable
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 44 */     AopNamespaceUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(parserContext, element);
/* 45 */     extendBeanDefinition(element, parserContext);
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   private void extendBeanDefinition(Element element, ParserContext parserContext) {
/* 51 */     BeanDefinition beanDef = parserContext.getRegistry().getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/* 52 */     if (element.hasChildNodes()) {
/* 53 */       addIncludePatterns(element, parserContext, beanDef);
/*    */     }
/*    */   }
/*    */   
/*    */   private void addIncludePatterns(Element element, ParserContext parserContext, BeanDefinition beanDef) {
/* 58 */     ManagedList<TypedStringValue> includePatterns = new ManagedList();
/* 59 */     NodeList childNodes = element.getChildNodes();
/* 60 */     for (int i = 0; i < childNodes.getLength(); i++) {
/* 61 */       Node node = childNodes.item(i);
/* 62 */       if (node instanceof Element) {
/* 63 */         Element includeElement = (Element)node;
/* 64 */         TypedStringValue valueHolder = new TypedStringValue(includeElement.getAttribute("name"));
/* 65 */         valueHolder.setSource(parserContext.extractSource(includeElement));
/* 66 */         includePatterns.add(valueHolder);
/*    */       } 
/*    */     } 
/* 69 */     if (!includePatterns.isEmpty()) {
/* 70 */       includePatterns.setSource(parserContext.extractSource(element));
/* 71 */       beanDef.getPropertyValues().add("includePatterns", includePatterns);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/AspectJAutoProxyBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */