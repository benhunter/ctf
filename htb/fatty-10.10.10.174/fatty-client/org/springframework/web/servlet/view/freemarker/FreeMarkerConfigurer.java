/*     */ package org.springframework.web.servlet.view.freemarker;
/*     */ 
/*     */ import freemarker.cache.ClassTemplateLoader;
/*     */ import freemarker.cache.TemplateLoader;
/*     */ import freemarker.ext.jsp.TaglibFactory;
/*     */ import freemarker.template.Configuration;
/*     */ import freemarker.template.TemplateException;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class FreeMarkerConfigurer
/*     */   extends FreeMarkerConfigurationFactory
/*     */   implements FreeMarkerConfig, InitializingBean, ResourceLoaderAware, ServletContextAware
/*     */ {
/*     */   @Nullable
/*     */   private Configuration configuration;
/*     */   @Nullable
/*     */   private TaglibFactory taglibFactory;
/*     */   
/*     */   public void setConfiguration(Configuration configuration) {
/*  98 */     this.configuration = configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/* 106 */     this.taglibFactory = new TaglibFactory(servletContext);
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
/*     */   public void afterPropertiesSet() throws IOException, TemplateException {
/* 119 */     if (this.configuration == null) {
/* 120 */       this.configuration = createConfiguration();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {
/* 130 */     templateLoaders.add(new ClassTemplateLoader(FreeMarkerConfigurer.class, ""));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration getConfiguration() {
/* 139 */     Assert.state((this.configuration != null), "No Configuration available");
/* 140 */     return this.configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TaglibFactory getTaglibFactory() {
/* 148 */     Assert.state((this.taglibFactory != null), "No TaglibFactory available");
/* 149 */     return this.taglibFactory;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/freemarker/FreeMarkerConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */