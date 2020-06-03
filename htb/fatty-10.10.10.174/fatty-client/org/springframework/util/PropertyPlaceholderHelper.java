/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class PropertyPlaceholderHelper
/*     */ {
/*  42 */   private static final Log logger = LogFactory.getLog(PropertyPlaceholderHelper.class);
/*     */   
/*  44 */   private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<>(4);
/*     */   
/*     */   static {
/*  47 */     wellKnownSimplePrefixes.put("}", "{");
/*  48 */     wellKnownSimplePrefixes.put("]", "[");
/*  49 */     wellKnownSimplePrefixes.put(")", "(");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final String placeholderPrefix;
/*     */ 
/*     */   
/*     */   private final String placeholderSuffix;
/*     */ 
/*     */   
/*     */   private final String simplePrefix;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final String valueSeparator;
/*     */ 
/*     */   
/*     */   private final boolean ignoreUnresolvablePlaceholders;
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix) {
/*  72 */     this(placeholderPrefix, placeholderSuffix, null, true);
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
/*     */   public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix, @Nullable String valueSeparator, boolean ignoreUnresolvablePlaceholders) {
/*  87 */     Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
/*  88 */     Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
/*  89 */     this.placeholderPrefix = placeholderPrefix;
/*  90 */     this.placeholderSuffix = placeholderSuffix;
/*  91 */     String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
/*  92 */     if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
/*  93 */       this.simplePrefix = simplePrefixForSuffix;
/*     */     } else {
/*     */       
/*  96 */       this.simplePrefix = this.placeholderPrefix;
/*     */     } 
/*  98 */     this.valueSeparator = valueSeparator;
/*  99 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
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
/*     */   public String replacePlaceholders(String value, Properties properties) {
/* 111 */     Assert.notNull(properties, "'properties' must not be null");
/* 112 */     return replacePlaceholders(value, properties::getProperty);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String replacePlaceholders(String value, PlaceholderResolver placeholderResolver) {
/* 123 */     Assert.notNull(value, "'value' must not be null");
/* 124 */     return parseStringValue(value, placeholderResolver, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String parseStringValue(String value, PlaceholderResolver placeholderResolver, @Nullable Set<String> visitedPlaceholders) {
/* 130 */     int startIndex = value.indexOf(this.placeholderPrefix);
/* 131 */     if (startIndex == -1) {
/* 132 */       return value;
/*     */     }
/*     */     
/* 135 */     StringBuilder result = new StringBuilder(value);
/* 136 */     while (startIndex != -1) {
/* 137 */       int endIndex = findPlaceholderEndIndex(result, startIndex);
/* 138 */       if (endIndex != -1) {
/* 139 */         String placeholder = result.substring(startIndex + this.placeholderPrefix.length(), endIndex);
/* 140 */         String originalPlaceholder = placeholder;
/* 141 */         if (visitedPlaceholders == null) {
/* 142 */           visitedPlaceholders = new HashSet<>(4);
/*     */         }
/* 144 */         if (!visitedPlaceholders.add(originalPlaceholder)) {
/* 145 */           throw new IllegalArgumentException("Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
/*     */         }
/*     */ 
/*     */         
/* 149 */         placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders);
/*     */         
/* 151 */         String propVal = placeholderResolver.resolvePlaceholder(placeholder);
/* 152 */         if (propVal == null && this.valueSeparator != null) {
/* 153 */           int separatorIndex = placeholder.indexOf(this.valueSeparator);
/* 154 */           if (separatorIndex != -1) {
/* 155 */             String actualPlaceholder = placeholder.substring(0, separatorIndex);
/* 156 */             String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
/* 157 */             propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
/* 158 */             if (propVal == null) {
/* 159 */               propVal = defaultValue;
/*     */             }
/*     */           } 
/*     */         } 
/* 163 */         if (propVal != null) {
/*     */ 
/*     */           
/* 166 */           propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
/* 167 */           result.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
/* 168 */           if (logger.isTraceEnabled()) {
/* 169 */             logger.trace("Resolved placeholder '" + placeholder + "'");
/*     */           }
/* 171 */           startIndex = result.indexOf(this.placeholderPrefix, startIndex + propVal.length());
/*     */         }
/* 173 */         else if (this.ignoreUnresolvablePlaceholders) {
/*     */           
/* 175 */           startIndex = result.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
/*     */         } else {
/*     */           
/* 178 */           throw new IllegalArgumentException("Could not resolve placeholder '" + placeholder + "' in value \"" + value + "\"");
/*     */         } 
/*     */         
/* 181 */         visitedPlaceholders.remove(originalPlaceholder);
/*     */         continue;
/*     */       } 
/* 184 */       startIndex = -1;
/*     */     } 
/*     */     
/* 187 */     return result.toString();
/*     */   }
/*     */   
/*     */   private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
/* 191 */     int index = startIndex + this.placeholderPrefix.length();
/* 192 */     int withinNestedPlaceholder = 0;
/* 193 */     while (index < buf.length()) {
/* 194 */       if (StringUtils.substringMatch(buf, index, this.placeholderSuffix)) {
/* 195 */         if (withinNestedPlaceholder > 0) {
/* 196 */           withinNestedPlaceholder--;
/* 197 */           index += this.placeholderSuffix.length();
/*     */           continue;
/*     */         } 
/* 200 */         return index;
/*     */       } 
/*     */       
/* 203 */       if (StringUtils.substringMatch(buf, index, this.simplePrefix)) {
/* 204 */         withinNestedPlaceholder++;
/* 205 */         index += this.simplePrefix.length();
/*     */         continue;
/*     */       } 
/* 208 */       index++;
/*     */     } 
/*     */     
/* 211 */     return -1;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface PlaceholderResolver {
/*     */     @Nullable
/*     */     String resolvePlaceholder(String param1String);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/PropertyPlaceholderHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */