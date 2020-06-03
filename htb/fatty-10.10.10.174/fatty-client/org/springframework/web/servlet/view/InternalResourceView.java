/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InternalResourceView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*     */   private boolean alwaysInclude = false;
/*     */   private boolean preventDispatchLoop = false;
/*     */   
/*     */   public InternalResourceView() {}
/*     */   
/*     */   public InternalResourceView(String url) {
/*  86 */     super(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternalResourceView(String url, boolean alwaysInclude) {
/*  95 */     super(url);
/*  96 */     this.alwaysInclude = alwaysInclude;
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
/*     */   public void setAlwaysInclude(boolean alwaysInclude) {
/* 109 */     this.alwaysInclude = alwaysInclude;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreventDispatchLoop(boolean preventDispatchLoop) {
/* 120 */     this.preventDispatchLoop = preventDispatchLoop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isContextRequired() {
/* 128 */     return false;
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
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 141 */     exposeModelAsRequestAttributes(model, request);
/*     */ 
/*     */     
/* 144 */     exposeHelpers(request);
/*     */ 
/*     */     
/* 147 */     String dispatcherPath = prepareForRendering(request, response);
/*     */ 
/*     */     
/* 150 */     RequestDispatcher rd = getRequestDispatcher(request, dispatcherPath);
/* 151 */     if (rd == null) {
/* 152 */       throw new ServletException("Could not get RequestDispatcher for [" + getUrl() + "]: Check that the corresponding file exists within your web application archive!");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 157 */     if (useInclude(request, response)) {
/* 158 */       response.setContentType(getContentType());
/* 159 */       if (this.logger.isDebugEnabled()) {
/* 160 */         this.logger.debug("Including [" + getUrl() + "]");
/*     */       }
/* 162 */       rd.include((ServletRequest)request, (ServletResponse)response);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 167 */       if (this.logger.isDebugEnabled()) {
/* 168 */         this.logger.debug("Forwarding to [" + getUrl() + "]");
/*     */       }
/* 170 */       rd.forward((ServletRequest)request, (ServletResponse)response);
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
/*     */   protected void exposeHelpers(HttpServletRequest request) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String prepareForRendering(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 203 */     String path = getUrl();
/* 204 */     Assert.state((path != null), "'url' not set");
/*     */     
/* 206 */     if (this.preventDispatchLoop) {
/* 207 */       String uri = request.getRequestURI();
/* 208 */       if (path.startsWith("/") ? uri.equals(path) : uri.equals(StringUtils.applyRelativePath(uri, path))) {
/* 209 */         throw new ServletException("Circular view path [" + path + "]: would dispatch back to the current handler URL [" + uri + "] again. Check your ViewResolver setup! (Hint: This may be the result of an unspecified view, due to default view name generation.)");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 214 */     return path;
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
/*     */   protected RequestDispatcher getRequestDispatcher(HttpServletRequest request, String path) {
/* 228 */     return request.getRequestDispatcher(path);
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
/*     */   protected boolean useInclude(HttpServletRequest request, HttpServletResponse response) {
/* 246 */     return (this.alwaysInclude || WebUtils.isIncludeRequest((ServletRequest)request) || response.isCommitted());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/InternalResourceView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */