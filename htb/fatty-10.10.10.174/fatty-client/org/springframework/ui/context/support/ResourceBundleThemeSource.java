/*     */ package org.springframework.ui.context.support;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.context.HierarchicalMessageSource;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.support.ResourceBundleMessageSource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.context.HierarchicalThemeSource;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceBundleThemeSource
/*     */   implements HierarchicalThemeSource, BeanClassLoaderAware
/*     */ {
/*  48 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private ThemeSource parentThemeSource;
/*     */   
/*  53 */   private String basenamePrefix = "";
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String defaultEncoding;
/*     */   
/*     */   @Nullable
/*     */   private Boolean fallbackToSystemLocale;
/*     */   
/*     */   @Nullable
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*  65 */   private final Map<String, Theme> themeCache = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentThemeSource(@Nullable ThemeSource parent) {
/*  70 */     this.parentThemeSource = parent;
/*     */ 
/*     */ 
/*     */     
/*  74 */     synchronized (this.themeCache) {
/*  75 */       for (Theme theme : this.themeCache.values()) {
/*  76 */         initParent(theme);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ThemeSource getParentThemeSource() {
/*  84 */     return this.parentThemeSource;
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
/*     */   public void setBasenamePrefix(@Nullable String basenamePrefix) {
/*  98 */     this.basenamePrefix = (basenamePrefix != null) ? basenamePrefix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultEncoding(@Nullable String defaultEncoding) {
/* 109 */     this.defaultEncoding = defaultEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFallbackToSystemLocale(boolean fallbackToSystemLocale) {
/* 120 */     this.fallbackToSystemLocale = Boolean.valueOf(fallbackToSystemLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(@Nullable ClassLoader beanClassLoader) {
/* 125 */     this.beanClassLoader = beanClassLoader;
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
/*     */   @Nullable
/*     */   public Theme getTheme(String themeName) {
/* 141 */     Theme theme = this.themeCache.get(themeName);
/* 142 */     if (theme == null) {
/* 143 */       synchronized (this.themeCache) {
/* 144 */         theme = this.themeCache.get(themeName);
/* 145 */         if (theme == null) {
/* 146 */           String basename = this.basenamePrefix + themeName;
/* 147 */           MessageSource messageSource = createMessageSource(basename);
/* 148 */           theme = new SimpleTheme(themeName, messageSource);
/* 149 */           initParent(theme);
/* 150 */           this.themeCache.put(themeName, theme);
/* 151 */           if (this.logger.isDebugEnabled()) {
/* 152 */             this.logger.debug("Theme created: name '" + themeName + "', basename [" + basename + "]");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 157 */     return theme;
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
/*     */   protected MessageSource createMessageSource(String basename) {
/* 172 */     ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
/* 173 */     messageSource.setBasename(basename);
/* 174 */     if (this.defaultEncoding != null) {
/* 175 */       messageSource.setDefaultEncoding(this.defaultEncoding);
/*     */     }
/* 177 */     if (this.fallbackToSystemLocale != null) {
/* 178 */       messageSource.setFallbackToSystemLocale(this.fallbackToSystemLocale.booleanValue());
/*     */     }
/* 180 */     if (this.beanClassLoader != null) {
/* 181 */       messageSource.setBeanClassLoader(this.beanClassLoader);
/*     */     }
/* 183 */     return (MessageSource)messageSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initParent(Theme theme) {
/* 192 */     if (theme.getMessageSource() instanceof HierarchicalMessageSource) {
/* 193 */       HierarchicalMessageSource messageSource = (HierarchicalMessageSource)theme.getMessageSource();
/* 194 */       if (getParentThemeSource() != null && messageSource.getParentMessageSource() == null) {
/* 195 */         Theme parentTheme = getParentThemeSource().getTheme(theme.getName());
/* 196 */         if (parentTheme != null)
/* 197 */           messageSource.setParentMessageSource(parentTheme.getMessageSource()); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ui/context/support/ResourceBundleThemeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */