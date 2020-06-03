/*     */ package org.springframework.web.servlet.i18n;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.LocaleResolver;
/*     */ import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocaleChangeInterceptor
/*     */   extends HandlerInterceptorAdapter
/*     */ {
/*     */   public static final String DEFAULT_PARAM_NAME = "locale";
/*  51 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  53 */   private String paramName = "locale";
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String[] httpMethods;
/*     */ 
/*     */   
/*     */   private boolean ignoreInvalidLocale = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParamName(String paramName) {
/*  66 */     this.paramName = paramName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParamName() {
/*  74 */     return this.paramName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHttpMethods(@Nullable String... httpMethods) {
/*  83 */     this.httpMethods = httpMethods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getHttpMethods() {
/*  92 */     return this.httpMethods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreInvalidLocale(boolean ignoreInvalidLocale) {
/* 100 */     this.ignoreInvalidLocale = ignoreInvalidLocale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoreInvalidLocale() {
/* 108 */     return this.ignoreInvalidLocale;
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
/*     */   @Deprecated
/*     */   public void setLanguageTagCompliant(boolean languageTagCompliant) {
/* 123 */     if (!languageTagCompliant) {
/* 124 */       throw new IllegalArgumentException("LocaleChangeInterceptor always accepts BCP 47 language tags");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean isLanguageTagCompliant() {
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
/* 144 */     String newLocale = request.getParameter(getParamName());
/* 145 */     if (newLocale != null && 
/* 146 */       checkHttpMethod(request.getMethod())) {
/* 147 */       LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
/* 148 */       if (localeResolver == null) {
/* 149 */         throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
/*     */       }
/*     */       
/*     */       try {
/* 153 */         localeResolver.setLocale(request, response, parseLocaleValue(newLocale));
/*     */       }
/* 155 */       catch (IllegalArgumentException ex) {
/* 156 */         if (isIgnoreInvalidLocale()) {
/* 157 */           if (this.logger.isDebugEnabled()) {
/* 158 */             this.logger.debug("Ignoring invalid locale value [" + newLocale + "]: " + ex.getMessage());
/*     */           }
/*     */         } else {
/*     */           
/* 162 */           throw ex;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 168 */     return true;
/*     */   }
/*     */   
/*     */   private boolean checkHttpMethod(String currentMethod) {
/* 172 */     String[] configuredMethods = getHttpMethods();
/* 173 */     if (ObjectUtils.isEmpty((Object[])configuredMethods)) {
/* 174 */       return true;
/*     */     }
/* 176 */     for (String configuredMethod : configuredMethods) {
/* 177 */       if (configuredMethod.equalsIgnoreCase(currentMethod)) {
/* 178 */         return true;
/*     */       }
/*     */     } 
/* 181 */     return false;
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
/*     */   @Nullable
/*     */   protected Locale parseLocaleValue(String localeValue) {
/* 194 */     return StringUtils.parseLocale(localeValue);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/i18n/LocaleChangeInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */