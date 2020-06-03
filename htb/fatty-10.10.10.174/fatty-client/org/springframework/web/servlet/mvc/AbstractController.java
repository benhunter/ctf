/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.support.WebContentGenerator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractController
/*     */   extends WebContentGenerator
/*     */   implements Controller
/*     */ {
/*     */   private boolean synchronizeOnSession = false;
/*     */   
/*     */   public AbstractController() {
/* 106 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractController(boolean restrictDefaultSupportedMethods) {
/* 117 */     super(restrictDefaultSupportedMethods);
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
/*     */   public final void setSynchronizeOnSession(boolean synchronizeOnSession) {
/* 141 */     this.synchronizeOnSession = synchronizeOnSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isSynchronizeOnSession() {
/* 148 */     return this.synchronizeOnSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 157 */     if (HttpMethod.OPTIONS.matches(request.getMethod())) {
/* 158 */       response.setHeader("Allow", getAllowHeader());
/* 159 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 163 */     checkRequest(request);
/* 164 */     prepareResponse(response);
/*     */ 
/*     */     
/* 167 */     if (this.synchronizeOnSession) {
/* 168 */       HttpSession session = request.getSession(false);
/* 169 */       if (session != null) {
/* 170 */         Object mutex = WebUtils.getSessionMutex(session);
/* 171 */         synchronized (mutex) {
/* 172 */           return handleRequestInternal(request, response);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 177 */     return handleRequestInternal(request, response);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract ModelAndView handleRequestInternal(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws Exception;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/AbstractController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */