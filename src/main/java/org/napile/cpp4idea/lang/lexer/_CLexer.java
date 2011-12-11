/* The following code was generated by JFlex 1.4.3 on 11.12.11 18:25 */

/* It's an automatically generated code. Do not modify it. */
package org.napile.cpp4idea.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

@SuppressWarnings({"ALL"})

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 11.12.11 18:25 from the specification file
 * <tt>H:/napile/tools/plugins/idea/cpp4idea/src/main/java/org/napile/cpp4idea/lang/lexer/c.flex</tt>
 */
class _CLexer implements FlexLexer {
  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\4\1\1\1\2\1\0\1\1\1\7\16\4\4\0\1\1\1\65"+
    "\1\31\1\34\1\3\1\72\1\70\1\27\1\73\1\74\1\6\1\67"+
    "\1\102\1\25\1\23\1\5\1\15\1\14\2\12\1\63\1\12\1\62"+
    "\1\12\2\10\1\105\1\101\1\32\1\64\1\33\1\104\1\106\1\13"+
    "\1\17\1\13\1\22\1\24\1\21\5\3\1\20\3\3\1\26\7\3"+
    "\1\16\2\3\1\77\1\30\1\100\1\71\1\11\1\0\1\47\1\45"+
    "\1\37\1\42\1\43\1\44\1\54\1\57\1\35\1\3\1\50\1\40"+
    "\1\3\1\36\1\52\1\56\1\3\1\46\1\51\1\53\1\41\1\60"+
    "\1\55\1\16\1\61\1\3\1\75\1\66\1\76\1\103\41\4\2\0"+
    "\4\3\4\0\1\3\2\0\1\4\7\0\1\3\4\0\1\3\5\0"+
    "\27\3\1\0\37\3\1\0\u013f\3\31\0\162\3\4\0\14\3\16\0"+
    "\5\3\11\0\1\3\21\0\130\4\5\0\23\4\12\0\1\3\13\0"+
    "\1\3\1\0\3\3\1\0\1\3\1\0\24\3\1\0\54\3\1\0"+
    "\46\3\1\0\5\3\4\0\202\3\1\0\4\4\3\0\105\3\1\0"+
    "\46\3\2\0\2\3\6\0\20\3\41\0\46\3\2\0\1\3\7\0"+
    "\47\3\11\0\21\4\1\0\27\4\1\0\3\4\1\0\1\4\1\0"+
    "\2\4\1\0\1\4\13\0\33\3\5\0\3\3\15\0\4\4\14\0"+
    "\6\4\13\0\32\3\5\0\13\3\16\4\7\0\12\4\4\0\2\3"+
    "\1\4\143\3\1\0\1\3\10\4\1\0\6\4\2\3\2\4\1\0"+
    "\4\4\2\3\12\4\3\3\2\0\1\3\17\0\1\4\1\3\1\4"+
    "\36\3\33\4\2\0\3\3\60\0\46\3\13\4\1\3\u014f\0\3\4"+
    "\66\3\2\0\1\4\1\3\20\4\2\0\1\3\4\4\3\0\12\3"+
    "\2\4\2\0\12\4\21\0\3\4\1\0\10\3\2\0\2\3\2\0"+
    "\26\3\1\0\7\3\1\0\1\3\3\0\4\3\2\0\1\4\1\3"+
    "\7\4\2\0\2\4\2\0\3\4\11\0\1\4\4\0\2\3\1\0"+
    "\3\3\2\4\2\0\12\4\4\3\15\0\3\4\1\0\6\3\4\0"+
    "\2\3\2\0\26\3\1\0\7\3\1\0\2\3\1\0\2\3\1\0"+
    "\2\3\2\0\1\4\1\0\5\4\4\0\2\4\2\0\3\4\13\0"+
    "\4\3\1\0\1\3\7\0\14\4\3\3\14\0\3\4\1\0\11\3"+
    "\1\0\3\3\1\0\26\3\1\0\7\3\1\0\2\3\1\0\5\3"+
    "\2\0\1\4\1\3\10\4\1\0\3\4\1\0\3\4\2\0\1\3"+
    "\17\0\2\3\2\4\2\0\12\4\1\0\1\3\17\0\3\4\1\0"+
    "\10\3\2\0\2\3\2\0\26\3\1\0\7\3\1\0\2\3\1\0"+
    "\5\3\2\0\1\4\1\3\6\4\3\0\2\4\2\0\3\4\10\0"+
    "\2\4\4\0\2\3\1\0\3\3\4\0\12\4\1\0\1\3\20\0"+
    "\1\4\1\3\1\0\6\3\3\0\3\3\1\0\4\3\3\0\2\3"+
    "\1\0\1\3\1\0\2\3\3\0\2\3\3\0\3\3\3\0\10\3"+
    "\1\0\3\3\4\0\5\4\3\0\3\4\1\0\4\4\11\0\1\4"+
    "\17\0\11\4\11\0\1\3\7\0\3\4\1\0\10\3\1\0\3\3"+
    "\1\0\27\3\1\0\12\3\1\0\5\3\4\0\7\4\1\0\3\4"+
    "\1\0\4\4\7\0\2\4\11\0\2\3\4\0\12\4\22\0\2\4"+
    "\1\0\10\3\1\0\3\3\1\0\27\3\1\0\12\3\1\0\5\3"+
    "\2\0\1\4\1\3\7\4\1\0\3\4\1\0\4\4\7\0\2\4"+
    "\7\0\1\3\1\0\2\3\4\0\12\4\22\0\2\4\1\0\10\3"+
    "\1\0\3\3\1\0\27\3\1\0\20\3\4\0\6\4\2\0\3\4"+
    "\1\0\4\4\11\0\1\4\10\0\2\3\4\0\12\4\22\0\2\4"+
    "\1\0\22\3\3\0\30\3\1\0\11\3\1\0\1\3\2\0\7\3"+
    "\3\0\1\4\4\0\6\4\1\0\1\4\1\0\10\4\22\0\2\4"+
    "\15\0\60\3\1\4\2\3\7\4\4\0\10\3\10\4\1\0\12\4"+
    "\47\0\2\3\1\0\1\3\2\0\2\3\1\0\1\3\2\0\1\3"+
    "\6\0\4\3\1\0\7\3\1\0\3\3\1\0\1\3\1\0\1\3"+
    "\2\0\2\3\1\0\4\3\1\4\2\3\6\4\1\0\2\4\1\3"+
    "\2\0\5\3\1\0\1\3\1\0\6\4\2\0\12\4\2\0\2\3"+
    "\42\0\1\3\27\0\2\4\6\0\12\4\13\0\1\4\1\0\1\4"+
    "\1\0\1\4\4\0\2\4\10\3\1\0\42\3\6\0\24\4\1\0"+
    "\2\4\4\3\4\0\10\4\1\0\44\4\11\0\1\4\71\0\42\3"+
    "\1\0\5\3\1\0\2\3\1\0\7\4\3\0\4\4\6\0\12\4"+
    "\6\0\6\3\4\4\106\0\46\3\12\0\51\3\7\0\132\3\5\0"+
    "\104\3\5\0\122\3\6\0\7\3\1\0\77\3\1\0\1\3\1\0"+
    "\4\3\2\0\7\3\1\0\1\3\1\0\4\3\2\0\47\3\1\0"+
    "\1\3\1\0\4\3\2\0\37\3\1\0\1\3\1\0\4\3\2\0"+
    "\7\3\1\0\1\3\1\0\4\3\2\0\7\3\1\0\7\3\1\0"+
    "\27\3\1\0\37\3\1\0\1\3\1\0\4\3\2\0\7\3\1\0"+
    "\47\3\1\0\23\3\16\0\11\4\56\0\125\3\14\0\u026c\3\2\0"+
    "\10\3\12\0\32\3\5\0\113\3\3\0\3\3\17\0\15\3\1\0"+
    "\4\3\3\4\13\0\22\3\3\4\13\0\22\3\2\4\14\0\15\3"+
    "\1\0\3\3\1\0\2\4\14\0\64\3\40\4\3\0\1\3\3\0"+
    "\2\3\1\4\2\0\12\4\41\0\3\4\2\0\12\4\6\0\130\3"+
    "\10\0\51\3\1\4\126\0\35\3\3\0\14\4\4\0\14\4\12\0"+
    "\12\4\36\3\2\0\5\3\u038b\0\154\3\224\0\234\3\4\0\132\3"+
    "\6\0\26\3\2\0\6\3\2\0\46\3\2\0\6\3\2\0\10\3"+
    "\1\0\1\3\1\0\1\3\1\0\1\3\1\0\37\3\2\0\65\3"+
    "\1\0\7\3\1\0\1\3\3\0\3\3\1\0\7\3\3\0\4\3"+
    "\2\0\6\3\4\0\15\3\5\0\3\3\1\0\7\3\17\0\4\4"+
    "\32\0\5\4\20\0\2\3\23\0\1\3\13\0\4\4\6\0\6\4"+
    "\1\0\1\3\15\0\1\3\40\0\22\3\36\0\15\4\4\0\1\4"+
    "\3\0\6\4\27\0\1\3\4\0\1\3\2\0\12\3\1\0\1\3"+
    "\3\0\5\3\6\0\1\3\1\0\1\3\1\0\1\3\1\0\4\3"+
    "\1\0\3\3\1\0\7\3\3\0\3\3\5\0\5\3\26\0\44\3"+
    "\u0e81\0\3\3\31\0\11\3\6\4\1\0\5\3\2\0\5\3\4\0"+
    "\126\3\2\0\2\4\2\0\3\3\1\0\137\3\5\0\50\3\4\0"+
    "\136\3\21\0\30\3\70\0\20\3\u0200\0\u19b6\3\112\0\u51a6\3\132\0"+
    "\u048d\3\u0773\0\u2ba4\3\u215c\0\u012e\3\2\0\73\3\225\0\7\3\14\0"+
    "\5\3\5\0\1\3\1\4\12\3\1\0\15\3\1\0\5\3\1\0"+
    "\1\3\1\0\2\3\1\0\2\3\1\0\154\3\41\0\u016b\3\22\0"+
    "\100\3\2\0\66\3\50\0\15\3\3\0\20\4\20\0\4\4\17\0"+
    "\2\3\30\0\3\3\31\0\1\3\6\0\5\3\1\0\207\3\2\0"+
    "\1\4\4\0\1\3\13\0\12\4\7\0\32\3\4\0\1\3\1\0"+
    "\32\3\12\0\132\3\3\0\6\3\2\0\6\3\2\0\6\3\2\0"+
    "\3\3\3\0\2\3\3\0\2\3\22\0\3\4\4\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7"+
    "\1\4\1\7\1\10\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\1\20\4\1\17\1\20\1\21\1\22\1\23"+
    "\1\24\1\25\1\26\1\27\1\30\1\31\1\32\1\33"+
    "\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
    "\1\44\1\45\1\0\1\46\1\47\3\50\1\4\2\0"+
    "\3\7\1\50\1\0\1\51\1\52\2\12\2\14\2\53"+
    "\1\54\1\55\3\0\1\56\7\4\1\57\13\4\1\60"+
    "\1\61\1\62\1\63\1\64\1\65\1\66\1\67\1\70"+
    "\1\71\1\43\2\50\1\4\2\0\1\7\1\46\1\0"+
    "\1\7\1\0\1\72\1\73\4\0\1\74\7\4\1\75"+
    "\11\4\2\0\1\4\3\0\1\50\7\0\1\76\2\4"+
    "\1\77\1\100\2\4\1\101\5\4\1\102\2\4\1\103"+
    "\1\43\1\4\3\0\1\47\1\50\5\0\1\104\3\4"+
    "\1\105\4\4\1\106\2\4\2\0\1\107\1\0\1\110"+
    "\3\4\1\111\1\112\1\113\1\4\1\114\1\115\1\0"+
    "\1\116\1\117\2\4\1\120\1\121\1\122\1\123\1\124";

  private static int [] zzUnpackAction() {
    int [] result = new int[228];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\107\0\216\0\107\0\325\0\u011c\0\u0163\0\u01aa"+
    "\0\u01f1\0\u0238\0\u027f\0\u02c6\0\u030d\0\107\0\u0354\0\u039b"+
    "\0\107\0\u03e2\0\u0429\0\u0470\0\u04b7\0\u04fe\0\u0545\0\u058c"+
    "\0\u05d3\0\u061a\0\u0661\0\u06a8\0\u06ef\0\u0736\0\u077d\0\u07c4"+
    "\0\u080b\0\u0852\0\u0899\0\u08e0\0\u0927\0\u096e\0\u09b5\0\u09fc"+
    "\0\u0a43\0\107\0\107\0\107\0\107\0\107\0\107\0\107"+
    "\0\107\0\107\0\107\0\107\0\107\0\u0a8a\0\u0ad1\0\107"+
    "\0\107\0\u0b18\0\107\0\107\0\107\0\u0b5f\0\u0ba6\0\u0bed"+
    "\0\u0c34\0\u0c7b\0\u0cc2\0\u0d09\0\u0d50\0\u0d97\0\u0dde\0\107"+
    "\0\107\0\107\0\u0e25\0\u0e6c\0\107\0\u0eb3\0\u0efa\0\u0f41"+
    "\0\u0eb3\0\u0f88\0\u0fcf\0\u1016\0\325\0\u105d\0\u10a4\0\u10eb"+
    "\0\u1132\0\u1179\0\u11c0\0\u1207\0\325\0\u124e\0\u1295\0\u12dc"+
    "\0\u1323\0\u136a\0\u13b1\0\u13f8\0\u143f\0\u1486\0\u14cd\0\u1514"+
    "\0\107\0\107\0\107\0\107\0\107\0\107\0\107\0\107"+
    "\0\107\0\107\0\u155b\0\u15a2\0\u15e9\0\u1630\0\u1677\0\u16be"+
    "\0\u1705\0\u16be\0\u174c\0\u1793\0\u17da\0\107\0\107\0\u1821"+
    "\0\u1868\0\u18af\0\u18f6\0\325\0\u193d\0\u1984\0\u19cb\0\u1a12"+
    "\0\u1a59\0\u1aa0\0\u1ae7\0\325\0\u1b2e\0\u1b75\0\u1bbc\0\u1c03"+
    "\0\u1c4a\0\u1c91\0\u1cd8\0\u1d1f\0\u1d66\0\u1dad\0\u1df4\0\u1e3b"+
    "\0\u1e82\0\u1ec9\0\u1f10\0\u1f57\0\u1f9e\0\u1fe5\0\u202c\0\u2073"+
    "\0\u20ba\0\u2101\0\u2148\0\325\0\u218f\0\u21d6\0\325\0\325"+
    "\0\u221d\0\u2264\0\325\0\u22ab\0\u22f2\0\u2339\0\u2380\0\u23c7"+
    "\0\325\0\u240e\0\u2455\0\325\0\107\0\u249c\0\u24e3\0\u252a"+
    "\0\u2571\0\u1e82\0\u1e82\0\u25b8\0\u25ff\0\u2646\0\u268d\0\u26d4"+
    "\0\325\0\u271b\0\u2762\0\u27a9\0\325\0\u27f0\0\u2837\0\u287e"+
    "\0\u28c5\0\325\0\u290c\0\u2953\0\u299a\0\u29e1\0\107\0\u2a28"+
    "\0\107\0\u2a6f\0\u2ab6\0\u2afd\0\325\0\325\0\325\0\u2b44"+
    "\0\325\0\325\0\u2b8b\0\107\0\107\0\u2bd2\0\u2c19\0\325"+
    "\0\325\0\107\0\325\0\325";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[228];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\5\1\2\1\6\1\7\1\3"+
    "\1\10\1\11\1\10\1\5\1\10\1\12\5\5\1\13"+
    "\1\5\1\14\1\5\1\15\1\16\1\17\1\20\1\21"+
    "\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31"+
    "\1\32\1\33\1\34\2\5\1\35\1\5\1\36\1\37"+
    "\1\40\1\41\1\5\1\42\1\5\2\10\1\43\1\44"+
    "\1\45\1\46\1\47\1\50\1\51\1\52\1\53\1\54"+
    "\1\55\1\56\1\57\1\60\1\61\1\62\1\63\1\64"+
    "\1\65\110\0\1\3\5\0\1\3\102\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\27\5\30\0"+
    "\1\66\1\67\55\0\1\70\106\0\1\71\32\0\1\10"+
    "\1\72\1\10\1\0\2\10\2\0\1\73\1\74\1\75"+
    "\1\76\1\77\13\0\1\73\1\0\1\75\1\77\1\74"+
    "\15\0\2\10\26\0\2\5\3\0\1\5\1\100\11\5"+
    "\1\0\1\5\1\0\1\5\6\0\27\5\33\0\1\101"+
    "\1\102\1\103\1\0\2\103\1\104\1\105\1\73\1\74"+
    "\1\75\1\76\1\77\13\0\1\73\1\0\1\75\1\77"+
    "\1\74\1\105\14\0\2\103\33\0\1\106\1\0\1\106"+
    "\1\0\2\106\5\0\1\107\36\0\2\106\50\0\1\110"+
    "\36\0\1\111\22\0\2\15\1\0\4\15\1\0\17\15"+
    "\1\112\1\113\56\15\2\17\1\0\4\17\1\0\20\17"+
    "\1\114\1\115\55\17\2\116\1\0\4\116\1\0\20\116"+
    "\1\117\1\116\1\120\31\116\1\121\22\116\35\0\1\122"+
    "\4\0\1\123\1\124\46\0\2\5\3\0\13\5\1\0"+
    "\1\5\1\0\1\5\6\0\7\5\1\125\17\5\26\0"+
    "\2\5\3\0\13\5\1\0\1\5\1\0\1\5\6\0"+
    "\6\5\1\126\20\5\26\0\2\5\3\0\13\5\1\0"+
    "\1\5\1\0\1\5\6\0\12\5\1\127\2\5\1\130"+
    "\4\5\1\131\4\5\26\0\2\5\3\0\13\5\1\0"+
    "\1\5\1\0\1\5\6\0\15\5\1\132\11\5\26\0"+
    "\2\5\3\0\13\5\1\0\1\5\1\0\1\5\6\0"+
    "\1\5\1\133\25\5\26\0\2\5\3\0\13\5\1\0"+
    "\1\5\1\0\1\5\6\0\6\5\1\134\6\5\1\135"+
    "\11\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\3\5\1\136\23\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\15\5\1\137"+
    "\11\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\11\5\1\140\15\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\6\5\1\141"+
    "\20\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\1\142\17\5\1\143\6\5\26\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\24\5"+
    "\1\144\2\5\26\0\2\5\3\0\13\5\1\0\1\5"+
    "\1\0\1\5\6\0\15\5\1\145\11\5\26\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\22\5"+
    "\1\146\4\5\26\0\2\5\3\0\13\5\1\0\1\5"+
    "\1\0\1\5\6\0\4\5\1\147\22\5\26\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\15\5"+
    "\1\150\11\5\107\0\1\151\106\0\1\152\106\0\1\153"+
    "\1\0\1\154\104\0\1\155\2\0\1\156\103\0\1\157"+
    "\3\0\1\160\102\0\1\161\106\0\1\162\22\0\2\66"+
    "\1\0\4\66\1\0\77\66\6\163\1\0\100\163\10\0"+
    "\1\10\1\72\1\10\1\0\2\10\44\0\2\10\33\0"+
    "\1\106\1\0\1\106\1\0\2\106\3\0\1\74\1\75"+
    "\1\0\1\77\15\0\1\75\1\77\1\74\15\0\2\106"+
    "\33\0\1\164\1\0\1\164\1\0\2\164\3\0\1\74"+
    "\1\75\2\0\1\165\14\0\1\75\1\0\1\74\15\0"+
    "\2\164\3\0\1\165\22\0\2\5\3\0\13\5\1\0"+
    "\1\5\1\0\1\5\6\0\1\166\26\5\33\0\1\101"+
    "\1\167\1\101\1\0\2\101\3\0\1\74\1\75\1\76"+
    "\1\77\15\0\1\75\1\77\1\74\15\0\2\101\33\0"+
    "\1\101\1\102\1\103\1\0\2\103\44\0\2\103\33\0"+
    "\1\101\1\102\1\103\1\0\2\103\2\0\1\73\1\74"+
    "\1\75\1\76\1\77\13\0\1\73\1\0\1\75\1\77"+
    "\1\74\15\0\2\103\23\0\2\170\1\0\5\170\1\171"+
    "\1\170\4\171\1\170\1\171\1\172\2\171\1\170\1\171"+
    "\1\170\1\173\10\170\1\171\1\172\1\170\4\171\1\170"+
    "\1\171\6\170\1\173\3\170\2\171\23\170\14\0\2\174"+
    "\2\0\1\73\17\0\1\73\56\0\1\106\1\175\1\106"+
    "\1\0\2\106\3\0\1\74\1\75\1\0\1\77\15\0"+
    "\1\75\1\77\1\74\15\0\2\106\46\0\1\176\63\0"+
    "\2\15\1\0\4\15\1\0\77\15\2\17\1\0\4\17"+
    "\1\0\77\17\2\116\1\0\4\116\1\0\20\116\1\117"+
    "\1\116\1\0\56\116\1\0\4\116\1\0\77\116\64\0"+
    "\1\177\60\0\1\200\5\0\1\201\105\0\1\202\101\0"+
    "\1\203\53\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\20\5\1\204\6\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\14\5\1\205"+
    "\12\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\1\5\1\206\25\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\12\5\1\207"+
    "\14\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\1\5\1\210\25\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\14\5\1\211"+
    "\12\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\7\5\1\212\17\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\14\5\1\213"+
    "\12\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\11\5\1\214\15\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\6\5\1\215"+
    "\20\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\16\5\1\216\10\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\17\5\1\217"+
    "\7\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\1\220\26\5\26\0\2\5\3\0\13\5"+
    "\1\0\1\5\1\0\1\5\6\0\21\5\1\221\5\5"+
    "\26\0\2\5\3\0\13\5\1\0\1\5\1\0\1\5"+
    "\6\0\16\5\1\222\10\5\26\0\2\5\3\0\13\5"+
    "\1\0\1\5\1\0\1\5\6\0\1\223\26\5\26\0"+
    "\2\5\3\0\13\5\1\0\1\5\1\0\1\5\6\0"+
    "\10\5\1\224\16\5\26\0\2\5\3\0\13\5\1\0"+
    "\1\5\1\0\1\5\6\0\1\225\26\5\23\0\6\163"+
    "\1\226\100\163\10\0\1\164\1\227\1\164\1\0\2\164"+
    "\3\0\1\74\1\75\17\0\1\75\1\0\1\74\15\0"+
    "\2\164\33\0\1\164\1\0\1\164\1\0\2\164\3\0"+
    "\1\74\1\75\17\0\1\75\1\0\1\74\15\0\2\164"+
    "\26\0\2\5\3\0\13\5\1\0\1\5\1\0\1\5"+
    "\6\0\1\5\1\230\25\5\33\0\1\101\1\167\1\101"+
    "\1\0\2\101\44\0\2\101\33\0\1\231\1\0\4\231"+
    "\1\0\1\231\1\0\2\231\1\0\1\231\1\0\1\232"+
    "\10\0\1\231\2\0\4\231\1\0\1\231\6\0\1\232"+
    "\3\0\2\231\23\0\2\170\1\0\5\170\1\171\1\233"+
    "\4\171\1\170\1\171\1\172\2\171\1\170\1\171\1\170"+
    "\1\173\10\170\1\171\1\172\1\170\4\171\1\170\1\171"+
    "\6\170\1\173\3\170\2\171\23\170\10\0\1\234\1\0"+
    "\1\234\1\231\2\234\1\0\1\231\1\0\2\231\1\0"+
    "\1\231\1\235\1\232\10\0\1\231\2\0\4\231\1\0"+
    "\1\231\6\0\1\232\3\0\2\234\3\0\1\235\30\0"+
    "\1\236\2\0\2\174\2\0\1\73\17\0\1\73\56\0"+
    "\1\106\1\175\1\106\1\0\2\106\44\0\2\106\62\0"+
    "\1\237\105\0\1\240\3\0\1\241\110\0\1\242\104\0"+
    "\1\243\47\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\6\5\1\244\20\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\14\5\1\245"+
    "\1\5\1\246\10\5\26\0\2\5\3\0\13\5\1\0"+
    "\1\5\1\0\1\5\6\0\11\5\1\247\15\5\26\0"+
    "\2\5\3\0\13\5\1\0\1\5\1\0\1\5\6\0"+
    "\17\5\1\250\7\5\26\0\2\5\3\0\13\5\1\0"+
    "\1\5\1\0\1\5\6\0\1\251\26\5\26\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\12\5"+
    "\1\252\14\5\26\0\2\5\3\0\13\5\1\0\1\5"+
    "\1\0\1\5\6\0\6\5\1\253\20\5\26\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\12\5"+
    "\1\254\14\5\26\0\2\5\3\0\13\5\1\0\1\5"+
    "\1\0\1\5\6\0\4\5\1\255\22\5\26\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\1\5"+
    "\1\256\25\5\26\0\2\5\3\0\13\5\1\0\1\5"+
    "\1\0\1\5\6\0\16\5\1\257\10\5\26\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\6\5"+
    "\1\260\20\5\26\0\2\5\3\0\13\5\1\0\1\5"+
    "\1\0\1\5\6\0\15\5\1\261\11\5\26\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\3\5"+
    "\1\262\23\5\26\0\2\5\3\0\13\5\1\0\1\5"+
    "\1\0\1\5\6\0\3\5\1\263\23\5\26\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\5\5"+
    "\1\264\21\5\23\0\5\163\1\265\1\226\100\163\10\0"+
    "\1\164\1\227\1\164\1\0\2\164\44\0\2\164\26\0"+
    "\2\5\3\0\13\5\1\0\1\5\1\0\1\5\6\0"+
    "\16\5\1\266\10\5\33\0\1\231\1\267\4\231\1\0"+
    "\1\231\1\0\2\231\1\0\1\231\1\0\1\232\10\0"+
    "\1\231\2\0\4\231\1\0\1\231\6\0\1\232\3\0"+
    "\2\231\33\0\1\164\1\0\1\164\1\0\2\164\7\0"+
    "\1\235\34\0\2\164\3\0\1\235\27\0\1\171\1\270"+
    "\4\171\1\0\1\171\1\0\2\171\1\0\1\171\1\0"+
    "\1\232\10\0\1\171\2\0\4\171\1\0\1\171\6\0"+
    "\1\232\3\0\2\171\33\0\1\234\1\271\1\234\1\231"+
    "\2\234\1\0\1\231\1\0\1\272\1\273\1\0\1\231"+
    "\1\0\1\232\10\0\1\231\2\0\1\273\1\231\1\272"+
    "\1\231\1\0\1\231\6\0\1\232\3\0\2\234\33\0"+
    "\1\164\1\0\1\164\1\0\2\164\44\0\2\164\34\0"+
    "\1\236\2\0\2\174\131\0\1\274\110\0\1\275\107\0"+
    "\1\276\100\0\1\277\106\0\1\300\54\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\16\5\1\301"+
    "\10\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\1\302\26\5\26\0\2\5\3\0\13\5"+
    "\1\0\1\5\1\0\1\5\6\0\17\5\1\303\7\5"+
    "\26\0\2\5\3\0\13\5\1\0\1\5\1\0\1\5"+
    "\6\0\4\5\1\304\22\5\26\0\2\5\3\0\13\5"+
    "\1\0\1\5\1\0\1\5\6\0\13\5\1\305\13\5"+
    "\26\0\2\5\3\0\13\5\1\0\1\5\1\0\1\5"+
    "\6\0\11\5\1\306\15\5\26\0\2\5\3\0\13\5"+
    "\1\0\1\5\1\0\1\5\6\0\6\5\1\307\20\5"+
    "\26\0\2\5\3\0\13\5\1\0\1\5\1\0\1\5"+
    "\6\0\2\5\1\310\24\5\26\0\2\5\3\0\13\5"+
    "\1\0\1\5\1\0\1\5\6\0\5\5\1\311\21\5"+
    "\26\0\2\5\3\0\13\5\1\0\1\5\1\0\1\5"+
    "\6\0\6\5\1\312\20\5\26\0\2\5\3\0\13\5"+
    "\1\0\1\5\1\0\1\5\6\0\1\313\26\5\26\0"+
    "\2\5\3\0\13\5\1\0\1\5\1\0\1\5\6\0"+
    "\25\5\1\314\1\5\33\0\1\231\1\267\4\231\1\0"+
    "\1\231\1\0\2\231\1\0\1\231\12\0\1\231\2\0"+
    "\4\231\1\0\1\231\12\0\2\231\33\0\1\171\1\270"+
    "\4\171\1\0\1\171\1\0\2\171\1\0\1\171\12\0"+
    "\1\171\2\0\4\171\1\0\1\171\12\0\2\171\33\0"+
    "\1\234\1\271\1\234\1\231\2\234\1\0\1\231\1\0"+
    "\2\231\1\0\1\231\12\0\1\231\2\0\4\231\1\0"+
    "\1\231\12\0\2\234\64\0\1\315\110\0\1\316\107\0"+
    "\1\317\100\0\1\320\114\0\1\321\45\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\1\5\1\322"+
    "\25\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\1\5\1\323\25\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\3\5\1\324"+
    "\23\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\1\5\1\325\25\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\5\5\1\326"+
    "\21\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\22\5\1\327\4\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\6\5\1\330"+
    "\20\5\26\0\2\5\3\0\13\5\1\0\1\5\1\0"+
    "\1\5\6\0\2\5\1\331\24\5\26\0\2\5\3\0"+
    "\13\5\1\0\1\5\1\0\1\5\6\0\26\5\1\332"+
    "\65\0\1\333\110\0\1\334\105\0\1\335\46\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\4\5"+
    "\1\336\22\5\26\0\2\5\3\0\13\5\1\0\1\5"+
    "\1\0\1\5\6\0\6\5\1\337\20\5\26\0\2\5"+
    "\3\0\13\5\1\0\1\5\1\0\1\5\6\0\16\5"+
    "\1\340\10\5\26\0\2\5\3\0\13\5\1\0\1\5"+
    "\1\0\1\5\6\0\7\5\1\341\17\5\66\0\1\342"+
    "\46\0\2\5\3\0\13\5\1\0\1\5\1\0\1\5"+
    "\6\0\6\5\1\343\20\5\26\0\2\5\3\0\13\5"+
    "\1\0\1\5\1\0\1\5\6\0\5\5\1\344\21\5"+
    "\23\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[11360];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;
  private static final char[] EMPTY_BUFFER = new char[0];
  private static final int YYEOF = -1;
  private static java.io.Reader zzReader = null; // Fake

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\1\1\1\11\11\1\1\11\2\1\1\11"+
    "\30\1\14\11\2\1\2\11\1\0\3\11\3\1\2\0"+
    "\4\1\1\0\3\11\2\1\1\11\4\1\3\0\24\1"+
    "\12\11\4\1\2\0\2\1\1\0\1\1\1\0\2\11"+
    "\4\0\22\1\2\0\1\1\3\0\1\1\7\0\21\1"+
    "\1\11\1\1\3\0\2\1\5\0\14\1\2\0\1\11"+
    "\1\0\1\11\11\1\1\0\2\11\4\1\1\11\2\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[228];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** this buffer may contains the current text array to be matched when it is cheap to acquire it */
  private char[] zzBufferArray;

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
  public _CLexer(){
    this((java.io.Reader)null);
  }

  public void goTo(int offset) {
    zzCurrentPos = zzMarkedPos = zzStartRead = offset;
    zzPushbackPos = 0;
    zzAtEOF = offset < zzEndRead;
  }


  _CLexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  _CLexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 1796) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart(){
    return zzStartRead;
  }

  public final int getTokenEnd(){
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end,int initialState){
    zzBuffer = buffer;
    zzBufferArray = com.intellij.util.text.CharArrayUtil.fromSequenceWithoutCopying(buffer);
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzPushbackPos = 0;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBufferArray != null ? zzBufferArray[zzStartRead+pos]:zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;
    char[] zzBufferArrayL = zzBufferArray;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL.charAt(zzCurrentPosL++);
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL.charAt(zzCurrentPosL++);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 37: 
          { return CTokenType.ASTERISKEQ;
          }
        case 85: break;
        case 59: 
          { return CTokenType.LTLTEQ;
          }
        case 86: break;
        case 80: 
          { return CTokenType.DEFAULT_KEYWORD;
          }
        case 87: break;
        case 29: 
          { return CTokenType.COMMA;
          }
        case 88: break;
        case 39: 
          { return CTokenType.FLOAT_LITERAL;
          }
        case 89: break;
        case 75: 
          { return CTokenType.SWITCH_KEYWORD;
          }
        case 90: break;
        case 43: 
          { return CTokenType.STRING_INCLUDE_LITERAL;
          }
        case 91: break;
        case 71: 
          { return CTokenType.S_IFDEF_KEYWORD;
          }
        case 92: break;
        case 40: 
          { return CTokenType.DOUBLE_LITERAL;
          }
        case 93: break;
        case 46: 
          { return CTokenType.IF_KEYWORD;
          }
        case 94: break;
        case 61: 
          { return CTokenType.FOR_KEYWORD;
          }
        case 95: break;
        case 56: 
          { return CTokenType.XOREQ;
          }
        case 96: break;
        case 66: 
          { return CTokenType.GOTO_KEYWORD;
          }
        case 97: break;
        case 22: 
          { return CTokenType.LPARENTH;
          }
        case 98: break;
        case 81: 
          { return CTokenType.TYPEDEF_KEYWORD;
          }
        case 99: break;
        case 12: 
          { return CTokenType.STRING_LITERAL;
          }
        case 100: break;
        case 10: 
          { return CTokenType.CHARACTER_LITERAL;
          }
        case 101: break;
        case 18: 
          { return CTokenType.PLUS;
          }
        case 102: break;
        case 67: 
          { return CTokenType.VOID_KEYWORD;
          }
        case 103: break;
        case 48: 
          { return CTokenType.EQEQ;
          }
        case 104: break;
        case 36: 
          { return CTokenType.DIVEQ;
          }
        case 105: break;
        case 38: 
          { return CTokenType.LONG_LITERAL;
          }
        case 106: break;
        case 4: 
          { return CTokenType.IDENTIFIER;
          }
        case 107: break;
        case 82: 
          { return CTokenType.S_INCLUDE_KEYWORD;
          }
        case 108: break;
        case 83: 
          { return CTokenType.CONTINUE_KEYWORD;
          }
        case 109: break;
        case 3: 
          { return CTokenType.NEW_LINE;
          }
        case 110: break;
        case 34: 
          { return CTokenType.END_OF_LINE_COMMENT;
          }
        case 111: break;
        case 44: 
          { return CTokenType.LTLT;
          }
        case 112: break;
        case 16: 
          { return CTokenType.EXCL;
          }
        case 113: break;
        case 1: 
          { return CTokenType.BAD_CHARACTER;
          }
        case 114: break;
        case 58: 
          { return CTokenType.ELLIPSIS;
          }
        case 115: break;
        case 13: 
          { return CTokenType.LT;
          }
        case 116: break;
        case 52: 
          { return CTokenType.PLUSEQ;
          }
        case 117: break;
        case 54: 
          { return CTokenType.ANDEQ;
          }
        case 118: break;
        case 57: 
          { return CTokenType.PERCEQ;
          }
        case 119: break;
        case 50: 
          { return CTokenType.OREQ;
          }
        case 120: break;
        case 2: 
          { return CTokenType.WHITE_SPACE;
          }
        case 121: break;
        case 62: 
          { return CTokenType.CASE_KEYWORD;
          }
        case 122: break;
        case 69: 
          { return CTokenType.BREAK_KEYWORD;
          }
        case 123: break;
        case 79: 
          { return CTokenType.S_DEFINE_KEYWORD;
          }
        case 124: break;
        case 30: 
          { return CTokenType.TILDE;
          }
        case 125: break;
        case 14: 
          { return CTokenType.GT;
          }
        case 126: break;
        case 19: 
          { return CTokenType.AND;
          }
        case 127: break;
        case 7: 
          { return CTokenType.INTEGER_LITERAL;
          }
        case 128: break;
        case 84: 
          { return CTokenType.UNSIGNED_KEYWORD;
          }
        case 129: break;
        case 17: 
          { return CTokenType.OR;
          }
        case 130: break;
        case 25: 
          { return CTokenType.RBRACE;
          }
        case 131: break;
        case 24: 
          { return CTokenType.LBRACE;
          }
        case 132: break;
        case 9: 
          { return CTokenType.MINUS;
          }
        case 133: break;
        case 73: 
          { return CTokenType.RETURN_KEYWORD;
          }
        case 134: break;
        case 64: 
          { return CTokenType.LONG_KEYWORD;
          }
        case 135: break;
        case 31: 
          { return CTokenType.QUEST;
          }
        case 136: break;
        case 41: 
          { return CTokenType.MINUSMINUS;
          }
        case 137: break;
        case 33: 
          { return CTokenType.AT;
          }
        case 138: break;
        case 27: 
          { return CTokenType.RBRACKET;
          }
        case 139: break;
        case 60: 
          { return CTokenType.NEW_KEYWORD;
          }
        case 140: break;
        case 70: 
          { return CTokenType.WHILE_KEYWORD;
          }
        case 141: break;
        case 49: 
          { return CTokenType.NE;
          }
        case 142: break;
        case 21: 
          { return CTokenType.PERC;
          }
        case 143: break;
        case 72: 
          { return CTokenType.S_ENDIF_KEYWORD;
          }
        case 144: break;
        case 45: 
          { return CTokenType.LE;
          }
        case 145: break;
        case 20: 
          { return CTokenType.XOR;
          }
        case 146: break;
        case 5: 
          { return CTokenType.DIV;
          }
        case 147: break;
        case 77: 
          { return CTokenType.__INT64_KEYWORD;
          }
        case 148: break;
        case 53: 
          { return CTokenType.PLUSPLUS;
          }
        case 149: break;
        case 42: 
          { return CTokenType.MINUSEQ;
          }
        case 150: break;
        case 76: 
          { return CTokenType.PUBLIC_KEYWORD;
          }
        case 151: break;
        case 35: 
          { return CTokenType.C_STYLE_COMMENT;
          }
        case 152: break;
        case 47: 
          { return CTokenType.DO_KEYWORD;
          }
        case 153: break;
        case 11: 
          { return CTokenType.NEXT_LINE;
          }
        case 154: break;
        case 55: 
          { return CTokenType.ANDAND;
          }
        case 155: break;
        case 26: 
          { return CTokenType.LBRACKET;
          }
        case 156: break;
        case 32: 
          { return CTokenType.COLON;
          }
        case 157: break;
        case 63: 
          { return CTokenType.CHAR_KEYWORD;
          }
        case 158: break;
        case 51: 
          { return CTokenType.OROR;
          }
        case 159: break;
        case 65: 
          { return CTokenType.ELSE_KEYWORD;
          }
        case 160: break;
        case 8: 
          { return CTokenType.DOT;
          }
        case 161: break;
        case 78: 
          { return CTokenType.S_IFNDEF_KEYWORD;
          }
        case 162: break;
        case 74: 
          { return CTokenType.SIGNED_KEYWORD;
          }
        case 163: break;
        case 28: 
          { return CTokenType.SEMICOLON;
          }
        case 164: break;
        case 68: 
          { return CTokenType.CONST_KEYWORD;
          }
        case 165: break;
        case 23: 
          { return CTokenType.RPARENTH;
          }
        case 166: break;
        case 15: 
          { return CTokenType.EQ;
          }
        case 167: break;
        case 6: 
          { return CTokenType.ASTERISK;
          }
        case 168: break;
        default:
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
            return null;
          }
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
