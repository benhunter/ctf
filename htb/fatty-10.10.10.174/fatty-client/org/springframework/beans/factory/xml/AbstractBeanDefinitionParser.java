/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
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
/*     */ public abstract class AbstractBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   public static final String ID_ATTRIBUTE = "id";
/*     */   public static final String NAME_ATTRIBUTE = "name";
/*     */   
/*     */   @Nullable
/*     */   public final BeanDefinition parse(Element element, ParserContext parserContext) {
/*  63 */     AbstractBeanDefinition definition = parseInternal(element, parserContext);
/*  64 */     if (definition != null && !parserContext.isNested()) {
/*     */       try {
/*  66 */         String id = resolveId(element, definition, parserContext);
/*  67 */         if (!StringUtils.hasText(id)) {
/*  68 */           parserContext.getReaderContext().error("Id is required for element '" + parserContext
/*  69 */               .getDelegate().getLocalName(element) + "' when used as a top-level tag", element);
/*     */         }
/*     */         
/*  72 */         String[] aliases = null;
/*  73 */         if (shouldParseNameAsAliases()) {
/*  74 */           String name = element.getAttribute("name");
/*  75 */           if (StringUtils.hasLength(name)) {
/*  76 */             aliases = StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(name));
/*     */           }
/*     */         } 
/*  79 */         BeanDefinitionHolder holder = new BeanDefinitionHolder((BeanDefinition)definition, id, aliases);
/*  80 */         registerBeanDefinition(holder, parserContext.getRegistry());
/*  81 */         if (shouldFireEvents()) {
/*  82 */           BeanComponentDefinition componentDefinition = new BeanComponentDefinition(holder);
/*  83 */           postProcessComponentDefinition(componentDefinition);
/*  84 */           parserContext.registerComponent((ComponentDefinition)componentDefinition);
/*     */         }
/*     */       
/*  87 */       } catch (BeanDefinitionStoreException ex) {
/*  88 */         String msg = ex.getMessage();
/*  89 */         parserContext.getReaderContext().error((msg != null) ? msg : ex.toString(), element);
/*  90 */         return null;
/*     */       } 
/*     */     }
/*  93 */     return (BeanDefinition)definition;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
/* 112 */     if (shouldGenerateId()) {
/* 113 */       return parserContext.getReaderContext().generateBeanName((BeanDefinition)definition);
/*     */     }
/*     */     
/* 116 */     String id = element.getAttribute("id");
/* 117 */     if (!StringUtils.hasText(id) && shouldGenerateIdAsFallback()) {
/* 118 */       id = parserContext.getReaderContext().generateBeanName((BeanDefinition)definition);
/*     */     }
/* 120 */     return id;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerBeanDefinition(BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {
/* 139 */     BeanDefinitionReaderUtils.registerBeanDefinition(definition, registry);
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
/*     */   @Nullable
/*     */   protected abstract AbstractBeanDefinition parseInternal(Element paramElement, ParserContext paramParserContext);
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
/*     */   protected boolean shouldGenerateId() {
/* 164 */     return false;
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
/*     */   protected boolean shouldGenerateIdAsFallback() {
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldParseNameAsAliases() {
/* 187 */     return true;
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
/*     */ 
/*     */   
/*     */   protected boolean shouldFireEvents() {
/* 203 */     return true;
/*     */   }
/*     */   
/*     */   protected void postProcessComponentDefinition(BeanComponentDefinition componentDefinition) {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/AbstractBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */