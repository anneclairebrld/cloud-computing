package app.palettegenerator;

//import net.sf.javaml.clustering.KMeans;
//import net.sf.javaml.core.Dataset;
//import net.sf.javaml.core.DefaultDataset;
//import net.sf.javaml.core.Instance;
//import net.sf.javaml.core.SparseInstance;
//import net.sf.javaml.tools.DatasetTools;
import app.palettegenerator.KMeans;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Generate a color palette of n colors based on the colors found in the image. This uses the Java Machine Learning
 * library (javaml) to perform k-means clustering (generating n clusters of similar colors), and then chooses the colors 
 * from the centroid of each cluster.
 * 
 * @author alexroussos@gmail.com
 *
 */
public class ImageBasedColorPaletteGenerator implements ColorPaletteGenerator {

	private final BufferedImage image;
	
	public ImageBasedColorPaletteGenerator(BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * Takes a random sampling of pixels from the image, clusters them based on color (as many clusters as desired colors)
	 * and then generates a palate based on those clusters.
	 */
	@Override
	public List<Color> generateColorPalette(int numColors) {
        List<Color> colors = new ArrayList<Color>();

    	KMeans kmeans = new KMeans();
    	BufferedImage result = kmeans.calculate(image, numColors);

		KMeans.Cluster[] clusters = kmeans.clusters;


    	for (KMeans.Cluster cluster : clusters) {
    		Color c = new Color(cluster.getRGB());
    		colors.add(c);
    	}
    	return colors;
	}
}

