/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.PropertyPlaceholderHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ServletContextPropertyUtils
/*     */ {
/*  39 */   private static final PropertyPlaceholderHelper strictHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
/*     */ 
/*     */ 
/*     */   
/*  43 */   private static final PropertyPlaceholderHelper nonStrictHelper = new PropertyPlaceholderHelper("${", "}", ":", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String resolvePlaceholders(String text, ServletContext servletContext) {
/*  60 */     return resolvePlaceholders(text, servletContext, false);
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
/*     */   public static String resolvePlaceholders(String text, ServletContext servletContext, boolean ignoreUnresolvablePlaceholders) {
/*  79 */     PropertyPlaceholderHelper helper = ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper;
/*  80 */     return helper.replacePlaceholders(text, new ServletContextPlaceholderResolver(text, servletContext));
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ServletContextPlaceholderResolver
/*     */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*     */   {
/*     */     private final String text;
/*     */     
/*     */     private final ServletContext servletContext;
/*     */     
/*     */     public ServletContextPlaceholderResolver(String text, ServletContext servletContext) {
/*  92 */       this.text = text;
/*  93 */       this.servletContext = servletContext;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String resolvePlaceholder(String placeholderName) {
/*     */       try {
/* 100 */         String propVal = this.servletContext.getInitParameter(placeholderName);
/* 101 */         if (propVal == null) {
/*     */           
/* 103 */           propVal = System.getProperty(placeholderName);
/* 104 */           if (propVal == null)
/*     */           {
/* 106 */             propVal = System.getenv(placeholderName);
/*     */           }
/*     */         } 
/* 109 */         return propVal;
/*     */       }
/* 111 */       catch (Throwable ex) {
/* 112 */         System.err.println("Could not resolve placeholder '" + placeholderName + "' in [" + this.text + "] as ServletContext init-parameter or system property: " + ex);
/*     */         
/* 114 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/ServletContextPropertyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */