/*     */ package org.springframework.web.util.pattern;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.http.server.PathContainer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class CaptureVariablePathElement
/*     */   extends PathElement
/*     */ {
/*     */   private final String variableName;
/*     */   @Nullable
/*     */   private Pattern constraintPattern;
/*     */   
/*     */   CaptureVariablePathElement(int pos, char[] captureDescriptor, boolean caseSensitive, char separator) {
/*  47 */     super(pos, separator);
/*  48 */     int colon = -1;
/*  49 */     for (int i = 0; i < captureDescriptor.length; i++) {
/*  50 */       if (captureDescriptor[i] == ':') {
/*  51 */         colon = i;
/*     */         break;
/*     */       } 
/*     */     } 
/*  55 */     if (colon == -1) {
/*     */       
/*  57 */       this.variableName = new String(captureDescriptor, 1, captureDescriptor.length - 2);
/*     */     } else {
/*     */       
/*  60 */       this.variableName = new String(captureDescriptor, 1, colon - 1);
/*  61 */       if (caseSensitive) {
/*  62 */         this.constraintPattern = Pattern.compile(new String(captureDescriptor, colon + 1, captureDescriptor.length - colon - 2));
/*     */       }
/*     */       else {
/*     */         
/*  66 */         this.constraintPattern = Pattern.compile(new String(captureDescriptor, colon + 1, captureDescriptor.length - colon - 2), 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(int pathIndex, PathPattern.MatchingContext matchingContext) {
/*  76 */     if (pathIndex >= matchingContext.pathLength)
/*     */     {
/*  78 */       return false;
/*     */     }
/*  80 */     String candidateCapture = matchingContext.pathElementValue(pathIndex);
/*  81 */     if (candidateCapture.length() == 0) {
/*  82 */       return false;
/*     */     }
/*     */     
/*  85 */     if (this.constraintPattern != null) {
/*     */ 
/*     */       
/*  88 */       Matcher matcher = this.constraintPattern.matcher(candidateCapture);
/*  89 */       if (matcher.groupCount() != 0) {
/*  90 */         throw new IllegalArgumentException("No capture groups allowed in the constraint regex: " + this.constraintPattern
/*  91 */             .pattern());
/*     */       }
/*  93 */       if (!matcher.matches()) {
/*  94 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  98 */     boolean match = false;
/*  99 */     pathIndex++;
/* 100 */     if (isNoMorePattern()) {
/* 101 */       if (matchingContext.determineRemainingPath) {
/* 102 */         matchingContext.remainingPathIndex = pathIndex;
/* 103 */         match = true;
/*     */       }
/*     */       else {
/*     */         
/* 107 */         match = (pathIndex == matchingContext.pathLength);
/* 108 */         if (!match && matchingContext.isMatchOptionalTrailingSeparator())
/*     */         {
/*     */           
/* 111 */           match = (pathIndex + 1 == matchingContext.pathLength && matchingContext.isSeparator(pathIndex));
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 116 */     else if (this.next != null) {
/* 117 */       match = this.next.matches(pathIndex, matchingContext);
/*     */     } 
/*     */ 
/*     */     
/* 121 */     if (match && matchingContext.extractingVariables) {
/* 122 */       matchingContext.set(this.variableName, candidateCapture, ((PathContainer.PathSegment)matchingContext.pathElements
/* 123 */           .get(pathIndex - 1)).parameters());
/*     */     }
/* 125 */     return match;
/*     */   }
/*     */   
/*     */   public String getVariableName() {
/* 129 */     return this.variableName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNormalizedLength() {
/* 134 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWildcardCount() {
/* 139 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCaptureCount() {
/* 144 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getScore() {
/* 149 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 154 */     return "CaptureVariable({" + this.variableName + ((this.constraintPattern != null) ? (":" + this.constraintPattern
/* 155 */       .pattern()) : "") + "})";
/*     */   }
/*     */   
/*     */   public char[] getChars() {
/* 159 */     StringBuilder b = new StringBuilder();
/* 160 */     b.append("{");
/* 161 */     b.append(this.variableName);
/* 162 */     if (this.constraintPattern != null) {
/* 163 */       b.append(":").append(this.constraintPattern.pattern());
/*     */     }
/* 165 */     b.append("}");
/* 166 */     return b.toString().toCharArray();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/CaptureVariablePathElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */