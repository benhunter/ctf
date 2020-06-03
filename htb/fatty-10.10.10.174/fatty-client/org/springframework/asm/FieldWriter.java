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
/*     */ final class FieldWriter
/*     */   extends FieldVisitor
/*     */ {
/*     */   private final SymbolTable symbolTable;
/*     */   private final int accessFlags;
/*     */   private final int nameIndex;
/*     */   private final int descriptorIndex;
/*     */   private int signatureIndex;
/*     */   private int constantValueIndex;
/*     */   private AnnotationWriter lastRuntimeVisibleAnnotation;
/*     */   private AnnotationWriter lastRuntimeInvisibleAnnotation;
/*     */   private AnnotationWriter lastRuntimeVisibleTypeAnnotation;
/*     */   private AnnotationWriter lastRuntimeInvisibleTypeAnnotation;
/*     */   private Attribute firstAttribute;
/*     */   
/*     */   FieldWriter(SymbolTable symbolTable, int access, String name, String descriptor, String signature, Object constantValue) {
/* 127 */     super(458752);
/* 128 */     this.symbolTable = symbolTable;
/* 129 */     this.accessFlags = access;
/* 130 */     this.nameIndex = symbolTable.addConstantUtf8(name);
/* 131 */     this.descriptorIndex = symbolTable.addConstantUtf8(descriptor);
/* 132 */     if (signature != null) {
/* 133 */       this.signatureIndex = symbolTable.addConstantUtf8(signature);
/*     */     }
/* 135 */     if (constantValue != null) {
/* 136 */       this.constantValueIndex = (symbolTable.addConstant(constantValue)).index;
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
/*     */   public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
/* 148 */     ByteVector annotation = new ByteVector();
/*     */     
/* 150 */     annotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
/* 151 */     if (visible) {
/* 152 */       return this.lastRuntimeVisibleAnnotation = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeVisibleAnnotation);
/*     */     }
/*     */     
/* 155 */     return this.lastRuntimeInvisibleAnnotation = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeInvisibleAnnotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 165 */     ByteVector typeAnnotation = new ByteVector();
/*     */     
/* 167 */     TypeReference.putTarget(typeRef, typeAnnotation);
/* 168 */     TypePath.put(typePath, typeAnnotation);
/*     */     
/* 170 */     typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
/* 171 */     if (visible) {
/* 172 */       return this.lastRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastRuntimeVisibleTypeAnnotation);
/*     */     }
/*     */     
/* 175 */     return this.lastRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastRuntimeInvisibleTypeAnnotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute attribute) {
/* 183 */     attribute.nextAttribute = this.firstAttribute;
/* 184 */     this.firstAttribute = attribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int computeFieldInfoSize() {
/* 204 */     int size = 8;
/*     */     
/* 206 */     if (this.constantValueIndex != 0) {
/*     */       
/* 208 */       this.symbolTable.addConstantUtf8("ConstantValue");
/* 209 */       size += 8;
/*     */     } 
/*     */     
/* 212 */     if ((this.accessFlags & 0x1000) != 0 && this.symbolTable
/* 213 */       .getMajorVersion() < 49) {
/*     */       
/* 215 */       this.symbolTable.addConstantUtf8("Synthetic");
/* 216 */       size += 6;
/*     */     } 
/* 218 */     if (this.signatureIndex != 0) {
/*     */       
/* 220 */       this.symbolTable.addConstantUtf8("Signature");
/* 221 */       size += 8;
/*     */     } 
/*     */     
/* 224 */     if ((this.accessFlags & 0x20000) != 0) {
/*     */       
/* 226 */       this.symbolTable.addConstantUtf8("Deprecated");
/* 227 */       size += 6;
/*     */     } 
/* 229 */     if (this.lastRuntimeVisibleAnnotation != null) {
/* 230 */       size += this.lastRuntimeVisibleAnnotation
/* 231 */         .computeAnnotationsSize("RuntimeVisibleAnnotations");
/*     */     }
/*     */     
/* 234 */     if (this.lastRuntimeInvisibleAnnotation != null) {
/* 235 */       size += this.lastRuntimeInvisibleAnnotation
/* 236 */         .computeAnnotationsSize("RuntimeInvisibleAnnotations");
/*     */     }
/*     */     
/* 239 */     if (this.lastRuntimeVisibleTypeAnnotation != null) {
/* 240 */       size += this.lastRuntimeVisibleTypeAnnotation
/* 241 */         .computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
/*     */     }
/*     */     
/* 244 */     if (this.lastRuntimeInvisibleTypeAnnotation != null) {
/* 245 */       size += this.lastRuntimeInvisibleTypeAnnotation
/* 246 */         .computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
/*     */     }
/*     */     
/* 249 */     if (this.firstAttribute != null) {
/* 250 */       size += this.firstAttribute.computeAttributesSize(this.symbolTable);
/*     */     }
/* 252 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void putFieldInfo(ByteVector output) {
/* 262 */     boolean useSyntheticAttribute = (this.symbolTable.getMajorVersion() < 49);
/*     */     
/* 264 */     int mask = useSyntheticAttribute ? 4096 : 0;
/* 265 */     output.putShort(this.accessFlags & (mask ^ 0xFFFFFFFF)).putShort(this.nameIndex).putShort(this.descriptorIndex);
/*     */ 
/*     */     
/* 268 */     int attributesCount = 0;
/* 269 */     if (this.constantValueIndex != 0) {
/* 270 */       attributesCount++;
/*     */     }
/* 272 */     if ((this.accessFlags & 0x1000) != 0 && useSyntheticAttribute) {
/* 273 */       attributesCount++;
/*     */     }
/* 275 */     if (this.signatureIndex != 0) {
/* 276 */       attributesCount++;
/*     */     }
/* 278 */     if ((this.accessFlags & 0x20000) != 0) {
/* 279 */       attributesCount++;
/*     */     }
/* 281 */     if (this.lastRuntimeVisibleAnnotation != null) {
/* 282 */       attributesCount++;
/*     */     }
/* 284 */     if (this.lastRuntimeInvisibleAnnotation != null) {
/* 285 */       attributesCount++;
/*     */     }
/* 287 */     if (this.lastRuntimeVisibleTypeAnnotation != null) {
/* 288 */       attributesCount++;
/*     */     }
/* 290 */     if (this.lastRuntimeInvisibleTypeAnnotation != null) {
/* 291 */       attributesCount++;
/*     */     }
/* 293 */     if (this.firstAttribute != null) {
/* 294 */       attributesCount += this.firstAttribute.getAttributeCount();
/*     */     }
/* 296 */     output.putShort(attributesCount);
/*     */ 
/*     */     
/* 299 */     if (this.constantValueIndex != 0) {
/* 300 */       output
/* 301 */         .putShort(this.symbolTable.addConstantUtf8("ConstantValue"))
/* 302 */         .putInt(2)
/* 303 */         .putShort(this.constantValueIndex);
/*     */     }
/* 305 */     if ((this.accessFlags & 0x1000) != 0 && useSyntheticAttribute) {
/* 306 */       output.putShort(this.symbolTable.addConstantUtf8("Synthetic")).putInt(0);
/*     */     }
/* 308 */     if (this.signatureIndex != 0) {
/* 309 */       output
/* 310 */         .putShort(this.symbolTable.addConstantUtf8("Signature"))
/* 311 */         .putInt(2)
/* 312 */         .putShort(this.signatureIndex);
/*     */     }
/* 314 */     if ((this.accessFlags & 0x20000) != 0) {
/* 315 */       output.putShort(this.symbolTable.addConstantUtf8("Deprecated")).putInt(0);
/*     */     }
/* 317 */     if (this.lastRuntimeVisibleAnnotation != null) {
/* 318 */       this.lastRuntimeVisibleAnnotation.putAnnotations(this.symbolTable
/* 319 */           .addConstantUtf8("RuntimeVisibleAnnotations"), output);
/*     */     }
/* 321 */     if (this.lastRuntimeInvisibleAnnotation != null) {
/* 322 */       this.lastRuntimeInvisibleAnnotation.putAnnotations(this.symbolTable
/* 323 */           .addConstantUtf8("RuntimeInvisibleAnnotations"), output);
/*     */     }
/* 325 */     if (this.lastRuntimeVisibleTypeAnnotation != null) {
/* 326 */       this.lastRuntimeVisibleTypeAnnotation.putAnnotations(this.symbolTable
/* 327 */           .addConstantUtf8("RuntimeVisibleTypeAnnotations"), output);
/*     */     }
/* 329 */     if (this.lastRuntimeInvisibleTypeAnnotation != null) {
/* 330 */       this.lastRuntimeInvisibleTypeAnnotation.putAnnotations(this.symbolTable
/* 331 */           .addConstantUtf8("RuntimeInvisibleTypeAnnotations"), output);
/*     */     }
/* 333 */     if (this.firstAttribute != null) {
/* 334 */       this.firstAttribute.putAttributes(this.symbolTable, output);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void collectAttributePrototypes(Attribute.Set attributePrototypes) {
/* 344 */     attributePrototypes.addAttributes(this.firstAttribute);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/FieldWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */