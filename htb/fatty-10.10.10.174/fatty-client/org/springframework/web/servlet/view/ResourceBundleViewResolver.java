/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.context.support.GenericWebApplicationContext;
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
/*     */ public class ResourceBundleViewResolver
/*     */   extends AbstractCachingViewResolver
/*     */   implements Ordered, InitializingBean, DisposableBean
/*     */ {
/*     */   public static final String DEFAULT_BASENAME = "views";
/*  70 */   private String[] basenames = new String[] { "views" };
/*     */   
/*  72 */   private ClassLoader bundleClassLoader = Thread.currentThread().getContextClassLoader();
/*     */   
/*     */   @Nullable
/*     */   private String defaultParentView;
/*     */   
/*     */   @Nullable
/*     */   private Locale[] localesToInitialize;
/*     */   
/*  80 */   private int order = Integer.MAX_VALUE;
/*     */ 
/*     */   
/*  83 */   private final Map<Locale, BeanFactory> localeCache = new HashMap<>();
/*     */ 
/*     */   
/*  86 */   private final Map<List<ResourceBundle>, ConfigurableApplicationContext> bundleCache = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBasename(String basename) {
/* 104 */     setBasenames(new String[] { basename });
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
/*     */   public void setBasenames(String... basenames) {
/* 125 */     this.basenames = basenames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBundleClassLoader(ClassLoader classLoader) {
/* 133 */     this.bundleClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader getBundleClassLoader() {
/* 142 */     return this.bundleClassLoader;
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
/*     */   public void setDefaultParentView(String defaultParentView) {
/* 159 */     this.defaultParentView = defaultParentView;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalesToInitialize(Locale... localesToInitialize) {
/* 168 */     this.localesToInitialize = localesToInitialize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/* 177 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 182 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws BeansException {
/* 191 */     if (this.localesToInitialize != null) {
/* 192 */       for (Locale locale : this.localesToInitialize) {
/* 193 */         initFactory(locale);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected View loadView(String viewName, Locale locale) throws Exception {
/* 201 */     BeanFactory factory = initFactory(locale);
/*     */     try {
/* 203 */       return (View)factory.getBean(viewName, View.class);
/*     */     }
/* 205 */     catch (NoSuchBeanDefinitionException ex) {
/*     */       
/* 207 */       return null;
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
/*     */   protected synchronized BeanFactory initFactory(Locale locale) throws BeansException {
/* 222 */     if (isCache()) {
/* 223 */       BeanFactory cachedFactory = this.localeCache.get(locale);
/* 224 */       if (cachedFactory != null) {
/* 225 */         return cachedFactory;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 230 */     List<ResourceBundle> bundles = new LinkedList<>();
/* 231 */     for (String basename : this.basenames) {
/* 232 */       ResourceBundle bundle = getBundle(basename, locale);
/* 233 */       bundles.add(bundle);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 238 */     if (isCache()) {
/* 239 */       BeanFactory cachedFactory = (BeanFactory)this.bundleCache.get(bundles);
/* 240 */       if (cachedFactory != null) {
/* 241 */         this.localeCache.put(locale, cachedFactory);
/* 242 */         return cachedFactory;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 247 */     GenericWebApplicationContext factory = new GenericWebApplicationContext();
/* 248 */     factory.setParent(getApplicationContext());
/* 249 */     factory.setServletContext(getServletContext());
/*     */ 
/*     */     
/* 252 */     PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader((BeanDefinitionRegistry)factory);
/* 253 */     reader.setDefaultParentBean(this.defaultParentView);
/* 254 */     for (ResourceBundle bundle : bundles) {
/* 255 */       reader.registerBeanDefinitions(bundle);
/*     */     }
/*     */     
/* 258 */     factory.refresh();
/*     */ 
/*     */     
/* 261 */     if (isCache()) {
/* 262 */       this.localeCache.put(locale, factory);
/* 263 */       this.bundleCache.put(bundles, factory);
/*     */     } 
/*     */     
/* 266 */     return (BeanFactory)factory;
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
/*     */   protected ResourceBundle getBundle(String basename, Locale locale) throws MissingResourceException {
/* 278 */     return ResourceBundle.getBundle(basename, locale, getBundleClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws BeansException {
/* 287 */     for (ConfigurableApplicationContext factory : this.bundleCache.values()) {
/* 288 */       factory.close();
/*     */     }
/* 290 */     this.localeCache.clear();
/* 291 */     this.bundleCache.clear();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/ResourceBundleViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */