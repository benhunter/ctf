/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import javax.servlet.jsp.jstl.core.Config;
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
/*     */ public class JspAwareRequestContext
/*     */   extends RequestContext
/*     */ {
/*     */   private PageContext pageContext;
/*     */   
/*     */   public JspAwareRequestContext(PageContext pageContext) {
/*  51 */     this(pageContext, (Map<String, Object>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JspAwareRequestContext(PageContext pageContext, @Nullable Map<String, Object> model) {
/*  62 */     super((HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse(), pageContext
/*  63 */         .getServletContext(), model);
/*  64 */     this.pageContext = pageContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PageContext getPageContext() {
/*  73 */     return this.pageContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Locale getFallbackLocale() {
/*  83 */     if (jstlPresent) {
/*  84 */       Locale locale = JstlPageLocaleResolver.getJstlLocale(getPageContext());
/*  85 */       if (locale != null) {
/*  86 */         return locale;
/*     */       }
/*     */     } 
/*  89 */     return getRequest().getLocale();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TimeZone getFallbackTimeZone() {
/*  98 */     if (jstlPresent) {
/*  99 */       TimeZone timeZone = JstlPageLocaleResolver.getJstlTimeZone(getPageContext());
/* 100 */       if (timeZone != null) {
/* 101 */         return timeZone;
/*     */       }
/*     */     } 
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JstlPageLocaleResolver
/*     */   {
/*     */     @Nullable
/*     */     public static Locale getJstlLocale(PageContext pageContext) {
/* 116 */       Object localeObject = Config.find(pageContext, "javax.servlet.jsp.jstl.fmt.locale");
/* 117 */       return (localeObject instanceof Locale) ? (Locale)localeObject : null;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public static TimeZone getJstlTimeZone(PageContext pageContext) {
/* 122 */       Object timeZoneObject = Config.find(pageContext, "javax.servlet.jsp.jstl.fmt.timeZone");
/* 123 */       return (timeZoneObject instanceof TimeZone) ? (TimeZone)timeZoneObject : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/JspAwareRequestContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */