public class Demo {
  public static void main(String[] args) {
    Subject subject = new Subject();
    BinObserver binObserver = new BinObserver(subject);
    OctObserver octObserver = new OctObserver(subject);
    HexObserver hexObserver = new HexObserver(subject);

    subject.setState(1);
    subject.setState(2);
    subject.setState(3);

  }
}
