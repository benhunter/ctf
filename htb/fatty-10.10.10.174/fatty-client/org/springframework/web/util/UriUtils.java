/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class UriUtils
/*     */ {
/*     */   public static String encodeScheme(String scheme, String encoding) {
/*  60 */     return encode(scheme, encoding, HierarchicalUriComponents.Type.SCHEME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeScheme(String scheme, Charset charset) {
/*  71 */     return encode(scheme, charset, HierarchicalUriComponents.Type.SCHEME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeAuthority(String authority, String encoding) {
/*  81 */     return encode(authority, encoding, HierarchicalUriComponents.Type.AUTHORITY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeAuthority(String authority, Charset charset) {
/*  92 */     return encode(authority, charset, HierarchicalUriComponents.Type.AUTHORITY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeUserInfo(String userInfo, String encoding) {
/* 102 */     return encode(userInfo, encoding, HierarchicalUriComponents.Type.USER_INFO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeUserInfo(String userInfo, Charset charset) {
/* 113 */     return encode(userInfo, charset, HierarchicalUriComponents.Type.USER_INFO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeHost(String host, String encoding) {
/* 123 */     return encode(host, encoding, HierarchicalUriComponents.Type.HOST_IPV4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeHost(String host, Charset charset) {
/* 134 */     return encode(host, charset, HierarchicalUriComponents.Type.HOST_IPV4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodePort(String port, String encoding) {
/* 144 */     return encode(port, encoding, HierarchicalUriComponents.Type.PORT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodePort(String port, Charset charset) {
/* 155 */     return encode(port, charset, HierarchicalUriComponents.Type.PORT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodePath(String path, String encoding) {
/* 165 */     return encode(path, encoding, HierarchicalUriComponents.Type.PATH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodePath(String path, Charset charset) {
/* 176 */     return encode(path, charset, HierarchicalUriComponents.Type.PATH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodePathSegment(String segment, String encoding) {
/* 186 */     return encode(segment, encoding, HierarchicalUriComponents.Type.PATH_SEGMENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodePathSegment(String segment, Charset charset) {
/* 197 */     return encode(segment, charset, HierarchicalUriComponents.Type.PATH_SEGMENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeQuery(String query, String encoding) {
/* 207 */     return encode(query, encoding, HierarchicalUriComponents.Type.QUERY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeQuery(String query, Charset charset) {
/* 218 */     return encode(query, charset, HierarchicalUriComponents.Type.QUERY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeQueryParam(String queryParam, String encoding) {
/* 229 */     return encode(queryParam, encoding, HierarchicalUriComponents.Type.QUERY_PARAM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeQueryParam(String queryParam, Charset charset) {
/* 240 */     return encode(queryParam, charset, HierarchicalUriComponents.Type.QUERY_PARAM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeFragment(String fragment, String encoding) {
/* 250 */     return encode(fragment, encoding, HierarchicalUriComponents.Type.FRAGMENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeFragment(String fragment, Charset charset) {
/* 261 */     return encode(fragment, charset, HierarchicalUriComponents.Type.FRAGMENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encode(String source, String encoding) {
/* 272 */     return encode(source, encoding, HierarchicalUriComponents.Type.URI);
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
/*     */   public static String encode(String source, Charset charset) {
/* 287 */     return encode(source, charset, HierarchicalUriComponents.Type.URI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> encodeUriVariables(Map<String, ?> uriVariables) {
/* 298 */     Map<String, String> result = new LinkedHashMap<>(uriVariables.size());
/* 299 */     uriVariables.forEach((key, value) -> {
/*     */           String stringValue = (value != null) ? value.toString() : "";
/*     */           result.put(key, encode(stringValue, StandardCharsets.UTF_8));
/*     */         });
/* 303 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object[] encodeUriVariables(Object... uriVariables) {
/* 314 */     return Arrays.<Object>stream(uriVariables)
/* 315 */       .map(value -> {
/*     */           String stringValue = (value != null) ? value.toString() : "";
/*     */           
/*     */           return encode(stringValue, StandardCharsets.UTF_8);
/* 319 */         }).toArray();
/*     */   }
/*     */   
/*     */   private static String encode(String scheme, String encoding, HierarchicalUriComponents.Type type) {
/* 323 */     return HierarchicalUriComponents.encodeUriComponent(scheme, encoding, type);
/*     */   }
/*     */   
/*     */   private static String encode(String scheme, Charset charset, HierarchicalUriComponents.Type type) {
/* 327 */     return HierarchicalUriComponents.encodeUriComponent(scheme, charset, type);
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
/*     */   public static String decode(String source, String encoding) {
/* 342 */     return StringUtils.uriDecode(source, Charset.forName(encoding));
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
/*     */   public static String decode(String source, Charset charset) {
/* 357 */     return StringUtils.uriDecode(source, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String extractFileExtension(String path) {
/* 368 */     int end = path.indexOf('?');
/* 369 */     int fragmentIndex = path.indexOf('#');
/* 370 */     if (fragmentIndex != -1 && (end == -1 || fragmentIndex < end)) {
/* 371 */       end = fragmentIndex;
/*     */     }
/* 373 */     if (end == -1) {
/* 374 */       end = path.length();
/*     */     }
/* 376 */     int begin = path.lastIndexOf('/', end) + 1;
/* 377 */     int paramIndex = path.indexOf(';', begin);
/* 378 */     end = (paramIndex != -1 && paramIndex < end) ? paramIndex : end;
/* 379 */     int extIndex = path.lastIndexOf('.', end);
/* 380 */     if (extIndex != -1 && extIndex > begin) {
/* 381 */       return path.substring(extIndex + 1, end);
/*     */     }
/* 383 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/UriUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */