package com.mei.router.processor;


import com.google.auto.service.AutoService;

import com.mei.router.annotation.Route;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author mxb
 * @date 2022/6/9
 * @desc 路由注解处理器
 * @desired
 */
// 通过注解：AutoService，把注解处理器注册到java编译器中，那么在java编译到时候，java编译器就会回调该注解处理器
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.mei.router.annotation.Route"})
public class RouteProcessor extends AbstractProcessor {

    // 日志打印对象
    private Messager mMessager;

    private Elements mElementUtils;

    private Filer mFile;

    /**
     * 注解处理器初始化方法，相当于Activity的onCreate方法。
     *
     * @param processingEnv 该入参可以提供若干工具类，供将来编写代码生成规则时所使用
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
        mFile = processingEnv.getFiler();
    }

    /**
     * 声明注解处理器生成java代码规则，在这里写你的扫描、评估和处理注解的代码，以及生成Java文件。
     *
     * @param annotations 支持处理的注解集合。
     * @param roundEnv    表示当前或是之前的运行环境,可以通过该对象查找指定注解下的节点信息。
     * @return 如果返回 true，则这些注解已处理，后续 Processor 无需再处理它们；
     * 如果返回 false，则这些注解未处理并且可能要求后续 Processor 处理它们。
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "注解处理器开始");
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }
        // 获取所有被该注解标注的元素
        mMessager.printMessage(Diagnostic.Kind.NOTE, "注解处理器开始2");
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Route.class);
        if (elements == null || elements.isEmpty()) {
            return false;
        }
        // 打印日志，看哪些类被标注了
        for (Element element : elements) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, element.getSimpleName());
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "注解处理器开始3");

        // 动态生成类
        // 泛型类，Map，key，value
        // ClassName mapClass = ClassName.get("java.util", "Map");
        ClassName mapClass = ClassName.get(Map.class);
        ClassName keyType = ClassName.get("java.lang", "String");
        ClassName valueType = ClassName.get("com.mei.router.annotation", "RouteMeta");
        // 构造泛型类型
        ParameterizedTypeName mapParam = ParameterizedTypeName.get(mapClass, keyType, valueType);
        // 根据指定类型，创建参数
        ParameterSpec parameterSpecGroupMap = ParameterSpec.builder(mapParam, "groupMap").build();
        // 1. 构建方法，loadInfo
        MethodSpec.Builder loadInfoMethodBuilder = MethodSpec.methodBuilder("loadInfo")
                .addAnnotation(ClassName.get("java.lang", "Override"))
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(parameterSpecGroupMap);
        mMessager.printMessage(Diagnostic.Kind.NOTE, "注解处理器开始4");
        // 打印日志，看哪些类被标注了
        for (Element element : elements) {
            Route annotation = element.getAnnotation(Route.class);
            String path = annotation.path();
            mMessager.printMessage(Diagnostic.Kind.NOTE,
                    "package=" + mElementUtils.getPackageOf(element).getQualifiedName());
            ClassName className = ClassName.get(
                    mElementUtils.getPackageOf(element).getQualifiedName().toString(),
                    element.getSimpleName().toString());
            loadInfoMethodBuilder.addStatement("groupMap.put($S,new RouteMeta($S,$T.class))", path,
                    path, className);
        }

        MethodSpec loadInfoMethod = loadInfoMethodBuilder.build();

        TypeSpec routeGroup = TypeSpec.classBuilder("RouteGroup")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get("com.mei.router.api", "IRouteGroup"))
                .addMethod(loadInfoMethod)
                .build();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "注解处理器开始5");
        JavaFile javaFile = JavaFile.builder("com.mei.router.api", routeGroup).build();

        try {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "注解处理器开始6");
            // 动态生成的类，打印到控制台
            javaFile.writeTo(System.out);
            // 动态生成的类，保存到指定到文件中
            javaFile.writeTo(mFile);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "注解处理器开始,成功");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "注解处理器开始7=" + e.getMessage());
            return false;
        }
    }

    /**
     * 返回一个当前注解处理器所有支持的注解的集合。当前注解处理器需要处理哪种注解就加入那种注解。如果类型符合，就会调用process（）方法。
     * 可以通过注解指定：@SupportedAnnotationTypes
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    /**
     * 需要通过那个版本的jdk来进行编译
     * 可以通过注解指定：@SupportedSourceVersion
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7; // 最低支持java1.7
    }

    /**
     * 接收外来传入的参数，最常用的形式就是在Gradle里`javaCompileOptions`
     * 可以通过注解指定：@SupportedOptions
     */
    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }


}
