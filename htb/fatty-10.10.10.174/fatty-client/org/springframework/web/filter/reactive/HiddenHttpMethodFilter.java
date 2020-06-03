/*     */ package org.springframework.web.filter.reactive;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.server.ServerWebExchange;
/*     */ import org.springframework.web.server.WebFilter;
/*     */ import org.springframework.web.server.WebFilterChain;
/*     */ import reactor.core.publisher.Mono;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HiddenHttpMethodFilter
/*     */   implements WebFilter
/*     */ {
/*  52 */   private static final List<HttpMethod> ALLOWED_METHODS = Collections.unmodifiableList(Arrays.asList(new HttpMethod[] { HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH }));
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_METHOD_PARAMETER_NAME = "_method";
/*     */ 
/*     */   
/*  59 */   private String methodParamName = "_method";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethodParamName(String methodParamName) {
/*  67 */     Assert.hasText(methodParamName, "'methodParamName' must not be empty");
/*  68 */     this.methodParamName = methodParamName;
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
/*     */   public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
/*  81 */     if (exchange.getRequest().getMethod() != HttpMethod.POST) {
/*  82 */       return chain.filter(exchange);
/*     */     }
/*     */     
/*  85 */     return exchange.getFormData()
/*  86 */       .map(formData -> {
/*     */           String method = (String)formData.getFirst(this.methodParamName);
/*     */           
/*     */           return StringUtils.hasLength(method) ? mapExchange(exchange, method) : exchange;
/*  90 */         }).flatMap(chain::filter);
/*     */   }
/*     */   
/*     */   private ServerWebExchange mapExchange(ServerWebExchange exchange, String methodParamValue) {
/*  94 */     HttpMethod httpMethod = HttpMethod.resolve(methodParamValue.toUpperCase(Locale.ENGLISH));
/*  95 */     Assert.notNull(httpMethod, () -> "HttpMethod '" + methodParamValue + "' not supported");
/*  96 */     if (ALLOWED_METHODS.contains(httpMethod)) {
/*  97 */       return exchange.mutate().request(builder -> builder.method(httpMethod)).build();
/*     */     }
/*     */     
/* 100 */     return exchange;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/reactive/HiddenHttpMethodFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */