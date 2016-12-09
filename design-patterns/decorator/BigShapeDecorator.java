public class BigShapeDecorator extends ShapeDecorator {
  public BigShapeDecorator(Shape shape) {
    this.shape = shape;
  }
  public void draw() {
    shape.draw();
    System.out.println("Which is big!");
  }
}
