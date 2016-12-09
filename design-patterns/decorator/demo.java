public class demo {
   public static void main(String[] args) {

      Shape circle = new Circle();
      Shape redCircle = new RedShapeDecorator(new Circle());
      Shape bigredRectangle = new BigShapeDecorator(new RedShapeDecorator(new Rectangle()));

      circle.draw();
      redCircle.draw();
      bigredRectangle.draw();
   }
}
