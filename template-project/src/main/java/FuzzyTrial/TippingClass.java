package FuzzyTrial;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

import java.io.File;

public class TippingClass {


	public static void main(String[] args) throws Exception {
	//	String filename = "C:\\Users\\Burak\\OneDrive - Universiteit Antwerpen\\Images\\Hichem Boot\\7_9_21\\template_project_gradlecopie\\template-project\\src\\main\\java\\FuzzyTrial\\tipper.fcl";
	//	String filename = "tipper.fcl";
		String filename = "";



		String os_type = System.getProperty("os.name");

		if (os_type.contains("Windows"))
		filename = "C:\\Users\\Burak\\OneDrive - Universiteit Antwerpen\\Images\\Hichem Boot\\7_9_21\\template_project_gradlecopie\\template-project\\src\\main\\java\\FuzzyTrial\\tipper.fcl";
		else
		filename = System.getProperty("user.dir") + File.separator + "tipper.fcl";







		//System.out.println(file.toString());




/*

		Path path = Paths.get("tipper.fcl");

		System.out.println("zz"+path.toRealPath() );


		String a= TippingClass.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		String sep = FileSystems.getDefault().getSeparator();

		a = a.replace("/", "\\");








		filename = a+"tipper.fcl";

		filename = URLDecoder.decode(filename, "UTF-8");
		filename = filename.substring(1);



		System.out.println(filename);

*/


		FIS fis = FIS.load(filename, true);

		if (fis == null) {
			System.err.println("Can't load file: '" + filename + "'");
			System.exit(1);
		}

		// Get default function block

		FunctionBlock fb = fis.getFunctionBlock(null);
		JFuzzyChart.get().chart(fb);



		fb.setVariable("obstacle_left", 20);
		fb.setVariable("obstacle_right", 150);
		fb.setVariable("obstacle_front", 80);

		int imp[]={1400,1100,900,700,500,300,200,100};
		// Set inputs
		for (int i=0; i< imp.length; i++){
		fb.setVariable("distance", imp[i]);




	//
		// Evaluate
		fb.evaluate();

		// Show output variable's chart
		fb.getVariable("speed").defuzzify();

		// Print ruleSet
	//	System.out.println(fb);
		System.out.println("Speed: " + fb.getVariable("speed").getValue());
		System.out.println("SpeedMem: " + fb.getVariable("speed").getMembership("stop"));





		}
		System.out.println("Rotation: " + fb.getVariable("rotation").getValue());


	}
}
