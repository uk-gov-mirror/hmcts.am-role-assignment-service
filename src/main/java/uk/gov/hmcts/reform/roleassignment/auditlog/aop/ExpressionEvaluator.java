package uk.gov.hmcts.reform.roleassignment.auditlog.aop;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class handling the SpEL expression parsing.
 */
public class ExpressionEvaluator extends CachedExpressionEvaluator {

    // shared param discoverer since it caches data internally
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    public EvaluationContext createEvaluationContext(Object object,
                                                     Class<?> targetClass,
                                                     Method method, Object[] args) {
        Method targetMethod = getTargetMethod(targetClass, method);
        ExpressionRootObject root = new ExpressionRootObject(object);
        return new MethodBasedEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);
    }

    public <T> T condition(String conditionExpression,
                           AnnotatedElementKey elementKey,
                           EvaluationContext evalContext,
                           Class<T> clazz) {
        return getExpression(this.conditionCache, elementKey, conditionExpression).getValue(evalContext, clazz);
    }

    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        return
            targetMethodCache.computeIfAbsent(methodKey, key -> AopUtils.getMostSpecificMethod(method, targetClass));

    }

    private static class ExpressionRootObject {
        private final Object object;

        public ExpressionRootObject(Object object) {
            this.object = object;

        }

        public Object getObject() {
            return object;
        }


    }
}
