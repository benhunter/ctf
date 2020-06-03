/*     */ package org.springframework.scheduling.config;
/*     */ 
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Element;
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
/*     */ public class AnnotationDrivenBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String ASYNC_EXECUTION_ASPECT_CLASS_NAME = "org.springframework.scheduling.aspectj.AnnotationAsyncExecutionAspect";
/*     */   
/*     */   @Nullable
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*  52 */     Object source = parserContext.extractSource(element);
/*     */ 
/*     */     
/*  55 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
/*  56 */     parserContext.pushContainingComponent(compDefinition);
/*     */ 
/*     */     
/*  59 */     BeanDefinitionRegistry registry = parserContext.getRegistry();
/*     */     
/*  61 */     String mode = element.getAttribute("mode");
/*  62 */     if ("aspectj".equals(mode)) {
/*     */       
/*  64 */       registerAsyncExecutionAspect(element, parserContext);
/*     */ 
/*     */     
/*     */     }
/*  68 */     else if (registry.containsBeanDefinition("org.springframework.context.annotation.internalAsyncAnnotationProcessor")) {
/*  69 */       parserContext.getReaderContext().error("Only one AsyncAnnotationBeanPostProcessor may exist within the context.", source);
/*     */     }
/*     */     else {
/*     */       
/*  73 */       BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor");
/*     */       
/*  75 */       builder.getRawBeanDefinition().setSource(source);
/*  76 */       String executor = element.getAttribute("executor");
/*  77 */       if (StringUtils.hasText(executor)) {
/*  78 */         builder.addPropertyReference("executor", executor);
/*     */       }
/*  80 */       String exceptionHandler = element.getAttribute("exception-handler");
/*  81 */       if (StringUtils.hasText(exceptionHandler)) {
/*  82 */         builder.addPropertyReference("exceptionHandler", exceptionHandler);
/*     */       }
/*  84 */       if (Boolean.valueOf(element.getAttribute("proxy-target-class")).booleanValue()) {
/*  85 */         builder.addPropertyValue("proxyTargetClass", Boolean.valueOf(true));
/*     */       }
/*  87 */       registerPostProcessor(parserContext, builder, "org.springframework.context.annotation.internalAsyncAnnotationProcessor");
/*     */     } 
/*     */ 
/*     */     
/*  91 */     if (registry.containsBeanDefinition("org.springframework.context.annotation.internalScheduledAnnotationProcessor")) {
/*  92 */       parserContext.getReaderContext().error("Only one ScheduledAnnotationBeanPostProcessor may exist within the context.", source);
/*     */     }
/*     */     else {
/*     */       
/*  96 */       BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor");
/*     */       
/*  98 */       builder.getRawBeanDefinition().setSource(source);
/*  99 */       String scheduler = element.getAttribute("scheduler");
/* 100 */       if (StringUtils.hasText(scheduler)) {
/* 101 */         builder.addPropertyReference("scheduler", scheduler);
/*     */       }
/* 103 */       registerPostProcessor(parserContext, builder, "org.springframework.context.annotation.internalScheduledAnnotationProcessor");
/*     */     } 
/*     */ 
/*     */     
/* 107 */     parserContext.popAndRegisterContainingComponent();
/*     */     
/* 109 */     return null;
/*     */   }
/*     */   
/*     */   private void registerAsyncExecutionAspect(Element element, ParserContext parserContext) {
/* 113 */     if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.scheduling.config.internalAsyncExecutionAspect")) {
/* 114 */       BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("org.springframework.scheduling.aspectj.AnnotationAsyncExecutionAspect");
/* 115 */       builder.setFactoryMethod("aspectOf");
/* 116 */       String executor = element.getAttribute("executor");
/* 117 */       if (StringUtils.hasText(executor)) {
/* 118 */         builder.addPropertyReference("executor", executor);
/*     */       }
/* 120 */       String exceptionHandler = element.getAttribute("exception-handler");
/* 121 */       if (StringUtils.hasText(exceptionHandler)) {
/* 122 */         builder.addPropertyReference("exceptionHandler", exceptionHandler);
/*     */       }
/* 124 */       parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)builder.getBeanDefinition(), "org.springframework.scheduling.config.internalAsyncExecutionAspect"));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerPostProcessor(ParserContext parserContext, BeanDefinitionBuilder builder, String beanName) {
/* 132 */     builder.setRole(2);
/* 133 */     parserContext.getRegistry().registerBeanDefinition(beanName, (BeanDefinition)builder.getBeanDefinition());
/* 134 */     BeanDefinitionHolder holder = new BeanDefinitionHolder((BeanDefinition)builder.getBeanDefinition(), beanName);
/* 135 */     parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition(holder));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/config/AnnotationDrivenBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */