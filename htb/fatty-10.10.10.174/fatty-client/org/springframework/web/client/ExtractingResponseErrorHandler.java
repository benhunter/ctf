/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtractingResponseErrorHandler
/*     */   extends DefaultResponseErrorHandler
/*     */ {
/*  60 */   private List<HttpMessageConverter<?>> messageConverters = Collections.emptyList();
/*     */   
/*  62 */   private final Map<HttpStatus, Class<? extends RestClientException>> statusMapping = new LinkedHashMap<>();
/*     */   
/*  64 */   private final Map<HttpStatus.Series, Class<? extends RestClientException>> seriesMapping = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtractingResponseErrorHandler() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtractingResponseErrorHandler(List<HttpMessageConverter<?>> messageConverters) {
/*  80 */     this.messageConverters = messageConverters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
/*  88 */     this.messageConverters = messageConverters;
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
/*     */   public void setStatusMapping(Map<HttpStatus, Class<? extends RestClientException>> statusMapping) {
/* 101 */     if (!CollectionUtils.isEmpty(statusMapping)) {
/* 102 */       this.statusMapping.putAll(statusMapping);
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
/*     */   public void setSeriesMapping(Map<HttpStatus.Series, Class<? extends RestClientException>> seriesMapping) {
/* 116 */     if (!CollectionUtils.isEmpty(seriesMapping)) {
/* 117 */       this.seriesMapping.putAll(seriesMapping);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasError(HttpStatus statusCode) {
/* 124 */     if (this.statusMapping.containsKey(statusCode)) {
/* 125 */       return (this.statusMapping.get(statusCode) != null);
/*     */     }
/* 127 */     if (this.seriesMapping.containsKey(statusCode.series())) {
/* 128 */       return (this.seriesMapping.get(statusCode.series()) != null);
/*     */     }
/*     */     
/* 131 */     return super.hasError(statusCode);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
/* 137 */     if (this.statusMapping.containsKey(statusCode)) {
/* 138 */       extract(this.statusMapping.get(statusCode), response);
/*     */     }
/* 140 */     else if (this.seriesMapping.containsKey(statusCode.series())) {
/* 141 */       extract(this.seriesMapping.get(statusCode.series()), response);
/*     */     } else {
/*     */       
/* 144 */       super.handleError(response, statusCode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void extract(@Nullable Class<? extends RestClientException> exceptionClass, ClientHttpResponse response) throws IOException {
/* 151 */     if (exceptionClass == null) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     HttpMessageConverterExtractor<? extends RestClientException> extractor = new HttpMessageConverterExtractor<>(exceptionClass, this.messageConverters);
/*     */     
/* 157 */     RestClientException exception = extractor.extractData(response);
/* 158 */     if (exception != null)
/* 159 */       throw exception; 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/ExtractingResponseErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */