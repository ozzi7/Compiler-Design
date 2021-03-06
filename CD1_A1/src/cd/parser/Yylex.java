/**
 * Scanner
 * Compiler Construction I 
 */
package cd.parser;
import static cd.parser.sym.ASTERISK;
import static cd.parser.sym.BANG;
import static cd.parser.sym.BANGEQ;
import static cd.parser.sym.BOOLEAN;
import static cd.parser.sym.BOOLVAL;
import static cd.parser.sym.CLASS;
import static cd.parser.sym.COMMA;
import static cd.parser.sym.DOUBLEAMP;
import static cd.parser.sym.DOUBLEEQ;
import static cd.parser.sym.DOUBLEPIPE;
import static cd.parser.sym.ELSE;
import static cd.parser.sym.EOF;
import static cd.parser.sym.EQ;
import static cd.parser.sym.EXTENDS;
import static cd.parser.sym.GREATER;
import static cd.parser.sym.GREATEREQ;
import static cd.parser.sym.IDENTIFIER;
import static cd.parser.sym.IF;
import static cd.parser.sym.INTEGER;
import static cd.parser.sym.INTVAL;
import static cd.parser.sym.LC;
import static cd.parser.sym.LESS;
import static cd.parser.sym.LESSEQ;
import static cd.parser.sym.LP;
import static cd.parser.sym.LS;
import static cd.parser.sym.MINUS;
import static cd.parser.sym.NEW;
import static cd.parser.sym.NULL;
import static cd.parser.sym.PERCENT;
import static cd.parser.sym.PERIOD;
import static cd.parser.sym.PLUS;
import static cd.parser.sym.RC;
import static cd.parser.sym.READ;
import static cd.parser.sym.RP;
import static cd.parser.sym.RS;
import static cd.parser.sym.SEMICOLON;
import static cd.parser.sym.SLASH;
import static cd.parser.sym.THIS;
import static cd.parser.sym.VOID;
import static cd.parser.sym.WHILE;
import static cd.parser.sym.WRITE;
import static cd.parser.sym.WRITELN;
import java_cup.runtime.Symbol;
import cd.exceptions.ParseFailure;


public class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 256;
	private final int YY_EOF = 257;

	public Integer intValue(String s, int radix) {
		try {
			return Integer.valueOf(s, radix);
		} catch (NumberFormatException e) {
			throw new ParseFailure(yyline+1, "Illegal integer: " + s);
		}
	} 
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	public Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		58
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NOT_ACCEPT,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NOT_ACCEPT,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,258,
"44:9,46,1,44,46,45,44:18,46,3,44:3,21,6,44,12,13,20,17,15,18,14,19,42,41:9," +
"44,16,4,2,5,44:2,43:6,40:20,10,44,11,44:3,27,31,33,35,25,26,40,39,36,40:2,2" +
"8,40,30,32,40:2,23,29,22,24,38,37,34,40:2,8,7,9,44:130,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,106,
"0,1:2,2,3,4,5,6,1:11,7,8,1,9,10,1:6,11,1:2,12:3,13,12:8,14,12:4,1:3,13,15,1" +
"6,17,18,19,1,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40" +
",41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,12,59,60,61,62,63,64")[0];

	private int yy_nxt[][] = unpackFromString(65,47,
"1,2,3,4,5,6,7,54,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,97,99,100,101,9" +
"9:3,75,102,99,103,99:2,55,104,105,99:2,23,56,99,59,2:2,-1:49,24,-1:46,25,-1" +
":46,26,-1:46,27,-1:50,28,-1:59,30,31,-1:45,32,-1:49,99,76,99:15,77,99:4,-1:" +
"44,23:2,-1:6,30:45,-1:22,99:22,-1:28,36:3,-1:3,36,-1,36,-1,36,-1:5,36:3,-1:" +
"25,99:6,74,99:15,-1:10,29,-1:61,99:4,33,99:3,61,99:13,-1:37,53,-1:6,23:2,-1" +
":23,52,-1:27,1,50,51:18,57,51:24,50,51,-1:22,99:15,34,99:6,-1:25,35,99:21,-" +
"1:25,99:3,37,99:18,-1:25,99:7,38,99:14,-1:25,99:13,39,99:8,-1:25,99:3,40,99" +
":18,-1:25,99:6,41,99:15,-1:25,99:13,42,99:8,-1:25,99:3,43,99:18,-1:25,99:7," +
"44,99:14,-1:25,99:3,45,99:18,-1:25,99:3,46,99:18,-1:25,99:7,47,99:14,-1:25," +
"99:8,48,99:13,-1:25,99:8,49,99:13,-1:25,99:2,82,60,99:18,-1:25,99:2,62,99:1" +
"9,-1:25,99:14,63,99:7,-1:25,99:5,64,99:16,-1:25,99:7,65,99:14,-1:25,87,99:2" +
"1,-1:25,99:6,88,99:15,-1:25,99:6,66,99:15,-1:25,99:10,89,99:11,-1:25,99:5,9" +
"0,99:16,-1:25,99:14,91,99:7,-1:25,99:14,67,99:7,-1:25,99:3,93,99:18,-1:25,9" +
"9:7,68,99:14,-1:25,99:6,94,99:15,-1:25,99:7,69,99:14,-1:25,70,99:21,-1:25,9" +
"9:6,71,99:15,-1:25,99:8,95,99:13,-1:25,99:3,96,99:18,-1:25,99:13,72,99:8,-1" +
":25,99:5,73,99:16,-1:25,99:3,78,99:18,-1:25,99:14,92,99:7,-1:25,99:6,79,99:" +
"5,80,99:9,-1:25,99:5,81,99:16,-1:25,99:10,83,99:11,-1:25,99:6,84,99:15,-1:2" +
"5,99,85,99:15,98,99:4,-1:25,99:10,86,99:11,-1:3");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

    return new Symbol(EOF, yyline+1, 0);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ /* ignore white space. */ }
					case -3:
						break;
					case 3:
						{ return new Symbol(EQ,         yyline+1, 0); }
					case -4:
						break;
					case 4:
						{ return new Symbol(BANG,       yyline+1, 0); }
					case -5:
						break;
					case 5:
						{ return new Symbol(LESS,       yyline+1, 0); }
					case -6:
						break;
					case 6:
						{ return new Symbol(GREATER,    yyline+1, 0); }
					case -7:
						break;
					case 7:
						{
    throw new ParseFailure(yyline+1, "Illegal character: " + yytext());
}
					case -8:
						break;
					case 8:
						{ return new Symbol(LC,         yyline+1, 0); }
					case -9:
						break;
					case 9:
						{ return new Symbol(RC,         yyline+1, 0); }
					case -10:
						break;
					case 10:
						{ return new Symbol(LS,         yyline+1, 0); }
					case -11:
						break;
					case 11:
						{ return new Symbol(RS,         yyline+1, 0); }
					case -12:
						break;
					case 12:
						{ return new Symbol(LP,         yyline+1, 0); }
					case -13:
						break;
					case 13:
						{ return new Symbol(RP,         yyline+1, 0); }
					case -14:
						break;
					case 14:
						{ return new Symbol(PERIOD,     yyline+1, 0); }
					case -15:
						break;
					case 15:
						{ return new Symbol(COMMA,      yyline+1, 0); }
					case -16:
						break;
					case 16:
						{ return new Symbol(SEMICOLON,  yyline+1, 0); }
					case -17:
						break;
					case 17:
						{ return new Symbol(PLUS,       yyline+1, 0); }
					case -18:
						break;
					case 18:
						{ return new Symbol(MINUS,      yyline+1, 0); }
					case -19:
						break;
					case 19:
						{ return new Symbol(SLASH,      yyline+1, 0); }
					case -20:
						break;
					case 20:
						{ return new Symbol(ASTERISK,   yyline+1, 0); }
					case -21:
						break;
					case 21:
						{ return new Symbol(PERCENT,    yyline+1, 0); }
					case -22:
						break;
					case 22:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -23:
						break;
					case 23:
						{ return new Symbol(INTVAL,     yyline+1, 0, intValue(yytext(), 10)); }
					case -24:
						break;
					case 24:
						{ return new Symbol(DOUBLEEQ,   yyline+1, 0); }
					case -25:
						break;
					case 25:
						{ return new Symbol(BANGEQ,     yyline+1, 0); }
					case -26:
						break;
					case 26:
						{ return new Symbol(LESSEQ,     yyline+1, 0); }
					case -27:
						break;
					case 27:
						{ return new Symbol(GREATEREQ,  yyline+1, 0); }
					case -28:
						break;
					case 28:
						{ return new Symbol(DOUBLEAMP,  yyline+1, 0); }
					case -29:
						break;
					case 29:
						{ return new Symbol(DOUBLEPIPE, yyline+1, 0); }
					case -30:
						break;
					case 30:
						{ /* ignore comment */ }
					case -31:
						break;
					case 31:
						{ yybegin(COMMENT); }
					case -32:
						break;
					case 32:
						{
    throw new ParseFailure(yyline+1, "Comment not opened");
}
					case -33:
						break;
					case 33:
						{ return new Symbol(IF,         yyline+1, 0); }
					case -34:
						break;
					case 34:
						{ return new Symbol(NEW,        yyline+1, 0); }
					case -35:
						break;
					case 35:
						{ return new Symbol(INTEGER,    yyline+1, 0); }
					case -36:
						break;
					case 36:
						{ return new Symbol(INTVAL,     yyline+1, 0, intValue(yytext().substring(2), 16)); }
					case -37:
						break;
					case 37:
						{ return new Symbol(BOOLVAL,    yyline+1, 0, true); }
					case -38:
						break;
					case 38:
						{ return new Symbol(THIS,       yyline+1, 0); }
					case -39:
						break;
					case 39:
						{ return new Symbol(READ,       yyline+1, 0); }
					case -40:
						break;
					case 40:
						{ return new Symbol(ELSE,       yyline+1, 0); }
					case -41:
						break;
					case 41:
						{ return new Symbol(NULL,       yyline+1, 0); }
					case -42:
						break;
					case 42:
						{ return new Symbol(VOID,       yyline+1, 0); }
					case -43:
						break;
					case 43:
						{ return new Symbol(BOOLVAL,    yyline+1, 0, false); }
					case -44:
						break;
					case 44:
						{ return new Symbol(CLASS,      yyline+1, 0); }
					case -45:
						break;
					case 45:
						{ return new Symbol(WRITE,      yyline+1, 0); }
					case -46:
						break;
					case 46:
						{ return new Symbol(WHILE,      yyline+1, 0); }
					case -47:
						break;
					case 47:
						{ return new Symbol(EXTENDS,    yyline+1, 0); }
					case -48:
						break;
					case 48:
						{ return new Symbol(BOOLEAN,    yyline+1, 0); }
					case -49:
						break;
					case 49:
						{ return new Symbol(WRITELN,    yyline+1, 0); }
					case -50:
						break;
					case 50:
						{ /* ignore comments. */ }
					case -51:
						break;
					case 51:
						{ /* ignore comments */ }
					case -52:
						break;
					case 52:
						{ yybegin(YYINITIAL); }
					case -53:
						break;
					case 54:
						{
    throw new ParseFailure(yyline+1, "Illegal character: " + yytext());
}
					case -54:
						break;
					case 55:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -55:
						break;
					case 56:
						{ return new Symbol(INTVAL,     yyline+1, 0, intValue(yytext(), 10)); }
					case -56:
						break;
					case 57:
						{ /* ignore comments */ }
					case -57:
						break;
					case 59:
						{
    throw new ParseFailure(yyline+1, "Illegal character: " + yytext());
}
					case -58:
						break;
					case 60:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -59:
						break;
					case 61:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -60:
						break;
					case 62:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -61:
						break;
					case 63:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -62:
						break;
					case 64:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -63:
						break;
					case 65:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -64:
						break;
					case 66:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -65:
						break;
					case 67:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -66:
						break;
					case 68:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -67:
						break;
					case 69:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -68:
						break;
					case 70:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -69:
						break;
					case 71:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -70:
						break;
					case 72:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -71:
						break;
					case 73:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -72:
						break;
					case 74:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -73:
						break;
					case 75:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -74:
						break;
					case 76:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -75:
						break;
					case 77:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -76:
						break;
					case 78:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -77:
						break;
					case 79:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -78:
						break;
					case 80:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -79:
						break;
					case 81:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -80:
						break;
					case 82:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -81:
						break;
					case 83:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -82:
						break;
					case 84:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -83:
						break;
					case 85:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -84:
						break;
					case 86:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -85:
						break;
					case 87:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -86:
						break;
					case 88:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -87:
						break;
					case 89:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -88:
						break;
					case 90:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -89:
						break;
					case 91:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -90:
						break;
					case 92:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -91:
						break;
					case 93:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -92:
						break;
					case 94:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -93:
						break;
					case 95:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -94:
						break;
					case 96:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -95:
						break;
					case 97:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -96:
						break;
					case 98:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -97:
						break;
					case 99:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -98:
						break;
					case 100:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -99:
						break;
					case 101:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -100:
						break;
					case 102:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -101:
						break;
					case 103:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -102:
						break;
					case 104:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -103:
						break;
					case 105:
						{ return new Symbol(IDENTIFIER, yyline+1, 0, yytext()); }
					case -104:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
