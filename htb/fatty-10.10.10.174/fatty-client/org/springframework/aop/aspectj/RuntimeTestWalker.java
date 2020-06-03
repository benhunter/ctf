/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.aspectj.weaver.ReferenceType;
/*     */ import org.aspectj.weaver.ReferenceTypeDelegate;
/*     */ import org.aspectj.weaver.ResolvedType;
/*     */ import org.aspectj.weaver.ast.And;
/*     */ import org.aspectj.weaver.ast.Call;
/*     */ import org.aspectj.weaver.ast.FieldGetCall;
/*     */ import org.aspectj.weaver.ast.HasAnnotation;
/*     */ import org.aspectj.weaver.ast.ITestVisitor;
/*     */ import org.aspectj.weaver.ast.Instanceof;
/*     */ import org.aspectj.weaver.ast.Literal;
/*     */ import org.aspectj.weaver.ast.Not;
/*     */ import org.aspectj.weaver.ast.Or;
/*     */ import org.aspectj.weaver.ast.Test;
/*     */ import org.aspectj.weaver.internal.tools.MatchingContextBasedTest;
/*     */ import org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate;
/*     */ import org.aspectj.weaver.reflect.ReflectionVar;
/*     */ import org.aspectj.weaver.reflect.ShadowMatchImpl;
/*     */ import org.aspectj.weaver.tools.ShadowMatch;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RuntimeTestWalker
/*     */ {
/*     */   private static final Field residualTestField;
/*     */   private static final Field varTypeField;
/*     */   private static final Field myClassField;
/*     */   @Nullable
/*     */   private final Test runtimeTest;
/*     */   
/*     */   static {
/*     */     try {
/*  71 */       residualTestField = ShadowMatchImpl.class.getDeclaredField("residualTest");
/*  72 */       varTypeField = ReflectionVar.class.getDeclaredField("varType");
/*  73 */       myClassField = ReflectionBasedReferenceTypeDelegate.class.getDeclaredField("myClass");
/*     */     }
/*  75 */     catch (NoSuchFieldException ex) {
/*  76 */       throw new IllegalStateException("The version of aspectjtools.jar / aspectjweaver.jar on the classpath is incompatible with this version of Spring: " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RuntimeTestWalker(ShadowMatch shadowMatch) {
/*     */     try {
/*  88 */       ReflectionUtils.makeAccessible(residualTestField);
/*  89 */       this.runtimeTest = (Test)residualTestField.get(shadowMatch);
/*     */     }
/*  91 */     catch (IllegalAccessException ex) {
/*  92 */       throw new IllegalStateException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean testsSubtypeSensitiveVars() {
/* 102 */     return (this.runtimeTest != null && (new SubtypeSensitiveVarTypeTestVisitor())
/* 103 */       .testsSubtypeSensitiveVars(this.runtimeTest));
/*     */   }
/*     */   
/*     */   public boolean testThisInstanceOfResidue(Class<?> thisClass) {
/* 107 */     return (this.runtimeTest != null && (new ThisInstanceOfResidueTestVisitor(thisClass))
/* 108 */       .thisInstanceOfMatches(this.runtimeTest));
/*     */   }
/*     */   
/*     */   public boolean testTargetInstanceOfResidue(Class<?> targetClass) {
/* 112 */     return (this.runtimeTest != null && (new TargetInstanceOfResidueTestVisitor(targetClass))
/* 113 */       .targetInstanceOfMatches(this.runtimeTest));
/*     */   }
/*     */   
/*     */   private static class TestVisitorAdapter
/*     */     implements ITestVisitor {
/*     */     protected static final int THIS_VAR = 0;
/*     */     protected static final int TARGET_VAR = 1;
/*     */     protected static final int AT_THIS_VAR = 3;
/*     */     protected static final int AT_TARGET_VAR = 4;
/*     */     protected static final int AT_ANNOTATION_VAR = 8;
/*     */     
/*     */     private TestVisitorAdapter() {}
/*     */     
/*     */     public void visit(And e) {
/* 127 */       e.getLeft().accept(this);
/* 128 */       e.getRight().accept(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit(Or e) {
/* 133 */       e.getLeft().accept(this);
/* 134 */       e.getRight().accept(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit(Not e) {
/* 139 */       e.getBody().accept(this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(Instanceof i) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(Literal literal) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(Call call) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(FieldGetCall fieldGetCall) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(HasAnnotation hasAnnotation) {}
/*     */ 
/*     */     
/*     */     public void visit(MatchingContextBasedTest matchingContextTest) {}
/*     */ 
/*     */     
/*     */     protected int getVarType(ReflectionVar v) {
/*     */       try {
/* 168 */         ReflectionUtils.makeAccessible(RuntimeTestWalker.varTypeField);
/* 169 */         return ((Integer)RuntimeTestWalker.varTypeField.get(v)).intValue();
/*     */       }
/* 171 */       catch (IllegalAccessException ex) {
/* 172 */         throw new IllegalStateException(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class InstanceOfResidueTestVisitor
/*     */     extends TestVisitorAdapter
/*     */   {
/*     */     private final Class<?> matchClass;
/*     */     
/*     */     private boolean matches;
/*     */     private final int matchVarType;
/*     */     
/*     */     public InstanceOfResidueTestVisitor(Class<?> matchClass, boolean defaultMatches, int matchVarType) {
/* 187 */       this.matchClass = matchClass;
/* 188 */       this.matches = defaultMatches;
/* 189 */       this.matchVarType = matchVarType;
/*     */     }
/*     */     
/*     */     public boolean instanceOfMatches(Test test) {
/* 193 */       test.accept(this);
/* 194 */       return this.matches;
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit(Instanceof i) {
/* 199 */       int varType = getVarType((ReflectionVar)i.getVar());
/* 200 */       if (varType != this.matchVarType) {
/*     */         return;
/*     */       }
/* 203 */       Class<?> typeClass = null;
/* 204 */       ResolvedType type = (ResolvedType)i.getType();
/* 205 */       if (type instanceof ReferenceType) {
/* 206 */         ReferenceTypeDelegate delegate = ((ReferenceType)type).getDelegate();
/* 207 */         if (delegate instanceof ReflectionBasedReferenceTypeDelegate) {
/*     */           try {
/* 209 */             ReflectionUtils.makeAccessible(RuntimeTestWalker.myClassField);
/* 210 */             typeClass = (Class)RuntimeTestWalker.myClassField.get(delegate);
/*     */           }
/* 212 */           catch (IllegalAccessException ex) {
/* 213 */             throw new IllegalStateException(ex);
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/*     */       try {
/* 219 */         if (typeClass == null) {
/* 220 */           typeClass = ClassUtils.forName(type.getName(), this.matchClass.getClassLoader());
/*     */         }
/* 222 */         this.matches = typeClass.isAssignableFrom(this.matchClass);
/*     */       }
/* 224 */       catch (ClassNotFoundException ex) {
/* 225 */         this.matches = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TargetInstanceOfResidueTestVisitor
/*     */     extends InstanceOfResidueTestVisitor
/*     */   {
/*     */     public TargetInstanceOfResidueTestVisitor(Class<?> targetClass) {
/* 237 */       super(targetClass, false, 1);
/*     */     }
/*     */     
/*     */     public boolean targetInstanceOfMatches(Test test) {
/* 241 */       return instanceOfMatches(test);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ThisInstanceOfResidueTestVisitor
/*     */     extends InstanceOfResidueTestVisitor
/*     */   {
/*     */     public ThisInstanceOfResidueTestVisitor(Class<?> thisClass) {
/* 252 */       super(thisClass, true, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean thisInstanceOfMatches(Test test) {
/* 257 */       return instanceOfMatches(test);
/*     */     } }
/*     */   
/*     */   private static class SubtypeSensitiveVarTypeTestVisitor extends TestVisitorAdapter { private final Object thisObj;
/*     */     private final Object targetObj;
/*     */     
/*     */     private SubtypeSensitiveVarTypeTestVisitor() {
/* 264 */       this.thisObj = new Object();
/*     */       
/* 266 */       this.targetObj = new Object();
/*     */       
/* 268 */       this.argsObjs = new Object[0];
/*     */       
/* 270 */       this.testsSubtypeSensitiveVars = false;
/*     */     } private final Object[] argsObjs; private boolean testsSubtypeSensitiveVars;
/*     */     public boolean testsSubtypeSensitiveVars(Test aTest) {
/* 273 */       aTest.accept(this);
/* 274 */       return this.testsSubtypeSensitiveVars;
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit(Instanceof i) {
/* 279 */       ReflectionVar v = (ReflectionVar)i.getVar();
/* 280 */       Object varUnderTest = v.getBindingAtJoinPoint(this.thisObj, this.targetObj, this.argsObjs);
/* 281 */       if (varUnderTest == this.thisObj || varUnderTest == this.targetObj) {
/* 282 */         this.testsSubtypeSensitiveVars = true;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(HasAnnotation hasAnn) {
/* 289 */       ReflectionVar v = (ReflectionVar)hasAnn.getVar();
/* 290 */       int varType = getVarType(v);
/* 291 */       if (varType == 3 || varType == 4 || varType == 8)
/* 292 */         this.testsSubtypeSensitiveVars = true; 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/RuntimeTestWalker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */