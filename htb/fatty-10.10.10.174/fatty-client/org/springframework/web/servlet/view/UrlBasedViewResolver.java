/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ import org.springframework.web.servlet.View;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlBasedViewResolver
/*     */   extends AbstractCachingViewResolver
/*     */   implements Ordered
/*     */ {
/*     */   public static final String REDIRECT_URL_PREFIX = "redirect:";
/*     */   public static final String FORWARD_URL_PREFIX = "forward:";
/*     */   @Nullable
/*     */   private Class<?> viewClass;
/* 108 */   private String prefix = "";
/*     */   
/* 110 */   private String suffix = "";
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String contentType;
/*     */   
/*     */   private boolean redirectContextRelative = true;
/*     */   
/*     */   private boolean redirectHttp10Compatible = true;
/*     */   
/*     */   @Nullable
/*     */   private String[] redirectHosts;
/*     */   
/*     */   @Nullable
/*     */   private String requestContextAttribute;
/*     */   
/* 126 */   private final Map<String, Object> staticAttributes = new HashMap<>();
/*     */   
/*     */   @Nullable
/*     */   private Boolean exposePathVariables;
/*     */   
/*     */   @Nullable
/*     */   private Boolean exposeContextBeansAsAttributes;
/*     */   
/*     */   @Nullable
/*     */   private String[] exposedContextBeanNames;
/*     */   
/*     */   @Nullable
/*     */   private String[] viewNames;
/*     */   
/* 140 */   private int order = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewClass(@Nullable Class<?> viewClass) {
/* 150 */     if (viewClass != null && !requiredViewClass().isAssignableFrom(viewClass)) {
/* 151 */       throw new IllegalArgumentException("Given view class [" + viewClass.getName() + "] is not of type [" + 
/* 152 */           requiredViewClass().getName() + "]");
/*     */     }
/* 154 */     this.viewClass = viewClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Class<?> getViewClass() {
/* 162 */     return this.viewClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> requiredViewClass() {
/* 171 */     return AbstractUrlBasedView.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(@Nullable String prefix) {
/* 178 */     this.prefix = (prefix != null) ? prefix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPrefix() {
/* 185 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(@Nullable String suffix) {
/* 192 */     this.suffix = (suffix != null) ? suffix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSuffix() {
/* 199 */     return this.suffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentType(@Nullable String contentType) {
/* 208 */     this.contentType = contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getContentType() {
/* 216 */     return this.contentType;
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
/*     */   public void setRedirectContextRelative(boolean redirectContextRelative) {
/* 232 */     this.redirectContextRelative = redirectContextRelative;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isRedirectContextRelative() {
/* 241 */     return this.redirectContextRelative;
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
/*     */   public void setRedirectHttp10Compatible(boolean redirectHttp10Compatible) {
/* 259 */     this.redirectHttp10Compatible = redirectHttp10Compatible;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isRedirectHttp10Compatible() {
/* 266 */     return this.redirectHttp10Compatible;
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
/*     */   public void setRedirectHosts(@Nullable String... redirectHosts) {
/* 280 */     this.redirectHosts = redirectHosts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getRedirectHosts() {
/* 289 */     return this.redirectHosts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequestContextAttribute(@Nullable String requestContextAttribute) {
/* 298 */     this.requestContextAttribute = requestContextAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getRequestContextAttribute() {
/* 306 */     return this.requestContextAttribute;
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
/*     */   public void setAttributes(Properties props) {
/* 321 */     CollectionUtils.mergePropertiesIntoMap(props, this.staticAttributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributesMap(@Nullable Map<String, ?> attributes) {
/* 332 */     if (attributes != null) {
/* 333 */       this.staticAttributes.putAll(attributes);
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
/*     */   public Map<String, Object> getAttributesMap() {
/* 345 */     return this.staticAttributes;
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
/*     */   public void setExposePathVariables(@Nullable Boolean exposePathVariables) {
/* 361 */     this.exposePathVariables = exposePathVariables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Boolean getExposePathVariables() {
/* 369 */     return this.exposePathVariables;
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
/*     */   public void setExposeContextBeansAsAttributes(boolean exposeContextBeansAsAttributes) {
/* 382 */     this.exposeContextBeansAsAttributes = Boolean.valueOf(exposeContextBeansAsAttributes);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Boolean getExposeContextBeansAsAttributes() {
/* 387 */     return this.exposeContextBeansAsAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExposedContextBeanNames(@Nullable String... exposedContextBeanNames) {
/* 397 */     this.exposedContextBeanNames = exposedContextBeanNames;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected String[] getExposedContextBeanNames() {
/* 402 */     return this.exposedContextBeanNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewNames(@Nullable String... viewNames) {
/* 413 */     this.viewNames = viewNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String[] getViewNames() {
/* 422 */     return this.viewNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/* 431 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 436 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initApplicationContext() {
/* 441 */     super.initApplicationContext();
/* 442 */     if (getViewClass() == null) {
/* 443 */       throw new IllegalArgumentException("Property 'viewClass' is required");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getCacheKey(String viewName, Locale locale) {
/* 454 */     return viewName;
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
/*     */   protected View createView(String viewName, Locale locale) throws Exception {
/* 469 */     if (!canHandle(viewName, locale)) {
/* 470 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 474 */     if (viewName.startsWith("redirect:")) {
/* 475 */       String redirectUrl = viewName.substring("redirect:".length());
/*     */       
/* 477 */       RedirectView view = new RedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
/* 478 */       String[] hosts = getRedirectHosts();
/* 479 */       if (hosts != null) {
/* 480 */         view.setHosts(hosts);
/*     */       }
/* 482 */       return applyLifecycleMethods("redirect:", view);
/*     */     } 
/*     */ 
/*     */     
/* 486 */     if (viewName.startsWith("forward:")) {
/* 487 */       String forwardUrl = viewName.substring("forward:".length());
/* 488 */       InternalResourceView view = new InternalResourceView(forwardUrl);
/* 489 */       return applyLifecycleMethods("forward:", view);
/*     */     } 
/*     */ 
/*     */     
/* 493 */     return super.createView(viewName, locale);
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
/*     */   protected boolean canHandle(String viewName, Locale locale) {
/* 507 */     String[] viewNames = getViewNames();
/* 508 */     return (viewNames == null || PatternMatchUtils.simpleMatch(viewNames, viewName));
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
/*     */   protected View loadView(String viewName, Locale locale) throws Exception {
/* 528 */     AbstractUrlBasedView view = buildView(viewName);
/* 529 */     View result = applyLifecycleMethods(viewName, view);
/* 530 */     return view.checkResource(locale) ? result : null;
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
/*     */   protected AbstractUrlBasedView buildView(String viewName) throws Exception {
/* 548 */     Class<?> viewClass = getViewClass();
/* 549 */     Assert.state((viewClass != null), "No view class");
/*     */     
/* 551 */     AbstractUrlBasedView view = (AbstractUrlBasedView)BeanUtils.instantiateClass(viewClass);
/* 552 */     view.setUrl(getPrefix() + viewName + getSuffix());
/*     */     
/* 554 */     String contentType = getContentType();
/* 555 */     if (contentType != null) {
/* 556 */       view.setContentType(contentType);
/*     */     }
/*     */     
/* 559 */     view.setRequestContextAttribute(getRequestContextAttribute());
/* 560 */     view.setAttributesMap(getAttributesMap());
/*     */     
/* 562 */     Boolean exposePathVariables = getExposePathVariables();
/* 563 */     if (exposePathVariables != null) {
/* 564 */       view.setExposePathVariables(exposePathVariables.booleanValue());
/*     */     }
/* 566 */     Boolean exposeContextBeansAsAttributes = getExposeContextBeansAsAttributes();
/* 567 */     if (exposeContextBeansAsAttributes != null) {
/* 568 */       view.setExposeContextBeansAsAttributes(exposeContextBeansAsAttributes.booleanValue());
/*     */     }
/* 570 */     String[] exposedContextBeanNames = getExposedContextBeanNames();
/* 571 */     if (exposedContextBeanNames != null) {
/* 572 */       view.setExposedContextBeanNames(exposedContextBeanNames);
/*     */     }
/*     */     
/* 575 */     return view;
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
/*     */   protected View applyLifecycleMethods(String viewName, AbstractUrlBasedView view) {
/* 592 */     ApplicationContext context = getApplicationContext();
/* 593 */     if (context != null) {
/* 594 */       Object initialized = context.getAutowireCapableBeanFactory().initializeBean(view, viewName);
/* 595 */       if (initialized instanceof View) {
/* 596 */         return (View)initialized;
/*     */       }
/*     */     } 
/* 599 */     return view;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/UrlBasedViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */