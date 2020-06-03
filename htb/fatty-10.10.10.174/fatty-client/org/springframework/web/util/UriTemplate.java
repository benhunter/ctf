/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class UriTemplate
/*     */   implements Serializable
/*     */ {
/*     */   private final String uriTemplate;
/*     */   private final UriComponents uriComponents;
/*     */   private final List<String> variableNames;
/*     */   private final Pattern matchPattern;
/*     */   
/*     */   public UriTemplate(String uriTemplate) {
/*  64 */     Assert.hasText(uriTemplate, "'uriTemplate' must not be null");
/*  65 */     this.uriTemplate = uriTemplate;
/*  66 */     this.uriComponents = UriComponentsBuilder.fromUriString(uriTemplate).build();
/*     */     
/*  68 */     TemplateInfo info = TemplateInfo.parse(uriTemplate);
/*  69 */     this.variableNames = Collections.unmodifiableList(info.getVariableNames());
/*  70 */     this.matchPattern = info.getMatchPattern();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getVariableNames() {
/*  79 */     return this.variableNames;
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
/*     */   public URI expand(Map<String, ?> uriVariables) {
/* 100 */     UriComponents expandedComponents = this.uriComponents.expand(uriVariables);
/* 101 */     UriComponents encodedComponents = expandedComponents.encode();
/* 102 */     return encodedComponents.toUri();
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
/*     */   public URI expand(Object... uriVariableValues) {
/* 120 */     UriComponents expandedComponents = this.uriComponents.expand(uriVariableValues);
/* 121 */     UriComponents encodedComponents = expandedComponents.encode();
/* 122 */     return encodedComponents.toUri();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(@Nullable String uri) {
/* 131 */     if (uri == null) {
/* 132 */       return false;
/*     */     }
/* 134 */     Matcher matcher = this.matchPattern.matcher(uri);
/* 135 */     return matcher.matches();
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
/*     */   public Map<String, String> match(String uri) {
/* 151 */     Assert.notNull(uri, "'uri' must not be null");
/* 152 */     Map<String, String> result = new LinkedHashMap<>(this.variableNames.size());
/* 153 */     Matcher matcher = this.matchPattern.matcher(uri);
/* 154 */     if (matcher.find()) {
/* 155 */       for (int i = 1; i <= matcher.groupCount(); i++) {
/* 156 */         String name = this.variableNames.get(i - 1);
/* 157 */         String value = matcher.group(i);
/* 158 */         result.put(name, value);
/*     */       } 
/*     */     }
/* 161 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 166 */     return this.uriTemplate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class TemplateInfo
/*     */   {
/*     */     private final List<String> variableNames;
/*     */ 
/*     */     
/*     */     private final Pattern pattern;
/*     */ 
/*     */     
/*     */     private TemplateInfo(List<String> vars, Pattern pattern) {
/* 180 */       this.variableNames = vars;
/* 181 */       this.pattern = pattern;
/*     */     }
/*     */     
/*     */     public List<String> getVariableNames() {
/* 185 */       return this.variableNames;
/*     */     }
/*     */     
/*     */     public Pattern getMatchPattern() {
/* 189 */       return this.pattern;
/*     */     }
/*     */     
/*     */     public static TemplateInfo parse(String uriTemplate) {
/* 193 */       int level = 0;
/* 194 */       List<String> variableNames = new ArrayList<>();
/* 195 */       StringBuilder pattern = new StringBuilder();
/* 196 */       StringBuilder builder = new StringBuilder();
/* 197 */       for (int i = 0; i < uriTemplate.length(); i++) {
/* 198 */         char c = uriTemplate.charAt(i);
/* 199 */         if (c == '{') {
/* 200 */           level++;
/* 201 */           if (level == 1) {
/*     */             
/* 203 */             pattern.append(quote(builder));
/* 204 */             builder = new StringBuilder();
/*     */             
/*     */             continue;
/*     */           } 
/*     */         } else {
/* 209 */           level--;
/* 210 */           if (c == '}' && level == 0) {
/*     */             
/* 212 */             String variable = builder.toString();
/* 213 */             int idx = variable.indexOf(':');
/* 214 */             if (idx == -1) {
/* 215 */               pattern.append("([^/]*)");
/* 216 */               variableNames.add(variable);
/*     */             } else {
/*     */               
/* 219 */               if (idx + 1 == variable.length()) {
/* 220 */                 throw new IllegalArgumentException("No custom regular expression specified after ':' in \"" + variable + "\"");
/*     */               }
/*     */               
/* 223 */               String regex = variable.substring(idx + 1, variable.length());
/* 224 */               pattern.append('(');
/* 225 */               pattern.append(regex);
/* 226 */               pattern.append(')');
/* 227 */               variableNames.add(variable.substring(0, idx));
/*     */             } 
/* 229 */             builder = new StringBuilder();
/*     */             continue;
/*     */           } 
/*     */         } 
/* 233 */         builder.append(c); continue;
/*     */       } 
/* 235 */       if (builder.length() > 0) {
/* 236 */         pattern.append(quote(builder));
/*     */       }
/* 238 */       return new TemplateInfo(variableNames, Pattern.compile(pattern.toString()));
/*     */     }
/*     */     
/*     */     private static String quote(StringBuilder builder) {
/* 242 */       return (builder.length() > 0) ? Pattern.quote(builder.toString()) : "";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/UriTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */