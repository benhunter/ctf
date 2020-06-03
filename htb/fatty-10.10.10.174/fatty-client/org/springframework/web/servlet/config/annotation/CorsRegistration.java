/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CorsRegistration
/*     */ {
/*     */   private final String pathPattern;
/*     */   private final CorsConfiguration config;
/*     */   
/*     */   public CorsRegistration(String pathPattern) {
/*  42 */     this.pathPattern = pathPattern;
/*     */     
/*  44 */     this.config = (new CorsConfiguration()).applyPermitDefaultValues();
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
/*     */   public CorsRegistration allowedOrigins(String... origins) {
/*  63 */     this.config.setAllowedOrigins(Arrays.asList(origins));
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CorsRegistration allowedMethods(String... methods) {
/*  75 */     this.config.setAllowedMethods(Arrays.asList(methods));
/*  76 */     return this;
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
/*     */   public CorsRegistration allowedHeaders(String... headers) {
/*  89 */     this.config.setAllowedHeaders(Arrays.asList(headers));
/*  90 */     return this;
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
/*     */   public CorsRegistration exposedHeaders(String... headers) {
/* 102 */     this.config.setExposedHeaders(Arrays.asList(headers));
/* 103 */     return this;
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
/*     */   public CorsRegistration allowCredentials(boolean allowCredentials) {
/* 120 */     this.config.setAllowCredentials(Boolean.valueOf(allowCredentials));
/* 121 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CorsRegistration maxAge(long maxAge) {
/* 130 */     this.config.setMaxAge(Long.valueOf(maxAge));
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   protected String getPathPattern() {
/* 135 */     return this.pathPattern;
/*     */   }
/*     */   
/*     */   protected CorsConfiguration getCorsConfiguration() {
/* 139 */     return this.config;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/CorsRegistration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */