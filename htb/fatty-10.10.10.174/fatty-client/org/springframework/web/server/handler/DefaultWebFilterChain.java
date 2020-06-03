/*     */ package org.springframework.web.server.handler;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.server.ServerWebExchange;
/*     */ import org.springframework.web.server.WebFilter;
/*     */ import org.springframework.web.server.WebFilterChain;
/*     */ import org.springframework.web.server.WebHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultWebFilterChain
/*     */   implements WebFilterChain
/*     */ {
/*     */   private final List<WebFilter> allFilters;
/*     */   private final WebHandler handler;
/*     */   @Nullable
/*     */   private final WebFilter currentFilter;
/*     */   @Nullable
/*     */   private final DefaultWebFilterChain next;
/*     */   
/*     */   public DefaultWebFilterChain(WebHandler handler, List<WebFilter> filters) {
/*  66 */     Assert.notNull(handler, "WebHandler is required");
/*  67 */     this.allFilters = Collections.unmodifiableList(filters);
/*  68 */     this.handler = handler;
/*  69 */     DefaultWebFilterChain chain = initChain(filters, handler);
/*  70 */     this.currentFilter = chain.currentFilter;
/*  71 */     this.next = chain.next;
/*     */   }
/*     */   
/*     */   private static DefaultWebFilterChain initChain(List<WebFilter> filters, WebHandler handler) {
/*  75 */     DefaultWebFilterChain chain = new DefaultWebFilterChain(filters, handler, null, null);
/*  76 */     ListIterator<? extends WebFilter> iterator = filters.listIterator(filters.size());
/*  77 */     while (iterator.hasPrevious()) {
/*  78 */       chain = new DefaultWebFilterChain(filters, handler, iterator.previous(), chain);
/*     */     }
/*  80 */     return chain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DefaultWebFilterChain(List<WebFilter> allFilters, WebHandler handler, @Nullable WebFilter currentFilter, @Nullable DefaultWebFilterChain next) {
/*  89 */     this.allFilters = allFilters;
/*  90 */     this.currentFilter = currentFilter;
/*  91 */     this.handler = handler;
/*  92 */     this.next = next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultWebFilterChain(WebHandler handler, WebFilter... filters) {
/* 104 */     this(handler, Arrays.asList(filters));
/*     */   }
/*     */ 
/*     */   
/*     */   public List<WebFilter> getFilters() {
/* 109 */     return this.allFilters;
/*     */   }
/*     */   
/*     */   public WebHandler getHandler() {
/* 113 */     return this.handler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> filter(ServerWebExchange exchange) {
/* 119 */     return Mono.defer(() -> 
/* 120 */         (this.currentFilter != null && this.next != null) ? this.currentFilter.filter(exchange, this.next) : this.handler.handle(exchange));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/handler/DefaultWebFilterChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */