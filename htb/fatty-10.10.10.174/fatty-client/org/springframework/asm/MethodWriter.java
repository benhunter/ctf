/*      */ package org.springframework.asm;
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
/*      */ final class MethodWriter
/*      */   extends MethodVisitor
/*      */ {
/*      */   static final int COMPUTE_NOTHING = 0;
/*      */   static final int COMPUTE_MAX_STACK_AND_LOCAL = 1;
/*      */   static final int COMPUTE_MAX_STACK_AND_LOCAL_FROM_FRAMES = 2;
/*      */   static final int COMPUTE_INSERTED_FRAMES = 3;
/*      */   static final int COMPUTE_ALL_FRAMES = 4;
/*      */   private static final int NA = 0;
/*   81 */   private static final int[] STACK_SIZE_DELTA = new int[] { 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 2, 1, 1, 1, 0, 0, 1, 2, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, -1, -1, -1, -1, -1, -2, -1, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, -4, -3, -4, -3, -3, -3, -3, -1, -2, 1, 1, 1, 2, 2, 2, 0, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -2, -1, -2, -1, -2, 0, 1, 0, 1, -1, -1, 0, 0, 1, 1, -1, 0, -1, 0, 0, 0, -3, -1, -1, -3, -3, -1, -1, -1, -1, -1, -1, -2, -2, -2, -2, -2, -2, -2, -2, 0, 1, 0, -1, -1, -1, -2, -1, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, -1, -1, 0, 0 };
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
/*      */ 
/*      */   
/*      */   private final SymbolTable symbolTable;
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
/*      */ 
/*      */   
/*      */   private final int accessFlags;
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
/*      */ 
/*      */   
/*      */   private final int nameIndex;
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
/*      */ 
/*      */   
/*      */   private final String name;
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
/*      */ 
/*      */   
/*      */   private final int descriptorIndex;
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
/*      */   
/*      */   private final String descriptor;
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
/*      */   
/*      */   private int maxStack;
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
/*      */   
/*      */   private int maxLocals;
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
/*      */   
/*  320 */   private final ByteVector code = new ByteVector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Handler firstHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Handler lastHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int lineNumberTableLength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector lineNumberTable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int localVariableTableLength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector localVariableTable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int localVariableTypeTableLength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector localVariableTypeTable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int stackMapTableNumberOfEntries;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector stackMapTableEntries;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter lastCodeRuntimeVisibleTypeAnnotation;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter lastCodeRuntimeInvisibleTypeAnnotation;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Attribute firstCodeAttribute;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int numberOfExceptions;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int[] exceptionIndexTable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int signatureIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter lastRuntimeVisibleAnnotation;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter lastRuntimeInvisibleAnnotation;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int visibleAnnotableParameterCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter[] lastRuntimeVisibleParameterAnnotations;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int invisibleAnnotableParameterCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter[] lastRuntimeInvisibleParameterAnnotations;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter lastRuntimeVisibleTypeAnnotation;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter lastRuntimeInvisibleTypeAnnotation;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector defaultValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int parametersCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector parameters;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Attribute firstAttribute;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int compute;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label firstBasicBlock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label lastBasicBlock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label currentBasicBlock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int relativeStackSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int maxRelativeStackSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int currentLocals;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int previousFrameOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] previousFrame;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] currentFrame;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasSubroutines;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasAsmInstructions;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int lastBytecodeOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int sourceOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int sourceLength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   MethodWriter(SymbolTable symbolTable, int access, String name, String descriptor, String signature, String[] exceptions, int compute) {
/*  595 */     super(458752);
/*  596 */     this.symbolTable = symbolTable;
/*  597 */     this.accessFlags = "<init>".equals(name) ? (access | 0x40000) : access;
/*  598 */     this.nameIndex = symbolTable.addConstantUtf8(name);
/*  599 */     this.name = name;
/*  600 */     this.descriptorIndex = symbolTable.addConstantUtf8(descriptor);
/*  601 */     this.descriptor = descriptor;
/*  602 */     this.signatureIndex = (signature == null) ? 0 : symbolTable.addConstantUtf8(signature);
/*  603 */     if (exceptions != null && exceptions.length > 0) {
/*  604 */       this.numberOfExceptions = exceptions.length;
/*  605 */       this.exceptionIndexTable = new int[this.numberOfExceptions];
/*  606 */       for (int i = 0; i < this.numberOfExceptions; i++) {
/*  607 */         this.exceptionIndexTable[i] = (symbolTable.addConstantClass(exceptions[i])).index;
/*      */       }
/*      */     } else {
/*  610 */       this.numberOfExceptions = 0;
/*  611 */       this.exceptionIndexTable = null;
/*      */     } 
/*  613 */     this.compute = compute;
/*  614 */     if (compute != 0) {
/*      */       
/*  616 */       int argumentsSize = Type.getArgumentsAndReturnSizes(descriptor) >> 2;
/*  617 */       if ((access & 0x8) != 0) {
/*  618 */         argumentsSize--;
/*      */       }
/*  620 */       this.maxLocals = argumentsSize;
/*  621 */       this.currentLocals = argumentsSize;
/*      */       
/*  623 */       this.firstBasicBlock = new Label();
/*  624 */       visitLabel(this.firstBasicBlock);
/*      */     } 
/*      */   }
/*      */   
/*      */   boolean hasFrames() {
/*  629 */     return (this.stackMapTableNumberOfEntries > 0);
/*      */   }
/*      */   
/*      */   boolean hasAsmInstructions() {
/*  633 */     return this.hasAsmInstructions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitParameter(String name, int access) {
/*  642 */     if (this.parameters == null) {
/*  643 */       this.parameters = new ByteVector();
/*      */     }
/*  645 */     this.parametersCount++;
/*  646 */     this.parameters.putShort((name == null) ? 0 : this.symbolTable.addConstantUtf8(name)).putShort(access);
/*      */   }
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitAnnotationDefault() {
/*  651 */     this.defaultValue = new ByteVector();
/*  652 */     return new AnnotationWriter(this.symbolTable, false, this.defaultValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
/*  659 */     ByteVector annotation = new ByteVector();
/*      */     
/*  661 */     annotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
/*  662 */     if (visible) {
/*  663 */       return this.lastRuntimeVisibleAnnotation = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeVisibleAnnotation);
/*      */     }
/*      */     
/*  666 */     return this.lastRuntimeInvisibleAnnotation = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeInvisibleAnnotation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/*  676 */     ByteVector typeAnnotation = new ByteVector();
/*      */     
/*  678 */     TypeReference.putTarget(typeRef, typeAnnotation);
/*  679 */     TypePath.put(typePath, typeAnnotation);
/*      */     
/*  681 */     typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
/*  682 */     if (visible) {
/*  683 */       return this.lastRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastRuntimeVisibleTypeAnnotation);
/*      */     }
/*      */     
/*  686 */     return this.lastRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastRuntimeInvisibleTypeAnnotation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
/*  693 */     if (visible) {
/*  694 */       this.visibleAnnotableParameterCount = parameterCount;
/*      */     } else {
/*  696 */       this.invisibleAnnotableParameterCount = parameterCount;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitParameterAnnotation(int parameter, String annotationDescriptor, boolean visible) {
/*  705 */     ByteVector annotation = new ByteVector();
/*      */     
/*  707 */     annotation.putShort(this.symbolTable.addConstantUtf8(annotationDescriptor)).putShort(0);
/*  708 */     if (visible) {
/*  709 */       if (this.lastRuntimeVisibleParameterAnnotations == null) {
/*  710 */         this
/*  711 */           .lastRuntimeVisibleParameterAnnotations = new AnnotationWriter[(Type.getArgumentTypes(this.descriptor)).length];
/*      */       }
/*  713 */       this.lastRuntimeVisibleParameterAnnotations[parameter] = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeVisibleParameterAnnotations[parameter]); return new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeVisibleParameterAnnotations[parameter]);
/*      */     } 
/*      */ 
/*      */     
/*  717 */     if (this.lastRuntimeInvisibleParameterAnnotations == null) {
/*  718 */       this
/*  719 */         .lastRuntimeInvisibleParameterAnnotations = new AnnotationWriter[(Type.getArgumentTypes(this.descriptor)).length];
/*      */     }
/*  721 */     this.lastRuntimeInvisibleParameterAnnotations[parameter] = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeInvisibleParameterAnnotations[parameter]); return new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeInvisibleParameterAnnotations[parameter]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitAttribute(Attribute attribute) {
/*  730 */     if (attribute.isCodeAttribute()) {
/*  731 */       attribute.nextAttribute = this.firstCodeAttribute;
/*  732 */       this.firstCodeAttribute = attribute;
/*      */     } else {
/*  734 */       attribute.nextAttribute = this.firstAttribute;
/*  735 */       this.firstAttribute = attribute;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitCode() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
/*  751 */     if (this.compute == 4) {
/*      */       return;
/*      */     }
/*      */     
/*  755 */     if (this.compute == 3) {
/*  756 */       if (this.currentBasicBlock.frame == null) {
/*      */ 
/*      */ 
/*      */         
/*  760 */         this.currentBasicBlock.frame = new CurrentFrame(this.currentBasicBlock);
/*  761 */         this.currentBasicBlock.frame.setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, numLocal);
/*      */         
/*  763 */         this.currentBasicBlock.frame.accept(this);
/*      */       } else {
/*  765 */         if (type == -1) {
/*  766 */           this.currentBasicBlock.frame.setInputFrameFromApiFormat(this.symbolTable, numLocal, local, numStack, stack);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  772 */         this.currentBasicBlock.frame.accept(this);
/*      */       } 
/*  774 */     } else if (type == -1) {
/*  775 */       if (this.previousFrame == null) {
/*  776 */         int argumentsSize = Type.getArgumentsAndReturnSizes(this.descriptor) >> 2;
/*  777 */         Frame implicitFirstFrame = new Frame(new Label());
/*  778 */         implicitFirstFrame.setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, argumentsSize);
/*      */         
/*  780 */         implicitFirstFrame.accept(this);
/*      */       } 
/*  782 */       this.currentLocals = numLocal;
/*  783 */       int frameIndex = visitFrameStart(this.code.length, numLocal, numStack); int i;
/*  784 */       for (i = 0; i < numLocal; i++) {
/*  785 */         this.currentFrame[frameIndex++] = Frame.getAbstractTypeFromApiFormat(this.symbolTable, local[i]);
/*      */       }
/*  787 */       for (i = 0; i < numStack; i++) {
/*  788 */         this.currentFrame[frameIndex++] = Frame.getAbstractTypeFromApiFormat(this.symbolTable, stack[i]);
/*      */       }
/*  790 */       visitFrameEnd();
/*      */     } else {
/*      */       int offsetDelta, i;
/*  793 */       if (this.stackMapTableEntries == null) {
/*  794 */         this.stackMapTableEntries = new ByteVector();
/*  795 */         offsetDelta = this.code.length;
/*      */       } else {
/*  797 */         offsetDelta = this.code.length - this.previousFrameOffset - 1;
/*  798 */         if (offsetDelta < 0) {
/*  799 */           if (type == 3) {
/*      */             return;
/*      */           }
/*  802 */           throw new IllegalStateException();
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  807 */       switch (type) {
/*      */         case 0:
/*  809 */           this.currentLocals = numLocal;
/*  810 */           this.stackMapTableEntries.putByte(255).putShort(offsetDelta).putShort(numLocal);
/*  811 */           for (i = 0; i < numLocal; i++) {
/*  812 */             putFrameType(local[i]);
/*      */           }
/*  814 */           this.stackMapTableEntries.putShort(numStack);
/*  815 */           for (i = 0; i < numStack; i++) {
/*  816 */             putFrameType(stack[i]);
/*      */           }
/*      */           break;
/*      */         case 1:
/*  820 */           this.currentLocals += numLocal;
/*  821 */           this.stackMapTableEntries.putByte(251 + numLocal).putShort(offsetDelta);
/*  822 */           for (i = 0; i < numLocal; i++) {
/*  823 */             putFrameType(local[i]);
/*      */           }
/*      */           break;
/*      */         case 2:
/*  827 */           this.currentLocals -= numLocal;
/*  828 */           this.stackMapTableEntries.putByte(251 - numLocal).putShort(offsetDelta);
/*      */           break;
/*      */         case 3:
/*  831 */           if (offsetDelta < 64) {
/*  832 */             this.stackMapTableEntries.putByte(offsetDelta); break;
/*      */           } 
/*  834 */           this.stackMapTableEntries.putByte(251).putShort(offsetDelta);
/*      */           break;
/*      */         
/*      */         case 4:
/*  838 */           if (offsetDelta < 64) {
/*  839 */             this.stackMapTableEntries.putByte(64 + offsetDelta);
/*      */           } else {
/*  841 */             this.stackMapTableEntries
/*  842 */               .putByte(247)
/*  843 */               .putShort(offsetDelta);
/*      */           } 
/*  845 */           putFrameType(stack[0]);
/*      */           break;
/*      */         default:
/*  848 */           throw new IllegalArgumentException();
/*      */       } 
/*      */       
/*  851 */       this.previousFrameOffset = this.code.length;
/*  852 */       this.stackMapTableNumberOfEntries++;
/*      */     } 
/*      */     
/*  855 */     if (this.compute == 2) {
/*  856 */       this.relativeStackSize = numStack;
/*  857 */       for (int i = 0; i < numStack; i++) {
/*  858 */         if (stack[i] == Opcodes.LONG || stack[i] == Opcodes.DOUBLE) {
/*  859 */           this.relativeStackSize++;
/*      */         }
/*      */       } 
/*  862 */       if (this.relativeStackSize > this.maxRelativeStackSize) {
/*  863 */         this.maxRelativeStackSize = this.relativeStackSize;
/*      */       }
/*      */     } 
/*      */     
/*  867 */     this.maxStack = Math.max(this.maxStack, numStack);
/*  868 */     this.maxLocals = Math.max(this.maxLocals, this.currentLocals);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitInsn(int opcode) {
/*  873 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/*  875 */     this.code.putByte(opcode);
/*      */     
/*  877 */     if (this.currentBasicBlock != null) {
/*  878 */       if (this.compute == 4 || this.compute == 3) {
/*  879 */         this.currentBasicBlock.frame.execute(opcode, 0, null, null);
/*      */       } else {
/*  881 */         int size = this.relativeStackSize + STACK_SIZE_DELTA[opcode];
/*  882 */         if (size > this.maxRelativeStackSize) {
/*  883 */           this.maxRelativeStackSize = size;
/*      */         }
/*  885 */         this.relativeStackSize = size;
/*      */       } 
/*  887 */       if ((opcode >= 172 && opcode <= 177) || opcode == 191) {
/*  888 */         endCurrentBasicBlockWithNoSuccessor();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitIntInsn(int opcode, int operand) {
/*  895 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/*  897 */     if (opcode == 17) {
/*  898 */       this.code.put12(opcode, operand);
/*      */     } else {
/*  900 */       this.code.put11(opcode, operand);
/*      */     } 
/*      */     
/*  903 */     if (this.currentBasicBlock != null) {
/*  904 */       if (this.compute == 4 || this.compute == 3) {
/*  905 */         this.currentBasicBlock.frame.execute(opcode, operand, null, null);
/*  906 */       } else if (opcode != 188) {
/*      */         
/*  908 */         int size = this.relativeStackSize + 1;
/*  909 */         if (size > this.maxRelativeStackSize) {
/*  910 */           this.maxRelativeStackSize = size;
/*      */         }
/*  912 */         this.relativeStackSize = size;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitVarInsn(int opcode, int var) {
/*  919 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/*  921 */     if (var < 4 && opcode != 169) {
/*      */       int optimizedOpcode;
/*  923 */       if (opcode < 54) {
/*  924 */         optimizedOpcode = 26 + (opcode - 21 << 2) + var;
/*      */       } else {
/*  926 */         optimizedOpcode = 59 + (opcode - 54 << 2) + var;
/*      */       } 
/*  928 */       this.code.putByte(optimizedOpcode);
/*  929 */     } else if (var >= 256) {
/*  930 */       this.code.putByte(196).put12(opcode, var);
/*      */     } else {
/*  932 */       this.code.put11(opcode, var);
/*      */     } 
/*      */     
/*  935 */     if (this.currentBasicBlock != null) {
/*  936 */       if (this.compute == 4 || this.compute == 3) {
/*  937 */         this.currentBasicBlock.frame.execute(opcode, var, null, null);
/*      */       }
/*  939 */       else if (opcode == 169) {
/*      */         
/*  941 */         this.currentBasicBlock.flags = (short)(this.currentBasicBlock.flags | 0x40);
/*  942 */         this.currentBasicBlock.outputStackSize = (short)this.relativeStackSize;
/*  943 */         endCurrentBasicBlockWithNoSuccessor();
/*      */       } else {
/*  945 */         int size = this.relativeStackSize + STACK_SIZE_DELTA[opcode];
/*  946 */         if (size > this.maxRelativeStackSize) {
/*  947 */           this.maxRelativeStackSize = size;
/*      */         }
/*  949 */         this.relativeStackSize = size;
/*      */       } 
/*      */     }
/*      */     
/*  953 */     if (this.compute != 0) {
/*      */       int currentMaxLocals;
/*  955 */       if (opcode == 22 || opcode == 24 || opcode == 55 || opcode == 57) {
/*      */ 
/*      */ 
/*      */         
/*  959 */         currentMaxLocals = var + 2;
/*      */       } else {
/*  961 */         currentMaxLocals = var + 1;
/*      */       } 
/*  963 */       if (currentMaxLocals > this.maxLocals) {
/*  964 */         this.maxLocals = currentMaxLocals;
/*      */       }
/*      */     } 
/*  967 */     if (opcode >= 54 && this.compute == 4 && this.firstHandler != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  975 */       visitLabel(new Label());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitTypeInsn(int opcode, String type) {
/*  981 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/*  983 */     Symbol typeSymbol = this.symbolTable.addConstantClass(type);
/*  984 */     this.code.put12(opcode, typeSymbol.index);
/*      */     
/*  986 */     if (this.currentBasicBlock != null) {
/*  987 */       if (this.compute == 4 || this.compute == 3) {
/*  988 */         this.currentBasicBlock.frame.execute(opcode, this.lastBytecodeOffset, typeSymbol, this.symbolTable);
/*  989 */       } else if (opcode == 187) {
/*      */         
/*  991 */         int size = this.relativeStackSize + 1;
/*  992 */         if (size > this.maxRelativeStackSize) {
/*  993 */           this.maxRelativeStackSize = size;
/*      */         }
/*  995 */         this.relativeStackSize = size;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
/* 1003 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/* 1005 */     Symbol fieldrefSymbol = this.symbolTable.addConstantFieldref(owner, name, descriptor);
/* 1006 */     this.code.put12(opcode, fieldrefSymbol.index);
/*      */     
/* 1008 */     if (this.currentBasicBlock != null) {
/* 1009 */       if (this.compute == 4 || this.compute == 3) {
/* 1010 */         this.currentBasicBlock.frame.execute(opcode, 0, fieldrefSymbol, this.symbolTable);
/*      */       } else {
/*      */         int size;
/* 1013 */         char firstDescChar = descriptor.charAt(0);
/* 1014 */         switch (opcode) {
/*      */           case 178:
/* 1016 */             size = this.relativeStackSize + ((firstDescChar == 'D' || firstDescChar == 'J') ? 2 : 1);
/*      */             break;
/*      */           case 179:
/* 1019 */             size = this.relativeStackSize + ((firstDescChar == 'D' || firstDescChar == 'J') ? -2 : -1);
/*      */             break;
/*      */           case 180:
/* 1022 */             size = this.relativeStackSize + ((firstDescChar == 'D' || firstDescChar == 'J') ? 1 : 0);
/*      */             break;
/*      */           
/*      */           default:
/* 1026 */             size = this.relativeStackSize + ((firstDescChar == 'D' || firstDescChar == 'J') ? -3 : -2);
/*      */             break;
/*      */         } 
/* 1029 */         if (size > this.maxRelativeStackSize) {
/* 1030 */           this.maxRelativeStackSize = size;
/*      */         }
/* 1032 */         this.relativeStackSize = size;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
/* 1044 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/* 1046 */     Symbol methodrefSymbol = this.symbolTable.addConstantMethodref(owner, name, descriptor, isInterface);
/* 1047 */     if (opcode == 185) {
/* 1048 */       this.code.put12(185, methodrefSymbol.index)
/* 1049 */         .put11(methodrefSymbol.getArgumentsAndReturnSizes() >> 2, 0);
/*      */     } else {
/* 1051 */       this.code.put12(opcode, methodrefSymbol.index);
/*      */     } 
/*      */     
/* 1054 */     if (this.currentBasicBlock != null) {
/* 1055 */       if (this.compute == 4 || this.compute == 3) {
/* 1056 */         this.currentBasicBlock.frame.execute(opcode, 0, methodrefSymbol, this.symbolTable);
/*      */       } else {
/* 1058 */         int size, argumentsAndReturnSize = methodrefSymbol.getArgumentsAndReturnSizes();
/* 1059 */         int stackSizeDelta = (argumentsAndReturnSize & 0x3) - (argumentsAndReturnSize >> 2);
/*      */         
/* 1061 */         if (opcode == 184) {
/* 1062 */           size = this.relativeStackSize + stackSizeDelta + 1;
/*      */         } else {
/* 1064 */           size = this.relativeStackSize + stackSizeDelta;
/*      */         } 
/* 1066 */         if (size > this.maxRelativeStackSize) {
/* 1067 */           this.maxRelativeStackSize = size;
/*      */         }
/* 1069 */         this.relativeStackSize = size;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
/* 1080 */     this.lastBytecodeOffset = this.code.length;
/*      */ 
/*      */     
/* 1083 */     Symbol invokeDynamicSymbol = this.symbolTable.addConstantInvokeDynamic(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
/*      */     
/* 1085 */     this.code.put12(186, invokeDynamicSymbol.index);
/* 1086 */     this.code.putShort(0);
/*      */     
/* 1088 */     if (this.currentBasicBlock != null) {
/* 1089 */       if (this.compute == 4 || this.compute == 3) {
/* 1090 */         this.currentBasicBlock.frame.execute(186, 0, invokeDynamicSymbol, this.symbolTable);
/*      */       } else {
/* 1092 */         int argumentsAndReturnSize = invokeDynamicSymbol.getArgumentsAndReturnSizes();
/* 1093 */         int stackSizeDelta = (argumentsAndReturnSize & 0x3) - (argumentsAndReturnSize >> 2) + 1;
/* 1094 */         int size = this.relativeStackSize + stackSizeDelta;
/* 1095 */         if (size > this.maxRelativeStackSize) {
/* 1096 */           this.maxRelativeStackSize = size;
/*      */         }
/* 1098 */         this.relativeStackSize = size;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitJumpInsn(int opcode, Label label) {
/* 1105 */     this.lastBytecodeOffset = this.code.length;
/*      */ 
/*      */     
/* 1108 */     int baseOpcode = (opcode >= 200) ? (opcode - 33) : opcode;
/*      */     
/* 1110 */     boolean nextInsnIsJumpTarget = false;
/* 1111 */     if ((label.flags & 0x4) != 0 && label.bytecodeOffset - this.code.length < -32768) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1117 */       if (baseOpcode == 167) {
/* 1118 */         this.code.putByte(200);
/* 1119 */       } else if (baseOpcode == 168) {
/* 1120 */         this.code.putByte(201);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1125 */         this.code.putByte((baseOpcode >= 198) ? (baseOpcode ^ 0x1) : ((baseOpcode + 1 ^ 0x1) - 1));
/* 1126 */         this.code.putShort(8);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1133 */         this.code.putByte(220);
/* 1134 */         this.hasAsmInstructions = true;
/*      */         
/* 1136 */         nextInsnIsJumpTarget = true;
/*      */       } 
/* 1138 */       label.put(this.code, this.code.length - 1, true);
/* 1139 */     } else if (baseOpcode != opcode) {
/*      */ 
/*      */       
/* 1142 */       this.code.putByte(opcode);
/* 1143 */       label.put(this.code, this.code.length - 1, true);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1148 */       this.code.putByte(baseOpcode);
/* 1149 */       label.put(this.code, this.code.length - 1, false);
/*      */     } 
/*      */ 
/*      */     
/* 1153 */     if (this.currentBasicBlock != null) {
/* 1154 */       Label nextBasicBlock = null;
/* 1155 */       if (this.compute == 4) {
/* 1156 */         this.currentBasicBlock.frame.execute(baseOpcode, 0, null, null);
/*      */         
/* 1158 */         (label.getCanonicalInstance()).flags = (short)((label.getCanonicalInstance()).flags | 0x2);
/*      */         
/* 1160 */         addSuccessorToCurrentBasicBlock(0, label);
/* 1161 */         if (baseOpcode != 167)
/*      */         {
/*      */ 
/*      */           
/* 1165 */           nextBasicBlock = new Label();
/*      */         }
/* 1167 */       } else if (this.compute == 3) {
/* 1168 */         this.currentBasicBlock.frame.execute(baseOpcode, 0, null, null);
/* 1169 */       } else if (this.compute == 2) {
/*      */         
/* 1171 */         this.relativeStackSize += STACK_SIZE_DELTA[baseOpcode];
/*      */       }
/* 1173 */       else if (baseOpcode == 168) {
/*      */         
/* 1175 */         if ((label.flags & 0x20) == 0) {
/* 1176 */           label.flags = (short)(label.flags | 0x20);
/* 1177 */           this.hasSubroutines = true;
/*      */         } 
/* 1179 */         this.currentBasicBlock.flags = (short)(this.currentBasicBlock.flags | 0x10);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1186 */         addSuccessorToCurrentBasicBlock(this.relativeStackSize + 1, label);
/*      */         
/* 1188 */         nextBasicBlock = new Label();
/*      */       } else {
/*      */         
/* 1191 */         this.relativeStackSize += STACK_SIZE_DELTA[baseOpcode];
/* 1192 */         addSuccessorToCurrentBasicBlock(this.relativeStackSize, label);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1197 */       if (nextBasicBlock != null) {
/* 1198 */         if (nextInsnIsJumpTarget) {
/* 1199 */           nextBasicBlock.flags = (short)(nextBasicBlock.flags | 0x2);
/*      */         }
/* 1201 */         visitLabel(nextBasicBlock);
/*      */       } 
/* 1203 */       if (baseOpcode == 167) {
/* 1204 */         endCurrentBasicBlockWithNoSuccessor();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLabel(Label label) {
/* 1212 */     this.hasAsmInstructions |= label.resolve(this.code.data, this.code.length);
/*      */ 
/*      */     
/* 1215 */     if ((label.flags & 0x1) != 0) {
/*      */       return;
/*      */     }
/* 1218 */     if (this.compute == 4) {
/* 1219 */       if (this.currentBasicBlock != null) {
/* 1220 */         if (label.bytecodeOffset == this.currentBasicBlock.bytecodeOffset) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1225 */           this.currentBasicBlock.flags = (short)(this.currentBasicBlock.flags | label.flags & 0x2);
/*      */ 
/*      */ 
/*      */           
/* 1229 */           label.frame = this.currentBasicBlock.frame;
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 1235 */         addSuccessorToCurrentBasicBlock(0, label);
/*      */       } 
/*      */       
/* 1238 */       if (this.lastBasicBlock != null) {
/* 1239 */         if (label.bytecodeOffset == this.lastBasicBlock.bytecodeOffset) {
/*      */           
/* 1241 */           this.lastBasicBlock.flags = (short)(this.lastBasicBlock.flags | label.flags & 0x2);
/*      */           
/* 1243 */           label.frame = this.lastBasicBlock.frame;
/* 1244 */           this.currentBasicBlock = this.lastBasicBlock;
/*      */           return;
/*      */         } 
/* 1247 */         this.lastBasicBlock.nextBasicBlock = label;
/*      */       } 
/* 1249 */       this.lastBasicBlock = label;
/*      */       
/* 1251 */       this.currentBasicBlock = label;
/*      */       
/* 1253 */       label.frame = new Frame(label);
/* 1254 */     } else if (this.compute == 3) {
/* 1255 */       if (this.currentBasicBlock == null) {
/*      */ 
/*      */         
/* 1258 */         this.currentBasicBlock = label;
/*      */       } else {
/*      */         
/* 1261 */         this.currentBasicBlock.frame.owner = label;
/*      */       } 
/* 1263 */     } else if (this.compute == 1) {
/* 1264 */       if (this.currentBasicBlock != null) {
/*      */         
/* 1266 */         this.currentBasicBlock.outputStackMax = (short)this.maxRelativeStackSize;
/* 1267 */         addSuccessorToCurrentBasicBlock(this.relativeStackSize, label);
/*      */       } 
/*      */       
/* 1270 */       this.currentBasicBlock = label;
/* 1271 */       this.relativeStackSize = 0;
/* 1272 */       this.maxRelativeStackSize = 0;
/*      */       
/* 1274 */       if (this.lastBasicBlock != null) {
/* 1275 */         this.lastBasicBlock.nextBasicBlock = label;
/*      */       }
/* 1277 */       this.lastBasicBlock = label;
/* 1278 */     } else if (this.compute == 2 && this.currentBasicBlock == null) {
/*      */ 
/*      */ 
/*      */       
/* 1282 */       this.currentBasicBlock = label;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLdcInsn(Object value) {
/* 1288 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/* 1290 */     Symbol constantSymbol = this.symbolTable.addConstant(value);
/* 1291 */     int constantIndex = constantSymbol.index;
/*      */ 
/*      */     
/*      */     char firstDescriptorChar;
/*      */ 
/*      */     
/* 1297 */     boolean isLongOrDouble = (constantSymbol.tag == 5 || constantSymbol.tag == 6 || (constantSymbol.tag == 17 && ((firstDescriptorChar = constantSymbol.value.charAt(0)) == 'J' || firstDescriptorChar == 'D')));
/*      */     
/* 1299 */     if (isLongOrDouble) {
/* 1300 */       this.code.put12(20, constantIndex);
/* 1301 */     } else if (constantIndex >= 256) {
/* 1302 */       this.code.put12(19, constantIndex);
/*      */     } else {
/* 1304 */       this.code.put11(18, constantIndex);
/*      */     } 
/*      */     
/* 1307 */     if (this.currentBasicBlock != null) {
/* 1308 */       if (this.compute == 4 || this.compute == 3) {
/* 1309 */         this.currentBasicBlock.frame.execute(18, 0, constantSymbol, this.symbolTable);
/*      */       } else {
/* 1311 */         int size = this.relativeStackSize + (isLongOrDouble ? 2 : 1);
/* 1312 */         if (size > this.maxRelativeStackSize) {
/* 1313 */           this.maxRelativeStackSize = size;
/*      */         }
/* 1315 */         this.relativeStackSize = size;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitIincInsn(int var, int increment) {
/* 1322 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/* 1324 */     if (var > 255 || increment > 127 || increment < -128) {
/* 1325 */       this.code.putByte(196).put12(132, var).putShort(increment);
/*      */     } else {
/* 1327 */       this.code.putByte(132).put11(var, increment);
/*      */     } 
/*      */     
/* 1330 */     if (this.currentBasicBlock != null && (this.compute == 4 || this.compute == 3))
/*      */     {
/* 1332 */       this.currentBasicBlock.frame.execute(132, var, null, null);
/*      */     }
/* 1334 */     if (this.compute != 0) {
/* 1335 */       int currentMaxLocals = var + 1;
/* 1336 */       if (currentMaxLocals > this.maxLocals) {
/* 1337 */         this.maxLocals = currentMaxLocals;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
/* 1345 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/* 1347 */     this.code.putByte(170).putByteArray(null, 0, (4 - this.code.length % 4) % 4);
/* 1348 */     dflt.put(this.code, this.lastBytecodeOffset, true);
/* 1349 */     this.code.putInt(min).putInt(max);
/* 1350 */     for (Label label : labels) {
/* 1351 */       label.put(this.code, this.lastBytecodeOffset, true);
/*      */     }
/*      */     
/* 1354 */     visitSwitchInsn(dflt, labels);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/* 1359 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/* 1361 */     this.code.putByte(171).putByteArray(null, 0, (4 - this.code.length % 4) % 4);
/* 1362 */     dflt.put(this.code, this.lastBytecodeOffset, true);
/* 1363 */     this.code.putInt(labels.length);
/* 1364 */     for (int i = 0; i < labels.length; i++) {
/* 1365 */       this.code.putInt(keys[i]);
/* 1366 */       labels[i].put(this.code, this.lastBytecodeOffset, true);
/*      */     } 
/*      */     
/* 1369 */     visitSwitchInsn(dflt, labels);
/*      */   }
/*      */   
/*      */   private void visitSwitchInsn(Label dflt, Label[] labels) {
/* 1373 */     if (this.currentBasicBlock != null) {
/* 1374 */       if (this.compute == 4) {
/* 1375 */         this.currentBasicBlock.frame.execute(171, 0, null, null);
/*      */         
/* 1377 */         addSuccessorToCurrentBasicBlock(0, dflt);
/* 1378 */         (dflt.getCanonicalInstance()).flags = (short)((dflt.getCanonicalInstance()).flags | 0x2);
/* 1379 */         for (Label label : labels) {
/* 1380 */           addSuccessorToCurrentBasicBlock(0, label);
/* 1381 */           (label.getCanonicalInstance()).flags = (short)((label.getCanonicalInstance()).flags | 0x2);
/*      */         } 
/* 1383 */       } else if (this.compute == 1) {
/*      */         
/* 1385 */         this.relativeStackSize--;
/*      */         
/* 1387 */         addSuccessorToCurrentBasicBlock(this.relativeStackSize, dflt);
/* 1388 */         for (Label label : labels) {
/* 1389 */           addSuccessorToCurrentBasicBlock(this.relativeStackSize, label);
/*      */         }
/*      */       } 
/*      */       
/* 1393 */       endCurrentBasicBlockWithNoSuccessor();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
/* 1399 */     this.lastBytecodeOffset = this.code.length;
/*      */     
/* 1401 */     Symbol descSymbol = this.symbolTable.addConstantClass(descriptor);
/* 1402 */     this.code.put12(197, descSymbol.index).putByte(numDimensions);
/*      */     
/* 1404 */     if (this.currentBasicBlock != null) {
/* 1405 */       if (this.compute == 4 || this.compute == 3) {
/* 1406 */         this.currentBasicBlock.frame.execute(197, numDimensions, descSymbol, this.symbolTable);
/*      */       }
/*      */       else {
/*      */         
/* 1410 */         this.relativeStackSize += 1 - numDimensions;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 1420 */     ByteVector typeAnnotation = new ByteVector();
/*      */     
/* 1422 */     TypeReference.putTarget(typeRef & 0xFF0000FF | this.lastBytecodeOffset << 8, typeAnnotation);
/* 1423 */     TypePath.put(typePath, typeAnnotation);
/*      */     
/* 1425 */     typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
/* 1426 */     if (visible) {
/* 1427 */       return this.lastCodeRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeVisibleTypeAnnotation);
/*      */     }
/*      */     
/* 1430 */     return this.lastCodeRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeInvisibleTypeAnnotation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
/* 1440 */     Handler newHandler = new Handler(start, end, handler, (type != null) ? (this.symbolTable.addConstantClass(type)).index : 0, type);
/* 1441 */     if (this.firstHandler == null) {
/* 1442 */       this.firstHandler = newHandler;
/*      */     } else {
/* 1444 */       this.lastHandler.nextHandler = newHandler;
/*      */     } 
/* 1446 */     this.lastHandler = newHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 1454 */     ByteVector typeAnnotation = new ByteVector();
/*      */     
/* 1456 */     TypeReference.putTarget(typeRef, typeAnnotation);
/* 1457 */     TypePath.put(typePath, typeAnnotation);
/*      */     
/* 1459 */     typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
/* 1460 */     if (visible) {
/* 1461 */       return this.lastCodeRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeVisibleTypeAnnotation);
/*      */     }
/*      */     
/* 1464 */     return this.lastCodeRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeInvisibleTypeAnnotation);
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
/*      */   public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
/* 1477 */     if (signature != null) {
/* 1478 */       if (this.localVariableTypeTable == null) {
/* 1479 */         this.localVariableTypeTable = new ByteVector();
/*      */       }
/* 1481 */       this.localVariableTypeTableLength++;
/* 1482 */       this.localVariableTypeTable
/* 1483 */         .putShort(start.bytecodeOffset)
/* 1484 */         .putShort(end.bytecodeOffset - start.bytecodeOffset)
/* 1485 */         .putShort(this.symbolTable.addConstantUtf8(name))
/* 1486 */         .putShort(this.symbolTable.addConstantUtf8(signature))
/* 1487 */         .putShort(index);
/*      */     } 
/* 1489 */     if (this.localVariableTable == null) {
/* 1490 */       this.localVariableTable = new ByteVector();
/*      */     }
/* 1492 */     this.localVariableTableLength++;
/* 1493 */     this.localVariableTable
/* 1494 */       .putShort(start.bytecodeOffset)
/* 1495 */       .putShort(end.bytecodeOffset - start.bytecodeOffset)
/* 1496 */       .putShort(this.symbolTable.addConstantUtf8(name))
/* 1497 */       .putShort(this.symbolTable.addConstantUtf8(descriptor))
/* 1498 */       .putShort(index);
/* 1499 */     if (this.compute != 0) {
/* 1500 */       char firstDescChar = descriptor.charAt(0);
/* 1501 */       int currentMaxLocals = index + ((firstDescChar == 'J' || firstDescChar == 'D') ? 2 : 1);
/* 1502 */       if (currentMaxLocals > this.maxLocals) {
/* 1503 */         this.maxLocals = currentMaxLocals;
/*      */       }
/*      */     } 
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
/*      */   public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
/* 1519 */     ByteVector typeAnnotation = new ByteVector();
/*      */     
/* 1521 */     typeAnnotation.putByte(typeRef >>> 24).putShort(start.length);
/* 1522 */     for (int i = 0; i < start.length; i++) {
/* 1523 */       typeAnnotation
/* 1524 */         .putShort((start[i]).bytecodeOffset)
/* 1525 */         .putShort((end[i]).bytecodeOffset - (start[i]).bytecodeOffset)
/* 1526 */         .putShort(index[i]);
/*      */     }
/* 1528 */     TypePath.put(typePath, typeAnnotation);
/*      */     
/* 1530 */     typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
/* 1531 */     if (visible) {
/* 1532 */       return this.lastCodeRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeVisibleTypeAnnotation);
/*      */     }
/*      */     
/* 1535 */     return this.lastCodeRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeInvisibleTypeAnnotation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLineNumber(int line, Label start) {
/* 1542 */     if (this.lineNumberTable == null) {
/* 1543 */       this.lineNumberTable = new ByteVector();
/*      */     }
/* 1545 */     this.lineNumberTableLength++;
/* 1546 */     this.lineNumberTable.putShort(start.bytecodeOffset);
/* 1547 */     this.lineNumberTable.putShort(line);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMaxs(int maxStack, int maxLocals) {
/* 1552 */     if (this.compute == 4) {
/* 1553 */       computeAllFrames();
/* 1554 */     } else if (this.compute == 1) {
/* 1555 */       computeMaxStackAndLocal();
/* 1556 */     } else if (this.compute == 2) {
/* 1557 */       this.maxStack = this.maxRelativeStackSize;
/*      */     } else {
/* 1559 */       this.maxStack = maxStack;
/* 1560 */       this.maxLocals = maxLocals;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void computeAllFrames() {
/* 1567 */     Handler handler = this.firstHandler;
/* 1568 */     while (handler != null) {
/* 1569 */       String catchTypeDescriptor = (handler.catchTypeDescriptor == null) ? "java/lang/Throwable" : handler.catchTypeDescriptor;
/*      */       
/* 1571 */       int catchType = Frame.getAbstractTypeFromInternalName(this.symbolTable, catchTypeDescriptor);
/*      */       
/* 1573 */       Label handlerBlock = handler.handlerPc.getCanonicalInstance();
/* 1574 */       handlerBlock.flags = (short)(handlerBlock.flags | 0x2);
/*      */       
/* 1576 */       Label handlerRangeBlock = handler.startPc.getCanonicalInstance();
/* 1577 */       Label handlerRangeEnd = handler.endPc.getCanonicalInstance();
/* 1578 */       while (handlerRangeBlock != handlerRangeEnd) {
/* 1579 */         handlerRangeBlock.outgoingEdges = new Edge(catchType, handlerBlock, handlerRangeBlock.outgoingEdges);
/*      */         
/* 1581 */         handlerRangeBlock = handlerRangeBlock.nextBasicBlock;
/*      */       } 
/* 1583 */       handler = handler.nextHandler;
/*      */     } 
/*      */ 
/*      */     
/* 1587 */     Frame firstFrame = this.firstBasicBlock.frame;
/* 1588 */     firstFrame.setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, this.maxLocals);
/* 1589 */     firstFrame.accept(this);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1597 */     Label listOfBlocksToProcess = this.firstBasicBlock;
/* 1598 */     listOfBlocksToProcess.nextListElement = Label.EMPTY_LIST;
/* 1599 */     int maxStackSize = 0;
/* 1600 */     while (listOfBlocksToProcess != Label.EMPTY_LIST) {
/*      */       
/* 1602 */       Label label = listOfBlocksToProcess;
/* 1603 */       listOfBlocksToProcess = listOfBlocksToProcess.nextListElement;
/* 1604 */       label.nextListElement = null;
/*      */       
/* 1606 */       label.flags = (short)(label.flags | 0x8);
/*      */       
/* 1608 */       int maxBlockStackSize = label.frame.getInputStackSize() + label.outputStackMax;
/* 1609 */       if (maxBlockStackSize > maxStackSize) {
/* 1610 */         maxStackSize = maxBlockStackSize;
/*      */       }
/*      */       
/* 1613 */       Edge outgoingEdge = label.outgoingEdges;
/* 1614 */       while (outgoingEdge != null) {
/* 1615 */         Label successorBlock = outgoingEdge.successor.getCanonicalInstance();
/*      */         
/* 1617 */         boolean successorBlockChanged = label.frame.merge(this.symbolTable, successorBlock.frame, outgoingEdge.info);
/* 1618 */         if (successorBlockChanged && successorBlock.nextListElement == null) {
/*      */ 
/*      */           
/* 1621 */           successorBlock.nextListElement = listOfBlocksToProcess;
/* 1622 */           listOfBlocksToProcess = successorBlock;
/*      */         } 
/* 1624 */         outgoingEdge = outgoingEdge.nextEdge;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1631 */     Label basicBlock = this.firstBasicBlock;
/* 1632 */     while (basicBlock != null) {
/* 1633 */       if ((basicBlock.flags & 0xA) == 10)
/*      */       {
/* 1635 */         basicBlock.frame.accept(this);
/*      */       }
/* 1637 */       if ((basicBlock.flags & 0x8) == 0) {
/*      */         
/* 1639 */         Label nextBasicBlock = basicBlock.nextBasicBlock;
/* 1640 */         int startOffset = basicBlock.bytecodeOffset;
/* 1641 */         int endOffset = ((nextBasicBlock == null) ? this.code.length : nextBasicBlock.bytecodeOffset) - 1;
/* 1642 */         if (endOffset >= startOffset) {
/*      */           
/* 1644 */           for (int i = startOffset; i < endOffset; i++) {
/* 1645 */             this.code.data[i] = 0;
/*      */           }
/* 1647 */           this.code.data[endOffset] = -65;
/*      */ 
/*      */           
/* 1650 */           int frameIndex = visitFrameStart(startOffset, 0, 1);
/* 1651 */           this.currentFrame[frameIndex] = 
/* 1652 */             Frame.getAbstractTypeFromInternalName(this.symbolTable, "java/lang/Throwable");
/* 1653 */           visitFrameEnd();
/*      */           
/* 1655 */           this.firstHandler = Handler.removeRange(this.firstHandler, basicBlock, nextBasicBlock);
/*      */           
/* 1657 */           maxStackSize = Math.max(maxStackSize, 1);
/*      */         } 
/*      */       } 
/* 1660 */       basicBlock = basicBlock.nextBasicBlock;
/*      */     } 
/*      */     
/* 1663 */     this.maxStack = maxStackSize;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void computeMaxStackAndLocal() {
/* 1669 */     Handler handler = this.firstHandler;
/* 1670 */     while (handler != null) {
/* 1671 */       Label handlerBlock = handler.handlerPc;
/* 1672 */       Label handlerRangeBlock = handler.startPc;
/* 1673 */       Label handlerRangeEnd = handler.endPc;
/*      */       
/* 1675 */       while (handlerRangeBlock != handlerRangeEnd) {
/* 1676 */         if ((handlerRangeBlock.flags & 0x10) == 0) {
/* 1677 */           handlerRangeBlock.outgoingEdges = new Edge(2147483647, handlerBlock, handlerRangeBlock.outgoingEdges);
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 1683 */           handlerRangeBlock.outgoingEdges.nextEdge.nextEdge = new Edge(2147483647, handlerBlock, handlerRangeBlock.outgoingEdges.nextEdge.nextEdge);
/*      */         } 
/*      */ 
/*      */         
/* 1687 */         handlerRangeBlock = handlerRangeBlock.nextBasicBlock;
/*      */       } 
/* 1689 */       handler = handler.nextHandler;
/*      */     } 
/*      */ 
/*      */     
/* 1693 */     if (this.hasSubroutines) {
/*      */ 
/*      */       
/* 1696 */       short numSubroutines = 1;
/* 1697 */       this.firstBasicBlock.markSubroutine(numSubroutines);
/*      */ 
/*      */       
/* 1700 */       for (short currentSubroutine = 1; currentSubroutine <= numSubroutines; currentSubroutine = (short)(currentSubroutine + 1)) {
/* 1701 */         Label label = this.firstBasicBlock;
/* 1702 */         while (label != null) {
/* 1703 */           if ((label.flags & 0x10) != 0 && label.subroutineId == currentSubroutine) {
/*      */             
/* 1705 */             Label jsrTarget = label.outgoingEdges.nextEdge.successor;
/* 1706 */             if (jsrTarget.subroutineId == 0) {
/*      */               
/* 1708 */               numSubroutines = (short)(numSubroutines + 1); jsrTarget.markSubroutine(numSubroutines);
/*      */             } 
/*      */           } 
/* 1711 */           label = label.nextBasicBlock;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1717 */       Label basicBlock = this.firstBasicBlock;
/* 1718 */       while (basicBlock != null) {
/* 1719 */         if ((basicBlock.flags & 0x10) != 0) {
/*      */ 
/*      */           
/* 1722 */           Label subroutine = basicBlock.outgoingEdges.nextEdge.successor;
/* 1723 */           subroutine.addSubroutineRetSuccessors(basicBlock);
/*      */         } 
/* 1725 */         basicBlock = basicBlock.nextBasicBlock;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1733 */     Label listOfBlocksToProcess = this.firstBasicBlock;
/* 1734 */     listOfBlocksToProcess.nextListElement = Label.EMPTY_LIST;
/* 1735 */     int maxStackSize = this.maxStack;
/* 1736 */     while (listOfBlocksToProcess != Label.EMPTY_LIST) {
/*      */ 
/*      */ 
/*      */       
/* 1740 */       Label basicBlock = listOfBlocksToProcess;
/* 1741 */       listOfBlocksToProcess = listOfBlocksToProcess.nextListElement;
/*      */       
/* 1743 */       int inputStackTop = basicBlock.inputStackSize;
/* 1744 */       int maxBlockStackSize = inputStackTop + basicBlock.outputStackMax;
/*      */       
/* 1746 */       if (maxBlockStackSize > maxStackSize) {
/* 1747 */         maxStackSize = maxBlockStackSize;
/*      */       }
/*      */ 
/*      */       
/* 1751 */       Edge outgoingEdge = basicBlock.outgoingEdges;
/* 1752 */       if ((basicBlock.flags & 0x10) != 0)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1757 */         outgoingEdge = outgoingEdge.nextEdge;
/*      */       }
/* 1759 */       while (outgoingEdge != null) {
/* 1760 */         Label successorBlock = outgoingEdge.successor;
/* 1761 */         if (successorBlock.nextListElement == null) {
/* 1762 */           successorBlock.inputStackSize = (short)((outgoingEdge.info == Integer.MAX_VALUE) ? 1 : (inputStackTop + outgoingEdge.info));
/*      */           
/* 1764 */           successorBlock.nextListElement = listOfBlocksToProcess;
/* 1765 */           listOfBlocksToProcess = successorBlock;
/*      */         } 
/* 1767 */         outgoingEdge = outgoingEdge.nextEdge;
/*      */       } 
/*      */     } 
/* 1770 */     this.maxStack = maxStackSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitEnd() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addSuccessorToCurrentBasicBlock(int info, Label successor) {
/* 1789 */     this.currentBasicBlock.outgoingEdges = new Edge(info, successor, this.currentBasicBlock.outgoingEdges);
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
/*      */   private void endCurrentBasicBlockWithNoSuccessor() {
/* 1801 */     if (this.compute == 4) {
/* 1802 */       Label nextBasicBlock = new Label();
/* 1803 */       nextBasicBlock.frame = new Frame(nextBasicBlock);
/* 1804 */       nextBasicBlock.resolve(this.code.data, this.code.length);
/* 1805 */       this.lastBasicBlock.nextBasicBlock = nextBasicBlock;
/* 1806 */       this.lastBasicBlock = nextBasicBlock;
/* 1807 */       this.currentBasicBlock = null;
/* 1808 */     } else if (this.compute == 1) {
/* 1809 */       this.currentBasicBlock.outputStackMax = (short)this.maxRelativeStackSize;
/* 1810 */       this.currentBasicBlock = null;
/*      */     } 
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
/*      */   
/*      */   int visitFrameStart(int offset, int numLocal, int numStack) {
/* 1827 */     int frameLength = 3 + numLocal + numStack;
/* 1828 */     if (this.currentFrame == null || this.currentFrame.length < frameLength) {
/* 1829 */       this.currentFrame = new int[frameLength];
/*      */     }
/* 1831 */     this.currentFrame[0] = offset;
/* 1832 */     this.currentFrame[1] = numLocal;
/* 1833 */     this.currentFrame[2] = numStack;
/* 1834 */     return 3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void visitAbstractType(int frameIndex, int abstractType) {
/* 1844 */     this.currentFrame[frameIndex] = abstractType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void visitFrameEnd() {
/* 1853 */     if (this.previousFrame != null) {
/* 1854 */       if (this.stackMapTableEntries == null) {
/* 1855 */         this.stackMapTableEntries = new ByteVector();
/*      */       }
/* 1857 */       putFrame();
/* 1858 */       this.stackMapTableNumberOfEntries++;
/*      */     } 
/* 1860 */     this.previousFrame = this.currentFrame;
/* 1861 */     this.currentFrame = null;
/*      */   }
/*      */ 
/*      */   
/*      */   private void putFrame() {
/* 1866 */     int numLocal = this.currentFrame[1];
/* 1867 */     int numStack = this.currentFrame[2];
/* 1868 */     if (this.symbolTable.getMajorVersion() < 50) {
/*      */       
/* 1870 */       this.stackMapTableEntries.putShort(this.currentFrame[0]).putShort(numLocal);
/* 1871 */       putAbstractTypes(3, 3 + numLocal);
/* 1872 */       this.stackMapTableEntries.putShort(numStack);
/* 1873 */       putAbstractTypes(3 + numLocal, 3 + numLocal + numStack);
/*      */       return;
/*      */     } 
/* 1876 */     int offsetDelta = (this.stackMapTableNumberOfEntries == 0) ? this.currentFrame[0] : (this.currentFrame[0] - this.previousFrame[0] - 1);
/*      */ 
/*      */ 
/*      */     
/* 1880 */     int previousNumlocal = this.previousFrame[1];
/* 1881 */     int numLocalDelta = numLocal - previousNumlocal;
/* 1882 */     int type = 255;
/* 1883 */     if (numStack == 0) {
/* 1884 */       switch (numLocalDelta) {
/*      */         case -3:
/*      */         case -2:
/*      */         case -1:
/* 1888 */           type = 248;
/*      */           break;
/*      */         case 0:
/* 1891 */           type = (offsetDelta < 64) ? 0 : 251;
/*      */           break;
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/* 1896 */           type = 252;
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */     
/* 1902 */     } else if (numLocalDelta == 0 && numStack == 1) {
/* 1903 */       type = (offsetDelta < 63) ? 64 : 247;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1908 */     if (type != 255) {
/*      */       
/* 1910 */       int frameIndex = 3;
/* 1911 */       for (int i = 0; i < previousNumlocal && i < numLocal; i++) {
/* 1912 */         if (this.currentFrame[frameIndex] != this.previousFrame[frameIndex]) {
/* 1913 */           type = 255;
/*      */           break;
/*      */         } 
/* 1916 */         frameIndex++;
/*      */       } 
/*      */     } 
/* 1919 */     switch (type) {
/*      */       case 0:
/* 1921 */         this.stackMapTableEntries.putByte(offsetDelta);
/*      */         return;
/*      */       case 64:
/* 1924 */         this.stackMapTableEntries.putByte(64 + offsetDelta);
/* 1925 */         putAbstractTypes(3 + numLocal, 4 + numLocal);
/*      */         return;
/*      */       case 247:
/* 1928 */         this.stackMapTableEntries
/* 1929 */           .putByte(247)
/* 1930 */           .putShort(offsetDelta);
/* 1931 */         putAbstractTypes(3 + numLocal, 4 + numLocal);
/*      */         return;
/*      */       case 251:
/* 1934 */         this.stackMapTableEntries.putByte(251).putShort(offsetDelta);
/*      */         return;
/*      */       case 248:
/* 1937 */         this.stackMapTableEntries
/* 1938 */           .putByte(251 + numLocalDelta)
/* 1939 */           .putShort(offsetDelta);
/*      */         return;
/*      */       case 252:
/* 1942 */         this.stackMapTableEntries
/* 1943 */           .putByte(251 + numLocalDelta)
/* 1944 */           .putShort(offsetDelta);
/* 1945 */         putAbstractTypes(3 + previousNumlocal, 3 + numLocal);
/*      */         return;
/*      */     } 
/*      */     
/* 1949 */     this.stackMapTableEntries.putByte(255).putShort(offsetDelta).putShort(numLocal);
/* 1950 */     putAbstractTypes(3, 3 + numLocal);
/* 1951 */     this.stackMapTableEntries.putShort(numStack);
/* 1952 */     putAbstractTypes(3 + numLocal, 3 + numLocal + numStack);
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
/*      */   private void putAbstractTypes(int start, int end) {
/* 1965 */     for (int i = start; i < end; i++) {
/* 1966 */       Frame.putAbstractType(this.symbolTable, this.currentFrame[i], this.stackMapTableEntries);
/*      */     }
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
/*      */   private void putFrameType(Object type) {
/* 1981 */     if (type instanceof Integer) {
/* 1982 */       this.stackMapTableEntries.putByte(((Integer)type).intValue());
/* 1983 */     } else if (type instanceof String) {
/* 1984 */       this.stackMapTableEntries
/* 1985 */         .putByte(7)
/* 1986 */         .putShort((this.symbolTable.addConstantClass((String)type)).index);
/*      */     } else {
/* 1988 */       this.stackMapTableEntries
/* 1989 */         .putByte(8)
/* 1990 */         .putShort(((Label)type).bytecodeOffset);
/*      */     } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean canCopyMethodAttributes(ClassReader source, int methodInfoOffset, int methodInfoLength, boolean hasSyntheticAttribute, boolean hasDeprecatedAttribute, int descriptorIndex, int signatureIndex, int exceptionsOffset) {
/* 2039 */     if (source != this.symbolTable.getSource() || descriptorIndex != this.descriptorIndex || signatureIndex != this.signatureIndex || hasDeprecatedAttribute != (((this.accessFlags & 0x20000) != 0)))
/*      */     {
/*      */ 
/*      */       
/* 2043 */       return false;
/*      */     }
/*      */     
/* 2046 */     boolean needSyntheticAttribute = (this.symbolTable.getMajorVersion() < 49 && (this.accessFlags & 0x1000) != 0);
/* 2047 */     if (hasSyntheticAttribute != needSyntheticAttribute) {
/* 2048 */       return false;
/*      */     }
/* 2050 */     if (exceptionsOffset == 0) {
/* 2051 */       if (this.numberOfExceptions != 0) {
/* 2052 */         return false;
/*      */       }
/* 2054 */     } else if (source.readUnsignedShort(exceptionsOffset) == this.numberOfExceptions) {
/* 2055 */       int currentExceptionOffset = exceptionsOffset + 2;
/* 2056 */       for (int i = 0; i < this.numberOfExceptions; i++) {
/* 2057 */         if (source.readUnsignedShort(currentExceptionOffset) != this.exceptionIndexTable[i]) {
/* 2058 */           return false;
/*      */         }
/* 2060 */         currentExceptionOffset += 2;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2066 */     this.sourceOffset = methodInfoOffset + 6;
/* 2067 */     this.sourceLength = methodInfoLength - 6;
/* 2068 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int computeMethodInfoSize() {
/* 2079 */     if (this.sourceOffset != 0)
/*      */     {
/* 2081 */       return 6 + this.sourceLength;
/*      */     }
/*      */     
/* 2084 */     int size = 8;
/*      */     
/* 2086 */     if (this.code.length > 0) {
/* 2087 */       if (this.code.length > 65535) {
/* 2088 */         throw new MethodTooLargeException(this.symbolTable
/* 2089 */             .getClassName(), this.name, this.descriptor, this.code.length);
/*      */       }
/* 2091 */       this.symbolTable.addConstantUtf8("Code");
/*      */ 
/*      */       
/* 2094 */       size += 16 + this.code.length + Handler.getExceptionTableSize(this.firstHandler);
/* 2095 */       if (this.stackMapTableEntries != null) {
/* 2096 */         boolean useStackMapTable = (this.symbolTable.getMajorVersion() >= 50);
/* 2097 */         this.symbolTable.addConstantUtf8(useStackMapTable ? "StackMapTable" : "StackMap");
/*      */         
/* 2099 */         size += 8 + this.stackMapTableEntries.length;
/*      */       } 
/* 2101 */       if (this.lineNumberTable != null) {
/* 2102 */         this.symbolTable.addConstantUtf8("LineNumberTable");
/*      */         
/* 2104 */         size += 8 + this.lineNumberTable.length;
/*      */       } 
/* 2106 */       if (this.localVariableTable != null) {
/* 2107 */         this.symbolTable.addConstantUtf8("LocalVariableTable");
/*      */         
/* 2109 */         size += 8 + this.localVariableTable.length;
/*      */       } 
/* 2111 */       if (this.localVariableTypeTable != null) {
/* 2112 */         this.symbolTable.addConstantUtf8("LocalVariableTypeTable");
/*      */         
/* 2114 */         size += 8 + this.localVariableTypeTable.length;
/*      */       } 
/* 2116 */       if (this.lastCodeRuntimeVisibleTypeAnnotation != null) {
/* 2117 */         size += this.lastCodeRuntimeVisibleTypeAnnotation
/* 2118 */           .computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
/*      */       }
/*      */       
/* 2121 */       if (this.lastCodeRuntimeInvisibleTypeAnnotation != null) {
/* 2122 */         size += this.lastCodeRuntimeInvisibleTypeAnnotation
/* 2123 */           .computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
/*      */       }
/*      */       
/* 2126 */       if (this.firstCodeAttribute != null) {
/* 2127 */         size += this.firstCodeAttribute
/* 2128 */           .computeAttributesSize(this.symbolTable, this.code.data, this.code.length, this.maxStack, this.maxLocals);
/*      */       }
/*      */     } 
/*      */     
/* 2132 */     if (this.numberOfExceptions > 0) {
/* 2133 */       this.symbolTable.addConstantUtf8("Exceptions");
/* 2134 */       size += 8 + 2 * this.numberOfExceptions;
/*      */     } 
/* 2136 */     boolean useSyntheticAttribute = (this.symbolTable.getMajorVersion() < 49);
/* 2137 */     if ((this.accessFlags & 0x1000) != 0 && useSyntheticAttribute) {
/* 2138 */       this.symbolTable.addConstantUtf8("Synthetic");
/* 2139 */       size += 6;
/*      */     } 
/* 2141 */     if (this.signatureIndex != 0) {
/* 2142 */       this.symbolTable.addConstantUtf8("Signature");
/* 2143 */       size += 8;
/*      */     } 
/* 2145 */     if ((this.accessFlags & 0x20000) != 0) {
/* 2146 */       this.symbolTable.addConstantUtf8("Deprecated");
/* 2147 */       size += 6;
/*      */     } 
/* 2149 */     if (this.lastRuntimeVisibleAnnotation != null) {
/* 2150 */       size += this.lastRuntimeVisibleAnnotation
/* 2151 */         .computeAnnotationsSize("RuntimeVisibleAnnotations");
/*      */     }
/*      */     
/* 2154 */     if (this.lastRuntimeInvisibleAnnotation != null) {
/* 2155 */       size += this.lastRuntimeInvisibleAnnotation
/* 2156 */         .computeAnnotationsSize("RuntimeInvisibleAnnotations");
/*      */     }
/*      */     
/* 2159 */     if (this.lastRuntimeVisibleParameterAnnotations != null) {
/* 2160 */       size += 
/* 2161 */         AnnotationWriter.computeParameterAnnotationsSize("RuntimeVisibleParameterAnnotations", this.lastRuntimeVisibleParameterAnnotations, (this.visibleAnnotableParameterCount == 0) ? this.lastRuntimeVisibleParameterAnnotations.length : this.visibleAnnotableParameterCount);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2168 */     if (this.lastRuntimeInvisibleParameterAnnotations != null) {
/* 2169 */       size += 
/* 2170 */         AnnotationWriter.computeParameterAnnotationsSize("RuntimeInvisibleParameterAnnotations", this.lastRuntimeInvisibleParameterAnnotations, (this.invisibleAnnotableParameterCount == 0) ? this.lastRuntimeInvisibleParameterAnnotations.length : this.invisibleAnnotableParameterCount);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2177 */     if (this.lastRuntimeVisibleTypeAnnotation != null) {
/* 2178 */       size += this.lastRuntimeVisibleTypeAnnotation
/* 2179 */         .computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
/*      */     }
/*      */     
/* 2182 */     if (this.lastRuntimeInvisibleTypeAnnotation != null) {
/* 2183 */       size += this.lastRuntimeInvisibleTypeAnnotation
/* 2184 */         .computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
/*      */     }
/*      */     
/* 2187 */     if (this.defaultValue != null) {
/* 2188 */       this.symbolTable.addConstantUtf8("AnnotationDefault");
/* 2189 */       size += 6 + this.defaultValue.length;
/*      */     } 
/* 2191 */     if (this.parameters != null) {
/* 2192 */       this.symbolTable.addConstantUtf8("MethodParameters");
/*      */       
/* 2194 */       size += 7 + this.parameters.length;
/*      */     } 
/* 2196 */     if (this.firstAttribute != null) {
/* 2197 */       size += this.firstAttribute.computeAttributesSize(this.symbolTable);
/*      */     }
/* 2199 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void putMethodInfo(ByteVector output) {
/* 2209 */     boolean useSyntheticAttribute = (this.symbolTable.getMajorVersion() < 49);
/* 2210 */     int mask = useSyntheticAttribute ? 4096 : 0;
/* 2211 */     output.putShort(this.accessFlags & (mask ^ 0xFFFFFFFF)).putShort(this.nameIndex).putShort(this.descriptorIndex);
/*      */     
/* 2213 */     if (this.sourceOffset != 0) {
/* 2214 */       output.putByteArray((this.symbolTable.getSource()).b, this.sourceOffset, this.sourceLength);
/*      */       
/*      */       return;
/*      */     } 
/* 2218 */     int attributeCount = 0;
/* 2219 */     if (this.code.length > 0) {
/* 2220 */       attributeCount++;
/*      */     }
/* 2222 */     if (this.numberOfExceptions > 0) {
/* 2223 */       attributeCount++;
/*      */     }
/* 2225 */     if ((this.accessFlags & 0x1000) != 0 && useSyntheticAttribute) {
/* 2226 */       attributeCount++;
/*      */     }
/* 2228 */     if (this.signatureIndex != 0) {
/* 2229 */       attributeCount++;
/*      */     }
/* 2231 */     if ((this.accessFlags & 0x20000) != 0) {
/* 2232 */       attributeCount++;
/*      */     }
/* 2234 */     if (this.lastRuntimeVisibleAnnotation != null) {
/* 2235 */       attributeCount++;
/*      */     }
/* 2237 */     if (this.lastRuntimeInvisibleAnnotation != null) {
/* 2238 */       attributeCount++;
/*      */     }
/* 2240 */     if (this.lastRuntimeVisibleParameterAnnotations != null) {
/* 2241 */       attributeCount++;
/*      */     }
/* 2243 */     if (this.lastRuntimeInvisibleParameterAnnotations != null) {
/* 2244 */       attributeCount++;
/*      */     }
/* 2246 */     if (this.lastRuntimeVisibleTypeAnnotation != null) {
/* 2247 */       attributeCount++;
/*      */     }
/* 2249 */     if (this.lastRuntimeInvisibleTypeAnnotation != null) {
/* 2250 */       attributeCount++;
/*      */     }
/* 2252 */     if (this.defaultValue != null) {
/* 2253 */       attributeCount++;
/*      */     }
/* 2255 */     if (this.parameters != null) {
/* 2256 */       attributeCount++;
/*      */     }
/* 2258 */     if (this.firstAttribute != null) {
/* 2259 */       attributeCount += this.firstAttribute.getAttributeCount();
/*      */     }
/*      */     
/* 2262 */     output.putShort(attributeCount);
/* 2263 */     if (this.code.length > 0) {
/*      */ 
/*      */       
/* 2266 */       int size = 10 + this.code.length + Handler.getExceptionTableSize(this.firstHandler);
/* 2267 */       int codeAttributeCount = 0;
/* 2268 */       if (this.stackMapTableEntries != null) {
/*      */         
/* 2270 */         size += 8 + this.stackMapTableEntries.length;
/* 2271 */         codeAttributeCount++;
/*      */       } 
/* 2273 */       if (this.lineNumberTable != null) {
/*      */         
/* 2275 */         size += 8 + this.lineNumberTable.length;
/* 2276 */         codeAttributeCount++;
/*      */       } 
/* 2278 */       if (this.localVariableTable != null) {
/*      */         
/* 2280 */         size += 8 + this.localVariableTable.length;
/* 2281 */         codeAttributeCount++;
/*      */       } 
/* 2283 */       if (this.localVariableTypeTable != null) {
/*      */         
/* 2285 */         size += 8 + this.localVariableTypeTable.length;
/* 2286 */         codeAttributeCount++;
/*      */       } 
/* 2288 */       if (this.lastCodeRuntimeVisibleTypeAnnotation != null) {
/* 2289 */         size += this.lastCodeRuntimeVisibleTypeAnnotation
/* 2290 */           .computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
/*      */         
/* 2292 */         codeAttributeCount++;
/*      */       } 
/* 2294 */       if (this.lastCodeRuntimeInvisibleTypeAnnotation != null) {
/* 2295 */         size += this.lastCodeRuntimeInvisibleTypeAnnotation
/* 2296 */           .computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
/*      */         
/* 2298 */         codeAttributeCount++;
/*      */       } 
/* 2300 */       if (this.firstCodeAttribute != null) {
/* 2301 */         size += this.firstCodeAttribute
/* 2302 */           .computeAttributesSize(this.symbolTable, this.code.data, this.code.length, this.maxStack, this.maxLocals);
/*      */         
/* 2304 */         codeAttributeCount += this.firstCodeAttribute.getAttributeCount();
/*      */       } 
/* 2306 */       output
/* 2307 */         .putShort(this.symbolTable.addConstantUtf8("Code"))
/* 2308 */         .putInt(size)
/* 2309 */         .putShort(this.maxStack)
/* 2310 */         .putShort(this.maxLocals)
/* 2311 */         .putInt(this.code.length)
/* 2312 */         .putByteArray(this.code.data, 0, this.code.length);
/* 2313 */       Handler.putExceptionTable(this.firstHandler, output);
/* 2314 */       output.putShort(codeAttributeCount);
/* 2315 */       if (this.stackMapTableEntries != null) {
/* 2316 */         boolean useStackMapTable = (this.symbolTable.getMajorVersion() >= 50);
/* 2317 */         output
/* 2318 */           .putShort(this.symbolTable
/* 2319 */             .addConstantUtf8(useStackMapTable ? "StackMapTable" : "StackMap"))
/*      */           
/* 2321 */           .putInt(2 + this.stackMapTableEntries.length)
/* 2322 */           .putShort(this.stackMapTableNumberOfEntries)
/* 2323 */           .putByteArray(this.stackMapTableEntries.data, 0, this.stackMapTableEntries.length);
/*      */       } 
/* 2325 */       if (this.lineNumberTable != null) {
/* 2326 */         output
/* 2327 */           .putShort(this.symbolTable.addConstantUtf8("LineNumberTable"))
/* 2328 */           .putInt(2 + this.lineNumberTable.length)
/* 2329 */           .putShort(this.lineNumberTableLength)
/* 2330 */           .putByteArray(this.lineNumberTable.data, 0, this.lineNumberTable.length);
/*      */       }
/* 2332 */       if (this.localVariableTable != null) {
/* 2333 */         output
/* 2334 */           .putShort(this.symbolTable.addConstantUtf8("LocalVariableTable"))
/* 2335 */           .putInt(2 + this.localVariableTable.length)
/* 2336 */           .putShort(this.localVariableTableLength)
/* 2337 */           .putByteArray(this.localVariableTable.data, 0, this.localVariableTable.length);
/*      */       }
/* 2339 */       if (this.localVariableTypeTable != null) {
/* 2340 */         output
/* 2341 */           .putShort(this.symbolTable.addConstantUtf8("LocalVariableTypeTable"))
/* 2342 */           .putInt(2 + this.localVariableTypeTable.length)
/* 2343 */           .putShort(this.localVariableTypeTableLength)
/* 2344 */           .putByteArray(this.localVariableTypeTable.data, 0, this.localVariableTypeTable.length);
/*      */       }
/* 2346 */       if (this.lastCodeRuntimeVisibleTypeAnnotation != null) {
/* 2347 */         this.lastCodeRuntimeVisibleTypeAnnotation.putAnnotations(this.symbolTable
/* 2348 */             .addConstantUtf8("RuntimeVisibleTypeAnnotations"), output);
/*      */       }
/* 2350 */       if (this.lastCodeRuntimeInvisibleTypeAnnotation != null) {
/* 2351 */         this.lastCodeRuntimeInvisibleTypeAnnotation.putAnnotations(this.symbolTable
/* 2352 */             .addConstantUtf8("RuntimeInvisibleTypeAnnotations"), output);
/*      */       }
/* 2354 */       if (this.firstCodeAttribute != null) {
/* 2355 */         this.firstCodeAttribute.putAttributes(this.symbolTable, this.code.data, this.code.length, this.maxStack, this.maxLocals, output);
/*      */       }
/*      */     } 
/*      */     
/* 2359 */     if (this.numberOfExceptions > 0) {
/* 2360 */       output
/* 2361 */         .putShort(this.symbolTable.addConstantUtf8("Exceptions"))
/* 2362 */         .putInt(2 + 2 * this.numberOfExceptions)
/* 2363 */         .putShort(this.numberOfExceptions);
/* 2364 */       for (int exceptionIndex : this.exceptionIndexTable) {
/* 2365 */         output.putShort(exceptionIndex);
/*      */       }
/*      */     } 
/* 2368 */     if ((this.accessFlags & 0x1000) != 0 && useSyntheticAttribute) {
/* 2369 */       output.putShort(this.symbolTable.addConstantUtf8("Synthetic")).putInt(0);
/*      */     }
/* 2371 */     if (this.signatureIndex != 0) {
/* 2372 */       output
/* 2373 */         .putShort(this.symbolTable.addConstantUtf8("Signature"))
/* 2374 */         .putInt(2)
/* 2375 */         .putShort(this.signatureIndex);
/*      */     }
/* 2377 */     if ((this.accessFlags & 0x20000) != 0) {
/* 2378 */       output.putShort(this.symbolTable.addConstantUtf8("Deprecated")).putInt(0);
/*      */     }
/* 2380 */     if (this.lastRuntimeVisibleAnnotation != null) {
/* 2381 */       this.lastRuntimeVisibleAnnotation.putAnnotations(this.symbolTable
/* 2382 */           .addConstantUtf8("RuntimeVisibleAnnotations"), output);
/*      */     }
/* 2384 */     if (this.lastRuntimeInvisibleAnnotation != null) {
/* 2385 */       this.lastRuntimeInvisibleAnnotation.putAnnotations(this.symbolTable
/* 2386 */           .addConstantUtf8("RuntimeInvisibleAnnotations"), output);
/*      */     }
/* 2388 */     if (this.lastRuntimeVisibleParameterAnnotations != null) {
/* 2389 */       AnnotationWriter.putParameterAnnotations(this.symbolTable
/* 2390 */           .addConstantUtf8("RuntimeVisibleParameterAnnotations"), this.lastRuntimeVisibleParameterAnnotations, (this.visibleAnnotableParameterCount == 0) ? this.lastRuntimeVisibleParameterAnnotations.length : this.visibleAnnotableParameterCount, output);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2397 */     if (this.lastRuntimeInvisibleParameterAnnotations != null) {
/* 2398 */       AnnotationWriter.putParameterAnnotations(this.symbolTable
/* 2399 */           .addConstantUtf8("RuntimeInvisibleParameterAnnotations"), this.lastRuntimeInvisibleParameterAnnotations, (this.invisibleAnnotableParameterCount == 0) ? this.lastRuntimeInvisibleParameterAnnotations.length : this.invisibleAnnotableParameterCount, output);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2406 */     if (this.lastRuntimeVisibleTypeAnnotation != null) {
/* 2407 */       this.lastRuntimeVisibleTypeAnnotation.putAnnotations(this.symbolTable
/* 2408 */           .addConstantUtf8("RuntimeVisibleTypeAnnotations"), output);
/*      */     }
/* 2410 */     if (this.lastRuntimeInvisibleTypeAnnotation != null) {
/* 2411 */       this.lastRuntimeInvisibleTypeAnnotation.putAnnotations(this.symbolTable
/* 2412 */           .addConstantUtf8("RuntimeInvisibleTypeAnnotations"), output);
/*      */     }
/* 2414 */     if (this.defaultValue != null) {
/* 2415 */       output
/* 2416 */         .putShort(this.symbolTable.addConstantUtf8("AnnotationDefault"))
/* 2417 */         .putInt(this.defaultValue.length)
/* 2418 */         .putByteArray(this.defaultValue.data, 0, this.defaultValue.length);
/*      */     }
/* 2420 */     if (this.parameters != null) {
/* 2421 */       output
/* 2422 */         .putShort(this.symbolTable.addConstantUtf8("MethodParameters"))
/* 2423 */         .putInt(1 + this.parameters.length)
/* 2424 */         .putByte(this.parametersCount)
/* 2425 */         .putByteArray(this.parameters.data, 0, this.parameters.length);
/*      */     }
/* 2427 */     if (this.firstAttribute != null) {
/* 2428 */       this.firstAttribute.putAttributes(this.symbolTable, output);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void collectAttributePrototypes(Attribute.Set attributePrototypes) {
/* 2438 */     attributePrototypes.addAttributes(this.firstAttribute);
/* 2439 */     attributePrototypes.addAttributes(this.firstCodeAttribute);
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/MethodWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */