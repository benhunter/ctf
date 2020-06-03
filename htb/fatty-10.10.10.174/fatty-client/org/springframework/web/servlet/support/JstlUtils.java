/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.servlet.jsp.jstl.core.Config;
/*     */ import javax.servlet.jsp.jstl.fmt.LocalizationContext;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.support.MessageSourceResourceBundle;
/*     */ import org.springframework.context.support.ResourceBundleMessageSource;
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
/*     */ public abstract class JstlUtils
/*     */ {
/*     */   public static MessageSource getJstlAwareMessageSource(@Nullable ServletContext servletContext, MessageSource messageSource) {
/*  57 */     if (servletContext != null) {
/*  58 */       String jstlInitParam = servletContext.getInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext");
/*  59 */       if (jstlInitParam != null) {
/*     */ 
/*     */ 
/*     */         
/*  63 */         ResourceBundleMessageSource jstlBundleWrapper = new ResourceBundleMessageSource();
/*  64 */         jstlBundleWrapper.setBasename(jstlInitParam);
/*  65 */         jstlBundleWrapper.setParentMessageSource(messageSource);
/*  66 */         return (MessageSource)jstlBundleWrapper;
/*     */       } 
/*     */     } 
/*  69 */     return messageSource;
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
/*     */   public static void exposeLocalizationContext(HttpServletRequest request, @Nullable MessageSource messageSource) {
/*  82 */     Locale jstlLocale = RequestContextUtils.getLocale(request);
/*  83 */     Config.set((ServletRequest)request, "javax.servlet.jsp.jstl.fmt.locale", jstlLocale);
/*  84 */     TimeZone timeZone = RequestContextUtils.getTimeZone(request);
/*  85 */     if (timeZone != null) {
/*  86 */       Config.set((ServletRequest)request, "javax.servlet.jsp.jstl.fmt.timeZone", timeZone);
/*     */     }
/*  88 */     if (messageSource != null) {
/*  89 */       LocalizationContext jstlContext = new SpringLocalizationContext(messageSource, request);
/*  90 */       Config.set((ServletRequest)request, "javax.servlet.jsp.jstl.fmt.localizationContext", jstlContext);
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
/*     */   public static void exposeLocalizationContext(RequestContext requestContext) {
/* 102 */     Config.set((ServletRequest)requestContext.getRequest(), "javax.servlet.jsp.jstl.fmt.locale", requestContext.getLocale());
/* 103 */     TimeZone timeZone = requestContext.getTimeZone();
/* 104 */     if (timeZone != null) {
/* 105 */       Config.set((ServletRequest)requestContext.getRequest(), "javax.servlet.jsp.jstl.fmt.timeZone", timeZone);
/*     */     }
/* 107 */     MessageSource messageSource = getJstlAwareMessageSource(requestContext
/* 108 */         .getServletContext(), requestContext.getMessageSource());
/* 109 */     LocalizationContext jstlContext = new SpringLocalizationContext(messageSource, requestContext.getRequest());
/* 110 */     Config.set((ServletRequest)requestContext.getRequest(), "javax.servlet.jsp.jstl.fmt.localizationContext", jstlContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SpringLocalizationContext
/*     */     extends LocalizationContext
/*     */   {
/*     */     private final MessageSource messageSource;
/*     */ 
/*     */     
/*     */     private final HttpServletRequest request;
/*     */ 
/*     */     
/*     */     public SpringLocalizationContext(MessageSource messageSource, HttpServletRequest request) {
/* 125 */       this.messageSource = messageSource;
/* 126 */       this.request = request;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceBundle getResourceBundle() {
/* 131 */       HttpSession session = this.request.getSession(false);
/* 132 */       if (session != null) {
/* 133 */         Object lcObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.localizationContext");
/* 134 */         if (lcObject instanceof LocalizationContext) {
/* 135 */           ResourceBundle lcBundle = ((LocalizationContext)lcObject).getResourceBundle();
/* 136 */           return (ResourceBundle)new MessageSourceResourceBundle(this.messageSource, getLocale(), lcBundle);
/*     */         } 
/*     */       } 
/* 139 */       return (ResourceBundle)new MessageSourceResourceBundle(this.messageSource, getLocale());
/*     */     }
/*     */ 
/*     */     
/*     */     public Locale getLocale() {
/* 144 */       HttpSession session = this.request.getSession(false);
/* 145 */       if (session != null) {
/* 146 */         Object localeObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.locale");
/* 147 */         if (localeObject instanceof Locale) {
/* 148 */           return (Locale)localeObject;
/*     */         }
/*     */       } 
/* 151 */       return RequestContextUtils.getLocale(this.request);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/JstlUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */