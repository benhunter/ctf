/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public final class ParserContext
/*     */ {
/*     */   private final XmlReaderContext readerContext;
/*     */   private final BeanDefinitionParserDelegate delegate;
/*     */   @Nullable
/*     */   private BeanDefinition containingBeanDefinition;
/*  50 */   private final Deque<CompositeComponentDefinition> containingComponents = new ArrayDeque<>();
/*     */ 
/*     */   
/*     */   public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate) {
/*  54 */     this.readerContext = readerContext;
/*  55 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate, @Nullable BeanDefinition containingBeanDefinition) {
/*  61 */     this.readerContext = readerContext;
/*  62 */     this.delegate = delegate;
/*  63 */     this.containingBeanDefinition = containingBeanDefinition;
/*     */   }
/*     */ 
/*     */   
/*     */   public final XmlReaderContext getReaderContext() {
/*  68 */     return this.readerContext;
/*     */   }
/*     */   
/*     */   public final BeanDefinitionRegistry getRegistry() {
/*  72 */     return this.readerContext.getRegistry();
/*     */   }
/*     */   
/*     */   public final BeanDefinitionParserDelegate getDelegate() {
/*  76 */     return this.delegate;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public final BeanDefinition getContainingBeanDefinition() {
/*  81 */     return this.containingBeanDefinition;
/*     */   }
/*     */   
/*     */   public final boolean isNested() {
/*  85 */     return (this.containingBeanDefinition != null);
/*     */   }
/*     */   
/*     */   public boolean isDefaultLazyInit() {
/*  89 */     return "true".equals(this.delegate.getDefaults().getLazyInit());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Object extractSource(Object sourceCandidate) {
/*  94 */     return this.readerContext.extractSource(sourceCandidate);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public CompositeComponentDefinition getContainingComponent() {
/*  99 */     return this.containingComponents.peek();
/*     */   }
/*     */   
/*     */   public void pushContainingComponent(CompositeComponentDefinition containingComponent) {
/* 103 */     this.containingComponents.push(containingComponent);
/*     */   }
/*     */   
/*     */   public CompositeComponentDefinition popContainingComponent() {
/* 107 */     return this.containingComponents.pop();
/*     */   }
/*     */   
/*     */   public void popAndRegisterContainingComponent() {
/* 111 */     registerComponent((ComponentDefinition)popContainingComponent());
/*     */   }
/*     */   
/*     */   public void registerComponent(ComponentDefinition component) {
/* 115 */     CompositeComponentDefinition containingComponent = getContainingComponent();
/* 116 */     if (containingComponent != null) {
/* 117 */       containingComponent.addNestedComponent(component);
/*     */     } else {
/*     */       
/* 120 */       this.readerContext.fireComponentRegistered(component);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerBeanComponent(BeanComponentDefinition component) {
/* 125 */     BeanDefinitionReaderUtils.registerBeanDefinition((BeanDefinitionHolder)component, getRegistry());
/* 126 */     registerComponent((ComponentDefinition)component);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/ParserContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */