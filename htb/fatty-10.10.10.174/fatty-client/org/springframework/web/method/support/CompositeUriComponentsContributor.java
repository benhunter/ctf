/*     */ package org.springframework.web.method.support;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.format.support.DefaultFormattingConversionService;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeUriComponentsContributor
/*     */   implements UriComponentsContributor
/*     */ {
/*  41 */   private final List<Object> contributors = new LinkedList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ConversionService conversionService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeUriComponentsContributor(UriComponentsContributor... contributors) {
/*  56 */     Collections.addAll(this.contributors, (Object[])contributors);
/*  57 */     this.conversionService = (ConversionService)new DefaultFormattingConversionService();
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
/*     */   public CompositeUriComponentsContributor(Collection<?> contributors) {
/*  70 */     this(contributors, null);
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
/*     */   public CompositeUriComponentsContributor(@Nullable Collection<?> contributors, @Nullable ConversionService cs) {
/*  88 */     if (contributors != null) {
/*  89 */       this.contributors.addAll(contributors);
/*     */     }
/*  91 */     this.conversionService = (cs != null) ? cs : (ConversionService)new DefaultFormattingConversionService();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasContributors() {
/*  96 */     return this.contributors.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/* 101 */     for (Object contributor : this.contributors) {
/* 102 */       if (contributor instanceof UriComponentsContributor) {
/* 103 */         if (((UriComponentsContributor)contributor).supportsParameter(parameter))
/* 104 */           return true; 
/*     */         continue;
/*     */       } 
/* 107 */       if (contributor instanceof HandlerMethodArgumentResolver && (
/* 108 */         (HandlerMethodArgumentResolver)contributor).supportsParameter(parameter)) {
/* 109 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables, ConversionService conversionService) {
/* 120 */     for (Object contributor : this.contributors) {
/* 121 */       if (contributor instanceof UriComponentsContributor) {
/* 122 */         UriComponentsContributor ucc = (UriComponentsContributor)contributor;
/* 123 */         if (ucc.supportsParameter(parameter)) {
/* 124 */           ucc.contributeMethodArgument(parameter, value, builder, uriVariables, conversionService); break;
/*     */         } 
/*     */         continue;
/*     */       } 
/* 128 */       if (contributor instanceof HandlerMethodArgumentResolver && (
/* 129 */         (HandlerMethodArgumentResolver)contributor).supportsParameter(parameter)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables) {
/* 142 */     contributeMethodArgument(parameter, value, builder, uriVariables, this.conversionService);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/support/CompositeUriComponentsContributor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */