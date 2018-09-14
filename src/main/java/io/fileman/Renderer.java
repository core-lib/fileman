package io.fileman;

/**
 * 文件信息渲染器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public interface Renderer extends Converter {

    /**
     * @return 表格列名
     */
    String name();

    /**
     * 渲染
     *
     * @param action 渲染动作
     * @return 渲染结果
     */
    Object render(Action action);

}
