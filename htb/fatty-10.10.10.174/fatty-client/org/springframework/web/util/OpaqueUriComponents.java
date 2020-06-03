/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ final class OpaqueUriComponents
/*     */   extends UriComponents
/*     */ {
/*  41 */   private static final MultiValueMap<String, String> QUERY_PARAMS_NONE = (MultiValueMap<String, String>)new LinkedMultiValueMap();
/*     */   
/*     */   @Nullable
/*     */   private final String ssp;
/*     */ 
/*     */   
/*     */   OpaqueUriComponents(@Nullable String scheme, @Nullable String schemeSpecificPart, @Nullable String fragment) {
/*  48 */     super(scheme, fragment);
/*  49 */     this.ssp = schemeSpecificPart;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSchemeSpecificPart() {
/*  56 */     return this.ssp;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getUserInfo() {
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getHost() {
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPort() {
/*  73 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPath() {
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getPathSegments() {
/*  84 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getQuery() {
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, String> getQueryParams() {
/*  95 */     return QUERY_PARAMS_NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public UriComponents encode(Charset charset) {
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected UriComponents expandInternal(UriComponents.UriTemplateVariables uriVariables) {
/* 105 */     String expandedScheme = expandUriComponent(getScheme(), uriVariables);
/* 106 */     String expandedSsp = expandUriComponent(getSchemeSpecificPart(), uriVariables);
/* 107 */     String expandedFragment = expandUriComponent(getFragment(), uriVariables);
/* 108 */     return new OpaqueUriComponents(expandedScheme, expandedSsp, expandedFragment);
/*     */   }
/*     */ 
/*     */   
/*     */   public UriComponents normalize() {
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toUriString() {
/* 118 */     StringBuilder uriBuilder = new StringBuilder();
/*     */     
/* 120 */     if (getScheme() != null) {
/* 121 */       uriBuilder.append(getScheme());
/* 122 */       uriBuilder.append(':');
/*     */     } 
/* 124 */     if (this.ssp != null) {
/* 125 */       uriBuilder.append(this.ssp);
/*     */     }
/* 127 */     if (getFragment() != null) {
/* 128 */       uriBuilder.append('#');
/* 129 */       uriBuilder.append(getFragment());
/*     */     } 
/*     */     
/* 132 */     return uriBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI toUri() {
/*     */     try {
/* 138 */       return new URI(getScheme(), this.ssp, getFragment());
/*     */     }
/* 140 */     catch (URISyntaxException ex) {
/* 141 */       throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
/* 147 */     if (getScheme() != null) {
/* 148 */       builder.scheme(getScheme());
/*     */     }
/* 150 */     if (getSchemeSpecificPart() != null) {
/* 151 */       builder.schemeSpecificPart(getSchemeSpecificPart());
/*     */     }
/* 153 */     if (getFragment() != null) {
/* 154 */       builder.fragment(getFragment());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 161 */     if (this == other) {
/* 162 */       return true;
/*     */     }
/* 164 */     if (!(other instanceof OpaqueUriComponents)) {
/* 165 */       return false;
/*     */     }
/* 167 */     OpaqueUriComponents otherComp = (OpaqueUriComponents)other;
/* 168 */     return (ObjectUtils.nullSafeEquals(getScheme(), otherComp.getScheme()) && 
/* 169 */       ObjectUtils.nullSafeEquals(this.ssp, otherComp.ssp) && 
/* 170 */       ObjectUtils.nullSafeEquals(getFragment(), otherComp.getFragment()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 175 */     int result = ObjectUtils.nullSafeHashCode(getScheme());
/* 176 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.ssp);
/* 177 */     result = 31 * result + ObjectUtils.nullSafeHashCode(getFragment());
/* 178 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/OpaqueUriComponents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */