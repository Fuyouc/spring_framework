package org.spring.core.handler;

/**
 * 关于 SpringBeanCandidateBeforeClassHandler 与 SpringBeanCandidateAfterClassHandler 接口设计问题
 * 这两个接口本应该是合并成一个接口，但是由于某些原因，不得不将其拆分出来
 * Candidate意为候选人，也就是当你需要向容器中注入一个bean，但是又想在有一定的条件的情况下在进行注入，则需要实现这两个接口
 * SpringBeanCandidateBeforeClassHandler 是在 SpringBeanCandidateAfterClassHandler 之前先执行
 * 这两个接口所需要处理的都是没有被类加载处理器加载进去的bean，需要在这两个接口进行一些条件判断，是否需要bean的注入
 */
public interface SpringBeanCandidateBeforeClassHandler {
    void handler(Class<?> beanClass);
}
