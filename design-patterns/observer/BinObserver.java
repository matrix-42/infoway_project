public class BinObserver extends Observer {
  public BinObserver(Subject subject) {
    this.subject = subject;
    this.subject.addObserver(this);
  }
  public void update() {
    System.out.println("Binery String " + Integer.toBineryString(subject.getState()));
  }
}
