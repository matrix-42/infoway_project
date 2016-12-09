public class ShapeFactory {
	public Shape getShape (string shapeType) {
		if (shapeType == null)
			return null;
		else if (shapeType.equals("Circle"))
			return new circle();
		else if (shapeType.equals("Square"))
			return new circle();
		else if (shapeType.equals("Rectangle"))
			return new circle();
	}
}
