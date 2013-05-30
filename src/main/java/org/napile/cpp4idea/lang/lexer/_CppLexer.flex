/* It's an automatically generated code. Do not modify it. */
package org.napile.cpp4idea.lang.lexer;

import org.napile.cpp4idea.lang.psi.CPsiTokens;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

@SuppressWarnings({"ALL"})
%%

%{
  public _CppLexer(){
    this((java.io.Reader)null);
  }

  public void goTo(int offset) {
    zzCurrentPos = zzMarkedPos = zzStartRead = offset;
    zzPushbackPos = 0;
    zzAtEOF = offset < zzEndRead;
  }
%}

%unicode
%public
%class _CppLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return;
%eof}

WHITE_SPACE_CHAR=[\ \r\t\f]
NEW_LINE_CHAR=[\n]

IDENTIFIER=[:jletter:] [:jletterdigit:]*

C_STYLE_COMMENT="/*""*"*("/"|([^"/""*"]{COMMENT_TAIL}))?
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?
END_OF_LINE_COMMENT="/""/"[^\r\n]*

DIGIT=[0-9]
DIGIT_OR_UNDERSCORE=[_0-9]
OCTAL_DIGIT=[0-7]
OCTAL_DIGIT_OR_UNDERSCORE=[_0-7]
HEX_DIGIT=[0-9A-Fa-f]
HEX_DIGIT_OR_UNDERSCORE=[_0-9A-Fa-f]
BINARY_DIGIT=[01]
BINARY_DIGIT_OR_UNDERSCORE=[_01]

DIGITS={DIGIT}|({DIGIT}{DIGIT_OR_UNDERSCORE}*{DIGIT})
HEX_DIGITS={HEX_DIGIT}|({HEX_DIGIT}{HEX_DIGIT_OR_UNDERSCORE}*{HEX_DIGIT})
BINARY_DIGITS={BINARY_DIGIT}|({BINARY_DIGIT}{BINARY_DIGIT_OR_UNDERSCORE}*{BINARY_DIGIT})

INTEGER_LITERAL={DECIMAL_INTEGER_LITERAL}|{OCTAL_INTEGER_LITERAL}|{HEX_INTEGER_LITERAL}|{BINARY_INTEGER_LITERAL}
DECIMAL_INTEGER_LITERAL=0|([1-9]({DIGIT_OR_UNDERSCORE}*{DIGIT})?)
OCTAL_INTEGER_LITERAL=0{OCTAL_DIGIT_OR_UNDERSCORE}*{OCTAL_DIGIT}
HEX_INTEGER_LITERAL=0[Xx]{HEX_DIGITS}?
BINARY_INTEGER_LITERAL=0[Bb]{BINARY_DIGITS}?
LONG_LITERAL={INTEGER_LITERAL}[Ll]

FLOAT_LITERAL=({FLOATING_POINT_LITERAL1}|{FLOATING_POINT_LITERAL2}|{FLOATING_POINT_LITERAL3}|{FLOATING_POINT_LITERAL4})[Ff]
DOUBLE_LITERAL=(({FLOATING_POINT_LITERAL1}|{FLOATING_POINT_LITERAL2}|{FLOATING_POINT_LITERAL3})[Dd]?)|({FLOATING_POINT_LITERAL4}[Dd])
FLOATING_POINT_LITERAL1={DIGITS}"."{DIGITS}?{EXPONENT_PART}?
FLOATING_POINT_LITERAL2="."{DIGITS}{EXPONENT_PART}?
FLOATING_POINT_LITERAL3={DIGITS}{EXPONENT_PART}
FLOATING_POINT_LITERAL4={DIGITS}
EXPONENT_PART=[Ee]["+""-"]?{DIGITS}?

HEX_FLOAT_LITERAL={HEX_SIGNIFICAND}{BINARY_EXPONENT}[Ff]
HEX_DOUBLE_LITERAL={HEX_SIGNIFICAND}{BINARY_EXPONENT}[Dd]?
BINARY_EXPONENT=[Pp][+-]?{DIGITS}
HEX_SIGNIFICAND={HEX_INTEGER_LITERAL}|{HEX_INTEGER_LITERAL}.|0[Xx]{HEX_DIGITS}?.{HEX_DIGITS}

CHARACTER_LITERAL="'"([^\\\'\r\n]|{ESCAPE_SEQUENCE})*("'"|\\)?
STRING_LITERAL=\"([^\\\"\r\n]|{ESCAPE_SEQUENCE})*(\"|\\)?
STRING_INCLUDE_LITERAL=\<([^\\\<\r\n]|{ESCAPE_SEQUENCE})*(\>|\\)?
ESCAPE_SEQUENCE=\\[^\r\n]

%%

<YYINITIAL> {WHITE_SPACE_CHAR}+ { return CPsiTokens.WHITE_SPACE; }
<YYINITIAL> {NEW_LINE_CHAR} { return CPsiTokens.NEW_LINE; }

<YYINITIAL> {C_STYLE_COMMENT} { return CPsiTokens.C_STYLE_COMMENT; }
<YYINITIAL> {END_OF_LINE_COMMENT} { return CPsiTokens.END_OF_LINE_COMMENT; }

<YYINITIAL> {LONG_LITERAL} { return CPsiTokens.LONG_LITERAL; }
<YYINITIAL> {INTEGER_LITERAL} { return CPsiTokens.INTEGER_LITERAL; }
<YYINITIAL> {FLOAT_LITERAL} { return CPsiTokens.FLOAT_LITERAL; }
<YYINITIAL> {HEX_FLOAT_LITERAL} { return CPsiTokens.FLOAT_LITERAL; }
<YYINITIAL> {DOUBLE_LITERAL} { return CPsiTokens.DOUBLE_LITERAL; }
<YYINITIAL> {HEX_DOUBLE_LITERAL} { return CPsiTokens.DOUBLE_LITERAL; }

<YYINITIAL> {CHARACTER_LITERAL} { return CPsiTokens.CHARACTER_LITERAL; }
<YYINITIAL> {STRING_LITERAL} { return CPsiTokens.STRING_LITERAL; }

<YYINITIAL> "#include" { return CPsiTokens.S_INCLUDE_KEYWORD; }
<YYINITIAL> "#define" { return CPsiTokens.S_DEFINE_KEYWORD; }
<YYINITIAL> "#ifndef" { return CPsiTokens.S_IFNDEF_KEYWORD; }
<YYINITIAL> "#ifdef" { return CPsiTokens.S_IFDEF_KEYWORD; }
<YYINITIAL> "#endif" { return CPsiTokens.S_ENDIF_KEYWORD; }
<YYINITIAL> "#else" { return CPsiTokens.S_ELSE_KEYWORD; }
<YYINITIAL> "break" { return CPsiTokens.BREAK_KEYWORD; }
<YYINITIAL> "case" { return CPsiTokens.CASE_KEYWORD; }
<YYINITIAL> "class" { return CPsiTokens.CLASS_KEYWORD; }
<YYINITIAL> "continue" { return CPsiTokens.CONTINUE_KEYWORD; }
<YYINITIAL> "default" { return CPsiTokens.DEFAULT_KEYWORD; }
<YYINITIAL> "do" { return CPsiTokens.DO_KEYWORD; }
<YYINITIAL> "else" { return CPsiTokens.ELSE_KEYWORD; }
<YYINITIAL> "for" { return CPsiTokens.FOR_KEYWORD; }
<YYINITIAL> "goto" { return CPsiTokens.GOTO_KEYWORD; }
<YYINITIAL> "if" { return CPsiTokens.IF_KEYWORD; }
<YYINITIAL> "new" { return CPsiTokens.NEW_KEYWORD; }
<YYINITIAL> "public" { return CPsiTokens.PUBLIC_KEYWORD; }
<YYINITIAL> "private" { return CPsiTokens.PRIVATE_KEYWORD; }
<YYINITIAL> "switch" { return CPsiTokens.SWITCH_KEYWORD; }
<YYINITIAL> "return" { return CPsiTokens.RETURN_KEYWORD; }
<YYINITIAL> "namespace" { return CPsiTokens.NAMESPACE_KEYWORD; }
<YYINITIAL> "void" { return CPsiTokens.VOID_KEYWORD; }
<YYINITIAL> "while" { return CPsiTokens.WHILE_KEYWORD; }
<YYINITIAL> "typedef" { return CPsiTokens.TYPEDEF_KEYWORD; }
<YYINITIAL> "enum" { return CPsiTokens.ENUM_KEYWORD; }

<YYINITIAL> "virtual" { return CPsiTokens.VIRTUAL_KEYWORD; }
<YYINITIAL> "explicit" { return CPsiTokens.EXPLICIT_KEYWORD; }
<YYINITIAL> "static" { return CPsiTokens.STATIC_KEYWORD; }
<YYINITIAL> "const" { return CPsiTokens.CONST_KEYWORD; }
<YYINITIAL> "extern" { return CPsiTokens.EXTERN_KEYWORD; }
<YYINITIAL> "signed" { return CPsiTokens.SIGNED_KEYWORD; }
<YYINITIAL> "unsigned" { return CPsiTokens.UNSIGNED_KEYWORD; }

<YYINITIAL> "long" { return CPsiTokens.LONG_KEYWORD; }
<YYINITIAL> "char" { return CPsiTokens.CHAR_KEYWORD; }
<YYINITIAL> "__int64" { return CPsiTokens.__INT64_KEYWORD; }
<YYINITIAL> "int" { return CPsiTokens.INT_KEYWORD; }
<YYINITIAL> "bool" { return CPsiTokens.BOOL_KEYWORD; }

<YYINITIAL> "true" { return CPsiTokens.BOOL_LITERAL; }
<YYINITIAL> "false" { return CPsiTokens.BOOL_LITERAL; }

<YYINITIAL> {IDENTIFIER} { return CPsiTokens.IDENTIFIER; }

<YYINITIAL> "==" { return CPsiTokens.EQEQ; }
<YYINITIAL> "!=" { return CPsiTokens.NE; }
<YYINITIAL> "||" { return CPsiTokens.OROR; }
<YYINITIAL> "++" { return CPsiTokens.PLUSPLUS; }
<YYINITIAL> "->" { return CPsiTokens.ARROW; }
<YYINITIAL> "--" { return CPsiTokens.MINUSMINUS; }

<YYINITIAL> "<" { return CPsiTokens.LT; }
<YYINITIAL> "<=" { return CPsiTokens.LE; }
<YYINITIAL> "<<=" { return CPsiTokens.LTLTEQ; }
<YYINITIAL> "<<" { return CPsiTokens.LTLT; }
<YYINITIAL> ">" { return CPsiTokens.GT; }
<YYINITIAL> "&" { return CPsiTokens.AND; }
<YYINITIAL> "&&" { return CPsiTokens.ANDAND; }

<YYINITIAL> "+=" { return CPsiTokens.PLUSEQ; }
<YYINITIAL> "-=" { return CPsiTokens.MINUSEQ; }
<YYINITIAL> "*=" { return CPsiTokens.ASTERISKEQ; }
<YYINITIAL> "/=" { return CPsiTokens.DIVEQ; }
<YYINITIAL> "&=" { return CPsiTokens.ANDEQ; }
<YYINITIAL> "|=" { return CPsiTokens.OREQ; }
<YYINITIAL> "^=" { return CPsiTokens.XOREQ; }
<YYINITIAL> "%=" { return CPsiTokens.PERCEQ; }

<YYINITIAL> "("   { return CPsiTokens.LPARENTH; }
<YYINITIAL> ")"   { return CPsiTokens.RPARENTH; }
<YYINITIAL> "{"   { return CPsiTokens.LBRACE; }
<YYINITIAL> "}"   { return CPsiTokens.RBRACE; }
<YYINITIAL> "["   { return CPsiTokens.LBRACKET; }
<YYINITIAL> "]"   { return CPsiTokens.RBRACKET; }
<YYINITIAL> ";"   { return CPsiTokens.SEMICOLON; }
<YYINITIAL> ","   { return CPsiTokens.COMMA; }
<YYINITIAL> "..." { return CPsiTokens.ELLIPSIS; }
<YYINITIAL> "."   { return CPsiTokens.DOT; }

<YYINITIAL> "=" { return CPsiTokens.EQ; }
<YYINITIAL> "!" { return CPsiTokens.EXCL; }
<YYINITIAL> "~" { return CPsiTokens.TILDE; }
<YYINITIAL> "?" { return CPsiTokens.QUEST; }
<YYINITIAL> "::" { return CPsiTokens.DOUBLE_COLON; }
<YYINITIAL> ":" { return CPsiTokens.COLON; }
<YYINITIAL> "+" { return CPsiTokens.PLUS; }
<YYINITIAL> "-" { return CPsiTokens.MINUS; }
<YYINITIAL> "*" { return CPsiTokens.ASTERISK; }
<YYINITIAL> "/" { return CPsiTokens.DIV; }
<YYINITIAL> "\\" { return CPsiTokens.NEXT_LINE; }
<YYINITIAL> "|" { return CPsiTokens.OR; }
<YYINITIAL> "^" { return CPsiTokens.XOR; }
<YYINITIAL> "%" { return CPsiTokens.PERC; }
<YYINITIAL> "@" { return CPsiTokens.AT; }

<YYINITIAL> {STRING_INCLUDE_LITERAL} { return CPsiTokens.STRING_INCLUDE_LITERAL; }

<YYINITIAL> . { return CPsiTokens.BAD_CHARACTER; }
