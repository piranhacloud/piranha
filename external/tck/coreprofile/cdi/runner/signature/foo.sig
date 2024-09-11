#Signature file v4.1
#Version 4.0.0-SNAPSHOT

CLSS public abstract interface !annotation jakarta.decorator.Decorator
 anno 0 jakarta.enterprise.inject.Stereotype()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.decorator.Delegate
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[FIELD, PARAMETER])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.enterprise.context.ApplicationScoped
 anno 0 jakarta.enterprise.context.NormalScope(boolean passivating=false)
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.context.ApplicationScoped$Literal
 outer jakarta.enterprise.context.ApplicationScoped
cons public init()
fld public final static jakarta.enterprise.context.ApplicationScoped$Literal INSTANCE
intf jakarta.enterprise.context.ApplicationScoped
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.context.ApplicationScoped>
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.context.BeforeDestroyed
 anno 0 jakarta.inject.Qualifier()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, PARAMETER, FIELD])
innr public final static Literal
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> value()

CLSS public final static jakarta.enterprise.context.BeforeDestroyed$Literal
 outer jakarta.enterprise.context.BeforeDestroyed
fld public final static jakarta.enterprise.context.BeforeDestroyed$Literal APPLICATION
fld public final static jakarta.enterprise.context.BeforeDestroyed$Literal CONVERSATION
fld public final static jakarta.enterprise.context.BeforeDestroyed$Literal REQUEST
fld public final static jakarta.enterprise.context.BeforeDestroyed$Literal SESSION
intf jakarta.enterprise.context.BeforeDestroyed
meth public java.lang.Class<? extends java.lang.annotation.Annotation> value()
meth public static jakarta.enterprise.context.BeforeDestroyed$Literal of(java.lang.Class<? extends java.lang.annotation.Annotation>)
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.context.BeforeDestroyed>
hfds serialVersionUID,value

CLSS public jakarta.enterprise.contextx.BusyConversationException
cons public init(java.lang.Integer)
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.enterprise.context.ContextException
hfds serialVersionUID

CLSS public jakarta.enterprise.context.ContextException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public jakarta.enterprise.context.ContextNotActiveException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.enterprise.context.ContextException
hfds serialVersionUID

CLSS public abstract interface jakarta.enterprise.context.Conversation
meth public abstract boolean isTransient()
meth public abstract java.lang.String getId()
meth public abstract long getTimeout()
meth public abstract void begin()
meth public abstract void begin(java.lang.String)
meth public abstract void end()
meth public abstract void setTimeout(long)

CLSS public abstract interface !annotation jakarta.enterprise.context.ConversationScoped
 anno 0 jakarta.enterprise.context.NormalScope(boolean passivating=true)
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.context.ConversationScoped$Literal
 outer jakarta.enterprise.context.ConversationScoped
cons public init()
fld public final static jakarta.enterprise.context.ConversationScoped$Literal INSTANCE
intf jakarta.enterprise.context.ConversationScoped
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.context.ConversationScoped>
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.context.Dependent
 anno 0 jakarta.inject.Scope()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, TYPE, FIELD])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.context.Dependent$Literal
 outer jakarta.enterprise.context.Dependent
cons public init()
fld public final static jakarta.enterprise.context.Dependent$Literal INSTANCE
intf jakarta.enterprise.context.Dependent
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.context.Dependent>
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.context.Destroyed
 anno 0 jakarta.inject.Qualifier()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, PARAMETER, FIELD])
innr public final static Literal
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> value()

CLSS public final static jakarta.enterprise.context.Destroyed$Literal
 outer jakarta.enterprise.context.Destroyed
fld public final static jakarta.enterprise.context.Destroyed$Literal APPLICATION
fld public final static jakarta.enterprise.context.Destroyed$Literal CONVERSATION
fld public final static jakarta.enterprise.context.Destroyed$Literal REQUEST
fld public final static jakarta.enterprise.context.Destroyed$Literal SESSION
intf jakarta.enterprise.context.Destroyed
meth public java.lang.Class<? extends java.lang.annotation.Annotation> value()
meth public static jakarta.enterprise.context.Destroyed$Literal of(java.lang.Class<? extends java.lang.annotation.Annotation>)
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.context.Destroyed>
hfds serialVersionUID,value

CLSS public abstract interface !annotation jakarta.enterprise.context.Initialized
 anno 0 jakarta.inject.Qualifier()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, PARAMETER, FIELD])
innr public final static Literal
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> value()

CLSS public final static jakarta.enterprise.context.Initialized$Literal
 outer jakarta.enterprise.context.Initialized
fld public final static jakarta.enterprise.context.Initialized$Literal APPLICATION
fld public final static jakarta.enterprise.context.Initialized$Literal CONVERSATION
fld public final static jakarta.enterprise.context.Initialized$Literal REQUEST
fld public final static jakarta.enterprise.context.Initialized$Literal SESSION
intf jakarta.enterprise.context.Initialized
meth public java.lang.Class<? extends java.lang.annotation.Annotation> value()
meth public static jakarta.enterprise.context.Initialized$Literal of(java.lang.Class<? extends java.lang.annotation.Annotation>)
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.context.Initialized>
hfds serialVersionUID,value

CLSS public jakarta.enterprise.context.NonexistentConversationException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.enterprise.context.ContextException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.context.NormalScope
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean passivating()

CLSS public abstract interface !annotation jakarta.enterprise.context.RequestScoped
 anno 0 jakarta.enterprise.context.NormalScope(boolean passivating=false)
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.context.RequestScoped$Literal
 outer jakarta.enterprise.context.RequestScoped
cons public init()
fld public final static jakarta.enterprise.context.RequestScoped$Literal INSTANCE
intf jakarta.enterprise.context.RequestScoped
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.context.RequestScoped>
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.context.SessionScoped
 anno 0 jakarta.enterprise.context.NormalScope(boolean passivating=true)
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.context.SessionScoped$Literal
 outer jakarta.enterprise.context.SessionScoped
cons public init()
fld public final static jakarta.enterprise.context.SessionScoped$Literal INSTANCE
intf jakarta.enterprise.context.SessionScoped
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.context.SessionScoped>
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.context.control.ActivateRequestContext
 anno 0 jakarta.interceptor.InterceptorBinding()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface jakarta.enterprise.context.control.RequestContextController
meth public abstract boolean activate()
meth public abstract void deactivate()

CLSS public abstract interface jakarta.enterprise.context.spi.AlterableContext
intf jakarta.enterprise.context.spi.Context
meth public abstract void destroy(jakarta.enterprise.context.spi.Contextual<?>)

CLSS public abstract interface jakarta.enterprise.context.spi.Context
meth public abstract <%0 extends java.lang.Object> {%%0} get(jakarta.enterprise.context.spi.Contextual<{%%0}>)
meth public abstract <%0 extends java.lang.Object> {%%0} get(jakarta.enterprise.context.spi.Contextual<{%%0}>,jakarta.enterprise.context.spi.CreationalContext<{%%0}>)
meth public abstract boolean isActive()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> getScope()

CLSS public abstract interface jakarta.enterprise.context.spi.Contextual<%0 extends java.lang.Object>
meth public abstract void destroy({jakarta.enterprise.context.spi.Contextual%0},jakarta.enterprise.context.spi.CreationalContext<{jakarta.enterprise.context.spi.Contextual%0}>)
meth public abstract {jakarta.enterprise.context.spi.Contextual%0} create(jakarta.enterprise.context.spi.CreationalContext<{jakarta.enterprise.context.spi.Contextual%0}>)

CLSS public abstract interface jakarta.enterprise.context.spi.CreationalContext<%0 extends java.lang.Object>
meth public abstract void push({jakarta.enterprise.context.spi.CreationalContext%0})
meth public abstract void release()

CLSS public abstract interface jakarta.enterprise.event.Event<%0 extends java.lang.Object>
meth public abstract !varargs <%0 extends {jakarta.enterprise.event.Event%0}> jakarta.enterprise.event.Event<{%%0}> select(jakarta.enterprise.util.TypeLiteral<{%%0}>,java.lang.annotation.Annotation[])
meth public abstract !varargs <%0 extends {jakarta.enterprise.event.Event%0}> jakarta.enterprise.event.Event<{%%0}> select(java.lang.Class<{%%0}>,java.lang.annotation.Annotation[])
meth public abstract !varargs jakarta.enterprise.event.Event<{jakarta.enterprise.event.Event%0}> select(java.lang.annotation.Annotation[])
meth public abstract <%0 extends {jakarta.enterprise.event.Event%0}> java.util.concurrent.CompletionStage<{%%0}> fireAsync({%%0})
meth public abstract <%0 extends {jakarta.enterprise.event.Event%0}> java.util.concurrent.CompletionStage<{%%0}> fireAsync({%%0},jakarta.enterprise.event.NotificationOptions)
meth public abstract void fire({jakarta.enterprise.event.Event%0})

CLSS public abstract interface jakarta.enterprise.event.NotificationOptions
innr public abstract interface static Builder
meth public abstract java.lang.Object get(java.lang.String)
meth public abstract java.util.concurrent.Executor getExecutor()
meth public static jakarta.enterprise.event.NotificationOptions of(java.lang.String,java.lang.Object)
meth public static jakarta.enterprise.event.NotificationOptions ofExecutor(java.util.concurrent.Executor)
meth public static jakarta.enterprise.event.NotificationOptions$Builder builder()

CLSS public abstract interface static jakarta.enterprise.event.NotificationOptions$Builder
 outer jakarta.enterprise.event.NotificationOptions
meth public abstract jakarta.enterprise.event.NotificationOptions build()
meth public abstract jakarta.enterprise.event.NotificationOptions$Builder set(java.lang.String,java.lang.Object)
meth public abstract jakarta.enterprise.event.NotificationOptions$Builder setExecutor(java.util.concurrent.Executor)

CLSS public jakarta.enterprise.event.ObserverException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.event.Observes
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault jakarta.enterprise.event.Reception notifyObserver()
meth public abstract !hasdefault jakarta.enterprise.event.TransactionPhase during()

CLSS public abstract interface !annotation jakarta.enterprise.event.ObservesAsync
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault jakarta.enterprise.event.Reception notifyObserver()

CLSS public final !enum jakarta.enterprise.event.Reception
fld public final static jakarta.enterprise.event.Reception ALWAYS
fld public final static jakarta.enterprise.event.Reception IF_EXISTS
meth public static jakarta.enterprise.event.Reception valueOf(java.lang.String)
meth public static jakarta.enterprise.event.Reception[] values()
supr java.lang.Enum<jakarta.enterprise.event.Reception>

CLSS public jakarta.enterprise.event.Shutdown
cons public init()
supr java.lang.Object

CLSS public jakarta.enterprise.event.Startup
cons public init()
supr java.lang.Object

CLSS public final !enum jakarta.enterprise.event.TransactionPhase
fld public final static jakarta.enterprise.event.TransactionPhase AFTER_COMPLETION
fld public final static jakarta.enterprise.event.TransactionPhase AFTER_FAILURE
fld public final static jakarta.enterprise.event.TransactionPhase AFTER_SUCCESS
fld public final static jakarta.enterprise.event.TransactionPhase BEFORE_COMPLETION
fld public final static jakarta.enterprise.event.TransactionPhase IN_PROGRESS
meth public static jakarta.enterprise.event.TransactionPhase valueOf(java.lang.String)
meth public static jakarta.enterprise.event.TransactionPhase[] values()
supr java.lang.Enum<jakarta.enterprise.event.TransactionPhase>

CLSS public abstract interface !annotation jakarta.enterprise.inject.Alternative
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.inject.Alternative$Literal
 outer jakarta.enterprise.inject.Alternative
cons public init()
fld public final static jakarta.enterprise.inject.Alternative$Literal INSTANCE
intf jakarta.enterprise.inject.Alternative
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.inject.Alternative>
hfds serialVersionUID

CLSS public jakarta.enterprise.inject.AmbiguousResolutionException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.enterprise.inject.ResolutionException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.inject.Any
 anno 0 jakarta.inject.Qualifier()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD, PARAMETER])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.inject.Any$Literal
 outer jakarta.enterprise.inject.Any
cons public init()
fld public final static jakarta.enterprise.inject.Any$Literal INSTANCE
intf jakarta.enterprise.inject.Any
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.inject.Any>
hfds serialVersionUID

CLSS public jakarta.enterprise.inject.CreationException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.enterprise.inject.InjectionException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.inject.Decorated
 anno 0 jakarta.inject.Qualifier()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER, FIELD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.enterprise.inject.Default
 anno 0 jakarta.inject.Qualifier()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, PARAMETER, FIELD])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.inject.Default$Literal
 outer jakarta.enterprise.inject.Default
cons public init()
fld public final static jakarta.enterprise.inject.Default$Literal INSTANCE
intf jakarta.enterprise.inject.Default
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.inject.Default>
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.inject.Disposes
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation

CLSS public jakarta.enterprise.inject.IllegalProductException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.enterprise.inject.InjectionException
hfds serialVersionUID

CLSS public jakarta.enterprise.inject.InjectionException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public abstract interface jakarta.enterprise.inject.Instance<%0 extends java.lang.Object>
innr public abstract interface static Handle
intf jakarta.inject.Provider<{jakarta.enterprise.inject.Instance%0}>
intf java.lang.Iterable<{jakarta.enterprise.inject.Instance%0}>
meth public abstract !varargs <%0 extends {jakarta.enterprise.inject.Instance%0}> jakarta.enterprise.inject.Instance<{%%0}> select(jakarta.enterprise.util.TypeLiteral<{%%0}>,java.lang.annotation.Annotation[])
meth public abstract !varargs <%0 extends {jakarta.enterprise.inject.Instance%0}> jakarta.enterprise.inject.Instance<{%%0}> select(java.lang.Class<{%%0}>,java.lang.annotation.Annotation[])
meth public abstract !varargs jakarta.enterprise.inject.Instance<{jakarta.enterprise.inject.Instance%0}> select(java.lang.annotation.Annotation[])
meth public abstract boolean isAmbiguous()
meth public abstract boolean isUnsatisfied()
meth public abstract jakarta.enterprise.inject.Instance$Handle<{jakarta.enterprise.inject.Instance%0}> getHandle()
meth public abstract java.lang.Iterable<? extends jakarta.enterprise.inject.Instance$Handle<{jakarta.enterprise.inject.Instance%0}>> handles()
meth public abstract void destroy({jakarta.enterprise.inject.Instance%0})
meth public boolean isResolvable()
meth public java.util.stream.Stream<? extends jakarta.enterprise.inject.Instance$Handle<{jakarta.enterprise.inject.Instance%0}>> handlesStream()
meth public java.util.stream.Stream<{jakarta.enterprise.inject.Instance%0}> stream()

CLSS public abstract interface static jakarta.enterprise.inject.Instance$Handle<%0 extends java.lang.Object>
 outer jakarta.enterprise.inject.Instance
intf java.lang.AutoCloseable
meth public abstract jakarta.enterprise.inject.spi.Bean<{jakarta.enterprise.inject.Instance$Handle%0}> getBean()
meth public abstract void close()
meth public abstract void destroy()
meth public abstract {jakarta.enterprise.inject.Instance$Handle%0} get()

CLSS public abstract interface !annotation jakarta.enterprise.inject.Intercepted
 anno 0 jakarta.inject.Qualifier()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER, FIELD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.enterprise.inject.Model
 anno 0 jakarta.enterprise.context.RequestScoped()
 anno 0 jakarta.enterprise.inject.Stereotype()
 anno 0 jakarta.inject.Named(java.lang.String value="")
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.enterprise.inject.Produces
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD])
intf java.lang.annotation.Annotation

CLSS public jakarta.enterprise.inject.ResolutionException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.enterprise.inject.InjectionException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.inject.Specializes
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.inject.Specializes$Literal
 outer jakarta.enterprise.inject.Specializes
cons public init()
fld public final static jakarta.enterprise.inject.Specializes$Literal INSTANCE
intf jakarta.enterprise.inject.Specializes
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.inject.Specializes>
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.inject.Stereotype
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.enterprise.inject.TransientReference
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.inject.TransientReference$Literal
 outer jakarta.enterprise.inject.TransientReference
cons public init()
fld public final static jakarta.enterprise.inject.TransientReference$Literal INSTANCE
intf jakarta.enterprise.inject.TransientReference
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.inject.TransientReference>
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.inject.Typed
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[FIELD, METHOD, TYPE])
innr public final static Literal
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<?>[] value()

CLSS public final static jakarta.enterprise.inject.Typed$Literal
 outer jakarta.enterprise.inject.Typed
fld public final static jakarta.enterprise.inject.Typed$Literal INSTANCE
intf jakarta.enterprise.inject.Typed
meth public java.lang.Class<?>[] value()
meth public static jakarta.enterprise.inject.Typed$Literal of(java.lang.Class<?>[])
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.inject.Typed>
hfds serialVersionUID,value

CLSS public jakarta.enterprise.inject.UnproxyableResolutionException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.enterprise.inject.ResolutionException
hfds serialVersionUID

CLSS public jakarta.enterprise.inject.UnsatisfiedResolutionException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.enterprise.inject.ResolutionException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.inject.Vetoed
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, PACKAGE])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.inject.Vetoed$Literal
 outer jakarta.enterprise.inject.Vetoed
cons public init()
fld public final static jakarta.enterprise.inject.Vetoed$Literal INSTANCE
intf jakarta.enterprise.inject.Vetoed
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.inject.Vetoed>
hfds serialVersionUID

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,boolean)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,boolean[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,byte)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,byte[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,char)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,char[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,double)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,double[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,float)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,float[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,int)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,int[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,jakarta.enterprise.lang.model.AnnotationInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,jakarta.enterprise.lang.model.AnnotationInfo[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,jakarta.enterprise.lang.model.AnnotationMember)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,jakarta.enterprise.lang.model.declarations.ClassInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,jakarta.enterprise.lang.model.declarations.ClassInfo,java.lang.String)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,jakarta.enterprise.lang.model.declarations.ClassInfo,java.lang.String[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,jakarta.enterprise.lang.model.declarations.ClassInfo[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,jakarta.enterprise.lang.model.types.Type)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,jakarta.enterprise.lang.model.types.Type[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,java.lang.Class<? extends java.lang.Enum<?>>,java.lang.String)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,java.lang.Class<? extends java.lang.Enum<?>>,java.lang.String[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,java.lang.Class<?>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,java.lang.Class<?>[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,java.lang.Enum<?>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,java.lang.Enum<?>[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,java.lang.String)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,java.lang.String[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,java.lang.annotation.Annotation[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,long)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,long[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,short)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder member(java.lang.String,short[])
meth public abstract jakarta.enterprise.lang.model.AnnotationInfo build()
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(boolean)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(boolean[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(byte)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(byte[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(char)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(char[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(double)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(double[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(float)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(float[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(int)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(int[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(jakarta.enterprise.lang.model.AnnotationInfo)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(jakarta.enterprise.lang.model.AnnotationInfo[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(jakarta.enterprise.lang.model.AnnotationMember)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(jakarta.enterprise.lang.model.declarations.ClassInfo)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(jakarta.enterprise.lang.model.declarations.ClassInfo,java.lang.String)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(jakarta.enterprise.lang.model.declarations.ClassInfo,java.lang.String[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(jakarta.enterprise.lang.model.declarations.ClassInfo[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(jakarta.enterprise.lang.model.types.Type)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(jakarta.enterprise.lang.model.types.Type[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(java.lang.Class<? extends java.lang.Enum<?>>,java.lang.String)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(java.lang.Class<? extends java.lang.Enum<?>>,java.lang.String[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(java.lang.Class<?>)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(java.lang.Class<?>[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(java.lang.Enum<?>)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(java.lang.Enum<?>[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(java.lang.String)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(java.lang.String[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(java.lang.annotation.Annotation)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(java.lang.annotation.Annotation[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(long)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(long[])
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(short)
meth public jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder value(short[])
meth public static jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder of(jakarta.enterprise.lang.model.declarations.ClassInfo)
meth public static jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder of(java.lang.Class<? extends java.lang.annotation.Annotation>)

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilderFactory
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder create(jakarta.enterprise.lang.model.declarations.ClassInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder create(java.lang.Class<? extends java.lang.annotation.Annotation>)

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.BeanInfo
meth public abstract boolean isAlternative()
meth public abstract boolean isClassBean()
meth public abstract boolean isProducerField()
meth public abstract boolean isProducerMethod()
meth public abstract boolean isSynthetic()
meth public abstract jakarta.enterprise.inject.build.compatible.spi.DisposerInfo disposer()
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ScopeInfo scope()
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo declaringClass()
meth public abstract jakarta.enterprise.lang.model.declarations.FieldInfo producerField()
meth public abstract jakarta.enterprise.lang.model.declarations.MethodInfo producerMethod()
meth public abstract java.lang.Integer priority()
meth public abstract java.lang.String name()
meth public abstract java.util.Collection<jakarta.enterprise.inject.build.compatible.spi.InjectionPointInfo> injectionPoints()
meth public abstract java.util.Collection<jakarta.enterprise.inject.build.compatible.spi.StereotypeInfo> stereotypes()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.AnnotationInfo> qualifiers()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.types.Type> types()
meth public boolean isInterceptor()
meth public jakarta.enterprise.inject.build.compatible.spi.InterceptorInfo asInterceptor()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.BuildServices
intf jakarta.enterprise.inject.spi.Prioritized
meth public abstract jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilderFactory annotationBuilderFactory()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.ClassConfig
intf jakarta.enterprise.inject.build.compatible.spi.DeclarationConfig
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ClassConfig addAnnotation(jakarta.enterprise.lang.model.AnnotationInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ClassConfig addAnnotation(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ClassConfig addAnnotation(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ClassConfig removeAllAnnotations()
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ClassConfig removeAnnotation(java.util.function.Predicate<jakarta.enterprise.lang.model.AnnotationInfo>)
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo info()
meth public abstract java.util.Collection<jakarta.enterprise.inject.build.compatible.spi.FieldConfig> fields()
meth public abstract java.util.Collection<jakarta.enterprise.inject.build.compatible.spi.MethodConfig> constructors()
meth public abstract java.util.Collection<jakarta.enterprise.inject.build.compatible.spi.MethodConfig> methods()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.DeclarationConfig
meth public abstract jakarta.enterprise.inject.build.compatible.spi.DeclarationConfig addAnnotation(jakarta.enterprise.lang.model.AnnotationInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.DeclarationConfig addAnnotation(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.DeclarationConfig addAnnotation(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.DeclarationConfig removeAllAnnotations()
meth public abstract jakarta.enterprise.inject.build.compatible.spi.DeclarationConfig removeAnnotation(java.util.function.Predicate<jakarta.enterprise.lang.model.AnnotationInfo>)
meth public abstract jakarta.enterprise.lang.model.declarations.DeclarationInfo info()

CLSS public abstract interface !annotation jakarta.enterprise.inject.build.compatible.spi.Discovery
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.DisposerInfo
meth public abstract jakarta.enterprise.lang.model.declarations.MethodInfo disposerMethod()
meth public abstract jakarta.enterprise.lang.model.declarations.ParameterInfo disposedParameter()

CLSS public abstract interface !annotation jakarta.enterprise.inject.build.compatible.spi.Enhancement
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean withSubtypes()
meth public abstract !hasdefault java.lang.Class<? extends java.lang.annotation.Annotation>[] withAnnotations()
meth public abstract java.lang.Class<?>[] types()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.FieldConfig
intf jakarta.enterprise.inject.build.compatible.spi.DeclarationConfig
meth public abstract jakarta.enterprise.inject.build.compatible.spi.FieldConfig addAnnotation(jakarta.enterprise.lang.model.AnnotationInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.FieldConfig addAnnotation(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.FieldConfig addAnnotation(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.FieldConfig removeAllAnnotations()
meth public abstract jakarta.enterprise.inject.build.compatible.spi.FieldConfig removeAnnotation(java.util.function.Predicate<jakarta.enterprise.lang.model.AnnotationInfo>)
meth public abstract jakarta.enterprise.lang.model.declarations.FieldInfo info()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.InjectionPointInfo
meth public abstract jakarta.enterprise.lang.model.declarations.DeclarationInfo declaration()
meth public abstract jakarta.enterprise.lang.model.types.Type type()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.AnnotationInfo> qualifiers()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.InterceptorInfo
intf jakarta.enterprise.inject.build.compatible.spi.BeanInfo
meth public abstract boolean intercepts(jakarta.enterprise.inject.spi.InterceptionType)
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.AnnotationInfo> interceptorBindings()
meth public boolean isInterceptor()
meth public jakarta.enterprise.inject.build.compatible.spi.InterceptorInfo asInterceptor()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.Messages
meth public abstract void error(java.lang.Exception)
meth public abstract void error(java.lang.String)
meth public abstract void error(java.lang.String,jakarta.enterprise.inject.build.compatible.spi.BeanInfo)
meth public abstract void error(java.lang.String,jakarta.enterprise.inject.build.compatible.spi.ObserverInfo)
meth public abstract void error(java.lang.String,jakarta.enterprise.lang.model.AnnotationTarget)
meth public abstract void info(java.lang.String)
meth public abstract void info(java.lang.String,jakarta.enterprise.inject.build.compatible.spi.BeanInfo)
meth public abstract void info(java.lang.String,jakarta.enterprise.inject.build.compatible.spi.ObserverInfo)
meth public abstract void info(java.lang.String,jakarta.enterprise.lang.model.AnnotationTarget)
meth public abstract void warn(java.lang.String)
meth public abstract void warn(java.lang.String,jakarta.enterprise.inject.build.compatible.spi.BeanInfo)
meth public abstract void warn(java.lang.String,jakarta.enterprise.inject.build.compatible.spi.ObserverInfo)
meth public abstract void warn(java.lang.String,jakarta.enterprise.lang.model.AnnotationTarget)

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ClassConfig addInterceptorBinding(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ClassConfig addQualifier(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ClassConfig addStereotype(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract void addContext(java.lang.Class<? extends java.lang.annotation.Annotation>,boolean,java.lang.Class<? extends jakarta.enterprise.context.spi.AlterableContext>)
meth public abstract void addContext(java.lang.Class<? extends java.lang.annotation.Annotation>,java.lang.Class<? extends jakarta.enterprise.context.spi.AlterableContext>)

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.MethodConfig
intf jakarta.enterprise.inject.build.compatible.spi.DeclarationConfig
meth public abstract jakarta.enterprise.inject.build.compatible.spi.MethodConfig addAnnotation(jakarta.enterprise.lang.model.AnnotationInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.MethodConfig addAnnotation(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.MethodConfig addAnnotation(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.MethodConfig removeAllAnnotations()
meth public abstract jakarta.enterprise.inject.build.compatible.spi.MethodConfig removeAnnotation(java.util.function.Predicate<jakarta.enterprise.lang.model.AnnotationInfo>)
meth public abstract jakarta.enterprise.lang.model.declarations.MethodInfo info()
meth public abstract java.util.List<jakarta.enterprise.inject.build.compatible.spi.ParameterConfig> parameters()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.ObserverInfo
meth public abstract boolean isAsync()
meth public abstract boolean isSynthetic()
meth public abstract int priority()
meth public abstract jakarta.enterprise.event.Reception reception()
meth public abstract jakarta.enterprise.event.TransactionPhase transactionPhase()
meth public abstract jakarta.enterprise.inject.build.compatible.spi.BeanInfo bean()
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo declaringClass()
meth public abstract jakarta.enterprise.lang.model.declarations.MethodInfo observerMethod()
meth public abstract jakarta.enterprise.lang.model.declarations.ParameterInfo eventParameter()
meth public abstract jakarta.enterprise.lang.model.types.Type eventType()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.AnnotationInfo> qualifiers()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.ParameterConfig
intf jakarta.enterprise.inject.build.compatible.spi.DeclarationConfig
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ParameterConfig addAnnotation(jakarta.enterprise.lang.model.AnnotationInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ParameterConfig addAnnotation(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ParameterConfig addAnnotation(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ParameterConfig removeAllAnnotations()
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ParameterConfig removeAnnotation(java.util.function.Predicate<jakarta.enterprise.lang.model.AnnotationInfo>)
meth public abstract jakarta.enterprise.lang.model.declarations.ParameterInfo info()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.Parameters
meth public abstract <%0 extends java.lang.Object> {%%0} get(java.lang.String,java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> {%%0} get(java.lang.String,java.lang.Class<{%%0}>,{%%0})

CLSS public abstract interface !annotation jakarta.enterprise.inject.build.compatible.spi.Registration
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<?>[] types()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.ScannedClasses
meth public abstract void add(java.lang.String)

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.ScopeInfo
meth public abstract boolean isNormal()
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo annotation()
meth public java.lang.String name()

CLSS public abstract interface !annotation jakarta.enterprise.inject.build.compatible.spi.SkipIfPortableExtensionPresent
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends jakarta.enterprise.inject.spi.Extension> value()

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.StereotypeInfo
meth public abstract boolean isAlternative()
meth public abstract boolean isNamed()
meth public abstract jakarta.enterprise.inject.build.compatible.spi.ScopeInfo defaultScope()
meth public abstract java.lang.Integer priority()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.AnnotationInfo> interceptorBindings()

CLSS public abstract interface !annotation jakarta.enterprise.inject.build.compatible.spi.Synthesis
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> alternative(boolean)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> createWith(java.lang.Class<? extends jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}>>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> disposeWith(java.lang.Class<? extends jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanDisposer<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}>>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> name(java.lang.String)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> priority(int)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> qualifier(jakarta.enterprise.lang.model.AnnotationInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> qualifier(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> qualifier(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> scope(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> stereotype(jakarta.enterprise.lang.model.declarations.ClassInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> stereotype(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> type(jakarta.enterprise.lang.model.declarations.ClassInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> type(jakarta.enterprise.lang.model.types.Type)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> type(java.lang.Class<?>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,boolean)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,boolean[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,double)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,double[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,int)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,int[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,jakarta.enterprise.lang.model.AnnotationInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,jakarta.enterprise.lang.model.AnnotationInfo[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,jakarta.enterprise.lang.model.declarations.ClassInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,jakarta.enterprise.lang.model.declarations.ClassInfo[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,java.lang.Class<?>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,java.lang.Class<?>[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,java.lang.Enum<?>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,java.lang.Enum<?>[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,java.lang.String)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,java.lang.String[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,java.lang.annotation.Annotation[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,long)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder%0}> withParam(java.lang.String,long[])

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator<%0 extends java.lang.Object>
meth public abstract {jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator%0} create(jakarta.enterprise.inject.Instance<java.lang.Object>,jakarta.enterprise.inject.build.compatible.spi.Parameters)

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanDisposer<%0 extends java.lang.Object>
meth public abstract void dispose({jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanDisposer%0},jakarta.enterprise.inject.Instance<java.lang.Object>,jakarta.enterprise.inject.build.compatible.spi.Parameters)

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanBuilder<{%%0}> addBean(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{%%0}> addObserver(jakarta.enterprise.lang.model.types.Type)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{%%0}> addObserver(java.lang.Class<{%%0}>)

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.SyntheticObserver<%0 extends java.lang.Object>
meth public abstract void observe(jakarta.enterprise.inject.spi.EventContext<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserver%0}>,jakarta.enterprise.inject.build.compatible.spi.Parameters) throws java.lang.Exception

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> async(boolean)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> declaringClass(jakarta.enterprise.lang.model.declarations.ClassInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> declaringClass(java.lang.Class<?>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> observeWith(java.lang.Class<? extends jakarta.enterprise.inject.build.compatible.spi.SyntheticObserver<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}>>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> priority(int)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> qualifier(jakarta.enterprise.lang.model.AnnotationInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> qualifier(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> qualifier(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> transactionPhase(jakarta.enterprise.event.TransactionPhase)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,boolean)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,boolean[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,double)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,double[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,int)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,int[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,jakarta.enterprise.lang.model.AnnotationInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,jakarta.enterprise.lang.model.AnnotationInfo[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,jakarta.enterprise.lang.model.declarations.ClassInfo)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,jakarta.enterprise.lang.model.declarations.ClassInfo[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,java.lang.Class<?>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,java.lang.Class<?>[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,java.lang.Enum<?>)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,java.lang.Enum<?>[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,java.lang.String)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,java.lang.String[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,java.lang.annotation.Annotation[])
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,long)
meth public abstract jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder<{jakarta.enterprise.inject.build.compatible.spi.SyntheticObserverBuilder%0}> withParam(java.lang.String,long[])

CLSS public abstract interface jakarta.enterprise.inject.build.compatible.spi.Types
meth public abstract !varargs jakarta.enterprise.lang.model.types.ParameterizedType parameterized(jakarta.enterprise.lang.model.types.ClassType,jakarta.enterprise.lang.model.types.Type[])
meth public abstract !varargs jakarta.enterprise.lang.model.types.ParameterizedType parameterized(java.lang.Class<?>,jakarta.enterprise.lang.model.types.Type[])
meth public abstract !varargs jakarta.enterprise.lang.model.types.ParameterizedType parameterized(java.lang.Class<?>,java.lang.Class<?>[])
meth public abstract jakarta.enterprise.lang.model.types.ArrayType ofArray(jakarta.enterprise.lang.model.types.Type,int)
meth public abstract jakarta.enterprise.lang.model.types.ClassType ofClass(jakarta.enterprise.lang.model.declarations.ClassInfo)
meth public abstract jakarta.enterprise.lang.model.types.ClassType ofClass(java.lang.String)
meth public abstract jakarta.enterprise.lang.model.types.PrimitiveType ofPrimitive(jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind)
meth public abstract jakarta.enterprise.lang.model.types.Type of(java.lang.Class<?>)
meth public abstract jakarta.enterprise.lang.model.types.VoidType ofVoid()
meth public abstract jakarta.enterprise.lang.model.types.WildcardType wildcardUnbounded()
meth public abstract jakarta.enterprise.lang.model.types.WildcardType wildcardWithLowerBound(jakarta.enterprise.lang.model.types.Type)
meth public abstract jakarta.enterprise.lang.model.types.WildcardType wildcardWithUpperBound(jakarta.enterprise.lang.model.types.Type)

CLSS public abstract interface !annotation jakarta.enterprise.inject.build.compatible.spi.Validation
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation

CLSS public final jakarta.enterprise.inject.literal.InjectLiteral
cons public init()
fld public final static jakarta.enterprise.inject.literal.InjectLiteral INSTANCE
intf jakarta.inject.Inject
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.inject.Inject>
hfds serialVersionUID

CLSS public final jakarta.enterprise.inject.literal.NamedLiteral
fld public final static jakarta.inject.Named INSTANCE
intf jakarta.inject.Named
meth public java.lang.String value()
meth public static jakarta.enterprise.inject.literal.NamedLiteral of(java.lang.String)
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.inject.Named>
hfds serialVersionUID,value

CLSS public final jakarta.enterprise.inject.literal.QualifierLiteral
cons public init()
fld public final static jakarta.enterprise.inject.literal.QualifierLiteral INSTANCE
intf jakarta.inject.Qualifier
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.inject.Qualifier>
hfds serialVersionUID

CLSS public final jakarta.enterprise.inject.literal.SingletonLiteral
cons public init()
fld public final static jakarta.enterprise.inject.literal.SingletonLiteral INSTANCE
intf jakarta.inject.Singleton
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.inject.Singleton>
hfds serialVersionUID

CLSS public abstract interface jakarta.enterprise.inject.se.SeContainer
intf jakarta.enterprise.inject.Instance<java.lang.Object>
intf java.lang.AutoCloseable
meth public abstract boolean isRunning()
meth public abstract jakarta.enterprise.inject.spi.BeanManager getBeanManager()
meth public abstract void close()
meth public jakarta.enterprise.inject.spi.BeanContainer getBeanContainer()

CLSS public abstract jakarta.enterprise.inject.se.SeContainerInitializer
cons public init()
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer addBeanClasses(java.lang.Class<?>[])
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer addExtensions(jakarta.enterprise.inject.spi.Extension[])
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer addExtensions(java.lang.Class<? extends jakarta.enterprise.inject.spi.Extension>[])
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer addPackages(boolean,java.lang.Class<?>[])
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer addPackages(boolean,java.lang.Package[])
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer addPackages(java.lang.Class<?>[])
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer addPackages(java.lang.Package[])
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer enableDecorators(java.lang.Class<?>[])
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer enableInterceptors(java.lang.Class<?>[])
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer selectAlternativeStereotypes(java.lang.Class<? extends java.lang.annotation.Annotation>[])
meth public abstract !varargs jakarta.enterprise.inject.se.SeContainerInitializer selectAlternatives(java.lang.Class<?>[])
meth public abstract jakarta.enterprise.inject.se.SeContainer initialize()
meth public abstract jakarta.enterprise.inject.se.SeContainerInitializer addProperty(java.lang.String,java.lang.Object)
meth public abstract jakarta.enterprise.inject.se.SeContainerInitializer disableDiscovery()
meth public abstract jakarta.enterprise.inject.se.SeContainerInitializer setClassLoader(java.lang.ClassLoader)
meth public abstract jakarta.enterprise.inject.se.SeContainerInitializer setProperties(java.util.Map<java.lang.String,java.lang.Object>)
meth public static jakarta.enterprise.inject.se.SeContainerInitializer newInstance()
supr java.lang.Object

CLSS public abstract interface jakarta.enterprise.inject.spi.AfterBeanDiscovery
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.AnnotatedType<{%%0}> getAnnotatedType(java.lang.Class<{%%0}>,java.lang.String)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{%%0}> addBean()
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{%%0}> addObserverMethod()
meth public abstract <%0 extends java.lang.Object> java.lang.Iterable<jakarta.enterprise.inject.spi.AnnotatedType<{%%0}>> getAnnotatedTypes(java.lang.Class<{%%0}>)
meth public abstract void addBean(jakarta.enterprise.inject.spi.Bean<?>)
meth public abstract void addContext(jakarta.enterprise.context.spi.Context)
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void addObserverMethod(jakarta.enterprise.inject.spi.ObserverMethod<?>)

CLSS public abstract interface jakarta.enterprise.inject.spi.AfterDeploymentValidation
meth public abstract void addDeploymentProblem(java.lang.Throwable)

CLSS public abstract interface jakarta.enterprise.inject.spi.AfterTypeDiscovery
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<{%%0}> addAnnotatedType(java.lang.Class<{%%0}>,java.lang.String)
meth public abstract java.util.List<java.lang.Class<?>> getAlternatives()
meth public abstract java.util.List<java.lang.Class<?>> getDecorators()
meth public abstract java.util.List<java.lang.Class<?>> getInterceptors()
meth public abstract void addAnnotatedType(jakarta.enterprise.inject.spi.AnnotatedType<?>,java.lang.String)

CLSS public abstract interface jakarta.enterprise.inject.spi.Annotated
meth public abstract <%0 extends java.lang.annotation.Annotation> java.util.Set<{%%0}> getAnnotations(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.annotation.Annotation> {%%0} getAnnotation(java.lang.Class<{%%0}>)
meth public abstract boolean isAnnotationPresent(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract java.lang.reflect.Type getBaseType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getAnnotations()
meth public abstract java.util.Set<java.lang.reflect.Type> getTypeClosure()

CLSS public abstract interface jakarta.enterprise.inject.spi.AnnotatedCallable<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.AnnotatedMember<{jakarta.enterprise.inject.spi.AnnotatedCallable%0}>
meth public abstract java.util.List<jakarta.enterprise.inject.spi.AnnotatedParameter<{jakarta.enterprise.inject.spi.AnnotatedCallable%0}>> getParameters()

CLSS public abstract interface jakarta.enterprise.inject.spi.AnnotatedConstructor<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.AnnotatedCallable<{jakarta.enterprise.inject.spi.AnnotatedConstructor%0}>
meth public <%0 extends java.lang.annotation.Annotation> java.util.Set<{%%0}> getAnnotations(java.lang.Class<{%%0}>)
meth public abstract java.lang.reflect.Constructor<{jakarta.enterprise.inject.spi.AnnotatedConstructor%0}> getJavaMember()

CLSS public abstract interface jakarta.enterprise.inject.spi.AnnotatedField<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.AnnotatedMember<{jakarta.enterprise.inject.spi.AnnotatedField%0}>
meth public <%0 extends java.lang.annotation.Annotation> java.util.Set<{%%0}> getAnnotations(java.lang.Class<{%%0}>)
meth public abstract java.lang.reflect.Field getJavaMember()

CLSS public abstract interface jakarta.enterprise.inject.spi.AnnotatedMember<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.Annotated
meth public abstract boolean isStatic()
meth public abstract jakarta.enterprise.inject.spi.AnnotatedType<{jakarta.enterprise.inject.spi.AnnotatedMember%0}> getDeclaringType()
meth public abstract java.lang.reflect.Member getJavaMember()

CLSS public abstract interface jakarta.enterprise.inject.spi.AnnotatedMethod<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.AnnotatedCallable<{jakarta.enterprise.inject.spi.AnnotatedMethod%0}>
meth public <%0 extends java.lang.annotation.Annotation> java.util.Set<{%%0}> getAnnotations(java.lang.Class<{%%0}>)
meth public abstract java.lang.reflect.Method getJavaMember()

CLSS public abstract interface jakarta.enterprise.inject.spi.AnnotatedParameter<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.Annotated
meth public <%0 extends java.lang.annotation.Annotation> java.util.Set<{%%0}> getAnnotations(java.lang.Class<{%%0}>)
meth public abstract int getPosition()
meth public abstract jakarta.enterprise.inject.spi.AnnotatedCallable<{jakarta.enterprise.inject.spi.AnnotatedParameter%0}> getDeclaringCallable()
meth public java.lang.reflect.Parameter getJavaParameter()

CLSS public abstract interface jakarta.enterprise.inject.spi.AnnotatedType<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.Annotated
meth public <%0 extends java.lang.annotation.Annotation> java.util.Set<{%%0}> getAnnotations(java.lang.Class<{%%0}>)
meth public abstract java.lang.Class<{jakarta.enterprise.inject.spi.AnnotatedType%0}> getJavaClass()
meth public abstract java.util.Set<jakarta.enterprise.inject.spi.AnnotatedConstructor<{jakarta.enterprise.inject.spi.AnnotatedType%0}>> getConstructors()
meth public abstract java.util.Set<jakarta.enterprise.inject.spi.AnnotatedField<? super {jakarta.enterprise.inject.spi.AnnotatedType%0}>> getFields()
meth public abstract java.util.Set<jakarta.enterprise.inject.spi.AnnotatedMethod<? super {jakarta.enterprise.inject.spi.AnnotatedType%0}>> getMethods()

CLSS public abstract interface jakarta.enterprise.inject.spi.Bean<%0 extends java.lang.Object>
intf jakarta.enterprise.context.spi.Contextual<{jakarta.enterprise.inject.spi.Bean%0}>
intf jakarta.enterprise.inject.spi.BeanAttributes<{jakarta.enterprise.inject.spi.Bean%0}>
meth public abstract java.lang.Class<?> getBeanClass()
meth public abstract java.util.Set<jakarta.enterprise.inject.spi.InjectionPoint> getInjectionPoints()

CLSS public abstract interface jakarta.enterprise.inject.spi.BeanAttributes<%0 extends java.lang.Object>
meth public abstract boolean isAlternative()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> getScope()
meth public abstract java.lang.String getName()
meth public abstract java.util.Set<java.lang.Class<? extends java.lang.annotation.Annotation>> getStereotypes()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getQualifiers()
meth public abstract java.util.Set<java.lang.reflect.Type> getTypes()

CLSS public abstract interface jakarta.enterprise.inject.spi.BeanContainer
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<jakarta.enterprise.inject.spi.ObserverMethod<? super {%%0}>> resolveObserverMethods({%%0},java.lang.annotation.Annotation[])
meth public abstract !varargs java.util.List<jakarta.enterprise.inject.spi.Interceptor<?>> resolveInterceptors(jakarta.enterprise.inject.spi.InterceptionType,java.lang.annotation.Annotation[])
meth public abstract !varargs java.util.Set<jakarta.enterprise.inject.spi.Bean<?>> getBeans(java.lang.reflect.Type,java.lang.annotation.Annotation[])
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.context.spi.CreationalContext<{%%0}> createCreationalContext(jakarta.enterprise.context.spi.Contextual<{%%0}>)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.Bean<? extends {%%0}> resolve(java.util.Set<jakarta.enterprise.inject.spi.Bean<? extends {%%0}>>)
meth public abstract boolean isInterceptorBinding(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isNormalScope(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isQualifier(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isScope(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isStereotype(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.context.spi.Context getContext(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.event.Event<java.lang.Object> getEvent()
meth public abstract jakarta.enterprise.inject.Instance<java.lang.Object> createInstance()
meth public abstract java.lang.Object getReference(jakarta.enterprise.inject.spi.Bean<?>,java.lang.reflect.Type,jakarta.enterprise.context.spi.CreationalContext<?>)
meth public abstract java.util.Set<jakarta.enterprise.inject.spi.Bean<?>> getBeans(java.lang.String)

CLSS public abstract interface jakarta.enterprise.inject.spi.BeanManager
intf jakarta.enterprise.inject.spi.BeanContainer
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<jakarta.enterprise.inject.spi.ObserverMethod<? super {%%0}>> resolveObserverMethods({%%0},java.lang.annotation.Annotation[])
meth public abstract !varargs java.util.List<jakarta.enterprise.inject.spi.Decorator<?>> resolveDecorators(java.util.Set<java.lang.reflect.Type>,java.lang.annotation.Annotation[])
meth public abstract <%0 extends jakarta.enterprise.inject.spi.Extension> {%%0} getExtension(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object, %1 extends java.lang.Object> jakarta.enterprise.inject.spi.Bean<{%%0}> createBean(jakarta.enterprise.inject.spi.BeanAttributes<{%%0}>,java.lang.Class<{%%1}>,jakarta.enterprise.inject.spi.ProducerFactory<{%%1}>)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.AnnotatedType<{%%0}> createAnnotatedType(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.Bean<{%%0}> createBean(jakarta.enterprise.inject.spi.BeanAttributes<{%%0}>,java.lang.Class<{%%0}>,jakarta.enterprise.inject.spi.InjectionTargetFactory<{%%0}>)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.BeanAttributes<{%%0}> createBeanAttributes(jakarta.enterprise.inject.spi.AnnotatedType<{%%0}>)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.InjectionTargetFactory<{%%0}> getInjectionTargetFactory(jakarta.enterprise.inject.spi.AnnotatedType<{%%0}>)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.InterceptionFactory<{%%0}> createInterceptionFactory(jakarta.enterprise.context.spi.CreationalContext<{%%0}>,java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.ProducerFactory<{%%0}> getProducerFactory(jakarta.enterprise.inject.spi.AnnotatedField<? super {%%0}>,jakarta.enterprise.inject.spi.Bean<{%%0}>)
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.ProducerFactory<{%%0}> getProducerFactory(jakarta.enterprise.inject.spi.AnnotatedMethod<? super {%%0}>,jakarta.enterprise.inject.spi.Bean<{%%0}>)
meth public abstract boolean areInterceptorBindingsEquivalent(java.lang.annotation.Annotation,java.lang.annotation.Annotation)
meth public abstract boolean areQualifiersEquivalent(java.lang.annotation.Annotation,java.lang.annotation.Annotation)
meth public abstract boolean isPassivatingScope(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract int getInterceptorBindingHashCode(java.lang.annotation.Annotation)
meth public abstract int getQualifierHashCode(java.lang.annotation.Annotation)
meth public abstract jakarta.el.ELResolver getELResolver()
meth public abstract jakarta.el.ExpressionFactory wrapExpressionFactory(jakarta.el.ExpressionFactory)
meth public abstract jakarta.enterprise.inject.spi.Bean<?> getPassivationCapableBean(java.lang.String)
meth public abstract jakarta.enterprise.inject.spi.BeanAttributes<?> createBeanAttributes(jakarta.enterprise.inject.spi.AnnotatedMember<?>)
meth public abstract jakarta.enterprise.inject.spi.InjectionPoint createInjectionPoint(jakarta.enterprise.inject.spi.AnnotatedField<?>)
meth public abstract jakarta.enterprise.inject.spi.InjectionPoint createInjectionPoint(jakarta.enterprise.inject.spi.AnnotatedParameter<?>)
meth public abstract java.lang.Object getInjectableReference(jakarta.enterprise.inject.spi.InjectionPoint,jakarta.enterprise.context.spi.CreationalContext<?>)
meth public abstract java.util.Set<java.lang.annotation.Annotation> getInterceptorBindingDefinition(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract java.util.Set<java.lang.annotation.Annotation> getStereotypeDefinition(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract void validate(jakarta.enterprise.inject.spi.InjectionPoint)

CLSS public abstract interface jakarta.enterprise.inject.spi.BeforeBeanDiscovery
meth public abstract !varargs void addInterceptorBinding(java.lang.Class<? extends java.lang.annotation.Annotation>,java.lang.annotation.Annotation[])
meth public abstract !varargs void addStereotype(java.lang.Class<? extends java.lang.annotation.Annotation>,java.lang.annotation.Annotation[])
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<{%%0}> addAnnotatedType(java.lang.Class<{%%0}>,java.lang.String)
meth public abstract <%0 extends java.lang.annotation.Annotation> jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<{%%0}> configureInterceptorBinding(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.annotation.Annotation> jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<{%%0}> configureQualifier(java.lang.Class<{%%0}>)
meth public abstract void addAnnotatedType(jakarta.enterprise.inject.spi.AnnotatedType<?>,java.lang.String)
meth public abstract void addInterceptorBinding(jakarta.enterprise.inject.spi.AnnotatedType<? extends java.lang.annotation.Annotation>)
meth public abstract void addQualifier(jakarta.enterprise.inject.spi.AnnotatedType<? extends java.lang.annotation.Annotation>)
meth public abstract void addQualifier(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract void addScope(java.lang.Class<? extends java.lang.annotation.Annotation>,boolean,boolean)

CLSS public abstract interface jakarta.enterprise.inject.spi.BeforeShutdown

CLSS public abstract jakarta.enterprise.inject.spi.CDI<%0 extends java.lang.Object>
cons public init()
fld protected static volatile jakarta.enterprise.inject.spi.CDIProvider configuredProvider
fld protected static volatile java.util.Set<jakarta.enterprise.inject.spi.CDIProvider> discoveredProviders
intf jakarta.enterprise.inject.Instance<{jakarta.enterprise.inject.spi.CDI%0}>
meth public abstract jakarta.enterprise.inject.spi.BeanManager getBeanManager()
meth public jakarta.enterprise.inject.spi.BeanContainer getBeanContainer()
meth public static jakarta.enterprise.inject.spi.CDI<java.lang.Object> current()
meth public static void setCDIProvider(jakarta.enterprise.inject.spi.CDIProvider)
supr java.lang.Object
hfds lock,providerSetManually

CLSS public abstract interface jakarta.enterprise.inject.spi.CDIProvider
fld public final static int DEFAULT_CDI_PROVIDER_PRIORITY = 0
intf jakarta.enterprise.inject.spi.Prioritized
meth public abstract jakarta.enterprise.inject.spi.CDI<java.lang.Object> getCDI()
meth public int getPriority()

CLSS public abstract interface jakarta.enterprise.inject.spi.Decorator<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.Bean<{jakarta.enterprise.inject.spi.Decorator%0}>
meth public abstract java.lang.reflect.Type getDelegateType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getDelegateQualifiers()
meth public abstract java.util.Set<java.lang.reflect.Type> getDecoratedTypes()

CLSS public jakarta.enterprise.inject.spi.DefinitionException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public jakarta.enterprise.inject.spi.DeploymentException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public abstract interface jakarta.enterprise.inject.spi.EventContext<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.EventMetadata getMetadata()
meth public abstract {jakarta.enterprise.inject.spi.EventContext%0} getEvent()

CLSS public abstract interface jakarta.enterprise.inject.spi.EventMetadata
meth public abstract jakarta.enterprise.inject.spi.InjectionPoint getInjectionPoint()
meth public abstract java.lang.reflect.Type getType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getQualifiers()

CLSS public abstract interface jakarta.enterprise.inject.spi.Extension

CLSS public abstract interface jakarta.enterprise.inject.spi.InjectionPoint
meth public abstract boolean isDelegate()
meth public abstract boolean isTransient()
meth public abstract jakarta.enterprise.inject.spi.Annotated getAnnotated()
meth public abstract jakarta.enterprise.inject.spi.Bean<?> getBean()
meth public abstract java.lang.reflect.Member getMember()
meth public abstract java.lang.reflect.Type getType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getQualifiers()

CLSS public abstract interface jakarta.enterprise.inject.spi.InjectionTarget<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.Producer<{jakarta.enterprise.inject.spi.InjectionTarget%0}>
meth public abstract void inject({jakarta.enterprise.inject.spi.InjectionTarget%0},jakarta.enterprise.context.spi.CreationalContext<{jakarta.enterprise.inject.spi.InjectionTarget%0}>)
meth public abstract void postConstruct({jakarta.enterprise.inject.spi.InjectionTarget%0})
meth public abstract void preDestroy({jakarta.enterprise.inject.spi.InjectionTarget%0})

CLSS public abstract interface jakarta.enterprise.inject.spi.InjectionTargetFactory<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.InjectionTarget<{jakarta.enterprise.inject.spi.InjectionTargetFactory%0}> createInjectionTarget(jakarta.enterprise.inject.spi.Bean<{jakarta.enterprise.inject.spi.InjectionTargetFactory%0}>)
meth public jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<{jakarta.enterprise.inject.spi.InjectionTargetFactory%0}> configure()

CLSS public abstract interface jakarta.enterprise.inject.spi.InterceptionFactory<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.InterceptionFactory<{jakarta.enterprise.inject.spi.InterceptionFactory%0}> ignoreFinalMethods()
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<{jakarta.enterprise.inject.spi.InterceptionFactory%0}> configure()
meth public abstract {jakarta.enterprise.inject.spi.InterceptionFactory%0} createInterceptedInstance({jakarta.enterprise.inject.spi.InterceptionFactory%0})

CLSS public final !enum jakarta.enterprise.inject.spi.InterceptionType
fld public final static jakarta.enterprise.inject.spi.InterceptionType AROUND_CONSTRUCT
fld public final static jakarta.enterprise.inject.spi.InterceptionType AROUND_INVOKE
fld public final static jakarta.enterprise.inject.spi.InterceptionType AROUND_TIMEOUT
fld public final static jakarta.enterprise.inject.spi.InterceptionType POST_ACTIVATE
fld public final static jakarta.enterprise.inject.spi.InterceptionType POST_CONSTRUCT
fld public final static jakarta.enterprise.inject.spi.InterceptionType PRE_DESTROY
fld public final static jakarta.enterprise.inject.spi.InterceptionType PRE_PASSIVATE
meth public static jakarta.enterprise.inject.spi.InterceptionType valueOf(java.lang.String)
meth public static jakarta.enterprise.inject.spi.InterceptionType[] values()
supr java.lang.Enum<jakarta.enterprise.inject.spi.InterceptionType>

CLSS public abstract interface jakarta.enterprise.inject.spi.Interceptor<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.Bean<{jakarta.enterprise.inject.spi.Interceptor%0}>
meth public abstract boolean intercepts(jakarta.enterprise.inject.spi.InterceptionType)
meth public abstract java.lang.Object intercept(jakarta.enterprise.inject.spi.InterceptionType,{jakarta.enterprise.inject.spi.Interceptor%0},jakarta.interceptor.InvocationContext) throws java.lang.Exception
meth public abstract java.util.Set<java.lang.annotation.Annotation> getInterceptorBindings()

CLSS public abstract interface jakarta.enterprise.inject.spi.ObserverMethod<%0 extends java.lang.Object>
fld public final static int DEFAULT_PRIORITY = 2500
intf jakarta.enterprise.inject.spi.Prioritized
meth public abstract jakarta.enterprise.event.Reception getReception()
meth public abstract jakarta.enterprise.event.TransactionPhase getTransactionPhase()
meth public abstract java.lang.Class<?> getBeanClass()
meth public abstract java.lang.reflect.Type getObservedType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getObservedQualifiers()
meth public boolean isAsync()
meth public int getPriority()
meth public jakarta.enterprise.inject.spi.Bean<?> getDeclaringBean()
meth public void notify(jakarta.enterprise.inject.spi.EventContext<{jakarta.enterprise.inject.spi.ObserverMethod%0}>)
meth public void notify({jakarta.enterprise.inject.spi.ObserverMethod%0})

CLSS public abstract interface jakarta.enterprise.inject.spi.PassivationCapable
meth public abstract java.lang.String getId()

CLSS public abstract interface jakarta.enterprise.inject.spi.Prioritized
meth public abstract int getPriority()

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessAnnotatedType<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedType<{jakarta.enterprise.inject.spi.ProcessAnnotatedType%0}> getAnnotatedType()
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<{jakarta.enterprise.inject.spi.ProcessAnnotatedType%0}> configureAnnotatedType()
meth public abstract void setAnnotatedType(jakarta.enterprise.inject.spi.AnnotatedType<{jakarta.enterprise.inject.spi.ProcessAnnotatedType%0}>)
meth public abstract void veto()

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessBean<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.Annotated getAnnotated()
meth public abstract jakarta.enterprise.inject.spi.Bean<{jakarta.enterprise.inject.spi.ProcessBean%0}> getBean()
meth public abstract void addDefinitionError(java.lang.Throwable)

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessBeanAttributes<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.Annotated getAnnotated()
meth public abstract jakarta.enterprise.inject.spi.BeanAttributes<{jakarta.enterprise.inject.spi.ProcessBeanAttributes%0}> getBeanAttributes()
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.ProcessBeanAttributes%0}> configureBeanAttributes()
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void ignoreFinalMethods()
meth public abstract void setBeanAttributes(jakarta.enterprise.inject.spi.BeanAttributes<{jakarta.enterprise.inject.spi.ProcessBeanAttributes%0}>)
meth public abstract void veto()

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessInjectionPoint<%0 extends java.lang.Object, %1 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.InjectionPoint getInjectionPoint()
meth public abstract jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator configureInjectionPoint()
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void setInjectionPoint(jakarta.enterprise.inject.spi.InjectionPoint)

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessInjectionTarget<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedType<{jakarta.enterprise.inject.spi.ProcessInjectionTarget%0}> getAnnotatedType()
meth public abstract jakarta.enterprise.inject.spi.InjectionTarget<{jakarta.enterprise.inject.spi.ProcessInjectionTarget%0}> getInjectionTarget()
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void setInjectionTarget(jakarta.enterprise.inject.spi.InjectionTarget<{jakarta.enterprise.inject.spi.ProcessInjectionTarget%0}>)

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessManagedBean<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.ProcessBean<{jakarta.enterprise.inject.spi.ProcessManagedBean%0}>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedType<{jakarta.enterprise.inject.spi.ProcessManagedBean%0}> getAnnotatedBeanClass()

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessObserverMethod<%0 extends java.lang.Object, %1 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedMethod<{jakarta.enterprise.inject.spi.ProcessObserverMethod%1}> getAnnotatedMethod()
meth public abstract jakarta.enterprise.inject.spi.ObserverMethod<{jakarta.enterprise.inject.spi.ProcessObserverMethod%0}> getObserverMethod()
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.ProcessObserverMethod%0}> configureObserverMethod()
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void setObserverMethod(jakarta.enterprise.inject.spi.ObserverMethod<{jakarta.enterprise.inject.spi.ProcessObserverMethod%0}>)
meth public abstract void veto()

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessProducer<%0 extends java.lang.Object, %1 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedMember<{jakarta.enterprise.inject.spi.ProcessProducer%0}> getAnnotatedMember()
meth public abstract jakarta.enterprise.inject.spi.Producer<{jakarta.enterprise.inject.spi.ProcessProducer%1}> getProducer()
meth public abstract jakarta.enterprise.inject.spi.configurator.ProducerConfigurator<{jakarta.enterprise.inject.spi.ProcessProducer%1}> configureProducer()
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void setProducer(jakarta.enterprise.inject.spi.Producer<{jakarta.enterprise.inject.spi.ProcessProducer%1}>)

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessProducerField<%0 extends java.lang.Object, %1 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.ProcessBean<{jakarta.enterprise.inject.spi.ProcessProducerField%1}>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedField<{jakarta.enterprise.inject.spi.ProcessProducerField%0}> getAnnotatedProducerField()
meth public abstract jakarta.enterprise.inject.spi.AnnotatedParameter<{jakarta.enterprise.inject.spi.ProcessProducerField%0}> getAnnotatedDisposedParameter()

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessProducerMethod<%0 extends java.lang.Object, %1 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.ProcessBean<{jakarta.enterprise.inject.spi.ProcessProducerMethod%1}>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedMethod<{jakarta.enterprise.inject.spi.ProcessProducerMethod%0}> getAnnotatedProducerMethod()
meth public abstract jakarta.enterprise.inject.spi.AnnotatedParameter<{jakarta.enterprise.inject.spi.ProcessProducerMethod%0}> getAnnotatedDisposedParameter()

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessSessionBean<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.ProcessManagedBean<java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.SessionBeanType getSessionBeanType()
meth public abstract java.lang.String getEjbName()

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessSyntheticAnnotatedType<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.ProcessAnnotatedType<{jakarta.enterprise.inject.spi.ProcessSyntheticAnnotatedType%0}>
meth public abstract jakarta.enterprise.inject.spi.Extension getSource()

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessSyntheticBean<%0 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.ProcessBean<{jakarta.enterprise.inject.spi.ProcessSyntheticBean%0}>
meth public abstract jakarta.enterprise.inject.spi.Extension getSource()

CLSS public abstract interface jakarta.enterprise.inject.spi.ProcessSyntheticObserverMethod<%0 extends java.lang.Object, %1 extends java.lang.Object>
intf jakarta.enterprise.inject.spi.ProcessObserverMethod<{jakarta.enterprise.inject.spi.ProcessSyntheticObserverMethod%0},{jakarta.enterprise.inject.spi.ProcessSyntheticObserverMethod%1}>
meth public abstract jakarta.enterprise.inject.spi.Extension getSource()

CLSS public abstract interface jakarta.enterprise.inject.spi.Producer<%0 extends java.lang.Object>
meth public abstract java.util.Set<jakarta.enterprise.inject.spi.InjectionPoint> getInjectionPoints()
meth public abstract void dispose({jakarta.enterprise.inject.spi.Producer%0})
meth public abstract {jakarta.enterprise.inject.spi.Producer%0} produce(jakarta.enterprise.context.spi.CreationalContext<{jakarta.enterprise.inject.spi.Producer%0}>)

CLSS public abstract interface jakarta.enterprise.inject.spi.ProducerFactory<%0 extends java.lang.Object>
meth public abstract <%0 extends java.lang.Object> jakarta.enterprise.inject.spi.Producer<{%%0}> createProducer(jakarta.enterprise.inject.spi.Bean<{%%0}>)

CLSS public final !enum jakarta.enterprise.inject.spi.SessionBeanType
fld public final static jakarta.enterprise.inject.spi.SessionBeanType SINGLETON
fld public final static jakarta.enterprise.inject.spi.SessionBeanType STATEFUL
fld public final static jakarta.enterprise.inject.spi.SessionBeanType STATELESS
meth public static jakarta.enterprise.inject.spi.SessionBeanType valueOf(java.lang.String)
meth public static jakarta.enterprise.inject.spi.SessionBeanType[] values()
supr java.lang.Enum<jakarta.enterprise.inject.spi.SessionBeanType>

CLSS public jakarta.enterprise.inject.spi.Unmanaged<%0 extends java.lang.Object>
cons public init(jakarta.enterprise.inject.spi.BeanManager,java.lang.Class<{jakarta.enterprise.inject.spi.Unmanaged%0}>)
cons public init(java.lang.Class<{jakarta.enterprise.inject.spi.Unmanaged%0}>)
innr public static UnmanagedInstance
meth public jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance<{jakarta.enterprise.inject.spi.Unmanaged%0}> newInstance()
supr java.lang.Object
hfds beanManager,injectionTarget

CLSS public static jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance<%0 extends java.lang.Object>
 outer jakarta.enterprise.inject.spi.Unmanaged
meth public jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance<{jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance%0}> dispose()
meth public jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance<{jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance%0}> inject()
meth public jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance<{jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance%0}> postConstruct()
meth public jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance<{jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance%0}> preDestroy()
meth public jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance<{jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance%0}> produce()
meth public {jakarta.enterprise.inject.spi.Unmanaged$UnmanagedInstance%0} get()
supr java.lang.Object
hfds ctx,disposed,injectionTarget,instance

CLSS public abstract interface !annotation jakarta.enterprise.inject.spi.WithAnnotations
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation>[] value()

CLSS public abstract interface jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedConstructor<{jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator%0}> getAnnotated()
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator%0}> add(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator%0}> remove(java.util.function.Predicate<java.lang.annotation.Annotation>)
meth public abstract java.util.List<jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator%0}>> params()
meth public jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator%0}> removeAll()
meth public java.util.stream.Stream<jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator%0}>> filterParams(java.util.function.Predicate<jakarta.enterprise.inject.spi.AnnotatedParameter<{jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator%0}>>)

CLSS public abstract interface jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedField<{jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator%0}> getAnnotated()
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator%0}> add(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator%0}> remove(java.util.function.Predicate<java.lang.annotation.Annotation>)
meth public jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator%0}> removeAll()

CLSS public abstract interface jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedMethod<{jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator%0}> getAnnotated()
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator%0}> add(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator%0}> remove(java.util.function.Predicate<java.lang.annotation.Annotation>)
meth public abstract java.util.List<jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator%0}>> params()
meth public jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator%0}> removeAll()
meth public java.util.stream.Stream<jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator%0}>> filterParams(java.util.function.Predicate<jakarta.enterprise.inject.spi.AnnotatedParameter<{jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator%0}>>)

CLSS public abstract interface jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedParameter<{jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator%0}> getAnnotated()
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator%0}> add(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator%0}> remove(java.util.function.Predicate<java.lang.annotation.Annotation>)
meth public jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator%0}> removeAll()

CLSS public abstract interface jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<%0 extends java.lang.Object>
meth public abstract jakarta.enterprise.inject.spi.AnnotatedType<{jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}> getAnnotated()
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}> add(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}> remove(java.util.function.Predicate<java.lang.annotation.Annotation>)
meth public abstract java.util.Set<jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}>> constructors()
meth public abstract java.util.Set<jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator<? super {jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}>> fields()
meth public abstract java.util.Set<jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator<? super {jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}>> methods()
meth public jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}> removeAll()
meth public java.util.stream.Stream<jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator<{jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}>> filterConstructors(java.util.function.Predicate<jakarta.enterprise.inject.spi.AnnotatedConstructor<{jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}>>)
meth public java.util.stream.Stream<jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator<? super {jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}>> filterFields(java.util.function.Predicate<jakarta.enterprise.inject.spi.AnnotatedField<? super {jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}>>)
meth public java.util.stream.Stream<jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator<? super {jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}>> filterMethods(java.util.function.Predicate<jakarta.enterprise.inject.spi.AnnotatedMethod<? super {jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator%0}>>)

CLSS public abstract interface jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<%0 extends java.lang.Object>
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> addQualifiers(java.lang.annotation.Annotation[])
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> addTypes(java.lang.reflect.Type[])
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> qualifiers(java.lang.annotation.Annotation[])
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> types(java.lang.reflect.Type[])
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> addQualifier(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> addQualifiers(java.util.Set<java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> addStereotype(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> addStereotypes(java.util.Set<java.lang.Class<? extends java.lang.annotation.Annotation>>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> addTransitiveTypeClosure(java.lang.reflect.Type)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> addType(jakarta.enterprise.util.TypeLiteral<?>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> addType(java.lang.reflect.Type)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> addTypes(java.util.Set<java.lang.reflect.Type>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> alternative(boolean)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> name(java.lang.String)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> qualifiers(java.util.Set<java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> scope(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> stereotypes(java.util.Set<java.lang.Class<? extends java.lang.annotation.Annotation>>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator%0}> types(java.util.Set<java.lang.reflect.Type>)

CLSS public abstract interface jakarta.enterprise.inject.spi.configurator.BeanConfigurator<%0 extends java.lang.Object>
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addInjectionPoints(jakarta.enterprise.inject.spi.InjectionPoint[])
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addQualifiers(java.lang.annotation.Annotation[])
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addTypes(java.lang.reflect.Type[])
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> injectionPoints(jakarta.enterprise.inject.spi.InjectionPoint[])
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> qualifiers(java.lang.annotation.Annotation[])
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> types(java.lang.reflect.Type[])
meth public abstract <%0 extends {jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{%%0}> createWith(java.util.function.Function<jakarta.enterprise.context.spi.CreationalContext<{%%0}>,{%%0}>)
meth public abstract <%0 extends {jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{%%0}> produceWith(java.util.function.Function<jakarta.enterprise.inject.Instance<java.lang.Object>,{%%0}>)
meth public abstract <%0 extends {jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{%%0}> read(jakarta.enterprise.inject.spi.AnnotatedType<{%%0}>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addInjectionPoint(jakarta.enterprise.inject.spi.InjectionPoint)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addInjectionPoints(java.util.Set<jakarta.enterprise.inject.spi.InjectionPoint>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addQualifier(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addQualifiers(java.util.Set<java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addStereotype(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addStereotypes(java.util.Set<java.lang.Class<? extends java.lang.annotation.Annotation>>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addTransitiveTypeClosure(java.lang.reflect.Type)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addType(jakarta.enterprise.util.TypeLiteral<?>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addType(java.lang.reflect.Type)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> addTypes(java.util.Set<java.lang.reflect.Type>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> alternative(boolean)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> beanClass(java.lang.Class<?>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> destroyWith(java.util.function.BiConsumer<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0},jakarta.enterprise.context.spi.CreationalContext<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}>>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> disposeWith(java.util.function.BiConsumer<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0},jakarta.enterprise.inject.Instance<java.lang.Object>>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> id(java.lang.String)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> injectionPoints(java.util.Set<jakarta.enterprise.inject.spi.InjectionPoint>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> name(java.lang.String)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> priority(int)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> qualifiers(java.util.Set<java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> read(jakarta.enterprise.inject.spi.BeanAttributes<?>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> scope(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> stereotypes(java.util.Set<java.lang.Class<? extends java.lang.annotation.Annotation>>)
meth public abstract jakarta.enterprise.inject.spi.configurator.BeanConfigurator<{jakarta.enterprise.inject.spi.configurator.BeanConfigurator%0}> types(java.util.Set<java.lang.reflect.Type>)

CLSS public abstract interface jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator addQualifiers(java.lang.annotation.Annotation[])
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator qualifiers(java.lang.annotation.Annotation[])
meth public abstract jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator addQualifier(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator addQualifiers(java.util.Set<java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator delegate(boolean)
meth public abstract jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator qualifiers(java.util.Set<java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator transientField(boolean)
meth public abstract jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator type(java.lang.reflect.Type)

CLSS public abstract interface jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<%0 extends java.lang.Object>
innr public abstract interface static EventConsumer
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> addQualifiers(java.lang.annotation.Annotation[])
meth public abstract !varargs jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> qualifiers(java.lang.annotation.Annotation[])
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> addQualifier(java.lang.annotation.Annotation)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> addQualifiers(java.util.Set<java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> async(boolean)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> beanClass(java.lang.Class<?>)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> notifyWith(jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator$EventConsumer<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}>)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> observedType(java.lang.reflect.Type)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> priority(int)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> qualifiers(java.util.Set<java.lang.annotation.Annotation>)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> read(jakarta.enterprise.inject.spi.AnnotatedMethod<?>)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> read(jakarta.enterprise.inject.spi.ObserverMethod<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}>)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> read(java.lang.reflect.Method)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> reception(jakarta.enterprise.event.Reception)
meth public abstract jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator%0}> transactionPhase(jakarta.enterprise.event.TransactionPhase)

CLSS public abstract interface static jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator$EventConsumer<%0 extends java.lang.Object>
 outer jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator
 anno 0 java.lang.FunctionalInterface()
meth public abstract void accept(jakarta.enterprise.inject.spi.EventContext<{jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator$EventConsumer%0}>) throws java.lang.Exception

CLSS public abstract interface jakarta.enterprise.inject.spi.configurator.ProducerConfigurator<%0 extends java.lang.Object>
meth public abstract <%0 extends {jakarta.enterprise.inject.spi.configurator.ProducerConfigurator%0}> jakarta.enterprise.inject.spi.configurator.ProducerConfigurator<{jakarta.enterprise.inject.spi.configurator.ProducerConfigurator%0}> produceWith(java.util.function.Function<jakarta.enterprise.context.spi.CreationalContext<{%%0}>,{%%0}>)
meth public abstract jakarta.enterprise.inject.spi.configurator.ProducerConfigurator<{jakarta.enterprise.inject.spi.configurator.ProducerConfigurator%0}> disposeWith(java.util.function.Consumer<{jakarta.enterprise.inject.spi.configurator.ProducerConfigurator%0}>)

CLSS public abstract interface jakarta.enterprise.lang.model.AnnotationInfo
meth public abstract boolean hasMember(java.lang.String)
meth public abstract jakarta.enterprise.lang.model.AnnotationMember member(java.lang.String)
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo declaration()
meth public abstract java.util.Map<java.lang.String,jakarta.enterprise.lang.model.AnnotationMember> members()
meth public boolean hasValue()
meth public boolean isRepeatable()
meth public jakarta.enterprise.lang.model.AnnotationMember value()
meth public java.lang.String name()

CLSS public abstract interface jakarta.enterprise.lang.model.AnnotationMember
fld public final static java.lang.String VALUE = "value"
innr public final static !enum Kind
meth public abstract <%0 extends java.lang.Enum<{%%0}>> {%%0} asEnum(java.lang.Class<{%%0}>)
meth public abstract boolean asBoolean()
meth public abstract byte asByte()
meth public abstract char asChar()
meth public abstract double asDouble()
meth public abstract float asFloat()
meth public abstract int asInt()
meth public abstract jakarta.enterprise.lang.model.AnnotationInfo asNestedAnnotation()
meth public abstract jakarta.enterprise.lang.model.AnnotationMember$Kind kind()
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo asEnumClass()
meth public abstract jakarta.enterprise.lang.model.types.Type asType()
meth public abstract java.lang.String asEnumConstant()
meth public abstract java.lang.String asString()
meth public abstract java.util.List<jakarta.enterprise.lang.model.AnnotationMember> asArray()
meth public abstract long asLong()
meth public abstract short asShort()
meth public boolean isArray()
meth public boolean isBoolean()
meth public boolean isByte()
meth public boolean isChar()
meth public boolean isClass()
meth public boolean isDouble()
meth public boolean isEnum()
meth public boolean isFloat()
meth public boolean isInt()
meth public boolean isLong()
meth public boolean isNestedAnnotation()
meth public boolean isShort()
meth public boolean isString()

CLSS public final static !enum jakarta.enterprise.lang.model.AnnotationMember$Kind
 outer jakarta.enterprise.lang.model.AnnotationMember
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind ARRAY
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind BOOLEAN
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind BYTE
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind CHAR
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind CLASS
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind DOUBLE
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind ENUM
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind FLOAT
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind INT
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind LONG
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind NESTED_ANNOTATION
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind SHORT
fld public final static jakarta.enterprise.lang.model.AnnotationMember$Kind STRING
meth public static jakarta.enterprise.lang.model.AnnotationMember$Kind valueOf(java.lang.String)
meth public static jakarta.enterprise.lang.model.AnnotationMember$Kind[] values()
supr java.lang.Enum<jakarta.enterprise.lang.model.AnnotationMember$Kind>

CLSS public abstract interface jakarta.enterprise.lang.model.AnnotationTarget
meth public abstract <%0 extends java.lang.annotation.Annotation> jakarta.enterprise.lang.model.AnnotationInfo annotation(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.annotation.Annotation> java.util.Collection<jakarta.enterprise.lang.model.AnnotationInfo> repeatableAnnotation(java.lang.Class<{%%0}>)
meth public abstract boolean hasAnnotation(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean hasAnnotation(java.util.function.Predicate<jakarta.enterprise.lang.model.AnnotationInfo>)
meth public abstract boolean isDeclaration()
meth public abstract boolean isType()
meth public abstract jakarta.enterprise.lang.model.declarations.DeclarationInfo asDeclaration()
meth public abstract jakarta.enterprise.lang.model.types.Type asType()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.AnnotationInfo> annotations()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.AnnotationInfo> annotations(java.util.function.Predicate<jakarta.enterprise.lang.model.AnnotationInfo>)

CLSS public abstract interface jakarta.enterprise.lang.model.declarations.ClassInfo
intf jakarta.enterprise.lang.model.declarations.DeclarationInfo
meth public abstract boolean isAbstract()
meth public abstract boolean isAnnotation()
meth public abstract boolean isEnum()
meth public abstract boolean isFinal()
meth public abstract boolean isInterface()
meth public abstract boolean isPlainClass()
meth public abstract boolean isRecord()
meth public abstract int modifiers()
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo superClassDeclaration()
meth public abstract jakarta.enterprise.lang.model.declarations.PackageInfo packageInfo()
meth public abstract jakarta.enterprise.lang.model.types.Type superClass()
meth public abstract java.lang.String name()
meth public abstract java.lang.String simpleName()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.declarations.FieldInfo> fields()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.declarations.MethodInfo> constructors()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.declarations.MethodInfo> methods()
meth public abstract java.util.Collection<jakarta.enterprise.lang.model.declarations.RecordComponentInfo> recordComponents()
meth public abstract java.util.List<jakarta.enterprise.lang.model.declarations.ClassInfo> superInterfacesDeclarations()
meth public abstract java.util.List<jakarta.enterprise.lang.model.types.Type> superInterfaces()
meth public abstract java.util.List<jakarta.enterprise.lang.model.types.TypeVariable> typeParameters()
meth public jakarta.enterprise.lang.model.declarations.ClassInfo asClass()
meth public jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind kind()

CLSS public abstract interface jakarta.enterprise.lang.model.declarations.DeclarationInfo
innr public final static !enum Kind
intf jakarta.enterprise.lang.model.AnnotationTarget
meth public abstract jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind kind()
meth public boolean isClass()
meth public boolean isDeclaration()
meth public boolean isField()
meth public boolean isMethod()
meth public boolean isPackage()
meth public boolean isParameter()
meth public boolean isRecordComponent()
meth public boolean isType()
meth public jakarta.enterprise.lang.model.declarations.ClassInfo asClass()
meth public jakarta.enterprise.lang.model.declarations.DeclarationInfo asDeclaration()
meth public jakarta.enterprise.lang.model.declarations.FieldInfo asField()
meth public jakarta.enterprise.lang.model.declarations.MethodInfo asMethod()
meth public jakarta.enterprise.lang.model.declarations.PackageInfo asPackage()
meth public jakarta.enterprise.lang.model.declarations.ParameterInfo asParameter()
meth public jakarta.enterprise.lang.model.declarations.RecordComponentInfo asRecordComponent()
meth public jakarta.enterprise.lang.model.types.Type asType()

CLSS public final static !enum jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind
 outer jakarta.enterprise.lang.model.declarations.DeclarationInfo
fld public final static jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind CLASS
fld public final static jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind FIELD
fld public final static jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind METHOD
fld public final static jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind PACKAGE
fld public final static jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind PARAMETER
fld public final static jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind RECORD_COMPONENT
meth public static jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind valueOf(java.lang.String)
meth public static jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind[] values()
supr java.lang.Enum<jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind>

CLSS public abstract interface jakarta.enterprise.lang.model.declarations.FieldInfo
intf jakarta.enterprise.lang.model.declarations.DeclarationInfo
meth public abstract boolean isFinal()
meth public abstract boolean isStatic()
meth public abstract int modifiers()
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo declaringClass()
meth public abstract jakarta.enterprise.lang.model.types.Type type()
meth public abstract java.lang.String name()
meth public jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind kind()
meth public jakarta.enterprise.lang.model.declarations.FieldInfo asField()

CLSS public abstract interface jakarta.enterprise.lang.model.declarations.MethodInfo
intf jakarta.enterprise.lang.model.declarations.DeclarationInfo
meth public abstract boolean isAbstract()
meth public abstract boolean isConstructor()
meth public abstract boolean isFinal()
meth public abstract boolean isStatic()
meth public abstract int modifiers()
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo declaringClass()
meth public abstract jakarta.enterprise.lang.model.types.Type receiverType()
meth public abstract jakarta.enterprise.lang.model.types.Type returnType()
meth public abstract java.lang.String name()
meth public abstract java.util.List<jakarta.enterprise.lang.model.declarations.ParameterInfo> parameters()
meth public abstract java.util.List<jakarta.enterprise.lang.model.types.Type> throwsTypes()
meth public abstract java.util.List<jakarta.enterprise.lang.model.types.TypeVariable> typeParameters()
meth public jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind kind()
meth public jakarta.enterprise.lang.model.declarations.MethodInfo asMethod()

CLSS public abstract interface jakarta.enterprise.lang.model.declarations.PackageInfo
intf jakarta.enterprise.lang.model.declarations.DeclarationInfo
meth public abstract java.lang.String name()
meth public jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind kind()
meth public jakarta.enterprise.lang.model.declarations.PackageInfo asPackage()

CLSS public abstract interface jakarta.enterprise.lang.model.declarations.ParameterInfo
intf jakarta.enterprise.lang.model.declarations.DeclarationInfo
meth public abstract jakarta.enterprise.lang.model.declarations.MethodInfo declaringMethod()
meth public abstract jakarta.enterprise.lang.model.types.Type type()
meth public abstract java.lang.String name()
meth public jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind kind()
meth public jakarta.enterprise.lang.model.declarations.ParameterInfo asParameter()

CLSS public abstract interface jakarta.enterprise.lang.model.declarations.RecordComponentInfo
intf jakarta.enterprise.lang.model.declarations.DeclarationInfo
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo declaringRecord()
meth public abstract jakarta.enterprise.lang.model.declarations.FieldInfo field()
meth public abstract jakarta.enterprise.lang.model.declarations.MethodInfo accessor()
meth public abstract jakarta.enterprise.lang.model.types.Type type()
meth public abstract java.lang.String name()
meth public jakarta.enterprise.lang.model.declarations.DeclarationInfo$Kind kind()
meth public jakarta.enterprise.lang.model.declarations.RecordComponentInfo asRecordComponent()

CLSS public abstract interface jakarta.enterprise.lang.model.types.ArrayType
intf jakarta.enterprise.lang.model.types.Type
meth public abstract jakarta.enterprise.lang.model.types.Type componentType()
meth public jakarta.enterprise.lang.model.types.ArrayType asArray()
meth public jakarta.enterprise.lang.model.types.Type$Kind kind()

CLSS public abstract interface jakarta.enterprise.lang.model.types.ClassType
intf jakarta.enterprise.lang.model.types.Type
meth public abstract jakarta.enterprise.lang.model.declarations.ClassInfo declaration()
meth public jakarta.enterprise.lang.model.types.ClassType asClass()
meth public jakarta.enterprise.lang.model.types.Type$Kind kind()

CLSS public abstract interface jakarta.enterprise.lang.model.types.ParameterizedType
intf jakarta.enterprise.lang.model.types.Type
meth public abstract jakarta.enterprise.lang.model.types.ClassType genericClass()
meth public abstract java.util.List<jakarta.enterprise.lang.model.types.Type> typeArguments()
meth public jakarta.enterprise.lang.model.declarations.ClassInfo declaration()
meth public jakarta.enterprise.lang.model.types.ParameterizedType asParameterizedType()
meth public jakarta.enterprise.lang.model.types.Type$Kind kind()

CLSS public abstract interface jakarta.enterprise.lang.model.types.PrimitiveType
innr public final static !enum PrimitiveKind
intf jakarta.enterprise.lang.model.types.Type
meth public abstract jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind primitiveKind()
meth public abstract java.lang.String name()
meth public boolean isBoolean()
meth public boolean isByte()
meth public boolean isChar()
meth public boolean isDouble()
meth public boolean isFloat()
meth public boolean isInt()
meth public boolean isLong()
meth public boolean isShort()
meth public jakarta.enterprise.lang.model.types.PrimitiveType asPrimitive()
meth public jakarta.enterprise.lang.model.types.Type$Kind kind()

CLSS public final static !enum jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind
 outer jakarta.enterprise.lang.model.types.PrimitiveType
fld public final static jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind BOOLEAN
fld public final static jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind BYTE
fld public final static jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind CHAR
fld public final static jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind DOUBLE
fld public final static jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind FLOAT
fld public final static jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind INT
fld public final static jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind LONG
fld public final static jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind SHORT
meth public static jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind valueOf(java.lang.String)
meth public static jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind[] values()
supr java.lang.Enum<jakarta.enterprise.lang.model.types.PrimitiveType$PrimitiveKind>

CLSS public abstract interface jakarta.enterprise.lang.model.types.Type
innr public final static !enum Kind
intf jakarta.enterprise.lang.model.AnnotationTarget
meth public abstract jakarta.enterprise.lang.model.types.Type$Kind kind()
meth public boolean isArray()
meth public boolean isClass()
meth public boolean isDeclaration()
meth public boolean isParameterizedType()
meth public boolean isPrimitive()
meth public boolean isType()
meth public boolean isTypeVariable()
meth public boolean isVoid()
meth public boolean isWildcardType()
meth public jakarta.enterprise.lang.model.declarations.DeclarationInfo asDeclaration()
meth public jakarta.enterprise.lang.model.types.ArrayType asArray()
meth public jakarta.enterprise.lang.model.types.ClassType asClass()
meth public jakarta.enterprise.lang.model.types.ParameterizedType asParameterizedType()
meth public jakarta.enterprise.lang.model.types.PrimitiveType asPrimitive()
meth public jakarta.enterprise.lang.model.types.Type asType()
meth public jakarta.enterprise.lang.model.types.TypeVariable asTypeVariable()
meth public jakarta.enterprise.lang.model.types.VoidType asVoid()
meth public jakarta.enterprise.lang.model.types.WildcardType asWildcardType()

CLSS public final static !enum jakarta.enterprise.lang.model.types.Type$Kind
 outer jakarta.enterprise.lang.model.types.Type
fld public final static jakarta.enterprise.lang.model.types.Type$Kind ARRAY
fld public final static jakarta.enterprise.lang.model.types.Type$Kind CLASS
fld public final static jakarta.enterprise.lang.model.types.Type$Kind PARAMETERIZED_TYPE
fld public final static jakarta.enterprise.lang.model.types.Type$Kind PRIMITIVE
fld public final static jakarta.enterprise.lang.model.types.Type$Kind TYPE_VARIABLE
fld public final static jakarta.enterprise.lang.model.types.Type$Kind VOID
fld public final static jakarta.enterprise.lang.model.types.Type$Kind WILDCARD_TYPE
meth public static jakarta.enterprise.lang.model.types.Type$Kind valueOf(java.lang.String)
meth public static jakarta.enterprise.lang.model.types.Type$Kind[] values()
supr java.lang.Enum<jakarta.enterprise.lang.model.types.Type$Kind>

CLSS public abstract interface jakarta.enterprise.lang.model.types.TypeVariable
intf jakarta.enterprise.lang.model.types.Type
meth public abstract java.lang.String name()
meth public abstract java.util.List<jakarta.enterprise.lang.model.types.Type> bounds()
meth public jakarta.enterprise.lang.model.types.Type$Kind kind()
meth public jakarta.enterprise.lang.model.types.TypeVariable asTypeVariable()

CLSS public abstract interface jakarta.enterprise.lang.model.types.VoidType
intf jakarta.enterprise.lang.model.types.Type
meth public abstract java.lang.String name()
meth public jakarta.enterprise.lang.model.types.Type$Kind kind()
meth public jakarta.enterprise.lang.model.types.VoidType asVoid()

CLSS public abstract interface jakarta.enterprise.lang.model.types.WildcardType
intf jakarta.enterprise.lang.model.types.Type
meth public abstract jakarta.enterprise.lang.model.types.Type lowerBound()
meth public abstract jakarta.enterprise.lang.model.types.Type upperBound()
meth public jakarta.enterprise.lang.model.types.Type$Kind kind()
meth public jakarta.enterprise.lang.model.types.WildcardType asWildcardType()

CLSS public abstract jakarta.enterprise.util.AnnotationLiteral<%0 extends java.lang.annotation.Annotation>
cons protected init()
intf java.io.Serializable
intf java.lang.annotation.Annotation
meth public boolean equals(java.lang.Object)
meth public int hashCode()
meth public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType()
meth public java.lang.String toString()
supr java.lang.Object
hfds annotationType,cachedHashCode,members,serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.util.Nonbinding
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.enterprise.util.Nonbinding$Literal
 outer jakarta.enterprise.util.Nonbinding
cons public init()
fld public final static jakarta.enterprise.util.Nonbinding$Literal INSTANCE
intf jakarta.enterprise.util.Nonbinding
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.util.Nonbinding>
hfds serialVersionUID

CLSS public abstract jakarta.enterprise.util.TypeLiteral<%0 extends java.lang.Object>
cons protected init()
intf java.io.Serializable
meth public boolean equals(java.lang.Object)
meth public final java.lang.Class<{jakarta.enterprise.util.TypeLiteral%0}> getRawType()
meth public final java.lang.reflect.Type getType()
meth public int hashCode()
meth public java.lang.String toString()
supr java.lang.Object
hfds actualType,serialVersionUID

CLSS public abstract interface !annotation jakarta.inject.Inject
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, CONSTRUCTOR, FIELD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.inject.Named
 anno 0 jakarta.inject.Qualifier()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String value()

CLSS public abstract interface jakarta.inject.Provider<%0 extends java.lang.Object>
meth public abstract {jakarta.inject.Provider%0} get()

CLSS public abstract interface !annotation jakarta.inject.Qualifier
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.inject.Scope
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.inject.Singleton
 anno 0 jakarta.inject.Scope()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.interceptor.AroundConstruct
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.interceptor.AroundInvoke
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.interceptor.AroundTimeout
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.interceptor.ExcludeClassInterceptors
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, CONSTRUCTOR])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.interceptor.ExcludeDefaultInterceptors
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, CONSTRUCTOR])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.interceptor.Interceptor
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
innr public static Priority
intf java.lang.annotation.Annotation

CLSS public static jakarta.interceptor.Interceptor$Priority
 outer jakarta.interceptor.Interceptor
fld public final static int APPLICATION = 2000
fld public final static int LIBRARY_AFTER = 3000
fld public final static int LIBRARY_BEFORE = 1000
fld public final static int PLATFORM_AFTER = 4000
fld public final static int PLATFORM_BEFORE = 0
supr java.lang.Object

CLSS public abstract interface !annotation jakarta.interceptor.InterceptorBinding
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.interceptor.Interceptors
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, CONSTRUCTOR])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class[] value()

CLSS public abstract interface jakarta.interceptor.InvocationContext
meth public abstract java.lang.Object getTarget()
meth public abstract java.lang.Object getTimer()
meth public abstract java.lang.Object proceed() throws java.lang.Exception
meth public abstract java.lang.Object[] getParameters()
meth public abstract java.lang.reflect.Constructor<?> getConstructor()
meth public abstract java.lang.reflect.Method getMethod()
meth public abstract java.util.Map<java.lang.String,java.lang.Object> getContextData()
meth public abstract void setParameters(java.lang.Object[])

CLSS public abstract interface java.io.Serializable

CLSS public abstract interface java.lang.AutoCloseable
meth public abstract void close() throws java.lang.Exception

CLSS public abstract interface java.lang.Comparable<%0 extends java.lang.Object>
meth public abstract int compareTo({java.lang.Comparable%0})

CLSS public abstract java.lang.Enum<%0 extends java.lang.Enum<{java.lang.Enum%0}>>
cons protected init(java.lang.String,int)
intf java.io.Serializable
intf java.lang.Comparable<{java.lang.Enum%0}>
meth protected final java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth protected final void finalize()
meth public final boolean equals(java.lang.Object)
meth public final int compareTo({java.lang.Enum%0})
meth public final int hashCode()
meth public final int ordinal()
meth public final java.lang.Class<{java.lang.Enum%0}> getDeclaringClass()
meth public final java.lang.String name()
meth public java.lang.String toString()
meth public static <%0 extends java.lang.Enum<{%%0}>> {%%0} valueOf(java.lang.Class<{%%0}>,java.lang.String)
supr java.lang.Object

CLSS public java.lang.Exception
cons protected init(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.Throwable

CLSS public abstract interface !annotation java.lang.FunctionalInterface
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface java.lang.Iterable<%0 extends java.lang.Object>
meth public abstract java.util.Iterator<{java.lang.Iterable%0}> iterator()
meth public java.util.Spliterator<{java.lang.Iterable%0}> spliterator()
meth public void forEach(java.util.function.Consumer<? super {java.lang.Iterable%0}>)

CLSS public java.lang.Object
cons public init()
meth protected java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth protected void finalize() throws java.lang.Throwable
 anno 0 java.lang.Deprecated(boolean forRemoval=false, java.lang.String since="9")
meth public boolean equals(java.lang.Object)
meth public final java.lang.Class<?> getClass()
meth public final void notify()
meth public final void notifyAll()
meth public final void wait() throws java.lang.InterruptedException
meth public final void wait(long) throws java.lang.InterruptedException
meth public final void wait(long,int) throws java.lang.InterruptedException
meth public int hashCode()
meth public java.lang.String toString()

CLSS public java.lang.RuntimeException
cons protected init(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.Exception

CLSS public java.lang.Throwable
cons protected init(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
intf java.io.Serializable
meth public final java.lang.Throwable[] getSuppressed()
meth public final void addSuppressed(java.lang.Throwable)
meth public java.lang.StackTraceElement[] getStackTrace()
meth public java.lang.String getLocalizedMessage()
meth public java.lang.String getMessage()
meth public java.lang.String toString()
meth public java.lang.Throwable fillInStackTrace()
meth public java.lang.Throwable getCause()
meth public java.lang.Throwable initCause(java.lang.Throwable)
meth public void printStackTrace()
meth public void printStackTrace(java.io.PrintStream)
meth public void printStackTrace(java.io.PrintWriter)
meth public void setStackTrace(java.lang.StackTraceElement[])
supr java.lang.Object

CLSS public abstract interface java.lang.annotation.Annotation
meth public abstract boolean equals(java.lang.Object)
meth public abstract int hashCode()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> annotationType()
meth public abstract java.lang.String toString()

CLSS public abstract interface !annotation java.lang.annotation.Documented
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation java.lang.annotation.Inherited
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation java.lang.annotation.Retention
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.annotation.RetentionPolicy value()

CLSS public abstract interface !annotation java.lang.annotation.Target
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.annotation.ElementType[] value()

