/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpMessageConverterExtractor<T>
/*     */   implements ResponseExtractor<T>
/*     */ {
/*     */   private final Type responseType;
/*     */   @Nullable
/*     */   private final Class<T> responseClass;
/*     */   private final List<HttpMessageConverter<?>> messageConverters;
/*     */   private final Log logger;
/*     */   
/*     */   public HttpMessageConverterExtractor(Class<T> responseType, List<HttpMessageConverter<?>> messageConverters) {
/*  61 */     this(responseType, messageConverters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters) {
/*  69 */     this(responseType, messageConverters, LogFactory.getLog(HttpMessageConverterExtractor.class));
/*     */   }
/*     */ 
/*     */   
/*     */   HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters, Log logger) {
/*  74 */     Assert.notNull(responseType, "'responseType' must not be null");
/*  75 */     Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
/*  76 */     this.responseType = responseType;
/*  77 */     this.responseClass = (responseType instanceof Class) ? (Class<T>)responseType : null;
/*  78 */     this.messageConverters = messageConverters;
/*  79 */     this.logger = logger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T extractData(ClientHttpResponse response) throws IOException {
/*  86 */     MessageBodyClientHttpResponseWrapper responseWrapper = new MessageBodyClientHttpResponseWrapper(response);
/*  87 */     if (!responseWrapper.hasMessageBody() || responseWrapper.hasEmptyMessageBody()) {
/*  88 */       return null;
/*     */     }
/*  90 */     MediaType contentType = getContentType(responseWrapper);
/*     */     
/*     */     try {
/*  93 */       for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
/*  94 */         if (messageConverter instanceof GenericHttpMessageConverter) {
/*  95 */           GenericHttpMessageConverter<?> genericMessageConverter = (GenericHttpMessageConverter)messageConverter;
/*     */           
/*  97 */           if (genericMessageConverter.canRead(this.responseType, null, contentType)) {
/*  98 */             if (this.logger.isDebugEnabled()) {
/*  99 */               ResolvableType resolvableType = ResolvableType.forType(this.responseType);
/* 100 */               this.logger.debug("Reading to [" + resolvableType + "]");
/*     */             } 
/* 102 */             return (T)genericMessageConverter.read(this.responseType, null, (HttpInputMessage)responseWrapper);
/*     */           } 
/*     */         } 
/* 105 */         if (this.responseClass != null && 
/* 106 */           messageConverter.canRead(this.responseClass, contentType)) {
/* 107 */           if (this.logger.isDebugEnabled()) {
/* 108 */             String className = this.responseClass.getName();
/* 109 */             this.logger.debug("Reading to [" + className + "] as \"" + contentType + "\"");
/*     */           } 
/* 111 */           return (T)messageConverter.read(this.responseClass, (HttpInputMessage)responseWrapper);
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 116 */     } catch (IOException|org.springframework.http.converter.HttpMessageNotReadableException ex) {
/* 117 */       throw new RestClientException("Error while extracting response for type [" + this.responseType + "] and content type [" + contentType + "]", ex);
/*     */     } 
/*     */ 
/*     */     
/* 121 */     throw new RestClientException("Could not extract response: no suitable HttpMessageConverter found for response type [" + this.responseType + "] and content type [" + contentType + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MediaType getContentType(ClientHttpResponse response) {
/* 133 */     MediaType contentType = response.getHeaders().getContentType();
/* 134 */     if (contentType == null) {
/* 135 */       if (this.logger.isTraceEnabled()) {
/* 136 */         this.logger.trace("No content-type, using 'application/octet-stream'");
/*     */       }
/* 138 */       contentType = MediaType.APPLICATION_OCTET_STREAM;
/*     */     } 
/* 140 */     return contentType;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/HttpMessageConverterExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */