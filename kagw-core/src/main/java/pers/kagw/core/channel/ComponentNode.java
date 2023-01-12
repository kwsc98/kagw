package pers.kagw.core.channel;

import lombok.Getter;
import pers.kagw.core.handler.ComponentHandler;

/**
 * @author kwsc98
 */
@Getter
public class ComponentNode {

    private ComponentNode preNode = null;

    private ComponentNode sufNode = null;

    private ComponentHandler<?> handler = null;

    private String configJsonStr = null;

    public static ComponentNode build() {
        return new ComponentNode();
    }

    public ComponentNode setPreNode(ComponentNode pre) {
        this.preNode = pre;
        return this;
    }

    public ComponentNode setSufNode(ComponentNode suf) {
        this.sufNode = suf;
        return this;
    }

    public ComponentNode setHandler(ComponentHandler handler) {
        this.handler = handler;
        return this;
    }

    public ComponentNode setConfigJsonStr(String configJsonStr) {
        this.configJsonStr = configJsonStr;
        return this;
    }
}
