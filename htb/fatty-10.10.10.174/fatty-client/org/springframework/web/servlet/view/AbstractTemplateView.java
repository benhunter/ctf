/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractTemplateView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*     */   public static final String SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE = "springMacroRequestContext";
/*     */   private boolean exposeRequestAttributes = false;
/*     */   private boolean allowRequestOverride = false;
/*     */   private boolean exposeSessionAttributes = false;
/*     */   private boolean allowSessionOverride = false;
/*     */   private boolean exposeSpringMacroHelpers = true;
/*     */   
/*     */   public void setExposeRequestAttributes(boolean exposeRequestAttributes) {
/*  70 */     this.exposeRequestAttributes = exposeRequestAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowRequestOverride(boolean allowRequestOverride) {
/*  80 */     this.allowRequestOverride = allowRequestOverride;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExposeSessionAttributes(boolean exposeSessionAttributes) {
/*  88 */     this.exposeSessionAttributes = exposeSessionAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowSessionOverride(boolean allowSessionOverride) {
/*  98 */     this.allowSessionOverride = allowSessionOverride;
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
/*     */   public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers) {
/* 110 */     this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 118 */     if (this.exposeRequestAttributes) {
/* 119 */       Map<String, Object> exposed = null;
/* 120 */       for (Enumeration<String> en = request.getAttributeNames(); en.hasMoreElements(); ) {
/* 121 */         String attribute = en.nextElement();
/* 122 */         if (model.containsKey(attribute) && !this.allowRequestOverride) {
/* 123 */           throw new ServletException("Cannot expose request attribute '" + attribute + "' because of an existing model object of the same name");
/*     */         }
/*     */         
/* 126 */         Object attributeValue = request.getAttribute(attribute);
/* 127 */         if (this.logger.isDebugEnabled()) {
/* 128 */           exposed = (exposed != null) ? exposed : new LinkedHashMap<>();
/* 129 */           exposed.put(attribute, attributeValue);
/*     */         } 
/* 131 */         model.put(attribute, attributeValue);
/*     */       } 
/* 133 */       if (this.logger.isTraceEnabled() && exposed != null) {
/* 134 */         this.logger.trace("Exposed request attributes to model: " + exposed);
/*     */       }
/*     */     } 
/*     */     
/* 138 */     if (this.exposeSessionAttributes) {
/* 139 */       HttpSession session = request.getSession(false);
/* 140 */       if (session != null) {
/* 141 */         Map<String, Object> exposed = null;
/* 142 */         for (Enumeration<String> en = session.getAttributeNames(); en.hasMoreElements(); ) {
/* 143 */           String attribute = en.nextElement();
/* 144 */           if (model.containsKey(attribute) && !this.allowSessionOverride) {
/* 145 */             throw new ServletException("Cannot expose session attribute '" + attribute + "' because of an existing model object of the same name");
/*     */           }
/*     */           
/* 148 */           Object attributeValue = session.getAttribute(attribute);
/* 149 */           if (this.logger.isDebugEnabled()) {
/* 150 */             exposed = (exposed != null) ? exposed : new LinkedHashMap<>();
/* 151 */             exposed.put(attribute, attributeValue);
/*     */           } 
/* 153 */           model.put(attribute, attributeValue);
/*     */         } 
/* 155 */         if (this.logger.isTraceEnabled() && exposed != null) {
/* 156 */           this.logger.trace("Exposed session attributes to model: " + exposed);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 161 */     if (this.exposeSpringMacroHelpers) {
/* 162 */       if (model.containsKey("springMacroRequestContext")) {
/* 163 */         throw new ServletException("Cannot expose bind macro helper 'springMacroRequestContext' because of an existing model object of the same name");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 168 */       model.put("springMacroRequestContext", new RequestContext(request, response, 
/* 169 */             getServletContext(), model));
/*     */     } 
/*     */     
/* 172 */     applyContentType(response);
/*     */     
/* 174 */     if (this.logger.isDebugEnabled()) {
/* 175 */       this.logger.debug("Rendering [" + getUrl() + "]");
/*     */     }
/*     */     
/* 178 */     renderMergedTemplateModel(model, request, response);
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
/*     */   protected void applyContentType(HttpServletResponse response) {
/* 191 */     if (response.getContentType() == null)
/* 192 */       response.setContentType(getContentType()); 
/*     */   }
/*     */   
/*     */   protected abstract void renderMergedTemplateModel(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws Exception;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/AbstractTemplateView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */