public class RedShapeDecorator extends ShapeDecorator {
  public RedShapeDecorator(Shape shape) {
    this.shape = shape;
  }
  public void draw() {
    shape.draw();
    System.out.println("Which is red!");
  }
}
