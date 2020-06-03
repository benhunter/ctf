/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeFilter
/*     */   implements Filter
/*     */ {
/*  44 */   private List<? extends Filter> filters = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public void setFilters(List<? extends Filter> filters) {
/*  48 */     this.filters = new ArrayList<>(filters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(FilterConfig config) throws ServletException {
/*  58 */     for (Filter filter : this.filters) {
/*  59 */       filter.init(config);
/*     */     }
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
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
/*  73 */     (new VirtualFilterChain(chain, this.filters)).doFilter(request, response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/*  82 */     for (int i = this.filters.size(); i-- > 0; ) {
/*  83 */       Filter filter = this.filters.get(i);
/*  84 */       filter.destroy();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class VirtualFilterChain
/*     */     implements FilterChain
/*     */   {
/*     */     private final FilterChain originalChain;
/*     */     
/*     */     private final List<? extends Filter> additionalFilters;
/*  95 */     private int currentPosition = 0;
/*     */     
/*     */     public VirtualFilterChain(FilterChain chain, List<? extends Filter> additionalFilters) {
/*  98 */       this.originalChain = chain;
/*  99 */       this.additionalFilters = additionalFilters;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
/* 106 */       if (this.currentPosition == this.additionalFilters.size()) {
/* 107 */         this.originalChain.doFilter(request, response);
/*     */       } else {
/*     */         
/* 110 */         this.currentPosition++;
/* 111 */         Filter nextFilter = this.additionalFilters.get(this.currentPosition - 1);
/* 112 */         nextFilter.doFilter(request, response, this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/CompositeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */