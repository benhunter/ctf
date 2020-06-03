/*     */ package org.springframework.aop.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.aop.aspectj.AspectJAfterAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAroundAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJExpressionPointcut;
/*     */ import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
/*     */ import org.springframework.aop.aspectj.DeclareParentsAdvisor;
/*     */ import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanReference;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanNameReference;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ParseState;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.DomUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class ConfigBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String ASPECT = "aspect";
/*     */   private static final String EXPRESSION = "expression";
/*     */   private static final String ID = "id";
/*     */   private static final String POINTCUT = "pointcut";
/*     */   private static final String ADVICE_BEAN_NAME = "adviceBeanName";
/*     */   private static final String ADVISOR = "advisor";
/*     */   private static final String ADVICE_REF = "advice-ref";
/*     */   private static final String POINTCUT_REF = "pointcut-ref";
/*     */   private static final String REF = "ref";
/*     */   private static final String BEFORE = "before";
/*     */   private static final String DECLARE_PARENTS = "declare-parents";
/*     */   private static final String TYPE_PATTERN = "types-matching";
/*     */   private static final String DEFAULT_IMPL = "default-impl";
/*     */   private static final String DELEGATE_REF = "delegate-ref";
/*     */   private static final String IMPLEMENT_INTERFACE = "implement-interface";
/*     */   private static final String AFTER = "after";
/*     */   private static final String AFTER_RETURNING_ELEMENT = "after-returning";
/*     */   private static final String AFTER_THROWING_ELEMENT = "after-throwing";
/*     */   private static final String AROUND = "around";
/*     */   private static final String RETURNING = "returning";
/*     */   private static final String RETURNING_PROPERTY = "returningName";
/*     */   private static final String THROWING = "throwing";
/*     */   private static final String THROWING_PROPERTY = "throwingName";
/*     */   private static final String ARG_NAMES = "arg-names";
/*     */   private static final String ARG_NAMES_PROPERTY = "argumentNames";
/*     */   private static final String ASPECT_NAME_PROPERTY = "aspectName";
/*     */   private static final String DECLARATION_ORDER_PROPERTY = "declarationOrder";
/*     */   private static final String ORDER_PROPERTY = "order";
/*     */   private static final int METHOD_INDEX = 0;
/*     */   private static final int POINTCUT_INDEX = 1;
/*     */   private static final int ASPECT_INSTANCE_FACTORY_INDEX = 2;
/*  96 */   private ParseState parseState = new ParseState();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 103 */     CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), parserContext.extractSource(element));
/* 104 */     parserContext.pushContainingComponent(compositeDef);
/*     */     
/* 106 */     configureAutoProxyCreator(parserContext, element);
/*     */     
/* 108 */     List<Element> childElts = DomUtils.getChildElements(element);
/* 109 */     for (Element elt : childElts) {
/* 110 */       String localName = parserContext.getDelegate().getLocalName(elt);
/* 111 */       if ("pointcut".equals(localName)) {
/* 112 */         parsePointcut(elt, parserContext); continue;
/*     */       } 
/* 114 */       if ("advisor".equals(localName)) {
/* 115 */         parseAdvisor(elt, parserContext); continue;
/*     */       } 
/* 117 */       if ("aspect".equals(localName)) {
/* 118 */         parseAspect(elt, parserContext);
/*     */       }
/*     */     } 
/*     */     
/* 122 */     parserContext.popAndRegisterContainingComponent();
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void configureAutoProxyCreator(ParserContext parserContext, Element element) {
/* 133 */     AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(parserContext, element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseAdvisor(Element advisorElement, ParserContext parserContext) {
/* 142 */     AbstractBeanDefinition advisorDef = createAdvisorBeanDefinition(advisorElement, parserContext);
/* 143 */     String id = advisorElement.getAttribute("id");
/*     */     
/*     */     try {
/* 146 */       this.parseState.push(new AdvisorEntry(id));
/* 147 */       String advisorBeanName = id;
/* 148 */       if (StringUtils.hasText(advisorBeanName)) {
/* 149 */         parserContext.getRegistry().registerBeanDefinition(advisorBeanName, (BeanDefinition)advisorDef);
/*     */       } else {
/*     */         
/* 152 */         advisorBeanName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)advisorDef);
/*     */       } 
/*     */       
/* 155 */       Object pointcut = parsePointcutProperty(advisorElement, parserContext);
/* 156 */       if (pointcut instanceof BeanDefinition) {
/* 157 */         advisorDef.getPropertyValues().add("pointcut", pointcut);
/* 158 */         parserContext.registerComponent((ComponentDefinition)new AdvisorComponentDefinition(advisorBeanName, (BeanDefinition)advisorDef, (BeanDefinition)pointcut));
/*     */       
/*     */       }
/* 161 */       else if (pointcut instanceof String) {
/* 162 */         advisorDef.getPropertyValues().add("pointcut", new RuntimeBeanReference((String)pointcut));
/* 163 */         parserContext.registerComponent((ComponentDefinition)new AdvisorComponentDefinition(advisorBeanName, (BeanDefinition)advisorDef));
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 168 */       this.parseState.pop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractBeanDefinition createAdvisorBeanDefinition(Element advisorElement, ParserContext parserContext) {
/* 177 */     RootBeanDefinition advisorDefinition = new RootBeanDefinition(DefaultBeanFactoryPointcutAdvisor.class);
/* 178 */     advisorDefinition.setSource(parserContext.extractSource(advisorElement));
/*     */     
/* 180 */     String adviceRef = advisorElement.getAttribute("advice-ref");
/* 181 */     if (!StringUtils.hasText(adviceRef)) {
/* 182 */       parserContext.getReaderContext().error("'advice-ref' attribute contains empty value.", advisorElement, this.parseState
/* 183 */           .snapshot());
/*     */     } else {
/*     */       
/* 186 */       advisorDefinition.getPropertyValues().add("adviceBeanName", new RuntimeBeanNameReference(adviceRef));
/*     */     } 
/*     */ 
/*     */     
/* 190 */     if (advisorElement.hasAttribute("order")) {
/* 191 */       advisorDefinition.getPropertyValues().add("order", advisorElement
/* 192 */           .getAttribute("order"));
/*     */     }
/*     */     
/* 195 */     return (AbstractBeanDefinition)advisorDefinition;
/*     */   }
/*     */   
/*     */   private void parseAspect(Element aspectElement, ParserContext parserContext) {
/* 199 */     String aspectId = aspectElement.getAttribute("id");
/* 200 */     String aspectName = aspectElement.getAttribute("ref");
/*     */     
/*     */     try {
/* 203 */       this.parseState.push(new AspectEntry(aspectId, aspectName));
/* 204 */       List<BeanDefinition> beanDefinitions = new ArrayList<>();
/* 205 */       List<BeanReference> beanReferences = new ArrayList<>();
/*     */       
/* 207 */       List<Element> declareParents = DomUtils.getChildElementsByTagName(aspectElement, "declare-parents");
/* 208 */       for (int i = 0; i < declareParents.size(); i++) {
/* 209 */         Element declareParentsElement = declareParents.get(i);
/* 210 */         beanDefinitions.add(parseDeclareParents(declareParentsElement, parserContext));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 215 */       NodeList nodeList = aspectElement.getChildNodes();
/* 216 */       boolean adviceFoundAlready = false;
/* 217 */       for (int j = 0; j < nodeList.getLength(); j++) {
/* 218 */         Node node = nodeList.item(j);
/* 219 */         if (isAdviceNode(node, parserContext)) {
/* 220 */           if (!adviceFoundAlready) {
/* 221 */             adviceFoundAlready = true;
/* 222 */             if (!StringUtils.hasText(aspectName)) {
/* 223 */               parserContext.getReaderContext().error("<aspect> tag needs aspect bean reference via 'ref' attribute when declaring advices.", aspectElement, this.parseState
/*     */                   
/* 225 */                   .snapshot());
/*     */               return;
/*     */             } 
/* 228 */             beanReferences.add(new RuntimeBeanReference(aspectName));
/*     */           } 
/* 230 */           AbstractBeanDefinition advisorDefinition = parseAdvice(aspectName, j, aspectElement, (Element)node, parserContext, beanDefinitions, beanReferences);
/*     */           
/* 232 */           beanDefinitions.add(advisorDefinition);
/*     */         } 
/*     */       } 
/*     */       
/* 236 */       AspectComponentDefinition aspectComponentDefinition = createAspectComponentDefinition(aspectElement, aspectId, beanDefinitions, beanReferences, parserContext);
/*     */       
/* 238 */       parserContext.pushContainingComponent(aspectComponentDefinition);
/*     */       
/* 240 */       List<Element> pointcuts = DomUtils.getChildElementsByTagName(aspectElement, "pointcut");
/* 241 */       for (Element pointcutElement : pointcuts) {
/* 242 */         parsePointcut(pointcutElement, parserContext);
/*     */       }
/*     */       
/* 245 */       parserContext.popAndRegisterContainingComponent();
/*     */     } finally {
/*     */       
/* 248 */       this.parseState.pop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AspectComponentDefinition createAspectComponentDefinition(Element aspectElement, String aspectId, List<BeanDefinition> beanDefs, List<BeanReference> beanRefs, ParserContext parserContext) {
/* 256 */     BeanDefinition[] beanDefArray = beanDefs.<BeanDefinition>toArray(new BeanDefinition[0]);
/* 257 */     BeanReference[] beanRefArray = beanRefs.<BeanReference>toArray(new BeanReference[0]);
/* 258 */     Object source = parserContext.extractSource(aspectElement);
/* 259 */     return new AspectComponentDefinition(aspectId, beanDefArray, beanRefArray, source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isAdviceNode(Node aNode, ParserContext parserContext) {
/* 268 */     if (!(aNode instanceof Element)) {
/* 269 */       return false;
/*     */     }
/*     */     
/* 272 */     String name = parserContext.getDelegate().getLocalName(aNode);
/* 273 */     return ("before".equals(name) || "after".equals(name) || "after-returning".equals(name) || "after-throwing"
/* 274 */       .equals(name) || "around".equals(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractBeanDefinition parseDeclareParents(Element declareParentsElement, ParserContext parserContext) {
/* 284 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DeclareParentsAdvisor.class);
/* 285 */     builder.addConstructorArgValue(declareParentsElement.getAttribute("implement-interface"));
/* 286 */     builder.addConstructorArgValue(declareParentsElement.getAttribute("types-matching"));
/*     */     
/* 288 */     String defaultImpl = declareParentsElement.getAttribute("default-impl");
/* 289 */     String delegateRef = declareParentsElement.getAttribute("delegate-ref");
/*     */     
/* 291 */     if (StringUtils.hasText(defaultImpl) && !StringUtils.hasText(delegateRef)) {
/* 292 */       builder.addConstructorArgValue(defaultImpl);
/*     */     }
/* 294 */     else if (StringUtils.hasText(delegateRef) && !StringUtils.hasText(defaultImpl)) {
/* 295 */       builder.addConstructorArgReference(delegateRef);
/*     */     } else {
/*     */       
/* 298 */       parserContext.getReaderContext().error("Exactly one of the default-impl or delegate-ref attributes must be specified", declareParentsElement, this.parseState
/*     */           
/* 300 */           .snapshot());
/*     */     } 
/*     */     
/* 303 */     AbstractBeanDefinition definition = builder.getBeanDefinition();
/* 304 */     definition.setSource(parserContext.extractSource(declareParentsElement));
/* 305 */     parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)definition);
/* 306 */     return definition;
/*     */   }
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
/*     */   private AbstractBeanDefinition parseAdvice(String aspectName, int order, Element aspectElement, Element adviceElement, ParserContext parserContext, List<BeanDefinition> beanDefinitions, List<BeanReference> beanReferences) {
/*     */     try {
/* 320 */       this.parseState.push(new AdviceEntry(parserContext.getDelegate().getLocalName(adviceElement)));
/*     */ 
/*     */       
/* 323 */       RootBeanDefinition methodDefinition = new RootBeanDefinition(MethodLocatingFactoryBean.class);
/* 324 */       methodDefinition.getPropertyValues().add("targetBeanName", aspectName);
/* 325 */       methodDefinition.getPropertyValues().add("methodName", adviceElement.getAttribute("method"));
/* 326 */       methodDefinition.setSynthetic(true);
/*     */ 
/*     */       
/* 329 */       RootBeanDefinition aspectFactoryDef = new RootBeanDefinition(SimpleBeanFactoryAwareAspectInstanceFactory.class);
/*     */       
/* 331 */       aspectFactoryDef.getPropertyValues().add("aspectBeanName", aspectName);
/* 332 */       aspectFactoryDef.setSynthetic(true);
/*     */ 
/*     */       
/* 335 */       AbstractBeanDefinition adviceDef = createAdviceDefinition(adviceElement, parserContext, aspectName, order, methodDefinition, aspectFactoryDef, beanDefinitions, beanReferences);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 340 */       RootBeanDefinition advisorDefinition = new RootBeanDefinition(AspectJPointcutAdvisor.class);
/* 341 */       advisorDefinition.setSource(parserContext.extractSource(adviceElement));
/* 342 */       advisorDefinition.getConstructorArgumentValues().addGenericArgumentValue(adviceDef);
/* 343 */       if (aspectElement.hasAttribute("order")) {
/* 344 */         advisorDefinition.getPropertyValues().add("order", aspectElement
/* 345 */             .getAttribute("order"));
/*     */       }
/*     */ 
/*     */       
/* 349 */       parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)advisorDefinition);
/*     */       
/* 351 */       return (AbstractBeanDefinition)advisorDefinition;
/*     */     } finally {
/*     */       
/* 354 */       this.parseState.pop();
/*     */     } 
/*     */   }
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
/*     */   private AbstractBeanDefinition createAdviceDefinition(Element adviceElement, ParserContext parserContext, String aspectName, int order, RootBeanDefinition methodDef, RootBeanDefinition aspectFactoryDef, List<BeanDefinition> beanDefinitions, List<BeanReference> beanReferences) {
/* 369 */     RootBeanDefinition adviceDefinition = new RootBeanDefinition(getAdviceClass(adviceElement, parserContext));
/* 370 */     adviceDefinition.setSource(parserContext.extractSource(adviceElement));
/*     */     
/* 372 */     adviceDefinition.getPropertyValues().add("aspectName", aspectName);
/* 373 */     adviceDefinition.getPropertyValues().add("declarationOrder", Integer.valueOf(order));
/*     */     
/* 375 */     if (adviceElement.hasAttribute("returning")) {
/* 376 */       adviceDefinition.getPropertyValues().add("returningName", adviceElement
/* 377 */           .getAttribute("returning"));
/*     */     }
/* 379 */     if (adviceElement.hasAttribute("throwing")) {
/* 380 */       adviceDefinition.getPropertyValues().add("throwingName", adviceElement
/* 381 */           .getAttribute("throwing"));
/*     */     }
/* 383 */     if (adviceElement.hasAttribute("arg-names")) {
/* 384 */       adviceDefinition.getPropertyValues().add("argumentNames", adviceElement
/* 385 */           .getAttribute("arg-names"));
/*     */     }
/*     */     
/* 388 */     ConstructorArgumentValues cav = adviceDefinition.getConstructorArgumentValues();
/* 389 */     cav.addIndexedArgumentValue(0, methodDef);
/*     */     
/* 391 */     Object pointcut = parsePointcutProperty(adviceElement, parserContext);
/* 392 */     if (pointcut instanceof BeanDefinition) {
/* 393 */       cav.addIndexedArgumentValue(1, pointcut);
/* 394 */       beanDefinitions.add((BeanDefinition)pointcut);
/*     */     }
/* 396 */     else if (pointcut instanceof String) {
/* 397 */       RuntimeBeanReference pointcutRef = new RuntimeBeanReference((String)pointcut);
/* 398 */       cav.addIndexedArgumentValue(1, pointcutRef);
/* 399 */       beanReferences.add(pointcutRef);
/*     */     } 
/*     */     
/* 402 */     cav.addIndexedArgumentValue(2, aspectFactoryDef);
/*     */     
/* 404 */     return (AbstractBeanDefinition)adviceDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class<?> getAdviceClass(Element adviceElement, ParserContext parserContext) {
/* 411 */     String elementName = parserContext.getDelegate().getLocalName(adviceElement);
/* 412 */     if ("before".equals(elementName)) {
/* 413 */       return AspectJMethodBeforeAdvice.class;
/*     */     }
/* 415 */     if ("after".equals(elementName)) {
/* 416 */       return AspectJAfterAdvice.class;
/*     */     }
/* 418 */     if ("after-returning".equals(elementName)) {
/* 419 */       return AspectJAfterReturningAdvice.class;
/*     */     }
/* 421 */     if ("after-throwing".equals(elementName)) {
/* 422 */       return AspectJAfterThrowingAdvice.class;
/*     */     }
/* 424 */     if ("around".equals(elementName)) {
/* 425 */       return AspectJAroundAdvice.class;
/*     */     }
/*     */     
/* 428 */     throw new IllegalArgumentException("Unknown advice kind [" + elementName + "].");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractBeanDefinition parsePointcut(Element pointcutElement, ParserContext parserContext) {
/* 437 */     String id = pointcutElement.getAttribute("id");
/* 438 */     String expression = pointcutElement.getAttribute("expression");
/*     */     
/* 440 */     AbstractBeanDefinition pointcutDefinition = null;
/*     */     
/*     */     try {
/* 443 */       this.parseState.push(new PointcutEntry(id));
/* 444 */       pointcutDefinition = createPointcutDefinition(expression);
/* 445 */       pointcutDefinition.setSource(parserContext.extractSource(pointcutElement));
/*     */       
/* 447 */       String pointcutBeanName = id;
/* 448 */       if (StringUtils.hasText(pointcutBeanName)) {
/* 449 */         parserContext.getRegistry().registerBeanDefinition(pointcutBeanName, (BeanDefinition)pointcutDefinition);
/*     */       } else {
/*     */         
/* 452 */         pointcutBeanName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)pointcutDefinition);
/*     */       } 
/*     */       
/* 455 */       parserContext.registerComponent((ComponentDefinition)new PointcutComponentDefinition(pointcutBeanName, (BeanDefinition)pointcutDefinition, expression));
/*     */     }
/*     */     finally {
/*     */       
/* 459 */       this.parseState.pop();
/*     */     } 
/*     */     
/* 462 */     return pointcutDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object parsePointcutProperty(Element element, ParserContext parserContext) {
/* 473 */     if (element.hasAttribute("pointcut") && element.hasAttribute("pointcut-ref")) {
/* 474 */       parserContext.getReaderContext().error("Cannot define both 'pointcut' and 'pointcut-ref' on <advisor> tag.", element, this.parseState
/*     */           
/* 476 */           .snapshot());
/* 477 */       return null;
/*     */     } 
/* 479 */     if (element.hasAttribute("pointcut")) {
/*     */       
/* 481 */       String expression = element.getAttribute("pointcut");
/* 482 */       AbstractBeanDefinition pointcutDefinition = createPointcutDefinition(expression);
/* 483 */       pointcutDefinition.setSource(parserContext.extractSource(element));
/* 484 */       return pointcutDefinition;
/*     */     } 
/* 486 */     if (element.hasAttribute("pointcut-ref")) {
/* 487 */       String pointcutRef = element.getAttribute("pointcut-ref");
/* 488 */       if (!StringUtils.hasText(pointcutRef)) {
/* 489 */         parserContext.getReaderContext().error("'pointcut-ref' attribute contains empty value.", element, this.parseState
/* 490 */             .snapshot());
/* 491 */         return null;
/*     */       } 
/* 493 */       return pointcutRef;
/*     */     } 
/*     */     
/* 496 */     parserContext.getReaderContext().error("Must define one of 'pointcut' or 'pointcut-ref' on <advisor> tag.", element, this.parseState
/*     */         
/* 498 */         .snapshot());
/* 499 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractBeanDefinition createPointcutDefinition(String expression) {
/* 508 */     RootBeanDefinition beanDefinition = new RootBeanDefinition(AspectJExpressionPointcut.class);
/* 509 */     beanDefinition.setScope("prototype");
/* 510 */     beanDefinition.setSynthetic(true);
/* 511 */     beanDefinition.getPropertyValues().add("expression", expression);
/* 512 */     return (AbstractBeanDefinition)beanDefinition;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/ConfigBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */