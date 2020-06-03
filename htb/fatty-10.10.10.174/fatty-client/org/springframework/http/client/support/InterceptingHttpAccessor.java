/*     */ package org.springframework.http.client.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.http.client.ClientHttpRequestFactory;
/*     */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*     */ import org.springframework.http.client.InterceptingClientHttpRequestFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class InterceptingHttpAccessor
/*     */   extends HttpAccessor
/*     */ {
/*  46 */   private final List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile ClientHttpRequestFactory interceptingRequestFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
/*  61 */     if (this.interceptors != interceptors) {
/*  62 */       this.interceptors.clear();
/*  63 */       this.interceptors.addAll(interceptors);
/*  64 */       AnnotationAwareOrderComparator.sort(this.interceptors);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ClientHttpRequestInterceptor> getInterceptors() {
/*  73 */     return this.interceptors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
/*  81 */     super.setRequestFactory(requestFactory);
/*  82 */     this.interceptingRequestFactory = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientHttpRequestFactory getRequestFactory() {
/*  92 */     List<ClientHttpRequestInterceptor> interceptors = getInterceptors();
/*  93 */     if (!CollectionUtils.isEmpty(interceptors)) {
/*  94 */       InterceptingClientHttpRequestFactory interceptingClientHttpRequestFactory; ClientHttpRequestFactory factory = this.interceptingRequestFactory;
/*  95 */       if (factory == null) {
/*  96 */         interceptingClientHttpRequestFactory = new InterceptingClientHttpRequestFactory(super.getRequestFactory(), interceptors);
/*  97 */         this.interceptingRequestFactory = (ClientHttpRequestFactory)interceptingClientHttpRequestFactory;
/*     */       } 
/*  99 */       return (ClientHttpRequestFactory)interceptingClientHttpRequestFactory;
/*     */     } 
/*     */     
/* 102 */     return super.getRequestFactory();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/support/InterceptingHttpAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */