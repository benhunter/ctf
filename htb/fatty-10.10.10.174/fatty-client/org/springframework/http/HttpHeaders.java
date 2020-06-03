/*      */ package org.springframework.http;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.URI;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.DecimalFormatSymbols;
/*      */ import java.time.Instant;
/*      */ import java.time.ZoneId;
/*      */ import java.time.ZonedDateTime;
/*      */ import java.time.format.DateTimeFormatter;
/*      */ import java.time.format.DateTimeParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Base64;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.stream.Collectors;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*      */ import org.springframework.util.LinkedMultiValueMap;
/*      */ import org.springframework.util.MultiValueMap;
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
/*      */ 
/*      */ 
/*      */ public class HttpHeaders
/*      */   implements MultiValueMap<String, String>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -8578554704772377436L;
/*      */   public static final String ACCEPT = "Accept";
/*      */   public static final String ACCEPT_CHARSET = "Accept-Charset";
/*      */   public static final String ACCEPT_ENCODING = "Accept-Encoding";
/*      */   public static final String ACCEPT_LANGUAGE = "Accept-Language";
/*      */   public static final String ACCEPT_RANGES = "Accept-Ranges";
/*      */   public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
/*      */   public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
/*      */   public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
/*      */   public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
/*      */   public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
/*      */   public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
/*      */   public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
/*      */   public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
/*      */   public static final String AGE = "Age";
/*      */   public static final String ALLOW = "Allow";
/*      */   public static final String AUTHORIZATION = "Authorization";
/*      */   public static final String CACHE_CONTROL = "Cache-Control";
/*      */   public static final String CONNECTION = "Connection";
/*      */   public static final String CONTENT_ENCODING = "Content-Encoding";
/*      */   public static final String CONTENT_DISPOSITION = "Content-Disposition";
/*      */   public static final String CONTENT_LANGUAGE = "Content-Language";
/*      */   public static final String CONTENT_LENGTH = "Content-Length";
/*      */   public static final String CONTENT_LOCATION = "Content-Location";
/*      */   public static final String CONTENT_RANGE = "Content-Range";
/*      */   public static final String CONTENT_TYPE = "Content-Type";
/*      */   public static final String COOKIE = "Cookie";
/*      */   public static final String DATE = "Date";
/*      */   public static final String ETAG = "ETag";
/*      */   public static final String EXPECT = "Expect";
/*      */   public static final String EXPIRES = "Expires";
/*      */   public static final String FROM = "From";
/*      */   public static final String HOST = "Host";
/*      */   public static final String IF_MATCH = "If-Match";
/*      */   public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
/*      */   public static final String IF_NONE_MATCH = "If-None-Match";
/*      */   public static final String IF_RANGE = "If-Range";
/*      */   public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
/*      */   public static final String LAST_MODIFIED = "Last-Modified";
/*      */   public static final String LINK = "Link";
/*      */   public static final String LOCATION = "Location";
/*      */   public static final String MAX_FORWARDS = "Max-Forwards";
/*      */   public static final String ORIGIN = "Origin";
/*      */   public static final String PRAGMA = "Pragma";
/*      */   public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
/*      */   public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
/*      */   public static final String RANGE = "Range";
/*      */   public static final String REFERER = "Referer";
/*      */   public static final String RETRY_AFTER = "Retry-After";
/*      */   public static final String SERVER = "Server";
/*      */   public static final String SET_COOKIE = "Set-Cookie";
/*      */   public static final String SET_COOKIE2 = "Set-Cookie2";
/*      */   public static final String TE = "TE";
/*      */   public static final String TRAILER = "Trailer";
/*      */   public static final String TRANSFER_ENCODING = "Transfer-Encoding";
/*      */   public static final String UPGRADE = "Upgrade";
/*      */   public static final String USER_AGENT = "User-Agent";
/*      */   public static final String VARY = "Vary";
/*      */   public static final String VIA = "Via";
/*      */   public static final String WARNING = "Warning";
/*      */   public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
/*  386 */   public static final HttpHeaders EMPTY = new ReadOnlyHttpHeaders(new HttpHeaders((MultiValueMap<String, String>)new LinkedMultiValueMap(0)));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  392 */   private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");
/*      */   
/*  394 */   private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.ENGLISH);
/*      */   
/*  396 */   private static final ZoneId GMT = ZoneId.of("GMT");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  402 */   private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).withZone(GMT);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  408 */   private static final DateTimeFormatter[] DATE_PARSERS = new DateTimeFormatter[] { DateTimeFormatter.RFC_1123_DATE_TIME, 
/*      */       
/*  410 */       DateTimeFormatter.ofPattern("EEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US), 
/*  411 */       DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", Locale.US).withZone(GMT) };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final MultiValueMap<String, String> headers;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders() {
/*  423 */     this(CollectionUtils.toMultiValueMap((Map)new LinkedCaseInsensitiveMap(8, Locale.ENGLISH)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders(MultiValueMap<String, String> headers) {
/*  434 */     Assert.notNull(headers, "MultiValueMap must not be null");
/*  435 */     this.headers = headers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccept(List<MediaType> acceptableMediaTypes) {
/*  444 */     set("Accept", MediaType.toString(acceptableMediaTypes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<MediaType> getAccept() {
/*  453 */     return MediaType.parseMediaTypes(get("Accept"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAcceptLanguage(List<Locale.LanguageRange> languages) {
/*  462 */     Assert.notNull(languages, "LanguageRange List must not be null");
/*  463 */     DecimalFormat decimal = new DecimalFormat("0.0", DECIMAL_FORMAT_SYMBOLS);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  469 */     List<String> values = (List<String>)languages.stream().map(range -> (range.getWeight() == 1.0D) ? range.getRange() : (range.getRange() + ";q=" + decimal.format(range.getWeight()))).collect(Collectors.toList());
/*  470 */     set("Accept-Language", toCommaDelimitedString(values));
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
/*      */   public List<Locale.LanguageRange> getAcceptLanguage() {
/*  483 */     String value = getFirst("Accept-Language");
/*  484 */     return StringUtils.hasText(value) ? Locale.LanguageRange.parse(value) : Collections.<Locale.LanguageRange>emptyList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAcceptLanguageAsLocales(List<Locale> locales) {
/*  492 */     setAcceptLanguage((List<Locale.LanguageRange>)locales.stream()
/*  493 */         .map(locale -> new Locale.LanguageRange(locale.toLanguageTag()))
/*  494 */         .collect(Collectors.toList()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Locale> getAcceptLanguageAsLocales() {
/*  505 */     List<Locale.LanguageRange> ranges = getAcceptLanguage();
/*  506 */     if (ranges.isEmpty()) {
/*  507 */       return Collections.emptyList();
/*      */     }
/*  509 */     return (List<Locale>)ranges.stream()
/*  510 */       .map(range -> Locale.forLanguageTag(range.getRange()))
/*  511 */       .filter(locale -> StringUtils.hasText(locale.getDisplayName()))
/*  512 */       .collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlAllowCredentials(boolean allowCredentials) {
/*  519 */     set("Access-Control-Allow-Credentials", Boolean.toString(allowCredentials));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAccessControlAllowCredentials() {
/*  526 */     return Boolean.parseBoolean(getFirst("Access-Control-Allow-Credentials"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlAllowHeaders(List<String> allowedHeaders) {
/*  533 */     set("Access-Control-Allow-Headers", toCommaDelimitedString(allowedHeaders));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getAccessControlAllowHeaders() {
/*  540 */     return getValuesAsList("Access-Control-Allow-Headers");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlAllowMethods(List<HttpMethod> allowedMethods) {
/*  547 */     set("Access-Control-Allow-Methods", StringUtils.collectionToCommaDelimitedString(allowedMethods));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<HttpMethod> getAccessControlAllowMethods() {
/*  554 */     List<HttpMethod> result = new ArrayList<>();
/*  555 */     String value = getFirst("Access-Control-Allow-Methods");
/*  556 */     if (value != null) {
/*  557 */       String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/*  558 */       for (String token : tokens) {
/*  559 */         HttpMethod resolved = HttpMethod.resolve(token);
/*  560 */         if (resolved != null) {
/*  561 */           result.add(resolved);
/*      */         }
/*      */       } 
/*      */     } 
/*  565 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlAllowOrigin(@Nullable String allowedOrigin) {
/*  572 */     setOrRemove("Access-Control-Allow-Origin", allowedOrigin);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getAccessControlAllowOrigin() {
/*  580 */     return getFieldValues("Access-Control-Allow-Origin");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlExposeHeaders(List<String> exposedHeaders) {
/*  587 */     set("Access-Control-Expose-Headers", toCommaDelimitedString(exposedHeaders));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getAccessControlExposeHeaders() {
/*  594 */     return getValuesAsList("Access-Control-Expose-Headers");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlMaxAge(long maxAge) {
/*  601 */     set("Access-Control-Max-Age", Long.toString(maxAge));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getAccessControlMaxAge() {
/*  609 */     String value = getFirst("Access-Control-Max-Age");
/*  610 */     return (value != null) ? Long.parseLong(value) : -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlRequestHeaders(List<String> requestHeaders) {
/*  617 */     set("Access-Control-Request-Headers", toCommaDelimitedString(requestHeaders));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getAccessControlRequestHeaders() {
/*  624 */     return getValuesAsList("Access-Control-Request-Headers");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlRequestMethod(@Nullable HttpMethod requestMethod) {
/*  631 */     setOrRemove("Access-Control-Request-Method", (requestMethod != null) ? requestMethod.name() : null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public HttpMethod getAccessControlRequestMethod() {
/*  639 */     return HttpMethod.resolve(getFirst("Access-Control-Request-Method"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAcceptCharset(List<Charset> acceptableCharsets) {
/*  647 */     StringBuilder builder = new StringBuilder();
/*  648 */     for (Iterator<Charset> iterator = acceptableCharsets.iterator(); iterator.hasNext(); ) {
/*  649 */       Charset charset = iterator.next();
/*  650 */       builder.append(charset.name().toLowerCase(Locale.ENGLISH));
/*  651 */       if (iterator.hasNext()) {
/*  652 */         builder.append(", ");
/*      */       }
/*      */     } 
/*  655 */     set("Accept-Charset", builder.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Charset> getAcceptCharset() {
/*  663 */     String value = getFirst("Accept-Charset");
/*  664 */     if (value != null) {
/*  665 */       String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/*  666 */       List<Charset> result = new ArrayList<>(tokens.length);
/*  667 */       for (String token : tokens) {
/*  668 */         String charsetName; int paramIdx = token.indexOf(';');
/*      */         
/*  670 */         if (paramIdx == -1) {
/*  671 */           charsetName = token;
/*      */         } else {
/*      */           
/*  674 */           charsetName = token.substring(0, paramIdx);
/*      */         } 
/*  676 */         if (!charsetName.equals("*")) {
/*  677 */           result.add(Charset.forName(charsetName));
/*      */         }
/*      */       } 
/*  680 */       return result;
/*      */     } 
/*      */     
/*  683 */     return Collections.emptyList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllow(Set<HttpMethod> allowedMethods) {
/*  692 */     set("Allow", StringUtils.collectionToCommaDelimitedString(allowedMethods));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<HttpMethod> getAllow() {
/*  701 */     String value = getFirst("Allow");
/*  702 */     if (StringUtils.hasLength(value)) {
/*  703 */       String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/*  704 */       List<HttpMethod> result = new ArrayList<>(tokens.length);
/*  705 */       for (String token : tokens) {
/*  706 */         HttpMethod resolved = HttpMethod.resolve(token);
/*  707 */         if (resolved != null) {
/*  708 */           result.add(resolved);
/*      */         }
/*      */       } 
/*  711 */       return EnumSet.copyOf(result);
/*      */     } 
/*      */     
/*  714 */     return EnumSet.noneOf(HttpMethod.class);
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
/*      */   public void setBasicAuth(String username, String password) {
/*  732 */     setBasicAuth(username, password, null);
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
/*      */   public void setBasicAuth(String username, String password, @Nullable Charset charset) {
/*  748 */     Assert.notNull(username, "Username must not be null");
/*  749 */     Assert.notNull(password, "Password must not be null");
/*  750 */     if (charset == null) {
/*  751 */       charset = StandardCharsets.ISO_8859_1;
/*      */     }
/*      */     
/*  754 */     CharsetEncoder encoder = charset.newEncoder();
/*  755 */     if (!encoder.canEncode(username) || !encoder.canEncode(password)) {
/*  756 */       throw new IllegalArgumentException("Username or password contains characters that cannot be encoded to " + charset
/*  757 */           .displayName());
/*      */     }
/*      */     
/*  760 */     String credentialsString = username + ":" + password;
/*  761 */     byte[] encodedBytes = Base64.getEncoder().encode(credentialsString.getBytes(charset));
/*  762 */     String encodedCredentials = new String(encodedBytes, charset);
/*  763 */     set("Authorization", "Basic " + encodedCredentials);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBearerAuth(String token) {
/*  774 */     set("Authorization", "Bearer " + token);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCacheControl(CacheControl cacheControl) {
/*  783 */     setOrRemove("Cache-Control", cacheControl.getHeaderValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCacheControl(@Nullable String cacheControl) {
/*  790 */     setOrRemove("Cache-Control", cacheControl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getCacheControl() {
/*  798 */     return getFieldValues("Cache-Control");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnection(String connection) {
/*  805 */     set("Connection", connection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnection(List<String> connection) {
/*  812 */     set("Connection", toCommaDelimitedString(connection));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getConnection() {
/*  819 */     return getValuesAsList("Connection");
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
/*      */   public void setContentDispositionFormData(String name, @Nullable String filename) {
/*  834 */     Assert.notNull(name, "Name must not be null");
/*  835 */     ContentDisposition.Builder disposition = ContentDisposition.builder("form-data").name(name);
/*  836 */     if (filename != null) {
/*  837 */       disposition.filename(filename);
/*      */     }
/*  839 */     setContentDisposition(disposition.build());
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
/*      */   public void setContentDisposition(ContentDisposition contentDisposition) {
/*  853 */     set("Content-Disposition", contentDisposition.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ContentDisposition getContentDisposition() {
/*  862 */     String contentDisposition = getFirst("Content-Disposition");
/*  863 */     if (contentDisposition != null) {
/*  864 */       return ContentDisposition.parse(contentDisposition);
/*      */     }
/*  866 */     return ContentDisposition.empty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContentLanguage(@Nullable Locale locale) {
/*  877 */     setOrRemove("Content-Language", (locale != null) ? locale.toLanguageTag() : null);
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
/*      */   @Nullable
/*      */   public Locale getContentLanguage() {
/*  890 */     return getValuesAsList("Content-Language")
/*  891 */       .stream()
/*  892 */       .findFirst()
/*  893 */       .map(Locale::forLanguageTag)
/*  894 */       .orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContentLength(long contentLength) {
/*  902 */     set("Content-Length", Long.toString(contentLength));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getContentLength() {
/*  911 */     String value = getFirst("Content-Length");
/*  912 */     return (value != null) ? Long.parseLong(value) : -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContentType(@Nullable MediaType mediaType) {
/*  920 */     if (mediaType != null) {
/*  921 */       Assert.isTrue(!mediaType.isWildcardType(), "Content-Type cannot contain wildcard type '*'");
/*  922 */       Assert.isTrue(!mediaType.isWildcardSubtype(), "Content-Type cannot contain wildcard subtype '*'");
/*  923 */       set("Content-Type", mediaType.toString());
/*      */     } else {
/*      */       
/*  926 */       remove("Content-Type");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public MediaType getContentType() {
/*  937 */     String value = getFirst("Content-Type");
/*  938 */     return StringUtils.hasLength(value) ? MediaType.parseMediaType(value) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDate(long date) {
/*  948 */     setDate("Date", date);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getDate() {
/*  959 */     return getFirstDate("Date");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setETag(@Nullable String etag) {
/*  966 */     if (etag != null) {
/*  967 */       Assert.isTrue((etag.startsWith("\"") || etag.startsWith("W/")), "Invalid ETag: does not start with W/ or \"");
/*      */       
/*  969 */       Assert.isTrue(etag.endsWith("\""), "Invalid ETag: does not end with \"");
/*  970 */       set("ETag", etag);
/*      */     } else {
/*      */       
/*  973 */       remove("ETag");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getETag() {
/*  982 */     return getFirst("ETag");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExpires(ZonedDateTime expires) {
/*  991 */     setZonedDateTime("Expires", expires);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExpires(long expires) {
/* 1001 */     setDate("Expires", expires);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getExpires() {
/* 1012 */     return getFirstDate("Expires", false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHost(@Nullable InetSocketAddress host) {
/* 1023 */     if (host != null) {
/* 1024 */       String value = host.getHostString();
/* 1025 */       int port = host.getPort();
/* 1026 */       if (port != 0) {
/* 1027 */         value = value + ":" + port;
/*      */       }
/* 1029 */       set("Host", value);
/*      */     } else {
/*      */       
/* 1032 */       remove("Host", null);
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
/*      */   @Nullable
/*      */   public InetSocketAddress getHost() {
/* 1045 */     String value = getFirst("Host");
/* 1046 */     if (value == null) {
/* 1047 */       return null;
/*      */     }
/*      */     
/* 1050 */     String host = null;
/* 1051 */     int port = 0;
/* 1052 */     int separator = value.startsWith("[") ? value.indexOf(':', value.indexOf(']')) : value.lastIndexOf(':');
/* 1053 */     if (separator != -1) {
/* 1054 */       host = value.substring(0, separator);
/* 1055 */       String portString = value.substring(separator + 1);
/*      */       try {
/* 1057 */         port = Integer.parseInt(portString);
/*      */       }
/* 1059 */       catch (NumberFormatException numberFormatException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1064 */     if (host == null) {
/* 1065 */       host = value;
/*      */     }
/* 1067 */     return InetSocketAddress.createUnresolved(host, port);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfMatch(String ifMatch) {
/* 1075 */     set("If-Match", ifMatch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfMatch(List<String> ifMatchList) {
/* 1083 */     set("If-Match", toCommaDelimitedString(ifMatchList));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getIfMatch() {
/* 1091 */     return getETagValuesAsList("If-Match");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfModifiedSince(ZonedDateTime ifModifiedSince) {
/* 1100 */     setZonedDateTime("If-Modified-Since", ifModifiedSince.withZoneSameInstant(GMT));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfModifiedSince(Instant ifModifiedSince) {
/* 1109 */     setInstant("If-Modified-Since", ifModifiedSince);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfModifiedSince(long ifModifiedSince) {
/* 1118 */     setDate("If-Modified-Since", ifModifiedSince);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIfModifiedSince() {
/* 1128 */     return getFirstDate("If-Modified-Since", false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfNoneMatch(String ifNoneMatch) {
/* 1135 */     set("If-None-Match", ifNoneMatch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfNoneMatch(List<String> ifNoneMatchList) {
/* 1142 */     set("If-None-Match", toCommaDelimitedString(ifNoneMatchList));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getIfNoneMatch() {
/* 1149 */     return getETagValuesAsList("If-None-Match");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfUnmodifiedSince(ZonedDateTime ifUnmodifiedSince) {
/* 1158 */     setZonedDateTime("If-Unmodified-Since", ifUnmodifiedSince.withZoneSameInstant(GMT));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfUnmodifiedSince(Instant ifUnmodifiedSince) {
/* 1167 */     setInstant("If-Unmodified-Since", ifUnmodifiedSince);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfUnmodifiedSince(long ifUnmodifiedSince) {
/* 1177 */     setDate("If-Unmodified-Since", ifUnmodifiedSince);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIfUnmodifiedSince() {
/* 1188 */     return getFirstDate("If-Unmodified-Since", false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastModified(ZonedDateTime lastModified) {
/* 1197 */     setZonedDateTime("Last-Modified", lastModified.withZoneSameInstant(GMT));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastModified(Instant lastModified) {
/* 1206 */     setInstant("Last-Modified", lastModified);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastModified(long lastModified) {
/* 1216 */     setDate("Last-Modified", lastModified);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastModified() {
/* 1227 */     return getFirstDate("Last-Modified", false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocation(@Nullable URI location) {
/* 1235 */     setOrRemove("Location", (location != null) ? location.toASCIIString() : null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public URI getLocation() {
/* 1245 */     String value = getFirst("Location");
/* 1246 */     return (value != null) ? URI.create(value) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOrigin(@Nullable String origin) {
/* 1253 */     setOrRemove("Origin", origin);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getOrigin() {
/* 1261 */     return getFirst("Origin");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPragma(@Nullable String pragma) {
/* 1268 */     setOrRemove("Pragma", pragma);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getPragma() {
/* 1276 */     return getFirst("Pragma");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRange(List<HttpRange> ranges) {
/* 1283 */     String value = HttpRange.toString(ranges);
/* 1284 */     set("Range", value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<HttpRange> getRange() {
/* 1292 */     String value = getFirst("Range");
/* 1293 */     return HttpRange.parseRanges(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUpgrade(@Nullable String upgrade) {
/* 1300 */     setOrRemove("Upgrade", upgrade);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getUpgrade() {
/* 1308 */     return getFirst("Upgrade");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVary(List<String> requestHeaders) {
/* 1319 */     set("Vary", toCommaDelimitedString(requestHeaders));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getVary() {
/* 1327 */     return getValuesAsList("Vary");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setZonedDateTime(String headerName, ZonedDateTime date) {
/* 1337 */     set(headerName, DATE_FORMATTER.format(date));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInstant(String headerName, Instant date) {
/* 1347 */     setZonedDateTime(headerName, ZonedDateTime.ofInstant(date, GMT));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDate(String headerName, long date) {
/* 1358 */     setInstant(headerName, Instant.ofEpochMilli(date));
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
/*      */   public long getFirstDate(String headerName) {
/* 1371 */     return getFirstDate(headerName, true);
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
/*      */   private long getFirstDate(String headerName, boolean rejectInvalid) {
/* 1387 */     ZonedDateTime zonedDateTime = getFirstZonedDateTime(headerName, rejectInvalid);
/* 1388 */     return (zonedDateTime != null) ? zonedDateTime.toInstant().toEpochMilli() : -1L;
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
/*      */   @Nullable
/*      */   public ZonedDateTime getFirstZonedDateTime(String headerName) {
/* 1401 */     return getFirstZonedDateTime(headerName, true);
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
/*      */   @Nullable
/*      */   private ZonedDateTime getFirstZonedDateTime(String headerName, boolean rejectInvalid) {
/* 1417 */     String headerValue = getFirst(headerName);
/* 1418 */     if (headerValue == null)
/*      */     {
/* 1420 */       return null;
/*      */     }
/* 1422 */     if (headerValue.length() >= 3) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1427 */       int parametersIndex = headerValue.indexOf(';');
/* 1428 */       if (parametersIndex != -1) {
/* 1429 */         headerValue = headerValue.substring(0, parametersIndex);
/*      */       }
/*      */       
/* 1432 */       for (DateTimeFormatter dateFormatter : DATE_PARSERS) {
/*      */         try {
/* 1434 */           return ZonedDateTime.parse(headerValue, dateFormatter);
/*      */         }
/* 1436 */         catch (DateTimeParseException dateTimeParseException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1442 */     if (rejectInvalid) {
/* 1443 */       throw new IllegalArgumentException("Cannot parse date value \"" + headerValue + "\" for \"" + headerName + "\" header");
/*      */     }
/*      */     
/* 1446 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getValuesAsList(String headerName) {
/* 1457 */     List<String> values = get(headerName);
/* 1458 */     if (values != null) {
/* 1459 */       List<String> result = new ArrayList<>();
/* 1460 */       for (String value : values) {
/* 1461 */         if (value != null) {
/* 1462 */           Collections.addAll(result, StringUtils.tokenizeToStringArray(value, ","));
/*      */         }
/*      */       } 
/* 1465 */       return result;
/*      */     } 
/* 1467 */     return Collections.emptyList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<String> getETagValuesAsList(String headerName) {
/* 1477 */     List<String> values = get(headerName);
/* 1478 */     if (values != null) {
/* 1479 */       List<String> result = new ArrayList<>();
/* 1480 */       for (String value : values) {
/* 1481 */         if (value != null) {
/* 1482 */           Matcher matcher = ETAG_HEADER_VALUE_PATTERN.matcher(value);
/* 1483 */           while (matcher.find()) {
/* 1484 */             if ("*".equals(matcher.group())) {
/* 1485 */               result.add(matcher.group());
/*      */               continue;
/*      */             } 
/* 1488 */             result.add(matcher.group(1));
/*      */           } 
/*      */           
/* 1491 */           if (result.isEmpty()) {
/* 1492 */             throw new IllegalArgumentException("Could not parse header '" + headerName + "' with value '" + value + "'");
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1497 */       return result;
/*      */     } 
/* 1499 */     return Collections.emptyList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected String getFieldValues(String headerName) {
/* 1510 */     List<String> headerValues = get(headerName);
/* 1511 */     return (headerValues != null) ? toCommaDelimitedString(headerValues) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String toCommaDelimitedString(List<String> headerValues) {
/* 1520 */     StringBuilder builder = new StringBuilder();
/* 1521 */     for (Iterator<String> it = headerValues.iterator(); it.hasNext(); ) {
/* 1522 */       String val = it.next();
/* 1523 */       if (val != null) {
/* 1524 */         builder.append(val);
/* 1525 */         if (it.hasNext()) {
/* 1526 */           builder.append(", ");
/*      */         }
/*      */       } 
/*      */     } 
/* 1530 */     return builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setOrRemove(String headerName, @Nullable String headerValue) {
/* 1539 */     if (headerValue != null) {
/* 1540 */       set(headerName, headerValue);
/*      */     } else {
/*      */       
/* 1543 */       remove(headerName);
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
/*      */   @Nullable
/*      */   public String getFirst(String headerName) {
/* 1558 */     return (String)this.headers.getFirst(headerName);
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
/*      */   public void add(String headerName, @Nullable String headerValue) {
/* 1571 */     this.headers.add(headerName, headerValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAll(String key, List<? extends String> values) {
/* 1576 */     this.headers.addAll(key, values);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAll(MultiValueMap<String, String> values) {
/* 1581 */     this.headers.addAll(values);
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
/*      */   public void set(String headerName, @Nullable String headerValue) {
/* 1594 */     this.headers.set(headerName, headerValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAll(Map<String, String> values) {
/* 1599 */     this.headers.setAll(values);
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, String> toSingleValueMap() {
/* 1604 */     return this.headers.toSingleValueMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/* 1612 */     return this.headers.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/* 1617 */     return this.headers.isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object key) {
/* 1622 */     return this.headers.containsKey(key);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(Object value) {
/* 1627 */     return this.headers.containsValue(value);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public List<String> get(Object key) {
/* 1633 */     return (List<String>)this.headers.get(key);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> put(String key, List<String> value) {
/* 1638 */     return (List<String>)this.headers.put(key, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> remove(Object key) {
/* 1643 */     return (List<String>)this.headers.remove(key);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends String, ? extends List<String>> map) {
/* 1648 */     this.headers.putAll(map);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 1653 */     this.headers.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> keySet() {
/* 1658 */     return this.headers.keySet();
/*      */   }
/*      */ 
/*      */   
/*      */   public Collection<List<String>> values() {
/* 1663 */     return this.headers.values();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<String, List<String>>> entrySet() {
/* 1668 */     return this.headers.entrySet();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object other) {
/* 1674 */     if (this == other) {
/* 1675 */       return true;
/*      */     }
/* 1677 */     if (!(other instanceof HttpHeaders)) {
/* 1678 */       return false;
/*      */     }
/* 1680 */     HttpHeaders otherHeaders = (HttpHeaders)other;
/* 1681 */     return this.headers.equals(otherHeaders.headers);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1686 */     return this.headers.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1691 */     return formatHeaders(this.headers);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers) {
/* 1699 */     Assert.notNull(headers, "HttpHeaders must not be null");
/* 1700 */     if (headers instanceof ReadOnlyHttpHeaders) {
/* 1701 */       return headers;
/*      */     }
/*      */     
/* 1704 */     return new ReadOnlyHttpHeaders(headers);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static HttpHeaders writableHttpHeaders(HttpHeaders headers) {
/* 1713 */     Assert.notNull(headers, "HttpHeaders must not be null");
/* 1714 */     if (headers == EMPTY) {
/* 1715 */       return new HttpHeaders();
/*      */     }
/* 1717 */     if (headers instanceof ReadOnlyHttpHeaders) {
/* 1718 */       return new HttpHeaders(headers.headers);
/*      */     }
/*      */     
/* 1721 */     return headers;
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
/*      */   public static String formatHeaders(MultiValueMap<String, String> headers) {
/* 1734 */     return headers.entrySet().stream()
/* 1735 */       .map(entry -> {
/*      */           List<String> values = (List<String>)entry.getValue();
/*      */ 
/*      */ 
/*      */           
/*      */           return (String)entry.getKey() + ":" + ((values.size() == 1) ? ("\"" + (String)values.get(0) + "\"") : values.stream().map(()).collect(Collectors.joining(", ")));
/* 1741 */         }).collect(Collectors.joining(", ", "[", "]"));
/*      */   }
/*      */ 
/*      */   
/*      */   static String formatDate(long date) {
/* 1746 */     Instant instant = Instant.ofEpochMilli(date);
/* 1747 */     ZonedDateTime time = ZonedDateTime.ofInstant(instant, GMT);
/* 1748 */     return DATE_FORMATTER.format(time);
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/HttpHeaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */