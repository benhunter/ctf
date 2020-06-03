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
/*     */ final class AnnotationWriter
/*     */   extends AnnotationVisitor
/*     */ {
/*     */   private final SymbolTable symbolTable;
/*     */   private final boolean useNamedValues;
/*     */   private final ByteVector annotation;
/*     */   private final int numElementValuePairsOffset;
/*     */   private int numElementValuePairs;
/*     */   private final AnnotationWriter previousAnnotation;
/*     */   private AnnotationWriter nextAnnotation;
/*     */   
/*     */   AnnotationWriter(SymbolTable symbolTable, boolean useNamedValues, ByteVector annotation, AnnotationWriter previousAnnotation) {
/* 115 */     super(458752);
/* 116 */     this.symbolTable = symbolTable;
/* 117 */     this.useNamedValues = useNamedValues;
/* 118 */     this.annotation = annotation;
/*     */     
/* 120 */     this.numElementValuePairsOffset = (annotation.length == 0) ? -1 : (annotation.length - 2);
/* 121 */     this.previousAnnotation = previousAnnotation;
/* 122 */     if (previousAnnotation != null) {
/* 123 */       previousAnnotation.nextAnnotation = this;
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
/*     */   AnnotationWriter(SymbolTable symbolTable, ByteVector annotation, AnnotationWriter previousAnnotation) {
/* 142 */     this(symbolTable, true, annotation, previousAnnotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(String name, Object value) {
/* 153 */     this.numElementValuePairs++;
/* 154 */     if (this.useNamedValues) {
/* 155 */       this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
/*     */     }
/* 157 */     if (value instanceof String) {
/* 158 */       this.annotation.put12(115, this.symbolTable.addConstantUtf8((String)value));
/* 159 */     } else if (value instanceof Byte) {
/* 160 */       this.annotation.put12(66, (this.symbolTable.addConstantInteger(((Byte)value).byteValue())).index);
/* 161 */     } else if (value instanceof Boolean) {
/* 162 */       int booleanValue = ((Boolean)value).booleanValue() ? 1 : 0;
/* 163 */       this.annotation.put12(90, (this.symbolTable.addConstantInteger(booleanValue)).index);
/* 164 */     } else if (value instanceof Character) {
/* 165 */       this.annotation.put12(67, (this.symbolTable.addConstantInteger(((Character)value).charValue())).index);
/* 166 */     } else if (value instanceof Short) {
/* 167 */       this.annotation.put12(83, (this.symbolTable.addConstantInteger(((Short)value).shortValue())).index);
/* 168 */     } else if (value instanceof Type) {
/* 169 */       this.annotation.put12(99, this.symbolTable.addConstantUtf8(((Type)value).getDescriptor()));
/* 170 */     } else if (value instanceof byte[]) {
/* 171 */       byte[] byteArray = (byte[])value;
/* 172 */       this.annotation.put12(91, byteArray.length);
/* 173 */       for (byte byteValue : byteArray) {
/* 174 */         this.annotation.put12(66, (this.symbolTable.addConstantInteger(byteValue)).index);
/*     */       }
/* 176 */     } else if (value instanceof boolean[]) {
/* 177 */       boolean[] booleanArray = (boolean[])value;
/* 178 */       this.annotation.put12(91, booleanArray.length);
/* 179 */       for (boolean booleanValue : booleanArray) {
/* 180 */         this.annotation.put12(90, (this.symbolTable.addConstantInteger(booleanValue ? 1 : 0)).index);
/*     */       }
/* 182 */     } else if (value instanceof short[]) {
/* 183 */       short[] shortArray = (short[])value;
/* 184 */       this.annotation.put12(91, shortArray.length);
/* 185 */       for (short shortValue : shortArray) {
/* 186 */         this.annotation.put12(83, (this.symbolTable.addConstantInteger(shortValue)).index);
/*     */       }
/* 188 */     } else if (value instanceof char[]) {
/* 189 */       char[] charArray = (char[])value;
/* 190 */       this.annotation.put12(91, charArray.length);
/* 191 */       for (char charValue : charArray) {
/* 192 */         this.annotation.put12(67, (this.symbolTable.addConstantInteger(charValue)).index);
/*     */       }
/* 194 */     } else if (value instanceof int[]) {
/* 195 */       int[] intArray = (int[])value;
/* 196 */       this.annotation.put12(91, intArray.length);
/* 197 */       for (int intValue : intArray) {
/* 198 */         this.annotation.put12(73, (this.symbolTable.addConstantInteger(intValue)).index);
/*     */       }
/* 200 */     } else if (value instanceof long[]) {
/* 201 */       long[] longArray = (long[])value;
/* 202 */       this.annotation.put12(91, longArray.length);
/* 203 */       for (long longValue : longArray) {
/* 204 */         this.annotation.put12(74, (this.symbolTable.addConstantLong(longValue)).index);
/*     */       }
/* 206 */     } else if (value instanceof float[]) {
/* 207 */       float[] floatArray = (float[])value;
/* 208 */       this.annotation.put12(91, floatArray.length);
/* 209 */       for (float floatValue : floatArray) {
/* 210 */         this.annotation.put12(70, (this.symbolTable.addConstantFloat(floatValue)).index);
/*     */       }
/* 212 */     } else if (value instanceof double[]) {
/* 213 */       double[] doubleArray = (double[])value;
/* 214 */       this.annotation.put12(91, doubleArray.length);
/* 215 */       for (double doubleValue : doubleArray) {
/* 216 */         this.annotation.put12(68, (this.symbolTable.addConstantDouble(doubleValue)).index);
/*     */       }
/*     */     } else {
/* 219 */       Symbol symbol = this.symbolTable.addConstant(value);
/* 220 */       this.annotation.put12(".s.IFJDCS".charAt(symbol.tag), symbol.index);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnum(String name, String descriptor, String value) {
/* 228 */     this.numElementValuePairs++;
/* 229 */     if (this.useNamedValues) {
/* 230 */       this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
/*     */     }
/* 232 */     this.annotation
/* 233 */       .put12(101, this.symbolTable.addConstantUtf8(descriptor))
/* 234 */       .putShort(this.symbolTable.addConstantUtf8(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String name, String descriptor) {
/* 241 */     this.numElementValuePairs++;
/* 242 */     if (this.useNamedValues) {
/* 243 */       this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
/*     */     }
/*     */     
/* 246 */     this.annotation.put12(64, this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
/* 247 */     return new AnnotationWriter(this.symbolTable, this.annotation, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitArray(String name) {
/* 254 */     this.numElementValuePairs++;
/* 255 */     if (this.useNamedValues) {
/* 256 */       this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     this.annotation.put12(91, 0);
/* 266 */     return new AnnotationWriter(this.symbolTable, false, this.annotation, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 271 */     if (this.numElementValuePairsOffset != -1) {
/* 272 */       byte[] data = this.annotation.data;
/* 273 */       data[this.numElementValuePairsOffset] = (byte)(this.numElementValuePairs >>> 8);
/* 274 */       data[this.numElementValuePairsOffset + 1] = (byte)this.numElementValuePairs;
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
/*     */   int computeAnnotationsSize(String attributeName) {
/* 293 */     if (attributeName != null) {
/* 294 */       this.symbolTable.addConstantUtf8(attributeName);
/*     */     }
/*     */     
/* 297 */     int attributeSize = 8;
/* 298 */     AnnotationWriter annotationWriter = this;
/* 299 */     while (annotationWriter != null) {
/* 300 */       attributeSize += annotationWriter.annotation.length;
/* 301 */       annotationWriter = annotationWriter.previousAnnotation;
/*     */     } 
/* 303 */     return attributeSize;
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
/*     */   void putAnnotations(int attributeNameIndex, ByteVector output) {
/* 316 */     int attributeLength = 2;
/* 317 */     int numAnnotations = 0;
/* 318 */     AnnotationWriter annotationWriter = this;
/* 319 */     AnnotationWriter firstAnnotation = null;
/* 320 */     while (annotationWriter != null) {
/*     */       
/* 322 */       annotationWriter.visitEnd();
/* 323 */       attributeLength += annotationWriter.annotation.length;
/* 324 */       numAnnotations++;
/* 325 */       firstAnnotation = annotationWriter;
/* 326 */       annotationWriter = annotationWriter.previousAnnotation;
/*     */     } 
/* 328 */     output.putShort(attributeNameIndex);
/* 329 */     output.putInt(attributeLength);
/* 330 */     output.putShort(numAnnotations);
/* 331 */     annotationWriter = firstAnnotation;
/* 332 */     while (annotationWriter != null) {
/* 333 */       output.putByteArray(annotationWriter.annotation.data, 0, annotationWriter.annotation.length);
/* 334 */       annotationWriter = annotationWriter.nextAnnotation;
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
/*     */   static int computeParameterAnnotationsSize(String attributeName, AnnotationWriter[] annotationWriters, int annotableParameterCount) {
/* 361 */     int attributeSize = 7 + 2 * annotableParameterCount;
/* 362 */     for (int i = 0; i < annotableParameterCount; i++) {
/* 363 */       AnnotationWriter annotationWriter = annotationWriters[i];
/* 364 */       attributeSize += (annotationWriter == null) ? 0 : (annotationWriter
/* 365 */         .computeAnnotationsSize(attributeName) - 8);
/*     */     } 
/* 367 */     return attributeSize;
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
/*     */   static void putParameterAnnotations(int attributeNameIndex, AnnotationWriter[] annotationWriters, int annotableParameterCount, ByteVector output) {
/* 389 */     int attributeLength = 1 + 2 * annotableParameterCount; int i;
/* 390 */     for (i = 0; i < annotableParameterCount; i++) {
/* 391 */       AnnotationWriter annotationWriter = annotationWriters[i];
/* 392 */       attributeLength += (annotationWriter == null) ? 0 : (annotationWriter
/* 393 */         .computeAnnotationsSize(null) - 8);
/*     */     } 
/* 395 */     output.putShort(attributeNameIndex);
/* 396 */     output.putInt(attributeLength);
/* 397 */     output.putByte(annotableParameterCount);
/* 398 */     for (i = 0; i < annotableParameterCount; i++) {
/* 399 */       AnnotationWriter annotationWriter = annotationWriters[i];
/* 400 */       AnnotationWriter firstAnnotation = null;
/* 401 */       int numAnnotations = 0;
/* 402 */       while (annotationWriter != null) {
/*     */         
/* 404 */         annotationWriter.visitEnd();
/* 405 */         numAnnotations++;
/* 406 */         firstAnnotation = annotationWriter;
/* 407 */         annotationWriter = annotationWriter.previousAnnotation;
/*     */       } 
/* 409 */       output.putShort(numAnnotations);
/* 410 */       annotationWriter = firstAnnotation;
/* 411 */       while (annotationWriter != null) {
/* 412 */         output.putByteArray(annotationWriter.annotation.data, 0, annotationWriter.annotation.length);
/*     */         
/* 414 */         annotationWriter = annotationWriter.nextAnnotation;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/AnnotationWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */