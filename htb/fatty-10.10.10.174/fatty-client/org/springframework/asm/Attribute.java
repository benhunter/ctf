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
/*     */ public class Attribute
/*     */ {
/*     */   public final String type;
/*     */   private byte[] content;
/*     */   Attribute nextAttribute;
/*     */   
/*     */   protected Attribute(String type) {
/*  65 */     this.type = type;
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
/*     */   public boolean isUnknown() {
/*  79 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCodeAttribute() {
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Label[] getLabels() {
/*  98 */     return new Label[0];
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
/*     */ 
/*     */   
/*     */   protected Attribute read(ClassReader classReader, int offset, int length, char[] charBuffer, int codeAttributeOffset, Label[] labels) {
/* 128 */     Attribute attribute = new Attribute(this.type);
/* 129 */     attribute.content = new byte[length];
/* 130 */     System.arraycopy(classReader.b, offset, attribute.content, 0, length);
/* 131 */     return attribute;
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
/*     */   protected ByteVector write(ClassWriter classWriter, byte[] code, int codeLength, int maxStack, int maxLocals) {
/* 159 */     return new ByteVector(this.content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int getAttributeCount() {
/* 168 */     int count = 0;
/* 169 */     Attribute attribute = this;
/* 170 */     while (attribute != null) {
/* 171 */       count++;
/* 172 */       attribute = attribute.nextAttribute;
/*     */     } 
/* 174 */     return count;
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
/*     */   final int computeAttributesSize(SymbolTable symbolTable) {
/* 187 */     byte[] code = null;
/* 188 */     int codeLength = 0;
/* 189 */     int maxStack = -1;
/* 190 */     int maxLocals = -1;
/* 191 */     return computeAttributesSize(symbolTable, code, 0, -1, -1);
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
/*     */   final int computeAttributesSize(SymbolTable symbolTable, byte[] code, int codeLength, int maxStack, int maxLocals) {
/* 219 */     ClassWriter classWriter = symbolTable.classWriter;
/* 220 */     int size = 0;
/* 221 */     Attribute attribute = this;
/* 222 */     while (attribute != null) {
/* 223 */       symbolTable.addConstantUtf8(attribute.type);
/* 224 */       size += 6 + (attribute.write(classWriter, code, codeLength, maxStack, maxLocals)).length;
/* 225 */       attribute = attribute.nextAttribute;
/*     */     } 
/* 227 */     return size;
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
/*     */   final void putAttributes(SymbolTable symbolTable, ByteVector output) {
/* 239 */     byte[] code = null;
/* 240 */     int codeLength = 0;
/* 241 */     int maxStack = -1;
/* 242 */     int maxLocals = -1;
/* 243 */     putAttributes(symbolTable, code, 0, -1, -1, output);
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
/*     */   final void putAttributes(SymbolTable symbolTable, byte[] code, int codeLength, int maxStack, int maxLocals, ByteVector output) {
/* 271 */     ClassWriter classWriter = symbolTable.classWriter;
/* 272 */     Attribute attribute = this;
/* 273 */     while (attribute != null) {
/*     */       
/* 275 */       ByteVector attributeContent = attribute.write(classWriter, code, codeLength, maxStack, maxLocals);
/*     */       
/* 277 */       output.putShort(symbolTable.addConstantUtf8(attribute.type)).putInt(attributeContent.length);
/* 278 */       output.putByteArray(attributeContent.data, 0, attributeContent.length);
/* 279 */       attribute = attribute.nextAttribute;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Set
/*     */   {
/*     */     private static final int SIZE_INCREMENT = 6;
/*     */     
/*     */     private int size;
/* 289 */     private Attribute[] data = new Attribute[6];
/*     */     
/*     */     void addAttributes(Attribute attributeList) {
/* 292 */       Attribute attribute = attributeList;
/* 293 */       while (attribute != null) {
/* 294 */         if (!contains(attribute)) {
/* 295 */           add(attribute);
/*     */         }
/* 297 */         attribute = attribute.nextAttribute;
/*     */       } 
/*     */     }
/*     */     
/*     */     Attribute[] toArray() {
/* 302 */       Attribute[] result = new Attribute[this.size];
/* 303 */       System.arraycopy(this.data, 0, result, 0, this.size);
/* 304 */       return result;
/*     */     }
/*     */     
/*     */     private boolean contains(Attribute attribute) {
/* 308 */       for (int i = 0; i < this.size; i++) {
/* 309 */         if ((this.data[i]).type.equals(attribute.type)) {
/* 310 */           return true;
/*     */         }
/*     */       } 
/* 313 */       return false;
/*     */     }
/*     */     
/*     */     private void add(Attribute attribute) {
/* 317 */       if (this.size >= this.data.length) {
/* 318 */         Attribute[] newData = new Attribute[this.data.length + 6];
/* 319 */         System.arraycopy(this.data, 0, newData, 0, this.size);
/* 320 */         this.data = newData;
/*     */       } 
/* 322 */       this.data[this.size++] = attribute;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/Attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */