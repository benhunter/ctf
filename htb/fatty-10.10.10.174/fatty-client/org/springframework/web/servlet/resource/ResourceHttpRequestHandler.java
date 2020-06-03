/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.HttpRange;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*     */ import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ import org.springframework.web.HttpRequestHandler;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
/*     */ import org.springframework.web.accept.ServletPathExtensionContentNegotiationStrategy;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.cors.CorsConfigurationSource;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.support.WebContentGenerator;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceHttpRequestHandler
/*     */   extends WebContentGenerator
/*     */   implements HttpRequestHandler, EmbeddedValueResolverAware, InitializingBean, CorsConfigurationSource
/*     */ {
/* 101 */   private static final Log logger = LogFactory.getLog(ResourceHttpRequestHandler.class);
/*     */ 
/*     */   
/*     */   private static final String URL_RESOURCE_CHARSET_PREFIX = "[charset=";
/*     */   
/* 106 */   private final List<String> locationValues = new ArrayList<>(4);
/*     */   
/* 108 */   private final List<Resource> locations = new ArrayList<>(4);
/*     */   
/* 110 */   private final Map<Resource, Charset> locationCharsets = new HashMap<>(4);
/*     */   
/* 112 */   private final List<ResourceResolver> resourceResolvers = new ArrayList<>(4);
/*     */   
/* 114 */   private final List<ResourceTransformer> resourceTransformers = new ArrayList<>(4);
/*     */   
/*     */   @Nullable
/*     */   private ResourceResolverChain resolverChain;
/*     */   
/*     */   @Nullable
/*     */   private ResourceTransformerChain transformerChain;
/*     */   
/*     */   @Nullable
/*     */   private ResourceHttpMessageConverter resourceHttpMessageConverter;
/*     */   
/*     */   @Nullable
/*     */   private ResourceRegionHttpMessageConverter resourceRegionHttpMessageConverter;
/*     */   
/*     */   @Nullable
/*     */   private ContentNegotiationManager contentNegotiationManager;
/*     */   
/*     */   @Nullable
/*     */   private PathExtensionContentNegotiationStrategy contentNegotiationStrategy;
/*     */   
/*     */   @Nullable
/*     */   private CorsConfiguration corsConfiguration;
/*     */   
/*     */   @Nullable
/*     */   private UrlPathHelper urlPathHelper;
/*     */   
/*     */   @Nullable
/*     */   private StringValueResolver embeddedValueResolver;
/*     */ 
/*     */   
/*     */   public ResourceHttpRequestHandler() {
/* 145 */     super(new String[] { HttpMethod.GET.name(), HttpMethod.HEAD.name() });
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
/*     */   public void setLocationValues(List<String> locationValues) {
/* 158 */     Assert.notNull(locationValues, "Location values list must not be null");
/* 159 */     this.locationValues.clear();
/* 160 */     this.locationValues.addAll(locationValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocations(List<Resource> locations) {
/* 169 */     Assert.notNull(locations, "Locations list must not be null");
/* 170 */     this.locations.clear();
/* 171 */     this.locations.addAll(locations);
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
/*     */   public List<Resource> getLocations() {
/* 183 */     return this.locations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceResolvers(@Nullable List<ResourceResolver> resourceResolvers) {
/* 192 */     this.resourceResolvers.clear();
/* 193 */     if (resourceResolvers != null) {
/* 194 */       this.resourceResolvers.addAll(resourceResolvers);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ResourceResolver> getResourceResolvers() {
/* 202 */     return this.resourceResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceTransformers(@Nullable List<ResourceTransformer> resourceTransformers) {
/* 210 */     this.resourceTransformers.clear();
/* 211 */     if (resourceTransformers != null) {
/* 212 */       this.resourceTransformers.addAll(resourceTransformers);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ResourceTransformer> getResourceTransformers() {
/* 220 */     return this.resourceTransformers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceHttpMessageConverter(@Nullable ResourceHttpMessageConverter messageConverter) {
/* 229 */     this.resourceHttpMessageConverter = messageConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ResourceHttpMessageConverter getResourceHttpMessageConverter() {
/* 238 */     return this.resourceHttpMessageConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceRegionHttpMessageConverter(@Nullable ResourceRegionHttpMessageConverter messageConverter) {
/* 247 */     this.resourceRegionHttpMessageConverter = messageConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ResourceRegionHttpMessageConverter getResourceRegionHttpMessageConverter() {
/* 256 */     return this.resourceRegionHttpMessageConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentNegotiationManager(@Nullable ContentNegotiationManager contentNegotiationManager) {
/* 266 */     this.contentNegotiationManager = contentNegotiationManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ContentNegotiationManager getContentNegotiationManager() {
/* 275 */     return this.contentNegotiationManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorsConfiguration(CorsConfiguration corsConfiguration) {
/* 283 */     this.corsConfiguration = corsConfiguration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
/* 292 */     return this.corsConfiguration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(@Nullable UrlPathHelper urlPathHelper) {
/* 302 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public UrlPathHelper getUrlPathHelper() {
/* 311 */     return this.urlPathHelper;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEmbeddedValueResolver(StringValueResolver resolver) {
/* 316 */     this.embeddedValueResolver = resolver;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 322 */     resolveResourceLocations();
/*     */     
/* 324 */     if (logger.isWarnEnabled() && CollectionUtils.isEmpty(this.locations)) {
/* 325 */       logger.warn("Locations list is empty. No resources will be served unless a custom ResourceResolver is configured as an alternative to PathResourceResolver.");
/*     */     }
/*     */ 
/*     */     
/* 329 */     if (this.resourceResolvers.isEmpty()) {
/* 330 */       this.resourceResolvers.add(new PathResourceResolver());
/*     */     }
/*     */     
/* 333 */     initAllowedLocations();
/*     */ 
/*     */     
/* 336 */     this.resolverChain = new DefaultResourceResolverChain(this.resourceResolvers);
/* 337 */     this.transformerChain = new DefaultResourceTransformerChain(this.resolverChain, this.resourceTransformers);
/*     */     
/* 339 */     if (this.resourceHttpMessageConverter == null) {
/* 340 */       this.resourceHttpMessageConverter = new ResourceHttpMessageConverter();
/*     */     }
/* 342 */     if (this.resourceRegionHttpMessageConverter == null) {
/* 343 */       this.resourceRegionHttpMessageConverter = new ResourceRegionHttpMessageConverter();
/*     */     }
/*     */     
/* 346 */     this.contentNegotiationStrategy = initContentNegotiationStrategy();
/*     */   }
/*     */   
/*     */   private void resolveResourceLocations() {
/* 350 */     if (CollectionUtils.isEmpty(this.locationValues)) {
/*     */       return;
/*     */     }
/* 353 */     if (!CollectionUtils.isEmpty(this.locations)) {
/* 354 */       throw new IllegalArgumentException("Please set either Resource-based \"locations\" or String-based \"locationValues\", but not both.");
/*     */     }
/*     */ 
/*     */     
/* 358 */     ApplicationContext applicationContext = obtainApplicationContext();
/* 359 */     for (String location : this.locationValues) {
/* 360 */       if (this.embeddedValueResolver != null) {
/* 361 */         String resolvedLocation = this.embeddedValueResolver.resolveStringValue(location);
/* 362 */         if (resolvedLocation == null) {
/* 363 */           throw new IllegalArgumentException("Location resolved to null: " + location);
/*     */         }
/* 365 */         location = resolvedLocation;
/*     */       } 
/* 367 */       Charset charset = null;
/* 368 */       location = location.trim();
/* 369 */       if (location.startsWith("[charset=")) {
/* 370 */         int endIndex = location.indexOf(']', "[charset=".length());
/* 371 */         if (endIndex == -1) {
/* 372 */           throw new IllegalArgumentException("Invalid charset syntax in location: " + location);
/*     */         }
/* 374 */         String value = location.substring("[charset=".length(), endIndex);
/* 375 */         charset = Charset.forName(value);
/* 376 */         location = location.substring(endIndex + 1);
/*     */       } 
/* 378 */       Resource resource = applicationContext.getResource(location);
/* 379 */       this.locations.add(resource);
/* 380 */       if (charset != null) {
/* 381 */         if (!(resource instanceof org.springframework.core.io.UrlResource)) {
/* 382 */           throw new IllegalArgumentException("Unexpected charset for non-UrlResource: " + resource);
/*     */         }
/* 384 */         this.locationCharsets.put(resource, charset);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initAllowedLocations() {
/* 395 */     if (CollectionUtils.isEmpty(this.locations)) {
/*     */       return;
/*     */     }
/* 398 */     for (int i = getResourceResolvers().size() - 1; i >= 0; i--) {
/* 399 */       if (getResourceResolvers().get(i) instanceof PathResourceResolver) {
/* 400 */         PathResourceResolver pathResolver = (PathResourceResolver)getResourceResolvers().get(i);
/* 401 */         if (ObjectUtils.isEmpty((Object[])pathResolver.getAllowedLocations())) {
/* 402 */           pathResolver.setAllowedLocations(getLocations().<Resource>toArray(new Resource[0]));
/*     */         }
/* 404 */         if (this.urlPathHelper != null) {
/* 405 */           pathResolver.setLocationCharsets(this.locationCharsets);
/* 406 */           pathResolver.setUrlPathHelper(this.urlPathHelper);
/*     */         } 
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
/*     */   protected PathExtensionContentNegotiationStrategy initContentNegotiationStrategy() {
/* 420 */     Map<String, MediaType> mediaTypes = null;
/* 421 */     if (getContentNegotiationManager() != null) {
/*     */       
/* 423 */       PathExtensionContentNegotiationStrategy strategy = (PathExtensionContentNegotiationStrategy)getContentNegotiationManager().getStrategy(PathExtensionContentNegotiationStrategy.class);
/* 424 */       if (strategy != null) {
/* 425 */         mediaTypes = new HashMap<>(strategy.getMediaTypes());
/*     */       }
/*     */     } 
/* 428 */     return (getServletContext() != null) ? (PathExtensionContentNegotiationStrategy)new ServletPathExtensionContentNegotiationStrategy(
/* 429 */         getServletContext(), mediaTypes) : new PathExtensionContentNegotiationStrategy(mediaTypes);
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
/*     */   public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 451 */     Resource resource = getResource(request);
/* 452 */     if (resource == null) {
/* 453 */       logger.debug("Resource not found");
/* 454 */       response.sendError(404);
/*     */       
/*     */       return;
/*     */     } 
/* 458 */     if (HttpMethod.OPTIONS.matches(request.getMethod())) {
/* 459 */       response.setHeader("Allow", getAllowHeader());
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 464 */     checkRequest(request);
/*     */ 
/*     */     
/* 467 */     if ((new ServletWebRequest(request, response)).checkNotModified(resource.lastModified())) {
/* 468 */       logger.trace("Resource not modified");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 473 */     prepareResponse(response);
/*     */ 
/*     */     
/* 476 */     MediaType mediaType = getMediaType(request, resource);
/*     */ 
/*     */     
/* 479 */     if ("HEAD".equals(request.getMethod())) {
/* 480 */       setHeaders(response, resource, mediaType);
/*     */       
/*     */       return;
/*     */     } 
/* 484 */     ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
/* 485 */     if (request.getHeader("Range") == null) {
/* 486 */       Assert.state((this.resourceHttpMessageConverter != null), "Not initialized");
/* 487 */       setHeaders(response, resource, mediaType);
/* 488 */       this.resourceHttpMessageConverter.write(resource, mediaType, (HttpOutputMessage)outputMessage);
/*     */     } else {
/*     */       
/* 491 */       Assert.state((this.resourceRegionHttpMessageConverter != null), "Not initialized");
/* 492 */       response.setHeader("Accept-Ranges", "bytes");
/* 493 */       ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
/*     */       try {
/* 495 */         List<HttpRange> httpRanges = inputMessage.getHeaders().getRange();
/* 496 */         response.setStatus(206);
/* 497 */         this.resourceRegionHttpMessageConverter.write(
/* 498 */             HttpRange.toResourceRegions(httpRanges, resource), mediaType, (HttpOutputMessage)outputMessage);
/*     */       }
/* 500 */       catch (IllegalArgumentException ex) {
/* 501 */         response.setHeader("Content-Range", "bytes */" + resource.contentLength());
/* 502 */         response.sendError(416);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Resource getResource(HttpServletRequest request) throws IOException {
/* 509 */     String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
/* 510 */     if (path == null) {
/* 511 */       throw new IllegalStateException("Required request attribute '" + HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE + "' is not set");
/*     */     }
/*     */ 
/*     */     
/* 515 */     path = processPath(path);
/* 516 */     if (!StringUtils.hasText(path) || isInvalidPath(path)) {
/* 517 */       return null;
/*     */     }
/* 519 */     if (isInvalidEncodedPath(path)) {
/* 520 */       return null;
/*     */     }
/*     */     
/* 523 */     Assert.notNull(this.resolverChain, "ResourceResolverChain not initialized.");
/* 524 */     Assert.notNull(this.transformerChain, "ResourceTransformerChain not initialized.");
/*     */     
/* 526 */     Resource resource = this.resolverChain.resolveResource(request, path, getLocations());
/* 527 */     if (resource != null) {
/* 528 */       resource = this.transformerChain.transform(request, resource);
/*     */     }
/* 530 */     return resource;
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
/*     */   protected String processPath(String path) {
/* 546 */     path = StringUtils.replace(path, "\\", "/");
/* 547 */     path = cleanDuplicateSlashes(path);
/* 548 */     return cleanLeadingSlash(path);
/*     */   }
/*     */   
/*     */   private String cleanDuplicateSlashes(String path) {
/* 552 */     StringBuilder sb = null;
/* 553 */     char prev = Character.MIN_VALUE;
/* 554 */     for (int i = 0; i < path.length(); i++) {
/* 555 */       char curr = path.charAt(i);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 571 */     return (sb != null) ? sb.toString() : path;
/*     */   }
/*     */   
/*     */   private String cleanLeadingSlash(String path) {
/* 575 */     boolean slash = false;
/* 576 */     for (int i = 0; i < path.length(); i++) {
/* 577 */       if (path.charAt(i) == '/') {
/* 578 */         slash = true;
/*     */       }
/* 580 */       else if (path.charAt(i) > ' ' && path.charAt(i) != '') {
/* 581 */         if (i == 0 || (i == 1 && slash)) {
/* 582 */           return path;
/*     */         }
/* 584 */         return slash ? ("/" + path.substring(i)) : path.substring(i);
/*     */       } 
/*     */     } 
/* 587 */     return slash ? "/" : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isInvalidEncodedPath(String path) {
/* 596 */     if (path.contains("%")) {
/*     */       
/*     */       try {
/* 599 */         String decodedPath = URLDecoder.decode(path, "UTF-8");
/* 600 */         if (isInvalidPath(decodedPath)) {
/* 601 */           return true;
/*     */         }
/* 603 */         decodedPath = processPath(decodedPath);
/* 604 */         if (isInvalidPath(decodedPath)) {
/* 605 */           return true;
/*     */         }
/*     */       }
/* 608 */       catch (IllegalArgumentException|java.io.UnsupportedEncodingException illegalArgumentException) {}
/*     */     }
/*     */ 
/*     */     
/* 612 */     return false;
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
/*     */   protected boolean isInvalidPath(String path) {
/* 632 */     if (path.contains("WEB-INF") || path.contains("META-INF")) {
/* 633 */       if (logger.isWarnEnabled()) {
/* 634 */         logger.warn("Path with \"WEB-INF\" or \"META-INF\": [" + path + "]");
/*     */       }
/* 636 */       return true;
/*     */     } 
/* 638 */     if (path.contains(":/")) {
/* 639 */       String relativePath = (path.charAt(0) == '/') ? path.substring(1) : path;
/* 640 */       if (ResourceUtils.isUrl(relativePath) || relativePath.startsWith("url:")) {
/* 641 */         if (logger.isWarnEnabled()) {
/* 642 */           logger.warn("Path represents URL or has \"url:\" prefix: [" + path + "]");
/*     */         }
/* 644 */         return true;
/*     */       } 
/*     */     } 
/* 647 */     if (path.contains("..") && StringUtils.cleanPath(path).contains("../")) {
/* 648 */       if (logger.isWarnEnabled()) {
/* 649 */         logger.warn("Path contains \"../\" after call to StringUtils#cleanPath: [" + path + "]");
/*     */       }
/* 651 */       return true;
/*     */     } 
/* 653 */     return false;
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
/*     */   @Nullable
/*     */   protected MediaType getMediaType(HttpServletRequest request, Resource resource) {
/* 667 */     return (this.contentNegotiationStrategy != null) ? this.contentNegotiationStrategy
/* 668 */       .getMediaTypeForResource(resource) : null;
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
/*     */   protected void setHeaders(HttpServletResponse response, Resource resource, @Nullable MediaType mediaType) throws IOException {
/* 682 */     long length = resource.contentLength();
/* 683 */     if (length > 2147483647L) {
/* 684 */       response.setContentLengthLong(length);
/*     */     } else {
/*     */       
/* 687 */       response.setContentLength((int)length);
/*     */     } 
/*     */     
/* 690 */     if (mediaType != null) {
/* 691 */       response.setContentType(mediaType.toString());
/*     */     }
/* 693 */     if (resource instanceof HttpResource) {
/* 694 */       HttpHeaders resourceHeaders = ((HttpResource)resource).getResponseHeaders();
/* 695 */       resourceHeaders.forEach((headerName, headerValues) -> {
/*     */             boolean first = true;
/*     */             
/*     */             for (String headerValue : headerValues) {
/*     */               if (first) {
/*     */                 response.setHeader(headerName, headerValue);
/*     */               } else {
/*     */                 response.addHeader(headerName, headerValue);
/*     */               } 
/*     */               first = false;
/*     */             } 
/*     */           });
/*     */     } 
/* 708 */     response.setHeader("Accept-Ranges", "bytes");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 714 */     return "ResourceHttpRequestHandler " + formatLocations();
/*     */   }
/*     */   
/*     */   private Object formatLocations() {
/* 718 */     if (!this.locationValues.isEmpty()) {
/* 719 */       return this.locationValues.stream().collect(Collectors.joining("\", \"", "[\"", "\"]"));
/*     */     }
/* 721 */     if (!this.locations.isEmpty()) {
/* 722 */       return this.locations;
/*     */     }
/* 724 */     return Collections.emptyList();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/ResourceHttpRequestHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */