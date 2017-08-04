package cd;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import cd.codegen.AstCodeGenerator;
import cd.debug.AstDump;
import cd.exceptions.ParseFailure;
import cd.ir.Ast.ClassDecl;
import cd.ir.Ast.UnaryOp;
import cd.parser.JavaliLexer;
import cd.parser.JavaliParser;
import cd.parser.JavaliWalker;

/** 
 * The main entrypoint for the compiler.  Consists of a series
 * of routines which must be invoked in order.  The main()
 * routine here invokes these routines, as does the unit testing
 * code. This is not the <b>best</b> programming practice, as the
 * series of calls to be invoked is duplicated in two places in the
 * code, but it will do for now. */
public class Main {
	
	// Set to non-null to write debug info out
	public Writer debug = null;
	
	public void debug(String format, Object... args) {
		if (debug != null) {
			String result = String.format(format, args);
			try {
				debug.write(result);
				debug.write('\n');
				debug.flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/** Parse command line, invoke compile() routine */
	public static void main(String args[]) throws IOException {
		
		
		Main m = new Main();
		
		args = new String[1];
		args[0] = "/home/stefan/workspace/nameyoulike/CD1_A2/javali_tests/A2/SimpleReturn.javali";
		
		for (String file : args) {
			
			if (file.equals("-d"))
				m.debug = new OutputStreamWriter(System.err);
			else {
				FileReader fin = new FileReader(file);

				// Parse:
				List<ClassDecl> astRoots = m.parse(file, fin, false);
				
				// Run the semantic check:
				m.semanticCheck(astRoots);
				
				// Generate code:
				String sFile = file + Config.ASMEXT;
				FileWriter fout = new FileWriter(sFile);
				m.generateCode(astRoots, fout);
				fout.close();
			}
		}
	}
	
	public Main() {
	}

	public List<ClassDecl> parse(Reader file, boolean debugParser)  throws IOException {
		return parse(null, file, debugParser);
	}
	
	/** Parses an input stream into an AST 
	 * @throws IOException */
	public List<ClassDecl> parse(String fileName, Reader file, boolean debugParser)  throws IOException {
		List<ClassDecl> result = new ArrayList<ClassDecl>();
		result = parseWithAntlr(fileName, file);
		return result;
	}
	
	public List<ClassDecl> parseWithAntlr(String file, Reader reader) throws IOException {
		
		try {
			
			ANTLRReaderStream input = new ANTLRReaderStream(reader);
			JavaliLexer lexer = new JavaliLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			
			JavaliParser parser = new JavaliParser(tokens);
			JavaliParser.unit_return parserReturn;
			parserReturn = parser.unit();
			
			CommonTreeNodeStream nodes = new CommonTreeNodeStream(parserReturn.getTree());
			nodes.setTokenStream(tokens);
			
			JavaliWalker walker = new JavaliWalker(nodes);
			
			debug("AST Resulting From Parsing Stage:");
			List<ClassDecl> result = walker.unit();
			
			dumpAst(result);
			
			return result;
			
		} catch (RecognitionException e) {
			ParseFailure pf = new ParseFailure(0, "?");
			pf.initCause(e);
			throw pf;
		}
	}
	
	public void semanticCheck(List<ClassDecl> astRoots) {
	}
	
	public void generateCode(List<ClassDecl> astRoots, Writer out) {
		AstCodeGenerator cg = new AstCodeGenerator(this, out);
		cg.go(astRoots);
	}

	/** Dumps the AST to the debug stream */
	private void dumpAst(List<ClassDecl> astRoots) throws IOException {
		if (this.debug == null) return;
		this.debug.write(AstDump.toString(astRoots));
	}
}
