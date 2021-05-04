package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Checker {

    private ArrayList<String> colorProperties = new ArrayList<>();
    private ArrayList<String> pixelProperties = new ArrayList<>();
    private LinkedList<HashMap<String,ExpressionType>> expressionTypeList;
    private LinkedList<HashMap<String,Expression>> expressionList;

    public void check(AST ast) {
        expressionTypeList = new LinkedList<>();
        expressionList = new LinkedList<>();
        checkNode(ast.root);
    }

    public Checker(){
        //De arraylist wordt gevuld met strings waarop later gechecked gaat worden.
        colorProperties.add("color");
        colorProperties.add("background-color");
        pixelProperties.add("width");
        pixelProperties.add("height");
    }

    private void checkNode(ASTNode node) {
        //De expressies en expressie types van de nodes wordt in de bijbehorende hashmap gepushed.
        HashMap<String,ExpressionType> expressionTypeHashMap = new HashMap<>();
        HashMap<String, Expression> expressionHashMap = new HashMap<>();
        expressionTypeList.push(expressionTypeHashMap);
        expressionList.push(expressionHashMap);

        //Voor iedere node worden de checks doorgelopen
        for (ASTNode child: node.getChildren())
        {
            if (child instanceof VariableAssignment){
                addVariable((VariableAssignment)child);
            }else if (child instanceof Declaration){
                checkDeclaration((Declaration)child);
            }else if (child instanceof Operation){
                checkOperation((Operation)child);
            }
            checkNode(child);
        }
        expressionTypeList.pop(); //Wanneer de node door de checks heen is wordt hij verwijderd van de linkedlist
    }

    //Functie checkt of de functie defined is en voegt de expressie en expressietype toe aan de lists
    private void addVariable(VariableAssignment child) {
        String childName = child.name.name;
        ExpressionType childType = getExpressionType(child.expression);

        if (childType == ExpressionType.UNDEFINED) {
            child.setError("The expressiontype of the variable " + child.expression.getNodeLabel() + " is undefined!");
        }

        expressionTypeList.peek().put(childName, childType);
        expressionList.peek().put(childName, child.expression);
    }

    /*
    * De declaraties worden gechecked
    * Wanneer de declaratie een color is wordt gekeken of de property dan ook van het type color is.
    * Als dit niet zo is wordt een error op de knoop geplaatst
    * Hetzelfde wordt gedaan met de pixel properties.
    * */
    private void checkDeclaration(Declaration declaration) {
            ExpressionType exp = getExpressionType(declaration.expression);
        if (colorProperties.contains(declaration.property.name)){
            if (!exp.equals(ExpressionType.COLOR)){
               declaration.setError("You can only assign a color value to the property: " + declaration.property.name);
            }
        }else if(pixelProperties.contains(declaration.property.name)){
            if (!exp.equals(ExpressionType.PERCENTAGE) && (!exp.equals(ExpressionType.PIXEL))) {
                declaration.setError("You can only assign a size value to the pixel or percentage property: " + declaration.property.name);
            }
        }else{
           declaration.setError("The property: "+ declaration.property.name + " is invalid!" );
        }
    }

    /*
    * De operaties worden gechecked
    * Er wordt gekeken of de linker of rechter helft een color literal zijn, wanneer één van beide dat is wordt een error gegeven.
    * Wanneer een Add of subtract wordt gebruikt is moeten de linker en rechter hand side beide van het zelfde classe type zijn anders wordt een error gegeven.
    * Bij een vermedevuldiging moet aan één van de kanten een scalar literal staan ook hier wordt een error gegeven wanneer dit niet zo is.
    * */
    private void checkOperation(Operation operation) {
        if(expressionToLiteral(operation.lhs) instanceof ColorLiteral || expressionToLiteral(operation.rhs) instanceof ColorLiteral){
            operation.setError("A color is not meant to calculate with!");
        }else if(operation instanceof AddOperation || operation instanceof SubtractOperation){
            if (expressionToLiteral(operation.lhs).getClass() != expressionToLiteral(operation.rhs).getClass()){
                operation.setError("You can only use add or subtract operations on the same type!");
            }
        }else if (operation instanceof MultiplyOperation){
            if (!(expressionToLiteral(operation.lhs) instanceof ScalarLiteral) && !(expressionToLiteral(operation.rhs) instanceof ScalarLiteral)){
                operation.setError("You can only multiply using a scalar literal!");
            }
        }
    }

    /*
    * Zoals de functienaam al zegt wordt een expressie omgezet naar een literal
    * Wanneer het een literal is wordt het als literal returned
    * Als het een operation is wordt de operationToLiteral ervoor aangeroepen
    * Wanneer het een variableReference is wordt de getfunctie hiervan uitgevoerd
    * */
    private Literal expressionToLiteral(Expression expression) {
        if (expression instanceof Literal){
            return (Literal) expression;
        } else if (expression instanceof Operation) {//wat als het een variable expression is?
            return operationToLiteral((Operation) expression);
        }else if (expression instanceof VariableReference){
            return expressionToLiteral(getVariableReference(((VariableReference)expression).name));
        }
        return null;
    }

    /*
    * De naam spreekt voor zich.
    * Als het een add of subtract operation is wordt de linkerhelft returned omdat links en rechts hetzelfde moeten zijn
    * Wanneer de linker kant een scalar is wordt de rechter kant returned omdat dit dan de operation is
    * Wanneer links niet de scalar is wordt links returned
    * */
    private Literal operationToLiteral(Operation operation){
        if (operation instanceof AddOperation || operation instanceof SubtractOperation){
            return expressionToLiteral(operation.lhs);
        } else {
            if(expressionToLiteral(operation.lhs) instanceof ScalarLiteral){
                return  expressionToLiteral(operation.rhs);
            }else{
                return expressionToLiteral(operation.lhs);
            }
        }

    }

    /*
    * Hier wordt het expressietype van de meegegeven expressie returned
    * */
    private ExpressionType getExpressionType(Expression expression) {
        if (expressionToLiteral(expression) instanceof PixelLiteral){
            return ExpressionType.PIXEL;
        } else if (expressionToLiteral(expression) instanceof PercentageLiteral) {
            return ExpressionType.PERCENTAGE;
        } else if (expressionToLiteral(expression) instanceof ColorLiteral) {
            return ExpressionType.COLOR;
        } else if (expressionToLiteral(expression) instanceof ScalarLiteral) {
            return ExpressionType.SCALAR;
        } else {
            return ExpressionType.UNDEFINED;
        }
    }

    /*
    * De hashmap looped door de expressielijst
    * Wanneer de hashmap de naam als key heeft wordt de expressie van de naam returned
    * */
    private Expression getVariableReference(String name) {
        for (HashMap<String, Expression> hashmap : expressionList){
            if(hashmap.containsKey(name))
                return hashmap.get(name);
        }
        return null;
    }

}
