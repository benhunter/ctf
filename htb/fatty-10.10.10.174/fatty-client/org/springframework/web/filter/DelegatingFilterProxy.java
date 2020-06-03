/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegatingFilterProxy
/*     */   extends GenericFilterBean
/*     */ {
/*     */   @Nullable
/*     */   private String contextAttribute;
/*     */   @Nullable
/*     */   private WebApplicationContext webApplicationContext;
/*     */   @Nullable
/*     */   private String targetBeanName;
/*     */   private boolean targetFilterLifecycle = false;
/*     */   @Nullable
/*     */   private volatile Filter delegate;
/*  98 */   private final Object delegateMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DelegatingFilterProxy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DelegatingFilterProxy(Filter delegate) {
/* 123 */     Assert.notNull(delegate, "Delegate Filter must not be null");
/* 124 */     this.delegate = delegate;
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
/*     */   public DelegatingFilterProxy(String targetBeanName) {
/* 141 */     this(targetBeanName, (WebApplicationContext)null);
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
/*     */   public DelegatingFilterProxy(String targetBeanName, @Nullable WebApplicationContext wac) {
/* 165 */     Assert.hasText(targetBeanName, "Target Filter bean name must not be null or empty");
/* 166 */     setTargetBeanName(targetBeanName);
/* 167 */     this.webApplicationContext = wac;
/* 168 */     if (wac != null) {
/* 169 */       setEnvironment(wac.getEnvironment());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContextAttribute(@Nullable String contextAttribute) {
/* 178 */     this.contextAttribute = contextAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getContextAttribute() {
/* 187 */     return this.contextAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetBeanName(@Nullable String targetBeanName) {
/* 197 */     this.targetBeanName = targetBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getTargetBeanName() {
/* 205 */     return this.targetBeanName;
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
/*     */   public void setTargetFilterLifecycle(boolean targetFilterLifecycle) {
/* 217 */     this.targetFilterLifecycle = targetFilterLifecycle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isTargetFilterLifecycle() {
/* 225 */     return this.targetFilterLifecycle;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initFilterBean() throws ServletException {
/* 231 */     synchronized (this.delegateMonitor) {
/* 232 */       if (this.delegate == null) {
/*     */         
/* 234 */         if (this.targetBeanName == null) {
/* 235 */           this.targetBeanName = getFilterName();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 240 */         WebApplicationContext wac = findWebApplicationContext();
/* 241 */         if (wac != null) {
/* 242 */           this.delegate = initDelegate(wac);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 253 */     Filter delegateToUse = this.delegate;
/* 254 */     if (delegateToUse == null) {
/* 255 */       synchronized (this.delegateMonitor) {
/* 256 */         delegateToUse = this.delegate;
/* 257 */         if (delegateToUse == null) {
/* 258 */           WebApplicationContext wac = findWebApplicationContext();
/* 259 */           if (wac == null) {
/* 260 */             throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener or DispatcherServlet registered?");
/*     */           }
/*     */           
/* 263 */           delegateToUse = initDelegate(wac);
/*     */         } 
/* 265 */         this.delegate = delegateToUse;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 270 */     invokeDelegate(delegateToUse, request, response, filterChain);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 275 */     Filter delegateToUse = this.delegate;
/* 276 */     if (delegateToUse != null) {
/* 277 */       destroyDelegate(delegateToUse);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected WebApplicationContext findWebApplicationContext() {
/* 300 */     if (this.webApplicationContext != null) {
/*     */       
/* 302 */       if (this.webApplicationContext instanceof ConfigurableApplicationContext) {
/* 303 */         ConfigurableApplicationContext cac = (ConfigurableApplicationContext)this.webApplicationContext;
/* 304 */         if (!cac.isActive())
/*     */         {
/* 306 */           cac.refresh();
/*     */         }
/*     */       } 
/* 309 */       return this.webApplicationContext;
/*     */     } 
/* 311 */     String attrName = getContextAttribute();
/* 312 */     if (attrName != null) {
/* 313 */       return WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
/*     */     }
/*     */     
/* 316 */     return WebApplicationContextUtils.findWebApplicationContext(getServletContext());
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
/*     */   protected Filter initDelegate(WebApplicationContext wac) throws ServletException {
/* 335 */     String targetBeanName = getTargetBeanName();
/* 336 */     Assert.state((targetBeanName != null), "No target bean name set");
/* 337 */     Filter delegate = (Filter)wac.getBean(targetBeanName, Filter.class);
/* 338 */     if (isTargetFilterLifecycle()) {
/* 339 */       delegate.init(getFilterConfig());
/*     */     }
/* 341 */     return delegate;
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
/*     */   protected void invokeDelegate(Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 357 */     delegate.doFilter(request, response, filterChain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroyDelegate(Filter delegate) {
/* 368 */     if (isTargetFilterLifecycle())
/* 369 */       delegate.destroy(); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/DelegatingFilterProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */