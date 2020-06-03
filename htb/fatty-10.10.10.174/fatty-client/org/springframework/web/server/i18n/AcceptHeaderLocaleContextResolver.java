/*     */ package org.springframework.web.server.i18n;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.SimpleLocaleContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.server.ServerWebExchange;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AcceptHeaderLocaleContextResolver
/*     */   implements LocaleContextResolver
/*     */ {
/*  45 */   private final List<Locale> supportedLocales = new ArrayList<>(4);
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
/*     */   public void setSupportedLocales(List<Locale> locales) {
/*  57 */     this.supportedLocales.clear();
/*  58 */     this.supportedLocales.addAll(locales);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Locale> getSupportedLocales() {
/*  65 */     return this.supportedLocales;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultLocale(@Nullable Locale defaultLocale) {
/*  74 */     this.defaultLocale = defaultLocale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Locale getDefaultLocale() {
/*  82 */     return this.defaultLocale;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
/*  88 */     List<Locale> requestLocales = null;
/*     */     try {
/*  90 */       requestLocales = exchange.getRequest().getHeaders().getAcceptLanguageAsLocales();
/*     */     }
/*  92 */     catch (IllegalArgumentException illegalArgumentException) {}
/*     */ 
/*     */     
/*  95 */     return (LocaleContext)new SimpleLocaleContext(resolveSupportedLocale(requestLocales));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Locale resolveSupportedLocale(@Nullable List<Locale> requestLocales) {
/* 100 */     if (CollectionUtils.isEmpty(requestLocales)) {
/* 101 */       return this.defaultLocale;
/*     */     }
/* 103 */     List<Locale> supportedLocales = getSupportedLocales();
/* 104 */     if (supportedLocales.isEmpty()) {
/* 105 */       return requestLocales.get(0);
/*     */     }
/*     */     
/* 108 */     Locale languageMatch = null;
/* 109 */     for (Locale locale : requestLocales) {
/* 110 */       if (supportedLocales.contains(locale)) {
/* 111 */         if (languageMatch == null || languageMatch.getLanguage().equals(locale.getLanguage()))
/*     */         {
/* 113 */           return locale; } 
/*     */         continue;
/*     */       } 
/* 116 */       if (languageMatch == null)
/*     */       {
/* 118 */         for (Locale candidate : supportedLocales) {
/* 119 */           if (!StringUtils.hasLength(candidate.getCountry()) && candidate
/* 120 */             .getLanguage().equals(locale.getLanguage())) {
/* 121 */             languageMatch = candidate;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 127 */     if (languageMatch != null) {
/* 128 */       return languageMatch;
/*     */     }
/*     */     
/* 131 */     return (this.defaultLocale != null) ? this.defaultLocale : requestLocales.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocaleContext(ServerWebExchange exchange, @Nullable LocaleContext locale) {
/* 136 */     throw new UnsupportedOperationException("Cannot change HTTP accept header - use a different locale context resolution strategy");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/i18n/AcceptHeaderLocaleContextResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */