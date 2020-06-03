/*     */ package org.springframework.web.util.pattern;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.http.server.PathContainer;
/*     */ import org.springframework.util.LinkedMultiValueMap;
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
/*     */ 
/*     */ 
/*     */ class CaptureTheRestPathElement
/*     */   extends PathElement
/*     */ {
/*     */   private final String variableName;
/*     */   
/*     */   CaptureTheRestPathElement(int pos, char[] captureDescriptor, char separator) {
/*  46 */     super(pos, separator);
/*  47 */     this.variableName = new String(captureDescriptor, 2, captureDescriptor.length - 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(int pathIndex, PathPattern.MatchingContext matchingContext) {
/*  58 */     if (pathIndex < matchingContext.pathLength && !matchingContext.isSeparator(pathIndex)) {
/*  59 */       return false;
/*     */     }
/*  61 */     if (matchingContext.determineRemainingPath) {
/*  62 */       matchingContext.remainingPathIndex = matchingContext.pathLength;
/*     */     }
/*  64 */     if (matchingContext.extractingVariables) {
/*     */       LinkedMultiValueMap linkedMultiValueMap;
/*  66 */       MultiValueMap<String, String> parametersCollector = null;
/*  67 */       for (int i = pathIndex; i < matchingContext.pathLength; i++) {
/*  68 */         PathContainer.Element element = matchingContext.pathElements.get(i);
/*  69 */         if (element instanceof PathContainer.PathSegment) {
/*  70 */           MultiValueMap<String, String> parameters = ((PathContainer.PathSegment)element).parameters();
/*  71 */           if (!parameters.isEmpty()) {
/*  72 */             if (parametersCollector == null) {
/*  73 */               linkedMultiValueMap = new LinkedMultiValueMap();
/*     */             }
/*  75 */             linkedMultiValueMap.addAll(parameters);
/*     */           } 
/*     */         } 
/*     */       } 
/*  79 */       matchingContext.set(this.variableName, pathToString(pathIndex, matchingContext.pathElements), (linkedMultiValueMap == null) ? NO_PARAMETERS : (MultiValueMap<String, String>)linkedMultiValueMap);
/*     */     } 
/*     */     
/*  82 */     return true;
/*     */   }
/*     */   
/*     */   private String pathToString(int fromSegment, List<PathContainer.Element> pathElements) {
/*  86 */     StringBuilder buf = new StringBuilder();
/*  87 */     for (int i = fromSegment, max = pathElements.size(); i < max; i++) {
/*  88 */       PathContainer.Element element = pathElements.get(i);
/*  89 */       if (element instanceof PathContainer.PathSegment) {
/*  90 */         buf.append(((PathContainer.PathSegment)element).valueToMatch());
/*     */       } else {
/*     */         
/*  93 */         buf.append(element.value());
/*     */       } 
/*     */     } 
/*  96 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNormalizedLength() {
/* 101 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWildcardCount() {
/* 106 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCaptureCount() {
/* 111 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return "CaptureTheRest(/{*" + this.variableName + "})";
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] getChars() {
/* 121 */     return ("/{*" + this.variableName + "}").toCharArray();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/CaptureTheRestPathElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */