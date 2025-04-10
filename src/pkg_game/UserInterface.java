package src.pkg_game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.net.URL;


/**
 * This class implements a simple graphical user interface with a 
 * text entry area, a text output area and an optional image.
 * 
 * @author Pierre MATAR
 */
public class UserInterface implements ActionListener
{
    private GameEngine aEngine;
    private JFrame     aMyFrame;
    private JTextField aEntryField;
    private JTextArea  aLog;
    private JLabel     aImage;
    private JButton    aButton; 


    /**
     * Construct a UserInterface. As a parameter, a Game Engine
     * (an object processing and executing the game commands) is
     * needed.
     * 
     * @param gameEngine  The GameEngine object implementing the game logic.
     */
    public UserInterface( final GameEngine pGameEngine )
    {
        this.aEngine = pGameEngine;
        this.createGUI();
    } // UserInterface(.)

    /**
     * Print out some text into the text area.
     * @param pText The text to print.
     */
    public void print( final String pText )
    {
        this.aLog.append( pText );
        this.aLog.setCaretPosition( this.aLog.getDocument().getLength() );
    } // print(.)

    /**
     * Print out some text into the text area, followed by a line break.
     * @param pText The text to print.
     */
    public void println( final String pText )
    {
        this.print( pText + "\n" );
    } // println(.)

    /**
     * Show an image file in the interface.
     * @param pImageName The name of the image file to display.
     */
  
    public void showImage(final String pImageName) {
    String vImagePath = "" + pImageName; 
    URL vImageURL = this.getClass().getClassLoader().getResource(vImagePath);
    if (vImageURL == null) {
        System.out.println("Image not found: " + vImagePath);
    } else {
        ImageIcon vIcon = new ImageIcon(vImageURL);
        Image vImage = vIcon.getImage();
        
        int maxWidth = 500; 
        int width = vImage.getWidth(null);
        int height = vImage.getHeight(null);
        
        if (width > maxWidth) {
            int newHeight = (int) ((double) maxWidth / width * height);
            vImage = vImage.getScaledInstance(maxWidth, newHeight, Image.SCALE_SMOOTH);
        }

        this.aImage.setIcon(new ImageIcon(vImage));
        this.aMyFrame.pack(); 
    }
} //showImage(.)

    /**
     * Enable or disable input in the entry field or button(s).
     * @param pOnOff True to enable input, false to disable it.
     */
    public void enable( final boolean pOnOff )
    {
        this.aEntryField.setEditable( pOnOff ); // enable/disable
        if ( pOnOff ) { // enable
            this.aEntryField.getCaret().setBlinkRate( 500 ); // cursor blink
            this.aEntryField.addActionListener( this ); // reacts to entry
        }
        else { // disable
            this.aEntryField.getCaret().setBlinkRate( 0 ); // cursor won't blink
            this.aEntryField.removeActionListener( this ); // won't react to entry
            this.aButton.setVisible(false);
        }
    } // enable(.)

    /**
     * Set up graphical user interface.
     */
    private void createGUI()
    {
        this.aMyFrame = new JFrame( "FixIt" );  
        this.aEntryField = new JTextField( 34 );

        this.aLog = new JTextArea();
        this.aLog.setEditable( false );
        JScrollPane vListScroller = new JScrollPane( this.aLog );
        vListScroller.setPreferredSize( new Dimension(200, 200) );
        vListScroller.setMinimumSize( new Dimension(100,100) );

        this.aImage = new JLabel();
        this.aButton = new JButton("Look"); 
        this.aButton.setPreferredSize(new Dimension(100, 10)); 

        JPanel vPanel = new JPanel();
        vPanel.setLayout( new BorderLayout() ); // ==> only five places
        vPanel.add( this.aImage, BorderLayout.NORTH );
        vPanel.add( vListScroller, BorderLayout.CENTER );
        vPanel.add( this.aEntryField, BorderLayout.SOUTH );
        vPanel.add( this.aButton, BorderLayout.WEST );



        this.aMyFrame.getContentPane().add( vPanel, BorderLayout.CENTER );

        // add some event listeners to some components
        this.aEntryField.addActionListener( this );
        this.aButton.addActionListener(this);

        // to end program when window is closed
        this.aMyFrame.addWindowListener(
            new WindowAdapter() { // anonymous class
                @Override public void windowClosing(final WindowEvent pE)
                {
                    System.exit(0);
                }
        } );

        this.aMyFrame.pack();
        this.aMyFrame.setVisible( true );
        this.aEntryField.requestFocus();
    } // createGUI()
    
    // REMOVE LOOK BUTTON WHEN TYPING PLAYER NAME IN THE INPUT

    /**
     * Actionlistener interface for entry textfield.
     * @param pE The ActionEvent triggered by the user.
     */
    @Override public void actionPerformed( final ActionEvent pE ) 
    {
        if (pE.getSource() == this.aButton) {
            this.processCommand("look");
            return;
        }
        
        String vInputValue = this.aEntryField.getText();
        
        if (vInputValue.equals("")) return;
            
        this.processCommand(vInputValue); 
        this.aEntryField.setText( "" );

       
      
    } // actionPerformed(.)

    /**
     * A command has been entered in the entry field.  
     * Read the command and do whatever is necessary to process it.
     * @param pString The command string to process.
     */
    private void processCommand(final String pString)
    {
        this.aEngine.interpretCommand( pString );
    } // processCommand()
    
   /** 
    * Close the window object.
    */
    public void closeWindow() {
    this.aMyFrame.dispose();
}
} // UserInterface 
