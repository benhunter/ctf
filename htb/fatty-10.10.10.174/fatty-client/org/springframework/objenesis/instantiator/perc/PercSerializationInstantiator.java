/*    */ package org.springframework.objenesis.instantiator.perc;
/*    */ 
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Typology;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Instantiator(Typology.SERIALIZATION)
/*    */ public class PercSerializationInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private Object[] typeArgs;
/*    */   private final Method newInstanceMethod;
/*    */   
/*    */   public PercSerializationInstantiator(Class<T> type) {
/* 48 */     Class<? super T> unserializableType = type;
/*    */     
/* 50 */     while (Serializable.class.isAssignableFrom(unserializableType)) {
/* 51 */       unserializableType = unserializableType.getSuperclass();
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 56 */       Class<?> percMethodClass = Class.forName("COM.newmonics.PercClassLoader.Method");
/*    */       
/* 58 */       this.newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("noArgConstruct", new Class[] { Class.class, Object.class, percMethodClass });
/*    */       
/* 60 */       this.newInstanceMethod.setAccessible(true);
/*    */ 
/*    */       
/* 63 */       Class<?> percClassClass = Class.forName("COM.newmonics.PercClassLoader.PercClass");
/* 64 */       Method getPercClassMethod = percClassClass.getDeclaredMethod("getPercClass", new Class[] { Class.class });
/* 65 */       Object someObject = getPercClassMethod.invoke(null, new Object[] { unserializableType });
/* 66 */       Method findMethodMethod = someObject.getClass().getDeclaredMethod("findMethod", new Class[] { String.class });
/*    */       
/* 68 */       Object percMethod = findMethodMethod.invoke(someObject, new Object[] { "<init>()V" });
/*    */       
/* 70 */       this.typeArgs = new Object[] { unserializableType, type, percMethod };
/*    */     
/*    */     }
/* 73 */     catch (ClassNotFoundException|NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 74 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 81 */       return (T)this.newInstanceMethod.invoke(null, this.typeArgs);
/*    */     }
/* 83 */     catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 84 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/objenesis/instantiator/perc/PercSerializationInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */