/*    */ package org.springframework.context;
/*    */ 
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.ResolvableTypeProvider;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class PayloadApplicationEvent<T>
/*    */   extends ApplicationEvent
/*    */   implements ResolvableTypeProvider
/*    */ {
/*    */   private final T payload;
/*    */   
/*    */   public PayloadApplicationEvent(Object source, T payload) {
/* 44 */     super(source);
/* 45 */     Assert.notNull(payload, "Payload must not be null");
/* 46 */     this.payload = payload;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ResolvableType getResolvableType() {
/* 52 */     return ResolvableType.forClassWithGenerics(getClass(), new ResolvableType[] { ResolvableType.forInstance(getPayload()) });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T getPayload() {
/* 59 */     return this.payload;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/PayloadApplicationEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */