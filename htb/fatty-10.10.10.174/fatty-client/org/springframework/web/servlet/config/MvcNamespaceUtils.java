/*     */ package org.springframework.web.servlet.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
/*     */ import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
/*     */ import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MvcNamespaceUtils
/*     */ {
/*  46 */   private static final String BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME = BeanNameUrlHandlerMapping.class
/*  47 */     .getName();
/*     */   
/*  49 */   private static final String SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME = SimpleControllerHandlerAdapter.class
/*  50 */     .getName();
/*     */   
/*  52 */   private static final String HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME = HttpRequestHandlerAdapter.class
/*  53 */     .getName();
/*     */   
/*     */   private static final String URL_PATH_HELPER_BEAN_NAME = "mvcUrlPathHelper";
/*     */   
/*     */   private static final String PATH_MATCHER_BEAN_NAME = "mvcPathMatcher";
/*     */   
/*     */   private static final String CORS_CONFIGURATION_BEAN_NAME = "mvcCorsConfigurations";
/*     */   
/*     */   private static final String HANDLER_MAPPING_INTROSPECTOR_BEAN_NAME = "mvcHandlerMappingIntrospector";
/*     */ 
/*     */   
/*     */   public static void registerDefaultComponents(ParserContext parserContext, @Nullable Object source) {
/*  65 */     registerBeanNameUrlHandlerMapping(parserContext, source);
/*  66 */     registerHttpRequestHandlerAdapter(parserContext, source);
/*  67 */     registerSimpleControllerHandlerAdapter(parserContext, source);
/*  68 */     registerHandlerMappingIntrospector(parserContext, source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RuntimeBeanReference registerUrlPathHelper(@Nullable RuntimeBeanReference urlPathHelperRef, ParserContext parserContext, @Nullable Object source) {
/*  79 */     if (urlPathHelperRef != null) {
/*  80 */       if (parserContext.getRegistry().isAlias("mvcUrlPathHelper")) {
/*  81 */         parserContext.getRegistry().removeAlias("mvcUrlPathHelper");
/*     */       }
/*  83 */       parserContext.getRegistry().registerAlias(urlPathHelperRef.getBeanName(), "mvcUrlPathHelper");
/*     */     }
/*  85 */     else if (!parserContext.getRegistry().isAlias("mvcUrlPathHelper") && 
/*  86 */       !parserContext.getRegistry().containsBeanDefinition("mvcUrlPathHelper")) {
/*  87 */       RootBeanDefinition urlPathHelperDef = new RootBeanDefinition(UrlPathHelper.class);
/*  88 */       urlPathHelperDef.setSource(source);
/*  89 */       urlPathHelperDef.setRole(2);
/*  90 */       parserContext.getRegistry().registerBeanDefinition("mvcUrlPathHelper", (BeanDefinition)urlPathHelperDef);
/*  91 */       parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)urlPathHelperDef, "mvcUrlPathHelper"));
/*     */     } 
/*  93 */     return new RuntimeBeanReference("mvcUrlPathHelper");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RuntimeBeanReference registerPathMatcher(@Nullable RuntimeBeanReference pathMatcherRef, ParserContext parserContext, @Nullable Object source) {
/* 104 */     if (pathMatcherRef != null) {
/* 105 */       if (parserContext.getRegistry().isAlias("mvcPathMatcher")) {
/* 106 */         parserContext.getRegistry().removeAlias("mvcPathMatcher");
/*     */       }
/* 108 */       parserContext.getRegistry().registerAlias(pathMatcherRef.getBeanName(), "mvcPathMatcher");
/*     */     }
/* 110 */     else if (!parserContext.getRegistry().isAlias("mvcPathMatcher") && 
/* 111 */       !parserContext.getRegistry().containsBeanDefinition("mvcPathMatcher")) {
/* 112 */       RootBeanDefinition pathMatcherDef = new RootBeanDefinition(AntPathMatcher.class);
/* 113 */       pathMatcherDef.setSource(source);
/* 114 */       pathMatcherDef.setRole(2);
/* 115 */       parserContext.getRegistry().registerBeanDefinition("mvcPathMatcher", (BeanDefinition)pathMatcherDef);
/* 116 */       parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)pathMatcherDef, "mvcPathMatcher"));
/*     */     } 
/* 118 */     return new RuntimeBeanReference("mvcPathMatcher");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerBeanNameUrlHandlerMapping(ParserContext context, @Nullable Object source) {
/* 126 */     if (!context.getRegistry().containsBeanDefinition(BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME)) {
/* 127 */       RootBeanDefinition mappingDef = new RootBeanDefinition(BeanNameUrlHandlerMapping.class);
/* 128 */       mappingDef.setSource(source);
/* 129 */       mappingDef.setRole(2);
/* 130 */       mappingDef.getPropertyValues().add("order", Integer.valueOf(2));
/* 131 */       RuntimeBeanReference corsRef = registerCorsConfigurations(null, context, source);
/* 132 */       mappingDef.getPropertyValues().add("corsConfigurations", corsRef);
/* 133 */       context.getRegistry().registerBeanDefinition(BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME, (BeanDefinition)mappingDef);
/* 134 */       context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)mappingDef, BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerHttpRequestHandlerAdapter(ParserContext context, @Nullable Object source) {
/* 143 */     if (!context.getRegistry().containsBeanDefinition(HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME)) {
/* 144 */       RootBeanDefinition adapterDef = new RootBeanDefinition(HttpRequestHandlerAdapter.class);
/* 145 */       adapterDef.setSource(source);
/* 146 */       adapterDef.setRole(2);
/* 147 */       context.getRegistry().registerBeanDefinition(HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME, (BeanDefinition)adapterDef);
/* 148 */       context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)adapterDef, HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerSimpleControllerHandlerAdapter(ParserContext context, @Nullable Object source) {
/* 157 */     if (!context.getRegistry().containsBeanDefinition(SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME)) {
/* 158 */       RootBeanDefinition beanDef = new RootBeanDefinition(SimpleControllerHandlerAdapter.class);
/* 159 */       beanDef.setSource(source);
/* 160 */       beanDef.setRole(2);
/* 161 */       context.getRegistry().registerBeanDefinition(SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME, (BeanDefinition)beanDef);
/* 162 */       context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)beanDef, SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME));
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
/*     */   public static RuntimeBeanReference registerCorsConfigurations(@Nullable Map<String, CorsConfiguration> corsConfigurations, ParserContext context, @Nullable Object source) {
/* 176 */     if (!context.getRegistry().containsBeanDefinition("mvcCorsConfigurations")) {
/* 177 */       RootBeanDefinition corsDef = new RootBeanDefinition(LinkedHashMap.class);
/* 178 */       corsDef.setSource(source);
/* 179 */       corsDef.setRole(2);
/* 180 */       if (corsConfigurations != null) {
/* 181 */         corsDef.getConstructorArgumentValues().addIndexedArgumentValue(0, corsConfigurations);
/*     */       }
/* 183 */       context.getReaderContext().getRegistry().registerBeanDefinition("mvcCorsConfigurations", (BeanDefinition)corsDef);
/* 184 */       context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)corsDef, "mvcCorsConfigurations"));
/*     */     }
/* 186 */     else if (corsConfigurations != null) {
/* 187 */       BeanDefinition corsDef = context.getRegistry().getBeanDefinition("mvcCorsConfigurations");
/* 188 */       corsDef.getConstructorArgumentValues().addIndexedArgumentValue(0, corsConfigurations);
/*     */     } 
/* 190 */     return new RuntimeBeanReference("mvcCorsConfigurations");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerHandlerMappingIntrospector(ParserContext parserContext, @Nullable Object source) {
/* 198 */     if (!parserContext.getRegistry().containsBeanDefinition("mvcHandlerMappingIntrospector")) {
/* 199 */       RootBeanDefinition beanDef = new RootBeanDefinition(HandlerMappingIntrospector.class);
/* 200 */       beanDef.setSource(source);
/* 201 */       beanDef.setRole(2);
/* 202 */       beanDef.setLazyInit(true);
/* 203 */       parserContext.getRegistry().registerBeanDefinition("mvcHandlerMappingIntrospector", (BeanDefinition)beanDef);
/* 204 */       parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)beanDef, "mvcHandlerMappingIntrospector"));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Object getContentNegotiationManager(ParserContext context) {
/* 215 */     String name = AnnotationDrivenBeanDefinitionParser.HANDLER_MAPPING_BEAN_NAME;
/* 216 */     if (context.getRegistry().containsBeanDefinition(name)) {
/* 217 */       BeanDefinition handlerMappingBeanDef = context.getRegistry().getBeanDefinition(name);
/* 218 */       return handlerMappingBeanDef.getPropertyValues().get("contentNegotiationManager");
/*     */     } 
/* 220 */     name = "mvcContentNegotiationManager";
/* 221 */     if (context.getRegistry().containsBeanDefinition(name)) {
/* 222 */       return new RuntimeBeanReference(name);
/*     */     }
/* 224 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/MvcNamespaceUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */