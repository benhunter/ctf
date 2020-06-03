/*     */ package org.springframework.beans.factory.groovy;
/*     */ 
/*     */ import groovy.lang.Closure;
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.GroovyObjectSupport;
/*     */ import groovy.lang.MetaClass;
/*     */ import groovy.lang.Reference;
/*     */ import groovy.transform.Generated;
/*     */ import groovy.xml.StreamingMarkupBuilder;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.codehaus.groovy.reflection.ClassInfo;
/*     */ import org.codehaus.groovy.runtime.BytecodeInterface8;
/*     */ import org.codehaus.groovy.runtime.GStringImpl;
/*     */ import org.codehaus.groovy.runtime.GeneratedClosure;
/*     */ import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
/*     */ import org.codehaus.groovy.runtime.callsite.CallSite;
/*     */ import org.codehaus.groovy.runtime.callsite.CallSiteArray;
/*     */ import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
/*     */ import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GroovyDynamicElementReader
/*     */   extends GroovyObjectSupport
/*     */ {
/*     */   private final String rootNamespace;
/*     */   private final Map<String, String> xmlNamespaces;
/*     */   private final BeanDefinitionParserDelegate delegate;
/*     */   private final GroovyBeanDefinitionWrapper beanDefinition;
/*     */   protected final boolean decorating;
/*     */   private boolean callAfterInvocation;
/*     */   
/*     */   public GroovyDynamicElementReader(String namespace, Map namespaceMap, BeanDefinitionParserDelegate delegate, GroovyBeanDefinitionWrapper beanDefinition, boolean decorating) {
/*  50 */     boolean bool = true;
/*  51 */     String str = namespace; this.rootNamespace = ShortTypeHandling.castToString(str);
/*  52 */     Map map = namespaceMap; this.xmlNamespaces = (Map<String, String>)ScriptBytecodeAdapter.castToType(map, Map.class);
/*  53 */     BeanDefinitionParserDelegate beanDefinitionParserDelegate = delegate; this.delegate = (BeanDefinitionParserDelegate)ScriptBytecodeAdapter.castToType(beanDefinitionParserDelegate, BeanDefinitionParserDelegate.class);
/*  54 */     GroovyBeanDefinitionWrapper groovyBeanDefinitionWrapper = beanDefinition; this.beanDefinition = (GroovyBeanDefinitionWrapper)ScriptBytecodeAdapter.castToType(groovyBeanDefinitionWrapper, $get$$class$org$springframework$beans$factory$groovy$GroovyBeanDefinitionWrapper());
/*  55 */     boolean bool1 = decorating; this.decorating = DefaultTypeTransformation.booleanUnbox(Boolean.valueOf(bool1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invokeMethod(String name, Object args) {
/*  61 */     Reference reference1 = new Reference(name), reference2 = new Reference(args); CallSite[] arrayOfCallSite = $getCallSiteArray(); if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[0].call(reference1.get(), "doCall"))) {
/*  62 */       Object object2 = arrayOfCallSite[1].call(reference2.get(), Integer.valueOf(0));
/*  63 */       Object object3 = arrayOfCallSite[2].callGetProperty(Closure.class); ScriptBytecodeAdapter.setProperty(object3, null, object2, "resolveStrategy");
/*  64 */       GroovyDynamicElementReader groovyDynamicElementReader = this; ScriptBytecodeAdapter.setProperty(groovyDynamicElementReader, null, object2, "delegate");
/*  65 */       Object result = arrayOfCallSite[3].call(object2);
/*     */       
/*  67 */       if (this.callAfterInvocation) {
/*  68 */         if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) { afterInvocation(); null; } else { arrayOfCallSite[4].callCurrent((GroovyObject)this); }
/*  69 */          boolean bool = false; this.callAfterInvocation = DefaultTypeTransformation.booleanUnbox(Boolean.valueOf(bool));
/*     */       } 
/*  71 */       return result;
/*     */     } 
/*     */ 
/*     */     
/*  75 */     Reference builder = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[5].callConstructor(StreamingMarkupBuilder.class), StreamingMarkupBuilder.class));
/*  76 */     Reference myNamespace = new Reference(this.rootNamespace);
/*  77 */     Reference myNamespaces = new Reference(this.xmlNamespaces);
/*     */     
/*  79 */     Object callable = new _invokeMethod_closure1(this, this, myNamespaces, reference2, builder, myNamespace, reference1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     Object object1 = arrayOfCallSite[6].callGetProperty(Closure.class); ScriptBytecodeAdapter.setProperty(object1, null, callable, "resolveStrategy");
/*  91 */     StreamingMarkupBuilder streamingMarkupBuilder = (StreamingMarkupBuilder)builder.get(); ScriptBytecodeAdapter.setProperty(streamingMarkupBuilder, null, callable, "delegate");
/*  92 */     Object writable = arrayOfCallSite[7].call(builder.get(), callable);
/*  93 */     Object sw = arrayOfCallSite[8].callConstructor(StringWriter.class);
/*  94 */     arrayOfCallSite[9].call(writable, sw);
/*     */     
/*  96 */     Element element = (Element)ScriptBytecodeAdapter.castToType(arrayOfCallSite[10].callGetProperty(arrayOfCallSite[11].call(arrayOfCallSite[12].callGetProperty(this.delegate), arrayOfCallSite[13].call(sw))), Element.class);
/*  97 */     arrayOfCallSite[14].call(this.delegate, element);
/*  98 */     if (this.decorating) {
/*  99 */       BeanDefinitionHolder holder = (BeanDefinitionHolder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[15].callGetProperty(this.beanDefinition), BeanDefinitionHolder.class);
/* 100 */       Object object = arrayOfCallSite[16].call(this.delegate, element, holder, null); holder = (BeanDefinitionHolder)ScriptBytecodeAdapter.castToType(object, BeanDefinitionHolder.class);
/* 101 */       arrayOfCallSite[17].call(this.beanDefinition, holder);
/*     */     } else {
/*     */       
/* 104 */       Object beanDefinition = arrayOfCallSite[18].call(this.delegate, element);
/* 105 */       if (DefaultTypeTransformation.booleanUnbox(beanDefinition)) {
/* 106 */         arrayOfCallSite[19].call(this.beanDefinition, beanDefinition);
/*     */       }
/*     */     } 
/* 109 */     if (this.callAfterInvocation) {
/* 110 */       if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) { afterInvocation(); null; } else { arrayOfCallSite[20].callCurrent((GroovyObject)this); }
/* 111 */        boolean bool = false; this.callAfterInvocation = DefaultTypeTransformation.booleanUnbox(Boolean.valueOf(bool));
/*     */     } 
/* 113 */     return element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterInvocation() {
/* 123 */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */   }
/*     */   
/*     */   public final class _invokeMethod_closure1 extends Closure implements GeneratedClosure {
/*     */     public _invokeMethod_closure1(Object _outerInstance, Object _thisObject, Reference myNamespaces, Reference args, Reference builder, Reference myNamespace, Reference name) {
/*     */       super(_outerInstance, _thisObject);
/*     */       Reference reference1 = myNamespaces;
/*     */       this.myNamespaces = reference1;
/*     */       Reference reference2 = args;
/*     */       this.args = reference2;
/*     */       Reference reference3 = builder;
/*     */       this.builder = reference3;
/*     */       Reference reference4 = myNamespace;
/*     */       this.myNamespace = reference4;
/*     */       Reference reference5 = name;
/*     */       this.name = reference5;
/*     */     }
/*     */     
/*     */     public Object doCall(Object it) {
/*     */       CallSite[] arrayOfCallSite;
/*     */       Object namespace;
/*     */       Iterator iterator;
/*     */       for (arrayOfCallSite = $getCallSiteArray(), namespace = null, iterator = (Iterator)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].call(this.myNamespaces.get()), Iterator.class); iterator.hasNext(); ) {
/*     */         namespace = iterator.next();
/*     */         arrayOfCallSite[1].call(arrayOfCallSite[2].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.createMap(new Object[] { arrayOfCallSite[3].callGetProperty(namespace), arrayOfCallSite[4].callGetProperty(namespace) }));
/*     */       } 
/*     */       if ((DefaultTypeTransformation.booleanUnbox(this.args.get()) && arrayOfCallSite[5].call(this.args.get(), Integer.valueOf(-1)) instanceof Closure)) {
/*     */         Object object1 = arrayOfCallSite[6].callGetProperty(Closure.class);
/*     */         ScriptBytecodeAdapter.setProperty(object1, null, arrayOfCallSite[7].call(this.args.get(), Integer.valueOf(-1)), "resolveStrategy");
/*     */         Object object2 = this.builder.get();
/*     */         ScriptBytecodeAdapter.setProperty(object2, null, arrayOfCallSite[8].call(this.args.get(), Integer.valueOf(-1)), "delegate");
/*     */       } 
/*     */       return ScriptBytecodeAdapter.invokeMethodN(_invokeMethod_closure1.class, ScriptBytecodeAdapter.getProperty(_invokeMethod_closure1.class, arrayOfCallSite[9].callGroovyObjectGetProperty(this), ShortTypeHandling.castToString(new GStringImpl(new Object[] { this.myNamespace.get() }, new String[] { "", "" }))), ShortTypeHandling.castToString(new GStringImpl(new Object[] { this.name.get() }, new String[] { "", "" })), ScriptBytecodeAdapter.despreadList(new Object[0], new Object[] { this.args.get() }, new int[] { 0 }));
/*     */     }
/*     */     
/*     */     public Object getMyNamespaces() {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return this.myNamespaces.get();
/*     */     }
/*     */     
/*     */     public Object getArgs() {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return this.args.get();
/*     */     }
/*     */     
/*     */     public StreamingMarkupBuilder getBuilder() {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return (StreamingMarkupBuilder)ScriptBytecodeAdapter.castToType(this.builder.get(), StreamingMarkupBuilder.class);
/*     */     }
/*     */     
/*     */     public Object getMyNamespace() {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return this.myNamespace.get();
/*     */     }
/*     */     
/*     */     public String getName() {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return ShortTypeHandling.castToString(this.name.get());
/*     */     }
/*     */     
/*     */     @Generated
/*     */     public Object doCall() {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return doCall(null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/groovy/GroovyDynamicElementReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */