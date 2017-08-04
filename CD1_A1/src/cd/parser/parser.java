





package cd.parser;

import java.util.ArrayList;
import java.util.List;

import java_cup.runtime.Symbol;
import cd.Main;
import cd.exceptions.ParseFailure;
import cd.ir.Ast;
import cd.ir.Ast.Assign;
import cd.ir.Ast.BinaryOp;
import cd.ir.Ast.BinaryOp.BOp;
import cd.ir.Ast.BooleanConst;
import cd.ir.Ast.BuiltInRead;
import cd.ir.Ast.BuiltInWrite;
import cd.ir.Ast.BuiltInWriteln;
import cd.ir.Ast.Cast;
import cd.ir.Ast.ClassDecl;
import cd.ir.Ast.Expr;
import cd.ir.Ast.Field;
import cd.ir.Ast.IfElse;
import cd.ir.Ast.Index;
import cd.ir.Ast.IntConst;
import cd.ir.Ast.MethodCall;
import cd.ir.Ast.MethodDecl;
import cd.ir.Ast.NewArray;
import cd.ir.Ast.NewObject;
import cd.ir.Ast.Nop;
import cd.ir.Ast.NullConst;
import cd.ir.Ast.Seq;
import cd.ir.Ast.ThisRef;
import cd.ir.Ast.UnaryOp;
import cd.ir.Ast.UnaryOp.UOp;
import cd.ir.Ast.Var;
import cd.ir.Ast.VarDecl;
import cd.ir.Ast.WhileLoop;
import cd.util.Pair;

/** CUP v0.11a beta 20060608 generated parser.
  * @version Thu Sep 19 16:38:24 CEST 2013
  */
public class parser extends java_cup.runtime.lr_parser {

  
  public parser() {super();}

  
  public parser(java_cup.runtime.Scanner s) {super(s);}

  
  public parser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\160\000\002\002\004\000\002\002\003\000\002\002" +
    "\004\000\002\003\010\000\002\004\002\000\002\004\004" +
    "\000\002\005\002\000\002\005\003\000\002\006\003\000" +
    "\002\006\004\000\002\007\003\000\002\007\003\000\002" +
    "\010\005\000\002\011\003\000\002\011\005\000\002\012" +
    "\012\000\002\012\013\000\002\013\002\000\002\013\003" +
    "\000\002\014\003\000\002\014\005\000\002\015\004\000" +
    "\002\016\002\000\002\016\003\000\002\017\003\000\002" +
    "\017\004\000\002\020\004\000\002\020\004\000\002\020" +
    "\003\000\002\020\003\000\002\021\005\000\002\022\005" +
    "\000\002\022\005\000\002\022\005\000\002\061\005\000" +
    "\002\060\006\000\002\060\007\000\002\060\007\000\002" +
    "\023\006\000\002\023\006\000\002\023\006\000\002\023" +
    "\005\000\002\024\002\000\002\024\003\000\002\025\003" +
    "\000\002\025\005\000\002\026\010\000\002\027\002\000" +
    "\002\027\004\000\002\030\007\000\002\032\003\000\002" +
    "\032\005\000\002\031\003\000\002\031\003\000\002\033" +
    "\003\000\002\033\003\000\002\033\003\000\002\033\003" +
    "\000\002\033\003\000\002\033\003\000\002\035\003\000" +
    "\002\035\005\000\002\034\003\000\002\034\003\000\002" +
    "\036\003\000\002\036\003\000\002\036\003\000\002\040" +
    "\003\000\002\040\005\000\002\037\003\000\002\037\003" +
    "\000\002\041\003\000\002\041\003\000\002\041\003\000" +
    "\002\041\003\000\002\043\004\000\002\043\003\000\002" +
    "\042\003\000\002\042\003\000\002\044\003\000\002\044" +
    "\003\000\002\046\003\000\002\046\003\000\002\046\003" +
    "\000\002\046\003\000\002\046\003\000\002\046\005\000" +
    "\002\046\005\000\002\046\004\000\002\046\006\000\002" +
    "\046\006\000\002\045\003\000\002\045\003\000\002\047" +
    "\003\000\002\047\003\000\002\047\003\000\002\050\003" +
    "\000\002\050\003\000\002\051\004\000\002\051\004\000" +
    "\002\052\004\000\002\052\004\000\002\052\004\000\002" +
    "\053\004\000\002\054\005\000\002\055\003\000\002\055" +
    "\003\000\002\055\003\000\002\056\003\000\002\056\003" +
    "\000\002\057\005\000\002\057\005" });

  
  public short[][] production_table() {return _production_table;}

  
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\271\000\004\035\006\001\002\000\006\002\000\035" +
    "\000\001\002\000\006\002\273\035\006\001\002\000\004" +
    "\052\007\001\002\000\006\014\ufffd\037\011\001\002\000" +
    "\004\014\013\001\002\000\004\052\012\001\002\000\004" +
    "\014\ufffc\001\002\000\014\015\ufffb\034\017\041\016\045" +
    "\021\052\025\001\002\000\004\052\265\001\002\000\026" +
    "\015\ufff9\034\ufff9\040\ufff9\041\ufff9\044\ufff9\045\ufff9\046" +
    "\ufff9\050\ufff9\051\ufff9\052\ufff9\001\002\000\006\016\uff95" +
    "\052\uff95\001\002\000\006\016\uff94\052\uff94\001\002\000" +
    "\026\015\ufff6\034\ufff6\040\ufff6\041\ufff6\044\ufff6\045\ufff6" +
    "\046\ufff6\050\ufff6\051\ufff6\052\ufff6\001\002\000\004\052" +
    "\036\001\002\000\014\015\ufffa\034\017\041\016\045\021" +
    "\052\025\001\002\000\006\016\033\052\uff98\001\002\000" +
    "\004\015\032\001\002\000\006\016\030\052\uff97\001\002" +
    "\000\004\052\uff96\001\002\000\026\015\ufff7\034\ufff7\040" +
    "\ufff7\041\ufff7\044\ufff7\045\ufff7\046\ufff7\050\ufff7\051\ufff7" +
    "\052\ufff7\001\002\000\004\017\031\001\002\000\006\021" +
    "\uff93\052\uff93\001\002\000\006\002\ufffe\035\ufffe\001\002" +
    "\000\004\017\034\001\002\000\006\021\uff92\052\uff92\001" +
    "\002\000\026\015\ufff8\034\ufff8\040\ufff8\041\ufff8\044\ufff8" +
    "\045\ufff8\046\ufff8\050\ufff8\051\ufff8\052\ufff8\001\002\000" +
    "\004\020\037\001\002\000\012\021\ufff0\034\017\041\016" +
    "\052\025\001\002\000\004\052\264\001\002\000\006\021" +
    "\uffef\023\262\001\002\000\004\021\044\001\002\000\006" +
    "\021\uffee\023\uffee\001\002\000\004\014\045\001\002\000" +
    "\026\015\uffeb\034\017\040\054\041\016\044\056\045\021" +
    "\046\050\050\055\051\051\052\052\001\002\000\004\024" +
    "\261\001\002\000\020\015\uffe9\040\uffe9\044\uffe9\046\uffe9" +
    "\050\uffe9\051\uffe9\052\uffe9\001\002\000\004\020\255\001" +
    "\002\000\004\020\253\001\002\000\014\016\154\020\074" +
    "\022\076\033\uffa4\052\uff97\001\002\000\012\016\uffa0\020" +
    "\250\022\uffa0\033\uffa0\001\002\000\004\020\236\001\002" +
    "\000\004\020\233\001\002\000\006\022\076\033\uffa3\001" +
    "\002\000\010\016\073\022\076\033\uffa2\001\002\000\004" +
    "\015\232\001\002\000\052\004\uffa1\005\uffa1\006\uffa1\007" +
    "\uffa1\010\uffa1\011\uffa1\012\uffa1\013\uffa1\016\uffa1\017\uffa1" +
    "\021\uffa1\022\uffa1\023\uffa1\024\uffa1\025\uffa1\026\uffa1\027" +
    "\uffa1\030\uffa1\031\uffa1\033\uffa1\001\002\000\020\015\uffe4" +
    "\040\uffe4\044\uffe4\046\uffe4\050\uffe4\051\uffe4\052\uffe4\001" +
    "\002\000\004\024\231\001\002\000\026\015\uffeb\034\017" +
    "\040\054\041\016\044\056\045\021\046\050\050\055\051" +
    "\051\052\052\001\002\000\020\015\uffe5\040\uffe5\044\uffe5" +
    "\046\uffe5\050\uffe5\051\uffe5\052\uffe5\001\002\000\004\033" +
    "\205\001\002\000\020\015\uffea\040\054\044\056\046\050" +
    "\050\055\051\051\052\071\001\002\000\020\015\uffe8\040" +
    "\uffe8\044\uffe8\046\uffe8\050\uffe8\051\uffe8\052\uffe8\001\002" +
    "\000\012\016\073\020\074\022\076\033\uffa4\001\002\000" +
    "\052\004\uff9f\005\uff9f\006\uff9f\007\uff9f\010\uff9f\011\uff9f" +
    "\012\uff9f\013\uff9f\016\uff9f\017\uff9f\021\uff9f\022\uff9f\023" +
    "\uff9f\024\uff9f\025\uff9f\026\uff9f\027\uff9f\030\uff9f\031\uff9f" +
    "\033\uff9f\001\002\000\024\020\105\025\103\026\117\032" +
    "\121\043\123\044\113\052\161\053\116\054\124\001\002" +
    "\000\026\020\105\021\uffd7\025\103\026\117\032\121\043" +
    "\123\044\113\052\110\053\116\054\124\001\002\000\054" +
    "\004\uff9d\005\uff9d\006\uff9d\007\uff9d\010\uff9d\011\uff9d\012" +
    "\uff9d\013\uff9d\016\uff9d\017\uff9d\020\uff9d\021\uff9d\022\uff9d" +
    "\023\uff9d\024\uff9d\025\uff9d\026\uff9d\027\uff9d\030\uff9d\031" +
    "\uff9d\033\uff9d\001\002\000\004\052\077\001\002\000\054" +
    "\004\uff9a\005\uff9a\006\uff9a\007\uff9a\010\uff9a\011\uff9a\012" +
    "\uff9a\013\uff9a\016\uff9a\017\uff9a\020\uff9a\021\uff9a\022\uff9a" +
    "\023\uff9a\024\uff9a\025\uff9a\026\uff9a\027\uff9a\030\uff9a\031" +
    "\uff9a\033\uff9a\001\002\000\004\021\204\001\002\000\010" +
    "\021\uffcd\023\uffcd\024\uffcd\001\002\000\024\004\177\005" +
    "\202\006\174\007\200\010\176\011\175\013\166\025\164" +
    "\026\165\001\002\000\020\020\uffb2\032\uffb2\043\uffb2\044" +
    "\uffb2\052\uffb2\053\uffb2\054\uffb2\001\002\000\020\020\105" +
    "\032\121\043\123\044\113\052\153\053\116\054\124\001" +
    "\002\000\030\020\105\025\103\026\117\032\121\034\017" +
    "\041\016\043\123\044\113\052\145\053\116\054\124\001" +
    "\002\000\032\004\uffc3\005\uffc3\006\uffc3\007\uffc3\010\uffc3" +
    "\011\uffc3\013\uffc3\021\uffcf\023\uffcf\024\uffcf\025\uffc3\026" +
    "\uffc3\001\002\000\006\021\uffd6\023\142\001\002\000\046" +
    "\004\uffc2\005\uffc2\006\uffc2\007\uffc2\010\uffc2\011\uffc2\012" +
    "\uffbb\013\uffc2\016\073\021\uffcc\022\076\023\uffcc\024\uffcc" +
    "\025\uffc2\026\uffc2\027\uffbb\030\uffbb\031\uffbb\001\002\000" +
    "\050\004\uffa0\005\uffa0\006\uffa0\007\uffa0\010\uffa0\011\uffa0" +
    "\012\uffa0\013\uffa0\016\uffa0\017\uffa0\021\uffa0\022\uffa0\023" +
    "\uffa0\024\uffa0\025\uffa0\026\uffa0\027\uffa0\030\uffa0\031\uffa0" +
    "\001\002\000\006\021\uffd5\023\uffd5\001\002\000\046\004" +
    "\uffaf\005\uffaf\006\uffaf\007\uffaf\010\uffaf\011\uffaf\012\uffaf" +
    "\013\uffaf\017\uffaf\021\uffaf\022\076\023\uffaf\024\uffaf\025" +
    "\uffaf\026\uffaf\027\uffaf\030\uffaf\031\uffaf\001\002\000\044" +
    "\004\uffc5\005\uffc5\006\uffc5\007\uffc5\010\uffc5\011\uffc5\012" +
    "\uffbc\013\uffc5\017\uffc5\021\uffc5\023\uffc5\024\uffc5\025\uffc5" +
    "\026\uffc5\027\uffbc\030\uffbc\031\uffbc\001\002\000\050\004" +
    "\uffb0\005\uffb0\006\uffb0\007\uffb0\010\uffb0\011\uffb0\012\uffb0" +
    "\013\uffb0\016\073\017\uffb0\021\uffb0\022\076\023\uffb0\024" +
    "\uffb0\025\uffb0\026\uffb0\027\uffb0\030\uffb0\031\uffb0\001\002" +
    "\000\044\004\uffae\005\uffae\006\uffae\007\uffae\010\uffae\011" +
    "\uffae\012\uffae\013\uffae\017\uffae\021\uffae\023\uffae\024\uffae" +
    "\025\uffae\026\uffae\027\uffae\030\uffae\031\uffae\001\002\000" +
    "\020\020\uffb1\032\uffb1\043\uffb1\044\uffb1\052\uffb1\053\uffb1" +
    "\054\uffb1\001\002\000\044\004\uffb5\005\uffb5\006\uffb5\007" +
    "\uffb5\010\uffb5\011\uffb5\012\uffb5\013\uffb5\017\uffb5\021\uffb5" +
    "\023\uffb5\024\uffb5\025\uffb5\026\uffb5\027\uffb5\030\uffb5\031" +
    "\uffb5\001\002\000\024\020\105\025\103\026\117\032\121" +
    "\043\123\044\113\052\135\053\116\054\124\001\002\000" +
    "\012\012\131\027\127\030\130\031\126\001\002\000\044" +
    "\004\uffac\005\uffac\006\uffac\007\uffac\010\uffac\011\uffac\012" +
    "\uffac\013\uffac\017\uffac\021\uffac\023\uffac\024\uffac\025\uffac" +
    "\026\uffac\027\uffac\030\uffac\031\uffac\001\002\000\044\004" +
    "\uffad\005\uffad\006\uffad\007\uffad\010\uffad\011\uffad\012\uffad" +
    "\013\uffad\017\uffad\021\uffad\023\uffad\024\uffad\025\uffad\026" +
    "\uffad\027\uffad\030\uffad\031\uffad\001\002\000\044\004\uffbe" +
    "\005\uffbe\006\uffbe\007\uffbe\010\uffbe\011\uffbe\012\uffbe\013" +
    "\uffbe\017\uffbe\021\uffbe\023\uffbe\024\uffbe\025\uffbe\026\uffbe" +
    "\027\uffbe\030\uffbe\031\uffbe\001\002\000\024\020\uffb8\025" +
    "\uffb8\026\uffb8\032\uffb8\043\uffb8\044\uffb8\052\uffb8\053\uffb8" +
    "\054\uffb8\001\002\000\024\020\uffb9\025\uffb9\026\uffb9\032" +
    "\uffb9\043\uffb9\044\uffb9\052\uffb9\053\uffb9\054\uffb9\001\002" +
    "\000\024\020\uffba\025\uffba\026\uffba\032\uffba\043\uffba\044" +
    "\uffba\052\uffba\053\uffba\054\uffba\001\002\000\024\020\uffb7" +
    "\025\uffb7\026\uffb7\032\uffb7\043\uffb7\044\uffb7\052\uffb7\053" +
    "\uffb7\054\uffb7\001\002\000\024\020\105\025\103\026\117" +
    "\032\121\043\123\044\113\052\135\053\116\054\124\001" +
    "\002\000\044\004\uffb4\005\uffb4\006\uffb4\007\uffb4\010\uffb4" +
    "\011\uffb4\012\uffb4\013\uffb4\017\uffb4\021\uffb4\023\uffb4\024" +
    "\uffb4\025\uffb4\026\uffb4\027\uffb4\030\uffb4\031\uffb4\001\002" +
    "\000\044\004\uffbd\005\uffbd\006\uffbd\007\uffbd\010\uffbd\011" +
    "\uffbd\012\uffbd\013\uffbd\017\uffbd\021\uffbd\023\uffbd\024\uffbd" +
    "\025\uffbd\026\uffbd\027\uffbd\030\uffbd\031\uffbd\001\002\000" +
    "\050\004\uffb3\005\uffb3\006\uffb3\007\uffb3\010\uffb3\011\uffb3" +
    "\012\uffb3\013\uffb3\016\073\017\uffb3\021\uffb3\022\076\023" +
    "\uffb3\024\uffb3\025\uffb3\026\uffb3\027\uffb3\030\uffb3\031\uffb3" +
    "\001\002\000\044\004\uffa9\005\uffa9\006\uffa9\007\uffa9\010" +
    "\uffa9\011\uffa9\012\uffa9\013\uffa9\017\uffa9\021\uffa9\023\uffa9" +
    "\024\uffa9\025\uffa9\026\uffa9\027\uffa9\030\uffa9\031\uffa9\001" +
    "\002\000\052\004\uff9e\005\uff9e\006\uff9e\007\uff9e\010\uff9e" +
    "\011\uff9e\012\uff9e\013\uff9e\016\uff9e\017\uff9e\021\uff9e\022" +
    "\uff9e\023\uff9e\024\uff9e\025\uff9e\026\uff9e\027\uff9e\030\uff9e" +
    "\031\uff9e\033\uff9e\001\002\000\054\004\uff9b\005\uff9b\006" +
    "\uff9b\007\uff9b\010\uff9b\011\uff9b\012\uff9b\013\uff9b\016\uff9b" +
    "\017\uff9b\020\uff9b\021\uff9b\022\uff9b\023\uff9b\024\uff9b\025" +
    "\uff9b\026\uff9b\027\uff9b\030\uff9b\031\uff9b\033\uff9b\001\002" +
    "\000\054\004\uff9c\005\uff9c\006\uff9c\007\uff9c\010\uff9c\011" +
    "\uff9c\012\uff9c\013\uff9c\016\uff9c\017\uff9c\020\uff9c\021\uff9c" +
    "\022\uff9c\023\uff9c\024\uff9c\025\uff9c\026\uff9c\027\uff9c\030" +
    "\uff9c\031\uff9c\033\uff9c\001\002\000\024\020\105\025\103" +
    "\026\117\032\121\043\123\044\113\052\110\053\116\054" +
    "\124\001\002\000\006\021\uffd4\023\uffd4\001\002\000\004" +
    "\021\172\001\002\000\042\004\uffc2\005\uffc2\006\uffc2\007" +
    "\uffc2\010\uffc2\011\uffc2\012\uffbb\013\uffc2\016\154\021\155" +
    "\022\076\025\uffc2\026\uffc2\027\uffbb\030\uffbb\031\uffbb\001" +
    "\002\000\004\016\033\001\002\000\004\021\150\001\002" +
    "\000\020\020\105\032\121\043\123\044\113\052\153\053" +
    "\116\054\124\001\002\000\044\004\uffa6\005\uffa6\006\uffa6" +
    "\007\uffa6\010\uffa6\011\uffa6\012\uffa6\013\uffa6\017\uffa6\021" +
    "\uffa6\023\uffa6\024\uffa6\025\uffa6\026\uffa6\027\uffa6\030\uffa6" +
    "\031\uffa6\001\002\000\044\004\uffa7\005\uffa7\006\uffa7\007" +
    "\uffa7\010\uffa7\011\uffa7\012\uffa7\013\uffa7\017\uffa7\021\uffa7" +
    "\023\uffa7\024\uffa7\025\uffa7\026\uffa7\027\uffa7\030\uffa7\031" +
    "\uffa7\001\002\000\050\004\uffa5\005\uffa5\006\uffa5\007\uffa5" +
    "\010\uffa5\011\uffa5\012\uffa5\013\uffa5\016\073\017\uffa5\021" +
    "\uffa5\022\076\023\uffa5\024\uffa5\025\uffa5\026\uffa5\027\uffa5" +
    "\030\uffa5\031\uffa5\001\002\000\026\017\031\020\105\025" +
    "\103\026\117\032\121\043\123\044\113\052\161\053\116" +
    "\054\124\001\002\000\062\004\uffab\005\uffab\006\uffab\007" +
    "\uffab\010\uffab\011\uffab\012\uffab\013\uffab\017\uffab\020\105" +
    "\021\uffab\023\uffab\024\uffab\025\uffab\026\uffab\027\uffab\030" +
    "\uffab\031\uffab\032\121\043\123\044\113\052\153\053\116" +
    "\054\124\001\002\000\044\004\uffa8\005\uffa8\006\uffa8\007" +
    "\uffa8\010\uffa8\011\uffa8\012\uffa8\013\uffa8\017\uffa8\021\uffa8" +
    "\023\uffa8\024\uffa8\025\uffa8\026\uffa8\027\uffa8\030\uffa8\031" +
    "\uffa8\001\002\000\012\013\166\017\163\025\164\026\165" +
    "\001\002\000\020\013\uffc3\017\uffc3\021\uffc3\023\uffc3\024" +
    "\uffc3\025\uffc3\026\uffc3\001\002\000\034\012\uffbb\013\uffc2" +
    "\016\073\017\uffc2\021\uffc2\022\076\023\uffc2\024\uffc2\025" +
    "\uffc2\026\uffc2\027\uffbb\030\uffbb\031\uffbb\001\002\000\024" +
    "\020\105\025\103\026\117\032\121\043\123\044\113\052" +
    "\171\053\116\054\124\001\002\000\052\004\uff99\005\uff99" +
    "\006\uff99\007\uff99\010\uff99\011\uff99\012\uff99\013\uff99\016" +
    "\uff99\017\uff99\021\uff99\022\uff99\023\uff99\024\uff99\025\uff99" +
    "\026\uff99\027\uff99\030\uff99\031\uff99\033\uff99\001\002\000" +
    "\024\020\uffc1\025\uffc1\026\uffc1\032\uffc1\043\uffc1\044\uffc1" +
    "\052\uffc1\053\uffc1\054\uffc1\001\002\000\024\020\uffc0\025" +
    "\uffc0\026\uffc0\032\uffc0\043\uffc0\044\uffc0\052\uffc0\053\uffc0" +
    "\054\uffc0\001\002\000\024\020\uffbf\025\uffbf\026\uffbf\032" +
    "\uffbf\043\uffbf\044\uffbf\052\uffbf\053\uffbf\054\uffbf\001\002" +
    "\000\044\004\uffbc\005\uffbc\006\uffbc\007\uffbc\010\uffbc\011" +
    "\uffbc\012\uffbc\013\uffbc\017\uffbc\021\uffbc\023\uffbc\024\uffbc" +
    "\025\uffbc\026\uffbc\027\uffbc\030\uffbc\031\uffbc\001\002\000" +
    "\044\004\uffc4\005\uffc4\006\uffc4\007\uffc4\010\uffc4\011\uffc4" +
    "\012\131\013\uffc4\017\uffc4\021\uffc4\023\uffc4\024\uffc4\025" +
    "\uffc4\026\uffc4\027\127\030\130\031\126\001\002\000\050" +
    "\004\uffbb\005\uffbb\006\uffbb\007\uffbb\010\uffbb\011\uffbb\012" +
    "\uffbb\013\uffbb\016\073\017\uffbb\021\uffbb\022\076\023\uffbb" +
    "\024\uffbb\025\uffbb\026\uffbb\027\uffbb\030\uffbb\031\uffbb\001" +
    "\002\000\044\004\uffaa\005\uffaa\006\uffaa\007\uffaa\010\uffaa" +
    "\011\uffaa\012\uffaa\013\uffaa\017\uffaa\021\uffaa\023\uffaa\024" +
    "\uffaa\025\uffaa\026\uffaa\027\uffaa\030\uffaa\031\uffaa\001\002" +
    "\000\044\004\uffb6\005\uffb6\006\uffb6\007\uffb6\010\uffb6\011" +
    "\uffb6\012\uffb6\013\uffb6\017\uffb6\021\uffb6\023\uffb6\024\uffb6" +
    "\025\uffb6\026\uffb6\027\uffb6\030\uffb6\031\uffb6\001\002\000" +
    "\024\020\uffc8\025\uffc8\026\uffc8\032\uffc8\043\uffc8\044\uffc8" +
    "\052\uffc8\053\uffc8\054\uffc8\001\002\000\024\020\uffc7\025" +
    "\uffc7\026\uffc7\032\uffc7\043\uffc7\044\uffc7\052\uffc7\053\uffc7" +
    "\054\uffc7\001\002\000\024\020\uffc6\025\uffc6\026\uffc6\032" +
    "\uffc6\043\uffc6\044\uffc6\052\uffc6\053\uffc6\054\uffc6\001\002" +
    "\000\024\020\uffcb\025\uffcb\026\uffcb\032\uffcb\043\uffcb\044" +
    "\uffcb\052\uffcb\053\uffcb\054\uffcb\001\002\000\024\020\uffc9" +
    "\025\uffc9\026\uffc9\032\uffc9\043\uffc9\044\uffc9\052\uffc9\053" +
    "\uffc9\054\uffc9\001\002\000\024\020\105\025\103\026\117" +
    "\032\121\043\123\044\113\052\161\053\116\054\124\001" +
    "\002\000\024\020\uffca\025\uffca\026\uffca\032\uffca\043\uffca" +
    "\044\uffca\052\uffca\053\uffca\054\uffca\001\002\000\016\013" +
    "\166\021\uffce\023\uffce\024\uffce\025\164\026\165\001\002" +
    "\000\004\024\uffda\001\002\000\030\020\105\025\103\026" +
    "\117\032\121\042\212\043\123\044\113\047\206\052\110" +
    "\053\116\054\124\001\002\000\004\020\225\001\002\000" +
    "\004\024\uffe1\001\002\000\004\024\uffe0\001\002\000\004" +
    "\024\uffe2\001\002\000\010\034\017\041\016\052\213\001" +
    "\002\000\006\016\220\020\221\001\002\000\004\016\215" +
    "\001\002\000\024\020\105\025\103\026\117\032\121\043" +
    "\123\044\113\052\161\053\116\054\124\001\002\000\012" +
    "\013\166\017\217\025\164\026\165\001\002\000\004\024" +
    "\uffdc\001\002\000\024\020\105\025\103\026\117\032\121" +
    "\043\123\044\113\052\161\053\116\054\124\001\002\000" +
    "\004\021\222\001\002\000\004\024\uffde\001\002\000\012" +
    "\013\166\017\224\025\164\026\165\001\002\000\004\024" +
    "\uffdd\001\002\000\004\021\226\001\002\000\004\024\uffdf" +
    "\001\002\000\004\015\230\001\002\000\026\015\ufff1\034" +
    "\ufff1\040\ufff1\041\ufff1\044\ufff1\045\ufff1\046\ufff1\050\ufff1" +
    "\051\ufff1\052\ufff1\001\002\000\020\015\uffe6\040\uffe6\044" +
    "\uffe6\046\uffe6\050\uffe6\051\uffe6\052\uffe6\001\002\000\026" +
    "\015\ufff2\034\ufff2\040\ufff2\041\ufff2\044\ufff2\045\ufff2\046" +
    "\ufff2\050\ufff2\051\ufff2\052\ufff2\001\002\000\024\020\105" +
    "\025\103\026\117\032\121\043\123\044\113\052\110\053" +
    "\116\054\124\001\002\000\004\021\235\001\002\000\004" +
    "\024\uffd9\001\002\000\024\020\105\025\103\026\117\032" +
    "\121\043\123\044\113\052\110\053\116\054\124\001\002" +
    "\000\004\021\240\001\002\000\004\014\241\001\002\000" +
    "\020\015\uffeb\040\054\044\056\046\050\050\055\051\051" +
    "\052\071\001\002\000\022\015\uffd2\036\243\040\uffd2\044" +
    "\uffd2\046\uffd2\050\uffd2\051\uffd2\052\uffd2\001\002\000\004" +
    "\014\241\001\002\000\020\015\uffd3\040\uffd3\044\uffd3\046" +
    "\uffd3\050\uffd3\051\uffd3\052\uffd3\001\002\000\020\015\uffd1" +
    "\040\uffd1\044\uffd1\046\uffd1\050\uffd1\051\uffd1\052\uffd1\001" +
    "\002\000\004\015\247\001\002\000\022\015\uffe3\036\uffe3" +
    "\040\uffe3\044\uffe3\046\uffe3\050\uffe3\051\uffe3\052\uffe3\001" +
    "\002\000\026\020\105\021\uffd7\025\103\026\117\032\121" +
    "\043\123\044\113\052\110\053\116\054\124\001\002\000" +
    "\004\021\252\001\002\000\004\024\uffdb\001\002\000\004" +
    "\021\254\001\002\000\004\024\uffd8\001\002\000\024\020" +
    "\105\025\103\026\117\032\121\043\123\044\113\052\110" +
    "\053\116\054\124\001\002\000\004\021\257\001\002\000" +
    "\004\014\241\001\002\000\020\015\uffd0\040\uffd0\044\uffd0" +
    "\046\uffd0\050\uffd0\051\uffd0\052\uffd0\001\002\000\020\015" +
    "\uffe7\040\uffe7\044\uffe7\046\uffe7\050\uffe7\051\uffe7\052\uffe7" +
    "\001\002\000\010\034\017\041\016\052\025\001\002\000" +
    "\006\021\uffed\023\uffed\001\002\000\006\021\uffec\023\uffec" +
    "\001\002\000\006\023\ufff4\024\ufff4\001\002\000\006\023" +
    "\267\024\270\001\002\000\004\052\271\001\002\000\026" +
    "\015\ufff5\034\ufff5\040\ufff5\041\ufff5\044\ufff5\045\ufff5\046" +
    "\ufff5\050\ufff5\051\ufff5\052\ufff5\001\002\000\006\023\ufff3" +
    "\024\ufff3\001\002\000\006\002\uffff\035\uffff\001\002\000" +
    "\004\002\001\001\002" });

  
  public short[][] action_table() {return _action_table;}

  
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\271\000\006\002\004\003\003\001\001\000\002\001" +
    "\001\000\004\003\271\001\001\000\002\001\001\000\004" +
    "\004\007\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\022\005\023\006\021\007\014\010\026" +
    "\012\017\055\013\056\022\057\025\001\001\000\004\011" +
    "\265\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\016\007" +
    "\034\010\026\012\017\055\013\056\022\057\025\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\016\013\041" +
    "\014\040\015\042\055\037\056\022\057\025\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\046\006\063\007\014\010" +
    "\026\012\017\016\057\017\066\020\046\022\045\023\062" +
    "\026\064\030\061\047\065\050\056\051\060\052\052\055" +
    "\013\056\022\057\025\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\006\053" +
    "\074\054\071\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\004\053\140\001\001\000\006\053" +
    "\137\054\136\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\044\007\034\010" +
    "\026\012\017\016\226\017\066\020\046\022\045\023\062" +
    "\026\064\030\061\047\065\050\056\051\060\052\052\055" +
    "\013\056\022\057\025\001\001\000\002\001\001\000\002" +
    "\001\001\000\024\020\067\022\045\023\062\026\064\030" +
    "\061\047\065\050\056\051\060\052\052\001\001\000\002" +
    "\001\001\000\006\053\074\054\071\001\001\000\002\001" +
    "\001\000\026\034\156\035\157\037\121\040\113\043\124" +
    "\044\103\046\117\050\114\051\060\052\110\001\001\000" +
    "\036\024\077\025\106\031\111\032\100\034\101\035\105" +
    "\037\121\040\113\043\124\044\103\046\117\050\114\051" +
    "\060\052\110\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\006\033\200\036\161\001\001\000\002\001\001\000\014" +
    "\045\172\046\150\050\114\051\060\052\110\001\001\000" +
    "\034\032\143\034\101\035\105\037\121\040\113\043\124" +
    "\044\103\046\117\050\114\051\060\052\110\056\145\057" +
    "\146\001\001\000\002\001\001\000\002\001\001\000\006" +
    "\053\074\054\071\001\001\000\002\001\001\000\002\001" +
    "\001\000\004\053\140\001\001\000\002\001\001\000\006" +
    "\053\137\054\136\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\020\042\135\043\132\044\103" +
    "\046\117\050\114\051\060\052\110\001\001\000\004\041" +
    "\131\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\020\042\133\043\132\044\103" +
    "\046\117\050\114\051\060\052\110\001\001\000\002\001" +
    "\001\000\002\001\001\000\006\053\074\054\071\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\032\031\142\032\100\034\101\035\105" +
    "\037\121\040\113\043\124\044\103\046\117\050\114\051" +
    "\060\052\110\001\001\000\002\001\001\000\002\001\001" +
    "\000\006\053\074\054\071\001\001\000\002\001\001\000" +
    "\002\001\001\000\014\045\151\046\150\050\114\051\060" +
    "\052\110\001\001\000\002\001\001\000\002\001\001\000" +
    "\006\053\074\054\071\001\001\000\026\034\156\035\157" +
    "\037\121\040\113\043\124\044\103\046\117\050\114\051" +
    "\060\052\110\001\001\000\014\045\155\046\150\050\114" +
    "\051\060\052\110\001\001\000\002\001\001\000\004\036" +
    "\161\001\001\000\002\001\001\000\006\053\074\054\071" +
    "\001\001\000\022\037\167\040\166\043\124\044\103\046" +
    "\117\050\114\051\060\052\110\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\004\041\131\001\001\000\006\053\074" +
    "\054\071\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\026\034\202\035\157\037" +
    "\121\040\113\043\124\044\103\046\117\050\114\051\060" +
    "\052\110\001\001\000\002\001\001\000\004\036\161\001" +
    "\001\000\002\001\001\000\036\031\210\032\100\034\101" +
    "\035\105\037\121\040\113\043\124\044\103\046\117\050" +
    "\114\051\060\052\110\060\206\061\207\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\004\056\213\001\001\000\002\001\001\000\002" +
    "\001\001\000\026\034\215\035\157\037\121\040\113\043" +
    "\124\044\103\046\117\050\114\051\060\052\110\001\001" +
    "\000\004\036\161\001\001\000\002\001\001\000\026\034" +
    "\222\035\157\037\121\040\113\043\124\044\103\046\117" +
    "\050\114\051\060\052\110\001\001\000\002\001\001\000" +
    "\002\001\001\000\004\036\161\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\032" +
    "\031\233\032\100\034\101\035\105\037\121\040\113\043" +
    "\124\044\103\046\117\050\114\051\060\052\110\001\001" +
    "\000\002\001\001\000\002\001\001\000\032\031\236\032" +
    "\100\034\101\035\105\037\121\040\113\043\124\044\103" +
    "\046\117\050\114\051\060\052\110\001\001\000\002\001" +
    "\001\000\004\021\241\001\001\000\030\016\245\017\066" +
    "\020\046\022\045\023\062\026\064\030\061\047\065\050" +
    "\056\051\060\052\052\001\001\000\004\027\243\001\001" +
    "\000\004\021\244\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\036\024\250" +
    "\025\106\031\111\032\100\034\101\035\105\037\121\040" +
    "\113\043\124\044\103\046\117\050\114\051\060\052\110" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\032\031\255\032\100\034\101" +
    "\035\105\037\121\040\113\043\124\044\103\046\117\050" +
    "\114\051\060\052\110\001\001\000\002\001\001\000\004" +
    "\021\257\001\001\000\002\001\001\000\002\001\001\000" +
    "\012\015\262\055\037\056\022\057\025\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001" });

  
  public short[][] reduce_table() {return _reduce_table;}

  
  protected CUP$parser$actions action_obj;

  
  protected void init_actions()
    {
      action_obj = new CUP$parser$actions(this);
    }

  
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    
    return action_obj.CUP$parser$do_action(act_num, parser, stack, top);
  }

  
  public int start_state() {return 0;}
  
  public int start_production() {return 0;}

  
  public int EOF_sym() {return 0;}

  
  public int error_sym() {return 1;}


  
  public void user_init() throws java.lang.Exception
    {


    }



	
	
	
    public List<ClassDecl> output;
    
    
    public Main main;
    
    
    
    
    public void debug(String format, Object... args) {
		if (this.main != null)
		    this.main.debug(format, args);
	}
    
    
    
    
    public void debug_message(String message) {
        debug("%s", DebugFormatter.format(message, cur_token));
    }
    
    
    
    
    public void syntax_error(Symbol curr) {
        throw new ParseFailure(curr.left, "Unexpected token");
    }

}


class CUP$parser$actions {


	public <T> Pair<T> pair(T item1, T item2) {
	  return new Pair<T>(item1, item2);
	}
	
    public <T> List<T> list(T item) {
      ArrayList<T> res = new ArrayList<T>();
      res.add(item);
      return res;
    }

    public <T> List<T> list(List<? extends T> items) {
      return new ArrayList<T>(items);
    }

    public <T> List<T> add(List<T> list, T item) {
      list.add(item);
      return list;
    }

    public <T> List<T> add(List<T> list, List<? extends T> items) {
      list.addAll(items);
      return list;
    }
    
    public <T> List<T> emptyList() {
      return java.util.Collections.emptyList();
    }
    
   	public Index index(String varName, Expr expr) {
      return new Index(new Var(varName), expr);
    }
    
   	public Field field(String varName, String fieldName) {
      return new Field(new Var(varName), fieldName);
    }
    
    public Field field(String fieldName) {
      return new Field(new ThisRef(), fieldName);
    }
    
    public UnaryOp not(Expr arg) {
      return new Ast.UnaryOp(UOp.U_BOOL_NOT, arg);
    }
    
    public NewArray newArray(String t, Expr q) {
      return new NewArray(t+"[]", q);
    }
    
    public Seq seq(List<Ast>... members) {
      Seq res = new Seq(null);
      for (List<Ast> lst : members)
        res.rwChildren().addAll(lst);
      return res;
    }

  private final parser parser;

  
  CUP$parser$actions(parser parser) {
    this.parser = parser;
  }

  
  public final java_cup.runtime.Symbol CUP$parser$do_action(
    int                        CUP$parser$act_num,
    java_cup.runtime.lr_parser CUP$parser$parser,
    java.util.Stack            CUP$parser$stack,
    int                        CUP$parser$top)
    throws java.lang.Exception
    {
      
      java_cup.runtime.Symbol CUP$parser$result;

      
      switch (CUP$parser$act_num)
        {
          
          case 111: 
            {
              String RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		 RESULT = s + "[]"; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ArrayType",45, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 110: 
            {
              String RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		 RESULT = s + "[]"; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ArrayType",45, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 109: 
            {
              String RESULT =null;
		 RESULT = "boolean"; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("PrimitiveType",44, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 108: 
            {
              String RESULT =null;
		 RESULT = "int";     
              CUP$parser$result = parser.getSymbolFactory().newSymbol("PrimitiveType",44, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 107: 
            {
              String RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int tright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String t = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = t; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Type",43, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 106: 
            {
              String RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = s; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Type",43, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 105: 
            {
              String RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int tright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String t = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = t; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Type",43, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 104: 
            {
              Expr RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = e; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("BracketExpr",42, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 103: 
            {
              String RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = s; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("DotId",41, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 102: 
            {
              Field RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Expr i = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Field(i, e);     
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Field",40, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 101: 
            {
              Field RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = field(e);            
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Field",40, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 100: 
            {
              Field RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int nright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		String n = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = field(n, e);         
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Field",40, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 99: 
            {
              Expr RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Expr i = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Index(i, e);     
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Index",39, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 98: 
            {
              Expr RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int nright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		String n = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = index(n, e);         
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Index",39, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 97: 
            {
              Expr RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Field f = (Field)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = f;                   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("IndexOrField",38, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 96: 
            {
              Expr RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr i = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = i;                   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("IndexOrField",38, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 95: 
            {
              Expr RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr i = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = i;                   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("IdentAccess",37, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 94: 
            {
              Expr RESULT =null;
		 RESULT = new ThisRef();       
              CUP$parser$result = parser.getSymbolFactory().newSymbol("IdentAccess",37, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 93: 
            {
              Expr RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String n = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Var(n);          
              CUP$parser$result = parser.getSymbolFactory().newSymbol("IdentAccess",37, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 92: 
            {
              Expr RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Var(i);          
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactor",35, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 91: 
            {
              Expr RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr f = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = f;                   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactor",35, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 90: 
            {
              Expr RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		String t = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr f = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Cast(f, t);      
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactorMinusId",36, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 89: 
            {
              Expr RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		String t = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr f = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Cast(f, t);      
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactorMinusId",36, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 88: 
            {
              Expr RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr f = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = not(f);              
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactorMinusId",36, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 87: 
            {
              Expr RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = e;                   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactorMinusId",36, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 86: 
            {
              Expr RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = new Var(i);          
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactorMinusId",36, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 85: 
            {
              Expr RESULT =null;
		 RESULT = new NullConst();     
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactorMinusId",36, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 84: 
            {
              Expr RESULT =null;
		int bleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int bright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Boolean b = (Boolean)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new BooleanConst(b); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactorMinusId",36, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 83: 
            {
              Expr RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Integer i = (Integer)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new IntConst(i);     
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactorMinusId",36, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 82: 
            {
              Expr RESULT =null;
		 RESULT = new ThisRef();       
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactorMinusId",36, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 81: 
            {
              Expr RESULT =null;
		int vleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int vright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr v = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = v;                   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NoSignFactorMinusId",36, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 80: 
            {
              UnaryOp.UOp RESULT =null;
		 RESULT = UOp.U_MINUS; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Sign",34, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 79: 
            {
              UnaryOp.UOp RESULT =null;
		 RESULT = UOp.U_PLUS;  
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Sign",34, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 78: 
            {
              Expr RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Var(i);          
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Factor",32, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 77: 
            {
              Expr RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr f = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = f;                   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Factor",32, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 76: 
            {
              Expr RESULT =null;
		int nsfleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int nsfright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr nsf = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = nsf;               
              CUP$parser$result = parser.getSymbolFactory().newSymbol("FactorMinusId",33, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 75: 
            {
              Expr RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		UnaryOp.UOp s = (UnaryOp.UOp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr f = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new UnaryOp(s, f); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("FactorMinusId",33, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 74: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_AND;   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("StrongOp",31, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 73: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_MOD;   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("StrongOp",31, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 72: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_DIV;   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("StrongOp",31, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 71: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_TIMES; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("StrongOp",31, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 70: 
            {
              Expr RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Var(i);          
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Term",29, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 69: 
            {
              Expr RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr f = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = f;                   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Term",29, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 68: 
            {
              Expr RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Expr l = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int oleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int oright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		BinaryOp.BOp o = (BinaryOp.BOp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr r = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new BinaryOp(l,o,r); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("TermMinusId",30, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 67: 
            {
              Expr RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr f = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = f;                   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("TermMinusId",30, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 66: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_OR;    
              CUP$parser$result = parser.getSymbolFactory().newSymbol("WeakOp",28, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 65: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_MINUS; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("WeakOp",28, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 64: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_PLUS;  
              CUP$parser$result = parser.getSymbolFactory().newSymbol("WeakOp",28, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 63: 
            {
              Expr RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Var(i);             
              CUP$parser$result = parser.getSymbolFactory().newSymbol("SimpleExpr",26, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 62: 
            {
              Expr RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr f = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = f;                      
              CUP$parser$result = parser.getSymbolFactory().newSymbol("SimpleExpr",26, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 61: 
            {
              Expr RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Expr l = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int oleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int oright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		BinaryOp.BOp o = (BinaryOp.BOp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr r = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new BinaryOp(l,o,r);    
              CUP$parser$result = parser.getSymbolFactory().newSymbol("SimpleExprMinusId",27, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 60: 
            {
              Expr RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int tright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr t = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = t;                      
              CUP$parser$result = parser.getSymbolFactory().newSymbol("SimpleExprMinusId",27, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 59: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_GREATER_OR_EQUAL; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Comp",25, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 58: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_GREATER_THAN;     
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Comp",25, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 57: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_LESS_OR_EQUAL;    
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Comp",25, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 56: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_LESS_THAN;        
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Comp",25, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 55: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_NOT_EQUAL;        
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Comp",25, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 54: 
            {
              BinaryOp.BOp RESULT =null;
		 RESULT = BOp.B_EQUAL;            
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Comp",25, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 53: 
            {
              Expr RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Var(i);             
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Expr",23, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 52: 
            {
              Expr RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr f = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = f;                      
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Expr",23, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 51: 
            {
              Expr RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Expr l = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int oleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int oright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		BinaryOp.BOp o = (BinaryOp.BOp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr r = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new BinaryOp(l,o,r);    
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ExprMinusId",24, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 50: 
            {
              Expr RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = e;                      
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ExprMinusId",24, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 49: 
            {
              Ast RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Ast l = (Ast)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new WhileLoop(e, l);    
              CUP$parser$result = parser.getSymbolFactory().newSymbol("WhileStmt",22, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 48: 
            {
              Ast RESULT =null;
		int bleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int bright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Ast b = (Ast)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = b;                      
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ElsePartOpt",21, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 47: 
            {
              Ast RESULT =null;
		 RESULT = new Nop();              
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ElsePartOpt",21, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 46: 
            {
              Ast RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value;
		int l1left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int l1right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Ast l1 = (Ast)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int l2left = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int l2right = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Ast l2 = (Ast)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new IfElse(e, l1, l2); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("IfStmt",20, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-5)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 45: 
            {
              List<Expr> RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		List<Expr> l = (List<Expr>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = add(l, e); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ActualParamList",19, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 44: 
            {
              List<Expr> RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = list(e);   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ActualParamList",19, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 43: 
            {
              List<Expr> RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		List<Expr> l = (List<Expr>)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = l; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ActualParamListOpt",18, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 42: 
            {
              List<Expr> RESULT =null;
		 RESULT = new ArrayList<Expr>(); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ActualParamListOpt",18, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 41: 
            {
              Ast RESULT =null;
		 RESULT = new BuiltInWriteln(); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("MethodCall",17, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 40: 
            {
              Ast RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = new BuiltInWrite(e); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("MethodCall",17, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 39: 
            {
              Ast RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<Expr> l = (List<Expr>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = new MethodCall(new ThisRef(), i, l); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("MethodCall",17, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 38: 
            {
              Ast RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).right;
		Field f = (Field)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<Expr> l = (List<Expr>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = new MethodCall(f.arg(), f.fieldName, l); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("MethodCall",17, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 37: 
            {
              Expr RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).right;
		String t = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value;
		int qleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int qright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Expr q = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = newArray(t,q);      
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NewExpr",46, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 36: 
            {
              Expr RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).right;
		String t = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value;
		int qleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int qright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Expr q = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = newArray(t,q);      
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NewExpr",46, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 35: 
            {
              Expr RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		String t = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		 RESULT = new NewObject(t);   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("NewExpr",46, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 34: 
            {
              BuiltInRead RESULT =null;
		 RESULT = new BuiltInRead();   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ReadExpr",47, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 33: 
            {
              Ast RESULT =null;
		int vleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int vright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Expr v = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		BuiltInRead r = (BuiltInRead)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Assign(v, r);   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Assignment",16, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 32: 
            {
              Ast RESULT =null;
		int vleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int vright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Expr v = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int nleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr n = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Assign(v, n);   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Assignment",16, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 31: 
            {
              Ast RESULT =null;
		int vleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int vright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Expr v = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Expr e = (Expr)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new Assign(v, e);   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Assignment",16, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 30: 
            {
              Ast RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<Ast> l = (List<Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = new Seq(l);         
              CUP$parser$result = parser.getSymbolFactory().newSymbol("StmtBlock",15, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 29: 
            {
              Ast RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Ast s = (Ast)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = s; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Stmt",14, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 28: 
            {
              Ast RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Ast s = (Ast)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = s; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Stmt",14, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 27: 
            {
              Ast RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Ast s = (Ast)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = s; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Stmt",14, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 26: 
            {
              Ast RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Ast s = (Ast)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = s; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Stmt",14, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 25: 
            {
              List<Ast> RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<Ast> l = (List<Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Ast s = (Ast)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = add(l, s); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("StmtList",13, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 24: 
            {
              List<Ast> RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Ast s = (Ast)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = list(s);   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("StmtList",13, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 23: 
            {
              List<Ast> RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		List<Ast> l = (List<Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = l; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("StmtListOpt",12, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 22: 
            {
              List<Ast> RESULT =null;
		 RESULT = emptyList(); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("StmtListOpt",12, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 21: 
            {
              Pair<String> RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		String t = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = pair(t,s); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("FormalParam",11, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 20: 
            {
              List<Pair<String>> RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		List<Pair<String>> l = (List<Pair<String>>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int pleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int pright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Pair<String> p = (Pair<String>)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = add(l,p); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("FormalParamList",10, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 19: 
            {
              List<Pair<String>> RESULT =null;
		int pleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int pright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Pair<String> p = (Pair<String>)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = list(p);  
              CUP$parser$result = parser.getSymbolFactory().newSymbol("FormalParamList",10, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 18: 
            {
              List<Pair<String>> RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		List<Pair<String>> l = (List<Pair<String>>)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = l; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("FormalParamListOpt",9, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 17: 
            {
              List<Pair<String>> RESULT =null;
		 RESULT = emptyList(); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("FormalParamListOpt",9, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 16: 
            {
              Ast.MethodDecl RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-7)).left;
		int nright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-7)).right;
		String n = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-7)).value;
		int pleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-5)).left;
		int pright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-5)).right;
		List<Pair<String>> p = (List<Pair<String>>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-5)).value;
		int dleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int dright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		List<Ast> d = (List<Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<Ast> s = (List<Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = new MethodDecl("void", n, p, seq(d), seq(s)); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("MethodDecl",8, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 15: 
            {
              Ast.MethodDecl RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-6)).left;
		int nright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-6)).right;
		String n = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-6)).value;
		int pleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left;
		int pright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).right;
		List<Pair<String>> p = (List<Pair<String>>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-4)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<Ast> s = (List<Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = new MethodDecl("void", n, p, seq(), seq(s)); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("MethodDecl",8, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-7)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 14: 
            {
              List<String> RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		List<String> l = (List<String>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = add(l,s); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("IdentList",7, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 13: 
            {
              List<String> RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = list(s);  
              CUP$parser$result = parser.getSymbolFactory().newSymbol("IdentList",7, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 12: 
            {
              List<VarDecl> RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		String t = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<String> l = (List<String>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 
      RESULT = new ArrayList<VarDecl>();
      for (String s : l) RESULT.add(new VarDecl(t, s));
    
              CUP$parser$result = parser.getSymbolFactory().newSymbol("VarDecls",6, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 11: 
            {
              List< ? extends Ast> RESULT =null;
		int dleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int dright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Ast.MethodDecl d = (Ast.MethodDecl)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = list(d); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Decl",5, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 10: 
            {
              List< ? extends Ast> RESULT =null;
		int dleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int dright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		List<VarDecl> d = (List<VarDecl>)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = d;       
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Decl",5, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 9: 
            {
              List<Ast> RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<Ast> l = (List<Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int dleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int dright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		List< ? extends Ast> d = (List< ? extends Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = add(l,d);     
              CUP$parser$result = parser.getSymbolFactory().newSymbol("DeclList",4, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 8: 
            {
              List<Ast> RESULT =null;
		int dleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int dright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		List< ? extends Ast> d = (List< ? extends Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = list(d); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("DeclList",4, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 7: 
            {
              List<Ast> RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		List<Ast> l = (List<Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = l; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("DeclListOpt",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 6: 
            {
              List<Ast> RESULT =null;
		 parser.debug("emptylist"); RESULT = emptyList(); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("DeclListOpt",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 5: 
            {
              String RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = s;        
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ExtendsOpt",2, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 4: 
            {
              String RESULT =null;
		 RESULT = "Object"; 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ExtendsOpt",2, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 3: 
            {
              Ast.ClassDecl RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left;
		int nright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).right;
		String n = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-4)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value;
		int dleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int dright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<Ast> d = (List<Ast>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = new ClassDecl(n,s,d); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("ClassDecl",1, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-5)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 2: 
            {
              List<Ast.ClassDecl> RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<Ast.ClassDecl> l = (List<Ast.ClassDecl>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Ast.ClassDecl c = (Ast.ClassDecl)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 parser.output = add(parser.output, c);     
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Unit",0, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 1: 
            {
              List<Ast.ClassDecl> RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
		int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Ast.ClassDecl c = (Ast.ClassDecl)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 parser.output = list(c);                   
              CUP$parser$result = parser.getSymbolFactory().newSymbol("Unit",0, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          
          case 0: 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		List<Ast.ClassDecl> start_val = (List<Ast.ClassDecl>)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		RESULT = start_val;
              CUP$parser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          
          CUP$parser$parser.done_parsing();
          return CUP$parser$result;

          
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

