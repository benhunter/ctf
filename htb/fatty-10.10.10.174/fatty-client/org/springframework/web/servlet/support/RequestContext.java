/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.servlet.jsp.jstl.core.Config;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.NoSuchMessageException;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ import org.springframework.ui.context.support.ResourceBundleThemeSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.web.bind.EscapedErrors;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.servlet.LocaleContextResolver;
/*     */ import org.springframework.web.servlet.LocaleResolver;
/*     */ import org.springframework.web.servlet.ThemeResolver;
/*     */ import org.springframework.web.util.HtmlUtils;
/*     */ import org.springframework.web.util.UriTemplate;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestContext
/*     */ {
/*     */   public static final String DEFAULT_THEME_NAME = "theme";
/*  91 */   public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = RequestContext.class.getName() + ".CONTEXT";
/*     */ 
/*     */   
/*  94 */   protected static final boolean jstlPresent = ClassUtils.isPresent("javax.servlet.jsp.jstl.core.Config", RequestContext.class
/*  95 */       .getClassLoader());
/*     */ 
/*     */   
/*     */   private HttpServletRequest request;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private HttpServletResponse response;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Map<String, Object> model;
/*     */ 
/*     */   
/*     */   private WebApplicationContext webApplicationContext;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Locale locale;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TimeZone timeZone;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Theme theme;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Boolean defaultHtmlEscape;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Boolean responseEncodedHtmlEscape;
/*     */ 
/*     */   
/*     */   private UrlPathHelper urlPathHelper;
/*     */   
/*     */   @Nullable
/*     */   private RequestDataValueProcessor requestDataValueProcessor;
/*     */   
/*     */   @Nullable
/*     */   private Map<String, Errors> errorsMap;
/*     */ 
/*     */   
/*     */   public RequestContext(HttpServletRequest request) {
/* 142 */     this(request, null, null, null);
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
/*     */   public RequestContext(HttpServletRequest request, HttpServletResponse response) {
/* 157 */     this(request, response, null, null);
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
/*     */   public RequestContext(HttpServletRequest request, @Nullable ServletContext servletContext) {
/* 173 */     this(request, null, servletContext, null);
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
/*     */   public RequestContext(HttpServletRequest request, @Nullable Map<String, Object> model) {
/* 188 */     this(request, null, null, model);
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
/*     */   public RequestContext(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable ServletContext servletContext, @Nullable Map<String, Object> model) {
/* 208 */     this.request = request;
/* 209 */     this.response = response;
/* 210 */     this.model = model;
/*     */ 
/*     */ 
/*     */     
/* 214 */     WebApplicationContext wac = (WebApplicationContext)request.getAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/* 215 */     if (wac == null) {
/* 216 */       wac = RequestContextUtils.findWebApplicationContext(request, servletContext);
/* 217 */       if (wac == null) {
/* 218 */         throw new IllegalStateException("No WebApplicationContext found: not in a DispatcherServlet request and no ContextLoaderListener registered?");
/*     */       }
/*     */     } 
/*     */     
/* 222 */     this.webApplicationContext = wac;
/*     */     
/* 224 */     Locale locale = null;
/* 225 */     TimeZone timeZone = null;
/*     */ 
/*     */     
/* 228 */     LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
/* 229 */     if (localeResolver instanceof LocaleContextResolver) {
/* 230 */       LocaleContext localeContext = ((LocaleContextResolver)localeResolver).resolveLocaleContext(request);
/* 231 */       locale = localeContext.getLocale();
/* 232 */       if (localeContext instanceof TimeZoneAwareLocaleContext) {
/* 233 */         timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/*     */       }
/*     */     }
/* 236 */     else if (localeResolver != null) {
/*     */       
/* 238 */       locale = localeResolver.resolveLocale(request);
/*     */     } 
/*     */     
/* 241 */     this.locale = locale;
/* 242 */     this.timeZone = timeZone;
/*     */ 
/*     */ 
/*     */     
/* 246 */     this.defaultHtmlEscape = WebUtils.getDefaultHtmlEscape(this.webApplicationContext.getServletContext());
/*     */ 
/*     */ 
/*     */     
/* 250 */     this
/* 251 */       .responseEncodedHtmlEscape = WebUtils.getResponseEncodedHtmlEscape(this.webApplicationContext.getServletContext());
/*     */     
/* 253 */     this.urlPathHelper = new UrlPathHelper();
/*     */     
/* 255 */     if (this.webApplicationContext.containsBean("requestDataValueProcessor")) {
/* 256 */       this.requestDataValueProcessor = (RequestDataValueProcessor)this.webApplicationContext.getBean("requestDataValueProcessor", RequestDataValueProcessor.class);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final HttpServletRequest getRequest() {
/* 266 */     return this.request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected final ServletContext getServletContext() {
/* 274 */     return this.webApplicationContext.getServletContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final WebApplicationContext getWebApplicationContext() {
/* 281 */     return this.webApplicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MessageSource getMessageSource() {
/* 288 */     return (MessageSource)this.webApplicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Map<String, Object> getModel() {
/* 297 */     return this.model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Locale getLocale() {
/* 307 */     return (this.locale != null) ? this.locale : getFallbackLocale();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TimeZone getTimeZone() {
/* 318 */     return (this.timeZone != null) ? this.timeZone : getFallbackTimeZone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Locale getFallbackLocale() {
/* 329 */     if (jstlPresent) {
/* 330 */       Locale locale = JstlLocaleResolver.getJstlLocale(getRequest(), getServletContext());
/* 331 */       if (locale != null) {
/* 332 */         return locale;
/*     */       }
/*     */     } 
/* 335 */     return getRequest().getLocale();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected TimeZone getFallbackTimeZone() {
/* 346 */     if (jstlPresent) {
/* 347 */       TimeZone timeZone = JstlLocaleResolver.getJstlTimeZone(getRequest(), getServletContext());
/* 348 */       if (timeZone != null) {
/* 349 */         return timeZone;
/*     */       }
/*     */     } 
/* 352 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changeLocale(Locale locale) {
/* 363 */     LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(this.request);
/* 364 */     if (localeResolver == null) {
/* 365 */       throw new IllegalStateException("Cannot change locale if no LocaleResolver configured");
/*     */     }
/* 367 */     localeResolver.setLocale(this.request, this.response, locale);
/* 368 */     this.locale = locale;
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
/*     */   public void changeLocale(Locale locale, TimeZone timeZone) {
/* 380 */     LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(this.request);
/* 381 */     if (!(localeResolver instanceof LocaleContextResolver)) {
/* 382 */       throw new IllegalStateException("Cannot change locale context if no LocaleContextResolver configured");
/*     */     }
/* 384 */     ((LocaleContextResolver)localeResolver).setLocaleContext(this.request, this.response, (LocaleContext)new SimpleTimeZoneAwareLocaleContext(locale, timeZone));
/*     */     
/* 386 */     this.locale = locale;
/* 387 */     this.timeZone = timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Theme getTheme() {
/* 395 */     if (this.theme == null) {
/*     */       
/* 397 */       this.theme = RequestContextUtils.getTheme(this.request);
/* 398 */       if (this.theme == null)
/*     */       {
/* 400 */         this.theme = getFallbackTheme();
/*     */       }
/*     */     } 
/* 403 */     return this.theme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Theme getFallbackTheme() {
/*     */     ResourceBundleThemeSource resourceBundleThemeSource;
/* 412 */     ThemeSource themeSource = RequestContextUtils.getThemeSource(getRequest());
/* 413 */     if (themeSource == null) {
/* 414 */       resourceBundleThemeSource = new ResourceBundleThemeSource();
/*     */     }
/* 416 */     Theme theme = resourceBundleThemeSource.getTheme("theme");
/* 417 */     if (theme == null) {
/* 418 */       throw new IllegalStateException("No theme defined and no fallback theme found");
/*     */     }
/* 420 */     return theme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changeTheme(@Nullable Theme theme) {
/* 430 */     ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(this.request);
/* 431 */     if (themeResolver == null) {
/* 432 */       throw new IllegalStateException("Cannot change theme if no ThemeResolver configured");
/*     */     }
/* 434 */     themeResolver.setThemeName(this.request, this.response, (theme != null) ? theme.getName() : null);
/* 435 */     this.theme = theme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changeTheme(String themeName) {
/* 445 */     ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(this.request);
/* 446 */     if (themeResolver == null) {
/* 447 */       throw new IllegalStateException("Cannot change theme if no ThemeResolver configured");
/*     */     }
/* 449 */     themeResolver.setThemeName(this.request, this.response, themeName);
/*     */     
/* 451 */     this.theme = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultHtmlEscape(boolean defaultHtmlEscape) {
/* 460 */     this.defaultHtmlEscape = Boolean.valueOf(defaultHtmlEscape);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefaultHtmlEscape() {
/* 467 */     return (this.defaultHtmlEscape != null && this.defaultHtmlEscape.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Boolean getDefaultHtmlEscape() {
/* 476 */     return this.defaultHtmlEscape;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResponseEncodedHtmlEscape() {
/* 486 */     return (this.responseEncodedHtmlEscape == null || this.responseEncodedHtmlEscape.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Boolean getResponseEncodedHtmlEscape() {
/* 497 */     return this.responseEncodedHtmlEscape;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/* 507 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 508 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlPathHelper getUrlPathHelper() {
/* 517 */     return this.urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RequestDataValueProcessor getRequestDataValueProcessor() {
/* 527 */     return this.requestDataValueProcessor;
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
/*     */   public String getContextPath() {
/* 539 */     return this.urlPathHelper.getOriginatingContextPath(this.request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContextUrl(String relativeUrl) {
/* 548 */     String url = getContextPath() + relativeUrl;
/* 549 */     if (this.response != null) {
/* 550 */       url = this.response.encodeURL(url);
/*     */     }
/* 552 */     return url;
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
/*     */   public String getContextUrl(String relativeUrl, Map<String, ?> params) {
/* 564 */     String url = getContextPath() + relativeUrl;
/* 565 */     UriTemplate template = new UriTemplate(url);
/* 566 */     url = template.expand(params).toASCIIString();
/* 567 */     if (this.response != null) {
/* 568 */       url = this.response.encodeURL(url);
/*     */     }
/* 570 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPathToServlet() {
/* 581 */     String path = this.urlPathHelper.getOriginatingContextPath(this.request);
/* 582 */     if (StringUtils.hasText(this.urlPathHelper.getPathWithinServletMapping(this.request))) {
/* 583 */       path = path + this.urlPathHelper.getOriginatingServletPath(this.request);
/*     */     }
/* 585 */     return path;
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
/*     */   public String getRequestUri() {
/* 598 */     return this.urlPathHelper.getOriginatingRequestUri(this.request);
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
/*     */   public String getQueryString() {
/* 611 */     return this.urlPathHelper.getOriginatingQueryString(this.request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, String defaultMessage) {
/* 621 */     return getMessage(code, null, defaultMessage, isDefaultHtmlEscape());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, @Nullable Object[] args, String defaultMessage) {
/* 632 */     return getMessage(code, args, defaultMessage, isDefaultHtmlEscape());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, @Nullable List<?> args, String defaultMessage) {
/* 643 */     return getMessage(code, (args != null) ? args.toArray() : null, defaultMessage, isDefaultHtmlEscape());
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
/*     */   public String getMessage(String code, @Nullable Object[] args, String defaultMessage, boolean htmlEscape) {
/* 655 */     String msg = this.webApplicationContext.getMessage(code, args, defaultMessage, getLocale());
/* 656 */     if (msg == null) {
/* 657 */       return "";
/*     */     }
/* 659 */     return htmlEscape ? HtmlUtils.htmlEscape(msg) : msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code) throws NoSuchMessageException {
/* 669 */     return getMessage(code, (Object[])null, isDefaultHtmlEscape());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, @Nullable Object[] args) throws NoSuchMessageException {
/* 680 */     return getMessage(code, args, isDefaultHtmlEscape());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, @Nullable List<?> args) throws NoSuchMessageException {
/* 691 */     return getMessage(code, (args != null) ? args.toArray() : null, isDefaultHtmlEscape());
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
/*     */   public String getMessage(String code, @Nullable Object[] args, boolean htmlEscape) throws NoSuchMessageException {
/* 703 */     String msg = this.webApplicationContext.getMessage(code, args, getLocale());
/* 704 */     return htmlEscape ? HtmlUtils.htmlEscape(msg) : msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
/* 714 */     return getMessage(resolvable, isDefaultHtmlEscape());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(MessageSourceResolvable resolvable, boolean htmlEscape) throws NoSuchMessageException {
/* 725 */     String msg = this.webApplicationContext.getMessage(resolvable, getLocale());
/* 726 */     return htmlEscape ? HtmlUtils.htmlEscape(msg) : msg;
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
/*     */   public String getThemeMessage(String code, String defaultMessage) {
/* 738 */     String msg = getTheme().getMessageSource().getMessage(code, null, defaultMessage, getLocale());
/* 739 */     return (msg != null) ? msg : "";
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
/*     */   public String getThemeMessage(String code, @Nullable Object[] args, String defaultMessage) {
/* 752 */     String msg = getTheme().getMessageSource().getMessage(code, args, defaultMessage, getLocale());
/* 753 */     return (msg != null) ? msg : "";
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
/*     */   public String getThemeMessage(String code, @Nullable List<?> args, String defaultMessage) {
/* 766 */     String msg = getTheme().getMessageSource().getMessage(code, (args != null) ? args.toArray() : null, defaultMessage, 
/* 767 */         getLocale());
/* 768 */     return (msg != null) ? msg : "";
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
/*     */   public String getThemeMessage(String code) throws NoSuchMessageException {
/* 780 */     return getTheme().getMessageSource().getMessage(code, null, getLocale());
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
/*     */   public String getThemeMessage(String code, @Nullable Object[] args) throws NoSuchMessageException {
/* 793 */     return getTheme().getMessageSource().getMessage(code, args, getLocale());
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
/*     */   public String getThemeMessage(String code, @Nullable List<?> args) throws NoSuchMessageException {
/* 806 */     return getTheme().getMessageSource().getMessage(code, (args != null) ? args.toArray() : null, getLocale());
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
/*     */   public String getThemeMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
/* 818 */     return getTheme().getMessageSource().getMessage(resolvable, getLocale());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Errors getErrors(String name) {
/* 828 */     return getErrors(name, isDefaultHtmlEscape());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Errors getErrors(String name, boolean htmlEscape) {
/*     */     BindingResult bindingResult;
/*     */     EscapedErrors escapedErrors;
/*     */     Errors errors1;
/* 839 */     if (this.errorsMap == null) {
/* 840 */       this.errorsMap = new HashMap<>();
/*     */     }
/* 842 */     Errors errors = this.errorsMap.get(name);
/* 843 */     boolean put = false;
/* 844 */     if (errors == null) {
/* 845 */       errors = (Errors)getModelObject(BindingResult.MODEL_KEY_PREFIX + name);
/*     */       
/* 847 */       if (errors instanceof BindException) {
/* 848 */         bindingResult = ((BindException)errors).getBindingResult();
/*     */       }
/* 850 */       if (bindingResult == null) {
/* 851 */         return null;
/*     */       }
/* 853 */       put = true;
/*     */     } 
/* 855 */     if (htmlEscape && !(bindingResult instanceof EscapedErrors)) {
/* 856 */       escapedErrors = new EscapedErrors((Errors)bindingResult);
/* 857 */       put = true;
/*     */     }
/* 859 */     else if (!htmlEscape && escapedErrors instanceof EscapedErrors) {
/* 860 */       errors1 = escapedErrors.getSource();
/* 861 */       put = true;
/*     */     } 
/* 863 */     if (put) {
/* 864 */       this.errorsMap.put(name, errors1);
/*     */     }
/* 866 */     return errors1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getModelObject(String modelName) {
/* 877 */     if (this.model != null) {
/* 878 */       return this.model.get(modelName);
/*     */     }
/*     */     
/* 881 */     return this.request.getAttribute(modelName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BindStatus getBindStatus(String path) throws IllegalStateException {
/* 892 */     return new BindStatus(this, path, isDefaultHtmlEscape());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BindStatus getBindStatus(String path, boolean htmlEscape) throws IllegalStateException {
/* 903 */     return new BindStatus(this, path, htmlEscape);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JstlLocaleResolver
/*     */   {
/*     */     @Nullable
/*     */     public static Locale getJstlLocale(HttpServletRequest request, @Nullable ServletContext servletContext) {
/* 915 */       Object localeObject = Config.get((ServletRequest)request, "javax.servlet.jsp.jstl.fmt.locale");
/* 916 */       if (localeObject == null) {
/* 917 */         HttpSession session = request.getSession(false);
/* 918 */         if (session != null) {
/* 919 */           localeObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.locale");
/*     */         }
/* 921 */         if (localeObject == null && servletContext != null) {
/* 922 */           localeObject = Config.get(servletContext, "javax.servlet.jsp.jstl.fmt.locale");
/*     */         }
/*     */       } 
/* 925 */       return (localeObject instanceof Locale) ? (Locale)localeObject : null;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public static TimeZone getJstlTimeZone(HttpServletRequest request, @Nullable ServletContext servletContext) {
/* 930 */       Object timeZoneObject = Config.get((ServletRequest)request, "javax.servlet.jsp.jstl.fmt.timeZone");
/* 931 */       if (timeZoneObject == null) {
/* 932 */         HttpSession session = request.getSession(false);
/* 933 */         if (session != null) {
/* 934 */           timeZoneObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.timeZone");
/*     */         }
/* 936 */         if (timeZoneObject == null && servletContext != null) {
/* 937 */           timeZoneObject = Config.get(servletContext, "javax.servlet.jsp.jstl.fmt.timeZone");
/*     */         }
/*     */       } 
/* 940 */       return (timeZoneObject instanceof TimeZone) ? (TimeZone)timeZoneObject : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/RequestContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */