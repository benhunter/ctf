/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.ViewResolver;
/*     */ import org.springframework.web.servlet.view.BeanNameViewResolver;
/*     */ import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
/*     */ import org.springframework.web.servlet.view.InternalResourceViewResolver;
/*     */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
/*     */ import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
/*     */ import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
/*     */ import org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer;
/*     */ import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
/*     */ import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer;
/*     */ import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver;
/*     */ import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
/*     */ import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ViewResolverRegistry
/*     */ {
/*     */   @Nullable
/*     */   private ContentNegotiationManager contentNegotiationManager;
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */   @Nullable
/*     */   private ContentNegotiatingViewResolver contentNegotiatingResolver;
/*  66 */   private final List<ViewResolver> viewResolvers = new ArrayList<>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Integer order;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ViewResolverRegistry(ContentNegotiationManager contentNegotiationManager, @Nullable ApplicationContext context) {
/*  79 */     this.contentNegotiationManager = contentNegotiationManager;
/*  80 */     this.applicationContext = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasRegistrations() {
/*  88 */     return (this.contentNegotiatingResolver != null || !this.viewResolvers.isEmpty());
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
/*     */   public void enableContentNegotiation(View... defaultViews) {
/* 100 */     initContentNegotiatingViewResolver(defaultViews);
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
/*     */   public void enableContentNegotiation(boolean useNotAcceptableStatus, View... defaultViews) {
/* 112 */     ContentNegotiatingViewResolver vr = initContentNegotiatingViewResolver(defaultViews);
/* 113 */     vr.setUseNotAcceptableStatusCode(useNotAcceptableStatus);
/*     */   }
/*     */ 
/*     */   
/*     */   private ContentNegotiatingViewResolver initContentNegotiatingViewResolver(View[] defaultViews) {
/* 118 */     this.order = Integer.valueOf((this.order != null) ? this.order.intValue() : Integer.MIN_VALUE);
/*     */     
/* 120 */     if (this.contentNegotiatingResolver != null) {
/* 121 */       if (!ObjectUtils.isEmpty((Object[])defaultViews) && 
/* 122 */         !CollectionUtils.isEmpty(this.contentNegotiatingResolver.getDefaultViews())) {
/* 123 */         List<View> views = new ArrayList<>(this.contentNegotiatingResolver.getDefaultViews());
/* 124 */         views.addAll(Arrays.asList(defaultViews));
/* 125 */         this.contentNegotiatingResolver.setDefaultViews(views);
/*     */       } 
/*     */     } else {
/*     */       
/* 129 */       this.contentNegotiatingResolver = new ContentNegotiatingViewResolver();
/* 130 */       this.contentNegotiatingResolver.setDefaultViews(Arrays.asList(defaultViews));
/* 131 */       this.contentNegotiatingResolver.setViewResolvers(this.viewResolvers);
/* 132 */       if (this.contentNegotiationManager != null) {
/* 133 */         this.contentNegotiatingResolver.setContentNegotiationManager(this.contentNegotiationManager);
/*     */       }
/*     */     } 
/* 136 */     return this.contentNegotiatingResolver;
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
/*     */   public UrlBasedViewResolverRegistration jsp() {
/* 149 */     return jsp("/WEB-INF/", ".jsp");
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
/*     */   public UrlBasedViewResolverRegistration jsp(String prefix, String suffix) {
/* 161 */     InternalResourceViewResolver resolver = new InternalResourceViewResolver();
/* 162 */     resolver.setPrefix(prefix);
/* 163 */     resolver.setSuffix(suffix);
/* 164 */     this.viewResolvers.add(resolver);
/* 165 */     return new UrlBasedViewResolverRegistration((UrlBasedViewResolver)resolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlBasedViewResolverRegistration tiles() {
/* 174 */     if (!checkBeanOfType(TilesConfigurer.class)) {
/* 175 */       throw new BeanInitializationException("In addition to a Tiles view resolver there must also be a single TilesConfigurer bean in this web application context (or its parent).");
/*     */     }
/*     */ 
/*     */     
/* 179 */     TilesRegistration registration = new TilesRegistration();
/* 180 */     this.viewResolvers.add(registration.getViewResolver());
/* 181 */     return registration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlBasedViewResolverRegistration freeMarker() {
/* 191 */     if (!checkBeanOfType(FreeMarkerConfigurer.class)) {
/* 192 */       throw new BeanInitializationException("In addition to a FreeMarker view resolver there must also be a single FreeMarkerConfig bean in this web application context (or its parent): FreeMarkerConfigurer is the usual implementation. This bean may be given any name.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 197 */     FreeMarkerRegistration registration = new FreeMarkerRegistration();
/* 198 */     this.viewResolvers.add(registration.getViewResolver());
/* 199 */     return registration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlBasedViewResolverRegistration groovy() {
/* 207 */     if (!checkBeanOfType(GroovyMarkupConfigurer.class)) {
/* 208 */       throw new BeanInitializationException("In addition to a Groovy markup view resolver there must also be a single GroovyMarkupConfig bean in this web application context (or its parent): GroovyMarkupConfigurer is the usual implementation. This bean may be given any name.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 213 */     GroovyMarkupRegistration registration = new GroovyMarkupRegistration();
/* 214 */     this.viewResolvers.add(registration.getViewResolver());
/* 215 */     return registration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlBasedViewResolverRegistration scriptTemplate() {
/* 223 */     if (!checkBeanOfType(ScriptTemplateConfigurer.class)) {
/* 224 */       throw new BeanInitializationException("In addition to a script template view resolver there must also be a single ScriptTemplateConfig bean in this web application context (or its parent): ScriptTemplateConfigurer is the usual implementation. This bean may be given any name.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 229 */     ScriptRegistration registration = new ScriptRegistration();
/* 230 */     this.viewResolvers.add(registration.getViewResolver());
/* 231 */     return registration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beanName() {
/* 239 */     BeanNameViewResolver resolver = new BeanNameViewResolver();
/* 240 */     this.viewResolvers.add(resolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void viewResolver(ViewResolver viewResolver) {
/* 250 */     if (viewResolver instanceof ContentNegotiatingViewResolver) {
/* 251 */       throw new BeanInitializationException("addViewResolver cannot be used to configure a ContentNegotiatingViewResolver. Please use the method enableContentNegotiation instead.");
/*     */     }
/*     */ 
/*     */     
/* 255 */     this.viewResolvers.add(viewResolver);
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
/*     */   public void order(int order) {
/* 271 */     this.order = Integer.valueOf(order);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkBeanOfType(Class<?> beanType) {
/* 276 */     return (this.applicationContext == null || 
/* 277 */       !ObjectUtils.isEmpty((Object[])BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)this.applicationContext, beanType, false, false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getOrder() {
/* 282 */     return (this.order != null) ? this.order.intValue() : Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   protected List<ViewResolver> getViewResolvers() {
/* 286 */     if (this.contentNegotiatingResolver != null) {
/* 287 */       return (List)Collections.singletonList(this.contentNegotiatingResolver);
/*     */     }
/*     */     
/* 290 */     return this.viewResolvers;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class TilesRegistration
/*     */     extends UrlBasedViewResolverRegistration
/*     */   {
/*     */     public TilesRegistration() {
/* 298 */       super((UrlBasedViewResolver)new TilesViewResolver());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FreeMarkerRegistration
/*     */     extends UrlBasedViewResolverRegistration {
/*     */     public FreeMarkerRegistration() {
/* 305 */       super((UrlBasedViewResolver)new FreeMarkerViewResolver());
/* 306 */       getViewResolver().setSuffix(".ftl");
/*     */     }
/*     */   }
/*     */   
/*     */   private static class GroovyMarkupRegistration
/*     */     extends UrlBasedViewResolverRegistration
/*     */   {
/*     */     public GroovyMarkupRegistration() {
/* 314 */       super((UrlBasedViewResolver)new GroovyMarkupViewResolver());
/* 315 */       getViewResolver().setSuffix(".tpl");
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ScriptRegistration
/*     */     extends UrlBasedViewResolverRegistration
/*     */   {
/*     */     public ScriptRegistration() {
/* 323 */       super((UrlBasedViewResolver)new ScriptTemplateViewResolver());
/* 324 */       getViewResolver();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/ViewResolverRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */