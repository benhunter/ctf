/*     */ package org.springframework.web.servlet.i18n;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.LocaleResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AcceptHeaderLocaleResolver
/*     */   implements LocaleResolver
/*     */ {
/*  45 */   private final List<Locale> supportedLocales = new ArrayList<>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Locale defaultLocale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSupportedLocales(List<Locale> locales) {
/*  59 */     this.supportedLocales.clear();
/*  60 */     this.supportedLocales.addAll(locales);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Locale> getSupportedLocales() {
/*  68 */     return this.supportedLocales;
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
/*     */   public void setDefaultLocale(@Nullable Locale defaultLocale) {
/*  81 */     this.defaultLocale = defaultLocale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Locale getDefaultLocale() {
/*  90 */     return this.defaultLocale;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale resolveLocale(HttpServletRequest request) {
/*  96 */     Locale defaultLocale = getDefaultLocale();
/*  97 */     if (defaultLocale != null && request.getHeader("Accept-Language") == null) {
/*  98 */       return defaultLocale;
/*     */     }
/* 100 */     Locale requestLocale = request.getLocale();
/* 101 */     List<Locale> supportedLocales = getSupportedLocales();
/* 102 */     if (supportedLocales.isEmpty() || supportedLocales.contains(requestLocale)) {
/* 103 */       return requestLocale;
/*     */     }
/* 105 */     Locale supportedLocale = findSupportedLocale(request, supportedLocales);
/* 106 */     if (supportedLocale != null) {
/* 107 */       return supportedLocale;
/*     */     }
/* 109 */     return (defaultLocale != null) ? defaultLocale : requestLocale;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Locale findSupportedLocale(HttpServletRequest request, List<Locale> supportedLocales) {
/* 114 */     Enumeration<Locale> requestLocales = request.getLocales();
/* 115 */     Locale languageMatch = null;
/* 116 */     while (requestLocales.hasMoreElements()) {
/* 117 */       Locale locale = requestLocales.nextElement();
/* 118 */       if (supportedLocales.contains(locale)) {
/* 119 */         if (languageMatch == null || languageMatch.getLanguage().equals(locale.getLanguage()))
/*     */         {
/* 121 */           return locale; } 
/*     */         continue;
/*     */       } 
/* 124 */       if (languageMatch == null)
/*     */       {
/* 126 */         for (Locale candidate : supportedLocales) {
/* 127 */           if (!StringUtils.hasLength(candidate.getCountry()) && candidate
/* 128 */             .getLanguage().equals(locale.getLanguage())) {
/* 129 */             languageMatch = candidate;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 135 */     return languageMatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
/* 140 */     throw new UnsupportedOperationException("Cannot change HTTP accept header - use a different locale resolution strategy");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/i18n/AcceptHeaderLocaleResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */