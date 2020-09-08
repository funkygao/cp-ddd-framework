package org.ddd.cp.ddd.ext;

import org.ddd.cp.ddd.model.IDomainModel;
import org.ddd.cp.ddd.api.ApiResult;
import org.ddd.cp.ddd.api.RequestProfile;

import javax.validation.constraints.NotNull;

/**
 * 领域模型扩展属性的解析、落库和渲染扩展点.
 * <p>
 * <p>中台作为中间环节负责控制扩展属性的传递和持久化，而前台作为中台调用方和扩展点执行方，负责两侧的扩展属性解释和业务处理</p>
 * <pre>
 * +-------------+     +----+      +---------------+
 * | BP client   |-----| CP |------| BP extensions |
 * +-------------+     +----+      +---------------+
 *                       |
 *                   +----------+
 *                   | Database |
 *                   +----------+
 *                       |
 *  | id   | order_no       | ... | x1 varchar(100)                                | x2     | x3
 *  | ---- | -------------- | --- | ---------------------------------------------- | ------ | ---------
 *  | 1    | 22010391388764 |     | 10.9                                           | 12,abc |
 *  | 2    | 22010397315689 |     | {"foo":1, "bar":"egg", "baz":{"a":1, "b":2}}   |        | 2020-01-09
 * </pre>
 */
public interface IModelAttachmentExt extends IDomainExtension {

    /**
     * 扩展属性数据的解析和处理.
     * <p>
     * <p>适用场景：扩展属性数据，从API里传递进来</p>
     *
     * @param source 扩展属性数据的来源，从系统入参里获取 {@link RequestProfile#ext}
     * @param target 把扩展信息传递到的目标领域模型
     */
    void explain(@NotNull RequestProfile source, @NotNull IDomainModel target);

    /**
     * 扩展属性数据的解析和处理.
     * <p>
     * <p>适用场景：扩展属性数据从数据库里获取，中台存放到模型里；前台扩展点对模型里的弱类型数据处理，还可以存放回模型的强类型扩展容器里</p>
     *
     * @param model 对模型里的扩展属性进行解析和处理
     */
    default void explain(@NotNull IDomainModel model) { }

    /**
     * 扩展属性数据的API显示.
     * <p>
     * <p>典型场景：订单详情页显示，需要把{@code x1, x2}等预留属性名称转换为明确业务含义的key value</p>
     *
     * @param source 扩展属性数据的来源
     * @param target 把扩展信息传递到的目标 {@link ApiResult#ext}
     */
    default void render(@NotNull IDomainModel source, @NotNull ApiResult target) { }

}
