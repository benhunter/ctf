/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import org.aspectj.weaver.tools.PointcutParser;
/*     */ import org.aspectj.weaver.tools.TypePatternMatcher;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ public class TypePatternClassFilter
/*     */   implements ClassFilter
/*     */ {
/*  36 */   private String typePattern = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TypePatternMatcher aspectJTypePatternMatcher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypePatternClassFilter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypePatternClassFilter(String typePattern) {
/*  58 */     setTypePattern(typePattern);
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
/*     */   public void setTypePattern(String typePattern) {
/*  78 */     Assert.notNull(typePattern, "Type pattern must not be null");
/*  79 */     this.typePattern = typePattern;
/*  80 */     this
/*     */       
/*  82 */       .aspectJTypePatternMatcher = PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution().parseTypePattern(replaceBooleanOperators(typePattern));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypePattern() {
/*  89 */     return this.typePattern;
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
/*     */   public boolean matches(Class<?> clazz) {
/* 101 */     Assert.state((this.aspectJTypePatternMatcher != null), "No type pattern has been set");
/* 102 */     return this.aspectJTypePatternMatcher.matches(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String replaceBooleanOperators(String pcExpr) {
/* 112 */     String result = StringUtils.replace(pcExpr, " and ", " && ");
/* 113 */     result = StringUtils.replace(result, " or ", " || ");
/* 114 */     return StringUtils.replace(result, " not ", " ! ");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/TypePatternClassFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */