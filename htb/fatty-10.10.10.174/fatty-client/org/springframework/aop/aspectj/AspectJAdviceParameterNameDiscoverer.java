/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.aspectj.lang.JoinPoint;
/*     */ import org.aspectj.lang.ProceedingJoinPoint;
/*     */ import org.aspectj.weaver.tools.PointcutParser;
/*     */ import org.aspectj.weaver.tools.PointcutPrimitive;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class AspectJAdviceParameterNameDiscoverer
/*     */   implements ParameterNameDiscoverer
/*     */ {
/*     */   private static final String THIS_JOIN_POINT = "thisJoinPoint";
/*     */   private static final String THIS_JOIN_POINT_STATIC_PART = "thisJoinPointStaticPart";
/*     */   private static final int STEP_JOIN_POINT_BINDING = 1;
/*     */   private static final int STEP_THROWING_BINDING = 2;
/*     */   private static final int STEP_ANNOTATION_BINDING = 3;
/*     */   private static final int STEP_RETURNING_BINDING = 4;
/*     */   private static final int STEP_PRIMITIVE_ARGS_BINDING = 5;
/*     */   private static final int STEP_THIS_TARGET_ARGS_BINDING = 6;
/*     */   private static final int STEP_REFERENCE_PCUT_BINDING = 7;
/*     */   private static final int STEP_FINISHED = 8;
/* 135 */   private static final Set<String> singleValuedAnnotationPcds = new HashSet<>();
/* 136 */   private static final Set<String> nonReferencePointcutTokens = new HashSet<>();
/*     */ 
/*     */   
/*     */   static {
/* 140 */     singleValuedAnnotationPcds.add("@this");
/* 141 */     singleValuedAnnotationPcds.add("@target");
/* 142 */     singleValuedAnnotationPcds.add("@within");
/* 143 */     singleValuedAnnotationPcds.add("@withincode");
/* 144 */     singleValuedAnnotationPcds.add("@annotation");
/*     */     
/* 146 */     Set<PointcutPrimitive> pointcutPrimitives = PointcutParser.getAllSupportedPointcutPrimitives();
/* 147 */     for (PointcutPrimitive primitive : pointcutPrimitives) {
/* 148 */       nonReferencePointcutTokens.add(primitive.getName());
/*     */     }
/* 150 */     nonReferencePointcutTokens.add("&&");
/* 151 */     nonReferencePointcutTokens.add("!");
/* 152 */     nonReferencePointcutTokens.add("||");
/* 153 */     nonReferencePointcutTokens.add("and");
/* 154 */     nonReferencePointcutTokens.add("or");
/* 155 */     nonReferencePointcutTokens.add("not");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String pointcutExpression;
/*     */ 
/*     */   
/*     */   private boolean raiseExceptions;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String returningName;
/*     */   
/*     */   @Nullable
/*     */   private String throwingName;
/*     */   
/* 173 */   private Class<?>[] argumentTypes = new Class[0];
/*     */   
/* 175 */   private String[] parameterNameBindings = new String[0];
/*     */ 
/*     */ 
/*     */   
/*     */   private int numberOfRemainingUnboundArguments;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AspectJAdviceParameterNameDiscoverer(@Nullable String pointcutExpression) {
/* 185 */     this.pointcutExpression = pointcutExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRaiseExceptions(boolean raiseExceptions) {
/* 195 */     this.raiseExceptions = raiseExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReturningName(@Nullable String returningName) {
/* 204 */     this.returningName = returningName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThrowingName(@Nullable String throwingName) {
/* 213 */     this.throwingName = throwingName;
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
/*     */   @Nullable
/*     */   public String[] getParameterNames(Method method) {
/* 227 */     this.argumentTypes = method.getParameterTypes();
/* 228 */     this.numberOfRemainingUnboundArguments = this.argumentTypes.length;
/* 229 */     this.parameterNameBindings = new String[this.numberOfRemainingUnboundArguments];
/*     */     
/* 231 */     int minimumNumberUnboundArgs = 0;
/* 232 */     if (this.returningName != null) {
/* 233 */       minimumNumberUnboundArgs++;
/*     */     }
/* 235 */     if (this.throwingName != null) {
/* 236 */       minimumNumberUnboundArgs++;
/*     */     }
/* 238 */     if (this.numberOfRemainingUnboundArguments < minimumNumberUnboundArgs) {
/* 239 */       throw new IllegalStateException("Not enough arguments in method to satisfy binding of returning and throwing variables");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 244 */       int algorithmicStep = 1;
/* 245 */       while (this.numberOfRemainingUnboundArguments > 0 && algorithmicStep < 8) {
/* 246 */         switch (algorithmicStep++) {
/*     */           case 1:
/* 248 */             if (!maybeBindThisJoinPoint()) {
/* 249 */               maybeBindThisJoinPointStaticPart();
/*     */             }
/*     */             continue;
/*     */           case 2:
/* 253 */             maybeBindThrowingVariable();
/*     */             continue;
/*     */           case 3:
/* 256 */             maybeBindAnnotationsFromPointcutExpression();
/*     */             continue;
/*     */           case 4:
/* 259 */             maybeBindReturningVariable();
/*     */             continue;
/*     */           case 5:
/* 262 */             maybeBindPrimitiveArgsFromPointcutExpression();
/*     */             continue;
/*     */           case 6:
/* 265 */             maybeBindThisOrTargetOrArgsFromPointcutExpression();
/*     */             continue;
/*     */           case 7:
/* 268 */             maybeBindReferencePointcutParameter();
/*     */             continue;
/*     */         } 
/* 271 */         throw new IllegalStateException("Unknown algorithmic step: " + (algorithmicStep - 1));
/*     */       }
/*     */     
/*     */     }
/* 275 */     catch (AmbiguousBindingException|IllegalArgumentException ex) {
/* 276 */       if (this.raiseExceptions) {
/* 277 */         throw ex;
/*     */       }
/*     */       
/* 280 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 284 */     if (this.numberOfRemainingUnboundArguments == 0) {
/* 285 */       return this.parameterNameBindings;
/*     */     }
/*     */     
/* 288 */     if (this.raiseExceptions) {
/* 289 */       throw new IllegalStateException("Failed to bind all argument names: " + this.numberOfRemainingUnboundArguments + " argument(s) could not be bound");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 294 */     return null;
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
/*     */   @Nullable
/*     */   public String[] getParameterNames(Constructor<?> ctor) {
/* 308 */     if (this.raiseExceptions) {
/* 309 */       throw new UnsupportedOperationException("An advice method can never be a constructor");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 314 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void bindParameterName(int index, String name) {
/* 320 */     this.parameterNameBindings[index] = name;
/* 321 */     this.numberOfRemainingUnboundArguments--;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean maybeBindThisJoinPoint() {
/* 329 */     if (this.argumentTypes[0] == JoinPoint.class || this.argumentTypes[0] == ProceedingJoinPoint.class) {
/* 330 */       bindParameterName(0, "thisJoinPoint");
/* 331 */       return true;
/*     */     } 
/*     */     
/* 334 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void maybeBindThisJoinPointStaticPart() {
/* 339 */     if (this.argumentTypes[0] == JoinPoint.StaticPart.class) {
/* 340 */       bindParameterName(0, "thisJoinPointStaticPart");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeBindThrowingVariable() {
/* 349 */     if (this.throwingName == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 354 */     int throwableIndex = -1;
/* 355 */     for (int i = 0; i < this.argumentTypes.length; i++) {
/* 356 */       if (isUnbound(i) && isSubtypeOf(Throwable.class, i)) {
/* 357 */         if (throwableIndex == -1) {
/* 358 */           throwableIndex = i;
/*     */         }
/*     */         else {
/*     */           
/* 362 */           throw new AmbiguousBindingException("Binding of throwing parameter '" + this.throwingName + "' is ambiguous: could be bound to argument " + throwableIndex + " or argument " + i);
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 369 */     if (throwableIndex == -1) {
/* 370 */       throw new IllegalStateException("Binding of throwing parameter '" + this.throwingName + "' could not be completed as no available arguments are a subtype of Throwable");
/*     */     }
/*     */ 
/*     */     
/* 374 */     bindParameterName(throwableIndex, this.throwingName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeBindReturningVariable() {
/* 382 */     if (this.numberOfRemainingUnboundArguments == 0) {
/* 383 */       throw new IllegalStateException("Algorithm assumes that there must be at least one unbound parameter on entry to this method");
/*     */     }
/*     */ 
/*     */     
/* 387 */     if (this.returningName != null) {
/* 388 */       if (this.numberOfRemainingUnboundArguments > 1) {
/* 389 */         throw new AmbiguousBindingException("Binding of returning parameter '" + this.returningName + "' is ambiguous, there are " + this.numberOfRemainingUnboundArguments + " candidates.");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 394 */       for (int i = 0; i < this.parameterNameBindings.length; i++) {
/* 395 */         if (this.parameterNameBindings[i] == null) {
/* 396 */           bindParameterName(i, this.returningName);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
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
/*     */   private void maybeBindAnnotationsFromPointcutExpression() {
/* 412 */     List<String> varNames = new ArrayList<>();
/* 413 */     String[] tokens = StringUtils.tokenizeToStringArray(this.pointcutExpression, " ");
/* 414 */     for (int i = 0; i < tokens.length; i++) {
/* 415 */       String toMatch = tokens[i];
/* 416 */       int firstParenIndex = toMatch.indexOf('(');
/* 417 */       if (firstParenIndex != -1) {
/* 418 */         toMatch = toMatch.substring(0, firstParenIndex);
/*     */       }
/* 420 */       if (singleValuedAnnotationPcds.contains(toMatch)) {
/* 421 */         PointcutBody body = getPointcutBody(tokens, i);
/* 422 */         i += body.numTokensConsumed;
/* 423 */         String varName = maybeExtractVariableName(body.text);
/* 424 */         if (varName != null) {
/* 425 */           varNames.add(varName);
/*     */         }
/*     */       }
/* 428 */       else if (tokens[i].startsWith("@args(") || tokens[i].equals("@args")) {
/* 429 */         PointcutBody body = getPointcutBody(tokens, i);
/* 430 */         i += body.numTokensConsumed;
/* 431 */         maybeExtractVariableNamesFromArgs(body.text, varNames);
/*     */       } 
/*     */     } 
/*     */     
/* 435 */     bindAnnotationsFromVarNames(varNames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void bindAnnotationsFromVarNames(List<String> varNames) {
/* 442 */     if (!varNames.isEmpty()) {
/*     */       
/* 444 */       int numAnnotationSlots = countNumberOfUnboundAnnotationArguments();
/* 445 */       if (numAnnotationSlots > 1) {
/* 446 */         throw new AmbiguousBindingException("Found " + varNames.size() + " potential annotation variable(s), and " + numAnnotationSlots + " potential argument slots");
/*     */       }
/*     */ 
/*     */       
/* 450 */       if (numAnnotationSlots == 1) {
/* 451 */         if (varNames.size() == 1) {
/*     */           
/* 453 */           findAndBind(Annotation.class, varNames.get(0));
/*     */         }
/*     */         else {
/*     */           
/* 457 */           throw new IllegalArgumentException("Found " + varNames.size() + " candidate annotation binding variables but only one potential argument binding slot");
/*     */         } 
/*     */       }
/*     */     } 
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
/*     */   @Nullable
/*     */   private String maybeExtractVariableName(@Nullable String candidateToken) {
/* 473 */     if (!StringUtils.hasLength(candidateToken)) {
/* 474 */       return null;
/*     */     }
/* 476 */     if (Character.isJavaIdentifierStart(candidateToken.charAt(0)) && 
/* 477 */       Character.isLowerCase(candidateToken.charAt(0))) {
/* 478 */       char[] tokenChars = candidateToken.toCharArray();
/* 479 */       for (char tokenChar : tokenChars) {
/* 480 */         if (!Character.isJavaIdentifierPart(tokenChar)) {
/* 481 */           return null;
/*     */         }
/*     */       } 
/* 484 */       return candidateToken;
/*     */     } 
/*     */     
/* 487 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeExtractVariableNamesFromArgs(@Nullable String argsSpec, List<String> varNames) {
/* 496 */     if (argsSpec == null) {
/*     */       return;
/*     */     }
/* 499 */     String[] tokens = StringUtils.tokenizeToStringArray(argsSpec, ",");
/* 500 */     for (int i = 0; i < tokens.length; i++) {
/* 501 */       tokens[i] = StringUtils.trimWhitespace(tokens[i]);
/* 502 */       String varName = maybeExtractVariableName(tokens[i]);
/* 503 */       if (varName != null) {
/* 504 */         varNames.add(varName);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeBindThisOrTargetOrArgsFromPointcutExpression() {
/* 514 */     if (this.numberOfRemainingUnboundArguments > 1) {
/* 515 */       throw new AmbiguousBindingException("Still " + this.numberOfRemainingUnboundArguments + " unbound args at this(),target(),args() binding stage, with no way to determine between them");
/*     */     }
/*     */ 
/*     */     
/* 519 */     List<String> varNames = new ArrayList<>();
/* 520 */     String[] tokens = StringUtils.tokenizeToStringArray(this.pointcutExpression, " ");
/* 521 */     for (int i = 0; i < tokens.length; i++) {
/* 522 */       if (tokens[i].equals("this") || tokens[i]
/* 523 */         .startsWith("this(") || tokens[i]
/* 524 */         .equals("target") || tokens[i]
/* 525 */         .startsWith("target(")) {
/* 526 */         PointcutBody body = getPointcutBody(tokens, i);
/* 527 */         i += body.numTokensConsumed;
/* 528 */         String varName = maybeExtractVariableName(body.text);
/* 529 */         if (varName != null) {
/* 530 */           varNames.add(varName);
/*     */         }
/*     */       }
/* 533 */       else if (tokens[i].equals("args") || tokens[i].startsWith("args(")) {
/* 534 */         PointcutBody body = getPointcutBody(tokens, i);
/* 535 */         i += body.numTokensConsumed;
/* 536 */         List<String> candidateVarNames = new ArrayList<>();
/* 537 */         maybeExtractVariableNamesFromArgs(body.text, candidateVarNames);
/*     */ 
/*     */         
/* 540 */         for (String varName : candidateVarNames) {
/* 541 */           if (!alreadyBound(varName)) {
/* 542 */             varNames.add(varName);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 549 */     if (varNames.size() > 1) {
/* 550 */       throw new AmbiguousBindingException("Found " + varNames.size() + " candidate this(), target() or args() variables but only one unbound argument slot");
/*     */     }
/*     */     
/* 553 */     if (varNames.size() == 1) {
/* 554 */       for (int j = 0; j < this.parameterNameBindings.length; j++) {
/* 555 */         if (isUnbound(j)) {
/* 556 */           bindParameterName(j, varNames.get(0));
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void maybeBindReferencePointcutParameter() {
/* 565 */     if (this.numberOfRemainingUnboundArguments > 1) {
/* 566 */       throw new AmbiguousBindingException("Still " + this.numberOfRemainingUnboundArguments + " unbound args at reference pointcut binding stage, with no way to determine between them");
/*     */     }
/*     */ 
/*     */     
/* 570 */     List<String> varNames = new ArrayList<>();
/* 571 */     String[] tokens = StringUtils.tokenizeToStringArray(this.pointcutExpression, " ");
/* 572 */     for (int i = 0; i < tokens.length; i++) {
/* 573 */       String toMatch = tokens[i];
/* 574 */       if (toMatch.startsWith("!")) {
/* 575 */         toMatch = toMatch.substring(1);
/*     */       }
/* 577 */       int firstParenIndex = toMatch.indexOf('(');
/* 578 */       if (firstParenIndex != -1) {
/* 579 */         toMatch = toMatch.substring(0, firstParenIndex);
/*     */       } else {
/*     */         
/* 582 */         if (tokens.length < i + 2) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 587 */         String nextToken = tokens[i + 1];
/* 588 */         if (nextToken.charAt(0) != '(') {
/*     */           continue;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 597 */       PointcutBody body = getPointcutBody(tokens, i);
/* 598 */       i += body.numTokensConsumed;
/*     */       
/* 600 */       if (!nonReferencePointcutTokens.contains(toMatch)) {
/*     */         
/* 602 */         String varName = maybeExtractVariableName(body.text);
/* 603 */         if (varName != null) {
/* 604 */           varNames.add(varName);
/*     */         }
/*     */       } 
/*     */       continue;
/*     */     } 
/* 609 */     if (varNames.size() > 1) {
/* 610 */       throw new AmbiguousBindingException("Found " + varNames.size() + " candidate reference pointcut variables but only one unbound argument slot");
/*     */     }
/*     */     
/* 613 */     if (varNames.size() == 1) {
/* 614 */       for (int j = 0; j < this.parameterNameBindings.length; j++) {
/* 615 */         if (isUnbound(j)) {
/* 616 */           bindParameterName(j, varNames.get(0));
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PointcutBody getPointcutBody(String[] tokens, int startIndex) {
/* 629 */     int numTokensConsumed = 0;
/* 630 */     String currentToken = tokens[startIndex];
/* 631 */     int bodyStart = currentToken.indexOf('(');
/* 632 */     if (currentToken.charAt(currentToken.length() - 1) == ')')
/*     */     {
/* 634 */       return new PointcutBody(0, currentToken.substring(bodyStart + 1, currentToken.length() - 1));
/*     */     }
/*     */     
/* 637 */     StringBuilder sb = new StringBuilder();
/* 638 */     if (bodyStart >= 0 && bodyStart != currentToken.length() - 1) {
/* 639 */       sb.append(currentToken.substring(bodyStart + 1));
/* 640 */       sb.append(" ");
/*     */     } 
/* 642 */     numTokensConsumed++;
/* 643 */     int currentIndex = startIndex + numTokensConsumed;
/* 644 */     while (currentIndex < tokens.length) {
/* 645 */       if (tokens[currentIndex].equals("(")) {
/* 646 */         currentIndex++;
/*     */         
/*     */         continue;
/*     */       } 
/* 650 */       if (tokens[currentIndex].endsWith(")")) {
/* 651 */         sb.append(tokens[currentIndex].substring(0, tokens[currentIndex].length() - 1));
/* 652 */         return new PointcutBody(numTokensConsumed, sb.toString().trim());
/*     */       } 
/*     */       
/* 655 */       String toAppend = tokens[currentIndex];
/* 656 */       if (toAppend.startsWith("(")) {
/* 657 */         toAppend = toAppend.substring(1);
/*     */       }
/* 659 */       sb.append(toAppend);
/* 660 */       sb.append(" ");
/* 661 */       currentIndex++;
/* 662 */       numTokensConsumed++;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 668 */     return new PointcutBody(numTokensConsumed, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeBindPrimitiveArgsFromPointcutExpression() {
/* 675 */     int numUnboundPrimitives = countNumberOfUnboundPrimitiveArguments();
/* 676 */     if (numUnboundPrimitives > 1) {
/* 677 */       throw new AmbiguousBindingException("Found '" + numUnboundPrimitives + "' unbound primitive arguments with no way to distinguish between them.");
/*     */     }
/*     */     
/* 680 */     if (numUnboundPrimitives == 1) {
/*     */       
/* 682 */       List<String> varNames = new ArrayList<>();
/* 683 */       String[] tokens = StringUtils.tokenizeToStringArray(this.pointcutExpression, " "); int i;
/* 684 */       for (i = 0; i < tokens.length; i++) {
/* 685 */         if (tokens[i].equals("args") || tokens[i].startsWith("args(")) {
/* 686 */           PointcutBody body = getPointcutBody(tokens, i);
/* 687 */           i += body.numTokensConsumed;
/* 688 */           maybeExtractVariableNamesFromArgs(body.text, varNames);
/*     */         } 
/*     */       } 
/* 691 */       if (varNames.size() > 1) {
/* 692 */         throw new AmbiguousBindingException("Found " + varNames.size() + " candidate variable names but only one candidate binding slot when matching primitive args");
/*     */       }
/*     */       
/* 695 */       if (varNames.size() == 1)
/*     */       {
/* 697 */         for (i = 0; i < this.argumentTypes.length; i++) {
/* 698 */           if (isUnbound(i) && this.argumentTypes[i].isPrimitive()) {
/* 699 */             bindParameterName(i, varNames.get(0));
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isUnbound(int i) {
/* 712 */     return (this.parameterNameBindings[i] == null);
/*     */   }
/*     */   
/*     */   private boolean alreadyBound(String varName) {
/* 716 */     for (int i = 0; i < this.parameterNameBindings.length; i++) {
/* 717 */       if (!isUnbound(i) && varName.equals(this.parameterNameBindings[i])) {
/* 718 */         return true;
/*     */       }
/*     */     } 
/* 721 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSubtypeOf(Class<?> supertype, int argumentNumber) {
/* 729 */     return supertype.isAssignableFrom(this.argumentTypes[argumentNumber]);
/*     */   }
/*     */   
/*     */   private int countNumberOfUnboundAnnotationArguments() {
/* 733 */     int count = 0;
/* 734 */     for (int i = 0; i < this.argumentTypes.length; i++) {
/* 735 */       if (isUnbound(i) && isSubtypeOf(Annotation.class, i)) {
/* 736 */         count++;
/*     */       }
/*     */     } 
/* 739 */     return count;
/*     */   }
/*     */   
/*     */   private int countNumberOfUnboundPrimitiveArguments() {
/* 743 */     int count = 0;
/* 744 */     for (int i = 0; i < this.argumentTypes.length; i++) {
/* 745 */       if (isUnbound(i) && this.argumentTypes[i].isPrimitive()) {
/* 746 */         count++;
/*     */       }
/*     */     } 
/* 749 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findAndBind(Class<?> argumentType, String varName) {
/* 757 */     for (int i = 0; i < this.argumentTypes.length; i++) {
/* 758 */       if (isUnbound(i) && isSubtypeOf(argumentType, i)) {
/* 759 */         bindParameterName(i, varName);
/*     */         return;
/*     */       } 
/*     */     } 
/* 763 */     throw new IllegalStateException("Expected to find an unbound argument of type '" + argumentType
/* 764 */         .getName() + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PointcutBody
/*     */   {
/*     */     private int numTokensConsumed;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private String text;
/*     */ 
/*     */ 
/*     */     
/*     */     public PointcutBody(int tokens, @Nullable String text) {
/* 780 */       this.numTokensConsumed = tokens;
/* 781 */       this.text = text;
/*     */     }
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
/*     */   public static class AmbiguousBindingException
/*     */     extends RuntimeException
/*     */   {
/*     */     public AmbiguousBindingException(String msg) {
/* 798 */       super(msg);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJAdviceParameterNameDiscoverer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */