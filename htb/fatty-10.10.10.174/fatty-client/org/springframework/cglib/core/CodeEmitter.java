/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CodeEmitter
/*     */   extends LocalVariablesSorter
/*     */ {
/*  27 */   private static final Signature BOOLEAN_VALUE = TypeUtils.parseSignature("boolean booleanValue()");
/*     */   
/*  29 */   private static final Signature CHAR_VALUE = TypeUtils.parseSignature("char charValue()");
/*     */   
/*  31 */   private static final Signature LONG_VALUE = TypeUtils.parseSignature("long longValue()");
/*     */   
/*  33 */   private static final Signature DOUBLE_VALUE = TypeUtils.parseSignature("double doubleValue()");
/*     */   
/*  35 */   private static final Signature FLOAT_VALUE = TypeUtils.parseSignature("float floatValue()");
/*     */   
/*  37 */   private static final Signature INT_VALUE = TypeUtils.parseSignature("int intValue()");
/*     */   
/*  39 */   private static final Signature CSTRUCT_NULL = TypeUtils.parseConstructor("");
/*     */   
/*  41 */   private static final Signature CSTRUCT_STRING = TypeUtils.parseConstructor("String");
/*     */   
/*     */   public static final int ADD = 96;
/*     */   
/*     */   public static final int MUL = 104;
/*     */   
/*     */   public static final int XOR = 130;
/*     */   public static final int USHR = 124;
/*     */   public static final int SUB = 100;
/*     */   public static final int DIV = 108;
/*     */   public static final int NEG = 116;
/*     */   public static final int REM = 112;
/*     */   public static final int AND = 126;
/*     */   public static final int OR = 128;
/*     */   public static final int GT = 157;
/*     */   public static final int LT = 155;
/*     */   public static final int GE = 156;
/*     */   public static final int LE = 158;
/*     */   public static final int NE = 154;
/*     */   public static final int EQ = 153;
/*     */   private ClassEmitter ce;
/*     */   private State state;
/*     */   
/*     */   private static class State
/*     */     extends MethodInfo
/*     */   {
/*     */     ClassInfo classInfo;
/*     */     int access;
/*     */     Signature sig;
/*     */     Type[] argumentTypes;
/*     */     int localOffset;
/*     */     Type[] exceptionTypes;
/*     */     
/*     */     State(ClassInfo classInfo, int access, Signature sig, Type[] exceptionTypes) {
/*  75 */       this.classInfo = classInfo;
/*  76 */       this.access = access;
/*  77 */       this.sig = sig;
/*  78 */       this.exceptionTypes = exceptionTypes;
/*  79 */       this.localOffset = TypeUtils.isStatic(access) ? 0 : 1;
/*  80 */       this.argumentTypes = sig.getArgumentTypes();
/*     */     }
/*     */     
/*     */     public ClassInfo getClassInfo() {
/*  84 */       return this.classInfo;
/*     */     }
/*     */     
/*     */     public int getModifiers() {
/*  88 */       return this.access;
/*     */     }
/*     */     
/*     */     public Signature getSignature() {
/*  92 */       return this.sig;
/*     */     }
/*     */     
/*     */     public Type[] getExceptionTypes() {
/*  96 */       return this.exceptionTypes;
/*     */     }
/*     */ 
/*     */     
/*     */     public Attribute getAttribute() {
/* 101 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   CodeEmitter(ClassEmitter ce, MethodVisitor mv, int access, Signature sig, Type[] exceptionTypes) {
/* 106 */     super(access, sig.getDescriptor(), mv);
/* 107 */     this.ce = ce;
/* 108 */     this.state = new State(ce.getClassInfo(), access, sig, exceptionTypes);
/*     */   }
/*     */   
/*     */   public CodeEmitter(CodeEmitter wrap) {
/* 112 */     super(wrap);
/* 113 */     this.ce = wrap.ce;
/* 114 */     this.state = wrap.state;
/*     */   }
/*     */   
/*     */   public boolean isStaticHook() {
/* 118 */     return false;
/*     */   }
/*     */   
/*     */   public Signature getSignature() {
/* 122 */     return this.state.sig;
/*     */   }
/*     */   
/*     */   public Type getReturnType() {
/* 126 */     return this.state.sig.getReturnType();
/*     */   }
/*     */   
/*     */   public MethodInfo getMethodInfo() {
/* 130 */     return this.state;
/*     */   }
/*     */   
/*     */   public ClassEmitter getClassEmitter() {
/* 134 */     return this.ce;
/*     */   }
/*     */   
/*     */   public void end_method() {
/* 138 */     visitMaxs(0, 0);
/*     */   }
/*     */   
/*     */   public Block begin_block() {
/* 142 */     return new Block(this);
/*     */   }
/*     */   
/*     */   public void catch_exception(Block block, Type exception) {
/* 146 */     if (block.getEnd() == null) {
/* 147 */       throw new IllegalStateException("end of block is unset");
/*     */     }
/* 149 */     this.mv.visitTryCatchBlock(block.getStart(), block
/* 150 */         .getEnd(), 
/* 151 */         mark(), exception
/* 152 */         .getInternalName());
/*     */   }
/*     */   
/* 155 */   public void goTo(Label label) { this.mv.visitJumpInsn(167, label); }
/* 156 */   public void ifnull(Label label) { this.mv.visitJumpInsn(198, label); } public void ifnonnull(Label label) {
/* 157 */     this.mv.visitJumpInsn(199, label);
/*     */   }
/*     */   public void if_jump(int mode, Label label) {
/* 160 */     this.mv.visitJumpInsn(mode, label);
/*     */   }
/*     */   
/*     */   public void if_icmp(int mode, Label label) {
/* 164 */     if_cmp(Type.INT_TYPE, mode, label);
/*     */   }
/*     */   
/*     */   public void if_cmp(Type type, int mode, Label label) {
/* 168 */     int intOp = -1;
/* 169 */     int jumpmode = mode;
/* 170 */     switch (mode) { case 156:
/* 171 */         jumpmode = 155; break;
/* 172 */       case 158: jumpmode = 157; break; }
/*     */     
/* 174 */     switch (type.getSort()) {
/*     */       case 7:
/* 176 */         this.mv.visitInsn(148);
/*     */         break;
/*     */       case 8:
/* 179 */         this.mv.visitInsn(152);
/*     */         break;
/*     */       case 6:
/* 182 */         this.mv.visitInsn(150);
/*     */         break;
/*     */       case 9:
/*     */       case 10:
/* 186 */         switch (mode) {
/*     */           case 153:
/* 188 */             this.mv.visitJumpInsn(165, label);
/*     */             return;
/*     */           case 154:
/* 191 */             this.mv.visitJumpInsn(166, label);
/*     */             return;
/*     */         } 
/* 194 */         throw new IllegalArgumentException("Bad comparison for type " + type);
/*     */       default:
/* 196 */         switch (mode) { case 153:
/* 197 */             intOp = 159; break;
/* 198 */           case 154: intOp = 160; break;
/* 199 */           case 156: swap();
/* 200 */           case 155: intOp = 161; break;
/* 201 */           case 158: swap();
/* 202 */           case 157: intOp = 163; break; }
/*     */         
/* 204 */         this.mv.visitJumpInsn(intOp, label);
/*     */         return;
/*     */     } 
/* 207 */     if_jump(jumpmode, label);
/*     */   }
/*     */   
/* 210 */   public void pop() { this.mv.visitInsn(87); }
/* 211 */   public void pop2() { this.mv.visitInsn(88); }
/* 212 */   public void dup() { this.mv.visitInsn(89); }
/* 213 */   public void dup2() { this.mv.visitInsn(92); }
/* 214 */   public void dup_x1() { this.mv.visitInsn(90); }
/* 215 */   public void dup_x2() { this.mv.visitInsn(91); }
/* 216 */   public void dup2_x1() { this.mv.visitInsn(93); }
/* 217 */   public void dup2_x2() { this.mv.visitInsn(94); }
/* 218 */   public void swap() { this.mv.visitInsn(95); } public void aconst_null() {
/* 219 */     this.mv.visitInsn(1);
/*     */   }
/*     */   public void swap(Type prev, Type type) {
/* 222 */     if (type.getSize() == 1) {
/* 223 */       if (prev.getSize() == 1) {
/* 224 */         swap();
/*     */       } else {
/* 226 */         dup_x2();
/* 227 */         pop();
/*     */       }
/*     */     
/* 230 */     } else if (prev.getSize() == 1) {
/* 231 */       dup2_x1();
/* 232 */       pop2();
/*     */     } else {
/* 234 */       dup2_x2();
/* 235 */       pop2();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void monitorenter() {
/* 240 */     this.mv.visitInsn(194); } public void monitorexit() {
/* 241 */     this.mv.visitInsn(195);
/*     */   } public void math(int op, Type type) {
/* 243 */     this.mv.visitInsn(type.getOpcode(op));
/*     */   }
/* 245 */   public void array_load(Type type) { this.mv.visitInsn(type.getOpcode(46)); } public void array_store(Type type) {
/* 246 */     this.mv.visitInsn(type.getOpcode(79));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cast_numeric(Type from, Type to) {
/* 252 */     if (from != to) {
/* 253 */       if (from == Type.DOUBLE_TYPE) {
/* 254 */         if (to == Type.FLOAT_TYPE) {
/* 255 */           this.mv.visitInsn(144);
/* 256 */         } else if (to == Type.LONG_TYPE) {
/* 257 */           this.mv.visitInsn(143);
/*     */         } else {
/* 259 */           this.mv.visitInsn(142);
/* 260 */           cast_numeric(Type.INT_TYPE, to);
/*     */         } 
/* 262 */       } else if (from == Type.FLOAT_TYPE) {
/* 263 */         if (to == Type.DOUBLE_TYPE) {
/* 264 */           this.mv.visitInsn(141);
/* 265 */         } else if (to == Type.LONG_TYPE) {
/* 266 */           this.mv.visitInsn(140);
/*     */         } else {
/* 268 */           this.mv.visitInsn(139);
/* 269 */           cast_numeric(Type.INT_TYPE, to);
/*     */         } 
/* 271 */       } else if (from == Type.LONG_TYPE) {
/* 272 */         if (to == Type.DOUBLE_TYPE) {
/* 273 */           this.mv.visitInsn(138);
/* 274 */         } else if (to == Type.FLOAT_TYPE) {
/* 275 */           this.mv.visitInsn(137);
/*     */         } else {
/* 277 */           this.mv.visitInsn(136);
/* 278 */           cast_numeric(Type.INT_TYPE, to);
/*     */         }
/*     */       
/* 281 */       } else if (to == Type.BYTE_TYPE) {
/* 282 */         this.mv.visitInsn(145);
/* 283 */       } else if (to == Type.CHAR_TYPE) {
/* 284 */         this.mv.visitInsn(146);
/* 285 */       } else if (to == Type.DOUBLE_TYPE) {
/* 286 */         this.mv.visitInsn(135);
/* 287 */       } else if (to == Type.FLOAT_TYPE) {
/* 288 */         this.mv.visitInsn(134);
/* 289 */       } else if (to == Type.LONG_TYPE) {
/* 290 */         this.mv.visitInsn(133);
/* 291 */       } else if (to == Type.SHORT_TYPE) {
/* 292 */         this.mv.visitInsn(147);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(int i) {
/* 299 */     if (i < -1) {
/* 300 */       this.mv.visitLdcInsn(new Integer(i));
/* 301 */     } else if (i <= 5) {
/* 302 */       this.mv.visitInsn(TypeUtils.ICONST(i));
/* 303 */     } else if (i <= 127) {
/* 304 */       this.mv.visitIntInsn(16, i);
/* 305 */     } else if (i <= 32767) {
/* 306 */       this.mv.visitIntInsn(17, i);
/*     */     } else {
/* 308 */       this.mv.visitLdcInsn(new Integer(i));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void push(long value) {
/* 313 */     if (value == 0L || value == 1L) {
/* 314 */       this.mv.visitInsn(TypeUtils.LCONST(value));
/*     */     } else {
/* 316 */       this.mv.visitLdcInsn(new Long(value));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void push(float value) {
/* 321 */     if (value == 0.0F || value == 1.0F || value == 2.0F) {
/* 322 */       this.mv.visitInsn(TypeUtils.FCONST(value));
/*     */     } else {
/* 324 */       this.mv.visitLdcInsn(new Float(value));
/*     */     } 
/*     */   }
/*     */   public void push(double value) {
/* 328 */     if (value == 0.0D || value == 1.0D) {
/* 329 */       this.mv.visitInsn(TypeUtils.DCONST(value));
/*     */     } else {
/* 331 */       this.mv.visitLdcInsn(new Double(value));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void push(String value) {
/* 336 */     this.mv.visitLdcInsn(value);
/*     */   }
/*     */   
/*     */   public void newarray() {
/* 340 */     newarray(Constants.TYPE_OBJECT);
/*     */   }
/*     */   
/*     */   public void newarray(Type type) {
/* 344 */     if (TypeUtils.isPrimitive(type)) {
/* 345 */       this.mv.visitIntInsn(188, TypeUtils.NEWARRAY(type));
/*     */     } else {
/* 347 */       emit_type(189, type);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void arraylength() {
/* 352 */     this.mv.visitInsn(190);
/*     */   }
/*     */   
/*     */   public void load_this() {
/* 356 */     if (TypeUtils.isStatic(this.state.access)) {
/* 357 */       throw new IllegalStateException("no 'this' pointer within static method");
/*     */     }
/* 359 */     this.mv.visitVarInsn(25, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load_args() {
/* 366 */     load_args(0, this.state.argumentTypes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load_arg(int index) {
/* 374 */     load_local(this.state.argumentTypes[index], this.state.localOffset + 
/* 375 */         skipArgs(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public void load_args(int fromArg, int count) {
/* 380 */     int pos = this.state.localOffset + skipArgs(fromArg);
/* 381 */     for (int i = 0; i < count; i++) {
/* 382 */       Type t = this.state.argumentTypes[fromArg + i];
/* 383 */       load_local(t, pos);
/* 384 */       pos += t.getSize();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int skipArgs(int numArgs) {
/* 389 */     int amount = 0;
/* 390 */     for (int i = 0; i < numArgs; i++) {
/* 391 */       amount += this.state.argumentTypes[i].getSize();
/*     */     }
/* 393 */     return amount;
/*     */   }
/*     */ 
/*     */   
/*     */   private void load_local(Type t, int pos) {
/* 398 */     this.mv.visitVarInsn(t.getOpcode(21), pos);
/*     */   }
/*     */ 
/*     */   
/*     */   private void store_local(Type t, int pos) {
/* 403 */     this.mv.visitVarInsn(t.getOpcode(54), pos);
/*     */   }
/*     */   
/*     */   public void iinc(Local local, int amount) {
/* 407 */     this.mv.visitIincInsn(local.getIndex(), amount);
/*     */   }
/*     */   
/*     */   public void store_local(Local local) {
/* 411 */     store_local(local.getType(), local.getIndex());
/*     */   }
/*     */   
/*     */   public void load_local(Local local) {
/* 415 */     load_local(local.getType(), local.getIndex());
/*     */   }
/*     */   
/*     */   public void return_value() {
/* 419 */     this.mv.visitInsn(this.state.sig.getReturnType().getOpcode(172));
/*     */   }
/*     */   
/*     */   public void getfield(String name) {
/* 423 */     ClassEmitter.FieldInfo info = this.ce.getFieldInfo(name);
/* 424 */     int opcode = TypeUtils.isStatic(info.access) ? 178 : 180;
/* 425 */     emit_field(opcode, this.ce.getClassType(), name, info.type);
/*     */   }
/*     */   
/*     */   public void putfield(String name) {
/* 429 */     ClassEmitter.FieldInfo info = this.ce.getFieldInfo(name);
/* 430 */     int opcode = TypeUtils.isStatic(info.access) ? 179 : 181;
/* 431 */     emit_field(opcode, this.ce.getClassType(), name, info.type);
/*     */   }
/*     */   
/*     */   public void super_getfield(String name, Type type) {
/* 435 */     emit_field(180, this.ce.getSuperType(), name, type);
/*     */   }
/*     */   
/*     */   public void super_putfield(String name, Type type) {
/* 439 */     emit_field(181, this.ce.getSuperType(), name, type);
/*     */   }
/*     */   
/*     */   public void super_getstatic(String name, Type type) {
/* 443 */     emit_field(178, this.ce.getSuperType(), name, type);
/*     */   }
/*     */   
/*     */   public void super_putstatic(String name, Type type) {
/* 447 */     emit_field(179, this.ce.getSuperType(), name, type);
/*     */   }
/*     */   
/*     */   public void getfield(Type owner, String name, Type type) {
/* 451 */     emit_field(180, owner, name, type);
/*     */   }
/*     */   
/*     */   public void putfield(Type owner, String name, Type type) {
/* 455 */     emit_field(181, owner, name, type);
/*     */   }
/*     */   
/*     */   public void getstatic(Type owner, String name, Type type) {
/* 459 */     emit_field(178, owner, name, type);
/*     */   }
/*     */   
/*     */   public void putstatic(Type owner, String name, Type type) {
/* 463 */     emit_field(179, owner, name, type);
/*     */   }
/*     */ 
/*     */   
/*     */   void emit_field(int opcode, Type ctype, String name, Type ftype) {
/* 468 */     this.mv.visitFieldInsn(opcode, ctype
/* 469 */         .getInternalName(), name, ftype
/*     */         
/* 471 */         .getDescriptor());
/*     */   }
/*     */   
/*     */   public void super_invoke() {
/* 475 */     super_invoke(this.state.sig);
/*     */   }
/*     */   
/*     */   public void super_invoke(Signature sig) {
/* 479 */     emit_invoke(183, this.ce.getSuperType(), sig);
/*     */   }
/*     */   
/*     */   public void invoke_constructor(Type type) {
/* 483 */     invoke_constructor(type, CSTRUCT_NULL);
/*     */   }
/*     */   
/*     */   public void super_invoke_constructor() {
/* 487 */     invoke_constructor(this.ce.getSuperType());
/*     */   }
/*     */   
/*     */   public void invoke_constructor_this() {
/* 491 */     invoke_constructor(this.ce.getClassType());
/*     */   }
/*     */   
/*     */   private void emit_invoke(int opcode, Type type, Signature sig) {
/* 495 */     if (!sig.getName().equals("<init>") || opcode == 182 || opcode == 184);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 500 */     this.mv.visitMethodInsn(opcode, type
/* 501 */         .getInternalName(), sig
/* 502 */         .getName(), sig
/* 503 */         .getDescriptor(), (opcode == 185));
/*     */   }
/*     */ 
/*     */   
/*     */   public void invoke_interface(Type owner, Signature sig) {
/* 508 */     emit_invoke(185, owner, sig);
/*     */   }
/*     */   
/*     */   public void invoke_virtual(Type owner, Signature sig) {
/* 512 */     emit_invoke(182, owner, sig);
/*     */   }
/*     */   
/*     */   public void invoke_static(Type owner, Signature sig) {
/* 516 */     emit_invoke(184, owner, sig);
/*     */   }
/*     */   
/*     */   public void invoke_virtual_this(Signature sig) {
/* 520 */     invoke_virtual(this.ce.getClassType(), sig);
/*     */   }
/*     */   
/*     */   public void invoke_static_this(Signature sig) {
/* 524 */     invoke_static(this.ce.getClassType(), sig);
/*     */   }
/*     */   
/*     */   public void invoke_constructor(Type type, Signature sig) {
/* 528 */     emit_invoke(183, type, sig);
/*     */   }
/*     */   
/*     */   public void invoke_constructor_this(Signature sig) {
/* 532 */     invoke_constructor(this.ce.getClassType(), sig);
/*     */   }
/*     */   
/*     */   public void super_invoke_constructor(Signature sig) {
/* 536 */     invoke_constructor(this.ce.getSuperType(), sig);
/*     */   }
/*     */   
/*     */   public void new_instance_this() {
/* 540 */     new_instance(this.ce.getClassType());
/*     */   }
/*     */   
/*     */   public void new_instance(Type type) {
/* 544 */     emit_type(187, type);
/*     */   }
/*     */   
/*     */   private void emit_type(int opcode, Type type) {
/*     */     String desc;
/* 549 */     if (TypeUtils.isArray(type)) {
/* 550 */       desc = type.getDescriptor();
/*     */     } else {
/* 552 */       desc = type.getInternalName();
/*     */     } 
/* 554 */     this.mv.visitTypeInsn(opcode, desc);
/*     */   }
/*     */   
/*     */   public void aaload(int index) {
/* 558 */     push(index);
/* 559 */     aaload();
/*     */   }
/*     */   
/* 562 */   public void aaload() { this.mv.visitInsn(50); }
/* 563 */   public void aastore() { this.mv.visitInsn(83); } public void athrow() {
/* 564 */     this.mv.visitInsn(191);
/*     */   }
/*     */   public Label make_label() {
/* 567 */     return new Label();
/*     */   }
/*     */   
/*     */   public Local make_local() {
/* 571 */     return make_local(Constants.TYPE_OBJECT);
/*     */   }
/*     */   
/*     */   public Local make_local(Type type) {
/* 575 */     return new Local(newLocal(type.getSize()), type);
/*     */   }
/*     */   
/*     */   public void checkcast_this() {
/* 579 */     checkcast(this.ce.getClassType());
/*     */   }
/*     */   
/*     */   public void checkcast(Type type) {
/* 583 */     if (!type.equals(Constants.TYPE_OBJECT)) {
/* 584 */       emit_type(192, type);
/*     */     }
/*     */   }
/*     */   
/*     */   public void instance_of(Type type) {
/* 589 */     emit_type(193, type);
/*     */   }
/*     */   
/*     */   public void instance_of_this() {
/* 593 */     instance_of(this.ce.getClassType());
/*     */   }
/*     */   
/*     */   public void process_switch(int[] keys, ProcessSwitchCallback callback) {
/*     */     float density;
/* 598 */     if (keys.length == 0) {
/* 599 */       density = 0.0F;
/*     */     } else {
/* 601 */       density = keys.length / (keys[keys.length - 1] - keys[0] + 1);
/*     */     } 
/* 603 */     process_switch(keys, callback, (density >= 0.5F));
/*     */   }
/*     */   
/*     */   public void process_switch(int[] keys, ProcessSwitchCallback callback, boolean useTable) {
/* 607 */     if (!isSorted(keys))
/* 608 */       throw new IllegalArgumentException("keys to switch must be sorted ascending"); 
/* 609 */     Label def = make_label();
/* 610 */     Label end = make_label();
/*     */     
/*     */     try {
/* 613 */       if (keys.length > 0) {
/* 614 */         int len = keys.length;
/* 615 */         int min = keys[0];
/* 616 */         int max = keys[len - 1];
/* 617 */         int range = max - min + 1;
/*     */         
/* 619 */         if (useTable) {
/* 620 */           Label[] labels = new Label[range];
/* 621 */           Arrays.fill((Object[])labels, def); int i;
/* 622 */           for (i = 0; i < len; i++) {
/* 623 */             labels[keys[i] - min] = make_label();
/*     */           }
/* 625 */           this.mv.visitTableSwitchInsn(min, max, def, labels);
/* 626 */           for (i = 0; i < range; i++) {
/* 627 */             Label label = labels[i];
/* 628 */             if (label != def) {
/* 629 */               mark(label);
/* 630 */               callback.processCase(i + min, end);
/*     */             } 
/*     */           } 
/*     */         } else {
/* 634 */           Label[] labels = new Label[len]; int i;
/* 635 */           for (i = 0; i < len; i++) {
/* 636 */             labels[i] = make_label();
/*     */           }
/* 638 */           this.mv.visitLookupSwitchInsn(def, keys, labels);
/* 639 */           for (i = 0; i < len; i++) {
/* 640 */             mark(labels[i]);
/* 641 */             callback.processCase(keys[i], end);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 646 */       mark(def);
/* 647 */       callback.processDefault();
/* 648 */       mark(end);
/*     */     }
/* 650 */     catch (RuntimeException e) {
/* 651 */       throw e;
/* 652 */     } catch (Error e) {
/* 653 */       throw e;
/* 654 */     } catch (Exception e) {
/* 655 */       throw new CodeGenerationException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isSorted(int[] keys) {
/* 660 */     for (int i = 1; i < keys.length; i++) {
/* 661 */       if (keys[i] < keys[i - 1])
/* 662 */         return false; 
/*     */     } 
/* 664 */     return true;
/*     */   }
/*     */   
/*     */   public void mark(Label label) {
/* 668 */     this.mv.visitLabel(label);
/*     */   }
/*     */   
/*     */   Label mark() {
/* 672 */     Label label = make_label();
/* 673 */     this.mv.visitLabel(label);
/* 674 */     return label;
/*     */   }
/*     */   
/*     */   public void push(boolean value) {
/* 678 */     push(value ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void not() {
/* 685 */     push(1);
/* 686 */     math(130, Type.INT_TYPE);
/*     */   }
/*     */   
/*     */   public void throw_exception(Type type, String msg) {
/* 690 */     new_instance(type);
/* 691 */     dup();
/* 692 */     push(msg);
/* 693 */     invoke_constructor(type, CSTRUCT_STRING);
/* 694 */     athrow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void box(Type type) {
/* 705 */     if (TypeUtils.isPrimitive(type)) {
/* 706 */       if (type == Type.VOID_TYPE) {
/* 707 */         aconst_null();
/*     */       } else {
/* 709 */         Type boxed = TypeUtils.getBoxedType(type);
/* 710 */         new_instance(boxed);
/* 711 */         if (type.getSize() == 2) {
/*     */           
/* 713 */           dup_x2();
/* 714 */           dup_x2();
/* 715 */           pop();
/*     */         } else {
/*     */           
/* 718 */           dup_x1();
/* 719 */           swap();
/*     */         } 
/* 721 */         invoke_constructor(boxed, new Signature("<init>", Type.VOID_TYPE, new Type[] { type }));
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
/*     */   public void unbox(Type type) {
/* 734 */     Type t = Constants.TYPE_NUMBER;
/* 735 */     Signature sig = null;
/* 736 */     switch (type.getSort()) {
/*     */       case 0:
/*     */         return;
/*     */       case 2:
/* 740 */         t = Constants.TYPE_CHARACTER;
/* 741 */         sig = CHAR_VALUE;
/*     */         break;
/*     */       case 1:
/* 744 */         t = Constants.TYPE_BOOLEAN;
/* 745 */         sig = BOOLEAN_VALUE;
/*     */         break;
/*     */       case 8:
/* 748 */         sig = DOUBLE_VALUE;
/*     */         break;
/*     */       case 6:
/* 751 */         sig = FLOAT_VALUE;
/*     */         break;
/*     */       case 7:
/* 754 */         sig = LONG_VALUE;
/*     */         break;
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/* 759 */         sig = INT_VALUE;
/*     */         break;
/*     */     } 
/* 762 */     if (sig == null) {
/* 763 */       checkcast(type);
/*     */     } else {
/* 765 */       checkcast(t);
/* 766 */       invoke_virtual(t, sig);
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
/*     */   public void create_arg_array() {
/* 780 */     push(this.state.argumentTypes.length);
/* 781 */     newarray();
/* 782 */     for (int i = 0; i < this.state.argumentTypes.length; i++) {
/* 783 */       dup();
/* 784 */       push(i);
/* 785 */       load_arg(i);
/* 786 */       box(this.state.argumentTypes[i]);
/* 787 */       aastore();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void zero_or_null(Type type) {
/* 796 */     if (TypeUtils.isPrimitive(type)) {
/* 797 */       switch (type.getSort()) {
/*     */         case 8:
/* 799 */           push(0.0D);
/*     */           return;
/*     */         case 7:
/* 802 */           push(0L);
/*     */           return;
/*     */         case 6:
/* 805 */           push(0.0F);
/*     */           return;
/*     */         case 0:
/* 808 */           aconst_null(); break;
/*     */       } 
/* 810 */       push(0);
/*     */     } else {
/*     */       
/* 813 */       aconst_null();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unbox_or_zero(Type type) {
/* 822 */     if (TypeUtils.isPrimitive(type)) {
/* 823 */       if (type != Type.VOID_TYPE) {
/* 824 */         Label nonNull = make_label();
/* 825 */         Label end = make_label();
/* 826 */         dup();
/* 827 */         ifnonnull(nonNull);
/* 828 */         pop();
/* 829 */         zero_or_null(type);
/* 830 */         goTo(end);
/* 831 */         mark(nonNull);
/* 832 */         unbox(type);
/* 833 */         mark(end);
/*     */       } 
/*     */     } else {
/* 836 */       checkcast(type);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void visitMaxs(int maxStack, int maxLocals) {
/* 841 */     if (!TypeUtils.isAbstract(this.state.access)) {
/* 842 */       this.mv.visitMaxs(0, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void invoke(MethodInfo method, Type virtualType) {
/* 847 */     ClassInfo classInfo = method.getClassInfo();
/* 848 */     Type type = classInfo.getType();
/* 849 */     Signature sig = method.getSignature();
/* 850 */     if (sig.getName().equals("<init>")) {
/* 851 */       invoke_constructor(type, sig);
/* 852 */     } else if (TypeUtils.isInterface(classInfo.getModifiers())) {
/* 853 */       invoke_interface(type, sig);
/* 854 */     } else if (TypeUtils.isStatic(method.getModifiers())) {
/* 855 */       invoke_static(type, sig);
/*     */     } else {
/* 857 */       invoke_virtual(virtualType, sig);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void invoke(MethodInfo method) {
/* 862 */     invoke(method, method.getClassInfo().getType());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cglib/core/CodeEmitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */