/*     */ package org.springframework.web.servlet.theme;
/*     */ 
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.ThemeResolver;
/*     */ import org.springframework.web.util.CookieGenerator;
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
/*     */ public class CookieThemeResolver
/*     */   extends CookieGenerator
/*     */   implements ThemeResolver
/*     */ {
/*     */   public static final String ORIGINAL_DEFAULT_THEME_NAME = "theme";
/*  57 */   public static final String THEME_REQUEST_ATTRIBUTE_NAME = CookieThemeResolver.class.getName() + ".THEME";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final String DEFAULT_COOKIE_NAME = CookieThemeResolver.class.getName() + ".THEME";
/*     */ 
/*     */   
/*  65 */   private String defaultThemeName = "theme";
/*     */ 
/*     */   
/*     */   public CookieThemeResolver() {
/*  69 */     setCookieName(DEFAULT_COOKIE_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultThemeName(String defaultThemeName) {
/*  77 */     this.defaultThemeName = defaultThemeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultThemeName() {
/*  84 */     return this.defaultThemeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String resolveThemeName(HttpServletRequest request) {
/*  91 */     String themeName = (String)request.getAttribute(THEME_REQUEST_ATTRIBUTE_NAME);
/*  92 */     if (themeName != null) {
/*  93 */       return themeName;
/*     */     }
/*     */ 
/*     */     
/*  97 */     String cookieName = getCookieName();
/*  98 */     if (cookieName != null) {
/*  99 */       Cookie cookie = WebUtils.getCookie(request, cookieName);
/* 100 */       if (cookie != null) {
/* 101 */         String value = cookie.getValue();
/* 102 */         if (StringUtils.hasText(value)) {
/* 103 */           themeName = value;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 109 */     if (themeName == null) {
/* 110 */       themeName = getDefaultThemeName();
/*     */     }
/* 112 */     request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, themeName);
/* 113 */     return themeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThemeName(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable String themeName) {
/* 120 */     Assert.notNull(response, "HttpServletResponse is required for CookieThemeResolver");
/*     */     
/* 122 */     if (StringUtils.hasText(themeName)) {
/*     */       
/* 124 */       request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, themeName);
/* 125 */       addCookie(response, themeName);
/*     */     }
/*     */     else {
/*     */       
/* 129 */       request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, getDefaultThemeName());
/* 130 */       removeCookie(response);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/theme/CookieThemeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */