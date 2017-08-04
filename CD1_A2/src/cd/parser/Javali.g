/**************************************************************************************************
 *   JAVALI PARSER GRAMMAR
 *   Compiler Construction I
 *
 *   ANTLR TIPS AND TRICKS:
 *   -----------------------
 *   
 *   Overview:
 *   ----------
 *
 *   ANTLR is a top-down (LL) parser generator. Given a grammar file
 *   (*.g), ANTLR generates a parser accepting programs written in the
 *   language specified by the grammar. By default, the generated
 *   parser is a Java program.
 *
 *   An ANTLR grammar file (*.g) consists of several sections. In this
 *   grammar file we make use of the following sections:
 *
 *   (1) option declarationsfile:///home/stefan/workspace/nameyoulike/CD1_A2/src/cd/parser/JavaliWalker.g

 *   (2) token declarations
 *   (3) action declarations
 *   (4) rule declarations
 *
 *   The rule section must appear at the end. Options (1) alter the
 *   way ANTLR generates code. In this grammar file we use the option
 *   'output=AST', which allows us to apply rewriting rules for AST
 *   construction. The token declaration section (2) declares
 *   tokens. For each declared token, ANTLR generates a corresponding
 *   ANTLR AST node. Rules (4) are either lexer rules (4-a) or parser
 *   rules (4-b).  Lexer rules (4-a) must begin with an upper case
 *   letter and describe tokens that occur in the input stream.
 *   Parser rules (4-b) must begin with a lower case letter and
 *   describe grammar productions.  For each rule in a grammar file,
 *   ANTLR generates a corresponding method in the generated
 *   parser. Using the action declaration section (3), additional
 *   fields and methods can be declared thafile:///home/stefan/workspace/nameyoulike/CD1_A2/src/cd/parser/JavaliWalker.g
t ANTLR inserts into the
 *   generated parser.
 *
 *   In this assignment, we provide the necessary option (1) and
 *   action (3) declarations, however, you must provide the
 *   appropriate token (2) and rule (4) declarations.
 *
 *   Imaginary tokens:
 *   ---------------
 *
 *   Using the tokens{...} specification, either imaginary tokens can
 *   be defined or aliases for token literals can be defined. An
 *   imaginary token is not associated with a particular input
 *   character (token literal) but is helpful for AST construction. An
 *   imaginary token can be assigned the line and column information
 *   from a token literal appearing in the input. The text of an
 *   imaginary token can be explicitly set to avoid copying the text
 *   from the literal token. E.g., in below example, an imaginary
 *   token ARGUMENTS is created and associated with the actual token
 *   '('. As a result, the imaginary token gets the line and column
 *   number information of the token '(', but hast the text
 *   "ARGUMENTS".
 *
 *   arguments
 *	:	lc='(' expressionlist? ')'
 *		-> ^(ARGUMENTS[$lc,"ARGUMENTS"] expressionlist?)
 *	;
 *
 *   Rules:
 *   ------
 *
 *   ANTLR supports EBNF. The following operators have the following meaning:
 *
 *   x?  x occurs at most once
 *   x*  x occurs zero or several times (left-associative)
 *   x+  x occurs once or several times (left-associative)
 *
 *   Note: Parentheses must be used to apply an operator to a group of tokens!
 *
 *
 *   Left-Associativity:   a op b op c = ( a op b ) op c
 *   Can in principle be achieved  using left recursion:
 *   E ::= E op T | T
 *   However, left-recursive rules are not accepted by ANTLR (see below)! Thus, left
 *   recursion must either be eliminated or, preferably, the EBNF repetition operator
 *   should be used:
 *   E ::= T ( op T)*
 *
 *   Right-Associativity:   a op b op c = a op ( b op c)
 *   Can be achieved using right recursion:
 *   E ::= T op E | T
 *   Above rule might have to be left factored (see below). Preferably, the EBNF '?'
 *   operator should be used:
 *   E ::= T ( op E )?
 *
 *   Left recursion, non-LL(*) decisions, and left-factoring:
 *   ----------------------------------------------
 *   
 *   Being a top-down parser generator, ANTLR cannot deal with
 *   left-recursive rules. As a consequence, any left recursion must
 *   be eliminated.
 *
 *   In a top-down parser, a production alternative must be selected
 *   based on the tokens seen next in the input.  In ANTLR, the number
 *   of tokens used for the decision can be explicitly indicated
 *   (LL(k)) or, alternatively, a variable lookahead (LL(*)) can be
 *   used. For this assignment, we use a variable lookahead. The
 *   selection of a production alternative based on a number k of
 *   lookahead tokens can generally be done by a deterministic finite
 *   automaton (DFA) that recognizes strings of length k and that has
 *   accepting states for each alternative in question. A variable
 *   number of tokens lookahead (LL(*)) can be supported by allowing
 *   cyclic DFAs.
 *
 *   Although LL(*) is clearly superior to LL(k) and therefore accepts
 *   grammars that would not be accepted using a fixed number of
 *   lookahead tokens, there may still be cases for which ANTLR
 *   reports that a rule has "non-LL(*) decisions" even if the grammar
 *   is not ambiguous. This can be the case if the lookahead language
 *   is not regular (i.e., cannot be recognized by a DFA) and/or if
 *   ANTLR does not succeed constructing the DFA for the lookahead due
 *   to recursive (i.e., repetitive) constructs. ANTLR is actually
 *   only capable of constructing the DFA for the lookahead language
 *   in case of recursive constructs as long as only one alternative
 *   is recursive and as long as the internal recursion overflow
 *   constant is sufficiently large (see ANTLR book, page 271). The
 *   only remedy for non-LL(*) decisions is grammar left-factoring.
 *
 *   For example, the grammar below uses non-regular constructs
 *   (nested parentheses) and can therefore not be parsed using the
 *   LL(*) option.
 *
 *   se  =  e '%'  |  e '!' ;
 *   e   =  '(' e ')' | ID ;
 *
 *   In below example, the fact that both alternatives in s are
 *   (indirectly) recursive causes troubles.
 *
 *   s  =  label ID '=' expr
 *      |  label 'return' expr  ;
 *
 *   label = ID ':' label  |  ;
 *
 *   If above grammar is rewritten to use the EBNF looping syntax
 *   instead, however, ANTLR is capable of identifying the looping
 *   construct and constructing the cyclic DFA.
 *
 *   s  =  label ID '=' expr
 *      |  label 'return' expr  ;
 *
 *   label = ( ID ':' )*  ;
 *
 *   And in the following example, finally, ANTLR is capable of
 *   constructing the corresponding DFA for the lookahead language as
 *   only one alternative is recursive and as the looping analysis
 *   recurses less often than specified by the recursion overflow
 *   threshold (see ANTLR book, p. 271):
 *
 *   a  = L a R
 *      | L L X  ;
 *
**************************************************************************************************/

grammar Javali; // parser grammar, parses streams of tokens

// (1) OPTIONS
options {output=AST;}

// (2) TOKENS
tokens {
	ClassDecl;
	VarDecl;
	VarDeclList;
	MethodDecl;
	MethodBody;
	Seq;
	BuiltInWrite;
	BuiltInWriteFloat;
	BuiltInWriteln;
	Assign;
	MethodCall;
	ReturnStmt;
	IfElse;
	IfStmt;
	WhileLoop;
	NewObject;
	NewArray;
	BuiltInRead;
	BuiltInReadFloat;
	B_Op;
	B_TIMES;
	B_DIV;
	B_MOD;
	B_PLUS;
	B_MINUS;
	B_AND;
	B_OR;
	B_EQUAL;
	B_NOT_EQUAL;
	B_LESS_THAN;
	B_LESS_OR_EQUAL;
	B_GREATER_THAN;
	B_GREATER_OR_EQUAL;
	U_PLUS; 
	U_MINUS;
	U_BOOL_NOT;
	Cast;
	DecimalIntConst;
	HexIntConst;
	FloatConst;
	BooleanConst;
	NullConst;
	Field;
	Index;
	Var;
	ThisRef;
	Identifier;
	ArrayType;
	Object;
	RefType;
	SelectorField;
	SelectorMethod;
	SelectorArray;
	DeclList;
	Type;
}

// (3) ACTIONS
@header {
package cd.parser;
}

@lexer::header {
package cd.parser;
}

// These code fragments in @members and @rulecatch are required to get a strict parser
// that stops on syntax errors and does NOT try to recover and continue.
// See page 242 of "The Definitive ANTLR Reference".
@members {
protected void mismatch(TokenStream input, int ttype, BitSet follow) throws RecognitionException {
	throw new MismatchedTokenException(ttype, input);
}

public void recoverFromMismatchedSet(TokenStream input, RecognitionException e, BitSet follow) throws RecognitionException {
	throw e;
}

protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {   
	throw new MismatchedTokenException(ttype, input);
}   
}

@rulecatch {
catch (RecognitionException re) {
	reportError(re);
	throw re;
}
}

// (4-a) PARSER RULES
// DECLARATIONS

unit
	:	classDecl+ EOF  -> classDecl+
	;
	
classDecl 
	:	'class' Identifier '{' declList? '}' -> ^(ClassDecl  Identifier Identifier["Object"] declList?) 
	|	'class' Identifier 'extends' Identifier '{' declList? '}' -> ^(ClassDecl  Identifier Identifier declList?)
	; 

declList
	:	varOrMethodDecl+ -> ^(DeclList varOrMethodDecl+)
	;
	
varOrMethodDecl
	:	( varDecl | methodDecl ) 
	; 

varDecl
	:	type Identifier ';' -> ^(VarDecl type Identifier)
	|	type Identifier ( ',' Identifier )+ ';' -> ^(VarDecl type Identifier)+
	; 
 
methodDecl
	: 	methodHeading methodBody -> ^(MethodDecl methodHeading methodBody)
	;
	
methodHeading
   	:	type Identifier '(' formalParamList? ')' -> type Identifier  formalParamList?
   	|  	'void' Identifier '(' formalParamList? ')' -> 'void' Identifier formalParamList?
   	;

formalParamList
	:	type Identifier ( ',' type Identifier )*  -> ^(VarDecl type Identifier)+
	;

//methodBody
//   	: 	 methodBodyWithDeclList  (  stmtList '}' | '}' ) -> ^(MethodBody methodBodyWithDeclList stmtList)
//   	|	 '{' stmtList '}' -> ^(MethodBody  stmtList)
//   	|	 '{'  '}' -> ^(MethodBody)
//   	;
   	
methodBody
	: '{' declList? stmtList '}' -> ^(MethodBody declList? stmtList)
	;

methodBodyWithDeclList
   	: 	'{' declList -> declList
   	;
   
// STATEMENTS

stmtList
	:	(stmt*) -> ^(Seq stmt*) 
	;

stmt
	:	assignmentOrMethodCall ';' -> assignmentOrMethodCall
	|  	ioStmt
	|	ifStmt
	|	whileStmt
	|  	returnStmt
	;

assignmentOrMethodCall
   : 		 identAccess
      		(  -> identAccess
       	| 	assignmentTail -> ^(Assign identAccess assignmentTail))
   ;

assignmentTail
	: 	'=' assignmentRHS -> assignmentRHS
	;

assignmentRHS
	:	( expr | newExpr | readExpr | readExprFloat )
	;

methodCallTail
	:	'(' actualParamList? ')' -> ^(Seq actualParamList?)
	;

actualParamList
	:	expr ( ',' expr )* -> expr+
	;
	
ioStmt
	: 	'write' '(' expr ')' ';' -> ^(BuiltInWrite expr)
  	| 	'writef' '(' expr ')' ';' -> ^(BuiltInWriteFloat expr)
   	| 	'writeln' '(' ')' ';' -> BuiltInWriteln
	;
   
ifStmt
	:	'if' '(' expr ')' fst=stmtBlock
		( -> ^(IfStmt expr $fst)
	|	'else' snd=stmtBlock -> ^(IfElse expr $fst $snd))
	;

whileStmt
	: 	'while' '(' expr ')' stmtBlock -> ^(WhileLoop expr stmtBlock )
	;

returnStmt
	:  	'return' expr? ';' -> ^(ReturnStmt expr?)
	;
   
stmtBlock
	:	'{' stmtList '}' -> stmtList
	;
	
// EXPRESSIONS

newExpr
	:	'new' Identifier '(' ')' -> ^(NewObject Identifier)
	|	'new' Identifier '[' simpleExpr ']' -> ^(NewArray Identifier simpleExpr)
	|	'new' primitiveType '[' simpleExpr ']'  -> ^(NewArray primitiveType simpleExpr)
	;

readExpr
	:	 'read' '(' ')' -> BuiltInRead
	;

readExprFloat
  	 :	'readf' '(' ')' -> BuiltInReadFloat
   	;

expr
	:	(simpleExpr -> simpleExpr)
		(compOp rhs=simpleExpr -> ^(B_Op $expr compOp $rhs))*
	;

compOp
	:	'==' -> B_EQUAL
	|	'!=' -> B_NOT_EQUAL
	|	'<' -> B_LESS_THAN
	|  	'<=' -> B_LESS_OR_EQUAL
	|	'>' -> B_GREATER_THAN
	|	'>=' -> B_GREATER_OR_EQUAL
	;

simpleExpr
	:	( term  -> term)
		( weakOp right=term -> ^(B_Op $simpleExpr weakOp $right))*
	;

weakOp
	:	'+' -> B_PLUS
	|	'-'  -> B_MINUS
	|	'||' -> B_OR
	;

term
	:	( factor -> factor) 
		(strongOp right=factor -> ^(B_Op $term strongOp $right))* 
	;

strongOp
	: 	'*' -> B_TIMES
	| 	'/' -> B_DIV
	| 	'%' -> B_MOD
	| 	'&&' -> B_AND
	;

factor
	: 	'+' noSignFactor -> ^(U_PLUS noSignFactor)
	| 	'-' noSignFactor -> ^(U_MINUS noSignFactor)
	| 	noSignFactor -> noSignFactor
	;

noSignFactor
	:	'!' factor -> ^(U_BOOL_NOT factor) 
	|	DecimalNumber -> ^(DecimalIntConst DecimalNumber)
	|	HexNumber -> ^(HexIntConst HexNumber)
	|	FloatNumber -> ^(FloatConst FloatNumber)
	|	BooleanLiteral -> ^(BooleanConst BooleanLiteral)
	|	'null' -> NullConst
   	|	identAccess -> identAccess
   	|	'(' expr ')' -> expr
	|	'(' referenceType ')' noSignFactor -> ^(Cast referenceType noSignFactor)
	;

identAccess
  :
  		identPart selectorSeq? //-> ^(IdentAccess identPart  selectorSeq?)
  ;	
  
identPart
   : 
      		Identifier -> ^(Var Identifier)
      	| 	Identifier methodCallTail -> ^(MethodCall Identifier methodCallTail)
      	|	'this' -> ThisRef 
   	;
   

   
selectorSeq
   	: 
   		( fieldSelector | elemSelector )
   		(fieldSelector | elemSelector)*
   	;

fieldSelector
   	:	 '.' Identifier methodCallTail -> ^(SelectorMethod Identifier methodCallTail) 
   	|	 '.' Identifier  -> ^(SelectorField Identifier)
   	;

elemSelector
   	:	  '[' simpleExpr ']' -> ^(SelectorArray simpleExpr)
   	;

// TYPES

type
	:	a=primitiveType -> Type[$a.text]
	|	b=referenceType -> Type[$b.text]
	;

referenceType
	:	Identifier -> ^(RefType Identifier)
	|	arrayType
	;

primitiveType
	:	'int'
	|  	'float'
	|	'boolean'
	;

arrayType 
	:	a=Identifier '[' ']' -> ^(RefType Identifier[$a.text+"[]"])
	|	a=(primitiveType) '[' ']' -> ^(RefType Identifier[$a.text+"[]"])
	;

// (4-b) LEXER RULES
DecimalNumber
	:	'0' 
	|	'1'..'9' ('0'..'9')*
	;
	
DigitNumber
   	:	 '0'..'9' ('0'..'9')*
   	;
	
FloatNumber
   	: 	DecimalNumber '.' DigitNumber
   	;	

HexNumber
	:	HexPrefix HexDigit+
	;

fragment
HexPrefix
   	:	'0x' | '0X'
    	;
        
fragment
HexDigit
    	:	('0'..'9'|'a'..'f'|'A'..'F')
    	;

BooleanLiteral
	:	'true'
	|	'false'
	;

Identifier 
	:	Letter (Letter|JavaIDDigit)*
	;

fragment
Letter
	:	'\u0024'
	|	'\u0041'..'\u005a'
	|	'\u005f'
	|	'\u0061'..'\u007a'
	|	'\u00c0'..'\u00d6'
	|	'\u00d8'..'\u00f6'
	|	'\u00f8'..'\u00ff'
	|	'\u0100'..'\u1fff'
	|	'\u3040'..'\u318f'
	|	'\u3300'..'\u337f'
	|	'\u3400'..'\u3d2d'
	|	'\u4e00'..'\u9fff'
	|	'\uf900'..'\ufaff'
	;

fragment
JavaIDDigit
	:	'\u0030'..'\u0039'
	|	'\u0660'..'\u0669'
	|	'\u06f0'..'\u06f9'
	|	'\u0966'..'\u096f'
	|	'\u09e6'..'\u09ef'
	|	'\u0a66'..'\u0a6f'
	|	'\u0ae6'..'\u0aef'
	|	'\u0b66'..'\u0b6f'
	|	'\u0be7'..'\u0bef'
	|	'\u0c66'..'\u0c6f'
	|	'\u0ce6'..'\u0cef'
	|	'\u0d66'..'\u0d6f'
	|	'\u0e50'..'\u0e59'
	|	'\u0ed0'..'\u0ed9'
	|	'\u1040'..'\u1049'
	;

WS
	:	(' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
	;

COMMENT
	:	'/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
	;

LINE_COMMENT
	:	'//' ~('\n'|'\r')*  ('\r\n' | '\r' | '\n')? {$channel=HIDDEN;}
	;
