/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.client.AsyncClientHttpRequest;
/*     */ import org.springframework.http.client.AsyncClientHttpRequestFactory;
/*     */ import org.springframework.http.client.ClientHttpRequest;
/*     */ import org.springframework.http.client.ClientHttpRequestFactory;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ import org.springframework.http.client.SimpleClientHttpRequestFactory;
/*     */ import org.springframework.http.client.support.InterceptingAsyncHttpAccessor;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureAdapter;
/*     */ import org.springframework.web.util.AbstractUriTemplateHandler;
/*     */ import org.springframework.web.util.DefaultUriBuilderFactory;
/*     */ import org.springframework.web.util.UriTemplateHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class AsyncRestTemplate
/*     */   extends InterceptingAsyncHttpAccessor
/*     */   implements AsyncRestOperations
/*     */ {
/*     */   private final RestTemplate syncTemplate;
/*     */   
/*     */   public AsyncRestTemplate() {
/*  82 */     this((AsyncListenableTaskExecutor)new SimpleAsyncTaskExecutor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncRestTemplate(AsyncListenableTaskExecutor taskExecutor) {
/*  92 */     Assert.notNull(taskExecutor, "AsyncTaskExecutor must not be null");
/*  93 */     SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
/*  94 */     requestFactory.setTaskExecutor(taskExecutor);
/*  95 */     this.syncTemplate = new RestTemplate((ClientHttpRequestFactory)requestFactory);
/*  96 */     setAsyncRequestFactory((AsyncClientHttpRequestFactory)requestFactory);
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
/*     */   public AsyncRestTemplate(AsyncClientHttpRequestFactory asyncRequestFactory) {
/* 109 */     this(asyncRequestFactory, (ClientHttpRequestFactory)asyncRequestFactory);
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
/*     */   public AsyncRestTemplate(AsyncClientHttpRequestFactory asyncRequestFactory, ClientHttpRequestFactory syncRequestFactory) {
/* 121 */     this(asyncRequestFactory, new RestTemplate(syncRequestFactory));
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
/*     */   public AsyncRestTemplate(AsyncClientHttpRequestFactory requestFactory, RestTemplate restTemplate) {
/* 133 */     Assert.notNull(restTemplate, "RestTemplate must not be null");
/* 134 */     this.syncTemplate = restTemplate;
/* 135 */     setAsyncRequestFactory(requestFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ResponseErrorHandler errorHandler) {
/* 145 */     this.syncTemplate.setErrorHandler(errorHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseErrorHandler getErrorHandler() {
/* 152 */     return this.syncTemplate.getErrorHandler();
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
/*     */   public void setDefaultUriVariables(Map<String, ?> defaultUriVariables) {
/* 169 */     UriTemplateHandler handler = this.syncTemplate.getUriTemplateHandler();
/* 170 */     if (handler instanceof DefaultUriBuilderFactory) {
/* 171 */       ((DefaultUriBuilderFactory)handler).setDefaultUriVariables(defaultUriVariables);
/*     */     }
/* 173 */     else if (handler instanceof AbstractUriTemplateHandler) {
/* 174 */       ((AbstractUriTemplateHandler)handler)
/* 175 */         .setDefaultUriVariables(defaultUriVariables);
/*     */     } else {
/*     */       
/* 178 */       throw new IllegalArgumentException("This property is not supported with the configured UriTemplateHandler.");
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
/*     */   public void setUriTemplateHandler(UriTemplateHandler handler) {
/* 190 */     this.syncTemplate.setUriTemplateHandler(handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriTemplateHandler getUriTemplateHandler() {
/* 197 */     return this.syncTemplate.getUriTemplateHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   public RestOperations getRestOperations() {
/* 202 */     return this.syncTemplate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
/* 210 */     this.syncTemplate.setMessageConverters(messageConverters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HttpMessageConverter<?>> getMessageConverters() {
/* 217 */     return this.syncTemplate.getMessageConverters();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 227 */     AsyncRequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/* 228 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 229 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 236 */     AsyncRequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/* 237 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 238 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> getForEntity(URI url, Class<T> responseType) throws RestClientException {
/* 245 */     AsyncRequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/* 246 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 247 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<HttpHeaders> headForHeaders(String url, Object... uriVariables) throws RestClientException {
/* 257 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/* 258 */     return execute(url, HttpMethod.HEAD, (AsyncRequestCallback)null, headersExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<HttpHeaders> headForHeaders(String url, Map<String, ?> uriVariables) throws RestClientException {
/* 265 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/* 266 */     return execute(url, HttpMethod.HEAD, (AsyncRequestCallback)null, headersExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<HttpHeaders> headForHeaders(URI url) throws RestClientException {
/* 271 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/* 272 */     return execute(url, HttpMethod.HEAD, (AsyncRequestCallback)null, headersExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<URI> postForLocation(String url, @Nullable HttpEntity<?> request, Object... uriVars) throws RestClientException {
/* 282 */     AsyncRequestCallback callback = httpEntityCallback(request);
/* 283 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 284 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.POST, callback, extractor, uriVars);
/* 285 */     return adaptToLocationHeader(future);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<URI> postForLocation(String url, @Nullable HttpEntity<?> request, Map<String, ?> uriVars) throws RestClientException {
/* 292 */     AsyncRequestCallback callback = httpEntityCallback(request);
/* 293 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 294 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.POST, callback, extractor, uriVars);
/* 295 */     return adaptToLocationHeader(future);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<URI> postForLocation(URI url, @Nullable HttpEntity<?> request) throws RestClientException {
/* 302 */     AsyncRequestCallback callback = httpEntityCallback(request);
/* 303 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 304 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.POST, callback, extractor);
/* 305 */     return adaptToLocationHeader(future);
/*     */   }
/*     */   
/*     */   private static ListenableFuture<URI> adaptToLocationHeader(ListenableFuture<HttpHeaders> future) {
/* 309 */     return (ListenableFuture)new ListenableFutureAdapter<URI, HttpHeaders>(future)
/*     */       {
/*     */         @Nullable
/*     */         protected URI adapt(HttpHeaders headers) throws ExecutionException {
/* 313 */           return headers.getLocation();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> postForEntity(String url, @Nullable HttpEntity<?> request, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 322 */     AsyncRequestCallback requestCallback = httpEntityCallback(request, responseType);
/* 323 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 324 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> postForEntity(String url, @Nullable HttpEntity<?> request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 331 */     AsyncRequestCallback requestCallback = httpEntityCallback(request, responseType);
/* 332 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 333 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> postForEntity(URI url, @Nullable HttpEntity<?> request, Class<T> responseType) throws RestClientException {
/* 340 */     AsyncRequestCallback requestCallback = httpEntityCallback(request, responseType);
/* 341 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 342 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> put(String url, @Nullable HttpEntity<?> request, Object... uriVars) throws RestClientException {
/* 352 */     AsyncRequestCallback requestCallback = httpEntityCallback(request);
/* 353 */     return execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null, uriVars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> put(String url, @Nullable HttpEntity<?> request, Map<String, ?> uriVars) throws RestClientException {
/* 360 */     AsyncRequestCallback requestCallback = httpEntityCallback(request);
/* 361 */     return execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null, uriVars);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> put(URI url, @Nullable HttpEntity<?> request) throws RestClientException {
/* 366 */     AsyncRequestCallback requestCallback = httpEntityCallback(request);
/* 367 */     return execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> delete(String url, Object... uriVariables) throws RestClientException {
/* 375 */     return execute(url, HttpMethod.DELETE, (AsyncRequestCallback)null, (ResponseExtractor<?>)null, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> delete(String url, Map<String, ?> uriVariables) throws RestClientException {
/* 380 */     return execute(url, HttpMethod.DELETE, (AsyncRequestCallback)null, (ResponseExtractor<?>)null, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> delete(URI url) throws RestClientException {
/* 385 */     return execute(url, HttpMethod.DELETE, (AsyncRequestCallback)null, (ResponseExtractor<?>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<Set<HttpMethod>> optionsForAllow(String url, Object... uriVars) throws RestClientException {
/* 395 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 396 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.OPTIONS, (AsyncRequestCallback)null, extractor, uriVars);
/* 397 */     return adaptToAllowHeader(future);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<Set<HttpMethod>> optionsForAllow(String url, Map<String, ?> uriVars) throws RestClientException {
/* 404 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 405 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.OPTIONS, (AsyncRequestCallback)null, extractor, uriVars);
/* 406 */     return adaptToAllowHeader(future);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<Set<HttpMethod>> optionsForAllow(URI url) throws RestClientException {
/* 411 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 412 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.OPTIONS, (AsyncRequestCallback)null, extractor);
/* 413 */     return adaptToAllowHeader(future);
/*     */   }
/*     */   
/*     */   private static ListenableFuture<Set<HttpMethod>> adaptToAllowHeader(ListenableFuture<HttpHeaders> future) {
/* 417 */     return (ListenableFuture)new ListenableFutureAdapter<Set<HttpMethod>, HttpHeaders>(future)
/*     */       {
/*     */         protected Set<HttpMethod> adapt(HttpHeaders headers) throws ExecutionException {
/* 420 */           return headers.getAllow();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 432 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/* 433 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 434 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 442 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/* 443 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 444 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(URI url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
/* 451 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/* 452 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 453 */     return execute(url, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
/* 461 */     Type type = responseType.getType();
/* 462 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/* 463 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/* 464 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 472 */     Type type = responseType.getType();
/* 473 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/* 474 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/* 475 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(URI url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) throws RestClientException {
/* 483 */     Type type = responseType.getType();
/* 484 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/* 485 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/* 486 */     return execute(url, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> execute(String url, HttpMethod method, @Nullable AsyncRequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor, Object... uriVariables) throws RestClientException {
/* 496 */     URI expanded = getUriTemplateHandler().expand(url, uriVariables);
/* 497 */     return doExecute(expanded, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> execute(String url, HttpMethod method, @Nullable AsyncRequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables) throws RestClientException {
/* 505 */     URI expanded = getUriTemplateHandler().expand(url, uriVariables);
/* 506 */     return doExecute(expanded, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> execute(URI url, HttpMethod method, @Nullable AsyncRequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {
/* 514 */     return doExecute(url, method, requestCallback, responseExtractor);
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
/*     */   protected <T> ListenableFuture<T> doExecute(URI url, HttpMethod method, @Nullable AsyncRequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {
/* 533 */     Assert.notNull(url, "'url' must not be null");
/* 534 */     Assert.notNull(method, "'method' must not be null");
/*     */     try {
/* 536 */       AsyncClientHttpRequest request = createAsyncRequest(url, method);
/* 537 */       if (requestCallback != null) {
/* 538 */         requestCallback.doWithRequest(request);
/*     */       }
/* 540 */       ListenableFuture<ClientHttpResponse> responseFuture = request.executeAsync();
/* 541 */       return (ListenableFuture<T>)new ResponseExtractorFuture<>(method, url, responseFuture, responseExtractor);
/*     */     }
/* 543 */     catch (IOException ex) {
/* 544 */       throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" + url + "\":" + ex
/* 545 */           .getMessage(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void logResponseStatus(HttpMethod method, URI url, ClientHttpResponse response) {
/* 550 */     if (this.logger.isDebugEnabled()) {
/*     */       try {
/* 552 */         this.logger.debug("Async " + method.name() + " request for \"" + url + "\" resulted in " + response
/* 553 */             .getRawStatusCode() + " (" + response.getStatusText() + ")");
/*     */       }
/* 555 */       catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleResponseError(HttpMethod method, URI url, ClientHttpResponse response) throws IOException {
/* 562 */     if (this.logger.isWarnEnabled()) {
/*     */       try {
/* 564 */         this.logger.warn("Async " + method.name() + " request for \"" + url + "\" resulted in " + response
/* 565 */             .getRawStatusCode() + " (" + response.getStatusText() + "); invoking error handler");
/*     */       }
/* 567 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/* 571 */     getErrorHandler().handleError(url, method, response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> AsyncRequestCallback acceptHeaderRequestCallback(Class<T> responseType) {
/* 580 */     return new AsyncRequestCallbackAdapter(this.syncTemplate.acceptHeaderRequestCallback(responseType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> AsyncRequestCallback httpEntityCallback(@Nullable HttpEntity<T> requestBody) {
/* 588 */     return new AsyncRequestCallbackAdapter(this.syncTemplate.httpEntityCallback(requestBody));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> AsyncRequestCallback httpEntityCallback(@Nullable HttpEntity<T> request, Type responseType) {
/* 596 */     return new AsyncRequestCallbackAdapter(this.syncTemplate.httpEntityCallback(request, responseType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
/* 603 */     return this.syncTemplate.responseEntityExtractor(responseType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResponseExtractor<HttpHeaders> headersExtractor() {
/* 610 */     return this.syncTemplate.headersExtractor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ResponseExtractorFuture<T>
/*     */     extends ListenableFutureAdapter<T, ClientHttpResponse>
/*     */   {
/*     */     private final HttpMethod method;
/*     */ 
/*     */     
/*     */     private final URI url;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final ResponseExtractor<T> responseExtractor;
/*     */ 
/*     */ 
/*     */     
/*     */     public ResponseExtractorFuture(HttpMethod method, URI url, @Nullable ListenableFuture<ClientHttpResponse> clientHttpResponseFuture, ResponseExtractor<T> responseExtractor) {
/* 631 */       super(clientHttpResponseFuture);
/* 632 */       this.method = method;
/* 633 */       this.url = url;
/* 634 */       this.responseExtractor = responseExtractor;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     protected final T adapt(ClientHttpResponse response) throws ExecutionException {
/*     */       try {
/* 641 */         if (!AsyncRestTemplate.this.getErrorHandler().hasError(response)) {
/* 642 */           AsyncRestTemplate.this.logResponseStatus(this.method, this.url, response);
/*     */         } else {
/*     */           
/* 645 */           AsyncRestTemplate.this.handleResponseError(this.method, this.url, response);
/*     */         } 
/* 647 */         return convertResponse(response);
/*     */       }
/* 649 */       catch (Throwable ex) {
/* 650 */         throw new ExecutionException(ex);
/*     */       } finally {
/*     */         
/* 653 */         response.close();
/*     */       } 
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     protected T convertResponse(ClientHttpResponse response) throws IOException {
/* 659 */       return (this.responseExtractor != null) ? this.responseExtractor.extractData(response) : null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AsyncRequestCallbackAdapter
/*     */     implements AsyncRequestCallback
/*     */   {
/*     */     private final RequestCallback adaptee;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AsyncRequestCallbackAdapter(RequestCallback requestCallback) {
/* 677 */       this.adaptee = requestCallback;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void doWithRequest(final AsyncClientHttpRequest request) throws IOException {
/* 684 */       this.adaptee.doWithRequest(new ClientHttpRequest()
/*     */           {
/*     */             public ClientHttpResponse execute() throws IOException {
/* 687 */               throw new UnsupportedOperationException("execute not supported");
/*     */             }
/*     */             
/*     */             public OutputStream getBody() throws IOException {
/* 691 */               return request.getBody();
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public HttpMethod getMethod() {
/* 696 */               return request.getMethod();
/*     */             }
/*     */             
/*     */             public String getMethodValue() {
/* 700 */               return request.getMethodValue();
/*     */             }
/*     */             
/*     */             public URI getURI() {
/* 704 */               return request.getURI();
/*     */             }
/*     */             
/*     */             public HttpHeaders getHeaders() {
/* 708 */               return request.getHeaders();
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/AsyncRestTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */