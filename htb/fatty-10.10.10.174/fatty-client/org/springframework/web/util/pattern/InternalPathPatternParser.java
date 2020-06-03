/*     */ package org.springframework.web.util.pattern;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.PatternSyntaxException;
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
/*     */ class InternalPathPatternParser
/*     */ {
/*     */   private final PathPatternParser parser;
/*  39 */   private char[] pathPatternData = new char[0];
/*     */ 
/*     */ 
/*     */   
/*     */   private int pathPatternLength;
/*     */ 
/*     */   
/*     */   int pos;
/*     */ 
/*     */   
/*     */   private int singleCharWildcardCount;
/*     */ 
/*     */   
/*     */   private boolean wildcard = false;
/*     */ 
/*     */   
/*     */   private boolean isCaptureTheRestVariable = false;
/*     */ 
/*     */   
/*     */   private boolean insideVariableCapture = false;
/*     */ 
/*     */   
/*  61 */   private int variableCaptureCount = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private int pathElementStart;
/*     */ 
/*     */ 
/*     */   
/*     */   private int variableCaptureStart;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private List<String> capturedVariableNames;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private PathElement headPE;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private PathElement currentPE;
/*     */ 
/*     */ 
/*     */   
/*     */   InternalPathPatternParser(PathPatternParser parentParser) {
/*  87 */     this.parser = parentParser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathPattern parse(String pathPattern) throws PatternParseException {
/*  95 */     Assert.notNull(pathPattern, "Path pattern must not be null");
/*     */     
/*  97 */     this.pathPatternData = pathPattern.toCharArray();
/*  98 */     this.pathPatternLength = this.pathPatternData.length;
/*  99 */     this.headPE = null;
/* 100 */     this.currentPE = null;
/* 101 */     this.capturedVariableNames = null;
/* 102 */     this.pathElementStart = -1;
/* 103 */     this.pos = 0;
/* 104 */     resetPathElementState();
/*     */     
/* 106 */     while (this.pos < this.pathPatternLength) {
/* 107 */       char ch = this.pathPatternData[this.pos];
/* 108 */       if (ch == this.parser.getSeparator()) {
/* 109 */         if (this.pathElementStart != -1) {
/* 110 */           pushPathElement(createPathElement());
/*     */         }
/* 112 */         if (peekDoubleWildcard()) {
/* 113 */           pushPathElement(new WildcardTheRestPathElement(this.pos, this.parser.getSeparator()));
/* 114 */           this.pos += 2;
/*     */         } else {
/*     */           
/* 117 */           pushPathElement(new SeparatorPathElement(this.pos, this.parser.getSeparator()));
/*     */         } 
/*     */       } else {
/*     */         
/* 121 */         if (this.pathElementStart == -1) {
/* 122 */           this.pathElementStart = this.pos;
/*     */         }
/* 124 */         if (ch == '?') {
/* 125 */           this.singleCharWildcardCount++;
/*     */         }
/* 127 */         else if (ch == '{') {
/* 128 */           if (this.insideVariableCapture) {
/* 129 */             throw new PatternParseException(this.pos, this.pathPatternData, PatternParseException.PatternMessage.ILLEGAL_NESTED_CAPTURE, new Object[0]);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 137 */           this.insideVariableCapture = true;
/* 138 */           this.variableCaptureStart = this.pos;
/*     */         }
/* 140 */         else if (ch == '}') {
/* 141 */           if (!this.insideVariableCapture) {
/* 142 */             throw new PatternParseException(this.pos, this.pathPatternData, PatternParseException.PatternMessage.MISSING_OPEN_CAPTURE, new Object[0]);
/*     */           }
/*     */           
/* 145 */           this.insideVariableCapture = false;
/* 146 */           if (this.isCaptureTheRestVariable && this.pos + 1 < this.pathPatternLength) {
/* 147 */             throw new PatternParseException(this.pos + 1, this.pathPatternData, PatternParseException.PatternMessage.NO_MORE_DATA_EXPECTED_AFTER_CAPTURE_THE_REST, new Object[0]);
/*     */           }
/*     */           
/* 150 */           this.variableCaptureCount++;
/*     */         }
/* 152 */         else if (ch == ':') {
/* 153 */           if (this.insideVariableCapture && !this.isCaptureTheRestVariable) {
/* 154 */             skipCaptureRegex();
/* 155 */             this.insideVariableCapture = false;
/* 156 */             this.variableCaptureCount++;
/*     */           }
/*     */         
/* 159 */         } else if (ch == '*') {
/* 160 */           if (this.insideVariableCapture && this.variableCaptureStart == this.pos - 1) {
/* 161 */             this.isCaptureTheRestVariable = true;
/*     */           }
/* 163 */           this.wildcard = true;
/*     */         } 
/*     */         
/* 166 */         if (this.insideVariableCapture) {
/* 167 */           if (this.variableCaptureStart + 1 + (this.isCaptureTheRestVariable ? 1 : 0) == this.pos && 
/* 168 */             !Character.isJavaIdentifierStart(ch)) {
/* 169 */             throw new PatternParseException(this.pos, this.pathPatternData, PatternParseException.PatternMessage.ILLEGAL_CHARACTER_AT_START_OF_CAPTURE_DESCRIPTOR, new Object[] {
/*     */                   
/* 171 */                   Character.toString(ch)
/*     */                 });
/*     */           }
/* 174 */           if (this.pos > this.variableCaptureStart + 1 + (this.isCaptureTheRestVariable ? 1 : 0) && 
/* 175 */             !Character.isJavaIdentifierPart(ch) && ch != '-')
/* 176 */             throw new PatternParseException(this.pos, this.pathPatternData, PatternParseException.PatternMessage.ILLEGAL_CHARACTER_IN_CAPTURE_DESCRIPTOR, new Object[] {
/*     */                   
/* 178 */                   Character.toString(ch)
/*     */                 }); 
/*     */         } 
/*     */       } 
/* 182 */       this.pos++;
/*     */     } 
/* 184 */     if (this.pathElementStart != -1) {
/* 185 */       pushPathElement(createPathElement());
/*     */     }
/* 187 */     return new PathPattern(pathPattern, this.parser, this.headPE);
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
/*     */   private void skipCaptureRegex() {
/* 200 */     int regexStart = ++this.pos;
/* 201 */     int curlyBracketDepth = 0;
/* 202 */     boolean previousBackslash = false;
/*     */     
/* 204 */     while (this.pos < this.pathPatternLength) {
/* 205 */       char ch = this.pathPatternData[this.pos];
/* 206 */       if (ch == '\\' && !previousBackslash) {
/* 207 */         this.pos++;
/* 208 */         previousBackslash = true;
/*     */         continue;
/*     */       } 
/* 211 */       if (ch == '{' && !previousBackslash) {
/* 212 */         curlyBracketDepth++;
/*     */       }
/* 214 */       else if (ch == '}' && !previousBackslash) {
/* 215 */         if (curlyBracketDepth == 0) {
/* 216 */           if (regexStart == this.pos) {
/* 217 */             throw new PatternParseException(regexStart, this.pathPatternData, PatternParseException.PatternMessage.MISSING_REGEX_CONSTRAINT, new Object[0]);
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 222 */         curlyBracketDepth--;
/*     */       } 
/* 224 */       if (ch == this.parser.getSeparator() && !previousBackslash) {
/* 225 */         throw new PatternParseException(this.pos, this.pathPatternData, PatternParseException.PatternMessage.MISSING_CLOSE_CAPTURE, new Object[0]);
/*     */       }
/*     */       
/* 228 */       this.pos++;
/* 229 */       previousBackslash = false;
/*     */     } 
/*     */     
/* 232 */     throw new PatternParseException(this.pos - 1, this.pathPatternData, PatternParseException.PatternMessage.MISSING_CLOSE_CAPTURE, new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean peekDoubleWildcard() {
/* 241 */     if (this.pos + 2 >= this.pathPatternLength) {
/* 242 */       return false;
/*     */     }
/* 244 */     if (this.pathPatternData[this.pos + 1] != '*' || this.pathPatternData[this.pos + 2] != '*') {
/* 245 */       return false;
/*     */     }
/* 247 */     return (this.pos + 3 == this.pathPatternLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pushPathElement(PathElement newPathElement) {
/* 255 */     if (newPathElement instanceof CaptureTheRestPathElement) {
/*     */ 
/*     */       
/* 258 */       if (this.currentPE == null) {
/* 259 */         this.headPE = newPathElement;
/* 260 */         this.currentPE = newPathElement;
/*     */       }
/* 262 */       else if (this.currentPE instanceof SeparatorPathElement) {
/* 263 */         PathElement peBeforeSeparator = this.currentPE.prev;
/* 264 */         if (peBeforeSeparator == null) {
/*     */           
/* 266 */           this.headPE = newPathElement;
/* 267 */           newPathElement.prev = null;
/*     */         } else {
/*     */           
/* 270 */           peBeforeSeparator.next = newPathElement;
/* 271 */           newPathElement.prev = peBeforeSeparator;
/*     */         } 
/* 273 */         this.currentPE = newPathElement;
/*     */       } else {
/*     */         
/* 276 */         throw new IllegalStateException("Expected SeparatorPathElement but was " + this.currentPE);
/*     */       }
/*     */     
/*     */     }
/* 280 */     else if (this.headPE == null) {
/* 281 */       this.headPE = newPathElement;
/* 282 */       this.currentPE = newPathElement;
/*     */     }
/* 284 */     else if (this.currentPE != null) {
/* 285 */       this.currentPE.next = newPathElement;
/* 286 */       newPathElement.prev = this.currentPE;
/* 287 */       this.currentPE = newPathElement;
/*     */     } 
/*     */ 
/*     */     
/* 291 */     resetPathElementState();
/*     */   }
/*     */   
/*     */   private char[] getPathElementText() {
/* 295 */     char[] pathElementText = new char[this.pos - this.pathElementStart];
/* 296 */     System.arraycopy(this.pathPatternData, this.pathElementStart, pathElementText, 0, this.pos - this.pathElementStart);
/*     */     
/* 298 */     return pathElementText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PathElement createPathElement() {
/* 307 */     if (this.insideVariableCapture) {
/* 308 */       throw new PatternParseException(this.pos, this.pathPatternData, PatternParseException.PatternMessage.MISSING_CLOSE_CAPTURE, new Object[0]);
/*     */     }
/*     */     
/* 311 */     PathElement newPE = null;
/*     */     
/* 313 */     if (this.variableCaptureCount > 0) {
/* 314 */       if (this.variableCaptureCount == 1 && this.pathElementStart == this.variableCaptureStart && this.pathPatternData[this.pos - 1] == '}') {
/*     */         
/* 316 */         if (this.isCaptureTheRestVariable) {
/*     */ 
/*     */           
/* 319 */           newPE = new CaptureTheRestPathElement(this.pathElementStart, getPathElementText(), this.parser.getSeparator());
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/*     */             
/* 325 */             newPE = new CaptureVariablePathElement(this.pathElementStart, getPathElementText(), this.parser.isCaseSensitive(), this.parser.getSeparator());
/*     */           }
/* 327 */           catch (PatternSyntaxException pse) {
/* 328 */             throw new PatternParseException(pse, 
/* 329 */                 findRegexStart(this.pathPatternData, this.pathElementStart) + pse.getIndex(), this.pathPatternData, PatternParseException.PatternMessage.REGEX_PATTERN_SYNTAX_EXCEPTION, new Object[0]);
/*     */           } 
/*     */           
/* 332 */           recordCapturedVariable(this.pathElementStart, ((CaptureVariablePathElement)newPE)
/* 333 */               .getVariableName());
/*     */         } 
/*     */       } else {
/*     */         
/* 337 */         if (this.isCaptureTheRestVariable) {
/* 338 */           throw new PatternParseException(this.pathElementStart, this.pathPatternData, PatternParseException.PatternMessage.CAPTURE_ALL_IS_STANDALONE_CONSTRUCT, new Object[0]);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 343 */         RegexPathElement newRegexSection = new RegexPathElement(this.pathElementStart, getPathElementText(), this.parser.isCaseSensitive(), this.pathPatternData, this.parser.getSeparator());
/* 344 */         for (String variableName : newRegexSection.getVariableNames()) {
/* 345 */           recordCapturedVariable(this.pathElementStart, variableName);
/*     */         }
/* 347 */         newPE = newRegexSection;
/*     */       }
/*     */     
/*     */     }
/* 351 */     else if (this.wildcard) {
/* 352 */       if (this.pos - 1 == this.pathElementStart) {
/* 353 */         newPE = new WildcardPathElement(this.pathElementStart, this.parser.getSeparator());
/*     */       }
/*     */       else {
/*     */         
/* 357 */         newPE = new RegexPathElement(this.pathElementStart, getPathElementText(), this.parser.isCaseSensitive(), this.pathPatternData, this.parser.getSeparator());
/*     */       }
/*     */     
/* 360 */     } else if (this.singleCharWildcardCount != 0) {
/*     */       
/* 362 */       newPE = new SingleCharWildcardedPathElement(this.pathElementStart, getPathElementText(), this.singleCharWildcardCount, this.parser.isCaseSensitive(), this.parser.getSeparator());
/*     */     }
/*     */     else {
/*     */       
/* 366 */       newPE = new LiteralPathElement(this.pathElementStart, getPathElementText(), this.parser.isCaseSensitive(), this.parser.getSeparator());
/*     */     } 
/*     */ 
/*     */     
/* 370 */     return newPE;
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
/*     */   private int findRegexStart(char[] data, int offset) {
/* 382 */     int pos = offset;
/* 383 */     while (pos < data.length) {
/* 384 */       if (data[pos] == ':') {
/* 385 */         return pos + 1;
/*     */       }
/* 387 */       pos++;
/*     */     } 
/* 389 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetPathElementState() {
/* 396 */     this.pathElementStart = -1;
/* 397 */     this.singleCharWildcardCount = 0;
/* 398 */     this.insideVariableCapture = false;
/* 399 */     this.variableCaptureCount = 0;
/* 400 */     this.wildcard = false;
/* 401 */     this.isCaptureTheRestVariable = false;
/* 402 */     this.variableCaptureStart = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void recordCapturedVariable(int pos, String variableName) {
/* 409 */     if (this.capturedVariableNames == null) {
/* 410 */       this.capturedVariableNames = new ArrayList<>();
/*     */     }
/* 412 */     if (this.capturedVariableNames.contains(variableName)) {
/* 413 */       throw new PatternParseException(pos, this.pathPatternData, PatternParseException.PatternMessage.ILLEGAL_DOUBLE_CAPTURE, new Object[] { variableName });
/*     */     }
/*     */     
/* 416 */     this.capturedVariableNames.add(variableName);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/InternalPathPatternParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */