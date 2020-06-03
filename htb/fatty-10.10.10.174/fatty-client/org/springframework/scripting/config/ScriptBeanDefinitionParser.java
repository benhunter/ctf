/*     */ package org.springframework.scripting.config;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionDefaults;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scripting.support.ScriptFactoryPostProcessor;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.DomUtils;
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
/*     */ class ScriptBeanDefinitionParser
/*     */   extends AbstractBeanDefinitionParser
/*     */ {
/*     */   private static final String ENGINE_ATTRIBUTE = "engine";
/*     */   private static final String SCRIPT_SOURCE_ATTRIBUTE = "script-source";
/*     */   private static final String INLINE_SCRIPT_ELEMENT = "inline-script";
/*     */   private static final String SCOPE_ATTRIBUTE = "scope";
/*     */   private static final String AUTOWIRE_ATTRIBUTE = "autowire";
/*     */   private static final String DEPENDS_ON_ATTRIBUTE = "depends-on";
/*     */   private static final String INIT_METHOD_ATTRIBUTE = "init-method";
/*     */   private static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
/*     */   private static final String SCRIPT_INTERFACES_ATTRIBUTE = "script-interfaces";
/*     */   private static final String REFRESH_CHECK_DELAY_ATTRIBUTE = "refresh-check-delay";
/*     */   private static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";
/*     */   private static final String CUSTOMIZER_REF_ATTRIBUTE = "customizer-ref";
/*     */   private final String scriptFactoryClassName;
/*     */   
/*     */   public ScriptBeanDefinitionParser(String scriptFactoryClassName) {
/*  97 */     this.scriptFactoryClassName = scriptFactoryClassName;
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
/*     */   @Nullable
/*     */   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
/* 110 */     String engine = element.getAttribute("engine");
/*     */ 
/*     */     
/* 113 */     String value = resolveScriptSource(element, parserContext.getReaderContext());
/* 114 */     if (value == null) {
/* 115 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 119 */     LangNamespaceUtils.registerScriptFactoryPostProcessorIfNecessary(parserContext.getRegistry());
/*     */ 
/*     */     
/* 122 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/* 123 */     bd.setBeanClassName(this.scriptFactoryClassName);
/* 124 */     bd.setSource(parserContext.extractSource(element));
/* 125 */     bd.setAttribute(ScriptFactoryPostProcessor.LANGUAGE_ATTRIBUTE, element.getLocalName());
/*     */ 
/*     */     
/* 128 */     String scope = element.getAttribute("scope");
/* 129 */     if (StringUtils.hasLength(scope)) {
/* 130 */       bd.setScope(scope);
/*     */     }
/*     */ 
/*     */     
/* 134 */     String autowire = element.getAttribute("autowire");
/* 135 */     int autowireMode = parserContext.getDelegate().getAutowireMode(autowire);
/*     */     
/* 137 */     if (autowireMode == 4) {
/* 138 */       autowireMode = 2;
/*     */     }
/* 140 */     else if (autowireMode == 3) {
/* 141 */       autowireMode = 0;
/*     */     } 
/* 143 */     bd.setAutowireMode(autowireMode);
/*     */ 
/*     */     
/* 146 */     String dependsOn = element.getAttribute("depends-on");
/* 147 */     if (StringUtils.hasLength(dependsOn)) {
/* 148 */       bd.setDependsOn(StringUtils.tokenizeToStringArray(dependsOn, ",; "));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 153 */     BeanDefinitionDefaults beanDefinitionDefaults = parserContext.getDelegate().getBeanDefinitionDefaults();
/*     */ 
/*     */     
/* 156 */     String initMethod = element.getAttribute("init-method");
/* 157 */     if (StringUtils.hasLength(initMethod)) {
/* 158 */       bd.setInitMethodName(initMethod);
/*     */     }
/* 160 */     else if (beanDefinitionDefaults.getInitMethodName() != null) {
/* 161 */       bd.setInitMethodName(beanDefinitionDefaults.getInitMethodName());
/*     */     } 
/*     */     
/* 164 */     if (element.hasAttribute("destroy-method")) {
/* 165 */       String destroyMethod = element.getAttribute("destroy-method");
/* 166 */       bd.setDestroyMethodName(destroyMethod);
/*     */     }
/* 168 */     else if (beanDefinitionDefaults.getDestroyMethodName() != null) {
/* 169 */       bd.setDestroyMethodName(beanDefinitionDefaults.getDestroyMethodName());
/*     */     } 
/*     */ 
/*     */     
/* 173 */     String refreshCheckDelay = element.getAttribute("refresh-check-delay");
/* 174 */     if (StringUtils.hasText(refreshCheckDelay)) {
/* 175 */       bd.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE, Long.valueOf(refreshCheckDelay));
/*     */     }
/*     */ 
/*     */     
/* 179 */     String proxyTargetClass = element.getAttribute("proxy-target-class");
/* 180 */     if (StringUtils.hasText(proxyTargetClass)) {
/* 181 */       bd.setAttribute(ScriptFactoryPostProcessor.PROXY_TARGET_CLASS_ATTRIBUTE, Boolean.valueOf(proxyTargetClass));
/*     */     }
/*     */ 
/*     */     
/* 185 */     ConstructorArgumentValues cav = bd.getConstructorArgumentValues();
/* 186 */     int constructorArgNum = 0;
/* 187 */     if (StringUtils.hasLength(engine)) {
/* 188 */       cav.addIndexedArgumentValue(constructorArgNum++, engine);
/*     */     }
/* 190 */     cav.addIndexedArgumentValue(constructorArgNum++, value);
/* 191 */     if (element.hasAttribute("script-interfaces")) {
/* 192 */       cav.addIndexedArgumentValue(constructorArgNum++, element
/* 193 */           .getAttribute("script-interfaces"), "java.lang.Class[]");
/*     */     }
/*     */ 
/*     */     
/* 197 */     if (element.hasAttribute("customizer-ref")) {
/* 198 */       String customizerBeanName = element.getAttribute("customizer-ref");
/* 199 */       if (!StringUtils.hasText(customizerBeanName)) {
/* 200 */         parserContext.getReaderContext().error("Attribute 'customizer-ref' has empty value", element);
/*     */       } else {
/*     */         
/* 203 */         cav.addIndexedArgumentValue(constructorArgNum++, new RuntimeBeanReference(customizerBeanName));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 208 */     parserContext.getDelegate().parsePropertyElements(element, (BeanDefinition)bd);
/*     */     
/* 210 */     return (AbstractBeanDefinition)bd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String resolveScriptSource(Element element, XmlReaderContext readerContext) {
/* 220 */     boolean hasScriptSource = element.hasAttribute("script-source");
/* 221 */     List<Element> elements = DomUtils.getChildElementsByTagName(element, "inline-script");
/* 222 */     if (hasScriptSource && !elements.isEmpty()) {
/* 223 */       readerContext.error("Only one of 'script-source' and 'inline-script' should be specified.", element);
/* 224 */       return null;
/*     */     } 
/* 226 */     if (hasScriptSource) {
/* 227 */       return element.getAttribute("script-source");
/*     */     }
/* 229 */     if (!elements.isEmpty()) {
/* 230 */       Element inlineElement = elements.get(0);
/* 231 */       return "inline:" + DomUtils.getTextValue(inlineElement);
/*     */     } 
/*     */     
/* 234 */     readerContext.error("Must specify either 'script-source' or 'inline-script'.", element);
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldGenerateIdAsFallback() {
/* 244 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/config/ScriptBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */