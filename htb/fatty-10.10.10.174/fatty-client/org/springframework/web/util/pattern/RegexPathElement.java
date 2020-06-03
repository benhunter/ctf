/*     */ package org.springframework.web.util.pattern;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.http.server.PathContainer;
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
/*     */ class RegexPathElement
/*     */   extends PathElement
/*     */ {
/*  38 */   private static final Pattern GLOB_PATTERN = Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");
/*     */ 
/*     */   
/*     */   private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";
/*     */   
/*     */   private char[] regex;
/*     */   
/*     */   private final boolean caseSensitive;
/*     */   
/*     */   private final Pattern pattern;
/*     */   
/*     */   private int wildcardCount;
/*     */   
/*  51 */   private final List<String> variableNames = new LinkedList<>();
/*     */ 
/*     */   
/*     */   RegexPathElement(int pos, char[] regex, boolean caseSensitive, char[] completePattern, char separator) {
/*  55 */     super(pos, separator);
/*  56 */     this.regex = regex;
/*  57 */     this.caseSensitive = caseSensitive;
/*  58 */     this.pattern = buildPattern(regex, completePattern);
/*     */   }
/*     */ 
/*     */   
/*     */   public Pattern buildPattern(char[] regex, char[] completePattern) {
/*  63 */     StringBuilder patternBuilder = new StringBuilder();
/*  64 */     String text = new String(regex);
/*  65 */     Matcher matcher = GLOB_PATTERN.matcher(text);
/*  66 */     int end = 0;
/*     */     
/*  68 */     while (matcher.find()) {
/*  69 */       patternBuilder.append(quote(text, end, matcher.start()));
/*  70 */       String match = matcher.group();
/*  71 */       if ("?".equals(match)) {
/*  72 */         patternBuilder.append('.');
/*     */       }
/*  74 */       else if ("*".equals(match)) {
/*  75 */         patternBuilder.append(".*");
/*  76 */         int pos = matcher.start();
/*  77 */         if (pos < 1 || text.charAt(pos - 1) != '.')
/*     */         {
/*     */           
/*  80 */           this.wildcardCount++;
/*     */         }
/*     */       }
/*  83 */       else if (match.startsWith("{") && match.endsWith("}")) {
/*  84 */         int colonIdx = match.indexOf(':');
/*  85 */         if (colonIdx == -1) {
/*  86 */           patternBuilder.append("(.*)");
/*  87 */           String variableName = matcher.group(1);
/*  88 */           if (this.variableNames.contains(variableName)) {
/*  89 */             throw new PatternParseException(this.pos, completePattern, PatternParseException.PatternMessage.ILLEGAL_DOUBLE_CAPTURE, new Object[] { variableName });
/*     */           }
/*     */           
/*  92 */           this.variableNames.add(variableName);
/*     */         } else {
/*     */           
/*  95 */           String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
/*  96 */           patternBuilder.append('(');
/*  97 */           patternBuilder.append(variablePattern);
/*  98 */           patternBuilder.append(')');
/*  99 */           String variableName = match.substring(1, colonIdx);
/* 100 */           if (this.variableNames.contains(variableName)) {
/* 101 */             throw new PatternParseException(this.pos, completePattern, PatternParseException.PatternMessage.ILLEGAL_DOUBLE_CAPTURE, new Object[] { variableName });
/*     */           }
/*     */           
/* 104 */           this.variableNames.add(variableName);
/*     */         } 
/*     */       } 
/* 107 */       end = matcher.end();
/*     */     } 
/*     */     
/* 110 */     patternBuilder.append(quote(text, end, text.length()));
/* 111 */     if (this.caseSensitive) {
/* 112 */       return Pattern.compile(patternBuilder.toString());
/*     */     }
/*     */     
/* 115 */     return Pattern.compile(patternBuilder.toString(), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getVariableNames() {
/* 120 */     return this.variableNames;
/*     */   }
/*     */   
/*     */   private String quote(String s, int start, int end) {
/* 124 */     if (start == end) {
/* 125 */       return "";
/*     */     }
/* 127 */     return Pattern.quote(s.substring(start, end));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(int pathIndex, PathPattern.MatchingContext matchingContext) {
/* 132 */     String textToMatch = matchingContext.pathElementValue(pathIndex);
/* 133 */     Matcher matcher = this.pattern.matcher(textToMatch);
/* 134 */     boolean matches = matcher.matches();
/*     */     
/* 136 */     if (matches) {
/* 137 */       if (isNoMorePattern()) {
/* 138 */         if (matchingContext.determineRemainingPath && (this.variableNames
/* 139 */           .isEmpty() || textToMatch.length() > 0)) {
/* 140 */           matchingContext.remainingPathIndex = pathIndex + 1;
/* 141 */           matches = true;
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 147 */           matches = (pathIndex + 1 >= matchingContext.pathLength && (this.variableNames.isEmpty() || textToMatch.length() > 0));
/* 148 */           if (!matches && matchingContext.isMatchOptionalTrailingSeparator())
/*     */           {
/*     */ 
/*     */             
/* 152 */             matches = ((this.variableNames.isEmpty() || textToMatch.length() > 0) && pathIndex + 2 >= matchingContext.pathLength && matchingContext.isSeparator(pathIndex + 1));
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 157 */         matches = (this.next != null && this.next.matches(pathIndex + 1, matchingContext));
/*     */       } 
/*     */     }
/*     */     
/* 161 */     if (matches && matchingContext.extractingVariables) {
/*     */       
/* 163 */       if (this.variableNames.size() != matcher.groupCount()) {
/* 164 */         throw new IllegalArgumentException("The number of capturing groups in the pattern segment " + this.pattern + " does not match the number of URI template variables it defines, which can occur if capturing groups are used in a URI template regex. Use non-capturing groups instead.");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 169 */       for (int i = 1; i <= matcher.groupCount(); i++) {
/* 170 */         String name = this.variableNames.get(i - 1);
/* 171 */         String value = matcher.group(i);
/* 172 */         matchingContext.set(name, value, 
/* 173 */             (i == this.variableNames.size()) ? ((PathContainer.PathSegment)matchingContext.pathElements
/* 174 */             .get(pathIndex)).parameters() : NO_PARAMETERS);
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     return matches;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNormalizedLength() {
/* 183 */     int varsLength = 0;
/* 184 */     for (String variableName : this.variableNames) {
/* 185 */       varsLength += variableName.length();
/*     */     }
/* 187 */     return this.regex.length - varsLength - this.variableNames.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCaptureCount() {
/* 192 */     return this.variableNames.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWildcardCount() {
/* 197 */     return this.wildcardCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getScore() {
/* 202 */     return getCaptureCount() * 1 + getWildcardCount() * 100;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 207 */     return "Regex(" + String.valueOf(this.regex) + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] getChars() {
/* 212 */     return this.regex;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/RegexPathElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */