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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class Frame
/*      */ {
/*      */   static final int SAME_FRAME = 0;
/*      */   static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
/*      */   static final int RESERVED = 128;
/*      */   static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
/*      */   static final int CHOP_FRAME = 248;
/*      */   static final int SAME_FRAME_EXTENDED = 251;
/*      */   static final int APPEND_FRAME = 252;
/*      */   static final int FULL_FRAME = 255;
/*      */   static final int ITEM_TOP = 0;
/*      */   static final int ITEM_INTEGER = 1;
/*      */   static final int ITEM_FLOAT = 2;
/*      */   static final int ITEM_DOUBLE = 3;
/*      */   static final int ITEM_LONG = 4;
/*      */   static final int ITEM_NULL = 5;
/*      */   static final int ITEM_UNINITIALIZED_THIS = 6;
/*      */   static final int ITEM_OBJECT = 7;
/*      */   static final int ITEM_UNINITIALIZED = 8;
/*      */   private static final int ITEM_ASM_BOOLEAN = 9;
/*      */   private static final int ITEM_ASM_BYTE = 10;
/*      */   private static final int ITEM_ASM_CHAR = 11;
/*      */   private static final int ITEM_ASM_SHORT = 12;
/*      */   private static final int DIM_MASK = -268435456;
/*      */   private static final int KIND_MASK = 251658240;
/*      */   private static final int FLAGS_MASK = 15728640;
/*      */   private static final int VALUE_MASK = 1048575;
/*      */   private static final int DIM_SHIFT = 28;
/*      */   private static final int ARRAY_OF = 268435456;
/*      */   private static final int ELEMENT_OF = -268435456;
/*      */   private static final int CONSTANT_KIND = 16777216;
/*      */   private static final int REFERENCE_KIND = 33554432;
/*      */   private static final int UNINITIALIZED_KIND = 50331648;
/*      */   private static final int LOCAL_KIND = 67108864;
/*      */   private static final int STACK_KIND = 83886080;
/*      */   private static final int TOP_IF_LONG_OR_DOUBLE_FLAG = 1048576;
/*      */   private static final int TOP = 16777216;
/*      */   private static final int BOOLEAN = 16777225;
/*      */   private static final int BYTE = 16777226;
/*      */   private static final int CHAR = 16777227;
/*      */   private static final int SHORT = 16777228;
/*      */   private static final int INTEGER = 16777217;
/*      */   private static final int FLOAT = 16777218;
/*      */   private static final int LONG = 16777220;
/*      */   private static final int DOUBLE = 16777219;
/*      */   private static final int NULL = 16777221;
/*      */   private static final int UNINITIALIZED_THIS = 16777222;
/*      */   Label owner;
/*      */   private int[] inputLocals;
/*      */   private int[] inputStack;
/*      */   private int[] outputLocals;
/*      */   private int[] outputStack;
/*      */   private short outputStackStart;
/*      */   private short outputStackTop;
/*      */   private int initializationCount;
/*      */   private int[] initializations;
/*      */   
/*      */   Frame(Label owner) {
/*  236 */     this.owner = owner;
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
/*      */   final void copyFrom(Frame frame) {
/*  248 */     this.inputLocals = frame.inputLocals;
/*  249 */     this.inputStack = frame.inputStack;
/*  250 */     this.outputStackStart = 0;
/*  251 */     this.outputLocals = frame.outputLocals;
/*  252 */     this.outputStack = frame.outputStack;
/*  253 */     this.outputStackTop = frame.outputStackTop;
/*  254 */     this.initializationCount = frame.initializationCount;
/*  255 */     this.initializations = frame.initializations;
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
/*      */   static int getAbstractTypeFromApiFormat(SymbolTable symbolTable, Object type) {
/*  274 */     if (type instanceof Integer)
/*  275 */       return 0x1000000 | ((Integer)type).intValue(); 
/*  276 */     if (type instanceof String) {
/*  277 */       String descriptor = Type.getObjectType((String)type).getDescriptor();
/*  278 */       return getAbstractTypeFromDescriptor(symbolTable, descriptor, 0);
/*      */     } 
/*  280 */     return 0x3000000 | symbolTable
/*  281 */       .addUninitializedType("", ((Label)type).bytecodeOffset);
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
/*      */   static int getAbstractTypeFromInternalName(SymbolTable symbolTable, String internalName) {
/*  295 */     return 0x2000000 | symbolTable.addType(internalName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getAbstractTypeFromDescriptor(SymbolTable symbolTable, String buffer, int offset) {
/*      */     String internalName;
/*      */     int elementDescriptorOffset;
/*      */     int typeValue;
/*  309 */     switch (buffer.charAt(offset)) {
/*      */       case 'V':
/*  311 */         return 0;
/*      */       case 'B':
/*      */       case 'C':
/*      */       case 'I':
/*      */       case 'S':
/*      */       case 'Z':
/*  317 */         return 16777217;
/*      */       case 'F':
/*  319 */         return 16777218;
/*      */       case 'J':
/*  321 */         return 16777220;
/*      */       case 'D':
/*  323 */         return 16777219;
/*      */       case 'L':
/*  325 */         internalName = buffer.substring(offset + 1, buffer.length() - 1);
/*  326 */         return 0x2000000 | symbolTable.addType(internalName);
/*      */       case '[':
/*  328 */         elementDescriptorOffset = offset + 1;
/*  329 */         while (buffer.charAt(elementDescriptorOffset) == '[') {
/*  330 */           elementDescriptorOffset++;
/*      */         }
/*      */         
/*  333 */         switch (buffer.charAt(elementDescriptorOffset)) {
/*      */           case 'Z':
/*  335 */             typeValue = 16777225;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  365 */             return elementDescriptorOffset - offset << 28 | typeValue;case 'C': typeValue = 16777227; return elementDescriptorOffset - offset << 28 | typeValue;case 'B': typeValue = 16777226; return elementDescriptorOffset - offset << 28 | typeValue;case 'S': typeValue = 16777228; return elementDescriptorOffset - offset << 28 | typeValue;case 'I': typeValue = 16777217; return elementDescriptorOffset - offset << 28 | typeValue;case 'F': typeValue = 16777218; return elementDescriptorOffset - offset << 28 | typeValue;case 'J': typeValue = 16777220; return elementDescriptorOffset - offset << 28 | typeValue;case 'D': typeValue = 16777219; return elementDescriptorOffset - offset << 28 | typeValue;case 'L': internalName = buffer.substring(elementDescriptorOffset + 1, buffer.length() - 1); typeValue = 0x2000000 | symbolTable.addType(internalName); return elementDescriptorOffset - offset << 28 | typeValue;
/*      */         }  throw new IllegalArgumentException();
/*  367 */     }  throw new IllegalArgumentException();
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
/*      */   final void setInputFrameFromDescriptor(SymbolTable symbolTable, int access, String descriptor, int maxLocals) {
/*  390 */     this.inputLocals = new int[maxLocals];
/*  391 */     this.inputStack = new int[0];
/*  392 */     int inputLocalIndex = 0;
/*  393 */     if ((access & 0x8) == 0) {
/*  394 */       if ((access & 0x40000) == 0) {
/*  395 */         this.inputLocals[inputLocalIndex++] = 0x2000000 | symbolTable
/*  396 */           .addType(symbolTable.getClassName());
/*      */       } else {
/*  398 */         this.inputLocals[inputLocalIndex++] = 16777222;
/*      */       } 
/*      */     }
/*  401 */     for (Type argumentType : Type.getArgumentTypes(descriptor)) {
/*      */       
/*  403 */       int abstractType = getAbstractTypeFromDescriptor(symbolTable, argumentType.getDescriptor(), 0);
/*  404 */       this.inputLocals[inputLocalIndex++] = abstractType;
/*  405 */       if (abstractType == 16777220 || abstractType == 16777219) {
/*  406 */         this.inputLocals[inputLocalIndex++] = 16777216;
/*      */       }
/*      */     } 
/*  409 */     while (inputLocalIndex < maxLocals) {
/*  410 */       this.inputLocals[inputLocalIndex++] = 16777216;
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
/*      */   final void setInputFrameFromApiFormat(SymbolTable symbolTable, int numLocal, Object[] local, int numStack, Object[] stack) {
/*  431 */     int inputLocalIndex = 0;
/*  432 */     for (int i = 0; i < numLocal; i++) {
/*  433 */       this.inputLocals[inputLocalIndex++] = getAbstractTypeFromApiFormat(symbolTable, local[i]);
/*  434 */       if (local[i] == Opcodes.LONG || local[i] == Opcodes.DOUBLE) {
/*  435 */         this.inputLocals[inputLocalIndex++] = 16777216;
/*      */       }
/*      */     } 
/*  438 */     while (inputLocalIndex < this.inputLocals.length) {
/*  439 */       this.inputLocals[inputLocalIndex++] = 16777216;
/*      */     }
/*  441 */     int numStackTop = 0;
/*  442 */     for (int j = 0; j < numStack; j++) {
/*  443 */       if (stack[j] == Opcodes.LONG || stack[j] == Opcodes.DOUBLE) {
/*  444 */         numStackTop++;
/*      */       }
/*      */     } 
/*  447 */     this.inputStack = new int[numStack + numStackTop];
/*  448 */     int inputStackIndex = 0;
/*  449 */     for (int k = 0; k < numStack; k++) {
/*  450 */       this.inputStack[inputStackIndex++] = getAbstractTypeFromApiFormat(symbolTable, stack[k]);
/*  451 */       if (stack[k] == Opcodes.LONG || stack[k] == Opcodes.DOUBLE) {
/*  452 */         this.inputStack[inputStackIndex++] = 16777216;
/*      */       }
/*      */     } 
/*  455 */     this.outputStackTop = 0;
/*  456 */     this.initializationCount = 0;
/*      */   }
/*      */   
/*      */   final int getInputStackSize() {
/*  460 */     return this.inputStack.length;
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
/*      */   private int getLocal(int localIndex) {
/*  474 */     if (this.outputLocals == null || localIndex >= this.outputLocals.length)
/*      */     {
/*      */       
/*  477 */       return 0x4000000 | localIndex;
/*      */     }
/*  479 */     int abstractType = this.outputLocals[localIndex];
/*  480 */     if (abstractType == 0)
/*      */     {
/*      */       
/*  483 */       abstractType = this.outputLocals[localIndex] = 0x4000000 | localIndex;
/*      */     }
/*  485 */     return abstractType;
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
/*      */   private void setLocal(int localIndex, int abstractType) {
/*  497 */     if (this.outputLocals == null) {
/*  498 */       this.outputLocals = new int[10];
/*      */     }
/*  500 */     int outputLocalsLength = this.outputLocals.length;
/*  501 */     if (localIndex >= outputLocalsLength) {
/*  502 */       int[] newOutputLocals = new int[Math.max(localIndex + 1, 2 * outputLocalsLength)];
/*  503 */       System.arraycopy(this.outputLocals, 0, newOutputLocals, 0, outputLocalsLength);
/*  504 */       this.outputLocals = newOutputLocals;
/*      */     } 
/*      */     
/*  507 */     this.outputLocals[localIndex] = abstractType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void push(int abstractType) {
/*  517 */     if (this.outputStack == null) {
/*  518 */       this.outputStack = new int[10];
/*      */     }
/*  520 */     int outputStackLength = this.outputStack.length;
/*  521 */     if (this.outputStackTop >= outputStackLength) {
/*  522 */       int[] newOutputStack = new int[Math.max(this.outputStackTop + 1, 2 * outputStackLength)];
/*  523 */       System.arraycopy(this.outputStack, 0, newOutputStack, 0, outputStackLength);
/*  524 */       this.outputStack = newOutputStack;
/*      */     } 
/*      */     
/*  527 */     this.outputStackTop = (short)(this.outputStackTop + 1); this.outputStack[this.outputStackTop] = abstractType;
/*      */ 
/*      */     
/*  530 */     short outputStackSize = (short)(this.outputStackStart + this.outputStackTop);
/*  531 */     if (outputStackSize > this.owner.outputStackMax) {
/*  532 */       this.owner.outputStackMax = outputStackSize;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void push(SymbolTable symbolTable, String descriptor) {
/*  543 */     int typeDescriptorOffset = (descriptor.charAt(0) == '(') ? (descriptor.indexOf(')') + 1) : 0;
/*  544 */     int abstractType = getAbstractTypeFromDescriptor(symbolTable, descriptor, typeDescriptorOffset);
/*  545 */     if (abstractType != 0) {
/*  546 */       push(abstractType);
/*  547 */       if (abstractType == 16777220 || abstractType == 16777219) {
/*  548 */         push(16777216);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int pop() {
/*  559 */     if (this.outputStackTop > 0) {
/*  560 */       return this.outputStack[this.outputStackTop = (short)(this.outputStackTop - 1)];
/*      */     }
/*      */     
/*  563 */     return 0x5000000 | -(this.outputStackStart = (short)(this.outputStackStart - 1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pop(int elements) {
/*  573 */     if (this.outputStackTop >= elements) {
/*  574 */       this.outputStackTop = (short)(this.outputStackTop - elements);
/*      */     }
/*      */     else {
/*      */       
/*  578 */       this.outputStackStart = (short)(this.outputStackStart - elements - this.outputStackTop);
/*  579 */       this.outputStackTop = 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pop(String descriptor) {
/*  589 */     char firstDescriptorChar = descriptor.charAt(0);
/*  590 */     if (firstDescriptorChar == '(') {
/*  591 */       pop((Type.getArgumentsAndReturnSizes(descriptor) >> 2) - 1);
/*  592 */     } else if (firstDescriptorChar == 'J' || firstDescriptorChar == 'D') {
/*  593 */       pop(2);
/*      */     } else {
/*  595 */       pop(1);
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
/*      */   private void addInitializedType(int abstractType) {
/*  611 */     if (this.initializations == null) {
/*  612 */       this.initializations = new int[2];
/*      */     }
/*  614 */     int initializationsLength = this.initializations.length;
/*  615 */     if (this.initializationCount >= initializationsLength) {
/*      */       
/*  617 */       int[] newInitializations = new int[Math.max(this.initializationCount + 1, 2 * initializationsLength)];
/*  618 */       System.arraycopy(this.initializations, 0, newInitializations, 0, initializationsLength);
/*  619 */       this.initializations = newInitializations;
/*      */     } 
/*      */     
/*  622 */     this.initializations[this.initializationCount++] = abstractType;
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
/*      */   private int getInitializedType(SymbolTable symbolTable, int abstractType) {
/*  635 */     if (abstractType == 16777222 || (abstractType & 0xFF000000) == 50331648)
/*      */     {
/*  637 */       for (int i = 0; i < this.initializationCount; i++) {
/*  638 */         int initializedType = this.initializations[i];
/*  639 */         int dim = initializedType & 0xF0000000;
/*  640 */         int kind = initializedType & 0xF000000;
/*  641 */         int value = initializedType & 0xFFFFF;
/*  642 */         if (kind == 67108864) {
/*  643 */           initializedType = dim + this.inputLocals[value];
/*  644 */         } else if (kind == 83886080) {
/*  645 */           initializedType = dim + this.inputStack[this.inputStack.length - value];
/*      */         } 
/*  647 */         if (abstractType == initializedType) {
/*  648 */           if (abstractType == 16777222) {
/*  649 */             return 0x2000000 | symbolTable.addType(symbolTable.getClassName());
/*      */           }
/*  651 */           return 0x2000000 | symbolTable
/*  652 */             .addType((symbolTable.getType(abstractType & 0xFFFFF)).value);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  657 */     return abstractType;
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
/*      */   void execute(int opcode, int arg, Symbol argSymbol, SymbolTable symbolTable) {
/*      */     int abstractType1;
/*      */     int abstractType2;
/*      */     int abstractType3;
/*      */     int abstractType4;
/*      */     String arrayElementType;
/*      */     String castType;
/*  679 */     switch (opcode) {
/*      */       case 0:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/*      */       case 145:
/*      */       case 146:
/*      */       case 147:
/*      */       case 167:
/*      */       case 177:
/*      */         return;
/*      */       case 1:
/*  692 */         push(16777221);
/*      */       
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 16:
/*      */       case 17:
/*      */       case 21:
/*  704 */         push(16777217);
/*      */       
/*      */       case 9:
/*      */       case 10:
/*      */       case 22:
/*  709 */         push(16777220);
/*  710 */         push(16777216);
/*      */       
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 23:
/*  716 */         push(16777218);
/*      */       
/*      */       case 14:
/*      */       case 15:
/*      */       case 24:
/*  721 */         push(16777219);
/*  722 */         push(16777216);
/*      */       
/*      */       case 18:
/*  725 */         switch (argSymbol.tag) {
/*      */           case 3:
/*  727 */             push(16777217);
/*      */           
/*      */           case 5:
/*  730 */             push(16777220);
/*  731 */             push(16777216);
/*      */           
/*      */           case 4:
/*  734 */             push(16777218);
/*      */           
/*      */           case 6:
/*  737 */             push(16777219);
/*  738 */             push(16777216);
/*      */           
/*      */           case 7:
/*  741 */             push(0x2000000 | symbolTable.addType("java/lang/Class"));
/*      */           
/*      */           case 8:
/*  744 */             push(0x2000000 | symbolTable.addType("java/lang/String"));
/*      */           
/*      */           case 16:
/*  747 */             push(0x2000000 | symbolTable.addType("java/lang/invoke/MethodType"));
/*      */           
/*      */           case 15:
/*  750 */             push(0x2000000 | symbolTable.addType("java/lang/invoke/MethodHandle"));
/*      */           
/*      */           case 17:
/*  753 */             push(symbolTable, argSymbol.value);
/*      */         } 
/*      */         
/*  756 */         throw new AssertionError();
/*      */ 
/*      */       
/*      */       case 25:
/*  760 */         push(getLocal(arg));
/*      */       
/*      */       case 47:
/*      */       case 143:
/*  764 */         pop(2);
/*  765 */         push(16777220);
/*  766 */         push(16777216);
/*      */       
/*      */       case 49:
/*      */       case 138:
/*  770 */         pop(2);
/*  771 */         push(16777219);
/*  772 */         push(16777216);
/*      */       
/*      */       case 50:
/*  775 */         pop(1);
/*  776 */         abstractType1 = pop();
/*  777 */         push((abstractType1 == 16777221) ? abstractType1 : (-268435456 + abstractType1));
/*      */       
/*      */       case 54:
/*      */       case 56:
/*      */       case 58:
/*  782 */         abstractType1 = pop();
/*  783 */         setLocal(arg, abstractType1);
/*  784 */         if (arg > 0) {
/*  785 */           int previousLocalType = getLocal(arg - 1);
/*  786 */           if (previousLocalType == 16777220 || previousLocalType == 16777219) {
/*  787 */             setLocal(arg - 1, 16777216);
/*  788 */           } else if ((previousLocalType & 0xF000000) == 67108864 || (previousLocalType & 0xF000000) == 83886080) {
/*      */ 
/*      */ 
/*      */             
/*  792 */             setLocal(arg - 1, previousLocalType | 0x100000);
/*      */           } 
/*      */         } 
/*      */       
/*      */       case 55:
/*      */       case 57:
/*  798 */         pop(1);
/*  799 */         abstractType1 = pop();
/*  800 */         setLocal(arg, abstractType1);
/*  801 */         setLocal(arg + 1, 16777216);
/*  802 */         if (arg > 0) {
/*  803 */           int previousLocalType = getLocal(arg - 1);
/*  804 */           if (previousLocalType == 16777220 || previousLocalType == 16777219) {
/*  805 */             setLocal(arg - 1, 16777216);
/*  806 */           } else if ((previousLocalType & 0xF000000) == 67108864 || (previousLocalType & 0xF000000) == 83886080) {
/*      */ 
/*      */ 
/*      */             
/*  810 */             setLocal(arg - 1, previousLocalType | 0x100000);
/*      */           } 
/*      */         } 
/*      */       
/*      */       case 79:
/*      */       case 81:
/*      */       case 83:
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/*  820 */         pop(3);
/*      */       
/*      */       case 80:
/*      */       case 82:
/*  824 */         pop(4);
/*      */       
/*      */       case 87:
/*      */       case 153:
/*      */       case 154:
/*      */       case 155:
/*      */       case 156:
/*      */       case 157:
/*      */       case 158:
/*      */       case 170:
/*      */       case 171:
/*      */       case 172:
/*      */       case 174:
/*      */       case 176:
/*      */       case 191:
/*      */       case 194:
/*      */       case 195:
/*      */       case 198:
/*      */       case 199:
/*  843 */         pop(1);
/*      */       
/*      */       case 88:
/*      */       case 159:
/*      */       case 160:
/*      */       case 161:
/*      */       case 162:
/*      */       case 163:
/*      */       case 164:
/*      */       case 165:
/*      */       case 166:
/*      */       case 173:
/*      */       case 175:
/*  856 */         pop(2);
/*      */       
/*      */       case 89:
/*  859 */         abstractType1 = pop();
/*  860 */         push(abstractType1);
/*  861 */         push(abstractType1);
/*      */       
/*      */       case 90:
/*  864 */         abstractType1 = pop();
/*  865 */         abstractType2 = pop();
/*  866 */         push(abstractType1);
/*  867 */         push(abstractType2);
/*  868 */         push(abstractType1);
/*      */       
/*      */       case 91:
/*  871 */         abstractType1 = pop();
/*  872 */         abstractType2 = pop();
/*  873 */         abstractType3 = pop();
/*  874 */         push(abstractType1);
/*  875 */         push(abstractType3);
/*  876 */         push(abstractType2);
/*  877 */         push(abstractType1);
/*      */       
/*      */       case 92:
/*  880 */         abstractType1 = pop();
/*  881 */         abstractType2 = pop();
/*  882 */         push(abstractType2);
/*  883 */         push(abstractType1);
/*  884 */         push(abstractType2);
/*  885 */         push(abstractType1);
/*      */       
/*      */       case 93:
/*  888 */         abstractType1 = pop();
/*  889 */         abstractType2 = pop();
/*  890 */         abstractType3 = pop();
/*  891 */         push(abstractType2);
/*  892 */         push(abstractType1);
/*  893 */         push(abstractType3);
/*  894 */         push(abstractType2);
/*  895 */         push(abstractType1);
/*      */       
/*      */       case 94:
/*  898 */         abstractType1 = pop();
/*  899 */         abstractType2 = pop();
/*  900 */         abstractType3 = pop();
/*  901 */         abstractType4 = pop();
/*  902 */         push(abstractType2);
/*  903 */         push(abstractType1);
/*  904 */         push(abstractType4);
/*  905 */         push(abstractType3);
/*  906 */         push(abstractType2);
/*  907 */         push(abstractType1);
/*      */       
/*      */       case 95:
/*  910 */         abstractType1 = pop();
/*  911 */         abstractType2 = pop();
/*  912 */         push(abstractType1);
/*  913 */         push(abstractType2);
/*      */       
/*      */       case 46:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 96:
/*      */       case 100:
/*      */       case 104:
/*      */       case 108:
/*      */       case 112:
/*      */       case 120:
/*      */       case 122:
/*      */       case 124:
/*      */       case 126:
/*      */       case 128:
/*      */       case 130:
/*      */       case 136:
/*      */       case 142:
/*      */       case 149:
/*      */       case 150:
/*  934 */         pop(2);
/*  935 */         push(16777217);
/*      */       
/*      */       case 97:
/*      */       case 101:
/*      */       case 105:
/*      */       case 109:
/*      */       case 113:
/*      */       case 127:
/*      */       case 129:
/*      */       case 131:
/*  945 */         pop(4);
/*  946 */         push(16777220);
/*  947 */         push(16777216);
/*      */       
/*      */       case 48:
/*      */       case 98:
/*      */       case 102:
/*      */       case 106:
/*      */       case 110:
/*      */       case 114:
/*      */       case 137:
/*      */       case 144:
/*  957 */         pop(2);
/*  958 */         push(16777218);
/*      */       
/*      */       case 99:
/*      */       case 103:
/*      */       case 107:
/*      */       case 111:
/*      */       case 115:
/*  965 */         pop(4);
/*  966 */         push(16777219);
/*  967 */         push(16777216);
/*      */       
/*      */       case 121:
/*      */       case 123:
/*      */       case 125:
/*  972 */         pop(3);
/*  973 */         push(16777220);
/*  974 */         push(16777216);
/*      */       
/*      */       case 132:
/*  977 */         setLocal(arg, 16777217);
/*      */       
/*      */       case 133:
/*      */       case 140:
/*  981 */         pop(1);
/*  982 */         push(16777220);
/*  983 */         push(16777216);
/*      */       
/*      */       case 134:
/*  986 */         pop(1);
/*  987 */         push(16777218);
/*      */       
/*      */       case 135:
/*      */       case 141:
/*  991 */         pop(1);
/*  992 */         push(16777219);
/*  993 */         push(16777216);
/*      */       
/*      */       case 139:
/*      */       case 190:
/*      */       case 193:
/*  998 */         pop(1);
/*  999 */         push(16777217);
/*      */       
/*      */       case 148:
/*      */       case 151:
/*      */       case 152:
/* 1004 */         pop(4);
/* 1005 */         push(16777217);
/*      */       
/*      */       case 168:
/*      */       case 169:
/* 1009 */         throw new IllegalArgumentException("JSR/RET are not supported with computeFrames option");
/*      */       case 178:
/* 1011 */         push(symbolTable, argSymbol.value);
/*      */       
/*      */       case 179:
/* 1014 */         pop(argSymbol.value);
/*      */       
/*      */       case 180:
/* 1017 */         pop(1);
/* 1018 */         push(symbolTable, argSymbol.value);
/*      */       
/*      */       case 181:
/* 1021 */         pop(argSymbol.value);
/* 1022 */         pop();
/*      */       
/*      */       case 182:
/*      */       case 183:
/*      */       case 184:
/*      */       case 185:
/* 1028 */         pop(argSymbol.value);
/* 1029 */         if (opcode != 184) {
/* 1030 */           abstractType1 = pop();
/* 1031 */           if (opcode == 183 && argSymbol.name.charAt(0) == '<') {
/* 1032 */             addInitializedType(abstractType1);
/*      */           }
/*      */         } 
/* 1035 */         push(symbolTable, argSymbol.value);
/*      */       
/*      */       case 186:
/* 1038 */         pop(argSymbol.value);
/* 1039 */         push(symbolTable, argSymbol.value);
/*      */       
/*      */       case 187:
/* 1042 */         push(0x3000000 | symbolTable.addUninitializedType(argSymbol.value, arg));
/*      */       
/*      */       case 188:
/* 1045 */         pop();
/* 1046 */         switch (arg) {
/*      */           case 4:
/* 1048 */             push(285212681);
/*      */           
/*      */           case 5:
/* 1051 */             push(285212683);
/*      */           
/*      */           case 8:
/* 1054 */             push(285212682);
/*      */           
/*      */           case 9:
/* 1057 */             push(285212684);
/*      */           
/*      */           case 10:
/* 1060 */             push(285212673);
/*      */           
/*      */           case 6:
/* 1063 */             push(285212674);
/*      */           
/*      */           case 7:
/* 1066 */             push(285212675);
/*      */           
/*      */           case 11:
/* 1069 */             push(285212676);
/*      */         } 
/*      */         
/* 1072 */         throw new IllegalArgumentException();
/*      */ 
/*      */       
/*      */       case 189:
/* 1076 */         arrayElementType = argSymbol.value;
/* 1077 */         pop();
/* 1078 */         if (arrayElementType.charAt(0) == '[') {
/* 1079 */           push(symbolTable, '[' + arrayElementType);
/*      */         } else {
/* 1081 */           push(0x12000000 | symbolTable.addType(arrayElementType));
/*      */         } 
/*      */       
/*      */       case 192:
/* 1085 */         castType = argSymbol.value;
/* 1086 */         pop();
/* 1087 */         if (castType.charAt(0) == '[') {
/* 1088 */           push(symbolTable, castType);
/*      */         } else {
/* 1090 */           push(0x2000000 | symbolTable.addType(castType));
/*      */         } 
/*      */       
/*      */       case 197:
/* 1094 */         pop(arg);
/* 1095 */         push(symbolTable, argSymbol.value);
/*      */     } 
/*      */     
/* 1098 */     throw new IllegalArgumentException();
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
/*      */   final boolean merge(SymbolTable symbolTable, Frame dstFrame, int catchTypeIndex) {
/* 1120 */     boolean frameChanged = false;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1125 */     int numLocal = this.inputLocals.length;
/* 1126 */     int numStack = this.inputStack.length;
/* 1127 */     if (dstFrame.inputLocals == null) {
/* 1128 */       dstFrame.inputLocals = new int[numLocal];
/* 1129 */       frameChanged = true;
/*      */     }  int i;
/* 1131 */     for (i = 0; i < numLocal; i++) {
/*      */       int concreteOutputType;
/* 1133 */       if (this.outputLocals != null && i < this.outputLocals.length) {
/* 1134 */         int abstractOutputType = this.outputLocals[i];
/* 1135 */         if (abstractOutputType == 0) {
/*      */ 
/*      */           
/* 1138 */           concreteOutputType = this.inputLocals[i];
/*      */         } else {
/* 1140 */           int dim = abstractOutputType & 0xF0000000;
/* 1141 */           int kind = abstractOutputType & 0xF000000;
/* 1142 */           if (kind == 67108864) {
/*      */ 
/*      */ 
/*      */             
/* 1146 */             concreteOutputType = dim + this.inputLocals[abstractOutputType & 0xFFFFF];
/* 1147 */             if ((abstractOutputType & 0x100000) != 0 && (concreteOutputType == 16777220 || concreteOutputType == 16777219))
/*      */             {
/* 1149 */               concreteOutputType = 16777216;
/*      */             }
/* 1151 */           } else if (kind == 83886080) {
/*      */ 
/*      */ 
/*      */             
/* 1155 */             concreteOutputType = dim + this.inputStack[numStack - (abstractOutputType & 0xFFFFF)];
/* 1156 */             if ((abstractOutputType & 0x100000) != 0 && (concreteOutputType == 16777220 || concreteOutputType == 16777219))
/*      */             {
/* 1158 */               concreteOutputType = 16777216;
/*      */             }
/*      */           } else {
/* 1161 */             concreteOutputType = abstractOutputType;
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 1167 */         concreteOutputType = this.inputLocals[i];
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1172 */       if (this.initializations != null) {
/* 1173 */         concreteOutputType = getInitializedType(symbolTable, concreteOutputType);
/*      */       }
/* 1175 */       frameChanged |= merge(symbolTable, concreteOutputType, dstFrame.inputLocals, i);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1183 */     if (catchTypeIndex > 0) {
/* 1184 */       for (i = 0; i < numLocal; i++) {
/* 1185 */         frameChanged |= merge(symbolTable, this.inputLocals[i], dstFrame.inputLocals, i);
/*      */       }
/* 1187 */       if (dstFrame.inputStack == null) {
/* 1188 */         dstFrame.inputStack = new int[1];
/* 1189 */         frameChanged = true;
/*      */       } 
/* 1191 */       frameChanged |= merge(symbolTable, catchTypeIndex, dstFrame.inputStack, 0);
/* 1192 */       return frameChanged;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1198 */     int numInputStack = this.inputStack.length + this.outputStackStart;
/* 1199 */     if (dstFrame.inputStack == null) {
/* 1200 */       dstFrame.inputStack = new int[numInputStack + this.outputStackTop];
/* 1201 */       frameChanged = true;
/*      */     } 
/*      */     
/*      */     int j;
/*      */     
/* 1206 */     for (j = 0; j < numInputStack; j++) {
/* 1207 */       int concreteOutputType = this.inputStack[j];
/* 1208 */       if (this.initializations != null) {
/* 1209 */         concreteOutputType = getInitializedType(symbolTable, concreteOutputType);
/*      */       }
/* 1211 */       frameChanged |= merge(symbolTable, concreteOutputType, dstFrame.inputStack, j);
/*      */     } 
/*      */ 
/*      */     
/* 1215 */     for (j = 0; j < this.outputStackTop; j++) {
/*      */       
/* 1217 */       int concreteOutputType, abstractOutputType = this.outputStack[j];
/* 1218 */       int dim = abstractOutputType & 0xF0000000;
/* 1219 */       int kind = abstractOutputType & 0xF000000;
/* 1220 */       if (kind == 67108864) {
/* 1221 */         concreteOutputType = dim + this.inputLocals[abstractOutputType & 0xFFFFF];
/* 1222 */         if ((abstractOutputType & 0x100000) != 0 && (concreteOutputType == 16777220 || concreteOutputType == 16777219))
/*      */         {
/* 1224 */           concreteOutputType = 16777216;
/*      */         }
/* 1226 */       } else if (kind == 83886080) {
/* 1227 */         concreteOutputType = dim + this.inputStack[numStack - (abstractOutputType & 0xFFFFF)];
/* 1228 */         if ((abstractOutputType & 0x100000) != 0 && (concreteOutputType == 16777220 || concreteOutputType == 16777219))
/*      */         {
/* 1230 */           concreteOutputType = 16777216;
/*      */         }
/*      */       } else {
/* 1233 */         concreteOutputType = abstractOutputType;
/*      */       } 
/* 1235 */       if (this.initializations != null) {
/* 1236 */         concreteOutputType = getInitializedType(symbolTable, concreteOutputType);
/*      */       }
/* 1238 */       frameChanged |= 
/* 1239 */         merge(symbolTable, concreteOutputType, dstFrame.inputStack, numInputStack + j);
/*      */     } 
/* 1241 */     return frameChanged;
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
/*      */   private static boolean merge(SymbolTable symbolTable, int sourceType, int[] dstTypes, int dstIndex) {
/* 1263 */     int mergedType, dstType = dstTypes[dstIndex];
/* 1264 */     if (dstType == sourceType)
/*      */     {
/* 1266 */       return false;
/*      */     }
/* 1268 */     int srcType = sourceType;
/* 1269 */     if ((sourceType & 0xFFFFFFF) == 16777221) {
/* 1270 */       if (dstType == 16777221) {
/* 1271 */         return false;
/*      */       }
/* 1273 */       srcType = 16777221;
/*      */     } 
/* 1275 */     if (dstType == 0) {
/*      */       
/* 1277 */       dstTypes[dstIndex] = srcType;
/* 1278 */       return true;
/*      */     } 
/*      */     
/* 1281 */     if ((dstType & 0xF0000000) != 0 || (dstType & 0xF000000) == 33554432) {
/*      */       
/* 1283 */       if (srcType == 16777221)
/*      */       {
/* 1285 */         return false; } 
/* 1286 */       if ((srcType & 0xFF000000) == (dstType & 0xFF000000)) {
/*      */         
/* 1288 */         if ((dstType & 0xF000000) == 33554432) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1294 */           mergedType = srcType & 0xF0000000 | 0x2000000 | symbolTable.addMergedType(srcType & 0xFFFFF, dstType & 0xFFFFF);
/*      */         }
/*      */         else {
/*      */           
/* 1298 */           int mergedDim = -268435456 + (srcType & 0xF0000000);
/* 1299 */           mergedType = mergedDim | 0x2000000 | symbolTable.addType("java/lang/Object");
/*      */         } 
/* 1301 */       } else if ((srcType & 0xF0000000) != 0 || (srcType & 0xF000000) == 33554432) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1306 */         int srcDim = srcType & 0xF0000000;
/* 1307 */         if (srcDim != 0 && (srcType & 0xF000000) != 33554432) {
/* 1308 */           srcDim = -268435456 + srcDim;
/*      */         }
/* 1310 */         int dstDim = dstType & 0xF0000000;
/* 1311 */         if (dstDim != 0 && (dstType & 0xF000000) != 33554432) {
/* 1312 */           dstDim = -268435456 + dstDim;
/*      */         }
/*      */         
/* 1315 */         mergedType = Math.min(srcDim, dstDim) | 0x2000000 | symbolTable.addType("java/lang/Object");
/*      */       } else {
/*      */         
/* 1318 */         mergedType = 16777216;
/*      */       } 
/* 1320 */     } else if (dstType == 16777221) {
/*      */ 
/*      */       
/* 1323 */       mergedType = ((srcType & 0xF0000000) != 0 || (srcType & 0xF000000) == 33554432) ? srcType : 16777216;
/*      */     }
/*      */     else {
/*      */       
/* 1327 */       mergedType = 16777216;
/*      */     } 
/* 1329 */     if (mergedType != dstType) {
/* 1330 */       dstTypes[dstIndex] = mergedType;
/* 1331 */       return true;
/*      */     } 
/* 1333 */     return false;
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
/*      */   final void accept(MethodWriter methodWriter) {
/* 1351 */     int[] localTypes = this.inputLocals;
/* 1352 */     int numLocal = 0;
/* 1353 */     int numTrailingTop = 0;
/* 1354 */     int i = 0;
/* 1355 */     while (i < localTypes.length) {
/* 1356 */       int localType = localTypes[i];
/* 1357 */       i += (localType == 16777220 || localType == 16777219) ? 2 : 1;
/* 1358 */       if (localType == 16777216) {
/* 1359 */         numTrailingTop++; continue;
/*      */       } 
/* 1361 */       numLocal += numTrailingTop + 1;
/* 1362 */       numTrailingTop = 0;
/*      */     } 
/*      */ 
/*      */     
/* 1366 */     int[] stackTypes = this.inputStack;
/* 1367 */     int numStack = 0;
/* 1368 */     i = 0;
/* 1369 */     while (i < stackTypes.length) {
/* 1370 */       int stackType = stackTypes[i];
/* 1371 */       i += (stackType == 16777220 || stackType == 16777219) ? 2 : 1;
/* 1372 */       numStack++;
/*      */     } 
/*      */     
/* 1375 */     int frameIndex = methodWriter.visitFrameStart(this.owner.bytecodeOffset, numLocal, numStack);
/* 1376 */     i = 0;
/* 1377 */     while (numLocal-- > 0) {
/* 1378 */       int localType = localTypes[i];
/* 1379 */       i += (localType == 16777220 || localType == 16777219) ? 2 : 1;
/* 1380 */       methodWriter.visitAbstractType(frameIndex++, localType);
/*      */     } 
/* 1382 */     i = 0;
/* 1383 */     while (numStack-- > 0) {
/* 1384 */       int stackType = stackTypes[i];
/* 1385 */       i += (stackType == 16777220 || stackType == 16777219) ? 2 : 1;
/* 1386 */       methodWriter.visitAbstractType(frameIndex++, stackType);
/*      */     } 
/* 1388 */     methodWriter.visitFrameEnd();
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
/*      */   static void putAbstractType(SymbolTable symbolTable, int abstractType, ByteVector output) {
/* 1404 */     int arrayDimensions = (abstractType & 0xF0000000) >> 28;
/* 1405 */     if (arrayDimensions == 0) {
/* 1406 */       int typeValue = abstractType & 0xFFFFF;
/* 1407 */       switch (abstractType & 0xF000000) {
/*      */         case 16777216:
/* 1409 */           output.putByte(typeValue);
/*      */           return;
/*      */         case 33554432:
/* 1412 */           output
/* 1413 */             .putByte(7)
/* 1414 */             .putShort((symbolTable.addConstantClass((symbolTable.getType(typeValue)).value)).index);
/*      */           return;
/*      */         case 50331648:
/* 1417 */           output.putByte(8).putShort((int)(symbolTable.getType(typeValue)).data);
/*      */           return;
/*      */       } 
/* 1420 */       throw new AssertionError();
/*      */     } 
/*      */ 
/*      */     
/* 1424 */     StringBuilder typeDescriptor = new StringBuilder();
/* 1425 */     while (arrayDimensions-- > 0) {
/* 1426 */       typeDescriptor.append('[');
/*      */     }
/* 1428 */     if ((abstractType & 0xF000000) == 33554432) {
/* 1429 */       typeDescriptor
/* 1430 */         .append('L')
/* 1431 */         .append((symbolTable.getType(abstractType & 0xFFFFF)).value)
/* 1432 */         .append(';');
/*      */     } else {
/* 1434 */       switch (abstractType & 0xFFFFF) {
/*      */         case 9:
/* 1436 */           typeDescriptor.append('Z');
/*      */           break;
/*      */         case 10:
/* 1439 */           typeDescriptor.append('B');
/*      */           break;
/*      */         case 11:
/* 1442 */           typeDescriptor.append('C');
/*      */           break;
/*      */         case 12:
/* 1445 */           typeDescriptor.append('S');
/*      */           break;
/*      */         case 1:
/* 1448 */           typeDescriptor.append('I');
/*      */           break;
/*      */         case 2:
/* 1451 */           typeDescriptor.append('F');
/*      */           break;
/*      */         case 4:
/* 1454 */           typeDescriptor.append('J');
/*      */           break;
/*      */         case 3:
/* 1457 */           typeDescriptor.append('D');
/*      */           break;
/*      */         default:
/* 1460 */           throw new AssertionError();
/*      */       } 
/*      */     } 
/* 1463 */     output
/* 1464 */       .putByte(7)
/* 1465 */       .putShort((symbolTable.addConstantClass(typeDescriptor.toString())).index);
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/Frame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */