/*     */ package org.springframework.web.servlet.view.freemarker;
/*     */ 
/*     */ import freemarker.core.ParseException;
/*     */ import freemarker.ext.jsp.TaglibFactory;
/*     */ import freemarker.ext.servlet.AllHttpScopesHashModel;
/*     */ import freemarker.ext.servlet.HttpRequestHashModel;
/*     */ import freemarker.ext.servlet.HttpRequestParametersHashModel;
/*     */ import freemarker.ext.servlet.HttpSessionHashModel;
/*     */ import freemarker.ext.servlet.ServletContextHashModel;
/*     */ import freemarker.template.Configuration;
/*     */ import freemarker.template.DefaultObjectWrapperBuilder;
/*     */ import freemarker.template.ObjectWrapper;
/*     */ import freemarker.template.SimpleHash;
/*     */ import freemarker.template.Template;
/*     */ import freemarker.template.TemplateException;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.GenericServlet;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ import org.springframework.web.servlet.view.AbstractTemplateView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FreeMarkerView
/*     */   extends AbstractTemplateView
/*     */ {
/*     */   @Nullable
/*     */   private String encoding;
/*     */   @Nullable
/*     */   private Configuration configuration;
/*     */   @Nullable
/*     */   private TaglibFactory taglibFactory;
/*     */   @Nullable
/*     */   private ServletContextHashModel servletContextHashModel;
/*     */   
/*     */   public void setEncoding(@Nullable String encoding) {
/* 110 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getEncoding() {
/* 118 */     return this.encoding;
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
/*     */   public void setConfiguration(@Nullable Configuration configuration) {
/* 131 */     this.configuration = configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Configuration getConfiguration() {
/* 139 */     return this.configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Configuration obtainConfiguration() {
/* 149 */     Configuration configuration = getConfiguration();
/* 150 */     Assert.state((configuration != null), "No Configuration set");
/* 151 */     return configuration;
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
/*     */   protected void initServletContext(ServletContext servletContext) throws BeansException {
/* 165 */     if (getConfiguration() != null) {
/* 166 */       this.taglibFactory = new TaglibFactory(servletContext);
/*     */     } else {
/*     */       
/* 169 */       FreeMarkerConfig config = autodetectConfiguration();
/* 170 */       setConfiguration(config.getConfiguration());
/* 171 */       this.taglibFactory = config.getTaglibFactory();
/*     */     } 
/*     */     
/* 174 */     GenericServlet servlet = new GenericServletAdapter();
/*     */     try {
/* 176 */       servlet.init(new DelegatingServletConfig());
/*     */     }
/* 178 */     catch (ServletException ex) {
/* 179 */       throw new BeanInitializationException("Initialization of GenericServlet adapter failed", ex);
/*     */     } 
/* 181 */     this.servletContextHashModel = new ServletContextHashModel(servlet, getObjectWrapper());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FreeMarkerConfig autodetectConfiguration() throws BeansException {
/*     */     try {
/* 193 */       return (FreeMarkerConfig)BeanFactoryUtils.beanOfTypeIncludingAncestors((ListableBeanFactory)
/* 194 */           obtainApplicationContext(), FreeMarkerConfig.class, true, false);
/*     */     }
/* 196 */     catch (NoSuchBeanDefinitionException ex) {
/* 197 */       throw new ApplicationContextException("Must define a single FreeMarkerConfig bean in this web application context (may be inherited): FreeMarkerConfigurer is the usual implementation. This bean may be given any name.", ex);
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
/*     */   protected ObjectWrapper getObjectWrapper() {
/* 210 */     ObjectWrapper ow = obtainConfiguration().getObjectWrapper();
/* 211 */     return (ow != null) ? ow : (ObjectWrapper)(new DefaultObjectWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS))
/* 212 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkResource(Locale locale) throws Exception {
/* 222 */     String url = getUrl();
/* 223 */     Assert.state((url != null), "'url' not set");
/*     */ 
/*     */     
/*     */     try {
/* 227 */       getTemplate(url, locale);
/* 228 */       return true;
/*     */     }
/* 230 */     catch (FileNotFoundException ex) {
/*     */       
/* 232 */       return false;
/*     */     }
/* 234 */     catch (ParseException ex) {
/* 235 */       throw new ApplicationContextException("Failed to parse [" + url + "]", ex);
/*     */     }
/* 237 */     catch (IOException ex) {
/* 238 */       throw new ApplicationContextException("Failed to load [" + url + "]", ex);
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
/*     */   protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 252 */     exposeHelpers(model, request);
/* 253 */     doRender(model, request, response);
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
/*     */   
/*     */   protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 296 */     exposeModelAsRequestAttributes(model, request);
/*     */     
/* 298 */     SimpleHash fmModel = buildTemplateModel(model, request, response);
/*     */ 
/*     */     
/* 301 */     Locale locale = RequestContextUtils.getLocale(request);
/* 302 */     processTemplate(getTemplate(locale), fmModel, response);
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
/*     */   protected SimpleHash buildTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
/* 316 */     AllHttpScopesHashModel fmModel = new AllHttpScopesHashModel(getObjectWrapper(), getServletContext(), request);
/* 317 */     fmModel.put("JspTaglibs", this.taglibFactory);
/* 318 */     fmModel.put("Application", this.servletContextHashModel);
/* 319 */     fmModel.put("Session", buildSessionModel(request, response));
/* 320 */     fmModel.put("Request", new HttpRequestHashModel(request, response, getObjectWrapper()));
/* 321 */     fmModel.put("RequestParameters", new HttpRequestParametersHashModel(request));
/* 322 */     fmModel.putAll(model);
/* 323 */     return (SimpleHash)fmModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpSessionHashModel buildSessionModel(HttpServletRequest request, HttpServletResponse response) {
/* 334 */     HttpSession session = request.getSession(false);
/* 335 */     if (session != null) {
/* 336 */       return new HttpSessionHashModel(session, getObjectWrapper());
/*     */     }
/*     */     
/* 339 */     return new HttpSessionHashModel(null, request, response, getObjectWrapper());
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
/*     */   protected Template getTemplate(Locale locale) throws IOException {
/* 355 */     String url = getUrl();
/* 356 */     Assert.state((url != null), "'url' not set");
/* 357 */     return getTemplate(url, locale);
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
/*     */   protected Template getTemplate(String name, Locale locale) throws IOException {
/* 371 */     return (getEncoding() != null) ? 
/* 372 */       obtainConfiguration().getTemplate(name, locale, getEncoding()) : 
/* 373 */       obtainConfiguration().getTemplate(name, locale);
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
/*     */   protected void processTemplate(Template template, SimpleHash model, HttpServletResponse response) throws IOException, TemplateException {
/* 389 */     template.process(model, response.getWriter());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class GenericServletAdapter
/*     */     extends GenericServlet
/*     */   {
/*     */     private GenericServletAdapter() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void service(ServletRequest servletRequest, ServletResponse servletResponse) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class DelegatingServletConfig
/*     */     implements ServletConfig
/*     */   {
/*     */     private DelegatingServletConfig() {}
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getServletName() {
/* 416 */       return FreeMarkerView.this.getBeanName();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ServletContext getServletContext() {
/* 422 */       return FreeMarkerView.this.getServletContext();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getInitParameter(String paramName) {
/* 428 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Enumeration<String> getInitParameterNames() {
/* 433 */       return Collections.enumeration(Collections.emptySet());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/freemarker/FreeMarkerView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */