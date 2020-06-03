/*     */ package org.springframework.web.jsf;
/*     */ 
/*     */ import javax.faces.application.NavigationHandler;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DecoratingNavigationHandler
/*     */   extends NavigationHandler
/*     */ {
/*     */   @Nullable
/*     */   private NavigationHandler decoratedNavigationHandler;
/*     */   
/*     */   protected DecoratingNavigationHandler() {}
/*     */   
/*     */   protected DecoratingNavigationHandler(NavigationHandler originalNavigationHandler) {
/*  57 */     this.decoratedNavigationHandler = originalNavigationHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final NavigationHandler getDecoratedNavigationHandler() {
/*  66 */     return this.decoratedNavigationHandler;
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
/*     */   public final void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {
/*  78 */     handleNavigation(facesContext, fromAction, outcome, this.decoratedNavigationHandler);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void handleNavigation(FacesContext paramFacesContext, @Nullable String paramString1, @Nullable String paramString2, @Nullable NavigationHandler paramNavigationHandler);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void callNextHandlerInChain(FacesContext facesContext, @Nullable String fromAction, @Nullable String outcome, @Nullable NavigationHandler originalNavigationHandler) {
/* 136 */     NavigationHandler decoratedNavigationHandler = getDecoratedNavigationHandler();
/*     */     
/* 138 */     if (decoratedNavigationHandler instanceof DecoratingNavigationHandler) {
/*     */ 
/*     */       
/* 141 */       DecoratingNavigationHandler decHandler = (DecoratingNavigationHandler)decoratedNavigationHandler;
/* 142 */       decHandler.handleNavigation(facesContext, fromAction, outcome, originalNavigationHandler);
/*     */     }
/* 144 */     else if (decoratedNavigationHandler != null) {
/*     */ 
/*     */ 
/*     */       
/* 148 */       decoratedNavigationHandler.handleNavigation(facesContext, fromAction, outcome);
/*     */     }
/* 150 */     else if (originalNavigationHandler != null) {
/*     */ 
/*     */       
/* 153 */       originalNavigationHandler.handleNavigation(facesContext, fromAction, outcome);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/jsf/DecoratingNavigationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */