package pers.kagw.core.channel;

import javax.xml.soap.Node;
import java.util.Objects;

/**
 * @author kwsc98
 */
public class NodeIterator {

    private ComponentNode componentNode;

    public NodeIterator(ComponentNode componentNode) {
        this.componentNode = componentNode;
    }


    public boolean hasNext() {
        return Objects.nonNull(componentNode.getPreNode()) && Objects.nonNull(componentNode.getSufNode());
    }

    public ComponentNode next() {
        return this.componentNode = this.componentNode.getSufNode();
    }


}
