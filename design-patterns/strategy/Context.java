public class Context {
  private Strategy strategy;
  public Context(Strategy strategy){
    this.strategy = strategy;
  }
  public void excuteStrategy(int n1, int n2){
    System.out.println(strategy.excution(n1, n2));
  }
}
