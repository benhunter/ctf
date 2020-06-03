/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
/*     */ import org.springframework.beans.factory.config.ListFactoryBean;
/*     */ import org.springframework.beans.factory.config.MapFactoryBean;
/*     */ import org.springframework.beans.factory.config.PropertiesFactoryBean;
/*     */ import org.springframework.beans.factory.config.PropertyPathFactoryBean;
/*     */ import org.springframework.beans.factory.config.SetFactoryBean;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
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
/*     */ public class UtilNamespaceHandler
/*     */   extends NamespaceHandlerSupport
/*     */ {
/*     */   private static final String SCOPE_ATTRIBUTE = "scope";
/*     */   
/*     */   public void init() {
/*  50 */     registerBeanDefinitionParser("constant", new ConstantBeanDefinitionParser());
/*  51 */     registerBeanDefinitionParser("property-path", new PropertyPathBeanDefinitionParser());
/*  52 */     registerBeanDefinitionParser("list", new ListBeanDefinitionParser());
/*  53 */     registerBeanDefinitionParser("set", new SetBeanDefinitionParser());
/*  54 */     registerBeanDefinitionParser("map", new MapBeanDefinitionParser());
/*  55 */     registerBeanDefinitionParser("properties", new PropertiesBeanDefinitionParser());
/*     */   }
/*     */   
/*     */   private static class ConstantBeanDefinitionParser
/*     */     extends AbstractSimpleBeanDefinitionParser {
/*     */     private ConstantBeanDefinitionParser() {}
/*     */     
/*     */     protected Class<?> getBeanClass(Element element) {
/*  63 */       return FieldRetrievingFactoryBean.class;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/*  68 */       String id = super.resolveId(element, definition, parserContext);
/*  69 */       if (!StringUtils.hasText(id)) {
/*  70 */         id = element.getAttribute("static-field");
/*     */       }
/*  72 */       return id;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PropertyPathBeanDefinitionParser
/*     */     extends AbstractSingleBeanDefinitionParser {
/*     */     private PropertyPathBeanDefinitionParser() {}
/*     */     
/*     */     protected Class<?> getBeanClass(Element element) {
/*  81 */       return PropertyPathFactoryBean.class;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/*  86 */       String path = element.getAttribute("path");
/*  87 */       if (!StringUtils.hasText(path)) {
/*  88 */         parserContext.getReaderContext().error("Attribute 'path' must not be empty", element);
/*     */         return;
/*     */       } 
/*  91 */       int dotIndex = path.indexOf('.');
/*  92 */       if (dotIndex == -1) {
/*  93 */         parserContext.getReaderContext().error("Attribute 'path' must follow pattern 'beanName.propertyName'", element);
/*     */         
/*     */         return;
/*     */       } 
/*  97 */       String beanName = path.substring(0, dotIndex);
/*  98 */       String propertyPath = path.substring(dotIndex + 1);
/*  99 */       builder.addPropertyValue("targetBeanName", beanName);
/* 100 */       builder.addPropertyValue("propertyPath", propertyPath);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/* 105 */       String id = super.resolveId(element, definition, parserContext);
/* 106 */       if (!StringUtils.hasText(id)) {
/* 107 */         id = element.getAttribute("path");
/*     */       }
/* 109 */       return id;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ListBeanDefinitionParser
/*     */     extends AbstractSingleBeanDefinitionParser {
/*     */     private ListBeanDefinitionParser() {}
/*     */     
/*     */     protected Class<?> getBeanClass(Element element) {
/* 118 */       return ListFactoryBean.class;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 123 */       List<Object> parsedList = parserContext.getDelegate().parseListElement(element, (BeanDefinition)builder.getRawBeanDefinition());
/* 124 */       builder.addPropertyValue("sourceList", parsedList);
/*     */       
/* 126 */       String listClass = element.getAttribute("list-class");
/* 127 */       if (StringUtils.hasText(listClass)) {
/* 128 */         builder.addPropertyValue("targetListClass", listClass);
/*     */       }
/*     */       
/* 131 */       String scope = element.getAttribute("scope");
/* 132 */       if (StringUtils.hasLength(scope))
/* 133 */         builder.setScope(scope); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SetBeanDefinitionParser
/*     */     extends AbstractSingleBeanDefinitionParser
/*     */   {
/*     */     private SetBeanDefinitionParser() {}
/*     */     
/*     */     protected Class<?> getBeanClass(Element element) {
/* 143 */       return SetFactoryBean.class;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 148 */       Set<Object> parsedSet = parserContext.getDelegate().parseSetElement(element, (BeanDefinition)builder.getRawBeanDefinition());
/* 149 */       builder.addPropertyValue("sourceSet", parsedSet);
/*     */       
/* 151 */       String setClass = element.getAttribute("set-class");
/* 152 */       if (StringUtils.hasText(setClass)) {
/* 153 */         builder.addPropertyValue("targetSetClass", setClass);
/*     */       }
/*     */       
/* 156 */       String scope = element.getAttribute("scope");
/* 157 */       if (StringUtils.hasLength(scope))
/* 158 */         builder.setScope(scope); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MapBeanDefinitionParser
/*     */     extends AbstractSingleBeanDefinitionParser
/*     */   {
/*     */     private MapBeanDefinitionParser() {}
/*     */     
/*     */     protected Class<?> getBeanClass(Element element) {
/* 168 */       return MapFactoryBean.class;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 173 */       Map<Object, Object> parsedMap = parserContext.getDelegate().parseMapElement(element, (BeanDefinition)builder.getRawBeanDefinition());
/* 174 */       builder.addPropertyValue("sourceMap", parsedMap);
/*     */       
/* 176 */       String mapClass = element.getAttribute("map-class");
/* 177 */       if (StringUtils.hasText(mapClass)) {
/* 178 */         builder.addPropertyValue("targetMapClass", mapClass);
/*     */       }
/*     */       
/* 181 */       String scope = element.getAttribute("scope");
/* 182 */       if (StringUtils.hasLength(scope))
/* 183 */         builder.setScope(scope); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PropertiesBeanDefinitionParser
/*     */     extends AbstractSingleBeanDefinitionParser
/*     */   {
/*     */     private PropertiesBeanDefinitionParser() {}
/*     */     
/*     */     protected Class<?> getBeanClass(Element element) {
/* 193 */       return PropertiesFactoryBean.class;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 198 */       Properties parsedProps = parserContext.getDelegate().parsePropsElement(element);
/* 199 */       builder.addPropertyValue("properties", parsedProps);
/*     */       
/* 201 */       String location = element.getAttribute("location");
/* 202 */       if (StringUtils.hasLength(location)) {
/* 203 */         location = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(location);
/* 204 */         String[] locations = StringUtils.commaDelimitedListToStringArray(location);
/* 205 */         builder.addPropertyValue("locations", locations);
/*     */       } 
/*     */       
/* 208 */       builder.addPropertyValue("ignoreResourceNotFound", 
/* 209 */           Boolean.valueOf(element.getAttribute("ignore-resource-not-found")));
/*     */       
/* 211 */       builder.addPropertyValue("localOverride", 
/* 212 */           Boolean.valueOf(element.getAttribute("local-override")));
/*     */       
/* 214 */       String scope = element.getAttribute("scope");
/* 215 */       if (StringUtils.hasLength(scope))
/* 216 */         builder.setScope(scope); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/UtilNamespaceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */