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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassWriter
/*     */   extends ClassVisitor
/*     */ {
/*     */   public static final int COMPUTE_MAXS = 1;
/*     */   public static final int COMPUTE_FRAMES = 2;
/*     */   private int version;
/*     */   private final SymbolTable symbolTable;
/*     */   private int accessFlags;
/*     */   private int thisClass;
/*     */   private int superClass;
/*     */   private int interfaceCount;
/*     */   private int[] interfaces;
/*     */   private FieldWriter firstField;
/*     */   private FieldWriter lastField;
/*     */   private MethodWriter firstMethod;
/*     */   private MethodWriter lastMethod;
/*     */   private int numberOfInnerClasses;
/*     */   private ByteVector innerClasses;
/*     */   private int enclosingClassIndex;
/*     */   private int enclosingMethodIndex;
/*     */   private int signatureIndex;
/*     */   private int sourceFileIndex;
/*     */   private ByteVector debugExtension;
/*     */   private AnnotationWriter lastRuntimeVisibleAnnotation;
/*     */   private AnnotationWriter lastRuntimeInvisibleAnnotation;
/*     */   private AnnotationWriter lastRuntimeVisibleTypeAnnotation;
/*     */   private AnnotationWriter lastRuntimeInvisibleTypeAnnotation;
/*     */   private ModuleWriter moduleWriter;
/*     */   private int nestHostClassIndex;
/*     */   private int numberOfNestMemberClasses;
/*     */   private ByteVector nestMemberClasses;
/*     */   private Attribute firstAttribute;
/*     */   private int compute;
/*     */   
/*     */   public ClassWriter(int flags) {
/* 209 */     this(null, flags);
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
/*     */   public ClassWriter(ClassReader classReader, int flags) {
/* 237 */     super(458752);
/* 238 */     this.symbolTable = (classReader == null) ? new SymbolTable(this) : new SymbolTable(this, classReader);
/* 239 */     if ((flags & 0x2) != 0) {
/* 240 */       this.compute = 4;
/* 241 */     } else if ((flags & 0x1) != 0) {
/* 242 */       this.compute = 1;
/*     */     } else {
/* 244 */       this.compute = 0;
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
/*     */   public final void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/* 260 */     this.version = version;
/* 261 */     this.accessFlags = access;
/* 262 */     this.thisClass = this.symbolTable.setMajorVersionAndClassName(version & 0xFFFF, name);
/* 263 */     if (signature != null) {
/* 264 */       this.signatureIndex = this.symbolTable.addConstantUtf8(signature);
/*     */     }
/* 266 */     this.superClass = (superName == null) ? 0 : (this.symbolTable.addConstantClass(superName)).index;
/* 267 */     if (interfaces != null && interfaces.length > 0) {
/* 268 */       this.interfaceCount = interfaces.length;
/* 269 */       this.interfaces = new int[this.interfaceCount];
/* 270 */       for (int i = 0; i < this.interfaceCount; i++) {
/* 271 */         this.interfaces[i] = (this.symbolTable.addConstantClass(interfaces[i])).index;
/*     */       }
/*     */     } 
/* 274 */     if (this.compute == 1 && (version & 0xFFFF) >= 51) {
/* 275 */       this.compute = 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void visitSource(String file, String debug) {
/* 281 */     if (file != null) {
/* 282 */       this.sourceFileIndex = this.symbolTable.addConstantUtf8(file);
/*     */     }
/* 284 */     if (debug != null) {
/* 285 */       this.debugExtension = (new ByteVector()).encodeUtf8(debug, 0, 2147483647);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final ModuleVisitor visitModule(String name, int access, String version) {
/* 292 */     return this
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 297 */       .moduleWriter = new ModuleWriter(this.symbolTable, (this.symbolTable.addConstantModule(name)).index, access, (version == null) ? 0 : this.symbolTable.addConstantUtf8(version));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitNestHost(String nestHost) {
/* 302 */     this.nestHostClassIndex = (this.symbolTable.addConstantClass(nestHost)).index;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void visitOuterClass(String owner, String name, String descriptor) {
/* 308 */     this.enclosingClassIndex = (this.symbolTable.addConstantClass(owner)).index;
/* 309 */     if (name != null && descriptor != null) {
/* 310 */       this.enclosingMethodIndex = this.symbolTable.addConstantNameAndType(name, descriptor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
/* 318 */     ByteVector annotation = new ByteVector();
/*     */     
/* 320 */     annotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
/* 321 */     if (visible) {
/* 322 */       return this.lastRuntimeVisibleAnnotation = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeVisibleAnnotation);
/*     */     }
/*     */     
/* 325 */     return this.lastRuntimeInvisibleAnnotation = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeInvisibleAnnotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 335 */     ByteVector typeAnnotation = new ByteVector();
/*     */     
/* 337 */     TypeReference.putTarget(typeRef, typeAnnotation);
/* 338 */     TypePath.put(typePath, typeAnnotation);
/*     */     
/* 340 */     typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
/* 341 */     if (visible) {
/* 342 */       return this.lastRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastRuntimeVisibleTypeAnnotation);
/*     */     }
/*     */     
/* 345 */     return this.lastRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastRuntimeInvisibleTypeAnnotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void visitAttribute(Attribute attribute) {
/* 353 */     attribute.nextAttribute = this.firstAttribute;
/* 354 */     this.firstAttribute = attribute;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitNestMember(String nestMember) {
/* 359 */     if (this.nestMemberClasses == null) {
/* 360 */       this.nestMemberClasses = new ByteVector();
/*     */     }
/* 362 */     this.numberOfNestMemberClasses++;
/* 363 */     this.nestMemberClasses.putShort((this.symbolTable.addConstantClass(nestMember)).index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void visitInnerClass(String name, String outerName, String innerName, int access) {
/* 369 */     if (this.innerClasses == null) {
/* 370 */       this.innerClasses = new ByteVector();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 378 */     Symbol nameSymbol = this.symbolTable.addConstantClass(name);
/* 379 */     if (nameSymbol.info == 0) {
/* 380 */       this.numberOfInnerClasses++;
/* 381 */       this.innerClasses.putShort(nameSymbol.index);
/* 382 */       this.innerClasses.putShort((outerName == null) ? 0 : (this.symbolTable.addConstantClass(outerName)).index);
/* 383 */       this.innerClasses.putShort((innerName == null) ? 0 : this.symbolTable.addConstantUtf8(innerName));
/* 384 */       this.innerClasses.putShort(access);
/* 385 */       nameSymbol.info = this.numberOfInnerClasses;
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
/*     */   public final FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
/* 398 */     FieldWriter fieldWriter = new FieldWriter(this.symbolTable, access, name, descriptor, signature, value);
/*     */     
/* 400 */     if (this.firstField == null) {
/* 401 */       this.firstField = fieldWriter;
/*     */     } else {
/* 403 */       this.lastField.fv = fieldWriter;
/*     */     } 
/* 405 */     return this.lastField = fieldWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
/* 415 */     MethodWriter methodWriter = new MethodWriter(this.symbolTable, access, name, descriptor, signature, exceptions, this.compute);
/*     */     
/* 417 */     if (this.firstMethod == null) {
/* 418 */       this.firstMethod = methodWriter;
/*     */     } else {
/* 420 */       this.lastMethod.mv = methodWriter;
/*     */     } 
/* 422 */     return this.lastMethod = methodWriter;
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
/*     */   public final void visitEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray() throws ClassTooLargeException, MethodTooLargeException {
/* 446 */     int size = 24 + 2 * this.interfaceCount;
/* 447 */     int fieldsCount = 0;
/* 448 */     FieldWriter fieldWriter = this.firstField;
/* 449 */     while (fieldWriter != null) {
/* 450 */       fieldsCount++;
/* 451 */       size += fieldWriter.computeFieldInfoSize();
/* 452 */       fieldWriter = (FieldWriter)fieldWriter.fv;
/*     */     } 
/* 454 */     int methodsCount = 0;
/* 455 */     MethodWriter methodWriter = this.firstMethod;
/* 456 */     while (methodWriter != null) {
/* 457 */       methodsCount++;
/* 458 */       size += methodWriter.computeMethodInfoSize();
/* 459 */       methodWriter = (MethodWriter)methodWriter.mv;
/*     */     } 
/*     */     
/* 462 */     int attributesCount = 0;
/* 463 */     if (this.innerClasses != null) {
/* 464 */       attributesCount++;
/* 465 */       size += 8 + this.innerClasses.length;
/* 466 */       this.symbolTable.addConstantUtf8("InnerClasses");
/*     */     } 
/* 468 */     if (this.enclosingClassIndex != 0) {
/* 469 */       attributesCount++;
/* 470 */       size += 10;
/* 471 */       this.symbolTable.addConstantUtf8("EnclosingMethod");
/*     */     } 
/* 473 */     if ((this.accessFlags & 0x1000) != 0 && (this.version & 0xFFFF) < 49) {
/* 474 */       attributesCount++;
/* 475 */       size += 6;
/* 476 */       this.symbolTable.addConstantUtf8("Synthetic");
/*     */     } 
/* 478 */     if (this.signatureIndex != 0) {
/* 479 */       attributesCount++;
/* 480 */       size += 8;
/* 481 */       this.symbolTable.addConstantUtf8("Signature");
/*     */     } 
/* 483 */     if (this.sourceFileIndex != 0) {
/* 484 */       attributesCount++;
/* 485 */       size += 8;
/* 486 */       this.symbolTable.addConstantUtf8("SourceFile");
/*     */     } 
/* 488 */     if (this.debugExtension != null) {
/* 489 */       attributesCount++;
/* 490 */       size += 6 + this.debugExtension.length;
/* 491 */       this.symbolTable.addConstantUtf8("SourceDebugExtension");
/*     */     } 
/* 493 */     if ((this.accessFlags & 0x20000) != 0) {
/* 494 */       attributesCount++;
/* 495 */       size += 6;
/* 496 */       this.symbolTable.addConstantUtf8("Deprecated");
/*     */     } 
/* 498 */     if (this.lastRuntimeVisibleAnnotation != null) {
/* 499 */       attributesCount++;
/* 500 */       size += this.lastRuntimeVisibleAnnotation
/* 501 */         .computeAnnotationsSize("RuntimeVisibleAnnotations");
/*     */     } 
/*     */     
/* 504 */     if (this.lastRuntimeInvisibleAnnotation != null) {
/* 505 */       attributesCount++;
/* 506 */       size += this.lastRuntimeInvisibleAnnotation
/* 507 */         .computeAnnotationsSize("RuntimeInvisibleAnnotations");
/*     */     } 
/*     */     
/* 510 */     if (this.lastRuntimeVisibleTypeAnnotation != null) {
/* 511 */       attributesCount++;
/* 512 */       size += this.lastRuntimeVisibleTypeAnnotation
/* 513 */         .computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
/*     */     } 
/*     */     
/* 516 */     if (this.lastRuntimeInvisibleTypeAnnotation != null) {
/* 517 */       attributesCount++;
/* 518 */       size += this.lastRuntimeInvisibleTypeAnnotation
/* 519 */         .computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
/*     */     } 
/*     */     
/* 522 */     if (this.symbolTable.computeBootstrapMethodsSize() > 0) {
/* 523 */       attributesCount++;
/* 524 */       size += this.symbolTable.computeBootstrapMethodsSize();
/*     */     } 
/* 526 */     if (this.moduleWriter != null) {
/* 527 */       attributesCount += this.moduleWriter.getAttributeCount();
/* 528 */       size += this.moduleWriter.computeAttributesSize();
/*     */     } 
/* 530 */     if (this.nestHostClassIndex != 0) {
/* 531 */       attributesCount++;
/* 532 */       size += 8;
/* 533 */       this.symbolTable.addConstantUtf8("NestHost");
/*     */     } 
/* 535 */     if (this.nestMemberClasses != null) {
/* 536 */       attributesCount++;
/* 537 */       size += 8 + this.nestMemberClasses.length;
/* 538 */       this.symbolTable.addConstantUtf8("NestMembers");
/*     */     } 
/* 540 */     if (this.firstAttribute != null) {
/* 541 */       attributesCount += this.firstAttribute.getAttributeCount();
/* 542 */       size += this.firstAttribute.computeAttributesSize(this.symbolTable);
/*     */     } 
/*     */ 
/*     */     
/* 546 */     size += this.symbolTable.getConstantPoolLength();
/* 547 */     int constantPoolCount = this.symbolTable.getConstantPoolCount();
/* 548 */     if (constantPoolCount > 65535) {
/* 549 */       throw new ClassTooLargeException(this.symbolTable.getClassName(), constantPoolCount);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 554 */     ByteVector result = new ByteVector(size);
/* 555 */     result.putInt(-889275714).putInt(this.version);
/* 556 */     this.symbolTable.putConstantPool(result);
/* 557 */     int mask = ((this.version & 0xFFFF) < 49) ? 4096 : 0;
/* 558 */     result.putShort(this.accessFlags & (mask ^ 0xFFFFFFFF)).putShort(this.thisClass).putShort(this.superClass);
/* 559 */     result.putShort(this.interfaceCount);
/* 560 */     for (int i = 0; i < this.interfaceCount; i++) {
/* 561 */       result.putShort(this.interfaces[i]);
/*     */     }
/* 563 */     result.putShort(fieldsCount);
/* 564 */     fieldWriter = this.firstField;
/* 565 */     while (fieldWriter != null) {
/* 566 */       fieldWriter.putFieldInfo(result);
/* 567 */       fieldWriter = (FieldWriter)fieldWriter.fv;
/*     */     } 
/* 569 */     result.putShort(methodsCount);
/* 570 */     boolean hasFrames = false;
/* 571 */     boolean hasAsmInstructions = false;
/* 572 */     methodWriter = this.firstMethod;
/* 573 */     while (methodWriter != null) {
/* 574 */       hasFrames |= methodWriter.hasFrames();
/* 575 */       hasAsmInstructions |= methodWriter.hasAsmInstructions();
/* 576 */       methodWriter.putMethodInfo(result);
/* 577 */       methodWriter = (MethodWriter)methodWriter.mv;
/*     */     } 
/*     */     
/* 580 */     result.putShort(attributesCount);
/* 581 */     if (this.innerClasses != null) {
/* 582 */       result
/* 583 */         .putShort(this.symbolTable.addConstantUtf8("InnerClasses"))
/* 584 */         .putInt(this.innerClasses.length + 2)
/* 585 */         .putShort(this.numberOfInnerClasses)
/* 586 */         .putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
/*     */     }
/* 588 */     if (this.enclosingClassIndex != 0) {
/* 589 */       result
/* 590 */         .putShort(this.symbolTable.addConstantUtf8("EnclosingMethod"))
/* 591 */         .putInt(4)
/* 592 */         .putShort(this.enclosingClassIndex)
/* 593 */         .putShort(this.enclosingMethodIndex);
/*     */     }
/* 595 */     if ((this.accessFlags & 0x1000) != 0 && (this.version & 0xFFFF) < 49) {
/* 596 */       result.putShort(this.symbolTable.addConstantUtf8("Synthetic")).putInt(0);
/*     */     }
/* 598 */     if (this.signatureIndex != 0) {
/* 599 */       result
/* 600 */         .putShort(this.symbolTable.addConstantUtf8("Signature"))
/* 601 */         .putInt(2)
/* 602 */         .putShort(this.signatureIndex);
/*     */     }
/* 604 */     if (this.sourceFileIndex != 0) {
/* 605 */       result
/* 606 */         .putShort(this.symbolTable.addConstantUtf8("SourceFile"))
/* 607 */         .putInt(2)
/* 608 */         .putShort(this.sourceFileIndex);
/*     */     }
/* 610 */     if (this.debugExtension != null) {
/* 611 */       int length = this.debugExtension.length;
/* 612 */       result
/* 613 */         .putShort(this.symbolTable.addConstantUtf8("SourceDebugExtension"))
/* 614 */         .putInt(length)
/* 615 */         .putByteArray(this.debugExtension.data, 0, length);
/*     */     } 
/* 617 */     if ((this.accessFlags & 0x20000) != 0) {
/* 618 */       result.putShort(this.symbolTable.addConstantUtf8("Deprecated")).putInt(0);
/*     */     }
/* 620 */     if (this.lastRuntimeVisibleAnnotation != null) {
/* 621 */       this.lastRuntimeVisibleAnnotation.putAnnotations(this.symbolTable
/* 622 */           .addConstantUtf8("RuntimeVisibleAnnotations"), result);
/*     */     }
/* 624 */     if (this.lastRuntimeInvisibleAnnotation != null) {
/* 625 */       this.lastRuntimeInvisibleAnnotation.putAnnotations(this.symbolTable
/* 626 */           .addConstantUtf8("RuntimeInvisibleAnnotations"), result);
/*     */     }
/* 628 */     if (this.lastRuntimeVisibleTypeAnnotation != null) {
/* 629 */       this.lastRuntimeVisibleTypeAnnotation.putAnnotations(this.symbolTable
/* 630 */           .addConstantUtf8("RuntimeVisibleTypeAnnotations"), result);
/*     */     }
/* 632 */     if (this.lastRuntimeInvisibleTypeAnnotation != null) {
/* 633 */       this.lastRuntimeInvisibleTypeAnnotation.putAnnotations(this.symbolTable
/* 634 */           .addConstantUtf8("RuntimeInvisibleTypeAnnotations"), result);
/*     */     }
/* 636 */     this.symbolTable.putBootstrapMethods(result);
/* 637 */     if (this.moduleWriter != null) {
/* 638 */       this.moduleWriter.putAttributes(result);
/*     */     }
/* 640 */     if (this.nestHostClassIndex != 0) {
/* 641 */       result
/* 642 */         .putShort(this.symbolTable.addConstantUtf8("NestHost"))
/* 643 */         .putInt(2)
/* 644 */         .putShort(this.nestHostClassIndex);
/*     */     }
/* 646 */     if (this.nestMemberClasses != null) {
/* 647 */       result
/* 648 */         .putShort(this.symbolTable.addConstantUtf8("NestMembers"))
/* 649 */         .putInt(this.nestMemberClasses.length + 2)
/* 650 */         .putShort(this.numberOfNestMemberClasses)
/* 651 */         .putByteArray(this.nestMemberClasses.data, 0, this.nestMemberClasses.length);
/*     */     }
/* 653 */     if (this.firstAttribute != null) {
/* 654 */       this.firstAttribute.putAttributes(this.symbolTable, result);
/*     */     }
/*     */ 
/*     */     
/* 658 */     if (hasAsmInstructions) {
/* 659 */       return replaceAsmInstructions(result.data, hasFrames);
/*     */     }
/* 661 */     return result.data;
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
/*     */   private byte[] replaceAsmInstructions(byte[] classFile, boolean hasFrames) {
/* 676 */     Attribute[] attributes = getAttributePrototypes();
/* 677 */     this.firstField = null;
/* 678 */     this.lastField = null;
/* 679 */     this.firstMethod = null;
/* 680 */     this.lastMethod = null;
/* 681 */     this.lastRuntimeVisibleAnnotation = null;
/* 682 */     this.lastRuntimeInvisibleAnnotation = null;
/* 683 */     this.lastRuntimeVisibleTypeAnnotation = null;
/* 684 */     this.lastRuntimeInvisibleTypeAnnotation = null;
/* 685 */     this.moduleWriter = null;
/* 686 */     this.nestHostClassIndex = 0;
/* 687 */     this.numberOfNestMemberClasses = 0;
/* 688 */     this.nestMemberClasses = null;
/* 689 */     this.firstAttribute = null;
/* 690 */     this.compute = hasFrames ? 3 : 0;
/* 691 */     (new ClassReader(classFile, 0, false))
/* 692 */       .accept(this, attributes, (hasFrames ? 8 : 0) | 0x100);
/*     */ 
/*     */ 
/*     */     
/* 696 */     return toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Attribute[] getAttributePrototypes() {
/* 705 */     Attribute.Set attributePrototypes = new Attribute.Set();
/* 706 */     attributePrototypes.addAttributes(this.firstAttribute);
/* 707 */     FieldWriter fieldWriter = this.firstField;
/* 708 */     while (fieldWriter != null) {
/* 709 */       fieldWriter.collectAttributePrototypes(attributePrototypes);
/* 710 */       fieldWriter = (FieldWriter)fieldWriter.fv;
/*     */     } 
/* 712 */     MethodWriter methodWriter = this.firstMethod;
/* 713 */     while (methodWriter != null) {
/* 714 */       methodWriter.collectAttributePrototypes(attributePrototypes);
/* 715 */       methodWriter = (MethodWriter)methodWriter.mv;
/*     */     } 
/* 717 */     return attributePrototypes.toArray();
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
/*     */   public int newConst(Object value) {
/* 734 */     return (this.symbolTable.addConstant(value)).index;
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
/*     */   public int newUTF8(String value) {
/* 747 */     return this.symbolTable.addConstantUtf8(value);
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
/*     */   public int newClass(String value) {
/* 759 */     return (this.symbolTable.addConstantClass(value)).index;
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
/*     */   public int newMethodType(String methodDescriptor) {
/* 771 */     return (this.symbolTable.addConstantMethodType(methodDescriptor)).index;
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
/*     */   public int newModule(String moduleName) {
/* 783 */     return (this.symbolTable.addConstantModule(moduleName)).index;
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
/*     */   public int newPackage(String packageName) {
/* 795 */     return (this.symbolTable.addConstantPackage(packageName)).index;
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
/*     */   @Deprecated
/*     */   public int newHandle(int tag, String owner, String name, String descriptor) {
/* 817 */     return newHandle(tag, owner, name, descriptor, (tag == 9));
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
/*     */   public int newHandle(int tag, String owner, String name, String descriptor, boolean isInterface) {
/* 841 */     return (this.symbolTable.addConstantMethodHandle(tag, owner, name, descriptor, isInterface)).index;
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
/*     */   public int newConstantDynamic(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
/* 860 */     return (this.symbolTable.addConstantDynamic(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments)).index;
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
/*     */   public int newInvokeDynamic(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
/* 881 */     return (this.symbolTable.addConstantInvokeDynamic(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments)).index;
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
/*     */   public int newField(String owner, String name, String descriptor) {
/* 897 */     return (this.symbolTable.addConstantFieldref(owner, name, descriptor)).index;
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
/*     */   public int newMethod(String owner, String name, String descriptor, boolean isInterface) {
/* 913 */     return (this.symbolTable.addConstantMethodref(owner, name, descriptor, isInterface)).index;
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
/*     */   public int newNameType(String name, String descriptor) {
/* 926 */     return this.symbolTable.addConstantNameAndType(name, descriptor);
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
/*     */   protected String getCommonSuperClass(String type1, String type2) {
/*     */     Class<?> class1, class2;
/* 946 */     ClassLoader classLoader = getClassLoader();
/*     */     
/*     */     try {
/* 949 */       class1 = Class.forName(type1.replace('/', '.'), false, classLoader);
/* 950 */     } catch (ClassNotFoundException e) {
/* 951 */       throw new TypeNotPresentException(type1, e);
/*     */     } 
/*     */     
/*     */     try {
/* 955 */       class2 = Class.forName(type2.replace('/', '.'), false, classLoader);
/* 956 */     } catch (ClassNotFoundException e) {
/* 957 */       throw new TypeNotPresentException(type2, e);
/*     */     } 
/* 959 */     if (class1.isAssignableFrom(class2)) {
/* 960 */       return type1;
/*     */     }
/* 962 */     if (class2.isAssignableFrom(class1)) {
/* 963 */       return type2;
/*     */     }
/* 965 */     if (class1.isInterface() || class2.isInterface()) {
/* 966 */       return "java/lang/Object";
/*     */     }
/*     */     while (true) {
/* 969 */       class1 = class1.getSuperclass();
/* 970 */       if (class1.isAssignableFrom(class2)) {
/* 971 */         return class1.getName().replace('.', '/');
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
/*     */   protected ClassLoader getClassLoader() {
/* 984 */     ClassLoader classLoader = null;
/*     */     try {
/* 986 */       classLoader = Thread.currentThread().getContextClassLoader();
/* 987 */     } catch (Throwable throwable) {}
/*     */ 
/*     */     
/* 990 */     return (classLoader != null) ? classLoader : getClass().getClassLoader();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/ClassWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */