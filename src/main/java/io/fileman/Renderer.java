package io.fileman;

import java.io.File;
import java.io.IOException;

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
    String column();

    /**
     * 渲染
     *
     * @param file    待渲染文件
     * @param context 渲染上下文
     * @return 渲染结果
     * @throws IOException I/O 异常
     */
    Object render(File file, RenderContext context) throws IOException;

}
