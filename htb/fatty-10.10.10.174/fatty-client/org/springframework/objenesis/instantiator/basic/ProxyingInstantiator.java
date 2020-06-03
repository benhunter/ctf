/*     */ package org.springframework.objenesis.instantiator.basic;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*     */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*     */ import org.springframework.objenesis.instantiator.annotations.Typology;
/*     */ import org.springframework.objenesis.instantiator.util.ClassDefinitionUtils;
/*     */ import org.springframework.objenesis.instantiator.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Instantiator(Typology.STANDARD)
/*     */ public class ProxyingInstantiator<T>
/*     */   implements ObjectInstantiator<T>
/*     */ {
/*     */   private static final int INDEX_CLASS_THIS = 1;
/*     */   private static final int INDEX_CLASS_SUPERCLASS = 2;
/*     */   private static final int INDEX_UTF8_CONSTRUCTOR_NAME = 3;
/*     */   private static final int INDEX_UTF8_CONSTRUCTOR_DESC = 4;
/*     */   private static final int INDEX_UTF8_CODE_ATTRIBUTE = 5;
/*     */   private static final int INDEX_UTF8_CLASS = 7;
/*     */   private static final int INDEX_UTF8_SUPERCLASS = 8;
/*     */   private static final int CONSTANT_POOL_COUNT = 9;
/*  52 */   private static final byte[] CODE = new byte[] { 42, -79 };
/*  53 */   private static final int CODE_ATTRIBUTE_LENGTH = 12 + CODE.length;
/*     */   
/*     */   private static final String SUFFIX = "$$$Objenesis";
/*     */   
/*     */   private static final String CONSTRUCTOR_NAME = "<init>";
/*     */   
/*     */   private static final String CONSTRUCTOR_DESC = "()V";
/*     */   
/*     */   private final Class<? extends T> newType;
/*     */   
/*     */   public ProxyingInstantiator(Class<T> type) {
/*  64 */     byte[] classBytes = writeExtendingClass(type);
/*     */     
/*     */     try {
/*  67 */       this.newType = ClassDefinitionUtils.defineClass(type.getName() + "$$$Objenesis", classBytes, type, type.getClassLoader());
/*  68 */     } catch (Exception e) {
/*  69 */       throw new ObjenesisException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public T newInstance() {
/*  74 */     return (T)ClassUtils.newInstance(this.newType);
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
/*     */   private static byte[] writeExtendingClass(Class<?> type) {
/*  86 */     String parentClazz = ClassUtils.classNameToInternalClassName(type.getName());
/*  87 */     String clazz = parentClazz + "$$$Objenesis";
/*     */     
/*  89 */     ByteArrayOutputStream bIn = new ByteArrayOutputStream(1000);
/*  90 */     try (DataOutputStream in = new DataOutputStream(bIn)) {
/*     */       
/*  92 */       in.write(ClassDefinitionUtils.MAGIC);
/*  93 */       in.write(ClassDefinitionUtils.VERSION);
/*  94 */       in.writeShort(9);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  99 */       in.writeByte(7);
/* 100 */       in.writeShort(7);
/*     */ 
/*     */       
/* 103 */       in.writeByte(7);
/* 104 */       in.writeShort(8);
/*     */ 
/*     */       
/* 107 */       in.writeByte(1);
/* 108 */       in.writeUTF("<init>");
/*     */ 
/*     */       
/* 111 */       in.writeByte(1);
/* 112 */       in.writeUTF("()V");
/*     */ 
/*     */       
/* 115 */       in.writeByte(1);
/* 116 */       in.writeUTF("Code");
/*     */ 
/*     */       
/* 119 */       in.writeByte(1);
/* 120 */       in.writeUTF("L" + clazz + ";");
/*     */ 
/*     */       
/* 123 */       in.writeByte(1);
/* 124 */       in.writeUTF(clazz);
/*     */ 
/*     */       
/* 127 */       in.writeByte(1);
/* 128 */       in.writeUTF(parentClazz);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 133 */       in.writeShort(33);
/*     */ 
/*     */       
/* 136 */       in.writeShort(1);
/*     */ 
/*     */       
/* 139 */       in.writeShort(2);
/*     */ 
/*     */       
/* 142 */       in.writeShort(0);
/*     */ 
/*     */       
/* 145 */       in.writeShort(0);
/*     */ 
/*     */       
/* 148 */       in.writeShort(1);
/*     */ 
/*     */       
/* 151 */       in.writeShort(1);
/* 152 */       in.writeShort(3);
/* 153 */       in.writeShort(4);
/* 154 */       in.writeShort(1);
/*     */ 
/*     */       
/* 157 */       in.writeShort(5);
/* 158 */       in.writeInt(CODE_ATTRIBUTE_LENGTH);
/* 159 */       in.writeShort(1);
/* 160 */       in.writeShort(1);
/* 161 */       in.writeInt(CODE.length);
/* 162 */       in.write(CODE);
/* 163 */       in.writeShort(0);
/* 164 */       in.writeShort(0);
/*     */ 
/*     */       
/* 167 */       in.writeShort(0);
/*     */     
/*     */     }
/* 170 */     catch (IOException e) {
/* 171 */       throw new ObjenesisException(e);
/*     */     } 
/*     */     
/* 174 */     return bIn.toByteArray();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/objenesis/instantiator/basic/ProxyingInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */