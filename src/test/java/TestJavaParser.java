import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

@RunWith(Parameterized.class)
public class TestJavaParser {
	
	@Parameter
	public TypeSolver typeSolver;
	
	@Parameters
	public static List<TypeSolver> getTypeSolver() {
		List<TypeSolver> solvers = new ArrayList<>();
		
		solvers.add(new CombinedTypeSolver(new TypeSolver[] {
				new JavaParserTypeSolver("src/main/java"),
				new ReflectionTypeSolver(false)
		}));
		
		solvers.add(new CombinedTypeSolver(new TypeSolver[] {
			new ReflectionTypeSolver(false),
			new JavaParserTypeSolver("src/main/java")
		}));
		
		return solvers;
	}
	
	@Test
	public void testReflectionFirst() throws FileNotFoundException {
		JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
		JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
		
		CompilationUnit source = JavaParser.parse(new File("src/main/java/main/MyClass.java"));
		boolean failed = false;
		
		for (MethodCallExpr mce : source.findAll(MethodCallExpr.class)) {
			System.out.println("Solving at " + source.getType(0).getNameAsString() + " " + mce.getBegin().get() + ": " + mce.getNameAsString());
			
			try {
				mce.resolve();
				System.out.println("Success!");
			} catch (Exception e) {
				System.err.println("Error :(");
				e.printStackTrace();
				failed = true;
			}
			
			System.out.println();
		}
		
		assertFalse(failed);
	}
	
}
