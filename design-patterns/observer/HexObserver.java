public class HexObserver extends Observer {
  public HexObserver(Subject subject) {
    this.subject = subject;
    this.subject.addObserver(this);
  }
  public void update() {
    System.out.println("Octal String " + Integer.toHexString(subject.getState()));
  }
}
