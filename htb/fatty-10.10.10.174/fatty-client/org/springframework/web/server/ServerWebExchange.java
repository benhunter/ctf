/*     */ package org.springframework.web.server;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.time.Instant;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.http.codec.multipart.Part;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ 
/*     */ public interface ServerWebExchange
/*     */ {
/*  53 */   public static final String LOG_ID_ATTRIBUTE = ServerWebExchange.class.getName() + ".LOG_ID";
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
/*     */   @Nullable
/*     */   default <T> T getAttribute(String name) {
/*  80 */     return (T)getAttributes().get(name);
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
/*     */   default <T> T getRequiredAttribute(String name) {
/*  92 */     T value = getAttribute(name);
/*  93 */     Assert.notNull(value, () -> "Required attribute '" + name + "' is missing");
/*  94 */     return value;
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
/*     */   default <T> T getAttributeOrDefault(String name, T defaultValue) {
/* 106 */     return (T)getAttributes().getOrDefault(name, defaultValue);
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
/*     */   default Builder mutate() {
/* 235 */     return new DefaultServerWebExchangeBuilder(this);
/*     */   }
/*     */   
/*     */   ServerHttpRequest getRequest();
/*     */   
/*     */   ServerHttpResponse getResponse();
/*     */   
/*     */   Map<String, Object> getAttributes();
/*     */   
/*     */   Mono<WebSession> getSession();
/*     */   
/*     */   <T extends Principal> Mono<T> getPrincipal();
/*     */   
/*     */   Mono<MultiValueMap<String, String>> getFormData();
/*     */   
/*     */   Mono<MultiValueMap<String, Part>> getMultipartData();
/*     */   
/*     */   LocaleContext getLocaleContext();
/*     */   
/*     */   @Nullable
/*     */   ApplicationContext getApplicationContext();
/*     */   
/*     */   boolean isNotModified();
/*     */   
/*     */   boolean checkNotModified(Instant paramInstant);
/*     */   
/*     */   boolean checkNotModified(String paramString);
/*     */   
/*     */   boolean checkNotModified(@Nullable String paramString, Instant paramInstant);
/*     */   
/*     */   String transformUrl(String paramString);
/*     */   
/*     */   void addUrlTransformer(Function<String, String> paramFunction);
/*     */   
/*     */   String getLogPrefix();
/*     */   
/*     */   public static interface Builder {
/*     */     Builder request(Consumer<ServerHttpRequest.Builder> param1Consumer);
/*     */     
/*     */     Builder request(ServerHttpRequest param1ServerHttpRequest);
/*     */     
/*     */     Builder response(ServerHttpResponse param1ServerHttpResponse);
/*     */     
/*     */     Builder principal(Mono<Principal> param1Mono);
/*     */     
/*     */     ServerWebExchange build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/ServerWebExchange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */