/*     */ package org.springframework.web.servlet.view.groovy;
/*     */ 
/*     */ import groovy.text.markup.MarkupTemplateEngine;
/*     */ import groovy.text.markup.TemplateConfiguration;
/*     */ import groovy.text.markup.TemplateResolver;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GroovyMarkupConfigurer
/*     */   extends TemplateConfiguration
/*     */   implements GroovyMarkupConfig, ApplicationContextAware, InitializingBean
/*     */ {
/*  89 */   private String resourceLoaderPath = "classpath:";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MarkupTemplateEngine templateEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceLoaderPath(String resourceLoaderPath) {
/* 107 */     this.resourceLoaderPath = resourceLoaderPath;
/*     */   }
/*     */   
/*     */   public String getResourceLoaderPath() {
/* 111 */     return this.resourceLoaderPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTemplateEngine(MarkupTemplateEngine templateEngine) {
/* 121 */     this.templateEngine = templateEngine;
/*     */   }
/*     */   
/*     */   public MarkupTemplateEngine getTemplateEngine() {
/* 125 */     Assert.state((this.templateEngine != null), "No MarkupTemplateEngine set");
/* 126 */     return this.templateEngine;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 131 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   protected ApplicationContext getApplicationContext() {
/* 135 */     Assert.state((this.applicationContext != null), "No ApplicationContext set");
/* 136 */     return this.applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocale(Locale locale) {
/* 145 */     super.setLocale(locale);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 151 */     if (this.templateEngine == null) {
/* 152 */       this.templateEngine = createTemplateEngine();
/*     */     }
/*     */   }
/*     */   
/*     */   protected MarkupTemplateEngine createTemplateEngine() throws IOException {
/* 157 */     if (this.templateEngine == null) {
/* 158 */       ClassLoader templateClassLoader = createTemplateClassLoader();
/* 159 */       this.templateEngine = new MarkupTemplateEngine(templateClassLoader, this, new LocaleTemplateResolver());
/*     */     } 
/* 161 */     return this.templateEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader createTemplateClassLoader() throws IOException {
/* 169 */     String[] paths = StringUtils.commaDelimitedListToStringArray(getResourceLoaderPath());
/* 170 */     List<URL> urls = new ArrayList<>();
/* 171 */     for (String path : paths) {
/* 172 */       Resource[] resources = getApplicationContext().getResources(path);
/* 173 */       if (resources.length > 0) {
/* 174 */         for (Resource resource : resources) {
/* 175 */           if (resource.exists()) {
/* 176 */             urls.add(resource.getURL());
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 181 */     ClassLoader classLoader = getApplicationContext().getClassLoader();
/* 182 */     Assert.state((classLoader != null), "No ClassLoader");
/* 183 */     return !urls.isEmpty() ? new URLClassLoader(urls.<URL>toArray(new URL[0]), classLoader) : classLoader;
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
/*     */   protected URL resolveTemplate(ClassLoader classLoader, String templatePath) throws IOException {
/* 195 */     MarkupTemplateEngine.TemplateResource resource = MarkupTemplateEngine.TemplateResource.parse(templatePath);
/* 196 */     Locale locale = LocaleContextHolder.getLocale();
/* 197 */     URL url = classLoader.getResource(resource.withLocale(StringUtils.replace(locale.toString(), "-", "_")).toString());
/* 198 */     if (url == null) {
/* 199 */       url = classLoader.getResource(resource.withLocale(locale.getLanguage()).toString());
/*     */     }
/* 201 */     if (url == null) {
/* 202 */       url = classLoader.getResource(resource.withLocale(null).toString());
/*     */     }
/* 204 */     if (url == null) {
/* 205 */       throw new IOException("Unable to load template:" + templatePath);
/*     */     }
/* 207 */     return url;
/*     */   }
/*     */ 
/*     */   
/*     */   private class LocaleTemplateResolver
/*     */     implements TemplateResolver
/*     */   {
/*     */     @Nullable
/*     */     private ClassLoader classLoader;
/*     */ 
/*     */     
/*     */     private LocaleTemplateResolver() {}
/*     */ 
/*     */     
/*     */     public void configure(ClassLoader templateClassLoader, TemplateConfiguration configuration) {
/* 222 */       this.classLoader = templateClassLoader;
/*     */     }
/*     */ 
/*     */     
/*     */     public URL resolveTemplate(String templatePath) throws IOException {
/* 227 */       Assert.state((this.classLoader != null), "No template ClassLoader available");
/* 228 */       return GroovyMarkupConfigurer.this.resolveTemplate(this.classLoader, templatePath);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/groovy/GroovyMarkupConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */