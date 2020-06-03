/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Iterator;
/*    */ import java.util.Spliterator;
/*    */ import java.util.Spliterators;
/*    */ import java.util.stream.Stream;
/*    */ import java.util.stream.StreamSupport;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ public interface PropertyValues
/*    */   extends Iterable<PropertyValue>
/*    */ {
/*    */   default Iterator<PropertyValue> iterator() {
/* 45 */     return Arrays.<PropertyValue>asList(getPropertyValues()).iterator();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default Spliterator<PropertyValue> spliterator() {
/* 54 */     return Spliterators.spliterator((Object[])getPropertyValues(), 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default Stream<PropertyValue> stream() {
/* 62 */     return StreamSupport.stream(spliterator(), false);
/*    */   }
/*    */   
/*    */   PropertyValue[] getPropertyValues();
/*    */   
/*    */   @Nullable
/*    */   PropertyValue getPropertyValue(String paramString);
/*    */   
/*    */   PropertyValues changesSince(PropertyValues paramPropertyValues);
/*    */   
/*    */   boolean contains(String paramString);
/*    */   
/*    */   boolean isEmpty();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/PropertyValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */