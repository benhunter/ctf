/*    */ package org.springframework.beans.factory.xml;
/*    */ 
/*    */ import org.springframework.beans.MutablePropertyValues;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*    */ import org.springframework.core.Conventions;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.w3c.dom.Attr;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimplePropertyNamespaceHandler
/*    */   implements NamespaceHandler
/*    */ {
/*    */   private static final String REF_SUFFIX = "-ref";
/*    */   
/*    */   public void init() {}
/*    */   
/*    */   @Nullable
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 63 */     parserContext.getReaderContext().error("Class [" + 
/* 64 */         getClass().getName() + "] does not support custom elements.", element);
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
/* 70 */     if (node instanceof Attr) {
/* 71 */       Attr attr = (Attr)node;
/* 72 */       String propertyName = parserContext.getDelegate().getLocalName(attr);
/* 73 */       String propertyValue = attr.getValue();
/* 74 */       MutablePropertyValues pvs = definition.getBeanDefinition().getPropertyValues();
/* 75 */       if (pvs.contains(propertyName)) {
/* 76 */         parserContext.getReaderContext().error("Property '" + propertyName + "' is already defined using both <property> and inline syntax. Only one approach may be used per property.", attr);
/*    */       }
/*    */       
/* 79 */       if (propertyName.endsWith("-ref")) {
/* 80 */         propertyName = propertyName.substring(0, propertyName.length() - "-ref".length());
/* 81 */         pvs.add(Conventions.attributeNameToPropertyName(propertyName), new RuntimeBeanReference(propertyValue));
/*    */       } else {
/*    */         
/* 84 */         pvs.add(Conventions.attributeNameToPropertyName(propertyName), propertyValue);
/*    */       } 
/*    */     } 
/* 87 */     return definition;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/SimplePropertyNamespaceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */