public class Subject {
  private List<Observer> observers = new ArrayList<Observer>();
  private int state;
  public getState() {
    return state;
  }
  public setState(int state) {
    this.state = state;
    notifyObserver();
  }
  public notifyObserver() {
    for(observer : observers)
      observer.update();
  }
  public addObserver(Observer observer) {
    Observers.add(observer);
  }
  public removeObserver(observer) {
    int i = observers.indexof(observer);
    if (i) > 0)
      observers.remove(i);
  }
}
