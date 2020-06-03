/*     */ package org.springframework.util;
/*     */ 
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
/*     */ public abstract class SystemPropertyUtils
/*     */ {
/*     */   public static final String PLACEHOLDER_PREFIX = "${";
/*     */   public static final String PLACEHOLDER_SUFFIX = "}";
/*     */   public static final String VALUE_SEPARATOR = ":";
/*  48 */   private static final PropertyPlaceholderHelper strictHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
/*     */ 
/*     */   
/*  51 */   private static final PropertyPlaceholderHelper nonStrictHelper = new PropertyPlaceholderHelper("${", "}", ":", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String resolvePlaceholders(String text) {
/*  65 */     return resolvePlaceholders(text, false);
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
/*     */   public static String resolvePlaceholders(String text, boolean ignoreUnresolvablePlaceholders) {
/*  81 */     PropertyPlaceholderHelper helper = ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper;
/*  82 */     return helper.replacePlaceholders(text, new SystemPropertyPlaceholderResolver(text));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SystemPropertyPlaceholderResolver
/*     */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*     */   {
/*     */     private final String text;
/*     */ 
/*     */ 
/*     */     
/*     */     public SystemPropertyPlaceholderResolver(String text) {
/*  95 */       this.text = text;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String resolvePlaceholder(String placeholderName) {
/*     */       try {
/* 102 */         String propVal = System.getProperty(placeholderName);
/* 103 */         if (propVal == null)
/*     */         {
/* 105 */           propVal = System.getenv(placeholderName);
/*     */         }
/* 107 */         return propVal;
/*     */       }
/* 109 */       catch (Throwable ex) {
/* 110 */         System.err.println("Could not resolve placeholder '" + placeholderName + "' in [" + this.text + "] as system property: " + ex);
/*     */         
/* 112 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/SystemPropertyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */