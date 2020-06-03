/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class AnnotationConfigBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   @Nullable
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 45 */     Object source = parserContext.extractSource(element);
/*    */ 
/*    */ 
/*    */     
/* 49 */     Set<BeanDefinitionHolder> processorDefinitions = AnnotationConfigUtils.registerAnnotationConfigProcessors(parserContext.getRegistry(), source);
/*    */ 
/*    */     
/* 52 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
/* 53 */     parserContext.pushContainingComponent(compDefinition);
/*    */ 
/*    */     
/* 56 */     for (BeanDefinitionHolder processorDefinition : processorDefinitions) {
/* 57 */       parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition(processorDefinition));
/*    */     }
/*    */ 
/*    */     
/* 61 */     parserContext.popAndRegisterContainingComponent();
/*    */     
/* 63 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AnnotationConfigBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */