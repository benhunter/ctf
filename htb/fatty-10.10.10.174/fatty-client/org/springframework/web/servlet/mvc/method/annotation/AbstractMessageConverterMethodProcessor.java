/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.InputStreamResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.ResourceRegion;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.HttpRange;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.http.server.ServerHttpRequest;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.servlet.HandlerMapping;
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
/*     */ public abstract class AbstractMessageConverterMethodProcessor
/*     */   extends AbstractMessageConverterMethodArgumentResolver
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*  77 */   private static final Set<String> WHITELISTED_EXTENSIONS = new HashSet<>(Arrays.asList(new String[] { "txt", "text", "yml", "properties", "csv", "json", "xml", "atom", "rss", "png", "jpe", "jpeg", "jpg", "gif", "wbmp", "bmp" }));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   private static final Set<String> WHITELISTED_MEDIA_BASE_TYPES = new HashSet<>(
/*  83 */       Arrays.asList(new String[] { "audio", "image", "video" }));
/*     */ 
/*     */   
/*  86 */   private static final List<MediaType> ALL_APPLICATION_MEDIA_TYPES = Arrays.asList(new MediaType[] { MediaType.ALL, new MediaType("application") });
/*     */   
/*  88 */   private static final Type RESOURCE_REGION_LIST_TYPE = (new ParameterizedTypeReference<List<ResourceRegion>>() {  }
/*  89 */     ).getType();
/*     */ 
/*     */   
/*  92 */   private static final UrlPathHelper decodingUrlPathHelper = new UrlPathHelper();
/*     */   
/*  94 */   private static final UrlPathHelper rawUrlPathHelper = new UrlPathHelper(); private final ContentNegotiationManager contentNegotiationManager;
/*     */   
/*     */   static {
/*  97 */     rawUrlPathHelper.setRemoveSemicolonContent(false);
/*  98 */     rawUrlPathHelper.setUrlDecode(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final PathExtensionContentNegotiationStrategy pathStrategy;
/*     */ 
/*     */   
/* 106 */   private final Set<String> safeExtensions = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters) {
/* 113 */     this(converters, (ContentNegotiationManager)null, (List<Object>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters, @Nullable ContentNegotiationManager contentNegotiationManager) {
/* 122 */     this(converters, contentNegotiationManager, (List<Object>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters, @Nullable ContentNegotiationManager manager, @Nullable List<Object> requestResponseBodyAdvice) {
/* 132 */     super(converters, requestResponseBodyAdvice);
/*     */     
/* 134 */     this.contentNegotiationManager = (manager != null) ? manager : new ContentNegotiationManager();
/* 135 */     this.pathStrategy = initPathStrategy(this.contentNegotiationManager);
/* 136 */     this.safeExtensions.addAll(this.contentNegotiationManager.getAllFileExtensions());
/* 137 */     this.safeExtensions.addAll(WHITELISTED_EXTENSIONS);
/*     */   }
/*     */   
/*     */   private static PathExtensionContentNegotiationStrategy initPathStrategy(ContentNegotiationManager manager) {
/* 141 */     Class<PathExtensionContentNegotiationStrategy> clazz = PathExtensionContentNegotiationStrategy.class;
/* 142 */     PathExtensionContentNegotiationStrategy strategy = (PathExtensionContentNegotiationStrategy)manager.getStrategy(clazz);
/* 143 */     return (strategy != null) ? strategy : new PathExtensionContentNegotiationStrategy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
/* 153 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 154 */     Assert.state((response != null), "No HttpServletResponse");
/* 155 */     return new ServletServerHttpResponse(response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> void writeWithMessageConverters(T value, MethodParameter returnType, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
/* 165 */     ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
/* 166 */     ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
/* 167 */     writeWithMessageConverters(value, returnType, inputMessage, outputMessage);
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
/*     */   protected <T> void writeWithMessageConverters(@Nullable T value, MethodParameter returnType, ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
/*     */     Object object;
/*     */     Class<?> clazz;
/*     */     Type type;
/* 189 */     if (value instanceof CharSequence) {
/* 190 */       object = value.toString();
/* 191 */       clazz = String.class;
/* 192 */       type = String.class;
/*     */     } else {
/*     */       
/* 195 */       object = value;
/* 196 */       clazz = getReturnValueType(object, returnType);
/* 197 */       type = GenericTypeResolver.resolveType(getGenericType(returnType), returnType.getContainingClass());
/*     */     } 
/*     */     
/* 200 */     if (isResourceType(value, returnType)) {
/* 201 */       outputMessage.getHeaders().set("Accept-Ranges", "bytes");
/* 202 */       if (value != null && inputMessage.getHeaders().getFirst("Range") != null && outputMessage
/* 203 */         .getServletResponse().getStatus() == 200) {
/* 204 */         Resource resource = (Resource)value;
/*     */         try {
/* 206 */           List<HttpRange> httpRanges = inputMessage.getHeaders().getRange();
/* 207 */           outputMessage.getServletResponse().setStatus(HttpStatus.PARTIAL_CONTENT.value());
/* 208 */           object = HttpRange.toResourceRegions(httpRanges, resource);
/* 209 */           clazz = object.getClass();
/* 210 */           type = RESOURCE_REGION_LIST_TYPE;
/*     */         }
/* 212 */         catch (IllegalArgumentException ex) {
/* 213 */           outputMessage.getHeaders().set("Content-Range", "bytes */" + resource.contentLength());
/* 214 */           outputMessage.getServletResponse().setStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 219 */     MediaType selectedMediaType = null;
/* 220 */     MediaType contentType = outputMessage.getHeaders().getContentType();
/* 221 */     if (contentType != null && contentType.isConcrete()) {
/* 222 */       if (this.logger.isDebugEnabled()) {
/* 223 */         this.logger.debug("Found 'Content-Type:" + contentType + "' in response");
/*     */       }
/* 225 */       selectedMediaType = contentType;
/*     */     } else {
/*     */       
/* 228 */       HttpServletRequest request = inputMessage.getServletRequest();
/* 229 */       List<MediaType> acceptableTypes = getAcceptableMediaTypes(request);
/* 230 */       List<MediaType> producibleTypes = getProducibleMediaTypes(request, clazz, type);
/*     */       
/* 232 */       if (object != null && producibleTypes.isEmpty()) {
/* 233 */         throw new HttpMessageNotWritableException("No converter found for return value of type: " + clazz);
/*     */       }
/*     */       
/* 236 */       List<MediaType> mediaTypesToUse = new ArrayList<>();
/* 237 */       for (MediaType requestedType : acceptableTypes) {
/* 238 */         for (MediaType producibleType : producibleTypes) {
/* 239 */           if (requestedType.isCompatibleWith(producibleType)) {
/* 240 */             mediaTypesToUse.add(getMostSpecificMediaType(requestedType, producibleType));
/*     */           }
/*     */         } 
/*     */       } 
/* 244 */       if (mediaTypesToUse.isEmpty()) {
/* 245 */         if (object != null) {
/* 246 */           throw new HttpMediaTypeNotAcceptableException(producibleTypes);
/*     */         }
/* 248 */         if (this.logger.isDebugEnabled()) {
/* 249 */           this.logger.debug("No match for " + acceptableTypes + ", supported: " + producibleTypes);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/* 254 */       MediaType.sortBySpecificityAndQuality(mediaTypesToUse);
/*     */       
/* 256 */       for (MediaType mediaType : mediaTypesToUse) {
/* 257 */         if (mediaType.isConcrete()) {
/* 258 */           selectedMediaType = mediaType;
/*     */           break;
/*     */         } 
/* 261 */         if (mediaType.isPresentIn(ALL_APPLICATION_MEDIA_TYPES)) {
/* 262 */           selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 267 */       if (this.logger.isDebugEnabled()) {
/* 268 */         this.logger.debug("Using '" + selectedMediaType + "', given " + acceptableTypes + " and supported " + producibleTypes);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 273 */     if (selectedMediaType != null) {
/* 274 */       selectedMediaType = selectedMediaType.removeQualityValue();
/* 275 */       for (HttpMessageConverter<?> converter : this.messageConverters) {
/* 276 */         GenericHttpMessageConverter genericConverter = (converter instanceof GenericHttpMessageConverter) ? (GenericHttpMessageConverter)converter : null;
/*     */         
/* 278 */         if ((genericConverter != null) ? ((GenericHttpMessageConverter)converter)
/* 279 */           .canWrite(type, clazz, selectedMediaType) : converter
/* 280 */           .canWrite(clazz, selectedMediaType)) {
/* 281 */           object = getAdvice().beforeBodyWrite(object, returnType, selectedMediaType, (Class)converter
/* 282 */               .getClass(), (ServerHttpRequest)inputMessage, (ServerHttpResponse)outputMessage);
/*     */           
/* 284 */           if (object != null) {
/* 285 */             Object theBody = object;
/* 286 */             LogFormatUtils.traceDebug(this.logger, traceOn -> "Writing [" + LogFormatUtils.formatValue(theBody, !traceOn.booleanValue()) + "]");
/*     */             
/* 288 */             addContentDispositionHeader(inputMessage, outputMessage);
/* 289 */             if (genericConverter != null) {
/* 290 */               genericConverter.write(object, type, selectedMediaType, (HttpOutputMessage)outputMessage);
/*     */             } else {
/*     */               
/* 293 */               converter.write(object, selectedMediaType, (HttpOutputMessage)outputMessage);
/*     */             }
/*     */           
/*     */           }
/* 297 */           else if (this.logger.isDebugEnabled()) {
/* 298 */             this.logger.debug("Nothing to write: null body");
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 306 */     if (object != null) {
/* 307 */       throw new HttpMediaTypeNotAcceptableException(this.allSupportedMediaTypes);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getReturnValueType(@Nullable Object value, MethodParameter returnType) {
/* 318 */     return (value != null) ? value.getClass() : returnType.getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isResourceType(@Nullable Object value, MethodParameter returnType) {
/* 325 */     Class<?> clazz = getReturnValueType(value, returnType);
/* 326 */     return (clazz != InputStreamResource.class && Resource.class.isAssignableFrom(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Type getGenericType(MethodParameter returnType) {
/* 334 */     if (HttpEntity.class.isAssignableFrom(returnType.getParameterType())) {
/* 335 */       return ResolvableType.forType(returnType.getGenericParameterType()).getGeneric(new int[0]).getType();
/*     */     }
/*     */     
/* 338 */     return returnType.getGenericParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> valueClass) {
/* 348 */     return getProducibleMediaTypes(request, valueClass, (Type)null);
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
/*     */   protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> valueClass, @Nullable Type targetType) {
/* 365 */     Set<MediaType> mediaTypes = (Set<MediaType>)request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
/* 366 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 367 */       return new ArrayList<>(mediaTypes);
/*     */     }
/* 369 */     if (!this.allSupportedMediaTypes.isEmpty()) {
/* 370 */       List<MediaType> result = new ArrayList<>();
/* 371 */       for (HttpMessageConverter<?> converter : this.messageConverters) {
/* 372 */         if (converter instanceof GenericHttpMessageConverter && targetType != null) {
/* 373 */           if (((GenericHttpMessageConverter)converter).canWrite(targetType, valueClass, null))
/* 374 */             result.addAll(converter.getSupportedMediaTypes()); 
/*     */           continue;
/*     */         } 
/* 377 */         if (converter.canWrite(valueClass, null)) {
/* 378 */           result.addAll(converter.getSupportedMediaTypes());
/*     */         }
/*     */       } 
/* 381 */       return result;
/*     */     } 
/*     */     
/* 384 */     return Collections.singletonList(MediaType.ALL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<MediaType> getAcceptableMediaTypes(HttpServletRequest request) throws HttpMediaTypeNotAcceptableException {
/* 391 */     return this.contentNegotiationManager.resolveMediaTypes((NativeWebRequest)new ServletWebRequest(request));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MediaType getMostSpecificMediaType(MediaType acceptType, MediaType produceType) {
/* 399 */     MediaType produceTypeToUse = produceType.copyQualityValue(acceptType);
/* 400 */     return (MediaType.SPECIFICITY_COMPARATOR.compare(acceptType, produceTypeToUse) <= 0) ? acceptType : produceTypeToUse;
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
/*     */   private void addContentDispositionHeader(ServletServerHttpRequest request, ServletServerHttpResponse response) {
/* 412 */     HttpHeaders headers = response.getHeaders();
/* 413 */     if (headers.containsKey("Content-Disposition")) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 418 */       int status = response.getServletResponse().getStatus();
/* 419 */       if (status < 200 || status > 299) {
/*     */         return;
/*     */       }
/*     */     }
/* 423 */     catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */     
/* 427 */     HttpServletRequest servletRequest = request.getServletRequest();
/* 428 */     String requestUri = rawUrlPathHelper.getOriginatingRequestUri(servletRequest);
/*     */     
/* 430 */     int index = requestUri.lastIndexOf('/') + 1;
/* 431 */     String filename = requestUri.substring(index);
/* 432 */     String pathParams = "";
/*     */     
/* 434 */     index = filename.indexOf(';');
/* 435 */     if (index != -1) {
/* 436 */       pathParams = filename.substring(index);
/* 437 */       filename = filename.substring(0, index);
/*     */     } 
/*     */     
/* 440 */     filename = decodingUrlPathHelper.decodeRequestString(servletRequest, filename);
/* 441 */     String ext = StringUtils.getFilenameExtension(filename);
/*     */     
/* 443 */     pathParams = decodingUrlPathHelper.decodeRequestString(servletRequest, pathParams);
/* 444 */     String extInPathParams = StringUtils.getFilenameExtension(pathParams);
/*     */     
/* 446 */     if (!safeExtension(servletRequest, ext) || !safeExtension(servletRequest, extInPathParams)) {
/* 447 */       headers.add("Content-Disposition", "inline;filename=f.txt");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean safeExtension(HttpServletRequest request, @Nullable String extension) {
/* 453 */     if (!StringUtils.hasText(extension)) {
/* 454 */       return true;
/*     */     }
/* 456 */     extension = extension.toLowerCase(Locale.ENGLISH);
/* 457 */     if (this.safeExtensions.contains(extension)) {
/* 458 */       return true;
/*     */     }
/* 460 */     String pattern = (String)request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
/* 461 */     if (pattern != null && pattern.endsWith("." + extension)) {
/* 462 */       return true;
/*     */     }
/* 464 */     if (extension.equals("html")) {
/* 465 */       String name = HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE;
/* 466 */       Set<MediaType> mediaTypes = (Set<MediaType>)request.getAttribute(name);
/* 467 */       if (!CollectionUtils.isEmpty(mediaTypes) && mediaTypes.contains(MediaType.TEXT_HTML)) {
/* 468 */         return true;
/*     */       }
/*     */     } 
/* 471 */     return safeMediaTypesForExtension((NativeWebRequest)new ServletWebRequest(request), extension);
/*     */   }
/*     */   
/*     */   private boolean safeMediaTypesForExtension(NativeWebRequest request, String extension) {
/* 475 */     List<MediaType> mediaTypes = null;
/*     */     try {
/* 477 */       mediaTypes = this.pathStrategy.resolveMediaTypeKey(request, extension);
/*     */     }
/* 479 */     catch (HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException) {}
/*     */ 
/*     */     
/* 482 */     if (CollectionUtils.isEmpty(mediaTypes)) {
/* 483 */       return false;
/*     */     }
/* 485 */     for (MediaType mediaType : mediaTypes) {
/* 486 */       if (!safeMediaType(mediaType)) {
/* 487 */         return false;
/*     */       }
/*     */     } 
/* 490 */     return true;
/*     */   }
/*     */   
/*     */   private boolean safeMediaType(MediaType mediaType) {
/* 494 */     return (WHITELISTED_MEDIA_BASE_TYPES.contains(mediaType.getType()) || mediaType
/* 495 */       .getSubtype().endsWith("+xml"));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/AbstractMessageConverterMethodProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */