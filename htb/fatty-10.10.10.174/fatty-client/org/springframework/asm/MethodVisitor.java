/*     */ package org.springframework.asm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MethodVisitor
/*     */ {
/*     */   private static final String REQUIRES_ASM5 = "This feature requires ASM5";
/*     */   protected final int api;
/*     */   protected MethodVisitor mv;
/*     */   
/*     */   public MethodVisitor(int api) {
/*  69 */     this(api, null);
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
/*     */   public MethodVisitor(int api, MethodVisitor methodVisitor) {
/*  81 */     if (api != 393216 && api != 327680 && api != 262144 && api != 458752) {
/*  82 */       throw new IllegalArgumentException();
/*     */     }
/*  84 */     this.api = api;
/*  85 */     this.mv = methodVisitor;
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
/*     */   public void visitParameter(String name, int access) {
/* 100 */     if (this.api < 327680) {
/* 101 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 103 */     if (this.mv != null) {
/* 104 */       this.mv.visitParameter(name, access);
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
/*     */   public AnnotationVisitor visitAnnotationDefault() {
/* 117 */     if (this.mv != null) {
/* 118 */       return this.mv.visitAnnotationDefault();
/*     */     }
/* 120 */     return null;
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
/*     */   public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
/* 132 */     if (this.mv != null) {
/* 133 */       return this.mv.visitAnnotation(descriptor, visible);
/*     */     }
/* 135 */     return null;
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
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 156 */     if (this.api < 327680) {
/* 157 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 159 */     if (this.mv != null) {
/* 160 */       return this.mv.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
/*     */     }
/* 162 */     return null;
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
/*     */   public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
/* 180 */     if (this.mv != null) {
/* 181 */       this.mv.visitAnnotableParameterCount(parameterCount, visible);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
/* 201 */     if (this.mv != null) {
/* 202 */       return this.mv.visitParameterAnnotation(parameter, descriptor, visible);
/*     */     }
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute attribute) {
/* 213 */     if (this.mv != null) {
/* 214 */       this.mv.visitAttribute(attribute);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitCode() {
/* 220 */     if (this.mv != null) {
/* 221 */       this.mv.visitCode();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
/* 288 */     if (this.mv != null) {
/* 289 */       this.mv.visitFrame(type, numLocal, local, numStack, stack);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInsn(int opcode) {
/* 312 */     if (this.mv != null) {
/* 313 */       this.mv.visitInsn(opcode);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitIntInsn(int opcode, int operand) {
/* 332 */     if (this.mv != null) {
/* 333 */       this.mv.visitIntInsn(opcode, operand);
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
/*     */   public void visitVarInsn(int opcode, int var) {
/* 347 */     if (this.mv != null) {
/* 348 */       this.mv.visitVarInsn(opcode, var);
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
/*     */   public void visitTypeInsn(int opcode, String type) {
/* 362 */     if (this.mv != null) {
/* 363 */       this.mv.visitTypeInsn(opcode, type);
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
/*     */ 
/*     */   
/*     */   public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
/* 379 */     if (this.mv != null) {
/* 380 */       this.mv.visitFieldInsn(opcode, owner, name, descriptor);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
/* 398 */     if (this.api >= 327680) {
/* 399 */       boolean isInterface = (opcode == 185);
/* 400 */       visitMethodInsn(opcode, owner, name, descriptor, isInterface);
/*     */       return;
/*     */     } 
/* 403 */     if (this.mv != null) {
/* 404 */       this.mv.visitMethodInsn(opcode, owner, name, descriptor);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
/* 425 */     if (this.api < 327680) {
/* 426 */       if (isInterface != ((opcode == 185))) {
/* 427 */         throw new IllegalArgumentException("INVOKESPECIAL/STATIC on interfaces requires ASM5");
/*     */       }
/* 429 */       visitMethodInsn(opcode, owner, name, descriptor);
/*     */       return;
/*     */     } 
/* 432 */     if (this.mv != null) {
/* 433 */       this.mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
/* 453 */     if (this.api < 327680) {
/* 454 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 456 */     if (this.mv != null) {
/* 457 */       this.mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
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
/*     */   
/*     */   public void visitJumpInsn(int opcode, Label label) {
/* 472 */     if (this.mv != null) {
/* 473 */       this.mv.visitJumpInsn(opcode, label);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLabel(Label label) {
/* 483 */     if (this.mv != null) {
/* 484 */       this.mv.visitLabel(label);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLdcInsn(Object value) {
/* 536 */     if (this.api < 327680 && (value instanceof Handle || (value instanceof Type && ((Type)value)
/*     */       
/* 538 */       .getSort() == 11))) {
/* 539 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 541 */     if (this.api != 458752 && value instanceof ConstantDynamic) {
/* 542 */       throw new UnsupportedOperationException("This feature requires ASM7");
/*     */     }
/* 544 */     if (this.mv != null) {
/* 545 */       this.mv.visitLdcInsn(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitIincInsn(int var, int increment) {
/* 556 */     if (this.mv != null) {
/* 557 */       this.mv.visitIincInsn(var, increment);
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
/*     */   
/*     */   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
/* 572 */     if (this.mv != null) {
/* 573 */       this.mv.visitTableSwitchInsn(min, max, dflt, labels);
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
/*     */   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/* 586 */     if (this.mv != null) {
/* 587 */       this.mv.visitLookupSwitchInsn(dflt, keys, labels);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
/* 598 */     if (this.mv != null) {
/* 599 */       this.mv.visitMultiANewArrayInsn(descriptor, numDimensions);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 624 */     if (this.api < 327680) {
/* 625 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 627 */     if (this.mv != null) {
/* 628 */       return this.mv.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
/*     */     }
/* 630 */     return null;
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
/*     */   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
/* 650 */     if (this.mv != null) {
/* 651 */       this.mv.visitTryCatchBlock(start, end, handler, type);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 672 */     if (this.api < 327680) {
/* 673 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 675 */     if (this.mv != null) {
/* 676 */       return this.mv.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
/*     */     }
/* 678 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
/* 702 */     if (this.mv != null) {
/* 703 */       this.mv.visitLocalVariable(name, descriptor, signature, start, end, index);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
/* 735 */     if (this.api < 327680) {
/* 736 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 738 */     if (this.mv != null) {
/* 739 */       return this.mv.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
/*     */     }
/*     */     
/* 742 */     return null;
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
/*     */   public void visitLineNumber(int line, Label start) {
/* 755 */     if (this.mv != null) {
/* 756 */       this.mv.visitLineNumber(line, start);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMaxs(int maxStack, int maxLocals) {
/* 767 */     if (this.mv != null) {
/* 768 */       this.mv.visitMaxs(maxStack, maxLocals);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 777 */     if (this.mv != null)
/* 778 */       this.mv.visitEnd(); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/MethodVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */