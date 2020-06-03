/*      */ package org.springframework.asm;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassReader
/*      */ {
/*      */   public static final int SKIP_CODE = 1;
/*      */   public static final int SKIP_DEBUG = 2;
/*      */   public static final int SKIP_FRAMES = 4;
/*      */   public static final int EXPAND_FRAMES = 8;
/*      */   static final int EXPAND_ASM_INSNS = 256;
/*      */   private static final int INPUT_STREAM_DATA_CHUNK_SIZE = 4096;
/*      */   public final byte[] b;
/*      */   private final int[] cpInfoOffsets;
/*      */   private final String[] constantUtf8Values;
/*      */   private final ConstantDynamic[] constantDynamicValues;
/*      */   private final int[] bootstrapMethodOffsets;
/*      */   private final int maxStringLength;
/*      */   public final int header;
/*      */   
/*      */   public ClassReader(byte[] classFile) {
/*  152 */     this(classFile, 0, classFile.length);
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
/*      */   public ClassReader(byte[] classFileBuffer, int classFileOffset, int classFileLength) {
/*  166 */     this(classFileBuffer, classFileOffset, true);
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
/*      */   ClassReader(byte[] classFileBuffer, int classFileOffset, boolean checkClassVersion) {
/*  179 */     this.b = classFileBuffer;
/*      */ 
/*      */     
/*  182 */     if (checkClassVersion && readShort(classFileOffset + 6) > 56) {
/*  183 */       throw new IllegalArgumentException("Unsupported class file major version " + 
/*  184 */           readShort(classFileOffset + 6));
/*      */     }
/*      */ 
/*      */     
/*  188 */     int constantPoolCount = readUnsignedShort(classFileOffset + 8);
/*  189 */     this.cpInfoOffsets = new int[constantPoolCount];
/*  190 */     this.constantUtf8Values = new String[constantPoolCount];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  195 */     int currentCpInfoIndex = 1;
/*  196 */     int currentCpInfoOffset = classFileOffset + 10;
/*  197 */     int currentMaxStringLength = 0;
/*  198 */     boolean hasConstantDynamic = false;
/*  199 */     boolean hasConstantInvokeDynamic = false;
/*      */     
/*  201 */     while (currentCpInfoIndex < constantPoolCount) {
/*  202 */       int cpInfoSize; this.cpInfoOffsets[currentCpInfoIndex++] = currentCpInfoOffset + 1;
/*      */       
/*  204 */       switch (classFileBuffer[currentCpInfoOffset]) {
/*      */         case 3:
/*      */         case 4:
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*  211 */           cpInfoSize = 5;
/*      */           break;
/*      */         case 17:
/*  214 */           cpInfoSize = 5;
/*  215 */           hasConstantDynamic = true;
/*      */           break;
/*      */         case 18:
/*  218 */           cpInfoSize = 5;
/*  219 */           hasConstantInvokeDynamic = true;
/*      */           break;
/*      */         case 5:
/*      */         case 6:
/*  223 */           cpInfoSize = 9;
/*  224 */           currentCpInfoIndex++;
/*      */           break;
/*      */         case 1:
/*  227 */           cpInfoSize = 3 + readUnsignedShort(currentCpInfoOffset + 1);
/*  228 */           if (cpInfoSize > currentMaxStringLength)
/*      */           {
/*      */ 
/*      */             
/*  232 */             currentMaxStringLength = cpInfoSize;
/*      */           }
/*      */           break;
/*      */         case 15:
/*  236 */           cpInfoSize = 4;
/*      */           break;
/*      */         case 7:
/*      */         case 8:
/*      */         case 16:
/*      */         case 19:
/*      */         case 20:
/*  243 */           cpInfoSize = 3;
/*      */           break;
/*      */         default:
/*  246 */           throw new IllegalArgumentException();
/*      */       } 
/*  248 */       currentCpInfoOffset += cpInfoSize;
/*      */     } 
/*  250 */     this.maxStringLength = currentMaxStringLength;
/*      */     
/*  252 */     this.header = currentCpInfoOffset;
/*      */ 
/*      */     
/*  255 */     this.constantDynamicValues = hasConstantDynamic ? new ConstantDynamic[constantPoolCount] : null;
/*      */ 
/*      */     
/*  258 */     this
/*      */       
/*  260 */       .bootstrapMethodOffsets = (hasConstantDynamic | hasConstantInvokeDynamic) ? readBootstrapMethodsAttribute(currentMaxStringLength) : null;
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
/*      */   public ClassReader(InputStream inputStream) throws IOException {
/*  273 */     this(readStream(inputStream, false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassReader(String className) throws IOException {
/*  284 */     this(
/*  285 */         readStream(
/*  286 */           ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class"), true));
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
/*      */   private static byte[] readStream(InputStream inputStream, boolean close) throws IOException {
/*  299 */     if (inputStream == null) {
/*  300 */       throw new IOException("Class not found");
/*      */     }
/*      */     try {
/*  303 */       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
/*  304 */       byte[] data = new byte[4096];
/*      */       int bytesRead;
/*  306 */       while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
/*  307 */         outputStream.write(data, 0, bytesRead);
/*      */       }
/*  309 */       outputStream.flush();
/*  310 */       return outputStream.toByteArray();
/*      */     } finally {
/*  312 */       if (close) {
/*  313 */         inputStream.close();
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
/*      */   
/*      */   public int getAccess() {
/*  330 */     return readUnsignedShort(this.header);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassName() {
/*  341 */     return readClass(this.header + 2, new char[this.maxStringLength]);
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
/*      */   public String getSuperName() {
/*  353 */     return readClass(this.header + 4, new char[this.maxStringLength]);
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
/*      */   public String[] getInterfaces() {
/*  365 */     int currentOffset = this.header + 6;
/*  366 */     int interfacesCount = readUnsignedShort(currentOffset);
/*  367 */     String[] interfaces = new String[interfacesCount];
/*  368 */     if (interfacesCount > 0) {
/*  369 */       char[] charBuffer = new char[this.maxStringLength];
/*  370 */       for (int i = 0; i < interfacesCount; i++) {
/*  371 */         currentOffset += 2;
/*  372 */         interfaces[i] = readClass(currentOffset, charBuffer);
/*      */       } 
/*      */     } 
/*  375 */     return interfaces;
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
/*      */   public void accept(ClassVisitor classVisitor, int parsingOptions) {
/*  391 */     accept(classVisitor, new Attribute[0], parsingOptions);
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
/*      */   public void accept(ClassVisitor classVisitor, Attribute[] attributePrototypes, int parsingOptions) {
/*  412 */     Context context = new Context();
/*  413 */     context.attributePrototypes = attributePrototypes;
/*  414 */     context.parsingOptions = parsingOptions;
/*  415 */     context.charBuffer = new char[this.maxStringLength];
/*      */ 
/*      */     
/*  418 */     char[] charBuffer = context.charBuffer;
/*  419 */     int currentOffset = this.header;
/*  420 */     int accessFlags = readUnsignedShort(currentOffset);
/*  421 */     String thisClass = readClass(currentOffset + 2, charBuffer);
/*  422 */     String superClass = readClass(currentOffset + 4, charBuffer);
/*  423 */     String[] interfaces = new String[readUnsignedShort(currentOffset + 6)];
/*  424 */     currentOffset += 8;
/*  425 */     for (int i = 0; i < interfaces.length; i++) {
/*  426 */       interfaces[i] = readClass(currentOffset, charBuffer);
/*  427 */       currentOffset += 2;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  433 */     int innerClassesOffset = 0;
/*      */     
/*  435 */     int enclosingMethodOffset = 0;
/*      */     
/*  437 */     String signature = null;
/*      */     
/*  439 */     String sourceFile = null;
/*      */     
/*  441 */     String sourceDebugExtension = null;
/*      */     
/*  443 */     int runtimeVisibleAnnotationsOffset = 0;
/*      */     
/*  445 */     int runtimeInvisibleAnnotationsOffset = 0;
/*      */     
/*  447 */     int runtimeVisibleTypeAnnotationsOffset = 0;
/*      */     
/*  449 */     int runtimeInvisibleTypeAnnotationsOffset = 0;
/*      */     
/*  451 */     int moduleOffset = 0;
/*      */     
/*  453 */     int modulePackagesOffset = 0;
/*      */     
/*  455 */     String moduleMainClass = null;
/*      */     
/*  457 */     String nestHostClass = null;
/*      */     
/*  459 */     int nestMembersOffset = 0;
/*      */ 
/*      */     
/*  462 */     Attribute attributes = null;
/*      */     
/*  464 */     int currentAttributeOffset = getFirstAttributeOffset();
/*  465 */     for (int j = readUnsignedShort(currentAttributeOffset - 2); j > 0; j--) {
/*      */       
/*  467 */       String attributeName = readUTF8(currentAttributeOffset, charBuffer);
/*  468 */       int attributeLength = readInt(currentAttributeOffset + 2);
/*  469 */       currentAttributeOffset += 6;
/*      */ 
/*      */       
/*  472 */       if ("SourceFile".equals(attributeName)) {
/*  473 */         sourceFile = readUTF8(currentAttributeOffset, charBuffer);
/*  474 */       } else if ("InnerClasses".equals(attributeName)) {
/*  475 */         innerClassesOffset = currentAttributeOffset;
/*  476 */       } else if ("EnclosingMethod".equals(attributeName)) {
/*  477 */         enclosingMethodOffset = currentAttributeOffset;
/*  478 */       } else if ("NestHost".equals(attributeName)) {
/*  479 */         nestHostClass = readClass(currentAttributeOffset, charBuffer);
/*  480 */       } else if ("NestMembers".equals(attributeName)) {
/*  481 */         nestMembersOffset = currentAttributeOffset;
/*  482 */       } else if ("Signature".equals(attributeName)) {
/*  483 */         signature = readUTF8(currentAttributeOffset, charBuffer);
/*  484 */       } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
/*  485 */         runtimeVisibleAnnotationsOffset = currentAttributeOffset;
/*  486 */       } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
/*  487 */         runtimeVisibleTypeAnnotationsOffset = currentAttributeOffset;
/*  488 */       } else if ("Deprecated".equals(attributeName)) {
/*  489 */         accessFlags |= 0x20000;
/*  490 */       } else if ("Synthetic".equals(attributeName)) {
/*  491 */         accessFlags |= 0x1000;
/*  492 */       } else if ("SourceDebugExtension".equals(attributeName)) {
/*      */         
/*  494 */         sourceDebugExtension = readUtf(currentAttributeOffset, attributeLength, new char[attributeLength]);
/*  495 */       } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
/*  496 */         runtimeInvisibleAnnotationsOffset = currentAttributeOffset;
/*  497 */       } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
/*  498 */         runtimeInvisibleTypeAnnotationsOffset = currentAttributeOffset;
/*  499 */       } else if ("Module".equals(attributeName)) {
/*  500 */         moduleOffset = currentAttributeOffset;
/*  501 */       } else if ("ModuleMainClass".equals(attributeName)) {
/*  502 */         moduleMainClass = readClass(currentAttributeOffset, charBuffer);
/*  503 */       } else if ("ModulePackages".equals(attributeName)) {
/*  504 */         modulePackagesOffset = currentAttributeOffset;
/*  505 */       } else if (!"BootstrapMethods".equals(attributeName)) {
/*      */ 
/*      */         
/*  508 */         Attribute attribute = readAttribute(attributePrototypes, attributeName, currentAttributeOffset, attributeLength, charBuffer, -1, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  516 */         attribute.nextAttribute = attributes;
/*  517 */         attributes = attribute;
/*      */       } 
/*  519 */       currentAttributeOffset += attributeLength;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  524 */     classVisitor.visit(
/*  525 */         readInt(this.cpInfoOffsets[1] - 7), accessFlags, thisClass, signature, superClass, interfaces);
/*      */ 
/*      */     
/*  528 */     if ((parsingOptions & 0x2) == 0 && (sourceFile != null || sourceDebugExtension != null))
/*      */     {
/*  530 */       classVisitor.visitSource(sourceFile, sourceDebugExtension);
/*      */     }
/*      */ 
/*      */     
/*  534 */     if (moduleOffset != 0) {
/*  535 */       readModuleAttributes(classVisitor, context, moduleOffset, modulePackagesOffset, moduleMainClass);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  540 */     if (nestHostClass != null) {
/*  541 */       classVisitor.visitNestHost(nestHostClass);
/*      */     }
/*      */ 
/*      */     
/*  545 */     if (enclosingMethodOffset != 0) {
/*  546 */       String className = readClass(enclosingMethodOffset, charBuffer);
/*  547 */       int methodIndex = readUnsignedShort(enclosingMethodOffset + 2);
/*  548 */       String name = (methodIndex == 0) ? null : readUTF8(this.cpInfoOffsets[methodIndex], charBuffer);
/*  549 */       String type = (methodIndex == 0) ? null : readUTF8(this.cpInfoOffsets[methodIndex] + 2, charBuffer);
/*  550 */       classVisitor.visitOuterClass(className, name, type);
/*      */     } 
/*      */ 
/*      */     
/*  554 */     if (runtimeVisibleAnnotationsOffset != 0) {
/*  555 */       int numAnnotations = readUnsignedShort(runtimeVisibleAnnotationsOffset);
/*  556 */       int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
/*  557 */       while (numAnnotations-- > 0) {
/*      */         
/*  559 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  560 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  563 */         currentAnnotationOffset = readElementValues(classVisitor
/*  564 */             .visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  572 */     if (runtimeInvisibleAnnotationsOffset != 0) {
/*  573 */       int numAnnotations = readUnsignedShort(runtimeInvisibleAnnotationsOffset);
/*  574 */       int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
/*  575 */       while (numAnnotations-- > 0) {
/*      */         
/*  577 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  578 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  581 */         currentAnnotationOffset = readElementValues(classVisitor
/*  582 */             .visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  590 */     if (runtimeVisibleTypeAnnotationsOffset != 0) {
/*  591 */       int numAnnotations = readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
/*  592 */       int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
/*  593 */       while (numAnnotations-- > 0) {
/*      */         
/*  595 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/*  597 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  598 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  601 */         currentAnnotationOffset = readElementValues(classVisitor
/*  602 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  614 */     if (runtimeInvisibleTypeAnnotationsOffset != 0) {
/*  615 */       int numAnnotations = readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
/*  616 */       int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
/*  617 */       while (numAnnotations-- > 0) {
/*      */         
/*  619 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/*  621 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  622 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  625 */         currentAnnotationOffset = readElementValues(classVisitor
/*  626 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  638 */     while (attributes != null) {
/*      */       
/*  640 */       Attribute nextAttribute = attributes.nextAttribute;
/*  641 */       attributes.nextAttribute = null;
/*  642 */       classVisitor.visitAttribute(attributes);
/*  643 */       attributes = nextAttribute;
/*      */     } 
/*      */ 
/*      */     
/*  647 */     if (nestMembersOffset != 0) {
/*  648 */       int numberOfNestMembers = readUnsignedShort(nestMembersOffset);
/*  649 */       int currentNestMemberOffset = nestMembersOffset + 2;
/*  650 */       while (numberOfNestMembers-- > 0) {
/*  651 */         classVisitor.visitNestMember(readClass(currentNestMemberOffset, charBuffer));
/*  652 */         currentNestMemberOffset += 2;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  657 */     if (innerClassesOffset != 0) {
/*  658 */       int numberOfClasses = readUnsignedShort(innerClassesOffset);
/*  659 */       int currentClassesOffset = innerClassesOffset + 2;
/*  660 */       while (numberOfClasses-- > 0) {
/*  661 */         classVisitor.visitInnerClass(
/*  662 */             readClass(currentClassesOffset, charBuffer), 
/*  663 */             readClass(currentClassesOffset + 2, charBuffer), 
/*  664 */             readUTF8(currentClassesOffset + 4, charBuffer), 
/*  665 */             readUnsignedShort(currentClassesOffset + 6));
/*  666 */         currentClassesOffset += 8;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  671 */     int fieldsCount = readUnsignedShort(currentOffset);
/*  672 */     currentOffset += 2;
/*  673 */     while (fieldsCount-- > 0) {
/*  674 */       currentOffset = readField(classVisitor, context, currentOffset);
/*      */     }
/*  676 */     int methodsCount = readUnsignedShort(currentOffset);
/*  677 */     currentOffset += 2;
/*  678 */     while (methodsCount-- > 0) {
/*  679 */       currentOffset = readMethod(classVisitor, context, currentOffset);
/*      */     }
/*      */ 
/*      */     
/*  683 */     classVisitor.visitEnd();
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
/*      */   private void readModuleAttributes(ClassVisitor classVisitor, Context context, int moduleOffset, int modulePackagesOffset, String moduleMainClass) {
/*  707 */     char[] buffer = context.charBuffer;
/*      */ 
/*      */     
/*  710 */     int currentOffset = moduleOffset;
/*  711 */     String moduleName = readModule(currentOffset, buffer);
/*  712 */     int moduleFlags = readUnsignedShort(currentOffset + 2);
/*  713 */     String moduleVersion = readUTF8(currentOffset + 4, buffer);
/*  714 */     currentOffset += 6;
/*  715 */     ModuleVisitor moduleVisitor = classVisitor.visitModule(moduleName, moduleFlags, moduleVersion);
/*  716 */     if (moduleVisitor == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  721 */     if (moduleMainClass != null) {
/*  722 */       moduleVisitor.visitMainClass(moduleMainClass);
/*      */     }
/*      */ 
/*      */     
/*  726 */     if (modulePackagesOffset != 0) {
/*  727 */       int packageCount = readUnsignedShort(modulePackagesOffset);
/*  728 */       int currentPackageOffset = modulePackagesOffset + 2;
/*  729 */       while (packageCount-- > 0) {
/*  730 */         moduleVisitor.visitPackage(readPackage(currentPackageOffset, buffer));
/*  731 */         currentPackageOffset += 2;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  736 */     int requiresCount = readUnsignedShort(currentOffset);
/*  737 */     currentOffset += 2;
/*  738 */     while (requiresCount-- > 0) {
/*      */       
/*  740 */       String requires = readModule(currentOffset, buffer);
/*  741 */       int requiresFlags = readUnsignedShort(currentOffset + 2);
/*  742 */       String requiresVersion = readUTF8(currentOffset + 4, buffer);
/*  743 */       currentOffset += 6;
/*  744 */       moduleVisitor.visitRequire(requires, requiresFlags, requiresVersion);
/*      */     } 
/*      */ 
/*      */     
/*  748 */     int exportsCount = readUnsignedShort(currentOffset);
/*  749 */     currentOffset += 2;
/*  750 */     while (exportsCount-- > 0) {
/*      */ 
/*      */       
/*  753 */       String exports = readPackage(currentOffset, buffer);
/*  754 */       int exportsFlags = readUnsignedShort(currentOffset + 2);
/*  755 */       int exportsToCount = readUnsignedShort(currentOffset + 4);
/*  756 */       currentOffset += 6;
/*  757 */       String[] exportsTo = null;
/*  758 */       if (exportsToCount != 0) {
/*  759 */         exportsTo = new String[exportsToCount];
/*  760 */         for (int i = 0; i < exportsToCount; i++) {
/*  761 */           exportsTo[i] = readModule(currentOffset, buffer);
/*  762 */           currentOffset += 2;
/*      */         } 
/*      */       } 
/*  765 */       moduleVisitor.visitExport(exports, exportsFlags, exportsTo);
/*      */     } 
/*      */ 
/*      */     
/*  769 */     int opensCount = readUnsignedShort(currentOffset);
/*  770 */     currentOffset += 2;
/*  771 */     while (opensCount-- > 0) {
/*      */       
/*  773 */       String opens = readPackage(currentOffset, buffer);
/*  774 */       int opensFlags = readUnsignedShort(currentOffset + 2);
/*  775 */       int opensToCount = readUnsignedShort(currentOffset + 4);
/*  776 */       currentOffset += 6;
/*  777 */       String[] opensTo = null;
/*  778 */       if (opensToCount != 0) {
/*  779 */         opensTo = new String[opensToCount];
/*  780 */         for (int i = 0; i < opensToCount; i++) {
/*  781 */           opensTo[i] = readModule(currentOffset, buffer);
/*  782 */           currentOffset += 2;
/*      */         } 
/*      */       } 
/*  785 */       moduleVisitor.visitOpen(opens, opensFlags, opensTo);
/*      */     } 
/*      */ 
/*      */     
/*  789 */     int usesCount = readUnsignedShort(currentOffset);
/*  790 */     currentOffset += 2;
/*  791 */     while (usesCount-- > 0) {
/*  792 */       moduleVisitor.visitUse(readClass(currentOffset, buffer));
/*  793 */       currentOffset += 2;
/*      */     } 
/*      */ 
/*      */     
/*  797 */     int providesCount = readUnsignedShort(currentOffset);
/*  798 */     currentOffset += 2;
/*  799 */     while (providesCount-- > 0) {
/*      */       
/*  801 */       String provides = readClass(currentOffset, buffer);
/*  802 */       int providesWithCount = readUnsignedShort(currentOffset + 2);
/*  803 */       currentOffset += 4;
/*  804 */       String[] providesWith = new String[providesWithCount];
/*  805 */       for (int i = 0; i < providesWithCount; i++) {
/*  806 */         providesWith[i] = readClass(currentOffset, buffer);
/*  807 */         currentOffset += 2;
/*      */       } 
/*  809 */       moduleVisitor.visitProvide(provides, providesWith);
/*      */     } 
/*      */ 
/*      */     
/*  813 */     moduleVisitor.visitEnd();
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
/*      */   private int readField(ClassVisitor classVisitor, Context context, int fieldInfoOffset) {
/*  826 */     char[] charBuffer = context.charBuffer;
/*      */ 
/*      */     
/*  829 */     int currentOffset = fieldInfoOffset;
/*  830 */     int accessFlags = readUnsignedShort(currentOffset);
/*  831 */     String name = readUTF8(currentOffset + 2, charBuffer);
/*  832 */     String descriptor = readUTF8(currentOffset + 4, charBuffer);
/*  833 */     currentOffset += 6;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  838 */     Object constantValue = null;
/*      */     
/*  840 */     String signature = null;
/*      */     
/*  842 */     int runtimeVisibleAnnotationsOffset = 0;
/*      */     
/*  844 */     int runtimeInvisibleAnnotationsOffset = 0;
/*      */     
/*  846 */     int runtimeVisibleTypeAnnotationsOffset = 0;
/*      */     
/*  848 */     int runtimeInvisibleTypeAnnotationsOffset = 0;
/*      */ 
/*      */     
/*  851 */     Attribute attributes = null;
/*      */     
/*  853 */     int attributesCount = readUnsignedShort(currentOffset);
/*  854 */     currentOffset += 2;
/*  855 */     while (attributesCount-- > 0) {
/*      */       
/*  857 */       String attributeName = readUTF8(currentOffset, charBuffer);
/*  858 */       int attributeLength = readInt(currentOffset + 2);
/*  859 */       currentOffset += 6;
/*      */ 
/*      */       
/*  862 */       if ("ConstantValue".equals(attributeName)) {
/*  863 */         int constantvalueIndex = readUnsignedShort(currentOffset);
/*  864 */         constantValue = (constantvalueIndex == 0) ? null : readConst(constantvalueIndex, charBuffer);
/*  865 */       } else if ("Signature".equals(attributeName)) {
/*  866 */         signature = readUTF8(currentOffset, charBuffer);
/*  867 */       } else if ("Deprecated".equals(attributeName)) {
/*  868 */         accessFlags |= 0x20000;
/*  869 */       } else if ("Synthetic".equals(attributeName)) {
/*  870 */         accessFlags |= 0x1000;
/*  871 */       } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
/*  872 */         runtimeVisibleAnnotationsOffset = currentOffset;
/*  873 */       } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
/*  874 */         runtimeVisibleTypeAnnotationsOffset = currentOffset;
/*  875 */       } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
/*  876 */         runtimeInvisibleAnnotationsOffset = currentOffset;
/*  877 */       } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
/*  878 */         runtimeInvisibleTypeAnnotationsOffset = currentOffset;
/*      */       } else {
/*      */         
/*  881 */         Attribute attribute = readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, -1, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  889 */         attribute.nextAttribute = attributes;
/*  890 */         attributes = attribute;
/*      */       } 
/*  892 */       currentOffset += attributeLength;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  897 */     FieldVisitor fieldVisitor = classVisitor.visitField(accessFlags, name, descriptor, signature, constantValue);
/*  898 */     if (fieldVisitor == null) {
/*  899 */       return currentOffset;
/*      */     }
/*      */ 
/*      */     
/*  903 */     if (runtimeVisibleAnnotationsOffset != 0) {
/*  904 */       int numAnnotations = readUnsignedShort(runtimeVisibleAnnotationsOffset);
/*  905 */       int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
/*  906 */       while (numAnnotations-- > 0) {
/*      */         
/*  908 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  909 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  912 */         currentAnnotationOffset = readElementValues(fieldVisitor
/*  913 */             .visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  921 */     if (runtimeInvisibleAnnotationsOffset != 0) {
/*  922 */       int numAnnotations = readUnsignedShort(runtimeInvisibleAnnotationsOffset);
/*  923 */       int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
/*  924 */       while (numAnnotations-- > 0) {
/*      */         
/*  926 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  927 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  930 */         currentAnnotationOffset = readElementValues(fieldVisitor
/*  931 */             .visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  939 */     if (runtimeVisibleTypeAnnotationsOffset != 0) {
/*  940 */       int numAnnotations = readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
/*  941 */       int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
/*  942 */       while (numAnnotations-- > 0) {
/*      */         
/*  944 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/*  946 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  947 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  950 */         currentAnnotationOffset = readElementValues(fieldVisitor
/*  951 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  963 */     if (runtimeInvisibleTypeAnnotationsOffset != 0) {
/*  964 */       int numAnnotations = readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
/*  965 */       int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
/*  966 */       while (numAnnotations-- > 0) {
/*      */         
/*  968 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/*  970 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/*  971 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/*  974 */         currentAnnotationOffset = readElementValues(fieldVisitor
/*  975 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  987 */     while (attributes != null) {
/*      */       
/*  989 */       Attribute nextAttribute = attributes.nextAttribute;
/*  990 */       attributes.nextAttribute = null;
/*  991 */       fieldVisitor.visitAttribute(attributes);
/*  992 */       attributes = nextAttribute;
/*      */     } 
/*      */ 
/*      */     
/*  996 */     fieldVisitor.visitEnd();
/*  997 */     return currentOffset;
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
/*      */   private int readMethod(ClassVisitor classVisitor, Context context, int methodInfoOffset) {
/* 1010 */     char[] charBuffer = context.charBuffer;
/*      */ 
/*      */     
/* 1013 */     int currentOffset = methodInfoOffset;
/* 1014 */     context.currentMethodAccessFlags = readUnsignedShort(currentOffset);
/* 1015 */     context.currentMethodName = readUTF8(currentOffset + 2, charBuffer);
/* 1016 */     context.currentMethodDescriptor = readUTF8(currentOffset + 4, charBuffer);
/* 1017 */     currentOffset += 6;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1022 */     int codeOffset = 0;
/*      */     
/* 1024 */     int exceptionsOffset = 0;
/*      */     
/* 1026 */     String[] exceptions = null;
/*      */     
/* 1028 */     boolean synthetic = false;
/*      */     
/* 1030 */     int signatureIndex = 0;
/*      */     
/* 1032 */     int runtimeVisibleAnnotationsOffset = 0;
/*      */     
/* 1034 */     int runtimeInvisibleAnnotationsOffset = 0;
/*      */     
/* 1036 */     int runtimeVisibleParameterAnnotationsOffset = 0;
/*      */     
/* 1038 */     int runtimeInvisibleParameterAnnotationsOffset = 0;
/*      */     
/* 1040 */     int runtimeVisibleTypeAnnotationsOffset = 0;
/*      */     
/* 1042 */     int runtimeInvisibleTypeAnnotationsOffset = 0;
/*      */     
/* 1044 */     int annotationDefaultOffset = 0;
/*      */     
/* 1046 */     int methodParametersOffset = 0;
/*      */ 
/*      */     
/* 1049 */     Attribute attributes = null;
/*      */     
/* 1051 */     int attributesCount = readUnsignedShort(currentOffset);
/* 1052 */     currentOffset += 2;
/* 1053 */     while (attributesCount-- > 0) {
/*      */       
/* 1055 */       String attributeName = readUTF8(currentOffset, charBuffer);
/* 1056 */       int attributeLength = readInt(currentOffset + 2);
/* 1057 */       currentOffset += 6;
/*      */ 
/*      */       
/* 1060 */       if ("Code".equals(attributeName)) {
/* 1061 */         if ((context.parsingOptions & 0x1) == 0) {
/* 1062 */           codeOffset = currentOffset;
/*      */         }
/* 1064 */       } else if ("Exceptions".equals(attributeName)) {
/* 1065 */         exceptionsOffset = currentOffset;
/* 1066 */         exceptions = new String[readUnsignedShort(exceptionsOffset)];
/* 1067 */         int currentExceptionOffset = exceptionsOffset + 2;
/* 1068 */         for (int i = 0; i < exceptions.length; i++) {
/* 1069 */           exceptions[i] = readClass(currentExceptionOffset, charBuffer);
/* 1070 */           currentExceptionOffset += 2;
/*      */         } 
/* 1072 */       } else if ("Signature".equals(attributeName)) {
/* 1073 */         signatureIndex = readUnsignedShort(currentOffset);
/* 1074 */       } else if ("Deprecated".equals(attributeName)) {
/* 1075 */         context.currentMethodAccessFlags |= 0x20000;
/* 1076 */       } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
/* 1077 */         runtimeVisibleAnnotationsOffset = currentOffset;
/* 1078 */       } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
/* 1079 */         runtimeVisibleTypeAnnotationsOffset = currentOffset;
/* 1080 */       } else if ("AnnotationDefault".equals(attributeName)) {
/* 1081 */         annotationDefaultOffset = currentOffset;
/* 1082 */       } else if ("Synthetic".equals(attributeName)) {
/* 1083 */         synthetic = true;
/* 1084 */         context.currentMethodAccessFlags |= 0x1000;
/* 1085 */       } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
/* 1086 */         runtimeInvisibleAnnotationsOffset = currentOffset;
/* 1087 */       } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
/* 1088 */         runtimeInvisibleTypeAnnotationsOffset = currentOffset;
/* 1089 */       } else if ("RuntimeVisibleParameterAnnotations".equals(attributeName)) {
/* 1090 */         runtimeVisibleParameterAnnotationsOffset = currentOffset;
/* 1091 */       } else if ("RuntimeInvisibleParameterAnnotations".equals(attributeName)) {
/* 1092 */         runtimeInvisibleParameterAnnotationsOffset = currentOffset;
/* 1093 */       } else if ("MethodParameters".equals(attributeName)) {
/* 1094 */         methodParametersOffset = currentOffset;
/*      */       } else {
/*      */         
/* 1097 */         Attribute attribute = readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, -1, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1105 */         attribute.nextAttribute = attributes;
/* 1106 */         attributes = attribute;
/*      */       } 
/* 1108 */       currentOffset += attributeLength;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1113 */     MethodVisitor methodVisitor = classVisitor.visitMethod(context.currentMethodAccessFlags, context.currentMethodName, context.currentMethodDescriptor, (signatureIndex == 0) ? null : 
/*      */ 
/*      */ 
/*      */         
/* 1117 */         readUtf(signatureIndex, charBuffer), exceptions);
/*      */     
/* 1119 */     if (methodVisitor == null) {
/* 1120 */       return currentOffset;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1127 */     if (methodVisitor instanceof MethodWriter) {
/* 1128 */       MethodWriter methodWriter = (MethodWriter)methodVisitor;
/* 1129 */       if (methodWriter.canCopyMethodAttributes(this, methodInfoOffset, currentOffset - methodInfoOffset, synthetic, ((context.currentMethodAccessFlags & 0x20000) != 0), 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1135 */           readUnsignedShort(methodInfoOffset + 4), signatureIndex, exceptionsOffset))
/*      */       {
/*      */         
/* 1138 */         return currentOffset;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1143 */     if (methodParametersOffset != 0) {
/* 1144 */       int parametersCount = readByte(methodParametersOffset);
/* 1145 */       int currentParameterOffset = methodParametersOffset + 1;
/* 1146 */       while (parametersCount-- > 0) {
/*      */         
/* 1148 */         methodVisitor.visitParameter(
/* 1149 */             readUTF8(currentParameterOffset, charBuffer), 
/* 1150 */             readUnsignedShort(currentParameterOffset + 2));
/* 1151 */         currentParameterOffset += 4;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1156 */     if (annotationDefaultOffset != 0) {
/* 1157 */       AnnotationVisitor annotationVisitor = methodVisitor.visitAnnotationDefault();
/* 1158 */       readElementValue(annotationVisitor, annotationDefaultOffset, null, charBuffer);
/* 1159 */       if (annotationVisitor != null) {
/* 1160 */         annotationVisitor.visitEnd();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1165 */     if (runtimeVisibleAnnotationsOffset != 0) {
/* 1166 */       int numAnnotations = readUnsignedShort(runtimeVisibleAnnotationsOffset);
/* 1167 */       int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
/* 1168 */       while (numAnnotations-- > 0) {
/*      */         
/* 1170 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1171 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1174 */         currentAnnotationOffset = readElementValues(methodVisitor
/* 1175 */             .visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1183 */     if (runtimeInvisibleAnnotationsOffset != 0) {
/* 1184 */       int numAnnotations = readUnsignedShort(runtimeInvisibleAnnotationsOffset);
/* 1185 */       int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
/* 1186 */       while (numAnnotations-- > 0) {
/*      */         
/* 1188 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1189 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1192 */         currentAnnotationOffset = readElementValues(methodVisitor
/* 1193 */             .visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1201 */     if (runtimeVisibleTypeAnnotationsOffset != 0) {
/* 1202 */       int numAnnotations = readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
/* 1203 */       int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
/* 1204 */       while (numAnnotations-- > 0) {
/*      */         
/* 1206 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/* 1208 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1209 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1212 */         currentAnnotationOffset = readElementValues(methodVisitor
/* 1213 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1225 */     if (runtimeInvisibleTypeAnnotationsOffset != 0) {
/* 1226 */       int numAnnotations = readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
/* 1227 */       int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
/* 1228 */       while (numAnnotations-- > 0) {
/*      */         
/* 1230 */         currentAnnotationOffset = readTypeAnnotationTarget(context, currentAnnotationOffset);
/*      */         
/* 1232 */         String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 1233 */         currentAnnotationOffset += 2;
/*      */ 
/*      */         
/* 1236 */         currentAnnotationOffset = readElementValues(methodVisitor
/* 1237 */             .visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1249 */     if (runtimeVisibleParameterAnnotationsOffset != 0) {
/* 1250 */       readParameterAnnotations(methodVisitor, context, runtimeVisibleParameterAnnotationsOffset, true);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1255 */     if (runtimeInvisibleParameterAnnotationsOffset != 0) {
/* 1256 */       readParameterAnnotations(methodVisitor, context, runtimeInvisibleParameterAnnotationsOffset, false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1264 */     while (attributes != null) {
/*      */       
/* 1266 */       Attribute nextAttribute = attributes.nextAttribute;
/* 1267 */       attributes.nextAttribute = null;
/* 1268 */       methodVisitor.visitAttribute(attributes);
/* 1269 */       attributes = nextAttribute;
/*      */     } 
/*      */ 
/*      */     
/* 1273 */     if (codeOffset != 0) {
/* 1274 */       methodVisitor.visitCode();
/* 1275 */       readCode(methodVisitor, context, codeOffset);
/*      */     } 
/*      */ 
/*      */     
/* 1279 */     methodVisitor.visitEnd();
/* 1280 */     return currentOffset;
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
/*      */   private void readCode(MethodVisitor methodVisitor, Context context, int codeOffset) {
/* 1297 */     int currentOffset = codeOffset;
/*      */ 
/*      */     
/* 1300 */     byte[] classFileBuffer = this.b;
/* 1301 */     char[] charBuffer = context.charBuffer;
/* 1302 */     int maxStack = readUnsignedShort(currentOffset);
/* 1303 */     int maxLocals = readUnsignedShort(currentOffset + 2);
/* 1304 */     int codeLength = readInt(currentOffset + 4);
/* 1305 */     currentOffset += 8;
/*      */ 
/*      */     
/* 1308 */     int bytecodeStartOffset = currentOffset;
/* 1309 */     int bytecodeEndOffset = currentOffset + codeLength;
/* 1310 */     Label[] labels = context.currentMethodLabels = new Label[codeLength + 1];
/* 1311 */     while (currentOffset < bytecodeEndOffset) {
/* 1312 */       int numTableEntries, numSwitchCases, bytecodeOffset = currentOffset - bytecodeStartOffset;
/* 1313 */       int opcode = classFileBuffer[currentOffset] & 0xFF;
/* 1314 */       switch (opcode) {
/*      */         case 0:
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/*      */         case 4:
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 8:
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 26:
/*      */         case 27:
/*      */         case 28:
/*      */         case 29:
/*      */         case 30:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 37:
/*      */         case 38:
/*      */         case 39:
/*      */         case 40:
/*      */         case 41:
/*      */         case 42:
/*      */         case 43:
/*      */         case 44:
/*      */         case 45:
/*      */         case 46:
/*      */         case 47:
/*      */         case 48:
/*      */         case 49:
/*      */         case 50:
/*      */         case 51:
/*      */         case 52:
/*      */         case 53:
/*      */         case 59:
/*      */         case 60:
/*      */         case 61:
/*      */         case 62:
/*      */         case 63:
/*      */         case 64:
/*      */         case 65:
/*      */         case 66:
/*      */         case 67:
/*      */         case 68:
/*      */         case 69:
/*      */         case 70:
/*      */         case 71:
/*      */         case 72:
/*      */         case 73:
/*      */         case 74:
/*      */         case 75:
/*      */         case 76:
/*      */         case 77:
/*      */         case 78:
/*      */         case 79:
/*      */         case 80:
/*      */         case 81:
/*      */         case 82:
/*      */         case 83:
/*      */         case 84:
/*      */         case 85:
/*      */         case 86:
/*      */         case 87:
/*      */         case 88:
/*      */         case 89:
/*      */         case 90:
/*      */         case 91:
/*      */         case 92:
/*      */         case 93:
/*      */         case 94:
/*      */         case 95:
/*      */         case 96:
/*      */         case 97:
/*      */         case 98:
/*      */         case 99:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/*      */         case 104:
/*      */         case 105:
/*      */         case 106:
/*      */         case 107:
/*      */         case 108:
/*      */         case 109:
/*      */         case 110:
/*      */         case 111:
/*      */         case 112:
/*      */         case 113:
/*      */         case 114:
/*      */         case 115:
/*      */         case 116:
/*      */         case 117:
/*      */         case 118:
/*      */         case 119:
/*      */         case 120:
/*      */         case 121:
/*      */         case 122:
/*      */         case 123:
/*      */         case 124:
/*      */         case 125:
/*      */         case 126:
/*      */         case 127:
/*      */         case 128:
/*      */         case 129:
/*      */         case 130:
/*      */         case 131:
/*      */         case 133:
/*      */         case 134:
/*      */         case 135:
/*      */         case 136:
/*      */         case 137:
/*      */         case 138:
/*      */         case 139:
/*      */         case 140:
/*      */         case 141:
/*      */         case 142:
/*      */         case 143:
/*      */         case 144:
/*      */         case 145:
/*      */         case 146:
/*      */         case 147:
/*      */         case 148:
/*      */         case 149:
/*      */         case 150:
/*      */         case 151:
/*      */         case 152:
/*      */         case 172:
/*      */         case 173:
/*      */         case 174:
/*      */         case 175:
/*      */         case 176:
/*      */         case 177:
/*      */         case 190:
/*      */         case 191:
/*      */         case 194:
/*      */         case 195:
/* 1462 */           currentOffset++;
/*      */           continue;
/*      */         case 153:
/*      */         case 154:
/*      */         case 155:
/*      */         case 156:
/*      */         case 157:
/*      */         case 158:
/*      */         case 159:
/*      */         case 160:
/*      */         case 161:
/*      */         case 162:
/*      */         case 163:
/*      */         case 164:
/*      */         case 165:
/*      */         case 166:
/*      */         case 167:
/*      */         case 168:
/*      */         case 198:
/*      */         case 199:
/* 1482 */           createLabel(bytecodeOffset + readShort(currentOffset + 1), labels);
/* 1483 */           currentOffset += 3;
/*      */           continue;
/*      */         case 202:
/*      */         case 203:
/*      */         case 204:
/*      */         case 205:
/*      */         case 206:
/*      */         case 207:
/*      */         case 208:
/*      */         case 209:
/*      */         case 210:
/*      */         case 211:
/*      */         case 212:
/*      */         case 213:
/*      */         case 214:
/*      */         case 215:
/*      */         case 216:
/*      */         case 217:
/*      */         case 218:
/*      */         case 219:
/* 1503 */           createLabel(bytecodeOffset + readUnsignedShort(currentOffset + 1), labels);
/* 1504 */           currentOffset += 3;
/*      */           continue;
/*      */         case 200:
/*      */         case 201:
/*      */         case 220:
/* 1509 */           createLabel(bytecodeOffset + readInt(currentOffset + 1), labels);
/* 1510 */           currentOffset += 5;
/*      */           continue;
/*      */         case 196:
/* 1513 */           switch (classFileBuffer[currentOffset + 1] & 0xFF) {
/*      */             case 21:
/*      */             case 22:
/*      */             case 23:
/*      */             case 24:
/*      */             case 25:
/*      */             case 54:
/*      */             case 55:
/*      */             case 56:
/*      */             case 57:
/*      */             case 58:
/*      */             case 169:
/* 1525 */               currentOffset += 4;
/*      */               continue;
/*      */             case 132:
/* 1528 */               currentOffset += 6;
/*      */               continue;
/*      */           } 
/* 1531 */           throw new IllegalArgumentException();
/*      */ 
/*      */ 
/*      */         
/*      */         case 170:
/* 1536 */           currentOffset += 4 - (bytecodeOffset & 0x3);
/*      */           
/* 1538 */           createLabel(bytecodeOffset + readInt(currentOffset), labels);
/* 1539 */           numTableEntries = readInt(currentOffset + 8) - readInt(currentOffset + 4) + 1;
/* 1540 */           currentOffset += 12;
/*      */           
/* 1542 */           while (numTableEntries-- > 0) {
/* 1543 */             createLabel(bytecodeOffset + readInt(currentOffset), labels);
/* 1544 */             currentOffset += 4;
/*      */           } 
/*      */           continue;
/*      */         
/*      */         case 171:
/* 1549 */           currentOffset += 4 - (bytecodeOffset & 0x3);
/*      */           
/* 1551 */           createLabel(bytecodeOffset + readInt(currentOffset), labels);
/* 1552 */           numSwitchCases = readInt(currentOffset + 4);
/* 1553 */           currentOffset += 8;
/*      */           
/* 1555 */           while (numSwitchCases-- > 0) {
/* 1556 */             createLabel(bytecodeOffset + readInt(currentOffset + 4), labels);
/* 1557 */             currentOffset += 8;
/*      */           } 
/*      */           continue;
/*      */         case 16:
/*      */         case 18:
/*      */         case 21:
/*      */         case 22:
/*      */         case 23:
/*      */         case 24:
/*      */         case 25:
/*      */         case 54:
/*      */         case 55:
/*      */         case 56:
/*      */         case 57:
/*      */         case 58:
/*      */         case 169:
/*      */         case 188:
/* 1574 */           currentOffset += 2;
/*      */           continue;
/*      */         case 17:
/*      */         case 19:
/*      */         case 20:
/*      */         case 132:
/*      */         case 178:
/*      */         case 179:
/*      */         case 180:
/*      */         case 181:
/*      */         case 182:
/*      */         case 183:
/*      */         case 184:
/*      */         case 187:
/*      */         case 189:
/*      */         case 192:
/*      */         case 193:
/* 1591 */           currentOffset += 3;
/*      */           continue;
/*      */         case 185:
/*      */         case 186:
/* 1595 */           currentOffset += 5;
/*      */           continue;
/*      */         case 197:
/* 1598 */           currentOffset += 4;
/*      */           continue;
/*      */       } 
/* 1601 */       throw new IllegalArgumentException();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1607 */     int exceptionTableLength = readUnsignedShort(currentOffset);
/* 1608 */     currentOffset += 2;
/* 1609 */     while (exceptionTableLength-- > 0) {
/* 1610 */       Label start = createLabel(readUnsignedShort(currentOffset), labels);
/* 1611 */       Label end = createLabel(readUnsignedShort(currentOffset + 2), labels);
/* 1612 */       Label handler = createLabel(readUnsignedShort(currentOffset + 4), labels);
/* 1613 */       String catchType = readUTF8(this.cpInfoOffsets[readUnsignedShort(currentOffset + 6)], charBuffer);
/* 1614 */       currentOffset += 8;
/* 1615 */       methodVisitor.visitTryCatchBlock(start, end, handler, catchType);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1624 */     int stackMapFrameOffset = 0;
/*      */     
/* 1626 */     int stackMapTableEndOffset = 0;
/*      */     
/* 1628 */     boolean compressedFrames = true;
/*      */     
/* 1630 */     int localVariableTableOffset = 0;
/*      */     
/* 1632 */     int localVariableTypeTableOffset = 0;
/*      */ 
/*      */     
/* 1635 */     int[] visibleTypeAnnotationOffsets = null;
/*      */ 
/*      */     
/* 1638 */     int[] invisibleTypeAnnotationOffsets = null;
/*      */ 
/*      */     
/* 1641 */     Attribute attributes = null;
/*      */     
/* 1643 */     int attributesCount = readUnsignedShort(currentOffset);
/* 1644 */     currentOffset += 2;
/* 1645 */     while (attributesCount-- > 0) {
/*      */       
/* 1647 */       String attributeName = readUTF8(currentOffset, charBuffer);
/* 1648 */       int attributeLength = readInt(currentOffset + 2);
/* 1649 */       currentOffset += 6;
/* 1650 */       if ("LocalVariableTable".equals(attributeName)) {
/* 1651 */         if ((context.parsingOptions & 0x2) == 0) {
/* 1652 */           localVariableTableOffset = currentOffset;
/*      */           
/* 1654 */           int currentLocalVariableTableOffset = currentOffset;
/* 1655 */           int localVariableTableLength = readUnsignedShort(currentLocalVariableTableOffset);
/* 1656 */           currentLocalVariableTableOffset += 2;
/* 1657 */           while (localVariableTableLength-- > 0) {
/* 1658 */             int startPc = readUnsignedShort(currentLocalVariableTableOffset);
/* 1659 */             createDebugLabel(startPc, labels);
/* 1660 */             int length = readUnsignedShort(currentLocalVariableTableOffset + 2);
/* 1661 */             createDebugLabel(startPc + length, labels);
/*      */             
/* 1663 */             currentLocalVariableTableOffset += 10;
/*      */           } 
/*      */         } 
/* 1666 */       } else if ("LocalVariableTypeTable".equals(attributeName)) {
/* 1667 */         localVariableTypeTableOffset = currentOffset;
/*      */       
/*      */       }
/* 1670 */       else if ("LineNumberTable".equals(attributeName)) {
/* 1671 */         if ((context.parsingOptions & 0x2) == 0) {
/*      */           
/* 1673 */           int currentLineNumberTableOffset = currentOffset;
/* 1674 */           int lineNumberTableLength = readUnsignedShort(currentLineNumberTableOffset);
/* 1675 */           currentLineNumberTableOffset += 2;
/* 1676 */           while (lineNumberTableLength-- > 0) {
/* 1677 */             int startPc = readUnsignedShort(currentLineNumberTableOffset);
/* 1678 */             int lineNumber = readUnsignedShort(currentLineNumberTableOffset + 2);
/* 1679 */             currentLineNumberTableOffset += 4;
/* 1680 */             createDebugLabel(startPc, labels);
/* 1681 */             labels[startPc].addLineNumber(lineNumber);
/*      */           } 
/*      */         } 
/* 1684 */       } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
/*      */         
/* 1686 */         visibleTypeAnnotationOffsets = readTypeAnnotations(methodVisitor, context, currentOffset, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1693 */       else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
/*      */         
/* 1695 */         invisibleTypeAnnotationOffsets = readTypeAnnotations(methodVisitor, context, currentOffset, false);
/*      */       }
/* 1697 */       else if ("StackMapTable".equals(attributeName)) {
/* 1698 */         if ((context.parsingOptions & 0x4) == 0) {
/* 1699 */           stackMapFrameOffset = currentOffset + 2;
/* 1700 */           stackMapTableEndOffset = currentOffset + attributeLength;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1711 */       else if ("StackMap".equals(attributeName)) {
/* 1712 */         if ((context.parsingOptions & 0x4) == 0) {
/* 1713 */           stackMapFrameOffset = currentOffset + 2;
/* 1714 */           stackMapTableEndOffset = currentOffset + attributeLength;
/* 1715 */           compressedFrames = false;
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1723 */         Attribute attribute = readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, codeOffset, labels);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1731 */         attribute.nextAttribute = attributes;
/* 1732 */         attributes = attribute;
/*      */       } 
/* 1734 */       currentOffset += attributeLength;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1739 */     boolean expandFrames = ((context.parsingOptions & 0x8) != 0);
/* 1740 */     if (stackMapFrameOffset != 0) {
/*      */ 
/*      */ 
/*      */       
/* 1744 */       context.currentFrameOffset = -1;
/* 1745 */       context.currentFrameType = 0;
/* 1746 */       context.currentFrameLocalCount = 0;
/* 1747 */       context.currentFrameLocalCountDelta = 0;
/* 1748 */       context.currentFrameLocalTypes = new Object[maxLocals];
/* 1749 */       context.currentFrameStackCount = 0;
/* 1750 */       context.currentFrameStackTypes = new Object[maxStack];
/* 1751 */       if (expandFrames) {
/* 1752 */         computeImplicitFrame(context);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1761 */       for (int offset = stackMapFrameOffset; offset < stackMapTableEndOffset - 2; offset++) {
/* 1762 */         if (classFileBuffer[offset] == 8) {
/* 1763 */           int potentialBytecodeOffset = readUnsignedShort(offset + 1);
/* 1764 */           if (potentialBytecodeOffset >= 0 && potentialBytecodeOffset < codeLength && (classFileBuffer[bytecodeStartOffset + potentialBytecodeOffset] & 0xFF) == 187)
/*      */           {
/*      */ 
/*      */             
/* 1768 */             createLabel(potentialBytecodeOffset, labels);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1773 */     if (expandFrames && (context.parsingOptions & 0x100) != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1780 */       methodVisitor.visitFrame(-1, maxLocals, null, 0, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1788 */     int currentVisibleTypeAnnotationIndex = 0;
/*      */ 
/*      */     
/* 1791 */     int currentVisibleTypeAnnotationBytecodeOffset = getTypeAnnotationBytecodeOffset(visibleTypeAnnotationOffsets, 0);
/*      */ 
/*      */     
/* 1794 */     int currentInvisibleTypeAnnotationIndex = 0;
/*      */ 
/*      */     
/* 1797 */     int currentInvisibleTypeAnnotationBytecodeOffset = getTypeAnnotationBytecodeOffset(invisibleTypeAnnotationOffsets, 0);
/*      */ 
/*      */     
/* 1800 */     boolean insertFrame = false;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1805 */     int wideJumpOpcodeDelta = ((context.parsingOptions & 0x100) == 0) ? 33 : 0;
/*      */ 
/*      */     
/* 1808 */     currentOffset = bytecodeStartOffset;
/* 1809 */     while (currentOffset < bytecodeEndOffset) {
/* 1810 */       Label target, defaultLabel; int cpInfoOffset, low, numPairs, nameAndTypeCpInfoOffset, high, keys[]; String owner, name; Label[] table, values; String str1, descriptor; int i; String str2; int bootstrapMethodOffset; Handle handle; Object[] bootstrapMethodArguments; int j, currentBytecodeOffset = currentOffset - bytecodeStartOffset;
/*      */ 
/*      */       
/* 1813 */       Label currentLabel = labels[currentBytecodeOffset];
/* 1814 */       if (currentLabel != null) {
/* 1815 */         currentLabel.accept(methodVisitor, ((context.parsingOptions & 0x2) == 0));
/*      */       }
/*      */ 
/*      */       
/* 1819 */       while (stackMapFrameOffset != 0 && (context.currentFrameOffset == currentBytecodeOffset || context.currentFrameOffset == -1)) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1824 */         if (context.currentFrameOffset != -1) {
/* 1825 */           if (!compressedFrames || expandFrames) {
/* 1826 */             methodVisitor.visitFrame(-1, context.currentFrameLocalCount, context.currentFrameLocalTypes, context.currentFrameStackCount, context.currentFrameStackTypes);
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/* 1833 */             methodVisitor.visitFrame(context.currentFrameType, context.currentFrameLocalCountDelta, context.currentFrameLocalTypes, context.currentFrameStackCount, context.currentFrameStackTypes);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1842 */           insertFrame = false;
/*      */         } 
/* 1844 */         if (stackMapFrameOffset < stackMapTableEndOffset) {
/*      */           
/* 1846 */           stackMapFrameOffset = readStackMapFrame(stackMapFrameOffset, compressedFrames, expandFrames, context); continue;
/*      */         } 
/* 1848 */         stackMapFrameOffset = 0;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1854 */       if (insertFrame) {
/* 1855 */         if ((context.parsingOptions & 0x8) != 0) {
/* 1856 */           methodVisitor.visitFrame(256, 0, null, 0, null);
/*      */         }
/* 1858 */         insertFrame = false;
/*      */       } 
/*      */ 
/*      */       
/* 1862 */       int opcode = classFileBuffer[currentOffset] & 0xFF;
/* 1863 */       switch (opcode) {
/*      */         case 0:
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/*      */         case 4:
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 8:
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 46:
/*      */         case 47:
/*      */         case 48:
/*      */         case 49:
/*      */         case 50:
/*      */         case 51:
/*      */         case 52:
/*      */         case 53:
/*      */         case 79:
/*      */         case 80:
/*      */         case 81:
/*      */         case 82:
/*      */         case 83:
/*      */         case 84:
/*      */         case 85:
/*      */         case 86:
/*      */         case 87:
/*      */         case 88:
/*      */         case 89:
/*      */         case 90:
/*      */         case 91:
/*      */         case 92:
/*      */         case 93:
/*      */         case 94:
/*      */         case 95:
/*      */         case 96:
/*      */         case 97:
/*      */         case 98:
/*      */         case 99:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/*      */         case 104:
/*      */         case 105:
/*      */         case 106:
/*      */         case 107:
/*      */         case 108:
/*      */         case 109:
/*      */         case 110:
/*      */         case 111:
/*      */         case 112:
/*      */         case 113:
/*      */         case 114:
/*      */         case 115:
/*      */         case 116:
/*      */         case 117:
/*      */         case 118:
/*      */         case 119:
/*      */         case 120:
/*      */         case 121:
/*      */         case 122:
/*      */         case 123:
/*      */         case 124:
/*      */         case 125:
/*      */         case 126:
/*      */         case 127:
/*      */         case 128:
/*      */         case 129:
/*      */         case 130:
/*      */         case 131:
/*      */         case 133:
/*      */         case 134:
/*      */         case 135:
/*      */         case 136:
/*      */         case 137:
/*      */         case 138:
/*      */         case 139:
/*      */         case 140:
/*      */         case 141:
/*      */         case 142:
/*      */         case 143:
/*      */         case 144:
/*      */         case 145:
/*      */         case 146:
/*      */         case 147:
/*      */         case 148:
/*      */         case 149:
/*      */         case 150:
/*      */         case 151:
/*      */         case 152:
/*      */         case 172:
/*      */         case 173:
/*      */         case 174:
/*      */         case 175:
/*      */         case 176:
/*      */         case 177:
/*      */         case 190:
/*      */         case 191:
/*      */         case 194:
/*      */         case 195:
/* 1971 */           methodVisitor.visitInsn(opcode);
/* 1972 */           currentOffset++;
/*      */           break;
/*      */         case 26:
/*      */         case 27:
/*      */         case 28:
/*      */         case 29:
/*      */         case 30:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 37:
/*      */         case 38:
/*      */         case 39:
/*      */         case 40:
/*      */         case 41:
/*      */         case 42:
/*      */         case 43:
/*      */         case 44:
/*      */         case 45:
/* 1994 */           opcode -= 26;
/* 1995 */           methodVisitor.visitVarInsn(21 + (opcode >> 2), opcode & 0x3);
/* 1996 */           currentOffset++;
/*      */           break;
/*      */         case 59:
/*      */         case 60:
/*      */         case 61:
/*      */         case 62:
/*      */         case 63:
/*      */         case 64:
/*      */         case 65:
/*      */         case 66:
/*      */         case 67:
/*      */         case 68:
/*      */         case 69:
/*      */         case 70:
/*      */         case 71:
/*      */         case 72:
/*      */         case 73:
/*      */         case 74:
/*      */         case 75:
/*      */         case 76:
/*      */         case 77:
/*      */         case 78:
/* 2018 */           opcode -= 59;
/* 2019 */           methodVisitor.visitVarInsn(54 + (opcode >> 2), opcode & 0x3);
/* 2020 */           currentOffset++;
/*      */           break;
/*      */         case 153:
/*      */         case 154:
/*      */         case 155:
/*      */         case 156:
/*      */         case 157:
/*      */         case 158:
/*      */         case 159:
/*      */         case 160:
/*      */         case 161:
/*      */         case 162:
/*      */         case 163:
/*      */         case 164:
/*      */         case 165:
/*      */         case 166:
/*      */         case 167:
/*      */         case 168:
/*      */         case 198:
/*      */         case 199:
/* 2040 */           methodVisitor.visitJumpInsn(opcode, labels[currentBytecodeOffset + 
/* 2041 */                 readShort(currentOffset + 1)]);
/* 2042 */           currentOffset += 3;
/*      */           break;
/*      */         case 200:
/*      */         case 201:
/* 2046 */           methodVisitor.visitJumpInsn(opcode - wideJumpOpcodeDelta, labels[currentBytecodeOffset + 
/*      */                 
/* 2048 */                 readInt(currentOffset + 1)]);
/* 2049 */           currentOffset += 5;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 202:
/*      */         case 203:
/*      */         case 204:
/*      */         case 205:
/*      */         case 206:
/*      */         case 207:
/*      */         case 208:
/*      */         case 209:
/*      */         case 210:
/*      */         case 211:
/*      */         case 212:
/*      */         case 213:
/*      */         case 214:
/*      */         case 215:
/*      */         case 216:
/*      */         case 217:
/*      */         case 218:
/*      */         case 219:
/* 2076 */           opcode = (opcode < 218) ? (opcode - 49) : (opcode - 20);
/*      */ 
/*      */ 
/*      */           
/* 2080 */           target = labels[currentBytecodeOffset + readUnsignedShort(currentOffset + 1)];
/* 2081 */           if (opcode == 167 || opcode == 168) {
/*      */             
/* 2083 */             methodVisitor.visitJumpInsn(opcode + 33, target);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 2088 */             opcode = (opcode < 167) ? ((opcode + 1 ^ 0x1) - 1) : (opcode ^ 0x1);
/* 2089 */             Label endif = createLabel(currentBytecodeOffset + 3, labels);
/* 2090 */             methodVisitor.visitJumpInsn(opcode, endif);
/* 2091 */             methodVisitor.visitJumpInsn(200, target);
/*      */ 
/*      */             
/* 2094 */             insertFrame = true;
/*      */           } 
/* 2096 */           currentOffset += 3;
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 220:
/* 2102 */           methodVisitor.visitJumpInsn(200, labels[currentBytecodeOffset + 
/* 2103 */                 readInt(currentOffset + 1)]);
/*      */ 
/*      */ 
/*      */           
/* 2107 */           insertFrame = true;
/* 2108 */           currentOffset += 5;
/*      */           break;
/*      */         
/*      */         case 196:
/* 2112 */           opcode = classFileBuffer[currentOffset + 1] & 0xFF;
/* 2113 */           if (opcode == 132) {
/* 2114 */             methodVisitor.visitIincInsn(
/* 2115 */                 readUnsignedShort(currentOffset + 2), readShort(currentOffset + 4));
/* 2116 */             currentOffset += 6; break;
/*      */           } 
/* 2118 */           methodVisitor.visitVarInsn(opcode, readUnsignedShort(currentOffset + 2));
/* 2119 */           currentOffset += 4;
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 170:
/* 2125 */           currentOffset += 4 - (currentBytecodeOffset & 0x3);
/*      */           
/* 2127 */           defaultLabel = labels[currentBytecodeOffset + readInt(currentOffset)];
/* 2128 */           low = readInt(currentOffset + 4);
/* 2129 */           high = readInt(currentOffset + 8);
/* 2130 */           currentOffset += 12;
/* 2131 */           table = new Label[high - low + 1];
/* 2132 */           for (i = 0; i < table.length; i++) {
/* 2133 */             table[i] = labels[currentBytecodeOffset + readInt(currentOffset)];
/* 2134 */             currentOffset += 4;
/*      */           } 
/* 2136 */           methodVisitor.visitTableSwitchInsn(low, high, defaultLabel, table);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 171:
/* 2142 */           currentOffset += 4 - (currentBytecodeOffset & 0x3);
/*      */           
/* 2144 */           defaultLabel = labels[currentBytecodeOffset + readInt(currentOffset)];
/* 2145 */           numPairs = readInt(currentOffset + 4);
/* 2146 */           currentOffset += 8;
/* 2147 */           keys = new int[numPairs];
/* 2148 */           values = new Label[numPairs];
/* 2149 */           for (i = 0; i < numPairs; i++) {
/* 2150 */             keys[i] = readInt(currentOffset);
/* 2151 */             values[i] = labels[currentBytecodeOffset + readInt(currentOffset + 4)];
/* 2152 */             currentOffset += 8;
/*      */           } 
/* 2154 */           methodVisitor.visitLookupSwitchInsn(defaultLabel, keys, values);
/*      */           break;
/*      */         
/*      */         case 21:
/*      */         case 22:
/*      */         case 23:
/*      */         case 24:
/*      */         case 25:
/*      */         case 54:
/*      */         case 55:
/*      */         case 56:
/*      */         case 57:
/*      */         case 58:
/*      */         case 169:
/* 2168 */           methodVisitor.visitVarInsn(opcode, classFileBuffer[currentOffset + 1] & 0xFF);
/* 2169 */           currentOffset += 2;
/*      */           break;
/*      */         case 16:
/*      */         case 188:
/* 2173 */           methodVisitor.visitIntInsn(opcode, classFileBuffer[currentOffset + 1]);
/* 2174 */           currentOffset += 2;
/*      */           break;
/*      */         case 17:
/* 2177 */           methodVisitor.visitIntInsn(opcode, readShort(currentOffset + 1));
/* 2178 */           currentOffset += 3;
/*      */           break;
/*      */         case 18:
/* 2181 */           methodVisitor.visitLdcInsn(
/* 2182 */               readConst(classFileBuffer[currentOffset + 1] & 0xFF, charBuffer));
/* 2183 */           currentOffset += 2;
/*      */           break;
/*      */         case 19:
/*      */         case 20:
/* 2187 */           methodVisitor.visitLdcInsn(readConst(readUnsignedShort(currentOffset + 1), charBuffer));
/* 2188 */           currentOffset += 3;
/*      */           break;
/*      */         
/*      */         case 178:
/*      */         case 179:
/*      */         case 180:
/*      */         case 181:
/*      */         case 182:
/*      */         case 183:
/*      */         case 184:
/*      */         case 185:
/* 2199 */           cpInfoOffset = this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)];
/* 2200 */           nameAndTypeCpInfoOffset = this.cpInfoOffsets[readUnsignedShort(cpInfoOffset + 2)];
/* 2201 */           owner = readClass(cpInfoOffset, charBuffer);
/* 2202 */           str1 = readUTF8(nameAndTypeCpInfoOffset, charBuffer);
/* 2203 */           str2 = readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
/* 2204 */           if (opcode < 182) {
/* 2205 */             methodVisitor.visitFieldInsn(opcode, owner, str1, str2);
/*      */           } else {
/* 2207 */             boolean isInterface = (classFileBuffer[cpInfoOffset - 1] == 11);
/*      */             
/* 2209 */             methodVisitor.visitMethodInsn(opcode, owner, str1, str2, isInterface);
/*      */           } 
/* 2211 */           if (opcode == 185) {
/* 2212 */             currentOffset += 5; break;
/*      */           } 
/* 2214 */           currentOffset += 3;
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 186:
/* 2220 */           cpInfoOffset = this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)];
/* 2221 */           nameAndTypeCpInfoOffset = this.cpInfoOffsets[readUnsignedShort(cpInfoOffset + 2)];
/* 2222 */           name = readUTF8(nameAndTypeCpInfoOffset, charBuffer);
/* 2223 */           descriptor = readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
/* 2224 */           bootstrapMethodOffset = this.bootstrapMethodOffsets[readUnsignedShort(cpInfoOffset)];
/*      */           
/* 2226 */           handle = (Handle)readConst(readUnsignedShort(bootstrapMethodOffset), charBuffer);
/*      */           
/* 2228 */           bootstrapMethodArguments = new Object[readUnsignedShort(bootstrapMethodOffset + 2)];
/* 2229 */           bootstrapMethodOffset += 4;
/* 2230 */           for (j = 0; j < bootstrapMethodArguments.length; j++) {
/* 2231 */             bootstrapMethodArguments[j] = 
/* 2232 */               readConst(readUnsignedShort(bootstrapMethodOffset), charBuffer);
/* 2233 */             bootstrapMethodOffset += 2;
/*      */           } 
/* 2235 */           methodVisitor.visitInvokeDynamicInsn(name, descriptor, handle, bootstrapMethodArguments);
/*      */           
/* 2237 */           currentOffset += 5;
/*      */           break;
/*      */         
/*      */         case 187:
/*      */         case 189:
/*      */         case 192:
/*      */         case 193:
/* 2244 */           methodVisitor.visitTypeInsn(opcode, readClass(currentOffset + 1, charBuffer));
/* 2245 */           currentOffset += 3;
/*      */           break;
/*      */         case 132:
/* 2248 */           methodVisitor.visitIincInsn(classFileBuffer[currentOffset + 1] & 0xFF, classFileBuffer[currentOffset + 2]);
/*      */           
/* 2250 */           currentOffset += 3;
/*      */           break;
/*      */         case 197:
/* 2253 */           methodVisitor.visitMultiANewArrayInsn(
/* 2254 */               readClass(currentOffset + 1, charBuffer), classFileBuffer[currentOffset + 3] & 0xFF);
/* 2255 */           currentOffset += 4;
/*      */           break;
/*      */         default:
/* 2258 */           throw new AssertionError();
/*      */       } 
/*      */ 
/*      */       
/* 2262 */       while (visibleTypeAnnotationOffsets != null && currentVisibleTypeAnnotationIndex < visibleTypeAnnotationOffsets.length && currentVisibleTypeAnnotationBytecodeOffset <= currentBytecodeOffset) {
/*      */ 
/*      */         
/* 2265 */         if (currentVisibleTypeAnnotationBytecodeOffset == currentBytecodeOffset) {
/*      */ 
/*      */           
/* 2268 */           int currentAnnotationOffset = readTypeAnnotationTarget(context, visibleTypeAnnotationOffsets[currentVisibleTypeAnnotationIndex]);
/*      */ 
/*      */           
/* 2271 */           String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 2272 */           currentAnnotationOffset += 2;
/*      */           
/* 2274 */           readElementValues(methodVisitor
/* 2275 */               .visitInsnAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2285 */         currentVisibleTypeAnnotationBytecodeOffset = getTypeAnnotationBytecodeOffset(visibleTypeAnnotationOffsets, ++currentVisibleTypeAnnotationIndex);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2290 */       while (invisibleTypeAnnotationOffsets != null && currentInvisibleTypeAnnotationIndex < invisibleTypeAnnotationOffsets.length && currentInvisibleTypeAnnotationBytecodeOffset <= currentBytecodeOffset) {
/*      */ 
/*      */         
/* 2293 */         if (currentInvisibleTypeAnnotationBytecodeOffset == currentBytecodeOffset) {
/*      */ 
/*      */           
/* 2296 */           int currentAnnotationOffset = readTypeAnnotationTarget(context, invisibleTypeAnnotationOffsets[currentInvisibleTypeAnnotationIndex]);
/*      */ 
/*      */           
/* 2299 */           String annotationDescriptor = readUTF8(currentAnnotationOffset, charBuffer);
/* 2300 */           currentAnnotationOffset += 2;
/*      */           
/* 2302 */           readElementValues(methodVisitor
/* 2303 */               .visitInsnAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2313 */         currentInvisibleTypeAnnotationBytecodeOffset = getTypeAnnotationBytecodeOffset(invisibleTypeAnnotationOffsets, ++currentInvisibleTypeAnnotationIndex);
/*      */       } 
/*      */     } 
/*      */     
/* 2317 */     if (labels[codeLength] != null) {
/* 2318 */       methodVisitor.visitLabel(labels[codeLength]);
/*      */     }
/*      */ 
/*      */     
/* 2322 */     if (localVariableTableOffset != 0 && (context.parsingOptions & 0x2) == 0) {
/*      */       
/* 2324 */       int[] typeTable = null;
/* 2325 */       if (localVariableTypeTableOffset != 0) {
/* 2326 */         typeTable = new int[readUnsignedShort(localVariableTypeTableOffset) * 3];
/* 2327 */         currentOffset = localVariableTypeTableOffset + 2;
/* 2328 */         int typeTableIndex = typeTable.length;
/* 2329 */         while (typeTableIndex > 0) {
/*      */           
/* 2331 */           typeTable[--typeTableIndex] = currentOffset + 6;
/* 2332 */           typeTable[--typeTableIndex] = readUnsignedShort(currentOffset + 8);
/* 2333 */           typeTable[--typeTableIndex] = readUnsignedShort(currentOffset);
/* 2334 */           currentOffset += 10;
/*      */         } 
/*      */       } 
/* 2337 */       int localVariableTableLength = readUnsignedShort(localVariableTableOffset);
/* 2338 */       currentOffset = localVariableTableOffset + 2;
/* 2339 */       while (localVariableTableLength-- > 0) {
/* 2340 */         int startPc = readUnsignedShort(currentOffset);
/* 2341 */         int length = readUnsignedShort(currentOffset + 2);
/* 2342 */         String name = readUTF8(currentOffset + 4, charBuffer);
/* 2343 */         String descriptor = readUTF8(currentOffset + 6, charBuffer);
/* 2344 */         int index = readUnsignedShort(currentOffset + 8);
/* 2345 */         currentOffset += 10;
/* 2346 */         String signature = null;
/* 2347 */         if (typeTable != null) {
/* 2348 */           for (int i = 0; i < typeTable.length; i += 3) {
/* 2349 */             if (typeTable[i] == startPc && typeTable[i + 1] == index) {
/* 2350 */               signature = readUTF8(typeTable[i + 2], charBuffer);
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/* 2355 */         methodVisitor.visitLocalVariable(name, descriptor, signature, labels[startPc], labels[startPc + length], index);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2361 */     if (visibleTypeAnnotationOffsets != null) {
/* 2362 */       for (int typeAnnotationOffset : visibleTypeAnnotationOffsets) {
/* 2363 */         int targetType = readByte(typeAnnotationOffset);
/* 2364 */         if (targetType == 64 || targetType == 65) {
/*      */ 
/*      */           
/* 2367 */           currentOffset = readTypeAnnotationTarget(context, typeAnnotationOffset);
/*      */           
/* 2369 */           String annotationDescriptor = readUTF8(currentOffset, charBuffer);
/* 2370 */           currentOffset += 2;
/*      */           
/* 2372 */           readElementValues(methodVisitor
/* 2373 */               .visitLocalVariableAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, context.currentLocalVariableAnnotationRangeStarts, context.currentLocalVariableAnnotationRangeEnds, context.currentLocalVariableAnnotationRangeIndices, annotationDescriptor, true), currentOffset, true, charBuffer);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2389 */     if (invisibleTypeAnnotationOffsets != null) {
/* 2390 */       for (int typeAnnotationOffset : invisibleTypeAnnotationOffsets) {
/* 2391 */         int targetType = readByte(typeAnnotationOffset);
/* 2392 */         if (targetType == 64 || targetType == 65) {
/*      */ 
/*      */           
/* 2395 */           currentOffset = readTypeAnnotationTarget(context, typeAnnotationOffset);
/*      */           
/* 2397 */           String annotationDescriptor = readUTF8(currentOffset, charBuffer);
/* 2398 */           currentOffset += 2;
/*      */           
/* 2400 */           readElementValues(methodVisitor
/* 2401 */               .visitLocalVariableAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, context.currentLocalVariableAnnotationRangeStarts, context.currentLocalVariableAnnotationRangeEnds, context.currentLocalVariableAnnotationRangeIndices, annotationDescriptor, false), currentOffset, true, charBuffer);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2417 */     while (attributes != null) {
/*      */       
/* 2419 */       Attribute nextAttribute = attributes.nextAttribute;
/* 2420 */       attributes.nextAttribute = null;
/* 2421 */       methodVisitor.visitAttribute(attributes);
/* 2422 */       attributes = nextAttribute;
/*      */     } 
/*      */ 
/*      */     
/* 2426 */     methodVisitor.visitMaxs(maxStack, maxLocals);
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
/*      */   protected Label readLabel(int bytecodeOffset, Label[] labels) {
/* 2441 */     if (bytecodeOffset >= labels.length) {
/* 2442 */       return new Label();
/*      */     }
/*      */     
/* 2445 */     if (labels[bytecodeOffset] == null) {
/* 2446 */       labels[bytecodeOffset] = new Label();
/*      */     }
/* 2448 */     return labels[bytecodeOffset];
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
/*      */   private Label createLabel(int bytecodeOffset, Label[] labels) {
/* 2461 */     Label label = readLabel(bytecodeOffset, labels);
/* 2462 */     label.flags = (short)(label.flags & 0xFFFFFFFE);
/* 2463 */     return label;
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
/*      */   private void createDebugLabel(int bytecodeOffset, Label[] labels) {
/* 2475 */     if (labels[bytecodeOffset] == null) {
/* 2476 */       (readLabel(bytecodeOffset, labels)).flags = (short)((readLabel(bytecodeOffset, labels)).flags | 0x1);
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
/*      */   private int[] readTypeAnnotations(MethodVisitor methodVisitor, Context context, int runtimeTypeAnnotationsOffset, boolean visible) {
/* 2503 */     char[] charBuffer = context.charBuffer;
/* 2504 */     int currentOffset = runtimeTypeAnnotationsOffset;
/*      */     
/* 2506 */     int[] typeAnnotationsOffsets = new int[readUnsignedShort(currentOffset)];
/* 2507 */     currentOffset += 2;
/*      */     
/* 2509 */     for (int i = 0; i < typeAnnotationsOffsets.length; i++) {
/* 2510 */       int tableLength; typeAnnotationsOffsets[i] = currentOffset;
/*      */ 
/*      */       
/* 2513 */       int targetType = readInt(currentOffset);
/* 2514 */       switch (targetType >>> 24) {
/*      */ 
/*      */         
/*      */         case 64:
/*      */         case 65:
/* 2519 */           tableLength = readUnsignedShort(currentOffset + 1);
/* 2520 */           currentOffset += 3;
/* 2521 */           while (tableLength-- > 0) {
/* 2522 */             int startPc = readUnsignedShort(currentOffset);
/* 2523 */             int length = readUnsignedShort(currentOffset + 2);
/*      */             
/* 2525 */             currentOffset += 6;
/* 2526 */             createLabel(startPc, context.currentMethodLabels);
/* 2527 */             createLabel(startPc + length, context.currentMethodLabels);
/*      */           } 
/*      */           break;
/*      */         case 71:
/*      */         case 72:
/*      */         case 73:
/*      */         case 74:
/*      */         case 75:
/* 2535 */           currentOffset += 4;
/*      */           break;
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 23:
/*      */         case 66:
/*      */         case 67:
/*      */         case 68:
/*      */         case 69:
/*      */         case 70:
/* 2546 */           currentOffset += 3;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/* 2556 */           throw new IllegalArgumentException();
/*      */       } 
/*      */ 
/*      */       
/* 2560 */       int pathLength = readByte(currentOffset);
/* 2561 */       if (targetType >>> 24 == 66) {
/*      */         
/* 2563 */         TypePath path = (pathLength == 0) ? null : new TypePath(this.b, currentOffset);
/* 2564 */         currentOffset += 1 + 2 * pathLength;
/*      */         
/* 2566 */         String annotationDescriptor = readUTF8(currentOffset, charBuffer);
/* 2567 */         currentOffset += 2;
/*      */ 
/*      */         
/* 2570 */         currentOffset = readElementValues(methodVisitor
/* 2571 */             .visitTryCatchAnnotation(targetType & 0xFFFFFF00, path, annotationDescriptor, visible), currentOffset, true, charBuffer);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */         
/* 2580 */         currentOffset += 3 + 2 * pathLength;
/*      */ 
/*      */ 
/*      */         
/* 2584 */         currentOffset = readElementValues(null, currentOffset, true, charBuffer);
/*      */       } 
/*      */     } 
/*      */     
/* 2588 */     return typeAnnotationsOffsets;
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
/*      */   private int getTypeAnnotationBytecodeOffset(int[] typeAnnotationOffsets, int typeAnnotationIndex) {
/* 2603 */     if (typeAnnotationOffsets == null || typeAnnotationIndex >= typeAnnotationOffsets.length || 
/*      */       
/* 2605 */       readByte(typeAnnotationOffsets[typeAnnotationIndex]) < 67) {
/* 2606 */       return -1;
/*      */     }
/* 2608 */     return readUnsignedShort(typeAnnotationOffsets[typeAnnotationIndex] + 1);
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
/*      */   private int readTypeAnnotationTarget(Context context, int typeAnnotationOffset) {
/* 2622 */     int tableLength, i, currentOffset = typeAnnotationOffset;
/*      */     
/* 2624 */     int targetType = readInt(typeAnnotationOffset);
/* 2625 */     switch (targetType >>> 24) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 22:
/* 2629 */         targetType &= 0xFFFF0000;
/* 2630 */         currentOffset += 2;
/*      */         break;
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/* 2635 */         targetType &= 0xFF000000;
/* 2636 */         currentOffset++;
/*      */         break;
/*      */       case 64:
/*      */       case 65:
/* 2640 */         targetType &= 0xFF000000;
/* 2641 */         tableLength = readUnsignedShort(currentOffset + 1);
/* 2642 */         currentOffset += 3;
/* 2643 */         context.currentLocalVariableAnnotationRangeStarts = new Label[tableLength];
/* 2644 */         context.currentLocalVariableAnnotationRangeEnds = new Label[tableLength];
/* 2645 */         context.currentLocalVariableAnnotationRangeIndices = new int[tableLength];
/* 2646 */         for (i = 0; i < tableLength; i++) {
/* 2647 */           int startPc = readUnsignedShort(currentOffset);
/* 2648 */           int length = readUnsignedShort(currentOffset + 2);
/* 2649 */           int index = readUnsignedShort(currentOffset + 4);
/* 2650 */           currentOffset += 6;
/* 2651 */           context.currentLocalVariableAnnotationRangeStarts[i] = 
/* 2652 */             createLabel(startPc, context.currentMethodLabels);
/* 2653 */           context.currentLocalVariableAnnotationRangeEnds[i] = 
/* 2654 */             createLabel(startPc + length, context.currentMethodLabels);
/* 2655 */           context.currentLocalVariableAnnotationRangeIndices[i] = index;
/*      */         } 
/*      */         break;
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/* 2663 */         targetType &= 0xFF0000FF;
/* 2664 */         currentOffset += 4;
/*      */         break;
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 23:
/*      */       case 66:
/* 2671 */         targetType &= 0xFFFFFF00;
/* 2672 */         currentOffset += 3;
/*      */         break;
/*      */       case 67:
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/* 2678 */         targetType &= 0xFF000000;
/* 2679 */         currentOffset += 3;
/*      */         break;
/*      */       default:
/* 2682 */         throw new IllegalArgumentException();
/*      */     } 
/* 2684 */     context.currentTypeAnnotationTarget = targetType;
/*      */     
/* 2686 */     int pathLength = readByte(currentOffset);
/* 2687 */     context.currentTypeAnnotationTargetPath = (pathLength == 0) ? null : new TypePath(this.b, currentOffset);
/*      */ 
/*      */     
/* 2690 */     return currentOffset + 1 + 2 * pathLength;
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
/*      */   private void readParameterAnnotations(MethodVisitor methodVisitor, Context context, int runtimeParameterAnnotationsOffset, boolean visible) {
/* 2709 */     int currentOffset = runtimeParameterAnnotationsOffset;
/* 2710 */     int numParameters = this.b[currentOffset++] & 0xFF;
/* 2711 */     methodVisitor.visitAnnotableParameterCount(numParameters, visible);
/* 2712 */     char[] charBuffer = context.charBuffer;
/* 2713 */     for (int i = 0; i < numParameters; i++) {
/* 2714 */       int numAnnotations = readUnsignedShort(currentOffset);
/* 2715 */       currentOffset += 2;
/* 2716 */       while (numAnnotations-- > 0) {
/*      */         
/* 2718 */         String annotationDescriptor = readUTF8(currentOffset, charBuffer);
/* 2719 */         currentOffset += 2;
/*      */ 
/*      */         
/* 2722 */         currentOffset = readElementValues(methodVisitor
/* 2723 */             .visitParameterAnnotation(i, annotationDescriptor, visible), currentOffset, true, charBuffer);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readElementValues(AnnotationVisitor annotationVisitor, int annotationOffset, boolean named, char[] charBuffer) {
/* 2750 */     int currentOffset = annotationOffset;
/*      */     
/* 2752 */     int numElementValuePairs = readUnsignedShort(currentOffset);
/* 2753 */     currentOffset += 2;
/* 2754 */     if (named) {
/*      */       
/* 2756 */       while (numElementValuePairs-- > 0) {
/* 2757 */         String elementName = readUTF8(currentOffset, charBuffer);
/*      */         
/* 2759 */         currentOffset = readElementValue(annotationVisitor, currentOffset + 2, elementName, charBuffer);
/*      */       } 
/*      */     } else {
/*      */       
/* 2763 */       while (numElementValuePairs-- > 0)
/*      */       {
/* 2765 */         currentOffset = readElementValue(annotationVisitor, currentOffset, null, charBuffer);
/*      */       }
/*      */     } 
/* 2768 */     if (annotationVisitor != null) {
/* 2769 */       annotationVisitor.visitEnd();
/*      */     }
/* 2771 */     return currentOffset;
/*      */   }
/*      */   
/*      */   private int readElementValue(AnnotationVisitor annotationVisitor, int elementValueOffset, String elementName, char[] charBuffer) {
/*      */     int numValues;
/*      */     byte[] byteValues;
/*      */     int i;
/*      */     boolean[] booleanValues;
/*      */     int j;
/*      */     short[] shortValues;
/*      */     int k;
/*      */     char[] charValues;
/*      */     int m, intValues[], n;
/*      */     long[] longValues;
/*      */     int i1;
/*      */     float[] floatValues;
/*      */     int i2;
/*      */     double[] doubleValues;
/* 2789 */     int i3, currentOffset = elementValueOffset;
/* 2790 */     if (annotationVisitor == null) {
/* 2791 */       switch (this.b[currentOffset] & 0xFF) {
/*      */         case 101:
/* 2793 */           return currentOffset + 5;
/*      */         case 64:
/* 2795 */           return readElementValues(null, currentOffset + 3, true, charBuffer);
/*      */         case 91:
/* 2797 */           return readElementValues(null, currentOffset + 1, false, charBuffer);
/*      */       } 
/* 2799 */       return currentOffset + 3;
/*      */     } 
/*      */     
/* 2802 */     switch (this.b[currentOffset++] & 0xFF) {
/*      */       case 66:
/* 2804 */         annotationVisitor.visit(elementName, 
/* 2805 */             Byte.valueOf((byte)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset)])));
/* 2806 */         currentOffset += 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2950 */         return currentOffset;case 67: annotationVisitor.visit(elementName, Character.valueOf((char)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset)]))); currentOffset += 2; return currentOffset;case 68: case 70: case 73: case 74: annotationVisitor.visit(elementName, readConst(readUnsignedShort(currentOffset), charBuffer)); currentOffset += 2; return currentOffset;case 83: annotationVisitor.visit(elementName, Short.valueOf((short)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset)]))); currentOffset += 2; return currentOffset;case 90: annotationVisitor.visit(elementName, (readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset)]) == 0) ? Boolean.FALSE : Boolean.TRUE); currentOffset += 2; return currentOffset;case 115: annotationVisitor.visit(elementName, readUTF8(currentOffset, charBuffer)); currentOffset += 2; return currentOffset;case 101: annotationVisitor.visitEnum(elementName, readUTF8(currentOffset, charBuffer), readUTF8(currentOffset + 2, charBuffer)); currentOffset += 4; return currentOffset;case 99: annotationVisitor.visit(elementName, Type.getType(readUTF8(currentOffset, charBuffer))); currentOffset += 2; return currentOffset;case 64: currentOffset = readElementValues(annotationVisitor.visitAnnotation(elementName, readUTF8(currentOffset, charBuffer)), currentOffset + 2, true, charBuffer); return currentOffset;case 91: numValues = readUnsignedShort(currentOffset); currentOffset += 2; if (numValues == 0) return readElementValues(annotationVisitor.visitArray(elementName), currentOffset - 2, false, charBuffer);  switch (this.b[currentOffset] & 0xFF) { case 66: byteValues = new byte[numValues]; for (i = 0; i < numValues; i++) { byteValues[i] = (byte)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]); currentOffset += 3; }  annotationVisitor.visit(elementName, byteValues); return currentOffset;case 90: booleanValues = new boolean[numValues]; for (j = 0; j < numValues; j++) { booleanValues[j] = (readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]) != 0); currentOffset += 3; }  annotationVisitor.visit(elementName, booleanValues); return currentOffset;case 83: shortValues = new short[numValues]; for (k = 0; k < numValues; k++) { shortValues[k] = (short)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]); currentOffset += 3; }  annotationVisitor.visit(elementName, shortValues); return currentOffset;case 67: charValues = new char[numValues]; for (m = 0; m < numValues; m++) { charValues[m] = (char)readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]); currentOffset += 3; }  annotationVisitor.visit(elementName, charValues); return currentOffset;case 73: intValues = new int[numValues]; for (n = 0; n < numValues; n++) { intValues[n] = readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]); currentOffset += 3; }  annotationVisitor.visit(elementName, intValues); return currentOffset;case 74: longValues = new long[numValues]; for (i1 = 0; i1 < numValues; i1++) { longValues[i1] = readLong(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)]); currentOffset += 3; }  annotationVisitor.visit(elementName, longValues); return currentOffset;case 70: floatValues = new float[numValues]; for (i2 = 0; i2 < numValues; i2++) { floatValues[i2] = Float.intBitsToFloat(readInt(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)])); currentOffset += 3; }  annotationVisitor.visit(elementName, floatValues); return currentOffset;case 68: doubleValues = new double[numValues]; for (i3 = 0; i3 < numValues; i3++) { doubleValues[i3] = Double.longBitsToDouble(readLong(this.cpInfoOffsets[readUnsignedShort(currentOffset + 1)])); currentOffset += 3; }  annotationVisitor.visit(elementName, doubleValues); return currentOffset; }  currentOffset = readElementValues(annotationVisitor.visitArray(elementName), currentOffset - 2, false, charBuffer); return currentOffset;
/*      */     } 
/*      */     throw new IllegalArgumentException();
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
/*      */   private void computeImplicitFrame(Context context) {
/* 2964 */     String methodDescriptor = context.currentMethodDescriptor;
/* 2965 */     Object[] locals = context.currentFrameLocalTypes;
/* 2966 */     int numLocal = 0;
/* 2967 */     if ((context.currentMethodAccessFlags & 0x8) == 0) {
/* 2968 */       if ("<init>".equals(context.currentMethodName)) {
/* 2969 */         locals[numLocal++] = Opcodes.UNINITIALIZED_THIS;
/*      */       } else {
/* 2971 */         locals[numLocal++] = readClass(this.header + 2, context.charBuffer);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 2976 */     int currentMethodDescritorOffset = 1;
/*      */     while (true) {
/* 2978 */       int currentArgumentDescriptorStartOffset = currentMethodDescritorOffset;
/* 2979 */       switch (methodDescriptor.charAt(currentMethodDescritorOffset++)) {
/*      */         case 'B':
/*      */         case 'C':
/*      */         case 'I':
/*      */         case 'S':
/*      */         case 'Z':
/* 2985 */           locals[numLocal++] = Opcodes.INTEGER;
/*      */           continue;
/*      */         case 'F':
/* 2988 */           locals[numLocal++] = Opcodes.FLOAT;
/*      */           continue;
/*      */         case 'J':
/* 2991 */           locals[numLocal++] = Opcodes.LONG;
/*      */           continue;
/*      */         case 'D':
/* 2994 */           locals[numLocal++] = Opcodes.DOUBLE;
/*      */           continue;
/*      */         case '[':
/* 2997 */           while (methodDescriptor.charAt(currentMethodDescritorOffset) == '[') {
/* 2998 */             currentMethodDescritorOffset++;
/*      */           }
/* 3000 */           if (methodDescriptor.charAt(currentMethodDescritorOffset) == 'L') {
/* 3001 */             currentMethodDescritorOffset++;
/* 3002 */             while (methodDescriptor.charAt(currentMethodDescritorOffset) != ';') {
/* 3003 */               currentMethodDescritorOffset++;
/*      */             }
/*      */           } 
/* 3006 */           locals[numLocal++] = methodDescriptor
/* 3007 */             .substring(currentArgumentDescriptorStartOffset, ++currentMethodDescritorOffset);
/*      */           continue;
/*      */         
/*      */         case 'L':
/* 3011 */           while (methodDescriptor.charAt(currentMethodDescritorOffset) != ';') {
/* 3012 */             currentMethodDescritorOffset++;
/*      */           }
/* 3014 */           locals[numLocal++] = methodDescriptor
/* 3015 */             .substring(currentArgumentDescriptorStartOffset + 1, currentMethodDescritorOffset++); continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 3019 */     context.currentFrameLocalCount = numLocal;
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
/*      */   private int readStackMapFrame(int stackMapFrameOffset, boolean compressed, boolean expand, Context context) {
/* 3044 */     int frameType, offsetDelta, currentOffset = stackMapFrameOffset;
/* 3045 */     char[] charBuffer = context.charBuffer;
/* 3046 */     Label[] labels = context.currentMethodLabels;
/*      */     
/* 3048 */     if (compressed) {
/*      */       
/* 3050 */       frameType = this.b[currentOffset++] & 0xFF;
/*      */     } else {
/* 3052 */       frameType = 255;
/* 3053 */       context.currentFrameOffset = -1;
/*      */     } 
/*      */     
/* 3056 */     context.currentFrameLocalCountDelta = 0;
/* 3057 */     if (frameType < 64) {
/* 3058 */       offsetDelta = frameType;
/* 3059 */       context.currentFrameType = 3;
/* 3060 */       context.currentFrameStackCount = 0;
/* 3061 */     } else if (frameType < 128) {
/* 3062 */       offsetDelta = frameType - 64;
/*      */       
/* 3064 */       currentOffset = readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, 0, charBuffer, labels);
/*      */       
/* 3066 */       context.currentFrameType = 4;
/* 3067 */       context.currentFrameStackCount = 1;
/* 3068 */     } else if (frameType >= 247) {
/* 3069 */       offsetDelta = readUnsignedShort(currentOffset);
/* 3070 */       currentOffset += 2;
/* 3071 */       if (frameType == 247) {
/*      */         
/* 3073 */         currentOffset = readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, 0, charBuffer, labels);
/*      */         
/* 3075 */         context.currentFrameType = 4;
/* 3076 */         context.currentFrameStackCount = 1;
/* 3077 */       } else if (frameType >= 248 && frameType < 251) {
/* 3078 */         context.currentFrameType = 2;
/* 3079 */         context.currentFrameLocalCountDelta = 251 - frameType;
/* 3080 */         context.currentFrameLocalCount -= context.currentFrameLocalCountDelta;
/* 3081 */         context.currentFrameStackCount = 0;
/* 3082 */       } else if (frameType == 251) {
/* 3083 */         context.currentFrameType = 3;
/* 3084 */         context.currentFrameStackCount = 0;
/* 3085 */       } else if (frameType < 255) {
/* 3086 */         int local = expand ? context.currentFrameLocalCount : 0;
/* 3087 */         for (int k = frameType - 251; k > 0; k--)
/*      */         {
/* 3089 */           currentOffset = readVerificationTypeInfo(currentOffset, context.currentFrameLocalTypes, local++, charBuffer, labels);
/*      */         }
/*      */         
/* 3092 */         context.currentFrameType = 1;
/* 3093 */         context.currentFrameLocalCountDelta = frameType - 251;
/* 3094 */         context.currentFrameLocalCount += context.currentFrameLocalCountDelta;
/* 3095 */         context.currentFrameStackCount = 0;
/*      */       } else {
/* 3097 */         int numberOfLocals = readUnsignedShort(currentOffset);
/* 3098 */         currentOffset += 2;
/* 3099 */         context.currentFrameType = 0;
/* 3100 */         context.currentFrameLocalCountDelta = numberOfLocals;
/* 3101 */         context.currentFrameLocalCount = numberOfLocals;
/* 3102 */         for (int local = 0; local < numberOfLocals; local++)
/*      */         {
/* 3104 */           currentOffset = readVerificationTypeInfo(currentOffset, context.currentFrameLocalTypes, local, charBuffer, labels);
/*      */         }
/*      */         
/* 3107 */         int numberOfStackItems = readUnsignedShort(currentOffset);
/* 3108 */         currentOffset += 2;
/* 3109 */         context.currentFrameStackCount = numberOfStackItems;
/* 3110 */         for (int stack = 0; stack < numberOfStackItems; stack++)
/*      */         {
/* 3112 */           currentOffset = readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, stack, charBuffer, labels);
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/* 3117 */       throw new IllegalArgumentException();
/*      */     } 
/* 3119 */     context.currentFrameOffset += offsetDelta + 1;
/* 3120 */     createLabel(context.currentFrameOffset, labels);
/* 3121 */     return currentOffset;
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
/*      */   private int readVerificationTypeInfo(int verificationTypeInfoOffset, Object[] frame, int index, char[] charBuffer, Label[] labels) {
/* 3144 */     int currentOffset = verificationTypeInfoOffset;
/* 3145 */     int tag = this.b[currentOffset++] & 0xFF;
/* 3146 */     switch (tag) {
/*      */       case 0:
/* 3148 */         frame[index] = Opcodes.TOP;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3179 */         return currentOffset;case 1: frame[index] = Opcodes.INTEGER; return currentOffset;case 2: frame[index] = Opcodes.FLOAT; return currentOffset;case 3: frame[index] = Opcodes.DOUBLE; return currentOffset;case 4: frame[index] = Opcodes.LONG; return currentOffset;case 5: frame[index] = Opcodes.NULL; return currentOffset;case 6: frame[index] = Opcodes.UNINITIALIZED_THIS; return currentOffset;case 7: frame[index] = readClass(currentOffset, charBuffer); currentOffset += 2; return currentOffset;case 8: frame[index] = createLabel(readUnsignedShort(currentOffset), labels); currentOffset += 2; return currentOffset;
/*      */     } 
/*      */     throw new IllegalArgumentException();
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
/*      */   final int getFirstAttributeOffset() {
/* 3194 */     int currentOffset = this.header + 8 + readUnsignedShort(this.header + 6) * 2;
/*      */ 
/*      */     
/* 3197 */     int fieldsCount = readUnsignedShort(currentOffset);
/* 3198 */     currentOffset += 2;
/*      */     
/* 3200 */     while (fieldsCount-- > 0) {
/*      */ 
/*      */ 
/*      */       
/* 3204 */       int attributesCount = readUnsignedShort(currentOffset + 6);
/* 3205 */       currentOffset += 8;
/*      */       
/* 3207 */       while (attributesCount-- > 0)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 3212 */         currentOffset += 6 + readInt(currentOffset + 2);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 3217 */     int methodsCount = readUnsignedShort(currentOffset);
/* 3218 */     currentOffset += 2;
/* 3219 */     while (methodsCount-- > 0) {
/* 3220 */       int attributesCount = readUnsignedShort(currentOffset + 6);
/* 3221 */       currentOffset += 8;
/* 3222 */       while (attributesCount-- > 0) {
/* 3223 */         currentOffset += 6 + readInt(currentOffset + 2);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 3228 */     return currentOffset + 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] readBootstrapMethodsAttribute(int maxStringLength) {
/* 3239 */     char[] charBuffer = new char[maxStringLength];
/* 3240 */     int currentAttributeOffset = getFirstAttributeOffset();
/* 3241 */     int[] currentBootstrapMethodOffsets = null;
/* 3242 */     for (int i = readUnsignedShort(currentAttributeOffset - 2); i > 0; i--) {
/*      */       
/* 3244 */       String attributeName = readUTF8(currentAttributeOffset, charBuffer);
/* 3245 */       int attributeLength = readInt(currentAttributeOffset + 2);
/* 3246 */       currentAttributeOffset += 6;
/* 3247 */       if ("BootstrapMethods".equals(attributeName)) {
/*      */         
/* 3249 */         currentBootstrapMethodOffsets = new int[readUnsignedShort(currentAttributeOffset)];
/*      */         
/* 3251 */         int currentBootstrapMethodOffset = currentAttributeOffset + 2;
/* 3252 */         for (int j = 0; j < currentBootstrapMethodOffsets.length; j++) {
/* 3253 */           currentBootstrapMethodOffsets[j] = currentBootstrapMethodOffset;
/*      */ 
/*      */           
/* 3256 */           currentBootstrapMethodOffset += 4 + 
/* 3257 */             readUnsignedShort(currentBootstrapMethodOffset + 2) * 2;
/*      */         } 
/* 3259 */         return currentBootstrapMethodOffsets;
/*      */       } 
/* 3261 */       currentAttributeOffset += attributeLength;
/*      */     } 
/* 3263 */     return null;
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
/*      */   private Attribute readAttribute(Attribute[] attributePrototypes, String type, int offset, int length, char[] charBuffer, int codeAttributeOffset, Label[] labels) {
/* 3292 */     for (Attribute attributePrototype : attributePrototypes) {
/* 3293 */       if (attributePrototype.type.equals(type)) {
/* 3294 */         return attributePrototype.read(this, offset, length, charBuffer, codeAttributeOffset, labels);
/*      */       }
/*      */     } 
/*      */     
/* 3298 */     return (new Attribute(type)).read(this, offset, length, null, -1, null);
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
/*      */   public int getItemCount() {
/* 3311 */     return this.cpInfoOffsets.length;
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
/*      */   public int getItem(int constantPoolEntryIndex) {
/* 3324 */     return this.cpInfoOffsets[constantPoolEntryIndex];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxStringLength() {
/* 3335 */     return this.maxStringLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readByte(int offset) {
/* 3346 */     return this.b[offset] & 0xFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readUnsignedShort(int offset) {
/* 3357 */     byte[] classFileBuffer = this.b;
/* 3358 */     return (classFileBuffer[offset] & 0xFF) << 8 | classFileBuffer[offset + 1] & 0xFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short readShort(int offset) {
/* 3369 */     byte[] classFileBuffer = this.b;
/* 3370 */     return (short)((classFileBuffer[offset] & 0xFF) << 8 | classFileBuffer[offset + 1] & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readInt(int offset) {
/* 3381 */     byte[] classFileBuffer = this.b;
/* 3382 */     return (classFileBuffer[offset] & 0xFF) << 24 | (classFileBuffer[offset + 1] & 0xFF) << 16 | (classFileBuffer[offset + 2] & 0xFF) << 8 | classFileBuffer[offset + 3] & 0xFF;
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
/*      */   public long readLong(int offset) {
/* 3396 */     long l1 = readInt(offset);
/* 3397 */     long l0 = readInt(offset + 4) & 0xFFFFFFFFL;
/* 3398 */     return l1 << 32L | l0;
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
/*      */   public String readUTF8(int offset, char[] charBuffer) {
/* 3413 */     int constantPoolEntryIndex = readUnsignedShort(offset);
/* 3414 */     if (offset == 0 || constantPoolEntryIndex == 0) {
/* 3415 */       return null;
/*      */     }
/* 3417 */     return readUtf(constantPoolEntryIndex, charBuffer);
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
/*      */   final String readUtf(int constantPoolEntryIndex, char[] charBuffer) {
/* 3430 */     String value = this.constantUtf8Values[constantPoolEntryIndex];
/* 3431 */     if (value != null) {
/* 3432 */       return value;
/*      */     }
/* 3434 */     int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
/* 3435 */     this.constantUtf8Values[constantPoolEntryIndex] = 
/* 3436 */       readUtf(cpInfoOffset + 2, readUnsignedShort(cpInfoOffset), charBuffer); return readUtf(cpInfoOffset + 2, readUnsignedShort(cpInfoOffset), charBuffer);
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
/*      */   private String readUtf(int utfOffset, int utfLength, char[] charBuffer) {
/* 3449 */     int currentOffset = utfOffset;
/* 3450 */     int endOffset = currentOffset + utfLength;
/* 3451 */     int strLength = 0;
/* 3452 */     byte[] classFileBuffer = this.b;
/* 3453 */     while (currentOffset < endOffset) {
/* 3454 */       int currentByte = classFileBuffer[currentOffset++];
/* 3455 */       if ((currentByte & 0x80) == 0) {
/* 3456 */         charBuffer[strLength++] = (char)(currentByte & 0x7F); continue;
/* 3457 */       }  if ((currentByte & 0xE0) == 192) {
/* 3458 */         charBuffer[strLength++] = (char)(((currentByte & 0x1F) << 6) + (classFileBuffer[currentOffset++] & 0x3F));
/*      */         continue;
/*      */       } 
/* 3461 */       charBuffer[strLength++] = (char)(((currentByte & 0xF) << 12) + ((classFileBuffer[currentOffset++] & 0x3F) << 6) + (classFileBuffer[currentOffset++] & 0x3F));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3468 */     return new String(charBuffer, 0, strLength);
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
/*      */   private String readStringish(int offset, char[] charBuffer) {
/* 3486 */     return readUTF8(this.cpInfoOffsets[readUnsignedShort(offset)], charBuffer);
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
/*      */   public String readClass(int offset, char[] charBuffer) {
/* 3500 */     return readStringish(offset, charBuffer);
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
/*      */   public String readModule(int offset, char[] charBuffer) {
/* 3514 */     return readStringish(offset, charBuffer);
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
/*      */   public String readPackage(int offset, char[] charBuffer) {
/* 3528 */     return readStringish(offset, charBuffer);
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
/*      */   private ConstantDynamic readConstantDynamic(int constantPoolEntryIndex, char[] charBuffer) {
/* 3542 */     ConstantDynamic constantDynamic = this.constantDynamicValues[constantPoolEntryIndex];
/* 3543 */     if (constantDynamic != null) {
/* 3544 */       return constantDynamic;
/*      */     }
/* 3546 */     int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
/* 3547 */     int nameAndTypeCpInfoOffset = this.cpInfoOffsets[readUnsignedShort(cpInfoOffset + 2)];
/* 3548 */     String name = readUTF8(nameAndTypeCpInfoOffset, charBuffer);
/* 3549 */     String descriptor = readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
/* 3550 */     int bootstrapMethodOffset = this.bootstrapMethodOffsets[readUnsignedShort(cpInfoOffset)];
/* 3551 */     Handle handle = (Handle)readConst(readUnsignedShort(bootstrapMethodOffset), charBuffer);
/* 3552 */     Object[] bootstrapMethodArguments = new Object[readUnsignedShort(bootstrapMethodOffset + 2)];
/* 3553 */     bootstrapMethodOffset += 4;
/* 3554 */     for (int i = 0; i < bootstrapMethodArguments.length; i++) {
/* 3555 */       bootstrapMethodArguments[i] = readConst(readUnsignedShort(bootstrapMethodOffset), charBuffer);
/* 3556 */       bootstrapMethodOffset += 2;
/*      */     } 
/* 3558 */     this.constantDynamicValues[constantPoolEntryIndex] = new ConstantDynamic(name, descriptor, handle, bootstrapMethodArguments); return new ConstantDynamic(name, descriptor, handle, bootstrapMethodArguments);
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
/*      */   public Object readConst(int constantPoolEntryIndex, char[] charBuffer) {
/*      */     int referenceKind, referenceCpInfoOffset, nameAndTypeCpInfoOffset;
/*      */     String owner, name, descriptor;
/*      */     boolean isInterface;
/* 3576 */     int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
/* 3577 */     switch (this.b[cpInfoOffset - 1]) {
/*      */       case 3:
/* 3579 */         return Integer.valueOf(readInt(cpInfoOffset));
/*      */       case 4:
/* 3581 */         return Float.valueOf(Float.intBitsToFloat(readInt(cpInfoOffset)));
/*      */       case 5:
/* 3583 */         return Long.valueOf(readLong(cpInfoOffset));
/*      */       case 6:
/* 3585 */         return Double.valueOf(Double.longBitsToDouble(readLong(cpInfoOffset)));
/*      */       case 7:
/* 3587 */         return Type.getObjectType(readUTF8(cpInfoOffset, charBuffer));
/*      */       case 8:
/* 3589 */         return readUTF8(cpInfoOffset, charBuffer);
/*      */       case 16:
/* 3591 */         return Type.getMethodType(readUTF8(cpInfoOffset, charBuffer));
/*      */       case 15:
/* 3593 */         referenceKind = readByte(cpInfoOffset);
/* 3594 */         referenceCpInfoOffset = this.cpInfoOffsets[readUnsignedShort(cpInfoOffset + 1)];
/* 3595 */         nameAndTypeCpInfoOffset = this.cpInfoOffsets[readUnsignedShort(referenceCpInfoOffset + 2)];
/* 3596 */         owner = readClass(referenceCpInfoOffset, charBuffer);
/* 3597 */         name = readUTF8(nameAndTypeCpInfoOffset, charBuffer);
/* 3598 */         descriptor = readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
/* 3599 */         isInterface = (this.b[referenceCpInfoOffset - 1] == 11);
/*      */         
/* 3601 */         return new Handle(referenceKind, owner, name, descriptor, isInterface);
/*      */       case 17:
/* 3603 */         return readConstantDynamic(constantPoolEntryIndex, charBuffer);
/*      */     } 
/* 3605 */     throw new IllegalArgumentException();
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/ClassReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */