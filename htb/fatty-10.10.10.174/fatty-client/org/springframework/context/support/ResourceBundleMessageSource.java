/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.PropertyResourceBundle;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceBundleMessageSource
/*     */   extends AbstractResourceBasedMessageSource
/*     */   implements BeanClassLoaderAware
/*     */ {
/*     */   @Nullable
/*     */   private ClassLoader bundleClassLoader;
/*     */   @Nullable
/*  85 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private final Map<String, Map<Locale, ResourceBundle>> cachedResourceBundles = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   private final Map<ResourceBundle, Map<String, Map<Locale, MessageFormat>>> cachedBundleMessageFormats = new ConcurrentHashMap<>();
/*     */   
/*     */   @Nullable
/* 108 */   private volatile MessageSourceControl control = new MessageSourceControl();
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceBundleMessageSource() {
/* 113 */     setDefaultEncoding("ISO-8859-1");
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
/*     */   public void setBundleClassLoader(ClassLoader classLoader) {
/* 126 */     this.bundleClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ClassLoader getBundleClassLoader() {
/* 136 */     return (this.bundleClassLoader != null) ? this.bundleClassLoader : this.beanClassLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 141 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveCodeWithoutArguments(String code, Locale locale) {
/* 151 */     Set<String> basenames = getBasenameSet();
/* 152 */     for (String basename : basenames) {
/* 153 */       ResourceBundle bundle = getResourceBundle(basename, locale);
/* 154 */       if (bundle != null) {
/* 155 */         String result = getStringOrNull(bundle, code);
/* 156 */         if (result != null) {
/* 157 */           return result;
/*     */         }
/*     */       } 
/*     */     } 
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MessageFormat resolveCode(String code, Locale locale) {
/* 171 */     Set<String> basenames = getBasenameSet();
/* 172 */     for (String basename : basenames) {
/* 173 */       ResourceBundle bundle = getResourceBundle(basename, locale);
/* 174 */       if (bundle != null) {
/* 175 */         MessageFormat messageFormat = getMessageFormat(bundle, code, locale);
/* 176 */         if (messageFormat != null) {
/* 177 */           return messageFormat;
/*     */         }
/*     */       } 
/*     */     } 
/* 181 */     return null;
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
/*     */   @Nullable
/*     */   protected ResourceBundle getResourceBundle(String basename, Locale locale) {
/* 195 */     if (getCacheMillis() >= 0L)
/*     */     {
/*     */       
/* 198 */       return doGetBundle(basename, locale);
/*     */     }
/*     */ 
/*     */     
/* 202 */     Map<Locale, ResourceBundle> localeMap = this.cachedResourceBundles.get(basename);
/* 203 */     if (localeMap != null) {
/* 204 */       ResourceBundle bundle = localeMap.get(locale);
/* 205 */       if (bundle != null) {
/* 206 */         return bundle;
/*     */       }
/*     */     } 
/*     */     try {
/* 210 */       ResourceBundle bundle = doGetBundle(basename, locale);
/* 211 */       if (localeMap == null) {
/* 212 */         localeMap = new ConcurrentHashMap<>();
/* 213 */         Map<Locale, ResourceBundle> existing = this.cachedResourceBundles.putIfAbsent(basename, localeMap);
/* 214 */         if (existing != null) {
/* 215 */           localeMap = existing;
/*     */         }
/*     */       } 
/* 218 */       localeMap.put(locale, bundle);
/* 219 */       return bundle;
/*     */     }
/* 221 */     catch (MissingResourceException ex) {
/* 222 */       if (this.logger.isWarnEnabled()) {
/* 223 */         this.logger.warn("ResourceBundle [" + basename + "] not found for MessageSource: " + ex.getMessage());
/*     */       }
/*     */ 
/*     */       
/* 227 */       return null;
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
/*     */   
/*     */   protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
/* 242 */     ClassLoader classLoader = getBundleClassLoader();
/* 243 */     Assert.state((classLoader != null), "No bundle ClassLoader set");
/*     */     
/* 245 */     MessageSourceControl control = this.control;
/* 246 */     if (control != null) {
/*     */       try {
/* 248 */         return ResourceBundle.getBundle(basename, locale, classLoader, control);
/*     */       }
/* 250 */       catch (UnsupportedOperationException ex) {
/*     */         
/* 252 */         this.control = null;
/* 253 */         String encoding = getDefaultEncoding();
/* 254 */         if (encoding != null && this.logger.isInfoEnabled()) {
/* 255 */           this.logger.info("ResourceBundleMessageSource is configured to read resources with encoding '" + encoding + "' but ResourceBundle.Control not supported in current system environment: " + ex
/*     */               
/* 257 */               .getMessage() + " - falling back to plain ResourceBundle.getBundle retrieval with the platform default encoding. Consider setting the 'defaultEncoding' property to 'null' for participating in the platform default and therefore avoiding this log message.");
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     return ResourceBundle.getBundle(basename, locale, classLoader);
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
/*     */   protected ResourceBundle loadBundle(Reader reader) throws IOException {
/* 284 */     return new PropertyResourceBundle(reader);
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
/*     */   protected ResourceBundle loadBundle(InputStream inputStream) throws IOException {
/* 306 */     return new PropertyResourceBundle(inputStream);
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
/*     */   @Nullable
/*     */   protected MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale) throws MissingResourceException {
/* 323 */     Map<String, Map<Locale, MessageFormat>> codeMap = this.cachedBundleMessageFormats.get(bundle);
/* 324 */     Map<Locale, MessageFormat> localeMap = null;
/* 325 */     if (codeMap != null) {
/* 326 */       localeMap = codeMap.get(code);
/* 327 */       if (localeMap != null) {
/* 328 */         MessageFormat result = localeMap.get(locale);
/* 329 */         if (result != null) {
/* 330 */           return result;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 335 */     String msg = getStringOrNull(bundle, code);
/* 336 */     if (msg != null) {
/* 337 */       if (codeMap == null) {
/* 338 */         codeMap = new ConcurrentHashMap<>();
/*     */         
/* 340 */         Map<String, Map<Locale, MessageFormat>> existing = this.cachedBundleMessageFormats.putIfAbsent(bundle, codeMap);
/* 341 */         if (existing != null) {
/* 342 */           codeMap = existing;
/*     */         }
/*     */       } 
/* 345 */       if (localeMap == null) {
/* 346 */         localeMap = new ConcurrentHashMap<>();
/* 347 */         Map<Locale, MessageFormat> existing = codeMap.putIfAbsent(code, localeMap);
/* 348 */         if (existing != null) {
/* 349 */           localeMap = existing;
/*     */         }
/*     */       } 
/* 352 */       MessageFormat result = createMessageFormat(msg, locale);
/* 353 */       localeMap.put(locale, result);
/* 354 */       return result;
/*     */     } 
/*     */     
/* 357 */     return null;
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
/*     */   @Nullable
/*     */   protected String getStringOrNull(ResourceBundle bundle, String key) {
/* 376 */     if (bundle.containsKey(key)) {
/*     */       try {
/* 378 */         return bundle.getString(key);
/*     */       }
/* 380 */       catch (MissingResourceException missingResourceException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 385 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 393 */     return getClass().getName() + ": basenames=" + getBasenameSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class MessageSourceControl
/*     */     extends ResourceBundle.Control
/*     */   {
/*     */     private MessageSourceControl() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
/* 410 */       if (format.equals("java.properties")) {
/* 411 */         InputStream inputStream; String bundleName = toBundleName(baseName, locale);
/* 412 */         String resourceName = toResourceName(bundleName, "properties");
/* 413 */         ClassLoader classLoader = loader;
/* 414 */         boolean reloadFlag = reload;
/*     */         
/*     */         try {
/* 417 */           inputStream = AccessController.<InputStream>doPrivileged(() -> {
/*     */                 InputStream is = null;
/*     */                 
/*     */                 if (reloadFlag) {
/*     */                   URL url = classLoader.getResource(resourceName);
/*     */                   
/*     */                   if (url != null) {
/*     */                     URLConnection connection = url.openConnection();
/*     */                     if (connection != null) {
/*     */                       connection.setUseCaches(false);
/*     */                       is = connection.getInputStream();
/*     */                     } 
/*     */                   } 
/*     */                 } else {
/*     */                   is = classLoader.getResourceAsStream(resourceName);
/*     */                 } 
/*     */                 return is;
/*     */               });
/* 435 */         } catch (PrivilegedActionException ex) {
/* 436 */           throw (IOException)ex.getException();
/*     */         } 
/* 438 */         if (inputStream != null) {
/* 439 */           String encoding = ResourceBundleMessageSource.this.getDefaultEncoding();
/* 440 */           if (encoding != null) {
/* 441 */             try (InputStreamReader bundleReader = new InputStreamReader(inputStream, encoding)) {
/* 442 */               return ResourceBundleMessageSource.this.loadBundle(bundleReader);
/*     */             } 
/*     */           }
/*     */           
/* 446 */           try (InputStream bundleStream = inputStream) {
/* 447 */             return ResourceBundleMessageSource.this.loadBundle(bundleStream);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 452 */         return null;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 457 */       return super.newBundle(baseName, locale, format, loader, reload);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Locale getFallbackLocale(String baseName, Locale locale) {
/* 464 */       return ResourceBundleMessageSource.this.isFallbackToSystemLocale() ? super.getFallbackLocale(baseName, locale) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getTimeToLive(String baseName, Locale locale) {
/* 469 */       long cacheMillis = ResourceBundleMessageSource.this.getCacheMillis();
/* 470 */       return (cacheMillis >= 0L) ? cacheMillis : super.getTimeToLive(baseName, locale);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
/* 477 */       if (super.needsReload(baseName, locale, format, loader, bundle, loadTime)) {
/* 478 */         ResourceBundleMessageSource.this.cachedBundleMessageFormats.remove(bundle);
/* 479 */         return true;
/*     */       } 
/*     */       
/* 482 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/ResourceBundleMessageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */