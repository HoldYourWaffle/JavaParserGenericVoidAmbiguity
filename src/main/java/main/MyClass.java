package main;

public class MyClass {
	
	@SuppressWarnings("unused")
	private static <T> void method(GenericLambda<T> func) {}
	
	@SuppressWarnings("unused")
	private static void method(NonGenericLambda func) {}
	
	
	
	public void choke() {
		method(()->{}); //lambda that doesn't return anything, could be NonGenericLambda or GenericLambda<Void> in JP's eyes
	}
	
	public void choke2() {
		method(()->{ return null; }); //lambda explicitly returns null, could be NonGenericLambda or GenericLambda<Void> in JP's eyes
	}
	
	
	
	public void works() {
		method((NonGenericLambda) ()->{}); //explicitly cast to NonGenericLambda
		//acceptCustom((GenericLambda<Void>) ()->{}); this doesn't compile, since GenericLambda<Void> lamda's still have to provide an explicit return statement
	}
	
	public void works2() {
		method((GenericLambda<Void>) ()->{return null;});
	}
	
	
	public void works3() {
		method((GenericLambda<Void>) ()->{ return null; });
		//acceptCustom((NonGenericLambda) ()->{ return null; }); this doesn't compile, void lambda's can't return null
	}
	
}
