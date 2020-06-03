/*     */ package org.springframework.http.server;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ final class DefaultPathContainer
/*     */   implements PathContainer
/*     */ {
/*  41 */   private static final MultiValueMap<String, String> EMPTY_MAP = (MultiValueMap<String, String>)new LinkedMultiValueMap();
/*     */   
/*  43 */   private static final PathContainer EMPTY_PATH = new DefaultPathContainer("", Collections.emptyList());
/*     */ 
/*     */   
/*     */   private static final PathContainer.Separator SEPARATOR = () -> "/";
/*     */   
/*     */   private final String path;
/*     */   
/*     */   private final List<PathContainer.Element> elements;
/*     */ 
/*     */   
/*     */   private DefaultPathContainer(String path, List<PathContainer.Element> elements) {
/*  54 */     this.path = path;
/*  55 */     this.elements = Collections.unmodifiableList(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String value() {
/*  61 */     return this.path;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PathContainer.Element> elements() {
/*  66 */     return this.elements;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/*  72 */     if (this == other) {
/*  73 */       return true;
/*     */     }
/*  75 */     if (other == null || getClass() != other.getClass()) {
/*  76 */       return false;
/*     */     }
/*  78 */     return this.path.equals(((DefaultPathContainer)other).path);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  83 */     return this.path.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  88 */     return value();
/*     */   }
/*     */   
/*     */   static PathContainer createFromUrlPath(String path) {
/*     */     int begin;
/*  93 */     if (path.equals("")) {
/*  94 */       return EMPTY_PATH;
/*     */     }
/*  96 */     String separator = "/";
/*  97 */     PathContainer.Separator separatorElement = separator.equals(SEPARATOR.value()) ? SEPARATOR : (() -> separator);
/*  98 */     List<PathContainer.Element> elements = new ArrayList<>();
/*     */     
/* 100 */     if (path.length() > 0 && path.startsWith(separator)) {
/* 101 */       begin = separator.length();
/* 102 */       elements.add(separatorElement);
/*     */     } else {
/*     */       
/* 105 */       begin = 0;
/*     */     } 
/* 107 */     while (begin < path.length()) {
/* 108 */       int end = path.indexOf(separator, begin);
/* 109 */       String segment = (end != -1) ? path.substring(begin, end) : path.substring(begin);
/* 110 */       if (!segment.equals("")) {
/* 111 */         elements.add(parsePathSegment(segment));
/*     */       }
/* 113 */       if (end == -1) {
/*     */         break;
/*     */       }
/* 116 */       elements.add(separatorElement);
/* 117 */       begin = end + separator.length();
/*     */     } 
/* 119 */     return new DefaultPathContainer(path, elements);
/*     */   }
/*     */   
/*     */   private static PathContainer.PathSegment parsePathSegment(String segment) {
/* 123 */     Charset charset = StandardCharsets.UTF_8;
/* 124 */     int index = segment.indexOf(';');
/* 125 */     if (index == -1) {
/* 126 */       String str = StringUtils.uriDecode(segment, charset);
/* 127 */       return new DefaultPathSegment(segment, str, EMPTY_MAP);
/*     */     } 
/*     */     
/* 130 */     String valueToMatch = StringUtils.uriDecode(segment.substring(0, index), charset);
/* 131 */     String pathParameterContent = segment.substring(index);
/* 132 */     MultiValueMap<String, String> parameters = parsePathParams(pathParameterContent, charset);
/* 133 */     return new DefaultPathSegment(segment, valueToMatch, parameters);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MultiValueMap<String, String> parsePathParams(String input, Charset charset) {
/* 138 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 139 */     int begin = 1;
/* 140 */     while (begin < input.length()) {
/* 141 */       int end = input.indexOf(';', begin);
/* 142 */       String param = (end != -1) ? input.substring(begin, end) : input.substring(begin);
/* 143 */       parsePathParamValues(param, charset, (MultiValueMap<String, String>)linkedMultiValueMap);
/* 144 */       if (end == -1) {
/*     */         break;
/*     */       }
/* 147 */       begin = end + 1;
/*     */     } 
/* 149 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*     */   }
/*     */   
/*     */   private static void parsePathParamValues(String input, Charset charset, MultiValueMap<String, String> output) {
/* 153 */     if (StringUtils.hasText(input)) {
/* 154 */       int index = input.indexOf('=');
/* 155 */       if (index != -1) {
/* 156 */         String name = input.substring(0, index);
/* 157 */         String value = input.substring(index + 1);
/* 158 */         for (String v : StringUtils.commaDelimitedListToStringArray(value)) {
/* 159 */           name = StringUtils.uriDecode(name, charset);
/* 160 */           if (StringUtils.hasText(name)) {
/* 161 */             output.add(name, StringUtils.uriDecode(v, charset));
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 166 */         String name = StringUtils.uriDecode(input, charset);
/* 167 */         if (StringUtils.hasText(name)) {
/* 168 */           output.add(input, "");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static PathContainer subPath(PathContainer container, int fromIndex, int toIndex) {
/* 175 */     List<PathContainer.Element> elements = container.elements();
/* 176 */     if (fromIndex == 0 && toIndex == elements.size()) {
/* 177 */       return container;
/*     */     }
/* 179 */     if (fromIndex == toIndex) {
/* 180 */       return EMPTY_PATH;
/*     */     }
/*     */     
/* 183 */     Assert.isTrue((fromIndex >= 0 && fromIndex < elements.size()), () -> "Invalid fromIndex: " + fromIndex);
/* 184 */     Assert.isTrue((toIndex >= 0 && toIndex <= elements.size()), () -> "Invalid toIndex: " + toIndex);
/* 185 */     Assert.isTrue((fromIndex < toIndex), () -> "fromIndex: " + fromIndex + " should be < toIndex " + toIndex);
/*     */     
/* 187 */     List<PathContainer.Element> subList = elements.subList(fromIndex, toIndex);
/* 188 */     String path = subList.stream().map(PathContainer.Element::value).collect(Collectors.joining(""));
/* 189 */     return new DefaultPathContainer(path, subList);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class DefaultPathSegment
/*     */     implements PathContainer.PathSegment
/*     */   {
/*     */     private final String value;
/*     */     
/*     */     private final String valueToMatch;
/*     */     
/*     */     private final char[] valueToMatchAsChars;
/*     */     private final MultiValueMap<String, String> parameters;
/*     */     
/*     */     public DefaultPathSegment(String value, String valueToMatch, MultiValueMap<String, String> params) {
/* 204 */       Assert.isTrue(!value.contains("/"), () -> "Invalid path segment value: " + value);
/* 205 */       this.value = value;
/* 206 */       this.valueToMatch = valueToMatch;
/* 207 */       this.valueToMatchAsChars = valueToMatch.toCharArray();
/* 208 */       this.parameters = CollectionUtils.unmodifiableMultiValueMap(params);
/*     */     }
/*     */ 
/*     */     
/*     */     public String value() {
/* 213 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public String valueToMatch() {
/* 218 */       return this.valueToMatch;
/*     */     }
/*     */ 
/*     */     
/*     */     public char[] valueToMatchAsChars() {
/* 223 */       return this.valueToMatchAsChars;
/*     */     }
/*     */ 
/*     */     
/*     */     public MultiValueMap<String, String> parameters() {
/* 228 */       return this.parameters;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object other) {
/* 233 */       if (this == other) {
/* 234 */         return true;
/*     */       }
/* 236 */       if (other == null || getClass() != other.getClass()) {
/* 237 */         return false;
/*     */       }
/* 239 */       return this.value.equals(((DefaultPathSegment)other).value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 244 */       return this.value.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 248 */       return "[value='" + this.value + "']";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/DefaultPathContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */