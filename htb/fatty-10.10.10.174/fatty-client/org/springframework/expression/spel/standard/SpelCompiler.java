/*     */ package org.springframework.expression.spel.standard;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.asm.ClassWriter;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Opcodes;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompiledExpression;
/*     */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public final class SpelCompiler
/*     */   implements Opcodes
/*     */ {
/*  70 */   private static final Log logger = LogFactory.getLog(SpelCompiler.class);
/*     */ 
/*     */   
/*     */   private static final int CLASSES_DEFINED_LIMIT = 100;
/*     */ 
/*     */   
/*  76 */   private static final Map<ClassLoader, SpelCompiler> compilers = (Map<ClassLoader, SpelCompiler>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */   
/*     */   private ChildClassLoader ccl;
/*     */ 
/*     */   
/*  82 */   private final AtomicInteger suffixId = new AtomicInteger(1);
/*     */ 
/*     */   
/*     */   private SpelCompiler(@Nullable ClassLoader classloader) {
/*  86 */     this.ccl = new ChildClassLoader(classloader);
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
/*     */   @Nullable
/*     */   public CompiledExpression compile(SpelNodeImpl expression) {
/* 101 */     if (expression.isCompilable()) {
/* 102 */       if (logger.isDebugEnabled()) {
/* 103 */         logger.debug("SpEL: compiling " + expression.toStringAST());
/*     */       }
/* 105 */       Class<? extends CompiledExpression> clazz = createExpressionClass(expression);
/* 106 */       if (clazz != null) {
/*     */         try {
/* 108 */           return ReflectionUtils.accessibleConstructor(clazz, new Class[0]).newInstance(new Object[0]);
/*     */         }
/* 110 */         catch (Throwable ex) {
/* 111 */           throw new IllegalStateException("Failed to instantiate CompiledExpression", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 116 */     if (logger.isDebugEnabled()) {
/* 117 */       logger.debug("SpEL: unable to compile " + expression.toStringAST());
/*     */     }
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   private int getNextSuffix() {
/* 123 */     return this.suffixId.incrementAndGet();
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
/*     */   private Class<? extends CompiledExpression> createExpressionClass(SpelNodeImpl expressionToCompile) {
/* 136 */     String className = "spel/Ex" + getNextSuffix();
/* 137 */     ClassWriter cw = new ExpressionClassWriter();
/* 138 */     cw.visit(49, 1, className, null, "org/springframework/expression/spel/CompiledExpression", null);
/*     */ 
/*     */     
/* 141 */     MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
/* 142 */     mv.visitCode();
/* 143 */     mv.visitVarInsn(25, 0);
/* 144 */     mv.visitMethodInsn(183, "org/springframework/expression/spel/CompiledExpression", "<init>", "()V", false);
/*     */     
/* 146 */     mv.visitInsn(177);
/* 147 */     mv.visitMaxs(1, 1);
/* 148 */     mv.visitEnd();
/*     */ 
/*     */     
/* 151 */     mv = cw.visitMethod(1, "getValue", "(Ljava/lang/Object;Lorg/springframework/expression/EvaluationContext;)Ljava/lang/Object;", null, new String[] { "org/springframework/expression/EvaluationException" });
/*     */ 
/*     */     
/* 154 */     mv.visitCode();
/*     */     
/* 156 */     CodeFlow cf = new CodeFlow(className, cw);
/*     */ 
/*     */     
/*     */     try {
/* 160 */       expressionToCompile.generateCode(mv, cf);
/*     */     }
/* 162 */     catch (IllegalStateException ex) {
/* 163 */       if (logger.isDebugEnabled()) {
/* 164 */         logger.debug(expressionToCompile.getClass().getSimpleName() + ".generateCode opted out of compilation: " + ex
/* 165 */             .getMessage());
/*     */       }
/* 167 */       return null;
/*     */     } 
/*     */     
/* 170 */     CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor());
/* 171 */     if ("V".equals(cf.lastDescriptor())) {
/* 172 */       mv.visitInsn(1);
/*     */     }
/* 174 */     mv.visitInsn(176);
/*     */     
/* 176 */     mv.visitMaxs(0, 0);
/* 177 */     mv.visitEnd();
/* 178 */     cw.visitEnd();
/*     */     
/* 180 */     cf.finish();
/*     */     
/* 182 */     byte[] data = cw.toByteArray();
/*     */ 
/*     */     
/* 185 */     return loadClass(StringUtils.replace(className, "/", "."), data);
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
/*     */   private Class<? extends CompiledExpression> loadClass(String name, byte[] bytes) {
/* 199 */     if (this.ccl.getClassesDefinedCount() > 100) {
/* 200 */       this.ccl = new ChildClassLoader(this.ccl.getParent());
/*     */     }
/* 202 */     return (Class)this.ccl.defineClass(name, bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SpelCompiler getCompiler(@Nullable ClassLoader classLoader) {
/* 213 */     ClassLoader clToUse = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
/* 214 */     synchronized (compilers) {
/* 215 */       SpelCompiler compiler = compilers.get(clToUse);
/* 216 */       if (compiler == null) {
/* 217 */         compiler = new SpelCompiler(clToUse);
/* 218 */         compilers.put(clToUse, compiler);
/*     */       } 
/* 220 */       return compiler;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean compile(Expression expression) {
/* 231 */     return (expression instanceof SpelExpression && ((SpelExpression)expression).compileExpression());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void revertToInterpreted(Expression expression) {
/* 240 */     if (expression instanceof SpelExpression) {
/* 241 */       ((SpelExpression)expression).revertToInterpreted();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ChildClassLoader
/*     */     extends URLClassLoader
/*     */   {
/* 251 */     private static final URL[] NO_URLS = new URL[0];
/*     */     
/* 253 */     private int classesDefinedCount = 0;
/*     */     
/*     */     public ChildClassLoader(@Nullable ClassLoader classLoader) {
/* 256 */       super(NO_URLS, classLoader);
/*     */     }
/*     */     
/*     */     int getClassesDefinedCount() {
/* 260 */       return this.classesDefinedCount;
/*     */     }
/*     */     
/*     */     public Class<?> defineClass(String name, byte[] bytes) {
/* 264 */       Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
/* 265 */       this.classesDefinedCount++;
/* 266 */       return clazz;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ExpressionClassWriter
/*     */     extends ClassWriter
/*     */   {
/*     */     public ExpressionClassWriter() {
/* 274 */       super(3);
/*     */     }
/*     */ 
/*     */     
/*     */     protected ClassLoader getClassLoader() {
/* 279 */       return SpelCompiler.this.ccl;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/standard/SpelCompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */