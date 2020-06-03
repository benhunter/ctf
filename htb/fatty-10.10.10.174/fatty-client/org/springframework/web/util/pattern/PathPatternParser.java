/*     */ package org.springframework.web.util.pattern;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathPatternParser
/*     */ {
/*     */   private boolean matchOptionalTrailingSeparator = true;
/*     */   private boolean caseSensitive = true;
/*     */   
/*     */   public void setMatchOptionalTrailingSeparator(boolean matchOptionalTrailingSeparator) {
/*  52 */     this.matchOptionalTrailingSeparator = matchOptionalTrailingSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMatchOptionalTrailingSeparator() {
/*  59 */     return this.matchOptionalTrailingSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean caseSensitive) {
/*  67 */     this.caseSensitive = caseSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCaseSensitive() {
/*  74 */     return this.caseSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   char getSeparator() {
/*  85 */     return '/';
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
/*     */   public PathPattern parse(String pathPattern) throws PatternParseException {
/* 100 */     return (new InternalPathPatternParser(this)).parse(pathPattern);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/PathPatternParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */