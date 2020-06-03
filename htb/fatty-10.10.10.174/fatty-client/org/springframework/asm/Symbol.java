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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class Symbol
/*     */ {
/*     */   static final int CONSTANT_CLASS_TAG = 7;
/*     */   static final int CONSTANT_FIELDREF_TAG = 9;
/*     */   static final int CONSTANT_METHODREF_TAG = 10;
/*     */   static final int CONSTANT_INTERFACE_METHODREF_TAG = 11;
/*     */   static final int CONSTANT_STRING_TAG = 8;
/*     */   static final int CONSTANT_INTEGER_TAG = 3;
/*     */   static final int CONSTANT_FLOAT_TAG = 4;
/*     */   static final int CONSTANT_LONG_TAG = 5;
/*     */   static final int CONSTANT_DOUBLE_TAG = 6;
/*     */   static final int CONSTANT_NAME_AND_TYPE_TAG = 12;
/*     */   static final int CONSTANT_UTF8_TAG = 1;
/*     */   static final int CONSTANT_METHOD_HANDLE_TAG = 15;
/*     */   static final int CONSTANT_METHOD_TYPE_TAG = 16;
/*     */   static final int CONSTANT_DYNAMIC_TAG = 17;
/*     */   static final int CONSTANT_INVOKE_DYNAMIC_TAG = 18;
/*     */   static final int CONSTANT_MODULE_TAG = 19;
/*     */   static final int CONSTANT_PACKAGE_TAG = 20;
/*     */   static final int BOOTSTRAP_METHOD_TAG = 64;
/*     */   static final int TYPE_TAG = 128;
/*     */   static final int UNINITIALIZED_TYPE_TAG = 129;
/*     */   static final int MERGED_TYPE_TAG = 130;
/*     */   final int index;
/*     */   final int tag;
/*     */   final String owner;
/*     */   final String name;
/*     */   final String value;
/*     */   final long data;
/*     */   int info;
/*     */   
/*     */   Symbol(int index, int tag, String owner, String name, String value, long data) {
/* 221 */     this.index = index;
/* 222 */     this.tag = tag;
/* 223 */     this.owner = owner;
/* 224 */     this.name = name;
/* 225 */     this.value = value;
/* 226 */     this.data = data;
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
/*     */   int getArgumentsAndReturnSizes() {
/* 238 */     if (this.info == 0) {
/* 239 */       this.info = Type.getArgumentsAndReturnSizes(this.value);
/*     */     }
/* 241 */     return this.info;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/Symbol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */