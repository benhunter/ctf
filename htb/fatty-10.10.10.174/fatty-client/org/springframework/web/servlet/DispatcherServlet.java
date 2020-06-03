/*      */ package org.springframework.web.servlet;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.servlet.DispatcherType;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanInitializationException;
/*      */ import org.springframework.beans.factory.ListableBeanFactory;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.context.ApplicationContext;
/*      */ import org.springframework.context.i18n.LocaleContext;
/*      */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*      */ import org.springframework.core.io.ClassPathResource;
/*      */ import org.springframework.core.io.Resource;
/*      */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*      */ import org.springframework.core.log.LogFormatUtils;
/*      */ import org.springframework.http.server.ServletServerHttpRequest;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.ui.context.ThemeSource;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.web.context.WebApplicationContext;
/*      */ import org.springframework.web.context.request.ServletWebRequest;
/*      */ import org.springframework.web.context.request.async.WebAsyncManager;
/*      */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*      */ import org.springframework.web.multipart.MultipartException;
/*      */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*      */ import org.springframework.web.multipart.MultipartResolver;
/*      */ import org.springframework.web.util.NestedServletException;
/*      */ import org.springframework.web.util.WebUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DispatcherServlet
/*      */   extends FrameworkServlet
/*      */ {
/*      */   public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";
/*      */   public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";
/*      */   public static final String THEME_RESOLVER_BEAN_NAME = "themeResolver";
/*      */   public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
/*      */   public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
/*      */   public static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";
/*      */   public static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";
/*      */   public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";
/*      */   public static final String FLASH_MAP_MANAGER_BEAN_NAME = "flashMapManager";
/*  216 */   public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  222 */   public static final String LOCALE_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".LOCALE_RESOLVER";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  228 */   public static final String THEME_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_RESOLVER";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  234 */   public static final String THEME_SOURCE_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_SOURCE";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  241 */   public static final String INPUT_FLASH_MAP_ATTRIBUTE = DispatcherServlet.class.getName() + ".INPUT_FLASH_MAP";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  248 */   public static final String OUTPUT_FLASH_MAP_ATTRIBUTE = DispatcherServlet.class.getName() + ".OUTPUT_FLASH_MAP";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  254 */   public static final String FLASH_MAP_MANAGER_ATTRIBUTE = DispatcherServlet.class.getName() + ".FLASH_MAP_MANAGER";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  261 */   public static final String EXCEPTION_ATTRIBUTE = DispatcherServlet.class.getName() + ".EXCEPTION";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DEFAULT_STRATEGIES_PREFIX = "org.springframework.web.servlet";
/*      */ 
/*      */ 
/*      */   
/*  278 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*      */ 
/*      */   
/*      */   private static final Properties defaultStrategies;
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*      */     try {
/*  287 */       ClassPathResource resource = new ClassPathResource("DispatcherServlet.properties", DispatcherServlet.class);
/*  288 */       defaultStrategies = PropertiesLoaderUtils.loadProperties((Resource)resource);
/*      */     }
/*  290 */     catch (IOException ex) {
/*  291 */       throw new IllegalStateException("Could not load 'DispatcherServlet.properties': " + ex.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean detectAllHandlerMappings = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean detectAllHandlerAdapters = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean detectAllHandlerExceptionResolvers = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean detectAllViewResolvers = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean throwExceptionIfNoHandlerFound = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean cleanupAfterInclude = true;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private MultipartResolver multipartResolver;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private LocaleResolver localeResolver;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private ThemeResolver themeResolver;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private List<HandlerMapping> handlerMappings;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private List<HandlerAdapter> handlerAdapters;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private List<HandlerExceptionResolver> handlerExceptionResolvers;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private RequestToViewNameTranslator viewNameTranslator;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private FlashMapManager flashMapManager;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private List<ViewResolver> viewResolvers;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DispatcherServlet() {
/*  369 */     setDispatchOptionsRequest(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DispatcherServlet(WebApplicationContext webApplicationContext) {
/*  412 */     super(webApplicationContext);
/*  413 */     setDispatchOptionsRequest(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDetectAllHandlerMappings(boolean detectAllHandlerMappings) {
/*  424 */     this.detectAllHandlerMappings = detectAllHandlerMappings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDetectAllHandlerAdapters(boolean detectAllHandlerAdapters) {
/*  434 */     this.detectAllHandlerAdapters = detectAllHandlerAdapters;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDetectAllHandlerExceptionResolvers(boolean detectAllHandlerExceptionResolvers) {
/*  444 */     this.detectAllHandlerExceptionResolvers = detectAllHandlerExceptionResolvers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDetectAllViewResolvers(boolean detectAllViewResolvers) {
/*  454 */     this.detectAllViewResolvers = detectAllViewResolvers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setThrowExceptionIfNoHandlerFound(boolean throwExceptionIfNoHandlerFound) {
/*  469 */     this.throwExceptionIfNoHandlerFound = throwExceptionIfNoHandlerFound;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCleanupAfterInclude(boolean cleanupAfterInclude) {
/*  485 */     this.cleanupAfterInclude = cleanupAfterInclude;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onRefresh(ApplicationContext context) {
/*  494 */     initStrategies(context);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initStrategies(ApplicationContext context) {
/*  502 */     initMultipartResolver(context);
/*  503 */     initLocaleResolver(context);
/*  504 */     initThemeResolver(context);
/*  505 */     initHandlerMappings(context);
/*  506 */     initHandlerAdapters(context);
/*  507 */     initHandlerExceptionResolvers(context);
/*  508 */     initRequestToViewNameTranslator(context);
/*  509 */     initViewResolvers(context);
/*  510 */     initFlashMapManager(context);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initMultipartResolver(ApplicationContext context) {
/*      */     try {
/*  520 */       this.multipartResolver = (MultipartResolver)context.getBean("multipartResolver", MultipartResolver.class);
/*  521 */       if (this.logger.isTraceEnabled()) {
/*  522 */         this.logger.trace("Detected " + this.multipartResolver);
/*      */       }
/*  524 */       else if (this.logger.isDebugEnabled()) {
/*  525 */         this.logger.debug("Detected " + this.multipartResolver.getClass().getSimpleName());
/*      */       }
/*      */     
/*  528 */     } catch (NoSuchBeanDefinitionException ex) {
/*      */       
/*  530 */       this.multipartResolver = null;
/*  531 */       if (this.logger.isTraceEnabled()) {
/*  532 */         this.logger.trace("No MultipartResolver 'multipartResolver' declared");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initLocaleResolver(ApplicationContext context) {
/*      */     try {
/*  544 */       this.localeResolver = (LocaleResolver)context.getBean("localeResolver", LocaleResolver.class);
/*  545 */       if (this.logger.isTraceEnabled()) {
/*  546 */         this.logger.trace("Detected " + this.localeResolver);
/*      */       }
/*  548 */       else if (this.logger.isDebugEnabled()) {
/*  549 */         this.logger.debug("Detected " + this.localeResolver.getClass().getSimpleName());
/*      */       }
/*      */     
/*  552 */     } catch (NoSuchBeanDefinitionException ex) {
/*      */       
/*  554 */       this.localeResolver = getDefaultStrategy(context, LocaleResolver.class);
/*  555 */       if (this.logger.isTraceEnabled()) {
/*  556 */         this.logger.trace("No LocaleResolver 'localeResolver': using default [" + this.localeResolver
/*  557 */             .getClass().getSimpleName() + "]");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initThemeResolver(ApplicationContext context) {
/*      */     try {
/*  569 */       this.themeResolver = (ThemeResolver)context.getBean("themeResolver", ThemeResolver.class);
/*  570 */       if (this.logger.isTraceEnabled()) {
/*  571 */         this.logger.trace("Detected " + this.themeResolver);
/*      */       }
/*  573 */       else if (this.logger.isDebugEnabled()) {
/*  574 */         this.logger.debug("Detected " + this.themeResolver.getClass().getSimpleName());
/*      */       }
/*      */     
/*  577 */     } catch (NoSuchBeanDefinitionException ex) {
/*      */       
/*  579 */       this.themeResolver = getDefaultStrategy(context, ThemeResolver.class);
/*  580 */       if (this.logger.isTraceEnabled()) {
/*  581 */         this.logger.trace("No ThemeResolver 'themeResolver': using default [" + this.themeResolver
/*  582 */             .getClass().getSimpleName() + "]");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initHandlerMappings(ApplicationContext context) {
/*  593 */     this.handlerMappings = null;
/*      */     
/*  595 */     if (this.detectAllHandlerMappings) {
/*      */ 
/*      */       
/*  598 */       Map<String, HandlerMapping> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)context, HandlerMapping.class, true, false);
/*  599 */       if (!matchingBeans.isEmpty()) {
/*  600 */         this.handlerMappings = new ArrayList<>(matchingBeans.values());
/*      */         
/*  602 */         AnnotationAwareOrderComparator.sort(this.handlerMappings);
/*      */       } 
/*      */     } else {
/*      */       
/*      */       try {
/*  607 */         HandlerMapping hm = (HandlerMapping)context.getBean("handlerMapping", HandlerMapping.class);
/*  608 */         this.handlerMappings = Collections.singletonList(hm);
/*      */       }
/*  610 */       catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  617 */     if (this.handlerMappings == null) {
/*  618 */       this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
/*  619 */       if (this.logger.isTraceEnabled()) {
/*  620 */         this.logger.trace("No HandlerMappings declared for servlet '" + getServletName() + "': using default strategies from DispatcherServlet.properties");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initHandlerAdapters(ApplicationContext context) {
/*  632 */     this.handlerAdapters = null;
/*      */     
/*  634 */     if (this.detectAllHandlerAdapters) {
/*      */ 
/*      */       
/*  637 */       Map<String, HandlerAdapter> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)context, HandlerAdapter.class, true, false);
/*  638 */       if (!matchingBeans.isEmpty()) {
/*  639 */         this.handlerAdapters = new ArrayList<>(matchingBeans.values());
/*      */         
/*  641 */         AnnotationAwareOrderComparator.sort(this.handlerAdapters);
/*      */       } 
/*      */     } else {
/*      */       
/*      */       try {
/*  646 */         HandlerAdapter ha = (HandlerAdapter)context.getBean("handlerAdapter", HandlerAdapter.class);
/*  647 */         this.handlerAdapters = Collections.singletonList(ha);
/*      */       }
/*  649 */       catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  656 */     if (this.handlerAdapters == null) {
/*  657 */       this.handlerAdapters = getDefaultStrategies(context, HandlerAdapter.class);
/*  658 */       if (this.logger.isTraceEnabled()) {
/*  659 */         this.logger.trace("No HandlerAdapters declared for servlet '" + getServletName() + "': using default strategies from DispatcherServlet.properties");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initHandlerExceptionResolvers(ApplicationContext context) {
/*  671 */     this.handlerExceptionResolvers = null;
/*      */     
/*  673 */     if (this.detectAllHandlerExceptionResolvers) {
/*      */ 
/*      */       
/*  676 */       Map<String, HandlerExceptionResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)context, HandlerExceptionResolver.class, true, false);
/*  677 */       if (!matchingBeans.isEmpty()) {
/*  678 */         this.handlerExceptionResolvers = new ArrayList<>(matchingBeans.values());
/*      */         
/*  680 */         AnnotationAwareOrderComparator.sort(this.handlerExceptionResolvers);
/*      */       } 
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*  686 */         HandlerExceptionResolver her = (HandlerExceptionResolver)context.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
/*  687 */         this.handlerExceptionResolvers = Collections.singletonList(her);
/*      */       }
/*  689 */       catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  696 */     if (this.handlerExceptionResolvers == null) {
/*  697 */       this.handlerExceptionResolvers = getDefaultStrategies(context, HandlerExceptionResolver.class);
/*  698 */       if (this.logger.isTraceEnabled()) {
/*  699 */         this.logger.trace("No HandlerExceptionResolvers declared in servlet '" + getServletName() + "': using default strategies from DispatcherServlet.properties");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initRequestToViewNameTranslator(ApplicationContext context) {
/*      */     try {
/*  711 */       this
/*  712 */         .viewNameTranslator = (RequestToViewNameTranslator)context.getBean("viewNameTranslator", RequestToViewNameTranslator.class);
/*  713 */       if (this.logger.isTraceEnabled()) {
/*  714 */         this.logger.trace("Detected " + this.viewNameTranslator.getClass().getSimpleName());
/*      */       }
/*  716 */       else if (this.logger.isDebugEnabled()) {
/*  717 */         this.logger.debug("Detected " + this.viewNameTranslator);
/*      */       }
/*      */     
/*  720 */     } catch (NoSuchBeanDefinitionException ex) {
/*      */       
/*  722 */       this.viewNameTranslator = getDefaultStrategy(context, RequestToViewNameTranslator.class);
/*  723 */       if (this.logger.isTraceEnabled()) {
/*  724 */         this.logger.trace("No RequestToViewNameTranslator 'viewNameTranslator': using default [" + this.viewNameTranslator
/*  725 */             .getClass().getSimpleName() + "]");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initViewResolvers(ApplicationContext context) {
/*  736 */     this.viewResolvers = null;
/*      */     
/*  738 */     if (this.detectAllViewResolvers) {
/*      */ 
/*      */       
/*  741 */       Map<String, ViewResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)context, ViewResolver.class, true, false);
/*  742 */       if (!matchingBeans.isEmpty()) {
/*  743 */         this.viewResolvers = new ArrayList<>(matchingBeans.values());
/*      */         
/*  745 */         AnnotationAwareOrderComparator.sort(this.viewResolvers);
/*      */       } 
/*      */     } else {
/*      */       
/*      */       try {
/*  750 */         ViewResolver vr = (ViewResolver)context.getBean("viewResolver", ViewResolver.class);
/*  751 */         this.viewResolvers = Collections.singletonList(vr);
/*      */       }
/*  753 */       catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  760 */     if (this.viewResolvers == null) {
/*  761 */       this.viewResolvers = getDefaultStrategies(context, ViewResolver.class);
/*  762 */       if (this.logger.isTraceEnabled()) {
/*  763 */         this.logger.trace("No ViewResolvers declared for servlet '" + getServletName() + "': using default strategies from DispatcherServlet.properties");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initFlashMapManager(ApplicationContext context) {
/*      */     try {
/*  776 */       this.flashMapManager = (FlashMapManager)context.getBean("flashMapManager", FlashMapManager.class);
/*  777 */       if (this.logger.isTraceEnabled()) {
/*  778 */         this.logger.trace("Detected " + this.flashMapManager.getClass().getSimpleName());
/*      */       }
/*  780 */       else if (this.logger.isDebugEnabled()) {
/*  781 */         this.logger.debug("Detected " + this.flashMapManager);
/*      */       }
/*      */     
/*  784 */     } catch (NoSuchBeanDefinitionException ex) {
/*      */       
/*  786 */       this.flashMapManager = getDefaultStrategy(context, FlashMapManager.class);
/*  787 */       if (this.logger.isTraceEnabled()) {
/*  788 */         this.logger.trace("No FlashMapManager 'flashMapManager': using default [" + this.flashMapManager
/*  789 */             .getClass().getSimpleName() + "]");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public final ThemeSource getThemeSource() {
/*  803 */     return (getWebApplicationContext() instanceof ThemeSource) ? (ThemeSource)getWebApplicationContext() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public final MultipartResolver getMultipartResolver() {
/*  813 */     return this.multipartResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public final List<HandlerMapping> getHandlerMappings() {
/*  828 */     return (this.handlerMappings != null) ? Collections.<HandlerMapping>unmodifiableList(this.handlerMappings) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <T> T getDefaultStrategy(ApplicationContext context, Class<T> strategyInterface) {
/*  841 */     List<T> strategies = getDefaultStrategies(context, strategyInterface);
/*  842 */     if (strategies.size() != 1) {
/*  843 */       throw new BeanInitializationException("DispatcherServlet needs exactly 1 strategy for interface [" + strategyInterface
/*  844 */           .getName() + "]");
/*      */     }
/*  846 */     return strategies.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {
/*  860 */     String key = strategyInterface.getName();
/*  861 */     String value = defaultStrategies.getProperty(key);
/*  862 */     if (value != null) {
/*  863 */       String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
/*  864 */       List<T> strategies = new ArrayList<>(classNames.length);
/*  865 */       for (String className : classNames) {
/*      */         try {
/*  867 */           Class<?> clazz = ClassUtils.forName(className, DispatcherServlet.class.getClassLoader());
/*  868 */           Object strategy = createDefaultStrategy(context, clazz);
/*  869 */           strategies.add((T)strategy);
/*      */         }
/*  871 */         catch (ClassNotFoundException ex) {
/*  872 */           throw new BeanInitializationException("Could not find DispatcherServlet's default strategy class [" + className + "] for interface [" + key + "]", ex);
/*      */ 
/*      */         
/*      */         }
/*  876 */         catch (LinkageError err) {
/*  877 */           throw new BeanInitializationException("Unresolvable class definition for DispatcherServlet's default strategy class [" + className + "] for interface [" + key + "]", err);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  882 */       return strategies;
/*      */     } 
/*      */     
/*  885 */     return new LinkedList<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object createDefaultStrategy(ApplicationContext context, Class<?> clazz) {
/*  900 */     return context.getAutowireCapableBeanFactory().createBean(clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  910 */     logRequest(request);
/*      */ 
/*      */ 
/*      */     
/*  914 */     Map<String, Object> attributesSnapshot = null;
/*  915 */     if (WebUtils.isIncludeRequest((ServletRequest)request)) {
/*  916 */       attributesSnapshot = new HashMap<>();
/*  917 */       Enumeration<?> attrNames = request.getAttributeNames();
/*  918 */       while (attrNames.hasMoreElements()) {
/*  919 */         String attrName = (String)attrNames.nextElement();
/*  920 */         if (this.cleanupAfterInclude || attrName.startsWith("org.springframework.web.servlet")) {
/*  921 */           attributesSnapshot.put(attrName, request.getAttribute(attrName));
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  927 */     request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
/*  928 */     request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
/*  929 */     request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
/*  930 */     request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());
/*      */     
/*  932 */     if (this.flashMapManager != null) {
/*  933 */       FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response);
/*  934 */       if (inputFlashMap != null) {
/*  935 */         request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
/*      */       }
/*  937 */       request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
/*  938 */       request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);
/*      */     } 
/*      */     
/*      */     try {
/*  942 */       doDispatch(request, response);
/*      */     } finally {
/*      */       
/*  945 */       if (!WebAsyncUtils.getAsyncManager((ServletRequest)request).isConcurrentHandlingStarted())
/*      */       {
/*  947 */         if (attributesSnapshot != null) {
/*  948 */           restoreAttributesAfterInclude(request, attributesSnapshot);
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void logRequest(HttpServletRequest request) {
/*  955 */     LogFormatUtils.traceDebug(this.logger, traceOn -> {
/*      */           String params;
/*      */           if (isEnableLoggingRequestDetails()) {
/*      */             params = request.getParameterMap().entrySet().stream().map(()).collect(Collectors.joining(", "));
/*      */           } else {
/*      */             params = request.getParameterMap().isEmpty() ? "" : "masked";
/*      */           } 
/*      */           String queryString = request.getQueryString();
/*      */           String queryClause = StringUtils.hasLength(queryString) ? ("?" + queryString) : "";
/*      */           String dispatchType = !request.getDispatcherType().equals(DispatcherType.REQUEST) ? ("\"" + request.getDispatcherType().name() + "\" dispatch for ") : "";
/*      */           String message = dispatchType + request.getMethod() + " \"" + getRequestUri(request) + queryClause + "\", parameters={" + params + "}";
/*      */           if (traceOn.booleanValue()) {
/*      */             List<String> values = Collections.list(request.getHeaderNames());
/*      */             String headers = (values.size() > 0) ? "masked" : "";
/*      */             if (isEnableLoggingRequestDetails()) {
/*      */               headers = values.stream().map(()).collect(Collectors.joining(", "));
/*      */             }
/*      */             return message + ", headers={" + headers + "} in DispatcherServlet '" + getServletName() + "'";
/*      */           } 
/*      */           return message;
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 1000 */     HttpServletRequest processedRequest = request;
/* 1001 */     HandlerExecutionChain mappedHandler = null;
/* 1002 */     boolean multipartRequestParsed = false;
/*      */     
/* 1004 */     WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager((ServletRequest)request);
/*      */     try {
/*      */       NestedServletException nestedServletException;
/* 1007 */       ModelAndView mv = null;
/* 1008 */       Exception dispatchException = null;
/*      */       
/*      */       try {
/* 1011 */         processedRequest = checkMultipart(request);
/* 1012 */         multipartRequestParsed = (processedRequest != request);
/*      */ 
/*      */         
/* 1015 */         mappedHandler = getHandler(processedRequest);
/* 1016 */         if (mappedHandler == null) {
/* 1017 */           noHandlerFound(processedRequest, response);
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 1022 */         HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
/*      */ 
/*      */         
/* 1025 */         String method = request.getMethod();
/* 1026 */         boolean isGet = "GET".equals(method);
/*      */         
/* 1028 */         long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
/* 1029 */         if ((isGet || "HEAD".equals(method)) && (new ServletWebRequest(request, response)).checkNotModified(lastModified) && isGet) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 1034 */         if (!mappedHandler.applyPreHandle(processedRequest, response)) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 1039 */         mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
/*      */         
/* 1041 */         if (asyncManager.isConcurrentHandlingStarted()) {
/*      */           return;
/*      */         }
/*      */         
/* 1045 */         applyDefaultViewName(processedRequest, mv);
/* 1046 */         mappedHandler.applyPostHandle(processedRequest, response, mv);
/*      */       }
/* 1048 */       catch (Exception ex) {
/* 1049 */         dispatchException = ex;
/*      */       }
/* 1051 */       catch (Throwable err) {
/*      */ 
/*      */         
/* 1054 */         nestedServletException = new NestedServletException("Handler dispatch failed", err);
/*      */       } 
/* 1056 */       processDispatchResult(processedRequest, response, mappedHandler, mv, (Exception)nestedServletException);
/*      */     }
/* 1058 */     catch (Exception ex) {
/* 1059 */       triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
/*      */     }
/* 1061 */     catch (Throwable err) {
/* 1062 */       triggerAfterCompletion(processedRequest, response, mappedHandler, (Exception)new NestedServletException("Handler processing failed", err));
/*      */     }
/*      */     finally {
/*      */       
/* 1066 */       if (asyncManager.isConcurrentHandlingStarted()) {
/*      */         
/* 1068 */         if (mappedHandler != null) {
/* 1069 */           mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1074 */       else if (multipartRequestParsed) {
/* 1075 */         cleanupMultipart(processedRequest);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void applyDefaultViewName(HttpServletRequest request, @Nullable ModelAndView mv) throws Exception {
/* 1085 */     if (mv != null && !mv.hasView()) {
/* 1086 */       String defaultViewName = getDefaultViewName(request);
/* 1087 */       if (defaultViewName != null) {
/* 1088 */         mv.setViewName(defaultViewName);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv, @Nullable Exception exception) throws Exception {
/* 1101 */     boolean errorView = false;
/*      */     
/* 1103 */     if (exception != null) {
/* 1104 */       if (exception instanceof ModelAndViewDefiningException) {
/* 1105 */         this.logger.debug("ModelAndViewDefiningException encountered", exception);
/* 1106 */         mv = ((ModelAndViewDefiningException)exception).getModelAndView();
/*      */       } else {
/*      */         
/* 1109 */         Object handler = (mappedHandler != null) ? mappedHandler.getHandler() : null;
/* 1110 */         mv = processHandlerException(request, response, handler, exception);
/* 1111 */         errorView = (mv != null);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1116 */     if (mv != null && !mv.wasCleared()) {
/* 1117 */       render(mv, request, response);
/* 1118 */       if (errorView) {
/* 1119 */         WebUtils.clearErrorRequestAttributes(request);
/*      */       
/*      */       }
/*      */     }
/* 1123 */     else if (this.logger.isTraceEnabled()) {
/* 1124 */       this.logger.trace("No view rendering, null ModelAndView returned.");
/*      */     } 
/*      */ 
/*      */     
/* 1128 */     if (WebAsyncUtils.getAsyncManager((ServletRequest)request).isConcurrentHandlingStarted()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1133 */     if (mappedHandler != null) {
/* 1134 */       mappedHandler.triggerAfterCompletion(request, response, null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected LocaleContext buildLocaleContext(HttpServletRequest request) {
/* 1147 */     LocaleResolver lr = this.localeResolver;
/* 1148 */     if (lr instanceof LocaleContextResolver) {
/* 1149 */       return ((LocaleContextResolver)lr).resolveLocaleContext(request);
/*      */     }
/*      */     
/* 1152 */     return () -> (lr != null) ? lr.resolveLocale(request) : request.getLocale();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HttpServletRequest checkMultipart(HttpServletRequest request) throws MultipartException {
/* 1164 */     if (this.multipartResolver != null && this.multipartResolver.isMultipart(request)) {
/* 1165 */       if (WebUtils.getNativeRequest((ServletRequest)request, MultipartHttpServletRequest.class) != null) {
/* 1166 */         if (request.getDispatcherType().equals(DispatcherType.REQUEST)) {
/* 1167 */           this.logger.trace("Request already resolved to MultipartHttpServletRequest, e.g. by MultipartFilter");
/*      */         }
/*      */       }
/* 1170 */       else if (hasMultipartException(request)) {
/* 1171 */         this.logger.debug("Multipart resolution previously failed for current request - skipping re-resolution for undisturbed error rendering");
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/* 1176 */           return (HttpServletRequest)this.multipartResolver.resolveMultipart(request);
/*      */         }
/* 1178 */         catch (MultipartException ex) {
/* 1179 */           if (request.getAttribute("javax.servlet.error.exception") != null) {
/* 1180 */             this.logger.debug("Multipart resolution failed for error dispatch", (Throwable)ex);
/*      */           }
/*      */           else {
/*      */             
/* 1184 */             throw ex;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 1190 */     return request;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasMultipartException(HttpServletRequest request) {
/* 1197 */     Throwable error = (Throwable)request.getAttribute("javax.servlet.error.exception");
/* 1198 */     while (error != null) {
/* 1199 */       if (error instanceof MultipartException) {
/* 1200 */         return true;
/*      */       }
/* 1202 */       error = error.getCause();
/*      */     } 
/* 1204 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void cleanupMultipart(HttpServletRequest request) {
/* 1213 */     if (this.multipartResolver != null) {
/*      */       
/* 1215 */       MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)WebUtils.getNativeRequest((ServletRequest)request, MultipartHttpServletRequest.class);
/* 1216 */       if (multipartRequest != null) {
/* 1217 */         this.multipartResolver.cleanupMultipart(multipartRequest);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
/* 1230 */     if (this.handlerMappings != null) {
/* 1231 */       for (HandlerMapping mapping : this.handlerMappings) {
/* 1232 */         HandlerExecutionChain handler = mapping.getHandler(request);
/* 1233 */         if (handler != null) {
/* 1234 */           return handler;
/*      */         }
/*      */       } 
/*      */     }
/* 1238 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 1248 */     if (pageNotFoundLogger.isWarnEnabled()) {
/* 1249 */       pageNotFoundLogger.warn("No mapping for " + request.getMethod() + " " + getRequestUri(request));
/*      */     }
/* 1251 */     if (this.throwExceptionIfNoHandlerFound) {
/* 1252 */       throw new NoHandlerFoundException(request.getMethod(), getRequestUri(request), (new ServletServerHttpRequest(request))
/* 1253 */           .getHeaders());
/*      */     }
/*      */     
/* 1256 */     response.sendError(404);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
/* 1266 */     if (this.handlerAdapters != null) {
/* 1267 */       for (HandlerAdapter adapter : this.handlerAdapters) {
/* 1268 */         if (adapter.supports(handler)) {
/* 1269 */           return adapter;
/*      */         }
/*      */       } 
/*      */     }
/* 1273 */     throw new ServletException("No adapter for handler [" + handler + "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) throws Exception {
/* 1292 */     request.removeAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
/*      */ 
/*      */     
/* 1295 */     ModelAndView exMv = null;
/* 1296 */     if (this.handlerExceptionResolvers != null) {
/* 1297 */       for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
/* 1298 */         exMv = resolver.resolveException(request, response, handler, ex);
/* 1299 */         if (exMv != null) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/* 1304 */     if (exMv != null) {
/* 1305 */       if (exMv.isEmpty()) {
/* 1306 */         request.setAttribute(EXCEPTION_ATTRIBUTE, ex);
/* 1307 */         return null;
/*      */       } 
/*      */       
/* 1310 */       if (!exMv.hasView()) {
/* 1311 */         String defaultViewName = getDefaultViewName(request);
/* 1312 */         if (defaultViewName != null) {
/* 1313 */           exMv.setViewName(defaultViewName);
/*      */         }
/*      */       } 
/* 1316 */       if (this.logger.isTraceEnabled()) {
/* 1317 */         this.logger.trace("Using resolved error view: " + exMv, ex);
/*      */       }
/* 1319 */       if (this.logger.isDebugEnabled()) {
/* 1320 */         this.logger.debug("Using resolved error view: " + exMv);
/*      */       }
/* 1322 */       WebUtils.exposeErrorRequestAttributes(request, ex, getServletName());
/* 1323 */       return exMv;
/*      */     } 
/*      */     
/* 1326 */     throw ex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
/*      */     View view;
/* 1341 */     Locale locale = (this.localeResolver != null) ? this.localeResolver.resolveLocale(request) : request.getLocale();
/* 1342 */     response.setLocale(locale);
/*      */ 
/*      */     
/* 1345 */     String viewName = mv.getViewName();
/* 1346 */     if (viewName != null) {
/*      */       
/* 1348 */       view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
/* 1349 */       if (view == null) {
/* 1350 */         throw new ServletException("Could not resolve view with name '" + mv.getViewName() + "' in servlet with name '" + 
/* 1351 */             getServletName() + "'");
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1356 */       view = mv.getView();
/* 1357 */       if (view == null) {
/* 1358 */         throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a View object in servlet with name '" + 
/* 1359 */             getServletName() + "'");
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1364 */     if (this.logger.isTraceEnabled()) {
/* 1365 */       this.logger.trace("Rendering view [" + view + "] ");
/*      */     }
/*      */     try {
/* 1368 */       if (mv.getStatus() != null) {
/* 1369 */         response.setStatus(mv.getStatus().value());
/*      */       }
/* 1371 */       view.render(mv.getModelInternal(), request, response);
/*      */     }
/* 1373 */     catch (Exception ex) {
/* 1374 */       if (this.logger.isDebugEnabled()) {
/* 1375 */         this.logger.debug("Error rendering view [" + view + "]", ex);
/*      */       }
/* 1377 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected String getDefaultViewName(HttpServletRequest request) throws Exception {
/* 1389 */     return (this.viewNameTranslator != null) ? this.viewNameTranslator.getViewName(request) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected View resolveViewName(String viewName, @Nullable Map<String, Object> model, Locale locale, HttpServletRequest request) throws Exception {
/* 1410 */     if (this.viewResolvers != null) {
/* 1411 */       for (ViewResolver viewResolver : this.viewResolvers) {
/* 1412 */         View view = viewResolver.resolveViewName(viewName, locale);
/* 1413 */         if (view != null) {
/* 1414 */           return view;
/*      */         }
/*      */       } 
/*      */     }
/* 1418 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, @Nullable HandlerExecutionChain mappedHandler, Exception ex) throws Exception {
/* 1424 */     if (mappedHandler != null) {
/* 1425 */       mappedHandler.triggerAfterCompletion(request, response, ex);
/*      */     }
/* 1427 */     throw ex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void restoreAttributesAfterInclude(HttpServletRequest request, Map<?, ?> attributesSnapshot) {
/* 1439 */     Set<String> attrsToCheck = new HashSet<>();
/* 1440 */     Enumeration<?> attrNames = request.getAttributeNames();
/* 1441 */     while (attrNames.hasMoreElements()) {
/* 1442 */       String attrName = (String)attrNames.nextElement();
/* 1443 */       if (this.cleanupAfterInclude || attrName.startsWith("org.springframework.web.servlet")) {
/* 1444 */         attrsToCheck.add(attrName);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1449 */     attrsToCheck.addAll((Collection)attributesSnapshot.keySet());
/*      */ 
/*      */ 
/*      */     
/* 1453 */     for (String attrName : attrsToCheck) {
/* 1454 */       Object attrValue = attributesSnapshot.get(attrName);
/* 1455 */       if (attrValue == null) {
/* 1456 */         request.removeAttribute(attrName); continue;
/*      */       } 
/* 1458 */       if (attrValue != request.getAttribute(attrName)) {
/* 1459 */         request.setAttribute(attrName, attrValue);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String getRequestUri(HttpServletRequest request) {
/* 1465 */     String uri = (String)request.getAttribute("javax.servlet.include.request_uri");
/* 1466 */     if (uri == null) {
/* 1467 */       uri = request.getRequestURI();
/*      */     }
/* 1469 */     return uri;
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/DispatcherServlet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */