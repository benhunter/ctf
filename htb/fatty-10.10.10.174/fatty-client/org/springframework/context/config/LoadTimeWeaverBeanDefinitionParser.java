/*     */ package org.springframework.context.config;
/*     */ 
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LoadTimeWeaverBeanDefinitionParser
/*     */   extends AbstractSingleBeanDefinitionParser
/*     */ {
/*     */   public static final String ASPECTJ_WEAVING_ENABLER_BEAN_NAME = "org.springframework.context.config.internalAspectJWeavingEnabler";
/*     */   private static final String ASPECTJ_WEAVING_ENABLER_CLASS_NAME = "org.springframework.context.weaving.AspectJWeavingEnabler";
/*     */   private static final String DEFAULT_LOAD_TIME_WEAVER_CLASS_NAME = "org.springframework.context.weaving.DefaultContextLoadTimeWeaver";
/*     */   private static final String WEAVER_CLASS_ATTRIBUTE = "weaver-class";
/*     */   private static final String ASPECTJ_WEAVING_ATTRIBUTE = "aspectj-weaving";
/*     */   
/*     */   protected String getBeanClassName(Element element) {
/*  61 */     if (element.hasAttribute("weaver-class")) {
/*  62 */       return element.getAttribute("weaver-class");
/*     */     }
/*  64 */     return "org.springframework.context.weaving.DefaultContextLoadTimeWeaver";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/*  69 */     return "loadTimeWeaver";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/*  74 */     builder.setRole(2);
/*     */     
/*  76 */     if (isAspectJWeavingEnabled(element.getAttribute("aspectj-weaving"), parserContext)) {
/*  77 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.context.config.internalAspectJWeavingEnabler")) {
/*  78 */         RootBeanDefinition def = new RootBeanDefinition("org.springframework.context.weaving.AspectJWeavingEnabler");
/*  79 */         parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)def, "org.springframework.context.config.internalAspectJWeavingEnabler"));
/*     */       } 
/*     */ 
/*     */       
/*  83 */       if (isBeanConfigurerAspectEnabled(parserContext.getReaderContext().getBeanClassLoader())) {
/*  84 */         (new SpringConfiguredBeanDefinitionParser()).parse(element, parserContext);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isAspectJWeavingEnabled(String value, ParserContext parserContext) {
/*  90 */     if ("on".equals(value)) {
/*  91 */       return true;
/*     */     }
/*  93 */     if ("off".equals(value)) {
/*  94 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  98 */     ClassLoader cl = parserContext.getReaderContext().getBeanClassLoader();
/*  99 */     return (cl != null && cl.getResource("META-INF/aop.xml") != null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isBeanConfigurerAspectEnabled(@Nullable ClassLoader beanClassLoader) {
/* 104 */     return ClassUtils.isPresent("org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect", beanClassLoader);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/config/LoadTimeWeaverBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */