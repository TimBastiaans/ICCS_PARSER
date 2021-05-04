package nl.han.ica.icss.ast;

import java.util.ArrayList;

//Klasse om te identificeren of iets een NestedStylerule is. dit is bijna een kopie van Stylerule.
public class NestedStylerule extends ASTNode {

    public Selector selector;
    public ArrayList<ASTNode> body = new ArrayList<>();
    public NestedStylerule(Selector selector, ArrayList<ASTNode> body) {
        this.selector = selector;
        this.body = body;
    }

    @Override
    public String getNodeLabel() {
        return "NestedStylerule";
    }
    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        if(selector != null)
            children.add(selector);
        children.addAll(body);
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if(child instanceof Selector)
            selector = (Selector) child;
        else
            body.add(child);
        return child;
    }
    @Override
    public ASTNode removeChild(ASTNode child) {
        if(selector == child)
            selector = null;
        else
            body.remove(child);
        return child;
    }
}
