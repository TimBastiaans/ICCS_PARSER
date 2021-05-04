package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

import java.util.ArrayList;

public class RemoveNesting implements Transform {

    @Override
    public void apply(AST ast) {
    //voor iedere node wordt gekeken of dit een nested node is.
        for (int i = 0; i < ast.root.getChildren().size(); i++){
            ASTNode child = ast.root.getChildren().get(i);
            getNestedNode(child, ast.root);
        }
    }

    /*
    * Voor iedere node wordt gekeken of het een instance of Stylerule is
    * Wanneer het een stylerule is wordt de root opgeslagen en gekeken of het een nestedstylerule is.
    * Voor iedere child wordt deze functie opniew uitgevoerd
    * */
    public void getNestedNode(ASTNode astNode, ASTNode root) {
        for (ASTNode child : astNode.getChildren()) {
            if(astNode instanceof Stylerule) {
                Stylerule rootStylerule = (Stylerule) astNode;
                ArrayList<Selector> selectors = rootStylerule.selectors;
                if(child instanceof NestedStylerule){
                    NestedStylerule nestedStylerule = (NestedStylerule) child;
                    Stylerule childStylerule = new Stylerule();
                    childStylerule.selectors = new ArrayList<>(selectors);
                    childStylerule.selectors.add(nestedStylerule.selector);
                    childStylerule.body = nestedStylerule.body;
                    root.addChild(childStylerule);
                    astNode.removeChild(child);
                }
            }
            getNestedNode(child, astNode);
        }
    }
}
