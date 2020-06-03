/*      */ package org.springframework.web.client;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Type;
/*      */ import java.net.URI;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.springframework.core.ParameterizedTypeReference;
/*      */ import org.springframework.http.HttpEntity;
/*      */ import org.springframework.http.HttpHeaders;
/*      */ import org.springframework.http.HttpMethod;
/*      */ import org.springframework.http.HttpOutputMessage;
/*      */ import org.springframework.http.HttpStatus;
/*      */ import org.springframework.http.MediaType;
/*      */ import org.springframework.http.RequestEntity;
/*      */ import org.springframework.http.ResponseEntity;
/*      */ import org.springframework.http.client.ClientHttpRequest;
/*      */ import org.springframework.http.client.ClientHttpRequestFactory;
/*      */ import org.springframework.http.client.ClientHttpResponse;
/*      */ import org.springframework.http.client.support.InterceptingHttpAccessor;
/*      */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*      */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*      */ import org.springframework.http.converter.HttpMessageConverter;
/*      */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*      */ import org.springframework.http.converter.StringHttpMessageConverter;
/*      */ import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
/*      */ import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
/*      */ import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
/*      */ import org.springframework.http.converter.json.GsonHttpMessageConverter;
/*      */ import org.springframework.http.converter.json.JsonbHttpMessageConverter;
/*      */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/*      */ import org.springframework.http.converter.smile.MappingJackson2SmileHttpMessageConverter;
/*      */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*      */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*      */ import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
/*      */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.web.util.AbstractUriTemplateHandler;
/*      */ import org.springframework.web.util.DefaultUriBuilderFactory;
/*      */ import org.springframework.web.util.UriTemplateHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RestTemplate
/*      */   extends InterceptingHttpAccessor
/*      */   implements RestOperations
/*      */ {
/*      */   private static boolean romePresent;
/*      */   private static final boolean jaxb2Present;
/*      */   private static final boolean jackson2Present;
/*      */   private static final boolean jackson2XmlPresent;
/*      */   private static final boolean jackson2SmilePresent;
/*      */   private static final boolean jackson2CborPresent;
/*      */   private static final boolean gsonPresent;
/*      */   private static final boolean jsonbPresent;
/*      */   
/*      */   static {
/*  112 */     ClassLoader classLoader = RestTemplate.class.getClassLoader();
/*  113 */     romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", classLoader);
/*  114 */     jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", classLoader);
/*      */ 
/*      */     
/*  117 */     jackson2Present = (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader));
/*  118 */     jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);
/*  119 */     jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
/*  120 */     jackson2CborPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.cbor.CBORFactory", classLoader);
/*  121 */     gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
/*  122 */     jsonbPresent = ClassUtils.isPresent("javax.json.bind.Jsonb", classLoader);
/*      */   }
/*      */ 
/*      */   
/*  126 */   private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
/*      */   
/*  128 */   private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
/*      */   
/*      */   private UriTemplateHandler uriTemplateHandler;
/*      */   
/*  132 */   private final ResponseExtractor<HttpHeaders> headersExtractor = new HeadersExtractor();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RestTemplate() {
/*  140 */     this.messageConverters.add(new ByteArrayHttpMessageConverter());
/*  141 */     this.messageConverters.add(new StringHttpMessageConverter());
/*  142 */     this.messageConverters.add(new ResourceHttpMessageConverter(false));
/*      */     try {
/*  144 */       this.messageConverters.add(new SourceHttpMessageConverter());
/*      */     }
/*  146 */     catch (Error error) {}
/*      */ 
/*      */     
/*  149 */     this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
/*      */     
/*  151 */     if (romePresent) {
/*  152 */       this.messageConverters.add(new AtomFeedHttpMessageConverter());
/*  153 */       this.messageConverters.add(new RssChannelHttpMessageConverter());
/*      */     } 
/*      */     
/*  156 */     if (jackson2XmlPresent) {
/*  157 */       this.messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
/*      */     }
/*  159 */     else if (jaxb2Present) {
/*  160 */       this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
/*      */     } 
/*      */     
/*  163 */     if (jackson2Present) {
/*  164 */       this.messageConverters.add(new MappingJackson2HttpMessageConverter());
/*      */     }
/*  166 */     else if (gsonPresent) {
/*  167 */       this.messageConverters.add(new GsonHttpMessageConverter());
/*      */     }
/*  169 */     else if (jsonbPresent) {
/*  170 */       this.messageConverters.add(new JsonbHttpMessageConverter());
/*      */     } 
/*      */     
/*  173 */     if (jackson2SmilePresent) {
/*  174 */       this.messageConverters.add(new MappingJackson2SmileHttpMessageConverter());
/*      */     }
/*  176 */     if (jackson2CborPresent) {
/*  177 */       this.messageConverters.add(new MappingJackson2CborHttpMessageConverter());
/*      */     }
/*      */     
/*  180 */     this.uriTemplateHandler = (UriTemplateHandler)initUriTemplateHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RestTemplate(ClientHttpRequestFactory requestFactory) {
/*  190 */     this();
/*  191 */     setRequestFactory(requestFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RestTemplate(List<HttpMessageConverter<?>> messageConverters) {
/*  201 */     Assert.notEmpty(messageConverters, "At least one HttpMessageConverter required");
/*  202 */     this.messageConverters.addAll(messageConverters);
/*  203 */     this.uriTemplateHandler = (UriTemplateHandler)initUriTemplateHandler();
/*      */   }
/*      */   
/*      */   private static DefaultUriBuilderFactory initUriTemplateHandler() {
/*  207 */     DefaultUriBuilderFactory uriFactory = new DefaultUriBuilderFactory();
/*  208 */     uriFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);
/*  209 */     return uriFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
/*  218 */     Assert.notEmpty(messageConverters, "At least one HttpMessageConverter required");
/*      */     
/*  220 */     if (this.messageConverters != messageConverters) {
/*  221 */       this.messageConverters.clear();
/*  222 */       this.messageConverters.addAll(messageConverters);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<HttpMessageConverter<?>> getMessageConverters() {
/*  231 */     return this.messageConverters;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setErrorHandler(ResponseErrorHandler errorHandler) {
/*  239 */     Assert.notNull(errorHandler, "ResponseErrorHandler must not be null");
/*  240 */     this.errorHandler = errorHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResponseErrorHandler getErrorHandler() {
/*  247 */     return this.errorHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultUriVariables(Map<String, ?> uriVars) {
/*  264 */     if (this.uriTemplateHandler instanceof DefaultUriBuilderFactory) {
/*  265 */       ((DefaultUriBuilderFactory)this.uriTemplateHandler).setDefaultUriVariables(uriVars);
/*      */     }
/*  267 */     else if (this.uriTemplateHandler instanceof AbstractUriTemplateHandler) {
/*  268 */       ((AbstractUriTemplateHandler)this.uriTemplateHandler)
/*  269 */         .setDefaultUriVariables(uriVars);
/*      */     } else {
/*      */       
/*  272 */       throw new IllegalArgumentException("This property is not supported with the configured UriTemplateHandler.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUriTemplateHandler(UriTemplateHandler handler) {
/*  291 */     Assert.notNull(handler, "UriTemplateHandler must not be null");
/*  292 */     this.uriTemplateHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriTemplateHandler getUriTemplateHandler() {
/*  299 */     return this.uriTemplateHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
/*  308 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/*      */     
/*  310 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), this.logger);
/*  311 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/*  317 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/*      */     
/*  319 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), this.logger);
/*  320 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
/*  326 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/*      */     
/*  328 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), this.logger);
/*  329 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
/*  336 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/*  337 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/*  338 */     return nonNull(execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/*  345 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/*  346 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/*  347 */     return nonNull(execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException {
/*  352 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/*  353 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/*  354 */     return nonNull(execute(url, HttpMethod.GET, requestCallback, responseExtractor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders headForHeaders(String url, Object... uriVariables) throws RestClientException {
/*  362 */     return nonNull(execute(url, HttpMethod.HEAD, (RequestCallback)null, headersExtractor(), uriVariables));
/*      */   }
/*      */ 
/*      */   
/*      */   public HttpHeaders headForHeaders(String url, Map<String, ?> uriVariables) throws RestClientException {
/*  367 */     return nonNull(execute(url, HttpMethod.HEAD, (RequestCallback)null, headersExtractor(), uriVariables));
/*      */   }
/*      */ 
/*      */   
/*      */   public HttpHeaders headForHeaders(URI url) throws RestClientException {
/*  372 */     return nonNull(execute(url, HttpMethod.HEAD, (RequestCallback)null, headersExtractor()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public URI postForLocation(String url, @Nullable Object request, Object... uriVariables) throws RestClientException {
/*  383 */     RequestCallback requestCallback = httpEntityCallback(request);
/*  384 */     HttpHeaders headers = execute(url, HttpMethod.POST, requestCallback, headersExtractor(), uriVariables);
/*  385 */     return (headers != null) ? headers.getLocation() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public URI postForLocation(String url, @Nullable Object request, Map<String, ?> uriVariables) throws RestClientException {
/*  393 */     RequestCallback requestCallback = httpEntityCallback(request);
/*  394 */     HttpHeaders headers = execute(url, HttpMethod.POST, requestCallback, headersExtractor(), uriVariables);
/*  395 */     return (headers != null) ? headers.getLocation() : null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public URI postForLocation(URI url, @Nullable Object request) throws RestClientException {
/*  401 */     RequestCallback requestCallback = httpEntityCallback(request);
/*  402 */     HttpHeaders headers = execute(url, HttpMethod.POST, requestCallback, headersExtractor());
/*  403 */     return (headers != null) ? headers.getLocation() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
/*  411 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*      */     
/*  413 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), this.logger);
/*  414 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/*  422 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*      */     
/*  424 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), this.logger);
/*  425 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T postForObject(URI url, @Nullable Object request, Class<T> responseType) throws RestClientException {
/*  433 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*      */     
/*  435 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType, getMessageConverters());
/*  436 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
/*  443 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*  444 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/*  445 */     return nonNull(execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/*  452 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*  453 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/*  454 */     return nonNull(execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> postForEntity(URI url, @Nullable Object request, Class<T> responseType) throws RestClientException {
/*  461 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*  462 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/*  463 */     return nonNull(execute(url, HttpMethod.POST, requestCallback, responseExtractor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void put(String url, @Nullable Object request, Object... uriVariables) throws RestClientException {
/*  473 */     RequestCallback requestCallback = httpEntityCallback(request);
/*  474 */     execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null, uriVariables);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void put(String url, @Nullable Object request, Map<String, ?> uriVariables) throws RestClientException {
/*  481 */     RequestCallback requestCallback = httpEntityCallback(request);
/*  482 */     execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null, uriVariables);
/*      */   }
/*      */ 
/*      */   
/*      */   public void put(URI url, @Nullable Object request) throws RestClientException {
/*  487 */     RequestCallback requestCallback = httpEntityCallback(request);
/*  488 */     execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T patchForObject(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
/*  499 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*      */     
/*  501 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), this.logger);
/*  502 */     return execute(url, HttpMethod.PATCH, requestCallback, responseExtractor, uriVariables);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T patchForObject(String url, @Nullable Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/*  510 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*      */     
/*  512 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), this.logger);
/*  513 */     return execute(url, HttpMethod.PATCH, requestCallback, responseExtractor, uriVariables);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T patchForObject(URI url, @Nullable Object request, Class<T> responseType) throws RestClientException {
/*  521 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*      */     
/*  523 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType, getMessageConverters());
/*  524 */     return execute(url, HttpMethod.PATCH, requestCallback, responseExtractor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void delete(String url, Object... uriVariables) throws RestClientException {
/*  532 */     execute(url, HttpMethod.DELETE, (RequestCallback)null, (ResponseExtractor<?>)null, uriVariables);
/*      */   }
/*      */ 
/*      */   
/*      */   public void delete(String url, Map<String, ?> uriVariables) throws RestClientException {
/*  537 */     execute(url, HttpMethod.DELETE, (RequestCallback)null, (ResponseExtractor<?>)null, uriVariables);
/*      */   }
/*      */ 
/*      */   
/*      */   public void delete(URI url) throws RestClientException {
/*  542 */     execute(url, HttpMethod.DELETE, (RequestCallback)null, (ResponseExtractor<?>)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<HttpMethod> optionsForAllow(String url, Object... uriVariables) throws RestClientException {
/*  550 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/*  551 */     HttpHeaders headers = execute(url, HttpMethod.OPTIONS, (RequestCallback)null, headersExtractor, uriVariables);
/*  552 */     return (headers != null) ? headers.getAllow() : Collections.<HttpMethod>emptySet();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<HttpMethod> optionsForAllow(String url, Map<String, ?> uriVariables) throws RestClientException {
/*  557 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/*  558 */     HttpHeaders headers = execute(url, HttpMethod.OPTIONS, (RequestCallback)null, headersExtractor, uriVariables);
/*  559 */     return (headers != null) ? headers.getAllow() : Collections.<HttpMethod>emptySet();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<HttpMethod> optionsForAllow(URI url) throws RestClientException {
/*  564 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/*  565 */     HttpHeaders headers = execute(url, HttpMethod.OPTIONS, (RequestCallback)null, headersExtractor);
/*  566 */     return (headers != null) ? headers.getAllow() : Collections.<HttpMethod>emptySet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
/*  577 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/*  578 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/*  579 */     return nonNull(execute(url, method, requestCallback, responseExtractor, uriVariables));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/*  587 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/*  588 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/*  589 */     return nonNull(execute(url, method, requestCallback, responseExtractor, uriVariables));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
/*  596 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/*  597 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/*  598 */     return nonNull(execute(url, method, requestCallback, responseExtractor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
/*  605 */     Type type = responseType.getType();
/*  606 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/*  607 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/*  608 */     return nonNull(execute(url, method, requestCallback, responseExtractor, uriVariables));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/*  615 */     Type type = responseType.getType();
/*  616 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/*  617 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/*  618 */     return nonNull(execute(url, method, requestCallback, responseExtractor, uriVariables));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) throws RestClientException {
/*  625 */     Type type = responseType.getType();
/*  626 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/*  627 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/*  628 */     return nonNull(execute(url, method, requestCallback, responseExtractor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
/*  635 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/*  636 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/*  637 */     return nonNull(doExecute(requestEntity.getUrl(), requestEntity.getMethod(), requestCallback, responseExtractor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) throws RestClientException {
/*  644 */     Type type = responseType.getType();
/*  645 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/*  646 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/*  647 */     return nonNull(doExecute(requestEntity.getUrl(), requestEntity.getMethod(), requestCallback, responseExtractor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T execute(String url, HttpMethod method, @Nullable RequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor, Object... uriVariables) throws RestClientException {
/*  669 */     URI expanded = getUriTemplateHandler().expand(url, uriVariables);
/*  670 */     return doExecute(expanded, method, requestCallback, responseExtractor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T execute(String url, HttpMethod method, @Nullable RequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables) throws RestClientException {
/*  690 */     URI expanded = getUriTemplateHandler().expand(url, uriVariables);
/*  691 */     return doExecute(expanded, method, requestCallback, responseExtractor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <T> T execute(URI url, HttpMethod method, @Nullable RequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {
/*  710 */     return doExecute(url, method, requestCallback, responseExtractor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback, @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {
/*  727 */     Assert.notNull(url, "URI is required");
/*  728 */     Assert.notNull(method, "HttpMethod is required");
/*  729 */     ClientHttpResponse response = null;
/*      */     try {
/*  731 */       ClientHttpRequest request = createRequest(url, method);
/*  732 */       if (requestCallback != null) {
/*  733 */         requestCallback.doWithRequest(request);
/*      */       }
/*  735 */       response = request.execute();
/*  736 */       handleResponse(url, method, response);
/*  737 */       return (responseExtractor != null) ? responseExtractor.extractData(response) : null;
/*      */     }
/*  739 */     catch (IOException ex) {
/*  740 */       String resource = url.toString();
/*  741 */       String query = url.getRawQuery();
/*  742 */       resource = (query != null) ? resource.substring(0, resource.indexOf('?')) : resource;
/*  743 */       throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" + resource + "\": " + ex
/*  744 */           .getMessage(), ex);
/*      */     } finally {
/*      */       
/*  747 */       if (response != null) {
/*  748 */         response.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleResponse(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
/*  765 */     ResponseErrorHandler errorHandler = getErrorHandler();
/*  766 */     boolean hasError = errorHandler.hasError(response);
/*  767 */     if (this.logger.isDebugEnabled()) {
/*      */       try {
/*  769 */         int code = response.getRawStatusCode();
/*  770 */         HttpStatus status = HttpStatus.resolve(code);
/*  771 */         this.logger.debug("Response " + ((status != null) ? status : Integer.valueOf(code)));
/*      */       }
/*  773 */       catch (IOException iOException) {}
/*      */     }
/*      */ 
/*      */     
/*  777 */     if (hasError) {
/*  778 */       errorHandler.handleError(url, method, response);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> RequestCallback acceptHeaderRequestCallback(Class<T> responseType) {
/*  788 */     return new AcceptHeaderRequestCallback(responseType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> RequestCallback httpEntityCallback(@Nullable Object requestBody) {
/*  796 */     return new HttpEntityRequestCallback(requestBody);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> RequestCallback httpEntityCallback(@Nullable Object requestBody, Type responseType) {
/*  808 */     return new HttpEntityRequestCallback(requestBody, responseType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
/*  815 */     return new ResponseEntityResponseExtractor<>(responseType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ResponseExtractor<HttpHeaders> headersExtractor() {
/*  822 */     return this.headersExtractor;
/*      */   }
/*      */   
/*      */   private static <T> T nonNull(@Nullable T result) {
/*  826 */     Assert.state((result != null), "No result");
/*  827 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class AcceptHeaderRequestCallback
/*      */     implements RequestCallback
/*      */   {
/*      */     @Nullable
/*      */     private final Type responseType;
/*      */ 
/*      */     
/*      */     public AcceptHeaderRequestCallback(Type responseType) {
/*  840 */       this.responseType = responseType;
/*      */     }
/*      */ 
/*      */     
/*      */     public void doWithRequest(ClientHttpRequest request) throws IOException {
/*  845 */       if (this.responseType != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  851 */         List<MediaType> allSupportedMediaTypes = (List<MediaType>)RestTemplate.this.getMessageConverters().stream().filter(converter -> canReadResponse(this.responseType, converter)).flatMap(this::getSupportedMediaTypes).distinct().sorted(MediaType.SPECIFICITY_COMPARATOR).collect(Collectors.toList());
/*  852 */         if (RestTemplate.this.logger.isDebugEnabled()) {
/*  853 */           RestTemplate.this.logger.debug("Accept=" + allSupportedMediaTypes);
/*      */         }
/*  855 */         request.getHeaders().setAccept(allSupportedMediaTypes);
/*      */       } 
/*      */     }
/*      */     
/*      */     private boolean canReadResponse(Type responseType, HttpMessageConverter<?> converter) {
/*  860 */       Class<?> responseClass = (responseType instanceof Class) ? (Class)responseType : null;
/*  861 */       if (responseClass != null) {
/*  862 */         return converter.canRead(responseClass, null);
/*      */       }
/*  864 */       if (converter instanceof GenericHttpMessageConverter) {
/*  865 */         GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter)converter;
/*  866 */         return genericConverter.canRead(responseType, null, null);
/*      */       } 
/*  868 */       return false;
/*      */     }
/*      */     
/*      */     private Stream<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
/*  872 */       return messageConverter.getSupportedMediaTypes()
/*  873 */         .stream()
/*  874 */         .map(mediaType -> (mediaType.getCharset() != null) ? new MediaType(mediaType.getType(), mediaType.getSubtype()) : mediaType);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class HttpEntityRequestCallback
/*      */     extends AcceptHeaderRequestCallback
/*      */   {
/*      */     private final HttpEntity<?> requestEntity;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public HttpEntityRequestCallback(Object requestBody) {
/*  892 */       this(requestBody, null);
/*      */     }
/*      */     
/*      */     public HttpEntityRequestCallback(@Nullable Object requestBody, Type responseType) {
/*  896 */       super(responseType);
/*  897 */       if (requestBody instanceof HttpEntity) {
/*  898 */         this.requestEntity = (HttpEntity)requestBody;
/*      */       }
/*  900 */       else if (requestBody != null) {
/*  901 */         this.requestEntity = new HttpEntity(requestBody);
/*      */       } else {
/*      */         
/*  904 */         this.requestEntity = HttpEntity.EMPTY;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
/*  911 */       super.doWithRequest(httpRequest);
/*  912 */       Object requestBody = this.requestEntity.getBody();
/*  913 */       if (requestBody == null) {
/*  914 */         HttpHeaders httpHeaders = httpRequest.getHeaders();
/*  915 */         HttpHeaders requestHeaders = this.requestEntity.getHeaders();
/*  916 */         if (!requestHeaders.isEmpty()) {
/*  917 */           requestHeaders.forEach((key, values) -> httpHeaders.put(key, new LinkedList(values)));
/*      */         }
/*  919 */         if (httpHeaders.getContentLength() < 0L) {
/*  920 */           httpHeaders.setContentLength(0L);
/*      */         }
/*      */       } else {
/*      */         
/*  924 */         Class<?> requestBodyClass = requestBody.getClass();
/*      */         
/*  926 */         Type requestBodyType = (this.requestEntity instanceof RequestEntity) ? ((RequestEntity)this.requestEntity).getType() : requestBodyClass;
/*  927 */         HttpHeaders httpHeaders = httpRequest.getHeaders();
/*  928 */         HttpHeaders requestHeaders = this.requestEntity.getHeaders();
/*  929 */         MediaType requestContentType = requestHeaders.getContentType();
/*  930 */         for (HttpMessageConverter<?> messageConverter : RestTemplate.this.getMessageConverters()) {
/*  931 */           if (messageConverter instanceof GenericHttpMessageConverter) {
/*  932 */             GenericHttpMessageConverter<Object> genericConverter = (GenericHttpMessageConverter)messageConverter;
/*      */             
/*  934 */             if (genericConverter.canWrite(requestBodyType, requestBodyClass, requestContentType)) {
/*  935 */               if (!requestHeaders.isEmpty()) {
/*  936 */                 requestHeaders.forEach((key, values) -> httpHeaders.put(key, new LinkedList(values)));
/*      */               }
/*  938 */               logBody(requestBody, requestContentType, (HttpMessageConverter<?>)genericConverter);
/*  939 */               genericConverter.write(requestBody, requestBodyType, requestContentType, (HttpOutputMessage)httpRequest); return;
/*      */             } 
/*      */             continue;
/*      */           } 
/*  943 */           if (messageConverter.canWrite(requestBodyClass, requestContentType)) {
/*  944 */             if (!requestHeaders.isEmpty()) {
/*  945 */               requestHeaders.forEach((key, values) -> httpHeaders.put(key, new LinkedList(values)));
/*      */             }
/*  947 */             logBody(requestBody, requestContentType, messageConverter);
/*  948 */             messageConverter.write(requestBody, requestContentType, (HttpOutputMessage)httpRequest);
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/*  953 */         String message = "No HttpMessageConverter for " + requestBodyClass.getName();
/*  954 */         if (requestContentType != null) {
/*  955 */           message = message + " and content type \"" + requestContentType + "\"";
/*      */         }
/*  957 */         throw new RestClientException(message);
/*      */       } 
/*      */     }
/*      */     
/*      */     private void logBody(Object body, @Nullable MediaType mediaType, HttpMessageConverter<?> converter) {
/*  962 */       if (RestTemplate.this.logger.isDebugEnabled()) {
/*  963 */         if (mediaType != null) {
/*  964 */           RestTemplate.this.logger.debug("Writing [" + body + "] as \"" + mediaType + "\"");
/*      */         } else {
/*      */           
/*  967 */           RestTemplate.this.logger.debug("Writing [" + body + "] with " + converter.getClass().getName());
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class ResponseEntityResponseExtractor<T>
/*      */     implements ResponseExtractor<ResponseEntity<T>>
/*      */   {
/*      */     @Nullable
/*      */     private final HttpMessageConverterExtractor<T> delegate;
/*      */ 
/*      */     
/*      */     public ResponseEntityResponseExtractor(Type responseType) {
/*  983 */       if (responseType != null && Void.class != responseType) {
/*  984 */         this.delegate = new HttpMessageConverterExtractor<>(responseType, RestTemplate.this.getMessageConverters(), RestTemplate.this.logger);
/*      */       } else {
/*      */         
/*  987 */         this.delegate = null;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
/*  993 */       if (this.delegate != null) {
/*  994 */         T body = this.delegate.extractData(response);
/*  995 */         return ((ResponseEntity.BodyBuilder)ResponseEntity.status(response.getRawStatusCode()).headers(response.getHeaders())).body(body);
/*      */       } 
/*      */       
/*  998 */       return ((ResponseEntity.BodyBuilder)ResponseEntity.status(response.getRawStatusCode()).headers(response.getHeaders())).build();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class HeadersExtractor
/*      */     implements ResponseExtractor<HttpHeaders>
/*      */   {
/*      */     private HeadersExtractor() {}
/*      */ 
/*      */     
/*      */     public HttpHeaders extractData(ClientHttpResponse response) {
/* 1011 */       return response.getHeaders();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/RestTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */