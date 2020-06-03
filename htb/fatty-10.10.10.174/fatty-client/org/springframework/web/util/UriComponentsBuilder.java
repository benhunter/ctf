/*      */ package org.springframework.web.util;
/*      */ 
/*      */ import java.net.URI;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.springframework.http.HttpHeaders;
/*      */ import org.springframework.http.HttpRequest;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
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
/*      */ public class UriComponentsBuilder
/*      */   implements UriBuilder, Cloneable
/*      */ {
/*   68 */   private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");
/*      */ 
/*      */   
/*      */   private static final String SCHEME_PATTERN = "([^:/?#]+):";
/*      */   
/*      */   private static final String HTTP_PATTERN = "(?i)(http|https):";
/*      */   
/*      */   private static final String USERINFO_PATTERN = "([^@\\[/?#]*)";
/*      */   
/*      */   private static final String HOST_IPV4_PATTERN = "[^\\[/?#:]*";
/*      */   
/*      */   private static final String HOST_IPV6_PATTERN = "\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]";
/*      */   
/*      */   private static final String HOST_PATTERN = "(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)";
/*      */   
/*      */   private static final String PORT_PATTERN = "(\\d*(?:\\{[^/]+?\\})?)";
/*      */   
/*      */   private static final String PATH_PATTERN = "([^?#]*)";
/*      */   
/*      */   private static final String QUERY_PATTERN = "([^#]*)";
/*      */   
/*      */   private static final String LAST_PATTERN = "(.*)";
/*      */   
/*   91 */   private static final Pattern URI_PATTERN = Pattern.compile("^(([^:/?#]+):)?(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)(:(\\d*(?:\\{[^/]+?\\})?))?)?([^?#]*)(\\?([^#]*))?(#(.*))?");
/*      */ 
/*      */ 
/*      */   
/*   95 */   private static final Pattern HTTP_URL_PATTERN = Pattern.compile("^(?i)(http|https):(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)(:(\\d*(?:\\{[^/]+?\\})?))?)?([^?#]*)(\\?(.*))?");
/*      */ 
/*      */ 
/*      */   
/*   99 */   private static final Pattern FORWARDED_HOST_PATTERN = Pattern.compile("host=\"?([^;,\"]+)\"?");
/*      */   
/*  101 */   private static final Pattern FORWARDED_PROTO_PATTERN = Pattern.compile("proto=\"?([^;,\"]+)\"?");
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private String scheme;
/*      */   
/*      */   @Nullable
/*      */   private String ssp;
/*      */   
/*      */   @Nullable
/*      */   private String userInfo;
/*      */   
/*      */   @Nullable
/*      */   private String host;
/*      */   
/*      */   @Nullable
/*      */   private String port;
/*      */   
/*      */   private CompositePathComponentBuilder pathBuilder;
/*      */   
/*  121 */   private final MultiValueMap<String, String> queryParams = (MultiValueMap<String, String>)new LinkedMultiValueMap();
/*      */   
/*      */   @Nullable
/*      */   private String fragment;
/*      */   
/*  126 */   private final Map<String, Object> uriVariables = new HashMap<>(4);
/*      */   
/*      */   private boolean encodeTemplate;
/*      */   
/*  130 */   private Charset charset = StandardCharsets.UTF_8;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected UriComponentsBuilder() {
/*  140 */     this.pathBuilder = new CompositePathComponentBuilder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected UriComponentsBuilder(UriComponentsBuilder other) {
/*  149 */     this.scheme = other.scheme;
/*  150 */     this.ssp = other.ssp;
/*  151 */     this.userInfo = other.userInfo;
/*  152 */     this.host = other.host;
/*  153 */     this.port = other.port;
/*  154 */     this.pathBuilder = other.pathBuilder.cloneBuilder();
/*  155 */     this.queryParams.putAll((Map)other.queryParams);
/*  156 */     this.fragment = other.fragment;
/*  157 */     this.encodeTemplate = other.encodeTemplate;
/*  158 */     this.charset = other.charset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UriComponentsBuilder newInstance() {
/*  169 */     return new UriComponentsBuilder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UriComponentsBuilder fromPath(String path) {
/*  178 */     UriComponentsBuilder builder = new UriComponentsBuilder();
/*  179 */     builder.path(path);
/*  180 */     return builder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UriComponentsBuilder fromUri(URI uri) {
/*  189 */     UriComponentsBuilder builder = new UriComponentsBuilder();
/*  190 */     builder.uri(uri);
/*  191 */     return builder;
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
/*      */   public static UriComponentsBuilder fromUriString(String uri) {
/*  209 */     Assert.notNull(uri, "URI must not be null");
/*  210 */     Matcher matcher = URI_PATTERN.matcher(uri);
/*  211 */     if (matcher.matches()) {
/*  212 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/*  213 */       String scheme = matcher.group(2);
/*  214 */       String userInfo = matcher.group(5);
/*  215 */       String host = matcher.group(6);
/*  216 */       String port = matcher.group(8);
/*  217 */       String path = matcher.group(9);
/*  218 */       String query = matcher.group(11);
/*  219 */       String fragment = matcher.group(13);
/*  220 */       boolean opaque = false;
/*  221 */       if (StringUtils.hasLength(scheme)) {
/*  222 */         String rest = uri.substring(scheme.length());
/*  223 */         if (!rest.startsWith(":/")) {
/*  224 */           opaque = true;
/*      */         }
/*      */       } 
/*  227 */       builder.scheme(scheme);
/*  228 */       if (opaque) {
/*  229 */         String ssp = uri.substring(scheme.length()).substring(1);
/*  230 */         if (StringUtils.hasLength(fragment)) {
/*  231 */           ssp = ssp.substring(0, ssp.length() - fragment.length() + 1);
/*      */         }
/*  233 */         builder.schemeSpecificPart(ssp);
/*      */       } else {
/*      */         
/*  236 */         builder.userInfo(userInfo);
/*  237 */         builder.host(host);
/*  238 */         if (StringUtils.hasLength(port)) {
/*  239 */           builder.port(port);
/*      */         }
/*  241 */         builder.path(path);
/*  242 */         builder.query(query);
/*      */       } 
/*  244 */       if (StringUtils.hasText(fragment)) {
/*  245 */         builder.fragment(fragment);
/*      */       }
/*  247 */       return builder;
/*      */     } 
/*      */     
/*  250 */     throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
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
/*      */   public static UriComponentsBuilder fromHttpUrl(String httpUrl) {
/*  269 */     Assert.notNull(httpUrl, "HTTP URL must not be null");
/*  270 */     Matcher matcher = HTTP_URL_PATTERN.matcher(httpUrl);
/*  271 */     if (matcher.matches()) {
/*  272 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/*  273 */       String scheme = matcher.group(1);
/*  274 */       builder.scheme((scheme != null) ? scheme.toLowerCase() : null);
/*  275 */       builder.userInfo(matcher.group(4));
/*  276 */       String host = matcher.group(5);
/*  277 */       if (StringUtils.hasLength(scheme) && !StringUtils.hasLength(host)) {
/*  278 */         throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
/*      */       }
/*  280 */       builder.host(host);
/*  281 */       String port = matcher.group(7);
/*  282 */       if (StringUtils.hasLength(port)) {
/*  283 */         builder.port(port);
/*      */       }
/*  285 */       builder.path(matcher.group(8));
/*  286 */       builder.query(matcher.group(10));
/*  287 */       return builder;
/*      */     } 
/*      */     
/*  290 */     throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
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
/*      */   public static UriComponentsBuilder fromHttpRequest(HttpRequest request) {
/*  305 */     return fromUri(request.getURI()).adaptFromForwardedHeaders(request.getHeaders());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UriComponentsBuilder fromOriginHeader(String origin) {
/*  313 */     Matcher matcher = URI_PATTERN.matcher(origin);
/*  314 */     if (matcher.matches()) {
/*  315 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/*  316 */       String scheme = matcher.group(2);
/*  317 */       String host = matcher.group(6);
/*  318 */       String port = matcher.group(8);
/*  319 */       if (StringUtils.hasLength(scheme)) {
/*  320 */         builder.scheme(scheme);
/*      */       }
/*  322 */       builder.host(host);
/*  323 */       if (StringUtils.hasLength(port)) {
/*  324 */         builder.port(port);
/*      */       }
/*  326 */       return builder;
/*      */     } 
/*      */     
/*  329 */     throw new IllegalArgumentException("[" + origin + "] is not a valid \"Origin\" header value");
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final UriComponentsBuilder encode() {
/*  355 */     return encode(StandardCharsets.UTF_8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder encode(Charset charset) {
/*  364 */     this.encodeTemplate = true;
/*  365 */     this.charset = charset;
/*  366 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponents build() {
/*  377 */     return build(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponents build(boolean encoded) {
/*  388 */     return buildInternal(encoded ? EncodingHint.FULLY_ENCODED : (this.encodeTemplate ? EncodingHint.ENCODE_TEMPLATE : EncodingHint.NONE));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private UriComponents buildInternal(EncodingHint hint) {
/*      */     UriComponents result;
/*  395 */     if (this.ssp != null) {
/*  396 */       result = new OpaqueUriComponents(this.scheme, this.ssp, this.fragment);
/*      */     }
/*      */     else {
/*      */       
/*  400 */       HierarchicalUriComponents uric = new HierarchicalUriComponents(this.scheme, this.fragment, this.userInfo, this.host, this.port, this.pathBuilder.build(), this.queryParams, (hint == EncodingHint.FULLY_ENCODED));
/*      */ 
/*      */       
/*  403 */       result = (hint == EncodingHint.ENCODE_TEMPLATE) ? uric.encodeTemplate(this.charset) : uric;
/*      */     } 
/*  405 */     if (!this.uriVariables.isEmpty()) {
/*  406 */       result = result.expand(name -> this.uriVariables.getOrDefault(name, UriComponents.UriTemplateVariables.SKIP_VALUE));
/*      */     }
/*  408 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponents buildAndExpand(Map<String, ?> uriVariables) {
/*  419 */     return build().expand(uriVariables);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponents buildAndExpand(Object... uriVariableValues) {
/*  430 */     return build().expand(uriVariableValues);
/*      */   }
/*      */ 
/*      */   
/*      */   public URI build(Object... uriVariables) {
/*  435 */     return buildInternal(EncodingHint.ENCODE_TEMPLATE).expand(uriVariables).toUri();
/*      */   }
/*      */ 
/*      */   
/*      */   public URI build(Map<String, ?> uriVariables) {
/*  440 */     return buildInternal(EncodingHint.ENCODE_TEMPLATE).expand(uriVariables).toUri();
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
/*      */   public String toUriString() {
/*  460 */     return this.uriVariables.isEmpty() ? 
/*  461 */       build().encode().toUriString() : 
/*  462 */       buildInternal(EncodingHint.ENCODE_TEMPLATE).toUriString();
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
/*      */   public UriComponentsBuilder uri(URI uri) {
/*  474 */     Assert.notNull(uri, "URI must not be null");
/*  475 */     this.scheme = uri.getScheme();
/*  476 */     if (uri.isOpaque()) {
/*  477 */       this.ssp = uri.getRawSchemeSpecificPart();
/*  478 */       resetHierarchicalComponents();
/*      */     } else {
/*      */       
/*  481 */       if (uri.getRawUserInfo() != null) {
/*  482 */         this.userInfo = uri.getRawUserInfo();
/*      */       }
/*  484 */       if (uri.getHost() != null) {
/*  485 */         this.host = uri.getHost();
/*      */       }
/*  487 */       if (uri.getPort() != -1) {
/*  488 */         this.port = String.valueOf(uri.getPort());
/*      */       }
/*  490 */       if (StringUtils.hasLength(uri.getRawPath())) {
/*  491 */         this.pathBuilder = new CompositePathComponentBuilder();
/*  492 */         this.pathBuilder.addPath(uri.getRawPath());
/*      */       } 
/*  494 */       if (StringUtils.hasLength(uri.getRawQuery())) {
/*  495 */         this.queryParams.clear();
/*  496 */         query(uri.getRawQuery());
/*      */       } 
/*  498 */       resetSchemeSpecificPart();
/*      */     } 
/*  500 */     if (uri.getRawFragment() != null) {
/*  501 */       this.fragment = uri.getRawFragment();
/*      */     }
/*  503 */     return this;
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
/*      */   public UriComponentsBuilder uriComponents(UriComponents uriComponents) {
/*  516 */     Assert.notNull(uriComponents, "UriComponents must not be null");
/*  517 */     uriComponents.copyToUriComponentsBuilder(this);
/*  518 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder scheme(@Nullable String scheme) {
/*  529 */     this.scheme = scheme;
/*  530 */     return this;
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
/*      */   public UriComponentsBuilder schemeSpecificPart(String ssp) {
/*  542 */     this.ssp = ssp;
/*  543 */     resetHierarchicalComponents();
/*  544 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder userInfo(@Nullable String userInfo) {
/*  555 */     this.userInfo = userInfo;
/*  556 */     resetSchemeSpecificPart();
/*  557 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder host(@Nullable String host) {
/*  568 */     this.host = host;
/*  569 */     resetSchemeSpecificPart();
/*  570 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder port(int port) {
/*  580 */     Assert.isTrue((port >= -1), "Port must be >= -1");
/*  581 */     this.port = String.valueOf(port);
/*  582 */     resetSchemeSpecificPart();
/*  583 */     return this;
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
/*      */   public UriComponentsBuilder port(@Nullable String port) {
/*  595 */     this.port = port;
/*  596 */     resetSchemeSpecificPart();
/*  597 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder path(String path) {
/*  608 */     this.pathBuilder.addPath(path);
/*  609 */     resetSchemeSpecificPart();
/*  610 */     return this;
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
/*      */   public UriComponentsBuilder pathSegment(String... pathSegments) throws IllegalArgumentException {
/*  622 */     this.pathBuilder.addPathSegments(pathSegments);
/*  623 */     resetSchemeSpecificPart();
/*  624 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder replacePath(@Nullable String path) {
/*  634 */     this.pathBuilder = new CompositePathComponentBuilder();
/*  635 */     if (path != null) {
/*  636 */       this.pathBuilder.addPath(path);
/*      */     }
/*  638 */     resetSchemeSpecificPart();
/*  639 */     return this;
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
/*      */   public UriComponentsBuilder query(@Nullable String query) {
/*  660 */     if (query != null) {
/*  661 */       Matcher matcher = QUERY_PARAM_PATTERN.matcher(query);
/*  662 */       while (matcher.find()) {
/*  663 */         String name = matcher.group(1);
/*  664 */         String eq = matcher.group(2);
/*  665 */         String value = matcher.group(3);
/*  666 */         queryParam(name, new Object[] { (value != null) ? value : (StringUtils.hasLength(eq) ? "" : null) });
/*      */       } 
/*      */     } else {
/*      */       
/*  670 */       this.queryParams.clear();
/*      */     } 
/*  672 */     resetSchemeSpecificPart();
/*  673 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder replaceQuery(@Nullable String query) {
/*  683 */     this.queryParams.clear();
/*  684 */     if (query != null) {
/*  685 */       query(query);
/*      */     }
/*  687 */     resetSchemeSpecificPart();
/*  688 */     return this;
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
/*      */   public UriComponentsBuilder queryParam(String name, Object... values) {
/*  702 */     Assert.notNull(name, "Name must not be null");
/*  703 */     if (!ObjectUtils.isEmpty(values)) {
/*  704 */       for (Object value : values) {
/*  705 */         String valueAsString = (value != null) ? value.toString() : null;
/*  706 */         this.queryParams.add(name, valueAsString);
/*      */       } 
/*      */     } else {
/*      */       
/*  710 */       this.queryParams.add(name, null);
/*      */     } 
/*  712 */     resetSchemeSpecificPart();
/*  713 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder queryParams(@Nullable MultiValueMap<String, String> params) {
/*  724 */     if (params != null) {
/*  725 */       this.queryParams.addAll(params);
/*      */     }
/*  727 */     return this;
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
/*      */   public UriComponentsBuilder replaceQueryParam(String name, Object... values) {
/*  739 */     Assert.notNull(name, "Name must not be null");
/*  740 */     this.queryParams.remove(name);
/*  741 */     if (!ObjectUtils.isEmpty(values)) {
/*  742 */       queryParam(name, values);
/*      */     }
/*  744 */     resetSchemeSpecificPart();
/*  745 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder replaceQueryParams(@Nullable MultiValueMap<String, String> params) {
/*  756 */     this.queryParams.clear();
/*  757 */     if (params != null) {
/*  758 */       this.queryParams.putAll((Map)params);
/*      */     }
/*  760 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder fragment(@Nullable String fragment) {
/*  771 */     if (fragment != null) {
/*  772 */       Assert.hasLength(fragment, "Fragment must not be empty");
/*  773 */       this.fragment = fragment;
/*      */     } else {
/*      */       
/*  776 */       this.fragment = null;
/*      */     } 
/*  778 */     return this;
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
/*      */   public UriComponentsBuilder uriVariables(Map<String, Object> uriVariables) {
/*  796 */     this.uriVariables.putAll(uriVariables);
/*  797 */     return this;
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
/*      */   UriComponentsBuilder adaptFromForwardedHeaders(HttpHeaders headers) {
/*      */     try {
/*  816 */       String forwardedHeader = headers.getFirst("Forwarded");
/*  817 */       if (StringUtils.hasText(forwardedHeader)) {
/*  818 */         String forwardedToUse = StringUtils.tokenizeToStringArray(forwardedHeader, ",")[0];
/*  819 */         Matcher matcher = FORWARDED_PROTO_PATTERN.matcher(forwardedToUse);
/*  820 */         if (matcher.find()) {
/*  821 */           scheme(matcher.group(1).trim());
/*  822 */           port((String)null);
/*      */         }
/*  824 */         else if (isForwardedSslOn(headers)) {
/*  825 */           scheme("https");
/*  826 */           port((String)null);
/*      */         } 
/*  828 */         matcher = FORWARDED_HOST_PATTERN.matcher(forwardedToUse);
/*  829 */         if (matcher.find()) {
/*  830 */           adaptForwardedHost(matcher.group(1).trim());
/*      */         }
/*      */       } else {
/*      */         
/*  834 */         String protocolHeader = headers.getFirst("X-Forwarded-Proto");
/*  835 */         if (StringUtils.hasText(protocolHeader)) {
/*  836 */           scheme(StringUtils.tokenizeToStringArray(protocolHeader, ",")[0]);
/*  837 */           port((String)null);
/*      */         }
/*  839 */         else if (isForwardedSslOn(headers)) {
/*  840 */           scheme("https");
/*  841 */           port((String)null);
/*      */         } 
/*      */         
/*  844 */         String hostHeader = headers.getFirst("X-Forwarded-Host");
/*  845 */         if (StringUtils.hasText(hostHeader)) {
/*  846 */           adaptForwardedHost(StringUtils.tokenizeToStringArray(hostHeader, ",")[0]);
/*      */         }
/*      */         
/*  849 */         String portHeader = headers.getFirst("X-Forwarded-Port");
/*  850 */         if (StringUtils.hasText(portHeader)) {
/*  851 */           port(Integer.parseInt(StringUtils.tokenizeToStringArray(portHeader, ",")[0]));
/*      */         }
/*      */       }
/*      */     
/*  855 */     } catch (NumberFormatException ex) {
/*  856 */       throw new IllegalArgumentException("Failed to parse a port from \"forwarded\"-type headers. If not behind a trusted proxy, consider using ForwardedHeaderFilter with the removeOnly=true. Request headers: " + headers);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  861 */     if (this.scheme != null && ((this.scheme.equals("http") && "80".equals(this.port)) || (this.scheme
/*  862 */       .equals("https") && "443".equals(this.port)))) {
/*  863 */       port((String)null);
/*      */     }
/*      */     
/*  866 */     return this;
/*      */   }
/*      */   
/*      */   private boolean isForwardedSslOn(HttpHeaders headers) {
/*  870 */     String forwardedSsl = headers.getFirst("X-Forwarded-Ssl");
/*  871 */     return (StringUtils.hasText(forwardedSsl) && forwardedSsl.equalsIgnoreCase("on"));
/*      */   }
/*      */   
/*      */   private void adaptForwardedHost(String hostToUse) {
/*  875 */     int portSeparatorIdx = hostToUse.lastIndexOf(':');
/*  876 */     if (portSeparatorIdx > hostToUse.lastIndexOf(']')) {
/*  877 */       host(hostToUse.substring(0, portSeparatorIdx));
/*  878 */       port(Integer.parseInt(hostToUse.substring(portSeparatorIdx + 1)));
/*      */     } else {
/*      */       
/*  881 */       host(hostToUse);
/*  882 */       port((String)null);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void resetHierarchicalComponents() {
/*  887 */     this.userInfo = null;
/*  888 */     this.host = null;
/*  889 */     this.port = null;
/*  890 */     this.pathBuilder = new CompositePathComponentBuilder();
/*  891 */     this.queryParams.clear();
/*      */   }
/*      */   
/*      */   private void resetSchemeSpecificPart() {
/*  895 */     this.ssp = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object clone() {
/*  905 */     return cloneBuilder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UriComponentsBuilder cloneBuilder() {
/*  914 */     return new UriComponentsBuilder(this);
/*      */   }
/*      */ 
/*      */   
/*      */   private static interface PathComponentBuilder
/*      */   {
/*      */     @Nullable
/*      */     HierarchicalUriComponents.PathComponent build();
/*      */     
/*      */     PathComponentBuilder cloneBuilder();
/*      */   }
/*      */   
/*      */   private static class CompositePathComponentBuilder
/*      */     implements PathComponentBuilder
/*      */   {
/*  929 */     private final LinkedList<UriComponentsBuilder.PathComponentBuilder> builders = new LinkedList<>();
/*      */     
/*      */     public void addPathSegments(String... pathSegments) {
/*  932 */       if (!ObjectUtils.isEmpty((Object[])pathSegments)) {
/*  933 */         UriComponentsBuilder.PathSegmentComponentBuilder psBuilder = getLastBuilder(UriComponentsBuilder.PathSegmentComponentBuilder.class);
/*  934 */         UriComponentsBuilder.FullPathComponentBuilder fpBuilder = getLastBuilder(UriComponentsBuilder.FullPathComponentBuilder.class);
/*  935 */         if (psBuilder == null) {
/*  936 */           psBuilder = new UriComponentsBuilder.PathSegmentComponentBuilder();
/*  937 */           this.builders.add(psBuilder);
/*  938 */           if (fpBuilder != null) {
/*  939 */             fpBuilder.removeTrailingSlash();
/*      */           }
/*      */         } 
/*  942 */         psBuilder.append(pathSegments);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void addPath(String path) {
/*  947 */       if (StringUtils.hasText(path)) {
/*  948 */         UriComponentsBuilder.PathSegmentComponentBuilder psBuilder = getLastBuilder(UriComponentsBuilder.PathSegmentComponentBuilder.class);
/*  949 */         UriComponentsBuilder.FullPathComponentBuilder fpBuilder = getLastBuilder(UriComponentsBuilder.FullPathComponentBuilder.class);
/*  950 */         if (psBuilder != null) {
/*  951 */           path = path.startsWith("/") ? path : ("/" + path);
/*      */         }
/*  953 */         if (fpBuilder == null) {
/*  954 */           fpBuilder = new UriComponentsBuilder.FullPathComponentBuilder();
/*  955 */           this.builders.add(fpBuilder);
/*      */         } 
/*  957 */         fpBuilder.append(path);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private <T> T getLastBuilder(Class<T> builderClass) {
/*  964 */       if (!this.builders.isEmpty()) {
/*  965 */         UriComponentsBuilder.PathComponentBuilder last = this.builders.getLast();
/*  966 */         if (builderClass.isInstance(last)) {
/*  967 */           return (T)last;
/*      */         }
/*      */       } 
/*  970 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public HierarchicalUriComponents.PathComponent build() {
/*  975 */       int size = this.builders.size();
/*  976 */       List<HierarchicalUriComponents.PathComponent> components = new ArrayList<>(size);
/*  977 */       for (UriComponentsBuilder.PathComponentBuilder componentBuilder : this.builders) {
/*  978 */         HierarchicalUriComponents.PathComponent pathComponent = componentBuilder.build();
/*  979 */         if (pathComponent != null) {
/*  980 */           components.add(pathComponent);
/*      */         }
/*      */       } 
/*  983 */       if (components.isEmpty()) {
/*  984 */         return HierarchicalUriComponents.NULL_PATH_COMPONENT;
/*      */       }
/*  986 */       if (components.size() == 1) {
/*  987 */         return components.get(0);
/*      */       }
/*  989 */       return new HierarchicalUriComponents.PathComponentComposite(components);
/*      */     }
/*      */ 
/*      */     
/*      */     public CompositePathComponentBuilder cloneBuilder() {
/*  994 */       CompositePathComponentBuilder compositeBuilder = new CompositePathComponentBuilder();
/*  995 */       for (UriComponentsBuilder.PathComponentBuilder builder : this.builders) {
/*  996 */         compositeBuilder.builders.add(builder.cloneBuilder());
/*      */       }
/*  998 */       return compositeBuilder;
/*      */     }
/*      */     
/*      */     private CompositePathComponentBuilder() {}
/*      */   }
/*      */   
/*      */   private static class FullPathComponentBuilder implements PathComponentBuilder {
/* 1005 */     private final StringBuilder path = new StringBuilder();
/*      */     
/*      */     public void append(String path) {
/* 1008 */       this.path.append(path);
/*      */     }
/*      */ 
/*      */     
/*      */     public HierarchicalUriComponents.PathComponent build() {
/* 1013 */       if (this.path.length() == 0) {
/* 1014 */         return null;
/*      */       }
/* 1016 */       String path = this.path.toString();
/*      */       while (true) {
/* 1018 */         int index = path.indexOf("//");
/* 1019 */         if (index == -1) {
/*      */           break;
/*      */         }
/* 1022 */         path = path.substring(0, index) + path.substring(index + 1);
/*      */       } 
/* 1024 */       return new HierarchicalUriComponents.FullPathComponent(path);
/*      */     }
/*      */     
/*      */     public void removeTrailingSlash() {
/* 1028 */       int index = this.path.length() - 1;
/* 1029 */       if (this.path.charAt(index) == '/')
/* 1030 */         this.path.deleteCharAt(index); 
/*      */     }
/*      */     
/*      */     private FullPathComponentBuilder() {}
/*      */     
/*      */     public FullPathComponentBuilder cloneBuilder() {
/* 1036 */       FullPathComponentBuilder builder = new FullPathComponentBuilder();
/* 1037 */       builder.append(this.path.toString());
/* 1038 */       return builder;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PathSegmentComponentBuilder
/*      */     implements PathComponentBuilder
/*      */   {
/* 1045 */     private final List<String> pathSegments = new LinkedList<>();
/*      */     
/*      */     public void append(String... pathSegments) {
/* 1048 */       for (String pathSegment : pathSegments) {
/* 1049 */         if (StringUtils.hasText(pathSegment)) {
/* 1050 */           this.pathSegments.add(pathSegment);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public HierarchicalUriComponents.PathComponent build() {
/* 1057 */       return this.pathSegments.isEmpty() ? null : new HierarchicalUriComponents.PathSegmentComponent(this.pathSegments);
/*      */     }
/*      */     
/*      */     private PathSegmentComponentBuilder() {}
/*      */     
/*      */     public PathSegmentComponentBuilder cloneBuilder() {
/* 1063 */       PathSegmentComponentBuilder builder = new PathSegmentComponentBuilder();
/* 1064 */       builder.pathSegments.addAll(this.pathSegments);
/* 1065 */       return builder;
/*      */     }
/*      */   }
/*      */   
/*      */   private enum EncodingHint {
/* 1070 */     ENCODE_TEMPLATE, FULLY_ENCODED, NONE;
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/UriComponentsBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */