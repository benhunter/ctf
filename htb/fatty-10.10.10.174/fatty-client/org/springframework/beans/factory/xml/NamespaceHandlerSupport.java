/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
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
/*     */ public abstract class NamespaceHandlerSupport
/*     */   implements NamespaceHandler
/*     */ {
/*  51 */   private final Map<String, BeanDefinitionParser> parsers = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private final Map<String, BeanDefinitionDecorator> decorators = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private final Map<String, BeanDefinitionDecorator> attributeDecorators = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*  73 */     BeanDefinitionParser parser = findParserForElement(element, parserContext);
/*  74 */     return (parser != null) ? parser.parse(element, parserContext) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext) {
/*  83 */     String localName = parserContext.getDelegate().getLocalName(element);
/*  84 */     BeanDefinitionParser parser = this.parsers.get(localName);
/*  85 */     if (parser == null) {
/*  86 */       parserContext.getReaderContext().fatal("Cannot locate BeanDefinitionParser for element [" + localName + "]", element);
/*     */     }
/*     */     
/*  89 */     return parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
/* 101 */     BeanDefinitionDecorator decorator = findDecoratorForNode(node, parserContext);
/* 102 */     return (decorator != null) ? decorator.decorate(node, definition, parserContext) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BeanDefinitionDecorator findDecoratorForNode(Node node, ParserContext parserContext) {
/* 112 */     BeanDefinitionDecorator decorator = null;
/* 113 */     String localName = parserContext.getDelegate().getLocalName(node);
/* 114 */     if (node instanceof Element) {
/* 115 */       decorator = this.decorators.get(localName);
/*     */     }
/* 117 */     else if (node instanceof org.w3c.dom.Attr) {
/* 118 */       decorator = this.attributeDecorators.get(localName);
/*     */     } else {
/*     */       
/* 121 */       parserContext.getReaderContext().fatal("Cannot decorate based on Nodes of type [" + node
/* 122 */           .getClass().getName() + "]", node);
/*     */     } 
/* 124 */     if (decorator == null) {
/* 125 */       parserContext.getReaderContext().fatal("Cannot locate BeanDefinitionDecorator for " + ((node instanceof Element) ? "element" : "attribute") + " [" + localName + "]", node);
/*     */     }
/*     */     
/* 128 */     return decorator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
/* 138 */     this.parsers.put(elementName, parser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void registerBeanDefinitionDecorator(String elementName, BeanDefinitionDecorator dec) {
/* 147 */     this.decorators.put(elementName, dec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void registerBeanDefinitionDecoratorForAttribute(String attrName, BeanDefinitionDecorator dec) {
/* 156 */     this.attributeDecorators.put(attrName, dec);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/NamespaceHandlerSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */