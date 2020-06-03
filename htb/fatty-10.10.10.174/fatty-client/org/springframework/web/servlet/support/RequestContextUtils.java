/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.context.ContextLoader;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
/*     */ import org.springframework.web.servlet.FlashMap;
/*     */ import org.springframework.web.servlet.FlashMapManager;
/*     */ import org.springframework.web.servlet.LocaleContextResolver;
/*     */ import org.springframework.web.servlet.LocaleResolver;
/*     */ import org.springframework.web.servlet.ThemeResolver;
/*     */ import org.springframework.web.util.UriComponents;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RequestContextUtils
/*     */ {
/*     */   public static final String REQUEST_DATA_VALUE_PROCESSOR_BEAN_NAME = "requestDataValueProcessor";
/*     */   
/*     */   @Nullable
/*     */   public static WebApplicationContext findWebApplicationContext(HttpServletRequest request, @Nullable ServletContext servletContext) {
/*  89 */     WebApplicationContext webApplicationContext = (WebApplicationContext)request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*     */     
/*  91 */     if (webApplicationContext == null) {
/*  92 */       if (servletContext != null) {
/*  93 */         webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
/*     */       }
/*  95 */       if (webApplicationContext == null) {
/*  96 */         webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
/*     */       }
/*     */     } 
/*  99 */     return webApplicationContext;
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
/*     */   @Nullable
/*     */   public static WebApplicationContext findWebApplicationContext(HttpServletRequest request) {
/* 119 */     return findWebApplicationContext(request, request.getServletContext());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static LocaleResolver getLocaleResolver(HttpServletRequest request) {
/* 130 */     return (LocaleResolver)request.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE);
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
/*     */   public static Locale getLocale(HttpServletRequest request) {
/* 149 */     LocaleResolver localeResolver = getLocaleResolver(request);
/* 150 */     return (localeResolver != null) ? localeResolver.resolveLocale(request) : request.getLocale();
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
/*     */   @Nullable
/*     */   public static TimeZone getTimeZone(HttpServletRequest request) {
/* 172 */     LocaleResolver localeResolver = getLocaleResolver(request);
/* 173 */     if (localeResolver instanceof LocaleContextResolver) {
/* 174 */       LocaleContext localeContext = ((LocaleContextResolver)localeResolver).resolveLocaleContext(request);
/* 175 */       if (localeContext instanceof TimeZoneAwareLocaleContext) {
/* 176 */         return ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/*     */       }
/*     */     } 
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static ThemeResolver getThemeResolver(HttpServletRequest request) {
/* 190 */     return (ThemeResolver)request.getAttribute(DispatcherServlet.THEME_RESOLVER_ATTRIBUTE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static ThemeSource getThemeSource(HttpServletRequest request) {
/* 201 */     return (ThemeSource)request.getAttribute(DispatcherServlet.THEME_SOURCE_ATTRIBUTE);
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
/*     */   public static Theme getTheme(HttpServletRequest request) {
/* 213 */     ThemeResolver themeResolver = getThemeResolver(request);
/* 214 */     ThemeSource themeSource = getThemeSource(request);
/* 215 */     if (themeResolver != null && themeSource != null) {
/* 216 */       String themeName = themeResolver.resolveThemeName(request);
/* 217 */       return themeSource.getTheme(themeName);
/*     */     } 
/*     */     
/* 220 */     return null;
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
/*     */   @Nullable
/*     */   public static Map<String, ?> getInputFlashMap(HttpServletRequest request) {
/* 233 */     return (Map<String, ?>)request.getAttribute(DispatcherServlet.INPUT_FLASH_MAP_ATTRIBUTE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FlashMap getOutputFlashMap(HttpServletRequest request) {
/* 243 */     return (FlashMap)request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE);
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
/*     */   @Nullable
/*     */   public static FlashMapManager getFlashMapManager(HttpServletRequest request) {
/* 256 */     return (FlashMapManager)request.getAttribute(DispatcherServlet.FLASH_MAP_MANAGER_ATTRIBUTE);
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
/*     */   public static void saveOutputFlashMap(String location, HttpServletRequest request, HttpServletResponse response) {
/* 269 */     FlashMap flashMap = getOutputFlashMap(request);
/* 270 */     if (CollectionUtils.isEmpty((Map)flashMap)) {
/*     */       return;
/*     */     }
/*     */     
/* 274 */     UriComponents uriComponents = UriComponentsBuilder.fromUriString(location).build();
/* 275 */     flashMap.setTargetRequestPath(uriComponents.getPath());
/* 276 */     flashMap.addTargetRequestParams(uriComponents.getQueryParams());
/*     */     
/* 278 */     FlashMapManager manager = getFlashMapManager(request);
/* 279 */     Assert.state((manager != null), "No FlashMapManager. Is this a DispatcherServlet handled request?");
/* 280 */     manager.saveOutputFlashMap(flashMap, request, response);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/RequestContextUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */