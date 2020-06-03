/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.faces.context.ExternalContext;
/*     */ import javax.faces.context.FacesContext;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.Scope;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.RequestScope;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.context.request.SessionScope;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WebApplicationContextUtils
/*     */ {
/*  68 */   private static final boolean jsfPresent = ClassUtils.isPresent("javax.faces.context.FacesContext", RequestContextHolder.class.getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WebApplicationContext getRequiredWebApplicationContext(ServletContext sc) throws IllegalStateException {
/*  82 */     WebApplicationContext wac = getWebApplicationContext(sc);
/*  83 */     if (wac == null) {
/*  84 */       throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
/*     */     }
/*  86 */     return wac;
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
/*     */   public static WebApplicationContext getWebApplicationContext(ServletContext sc) {
/* 100 */     return getWebApplicationContext(sc, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static WebApplicationContext getWebApplicationContext(ServletContext sc, String attrName) {
/* 111 */     Assert.notNull(sc, "ServletContext must not be null");
/* 112 */     Object attr = sc.getAttribute(attrName);
/* 113 */     if (attr == null) {
/* 114 */       return null;
/*     */     }
/* 116 */     if (attr instanceof RuntimeException) {
/* 117 */       throw (RuntimeException)attr;
/*     */     }
/* 119 */     if (attr instanceof Error) {
/* 120 */       throw (Error)attr;
/*     */     }
/* 122 */     if (attr instanceof Exception) {
/* 123 */       throw new IllegalStateException((Exception)attr);
/*     */     }
/* 125 */     if (!(attr instanceof WebApplicationContext)) {
/* 126 */       throw new IllegalStateException("Context attribute is not of type WebApplicationContext: " + attr);
/*     */     }
/* 128 */     return (WebApplicationContext)attr;
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
/*     */   public static WebApplicationContext findWebApplicationContext(ServletContext sc) {
/* 148 */     WebApplicationContext wac = getWebApplicationContext(sc);
/* 149 */     if (wac == null) {
/* 150 */       Enumeration<String> attrNames = sc.getAttributeNames();
/* 151 */       while (attrNames.hasMoreElements()) {
/* 152 */         String attrName = attrNames.nextElement();
/* 153 */         Object attrValue = sc.getAttribute(attrName);
/* 154 */         if (attrValue instanceof WebApplicationContext) {
/* 155 */           if (wac != null) {
/* 156 */             throw new IllegalStateException("No unique WebApplicationContext found: more than one DispatcherServlet registered with publishContext=true?");
/*     */           }
/*     */           
/* 159 */           wac = (WebApplicationContext)attrValue;
/*     */         } 
/*     */       } 
/*     */     } 
/* 163 */     return wac;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerWebApplicationScopes(ConfigurableListableBeanFactory beanFactory) {
/* 173 */     registerWebApplicationScopes(beanFactory, null);
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
/*     */   public static void registerWebApplicationScopes(ConfigurableListableBeanFactory beanFactory, @Nullable ServletContext sc) {
/* 185 */     beanFactory.registerScope("request", (Scope)new RequestScope());
/* 186 */     beanFactory.registerScope("session", (Scope)new SessionScope());
/* 187 */     if (sc != null) {
/* 188 */       ServletContextScope appScope = new ServletContextScope(sc);
/* 189 */       beanFactory.registerScope("application", appScope);
/*     */       
/* 191 */       sc.setAttribute(ServletContextScope.class.getName(), appScope);
/*     */     } 
/*     */     
/* 194 */     beanFactory.registerResolvableDependency(ServletRequest.class, new RequestObjectFactory());
/* 195 */     beanFactory.registerResolvableDependency(ServletResponse.class, new ResponseObjectFactory());
/* 196 */     beanFactory.registerResolvableDependency(HttpSession.class, new SessionObjectFactory());
/* 197 */     beanFactory.registerResolvableDependency(WebRequest.class, new WebRequestObjectFactory());
/* 198 */     if (jsfPresent) {
/* 199 */       FacesDependencyRegistrar.registerFacesDependencies(beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerEnvironmentBeans(ConfigurableListableBeanFactory bf, @Nullable ServletContext sc) {
/* 210 */     registerEnvironmentBeans(bf, sc, null);
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
/*     */   public static void registerEnvironmentBeans(ConfigurableListableBeanFactory bf, @Nullable ServletContext servletContext, @Nullable ServletConfig servletConfig) {
/* 223 */     if (servletContext != null && !bf.containsBean("servletContext")) {
/* 224 */       bf.registerSingleton("servletContext", servletContext);
/*     */     }
/*     */     
/* 227 */     if (servletConfig != null && !bf.containsBean("servletConfig")) {
/* 228 */       bf.registerSingleton("servletConfig", servletConfig);
/*     */     }
/*     */     
/* 231 */     if (!bf.containsBean("contextParameters")) {
/* 232 */       Map<String, String> parameterMap = new HashMap<>();
/* 233 */       if (servletContext != null) {
/* 234 */         Enumeration<?> paramNameEnum = servletContext.getInitParameterNames();
/* 235 */         while (paramNameEnum.hasMoreElements()) {
/* 236 */           String paramName = (String)paramNameEnum.nextElement();
/* 237 */           parameterMap.put(paramName, servletContext.getInitParameter(paramName));
/*     */         } 
/*     */       } 
/* 240 */       if (servletConfig != null) {
/* 241 */         Enumeration<?> paramNameEnum = servletConfig.getInitParameterNames();
/* 242 */         while (paramNameEnum.hasMoreElements()) {
/* 243 */           String paramName = (String)paramNameEnum.nextElement();
/* 244 */           parameterMap.put(paramName, servletConfig.getInitParameter(paramName));
/*     */         } 
/*     */       } 
/* 247 */       bf.registerSingleton("contextParameters", 
/* 248 */           Collections.unmodifiableMap(parameterMap));
/*     */     } 
/*     */     
/* 251 */     if (!bf.containsBean("contextAttributes")) {
/* 252 */       Map<String, Object> attributeMap = new HashMap<>();
/* 253 */       if (servletContext != null) {
/* 254 */         Enumeration<?> attrNameEnum = servletContext.getAttributeNames();
/* 255 */         while (attrNameEnum.hasMoreElements()) {
/* 256 */           String attrName = (String)attrNameEnum.nextElement();
/* 257 */           attributeMap.put(attrName, servletContext.getAttribute(attrName));
/*     */         } 
/*     */       } 
/* 260 */       bf.registerSingleton("contextAttributes", 
/* 261 */           Collections.unmodifiableMap(attributeMap));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initServletPropertySources(MutablePropertySources propertySources, ServletContext servletContext) {
/* 272 */     initServletPropertySources(propertySources, servletContext, null);
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
/*     */   
/*     */   public static void initServletPropertySources(MutablePropertySources sources, @Nullable ServletContext servletContext, @Nullable ServletConfig servletConfig) {
/* 296 */     Assert.notNull(sources, "'propertySources' must not be null");
/* 297 */     String name = "servletContextInitParams";
/* 298 */     if (servletContext != null && sources.contains(name) && sources.get(name) instanceof PropertySource.StubPropertySource) {
/* 299 */       sources.replace(name, (PropertySource)new ServletContextPropertySource(name, servletContext));
/*     */     }
/* 301 */     name = "servletConfigInitParams";
/* 302 */     if (servletConfig != null && sources.contains(name) && sources.get(name) instanceof PropertySource.StubPropertySource) {
/* 303 */       sources.replace(name, (PropertySource)new ServletConfigPropertySource(name, servletConfig));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ServletRequestAttributes currentRequestAttributes() {
/* 312 */     RequestAttributes requestAttr = RequestContextHolder.currentRequestAttributes();
/* 313 */     if (!(requestAttr instanceof ServletRequestAttributes)) {
/* 314 */       throw new IllegalStateException("Current request is not a servlet request");
/*     */     }
/* 316 */     return (ServletRequestAttributes)requestAttr;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RequestObjectFactory
/*     */     implements ObjectFactory<ServletRequest>, Serializable
/*     */   {
/*     */     private RequestObjectFactory() {}
/*     */ 
/*     */     
/*     */     public ServletRequest getObject() {
/* 328 */       return (ServletRequest)WebApplicationContextUtils.currentRequestAttributes().getRequest();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 333 */       return "Current HttpServletRequest";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ResponseObjectFactory
/*     */     implements ObjectFactory<ServletResponse>, Serializable
/*     */   {
/*     */     private ResponseObjectFactory() {}
/*     */ 
/*     */     
/*     */     public ServletResponse getObject() {
/* 346 */       HttpServletResponse httpServletResponse = WebApplicationContextUtils.currentRequestAttributes().getResponse();
/* 347 */       if (httpServletResponse == null) {
/* 348 */         throw new IllegalStateException("Current servlet response not available - consider using RequestContextFilter instead of RequestContextListener");
/*     */       }
/*     */       
/* 351 */       return (ServletResponse)httpServletResponse;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 356 */       return "Current HttpServletResponse";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SessionObjectFactory
/*     */     implements ObjectFactory<HttpSession>, Serializable
/*     */   {
/*     */     private SessionObjectFactory() {}
/*     */ 
/*     */     
/*     */     public HttpSession getObject() {
/* 369 */       return WebApplicationContextUtils.currentRequestAttributes().getRequest().getSession();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 374 */       return "Current HttpSession";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class WebRequestObjectFactory
/*     */     implements ObjectFactory<WebRequest>, Serializable
/*     */   {
/*     */     private WebRequestObjectFactory() {}
/*     */ 
/*     */     
/*     */     public WebRequest getObject() {
/* 387 */       ServletRequestAttributes requestAttr = WebApplicationContextUtils.currentRequestAttributes();
/* 388 */       return (WebRequest)new ServletWebRequest(requestAttr.getRequest(), requestAttr.getResponse());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 393 */       return "Current ServletWebRequest";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FacesDependencyRegistrar
/*     */   {
/*     */     public static void registerFacesDependencies(ConfigurableListableBeanFactory beanFactory) {
/* 404 */       beanFactory.registerResolvableDependency(FacesContext.class, new ObjectFactory<FacesContext>()
/*     */           {
/*     */             public FacesContext getObject() {
/* 407 */               return FacesContext.getCurrentInstance();
/*     */             }
/*     */             
/*     */             public String toString() {
/* 411 */               return "Current JSF FacesContext";
/*     */             }
/*     */           });
/* 414 */       beanFactory.registerResolvableDependency(ExternalContext.class, new ObjectFactory<ExternalContext>()
/*     */           {
/*     */             public ExternalContext getObject() {
/* 417 */               return FacesContext.getCurrentInstance().getExternalContext();
/*     */             }
/*     */             
/*     */             public String toString() {
/* 421 */               return "Current JSF ExternalContext";
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/WebApplicationContextUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */