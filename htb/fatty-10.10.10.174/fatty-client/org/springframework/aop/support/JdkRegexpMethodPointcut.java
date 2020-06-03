/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
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
/*     */ public class JdkRegexpMethodPointcut
/*     */   extends AbstractRegexpMethodPointcut
/*     */ {
/*  46 */   private Pattern[] compiledPatterns = new Pattern[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private Pattern[] compiledExclusionPatterns = new Pattern[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPatternRepresentation(String[] patterns) throws PatternSyntaxException {
/*  59 */     this.compiledPatterns = compilePatterns(patterns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initExcludedPatternRepresentation(String[] excludedPatterns) throws PatternSyntaxException {
/*  67 */     this.compiledExclusionPatterns = compilePatterns(excludedPatterns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean matches(String pattern, int patternIndex) {
/*  76 */     Matcher matcher = this.compiledPatterns[patternIndex].matcher(pattern);
/*  77 */     return matcher.matches();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean matchesExclusion(String candidate, int patternIndex) {
/*  86 */     Matcher matcher = this.compiledExclusionPatterns[patternIndex].matcher(candidate);
/*  87 */     return matcher.matches();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Pattern[] compilePatterns(String[] source) throws PatternSyntaxException {
/*  96 */     Pattern[] destination = new Pattern[source.length];
/*  97 */     for (int i = 0; i < source.length; i++) {
/*  98 */       destination[i] = Pattern.compile(source[i]);
/*     */     }
/* 100 */     return destination;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/JdkRegexpMethodPointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */