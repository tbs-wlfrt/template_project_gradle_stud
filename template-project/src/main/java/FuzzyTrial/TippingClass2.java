package FuzzyTrial;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class TippingClass2 {
	public static void main(String[] args) throws Exception {
		String filename = "C:\\Users\\Burak\\OneDrive - Universiteit Antwerpen\\Images\\Hichem Boot\\7_9_21\\template_project_gradlecopie\\template-project\\src\\main\\java\\FuzzyTrial\\tipper2.fcl";
		FIS fis = FIS.load(filename, false);

		if (fis == null) {
			System.err.println("Can't load file: '" + filename + "'");
			System.exit(1);
		}

		// Get default function block
		FunctionBlock fb = fis.getFunctionBlock(null);

		// Set inputs
		fb.setVariable("sensor", 6.61);
		fb.setVariable("time", 4);



		// Evaluate
		fb.evaluate();





		System.out.println(fb.getVariable("sensor").getMembership("cold"));
		System.out.println(fb.getVariable("sensor").getMembership("warm"));
		System.out.println(fb.getVariable("sensor").getMembership("hot"));

		System.out.println();

		System.out.println(fb.getVariable("time").getMembership("short"));
		System.out.println(fb.getVariable("time").getMembership("long"));
		System.out.println(fb.getVariable("time").getMembership("extreme"));


		// Show output variable's chart
		fb.getVariable("speed").defuzzify();



		// Get default function block


		JFuzzyChart.get().chart(fb);

		// Print ruleSet
	//	System.out.println(fb);
		System.out.println("Speed: " + fb.getVariable("speed").getValue() +  " " +  fb.getVariable("speed").getMembership("low") + " " +  fb.getVariable("speed").getMembership("average") + " " + fb.getVariable("speed").getMembership("fast"));

	}

}
