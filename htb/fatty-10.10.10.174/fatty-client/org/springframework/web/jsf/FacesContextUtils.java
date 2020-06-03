/*     */ package org.springframework.web.jsf;
/*     */ 
/*     */ import javax.faces.context.ExternalContext;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.WebApplicationContext;
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
/*     */ public abstract class FacesContextUtils
/*     */ {
/*     */   @Nullable
/*     */   public static WebApplicationContext getWebApplicationContext(FacesContext fc) {
/*  52 */     Assert.notNull(fc, "FacesContext must not be null");
/*  53 */     Object attr = fc.getExternalContext().getApplicationMap().get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*     */     
/*  55 */     if (attr == null) {
/*  56 */       return null;
/*     */     }
/*  58 */     if (attr instanceof RuntimeException) {
/*  59 */       throw (RuntimeException)attr;
/*     */     }
/*  61 */     if (attr instanceof Error) {
/*  62 */       throw (Error)attr;
/*     */     }
/*  64 */     if (!(attr instanceof WebApplicationContext)) {
/*  65 */       throw new IllegalStateException("Root context attribute is not of type WebApplicationContext: " + attr);
/*     */     }
/*  67 */     return (WebApplicationContext)attr;
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
/*     */   public static WebApplicationContext getRequiredWebApplicationContext(FacesContext fc) throws IllegalStateException {
/*  81 */     WebApplicationContext wac = getWebApplicationContext(fc);
/*  82 */     if (wac == null) {
/*  83 */       throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
/*     */     }
/*  85 */     return wac;
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
/*     */   @Nullable
/*     */   public static Object getSessionMutex(FacesContext fc) {
/* 110 */     Assert.notNull(fc, "FacesContext must not be null");
/* 111 */     ExternalContext ec = fc.getExternalContext();
/* 112 */     Object mutex = ec.getSessionMap().get(WebUtils.SESSION_MUTEX_ATTRIBUTE);
/* 113 */     if (mutex == null) {
/* 114 */       mutex = ec.getSession(true);
/*     */     }
/* 116 */     return mutex;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/jsf/FacesContextUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */