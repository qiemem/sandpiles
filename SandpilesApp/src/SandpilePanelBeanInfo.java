/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.beans.*;

/**
 * @author headb
 */
public class SandpilePanelBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( SandpilePanel.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor

    // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor


    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_controlState = 0;
    private static final int PROPERTY_gridCols = 1;
    private static final int PROPERTY_gridRows = 2;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[3];
    
        try {
            properties[PROPERTY_controlState] = new PropertyDescriptor ( "controlState", SandpilePanel.class, null, "setControlState" ); // NOI18N
            properties[PROPERTY_gridCols] = new PropertyDescriptor ( "gridCols", SandpilePanel.class, null, "setGridCols" ); // NOI18N
            properties[PROPERTY_gridRows] = new PropertyDescriptor ( "gridRows", SandpilePanel.class, null, "setGridRows" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties

    // Here you can add code for customizing the properties array.

        return properties;     }//GEN-LAST:Properties

    // EventSet identifiers//GEN-FIRST:Events

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[0];//GEN-HEADEREND:Events

    // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events

    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_actionPerformed0 = 0;
    private static final int METHOD_addEdge1 = 1;
    private static final int METHOD_addEdgeMouseListener2 = 2;
    private static final int METHOD_addSand3 = 3;
    private static final int METHOD_addSandEverywhere4 = 4;
    private static final int METHOD_addSandMouseListener5 = 5;
    private static final int METHOD_addUndiEdgeMouseListener6 = 6;
    private static final int METHOD_addVertex7 = 7;
    private static final int METHOD_addVertMouseListener8 = 8;
    private static final int METHOD_clearSand9 = 9;
    private static final int METHOD_delEdge10 = 10;
    private static final int METHOD_delEdgeMouseListener11 = 11;
    private static final int METHOD_delSandMouseListener12 = 12;
    private static final int METHOD_delUndiEdgeMouseListener13 = 13;
    private static final int METHOD_delVertex14 = 14;
    private static final int METHOD_delVertMouseListener15 = 15;
    private static final int METHOD_initWithSandpileGraph16 = 16;
    private static final int METHOD_makeGrid17 = 17;
    private static final int METHOD_maxStableConfig18 = 18;
    private static final int METHOD_paintComponent19 = 19;
    private static final int METHOD_touchingVertex20 = 20;
    private static final int METHOD_update21 = 21;
    private static final int METHOD_updateSandCounts22 = 22;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[23];
    
        try {
            methods[METHOD_actionPerformed0] = new MethodDescriptor(SandpilePanel.class.getMethod("actionPerformed", new Class[] {java.awt.event.ActionEvent.class})); // NOI18N
            methods[METHOD_actionPerformed0].setDisplayName ( "" );
            methods[METHOD_addEdge1] = new MethodDescriptor(SandpilePanel.class.getMethod("addEdge", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_addEdge1].setDisplayName ( "" );
            methods[METHOD_addEdgeMouseListener2] = new MethodDescriptor(SandpilePanel.class.getMethod("addEdgeMouseListener", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_addEdgeMouseListener2].setDisplayName ( "" );
            methods[METHOD_addSand3] = new MethodDescriptor(SandpilePanel.class.getMethod("addSand", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_addSand3].setDisplayName ( "" );
            methods[METHOD_addSandEverywhere4] = new MethodDescriptor(SandpilePanel.class.getMethod("addSandEverywhere", new Class[] {int.class})); // NOI18N
            methods[METHOD_addSandEverywhere4].setDisplayName ( "" );
            methods[METHOD_addSandMouseListener5] = new MethodDescriptor(SandpilePanel.class.getMethod("addSandMouseListener", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_addSandMouseListener5].setDisplayName ( "" );
            methods[METHOD_addUndiEdgeMouseListener6] = new MethodDescriptor(SandpilePanel.class.getMethod("addUndiEdgeMouseListener", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_addUndiEdgeMouseListener6].setDisplayName ( "" );
            methods[METHOD_addVertex7] = new MethodDescriptor(SandpilePanel.class.getMethod("addVertex", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_addVertex7].setDisplayName ( "" );
            methods[METHOD_addVertMouseListener8] = new MethodDescriptor(SandpilePanel.class.getMethod("addVertMouseListener", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_addVertMouseListener8].setDisplayName ( "" );
            methods[METHOD_clearSand9] = new MethodDescriptor(SandpilePanel.class.getMethod("clearSand", new Class[] {})); // NOI18N
            methods[METHOD_clearSand9].setDisplayName ( "" );
            methods[METHOD_delEdge10] = new MethodDescriptor(SandpilePanel.class.getMethod("delEdge", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_delEdge10].setDisplayName ( "" );
            methods[METHOD_delEdgeMouseListener11] = new MethodDescriptor(SandpilePanel.class.getMethod("delEdgeMouseListener", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_delEdgeMouseListener11].setDisplayName ( "" );
            methods[METHOD_delSandMouseListener12] = new MethodDescriptor(SandpilePanel.class.getMethod("delSandMouseListener", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_delSandMouseListener12].setDisplayName ( "" );
            methods[METHOD_delUndiEdgeMouseListener13] = new MethodDescriptor(SandpilePanel.class.getMethod("delUndiEdgeMouseListener", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_delUndiEdgeMouseListener13].setDisplayName ( "" );
            methods[METHOD_delVertex14] = new MethodDescriptor(SandpilePanel.class.getMethod("delVertex", new Class[] {int.class})); // NOI18N
            methods[METHOD_delVertex14].setDisplayName ( "" );
            methods[METHOD_delVertMouseListener15] = new MethodDescriptor(SandpilePanel.class.getMethod("delVertMouseListener", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_delVertMouseListener15].setDisplayName ( "" );
            methods[METHOD_initWithSandpileGraph16] = new MethodDescriptor(SandpilePanel.class.getMethod("initWithSandpileGraph", new Class[] {SandpileGraph.class})); // NOI18N
            methods[METHOD_initWithSandpileGraph16].setDisplayName ( "" );
            methods[METHOD_makeGrid17] = new MethodDescriptor(SandpilePanel.class.getMethod("makeGrid", new Class[] {int.class, int.class, int.class, int.class})); // NOI18N
            methods[METHOD_makeGrid17].setDisplayName ( "" );
            methods[METHOD_maxStableConfig18] = new MethodDescriptor(SandpilePanel.class.getMethod("maxStableConfig", new Class[] {})); // NOI18N
            methods[METHOD_maxStableConfig18].setDisplayName ( "" );
            methods[METHOD_paintComponent19] = new MethodDescriptor(SandpilePanel.class.getMethod("paintComponent", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_paintComponent19].setDisplayName ( "" );
            methods[METHOD_touchingVertex20] = new MethodDescriptor(SandpilePanel.class.getMethod("touchingVertex", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_touchingVertex20].setDisplayName ( "" );
            methods[METHOD_update21] = new MethodDescriptor(SandpilePanel.class.getMethod("update", new Class[] {})); // NOI18N
            methods[METHOD_update21].setDisplayName ( "" );
            methods[METHOD_updateSandCounts22] = new MethodDescriptor(SandpilePanel.class.getMethod("updateSandCounts", new Class[] {})); // NOI18N
            methods[METHOD_updateSandCounts22].setDisplayName ( "" );
        }
        catch( Exception e) {}//GEN-HEADEREND:Methods

    // Here you can add code for customizing the methods array.
    
        return methods;     }//GEN-LAST:Methods

    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons

    private static final int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;//GEN-END:Idx

    
//GEN-FIRST:Superclass

    // Here you can add code for customizing the Superclass BeanInfo.

//GEN-LAST:Superclass
	
    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     * 
     * @return BeanDescriptor describing the editable
     * properties of this bean.  May return null if the
     * information should be obtained by automatic analysis.
     */
    public BeanDescriptor getBeanDescriptor() {
	return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     * 
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean.  May return null if the
     * information should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will
     * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
     * A client of getPropertyDescriptors can use "instanceof" to check
     * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
	return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     * 
     * @return  An array of EventSetDescriptors describing the kinds of 
     * events fired by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public EventSetDescriptor[] getEventSetDescriptors() {
	return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     * 
     * @return  An array of MethodDescriptors describing the methods 
     * implemented by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public MethodDescriptor[] getMethodDescriptors() {
	return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are 
     * customizing the bean.
     * @return  Index of default property in the PropertyDescriptor array
     * 		returned by getPropertyDescriptors.
     * <P>	Returns -1 if there is no default property.
     */
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will
     * mostly commonly be used by human's when using the bean. 
     * @return Index of default event in the EventSetDescriptor array
     *		returned by getEventSetDescriptors.
     * <P>	Returns -1 if there is no default event.
     */
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to
     * represent the bean in toolboxes, toolbars, etc.   Icon images
     * will typically be GIFs, but may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from
     * this method.
     * <p>
     * There are four possible flavors of icons (16x16 color,
     * 32x32 color, 16x16 mono, 32x32 mono).  If a bean choses to only
     * support a single icon we recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background
     * so they can be rendered onto an existing background.
     *
     * @param  iconKind  The kind of icon requested.  This should be
     *    one of the constant values ICON_COLOR_16x16, ICON_COLOR_32x32, 
     *    ICON_MONO_16x16, or ICON_MONO_32x32.
     * @return  An image object representing the requested icon.  May
     *    return null if no suitable icon is available.
     */
    public java.awt.Image getIcon(int iconKind) {
        switch ( iconKind ) {
        case ICON_COLOR_16x16:
            if ( iconNameC16 == null )
                return null;
            else {
                if( iconColor16 == null )
                    iconColor16 = loadImage( iconNameC16 );
                return iconColor16;
            }
        case ICON_COLOR_32x32:
            if ( iconNameC32 == null )
                return null;
            else {
                if( iconColor32 == null )
                    iconColor32 = loadImage( iconNameC32 );
                return iconColor32;
            }
        case ICON_MONO_16x16:
            if ( iconNameM16 == null )
                return null;
            else {
                if( iconMono16 == null )
                    iconMono16 = loadImage( iconNameM16 );
                return iconMono16;
            }
        case ICON_MONO_32x32:
            if ( iconNameM32 == null )
                return null;
            else {
                if( iconMono32 == null )
                    iconMono32 = loadImage( iconNameM32 );
                return iconMono32;
            }
	default: return null;
        }
    }

}

