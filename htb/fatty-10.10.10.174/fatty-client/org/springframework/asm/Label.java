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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Label
/*     */ {
/*     */   static final int FLAG_DEBUG_ONLY = 1;
/*     */   static final int FLAG_JUMP_TARGET = 2;
/*     */   static final int FLAG_RESOLVED = 4;
/*     */   static final int FLAG_REACHABLE = 8;
/*     */   static final int FLAG_SUBROUTINE_CALLER = 16;
/*     */   static final int FLAG_SUBROUTINE_START = 32;
/*     */   static final int FLAG_SUBROUTINE_END = 64;
/*     */   static final int LINE_NUMBERS_CAPACITY_INCREMENT = 4;
/*     */   static final int FORWARD_REFERENCES_CAPACITY_INCREMENT = 6;
/*     */   static final int FORWARD_REFERENCE_TYPE_MASK = -268435456;
/*     */   static final int FORWARD_REFERENCE_TYPE_SHORT = 268435456;
/*     */   static final int FORWARD_REFERENCE_TYPE_WIDE = 536870912;
/*     */   static final int FORWARD_REFERENCE_HANDLE_MASK = 268435455;
/* 130 */   static final Label EMPTY_LIST = new Label();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object info;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   short flags;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private short lineNumber;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] otherLineNumbers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int bytecodeOffset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] forwardReferences;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   short inputStackSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   short outputStackSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   short outputStackMax;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   short subroutineId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Frame frame;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Label nextBasicBlock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Edge outgoingEdges;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Label nextListElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOffset() {
/* 301 */     if ((this.flags & 0x4) == 0) {
/* 302 */       throw new IllegalStateException("Label offset position has not been resolved yet");
/*     */     }
/* 304 */     return this.bytecodeOffset;
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
/*     */   final Label getCanonicalInstance() {
/* 321 */     return (this.frame == null) ? this : this.frame.owner;
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
/*     */   final void addLineNumber(int lineNumber) {
/* 334 */     if (this.lineNumber == 0) {
/* 335 */       this.lineNumber = (short)lineNumber;
/*     */     } else {
/* 337 */       if (this.otherLineNumbers == null) {
/* 338 */         this.otherLineNumbers = new int[4];
/*     */       }
/* 340 */       int otherLineNumberIndex = this.otherLineNumbers[0] = this.otherLineNumbers[0] + 1;
/* 341 */       if (otherLineNumberIndex >= this.otherLineNumbers.length) {
/* 342 */         int[] newLineNumbers = new int[this.otherLineNumbers.length + 4];
/* 343 */         System.arraycopy(this.otherLineNumbers, 0, newLineNumbers, 0, this.otherLineNumbers.length);
/* 344 */         this.otherLineNumbers = newLineNumbers;
/*     */       } 
/* 346 */       this.otherLineNumbers[otherLineNumberIndex] = lineNumber;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void accept(MethodVisitor methodVisitor, boolean visitLineNumbers) {
/* 357 */     methodVisitor.visitLabel(this);
/* 358 */     if (visitLineNumbers && this.lineNumber != 0) {
/* 359 */       methodVisitor.visitLineNumber(this.lineNumber & 0xFFFF, this);
/* 360 */       if (this.otherLineNumbers != null) {
/* 361 */         for (int i = 1; i <= this.otherLineNumbers[0]; i++) {
/* 362 */           methodVisitor.visitLineNumber(this.otherLineNumbers[i], this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void put(ByteVector code, int sourceInsnBytecodeOffset, boolean wideReference) {
/* 385 */     if ((this.flags & 0x4) == 0) {
/* 386 */       if (wideReference) {
/* 387 */         addForwardReference(sourceInsnBytecodeOffset, 536870912, code.length);
/* 388 */         code.putInt(-1);
/*     */       } else {
/* 390 */         addForwardReference(sourceInsnBytecodeOffset, 268435456, code.length);
/* 391 */         code.putShort(-1);
/*     */       }
/*     */     
/* 394 */     } else if (wideReference) {
/* 395 */       code.putInt(this.bytecodeOffset - sourceInsnBytecodeOffset);
/*     */     } else {
/* 397 */       code.putShort(this.bytecodeOffset - sourceInsnBytecodeOffset);
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
/*     */   private void addForwardReference(int sourceInsnBytecodeOffset, int referenceType, int referenceHandle) {
/* 416 */     if (this.forwardReferences == null) {
/* 417 */       this.forwardReferences = new int[6];
/*     */     }
/* 419 */     int lastElementIndex = this.forwardReferences[0];
/* 420 */     if (lastElementIndex + 2 >= this.forwardReferences.length) {
/* 421 */       int[] newValues = new int[this.forwardReferences.length + 6];
/* 422 */       System.arraycopy(this.forwardReferences, 0, newValues, 0, this.forwardReferences.length);
/* 423 */       this.forwardReferences = newValues;
/*     */     } 
/* 425 */     this.forwardReferences[++lastElementIndex] = sourceInsnBytecodeOffset;
/* 426 */     this.forwardReferences[++lastElementIndex] = referenceType | referenceHandle;
/* 427 */     this.forwardReferences[0] = lastElementIndex;
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
/*     */   final boolean resolve(byte[] code, int bytecodeOffset) {
/* 445 */     this.flags = (short)(this.flags | 0x4);
/* 446 */     this.bytecodeOffset = bytecodeOffset;
/* 447 */     if (this.forwardReferences == null) {
/* 448 */       return false;
/*     */     }
/* 450 */     boolean hasAsmInstructions = false;
/* 451 */     for (int i = this.forwardReferences[0]; i > 0; i -= 2) {
/* 452 */       int sourceInsnBytecodeOffset = this.forwardReferences[i - 1];
/* 453 */       int reference = this.forwardReferences[i];
/* 454 */       int relativeOffset = bytecodeOffset - sourceInsnBytecodeOffset;
/* 455 */       int handle = reference & 0xFFFFFFF;
/* 456 */       if ((reference & 0xF0000000) == 268435456) {
/* 457 */         if (relativeOffset < -32768 || relativeOffset > 32767) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 462 */           int opcode = code[sourceInsnBytecodeOffset] & 0xFF;
/* 463 */           if (opcode < 198) {
/*     */             
/* 465 */             code[sourceInsnBytecodeOffset] = (byte)(opcode + 49);
/*     */           } else {
/*     */             
/* 468 */             code[sourceInsnBytecodeOffset] = (byte)(opcode + 20);
/*     */           } 
/* 470 */           hasAsmInstructions = true;
/*     */         } 
/* 472 */         code[handle++] = (byte)(relativeOffset >>> 8);
/* 473 */         code[handle] = (byte)relativeOffset;
/*     */       } else {
/* 475 */         code[handle++] = (byte)(relativeOffset >>> 24);
/* 476 */         code[handle++] = (byte)(relativeOffset >>> 16);
/* 477 */         code[handle++] = (byte)(relativeOffset >>> 8);
/* 478 */         code[handle] = (byte)relativeOffset;
/*     */       } 
/*     */     } 
/* 481 */     return hasAsmInstructions;
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
/*     */   final void markSubroutine(short subroutineId) {
/* 505 */     Label listOfBlocksToProcess = this;
/* 506 */     listOfBlocksToProcess.nextListElement = EMPTY_LIST;
/* 507 */     while (listOfBlocksToProcess != EMPTY_LIST) {
/*     */       
/* 509 */       Label basicBlock = listOfBlocksToProcess;
/* 510 */       listOfBlocksToProcess = listOfBlocksToProcess.nextListElement;
/* 511 */       basicBlock.nextListElement = null;
/*     */ 
/*     */ 
/*     */       
/* 515 */       if (basicBlock.subroutineId == 0) {
/* 516 */         basicBlock.subroutineId = subroutineId;
/* 517 */         listOfBlocksToProcess = basicBlock.pushSuccessors(listOfBlocksToProcess);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void addSubroutineRetSuccessors(Label subroutineCaller) {
/* 541 */     Label listOfProcessedBlocks = EMPTY_LIST;
/* 542 */     Label listOfBlocksToProcess = this;
/* 543 */     listOfBlocksToProcess.nextListElement = EMPTY_LIST;
/* 544 */     while (listOfBlocksToProcess != EMPTY_LIST) {
/*     */       
/* 546 */       Label basicBlock = listOfBlocksToProcess;
/* 547 */       listOfBlocksToProcess = basicBlock.nextListElement;
/* 548 */       basicBlock.nextListElement = listOfProcessedBlocks;
/* 549 */       listOfProcessedBlocks = basicBlock;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 554 */       if ((basicBlock.flags & 0x40) != 0 && basicBlock.subroutineId != subroutineCaller.subroutineId)
/*     */       {
/* 556 */         basicBlock.outgoingEdges = new Edge(basicBlock.outputStackSize, subroutineCaller.outgoingEdges.successor, basicBlock.outgoingEdges);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 569 */       listOfBlocksToProcess = basicBlock.pushSuccessors(listOfBlocksToProcess);
/*     */     } 
/*     */ 
/*     */     
/* 573 */     while (listOfProcessedBlocks != EMPTY_LIST) {
/* 574 */       Label newListOfProcessedBlocks = listOfProcessedBlocks.nextListElement;
/* 575 */       listOfProcessedBlocks.nextListElement = null;
/* 576 */       listOfProcessedBlocks = newListOfProcessedBlocks;
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
/*     */   private Label pushSuccessors(Label listOfLabelsToProcess) {
/* 590 */     Label newListOfLabelsToProcess = listOfLabelsToProcess;
/* 591 */     Edge outgoingEdge = this.outgoingEdges;
/* 592 */     while (outgoingEdge != null) {
/*     */ 
/*     */       
/* 595 */       boolean isJsrTarget = ((this.flags & 0x10) != 0 && outgoingEdge == this.outgoingEdges.nextEdge);
/*     */       
/* 597 */       if (!isJsrTarget && outgoingEdge.successor.nextListElement == null) {
/*     */ 
/*     */         
/* 600 */         outgoingEdge.successor.nextListElement = newListOfLabelsToProcess;
/* 601 */         newListOfLabelsToProcess = outgoingEdge.successor;
/*     */       } 
/* 603 */       outgoingEdge = outgoingEdge.nextEdge;
/*     */     } 
/* 605 */     return newListOfLabelsToProcess;
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
/*     */   public String toString() {
/* 619 */     return "L" + System.identityHashCode(this);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/Label.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */