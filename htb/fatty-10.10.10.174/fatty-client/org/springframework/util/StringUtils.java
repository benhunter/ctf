/*      */ package org.springframework.util;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import org.springframework.lang.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class StringUtils
/*      */ {
/*      */   private static final String FOLDER_SEPARATOR = "/";
/*      */   private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
/*      */   private static final String TOP_PATH = "..";
/*      */   private static final String CURRENT_PATH = ".";
/*      */   private static final char EXTENSION_SEPARATOR = '.';
/*      */   
/*      */   public static boolean isEmpty(@Nullable Object str) {
/*   94 */     return (str == null || "".equals(str));
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
/*      */   public static boolean hasLength(@Nullable CharSequence str) {
/*  114 */     return (str != null && str.length() > 0);
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
/*      */   public static boolean hasLength(@Nullable String str) {
/*  127 */     return (str != null && !str.isEmpty());
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
/*      */   public static boolean hasText(@Nullable CharSequence str) {
/*  150 */     return (str != null && str.length() > 0 && containsText(str));
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
/*      */   public static boolean hasText(@Nullable String str) {
/*  166 */     return (str != null && !str.isEmpty() && containsText(str));
/*      */   }
/*      */   
/*      */   private static boolean containsText(CharSequence str) {
/*  170 */     int strLen = str.length();
/*  171 */     for (int i = 0; i < strLen; i++) {
/*  172 */       if (!Character.isWhitespace(str.charAt(i))) {
/*  173 */         return true;
/*      */       }
/*      */     } 
/*  176 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean containsWhitespace(@Nullable CharSequence str) {
/*  187 */     if (!hasLength(str)) {
/*  188 */       return false;
/*      */     }
/*      */     
/*  191 */     int strLen = str.length();
/*  192 */     for (int i = 0; i < strLen; i++) {
/*  193 */       if (Character.isWhitespace(str.charAt(i))) {
/*  194 */         return true;
/*      */       }
/*      */     } 
/*  197 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean containsWhitespace(@Nullable String str) {
/*  208 */     return containsWhitespace(str);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimWhitespace(String str) {
/*  218 */     if (!hasLength(str)) {
/*  219 */       return str;
/*      */     }
/*      */     
/*  222 */     int beginIndex = 0;
/*  223 */     int endIndex = str.length() - 1;
/*      */     
/*  225 */     while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
/*  226 */       beginIndex++;
/*      */     }
/*      */     
/*  229 */     while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
/*  230 */       endIndex--;
/*      */     }
/*      */     
/*  233 */     return str.substring(beginIndex, endIndex + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimAllWhitespace(String str) {
/*  244 */     if (!hasLength(str)) {
/*  245 */       return str;
/*      */     }
/*      */     
/*  248 */     int len = str.length();
/*  249 */     StringBuilder sb = new StringBuilder(str.length());
/*  250 */     for (int i = 0; i < len; i++) {
/*  251 */       char c = str.charAt(i);
/*  252 */       if (!Character.isWhitespace(c)) {
/*  253 */         sb.append(c);
/*      */       }
/*      */     } 
/*  256 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimLeadingWhitespace(String str) {
/*  266 */     if (!hasLength(str)) {
/*  267 */       return str;
/*      */     }
/*      */     
/*  270 */     StringBuilder sb = new StringBuilder(str);
/*  271 */     while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
/*  272 */       sb.deleteCharAt(0);
/*      */     }
/*  274 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimTrailingWhitespace(String str) {
/*  284 */     if (!hasLength(str)) {
/*  285 */       return str;
/*      */     }
/*      */     
/*  288 */     StringBuilder sb = new StringBuilder(str);
/*  289 */     while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
/*  290 */       sb.deleteCharAt(sb.length() - 1);
/*      */     }
/*  292 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimLeadingCharacter(String str, char leadingCharacter) {
/*  302 */     if (!hasLength(str)) {
/*  303 */       return str;
/*      */     }
/*      */     
/*  306 */     StringBuilder sb = new StringBuilder(str);
/*  307 */     while (sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
/*  308 */       sb.deleteCharAt(0);
/*      */     }
/*  310 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimTrailingCharacter(String str, char trailingCharacter) {
/*  320 */     if (!hasLength(str)) {
/*  321 */       return str;
/*      */     }
/*      */     
/*  324 */     StringBuilder sb = new StringBuilder(str);
/*  325 */     while (sb.length() > 0 && sb.charAt(sb.length() - 1) == trailingCharacter) {
/*  326 */       sb.deleteCharAt(sb.length() - 1);
/*      */     }
/*  328 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean startsWithIgnoreCase(@Nullable String str, @Nullable String prefix) {
/*  339 */     return (str != null && prefix != null && str.length() >= prefix.length() && str
/*  340 */       .regionMatches(true, 0, prefix, 0, prefix.length()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean endsWithIgnoreCase(@Nullable String str, @Nullable String suffix) {
/*  351 */     return (str != null && suffix != null && str.length() >= suffix.length() && str
/*  352 */       .regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
/*  363 */     if (index + substring.length() > str.length()) {
/*  364 */       return false;
/*      */     }
/*  366 */     for (int i = 0; i < substring.length(); i++) {
/*  367 */       if (str.charAt(index + i) != substring.charAt(i)) {
/*  368 */         return false;
/*      */       }
/*      */     } 
/*  371 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int countOccurrencesOf(String str, String sub) {
/*  380 */     if (!hasLength(str) || !hasLength(sub)) {
/*  381 */       return 0;
/*      */     }
/*      */     
/*  384 */     int count = 0;
/*  385 */     int pos = 0;
/*      */     int idx;
/*  387 */     while ((idx = str.indexOf(sub, pos)) != -1) {
/*  388 */       count++;
/*  389 */       pos = idx + sub.length();
/*      */     } 
/*  391 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replace(String inString, String oldPattern, @Nullable String newPattern) {
/*  402 */     if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
/*  403 */       return inString;
/*      */     }
/*  405 */     int index = inString.indexOf(oldPattern);
/*  406 */     if (index == -1)
/*      */     {
/*  408 */       return inString;
/*      */     }
/*      */     
/*  411 */     int capacity = inString.length();
/*  412 */     if (newPattern.length() > oldPattern.length()) {
/*  413 */       capacity += 16;
/*      */     }
/*  415 */     StringBuilder sb = new StringBuilder(capacity);
/*      */     
/*  417 */     int pos = 0;
/*  418 */     int patLen = oldPattern.length();
/*  419 */     while (index >= 0) {
/*  420 */       sb.append(inString.substring(pos, index));
/*  421 */       sb.append(newPattern);
/*  422 */       pos = index + patLen;
/*  423 */       index = inString.indexOf(oldPattern, pos);
/*      */     } 
/*      */ 
/*      */     
/*  427 */     sb.append(inString.substring(pos));
/*  428 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String delete(String inString, String pattern) {
/*  438 */     return replace(inString, pattern, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String deleteAny(String inString, @Nullable String charsToDelete) {
/*  449 */     if (!hasLength(inString) || !hasLength(charsToDelete)) {
/*  450 */       return inString;
/*      */     }
/*      */     
/*  453 */     StringBuilder sb = new StringBuilder(inString.length());
/*  454 */     for (int i = 0; i < inString.length(); i++) {
/*  455 */       char c = inString.charAt(i);
/*  456 */       if (charsToDelete.indexOf(c) == -1) {
/*  457 */         sb.append(c);
/*      */       }
/*      */     } 
/*  460 */     return sb.toString();
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
/*      */   public static String quote(@Nullable String str) {
/*  476 */     return (str != null) ? ("'" + str + "'") : null;
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
/*      */   public static Object quoteIfString(@Nullable Object obj) {
/*  488 */     return (obj instanceof String) ? quote((String)obj) : obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String unqualify(String qualifiedName) {
/*  497 */     return unqualify(qualifiedName, '.');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String unqualify(String qualifiedName, char separator) {
/*  507 */     return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String capitalize(String str) {
/*  518 */     return changeFirstCharacterCase(str, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String uncapitalize(String str) {
/*  529 */     return changeFirstCharacterCase(str, false);
/*      */   }
/*      */   private static String changeFirstCharacterCase(String str, boolean capitalize) {
/*      */     char updatedChar;
/*  533 */     if (!hasLength(str)) {
/*  534 */       return str;
/*      */     }
/*      */     
/*  537 */     char baseChar = str.charAt(0);
/*      */     
/*  539 */     if (capitalize) {
/*  540 */       updatedChar = Character.toUpperCase(baseChar);
/*      */     } else {
/*      */       
/*  543 */       updatedChar = Character.toLowerCase(baseChar);
/*      */     } 
/*  545 */     if (baseChar == updatedChar) {
/*  546 */       return str;
/*      */     }
/*      */     
/*  549 */     char[] chars = str.toCharArray();
/*  550 */     chars[0] = updatedChar;
/*  551 */     return new String(chars, 0, chars.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static String getFilename(@Nullable String path) {
/*  562 */     if (path == null) {
/*  563 */       return null;
/*      */     }
/*      */     
/*  566 */     int separatorIndex = path.lastIndexOf("/");
/*  567 */     return (separatorIndex != -1) ? path.substring(separatorIndex + 1) : path;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static String getFilenameExtension(@Nullable String path) {
/*  578 */     if (path == null) {
/*  579 */       return null;
/*      */     }
/*      */     
/*  582 */     int extIndex = path.lastIndexOf('.');
/*  583 */     if (extIndex == -1) {
/*  584 */       return null;
/*      */     }
/*      */     
/*  587 */     int folderIndex = path.lastIndexOf("/");
/*  588 */     if (folderIndex > extIndex) {
/*  589 */       return null;
/*      */     }
/*      */     
/*  592 */     return path.substring(extIndex + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String stripFilenameExtension(String path) {
/*  602 */     int extIndex = path.lastIndexOf('.');
/*  603 */     if (extIndex == -1) {
/*  604 */       return path;
/*      */     }
/*      */     
/*  607 */     int folderIndex = path.lastIndexOf("/");
/*  608 */     if (folderIndex > extIndex) {
/*  609 */       return path;
/*      */     }
/*      */     
/*  612 */     return path.substring(0, extIndex);
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
/*      */   public static String applyRelativePath(String path, String relativePath) {
/*  624 */     int separatorIndex = path.lastIndexOf("/");
/*  625 */     if (separatorIndex != -1) {
/*  626 */       String newPath = path.substring(0, separatorIndex);
/*  627 */       if (!relativePath.startsWith("/")) {
/*  628 */         newPath = newPath + "/";
/*      */       }
/*  630 */       return newPath + relativePath;
/*      */     } 
/*      */     
/*  633 */     return relativePath;
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
/*      */   public static String cleanPath(String path) {
/*  646 */     if (!hasLength(path)) {
/*  647 */       return path;
/*      */     }
/*  649 */     String pathToUse = replace(path, "\\", "/");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  655 */     int prefixIndex = pathToUse.indexOf(':');
/*  656 */     String prefix = "";
/*  657 */     if (prefixIndex != -1) {
/*  658 */       prefix = pathToUse.substring(0, prefixIndex + 1);
/*  659 */       if (prefix.contains("/")) {
/*  660 */         prefix = "";
/*      */       } else {
/*      */         
/*  663 */         pathToUse = pathToUse.substring(prefixIndex + 1);
/*      */       } 
/*      */     } 
/*  666 */     if (pathToUse.startsWith("/")) {
/*  667 */       prefix = prefix + "/";
/*  668 */       pathToUse = pathToUse.substring(1);
/*      */     } 
/*      */     
/*  671 */     String[] pathArray = delimitedListToStringArray(pathToUse, "/");
/*  672 */     LinkedList<String> pathElements = new LinkedList<>();
/*  673 */     int tops = 0;
/*      */     int i;
/*  675 */     for (i = pathArray.length - 1; i >= 0; i--) {
/*  676 */       String element = pathArray[i];
/*  677 */       if (!".".equals(element))
/*      */       {
/*      */         
/*  680 */         if ("..".equals(element)) {
/*      */           
/*  682 */           tops++;
/*      */         
/*      */         }
/*  685 */         else if (tops > 0) {
/*      */           
/*  687 */           tops--;
/*      */         }
/*      */         else {
/*      */           
/*  691 */           pathElements.add(0, element);
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  697 */     for (i = 0; i < tops; i++) {
/*  698 */       pathElements.add(0, "..");
/*      */     }
/*      */     
/*  701 */     if (pathElements.size() == 1 && "".equals(pathElements.getLast()) && !prefix.endsWith("/")) {
/*  702 */       pathElements.add(0, ".");
/*      */     }
/*      */     
/*  705 */     return prefix + collectionToDelimitedString(pathElements, "/");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean pathEquals(String path1, String path2) {
/*  715 */     return cleanPath(path1).equals(cleanPath(path2));
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
/*      */   public static String uriDecode(String source, Charset charset) {
/*  734 */     int length = source.length();
/*  735 */     if (length == 0) {
/*  736 */       return source;
/*      */     }
/*  738 */     Assert.notNull(charset, "Charset must not be null");
/*      */     
/*  740 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
/*  741 */     boolean changed = false;
/*  742 */     for (int i = 0; i < length; i++) {
/*  743 */       int ch = source.charAt(i);
/*  744 */       if (ch == 37) {
/*  745 */         if (i + 2 < length) {
/*  746 */           char hex1 = source.charAt(i + 1);
/*  747 */           char hex2 = source.charAt(i + 2);
/*  748 */           int u = Character.digit(hex1, 16);
/*  749 */           int l = Character.digit(hex2, 16);
/*  750 */           if (u == -1 || l == -1) {
/*  751 */             throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/*      */           }
/*  753 */           bos.write((char)((u << 4) + l));
/*  754 */           i += 2;
/*  755 */           changed = true;
/*      */         } else {
/*      */           
/*  758 */           throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/*      */         } 
/*      */       } else {
/*      */         
/*  762 */         bos.write(ch);
/*      */       } 
/*      */     } 
/*  765 */     return changed ? new String(bos.toByteArray(), charset) : source;
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
/*      */   @Nullable
/*      */   public static Locale parseLocale(String localeValue) {
/*  783 */     String[] tokens = tokenizeLocaleSource(localeValue);
/*  784 */     if (tokens.length == 1) {
/*  785 */       validateLocalePart(localeValue);
/*  786 */       Locale resolved = Locale.forLanguageTag(localeValue);
/*  787 */       if (resolved.getLanguage().length() > 0) {
/*  788 */         return resolved;
/*      */       }
/*      */     } 
/*  791 */     return parseLocaleTokens(localeValue, tokens);
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
/*      */   @Nullable
/*      */   public static Locale parseLocaleString(String localeString) {
/*  810 */     return parseLocaleTokens(localeString, tokenizeLocaleSource(localeString));
/*      */   }
/*      */   
/*      */   private static String[] tokenizeLocaleSource(String localeSource) {
/*  814 */     return tokenizeToStringArray(localeSource, "_ ", false, false);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private static Locale parseLocaleTokens(String localeString, String[] tokens) {
/*  819 */     String language = (tokens.length > 0) ? tokens[0] : "";
/*  820 */     String country = (tokens.length > 1) ? tokens[1] : "";
/*  821 */     validateLocalePart(language);
/*  822 */     validateLocalePart(country);
/*      */     
/*  824 */     String variant = "";
/*  825 */     if (tokens.length > 2) {
/*      */ 
/*      */       
/*  828 */       int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
/*      */       
/*  830 */       variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
/*  831 */       if (variant.startsWith("_")) {
/*  832 */         variant = trimLeadingCharacter(variant, '_');
/*      */       }
/*      */     } 
/*      */     
/*  836 */     if (variant.isEmpty() && country.startsWith("#")) {
/*  837 */       variant = country;
/*  838 */       country = "";
/*      */     } 
/*      */     
/*  841 */     return (language.length() > 0) ? new Locale(language, country, variant) : null;
/*      */   }
/*      */   
/*      */   private static void validateLocalePart(String localePart) {
/*  845 */     for (int i = 0; i < localePart.length(); i++) {
/*  846 */       char ch = localePart.charAt(i);
/*  847 */       if (ch != ' ' && ch != '_' && ch != '-' && ch != '#' && !Character.isLetterOrDigit(ch)) {
/*  848 */         throw new IllegalArgumentException("Locale part \"" + localePart + "\" contains invalid characters");
/*      */       }
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
/*      */   @Deprecated
/*      */   public static String toLanguageTag(Locale locale) {
/*  863 */     return locale.getLanguage() + (hasText(locale.getCountry()) ? ("-" + locale.getCountry()) : "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static TimeZone parseTimeZoneString(String timeZoneString) {
/*  874 */     TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
/*  875 */     if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT"))
/*      */     {
/*  877 */       throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
/*      */     }
/*  879 */     return timeZone;
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
/*      */   public static String[] toStringArray(@Nullable Collection<String> collection) {
/*  895 */     return (collection != null) ? collection.<String>toArray(new String[0]) : new String[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] toStringArray(@Nullable Enumeration<String> enumeration) {
/*  906 */     return (enumeration != null) ? toStringArray(Collections.list(enumeration)) : new String[0];
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
/*      */   public static String[] addStringToArray(@Nullable String[] array, String str) {
/*  918 */     if (ObjectUtils.isEmpty((Object[])array)) {
/*  919 */       return new String[] { str };
/*      */     }
/*      */     
/*  922 */     String[] newArr = new String[array.length + 1];
/*  923 */     System.arraycopy(array, 0, newArr, 0, array.length);
/*  924 */     newArr[array.length] = str;
/*  925 */     return newArr;
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
/*      */   public static String[] concatenateStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
/*  938 */     if (ObjectUtils.isEmpty((Object[])array1)) {
/*  939 */       return array2;
/*      */     }
/*  941 */     if (ObjectUtils.isEmpty((Object[])array2)) {
/*  942 */       return array1;
/*      */     }
/*      */     
/*  945 */     String[] newArr = new String[array1.length + array2.length];
/*  946 */     System.arraycopy(array1, 0, newArr, 0, array1.length);
/*  947 */     System.arraycopy(array2, 0, newArr, array1.length, array2.length);
/*  948 */     return newArr;
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
/*      */   @Deprecated
/*      */   @Nullable
/*      */   public static String[] mergeStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
/*  966 */     if (ObjectUtils.isEmpty((Object[])array1)) {
/*  967 */       return array2;
/*      */     }
/*  969 */     if (ObjectUtils.isEmpty((Object[])array2)) {
/*  970 */       return array1;
/*      */     }
/*      */     
/*  973 */     List<String> result = new ArrayList<>();
/*  974 */     result.addAll(Arrays.asList(array1));
/*  975 */     for (String str : array2) {
/*  976 */       if (!result.contains(str)) {
/*  977 */         result.add(str);
/*      */       }
/*      */     } 
/*  980 */     return toStringArray(result);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] sortStringArray(String[] array) {
/*  989 */     if (ObjectUtils.isEmpty((Object[])array)) {
/*  990 */       return array;
/*      */     }
/*      */     
/*  993 */     Arrays.sort((Object[])array);
/*  994 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] trimArrayElements(String[] array) {
/* 1004 */     if (ObjectUtils.isEmpty((Object[])array)) {
/* 1005 */       return array;
/*      */     }
/*      */     
/* 1008 */     String[] result = new String[array.length];
/* 1009 */     for (int i = 0; i < array.length; i++) {
/* 1010 */       String element = array[i];
/* 1011 */       result[i] = (element != null) ? element.trim() : null;
/*      */     } 
/* 1013 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] removeDuplicateStrings(String[] array) {
/* 1023 */     if (ObjectUtils.isEmpty((Object[])array)) {
/* 1024 */       return array;
/*      */     }
/*      */     
/* 1027 */     Set<String> set = new LinkedHashSet<>(Arrays.asList(array));
/* 1028 */     return toStringArray(set);
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
/*      */   public static String[] split(@Nullable String toSplit, @Nullable String delimiter) {
/* 1042 */     if (!hasLength(toSplit) || !hasLength(delimiter)) {
/* 1043 */       return null;
/*      */     }
/* 1045 */     int offset = toSplit.indexOf(delimiter);
/* 1046 */     if (offset < 0) {
/* 1047 */       return null;
/*      */     }
/*      */     
/* 1050 */     String beforeDelimiter = toSplit.substring(0, offset);
/* 1051 */     String afterDelimiter = toSplit.substring(offset + delimiter.length());
/* 1052 */     return new String[] { beforeDelimiter, afterDelimiter };
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
/*      */   @Nullable
/*      */   public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
/* 1067 */     return splitArrayElementsIntoProperties(array, delimiter, null);
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
/*      */   @Nullable
/*      */   public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, @Nullable String charsToDelete) {
/* 1088 */     if (ObjectUtils.isEmpty((Object[])array)) {
/* 1089 */       return null;
/*      */     }
/*      */     
/* 1092 */     Properties result = new Properties();
/* 1093 */     for (String element : array) {
/* 1094 */       if (charsToDelete != null) {
/* 1095 */         element = deleteAny(element, charsToDelete);
/*      */       }
/* 1097 */       String[] splittedElement = split(element, delimiter);
/* 1098 */       if (splittedElement != null)
/*      */       {
/*      */         
/* 1101 */         result.setProperty(splittedElement[0].trim(), splittedElement[1].trim()); } 
/*      */     } 
/* 1103 */     return result;
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
/*      */   public static String[] tokenizeToStringArray(@Nullable String str, String delimiters) {
/* 1123 */     return tokenizeToStringArray(str, delimiters, true, true);
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
/*      */   public static String[] tokenizeToStringArray(@Nullable String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
/* 1148 */     if (str == null) {
/* 1149 */       return new String[0];
/*      */     }
/*      */     
/* 1152 */     StringTokenizer st = new StringTokenizer(str, delimiters);
/* 1153 */     List<String> tokens = new ArrayList<>();
/* 1154 */     while (st.hasMoreTokens()) {
/* 1155 */       String token = st.nextToken();
/* 1156 */       if (trimTokens) {
/* 1157 */         token = token.trim();
/*      */       }
/* 1159 */       if (!ignoreEmptyTokens || token.length() > 0) {
/* 1160 */         tokens.add(token);
/*      */       }
/*      */     } 
/* 1163 */     return toStringArray(tokens);
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
/*      */   public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter) {
/* 1180 */     return delimitedListToStringArray(str, delimiter, null);
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
/*      */   public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter, @Nullable String charsToDelete) {
/* 1201 */     if (str == null) {
/* 1202 */       return new String[0];
/*      */     }
/* 1204 */     if (delimiter == null) {
/* 1205 */       return new String[] { str };
/*      */     }
/*      */     
/* 1208 */     List<String> result = new ArrayList<>();
/* 1209 */     if (delimiter.isEmpty()) {
/* 1210 */       for (int i = 0; i < str.length(); i++) {
/* 1211 */         result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
/*      */       }
/*      */     } else {
/*      */       
/* 1215 */       int pos = 0;
/*      */       int delPos;
/* 1217 */       while ((delPos = str.indexOf(delimiter, pos)) != -1) {
/* 1218 */         result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
/* 1219 */         pos = delPos + delimiter.length();
/*      */       } 
/* 1221 */       if (str.length() > 0 && pos <= str.length())
/*      */       {
/* 1223 */         result.add(deleteAny(str.substring(pos), charsToDelete));
/*      */       }
/*      */     } 
/* 1226 */     return toStringArray(result);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] commaDelimitedListToStringArray(@Nullable String str) {
/* 1236 */     return delimitedListToStringArray(str, ",");
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
/*      */   public static Set<String> commaDelimitedListToSet(@Nullable String str) {
/* 1248 */     String[] tokens = commaDelimitedListToStringArray(str);
/* 1249 */     return new LinkedHashSet<>(Arrays.asList(tokens));
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
/*      */   public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim, String prefix, String suffix) {
/* 1264 */     if (CollectionUtils.isEmpty(coll)) {
/* 1265 */       return "";
/*      */     }
/*      */     
/* 1268 */     StringBuilder sb = new StringBuilder();
/* 1269 */     Iterator<?> it = coll.iterator();
/* 1270 */     while (it.hasNext()) {
/* 1271 */       sb.append(prefix).append(it.next()).append(suffix);
/* 1272 */       if (it.hasNext()) {
/* 1273 */         sb.append(delim);
/*      */       }
/*      */     } 
/* 1276 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim) {
/* 1287 */     return collectionToDelimitedString(coll, delim, "", "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String collectionToCommaDelimitedString(@Nullable Collection<?> coll) {
/* 1297 */     return collectionToDelimitedString(coll, ",");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String arrayToDelimitedString(@Nullable Object[] arr, String delim) {
/* 1308 */     if (ObjectUtils.isEmpty(arr)) {
/* 1309 */       return "";
/*      */     }
/* 1311 */     if (arr.length == 1) {
/* 1312 */       return ObjectUtils.nullSafeToString(arr[0]);
/*      */     }
/*      */     
/* 1315 */     StringBuilder sb = new StringBuilder();
/* 1316 */     for (int i = 0; i < arr.length; i++) {
/* 1317 */       if (i > 0) {
/* 1318 */         sb.append(delim);
/*      */       }
/* 1320 */       sb.append(arr[i]);
/*      */     } 
/* 1322 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String arrayToCommaDelimitedString(@Nullable Object[] arr) {
/* 1333 */     return arrayToDelimitedString(arr, ",");
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */