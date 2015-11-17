package cz.boosik.boosadminforminecraft.app.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

/**
 * Custom number picker allowing setting the min and max values in the layout xml file
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class CustomNumberPicker extends NumberPicker {

    /**
     * Default constructor
     *
     * @param context Context of number picker
     */
    public CustomNumberPicker(Context context) {
        super(context);
    }

    /**
     * Constructor with attributes
     *
     * @param context Context of number picker
     * @param attrs   Target attributes
     */
    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributeSet(attrs);
    }

    /**
     * Constructor with attributes and style
     *
     * @param context  Context of number picker
     * @param attrs    Target attributes
     * @param defStyle Target style
     */
    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttributeSet(attrs);
    }

    /**
     * Sets the min/max values according to the layout xml file
     *
     * @param attrs Attributes
     */
    private void processAttributeSet(AttributeSet attrs) {
        this.setMinValue(attrs.getAttributeIntValue(null, "min", 0));
        this.setMaxValue(attrs.getAttributeIntValue(null, "max", 0));
    }
}
