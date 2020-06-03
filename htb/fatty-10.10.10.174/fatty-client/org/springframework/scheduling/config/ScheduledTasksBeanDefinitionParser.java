/*     */ package org.springframework.scheduling.config;
/*     */ 
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ public class ScheduledTasksBeanDefinitionParser
/*     */   extends AbstractSingleBeanDefinitionParser
/*     */ {
/*     */   private static final String ELEMENT_SCHEDULED = "scheduled";
/*     */   private static final long ZERO_INITIAL_DELAY = 0L;
/*     */   
/*     */   protected boolean shouldGenerateId() {
/*  47 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getBeanClassName(Element element) {
/*  52 */     return "org.springframework.scheduling.config.ContextLifecycleScheduledTaskRegistrar";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/*  57 */     builder.setLazyInit(false);
/*  58 */     ManagedList<RuntimeBeanReference> cronTaskList = new ManagedList();
/*  59 */     ManagedList<RuntimeBeanReference> fixedDelayTaskList = new ManagedList();
/*  60 */     ManagedList<RuntimeBeanReference> fixedRateTaskList = new ManagedList();
/*  61 */     ManagedList<RuntimeBeanReference> triggerTaskList = new ManagedList();
/*  62 */     NodeList childNodes = element.getChildNodes();
/*  63 */     for (int i = 0; i < childNodes.getLength(); i++) {
/*  64 */       Node child = childNodes.item(i);
/*  65 */       if (isScheduledElement(child, parserContext)) {
/*     */ 
/*     */         
/*  68 */         Element taskElement = (Element)child;
/*  69 */         String ref = taskElement.getAttribute("ref");
/*  70 */         String method = taskElement.getAttribute("method");
/*     */ 
/*     */         
/*  73 */         if (!StringUtils.hasText(ref) || !StringUtils.hasText(method)) {
/*  74 */           parserContext.getReaderContext().error("Both 'ref' and 'method' are required", taskElement);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/*  79 */           String cronAttribute = taskElement.getAttribute("cron");
/*  80 */           String fixedDelayAttribute = taskElement.getAttribute("fixed-delay");
/*  81 */           String fixedRateAttribute = taskElement.getAttribute("fixed-rate");
/*  82 */           String triggerAttribute = taskElement.getAttribute("trigger");
/*  83 */           String initialDelayAttribute = taskElement.getAttribute("initial-delay");
/*     */           
/*  85 */           boolean hasCronAttribute = StringUtils.hasText(cronAttribute);
/*  86 */           boolean hasFixedDelayAttribute = StringUtils.hasText(fixedDelayAttribute);
/*  87 */           boolean hasFixedRateAttribute = StringUtils.hasText(fixedRateAttribute);
/*  88 */           boolean hasTriggerAttribute = StringUtils.hasText(triggerAttribute);
/*  89 */           boolean hasInitialDelayAttribute = StringUtils.hasText(initialDelayAttribute);
/*     */           
/*  91 */           if (!hasCronAttribute && !hasFixedDelayAttribute && !hasFixedRateAttribute && !hasTriggerAttribute) {
/*  92 */             parserContext.getReaderContext().error("one of the 'cron', 'fixed-delay', 'fixed-rate', or 'trigger' attributes is required", taskElement);
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*  97 */           else if (hasInitialDelayAttribute && (hasCronAttribute || hasTriggerAttribute)) {
/*  98 */             parserContext.getReaderContext().error("the 'initial-delay' attribute may not be used with cron and trigger tasks", taskElement);
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 104 */             String runnableName = runnableReference(ref, method, taskElement, parserContext).getBeanName();
/*     */             
/* 106 */             if (hasFixedDelayAttribute) {
/* 107 */               fixedDelayTaskList.add(intervalTaskReference(runnableName, initialDelayAttribute, fixedDelayAttribute, taskElement, parserContext));
/*     */             }
/*     */             
/* 110 */             if (hasFixedRateAttribute) {
/* 111 */               fixedRateTaskList.add(intervalTaskReference(runnableName, initialDelayAttribute, fixedRateAttribute, taskElement, parserContext));
/*     */             }
/*     */             
/* 114 */             if (hasCronAttribute) {
/* 115 */               cronTaskList.add(cronTaskReference(runnableName, cronAttribute, taskElement, parserContext));
/*     */             }
/*     */             
/* 118 */             if (hasTriggerAttribute)
/* 119 */             { String triggerName = (new RuntimeBeanReference(triggerAttribute)).getBeanName();
/* 120 */               triggerTaskList.add(triggerTaskReference(runnableName, triggerName, taskElement, parserContext)); } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 124 */     }  String schedulerRef = element.getAttribute("scheduler");
/* 125 */     if (StringUtils.hasText(schedulerRef)) {
/* 126 */       builder.addPropertyReference("taskScheduler", schedulerRef);
/*     */     }
/* 128 */     builder.addPropertyValue("cronTasksList", cronTaskList);
/* 129 */     builder.addPropertyValue("fixedDelayTasksList", fixedDelayTaskList);
/* 130 */     builder.addPropertyValue("fixedRateTasksList", fixedRateTaskList);
/* 131 */     builder.addPropertyValue("triggerTasksList", triggerTaskList);
/*     */   }
/*     */   
/*     */   private boolean isScheduledElement(Node node, ParserContext parserContext) {
/* 135 */     return (node.getNodeType() == 1 && "scheduled"
/* 136 */       .equals(parserContext.getDelegate().getLocalName(node)));
/*     */   }
/*     */   
/*     */   private RuntimeBeanReference runnableReference(String ref, String method, Element taskElement, ParserContext parserContext) {
/* 140 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("org.springframework.scheduling.support.ScheduledMethodRunnable");
/*     */     
/* 142 */     builder.addConstructorArgReference(ref);
/* 143 */     builder.addConstructorArgValue(method);
/* 144 */     return beanReference(taskElement, parserContext, builder);
/*     */   }
/*     */ 
/*     */   
/*     */   private RuntimeBeanReference intervalTaskReference(String runnableBeanName, String initialDelay, String interval, Element taskElement, ParserContext parserContext) {
/* 149 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("org.springframework.scheduling.config.IntervalTask");
/*     */     
/* 151 */     builder.addConstructorArgReference(runnableBeanName);
/* 152 */     builder.addConstructorArgValue(interval);
/* 153 */     builder.addConstructorArgValue(StringUtils.hasLength(initialDelay) ? initialDelay : Long.valueOf(0L));
/* 154 */     return beanReference(taskElement, parserContext, builder);
/*     */   }
/*     */ 
/*     */   
/*     */   private RuntimeBeanReference cronTaskReference(String runnableBeanName, String cronExpression, Element taskElement, ParserContext parserContext) {
/* 159 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("org.springframework.scheduling.config.CronTask");
/*     */     
/* 161 */     builder.addConstructorArgReference(runnableBeanName);
/* 162 */     builder.addConstructorArgValue(cronExpression);
/* 163 */     return beanReference(taskElement, parserContext, builder);
/*     */   }
/*     */ 
/*     */   
/*     */   private RuntimeBeanReference triggerTaskReference(String runnableBeanName, String triggerBeanName, Element taskElement, ParserContext parserContext) {
/* 168 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("org.springframework.scheduling.config.TriggerTask");
/*     */     
/* 170 */     builder.addConstructorArgReference(runnableBeanName);
/* 171 */     builder.addConstructorArgReference(triggerBeanName);
/* 172 */     return beanReference(taskElement, parserContext, builder);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RuntimeBeanReference beanReference(Element taskElement, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 178 */     builder.getRawBeanDefinition().setSource(parserContext.extractSource(taskElement));
/* 179 */     String generatedName = parserContext.getReaderContext().generateBeanName((BeanDefinition)builder.getRawBeanDefinition());
/* 180 */     parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)builder.getBeanDefinition(), generatedName));
/* 181 */     return new RuntimeBeanReference(generatedName);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/config/ScheduledTasksBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */