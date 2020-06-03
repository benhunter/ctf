/*     */ package org.springframework.objenesis.instantiator.basic;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.Serializable;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*     */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*     */ import org.springframework.objenesis.instantiator.annotations.Typology;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Instantiator(Typology.SERIALIZATION)
/*     */ public class ObjectInputStreamInstantiator<T>
/*     */   implements ObjectInstantiator<T>
/*     */ {
/*     */   private ObjectInputStream inputStream;
/*     */   
/*     */   private static class MockStream
/*     */     extends InputStream
/*     */   {
/*     */     private int pointer;
/*     */     private byte[] data;
/*     */     private int sequence;
/*  50 */     private static final int[] NEXT = new int[] { 1, 2, 2 };
/*     */     
/*     */     private byte[][] buffers;
/*     */     private final byte[] FIRST_DATA;
/*     */     private static byte[] HEADER;
/*     */     private static byte[] REPEATING_DATA;
/*     */     
/*     */     static {
/*  58 */       initialize();
/*     */     }
/*     */     
/*     */     private static void initialize() {
/*     */       try {
/*  63 */         ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
/*  64 */         DataOutputStream dout = new DataOutputStream(byteOut);
/*  65 */         dout.writeShort(-21267);
/*  66 */         dout.writeShort(5);
/*  67 */         HEADER = byteOut.toByteArray();
/*     */         
/*  69 */         byteOut = new ByteArrayOutputStream();
/*  70 */         dout = new DataOutputStream(byteOut);
/*     */         
/*  72 */         dout.writeByte(115);
/*  73 */         dout.writeByte(113);
/*  74 */         dout.writeInt(8257536);
/*  75 */         REPEATING_DATA = byteOut.toByteArray();
/*     */       }
/*  77 */       catch (IOException e) {
/*  78 */         throw new Error("IOException: " + e.getMessage());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public MockStream(Class<?> clazz) {
/*  84 */       this.pointer = 0;
/*  85 */       this.sequence = 0;
/*  86 */       this.data = HEADER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  97 */       ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
/*  98 */       DataOutputStream dout = new DataOutputStream(byteOut);
/*     */       try {
/* 100 */         dout.writeByte(115);
/* 101 */         dout.writeByte(114);
/* 102 */         dout.writeUTF(clazz.getName());
/* 103 */         dout.writeLong(ObjectStreamClass.lookup(clazz).getSerialVersionUID());
/* 104 */         dout.writeByte(2);
/* 105 */         dout.writeShort(0);
/* 106 */         dout.writeByte(120);
/* 107 */         dout.writeByte(112);
/*     */       }
/* 109 */       catch (IOException e) {
/* 110 */         throw new Error("IOException: " + e.getMessage());
/*     */       } 
/* 112 */       this.FIRST_DATA = byteOut.toByteArray();
/* 113 */       this.buffers = new byte[][] { HEADER, this.FIRST_DATA, REPEATING_DATA };
/*     */     }
/*     */     
/*     */     private void advanceBuffer() {
/* 117 */       this.pointer = 0;
/* 118 */       this.sequence = NEXT[this.sequence];
/* 119 */       this.data = this.buffers[this.sequence];
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() {
/* 124 */       int result = this.data[this.pointer++];
/* 125 */       if (this.pointer >= this.data.length) {
/* 126 */         advanceBuffer();
/*     */       }
/*     */       
/* 129 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public int available() {
/* 134 */       return Integer.MAX_VALUE;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) {
/* 139 */       int left = len;
/* 140 */       int remaining = this.data.length - this.pointer;
/*     */       
/* 142 */       while (remaining <= left) {
/* 143 */         System.arraycopy(this.data, this.pointer, b, off, remaining);
/* 144 */         off += remaining;
/* 145 */         left -= remaining;
/* 146 */         advanceBuffer();
/* 147 */         remaining = this.data.length - this.pointer;
/*     */       } 
/* 149 */       if (left > 0) {
/* 150 */         System.arraycopy(this.data, this.pointer, b, off, left);
/* 151 */         this.pointer += left;
/*     */       } 
/*     */       
/* 154 */       return len;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectInputStreamInstantiator(Class<T> clazz) {
/* 161 */     if (Serializable.class.isAssignableFrom(clazz)) {
/*     */       try {
/* 163 */         this.inputStream = new ObjectInputStream(new MockStream(clazz));
/*     */       }
/* 165 */       catch (IOException e) {
/* 166 */         throw new Error("IOException: " + e.getMessage());
/*     */       } 
/*     */     } else {
/*     */       
/* 170 */       throw new ObjenesisException(new NotSerializableException(clazz + " not serializable"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public T newInstance() {
/*     */     try {
/* 177 */       return (T)this.inputStream.readObject();
/*     */     }
/* 179 */     catch (ClassNotFoundException e) {
/* 180 */       throw new Error("ClassNotFoundException: " + e.getMessage());
/*     */     }
/* 182 */     catch (Exception e) {
/* 183 */       throw new ObjenesisException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/objenesis/instantiator/basic/ObjectInputStreamInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */