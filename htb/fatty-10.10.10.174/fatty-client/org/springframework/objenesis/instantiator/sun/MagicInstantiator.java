/*     */ package org.springframework.objenesis.instantiator.sun;
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
/*     */ @Instantiator(Typology.STANDARD)
/*     */ public class MagicInstantiator<T>
/*     */   implements ObjectInstantiator<T>
/*     */ {
/*  41 */   private static final String MAGIC_ACCESSOR = getMagicClass();
/*     */   
/*     */   private static final int INDEX_CLASS_THIS = 1;
/*     */   
/*     */   private static final int INDEX_CLASS_SUPERCLASS = 2;
/*     */   
/*     */   private static final int INDEX_UTF8_CONSTRUCTOR_NAME = 3;
/*     */   private static final int INDEX_UTF8_CONSTRUCTOR_DESC = 4;
/*     */   private static final int INDEX_UTF8_CODE_ATTRIBUTE = 5;
/*     */   private static final int INDEX_UTF8_INSTANTIATOR_CLASS = 7;
/*     */   private static final int INDEX_UTF8_SUPERCLASS = 8;
/*     */   private static final int INDEX_CLASS_INTERFACE = 9;
/*     */   private static final int INDEX_UTF8_INTERFACE = 10;
/*     */   private static final int INDEX_UTF8_NEWINSTANCE_NAME = 11;
/*     */   private static final int INDEX_UTF8_NEWINSTANCE_DESC = 12;
/*     */   private static final int INDEX_METHODREF_OBJECT_CONSTRUCTOR = 13;
/*     */   private static final int INDEX_CLASS_OBJECT = 14;
/*     */   private static final int INDEX_UTF8_OBJECT = 15;
/*     */   private static final int INDEX_NAMEANDTYPE_DEFAULT_CONSTRUCTOR = 16;
/*     */   private static final int INDEX_CLASS_TYPE = 17;
/*     */   private static final int INDEX_UTF8_TYPE = 18;
/*     */   private static final int CONSTANT_POOL_COUNT = 19;
/*  63 */   private static final byte[] CONSTRUCTOR_CODE = new byte[] { 42, -73, 0, 13, -79 };
/*  64 */   private static final int CONSTRUCTOR_CODE_ATTRIBUTE_LENGTH = 12 + CONSTRUCTOR_CODE.length;
/*     */   
/*  66 */   private static final byte[] NEWINSTANCE_CODE = new byte[] { -69, 0, 17, 89, -73, 0, 13, -80 };
/*  67 */   private static final int NEWINSTANCE_CODE_ATTRIBUTE_LENGTH = 12 + NEWINSTANCE_CODE.length;
/*     */   
/*     */   private static final String CONSTRUCTOR_NAME = "<init>";
/*     */   
/*     */   private static final String CONSTRUCTOR_DESC = "()V";
/*     */   private final ObjectInstantiator<T> instantiator;
/*     */   
/*     */   public MagicInstantiator(Class<T> type) {
/*  75 */     this.instantiator = newInstantiatorOf(type);
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
/*     */   public ObjectInstantiator<T> getInstantiator() {
/*  88 */     return this.instantiator;
/*     */   }
/*     */   
/*     */   private ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
/*  92 */     String suffix = type.getSimpleName();
/*  93 */     String className = getClass().getName() + "$$$" + suffix;
/*     */     
/*  95 */     Class<ObjectInstantiator<T>> clazz = ClassUtils.getExistingClass(getClass().getClassLoader(), className);
/*     */     
/*  97 */     if (clazz == null) {
/*  98 */       byte[] classBytes = writeExtendingClass(type, className);
/*     */       
/*     */       try {
/* 101 */         clazz = ClassDefinitionUtils.defineClass(className, classBytes, type, getClass().getClassLoader());
/* 102 */       } catch (Exception e) {
/* 103 */         throw new ObjenesisException(e);
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     return (ObjectInstantiator<T>)ClassUtils.newInstance(clazz);
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
/*     */   private byte[] writeExtendingClass(Class<?> type, String className) {
/* 120 */     String clazz = ClassUtils.classNameToInternalClassName(className);
/*     */     
/* 122 */     ByteArrayOutputStream bIn = new ByteArrayOutputStream(1000);
/* 123 */     try (DataOutputStream in = new DataOutputStream(bIn)) {
/* 124 */       in.write(ClassDefinitionUtils.MAGIC);
/* 125 */       in.write(ClassDefinitionUtils.VERSION);
/* 126 */       in.writeShort(19);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 131 */       in.writeByte(7);
/* 132 */       in.writeShort(7);
/*     */ 
/*     */       
/* 135 */       in.writeByte(7);
/* 136 */       in.writeShort(8);
/*     */ 
/*     */       
/* 139 */       in.writeByte(1);
/* 140 */       in.writeUTF("<init>");
/*     */ 
/*     */       
/* 143 */       in.writeByte(1);
/* 144 */       in.writeUTF("()V");
/*     */ 
/*     */       
/* 147 */       in.writeByte(1);
/* 148 */       in.writeUTF("Code");
/*     */ 
/*     */       
/* 151 */       in.writeByte(1);
/* 152 */       in.writeUTF("L" + clazz + ";");
/*     */ 
/*     */       
/* 155 */       in.writeByte(1);
/* 156 */       in.writeUTF(clazz);
/*     */ 
/*     */       
/* 159 */       in.writeByte(1);
/*     */       
/* 161 */       in.writeUTF(MAGIC_ACCESSOR);
/*     */ 
/*     */       
/* 164 */       in.writeByte(7);
/* 165 */       in.writeShort(10);
/*     */ 
/*     */       
/* 168 */       in.writeByte(1);
/* 169 */       in.writeUTF(ObjectInstantiator.class.getName().replace('.', '/'));
/*     */ 
/*     */       
/* 172 */       in.writeByte(1);
/* 173 */       in.writeUTF("newInstance");
/*     */ 
/*     */       
/* 176 */       in.writeByte(1);
/* 177 */       in.writeUTF("()Ljava/lang/Object;");
/*     */ 
/*     */       
/* 180 */       in.writeByte(10);
/* 181 */       in.writeShort(14);
/* 182 */       in.writeShort(16);
/*     */ 
/*     */       
/* 185 */       in.writeByte(7);
/* 186 */       in.writeShort(15);
/*     */ 
/*     */       
/* 189 */       in.writeByte(1);
/* 190 */       in.writeUTF("java/lang/Object");
/*     */ 
/*     */       
/* 193 */       in.writeByte(12);
/* 194 */       in.writeShort(3);
/* 195 */       in.writeShort(4);
/*     */ 
/*     */       
/* 198 */       in.writeByte(7);
/* 199 */       in.writeShort(18);
/*     */ 
/*     */       
/* 202 */       in.writeByte(1);
/* 203 */       in.writeUTF(ClassUtils.classNameToInternalClassName(type.getName()));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 208 */       in.writeShort(49);
/*     */ 
/*     */       
/* 211 */       in.writeShort(1);
/*     */ 
/*     */       
/* 214 */       in.writeShort(2);
/*     */ 
/*     */       
/* 217 */       in.writeShort(1);
/* 218 */       in.writeShort(9);
/*     */ 
/*     */       
/* 221 */       in.writeShort(0);
/*     */ 
/*     */       
/* 224 */       in.writeShort(2);
/*     */ 
/*     */       
/* 227 */       in.writeShort(1);
/* 228 */       in.writeShort(3);
/* 229 */       in.writeShort(4);
/* 230 */       in.writeShort(1);
/*     */ 
/*     */       
/* 233 */       in.writeShort(5);
/* 234 */       in.writeInt(CONSTRUCTOR_CODE_ATTRIBUTE_LENGTH);
/* 235 */       in.writeShort(0);
/* 236 */       in.writeShort(1);
/* 237 */       in.writeInt(CONSTRUCTOR_CODE.length);
/* 238 */       in.write(CONSTRUCTOR_CODE);
/* 239 */       in.writeShort(0);
/* 240 */       in.writeShort(0);
/*     */ 
/*     */       
/* 243 */       in.writeShort(1);
/* 244 */       in.writeShort(11);
/* 245 */       in.writeShort(12);
/* 246 */       in.writeShort(1);
/*     */ 
/*     */       
/* 249 */       in.writeShort(5);
/* 250 */       in.writeInt(NEWINSTANCE_CODE_ATTRIBUTE_LENGTH);
/* 251 */       in.writeShort(2);
/* 252 */       in.writeShort(1);
/* 253 */       in.writeInt(NEWINSTANCE_CODE.length);
/* 254 */       in.write(NEWINSTANCE_CODE);
/* 255 */       in.writeShort(0);
/* 256 */       in.writeShort(0);
/*     */ 
/*     */       
/* 259 */       in.writeShort(0);
/*     */     }
/* 261 */     catch (IOException e) {
/* 262 */       throw new ObjenesisException(e);
/*     */     } 
/*     */     
/* 265 */     return bIn.toByteArray();
/*     */   }
/*     */   
/*     */   public T newInstance() {
/* 269 */     return (T)this.instantiator.newInstance();
/*     */   }
/*     */   
/*     */   private static String getMagicClass() {
/*     */     try {
/* 274 */       Class.forName("sun.reflect.MagicAccessorImpl", false, MagicInstantiator.class.getClassLoader());
/* 275 */       return "sun/reflect/MagicAccessorImpl";
/* 276 */     } catch (ClassNotFoundException e) {
/* 277 */       return "jdk/internal/reflect/MagicAccessorImpl";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/objenesis/instantiator/sun/MagicInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */