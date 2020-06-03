/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URI;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public abstract class UriComponents
/*     */   implements Serializable
/*     */ {
/*  51 */   private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final String scheme;
/*     */   
/*     */   @Nullable
/*     */   private final String fragment;
/*     */ 
/*     */   
/*     */   protected UriComponents(@Nullable String scheme, @Nullable String fragment) {
/*  62 */     this.scheme = scheme;
/*  63 */     this.fragment = fragment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final String getScheme() {
/*  74 */     return this.scheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final String getFragment() {
/*  82 */     return this.fragment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public abstract String getSchemeSpecificPart();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public abstract String getUserInfo();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public abstract String getHost();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getPort();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public abstract String getPath();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract List<String> getPathSegments();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public abstract String getQuery();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract MultiValueMap<String, String> getQueryParams();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UriComponents encode() {
/* 142 */     return encode(StandardCharsets.UTF_8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract UriComponents encode(Charset paramCharset);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UriComponents expand(Map<String, ?> uriVariables) {
/* 160 */     Assert.notNull(uriVariables, "'uriVariables' must not be null");
/* 161 */     return expandInternal(new MapTemplateVariables(uriVariables));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UriComponents expand(Object... uriVariableValues) {
/* 171 */     Assert.notNull(uriVariableValues, "'uriVariableValues' must not be null");
/* 172 */     return expandInternal(new VarArgsTemplateVariables(uriVariableValues));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UriComponents expand(UriTemplateVariables uriVariables) {
/* 182 */     Assert.notNull(uriVariables, "'uriVariables' must not be null");
/* 183 */     return expandInternal(uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract UriComponents expandInternal(UriTemplateVariables paramUriTemplateVariables);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract UriComponents normalize();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String toUriString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract URI toUri();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 227 */     return toUriString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void copyToUriComponentsBuilder(UriComponentsBuilder paramUriComponentsBuilder);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static String expandUriComponent(@Nullable String source, UriTemplateVariables uriVariables) {
/* 241 */     return expandUriComponent(source, uriVariables, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static String expandUriComponent(@Nullable String source, UriTemplateVariables uriVariables, @Nullable UnaryOperator<String> encoder) {
/* 248 */     if (source == null) {
/* 249 */       return null;
/*     */     }
/* 251 */     if (source.indexOf('{') == -1) {
/* 252 */       return source;
/*     */     }
/* 254 */     if (source.indexOf(':') != -1) {
/* 255 */       source = sanitizeSource(source);
/*     */     }
/* 257 */     Matcher matcher = NAMES_PATTERN.matcher(source);
/* 258 */     StringBuffer sb = new StringBuffer();
/* 259 */     while (matcher.find()) {
/* 260 */       String match = matcher.group(1);
/* 261 */       String varName = getVariableName(match);
/* 262 */       Object varValue = uriVariables.getValue(varName);
/* 263 */       if (UriTemplateVariables.SKIP_VALUE.equals(varValue)) {
/*     */         continue;
/*     */       }
/* 266 */       String formatted = getVariableValueAsString(varValue);
/* 267 */       formatted = (encoder != null) ? encoder.apply(formatted) : Matcher.quoteReplacement(formatted);
/* 268 */       matcher.appendReplacement(sb, formatted);
/*     */     } 
/* 270 */     matcher.appendTail(sb);
/* 271 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String sanitizeSource(String source) {
/* 278 */     int level = 0;
/* 279 */     StringBuilder sb = new StringBuilder();
/* 280 */     for (char c : source.toCharArray()) {
/* 281 */       if (c == '{') {
/* 282 */         level++;
/*     */       }
/* 284 */       if (c == '}') {
/* 285 */         level--;
/*     */       }
/* 287 */       if (level <= 1 && (level != 1 || c != '}'))
/*     */       {
/*     */         
/* 290 */         sb.append(c); } 
/*     */     } 
/* 292 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static String getVariableName(String match) {
/* 296 */     int colonIdx = match.indexOf(':');
/* 297 */     return (colonIdx != -1) ? match.substring(0, colonIdx) : match;
/*     */   }
/*     */   
/*     */   private static String getVariableValueAsString(@Nullable Object variableValue) {
/* 301 */     return (variableValue != null) ? variableValue.toString() : "";
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
/*     */   public static interface UriTemplateVariables
/*     */   {
/* 316 */     public static final Object SKIP_VALUE = UriTemplateVariables.class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     Object getValue(@Nullable String param1String);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MapTemplateVariables
/*     */     implements UriTemplateVariables
/*     */   {
/*     */     private final Map<String, ?> uriVariables;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MapTemplateVariables(Map<String, ?> uriVariables) {
/* 338 */       this.uriVariables = uriVariables;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object getValue(@Nullable String name) {
/* 344 */       if (!this.uriVariables.containsKey(name)) {
/* 345 */         throw new IllegalArgumentException("Map has no value for '" + name + "'");
/*     */       }
/* 347 */       return this.uriVariables.get(name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class VarArgsTemplateVariables
/*     */     implements UriTemplateVariables
/*     */   {
/*     */     private final Iterator<Object> valueIterator;
/*     */ 
/*     */     
/*     */     public VarArgsTemplateVariables(Object... uriVariableValues) {
/* 360 */       this.valueIterator = Arrays.<Object>asList(uriVariableValues).iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object getValue(@Nullable String name) {
/* 366 */       if (!this.valueIterator.hasNext()) {
/* 367 */         throw new IllegalArgumentException("Not enough variable values available to expand '" + name + "'");
/*     */       }
/* 369 */       return this.valueIterator.next();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/UriComponents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */