/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public abstract class AbstractSingleBeanDefinitionParser
/*     */   extends AbstractBeanDefinitionParser
/*     */ {
/*     */   protected final AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
/*  63 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
/*  64 */     String parentName = getParentName(element);
/*  65 */     if (parentName != null) {
/*  66 */       builder.getRawBeanDefinition().setParentName(parentName);
/*     */     }
/*  68 */     Class<?> beanClass = getBeanClass(element);
/*  69 */     if (beanClass != null) {
/*  70 */       builder.getRawBeanDefinition().setBeanClass(beanClass);
/*     */     } else {
/*     */       
/*  73 */       String beanClassName = getBeanClassName(element);
/*  74 */       if (beanClassName != null) {
/*  75 */         builder.getRawBeanDefinition().setBeanClassName(beanClassName);
/*     */       }
/*     */     } 
/*  78 */     builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
/*  79 */     BeanDefinition containingBd = parserContext.getContainingBeanDefinition();
/*  80 */     if (containingBd != null)
/*     */     {
/*  82 */       builder.setScope(containingBd.getScope());
/*     */     }
/*  84 */     if (parserContext.isDefaultLazyInit())
/*     */     {
/*  86 */       builder.setLazyInit(true);
/*     */     }
/*  88 */     doParse(element, parserContext, builder);
/*  89 */     return builder.getBeanDefinition();
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
/*     */   protected String getParentName(Element element) {
/* 103 */     return null;
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
/*     */   @Nullable
/*     */   protected Class<?> getBeanClass(Element element) {
/* 120 */     return null;
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
/*     */   protected String getBeanClassName(Element element) {
/* 132 */     return null;
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
/*     */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 146 */     doParse(element, builder);
/*     */   }
/*     */   
/*     */   protected void doParse(Element element, BeanDefinitionBuilder builder) {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/AbstractSingleBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */