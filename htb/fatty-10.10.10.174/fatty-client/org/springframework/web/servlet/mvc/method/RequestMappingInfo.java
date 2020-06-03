/*     */ package org.springframework.web.servlet.mvc.method;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.RequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;
/*     */ import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
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
/*     */ public final class RequestMappingInfo
/*     */   implements RequestCondition<RequestMappingInfo>
/*     */ {
/*     */   @Nullable
/*     */   private final String name;
/*     */   private final PatternsRequestCondition patternsCondition;
/*     */   private final RequestMethodsRequestCondition methodsCondition;
/*     */   private final ParamsRequestCondition paramsCondition;
/*     */   private final HeadersRequestCondition headersCondition;
/*     */   private final ConsumesRequestCondition consumesCondition;
/*     */   private final ProducesRequestCondition producesCondition;
/*     */   private final RequestConditionHolder customConditionHolder;
/*     */   
/*     */   public RequestMappingInfo(@Nullable String name, @Nullable PatternsRequestCondition patterns, @Nullable RequestMethodsRequestCondition methods, @Nullable ParamsRequestCondition params, @Nullable HeadersRequestCondition headers, @Nullable ConsumesRequestCondition consumes, @Nullable ProducesRequestCondition produces, @Nullable RequestCondition<?> custom) {
/*  80 */     this.name = StringUtils.hasText(name) ? name : null;
/*  81 */     this.patternsCondition = (patterns != null) ? patterns : new PatternsRequestCondition(new String[0]);
/*  82 */     this.methodsCondition = (methods != null) ? methods : new RequestMethodsRequestCondition(new RequestMethod[0]);
/*  83 */     this.paramsCondition = (params != null) ? params : new ParamsRequestCondition(new String[0]);
/*  84 */     this.headersCondition = (headers != null) ? headers : new HeadersRequestCondition(new String[0]);
/*  85 */     this.consumesCondition = (consumes != null) ? consumes : new ConsumesRequestCondition(new String[0]);
/*  86 */     this.producesCondition = (produces != null) ? produces : new ProducesRequestCondition(new String[0]);
/*  87 */     this.customConditionHolder = new RequestConditionHolder(custom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestMappingInfo(@Nullable PatternsRequestCondition patterns, @Nullable RequestMethodsRequestCondition methods, @Nullable ParamsRequestCondition params, @Nullable HeadersRequestCondition headers, @Nullable ConsumesRequestCondition consumes, @Nullable ProducesRequestCondition produces, @Nullable RequestCondition<?> custom) {
/*  98 */     this(null, patterns, methods, params, headers, consumes, produces, custom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestMappingInfo(RequestMappingInfo info, @Nullable RequestCondition<?> customRequestCondition) {
/* 105 */     this(info.name, info.patternsCondition, info.methodsCondition, info.paramsCondition, info.headersCondition, info.consumesCondition, info.producesCondition, customRequestCondition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getName() {
/* 115 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternsRequestCondition getPatternsCondition() {
/* 123 */     return this.patternsCondition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestMethodsRequestCondition getMethodsCondition() {
/* 131 */     return this.methodsCondition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParamsRequestCondition getParamsCondition() {
/* 139 */     return this.paramsCondition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeadersRequestCondition getHeadersCondition() {
/* 147 */     return this.headersCondition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConsumesRequestCondition getConsumesCondition() {
/* 155 */     return this.consumesCondition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProducesRequestCondition getProducesCondition() {
/* 163 */     return this.producesCondition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RequestCondition<?> getCustomCondition() {
/* 171 */     return this.customConditionHolder.getCondition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestMappingInfo combine(RequestMappingInfo other) {
/* 182 */     String name = combineNames(other);
/* 183 */     PatternsRequestCondition patterns = this.patternsCondition.combine(other.patternsCondition);
/* 184 */     RequestMethodsRequestCondition methods = this.methodsCondition.combine(other.methodsCondition);
/* 185 */     ParamsRequestCondition params = this.paramsCondition.combine(other.paramsCondition);
/* 186 */     HeadersRequestCondition headers = this.headersCondition.combine(other.headersCondition);
/* 187 */     ConsumesRequestCondition consumes = this.consumesCondition.combine(other.consumesCondition);
/* 188 */     ProducesRequestCondition produces = this.producesCondition.combine(other.producesCondition);
/* 189 */     RequestConditionHolder custom = this.customConditionHolder.combine(other.customConditionHolder);
/*     */     
/* 191 */     return new RequestMappingInfo(name, patterns, methods, params, headers, consumes, produces, custom
/* 192 */         .getCondition());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String combineNames(RequestMappingInfo other) {
/* 197 */     if (this.name != null && other.name != null) {
/* 198 */       String separator = "#";
/* 199 */       return this.name + separator + other.name;
/*     */     } 
/* 201 */     if (this.name != null) {
/* 202 */       return this.name;
/*     */     }
/*     */     
/* 205 */     return other.name;
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
/*     */   public RequestMappingInfo getMatchingCondition(HttpServletRequest request) {
/* 219 */     RequestMethodsRequestCondition methods = this.methodsCondition.getMatchingCondition(request);
/* 220 */     if (methods == null) {
/* 221 */       return null;
/*     */     }
/* 223 */     ParamsRequestCondition params = this.paramsCondition.getMatchingCondition(request);
/* 224 */     if (params == null) {
/* 225 */       return null;
/*     */     }
/* 227 */     HeadersRequestCondition headers = this.headersCondition.getMatchingCondition(request);
/* 228 */     if (headers == null) {
/* 229 */       return null;
/*     */     }
/* 231 */     ConsumesRequestCondition consumes = this.consumesCondition.getMatchingCondition(request);
/* 232 */     if (consumes == null) {
/* 233 */       return null;
/*     */     }
/* 235 */     ProducesRequestCondition produces = this.producesCondition.getMatchingCondition(request);
/* 236 */     if (produces == null) {
/* 237 */       return null;
/*     */     }
/* 239 */     PatternsRequestCondition patterns = this.patternsCondition.getMatchingCondition(request);
/* 240 */     if (patterns == null) {
/* 241 */       return null;
/*     */     }
/* 243 */     RequestConditionHolder custom = this.customConditionHolder.getMatchingCondition(request);
/* 244 */     if (custom == null) {
/* 245 */       return null;
/*     */     }
/*     */     
/* 248 */     return new RequestMappingInfo(this.name, patterns, methods, params, headers, consumes, produces, custom
/* 249 */         .getCondition());
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
/*     */   public int compareTo(RequestMappingInfo other, HttpServletRequest request) {
/* 262 */     if (HttpMethod.HEAD.matches(request.getMethod())) {
/* 263 */       int i = this.methodsCondition.compareTo(other.getMethodsCondition(), request);
/* 264 */       if (i != 0) {
/* 265 */         return i;
/*     */       }
/*     */     } 
/* 268 */     int result = this.patternsCondition.compareTo(other.getPatternsCondition(), request);
/* 269 */     if (result != 0) {
/* 270 */       return result;
/*     */     }
/* 272 */     result = this.paramsCondition.compareTo(other.getParamsCondition(), request);
/* 273 */     if (result != 0) {
/* 274 */       return result;
/*     */     }
/* 276 */     result = this.headersCondition.compareTo(other.getHeadersCondition(), request);
/* 277 */     if (result != 0) {
/* 278 */       return result;
/*     */     }
/* 280 */     result = this.consumesCondition.compareTo(other.getConsumesCondition(), request);
/* 281 */     if (result != 0) {
/* 282 */       return result;
/*     */     }
/* 284 */     result = this.producesCondition.compareTo(other.getProducesCondition(), request);
/* 285 */     if (result != 0) {
/* 286 */       return result;
/*     */     }
/*     */     
/* 289 */     result = this.methodsCondition.compareTo(other.getMethodsCondition(), request);
/* 290 */     if (result != 0) {
/* 291 */       return result;
/*     */     }
/* 293 */     result = this.customConditionHolder.compareTo(other.customConditionHolder, request);
/* 294 */     if (result != 0) {
/* 295 */       return result;
/*     */     }
/* 297 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 302 */     if (this == other) {
/* 303 */       return true;
/*     */     }
/* 305 */     if (!(other instanceof RequestMappingInfo)) {
/* 306 */       return false;
/*     */     }
/* 308 */     RequestMappingInfo otherInfo = (RequestMappingInfo)other;
/* 309 */     return (this.patternsCondition.equals(otherInfo.patternsCondition) && this.methodsCondition
/* 310 */       .equals(otherInfo.methodsCondition) && this.paramsCondition
/* 311 */       .equals(otherInfo.paramsCondition) && this.headersCondition
/* 312 */       .equals(otherInfo.headersCondition) && this.consumesCondition
/* 313 */       .equals(otherInfo.consumesCondition) && this.producesCondition
/* 314 */       .equals(otherInfo.producesCondition) && this.customConditionHolder
/* 315 */       .equals(otherInfo.customConditionHolder));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 320 */     return this.patternsCondition.hashCode() * 31 + this.methodsCondition
/* 321 */       .hashCode() + this.paramsCondition.hashCode() + this.headersCondition
/* 322 */       .hashCode() + this.consumesCondition.hashCode() + this.producesCondition
/* 323 */       .hashCode() + this.customConditionHolder.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 328 */     StringBuilder builder = new StringBuilder("{");
/* 329 */     if (!this.methodsCondition.isEmpty()) {
/* 330 */       Set<RequestMethod> httpMethods = this.methodsCondition.getMethods();
/* 331 */       builder.append((httpMethods.size() == 1) ? httpMethods.iterator().next() : httpMethods);
/*     */     } 
/* 333 */     if (!this.patternsCondition.isEmpty()) {
/* 334 */       Set<String> patterns = this.patternsCondition.getPatterns();
/* 335 */       builder.append(" ").append((patterns.size() == 1) ? patterns.iterator().next() : patterns);
/*     */     } 
/* 337 */     if (!this.paramsCondition.isEmpty()) {
/* 338 */       builder.append(", params ").append(this.paramsCondition);
/*     */     }
/* 340 */     if (!this.headersCondition.isEmpty()) {
/* 341 */       builder.append(", headers ").append(this.headersCondition);
/*     */     }
/* 343 */     if (!this.consumesCondition.isEmpty()) {
/* 344 */       builder.append(", consumes ").append(this.consumesCondition);
/*     */     }
/* 346 */     if (!this.producesCondition.isEmpty()) {
/* 347 */       builder.append(", produces ").append(this.producesCondition);
/*     */     }
/* 349 */     if (!this.customConditionHolder.isEmpty()) {
/* 350 */       builder.append(", and ").append(this.customConditionHolder);
/*     */     }
/* 352 */     builder.append('}');
/* 353 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder paths(String... paths) {
/* 363 */     return new DefaultBuilder(paths);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface Builder
/*     */   {
/*     */     Builder paths(String... param1VarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder methods(RequestMethod... param1VarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder params(String... param1VarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder headers(String... param1VarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder consumes(String... param1VarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder produces(String... param1VarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder mappingName(String param1String);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder customCondition(RequestCondition<?> param1RequestCondition);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder options(RequestMappingInfo.BuilderConfiguration param1BuilderConfiguration);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     RequestMappingInfo build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DefaultBuilder
/*     */     implements Builder
/*     */   {
/* 428 */     private String[] paths = new String[0];
/*     */     
/* 430 */     private RequestMethod[] methods = new RequestMethod[0];
/*     */     
/* 432 */     private String[] params = new String[0];
/*     */     
/* 434 */     private String[] headers = new String[0];
/*     */     
/* 436 */     private String[] consumes = new String[0];
/*     */     
/* 438 */     private String[] produces = new String[0];
/*     */     
/*     */     @Nullable
/*     */     private String mappingName;
/*     */     
/*     */     @Nullable
/*     */     private RequestCondition<?> customCondition;
/*     */     
/* 446 */     private RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
/*     */     
/*     */     public DefaultBuilder(String... paths) {
/* 449 */       this.paths = paths;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestMappingInfo.Builder paths(String... paths) {
/* 454 */       this.paths = paths;
/* 455 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultBuilder methods(RequestMethod... methods) {
/* 460 */       this.methods = methods;
/* 461 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultBuilder params(String... params) {
/* 466 */       this.params = params;
/* 467 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultBuilder headers(String... headers) {
/* 472 */       this.headers = headers;
/* 473 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultBuilder consumes(String... consumes) {
/* 478 */       this.consumes = consumes;
/* 479 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultBuilder produces(String... produces) {
/* 484 */       this.produces = produces;
/* 485 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultBuilder mappingName(String name) {
/* 490 */       this.mappingName = name;
/* 491 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultBuilder customCondition(RequestCondition<?> condition) {
/* 496 */       this.customCondition = condition;
/* 497 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestMappingInfo.Builder options(RequestMappingInfo.BuilderConfiguration options) {
/* 502 */       this.options = options;
/* 503 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestMappingInfo build() {
/* 508 */       ContentNegotiationManager manager = this.options.getContentNegotiationManager();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 513 */       PatternsRequestCondition patternsCondition = new PatternsRequestCondition(this.paths, this.options.getUrlPathHelper(), this.options.getPathMatcher(), this.options.useSuffixPatternMatch(), this.options.useTrailingSlashMatch(), this.options.getFileExtensions());
/*     */       
/* 515 */       return new RequestMappingInfo(this.mappingName, patternsCondition, new RequestMethodsRequestCondition(this.methods), new ParamsRequestCondition(this.params), new HeadersRequestCondition(this.headers), new ConsumesRequestCondition(this.consumes, this.headers), new ProducesRequestCondition(this.produces, this.headers, manager), this.customCondition);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BuilderConfiguration
/*     */   {
/*     */     @Nullable
/*     */     private UrlPathHelper urlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private PathMatcher pathMatcher;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean trailingSlashMatch = true;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean suffixPatternMatch = true;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean registeredSuffixPatternMatch = false;
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private ContentNegotiationManager contentNegotiationManager;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setUrlPathHelper(@Nullable UrlPathHelper urlPathHelper) {
/* 556 */       this.urlPathHelper = urlPathHelper;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public UrlPathHelper getUrlPathHelper() {
/* 564 */       return this.urlPathHelper;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPathMatcher(@Nullable PathMatcher pathMatcher) {
/* 572 */       this.pathMatcher = pathMatcher;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public PathMatcher getPathMatcher() {
/* 580 */       return this.pathMatcher;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTrailingSlashMatch(boolean trailingSlashMatch) {
/* 588 */       this.trailingSlashMatch = trailingSlashMatch;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean useTrailingSlashMatch() {
/* 595 */       return this.trailingSlashMatch;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setSuffixPatternMatch(boolean suffixPatternMatch) {
/* 604 */       this.suffixPatternMatch = suffixPatternMatch;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean useSuffixPatternMatch() {
/* 611 */       return this.suffixPatternMatch;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setRegisteredSuffixPatternMatch(boolean registeredSuffixPatternMatch) {
/* 622 */       this.registeredSuffixPatternMatch = registeredSuffixPatternMatch;
/* 623 */       this.suffixPatternMatch = (registeredSuffixPatternMatch || this.suffixPatternMatch);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean useRegisteredSuffixPatternMatch() {
/* 631 */       return this.registeredSuffixPatternMatch;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public List<String> getFileExtensions() {
/* 641 */       if (useRegisteredSuffixPatternMatch() && this.contentNegotiationManager != null) {
/* 642 */         return this.contentNegotiationManager.getAllFileExtensions();
/*     */       }
/* 644 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
/* 652 */       this.contentNegotiationManager = contentNegotiationManager;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ContentNegotiationManager getContentNegotiationManager() {
/* 661 */       return this.contentNegotiationManager;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/RequestMappingInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */