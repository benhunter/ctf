/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Attr;
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
/*     */ public class SimpleConstructorNamespaceHandler
/*     */   implements NamespaceHandler
/*     */ {
/*     */   private static final String REF_SUFFIX = "-ref";
/*     */   private static final String DELIMITER_PREFIX = "_";
/*     */   
/*     */   public void init() {}
/*     */   
/*     */   @Nullable
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*  74 */     parserContext.getReaderContext().error("Class [" + 
/*  75 */         getClass().getName() + "] does not support custom elements.", element);
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
/*  81 */     if (node instanceof Attr) {
/*  82 */       Attr attr = (Attr)node;
/*  83 */       String argName = StringUtils.trimWhitespace(parserContext.getDelegate().getLocalName(attr));
/*  84 */       String argValue = StringUtils.trimWhitespace(attr.getValue());
/*     */       
/*  86 */       ConstructorArgumentValues cvs = definition.getBeanDefinition().getConstructorArgumentValues();
/*  87 */       boolean ref = false;
/*     */ 
/*     */       
/*  90 */       if (argName.endsWith("-ref")) {
/*  91 */         ref = true;
/*  92 */         argName = argName.substring(0, argName.length() - "-ref".length());
/*     */       } 
/*     */       
/*  95 */       ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(ref ? new RuntimeBeanReference(argValue) : argValue);
/*  96 */       valueHolder.setSource(parserContext.getReaderContext().extractSource(attr));
/*     */ 
/*     */       
/*  99 */       if (argName.startsWith("_")) {
/* 100 */         String arg = argName.substring(1).trim();
/*     */ 
/*     */         
/* 103 */         if (!StringUtils.hasText(arg)) {
/* 104 */           cvs.addGenericArgumentValue(valueHolder);
/*     */         }
/*     */         else {
/*     */           
/* 108 */           int index = -1;
/*     */           try {
/* 110 */             index = Integer.parseInt(arg);
/*     */           }
/* 112 */           catch (NumberFormatException ex) {
/* 113 */             parserContext.getReaderContext().error("Constructor argument '" + argName + "' specifies an invalid integer", attr);
/*     */           } 
/*     */           
/* 116 */           if (index < 0) {
/* 117 */             parserContext.getReaderContext().error("Constructor argument '" + argName + "' specifies a negative index", attr);
/*     */           }
/*     */ 
/*     */           
/* 121 */           if (cvs.hasIndexedArgumentValue(index)) {
/* 122 */             parserContext.getReaderContext().error("Constructor argument '" + argName + "' with index " + index + " already defined using <constructor-arg>. Only one approach may be used per argument.", attr);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 127 */           cvs.addIndexedArgumentValue(index, valueHolder);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 132 */         String name = Conventions.attributeNameToPropertyName(argName);
/* 133 */         if (containsArgWithName(name, cvs)) {
/* 134 */           parserContext.getReaderContext().error("Constructor argument '" + argName + "' already defined using <constructor-arg>. Only one approach may be used per argument.", attr);
/*     */         }
/*     */ 
/*     */         
/* 138 */         valueHolder.setName(Conventions.attributeNameToPropertyName(argName));
/* 139 */         cvs.addGenericArgumentValue(valueHolder);
/*     */       } 
/*     */     } 
/* 142 */     return definition;
/*     */   }
/*     */   
/*     */   private boolean containsArgWithName(String name, ConstructorArgumentValues cvs) {
/* 146 */     return (checkName(name, cvs.getGenericArgumentValues()) || 
/* 147 */       checkName(name, cvs.getIndexedArgumentValues().values()));
/*     */   }
/*     */   
/*     */   private boolean checkName(String name, Collection<ConstructorArgumentValues.ValueHolder> values) {
/* 151 */     for (ConstructorArgumentValues.ValueHolder holder : values) {
/* 152 */       if (name.equals(holder.getName())) {
/* 153 */         return true;
/*     */       }
/*     */     } 
/* 156 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/SimpleConstructorNamespaceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */