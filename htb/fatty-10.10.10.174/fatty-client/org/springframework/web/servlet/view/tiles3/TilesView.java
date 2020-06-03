/*     */ package org.springframework.web.servlet.view.tiles3;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.tiles.TilesContainer;
/*     */ import org.apache.tiles.access.TilesAccess;
/*     */ import org.apache.tiles.renderer.DefinitionRenderer;
/*     */ import org.apache.tiles.request.AbstractRequest;
/*     */ import org.apache.tiles.request.ApplicationContext;
/*     */ import org.apache.tiles.request.Request;
/*     */ import org.apache.tiles.request.render.Renderer;
/*     */ import org.apache.tiles.request.servlet.ServletRequest;
/*     */ import org.apache.tiles.request.servlet.ServletUtil;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ import org.springframework.web.servlet.support.JstlUtils;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TilesView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*     */   @Nullable
/*     */   private Renderer renderer;
/*     */   private boolean exposeJstlAttributes = true;
/*     */   private boolean alwaysInclude = false;
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */   public void setRenderer(Renderer renderer) {
/*  74 */     this.renderer = renderer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setExposeJstlAttributes(boolean exposeJstlAttributes) {
/*  82 */     this.exposeJstlAttributes = exposeJstlAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysInclude(boolean alwaysInclude) {
/*  93 */     this.alwaysInclude = alwaysInclude;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/*  98 */     super.afterPropertiesSet();
/*     */     
/* 100 */     ServletContext servletContext = getServletContext();
/* 101 */     Assert.state((servletContext != null), "No ServletContext");
/* 102 */     this.applicationContext = ServletUtil.getApplicationContext(servletContext);
/*     */     
/* 104 */     if (this.renderer == null) {
/* 105 */       TilesContainer container = TilesAccess.getContainer(this.applicationContext);
/* 106 */       this.renderer = (Renderer)new DefinitionRenderer(container);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkResource(final Locale locale) throws Exception {
/* 113 */     Assert.state((this.renderer != null), "No Renderer set");
/*     */     
/* 115 */     HttpServletRequest servletRequest = null;
/* 116 */     RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
/* 117 */     if (requestAttributes instanceof ServletRequestAttributes) {
/* 118 */       servletRequest = ((ServletRequestAttributes)requestAttributes).getRequest();
/*     */     }
/*     */     
/* 121 */     ServletRequest servletRequest1 = new ServletRequest(this.applicationContext, servletRequest, null)
/*     */       {
/*     */         public Locale getRequestLocale() {
/* 124 */           return locale;
/*     */         }
/*     */       };
/*     */     
/* 128 */     return this.renderer.isRenderable(getUrl(), (Request)servletRequest1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 135 */     Assert.state((this.renderer != null), "No Renderer set");
/*     */     
/* 137 */     exposeModelAsRequestAttributes(model, request);
/* 138 */     if (this.exposeJstlAttributes) {
/* 139 */       JstlUtils.exposeLocalizationContext(new RequestContext(request, getServletContext()));
/*     */     }
/* 141 */     if (this.alwaysInclude) {
/* 142 */       request.setAttribute(AbstractRequest.FORCE_INCLUDE_ATTRIBUTE_NAME, Boolean.valueOf(true));
/*     */     }
/*     */     
/* 145 */     Request tilesRequest = createTilesRequest(request, response);
/* 146 */     this.renderer.render(getUrl(), tilesRequest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Request createTilesRequest(final HttpServletRequest request, HttpServletResponse response) {
/* 157 */     return (Request)new ServletRequest(this.applicationContext, request, response)
/*     */       {
/*     */         public Locale getRequestLocale() {
/* 160 */           return RequestContextUtils.getLocale(request);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/tiles3/TilesView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */