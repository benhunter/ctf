/*      */ package org.springframework.expression.spel.standard;
/*      */ 
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Deque;
/*      */ import java.util.List;
/*      */ import java.util.regex.Pattern;
/*      */ import org.springframework.expression.Expression;
/*      */ import org.springframework.expression.ParseException;
/*      */ import org.springframework.expression.ParserContext;
/*      */ import org.springframework.expression.common.TemplateAwareExpressionParser;
/*      */ import org.springframework.expression.spel.InternalParseException;
/*      */ import org.springframework.expression.spel.SpelMessage;
/*      */ import org.springframework.expression.spel.SpelParseException;
/*      */ import org.springframework.expression.spel.SpelParserConfiguration;
/*      */ import org.springframework.expression.spel.ast.Assign;
/*      */ import org.springframework.expression.spel.ast.BeanReference;
/*      */ import org.springframework.expression.spel.ast.BooleanLiteral;
/*      */ import org.springframework.expression.spel.ast.CompoundExpression;
/*      */ import org.springframework.expression.spel.ast.ConstructorReference;
/*      */ import org.springframework.expression.spel.ast.Elvis;
/*      */ import org.springframework.expression.spel.ast.FunctionReference;
/*      */ import org.springframework.expression.spel.ast.Identifier;
/*      */ import org.springframework.expression.spel.ast.Indexer;
/*      */ import org.springframework.expression.spel.ast.InlineList;
/*      */ import org.springframework.expression.spel.ast.InlineMap;
/*      */ import org.springframework.expression.spel.ast.Literal;
/*      */ import org.springframework.expression.spel.ast.MethodReference;
/*      */ import org.springframework.expression.spel.ast.NullLiteral;
/*      */ import org.springframework.expression.spel.ast.OpAnd;
/*      */ import org.springframework.expression.spel.ast.OpDec;
/*      */ import org.springframework.expression.spel.ast.OpDivide;
/*      */ import org.springframework.expression.spel.ast.OpEQ;
/*      */ import org.springframework.expression.spel.ast.OpGE;
/*      */ import org.springframework.expression.spel.ast.OpGT;
/*      */ import org.springframework.expression.spel.ast.OpInc;
/*      */ import org.springframework.expression.spel.ast.OpLE;
/*      */ import org.springframework.expression.spel.ast.OpLT;
/*      */ import org.springframework.expression.spel.ast.OpMinus;
/*      */ import org.springframework.expression.spel.ast.OpModulus;
/*      */ import org.springframework.expression.spel.ast.OpMultiply;
/*      */ import org.springframework.expression.spel.ast.OpNE;
/*      */ import org.springframework.expression.spel.ast.OpOr;
/*      */ import org.springframework.expression.spel.ast.OpPlus;
/*      */ import org.springframework.expression.spel.ast.OperatorBetween;
/*      */ import org.springframework.expression.spel.ast.OperatorInstanceof;
/*      */ import org.springframework.expression.spel.ast.OperatorMatches;
/*      */ import org.springframework.expression.spel.ast.OperatorNot;
/*      */ import org.springframework.expression.spel.ast.OperatorPower;
/*      */ import org.springframework.expression.spel.ast.Projection;
/*      */ import org.springframework.expression.spel.ast.PropertyOrFieldReference;
/*      */ import org.springframework.expression.spel.ast.QualifiedIdentifier;
/*      */ import org.springframework.expression.spel.ast.Selection;
/*      */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*      */ import org.springframework.expression.spel.ast.StringLiteral;
/*      */ import org.springframework.expression.spel.ast.Ternary;
/*      */ import org.springframework.expression.spel.ast.TypeReference;
/*      */ import org.springframework.expression.spel.ast.VariableReference;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.StringUtils;
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
/*      */ class InternalSpelExpressionParser
/*      */   extends TemplateAwareExpressionParser
/*      */ {
/*   90 */   private static final Pattern VALID_QUALIFIED_ID_PATTERN = Pattern.compile("[\\p{L}\\p{N}_$]+");
/*      */ 
/*      */   
/*      */   private final SpelParserConfiguration configuration;
/*      */ 
/*      */   
/*   96 */   private final Deque<SpelNodeImpl> constructedNodes = new ArrayDeque<>();
/*      */ 
/*      */   
/*   99 */   private String expressionString = "";
/*      */ 
/*      */   
/*  102 */   private List<Token> tokenStream = Collections.emptyList();
/*      */ 
/*      */ 
/*      */   
/*      */   private int tokenStreamLength;
/*      */ 
/*      */ 
/*      */   
/*      */   private int tokenStreamPointer;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InternalSpelExpressionParser(SpelParserConfiguration configuration) {
/*  116 */     this.configuration = configuration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SpelExpression doParseExpression(String expressionString, @Nullable ParserContext context) throws ParseException {
/*      */     try {
/*  125 */       this.expressionString = expressionString;
/*  126 */       Tokenizer tokenizer = new Tokenizer(expressionString);
/*  127 */       this.tokenStream = tokenizer.process();
/*  128 */       this.tokenStreamLength = this.tokenStream.size();
/*  129 */       this.tokenStreamPointer = 0;
/*  130 */       this.constructedNodes.clear();
/*  131 */       SpelNodeImpl ast = eatExpression();
/*  132 */       Assert.state((ast != null), "No node");
/*  133 */       Token t = peekToken();
/*  134 */       if (t != null) {
/*  135 */         throw new SpelParseException(t.startPos, SpelMessage.MORE_INPUT, new Object[] { toString(nextToken()) });
/*      */       }
/*  137 */       Assert.isTrue(this.constructedNodes.isEmpty(), "At least one node expected");
/*  138 */       return new SpelExpression(expressionString, ast, this.configuration);
/*      */     }
/*  140 */     catch (InternalParseException ex) {
/*  141 */       throw ex.getCause();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatExpression() {
/*      */     NullLiteral nullLiteral;
/*  153 */     SpelNodeImpl expr = eatLogicalOrExpression();
/*  154 */     Token t = peekToken();
/*  155 */     if (t != null) {
/*  156 */       if (t.kind == TokenKind.ASSIGN) {
/*  157 */         if (expr == null) {
/*  158 */           nullLiteral = new NullLiteral(toPos(t.startPos - 1, t.endPos - 1));
/*      */         }
/*  160 */         nextToken();
/*  161 */         SpelNodeImpl assignedValue = eatLogicalOrExpression();
/*  162 */         return (SpelNodeImpl)new Assign(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)nullLiteral, assignedValue });
/*      */       } 
/*  164 */       if (t.kind == TokenKind.ELVIS) {
/*  165 */         NullLiteral nullLiteral1; if (nullLiteral == null) {
/*  166 */           nullLiteral = new NullLiteral(toPos(t.startPos - 1, t.endPos - 2));
/*      */         }
/*  168 */         nextToken();
/*  169 */         SpelNodeImpl valueIfNull = eatExpression();
/*  170 */         if (valueIfNull == null) {
/*  171 */           nullLiteral1 = new NullLiteral(toPos(t.startPos + 1, t.endPos + 1));
/*      */         }
/*  173 */         return (SpelNodeImpl)new Elvis(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)nullLiteral, (SpelNodeImpl)nullLiteral1 });
/*      */       } 
/*  175 */       if (t.kind == TokenKind.QMARK) {
/*  176 */         if (nullLiteral == null) {
/*  177 */           nullLiteral = new NullLiteral(toPos(t.startPos - 1, t.endPos - 1));
/*      */         }
/*  179 */         nextToken();
/*  180 */         SpelNodeImpl ifTrueExprValue = eatExpression();
/*  181 */         eatToken(TokenKind.COLON);
/*  182 */         SpelNodeImpl ifFalseExprValue = eatExpression();
/*  183 */         return (SpelNodeImpl)new Ternary(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)nullLiteral, ifTrueExprValue, ifFalseExprValue });
/*      */       } 
/*      */     } 
/*  186 */     return (SpelNodeImpl)nullLiteral;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatLogicalOrExpression() {
/*      */     OpOr opOr;
/*  192 */     SpelNodeImpl expr = eatLogicalAndExpression();
/*  193 */     while (peekIdentifierToken("or") || peekToken(TokenKind.SYMBOLIC_OR)) {
/*  194 */       Token t = takeToken();
/*  195 */       SpelNodeImpl rhExpr = eatLogicalAndExpression();
/*  196 */       checkOperands(t, expr, rhExpr);
/*  197 */       opOr = new OpOr(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  199 */     return (SpelNodeImpl)opOr;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatLogicalAndExpression() {
/*      */     OpAnd opAnd;
/*  205 */     SpelNodeImpl expr = eatRelationalExpression();
/*  206 */     while (peekIdentifierToken("and") || peekToken(TokenKind.SYMBOLIC_AND)) {
/*  207 */       Token t = takeToken();
/*  208 */       SpelNodeImpl rhExpr = eatRelationalExpression();
/*  209 */       checkOperands(t, expr, rhExpr);
/*  210 */       opAnd = new OpAnd(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  212 */     return (SpelNodeImpl)opAnd;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatRelationalExpression() {
/*  218 */     SpelNodeImpl expr = eatSumExpression();
/*  219 */     Token relationalOperatorToken = maybeEatRelationalOperator();
/*  220 */     if (relationalOperatorToken != null) {
/*  221 */       Token t = takeToken();
/*  222 */       SpelNodeImpl rhExpr = eatSumExpression();
/*  223 */       checkOperands(t, expr, rhExpr);
/*  224 */       TokenKind tk = relationalOperatorToken.kind;
/*      */       
/*  226 */       if (relationalOperatorToken.isNumericRelationalOperator()) {
/*  227 */         int pos = toPos(t);
/*  228 */         if (tk == TokenKind.GT) {
/*  229 */           return (SpelNodeImpl)new OpGT(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  231 */         if (tk == TokenKind.LT) {
/*  232 */           return (SpelNodeImpl)new OpLT(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  234 */         if (tk == TokenKind.LE) {
/*  235 */           return (SpelNodeImpl)new OpLE(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  237 */         if (tk == TokenKind.GE) {
/*  238 */           return (SpelNodeImpl)new OpGE(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  240 */         if (tk == TokenKind.EQ) {
/*  241 */           return (SpelNodeImpl)new OpEQ(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  243 */         Assert.isTrue((tk == TokenKind.NE), "Not-equals token expected");
/*  244 */         return (SpelNodeImpl)new OpNE(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */       } 
/*      */       
/*  247 */       if (tk == TokenKind.INSTANCEOF) {
/*  248 */         return (SpelNodeImpl)new OperatorInstanceof(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */       }
/*      */       
/*  251 */       if (tk == TokenKind.MATCHES) {
/*  252 */         return (SpelNodeImpl)new OperatorMatches(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */       }
/*      */       
/*  255 */       Assert.isTrue((tk == TokenKind.BETWEEN), "Between token expected");
/*  256 */       return (SpelNodeImpl)new OperatorBetween(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  258 */     return expr;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatSumExpression() {
/*      */     OpMinus opMinus;
/*  264 */     SpelNodeImpl expr = eatProductExpression();
/*  265 */     while (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.INC)) {
/*  266 */       OpPlus opPlus; Token t = takeToken();
/*  267 */       SpelNodeImpl rhExpr = eatProductExpression();
/*  268 */       checkRightOperand(t, rhExpr);
/*  269 */       if (t.kind == TokenKind.PLUS) {
/*  270 */         opPlus = new OpPlus(toPos(t), new SpelNodeImpl[] { expr, rhExpr }); continue;
/*      */       } 
/*  272 */       if (t.kind == TokenKind.MINUS) {
/*  273 */         opMinus = new OpMinus(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)opPlus, rhExpr });
/*      */       }
/*      */     } 
/*  276 */     return (SpelNodeImpl)opMinus;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatProductExpression() {
/*      */     OpModulus opModulus;
/*  282 */     SpelNodeImpl expr = eatPowerIncDecExpression();
/*  283 */     while (peekToken(TokenKind.STAR, TokenKind.DIV, TokenKind.MOD)) {
/*  284 */       OpMultiply opMultiply; OpDivide opDivide; Token t = takeToken();
/*  285 */       SpelNodeImpl rhExpr = eatPowerIncDecExpression();
/*  286 */       checkOperands(t, expr, rhExpr);
/*  287 */       if (t.kind == TokenKind.STAR) {
/*  288 */         opMultiply = new OpMultiply(toPos(t), new SpelNodeImpl[] { expr, rhExpr }); continue;
/*      */       } 
/*  290 */       if (t.kind == TokenKind.DIV) {
/*  291 */         opDivide = new OpDivide(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)opMultiply, rhExpr });
/*      */         continue;
/*      */       } 
/*  294 */       Assert.isTrue((t.kind == TokenKind.MOD), "Mod token expected");
/*  295 */       opModulus = new OpModulus(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)opDivide, rhExpr });
/*      */     } 
/*      */     
/*  298 */     return (SpelNodeImpl)opModulus;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatPowerIncDecExpression() {
/*  304 */     SpelNodeImpl expr = eatUnaryExpression();
/*  305 */     if (peekToken(TokenKind.POWER)) {
/*  306 */       Token t = takeToken();
/*  307 */       SpelNodeImpl rhExpr = eatUnaryExpression();
/*  308 */       checkRightOperand(t, rhExpr);
/*  309 */       return (SpelNodeImpl)new OperatorPower(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  311 */     if (expr != null && peekToken(TokenKind.INC, TokenKind.DEC)) {
/*  312 */       Token t = takeToken();
/*  313 */       if (t.getKind() == TokenKind.INC) {
/*  314 */         return (SpelNodeImpl)new OpInc(toPos(t), true, new SpelNodeImpl[] { expr });
/*      */       }
/*  316 */       return (SpelNodeImpl)new OpDec(toPos(t), true, new SpelNodeImpl[] { expr });
/*      */     } 
/*  318 */     return expr;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatUnaryExpression() {
/*  324 */     if (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.NOT)) {
/*  325 */       Token t = takeToken();
/*  326 */       SpelNodeImpl expr = eatUnaryExpression();
/*  327 */       Assert.state((expr != null), "No node");
/*  328 */       if (t.kind == TokenKind.NOT) {
/*  329 */         return (SpelNodeImpl)new OperatorNot(toPos(t), expr);
/*      */       }
/*  331 */       if (t.kind == TokenKind.PLUS) {
/*  332 */         return (SpelNodeImpl)new OpPlus(toPos(t), new SpelNodeImpl[] { expr });
/*      */       }
/*  334 */       Assert.isTrue((t.kind == TokenKind.MINUS), "Minus token expected");
/*  335 */       return (SpelNodeImpl)new OpMinus(toPos(t), new SpelNodeImpl[] { expr });
/*      */     } 
/*  337 */     if (peekToken(TokenKind.INC, TokenKind.DEC)) {
/*  338 */       Token t = takeToken();
/*  339 */       SpelNodeImpl expr = eatUnaryExpression();
/*  340 */       if (t.getKind() == TokenKind.INC) {
/*  341 */         return (SpelNodeImpl)new OpInc(toPos(t), false, new SpelNodeImpl[] { expr });
/*      */       }
/*  343 */       return (SpelNodeImpl)new OpDec(toPos(t), false, new SpelNodeImpl[] { expr });
/*      */     } 
/*  345 */     return eatPrimaryExpression();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatPrimaryExpression() {
/*  351 */     SpelNodeImpl start = eatStartNode();
/*  352 */     List<SpelNodeImpl> nodes = null;
/*  353 */     SpelNodeImpl node = eatNode();
/*  354 */     while (node != null) {
/*  355 */       if (nodes == null) {
/*  356 */         nodes = new ArrayList<>(4);
/*  357 */         nodes.add(start);
/*      */       } 
/*  359 */       nodes.add(node);
/*  360 */       node = eatNode();
/*      */     } 
/*  362 */     if (start == null || nodes == null) {
/*  363 */       return start;
/*      */     }
/*  365 */     return (SpelNodeImpl)new CompoundExpression(toPos(start.getStartPosition(), ((SpelNodeImpl)nodes
/*  366 */           .get(nodes.size() - 1)).getEndPosition()), nodes
/*  367 */         .<SpelNodeImpl>toArray(new SpelNodeImpl[0]));
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatNode() {
/*  373 */     return peekToken(TokenKind.DOT, TokenKind.SAFE_NAVI) ? eatDottedNode() : eatNonDottedNode();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatNonDottedNode() {
/*  379 */     if (peekToken(TokenKind.LSQUARE) && 
/*  380 */       maybeEatIndexer()) {
/*  381 */       return pop();
/*      */     }
/*      */     
/*  384 */     return null;
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
/*      */   private SpelNodeImpl eatDottedNode() {
/*  397 */     Token t = takeToken();
/*  398 */     boolean nullSafeNavigation = (t.kind == TokenKind.SAFE_NAVI);
/*  399 */     if (maybeEatMethodOrProperty(nullSafeNavigation) || maybeEatFunctionOrVar() || 
/*  400 */       maybeEatProjection(nullSafeNavigation) || maybeEatSelection(nullSafeNavigation)) {
/*  401 */       return pop();
/*      */     }
/*  403 */     if (peekToken() == null)
/*      */     {
/*  405 */       throw internalException(t.startPos, SpelMessage.OOD, new Object[0]);
/*      */     }
/*      */     
/*  408 */     throw internalException(t.startPos, SpelMessage.UNEXPECTED_DATA_AFTER_DOT, new Object[] { toString(peekToken()) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatFunctionOrVar() {
/*  419 */     if (!peekToken(TokenKind.HASH)) {
/*  420 */       return false;
/*      */     }
/*  422 */     Token t = takeToken();
/*  423 */     Token functionOrVariableName = eatToken(TokenKind.IDENTIFIER);
/*  424 */     SpelNodeImpl[] args = maybeEatMethodArgs();
/*  425 */     if (args == null) {
/*  426 */       push((SpelNodeImpl)new VariableReference(functionOrVariableName.stringValue(), 
/*  427 */             toPos(t.startPos, functionOrVariableName.endPos)));
/*  428 */       return true;
/*      */     } 
/*      */     
/*  431 */     push((SpelNodeImpl)new FunctionReference(functionOrVariableName.stringValue(), 
/*  432 */           toPos(t.startPos, functionOrVariableName.endPos), args));
/*  433 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl[] maybeEatMethodArgs() {
/*  439 */     if (!peekToken(TokenKind.LPAREN)) {
/*  440 */       return null;
/*      */     }
/*  442 */     List<SpelNodeImpl> args = new ArrayList<>();
/*  443 */     consumeArguments(args);
/*  444 */     eatToken(TokenKind.RPAREN);
/*  445 */     return args.<SpelNodeImpl>toArray(new SpelNodeImpl[0]);
/*      */   }
/*      */   
/*      */   private void eatConstructorArgs(List<SpelNodeImpl> accumulatedArguments) {
/*  449 */     if (!peekToken(TokenKind.LPAREN)) {
/*  450 */       throw new InternalParseException(new SpelParseException(this.expressionString, 
/*  451 */             positionOf(peekToken()), SpelMessage.MISSING_CONSTRUCTOR_ARGS, new Object[0]));
/*      */     }
/*  453 */     consumeArguments(accumulatedArguments);
/*  454 */     eatToken(TokenKind.RPAREN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void consumeArguments(List<SpelNodeImpl> accumulatedArguments) {
/*  461 */     Token next, t = peekToken();
/*  462 */     Assert.state((t != null), "Expected token");
/*  463 */     int pos = t.startPos;
/*      */     
/*      */     do {
/*  466 */       nextToken();
/*  467 */       t = peekToken();
/*  468 */       if (t == null) {
/*  469 */         throw internalException(pos, SpelMessage.RUN_OUT_OF_ARGUMENTS, new Object[0]);
/*      */       }
/*  471 */       if (t.kind != TokenKind.RPAREN) {
/*  472 */         accumulatedArguments.add(eatExpression());
/*      */       }
/*  474 */       next = peekToken();
/*      */     }
/*  476 */     while (next != null && next.kind == TokenKind.COMMA);
/*      */     
/*  478 */     if (next == null) {
/*  479 */       throw internalException(pos, SpelMessage.RUN_OUT_OF_ARGUMENTS, new Object[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   private int positionOf(@Nullable Token t) {
/*  484 */     if (t == null)
/*      */     {
/*      */       
/*  487 */       return this.expressionString.length();
/*      */     }
/*  489 */     return t.startPos;
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
/*      */   private SpelNodeImpl eatStartNode() {
/*  505 */     if (maybeEatLiteral()) {
/*  506 */       return pop();
/*      */     }
/*  508 */     if (maybeEatParenExpression()) {
/*  509 */       return pop();
/*      */     }
/*  511 */     if (maybeEatTypeReference() || maybeEatNullReference() || maybeEatConstructorReference() || 
/*  512 */       maybeEatMethodOrProperty(false) || maybeEatFunctionOrVar()) {
/*  513 */       return pop();
/*      */     }
/*  515 */     if (maybeEatBeanReference()) {
/*  516 */       return pop();
/*      */     }
/*  518 */     if (maybeEatProjection(false) || maybeEatSelection(false) || maybeEatIndexer()) {
/*  519 */       return pop();
/*      */     }
/*  521 */     if (maybeEatInlineListOrMap()) {
/*  522 */       return pop();
/*      */     }
/*      */     
/*  525 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatBeanReference() {
/*  532 */     if (peekToken(TokenKind.BEAN_REF) || peekToken(TokenKind.FACTORY_BEAN_REF)) {
/*  533 */       BeanReference beanReference; Token beanRefToken = takeToken();
/*  534 */       Token beanNameToken = null;
/*  535 */       String beanName = null;
/*  536 */       if (peekToken(TokenKind.IDENTIFIER)) {
/*  537 */         beanNameToken = eatToken(TokenKind.IDENTIFIER);
/*  538 */         beanName = beanNameToken.stringValue();
/*      */       }
/*  540 */       else if (peekToken(TokenKind.LITERAL_STRING)) {
/*  541 */         beanNameToken = eatToken(TokenKind.LITERAL_STRING);
/*  542 */         beanName = beanNameToken.stringValue();
/*  543 */         beanName = beanName.substring(1, beanName.length() - 1);
/*      */       } else {
/*      */         
/*  546 */         throw internalException(beanRefToken.startPos, SpelMessage.INVALID_BEAN_REFERENCE, new Object[0]);
/*      */       } 
/*      */       
/*  549 */       if (beanRefToken.getKind() == TokenKind.FACTORY_BEAN_REF) {
/*  550 */         String beanNameString = String.valueOf(TokenKind.FACTORY_BEAN_REF.tokenChars) + beanName;
/*      */         
/*  552 */         beanReference = new BeanReference(toPos(beanRefToken.startPos, beanNameToken.endPos), beanNameString);
/*      */       } else {
/*      */         
/*  555 */         beanReference = new BeanReference(toPos(beanNameToken), beanName);
/*      */       } 
/*  557 */       this.constructedNodes.push(beanReference);
/*  558 */       return true;
/*      */     } 
/*  560 */     return false;
/*      */   }
/*      */   
/*      */   private boolean maybeEatTypeReference() {
/*  564 */     if (peekToken(TokenKind.IDENTIFIER)) {
/*  565 */       Token typeName = peekToken();
/*  566 */       Assert.state((typeName != null), "Expected token");
/*  567 */       if (!"T".equals(typeName.stringValue())) {
/*  568 */         return false;
/*      */       }
/*      */       
/*  571 */       Token t = takeToken();
/*  572 */       if (peekToken(TokenKind.RSQUARE)) {
/*      */         
/*  574 */         push((SpelNodeImpl)new PropertyOrFieldReference(false, t.stringValue(), toPos(t)));
/*  575 */         return true;
/*      */       } 
/*  577 */       eatToken(TokenKind.LPAREN);
/*  578 */       SpelNodeImpl node = eatPossiblyQualifiedId();
/*      */ 
/*      */       
/*  581 */       int dims = 0;
/*  582 */       while (peekToken(TokenKind.LSQUARE, true)) {
/*  583 */         eatToken(TokenKind.RSQUARE);
/*  584 */         dims++;
/*      */       } 
/*  586 */       eatToken(TokenKind.RPAREN);
/*  587 */       this.constructedNodes.push(new TypeReference(toPos(typeName), node, dims));
/*  588 */       return true;
/*      */     } 
/*  590 */     return false;
/*      */   }
/*      */   
/*      */   private boolean maybeEatNullReference() {
/*  594 */     if (peekToken(TokenKind.IDENTIFIER)) {
/*  595 */       Token nullToken = peekToken();
/*  596 */       Assert.state((nullToken != null), "Expected token");
/*  597 */       if (!"null".equalsIgnoreCase(nullToken.stringValue())) {
/*  598 */         return false;
/*      */       }
/*  600 */       nextToken();
/*  601 */       this.constructedNodes.push(new NullLiteral(toPos(nullToken)));
/*  602 */       return true;
/*      */     } 
/*  604 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean maybeEatProjection(boolean nullSafeNavigation) {
/*  609 */     Token t = peekToken();
/*  610 */     if (!peekToken(TokenKind.PROJECT, true)) {
/*  611 */       return false;
/*      */     }
/*  613 */     Assert.state((t != null), "No token");
/*  614 */     SpelNodeImpl expr = eatExpression();
/*  615 */     Assert.state((expr != null), "No node");
/*  616 */     eatToken(TokenKind.RSQUARE);
/*  617 */     this.constructedNodes.push(new Projection(nullSafeNavigation, toPos(t), expr));
/*  618 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean maybeEatInlineListOrMap() {
/*      */     InlineMap inlineMap;
/*  624 */     Token t = peekToken();
/*  625 */     if (!peekToken(TokenKind.LCURLY, true)) {
/*  626 */       return false;
/*      */     }
/*  628 */     Assert.state((t != null), "No token");
/*  629 */     SpelNodeImpl expr = null;
/*  630 */     Token closingCurly = peekToken();
/*  631 */     if (peekToken(TokenKind.RCURLY, true))
/*      */     
/*  633 */     { Assert.state((closingCurly != null), "No token");
/*  634 */       InlineList inlineList = new InlineList(toPos(t.startPos, closingCurly.endPos), new SpelNodeImpl[0]); }
/*      */     
/*  636 */     else if (peekToken(TokenKind.COLON, true))
/*  637 */     { closingCurly = eatToken(TokenKind.RCURLY);
/*      */       
/*  639 */       inlineMap = new InlineMap(toPos(t.startPos, closingCurly.endPos), new SpelNodeImpl[0]); }
/*      */     else
/*      */     
/*  642 */     { SpelNodeImpl firstExpression = eatExpression();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  647 */       if (peekToken(TokenKind.RCURLY))
/*  648 */       { List<SpelNodeImpl> elements = new ArrayList<>();
/*  649 */         elements.add(firstExpression);
/*  650 */         closingCurly = eatToken(TokenKind.RCURLY);
/*  651 */         InlineList inlineList = new InlineList(toPos(t.startPos, closingCurly.endPos), elements.<SpelNodeImpl>toArray(new SpelNodeImpl[0])); }
/*      */       else
/*  653 */       { if (peekToken(TokenKind.COMMA, true))
/*  654 */         { List<SpelNodeImpl> elements = new ArrayList<>();
/*  655 */           elements.add(firstExpression);
/*      */           while (true)
/*  657 */           { elements.add(eatExpression());
/*      */             
/*  659 */             if (!peekToken(TokenKind.COMMA, true))
/*  660 */             { closingCurly = eatToken(TokenKind.RCURLY);
/*  661 */               InlineList inlineList = new InlineList(toPos(t.startPos, closingCurly.endPos), elements.<SpelNodeImpl>toArray(new SpelNodeImpl[0]));
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
/*  680 */               this.constructedNodes.push(inlineList);
/*  681 */               return true; }  }  }  if (peekToken(TokenKind.COLON, true)) { List<SpelNodeImpl> elements = new ArrayList<>(); elements.add(firstExpression); elements.add(eatExpression()); while (peekToken(TokenKind.COMMA, true)) { elements.add(eatExpression()); eatToken(TokenKind.COLON); elements.add(eatExpression()); }  closingCurly = eatToken(TokenKind.RCURLY); inlineMap = new InlineMap(toPos(t.startPos, closingCurly.endPos), elements.<SpelNodeImpl>toArray(new SpelNodeImpl[0])); } else { throw internalException(t.startPos, SpelMessage.OOD, new Object[0]); }  }  }  this.constructedNodes.push(inlineMap); return true;
/*      */   }
/*      */   
/*      */   private boolean maybeEatIndexer() {
/*  685 */     Token t = peekToken();
/*  686 */     if (!peekToken(TokenKind.LSQUARE, true)) {
/*  687 */       return false;
/*      */     }
/*  689 */     Assert.state((t != null), "No token");
/*  690 */     SpelNodeImpl expr = eatExpression();
/*  691 */     Assert.state((expr != null), "No node");
/*  692 */     eatToken(TokenKind.RSQUARE);
/*  693 */     this.constructedNodes.push(new Indexer(toPos(t), expr));
/*  694 */     return true;
/*      */   }
/*      */   
/*      */   private boolean maybeEatSelection(boolean nullSafeNavigation) {
/*  698 */     Token t = peekToken();
/*  699 */     if (!peekSelectToken()) {
/*  700 */       return false;
/*      */     }
/*  702 */     Assert.state((t != null), "No token");
/*  703 */     nextToken();
/*  704 */     SpelNodeImpl expr = eatExpression();
/*  705 */     if (expr == null) {
/*  706 */       throw internalException(toPos(t), SpelMessage.MISSING_SELECTION_EXPRESSION, new Object[0]);
/*      */     }
/*  708 */     eatToken(TokenKind.RSQUARE);
/*  709 */     if (t.kind == TokenKind.SELECT_FIRST) {
/*  710 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 1, toPos(t), expr));
/*      */     }
/*  712 */     else if (t.kind == TokenKind.SELECT_LAST) {
/*  713 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 2, toPos(t), expr));
/*      */     } else {
/*      */       
/*  716 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 0, toPos(t), expr));
/*      */     } 
/*  718 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SpelNodeImpl eatPossiblyQualifiedId() {
/*  726 */     Deque<SpelNodeImpl> qualifiedIdPieces = new ArrayDeque<>();
/*  727 */     Token node = peekToken();
/*  728 */     while (isValidQualifiedId(node)) {
/*  729 */       nextToken();
/*  730 */       if (node.kind != TokenKind.DOT) {
/*  731 */         qualifiedIdPieces.add(new Identifier(node.stringValue(), toPos(node)));
/*      */       }
/*  733 */       node = peekToken();
/*      */     } 
/*  735 */     if (qualifiedIdPieces.isEmpty()) {
/*  736 */       if (node == null) {
/*  737 */         throw internalException(this.expressionString.length(), SpelMessage.OOD, new Object[0]);
/*      */       }
/*  739 */       throw internalException(node.startPos, SpelMessage.NOT_EXPECTED_TOKEN, new Object[] { "qualified ID", node
/*  740 */             .getKind().toString().toLowerCase() });
/*      */     } 
/*  742 */     int pos = toPos(((SpelNodeImpl)qualifiedIdPieces.getFirst()).getStartPosition(), ((SpelNodeImpl)qualifiedIdPieces.getLast()).getEndPosition());
/*  743 */     return (SpelNodeImpl)new QualifiedIdentifier(pos, (SpelNodeImpl[])qualifiedIdPieces.toArray((Object[])new SpelNodeImpl[0]));
/*      */   }
/*      */   
/*      */   private boolean isValidQualifiedId(@Nullable Token node) {
/*  747 */     if (node == null || node.kind == TokenKind.LITERAL_STRING) {
/*  748 */       return false;
/*      */     }
/*  750 */     if (node.kind == TokenKind.DOT || node.kind == TokenKind.IDENTIFIER) {
/*  751 */       return true;
/*      */     }
/*  753 */     String value = node.stringValue();
/*  754 */     return (StringUtils.hasLength(value) && VALID_QUALIFIED_ID_PATTERN.matcher(value).matches());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatMethodOrProperty(boolean nullSafeNavigation) {
/*  761 */     if (peekToken(TokenKind.IDENTIFIER)) {
/*  762 */       Token methodOrPropertyName = takeToken();
/*  763 */       SpelNodeImpl[] args = maybeEatMethodArgs();
/*  764 */       if (args == null) {
/*      */         
/*  766 */         push((SpelNodeImpl)new PropertyOrFieldReference(nullSafeNavigation, methodOrPropertyName.stringValue(), 
/*  767 */               toPos(methodOrPropertyName)));
/*  768 */         return true;
/*      */       } 
/*      */       
/*  771 */       push((SpelNodeImpl)new MethodReference(nullSafeNavigation, methodOrPropertyName.stringValue(), 
/*  772 */             toPos(methodOrPropertyName), args));
/*      */       
/*  774 */       return true;
/*      */     } 
/*  776 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatConstructorReference() {
/*  782 */     if (peekIdentifierToken("new")) {
/*  783 */       Token newToken = takeToken();
/*      */       
/*  785 */       if (peekToken(TokenKind.RSQUARE)) {
/*      */         
/*  787 */         push((SpelNodeImpl)new PropertyOrFieldReference(false, newToken.stringValue(), toPos(newToken)));
/*  788 */         return true;
/*      */       } 
/*  790 */       SpelNodeImpl possiblyQualifiedConstructorName = eatPossiblyQualifiedId();
/*  791 */       List<SpelNodeImpl> nodes = new ArrayList<>();
/*  792 */       nodes.add(possiblyQualifiedConstructorName);
/*  793 */       if (peekToken(TokenKind.LSQUARE)) {
/*      */         
/*  795 */         List<SpelNodeImpl> dimensions = new ArrayList<>();
/*  796 */         while (peekToken(TokenKind.LSQUARE, true)) {
/*  797 */           if (!peekToken(TokenKind.RSQUARE)) {
/*  798 */             dimensions.add(eatExpression());
/*      */           } else {
/*      */             
/*  801 */             dimensions.add(null);
/*      */           } 
/*  803 */           eatToken(TokenKind.RSQUARE);
/*      */         } 
/*  805 */         if (maybeEatInlineListOrMap()) {
/*  806 */           nodes.add(pop());
/*      */         }
/*  808 */         push((SpelNodeImpl)new ConstructorReference(toPos(newToken), dimensions
/*  809 */               .<SpelNodeImpl>toArray(new SpelNodeImpl[0]), nodes.<SpelNodeImpl>toArray(new SpelNodeImpl[0])));
/*      */       }
/*      */       else {
/*      */         
/*  813 */         eatConstructorArgs(nodes);
/*      */         
/*  815 */         push((SpelNodeImpl)new ConstructorReference(toPos(newToken), nodes.<SpelNodeImpl>toArray(new SpelNodeImpl[0])));
/*      */       } 
/*  817 */       return true;
/*      */     } 
/*  819 */     return false;
/*      */   }
/*      */   
/*      */   private void push(SpelNodeImpl newNode) {
/*  823 */     this.constructedNodes.push(newNode);
/*      */   }
/*      */   
/*      */   private SpelNodeImpl pop() {
/*  827 */     return this.constructedNodes.pop();
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
/*      */   private boolean maybeEatLiteral() {
/*  839 */     Token t = peekToken();
/*  840 */     if (t == null) {
/*  841 */       return false;
/*      */     }
/*  843 */     if (t.kind == TokenKind.LITERAL_INT) {
/*  844 */       push((SpelNodeImpl)Literal.getIntLiteral(t.stringValue(), toPos(t), 10));
/*      */     }
/*  846 */     else if (t.kind == TokenKind.LITERAL_LONG) {
/*  847 */       push((SpelNodeImpl)Literal.getLongLiteral(t.stringValue(), toPos(t), 10));
/*      */     }
/*  849 */     else if (t.kind == TokenKind.LITERAL_HEXINT) {
/*  850 */       push((SpelNodeImpl)Literal.getIntLiteral(t.stringValue(), toPos(t), 16));
/*      */     }
/*  852 */     else if (t.kind == TokenKind.LITERAL_HEXLONG) {
/*  853 */       push((SpelNodeImpl)Literal.getLongLiteral(t.stringValue(), toPos(t), 16));
/*      */     }
/*  855 */     else if (t.kind == TokenKind.LITERAL_REAL) {
/*  856 */       push((SpelNodeImpl)Literal.getRealLiteral(t.stringValue(), toPos(t), false));
/*      */     }
/*  858 */     else if (t.kind == TokenKind.LITERAL_REAL_FLOAT) {
/*  859 */       push((SpelNodeImpl)Literal.getRealLiteral(t.stringValue(), toPos(t), true));
/*      */     }
/*  861 */     else if (peekIdentifierToken("true")) {
/*  862 */       push((SpelNodeImpl)new BooleanLiteral(t.stringValue(), toPos(t), true));
/*      */     }
/*  864 */     else if (peekIdentifierToken("false")) {
/*  865 */       push((SpelNodeImpl)new BooleanLiteral(t.stringValue(), toPos(t), false));
/*      */     }
/*  867 */     else if (t.kind == TokenKind.LITERAL_STRING) {
/*  868 */       push((SpelNodeImpl)new StringLiteral(t.stringValue(), toPos(t), t.stringValue()));
/*      */     } else {
/*      */       
/*  871 */       return false;
/*      */     } 
/*  873 */     nextToken();
/*  874 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean maybeEatParenExpression() {
/*  879 */     if (peekToken(TokenKind.LPAREN)) {
/*  880 */       nextToken();
/*  881 */       SpelNodeImpl expr = eatExpression();
/*  882 */       Assert.state((expr != null), "No node");
/*  883 */       eatToken(TokenKind.RPAREN);
/*  884 */       push(expr);
/*  885 */       return true;
/*      */     } 
/*      */     
/*  888 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Token maybeEatRelationalOperator() {
/*  897 */     Token t = peekToken();
/*  898 */     if (t == null) {
/*  899 */       return null;
/*      */     }
/*  901 */     if (t.isNumericRelationalOperator()) {
/*  902 */       return t;
/*      */     }
/*  904 */     if (t.isIdentifier()) {
/*  905 */       String idString = t.stringValue();
/*  906 */       if (idString.equalsIgnoreCase("instanceof")) {
/*  907 */         return t.asInstanceOfToken();
/*      */       }
/*  909 */       if (idString.equalsIgnoreCase("matches")) {
/*  910 */         return t.asMatchesToken();
/*      */       }
/*  912 */       if (idString.equalsIgnoreCase("between")) {
/*  913 */         return t.asBetweenToken();
/*      */       }
/*      */     } 
/*  916 */     return null;
/*      */   }
/*      */   
/*      */   private Token eatToken(TokenKind expectedKind) {
/*  920 */     Token t = nextToken();
/*  921 */     if (t == null) {
/*  922 */       int pos = this.expressionString.length();
/*  923 */       throw internalException(pos, SpelMessage.OOD, new Object[0]);
/*      */     } 
/*  925 */     if (t.kind != expectedKind) {
/*  926 */       throw internalException(t.startPos, SpelMessage.NOT_EXPECTED_TOKEN, new Object[] { expectedKind
/*  927 */             .toString().toLowerCase(), t.getKind().toString().toLowerCase() });
/*      */     }
/*  929 */     return t;
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind desiredTokenKind) {
/*  933 */     return peekToken(desiredTokenKind, false);
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind desiredTokenKind, boolean consumeIfMatched) {
/*  937 */     Token t = peekToken();
/*  938 */     if (t == null) {
/*  939 */       return false;
/*      */     }
/*  941 */     if (t.kind == desiredTokenKind) {
/*  942 */       if (consumeIfMatched) {
/*  943 */         this.tokenStreamPointer++;
/*      */       }
/*  945 */       return true;
/*      */     } 
/*      */     
/*  948 */     if (desiredTokenKind == TokenKind.IDENTIFIER)
/*      */     {
/*      */ 
/*      */       
/*  952 */       if (t.kind.ordinal() >= TokenKind.DIV.ordinal() && t.kind.ordinal() <= TokenKind.NOT.ordinal() && t.data != null)
/*      */       {
/*      */         
/*  955 */         return true;
/*      */       }
/*      */     }
/*  958 */     return false;
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind possible1, TokenKind possible2) {
/*  962 */     Token t = peekToken();
/*  963 */     if (t == null) {
/*  964 */       return false;
/*      */     }
/*  966 */     return (t.kind == possible1 || t.kind == possible2);
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind possible1, TokenKind possible2, TokenKind possible3) {
/*  970 */     Token t = peekToken();
/*  971 */     if (t == null) {
/*  972 */       return false;
/*      */     }
/*  974 */     return (t.kind == possible1 || t.kind == possible2 || t.kind == possible3);
/*      */   }
/*      */   
/*      */   private boolean peekIdentifierToken(String identifierString) {
/*  978 */     Token t = peekToken();
/*  979 */     if (t == null) {
/*  980 */       return false;
/*      */     }
/*  982 */     return (t.kind == TokenKind.IDENTIFIER && identifierString.equalsIgnoreCase(t.stringValue()));
/*      */   }
/*      */   
/*      */   private boolean peekSelectToken() {
/*  986 */     Token t = peekToken();
/*  987 */     if (t == null) {
/*  988 */       return false;
/*      */     }
/*  990 */     return (t.kind == TokenKind.SELECT || t.kind == TokenKind.SELECT_FIRST || t.kind == TokenKind.SELECT_LAST);
/*      */   }
/*      */   
/*      */   private Token takeToken() {
/*  994 */     if (this.tokenStreamPointer >= this.tokenStreamLength) {
/*  995 */       throw new IllegalStateException("No token");
/*      */     }
/*  997 */     return this.tokenStream.get(this.tokenStreamPointer++);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Token nextToken() {
/* 1002 */     if (this.tokenStreamPointer >= this.tokenStreamLength) {
/* 1003 */       return null;
/*      */     }
/* 1005 */     return this.tokenStream.get(this.tokenStreamPointer++);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Token peekToken() {
/* 1010 */     if (this.tokenStreamPointer >= this.tokenStreamLength) {
/* 1011 */       return null;
/*      */     }
/* 1013 */     return this.tokenStream.get(this.tokenStreamPointer);
/*      */   }
/*      */   
/*      */   public String toString(@Nullable Token t) {
/* 1017 */     if (t == null) {
/* 1018 */       return "";
/*      */     }
/* 1020 */     if (t.getKind().hasPayload()) {
/* 1021 */       return t.stringValue();
/*      */     }
/* 1023 */     return t.kind.toString().toLowerCase();
/*      */   }
/*      */   
/*      */   private void checkOperands(Token token, @Nullable SpelNodeImpl left, @Nullable SpelNodeImpl right) {
/* 1027 */     checkLeftOperand(token, left);
/* 1028 */     checkRightOperand(token, right);
/*      */   }
/*      */   
/*      */   private void checkLeftOperand(Token token, @Nullable SpelNodeImpl operandExpression) {
/* 1032 */     if (operandExpression == null) {
/* 1033 */       throw internalException(token.startPos, SpelMessage.LEFT_OPERAND_PROBLEM, new Object[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkRightOperand(Token token, @Nullable SpelNodeImpl operandExpression) {
/* 1038 */     if (operandExpression == null) {
/* 1039 */       throw internalException(token.startPos, SpelMessage.RIGHT_OPERAND_PROBLEM, new Object[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   private InternalParseException internalException(int pos, SpelMessage message, Object... inserts) {
/* 1044 */     return new InternalParseException(new SpelParseException(this.expressionString, pos, message, inserts));
/*      */   }
/*      */ 
/*      */   
/*      */   private int toPos(Token t) {
/* 1049 */     return (t.startPos << 16) + t.endPos;
/*      */   }
/*      */   
/*      */   private int toPos(int start, int end) {
/* 1053 */     return (start << 16) + end;
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/standard/InternalSpelExpressionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */