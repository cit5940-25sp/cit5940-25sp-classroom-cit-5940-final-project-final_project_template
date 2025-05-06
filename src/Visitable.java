public interface Visitable<R>{
    R accept(ASTVisitor<R> visitor);
}
