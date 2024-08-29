package com.dwei.core.config.mvc.processor;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * 增强 RequestResponseBodyMethodProcessor 使其对参数都进行参数校验
 * <p>
 * 通过添加 {@link Validated} 实现
 */
public class RequestBodyMethodProcessor implements HandlerMethodArgumentResolver {

    private final RequestResponseBodyMethodProcessor processor;
    private Field combinedAnnotationsField;

    public RequestBodyMethodProcessor(RequestResponseBodyMethodProcessor processor) {
        this.processor = processor;
    }

    @Override
    public boolean supportsParameter(@NonNull MethodParameter methodParameter) {
        return processor.supportsParameter(methodParameter);
    }

    @SneakyThrows
    private Field getCombinedAnnotationsField(MethodParameter methodParameter) {
        if (combinedAnnotationsField != null) return combinedAnnotationsField;
        combinedAnnotationsField = methodParameter.getClass().getDeclaredField("combinedAnnotations");
        combinedAnnotationsField.setAccessible(true);
        return combinedAnnotationsField;
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Annotation[] parameterAnnotations = methodParameter.getParameterAnnotations();
        for (Annotation ann : parameterAnnotations) {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                return processor.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory);
            }
        }
        MethodParameter parameter = methodParameter.clone();
        Field field = getCombinedAnnotationsField(parameter);
        Annotation[] annotations = Arrays.copyOf(parameterAnnotations, parameterAnnotations.length + 1);
        annotations[annotations.length - 1] = validated;
        field.set(parameter, annotations);
        return processor.resolveArgument(parameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory);
    }

    private static final Validated validated = new Validated() {
        @Override
        public Class<?>[] value() {
            return null;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Validated.class;
        }
    };

}
