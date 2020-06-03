/*     */ package org.springframework.web.servlet.config;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.util.xml.DomUtils;
/*     */ import org.springframework.web.servlet.view.BeanNameViewResolver;
/*     */ import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
/*     */ import org.springframework.web.servlet.view.InternalResourceViewResolver;
/*     */ import org.springframework.web.servlet.view.ViewResolverComposite;
/*     */ import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
/*     */ import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
/*     */ import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver;
/*     */ import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
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
/*     */ public class ViewResolversBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   public static final String VIEW_RESOLVER_BEAN_NAME = "mvcViewResolver";
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext context) {
/*  73 */     Object source = context.extractSource(element);
/*  74 */     context.pushContainingComponent(new CompositeComponentDefinition(element.getTagName(), source));
/*     */     
/*  76 */     ManagedList<Object> resolvers = new ManagedList(4);
/*  77 */     resolvers.setSource(context.extractSource(element));
/*  78 */     String[] names = { "jsp", "tiles", "bean-name", "freemarker", "groovy", "script-template", "bean", "ref" };
/*     */ 
/*     */     
/*  81 */     for (Element resolverElement : DomUtils.getChildElementsByTagName(element, names)) {
/*  82 */       RootBeanDefinition resolverBeanDef; String name = resolverElement.getLocalName();
/*  83 */       if ("bean".equals(name) || "ref".equals(name)) {
/*  84 */         resolvers.add(context.getDelegate().parsePropertySubElement(resolverElement, null));
/*     */         
/*     */         continue;
/*     */       } 
/*  88 */       if ("jsp".equals(name)) {
/*  89 */         resolverBeanDef = new RootBeanDefinition(InternalResourceViewResolver.class);
/*  90 */         resolverBeanDef.getPropertyValues().add("prefix", "/WEB-INF/");
/*  91 */         resolverBeanDef.getPropertyValues().add("suffix", ".jsp");
/*  92 */         addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */       }
/*  94 */       else if ("tiles".equals(name)) {
/*  95 */         resolverBeanDef = new RootBeanDefinition(TilesViewResolver.class);
/*  96 */         addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */       }
/*  98 */       else if ("freemarker".equals(name)) {
/*  99 */         resolverBeanDef = new RootBeanDefinition(FreeMarkerViewResolver.class);
/* 100 */         resolverBeanDef.getPropertyValues().add("suffix", ".ftl");
/* 101 */         addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */       }
/* 103 */       else if ("groovy".equals(name)) {
/* 104 */         resolverBeanDef = new RootBeanDefinition(GroovyMarkupViewResolver.class);
/* 105 */         resolverBeanDef.getPropertyValues().add("suffix", ".tpl");
/* 106 */         addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */       }
/* 108 */       else if ("script-template".equals(name)) {
/* 109 */         resolverBeanDef = new RootBeanDefinition(ScriptTemplateViewResolver.class);
/* 110 */         addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */       }
/* 112 */       else if ("bean-name".equals(name)) {
/* 113 */         resolverBeanDef = new RootBeanDefinition(BeanNameViewResolver.class);
/*     */       }
/*     */       else {
/*     */         
/* 117 */         throw new IllegalStateException("Unexpected element name: " + name);
/*     */       } 
/* 119 */       resolverBeanDef.setSource(source);
/* 120 */       resolverBeanDef.setRole(2);
/* 121 */       resolvers.add(resolverBeanDef);
/*     */     } 
/*     */     
/* 124 */     String beanName = "mvcViewResolver";
/* 125 */     RootBeanDefinition compositeResolverBeanDef = new RootBeanDefinition(ViewResolverComposite.class);
/* 126 */     compositeResolverBeanDef.setSource(source);
/* 127 */     compositeResolverBeanDef.setRole(2);
/*     */     
/* 129 */     names = new String[] { "content-negotiation" };
/* 130 */     List<Element> contentNegotiationElements = DomUtils.getChildElementsByTagName(element, names);
/* 131 */     if (contentNegotiationElements.isEmpty()) {
/* 132 */       compositeResolverBeanDef.getPropertyValues().add("viewResolvers", resolvers);
/*     */     }
/* 134 */     else if (contentNegotiationElements.size() == 1) {
/* 135 */       BeanDefinition beanDef = createContentNegotiatingViewResolver(contentNegotiationElements.get(0), context);
/* 136 */       beanDef.getPropertyValues().add("viewResolvers", resolvers);
/* 137 */       ManagedList<Object> list = new ManagedList(1);
/* 138 */       list.add(beanDef);
/* 139 */       compositeResolverBeanDef.getPropertyValues().add("order", Integer.valueOf(-2147483648));
/* 140 */       compositeResolverBeanDef.getPropertyValues().add("viewResolvers", list);
/*     */     } else {
/*     */       
/* 143 */       throw new IllegalArgumentException("Only one <content-negotiation> element is allowed.");
/*     */     } 
/*     */     
/* 146 */     if (element.hasAttribute("order")) {
/* 147 */       compositeResolverBeanDef.getPropertyValues().add("order", element.getAttribute("order"));
/*     */     }
/*     */     
/* 150 */     context.getReaderContext().getRegistry().registerBeanDefinition(beanName, (BeanDefinition)compositeResolverBeanDef);
/* 151 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)compositeResolverBeanDef, beanName));
/* 152 */     context.popAndRegisterContainingComponent();
/* 153 */     return null;
/*     */   }
/*     */   
/*     */   private void addUrlBasedViewResolverProperties(Element element, RootBeanDefinition beanDefinition) {
/* 157 */     if (element.hasAttribute("prefix")) {
/* 158 */       beanDefinition.getPropertyValues().add("prefix", element.getAttribute("prefix"));
/*     */     }
/* 160 */     if (element.hasAttribute("suffix")) {
/* 161 */       beanDefinition.getPropertyValues().add("suffix", element.getAttribute("suffix"));
/*     */     }
/* 163 */     if (element.hasAttribute("cache-views")) {
/* 164 */       beanDefinition.getPropertyValues().add("cache", element.getAttribute("cache-views"));
/*     */     }
/* 166 */     if (element.hasAttribute("view-class")) {
/* 167 */       beanDefinition.getPropertyValues().add("viewClass", element.getAttribute("view-class"));
/*     */     }
/* 169 */     if (element.hasAttribute("view-names")) {
/* 170 */       beanDefinition.getPropertyValues().add("viewNames", element.getAttribute("view-names"));
/*     */     }
/*     */   }
/*     */   
/*     */   private BeanDefinition createContentNegotiatingViewResolver(Element resolverElement, ParserContext context) {
/* 175 */     RootBeanDefinition beanDef = new RootBeanDefinition(ContentNegotiatingViewResolver.class);
/* 176 */     beanDef.setSource(context.extractSource(resolverElement));
/* 177 */     beanDef.setRole(2);
/* 178 */     MutablePropertyValues values = beanDef.getPropertyValues();
/*     */     
/* 180 */     List<Element> elements = DomUtils.getChildElementsByTagName(resolverElement, "default-views");
/* 181 */     if (!elements.isEmpty()) {
/* 182 */       ManagedList<Object> list = new ManagedList();
/* 183 */       for (Element element : DomUtils.getChildElementsByTagName(elements.get(0), new String[] { "bean", "ref" })) {
/* 184 */         list.add(context.getDelegate().parsePropertySubElement(element, null));
/*     */       }
/* 186 */       values.add("defaultViews", list);
/*     */     } 
/* 188 */     if (resolverElement.hasAttribute("use-not-acceptable")) {
/* 189 */       values.add("useNotAcceptableStatusCode", resolverElement.getAttribute("use-not-acceptable"));
/*     */     }
/* 191 */     Object manager = MvcNamespaceUtils.getContentNegotiationManager(context);
/* 192 */     if (manager != null) {
/* 193 */       values.add("contentNegotiationManager", manager);
/*     */     }
/* 195 */     return (BeanDefinition)beanDef;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/ViewResolversBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */