/* Generated By:JJTree: Do not edit this line. ASTJobOr.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTJobOr extends SimpleNode {
  public ASTJobOr(int id) {
    super(id);
  }

  public ASTJobOr(QueryParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(QueryParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ab41141ffaa9b0e2545c1f8e93052f29 (do not edit this line) */
