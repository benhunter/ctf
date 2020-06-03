/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.function.Predicate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ProfilesParser
/*     */ {
/*     */   static Profiles parse(String... expressions) {
/*  42 */     Assert.notEmpty((Object[])expressions, "Must specify at least one profile");
/*  43 */     Profiles[] parsed = new Profiles[expressions.length];
/*  44 */     for (int i = 0; i < expressions.length; i++) {
/*  45 */       parsed[i] = parseExpression(expressions[i]);
/*     */     }
/*  47 */     return new ParsedProfiles(expressions, parsed);
/*     */   }
/*     */   
/*     */   private static Profiles parseExpression(String expression) {
/*  51 */     Assert.hasText(expression, () -> "Invalid profile expression [" + expression + "]: must contain text");
/*  52 */     StringTokenizer tokens = new StringTokenizer(expression, "()&|!", true);
/*  53 */     return parseTokens(expression, tokens);
/*     */   }
/*     */   
/*     */   private static Profiles parseTokens(String expression, StringTokenizer tokens) {
/*  57 */     return parseTokens(expression, tokens, Context.NONE);
/*     */   }
/*     */   private static Profiles parseTokens(String expression, StringTokenizer tokens, Context context) {
/*  60 */     List<Profiles> elements = new ArrayList<>();
/*  61 */     Operator operator = null;
/*  62 */     while (tokens.hasMoreTokens()) {
/*  63 */       Profiles contents, merged; String token = tokens.nextToken().trim();
/*  64 */       if (token.isEmpty()) {
/*     */         continue;
/*     */       }
/*  67 */       switch (token) {
/*     */         case "(":
/*  69 */           contents = parseTokens(expression, tokens, Context.BRACKET);
/*  70 */           if (context == Context.INVERT) {
/*  71 */             return contents;
/*     */           }
/*  73 */           elements.add(contents);
/*     */           continue;
/*     */         case "&":
/*  76 */           assertWellFormed(expression, (operator == null || operator == Operator.AND));
/*  77 */           operator = Operator.AND;
/*     */           continue;
/*     */         case "|":
/*  80 */           assertWellFormed(expression, (operator == null || operator == Operator.OR));
/*  81 */           operator = Operator.OR;
/*     */           continue;
/*     */         case "!":
/*  84 */           elements.add(not(parseTokens(expression, tokens, Context.INVERT)));
/*     */           continue;
/*     */         case ")":
/*  87 */           merged = merge(expression, elements, operator);
/*  88 */           if (context == Context.BRACKET) {
/*  89 */             return merged;
/*     */           }
/*  91 */           elements.clear();
/*  92 */           elements.add(merged);
/*  93 */           operator = null;
/*     */           continue;
/*     */       } 
/*  96 */       Profiles value = equals(token);
/*  97 */       if (context == Context.INVERT) {
/*  98 */         return value;
/*     */       }
/* 100 */       elements.add(value);
/*     */     } 
/*     */     
/* 103 */     return merge(expression, elements, operator);
/*     */   }
/*     */   
/*     */   private static Profiles merge(String expression, List<Profiles> elements, @Nullable Operator operator) {
/* 107 */     assertWellFormed(expression, !elements.isEmpty());
/* 108 */     if (elements.size() == 1) {
/* 109 */       return elements.get(0);
/*     */     }
/* 111 */     Profiles[] profiles = elements.<Profiles>toArray(new Profiles[0]);
/* 112 */     return (operator == Operator.AND) ? and(profiles) : or(profiles);
/*     */   }
/*     */   
/*     */   private static void assertWellFormed(String expression, boolean wellFormed) {
/* 116 */     Assert.isTrue(wellFormed, () -> "Malformed profile expression [" + expression + "]");
/*     */   }
/*     */   
/*     */   private static Profiles or(Profiles... profiles) {
/* 120 */     return activeProfile -> Arrays.<Profiles>stream(profiles).anyMatch(isMatch(activeProfile));
/*     */   }
/*     */   
/*     */   private static Profiles and(Profiles... profiles) {
/* 124 */     return activeProfile -> Arrays.<Profiles>stream(profiles).allMatch(isMatch(activeProfile));
/*     */   }
/*     */   
/*     */   private static Profiles not(Profiles profiles) {
/* 128 */     return activeProfile -> !profiles.matches(activeProfile);
/*     */   }
/*     */   
/*     */   private static Profiles equals(String profile) {
/* 132 */     return activeProfile -> activeProfile.test(profile);
/*     */   }
/*     */   
/*     */   private static Predicate<Profiles> isMatch(Predicate<String> activeProfile) {
/* 136 */     return profiles -> profiles.matches(activeProfile);
/*     */   }
/*     */   
/*     */   private enum Operator {
/* 140 */     AND, OR; }
/*     */   
/*     */   private enum Context {
/* 143 */     NONE, INVERT, BRACKET;
/*     */   }
/*     */   
/*     */   private static class ParsedProfiles
/*     */     implements Profiles
/*     */   {
/*     */     private final String[] expressions;
/*     */     private final Profiles[] parsed;
/*     */     
/*     */     ParsedProfiles(String[] expressions, Profiles[] parsed) {
/* 153 */       this.expressions = expressions;
/* 154 */       this.parsed = parsed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Predicate<String> activeProfiles) {
/* 159 */       for (Profiles candidate : this.parsed) {
/* 160 */         if (candidate.matches(activeProfiles)) {
/* 161 */           return true;
/*     */         }
/*     */       } 
/* 164 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 169 */       return StringUtils.arrayToDelimitedString((Object[])this.expressions, " or ");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/env/ProfilesParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */