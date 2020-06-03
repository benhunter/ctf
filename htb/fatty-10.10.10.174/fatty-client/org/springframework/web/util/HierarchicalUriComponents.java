/*      */ package org.springframework.web.util;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.UnaryOperator;
/*      */ import org.springframework.lang.NonNull;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.LinkedMultiValueMap;
/*      */ import org.springframework.util.MultiValueMap;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
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
/*      */ final class HierarchicalUriComponents
/*      */   extends UriComponents
/*      */ {
/*      */   private static final char PATH_DELIMITER = '/';
/*      */   private static final String PATH_DELIMITER_STRING = "/";
/*   59 */   private static final MultiValueMap<String, String> EMPTY_QUERY_PARAMS = CollectionUtils.unmodifiableMultiValueMap((MultiValueMap)new LinkedMultiValueMap());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   65 */   static final PathComponent NULL_PATH_COMPONENT = new PathComponent()
/*      */     {
/*      */       public String getPath() {
/*   68 */         return "";
/*      */       }
/*      */       
/*      */       public List<String> getPathSegments() {
/*   72 */         return Collections.emptyList();
/*      */       }
/*      */       
/*      */       public HierarchicalUriComponents.PathComponent encode(BiFunction<String, HierarchicalUriComponents.Type, String> encoder) {
/*   76 */         return this;
/*      */       }
/*      */ 
/*      */       
/*      */       public void verify() {}
/*      */       
/*      */       public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables, @Nullable UnaryOperator<String> encoder) {
/*   83 */         return this;
/*      */       }
/*      */ 
/*      */       
/*      */       public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {}
/*      */       
/*      */       public boolean equals(Object other) {
/*   90 */         return (this == other);
/*      */       }
/*      */       
/*      */       public int hashCode() {
/*   94 */         return getClass().hashCode();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final String userInfo;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final String host;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final String port;
/*      */ 
/*      */ 
/*      */   
/*      */   private final PathComponent path;
/*      */ 
/*      */ 
/*      */   
/*      */   private final MultiValueMap<String, String> queryParams;
/*      */ 
/*      */ 
/*      */   
/*      */   private final EncodeState encodeState;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private UnaryOperator<String> variableEncoder;
/*      */ 
/*      */ 
/*      */   
/*      */   HierarchicalUriComponents(@Nullable String scheme, @Nullable String fragment, @Nullable String userInfo, @Nullable String host, @Nullable String port, @Nullable PathComponent path, @Nullable MultiValueMap<String, String> query, boolean encoded) {
/*  133 */     super(scheme, fragment);
/*      */     
/*  135 */     this.userInfo = userInfo;
/*  136 */     this.host = host;
/*  137 */     this.port = port;
/*  138 */     this.path = (path != null) ? path : NULL_PATH_COMPONENT;
/*  139 */     this.queryParams = (query != null) ? CollectionUtils.unmodifiableMultiValueMap(query) : EMPTY_QUERY_PARAMS;
/*  140 */     this.encodeState = encoded ? EncodeState.FULLY_ENCODED : EncodeState.RAW;
/*      */ 
/*      */     
/*  143 */     if (encoded) {
/*  144 */       verify();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private HierarchicalUriComponents(@Nullable String scheme, @Nullable String fragment, @Nullable String userInfo, @Nullable String host, @Nullable String port, PathComponent path, MultiValueMap<String, String> queryParams, EncodeState encodeState, @Nullable UnaryOperator<String> variableEncoder) {
/*  153 */     super(scheme, fragment);
/*      */     
/*  155 */     this.userInfo = userInfo;
/*  156 */     this.host = host;
/*  157 */     this.port = port;
/*  158 */     this.path = path;
/*  159 */     this.queryParams = queryParams;
/*  160 */     this.encodeState = encodeState;
/*  161 */     this.variableEncoder = variableEncoder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getSchemeSpecificPart() {
/*  170 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getUserInfo() {
/*  176 */     return this.userInfo;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getHost() {
/*  182 */     return this.host;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPort() {
/*  187 */     if (this.port == null) {
/*  188 */       return -1;
/*      */     }
/*  190 */     if (this.port.contains("{")) {
/*  191 */       throw new IllegalStateException("The port contains a URI variable but has not been expanded yet: " + this.port);
/*      */     }
/*      */     
/*  194 */     return Integer.parseInt(this.port);
/*      */   }
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public String getPath() {
/*  200 */     return this.path.getPath();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> getPathSegments() {
/*  205 */     return this.path.getPathSegments();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getQuery() {
/*  211 */     if (!this.queryParams.isEmpty()) {
/*  212 */       StringBuilder queryBuilder = new StringBuilder();
/*  213 */       this.queryParams.forEach((name, values) -> {
/*      */             if (CollectionUtils.isEmpty(values)) {
/*      */               if (queryBuilder.length() != 0) {
/*      */                 queryBuilder.append('&');
/*      */               }
/*      */               
/*      */               queryBuilder.append(name);
/*      */             } else {
/*      */               for (Object value : values) {
/*      */                 if (queryBuilder.length() != 0) {
/*      */                   queryBuilder.append('&');
/*      */                 }
/*      */                 queryBuilder.append(name);
/*      */                 if (value != null) {
/*      */                   queryBuilder.append('=').append(value.toString());
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           });
/*  232 */       return queryBuilder.toString();
/*      */     } 
/*      */     
/*  235 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MultiValueMap<String, String> getQueryParams() {
/*  244 */     return this.queryParams;
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
/*      */   HierarchicalUriComponents encodeTemplate(Charset charset) {
/*  256 */     if (this.encodeState.isEncoded()) {
/*  257 */       return this;
/*      */     }
/*      */ 
/*      */     
/*  261 */     this.variableEncoder = (value -> encodeUriComponent(value, charset, Type.URI));
/*      */     
/*  263 */     UriTemplateEncoder encoder = new UriTemplateEncoder(charset);
/*  264 */     String schemeTo = (getScheme() != null) ? encoder.apply(getScheme(), Type.SCHEME) : null;
/*  265 */     String fragmentTo = (getFragment() != null) ? encoder.apply(getFragment(), Type.FRAGMENT) : null;
/*  266 */     String userInfoTo = (getUserInfo() != null) ? encoder.apply(getUserInfo(), Type.USER_INFO) : null;
/*  267 */     String hostTo = (getHost() != null) ? encoder.apply(getHost(), getHostType()) : null;
/*  268 */     PathComponent pathTo = this.path.encode(encoder);
/*  269 */     MultiValueMap<String, String> queryParamsTo = encodeQueryParams(encoder);
/*      */     
/*  271 */     return new HierarchicalUriComponents(schemeTo, fragmentTo, userInfoTo, hostTo, this.port, pathTo, queryParamsTo, EncodeState.TEMPLATE_ENCODED, this.variableEncoder);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public HierarchicalUriComponents encode(Charset charset) {
/*  277 */     if (this.encodeState.isEncoded()) {
/*  278 */       return this;
/*      */     }
/*  280 */     String scheme = getScheme();
/*  281 */     String fragment = getFragment();
/*  282 */     String schemeTo = (scheme != null) ? encodeUriComponent(scheme, charset, Type.SCHEME) : null;
/*  283 */     String fragmentTo = (fragment != null) ? encodeUriComponent(fragment, charset, Type.FRAGMENT) : null;
/*  284 */     String userInfoTo = (this.userInfo != null) ? encodeUriComponent(this.userInfo, charset, Type.USER_INFO) : null;
/*  285 */     String hostTo = (this.host != null) ? encodeUriComponent(this.host, charset, getHostType()) : null;
/*  286 */     BiFunction<String, Type, String> encoder = (s, type) -> encodeUriComponent(s, charset, type);
/*  287 */     PathComponent pathTo = this.path.encode(encoder);
/*  288 */     MultiValueMap<String, String> queryParamsTo = encodeQueryParams(encoder);
/*      */     
/*  290 */     return new HierarchicalUriComponents(schemeTo, fragmentTo, userInfoTo, hostTo, this.port, pathTo, queryParamsTo, EncodeState.FULLY_ENCODED, null);
/*      */   }
/*      */ 
/*      */   
/*      */   private MultiValueMap<String, String> encodeQueryParams(BiFunction<String, Type, String> encoder) {
/*  295 */     int size = this.queryParams.size();
/*  296 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(size);
/*  297 */     this.queryParams.forEach((key, values) -> {
/*      */           String name = encoder.apply(key, Type.QUERY_PARAM);
/*      */           List<String> encodedValues = new ArrayList<>(values.size());
/*      */           for (String value : values) {
/*      */             encodedValues.add((value != null) ? encoder.apply(value, Type.QUERY_PARAM) : null);
/*      */           }
/*      */           result.put(name, encodedValues);
/*      */         });
/*  305 */     return CollectionUtils.unmodifiableMultiValueMap((MultiValueMap)linkedMultiValueMap);
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
/*      */   static String encodeUriComponent(String source, String encoding, Type type) {
/*  318 */     return encodeUriComponent(source, Charset.forName(encoding), type);
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
/*      */   static String encodeUriComponent(String source, Charset charset, Type type) {
/*  331 */     if (!StringUtils.hasLength(source)) {
/*  332 */       return source;
/*      */     }
/*  334 */     Assert.notNull(charset, "Charset must not be null");
/*  335 */     Assert.notNull(type, "Type must not be null");
/*      */     
/*  337 */     byte[] bytes = source.getBytes(charset);
/*  338 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
/*  339 */     boolean changed = false;
/*  340 */     for (byte b : bytes) {
/*  341 */       if (b < 0) {
/*  342 */         b = (byte)(b + 256);
/*      */       }
/*  344 */       if (type.isAllowed(b)) {
/*  345 */         bos.write(b);
/*      */       } else {
/*      */         
/*  348 */         bos.write(37);
/*  349 */         char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/*  350 */         char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/*  351 */         bos.write(hex1);
/*  352 */         bos.write(hex2);
/*  353 */         changed = true;
/*      */       } 
/*      */     } 
/*  356 */     return changed ? new String(bos.toByteArray(), charset) : source;
/*      */   }
/*      */   
/*      */   private Type getHostType() {
/*  360 */     return (this.host != null && this.host.startsWith("[")) ? Type.HOST_IPV6 : Type.HOST_IPV4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verify() {
/*  370 */     verifyUriComponent(getScheme(), Type.SCHEME);
/*  371 */     verifyUriComponent(this.userInfo, Type.USER_INFO);
/*  372 */     verifyUriComponent(this.host, getHostType());
/*  373 */     this.path.verify();
/*  374 */     this.queryParams.forEach((key, values) -> {
/*      */           verifyUriComponent(key, Type.QUERY_PARAM);
/*      */           for (String value : values) {
/*      */             verifyUriComponent(value, Type.QUERY_PARAM);
/*      */           }
/*      */         });
/*  380 */     verifyUriComponent(getFragment(), Type.FRAGMENT);
/*      */   }
/*      */   
/*      */   private static void verifyUriComponent(@Nullable String source, Type type) {
/*  384 */     if (source == null) {
/*      */       return;
/*      */     }
/*  387 */     int length = source.length();
/*  388 */     for (int i = 0; i < length; i++) {
/*  389 */       char ch = source.charAt(i);
/*  390 */       if (ch == '%') {
/*  391 */         if (i + 2 < length) {
/*  392 */           char hex1 = source.charAt(i + 1);
/*  393 */           char hex2 = source.charAt(i + 2);
/*  394 */           int u = Character.digit(hex1, 16);
/*  395 */           int l = Character.digit(hex2, 16);
/*  396 */           if (u == -1 || l == -1) {
/*  397 */             throw new IllegalArgumentException("Invalid encoded sequence \"" + source
/*  398 */                 .substring(i) + "\"");
/*      */           }
/*  400 */           i += 2;
/*      */         } else {
/*      */           
/*  403 */           throw new IllegalArgumentException("Invalid encoded sequence \"" + source
/*  404 */               .substring(i) + "\"");
/*      */         }
/*      */       
/*  407 */       } else if (!type.isAllowed(ch)) {
/*  408 */         throw new IllegalArgumentException("Invalid character '" + ch + "' for " + type
/*  409 */             .name() + " in \"" + source + "\"");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HierarchicalUriComponents expandInternal(UriComponents.UriTemplateVariables uriVariables) {
/*  420 */     Assert.state(!this.encodeState.equals(EncodeState.FULLY_ENCODED), "URI components already encoded, and could not possibly contain '{' or '}'.");
/*      */ 
/*      */     
/*  423 */     String schemeTo = expandUriComponent(getScheme(), uriVariables, this.variableEncoder);
/*  424 */     String fragmentTo = expandUriComponent(getFragment(), uriVariables, this.variableEncoder);
/*  425 */     String userInfoTo = expandUriComponent(this.userInfo, uriVariables, this.variableEncoder);
/*  426 */     String hostTo = expandUriComponent(this.host, uriVariables, this.variableEncoder);
/*  427 */     String portTo = expandUriComponent(this.port, uriVariables, this.variableEncoder);
/*  428 */     PathComponent pathTo = this.path.expand(uriVariables, this.variableEncoder);
/*  429 */     MultiValueMap<String, String> queryParamsTo = expandQueryParams(uriVariables);
/*      */     
/*  431 */     return new HierarchicalUriComponents(schemeTo, fragmentTo, userInfoTo, hostTo, portTo, pathTo, queryParamsTo, this.encodeState, this.variableEncoder);
/*      */   }
/*      */ 
/*      */   
/*      */   private MultiValueMap<String, String> expandQueryParams(UriComponents.UriTemplateVariables variables) {
/*  436 */     int size = this.queryParams.size();
/*  437 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(size);
/*  438 */     UriComponents.UriTemplateVariables queryVariables = new QueryUriTemplateVariables(variables);
/*  439 */     this.queryParams.forEach((key, values) -> {
/*      */           String name = expandUriComponent(key, queryVariables, this.variableEncoder);
/*      */           List<String> expandedValues = new ArrayList<>(values.size());
/*      */           for (String value : values) {
/*      */             expandedValues.add(expandUriComponent(value, queryVariables, this.variableEncoder));
/*      */           }
/*      */           result.put(name, expandedValues);
/*      */         });
/*  447 */     return CollectionUtils.unmodifiableMultiValueMap((MultiValueMap)linkedMultiValueMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public UriComponents normalize() {
/*  452 */     String normalizedPath = StringUtils.cleanPath(getPath());
/*  453 */     FullPathComponent path = new FullPathComponent(normalizedPath);
/*  454 */     return new HierarchicalUriComponents(getScheme(), getFragment(), this.userInfo, this.host, this.port, path, this.queryParams, this.encodeState, this.variableEncoder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toUriString() {
/*  463 */     StringBuilder uriBuilder = new StringBuilder();
/*  464 */     if (getScheme() != null) {
/*  465 */       uriBuilder.append(getScheme()).append(':');
/*      */     }
/*  467 */     if (this.userInfo != null || this.host != null) {
/*  468 */       uriBuilder.append("//");
/*  469 */       if (this.userInfo != null) {
/*  470 */         uriBuilder.append(this.userInfo).append('@');
/*      */       }
/*  472 */       if (this.host != null) {
/*  473 */         uriBuilder.append(this.host);
/*      */       }
/*  475 */       if (getPort() != -1) {
/*  476 */         uriBuilder.append(':').append(this.port);
/*      */       }
/*      */     } 
/*  479 */     String path = getPath();
/*  480 */     if (StringUtils.hasLength(path)) {
/*  481 */       if (uriBuilder.length() != 0 && path.charAt(0) != '/') {
/*  482 */         uriBuilder.append('/');
/*      */       }
/*  484 */       uriBuilder.append(path);
/*      */     } 
/*  486 */     String query = getQuery();
/*  487 */     if (query != null) {
/*  488 */       uriBuilder.append('?').append(query);
/*      */     }
/*  490 */     if (getFragment() != null) {
/*  491 */       uriBuilder.append('#').append(getFragment());
/*      */     }
/*  493 */     return uriBuilder.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public URI toUri() {
/*      */     try {
/*  499 */       if (this.encodeState.isEncoded()) {
/*  500 */         return new URI(toUriString());
/*      */       }
/*      */       
/*  503 */       String path = getPath();
/*  504 */       if (StringUtils.hasLength(path) && path.charAt(0) != '/')
/*      */       {
/*  506 */         if (getScheme() != null || getUserInfo() != null || getHost() != null || getPort() != -1) {
/*  507 */           path = '/' + path;
/*      */         }
/*      */       }
/*  510 */       return new URI(getScheme(), getUserInfo(), getHost(), getPort(), path, getQuery(), getFragment());
/*      */     
/*      */     }
/*  513 */     catch (URISyntaxException ex) {
/*  514 */       throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
/*  520 */     if (getScheme() != null) {
/*  521 */       builder.scheme(getScheme());
/*      */     }
/*  523 */     if (getUserInfo() != null) {
/*  524 */       builder.userInfo(getUserInfo());
/*      */     }
/*  526 */     if (getHost() != null) {
/*  527 */       builder.host(getHost());
/*      */     }
/*      */     
/*  530 */     if (this.port != null) {
/*  531 */       builder.port(this.port);
/*      */     }
/*  533 */     this.path.copyToUriComponentsBuilder(builder);
/*  534 */     if (!getQueryParams().isEmpty()) {
/*  535 */       builder.queryParams(getQueryParams());
/*      */     }
/*  537 */     if (getFragment() != null) {
/*  538 */       builder.fragment(getFragment());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object other) {
/*  545 */     if (this == other) {
/*  546 */       return true;
/*      */     }
/*  548 */     if (!(other instanceof HierarchicalUriComponents)) {
/*  549 */       return false;
/*      */     }
/*  551 */     HierarchicalUriComponents otherComp = (HierarchicalUriComponents)other;
/*  552 */     return (ObjectUtils.nullSafeEquals(getScheme(), otherComp.getScheme()) && 
/*  553 */       ObjectUtils.nullSafeEquals(getUserInfo(), otherComp.getUserInfo()) && 
/*  554 */       ObjectUtils.nullSafeEquals(getHost(), otherComp.getHost()) && 
/*  555 */       getPort() == otherComp.getPort() && this.path
/*  556 */       .equals(otherComp.path) && this.queryParams
/*  557 */       .equals(otherComp.queryParams) && 
/*  558 */       ObjectUtils.nullSafeEquals(getFragment(), otherComp.getFragment()));
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  563 */     int result = ObjectUtils.nullSafeHashCode(getScheme());
/*  564 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.userInfo);
/*  565 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.host);
/*  566 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.port);
/*  567 */     result = 31 * result + this.path.hashCode();
/*  568 */     result = 31 * result + this.queryParams.hashCode();
/*  569 */     result = 31 * result + ObjectUtils.nullSafeHashCode(getFragment());
/*  570 */     return result;
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
/*      */   enum Type
/*      */   {
/*  583 */     SCHEME
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  586 */         return (isAlpha(c) || isDigit(c) || 43 == c || 45 == c || 46 == c);
/*      */       }
/*      */     },
/*  589 */     AUTHORITY
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  592 */         return (isUnreserved(c) || isSubDelimiter(c) || 58 == c || 64 == c);
/*      */       }
/*      */     },
/*  595 */     USER_INFO
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  598 */         return (isUnreserved(c) || isSubDelimiter(c) || 58 == c);
/*      */       }
/*      */     },
/*  601 */     HOST_IPV4
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  604 */         return (isUnreserved(c) || isSubDelimiter(c));
/*      */       }
/*      */     },
/*  607 */     HOST_IPV6
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  610 */         return (isUnreserved(c) || isSubDelimiter(c) || 91 == c || 93 == c || 58 == c);
/*      */       }
/*      */     },
/*  613 */     PORT
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  616 */         return isDigit(c);
/*      */       }
/*      */     },
/*  619 */     PATH
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  622 */         return (isPchar(c) || 47 == c);
/*      */       }
/*      */     },
/*  625 */     PATH_SEGMENT
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  628 */         return isPchar(c);
/*      */       }
/*      */     },
/*  631 */     QUERY
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  634 */         return (isPchar(c) || 47 == c || 63 == c);
/*      */       }
/*      */     },
/*  637 */     QUERY_PARAM
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  640 */         if (61 == c || 38 == c) {
/*  641 */           return false;
/*      */         }
/*      */         
/*  644 */         return (isPchar(c) || 47 == c || 63 == c);
/*      */       }
/*      */     },
/*      */     
/*  648 */     FRAGMENT
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  651 */         return (isPchar(c) || 47 == c || 63 == c);
/*      */       }
/*      */     },
/*  654 */     URI
/*      */     {
/*      */       public boolean isAllowed(int c) {
/*  657 */         return isUnreserved(c);
/*      */       }
/*      */     };
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
/*      */     protected boolean isAlpha(int c) {
/*  672 */       return ((c >= 97 && c <= 122) || (c >= 65 && c <= 90));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isDigit(int c) {
/*  680 */       return (c >= 48 && c <= 57);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isGenericDelimiter(int c) {
/*  688 */       return (58 == c || 47 == c || 63 == c || 35 == c || 91 == c || 93 == c || 64 == c);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isSubDelimiter(int c) {
/*  696 */       return (33 == c || 36 == c || 38 == c || 39 == c || 40 == c || 41 == c || 42 == c || 43 == c || 44 == c || 59 == c || 61 == c);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isReserved(int c) {
/*  705 */       return (isGenericDelimiter(c) || isSubDelimiter(c));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isUnreserved(int c) {
/*  713 */       return (isAlpha(c) || isDigit(c) || 45 == c || 46 == c || 95 == c || 126 == c);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isPchar(int c) {
/*  721 */       return (isUnreserved(c) || isSubDelimiter(c) || 58 == c || 64 == c);
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract boolean isAllowed(int param1Int);
/*      */   }
/*      */ 
/*      */   
/*      */   private enum EncodeState
/*      */   {
/*  731 */     RAW,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  737 */     FULLY_ENCODED,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  744 */     TEMPLATE_ENCODED;
/*      */ 
/*      */     
/*      */     public boolean isEncoded() {
/*  748 */       return (equals(FULLY_ENCODED) || equals(TEMPLATE_ENCODED));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class UriTemplateEncoder
/*      */     implements BiFunction<String, Type, String>
/*      */   {
/*      */     private final Charset charset;
/*  757 */     private final StringBuilder currentLiteral = new StringBuilder();
/*      */     
/*  759 */     private final StringBuilder currentVariable = new StringBuilder();
/*      */     
/*  761 */     private final StringBuilder output = new StringBuilder();
/*      */ 
/*      */     
/*      */     public UriTemplateEncoder(Charset charset) {
/*  765 */       this.charset = charset;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String apply(String source, HierarchicalUriComponents.Type type) {
/*  773 */       if (source.length() > 1 && source.charAt(0) == '{' && source.charAt(source.length() - 1) == '}') {
/*  774 */         return source;
/*      */       }
/*      */ 
/*      */       
/*  778 */       if (source.indexOf('{') == -1) {
/*  779 */         return HierarchicalUriComponents.encodeUriComponent(source, this.charset, type);
/*      */       }
/*      */ 
/*      */       
/*  783 */       int level = 0;
/*  784 */       clear(this.currentLiteral);
/*  785 */       clear(this.currentVariable);
/*  786 */       clear(this.output);
/*  787 */       for (char c : source.toCharArray()) {
/*      */         
/*  789 */         level++;
/*  790 */         if (c == '{' && level == 1) {
/*  791 */           encodeAndAppendCurrentLiteral(type);
/*      */         }
/*      */         
/*  794 */         if (c == '}' && level > 0) {
/*  795 */           level--;
/*  796 */           this.currentVariable.append('}');
/*  797 */           if (level == 0) {
/*  798 */             this.output.append(this.currentVariable);
/*  799 */             clear(this.currentVariable);
/*      */           }
/*      */         
/*  802 */         } else if (level > 0) {
/*  803 */           this.currentVariable.append(c);
/*      */         } else {
/*      */           
/*  806 */           this.currentLiteral.append(c);
/*      */         } 
/*      */       } 
/*  809 */       if (level > 0) {
/*  810 */         this.currentLiteral.append(this.currentVariable);
/*      */       }
/*  812 */       encodeAndAppendCurrentLiteral(type);
/*  813 */       return this.output.toString();
/*      */     }
/*      */     
/*      */     private void encodeAndAppendCurrentLiteral(HierarchicalUriComponents.Type type) {
/*  817 */       this.output.append(HierarchicalUriComponents.encodeUriComponent(this.currentLiteral.toString(), this.charset, type));
/*  818 */       clear(this.currentLiteral);
/*      */     }
/*      */     
/*      */     private void clear(StringBuilder sb) {
/*  822 */       sb.delete(0, sb.length());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static interface PathComponent
/*      */     extends Serializable
/*      */   {
/*      */     String getPath();
/*      */ 
/*      */     
/*      */     List<String> getPathSegments();
/*      */ 
/*      */     
/*      */     PathComponent encode(BiFunction<String, HierarchicalUriComponents.Type, String> param1BiFunction);
/*      */ 
/*      */     
/*      */     void verify();
/*      */ 
/*      */     
/*      */     PathComponent expand(UriComponents.UriTemplateVariables param1UriTemplateVariables, @Nullable UnaryOperator<String> param1UnaryOperator);
/*      */ 
/*      */     
/*      */     void copyToUriComponentsBuilder(UriComponentsBuilder param1UriComponentsBuilder);
/*      */   }
/*      */   
/*      */   static final class FullPathComponent
/*      */     implements PathComponent
/*      */   {
/*      */     private final String path;
/*      */     
/*      */     public FullPathComponent(@Nullable String path) {
/*  854 */       this.path = (path != null) ? path : "";
/*      */     }
/*      */ 
/*      */     
/*      */     public String getPath() {
/*  859 */       return this.path;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<String> getPathSegments() {
/*  864 */       String[] segments = StringUtils.tokenizeToStringArray(getPath(), "/");
/*  865 */       return Collections.unmodifiableList(Arrays.asList(segments));
/*      */     }
/*      */ 
/*      */     
/*      */     public HierarchicalUriComponents.PathComponent encode(BiFunction<String, HierarchicalUriComponents.Type, String> encoder) {
/*  870 */       String encodedPath = encoder.apply(getPath(), HierarchicalUriComponents.Type.PATH);
/*  871 */       return new FullPathComponent(encodedPath);
/*      */     }
/*      */ 
/*      */     
/*      */     public void verify() {
/*  876 */       HierarchicalUriComponents.verifyUriComponent(getPath(), HierarchicalUriComponents.Type.PATH);
/*      */     }
/*      */ 
/*      */     
/*      */     public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables, @Nullable UnaryOperator<String> encoder) {
/*  881 */       String expandedPath = UriComponents.expandUriComponent(getPath(), uriVariables, encoder);
/*  882 */       return new FullPathComponent(expandedPath);
/*      */     }
/*      */ 
/*      */     
/*      */     public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
/*  887 */       builder.path(getPath());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  892 */       return (this == other || (other instanceof FullPathComponent && 
/*  893 */         getPath().equals(((FullPathComponent)other).getPath())));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  898 */       return getPath().hashCode();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class PathSegmentComponent
/*      */     implements PathComponent
/*      */   {
/*      */     private final List<String> pathSegments;
/*      */ 
/*      */     
/*      */     public PathSegmentComponent(List<String> pathSegments) {
/*  911 */       Assert.notNull(pathSegments, "List must not be null");
/*  912 */       this.pathSegments = Collections.unmodifiableList(new ArrayList<>(pathSegments));
/*      */     }
/*      */ 
/*      */     
/*      */     public String getPath() {
/*  917 */       StringBuilder pathBuilder = new StringBuilder();
/*  918 */       pathBuilder.append('/');
/*  919 */       for (Iterator<String> iterator = this.pathSegments.iterator(); iterator.hasNext(); ) {
/*  920 */         String pathSegment = iterator.next();
/*  921 */         pathBuilder.append(pathSegment);
/*  922 */         if (iterator.hasNext()) {
/*  923 */           pathBuilder.append('/');
/*      */         }
/*      */       } 
/*  926 */       return pathBuilder.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<String> getPathSegments() {
/*  931 */       return this.pathSegments;
/*      */     }
/*      */ 
/*      */     
/*      */     public HierarchicalUriComponents.PathComponent encode(BiFunction<String, HierarchicalUriComponents.Type, String> encoder) {
/*  936 */       List<String> pathSegments = getPathSegments();
/*  937 */       List<String> encodedPathSegments = new ArrayList<>(pathSegments.size());
/*  938 */       for (String pathSegment : pathSegments) {
/*  939 */         String encodedPathSegment = encoder.apply(pathSegment, HierarchicalUriComponents.Type.PATH_SEGMENT);
/*  940 */         encodedPathSegments.add(encodedPathSegment);
/*      */       } 
/*  942 */       return new PathSegmentComponent(encodedPathSegments);
/*      */     }
/*      */ 
/*      */     
/*      */     public void verify() {
/*  947 */       for (String pathSegment : getPathSegments()) {
/*  948 */         HierarchicalUriComponents.verifyUriComponent(pathSegment, HierarchicalUriComponents.Type.PATH_SEGMENT);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables, @Nullable UnaryOperator<String> encoder) {
/*  954 */       List<String> pathSegments = getPathSegments();
/*  955 */       List<String> expandedPathSegments = new ArrayList<>(pathSegments.size());
/*  956 */       for (String pathSegment : pathSegments) {
/*  957 */         String expandedPathSegment = UriComponents.expandUriComponent(pathSegment, uriVariables, encoder);
/*  958 */         expandedPathSegments.add(expandedPathSegment);
/*      */       } 
/*  960 */       return new PathSegmentComponent(expandedPathSegments);
/*      */     }
/*      */ 
/*      */     
/*      */     public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
/*  965 */       builder.pathSegment(StringUtils.toStringArray(getPathSegments()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  970 */       return (this == other || (other instanceof PathSegmentComponent && 
/*  971 */         getPathSegments().equals(((PathSegmentComponent)other).getPathSegments())));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  976 */       return getPathSegments().hashCode();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class PathComponentComposite
/*      */     implements PathComponent
/*      */   {
/*      */     private final List<HierarchicalUriComponents.PathComponent> pathComponents;
/*      */ 
/*      */     
/*      */     public PathComponentComposite(List<HierarchicalUriComponents.PathComponent> pathComponents) {
/*  989 */       Assert.notNull(pathComponents, "PathComponent List must not be null");
/*  990 */       this.pathComponents = pathComponents;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getPath() {
/*  995 */       StringBuilder pathBuilder = new StringBuilder();
/*  996 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/*  997 */         pathBuilder.append(pathComponent.getPath());
/*      */       }
/*  999 */       return pathBuilder.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<String> getPathSegments() {
/* 1004 */       List<String> result = new ArrayList<>();
/* 1005 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 1006 */         result.addAll(pathComponent.getPathSegments());
/*      */       }
/* 1008 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public HierarchicalUriComponents.PathComponent encode(BiFunction<String, HierarchicalUriComponents.Type, String> encoder) {
/* 1013 */       List<HierarchicalUriComponents.PathComponent> encodedComponents = new ArrayList<>(this.pathComponents.size());
/* 1014 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 1015 */         encodedComponents.add(pathComponent.encode(encoder));
/*      */       }
/* 1017 */       return new PathComponentComposite(encodedComponents);
/*      */     }
/*      */ 
/*      */     
/*      */     public void verify() {
/* 1022 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 1023 */         pathComponent.verify();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables, @Nullable UnaryOperator<String> encoder) {
/* 1029 */       List<HierarchicalUriComponents.PathComponent> expandedComponents = new ArrayList<>(this.pathComponents.size());
/* 1030 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 1031 */         expandedComponents.add(pathComponent.expand(uriVariables, encoder));
/*      */       }
/* 1033 */       return new PathComponentComposite(expandedComponents);
/*      */     }
/*      */ 
/*      */     
/*      */     public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
/* 1038 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 1039 */         pathComponent.copyToUriComponentsBuilder(builder);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class QueryUriTemplateVariables
/*      */     implements UriComponents.UriTemplateVariables
/*      */   {
/*      */     private final UriComponents.UriTemplateVariables delegate;
/*      */     
/*      */     public QueryUriTemplateVariables(UriComponents.UriTemplateVariables delegate) {
/* 1050 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getValue(@Nullable String name) {
/* 1055 */       Object value = this.delegate.getValue(name);
/* 1056 */       if (ObjectUtils.isArray(value)) {
/* 1057 */         value = StringUtils.arrayToCommaDelimitedString(ObjectUtils.toObjectArray(value));
/*      */       }
/* 1059 */       return value;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/HierarchicalUriComponents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */