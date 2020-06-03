/*     */ package org.springframework.web.bind.support;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.http.codec.multipart.FormFieldPart;
/*     */ import org.springframework.http.codec.multipart.Part;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.server.ServerWebExchange;
/*     */ import reactor.core.publisher.Mono;
/*     */ import reactor.util.function.Tuple3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebExchangeDataBinder
/*     */   extends WebDataBinder
/*     */ {
/*     */   public WebExchangeDataBinder(@Nullable Object target) {
/*  51 */     super(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebExchangeDataBinder(@Nullable Object target, String objectName) {
/*  61 */     super(target, objectName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> bind(ServerWebExchange exchange) {
/*  71 */     return getValuesToBind(exchange)
/*  72 */       .doOnNext(values -> doBind(new MutablePropertyValues(values)))
/*  73 */       .then();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Mono<Map<String, Object>> getValuesToBind(ServerWebExchange exchange) {
/*  81 */     return extractValuesToBind(exchange);
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
/*     */   public static Mono<Map<String, Object>> extractValuesToBind(ServerWebExchange exchange) {
/*  96 */     MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
/*  97 */     Mono<MultiValueMap<String, String>> formData = exchange.getFormData();
/*  98 */     Mono<MultiValueMap<String, Part>> multipartData = exchange.getMultipartData();
/*     */     
/* 100 */     return Mono.zip(Mono.just(queryParams), formData, multipartData)
/* 101 */       .map(tuple -> {
/*     */           Map<String, Object> result = new TreeMap<>();
/*     */           ((MultiValueMap)tuple.getT1()).forEach(());
/*     */           ((MultiValueMap)tuple.getT2()).forEach(());
/*     */           ((MultiValueMap)tuple.getT3()).forEach(());
/*     */           return result;
/*     */         });
/*     */   }
/*     */   
/*     */   private static void addBindValue(Map<String, Object> params, String key, List<?> values) {
/* 111 */     if (!CollectionUtils.isEmpty(values)) {
/*     */ 
/*     */       
/* 114 */       values = (List)values.stream().map(value -> (value instanceof FormFieldPart) ? ((FormFieldPart)value).value() : value).collect(Collectors.toList());
/* 115 */       params.put(key, (values.size() == 1) ? values.get(0) : values);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/support/WebExchangeDataBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */