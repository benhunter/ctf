/*     */ package org.springframework.web.servlet.view.tiles3;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.el.ArrayELResolver;
/*     */ import javax.el.BeanELResolver;
/*     */ import javax.el.CompositeELResolver;
/*     */ import javax.el.ELResolver;
/*     */ import javax.el.ListELResolver;
/*     */ import javax.el.MapELResolver;
/*     */ import javax.el.ResourceBundleELResolver;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.jsp.JspFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.tiles.TilesContainer;
/*     */ import org.apache.tiles.TilesException;
/*     */ import org.apache.tiles.definition.DefinitionsFactory;
/*     */ import org.apache.tiles.definition.DefinitionsReader;
/*     */ import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
/*     */ import org.apache.tiles.definition.dao.CachingLocaleUrlDefinitionDAO;
/*     */ import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
/*     */ import org.apache.tiles.el.ELAttributeEvaluator;
/*     */ import org.apache.tiles.el.ScopeELResolver;
/*     */ import org.apache.tiles.el.TilesContextBeanELResolver;
/*     */ import org.apache.tiles.el.TilesContextELResolver;
/*     */ import org.apache.tiles.evaluator.AttributeEvaluator;
/*     */ import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
/*     */ import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
/*     */ import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
/*     */ import org.apache.tiles.extras.complete.CompleteAutoloadTilesContainerFactory;
/*     */ import org.apache.tiles.extras.complete.CompleteAutoloadTilesInitializer;
/*     */ import org.apache.tiles.factory.AbstractTilesContainerFactory;
/*     */ import org.apache.tiles.factory.BasicTilesContainerFactory;
/*     */ import org.apache.tiles.impl.mgmt.CachingTilesContainer;
/*     */ import org.apache.tiles.locale.LocaleResolver;
/*     */ import org.apache.tiles.preparer.factory.PreparerFactory;
/*     */ import org.apache.tiles.request.ApplicationContext;
/*     */ import org.apache.tiles.request.ApplicationContextAware;
/*     */ import org.apache.tiles.request.ApplicationResource;
/*     */ import org.apache.tiles.startup.DefaultTilesInitializer;
/*     */ import org.apache.tiles.startup.TilesInitializer;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TilesConfigurer
/*     */   implements ServletContextAware, InitializingBean, DisposableBean
/*     */ {
/* 130 */   private static final boolean tilesElPresent = ClassUtils.isPresent("org.apache.tiles.el.ELAttributeEvaluator", TilesConfigurer.class.getClassLoader());
/*     */ 
/*     */   
/* 133 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TilesInitializer tilesInitializer;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String[] definitions;
/*     */ 
/*     */   
/*     */   private boolean checkRefresh = false;
/*     */ 
/*     */   
/*     */   private boolean validateDefinitions = true;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Class<? extends DefinitionsFactory> definitionsFactoryClass;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Class<? extends PreparerFactory> preparerFactoryClass;
/*     */ 
/*     */   
/*     */   private boolean useMutableTilesContainer = false;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ServletContext servletContext;
/*     */ 
/*     */   
/*     */   public void setTilesInitializer(TilesInitializer tilesInitializer) {
/* 166 */     this.tilesInitializer = tilesInitializer;
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
/*     */   public void setCompleteAutoload(boolean completeAutoload) {
/* 180 */     if (completeAutoload) {
/*     */       try {
/* 182 */         this.tilesInitializer = (TilesInitializer)new SpringCompleteAutoloadTilesInitializer();
/*     */       }
/* 184 */       catch (Throwable ex) {
/* 185 */         throw new IllegalStateException("Tiles-Extras 3.0 not available", ex);
/*     */       } 
/*     */     } else {
/*     */       
/* 189 */       this.tilesInitializer = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefinitions(String... definitions) {
/* 198 */     this.definitions = definitions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCheckRefresh(boolean checkRefresh) {
/* 206 */     this.checkRefresh = checkRefresh;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidateDefinitions(boolean validateDefinitions) {
/* 213 */     this.validateDefinitions = validateDefinitions;
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
/*     */   public void setDefinitionsFactoryClass(Class<? extends DefinitionsFactory> definitionsFactoryClass) {
/* 226 */     this.definitionsFactoryClass = definitionsFactoryClass;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreparerFactoryClass(Class<? extends PreparerFactory> preparerFactoryClass) {
/* 249 */     this.preparerFactoryClass = preparerFactoryClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseMutableTilesContainer(boolean useMutableTilesContainer) {
/* 259 */     this.useMutableTilesContainer = useMutableTilesContainer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/* 264 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws TilesException {
/* 274 */     Assert.state((this.servletContext != null), "No ServletContext available");
/* 275 */     SpringWildcardServletTilesApplicationContext springWildcardServletTilesApplicationContext = new SpringWildcardServletTilesApplicationContext(this.servletContext);
/* 276 */     if (this.tilesInitializer == null) {
/* 277 */       this.tilesInitializer = (TilesInitializer)new SpringTilesInitializer();
/*     */     }
/* 279 */     this.tilesInitializer.initialize((ApplicationContext)springWildcardServletTilesApplicationContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws TilesException {
/* 288 */     if (this.tilesInitializer != null)
/* 289 */       this.tilesInitializer.destroy(); 
/*     */   }
/*     */   
/*     */   private class SpringTilesInitializer
/*     */     extends DefaultTilesInitializer
/*     */   {
/*     */     private SpringTilesInitializer() {}
/*     */     
/*     */     protected AbstractTilesContainerFactory createContainerFactory(ApplicationContext context) {
/* 298 */       return (AbstractTilesContainerFactory)new TilesConfigurer.SpringTilesContainerFactory();
/*     */     }
/*     */   }
/*     */   
/*     */   private class SpringTilesContainerFactory
/*     */     extends BasicTilesContainerFactory {
/*     */     private SpringTilesContainerFactory() {}
/*     */     
/*     */     protected TilesContainer createDecoratedContainer(TilesContainer originalContainer, ApplicationContext context) {
/* 307 */       return TilesConfigurer.this.useMutableTilesContainer ? (TilesContainer)new CachingTilesContainer(originalContainer) : originalContainer;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<ApplicationResource> getSources(ApplicationContext applicationContext) {
/* 312 */       if (TilesConfigurer.this.definitions != null) {
/* 313 */         List<ApplicationResource> result = new LinkedList<>();
/* 314 */         for (String definition : TilesConfigurer.this.definitions) {
/* 315 */           Collection<ApplicationResource> resources = applicationContext.getResources(definition);
/* 316 */           if (resources != null) {
/* 317 */             result.addAll(resources);
/*     */           }
/*     */         } 
/* 320 */         return result;
/*     */       } 
/*     */       
/* 323 */       return super.getSources(applicationContext);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(ApplicationContext applicationContext, LocaleResolver resolver) {
/* 330 */       BaseLocaleUrlDefinitionDAO dao = super.instantiateLocaleDefinitionDao(applicationContext, resolver);
/* 331 */       if (TilesConfigurer.this.checkRefresh && dao instanceof CachingLocaleUrlDefinitionDAO) {
/* 332 */         ((CachingLocaleUrlDefinitionDAO)dao).setCheckRefresh(true);
/*     */       }
/* 334 */       return dao;
/*     */     }
/*     */ 
/*     */     
/*     */     protected DefinitionsReader createDefinitionsReader(ApplicationContext context) {
/* 339 */       DigesterDefinitionsReader reader = (DigesterDefinitionsReader)super.createDefinitionsReader(context);
/* 340 */       reader.setValidating(TilesConfigurer.this.validateDefinitions);
/* 341 */       return (DefinitionsReader)reader;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected DefinitionsFactory createDefinitionsFactory(ApplicationContext applicationContext, LocaleResolver resolver) {
/* 348 */       if (TilesConfigurer.this.definitionsFactoryClass != null) {
/* 349 */         DefinitionsFactory factory = (DefinitionsFactory)BeanUtils.instantiateClass(TilesConfigurer.this.definitionsFactoryClass);
/* 350 */         if (factory instanceof ApplicationContextAware) {
/* 351 */           ((ApplicationContextAware)factory).setApplicationContext(applicationContext);
/*     */         }
/* 353 */         BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(factory);
/* 354 */         if (bw.isWritableProperty("localeResolver")) {
/* 355 */           bw.setPropertyValue("localeResolver", resolver);
/*     */         }
/* 357 */         if (bw.isWritableProperty("definitionDAO")) {
/* 358 */           bw.setPropertyValue("definitionDAO", createLocaleDefinitionDao(applicationContext, resolver));
/*     */         }
/* 360 */         return factory;
/*     */       } 
/*     */       
/* 363 */       return super.createDefinitionsFactory(applicationContext, resolver);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected PreparerFactory createPreparerFactory(ApplicationContext context) {
/* 369 */       if (TilesConfigurer.this.preparerFactoryClass != null) {
/* 370 */         return (PreparerFactory)BeanUtils.instantiateClass(TilesConfigurer.this.preparerFactoryClass);
/*     */       }
/*     */       
/* 373 */       return super.createPreparerFactory(context);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected LocaleResolver createLocaleResolver(ApplicationContext context) {
/* 379 */       return (LocaleResolver)new SpringLocaleResolver();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(ApplicationContext context, LocaleResolver resolver) {
/*     */       DirectAttributeEvaluator directAttributeEvaluator;
/* 386 */       if (TilesConfigurer.tilesElPresent && JspFactory.getDefaultFactory() != null) {
/* 387 */         AttributeEvaluator evaluator = (new TilesConfigurer.TilesElActivator()).createEvaluator();
/*     */       } else {
/*     */         
/* 390 */         directAttributeEvaluator = new DirectAttributeEvaluator();
/*     */       } 
/* 392 */       return (AttributeEvaluatorFactory)new BasicAttributeEvaluatorFactory((AttributeEvaluator)directAttributeEvaluator);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpringCompleteAutoloadTilesInitializer
/*     */     extends CompleteAutoloadTilesInitializer {
/*     */     private SpringCompleteAutoloadTilesInitializer() {}
/*     */     
/*     */     protected AbstractTilesContainerFactory createContainerFactory(ApplicationContext context) {
/* 401 */       return (AbstractTilesContainerFactory)new TilesConfigurer.SpringCompleteAutoloadTilesContainerFactory();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpringCompleteAutoloadTilesContainerFactory
/*     */     extends CompleteAutoloadTilesContainerFactory {
/*     */     private SpringCompleteAutoloadTilesContainerFactory() {}
/*     */     
/*     */     protected LocaleResolver createLocaleResolver(ApplicationContext applicationContext) {
/* 410 */       return (LocaleResolver)new SpringLocaleResolver();
/*     */     }
/*     */   }
/*     */   
/*     */   private class TilesElActivator {
/*     */     private TilesElActivator() {}
/*     */     
/*     */     public AttributeEvaluator createEvaluator() {
/* 418 */       ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
/* 419 */       evaluator.setExpressionFactory(
/* 420 */           JspFactory.getDefaultFactory().getJspApplicationContext(TilesConfigurer.this.servletContext).getExpressionFactory());
/* 421 */       evaluator.setResolver((ELResolver)new TilesConfigurer.CompositeELResolverImpl());
/* 422 */       return (AttributeEvaluator)evaluator;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CompositeELResolverImpl
/*     */     extends CompositeELResolver
/*     */   {
/*     */     public CompositeELResolverImpl() {
/* 430 */       add((ELResolver)new ScopeELResolver());
/* 431 */       add((ELResolver)new TilesContextELResolver((ELResolver)new TilesContextBeanELResolver()));
/* 432 */       add((ELResolver)new TilesContextBeanELResolver());
/* 433 */       add((ELResolver)new ArrayELResolver(false));
/* 434 */       add((ELResolver)new ListELResolver(false));
/* 435 */       add((ELResolver)new MapELResolver(false));
/* 436 */       add((ELResolver)new ResourceBundleELResolver());
/* 437 */       add((ELResolver)new BeanELResolver(false));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/tiles3/TilesConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */