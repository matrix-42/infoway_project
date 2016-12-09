public class demo {
  public static void main(String[] args) {
    Context context;
    context = new Context(new StrategyAdd);
    context.excuteStrategy(4, 2);

    context = new Context(new StrategyMultiply);
    context.excuteStrategy(4, 2);

    context = new Context(new StrategySubstract);
    context.excuteStrategy(4, 2);
    
  }
}
