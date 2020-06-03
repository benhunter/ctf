/*     */ package org.springframework.web.servlet.view.groovy;
/*     */ 
/*     */ import groovy.text.Template;
/*     */ import groovy.text.markup.MarkupTemplateEngine;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.view.AbstractTemplateView;
/*     */ import org.springframework.web.util.NestedServletException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GroovyMarkupView
/*     */   extends AbstractTemplateView
/*     */ {
/*     */   @Nullable
/*     */   private MarkupTemplateEngine engine;
/*     */   
/*     */   public void setTemplateEngine(MarkupTemplateEngine engine) {
/*  66 */     this.engine = engine;
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
/*     */   protected void initApplicationContext(ApplicationContext context) {
/*  79 */     initApplicationContext();
/*  80 */     if (this.engine == null) {
/*  81 */       setTemplateEngine(autodetectMarkupTemplateEngine());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MarkupTemplateEngine autodetectMarkupTemplateEngine() throws BeansException {
/*     */     try {
/*  91 */       return ((GroovyMarkupConfig)BeanFactoryUtils.beanOfTypeIncludingAncestors((ListableBeanFactory)obtainApplicationContext(), GroovyMarkupConfig.class, true, false))
/*  92 */         .getTemplateEngine();
/*     */     }
/*  94 */     catch (NoSuchBeanDefinitionException ex) {
/*  95 */       throw new ApplicationContextException("Expected a single GroovyMarkupConfig bean in the current Servlet web application context or the parent root context: GroovyMarkupConfigurer is the usual implementation. This bean may have any name.", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkResource(Locale locale) throws Exception {
/* 104 */     Assert.state((this.engine != null), "No MarkupTemplateEngine set");
/*     */     try {
/* 106 */       this.engine.resolveTemplate(getUrl());
/*     */     }
/* 108 */     catch (IOException ex) {
/* 109 */       return false;
/*     */     } 
/* 111 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 118 */     String url = getUrl();
/* 119 */     Assert.state((url != null), "'url' not set");
/*     */     
/* 121 */     Template template = getTemplate(url);
/* 122 */     template.make(model).writeTo(new BufferedWriter(response.getWriter()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Template getTemplate(String viewUrl) throws Exception {
/* 130 */     Assert.state((this.engine != null), "No MarkupTemplateEngine set");
/*     */     try {
/* 132 */       return this.engine.createTemplateByPath(viewUrl);
/*     */     }
/* 134 */     catch (ClassNotFoundException ex) {
/* 135 */       Throwable cause = (ex.getCause() != null) ? ex.getCause() : ex;
/* 136 */       throw new NestedServletException("Could not find class while rendering Groovy Markup view with name '" + 
/*     */           
/* 138 */           getUrl() + "': " + ex.getMessage() + "'", cause);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/groovy/GroovyMarkupView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */