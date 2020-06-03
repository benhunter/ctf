/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.function.Supplier;
/*      */ import org.springframework.beans.BeanMetadataAttributeAccessor;
/*      */ import org.springframework.beans.MutablePropertyValues;
/*      */ import org.springframework.beans.PropertyValues;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*      */ import org.springframework.core.AttributeAccessor;
/*      */ import org.springframework.core.io.DescriptiveResource;
/*      */ import org.springframework.core.io.Resource;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractBeanDefinition
/*      */   extends BeanMetadataAttributeAccessor
/*      */   implements BeanDefinition, Cloneable
/*      */ {
/*      */   public static final String SCOPE_DEFAULT = "";
/*      */   public static final int AUTOWIRE_NO = 0;
/*      */   public static final int AUTOWIRE_BY_NAME = 1;
/*      */   public static final int AUTOWIRE_BY_TYPE = 2;
/*      */   public static final int AUTOWIRE_CONSTRUCTOR = 3;
/*      */   @Deprecated
/*      */   public static final int AUTOWIRE_AUTODETECT = 4;
/*      */   public static final int DEPENDENCY_CHECK_NONE = 0;
/*      */   public static final int DEPENDENCY_CHECK_OBJECTS = 1;
/*      */   public static final int DEPENDENCY_CHECK_SIMPLE = 2;
/*      */   public static final int DEPENDENCY_CHECK_ALL = 3;
/*      */   public static final String INFER_METHOD = "(inferred)";
/*      */   @Nullable
/*      */   private volatile Object beanClass;
/*      */   @Nullable
/*  143 */   private String scope = "";
/*      */ 
/*      */   
/*      */   private boolean abstractFlag = false;
/*      */   
/*      */   private boolean lazyInit = false;
/*      */   
/*  150 */   private int autowireMode = 0;
/*      */   
/*  152 */   private int dependencyCheck = 0;
/*      */   
/*      */   @Nullable
/*      */   private String[] dependsOn;
/*      */   
/*      */   private boolean autowireCandidate = true;
/*      */   
/*      */   private boolean primary = false;
/*      */   
/*  161 */   private final Map<String, AutowireCandidateQualifier> qualifiers = new LinkedHashMap<>();
/*      */   
/*      */   @Nullable
/*      */   private Supplier<?> instanceSupplier;
/*      */   
/*      */   private boolean nonPublicAccessAllowed = true;
/*      */   
/*      */   private boolean lenientConstructorResolution = true;
/*      */   
/*      */   @Nullable
/*      */   private String factoryBeanName;
/*      */   
/*      */   @Nullable
/*      */   private String factoryMethodName;
/*      */   
/*      */   @Nullable
/*      */   private ConstructorArgumentValues constructorArgumentValues;
/*      */   
/*      */   @Nullable
/*      */   private MutablePropertyValues propertyValues;
/*      */   
/*      */   @Nullable
/*      */   private MethodOverrides methodOverrides;
/*      */   
/*      */   @Nullable
/*      */   private String initMethodName;
/*      */   
/*      */   @Nullable
/*      */   private String destroyMethodName;
/*      */   
/*      */   private boolean enforceInitMethod = true;
/*      */   
/*      */   private boolean enforceDestroyMethod = true;
/*      */   
/*      */   private boolean synthetic = false;
/*      */   
/*  197 */   private int role = 0;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private String description;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Resource resource;
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractBeanDefinition() {
/*  210 */     this((ConstructorArgumentValues)null, (MutablePropertyValues)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractBeanDefinition(@Nullable ConstructorArgumentValues cargs, @Nullable MutablePropertyValues pvs) {
/*  218 */     this.constructorArgumentValues = cargs;
/*  219 */     this.propertyValues = pvs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractBeanDefinition(BeanDefinition original) {
/*  228 */     setParentName(original.getParentName());
/*  229 */     setBeanClassName(original.getBeanClassName());
/*  230 */     setScope(original.getScope());
/*  231 */     setAbstract(original.isAbstract());
/*  232 */     setLazyInit(original.isLazyInit());
/*  233 */     setFactoryBeanName(original.getFactoryBeanName());
/*  234 */     setFactoryMethodName(original.getFactoryMethodName());
/*  235 */     setRole(original.getRole());
/*  236 */     setSource(original.getSource());
/*  237 */     copyAttributesFrom((AttributeAccessor)original);
/*      */     
/*  239 */     if (original instanceof AbstractBeanDefinition) {
/*  240 */       AbstractBeanDefinition originalAbd = (AbstractBeanDefinition)original;
/*  241 */       if (originalAbd.hasBeanClass()) {
/*  242 */         setBeanClass(originalAbd.getBeanClass());
/*      */       }
/*  244 */       if (originalAbd.hasConstructorArgumentValues()) {
/*  245 */         setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
/*      */       }
/*  247 */       if (originalAbd.hasPropertyValues()) {
/*  248 */         setPropertyValues(new MutablePropertyValues((PropertyValues)original.getPropertyValues()));
/*      */       }
/*  250 */       if (originalAbd.hasMethodOverrides()) {
/*  251 */         setMethodOverrides(new MethodOverrides(originalAbd.getMethodOverrides()));
/*      */       }
/*  253 */       setAutowireMode(originalAbd.getAutowireMode());
/*  254 */       setDependencyCheck(originalAbd.getDependencyCheck());
/*  255 */       setDependsOn(originalAbd.getDependsOn());
/*  256 */       setAutowireCandidate(originalAbd.isAutowireCandidate());
/*  257 */       setPrimary(originalAbd.isPrimary());
/*  258 */       copyQualifiersFrom(originalAbd);
/*  259 */       setInstanceSupplier(originalAbd.getInstanceSupplier());
/*  260 */       setNonPublicAccessAllowed(originalAbd.isNonPublicAccessAllowed());
/*  261 */       setLenientConstructorResolution(originalAbd.isLenientConstructorResolution());
/*  262 */       setInitMethodName(originalAbd.getInitMethodName());
/*  263 */       setEnforceInitMethod(originalAbd.isEnforceInitMethod());
/*  264 */       setDestroyMethodName(originalAbd.getDestroyMethodName());
/*  265 */       setEnforceDestroyMethod(originalAbd.isEnforceDestroyMethod());
/*  266 */       setSynthetic(originalAbd.isSynthetic());
/*  267 */       setResource(originalAbd.getResource());
/*      */     } else {
/*      */       
/*  270 */       setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
/*  271 */       setPropertyValues(new MutablePropertyValues((PropertyValues)original.getPropertyValues()));
/*  272 */       setResourceDescription(original.getResourceDescription());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void overrideFrom(BeanDefinition other) {
/*  294 */     if (StringUtils.hasLength(other.getBeanClassName())) {
/*  295 */       setBeanClassName(other.getBeanClassName());
/*      */     }
/*  297 */     if (StringUtils.hasLength(other.getScope())) {
/*  298 */       setScope(other.getScope());
/*      */     }
/*  300 */     setAbstract(other.isAbstract());
/*  301 */     setLazyInit(other.isLazyInit());
/*  302 */     if (StringUtils.hasLength(other.getFactoryBeanName())) {
/*  303 */       setFactoryBeanName(other.getFactoryBeanName());
/*      */     }
/*  305 */     if (StringUtils.hasLength(other.getFactoryMethodName())) {
/*  306 */       setFactoryMethodName(other.getFactoryMethodName());
/*      */     }
/*  308 */     setRole(other.getRole());
/*  309 */     setSource(other.getSource());
/*  310 */     copyAttributesFrom((AttributeAccessor)other);
/*      */     
/*  312 */     if (other instanceof AbstractBeanDefinition) {
/*  313 */       AbstractBeanDefinition otherAbd = (AbstractBeanDefinition)other;
/*  314 */       if (otherAbd.hasBeanClass()) {
/*  315 */         setBeanClass(otherAbd.getBeanClass());
/*      */       }
/*  317 */       if (otherAbd.hasConstructorArgumentValues()) {
/*  318 */         getConstructorArgumentValues().addArgumentValues(other.getConstructorArgumentValues());
/*      */       }
/*  320 */       if (otherAbd.hasPropertyValues()) {
/*  321 */         getPropertyValues().addPropertyValues((PropertyValues)other.getPropertyValues());
/*      */       }
/*  323 */       if (otherAbd.hasMethodOverrides()) {
/*  324 */         getMethodOverrides().addOverrides(otherAbd.getMethodOverrides());
/*      */       }
/*  326 */       setAutowireMode(otherAbd.getAutowireMode());
/*  327 */       setDependencyCheck(otherAbd.getDependencyCheck());
/*  328 */       setDependsOn(otherAbd.getDependsOn());
/*  329 */       setAutowireCandidate(otherAbd.isAutowireCandidate());
/*  330 */       setPrimary(otherAbd.isPrimary());
/*  331 */       copyQualifiersFrom(otherAbd);
/*  332 */       setInstanceSupplier(otherAbd.getInstanceSupplier());
/*  333 */       setNonPublicAccessAllowed(otherAbd.isNonPublicAccessAllowed());
/*  334 */       setLenientConstructorResolution(otherAbd.isLenientConstructorResolution());
/*  335 */       if (otherAbd.getInitMethodName() != null) {
/*  336 */         setInitMethodName(otherAbd.getInitMethodName());
/*  337 */         setEnforceInitMethod(otherAbd.isEnforceInitMethod());
/*      */       } 
/*  339 */       if (otherAbd.getDestroyMethodName() != null) {
/*  340 */         setDestroyMethodName(otherAbd.getDestroyMethodName());
/*  341 */         setEnforceDestroyMethod(otherAbd.isEnforceDestroyMethod());
/*      */       } 
/*  343 */       setSynthetic(otherAbd.isSynthetic());
/*  344 */       setResource(otherAbd.getResource());
/*      */     } else {
/*      */       
/*  347 */       getConstructorArgumentValues().addArgumentValues(other.getConstructorArgumentValues());
/*  348 */       getPropertyValues().addPropertyValues((PropertyValues)other.getPropertyValues());
/*  349 */       setResourceDescription(other.getResourceDescription());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void applyDefaults(BeanDefinitionDefaults defaults) {
/*  359 */     setLazyInit(defaults.isLazyInit());
/*  360 */     setAutowireMode(defaults.getAutowireMode());
/*  361 */     setDependencyCheck(defaults.getDependencyCheck());
/*  362 */     setInitMethodName(defaults.getInitMethodName());
/*  363 */     setEnforceInitMethod(false);
/*  364 */     setDestroyMethodName(defaults.getDestroyMethodName());
/*  365 */     setEnforceDestroyMethod(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBeanClassName(@Nullable String beanClassName) {
/*  374 */     this.beanClass = beanClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getBeanClassName() {
/*  383 */     Object beanClassObject = this.beanClass;
/*  384 */     if (beanClassObject instanceof Class) {
/*  385 */       return ((Class)beanClassObject).getName();
/*      */     }
/*      */     
/*  388 */     return (String)beanClassObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBeanClass(@Nullable Class<?> beanClass) {
/*  396 */     this.beanClass = beanClass;
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
/*      */   public Class<?> getBeanClass() throws IllegalStateException {
/*  409 */     Object beanClassObject = this.beanClass;
/*  410 */     if (beanClassObject == null) {
/*  411 */       throw new IllegalStateException("No bean class specified on bean definition");
/*      */     }
/*  413 */     if (!(beanClassObject instanceof Class)) {
/*  414 */       throw new IllegalStateException("Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
/*      */     }
/*      */     
/*  417 */     return (Class)beanClassObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasBeanClass() {
/*  427 */     return this.beanClass instanceof Class;
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
/*      */   @Nullable
/*      */   public Class<?> resolveBeanClass(@Nullable ClassLoader classLoader) throws ClassNotFoundException {
/*  440 */     String className = getBeanClassName();
/*  441 */     if (className == null) {
/*  442 */       return null;
/*      */     }
/*  444 */     Class<?> resolvedClass = ClassUtils.forName(className, classLoader);
/*  445 */     this.beanClass = resolvedClass;
/*  446 */     return resolvedClass;
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
/*      */   public void setScope(@Nullable String scope) {
/*  461 */     this.scope = scope;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getScope() {
/*  470 */     return this.scope;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSingleton() {
/*  480 */     return ("singleton".equals(this.scope) || "".equals(this.scope));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrototype() {
/*  490 */     return "prototype".equals(this.scope);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAbstract(boolean abstractFlag) {
/*  500 */     this.abstractFlag = abstractFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAbstract() {
/*  509 */     return this.abstractFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLazyInit(boolean lazyInit) {
/*  519 */     this.lazyInit = lazyInit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLazyInit() {
/*  529 */     return this.lazyInit;
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
/*      */   public void setAutowireMode(int autowireMode) {
/*  546 */     this.autowireMode = autowireMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAutowireMode() {
/*  553 */     return this.autowireMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getResolvedAutowireMode() {
/*  564 */     if (this.autowireMode == 4) {
/*      */ 
/*      */ 
/*      */       
/*  568 */       Constructor[] arrayOfConstructor = (Constructor[])getBeanClass().getConstructors();
/*  569 */       for (Constructor<?> constructor : arrayOfConstructor) {
/*  570 */         if (constructor.getParameterCount() == 0) {
/*  571 */           return 2;
/*      */         }
/*      */       } 
/*  574 */       return 3;
/*      */     } 
/*      */     
/*  577 */     return this.autowireMode;
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
/*      */   public void setDependencyCheck(int dependencyCheck) {
/*  591 */     this.dependencyCheck = dependencyCheck;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDependencyCheck() {
/*  598 */     return this.dependencyCheck;
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
/*      */   public void setDependsOn(@Nullable String... dependsOn) {
/*  610 */     this.dependsOn = dependsOn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String[] getDependsOn() {
/*  619 */     return this.dependsOn;
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
/*      */   public void setAutowireCandidate(boolean autowireCandidate) {
/*  633 */     this.autowireCandidate = autowireCandidate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutowireCandidate() {
/*  641 */     return this.autowireCandidate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPrimary(boolean primary) {
/*  651 */     this.primary = primary;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrimary() {
/*  659 */     return this.primary;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addQualifier(AutowireCandidateQualifier qualifier) {
/*  668 */     this.qualifiers.put(qualifier.getTypeName(), qualifier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasQualifier(String typeName) {
/*  675 */     return this.qualifiers.containsKey(typeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public AutowireCandidateQualifier getQualifier(String typeName) {
/*  683 */     return this.qualifiers.get(typeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<AutowireCandidateQualifier> getQualifiers() {
/*  691 */     return new LinkedHashSet<>(this.qualifiers.values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyQualifiersFrom(AbstractBeanDefinition source) {
/*  699 */     Assert.notNull(source, "Source must not be null");
/*  700 */     this.qualifiers.putAll(source.qualifiers);
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
/*      */   public void setInstanceSupplier(@Nullable Supplier<?> instanceSupplier) {
/*  714 */     this.instanceSupplier = instanceSupplier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Supplier<?> getInstanceSupplier() {
/*  723 */     return this.instanceSupplier;
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
/*      */   public void setNonPublicAccessAllowed(boolean nonPublicAccessAllowed) {
/*  738 */     this.nonPublicAccessAllowed = nonPublicAccessAllowed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNonPublicAccessAllowed() {
/*  745 */     return this.nonPublicAccessAllowed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLenientConstructorResolution(boolean lenientConstructorResolution) {
/*  755 */     this.lenientConstructorResolution = lenientConstructorResolution;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLenientConstructorResolution() {
/*  762 */     return this.lenientConstructorResolution;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFactoryBeanName(@Nullable String factoryBeanName) {
/*  772 */     this.factoryBeanName = factoryBeanName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getFactoryBeanName() {
/*  781 */     return this.factoryBeanName;
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
/*      */   public void setFactoryMethodName(@Nullable String factoryMethodName) {
/*  794 */     this.factoryMethodName = factoryMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getFactoryMethodName() {
/*  803 */     return this.factoryMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
/*  810 */     this.constructorArgumentValues = constructorArgumentValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConstructorArgumentValues getConstructorArgumentValues() {
/*  818 */     if (this.constructorArgumentValues == null) {
/*  819 */       this.constructorArgumentValues = new ConstructorArgumentValues();
/*      */     }
/*  821 */     return this.constructorArgumentValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasConstructorArgumentValues() {
/*  829 */     return (this.constructorArgumentValues != null && !this.constructorArgumentValues.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyValues(MutablePropertyValues propertyValues) {
/*  836 */     this.propertyValues = propertyValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MutablePropertyValues getPropertyValues() {
/*  844 */     if (this.propertyValues == null) {
/*  845 */       this.propertyValues = new MutablePropertyValues();
/*      */     }
/*  847 */     return this.propertyValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasPropertyValues() {
/*  856 */     return (this.propertyValues != null && !this.propertyValues.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMethodOverrides(MethodOverrides methodOverrides) {
/*  863 */     this.methodOverrides = methodOverrides;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MethodOverrides getMethodOverrides() {
/*  872 */     if (this.methodOverrides == null) {
/*  873 */       this.methodOverrides = new MethodOverrides();
/*      */     }
/*  875 */     return this.methodOverrides;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasMethodOverrides() {
/*  883 */     return (this.methodOverrides != null && !this.methodOverrides.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInitMethodName(@Nullable String initMethodName) {
/*  892 */     this.initMethodName = initMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getInitMethodName() {
/*  901 */     return this.initMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnforceInitMethod(boolean enforceInitMethod) {
/*  910 */     this.enforceInitMethod = enforceInitMethod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnforceInitMethod() {
/*  918 */     return this.enforceInitMethod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestroyMethodName(@Nullable String destroyMethodName) {
/*  927 */     this.destroyMethodName = destroyMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getDestroyMethodName() {
/*  936 */     return this.destroyMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnforceDestroyMethod(boolean enforceDestroyMethod) {
/*  945 */     this.enforceDestroyMethod = enforceDestroyMethod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnforceDestroyMethod() {
/*  953 */     return this.enforceDestroyMethod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSynthetic(boolean synthetic) {
/*  962 */     this.synthetic = synthetic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSynthetic() {
/*  970 */     return this.synthetic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRole(int role) {
/*  978 */     this.role = role;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRole() {
/*  986 */     return this.role;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDescription(@Nullable String description) {
/*  994 */     this.description = description;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getDescription() {
/* 1003 */     return this.description;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResource(@Nullable Resource resource) {
/* 1011 */     this.resource = resource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Resource getResource() {
/* 1019 */     return this.resource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResourceDescription(@Nullable String resourceDescription) {
/* 1027 */     this.resource = (resourceDescription != null) ? (Resource)new DescriptiveResource(resourceDescription) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getResourceDescription() {
/* 1037 */     return (this.resource != null) ? this.resource.getDescription() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOriginatingBeanDefinition(BeanDefinition originatingBd) {
/* 1044 */     this.resource = (Resource)new BeanDefinitionResource(originatingBd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public BeanDefinition getOriginatingBeanDefinition() {
/* 1056 */     return (this.resource instanceof BeanDefinitionResource) ? ((BeanDefinitionResource)this.resource)
/* 1057 */       .getBeanDefinition() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void validate() throws BeanDefinitionValidationException {
/* 1065 */     if (hasMethodOverrides() && getFactoryMethodName() != null) {
/* 1066 */       throw new BeanDefinitionValidationException("Cannot combine static factory method with method overrides: the static factory method must create the instance");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1071 */     if (hasBeanClass()) {
/* 1072 */       prepareMethodOverrides();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void prepareMethodOverrides() throws BeanDefinitionValidationException {
/* 1083 */     if (hasMethodOverrides()) {
/* 1084 */       Set<MethodOverride> overrides = getMethodOverrides().getOverrides();
/* 1085 */       synchronized (overrides) {
/* 1086 */         for (MethodOverride mo : overrides) {
/* 1087 */           prepareMethodOverride(mo);
/*      */         }
/*      */       } 
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
/*      */   protected void prepareMethodOverride(MethodOverride mo) throws BeanDefinitionValidationException {
/* 1101 */     int count = ClassUtils.getMethodCountForName(getBeanClass(), mo.getMethodName());
/* 1102 */     if (count == 0) {
/* 1103 */       throw new BeanDefinitionValidationException("Invalid method override: no method with name '" + mo
/* 1104 */           .getMethodName() + "' on class [" + 
/* 1105 */           getBeanClassName() + "]");
/*      */     }
/* 1107 */     if (count == 1)
/*      */     {
/* 1109 */       mo.setOverloaded(false);
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
/*      */   public Object clone() {
/* 1121 */     return cloneBeanDefinition();
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
/*      */   public abstract AbstractBeanDefinition cloneBeanDefinition();
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
/*      */   public boolean equals(Object other) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: if_acmpne -> 7
/*      */     //   5: iconst_1
/*      */     //   6: ireturn
/*      */     //   7: aload_1
/*      */     //   8: instanceof org/springframework/beans/factory/support/AbstractBeanDefinition
/*      */     //   11: ifne -> 16
/*      */     //   14: iconst_0
/*      */     //   15: ireturn
/*      */     //   16: aload_1
/*      */     //   17: checkcast org/springframework/beans/factory/support/AbstractBeanDefinition
/*      */     //   20: astore_2
/*      */     //   21: aload_0
/*      */     //   22: invokevirtual getBeanClassName : ()Ljava/lang/String;
/*      */     //   25: aload_2
/*      */     //   26: invokevirtual getBeanClassName : ()Ljava/lang/String;
/*      */     //   29: invokestatic nullSafeEquals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*      */     //   32: istore_3
/*      */     //   33: iload_3
/*      */     //   34: aload_0
/*      */     //   35: getfield scope : Ljava/lang/String;
/*      */     //   38: aload_2
/*      */     //   39: getfield scope : Ljava/lang/String;
/*      */     //   42: invokestatic nullSafeEquals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*      */     //   45: iand
/*      */     //   46: dup
/*      */     //   47: istore_3
/*      */     //   48: istore_3
/*      */     //   49: iload_3
/*      */     //   50: aload_0
/*      */     //   51: getfield abstractFlag : Z
/*      */     //   54: aload_2
/*      */     //   55: getfield abstractFlag : Z
/*      */     //   58: if_icmpne -> 65
/*      */     //   61: iconst_1
/*      */     //   62: goto -> 66
/*      */     //   65: iconst_0
/*      */     //   66: iand
/*      */     //   67: dup
/*      */     //   68: istore_3
/*      */     //   69: istore_3
/*      */     //   70: iload_3
/*      */     //   71: aload_0
/*      */     //   72: getfield lazyInit : Z
/*      */     //   75: aload_2
/*      */     //   76: getfield lazyInit : Z
/*      */     //   79: if_icmpne -> 86
/*      */     //   82: iconst_1
/*      */     //   83: goto -> 87
/*      */     //   86: iconst_0
/*      */     //   87: iand
/*      */     //   88: dup
/*      */     //   89: istore_3
/*      */     //   90: istore_3
/*      */     //   91: iload_3
/*      */     //   92: aload_0
/*      */     //   93: getfield autowireMode : I
/*      */     //   96: aload_2
/*      */     //   97: getfield autowireMode : I
/*      */     //   100: if_icmpne -> 107
/*      */     //   103: iconst_1
/*      */     //   104: goto -> 108
/*      */     //   107: iconst_0
/*      */     //   108: iand
/*      */     //   109: dup
/*      */     //   110: istore_3
/*      */     //   111: istore_3
/*      */     //   112: iload_3
/*      */     //   113: aload_0
/*      */     //   114: getfield dependencyCheck : I
/*      */     //   117: aload_2
/*      */     //   118: getfield dependencyCheck : I
/*      */     //   121: if_icmpne -> 128
/*      */     //   124: iconst_1
/*      */     //   125: goto -> 129
/*      */     //   128: iconst_0
/*      */     //   129: iand
/*      */     //   130: dup
/*      */     //   131: istore_3
/*      */     //   132: istore_3
/*      */     //   133: iload_3
/*      */     //   134: aload_0
/*      */     //   135: getfield dependsOn : [Ljava/lang/String;
/*      */     //   138: aload_2
/*      */     //   139: getfield dependsOn : [Ljava/lang/String;
/*      */     //   142: invokestatic equals : ([Ljava/lang/Object;[Ljava/lang/Object;)Z
/*      */     //   145: iand
/*      */     //   146: dup
/*      */     //   147: istore_3
/*      */     //   148: istore_3
/*      */     //   149: iload_3
/*      */     //   150: aload_0
/*      */     //   151: getfield autowireCandidate : Z
/*      */     //   154: aload_2
/*      */     //   155: getfield autowireCandidate : Z
/*      */     //   158: if_icmpne -> 165
/*      */     //   161: iconst_1
/*      */     //   162: goto -> 166
/*      */     //   165: iconst_0
/*      */     //   166: iand
/*      */     //   167: dup
/*      */     //   168: istore_3
/*      */     //   169: istore_3
/*      */     //   170: iload_3
/*      */     //   171: aload_0
/*      */     //   172: getfield qualifiers : Ljava/util/Map;
/*      */     //   175: aload_2
/*      */     //   176: getfield qualifiers : Ljava/util/Map;
/*      */     //   179: invokestatic nullSafeEquals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*      */     //   182: iand
/*      */     //   183: dup
/*      */     //   184: istore_3
/*      */     //   185: istore_3
/*      */     //   186: iload_3
/*      */     //   187: aload_0
/*      */     //   188: getfield primary : Z
/*      */     //   191: aload_2
/*      */     //   192: getfield primary : Z
/*      */     //   195: if_icmpne -> 202
/*      */     //   198: iconst_1
/*      */     //   199: goto -> 203
/*      */     //   202: iconst_0
/*      */     //   203: iand
/*      */     //   204: dup
/*      */     //   205: istore_3
/*      */     //   206: istore_3
/*      */     //   207: iload_3
/*      */     //   208: aload_0
/*      */     //   209: getfield nonPublicAccessAllowed : Z
/*      */     //   212: aload_2
/*      */     //   213: getfield nonPublicAccessAllowed : Z
/*      */     //   216: if_icmpne -> 223
/*      */     //   219: iconst_1
/*      */     //   220: goto -> 224
/*      */     //   223: iconst_0
/*      */     //   224: iand
/*      */     //   225: dup
/*      */     //   226: istore_3
/*      */     //   227: istore_3
/*      */     //   228: iload_3
/*      */     //   229: aload_0
/*      */     //   230: getfield lenientConstructorResolution : Z
/*      */     //   233: aload_2
/*      */     //   234: getfield lenientConstructorResolution : Z
/*      */     //   237: if_icmpne -> 244
/*      */     //   240: iconst_1
/*      */     //   241: goto -> 245
/*      */     //   244: iconst_0
/*      */     //   245: iand
/*      */     //   246: dup
/*      */     //   247: istore_3
/*      */     //   248: istore_3
/*      */     //   249: iload_3
/*      */     //   250: aload_0
/*      */     //   251: getfield constructorArgumentValues : Lorg/springframework/beans/factory/config/ConstructorArgumentValues;
/*      */     //   254: aload_2
/*      */     //   255: getfield constructorArgumentValues : Lorg/springframework/beans/factory/config/ConstructorArgumentValues;
/*      */     //   258: invokestatic nullSafeEquals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*      */     //   261: iand
/*      */     //   262: dup
/*      */     //   263: istore_3
/*      */     //   264: istore_3
/*      */     //   265: iload_3
/*      */     //   266: aload_0
/*      */     //   267: getfield propertyValues : Lorg/springframework/beans/MutablePropertyValues;
/*      */     //   270: aload_2
/*      */     //   271: getfield propertyValues : Lorg/springframework/beans/MutablePropertyValues;
/*      */     //   274: invokestatic nullSafeEquals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*      */     //   277: iand
/*      */     //   278: dup
/*      */     //   279: istore_3
/*      */     //   280: istore_3
/*      */     //   281: iload_3
/*      */     //   282: aload_0
/*      */     //   283: getfield methodOverrides : Lorg/springframework/beans/factory/support/MethodOverrides;
/*      */     //   286: aload_2
/*      */     //   287: getfield methodOverrides : Lorg/springframework/beans/factory/support/MethodOverrides;
/*      */     //   290: invokestatic nullSafeEquals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*      */     //   293: iand
/*      */     //   294: dup
/*      */     //   295: istore_3
/*      */     //   296: istore_3
/*      */     //   297: iload_3
/*      */     //   298: aload_0
/*      */     //   299: getfield factoryBeanName : Ljava/lang/String;
/*      */     //   302: aload_2
/*      */     //   303: getfield factoryBeanName : Ljava/lang/String;
/*      */     //   306: invokestatic nullSafeEquals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*      */     //   309: iand
/*      */     //   310: dup
/*      */     //   311: istore_3
/*      */     //   312: istore_3
/*      */     //   313: iload_3
/*      */     //   314: aload_0
/*      */     //   315: getfield factoryMethodName : Ljava/lang/String;
/*      */     //   318: aload_2
/*      */     //   319: getfield factoryMethodName : Ljava/lang/String;
/*      */     //   322: invokestatic nullSafeEquals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*      */     //   325: iand
/*      */     //   326: dup
/*      */     //   327: istore_3
/*      */     //   328: istore_3
/*      */     //   329: iload_3
/*      */     //   330: aload_0
/*      */     //   331: getfield initMethodName : Ljava/lang/String;
/*      */     //   334: aload_2
/*      */     //   335: getfield initMethodName : Ljava/lang/String;
/*      */     //   338: invokestatic nullSafeEquals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*      */     //   341: iand
/*      */     //   342: dup
/*      */     //   343: istore_3
/*      */     //   344: istore_3
/*      */     //   345: iload_3
/*      */     //   346: aload_0
/*      */     //   347: getfield enforceInitMethod : Z
/*      */     //   350: aload_2
/*      */     //   351: getfield enforceInitMethod : Z
/*      */     //   354: if_icmpne -> 361
/*      */     //   357: iconst_1
/*      */     //   358: goto -> 362
/*      */     //   361: iconst_0
/*      */     //   362: iand
/*      */     //   363: dup
/*      */     //   364: istore_3
/*      */     //   365: istore_3
/*      */     //   366: iload_3
/*      */     //   367: aload_0
/*      */     //   368: getfield destroyMethodName : Ljava/lang/String;
/*      */     //   371: aload_2
/*      */     //   372: getfield destroyMethodName : Ljava/lang/String;
/*      */     //   375: invokestatic nullSafeEquals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*      */     //   378: iand
/*      */     //   379: dup
/*      */     //   380: istore_3
/*      */     //   381: istore_3
/*      */     //   382: iload_3
/*      */     //   383: aload_0
/*      */     //   384: getfield enforceDestroyMethod : Z
/*      */     //   387: aload_2
/*      */     //   388: getfield enforceDestroyMethod : Z
/*      */     //   391: if_icmpne -> 398
/*      */     //   394: iconst_1
/*      */     //   395: goto -> 399
/*      */     //   398: iconst_0
/*      */     //   399: iand
/*      */     //   400: dup
/*      */     //   401: istore_3
/*      */     //   402: istore_3
/*      */     //   403: iload_3
/*      */     //   404: aload_0
/*      */     //   405: getfield synthetic : Z
/*      */     //   408: aload_2
/*      */     //   409: getfield synthetic : Z
/*      */     //   412: if_icmpne -> 419
/*      */     //   415: iconst_1
/*      */     //   416: goto -> 420
/*      */     //   419: iconst_0
/*      */     //   420: iand
/*      */     //   421: dup
/*      */     //   422: istore_3
/*      */     //   423: istore_3
/*      */     //   424: iload_3
/*      */     //   425: aload_0
/*      */     //   426: getfield role : I
/*      */     //   429: aload_2
/*      */     //   430: getfield role : I
/*      */     //   433: if_icmpne -> 440
/*      */     //   436: iconst_1
/*      */     //   437: goto -> 441
/*      */     //   440: iconst_0
/*      */     //   441: iand
/*      */     //   442: dup
/*      */     //   443: istore_3
/*      */     //   444: istore_3
/*      */     //   445: iload_3
/*      */     //   446: ifeq -> 461
/*      */     //   449: aload_0
/*      */     //   450: aload_1
/*      */     //   451: invokespecial equals : (Ljava/lang/Object;)Z
/*      */     //   454: ifeq -> 461
/*      */     //   457: iconst_1
/*      */     //   458: goto -> 462
/*      */     //   461: iconst_0
/*      */     //   462: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1133	-> 0
/*      */     //   #1134	-> 5
/*      */     //   #1136	-> 7
/*      */     //   #1137	-> 14
/*      */     //   #1139	-> 16
/*      */     //   #1140	-> 21
/*      */     //   #1141	-> 33
/*      */     //   #1142	-> 49
/*      */     //   #1143	-> 70
/*      */     //   #1144	-> 91
/*      */     //   #1145	-> 112
/*      */     //   #1146	-> 133
/*      */     //   #1147	-> 149
/*      */     //   #1148	-> 170
/*      */     //   #1149	-> 186
/*      */     //   #1150	-> 207
/*      */     //   #1151	-> 228
/*      */     //   #1152	-> 249
/*      */     //   #1153	-> 265
/*      */     //   #1154	-> 281
/*      */     //   #1155	-> 297
/*      */     //   #1156	-> 313
/*      */     //   #1157	-> 329
/*      */     //   #1158	-> 345
/*      */     //   #1159	-> 366
/*      */     //   #1160	-> 382
/*      */     //   #1161	-> 403
/*      */     //   #1162	-> 424
/*      */     //   #1163	-> 445
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	463	0	this	Lorg/springframework/beans/factory/support/AbstractBeanDefinition;
/*      */     //   0	463	1	other	Ljava/lang/Object;
/*      */     //   21	442	2	that	Lorg/springframework/beans/factory/support/AbstractBeanDefinition;
/*      */     //   33	430	3	rtn	Z
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
/*      */   public int hashCode() {
/* 1168 */     int hashCode = ObjectUtils.nullSafeHashCode(getBeanClassName());
/* 1169 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.scope);
/* 1170 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.constructorArgumentValues);
/* 1171 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.propertyValues);
/* 1172 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryBeanName);
/* 1173 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryMethodName);
/* 1174 */     hashCode = 29 * hashCode + super.hashCode();
/* 1175 */     return hashCode;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1180 */     StringBuilder sb = new StringBuilder("class [");
/* 1181 */     sb.append(getBeanClassName()).append("]");
/* 1182 */     sb.append("; scope=").append(this.scope);
/* 1183 */     sb.append("; abstract=").append(this.abstractFlag);
/* 1184 */     sb.append("; lazyInit=").append(this.lazyInit);
/* 1185 */     sb.append("; autowireMode=").append(this.autowireMode);
/* 1186 */     sb.append("; dependencyCheck=").append(this.dependencyCheck);
/* 1187 */     sb.append("; autowireCandidate=").append(this.autowireCandidate);
/* 1188 */     sb.append("; primary=").append(this.primary);
/* 1189 */     sb.append("; factoryBeanName=").append(this.factoryBeanName);
/* 1190 */     sb.append("; factoryMethodName=").append(this.factoryMethodName);
/* 1191 */     sb.append("; initMethodName=").append(this.initMethodName);
/* 1192 */     sb.append("; destroyMethodName=").append(this.destroyMethodName);
/* 1193 */     if (this.resource != null) {
/* 1194 */       sb.append("; defined in ").append(this.resource.getDescription());
/*      */     }
/* 1196 */     return sb.toString();
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/AbstractBeanDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */