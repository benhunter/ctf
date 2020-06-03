/*    */ package org.springframework.web.cors.reactive;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.http.server.PathContainer;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.cors.CorsConfiguration;
/*    */ import org.springframework.web.server.ServerWebExchange;
/*    */ import org.springframework.web.util.pattern.PathPattern;
/*    */ import org.springframework.web.util.pattern.PathPatternParser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UrlBasedCorsConfigurationSource
/*    */   implements CorsConfigurationSource
/*    */ {
/*    */   private final Map<PathPattern, CorsConfiguration> corsConfigurations;
/*    */   private final PathPatternParser patternParser;
/*    */   
/*    */   public UrlBasedCorsConfigurationSource() {
/* 53 */     this(new PathPatternParser());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UrlBasedCorsConfigurationSource(PathPatternParser patternParser) {
/* 61 */     this.corsConfigurations = new LinkedHashMap<>();
/* 62 */     this.patternParser = patternParser;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCorsConfigurations(@Nullable Map<String, CorsConfiguration> corsConfigurations) {
/* 70 */     this.corsConfigurations.clear();
/* 71 */     if (corsConfigurations != null) {
/* 72 */       corsConfigurations.forEach(this::registerCorsConfiguration);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerCorsConfiguration(String path, CorsConfiguration config) {
/* 80 */     this.corsConfigurations.put(this.patternParser.parse(path), config);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
/* 86 */     PathContainer lookupPath = exchange.getRequest().getPath().pathWithinApplication();
/* 87 */     return this.corsConfigurations.entrySet().stream()
/* 88 */       .filter(entry -> ((PathPattern)entry.getKey()).matches(lookupPath))
/* 89 */       .map(Map.Entry::getValue)
/* 90 */       .findFirst()
/* 91 */       .orElse(null);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/cors/reactive/UrlBasedCorsConfigurationSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */