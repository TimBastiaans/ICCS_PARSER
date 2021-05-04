package nl.han.ica.icss.parser;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Stack;


/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private Stack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new Stack<>();
	}

	public AST getAST() {
		return ast;
	}

	@Override public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		currentContainer.push(ast.root);
	}

	@Override public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		currentContainer.pop();
	}

	/*
	* Er wordt een nieuwe pixelliteral aangemaakt met de bijbehorende context
	* De literal wordt toegevoegd aan het uiteinde van de stack
	* */
	@Override public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
		Literal literal = new PixelLiteral(ctx.getText());
		currentContainer.peek().addChild(literal);
		currentContainer.push(literal);
	}

	@Override public void exitPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		Literal literal = new ColorLiteral(ctx.getText());
		currentContainer.peek().addChild(literal);
		currentContainer.push(literal);
	}

	@Override public void exitColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
		Literal literal = new PercentageLiteral(ctx.getText());
		currentContainer.peek().addChild(literal);
		currentContainer.push(literal);
	}

	@Override public void exitPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
		Literal literal = new ScalarLiteral(ctx.getText());
		currentContainer.peek().addChild(literal);
		currentContainer.push(literal);
	}

	@Override public void exitScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
		VariableReference variableReference = new VariableReference(ctx.getText());
		currentContainer.peek().addChild(variableReference);
		currentContainer.push(variableReference);
	}

	@Override public void exitVariableReference(ICSSParser.VariableReferenceContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		VariableAssignment variableAssignment = new VariableAssignment();
		currentContainer.peek().addChild(variableAssignment);
		currentContainer.push(variableAssignment);
	}

	@Override public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
		Selector selector = new ClassSelector(ctx.getText());
		currentContainer.peek().addChild(selector);
		currentContainer.push(selector);
	}

	@Override public void exitClassSelector(ICSSParser.ClassSelectorContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
		Selector selector = new TagSelector(ctx.getText());
		currentContainer.peek().addChild(selector);
		currentContainer.push(selector);
	}

	@Override public void exitTagSelector(ICSSParser.TagSelectorContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
		Selector selector = new IdSelector(ctx.getText());
		currentContainer.peek().addChild(selector);
		currentContainer.push(selector);
	}

	@Override public void exitIdSelector(ICSSParser.IdSelectorContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterAddOperation(ICSSParser.AddOperationContext ctx) {
		Operation operation = new AddOperation();
		currentContainer.peek().addChild(operation);
		currentContainer.push(operation);
	}

	@Override public void exitAddOperation(ICSSParser.AddOperationContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
		Operation operation = new MultiplyOperation();
		currentContainer.peek().addChild(operation);
		currentContainer.push(operation);
	}

	@Override public void exitMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterSubtractOperation(ICSSParser.SubtractOperationContext ctx) {
		Operation operation = new SubtractOperation();
		currentContainer.peek().addChild(operation);
		currentContainer.push(operation);
	}

	@Override public void exitSubtractOperation(ICSSParser.SubtractOperationContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
		PropertyName propertyName= new PropertyName(ctx.getText());
		currentContainer.peek().addChild(propertyName);
		currentContainer.push(propertyName);
	}

	@Override public void exitPropertyName(ICSSParser.PropertyNameContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterProperty(ICSSParser.PropertyContext ctx) {
	}

	@Override public void exitProperty(ICSSParser.PropertyContext ctx) {

	}

	@Override public void enterLiteral(ICSSParser.LiteralContext ctx) {

	}

	@Override public void exitLiteral(ICSSParser.LiteralContext ctx) {

	}

	@Override public void enterSelector(ICSSParser.SelectorContext ctx) {

	}

	@Override public void exitSelector(ICSSParser.SelectorContext ctx) {

	}

	@Override public void enterStyleRule(ICSSParser.StyleRuleContext ctx) {
		Stylerule stylerule= new Stylerule();
		currentContainer.peek().addChild(stylerule);
		currentContainer.push(stylerule);
	}

	@Override public void exitStyleRule(ICSSParser.StyleRuleContext ctx) {
		currentContainer.pop();
	}

	@Override public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		currentContainer.push(new Declaration());
	}

	@Override public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		ASTNode declaration = currentContainer.pop();
		currentContainer.peek().addChild(declaration);
	}


	@Override public void enterEveryRule(ParserRuleContext ctx) {

	}

	@Override public void exitEveryRule(ParserRuleContext ctx) {

	}

	@Override public void visitTerminal(TerminalNode node) {

	}

	@Override public void visitErrorNode(ErrorNode node) {

	}

}
