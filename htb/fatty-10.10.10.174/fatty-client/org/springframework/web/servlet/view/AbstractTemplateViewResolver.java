/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AbstractTemplateViewResolver
/*     */   extends UrlBasedViewResolver
/*     */ {
/*     */   private boolean exposeRequestAttributes = false;
/*     */   private boolean allowRequestOverride = false;
/*     */   private boolean exposeSessionAttributes = false;
/*     */   private boolean allowSessionOverride = false;
/*     */   private boolean exposeSpringMacroHelpers = true;
/*     */   
/*     */   protected Class<?> requiredViewClass() {
/*  45 */     return AbstractTemplateView.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExposeRequestAttributes(boolean exposeRequestAttributes) {
/*  54 */     this.exposeRequestAttributes = exposeRequestAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowRequestOverride(boolean allowRequestOverride) {
/*  65 */     this.allowRequestOverride = allowRequestOverride;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExposeSessionAttributes(boolean exposeSessionAttributes) {
/*  74 */     this.exposeSessionAttributes = exposeSessionAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowSessionOverride(boolean allowSessionOverride) {
/*  85 */     this.allowSessionOverride = allowSessionOverride;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers) {
/*  94 */     this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractUrlBasedView buildView(String viewName) throws Exception {
/* 100 */     AbstractTemplateView view = (AbstractTemplateView)super.buildView(viewName);
/* 101 */     view.setExposeRequestAttributes(this.exposeRequestAttributes);
/* 102 */     view.setAllowRequestOverride(this.allowRequestOverride);
/* 103 */     view.setExposeSessionAttributes(this.exposeSessionAttributes);
/* 104 */     view.setAllowSessionOverride(this.allowSessionOverride);
/* 105 */     view.setExposeSpringMacroHelpers(this.exposeSpringMacroHelpers);
/* 106 */     return view;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/AbstractTemplateViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */