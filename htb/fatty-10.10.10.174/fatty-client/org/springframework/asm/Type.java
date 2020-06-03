/*     */ package org.springframework.asm;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Type
/*     */ {
/*     */   public static final int VOID = 0;
/*     */   public static final int BOOLEAN = 1;
/*     */   public static final int CHAR = 2;
/*     */   public static final int BYTE = 3;
/*     */   public static final int SHORT = 4;
/*     */   public static final int INT = 5;
/*     */   public static final int FLOAT = 6;
/*     */   public static final int LONG = 7;
/*     */   public static final int DOUBLE = 8;
/*     */   public static final int ARRAY = 9;
/*     */   public static final int OBJECT = 10;
/*     */   public static final int METHOD = 11;
/*     */   private static final int INTERNAL = 12;
/*     */   private static final String PRIMITIVE_DESCRIPTORS = "VZCBSIFJD";
/*  85 */   public static final Type VOID_TYPE = new Type(0, "VZCBSIFJD", 0, 1);
/*     */ 
/*     */   
/*  88 */   public static final Type BOOLEAN_TYPE = new Type(1, "VZCBSIFJD", 1, 2);
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static final Type CHAR_TYPE = new Type(2, "VZCBSIFJD", 2, 3);
/*     */ 
/*     */   
/*  95 */   public static final Type BYTE_TYPE = new Type(3, "VZCBSIFJD", 3, 4);
/*     */ 
/*     */   
/*  98 */   public static final Type SHORT_TYPE = new Type(4, "VZCBSIFJD", 4, 5);
/*     */ 
/*     */   
/* 101 */   public static final Type INT_TYPE = new Type(5, "VZCBSIFJD", 5, 6);
/*     */ 
/*     */   
/* 104 */   public static final Type FLOAT_TYPE = new Type(6, "VZCBSIFJD", 6, 7);
/*     */ 
/*     */   
/* 107 */   public static final Type LONG_TYPE = new Type(7, "VZCBSIFJD", 7, 8);
/*     */ 
/*     */   
/* 110 */   public static final Type DOUBLE_TYPE = new Type(8, "VZCBSIFJD", 8, 9);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int sort;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String valueBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int valueBegin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int valueEnd;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Type(int sort, String valueBuffer, int valueBegin, int valueEnd) {
/* 160 */     this.sort = sort;
/* 161 */     this.valueBuffer = valueBuffer;
/* 162 */     this.valueBegin = valueBegin;
/* 163 */     this.valueEnd = valueEnd;
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
/*     */   public static Type getType(String typeDescriptor) {
/* 177 */     return getTypeInternal(typeDescriptor, 0, typeDescriptor.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getType(Class<?> clazz) {
/* 187 */     if (clazz.isPrimitive()) {
/* 188 */       if (clazz == int.class)
/* 189 */         return INT_TYPE; 
/* 190 */       if (clazz == void.class)
/* 191 */         return VOID_TYPE; 
/* 192 */       if (clazz == boolean.class)
/* 193 */         return BOOLEAN_TYPE; 
/* 194 */       if (clazz == byte.class)
/* 195 */         return BYTE_TYPE; 
/* 196 */       if (clazz == char.class)
/* 197 */         return CHAR_TYPE; 
/* 198 */       if (clazz == short.class)
/* 199 */         return SHORT_TYPE; 
/* 200 */       if (clazz == double.class)
/* 201 */         return DOUBLE_TYPE; 
/* 202 */       if (clazz == float.class)
/* 203 */         return FLOAT_TYPE; 
/* 204 */       if (clazz == long.class) {
/* 205 */         return LONG_TYPE;
/*     */       }
/* 207 */       throw new AssertionError();
/*     */     } 
/*     */     
/* 210 */     return getType(getDescriptor(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getType(Constructor<?> constructor) {
/* 221 */     return getType(getConstructorDescriptor(constructor));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getType(Method method) {
/* 231 */     return getType(getMethodDescriptor(method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getElementType() {
/* 241 */     int numDimensions = getDimensions();
/* 242 */     return getTypeInternal(this.valueBuffer, this.valueBegin + numDimensions, this.valueEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getObjectType(String internalName) {
/* 252 */     return new Type(
/* 253 */         (internalName.charAt(0) == '[') ? 9 : 12, internalName, 0, internalName.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getMethodType(String methodDescriptor) {
/* 264 */     return new Type(11, methodDescriptor, 0, methodDescriptor.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getMethodType(Type returnType, Type... argumentTypes) {
/* 275 */     return getType(getMethodDescriptor(returnType, argumentTypes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] getArgumentTypes() {
/* 285 */     return getArgumentTypes(getDescriptor());
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
/*     */   public static Type[] getArgumentTypes(String methodDescriptor) {
/* 298 */     int numArgumentTypes = 0;
/*     */     
/* 300 */     int currentOffset = 1;
/*     */     
/* 302 */     while (methodDescriptor.charAt(currentOffset) != ')') {
/* 303 */       while (methodDescriptor.charAt(currentOffset) == '[') {
/* 304 */         currentOffset++;
/*     */       }
/* 306 */       if (methodDescriptor.charAt(currentOffset++) == 'L')
/*     */       {
/* 308 */         currentOffset = methodDescriptor.indexOf(';', currentOffset) + 1;
/*     */       }
/* 310 */       numArgumentTypes++;
/*     */     } 
/*     */ 
/*     */     
/* 314 */     Type[] argumentTypes = new Type[numArgumentTypes];
/*     */     
/* 316 */     currentOffset = 1;
/*     */     
/* 318 */     int currentArgumentTypeIndex = 0;
/* 319 */     while (methodDescriptor.charAt(currentOffset) != ')') {
/* 320 */       int currentArgumentTypeOffset = currentOffset;
/* 321 */       while (methodDescriptor.charAt(currentOffset) == '[') {
/* 322 */         currentOffset++;
/*     */       }
/* 324 */       if (methodDescriptor.charAt(currentOffset++) == 'L')
/*     */       {
/* 326 */         currentOffset = methodDescriptor.indexOf(';', currentOffset) + 1;
/*     */       }
/* 328 */       argumentTypes[currentArgumentTypeIndex++] = 
/* 329 */         getTypeInternal(methodDescriptor, currentArgumentTypeOffset, currentOffset);
/*     */     } 
/* 331 */     return argumentTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type[] getArgumentTypes(Method method) {
/* 341 */     Class<?>[] classes = method.getParameterTypes();
/* 342 */     Type[] types = new Type[classes.length];
/* 343 */     for (int i = classes.length - 1; i >= 0; i--) {
/* 344 */       types[i] = getType(classes[i]);
/*     */     }
/* 346 */     return types;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getReturnType() {
/* 356 */     return getReturnType(getDescriptor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getReturnType(String methodDescriptor) {
/* 367 */     int currentOffset = 1;
/*     */     
/* 369 */     while (methodDescriptor.charAt(currentOffset) != ')') {
/* 370 */       while (methodDescriptor.charAt(currentOffset) == '[') {
/* 371 */         currentOffset++;
/*     */       }
/* 373 */       if (methodDescriptor.charAt(currentOffset++) == 'L')
/*     */       {
/* 375 */         currentOffset = methodDescriptor.indexOf(';', currentOffset) + 1;
/*     */       }
/*     */     } 
/* 378 */     return getTypeInternal(methodDescriptor, currentOffset + 1, methodDescriptor.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getReturnType(Method method) {
/* 388 */     return getType(method.getReturnType());
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
/*     */   private static Type getTypeInternal(String descriptorBuffer, int descriptorBegin, int descriptorEnd) {
/* 403 */     switch (descriptorBuffer.charAt(descriptorBegin)) {
/*     */       case 'V':
/* 405 */         return VOID_TYPE;
/*     */       case 'Z':
/* 407 */         return BOOLEAN_TYPE;
/*     */       case 'C':
/* 409 */         return CHAR_TYPE;
/*     */       case 'B':
/* 411 */         return BYTE_TYPE;
/*     */       case 'S':
/* 413 */         return SHORT_TYPE;
/*     */       case 'I':
/* 415 */         return INT_TYPE;
/*     */       case 'F':
/* 417 */         return FLOAT_TYPE;
/*     */       case 'J':
/* 419 */         return LONG_TYPE;
/*     */       case 'D':
/* 421 */         return DOUBLE_TYPE;
/*     */       case '[':
/* 423 */         return new Type(9, descriptorBuffer, descriptorBegin, descriptorEnd);
/*     */       case 'L':
/* 425 */         return new Type(10, descriptorBuffer, descriptorBegin + 1, descriptorEnd - 1);
/*     */       case '(':
/* 427 */         return new Type(11, descriptorBuffer, descriptorBegin, descriptorEnd);
/*     */     } 
/* 429 */     throw new IllegalArgumentException();
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
/*     */   public String getClassName() {
/*     */     StringBuilder stringBuilder;
/*     */     int i;
/* 444 */     switch (this.sort) {
/*     */       case 0:
/* 446 */         return "void";
/*     */       case 1:
/* 448 */         return "boolean";
/*     */       case 2:
/* 450 */         return "char";
/*     */       case 3:
/* 452 */         return "byte";
/*     */       case 4:
/* 454 */         return "short";
/*     */       case 5:
/* 456 */         return "int";
/*     */       case 6:
/* 458 */         return "float";
/*     */       case 7:
/* 460 */         return "long";
/*     */       case 8:
/* 462 */         return "double";
/*     */       case 9:
/* 464 */         stringBuilder = new StringBuilder(getElementType().getClassName());
/* 465 */         for (i = getDimensions(); i > 0; i--) {
/* 466 */           stringBuilder.append("[]");
/*     */         }
/* 468 */         return stringBuilder.toString();
/*     */       case 10:
/*     */       case 12:
/* 471 */         return this.valueBuffer.substring(this.valueBegin, this.valueEnd).replace('/', '.');
/*     */     } 
/* 473 */     throw new AssertionError();
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
/*     */   public String getInternalName() {
/* 485 */     return this.valueBuffer.substring(this.valueBegin, this.valueEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getInternalName(Class<?> clazz) {
/* 496 */     return clazz.getName().replace('.', '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptor() {
/* 505 */     if (this.sort == 10)
/* 506 */       return this.valueBuffer.substring(this.valueBegin - 1, this.valueEnd + 1); 
/* 507 */     if (this.sort == 12) {
/* 508 */       return (new StringBuilder())
/* 509 */         .append('L')
/* 510 */         .append(this.valueBuffer, this.valueBegin, this.valueEnd)
/* 511 */         .append(';')
/* 512 */         .toString();
/*     */     }
/* 514 */     return this.valueBuffer.substring(this.valueBegin, this.valueEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDescriptor(Class<?> clazz) {
/* 525 */     StringBuilder stringBuilder = new StringBuilder();
/* 526 */     appendDescriptor(clazz, stringBuilder);
/* 527 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getConstructorDescriptor(Constructor<?> constructor) {
/* 537 */     StringBuilder stringBuilder = new StringBuilder();
/* 538 */     stringBuilder.append('(');
/* 539 */     Class<?>[] parameters = constructor.getParameterTypes();
/* 540 */     for (Class<?> parameter : parameters) {
/* 541 */       appendDescriptor(parameter, stringBuilder);
/*     */     }
/* 543 */     return stringBuilder.append(")V").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMethodDescriptor(Type returnType, Type... argumentTypes) {
/* 554 */     StringBuilder stringBuilder = new StringBuilder();
/* 555 */     stringBuilder.append('(');
/* 556 */     for (Type argumentType : argumentTypes) {
/* 557 */       argumentType.appendDescriptor(stringBuilder);
/*     */     }
/* 559 */     stringBuilder.append(')');
/* 560 */     returnType.appendDescriptor(stringBuilder);
/* 561 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMethodDescriptor(Method method) {
/* 571 */     StringBuilder stringBuilder = new StringBuilder();
/* 572 */     stringBuilder.append('(');
/* 573 */     Class<?>[] parameters = method.getParameterTypes();
/* 574 */     for (Class<?> parameter : parameters) {
/* 575 */       appendDescriptor(parameter, stringBuilder);
/*     */     }
/* 577 */     stringBuilder.append(')');
/* 578 */     appendDescriptor(method.getReturnType(), stringBuilder);
/* 579 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendDescriptor(StringBuilder stringBuilder) {
/* 588 */     if (this.sort == 10) {
/* 589 */       stringBuilder.append(this.valueBuffer, this.valueBegin - 1, this.valueEnd + 1);
/* 590 */     } else if (this.sort == 12) {
/* 591 */       stringBuilder.append('L').append(this.valueBuffer, this.valueBegin, this.valueEnd).append(';');
/*     */     } else {
/* 593 */       stringBuilder.append(this.valueBuffer, this.valueBegin, this.valueEnd);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendDescriptor(Class<?> clazz, StringBuilder stringBuilder) {
/* 604 */     Class<?> currentClass = clazz;
/* 605 */     while (currentClass.isArray()) {
/* 606 */       stringBuilder.append('[');
/* 607 */       currentClass = currentClass.getComponentType();
/*     */     } 
/* 609 */     if (currentClass.isPrimitive()) {
/*     */       char descriptor;
/* 611 */       if (currentClass == int.class) {
/* 612 */         descriptor = 'I';
/* 613 */       } else if (currentClass == void.class) {
/* 614 */         descriptor = 'V';
/* 615 */       } else if (currentClass == boolean.class) {
/* 616 */         descriptor = 'Z';
/* 617 */       } else if (currentClass == byte.class) {
/* 618 */         descriptor = 'B';
/* 619 */       } else if (currentClass == char.class) {
/* 620 */         descriptor = 'C';
/* 621 */       } else if (currentClass == short.class) {
/* 622 */         descriptor = 'S';
/* 623 */       } else if (currentClass == double.class) {
/* 624 */         descriptor = 'D';
/* 625 */       } else if (currentClass == float.class) {
/* 626 */         descriptor = 'F';
/* 627 */       } else if (currentClass == long.class) {
/* 628 */         descriptor = 'J';
/*     */       } else {
/* 630 */         throw new AssertionError();
/*     */       } 
/* 632 */       stringBuilder.append(descriptor);
/*     */     } else {
/* 634 */       stringBuilder.append('L');
/* 635 */       String name = currentClass.getName();
/* 636 */       int nameLength = name.length();
/* 637 */       for (int i = 0; i < nameLength; i++) {
/* 638 */         char car = name.charAt(i);
/* 639 */         stringBuilder.append((car == '.') ? 47 : car);
/*     */       } 
/* 641 */       stringBuilder.append(';');
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
/*     */   public int getSort() {
/* 657 */     return (this.sort == 12) ? 10 : this.sort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDimensions() {
/* 667 */     int numDimensions = 1;
/* 668 */     while (this.valueBuffer.charAt(this.valueBegin + numDimensions) == '[') {
/* 669 */       numDimensions++;
/*     */     }
/* 671 */     return numDimensions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 681 */     switch (this.sort) {
/*     */       case 0:
/* 683 */         return 0;
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 9:
/*     */       case 10:
/*     */       case 12:
/* 693 */         return 1;
/*     */       case 7:
/*     */       case 8:
/* 696 */         return 2;
/*     */     } 
/* 698 */     throw new AssertionError();
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
/*     */   public int getArgumentsAndReturnSizes() {
/* 712 */     return getArgumentsAndReturnSizes(getDescriptor());
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
/*     */   public static int getArgumentsAndReturnSizes(String methodDescriptor) {
/* 725 */     int argumentsSize = 1;
/*     */     
/* 727 */     int currentOffset = 1;
/* 728 */     int currentChar = methodDescriptor.charAt(currentOffset);
/*     */     
/* 730 */     while (currentChar != 41) {
/* 731 */       if (currentChar == 74 || currentChar == 68) {
/* 732 */         currentOffset++;
/* 733 */         argumentsSize += 2;
/*     */       } else {
/* 735 */         while (methodDescriptor.charAt(currentOffset) == '[') {
/* 736 */           currentOffset++;
/*     */         }
/* 738 */         if (methodDescriptor.charAt(currentOffset++) == 'L')
/*     */         {
/* 740 */           currentOffset = methodDescriptor.indexOf(';', currentOffset) + 1;
/*     */         }
/* 742 */         argumentsSize++;
/*     */       } 
/* 744 */       currentChar = methodDescriptor.charAt(currentOffset);
/*     */     } 
/* 746 */     currentChar = methodDescriptor.charAt(currentOffset + 1);
/* 747 */     if (currentChar == 86) {
/* 748 */       return argumentsSize << 2;
/*     */     }
/* 750 */     int returnSize = (currentChar == 74 || currentChar == 68) ? 2 : 1;
/* 751 */     return argumentsSize << 2 | returnSize;
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
/*     */   public int getOpcode(int opcode) {
/* 767 */     if (opcode == 46 || opcode == 79) {
/* 768 */       switch (this.sort) {
/*     */         case 1:
/*     */         case 3:
/* 771 */           return opcode + 5;
/*     */         case 2:
/* 773 */           return opcode + 6;
/*     */         case 4:
/* 775 */           return opcode + 7;
/*     */         case 5:
/* 777 */           return opcode;
/*     */         case 6:
/* 779 */           return opcode + 2;
/*     */         case 7:
/* 781 */           return opcode + 1;
/*     */         case 8:
/* 783 */           return opcode + 3;
/*     */         case 9:
/*     */         case 10:
/*     */         case 12:
/* 787 */           return opcode + 4;
/*     */         case 0:
/*     */         case 11:
/* 790 */           throw new UnsupportedOperationException();
/*     */       } 
/* 792 */       throw new AssertionError();
/*     */     } 
/*     */     
/* 795 */     switch (this.sort) {
/*     */       case 0:
/* 797 */         if (opcode != 172) {
/* 798 */           throw new UnsupportedOperationException();
/*     */         }
/* 800 */         return 177;
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/* 806 */         return opcode;
/*     */       case 6:
/* 808 */         return opcode + 2;
/*     */       case 7:
/* 810 */         return opcode + 1;
/*     */       case 8:
/* 812 */         return opcode + 3;
/*     */       case 9:
/*     */       case 10:
/*     */       case 12:
/* 816 */         if (opcode != 21 && opcode != 54 && opcode != 172) {
/* 817 */           throw new UnsupportedOperationException();
/*     */         }
/* 819 */         return opcode + 4;
/*     */       case 11:
/* 821 */         throw new UnsupportedOperationException();
/*     */     } 
/* 823 */     throw new AssertionError();
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
/*     */   public boolean equals(Object object) {
/* 840 */     if (this == object) {
/* 841 */       return true;
/*     */     }
/* 843 */     if (!(object instanceof Type)) {
/* 844 */       return false;
/*     */     }
/* 846 */     Type other = (Type)object;
/* 847 */     if (((this.sort == 12) ? true : this.sort) != ((other.sort == 12) ? true : other.sort)) {
/* 848 */       return false;
/*     */     }
/* 850 */     int begin = this.valueBegin;
/* 851 */     int end = this.valueEnd;
/* 852 */     int otherBegin = other.valueBegin;
/* 853 */     int otherEnd = other.valueEnd;
/*     */     
/* 855 */     if (end - begin != otherEnd - otherBegin) {
/* 856 */       return false;
/*     */     }
/* 858 */     for (int i = begin, j = otherBegin; i < end; i++, j++) {
/* 859 */       if (this.valueBuffer.charAt(i) != other.valueBuffer.charAt(j)) {
/* 860 */         return false;
/*     */       }
/*     */     } 
/* 863 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 873 */     int hashCode = 13 * ((this.sort == 12) ? 10 : this.sort);
/* 874 */     if (this.sort >= 9) {
/* 875 */       for (int i = this.valueBegin, end = this.valueEnd; i < end; i++) {
/* 876 */         hashCode = 17 * (hashCode + this.valueBuffer.charAt(i));
/*     */       }
/*     */     }
/* 879 */     return hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 889 */     return getDescriptor();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */