package org.spring.core.init;

/**
 * 如果被加载的 bean 实现了接口，则调用该接口的 init 方法来处理bean的初始化逻辑
 * 注意：该接口是在bean加载完后，就直接调用init方法
 */
public interface InitializingBean{
    void init();
}
