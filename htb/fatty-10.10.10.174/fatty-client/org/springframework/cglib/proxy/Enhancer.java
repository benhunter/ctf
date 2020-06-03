/*      */ package org.springframework.cglib.proxy;
/*      */ 
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.springframework.asm.ClassVisitor;
/*      */ import org.springframework.asm.Label;
/*      */ import org.springframework.asm.Type;
/*      */ import org.springframework.cglib.core.AbstractClassGenerator;
/*      */ import org.springframework.cglib.core.ClassEmitter;
/*      */ import org.springframework.cglib.core.CodeEmitter;
/*      */ import org.springframework.cglib.core.CodeGenerationException;
/*      */ import org.springframework.cglib.core.CollectionUtils;
/*      */ import org.springframework.cglib.core.Constants;
/*      */ import org.springframework.cglib.core.DuplicatesPredicate;
/*      */ import org.springframework.cglib.core.EmitUtils;
/*      */ import org.springframework.cglib.core.KeyFactory;
/*      */ import org.springframework.cglib.core.KeyFactoryCustomizer;
/*      */ import org.springframework.cglib.core.Local;
/*      */ import org.springframework.cglib.core.MethodInfo;
/*      */ import org.springframework.cglib.core.MethodInfoTransformer;
/*      */ import org.springframework.cglib.core.MethodWrapper;
/*      */ import org.springframework.cglib.core.ObjectSwitchCallback;
/*      */ import org.springframework.cglib.core.Predicate;
/*      */ import org.springframework.cglib.core.ProcessSwitchCallback;
/*      */ import org.springframework.cglib.core.ReflectUtils;
/*      */ import org.springframework.cglib.core.RejectModifierPredicate;
/*      */ import org.springframework.cglib.core.Signature;
/*      */ import org.springframework.cglib.core.Transformer;
/*      */ import org.springframework.cglib.core.TypeUtils;
/*      */ import org.springframework.cglib.core.VisibilityPredicate;
/*      */ import org.springframework.cglib.core.WeakCacheKey;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Enhancer
/*      */   extends AbstractClassGenerator
/*      */ {
/*   95 */   private static final CallbackFilter ALL_ZERO = new CallbackFilter() {
/*      */       public int accept(Method method) {
/*   97 */         return 0;
/*      */       }
/*      */     };
/*      */   
/*  101 */   private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(Enhancer.class.getName());
/*      */ 
/*      */   
/*  104 */   private static final EnhancerKey KEY_FACTORY = (EnhancerKey)KeyFactory.create(EnhancerKey.class, (KeyFactoryCustomizer)KeyFactory.HASH_ASM_TYPE, null);
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String BOUND_FIELD = "CGLIB$BOUND";
/*      */ 
/*      */   
/*      */   private static final String FACTORY_DATA_FIELD = "CGLIB$FACTORY_DATA";
/*      */ 
/*      */   
/*      */   private static final String THREAD_CALLBACKS_FIELD = "CGLIB$THREAD_CALLBACKS";
/*      */ 
/*      */   
/*      */   private static final String STATIC_CALLBACKS_FIELD = "CGLIB$STATIC_CALLBACKS";
/*      */ 
/*      */   
/*      */   private static final String SET_THREAD_CALLBACKS_NAME = "CGLIB$SET_THREAD_CALLBACKS";
/*      */ 
/*      */   
/*      */   private static final String SET_STATIC_CALLBACKS_NAME = "CGLIB$SET_STATIC_CALLBACKS";
/*      */ 
/*      */   
/*      */   private static final String CONSTRUCTED_FIELD = "CGLIB$CONSTRUCTED";
/*      */ 
/*      */   
/*      */   private static final String CALLBACK_FILTER_FIELD = "CGLIB$CALLBACK_FILTER";
/*      */ 
/*      */   
/*  132 */   private static final Type OBJECT_TYPE = TypeUtils.parseType("Object");
/*      */ 
/*      */   
/*  135 */   private static final Type FACTORY = TypeUtils.parseType("org.springframework.cglib.proxy.Factory");
/*      */ 
/*      */   
/*  138 */   private static final Type ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
/*      */ 
/*      */   
/*  141 */   private static final Type ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
/*      */ 
/*      */   
/*  144 */   private static final Type THREAD_LOCAL = TypeUtils.parseType("ThreadLocal");
/*      */ 
/*      */   
/*  147 */   private static final Type CALLBACK = TypeUtils.parseType("org.springframework.cglib.proxy.Callback");
/*      */ 
/*      */   
/*  150 */   private static final Type CALLBACK_ARRAY = Type.getType(Callback[].class);
/*      */ 
/*      */   
/*  153 */   private static final Signature CSTRUCT_NULL = TypeUtils.parseConstructor("");
/*      */   
/*  155 */   private static final Signature SET_THREAD_CALLBACKS = new Signature("CGLIB$SET_THREAD_CALLBACKS", Type.VOID_TYPE, new Type[] { CALLBACK_ARRAY });
/*      */ 
/*      */   
/*  158 */   private static final Signature SET_STATIC_CALLBACKS = new Signature("CGLIB$SET_STATIC_CALLBACKS", Type.VOID_TYPE, new Type[] { CALLBACK_ARRAY });
/*      */ 
/*      */   
/*  161 */   private static final Signature NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[] { CALLBACK_ARRAY });
/*      */ 
/*      */   
/*  164 */   private static final Signature MULTIARG_NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[] { Constants.TYPE_CLASS_ARRAY, Constants.TYPE_OBJECT_ARRAY, CALLBACK_ARRAY });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  171 */   private static final Signature SINGLE_NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[] { CALLBACK });
/*      */ 
/*      */   
/*  174 */   private static final Signature SET_CALLBACK = new Signature("setCallback", Type.VOID_TYPE, new Type[] { Type.INT_TYPE, CALLBACK });
/*      */ 
/*      */   
/*  177 */   private static final Signature GET_CALLBACK = new Signature("getCallback", CALLBACK, new Type[] { Type.INT_TYPE });
/*      */ 
/*      */   
/*  180 */   private static final Signature SET_CALLBACKS = new Signature("setCallbacks", Type.VOID_TYPE, new Type[] { CALLBACK_ARRAY });
/*      */ 
/*      */   
/*  183 */   private static final Signature GET_CALLBACKS = new Signature("getCallbacks", CALLBACK_ARRAY, new Type[0]);
/*      */ 
/*      */ 
/*      */   
/*  187 */   private static final Signature THREAD_LOCAL_GET = TypeUtils.parseSignature("Object get()");
/*      */ 
/*      */   
/*  190 */   private static final Signature THREAD_LOCAL_SET = TypeUtils.parseSignature("void set(Object)");
/*      */ 
/*      */   
/*  193 */   private static final Signature BIND_CALLBACKS = TypeUtils.parseSignature("void CGLIB$BIND_CALLBACKS(Object)");
/*      */ 
/*      */ 
/*      */   
/*      */   private EnhancerFactoryData currentData;
/*      */ 
/*      */ 
/*      */   
/*      */   private Object currentKey;
/*      */ 
/*      */ 
/*      */   
/*      */   private Class[] interfaces;
/*      */ 
/*      */ 
/*      */   
/*      */   private CallbackFilter filter;
/*      */ 
/*      */ 
/*      */   
/*      */   private Callback[] callbacks;
/*      */ 
/*      */ 
/*      */   
/*      */   private Type[] callbackTypes;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean validateCallbackTypes;
/*      */ 
/*      */   
/*      */   private boolean classOnly;
/*      */ 
/*      */   
/*      */   private Class superclass;
/*      */ 
/*      */   
/*      */   private Class[] argumentTypes;
/*      */ 
/*      */   
/*      */   private Object[] arguments;
/*      */ 
/*      */   
/*      */   private boolean useFactory = true;
/*      */ 
/*      */   
/*      */   private Long serialVersionUID;
/*      */ 
/*      */   
/*      */   private boolean interceptDuringConstruction = true;
/*      */ 
/*      */ 
/*      */   
/*      */   public Enhancer() {
/*  247 */     super(SOURCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSuperclass(Class superclass) {
/*  260 */     if (superclass != null && superclass.isInterface()) {
/*  261 */       setInterfaces(new Class[] { superclass });
/*      */     }
/*  263 */     else if (superclass != null && superclass.equals(Object.class)) {
/*      */       
/*  265 */       this.superclass = null;
/*      */     } else {
/*      */       
/*  268 */       this.superclass = superclass;
/*      */       
/*  270 */       setContextClass(superclass);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInterfaces(Class[] interfaces) {
/*  282 */     this.interfaces = interfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallbackFilter(CallbackFilter filter) {
/*  294 */     this.filter = filter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallback(Callback callback) {
/*  305 */     setCallbacks(new Callback[] { callback });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallbacks(Callback[] callbacks) {
/*  318 */     if (callbacks != null && callbacks.length == 0) {
/*  319 */       throw new IllegalArgumentException("Array cannot be empty");
/*      */     }
/*  321 */     this.callbacks = callbacks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseFactory(boolean useFactory) {
/*  334 */     this.useFactory = useFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInterceptDuringConstruction(boolean interceptDuringConstruction) {
/*  344 */     this.interceptDuringConstruction = interceptDuringConstruction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallbackType(Class callbackType) {
/*  356 */     setCallbackTypes(new Class[] { callbackType });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallbackTypes(Class[] callbackTypes) {
/*  369 */     if (callbackTypes != null && callbackTypes.length == 0) {
/*  370 */       throw new IllegalArgumentException("Array cannot be empty");
/*      */     }
/*  372 */     this.callbackTypes = CallbackInfo.determineTypes(callbackTypes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object create() {
/*  382 */     this.classOnly = false;
/*  383 */     this.argumentTypes = null;
/*  384 */     return createHelper();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object create(Class[] argumentTypes, Object[] arguments) {
/*  397 */     this.classOnly = false;
/*  398 */     if (argumentTypes == null || arguments == null || argumentTypes.length != arguments.length) {
/*  399 */       throw new IllegalArgumentException("Arguments must be non-null and of equal length");
/*      */     }
/*  401 */     this.argumentTypes = argumentTypes;
/*  402 */     this.arguments = arguments;
/*  403 */     return createHelper();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class createClass() {
/*  415 */     this.classOnly = true;
/*  416 */     return (Class)createHelper();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSerialVersionUID(Long sUID) {
/*  424 */     this.serialVersionUID = sUID;
/*      */   }
/*      */   
/*      */   private void preValidate() {
/*  428 */     if (this.callbackTypes == null) {
/*  429 */       this.callbackTypes = CallbackInfo.determineTypes(this.callbacks, false);
/*  430 */       this.validateCallbackTypes = true;
/*      */     } 
/*  432 */     if (this.filter == null) {
/*  433 */       if (this.callbackTypes.length > 1) {
/*  434 */         throw new IllegalStateException("Multiple callback types possible but no filter specified");
/*      */       }
/*  436 */       this.filter = ALL_ZERO;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void validate() {
/*  441 */     if ((this.classOnly ^ ((this.callbacks == null) ? 1 : 0)) != 0) {
/*  442 */       if (this.classOnly) {
/*  443 */         throw new IllegalStateException("createClass does not accept callbacks");
/*      */       }
/*      */       
/*  446 */       throw new IllegalStateException("Callbacks are required");
/*      */     } 
/*      */     
/*  449 */     if (this.classOnly && this.callbackTypes == null) {
/*  450 */       throw new IllegalStateException("Callback types are required");
/*      */     }
/*  452 */     if (this.validateCallbackTypes) {
/*  453 */       this.callbackTypes = null;
/*      */     }
/*  455 */     if (this.callbacks != null && this.callbackTypes != null) {
/*  456 */       if (this.callbacks.length != this.callbackTypes.length) {
/*  457 */         throw new IllegalStateException("Lengths of callback and callback types array must be the same");
/*      */       }
/*  459 */       Type[] check = CallbackInfo.determineTypes(this.callbacks);
/*  460 */       for (int i = 0; i < check.length; i++) {
/*  461 */         if (!check[i].equals(this.callbackTypes[i])) {
/*  462 */           throw new IllegalStateException("Callback " + check[i] + " is not assignable to " + this.callbackTypes[i]);
/*      */         }
/*      */       }
/*      */     
/*  466 */     } else if (this.callbacks != null) {
/*  467 */       this.callbackTypes = CallbackInfo.determineTypes(this.callbacks);
/*      */     } 
/*  469 */     if (this.interfaces != null) {
/*  470 */       for (int i = 0; i < this.interfaces.length; i++) {
/*  471 */         if (this.interfaces[i] == null) {
/*  472 */           throw new IllegalStateException("Interfaces cannot be null");
/*      */         }
/*  474 */         if (!this.interfaces[i].isInterface()) {
/*  475 */           throw new IllegalStateException(this.interfaces[i] + " is not an interface");
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class EnhancerFactoryData
/*      */   {
/*      */     public final Class generatedClass;
/*      */ 
/*      */     
/*      */     private final Method setThreadCallbacks;
/*      */ 
/*      */     
/*      */     private final Class[] primaryConstructorArgTypes;
/*      */     
/*      */     private final Constructor primaryConstructor;
/*      */ 
/*      */     
/*      */     public EnhancerFactoryData(Class generatedClass, Class[] primaryConstructorArgTypes, boolean classOnly) {
/*  497 */       this.generatedClass = generatedClass;
/*      */       try {
/*  499 */         this.setThreadCallbacks = Enhancer.getCallbacksSetter(generatedClass, "CGLIB$SET_THREAD_CALLBACKS");
/*  500 */         if (classOnly) {
/*  501 */           this.primaryConstructorArgTypes = null;
/*  502 */           this.primaryConstructor = null;
/*      */         } else {
/*      */           
/*  505 */           this.primaryConstructorArgTypes = primaryConstructorArgTypes;
/*  506 */           this.primaryConstructor = ReflectUtils.getConstructor(generatedClass, primaryConstructorArgTypes);
/*      */         }
/*      */       
/*  509 */       } catch (NoSuchMethodException e) {
/*  510 */         throw new CodeGenerationException(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object newInstance(Class[] argumentTypes, Object[] arguments, Callback[] callbacks) {
/*  527 */       setThreadCallbacks(callbacks);
/*      */       
/*      */       try {
/*  530 */         if (this.primaryConstructorArgTypes == argumentTypes || 
/*  531 */           Arrays.equals((Object[])this.primaryConstructorArgTypes, (Object[])argumentTypes))
/*      */         {
/*      */           
/*  534 */           return ReflectUtils.newInstance(this.primaryConstructor, arguments);
/*      */         }
/*      */         
/*  537 */         return ReflectUtils.newInstance(this.generatedClass, argumentTypes, arguments);
/*      */       }
/*      */       finally {
/*      */         
/*  541 */         setThreadCallbacks(null);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void setThreadCallbacks(Callback[] callbacks) {
/*      */       try {
/*  548 */         this.setThreadCallbacks.invoke(this.generatedClass, new Object[] { callbacks });
/*      */       }
/*  550 */       catch (IllegalAccessException e) {
/*  551 */         throw new CodeGenerationException(e);
/*      */       }
/*  553 */       catch (InvocationTargetException e) {
/*  554 */         throw new CodeGenerationException(e.getTargetException());
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private Object createHelper() {
/*  560 */     preValidate();
/*  561 */     Object key = KEY_FACTORY.newInstance((this.superclass != null) ? this.superclass.getName() : null, 
/*  562 */         ReflectUtils.getNames(this.interfaces), (this.filter == ALL_ZERO) ? null : new WeakCacheKey(this.filter), this.callbackTypes, this.useFactory, this.interceptDuringConstruction, this.serialVersionUID);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  568 */     this.currentKey = key;
/*  569 */     Object result = create(key);
/*  570 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Class generate(AbstractClassGenerator.ClassLoaderData data) {
/*  575 */     validate();
/*  576 */     if (this.superclass != null) {
/*  577 */       setNamePrefix(this.superclass.getName());
/*      */     }
/*  579 */     else if (this.interfaces != null) {
/*  580 */       setNamePrefix(this.interfaces[ReflectUtils.findPackageProtected(this.interfaces)].getName());
/*      */     } 
/*  582 */     return super.generate(data);
/*      */   }
/*      */   
/*      */   protected ClassLoader getDefaultClassLoader() {
/*  586 */     if (this.superclass != null) {
/*  587 */       return this.superclass.getClassLoader();
/*      */     }
/*  589 */     if (this.interfaces != null) {
/*  590 */       return this.interfaces[0].getClassLoader();
/*      */     }
/*      */     
/*  593 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected ProtectionDomain getProtectionDomain() {
/*  598 */     if (this.superclass != null) {
/*  599 */       return ReflectUtils.getProtectionDomain(this.superclass);
/*      */     }
/*  601 */     if (this.interfaces != null) {
/*  602 */       return ReflectUtils.getProtectionDomain(this.interfaces[0]);
/*      */     }
/*      */     
/*  605 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private Signature rename(Signature sig, int index) {
/*  610 */     return new Signature("CGLIB$" + sig.getName() + "$" + index, sig
/*  611 */         .getDescriptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void getMethods(Class superclass, Class[] interfaces, List methods) {
/*  628 */     getMethods(superclass, interfaces, methods, (List)null, (Set)null);
/*      */   }
/*      */   
/*      */   private static void getMethods(Class superclass, Class[] interfaces, List methods, List interfaceMethods, Set forcePublic) {
/*  632 */     ReflectUtils.addAllMethods(superclass, methods);
/*  633 */     List target = (interfaceMethods != null) ? interfaceMethods : methods;
/*  634 */     if (interfaces != null) {
/*  635 */       for (int i = 0; i < interfaces.length; i++) {
/*  636 */         if (interfaces[i] != Factory.class) {
/*  637 */           ReflectUtils.addAllMethods(interfaces[i], target);
/*      */         }
/*      */       } 
/*      */     }
/*  641 */     if (interfaceMethods != null) {
/*  642 */       if (forcePublic != null) {
/*  643 */         forcePublic.addAll(MethodWrapper.createSet(interfaceMethods));
/*      */       }
/*  645 */       methods.addAll(interfaceMethods);
/*      */     } 
/*  647 */     CollectionUtils.filter(methods, (Predicate)new RejectModifierPredicate(8));
/*  648 */     CollectionUtils.filter(methods, (Predicate)new VisibilityPredicate(superclass, true));
/*  649 */     CollectionUtils.filter(methods, (Predicate)new DuplicatesPredicate());
/*  650 */     CollectionUtils.filter(methods, (Predicate)new RejectModifierPredicate(16));
/*      */   }
/*      */   
/*      */   public void generateClass(ClassVisitor v) throws Exception {
/*  654 */     Class sc = (this.superclass == null) ? Object.class : this.superclass;
/*      */     
/*  656 */     if (TypeUtils.isFinal(sc.getModifiers()))
/*  657 */       throw new IllegalArgumentException("Cannot subclass final class " + sc.getName()); 
/*  658 */     List constructors = new ArrayList(Arrays.asList((Object[])sc.getDeclaredConstructors()));
/*  659 */     filterConstructors(sc, constructors);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  664 */     List actualMethods = new ArrayList();
/*  665 */     List interfaceMethods = new ArrayList();
/*  666 */     final Set forcePublic = new HashSet();
/*  667 */     getMethods(sc, this.interfaces, actualMethods, interfaceMethods, forcePublic);
/*      */     
/*  669 */     List methods = CollectionUtils.transform(actualMethods, new Transformer() {
/*      */           public Object transform(Object value) {
/*  671 */             Method method = (Method)value;
/*      */             
/*  673 */             int modifiers = 0x10 | method.getModifiers() & 0xFFFFFBFF & 0xFFFFFEFF & 0xFFFFFFDF;
/*      */ 
/*      */ 
/*      */             
/*  677 */             if (forcePublic.contains(MethodWrapper.create(method))) {
/*  678 */               modifiers = modifiers & 0xFFFFFFFB | 0x1;
/*      */             }
/*  680 */             return ReflectUtils.getMethodInfo(method, modifiers);
/*      */           }
/*      */         });
/*      */     
/*  684 */     ClassEmitter e = new ClassEmitter(v);
/*  685 */     if (this.currentData == null) {
/*  686 */       e.begin_class(46, 1, 
/*      */           
/*  688 */           getClassName(), 
/*  689 */           Type.getType(sc), this.useFactory ? 
/*      */           
/*  691 */           TypeUtils.add(TypeUtils.getTypes(this.interfaces), FACTORY) : 
/*  692 */           TypeUtils.getTypes(this.interfaces), "<generated>");
/*      */     }
/*      */     else {
/*      */       
/*  696 */       e.begin_class(46, 1, 
/*      */           
/*  698 */           getClassName(), null, new Type[] { FACTORY }, "<generated>");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  703 */     List constructorInfo = CollectionUtils.transform(constructors, (Transformer)MethodInfoTransformer.getInstance());
/*      */     
/*  705 */     e.declare_field(2, "CGLIB$BOUND", Type.BOOLEAN_TYPE, null);
/*  706 */     e.declare_field(9, "CGLIB$FACTORY_DATA", OBJECT_TYPE, null);
/*  707 */     if (!this.interceptDuringConstruction) {
/*  708 */       e.declare_field(2, "CGLIB$CONSTRUCTED", Type.BOOLEAN_TYPE, null);
/*      */     }
/*  710 */     e.declare_field(26, "CGLIB$THREAD_CALLBACKS", THREAD_LOCAL, null);
/*  711 */     e.declare_field(26, "CGLIB$STATIC_CALLBACKS", CALLBACK_ARRAY, null);
/*  712 */     if (this.serialVersionUID != null) {
/*  713 */       e.declare_field(26, "serialVersionUID", Type.LONG_TYPE, this.serialVersionUID);
/*      */     }
/*      */     
/*  716 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/*  717 */       e.declare_field(2, getCallbackField(i), this.callbackTypes[i], null);
/*      */     }
/*      */     
/*  720 */     e.declare_field(10, "CGLIB$CALLBACK_FILTER", OBJECT_TYPE, null);
/*      */     
/*  722 */     if (this.currentData == null) {
/*  723 */       emitMethods(e, methods, actualMethods);
/*  724 */       emitConstructors(e, constructorInfo);
/*      */     } else {
/*      */       
/*  727 */       emitDefaultConstructor(e);
/*      */     } 
/*  729 */     emitSetThreadCallbacks(e);
/*  730 */     emitSetStaticCallbacks(e);
/*  731 */     emitBindCallbacks(e);
/*      */     
/*  733 */     if (this.useFactory || this.currentData != null) {
/*  734 */       int[] keys = getCallbackKeys();
/*  735 */       emitNewInstanceCallbacks(e);
/*  736 */       emitNewInstanceCallback(e);
/*  737 */       emitNewInstanceMultiarg(e, constructorInfo);
/*  738 */       emitGetCallback(e, keys);
/*  739 */       emitSetCallback(e, keys);
/*  740 */       emitGetCallbacks(e);
/*  741 */       emitSetCallbacks(e);
/*      */     } 
/*      */     
/*  744 */     e.end_class();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void filterConstructors(Class sc, List constructors) {
/*  758 */     CollectionUtils.filter(constructors, (Predicate)new VisibilityPredicate(sc, true));
/*  759 */     if (constructors.size() == 0) {
/*  760 */       throw new IllegalArgumentException("No visible constructors in " + sc);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object firstInstance(Class type) throws Exception {
/*  773 */     if (this.classOnly) {
/*  774 */       return type;
/*      */     }
/*      */     
/*  777 */     return createUsingReflection(type);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object nextInstance(Object instance) {
/*  782 */     EnhancerFactoryData data = (EnhancerFactoryData)instance;
/*      */     
/*  784 */     if (this.classOnly) {
/*  785 */       return data.generatedClass;
/*      */     }
/*      */     
/*  788 */     Class[] argumentTypes = this.argumentTypes;
/*  789 */     Object[] arguments = this.arguments;
/*  790 */     if (argumentTypes == null) {
/*  791 */       argumentTypes = Constants.EMPTY_CLASS_ARRAY;
/*  792 */       arguments = null;
/*      */     } 
/*  794 */     return data.newInstance(argumentTypes, arguments, this.callbacks);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object wrapCachedClass(Class klass) {
/*  799 */     Class[] argumentTypes = this.argumentTypes;
/*  800 */     if (argumentTypes == null) {
/*  801 */       argumentTypes = Constants.EMPTY_CLASS_ARRAY;
/*      */     }
/*  803 */     EnhancerFactoryData factoryData = new EnhancerFactoryData(klass, argumentTypes, this.classOnly);
/*  804 */     Field factoryDataField = null;
/*      */ 
/*      */     
/*      */     try {
/*  808 */       factoryDataField = klass.getField("CGLIB$FACTORY_DATA");
/*  809 */       factoryDataField.set(null, factoryData);
/*  810 */       Field callbackFilterField = klass.getDeclaredField("CGLIB$CALLBACK_FILTER");
/*  811 */       callbackFilterField.setAccessible(true);
/*  812 */       callbackFilterField.set(null, this.filter);
/*      */     }
/*  814 */     catch (NoSuchFieldException e) {
/*  815 */       throw new CodeGenerationException(e);
/*      */     }
/*  817 */     catch (IllegalAccessException e) {
/*  818 */       throw new CodeGenerationException(e);
/*      */     } 
/*  820 */     return new WeakReference<>(factoryData);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object unwrapCachedValue(Object cached) {
/*  825 */     if (this.currentKey instanceof EnhancerKey) {
/*  826 */       EnhancerFactoryData data = ((WeakReference<EnhancerFactoryData>)cached).get();
/*  827 */       return data;
/*      */     } 
/*  829 */     return super.unwrapCachedValue(cached);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void registerCallbacks(Class generatedClass, Callback[] callbacks) {
/*  856 */     setThreadCallbacks(generatedClass, callbacks);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void registerStaticCallbacks(Class generatedClass, Callback[] callbacks) {
/*  869 */     setCallbacksHelper(generatedClass, callbacks, "CGLIB$SET_STATIC_CALLBACKS");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEnhanced(Class type) {
/*      */     try {
/*  879 */       getCallbacksSetter(type, "CGLIB$SET_THREAD_CALLBACKS");
/*  880 */       return true;
/*      */     }
/*  882 */     catch (NoSuchMethodException e) {
/*  883 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void setThreadCallbacks(Class type, Callback[] callbacks) {
/*  888 */     setCallbacksHelper(type, callbacks, "CGLIB$SET_THREAD_CALLBACKS");
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setCallbacksHelper(Class type, Callback[] callbacks, String methodName) {
/*      */     try {
/*  894 */       Method setter = getCallbacksSetter(type, methodName);
/*  895 */       setter.invoke(null, new Object[] { callbacks });
/*      */     }
/*  897 */     catch (NoSuchMethodException e) {
/*  898 */       throw new IllegalArgumentException(type + " is not an enhanced class");
/*      */     }
/*  900 */     catch (IllegalAccessException e) {
/*  901 */       throw new CodeGenerationException(e);
/*      */     }
/*  903 */     catch (InvocationTargetException e) {
/*  904 */       throw new CodeGenerationException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static Method getCallbacksSetter(Class type, String methodName) throws NoSuchMethodException {
/*  909 */     return type.getDeclaredMethod(methodName, new Class[] { Callback[].class });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object createUsingReflection(Class type) {
/*  921 */     setThreadCallbacks(type, this.callbacks);
/*      */     
/*      */     try {
/*  924 */       if (this.argumentTypes != null)
/*      */       {
/*  926 */         return ReflectUtils.newInstance(type, this.argumentTypes, this.arguments);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  931 */       return ReflectUtils.newInstance(type);
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */       
/*  937 */       setThreadCallbacks(type, (Callback[])null);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object create(Class type, Callback callback) {
/*  949 */     Enhancer e = new Enhancer();
/*  950 */     e.setSuperclass(type);
/*  951 */     e.setCallback(callback);
/*  952 */     return e.create();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object create(Class superclass, Class[] interfaces, Callback callback) {
/*  964 */     Enhancer e = new Enhancer();
/*  965 */     e.setSuperclass(superclass);
/*  966 */     e.setInterfaces(interfaces);
/*  967 */     e.setCallback(callback);
/*  968 */     return e.create();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object create(Class superclass, Class[] interfaces, CallbackFilter filter, Callback[] callbacks) {
/*  981 */     Enhancer e = new Enhancer();
/*  982 */     e.setSuperclass(superclass);
/*  983 */     e.setInterfaces(interfaces);
/*  984 */     e.setCallbackFilter(filter);
/*  985 */     e.setCallbacks(callbacks);
/*  986 */     return e.create();
/*      */   }
/*      */   
/*      */   private void emitDefaultConstructor(ClassEmitter ce) {
/*      */     Constructor<Object> declaredConstructor;
/*      */     try {
/*  992 */       declaredConstructor = Object.class.getDeclaredConstructor(new Class[0]);
/*      */     }
/*  994 */     catch (NoSuchMethodException noSuchMethodException) {
/*  995 */       throw new IllegalStateException("Object should have default constructor ", noSuchMethodException);
/*      */     } 
/*  997 */     MethodInfo constructor = (MethodInfo)MethodInfoTransformer.getInstance().transform(declaredConstructor);
/*  998 */     CodeEmitter e = EmitUtils.begin_method(ce, constructor, 1);
/*  999 */     e.load_this();
/* 1000 */     e.dup();
/* 1001 */     Signature sig = constructor.getSignature();
/* 1002 */     e.super_invoke_constructor(sig);
/* 1003 */     e.return_value();
/* 1004 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitConstructors(ClassEmitter ce, List constructors) {
/* 1008 */     boolean seenNull = false;
/* 1009 */     for (Iterator<MethodInfo> it = constructors.iterator(); it.hasNext(); ) {
/* 1010 */       MethodInfo constructor = it.next();
/* 1011 */       if (this.currentData != null && !"()V".equals(constructor.getSignature().getDescriptor())) {
/*      */         continue;
/*      */       }
/* 1014 */       CodeEmitter e = EmitUtils.begin_method(ce, constructor, 1);
/* 1015 */       e.load_this();
/* 1016 */       e.dup();
/* 1017 */       e.load_args();
/* 1018 */       Signature sig = constructor.getSignature();
/* 1019 */       seenNull = (seenNull || sig.getDescriptor().equals("()V"));
/* 1020 */       e.super_invoke_constructor(sig);
/* 1021 */       if (this.currentData == null) {
/* 1022 */         e.invoke_static_this(BIND_CALLBACKS);
/* 1023 */         if (!this.interceptDuringConstruction) {
/* 1024 */           e.load_this();
/* 1025 */           e.push(1);
/* 1026 */           e.putfield("CGLIB$CONSTRUCTED");
/*      */         } 
/*      */       } 
/* 1029 */       e.return_value();
/* 1030 */       e.end_method();
/*      */     } 
/* 1032 */     if (!this.classOnly && !seenNull && this.arguments == null)
/* 1033 */       throw new IllegalArgumentException("Superclass has no null constructors but no arguments were given"); 
/*      */   }
/*      */   
/*      */   private int[] getCallbackKeys() {
/* 1037 */     int[] keys = new int[this.callbackTypes.length];
/* 1038 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/* 1039 */       keys[i] = i;
/*      */     }
/* 1041 */     return keys;
/*      */   }
/*      */   
/*      */   private void emitGetCallback(ClassEmitter ce, int[] keys) {
/* 1045 */     final CodeEmitter e = ce.begin_method(1, GET_CALLBACK, null);
/* 1046 */     e.load_this();
/* 1047 */     e.invoke_static_this(BIND_CALLBACKS);
/* 1048 */     e.load_this();
/* 1049 */     e.load_arg(0);
/* 1050 */     e.process_switch(keys, new ProcessSwitchCallback() {
/*      */           public void processCase(int key, Label end) {
/* 1052 */             e.getfield(Enhancer.getCallbackField(key));
/* 1053 */             e.goTo(end);
/*      */           }
/*      */           
/*      */           public void processDefault() {
/* 1057 */             e.pop();
/* 1058 */             e.aconst_null();
/*      */           }
/*      */         });
/* 1061 */     e.return_value();
/* 1062 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetCallback(ClassEmitter ce, int[] keys) {
/* 1066 */     final CodeEmitter e = ce.begin_method(1, SET_CALLBACK, null);
/* 1067 */     e.load_arg(0);
/* 1068 */     e.process_switch(keys, new ProcessSwitchCallback() {
/*      */           public void processCase(int key, Label end) {
/* 1070 */             e.load_this();
/* 1071 */             e.load_arg(1);
/* 1072 */             e.checkcast(Enhancer.this.callbackTypes[key]);
/* 1073 */             e.putfield(Enhancer.getCallbackField(key));
/* 1074 */             e.goTo(end);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public void processDefault() {}
/*      */         });
/* 1081 */     e.return_value();
/* 1082 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetCallbacks(ClassEmitter ce) {
/* 1086 */     CodeEmitter e = ce.begin_method(1, SET_CALLBACKS, null);
/* 1087 */     e.load_this();
/* 1088 */     e.load_arg(0);
/* 1089 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/* 1090 */       e.dup2();
/* 1091 */       e.aaload(i);
/* 1092 */       e.checkcast(this.callbackTypes[i]);
/* 1093 */       e.putfield(getCallbackField(i));
/*      */     } 
/* 1095 */     e.return_value();
/* 1096 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitGetCallbacks(ClassEmitter ce) {
/* 1100 */     CodeEmitter e = ce.begin_method(1, GET_CALLBACKS, null);
/* 1101 */     e.load_this();
/* 1102 */     e.invoke_static_this(BIND_CALLBACKS);
/* 1103 */     e.load_this();
/* 1104 */     e.push(this.callbackTypes.length);
/* 1105 */     e.newarray(CALLBACK);
/* 1106 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/* 1107 */       e.dup();
/* 1108 */       e.push(i);
/* 1109 */       e.load_this();
/* 1110 */       e.getfield(getCallbackField(i));
/* 1111 */       e.aastore();
/*      */     } 
/* 1113 */     e.return_value();
/* 1114 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitNewInstanceCallbacks(ClassEmitter ce) {
/* 1118 */     CodeEmitter e = ce.begin_method(1, NEW_INSTANCE, null);
/* 1119 */     Type thisType = getThisType(e);
/* 1120 */     e.load_arg(0);
/* 1121 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS);
/* 1122 */     emitCommonNewInstance(e);
/*      */   }
/*      */   
/*      */   private Type getThisType(CodeEmitter e) {
/* 1126 */     if (this.currentData == null) {
/* 1127 */       return e.getClassEmitter().getClassType();
/*      */     }
/*      */     
/* 1130 */     return Type.getType(this.currentData.generatedClass);
/*      */   }
/*      */ 
/*      */   
/*      */   private void emitCommonNewInstance(CodeEmitter e) {
/* 1135 */     Type thisType = getThisType(e);
/* 1136 */     e.new_instance(thisType);
/* 1137 */     e.dup();
/* 1138 */     e.invoke_constructor(thisType);
/* 1139 */     e.aconst_null();
/* 1140 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS);
/* 1141 */     e.return_value();
/* 1142 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitNewInstanceCallback(ClassEmitter ce) {
/* 1146 */     CodeEmitter e = ce.begin_method(1, SINGLE_NEW_INSTANCE, null);
/* 1147 */     switch (this.callbackTypes.length) {
/*      */       case 0:
/*      */         break;
/*      */ 
/*      */       
/*      */       case 1:
/* 1153 */         e.push(1);
/* 1154 */         e.newarray(CALLBACK);
/* 1155 */         e.dup();
/* 1156 */         e.push(0);
/* 1157 */         e.load_arg(0);
/* 1158 */         e.aastore();
/* 1159 */         e.invoke_static(getThisType(e), SET_THREAD_CALLBACKS);
/*      */         break;
/*      */       default:
/* 1162 */         e.throw_exception(ILLEGAL_STATE_EXCEPTION, "More than one callback object required"); break;
/*      */     } 
/* 1164 */     emitCommonNewInstance(e);
/*      */   }
/*      */   
/*      */   private void emitNewInstanceMultiarg(ClassEmitter ce, List constructors) {
/* 1168 */     final CodeEmitter e = ce.begin_method(1, MULTIARG_NEW_INSTANCE, null);
/* 1169 */     final Type thisType = getThisType(e);
/* 1170 */     e.load_arg(2);
/* 1171 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS);
/* 1172 */     e.new_instance(thisType);
/* 1173 */     e.dup();
/* 1174 */     e.load_arg(0);
/* 1175 */     EmitUtils.constructor_switch(e, constructors, new ObjectSwitchCallback() {
/*      */           public void processCase(Object key, Label end) {
/* 1177 */             MethodInfo constructor = (MethodInfo)key;
/* 1178 */             Type[] types = constructor.getSignature().getArgumentTypes();
/* 1179 */             for (int i = 0; i < types.length; i++) {
/* 1180 */               e.load_arg(1);
/* 1181 */               e.push(i);
/* 1182 */               e.aaload();
/* 1183 */               e.unbox(types[i]);
/*      */             } 
/* 1185 */             e.invoke_constructor(thisType, constructor.getSignature());
/* 1186 */             e.goTo(end);
/*      */           }
/*      */           
/*      */           public void processDefault() {
/* 1190 */             e.throw_exception(Enhancer.ILLEGAL_ARGUMENT_EXCEPTION, "Constructor not found");
/*      */           }
/*      */         });
/* 1193 */     e.aconst_null();
/* 1194 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS);
/* 1195 */     e.return_value();
/* 1196 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitMethods(ClassEmitter ce, List methods, List<E> actualMethods) {
/* 1200 */     CallbackGenerator[] generators = CallbackInfo.getGenerators(this.callbackTypes);
/*      */     
/* 1202 */     Map<Object, Object> groups = new HashMap<>();
/* 1203 */     final Map<Object, Object> indexes = new HashMap<>();
/* 1204 */     final Map<Object, Object> originalModifiers = new HashMap<>();
/* 1205 */     final Map positions = CollectionUtils.getIndexMap(methods);
/* 1206 */     Map<Object, Object> declToBridge = new HashMap<>();
/*      */     
/* 1208 */     Iterator<MethodInfo> it1 = methods.iterator();
/* 1209 */     Iterator<E> it2 = (actualMethods != null) ? actualMethods.iterator() : null;
/*      */     
/* 1211 */     while (it1.hasNext()) {
/* 1212 */       MethodInfo method = it1.next();
/* 1213 */       Method actualMethod = (it2 != null) ? (Method)it2.next() : null;
/* 1214 */       int index = this.filter.accept(actualMethod);
/* 1215 */       if (index >= this.callbackTypes.length) {
/* 1216 */         throw new IllegalArgumentException("Callback filter returned an index that is too large: " + index);
/*      */       }
/* 1218 */       originalModifiers.put(method, Integer.valueOf((actualMethod != null) ? actualMethod.getModifiers() : method.getModifiers()));
/* 1219 */       indexes.put(method, Integer.valueOf(index));
/* 1220 */       List<MethodInfo> group = (List)groups.get(generators[index]);
/* 1221 */       if (group == null) {
/* 1222 */         groups.put(generators[index], group = new ArrayList(methods.size()));
/*      */       }
/* 1224 */       group.add(method);
/*      */ 
/*      */ 
/*      */       
/* 1228 */       if (TypeUtils.isBridge(actualMethod.getModifiers())) {
/* 1229 */         Set<Signature> bridges = (Set)declToBridge.get(actualMethod.getDeclaringClass());
/* 1230 */         if (bridges == null) {
/* 1231 */           bridges = new HashSet();
/* 1232 */           declToBridge.put(actualMethod.getDeclaringClass(), bridges);
/*      */         } 
/* 1234 */         bridges.add(method.getSignature());
/*      */       } 
/*      */     } 
/*      */     
/* 1238 */     final Map bridgeToTarget = (new BridgeMethodResolver(declToBridge, getClassLoader())).resolveAll();
/*      */     
/* 1240 */     Set<CallbackGenerator> seenGen = new HashSet();
/* 1241 */     CodeEmitter se = ce.getStaticHook();
/* 1242 */     se.new_instance(THREAD_LOCAL);
/* 1243 */     se.dup();
/* 1244 */     se.invoke_constructor(THREAD_LOCAL, CSTRUCT_NULL);
/* 1245 */     se.putfield("CGLIB$THREAD_CALLBACKS");
/*      */     
/* 1247 */     Object[] state = new Object[1];
/* 1248 */     CallbackGenerator.Context context = new CallbackGenerator.Context() {
/*      */         public ClassLoader getClassLoader() {
/* 1250 */           return Enhancer.this.getClassLoader();
/*      */         }
/*      */         
/*      */         public int getOriginalModifiers(MethodInfo method) {
/* 1254 */           return ((Integer)originalModifiers.get(method)).intValue();
/*      */         }
/*      */         
/*      */         public int getIndex(MethodInfo method) {
/* 1258 */           return ((Integer)indexes.get(method)).intValue();
/*      */         }
/*      */         
/*      */         public void emitCallback(CodeEmitter e, int index) {
/* 1262 */           Enhancer.this.emitCurrentCallback(e, index);
/*      */         }
/*      */         
/*      */         public Signature getImplSignature(MethodInfo method) {
/* 1266 */           return Enhancer.this.rename(method.getSignature(), ((Integer)positions.get(method)).intValue());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public void emitLoadArgsAndInvoke(CodeEmitter e, MethodInfo method) {
/* 1274 */           Signature bridgeTarget = (Signature)bridgeToTarget.get(method.getSignature());
/* 1275 */           if (bridgeTarget != null) {
/*      */             
/* 1277 */             for (int i = 0; i < (bridgeTarget.getArgumentTypes()).length; i++) {
/* 1278 */               e.load_arg(i);
/* 1279 */               Type target = bridgeTarget.getArgumentTypes()[i];
/* 1280 */               if (!target.equals(method.getSignature().getArgumentTypes()[i])) {
/* 1281 */                 e.checkcast(target);
/*      */               }
/*      */             } 
/*      */             
/* 1285 */             e.invoke_virtual_this(bridgeTarget);
/*      */             
/* 1287 */             Type retType = method.getSignature().getReturnType();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1298 */             if (!retType.equals(bridgeTarget.getReturnType())) {
/* 1299 */               e.checkcast(retType);
/*      */             }
/*      */           } else {
/*      */             
/* 1303 */             e.load_args();
/* 1304 */             e.super_invoke(method.getSignature());
/*      */           } 
/*      */         }
/*      */         
/*      */         public CodeEmitter beginMethod(ClassEmitter ce, MethodInfo method) {
/* 1309 */           CodeEmitter e = EmitUtils.begin_method(ce, method);
/* 1310 */           if (!Enhancer.this.interceptDuringConstruction && 
/* 1311 */             !TypeUtils.isAbstract(method.getModifiers())) {
/* 1312 */             Label constructed = e.make_label();
/* 1313 */             e.load_this();
/* 1314 */             e.getfield("CGLIB$CONSTRUCTED");
/* 1315 */             e.if_jump(154, constructed);
/* 1316 */             e.load_this();
/* 1317 */             e.load_args();
/* 1318 */             e.super_invoke();
/* 1319 */             e.return_value();
/* 1320 */             e.mark(constructed);
/*      */           } 
/* 1322 */           return e;
/*      */         }
/*      */       };
/* 1325 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/* 1326 */       CallbackGenerator gen = generators[i];
/* 1327 */       if (!seenGen.contains(gen)) {
/* 1328 */         seenGen.add(gen);
/* 1329 */         List fmethods = (List)groups.get(gen);
/* 1330 */         if (fmethods != null) {
/*      */           try {
/* 1332 */             gen.generate(ce, context, fmethods);
/* 1333 */             gen.generateStatic(se, context, fmethods);
/*      */           }
/* 1335 */           catch (RuntimeException x) {
/* 1336 */             throw x;
/*      */           }
/* 1338 */           catch (Exception x) {
/* 1339 */             throw new CodeGenerationException(x);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1344 */     se.return_value();
/* 1345 */     se.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetThreadCallbacks(ClassEmitter ce) {
/* 1349 */     CodeEmitter e = ce.begin_method(9, SET_THREAD_CALLBACKS, null);
/*      */ 
/*      */     
/* 1352 */     e.getfield("CGLIB$THREAD_CALLBACKS");
/* 1353 */     e.load_arg(0);
/* 1354 */     e.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_SET);
/* 1355 */     e.return_value();
/* 1356 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetStaticCallbacks(ClassEmitter ce) {
/* 1360 */     CodeEmitter e = ce.begin_method(9, SET_STATIC_CALLBACKS, null);
/*      */ 
/*      */     
/* 1363 */     e.load_arg(0);
/* 1364 */     e.putfield("CGLIB$STATIC_CALLBACKS");
/* 1365 */     e.return_value();
/* 1366 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitCurrentCallback(CodeEmitter e, int index) {
/* 1370 */     e.load_this();
/* 1371 */     e.getfield(getCallbackField(index));
/* 1372 */     e.dup();
/* 1373 */     Label end = e.make_label();
/* 1374 */     e.ifnonnull(end);
/* 1375 */     e.pop();
/* 1376 */     e.load_this();
/* 1377 */     e.invoke_static_this(BIND_CALLBACKS);
/* 1378 */     e.load_this();
/* 1379 */     e.getfield(getCallbackField(index));
/* 1380 */     e.mark(end);
/*      */   }
/*      */   
/*      */   private void emitBindCallbacks(ClassEmitter ce) {
/* 1384 */     CodeEmitter e = ce.begin_method(26, BIND_CALLBACKS, null);
/*      */ 
/*      */     
/* 1387 */     Local me = e.make_local();
/* 1388 */     e.load_arg(0);
/* 1389 */     e.checkcast_this();
/* 1390 */     e.store_local(me);
/*      */     
/* 1392 */     Label end = e.make_label();
/* 1393 */     e.load_local(me);
/* 1394 */     e.getfield("CGLIB$BOUND");
/* 1395 */     e.if_jump(154, end);
/* 1396 */     e.load_local(me);
/* 1397 */     e.push(1);
/* 1398 */     e.putfield("CGLIB$BOUND");
/*      */     
/* 1400 */     e.getfield("CGLIB$THREAD_CALLBACKS");
/* 1401 */     e.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_GET);
/* 1402 */     e.dup();
/* 1403 */     Label found_callback = e.make_label();
/* 1404 */     e.ifnonnull(found_callback);
/* 1405 */     e.pop();
/*      */     
/* 1407 */     e.getfield("CGLIB$STATIC_CALLBACKS");
/* 1408 */     e.dup();
/* 1409 */     e.ifnonnull(found_callback);
/* 1410 */     e.pop();
/* 1411 */     e.goTo(end);
/*      */     
/* 1413 */     e.mark(found_callback);
/* 1414 */     e.checkcast(CALLBACK_ARRAY);
/* 1415 */     e.load_local(me);
/* 1416 */     e.swap();
/* 1417 */     for (int i = this.callbackTypes.length - 1; i >= 0; i--) {
/* 1418 */       if (i != 0) {
/* 1419 */         e.dup2();
/*      */       }
/* 1421 */       e.aaload(i);
/* 1422 */       e.checkcast(this.callbackTypes[i]);
/* 1423 */       e.putfield(getCallbackField(i));
/*      */     } 
/*      */     
/* 1426 */     e.mark(end);
/* 1427 */     e.return_value();
/* 1428 */     e.end_method();
/*      */   }
/*      */   
/*      */   private static String getCallbackField(int index) {
/* 1432 */     return "CGLIB$CALLBACK_" + index;
/*      */   }
/*      */   
/*      */   public static interface EnhancerKey {
/*      */     Object newInstance(String param1String, String[] param1ArrayOfString, WeakCacheKey<CallbackFilter> param1WeakCacheKey, Type[] param1ArrayOfType, boolean param1Boolean1, boolean param1Boolean2, Long param1Long);
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cglib/proxy/Enhancer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */