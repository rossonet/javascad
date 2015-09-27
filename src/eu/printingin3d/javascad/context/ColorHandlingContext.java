package eu.printingin3d.javascad.context;

import java.awt.Color;

/**
 * Base class for all classes which implement IColorGenerationContext interface.
 * Implements the logic of the getColor() method. 
 * @author Ivan
 *
 */
public class ColorHandlingContext implements IColorGenerationContext {
	public static final IColorGenerationContext DEFAULT = new ColorHandlingContext(null, null, 0);
	
	protected final ITagColors tagColors;
	protected final IColorGenerationContext parent;
	protected final int tag;
	
	public ColorHandlingContext(ITagColors tagColors) {
		this(tagColors, null, 0);
	}
	
	protected ColorHandlingContext(ITagColors tagColors, IColorGenerationContext parent, int tag) {
		this.tagColors = tagColors;
		this.parent = parent;
		this.tag = tag;
	}

	@Override
	public Color getColor() {
		if (tag!=0 && tagColors!=null) {
			Color result = tagColors.getColor(tag);
			if (result!=null) {
				return result;
			}
		}
		return parent==null ? null : parent.getColor();
	}
	
	@Override
	public IColorGenerationContext applyTag(int tag) {
		if ((tag==this.tag) || tag==0) {
			return this;
		}
		
		return new ColorHandlingContext(tagColors, this, tag);
	}

}