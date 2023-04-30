/*
 * Name: Randall Rinehart
 * Assignment: Emoji Contest
 * Date Completed: April 30th, 2023
 * Time Taken: Many, many hours...I didn't keep track
 * Program Description: This program is in 2 main parts: drawing the emoji, and drawing the computer
 
 :: EMOJI ::
 
  - Head: The head is just a simple tan ellipse.
  
  - Hair: The hair consists of swirly lines representing curly hair that are drawn above the head (by the forehead) and below the head (to make the long locks).
  
  - Eyes: The shape of each eye comes from two mirrored parabolas, and then two circles for the pupil.
  
  - Eyebrows: The shape of the eyebrows are defined by a quadratic Bezier curve. Using the derivative of that curve, thin lines for hair are drawn randomly along it, with their angle normal to the curve, and some slight variation to make it more bushy.
  
  - Glasses: The glasses are made by 2 rounded rectangles (with corner radii that relatively match my glasses), connected by a quadratic arc, and then simple lines forming the glasses arms.
  
  - Nose: The nose is a simple outline of a cubic Bezier curve, giving it a shape that can be more specific.
  
  - Mouth: The mouth is just a sector of an arc making a smirking shape.
 
 
 
 :: COMPUTER ::
 
  - Frame/Screen/Touchpad: These are just rounded rectangles.
  
  - Program: On the computer screen, there is a randomly generated program. This uses an algorithm I designed called LipsumScript, which randomly generates a Java-like program but with gibberish names for variables and functions. It supports declarations, reassignents, function calls, comments, if-statements, and for- and while-loops. It is also colored like an IDE, using a Word class I wrote that allows keywords to be made into specific colors.
  
  - Keyboard: The keyboard is a 2D array of keys, each with a width and height that roughly match the keyboard on my computer.
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import java.lang.Math;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import java.util.ArrayList;

// TODO: Fix hair color so its less colorful and more just shades of light-dark brown?

public class EmojiFace extends Application {
  // Global variables
  
  // Window size (and canvas size)
  static double windowWidth = 650;
  static double windowHeight = 650;
  
  // Head variables
  static double headX = windowWidth * 0.38;
  static double headY = windowHeight * 0.375;
  static double headWidth = 250;
  static double headHeight = 350;
  
  // Eye variables
  static double eyeSpacing = 115;
  static double eye1X = -eyeSpacing / 2;
  static double eye2X = eyeSpacing / 2;
  static double eyeY = -15;
  static double eyeWidth = 62;
  static double eyeHeight = 55;
  static double pupilSize = 24;
  static double pupilX = 0; // How much to the side the pupils are looking
  static double eyeDotSize = 11;
  
  // Eyebrow variables
  public static double eyebrowThickness = 3.5;
  public static double eyebrowY = -36;
  public static double eyebrowWidth = 80;
  public static double eyebrowHeight = 20;
  public static double eyebrowSkew = 0.35; // The ratio from 0-1 where the eyebrow bends towards
  
  // Glasses variables
  static double glassesThickness = 5; // Line width of the frame
  static double lensWidth = 75 * 1.15;
  static double lensHeight = 50 * 1.15;
  
  // Mouth variables
  static double mouthY = 90;
  static double mouthWidth = 105;
  static double mouthHeight = 70;
  static double mouthThickness = 5.8;
  
  // Nose variables
  static double noseY = 5;
  static double noseHeight = 65;
  static double noseWidth = 50;
  static double noseLineWidth = 3;
  
  // Computer variables
  static double computerX = windowWidth * 0.69;
  static double computerY = windowHeight * 0.68;
  static double computerWidth = 280; // The width and height of the ENTIRE computer, different from the screen size
  static double computerHeight = 320;
  static double computerScreenPadding = 7;
  static double keyboardPaddingLR = 10; // The left/right padding on the keyboard
  static double keyboardPaddingT = 0; // The top padding on the keyboard
  static double keyboardPaddingB = 45; // The bottom padding on the keyboard
  static double computerHalfwayPoint = 0.58; // The ratio along the computer where it goes from screen to keyboard
  static double[] computerFrameCornerRadii = {12, 12, 12, 12};
  static double[] computerScreenCornerRadii = {5, 5, 5, 5};
  static double[] keyCornerRadii = {2, 2, 2, 2};
  static double keyPadding = 1.5;
  static double touchpadWidth = 65;
  static double touchpadPaddingY = 5; // Distance top/bottom between the keyboard, touchpad, and bottom of computer
  static double[] touchpadCornerRadii = {5, 5, 5, 5};
  static double touchpadOutlineThickness = 1.3;
  
  // Define palette colors
  static Color skinColor = Color.rgb(255, 219, 172);
  static Color hairColor = Color.rgb(92, 40, 3);
  static Color eyeColor = Color.rgb(77, 33, 2);
  static Color eyeDotColor = Color.hsb(25, 0.95, 0.055);
  static Color glassesColor = Color.hsb(25, 0.96, 0.09);
  static Color computerFrameColor = Color.hsb(0, 0, 0.5);
  static Color computerScreenColor = Color.BLACK;
  static Color keyColor = Color.hsb(0, 0, 0.28);
  static Color touchpadFillColor = Color.hsb(0, 0, 0.42);
  static Color touchpadOutlineColor = Color.hsb(0, 0, 0.2);
  static Color eyebrowColor = Color.hsb(25, 0.94, 0.13);
  static Color lineNumberBackgroundColor = Color.hsb(0, 0, 0.1);
  
  // Computer screen font variables
  static double screenFontSize = 10;
  static Font screenFont = Font.font("monospace", FontWeight.BOLD, FontPosture.REGULAR, screenFontSize);
  static Font lineNumberFont = Font.font("monospace", FontWeight.NORMAL, FontPosture.REGULAR, screenFontSize);
  static double screenFontPadding = 5;
  
  // JavaFX variables
  // I decided to go with the Canvas API instead of using Scene Shapes, since it feels much more intuitive for me
  static Canvas canvas = new Canvas(windowWidth, windowHeight);
  static Group root = new Group();
  static Scene scene = new Scene(root, windowWidth, windowHeight);
  static GraphicsContext ctx = canvas.getGraphicsContext2D(); // Create the canvas rendering context
  
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Minor global setting changes
    ctx.setLineCap(StrokeLineCap.ROUND);
    
    // Draw the emoji
    drawEmoji();
    drawComputer();
    
    // Finalize and open window
    root.getChildren().add(canvas);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Emoji Face");
    primaryStage.show();
  }
  
  
  
  public static void drawEmoji() {
    // Draws all of the components of the face, in order
    
    drawLowerHair();
    
    drawHead();
    
    drawEyes();
    
    drawEyebrows();
    
    drawNose();
    
    drawUpperHair();
    
    drawGlasses();
    
    drawMouth();
  }
  
  
  
  public static void drawComputer() {
    // Draws all of the components of the computer, in order
    
    drawComputerFrame();
    
    drawComputerScreen();
    
    drawKeyboard();
    
    drawTouchpad();
  }
  
  
  
  public static void drawComputerFrame() {
    // Draws the "background" geometry of the computer (basically everything under the screen and keyboard)
    
    // The computer frame is just a gray rounded rectangle
    
    ctx.setFill(computerFrameColor);
    drawRoundedRect(computerX - (computerWidth / 2), computerY - (computerHeight / 2), computerWidth, computerHeight, computerFrameCornerRadii, true);
  }
  
  
  
  public static void drawComputerScreen() {
    // Draw the back of the screen
    
    // The computer screen is a black rounded rectangle with text on it
    
    double x = computerX - (computerWidth / 2) + computerScreenPadding;
    double y = computerY - (computerHeight / 2) + computerScreenPadding;
    double width = computerWidth - (computerScreenPadding * 2);
    double height = (computerHeight * computerHalfwayPoint) - (computerScreenPadding * 2);
    
    double charWidth = (screenFontSize / 2) * 1.2; // For some reason, I need to multiply this by 1.2 to make it match the normal character spacing. Go figure.
    double charHeight = screenFontSize * 1.2;
    
    // Draw black background
    ctx.setFill(computerScreenColor);
    drawRoundedRect(
      x, y,
      width, height,
      computerScreenCornerRadii, // radii
      true // filled
    );
    
    // Draw background for line numbers
    ctx.setFill(lineNumberBackgroundColor);
    drawRoundedRect(
      x, y,
      charWidth * 3.35, height,
      new double[] {computerScreenCornerRadii[0], 0, 0, computerScreenCornerRadii[3]}, // radii
      true // filled
    );
    
    // Draw the text on the screen
    
    double textX = computerX - (computerWidth / 2) + computerScreenPadding + screenFontPadding;
    double textY = computerY - (computerHeight / 2) + computerScreenPadding + screenFontPadding;
    
    // Clip text into bounding box of computer screen
    ctx.save(); // Save context before clip
    ctx.beginPath();
    ctx.rect(x, y, width, height);
    ctx.clip();
    
    
    
    ctx.setFont(screenFont);
    
    // Create a LipsumScript program to draw on the screen
    ArrayList<Word> program = generateLipsumScript();
    
    final int MAX_CHARS = 42; // 42 chars fit on a single line
    
    int currLine = 1; // This is the variable that keeps track of the line number to be drawn (it's different than the y-value since sometimes lines wrap over, and we don't want to increment the line number there)
    
    int charX = 0; // Number of chars in the x-direction currently
    int charY = 0; // Number of chars in the y-direction currently
    
    for (int i = 0; i < program.size(); i++) { // Step through all Words in the program:
      if (charX == 0) { // Add line numbers
        // Generate the line number and align it to the right if its too few digits
        String lineNum = Integer.toString(currLine);
        while (lineNum.length() < 2) lineNum = " " + lineNum;
        double __x = textX; // The position of the line number to be drawn
        double __y = textY + ((charY + 0.5) * charHeight);
        
        // Draw the line number
        ctx.setFont(lineNumberFont);
        (Word.make(lineNum, Word.lineNumberColor)).draw(__x, __y);
        ctx.setFont(screenFont);
        
        // Move everything over so we don't overlap the line number
        charX += 3;
      }
      
      // Wrap word onto next line if we're about to overlap the edge of the screen
      if (charX + program.get(i).content.length() >= MAX_CHARS && program.get(i).content != "\n") {
        charX = 3; // This makes sure we won't draw the next line number, and go ahead and take into account the fact it needs to be moved over
        charY++;
      }
      
      // The actual (x, y) position on the canvas
      double _x = charX * charWidth;
      double _y = (charY + 0.5) * charHeight;
      
      // Draw the text
      program.get(i).draw(textX + _x, textY + _y);
      
      // Move the next word over by the appropriate distance
      charX += program.get(i).content.length();
      
      // Scoot everything down if we hit a newline
      if (program.get(i).content.equals("\n")) {
        charX = 0;
        charY++;
        currLine++;
      }
    }
    
    // Remove clip
    ctx.restore();
  }
  
  
  
  static String[] vowels = {"a", "e", "i", "o", "u", "oo", "ie", "ou", "ai"};
  static String[] consonants = {"b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z"};
  static String[] foobar = {"foo", "foobar", "barfoo", "too", "goo", "boo", "fig", "app", "mop", "brig", "frig", "gig", "bar", "zar", "mat", "lop", "baz", "qux", "quux", "corge", "grault", "garply", "waldo", "fred", "plugh", "xyzzy", "thud", "blarg", "chomp", "snarf", "snark", "snafu", "wibble", "wobble", "wubble", "flim", "flam", "flem", "spow", "spat", "splick", "splonk", "thwack", "thump", "bop", "biff", "bam", "zam", "wut", "beep", "blip", "bomp", "feeb", "thup", "thack", "spoo", "faz", "jaz", "taz", "ping", "pong", "pang", "zing", "zong", "zang", "wibb", "wobb", "wubb", "nark", "bot", "jot", "pox", "jox", "flob", "glop", "pow", "wham", "zap", "zoom", "boop", "bloop", "sup", "burp", "yow", "zoink", "yike", "jeep", "golly", "egad", "gad", "zook", "darn", "drat", "blast", "rat", "shuck", "tar", "fiddle", "fid", "crik", "shee", "hooey", "hoop", "lag", "kerf", "hull", "hocus", "cad", "prest", "zest", "alak", "shaz", "sim", "gib", "toto", "blab", "blob", "yill", "fubar", "quib"}; // This is a list of metasyntactic variable names, along with some I (and ChatGPT) came up with, to make pronounceable names for variables and methods
  public static String generateGibberish() {
    // Returns a random foobar word
    
    return foobar[(int) (Math.random() * foobar.length)];
  }
  
  
  static String[] typeWords = {"int", "long", "double", "float", "char", "boolean", "String"};
  public static ArrayList<Word> generateLipsumScript() {
    // Generates an array of Words representing a randomly generated gibberish Java-like program, which I call "LoremScript"
    
    int numLines = 14;
    
    ArrayList<Word> program = new ArrayList<Word>();
    
    boolean indented = false; // Whether the current line is currently indented or not
    
    int lastExpressionType = -1; // The expression type of the previous line
    
    for (int i = 0; i < numLines; i++) {
      // These lines ensures that every nested structure has at least one statement in it (and that it can't begin with a comment)
      boolean prohibitNesting = lastExpressionType == 3 || i >= numLines - 2;
      int[] possibleExpressionTypes = prohibitNesting ? new int[] {0, 1} : new int[] {0, 1, 2, 3};
      
      // Choose a random type of expression
      int expressionType = possibleExpressionTypes[(int) (Math.random() * possibleExpressionTypes.length)];
      
      if (i == numLines - 1 && indented) expressionType = 3; // Make sure we close any open nested statements at the end of the program
      
      switch (expressionType) {
        case 0: // Variable declaration
          if (indented) program.add(Word.make("  ")); // Indent if necessary
          
          addDeclarationToProgram(program);
          break;
        
        case 1: // Function call
          if (indented) program.add(Word.make("  ")); // Indent if necessary
          
          addFunctionToProgram(program);
          break;
        
        case 2: // Comment
          if (indented) program.add(Word.make("  ")); // Indent if necessary
          
          addCommentToProgram(program);
          break;
        
        case 3: // If/for/while
          if (indented) { // Toggle indentation
            indented = false;
            program.add(Word.make("}")); // Close brackets
          }
          else {
            addNestToProgram(program);
            indented = true;
          }
          break;
      }
      
      program.add(Word.make("\n")); // Add newline for next expression
      
      lastExpressionType = expressionType;
    }
    
    return program;
  }
  
  
  
  public static String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
  public static String[] comparisonOperators = {"==", ">", "<", ">=", "<=", "!="};
  public static void addNestToProgram(ArrayList<Word> program) {
    // Adds a nested statement (if / for / while) to a LipsumScript program
    
    int nestType = (int) (Math.random() * 3); // 0, 1, or 2
    
    switch (nestType) {
      case 0: // if
        program.add(Word.make("if "));
        break;
        
      case 1: // while
        program.add(Word.make("while "));
        break;
      
      case 2: // for
        program.add(Word.make("for "));
        break;
    }
    
    program.add(Word.make("("));
    
    if (nestType == 2) { // for loops have a special syntax
      Word iterable = Word.make(alphabet[(int) (Math.random() * alphabet.length)]); // Random single letter
      Word start = randomDataFromType(0);
      Word end = randomDataFromType(0);
      String comp = comparisonOperators[(int) (Math.random() * comparisonOperators.length)]; // comparison
      Word perLoop = Word.make(Math.random() < 0.5 ? "++" : "--");
      
      program.add(Word.make("int "));           // int
      program.add(iterable);                    // i
      program.add(Word.make(" = "));            // =
      program.add(start);                       // 0
      program.add(Word.make("; "));             // ;
      program.add(iterable);                    // i
      program.add(Word.make(" " + comp + " ")); // <
      program.add(end);                         // 5
      program.add(Word.make("; "));             // ;
      program.add(iterable);                    // i
      program.add(perLoop);                     // ++
    }
    else { // if statements and while loops both just use Boolean conditions
      ArrayList<Word> condition = makeRandomCondition();
      
      for (int i = 0; i < condition.size(); i++) program.add(condition.get(i));
    }
    
    // Close everything off
    program.add(Word.make(")"));
    program.add(Word.make(" {"));
  }
  
  
  
  public static ArrayList<Word> makeRandomCondition() {
    // Generates a random Boolean expression
    
    // The number of expressions, separated by && or ||
    int numParts = Math.random() < 0.7 ? 1 : 2; // 70% chance to have one part
    
    // The condition has multiple parts so we need to return an array of Words, since the parts are different colors
    ArrayList<Word> condition = new ArrayList<Word>();
    
    for (int i = 0; i < numParts; i++) {
      Word var1 = Word.make(generateGibberish() + " "); // The first variable in the comparison
      Word var2 = Word.make(generateGibberish()); // The second variable in the comparison
      
      int[] possibleTypes = {0, 2, 4, 5, 6}; // Exclude longs and floats since they're ugly
      int chosenType = possibleTypes[(int) (Math.random() * possibleTypes.length)]; // Choose a random data type
      
      if (Math.random() < 0.5) { // 50% chance to make var2 a literal (instead of a variable)
        var2 = randomDataFromType(chosenType);
      }
      
      String comp = comparisonOperators[(int) (Math.random() * comparisonOperators.length)]; // Choose a random comparator
      if (chosenType == 6 || chosenType == 4) { // Strings and chars don't have > or <
        comp = Math.random() < 0.5 ? "==" : "!="; // Randomly choose either equals or not equals
      }
      
      if (chosenType == 5) { // Booleans get special logic since there's no comparision operator
        Word sign = Word.make(Math.random() < 0.5 ? "" : "!"); // Either negated or not
        
        var1 = Word.make(generateGibberish()); // Remove trailing space
        
        condition.add(sign); // Whether or not there's a !
        condition.add(var1);
      }
      else { // If it's not a Boolean, we just do var1 == var2
        condition.add(var1);
        condition.add(Word.make(comp + " "));
        condition.add(var2);
      }
      
      // Add AND or OR operator between conditions, if there are multiple
      if (numParts > 1 && i != numParts - 1) { // Don't add anything to the final part
        String boolOperator = Math.random() < 0.5 ? "&&" : "||"; // Choose a random operation
        condition.add(Word.make(" " + boolOperator + " "));
      }
    }
    
    return condition;
  }
  
  
  
  public static void addCommentToProgram(ArrayList<Word> program) {
    // Adds a random comment to a LipsumScript program
    
    program.add(Word.make("// ", Word.commentColor));
    
    int numWords = (int) Math.round(randBetween(randBetween(1, 2), 6)); // How many words are in the comment
    
    for (int i = 0; i < numWords; i++) {
      String gib = generateGibberish();
      if (i == 0) gib = gib.substring(0, 1).toUpperCase() + gib.substring(1); // The first word is capitalized
      
      // Add the word to the program (without a trailing space if its at the end)
      program.add(Word.make(gib + (i == numWords - 1 ? "" : " "), Word.commentColor));
    }
  }
  
  
  
  public static Word randomDataFromType(int dataType) {
    // Returns a Word representing a literal value from the given data type
    
    // ["int", "long", "double", "float", "char", "boolean", "String"]
    
    Word value = Word.make(" ");
    
    switch (dataType) {
      case 0: // int
        // Random number between 1-100
        value = Word.make(Integer.toString((int) Math.round(randBetween(0, 100))), Word.numberColor);
        
        break;
      
      case 1: // long
        // Random number between 1-100
        value = Word.make(Integer.toString((int) Math.round(randBetween(0, 100))) + "L", Word.numberColor);
        
        break;
      
      case 2: // double
        // Random number between 1-100 (up to 3 decimal places)
        int precision1 = (int) Math.round(randBetween(1, 3)); // Number of decimal places
        value = Word.make(String.format("%." + precision1 + "f", randBetween(0, 100)), Word.numberColor);
        
        break;
      
      case 3: // float
        // Random number between 1-100 (up to 3 decimal places)
        int precision2 = (int) Math.round(randBetween(1, 3)); // Number of decimal places
        value = Word.make(String.format("%." + precision2 + "f", randBetween(0, 100)) + "f", Word.numberColor);
        
        break;
      
      case 4: // char
        // Choose a random ASCII character
        int asciiValue = (int) Math.round(randBetween(32, 126));
        String ch = Character.toString(asciiValue);
        
        if (ch.equals("\\") || ch.equals("'")) ch = "\\" + ch; // Since ''' and '\' aren't valid syntax, we escape these cases with a backslash
        
        value = Word.make("'" +  + "'", Word.stringColor); // Add the character in between single quotes
        
        break;
      
      case 5: // boolean
        // Simply either true or false
        
        value = Word.make(Math.random() < 0.5 ? "true" : "false");
        
        break;
      
      case 6: // String
        if (Math.random() < 0.25) { // 25% chance to be random ASCII characters
          int numChars = (int) Math.round(randBetween(2, 10)); // Length of the string
          
          String str = "";
          
          for (int q = 0; q < numChars; q++) {
            String ch = Character.toString((int) Math.round(randBetween(32, 126))); // Choose a random ASCII character
            
            if (ch.equals("\"") || ch.equals("\\")) { // Escape double-quotes and backslashes
              str += "\\";
              numChars--; // Since we added to the visual length of the string, decrease the limit by 1 so it stays the same length
            }
            
            str += ch; // Add the character to the string
          }
          
          value = Word.make("\"" + str + "\"", Word.stringColor); // Add the string in between double quotes
        }
        else { // If we're not doing random characters, use foobar words
          int numWords = (int) Math.round(randBetween(1, 2)); // Either 1 or 2 words long
          
          String str = "";
          
          for (int q = 0; q < numWords; q++) {
            str += foobar[(int) (Math.random() * foobar.length)]; // Choose a random foobar word
            if (q != numWords - 1) str += " "; // Add a space if it's we're not the final word
          }
          
          value = Word.make("\"" + str + "\"", Word.stringColor);
        }
        
        break;
    }
    
    return value;
  }
  
  
  
  public static void addDeclarationToProgram(ArrayList<Word> program) {
    // Given a LipsumScript program, it adds an expression containing a random variable declaration
    
    // As an example, consider "int foo = 5;"
    
    int dataType = (int) (Math.random() * typeWords.length); // What kind of data type it is
    
    // 70% chance to be a declaration; 30% chance to just be a reassignment
    if (Math.random() < 0.7) program.add(Word.make(typeWords[dataType] + " ")); // "int "
    program.add(Word.make(generateGibberish() + " "));                          // "foo "
    program.add(Word.make("= "));                                               // "= "
    
    Word value = randomDataFromType(dataType); // The value of the declaration
    
    if (dataType == 0 || dataType == 1 || dataType == 2 || dataType == 3) { // Number types:
      if (Math.random() < 0.5) program.add(Word.make("-")); // 50% chance to be negative
    }
    
    program.add(value); // 5
    
    program.add(Word.make(";")); // ;
  }
  
  
  
  public static void addFunctionToProgram(ArrayList<Word> program) {
    // Given a LipsumScript program, it adds an expression containing a random function call
    
    // foo.bar(faz, too, nom);
    
    boolean doChain = Math.random() < 0.25; // 25% chance to chain: foo.bar()
    int numArgs = Math.random() < 0.4 ? 0 : ((int) Math.round(randBetween(1, randBetween(2, 3)))); // 40% chance for no arguments, otherwise 1-3 args (with 3 less likely)
    
    String name1 = generateGibberish(); // foo
    String name2 = generateGibberish(); // bar
    
    program.add(Word.make(name1));
    if (doChain) { // Add the chaining if it was chosen
      program.add(Word.make("."));
      program.add(Word.make(name2));
    }
    
    program.add(Word.make("("));
    for (int i = 0; i < numArgs; i++) {
      boolean argIsLiteral = Math.random() < 0.5; // 50% chance to make the argument a literal vs. variable
      if (argIsLiteral) {
        int[] possibleTypes = {0, 2, 5, 6}; // Exclude longs, floats, and chars
        program.add(randomDataFromType(possibleTypes[(int) (Math.random() * possibleTypes.length)]));
      }
      else program.add(Word.make(generateGibberish()));
      if (i != numArgs - 1) program.add(Word.make(", "));
    }
    program.add(Word.make(")"));
    
    program.add(Word.make(";"));
  }
  
  
  
  public static void drawKeyboard() {
    // Draws all the keys that make up the computer's keyboard
    
    // The keyboard is comprised of rows of keys at various sizes, where each key is a rounded rectangle
    
    // This function...is a bit of a mess. But I like the 2D array for the keys, it's very easy to manipulate to change the sizes of the keys
    
    ctx.setFill(keyColor);
    
    double height = (computerHeight * (1 - computerHalfwayPoint)) - (keyboardPaddingT + keyboardPaddingB);
    double width = computerWidth - (keyboardPaddingLR * 2);
    
    int rows = 6; // Number of rows of keys
    double unitSize = height / rows; // The width and height of a square key, for spacing purposes
    
    double fnRowRatio = 0.64; // The ratio of the first row (fn keys)
    double arrowTBSpacing = 1.6; // Vertical padding between the top and bottom arrow keys
    
    double heightAdjust = 6 / (fnRowRatio + rows - 1);
    
    // These represent the relative widths of each key in each row
    double[][] keyWidths = {
      {0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9},
      {0.6, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 1.7},
      {1.3, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 1.15},
      {1.55, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 1.85},
      {1.8, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 2},
      {0.9, 0.9, 0.9, 0.9, 5, 0.9, 0.9, 1, 1, 1}
    };
    
    // (current x, current y)
    double cx = computerX - (computerWidth / 2) + keyboardPaddingLR;
    double cy = computerY - (computerHeight / 2) + (computerHeight * computerHalfwayPoint) + keyboardPaddingT;
    
    // Step through each row:
    for (int row = 0; row < rows; row++) {
      cx = computerX - (computerWidth / 2) + keyboardPaddingLR;
      
      // Calculate total length of row to be able to adjust it, if it is too long or too short to fit the keyboard
      double totalWidth = 0;
      for (int i = 0; i < keyWidths[row].length; i++) {
        totalWidth += keyWidths[row][i] * unitSize;
      }
      double correctionFactor = width / totalWidth;
      
      // Step through each key:
      for (int key = 0; key < keyWidths[row].length; key++) {
        boolean isLastRow = row == rows - 1; // In lowest row
        boolean isArrowKey = (key >= keyWidths[row].length - 3) && isLastRow; // In the last 3 key slots
        boolean isArrowTB = (key == keyWidths[row].length - 2) && isArrowKey; // Middle arrow key
        
        double currWidth = keyWidths[row][key] * unitSize * correctionFactor;
        
        double _x = cx + keyPadding;
        double _y = cy + keyPadding;
        
        double w = currWidth - (keyPadding * 2);
        double h = ((row == 0 ? unitSize * fnRowRatio : unitSize) * heightAdjust) - (keyPadding * 2); // First row is smaller
        if (isArrowKey) {
          h /= 2;
          _y += (unitSize / 2) - keyPadding;
        }
        
        if (isArrowTB) { // We need to draw 2 keys for the up/down arrows since they're in the same slot
          drawRoundedRect(_x, _y - (unitSize / 2) + keyPadding, w, h - (arrowTBSpacing / 2), new double[] {keyCornerRadii[0], keyCornerRadii[0], 1, 1}, true); // Top
          drawRoundedRect(_x, _y + (arrowTBSpacing / 2), w, h - (arrowTBSpacing / 2), new double[] {1, 1, keyCornerRadii[0], keyCornerRadii[0]}, true); // Bottom
          
          // Uncomment this to give a small line between the top and bottom arrow keys
          
          /*ctx.setLineCap(StrokeLineCap.BUTT);
          ctx.setStroke(Color.hsb(0, 0, 0.1));
          ctx.setLineWidth(arrowTBSpacing * 1.5);
          
          ctx.beginPath();
          ctx.moveTo(_x, _y + (arrowTBSpacing / 2));
          ctx.lineTo(_x + w, _y + (arrowTBSpacing / 2));
          ctx.stroke();
          
          ctx.setLineCap(StrokeLineCap.ROUND);*/
        }
        else {
          drawRoundedRect(_x, _y, w, h, keyCornerRadii, true);
        }
        
        cx += currWidth;
      }
      
      // First row is thinner
      if (row != 0) {
        cy += unitSize * heightAdjust;
      }
      else {
        cy += unitSize * fnRowRatio * heightAdjust;
      }
    }
  }
  
  
  
  public static void drawTouchpad() {
    // The touchpad is a rounded rectangle with an outline and a line separating the left and right click
    
    ctx.setFill(touchpadFillColor);
    ctx.setStroke(touchpadOutlineColor);
    ctx.setLineWidth(touchpadOutlineThickness);
    
    double y = computerY + (computerHeight / 2) - keyboardPaddingB + touchpadPaddingY;
    y -= 2; // I have NO idea why the y-position is *slightly* off, but this should fix it
    
    double height = keyboardPaddingB - (touchpadPaddingY * 2);
    
    drawRoundedRect(computerX - (touchpadWidth / 2), y, touchpadWidth, height, touchpadCornerRadii, true);
    drawRoundedRect(computerX - (touchpadWidth / 2), y, touchpadWidth, height, touchpadCornerRadii, false);
    
    ctx.beginPath();
    ctx.moveTo(computerX, y + height);
    ctx.lineTo(computerX, y + height - 8);
    ctx.stroke();
  }
  
  
  
  public static void drawHead() {
    // Set up style
    ctx.setStroke(Color.BLACK);
    ctx.setFill(skinColor);
    ctx.setLineWidth(3);
    
    // Draw ellipse for head
    ctx.beginPath();
    ctx.arc(headX, headY, headWidth / 2, headHeight / 2, 0, 360);
    ctx.fill();
    ctx.stroke();
  }
  
  
  
  //static Color noseColor = Color.hsb(
  public static void drawNose() {    
    // The nose is an outline in the shape of a cubic Bezier curve
    
    ctx.setLineWidth(noseLineWidth);
    
    ctx.beginPath();
    
    ctx.moveTo(headX, headY + noseY);
    
    ctx.bezierCurveTo(
      headX + (noseWidth * 0.1),           // cp1x
      headY + noseY + (noseHeight * 0.62), // cp1y
      headX + (noseWidth * 0.75),          // cp2x
      headY + noseY + (noseHeight * 0.9),  // cp2y
      headX, headY + noseY + noseHeight    // endX, endY
    );
    
    ctx.stroke();
  }
  
  
  
  public static void drawEyes() {
    // Draw both eyes
    
    drawOneEye(headX + eye1X, headY + eyeY); // Left eye
    
    drawOneEye(headX + eye2X, headY + eyeY); // Right eye
  }
  
  
  
  public static void drawOneEye(double x, double y) {
    // Draws an eye at the given position
    
    // Each eye is a circle pupil within an ellipse
    
    // Set up style
    ctx.setLineWidth(1);
    ctx.setStroke(Color.BLACK);
    
    // Draw the eye
    ctx.setFill(Color.WHITE);
    
    ctx.save(); // Save the current context so the clip will be reset
    
    ctx.beginPath();
    
    ctx.moveTo(x - (eyeWidth / 2), y);
    ctx.quadraticCurveTo(x, y - (eyeHeight / 2), x + (eyeWidth / 2), y);
    ctx.quadraticCurveTo(x, y + (eyeHeight / 2), x - (eyeWidth / 2), y);
    
    ctx.fill(); // Draw the eye itself
    ctx.stroke();
    
    ctx.clip(); // Set this to a clip so the pupil won't overlap
    
    ctx.setFill(eyeColor);
    ctx.beginPath();
    ctx.arc(x + pupilX, y, pupilSize / 2, pupilSize / 2, 0, 360);
    ctx.fill();
    
    ctx.restore();
    
    ctx.setFill(eyeDotColor);
    ctx.beginPath();
    ctx.arc(x + pupilX, y, eyeDotSize / 2, eyeDotSize / 2, 0, 360);
    ctx.fill();
    
    
    // **** Uncomment this to add eyelashes: ****
    
    /*double[] cp = {x, y - (eyeHeight / 2)};
    double[] p1 = {x - (eyeWidth / 2), y};
    double[] p2 = {x + (eyeWidth / 2), y};
    int numLashes = 30;
    double lashLength = 2;
    
    ctx.setStroke(Color.BLACK);
    ctx.setLineWidth(1);
    
    for (int j = 0; j < 2; j++) {
      for (int i = 0; i < numLashes; i++) {
        double iRatio = ((double) i) / (numLashes - 1);
        if (iRatio < 0) iRatio = 0;
        if (iRatio > 1) iRatio = 1;
        
        iRatio = (iRatio * 0.95) + ((1 - 0.95) / 2);
        
        double[] pos = pointOnQuadCurve(cp, p1, p2, iRatio);
        double[] vel = velocityOnQuadCurve(cp, p1, p2, iRatio);
        double a = Math.atan2(vel[1], vel[0]) - ((Math.PI / 2.0) * (j == 0 ? 1 : -1));
        
        double endX = pos[0] + (lashLength * Math.cos(a));
        double endY = pos[1] + (lashLength * Math.sin(a));
        
        ctx.beginPath();
        ctx.moveTo(pos[0], pos[1]);
        ctx.lineTo(endX, endY);
        ctx.stroke();
      }
      
      cp = new double[] {x, y + (eyeHeight / 2)};
    }*/
  }
  
  
  
  public static void drawEyebrows() {
    // Draw left eyebrow
    drawOneEyebrow(headX + eye1X, headY + eyeY + eyebrowY, true);
    
    // Draw right eyebrow
    drawOneEyebrow(headX + eye2X, headY + eyeY + eyebrowY, false);
  }
  
  
  
  public static void drawOneEyebrow(double x, double y, boolean isLeft) {
    // x, y is the position of the center of the eyebrow
    // isLeft means its the left eyebrow
    
    // Each eyebrow is comprised of "hairs" trailing along and tangent to a quadratic Bezier curve, with some variation to make it bushy
    
    int sign = isLeft ? 1 : -1; // Sign for some mirroring calculations
    
    double adjust = 3.5; // Makes the eyebrow slightly angled up
    
    x -= eyebrowWidth * 0.5 * sign; // Move x to the edge
    
    double cpx = x + (eyebrowWidth * eyebrowSkew * sign);
    double cpy = y - eyebrowHeight;
    double endX = x + (eyebrowWidth * sign);
    double endY = y - adjust;
    
    int numHairs = 150;
    
    for (int i = 0; i < numHairs; i++) {
      for (int k = 0; k < 3; k++) { // Draw several hairs at the same place to make the eyebrows more "filled in"
        double j = i + randBetween(-1, 1);
        if (j < 0) {
          j = 0;
        }
        if (j >= numHairs) {
          j = numHairs - 1;
        }
        
        double iRatio = j / (numHairs - 1.0);
        
        double[] pos = pointOnQuadCurve(new double[] {cpx, cpy}, new double[] {x, y}, new double[] {endX, endY}, iRatio);
        double[] vel = velocityOnQuadCurve(new double[] {cpx, cpy}, new double[] {x, y}, new double[] {endX, endY}, iRatio);
        double angle = Math.atan2(vel[1], vel[0]) + Math.PI;
        angle += sign * Math.toRadians(Math.random() * 35); // Introduce some angle variation to make the eyebrows bushy
        double len = randBetween(5, 8) * 1.4;
        
        double xOff = randBetween(-2, 2); // Slightly vary the position of the hair
        double yOff = randBetween(-2, 2);
        
        double[] end = new double[] {pos[0] + (len * Math.cos(angle)), pos[1] + (len * Math.sin(angle))};
        
        ctx.setLineWidth(eyebrowThickness * randBetween(0.08, 0.14)); // Set random (but thin) line width
        
        // Generate a random hair color
        double h = 25 + randBetween(-13, 13);
        double s = randBetween(60, 85) / 100;
        double l = (randBetween(25, 47) - 10) / 100;
        Color c = Color.hsb(h, s, l);
        ctx.setStroke(c);
        
        ctx.beginPath();
        ctx.moveTo(pos[0] + xOff, pos[1] + yOff);
        ctx.lineTo(end[0] + xOff, end[1] + yOff);
        ctx.stroke();
      }
    }
  }
  
  
  
  public static void drawMouth() {
    // Draws the mouth
    
    // The mouth is an outline of a partial elliptic arc making a smirk shape
    
    ctx.setStroke(Color.BLACK);
    ctx.setLineWidth(mouthThickness);
    
    ctx.beginPath();
    ctx.arc(headX, headY + mouthY, mouthWidth / 2, mouthHeight / 2, 190 - 5, 122 - 5);
    ctx.stroke();
  }
  
  
  
  public static void drawGlasses() {
    // The glasses are 2 asymmetric rounded rectangles and some connections
    
    ctx.setStroke(glassesColor);
    ctx.setLineWidth(glassesThickness);
    
    // Estimated radii of glasses corners, for left lens
    // This has to be explicitly declared since the right is a mirror of the left
    double[][] cornerRadii = {
      {9, 9},
      {23, 12},
      {20, 33},
      {15, 28}
    };
    
    // Left lens
    drawRoundedRect(
      headX + eye1X - (lensWidth / 2), // x
      headY + eyeY - (lensHeight / 2), // y
      lensWidth, // width
      lensHeight, // height
      cornerRadii, // Corner radii
      false // filled
    );
    
    // Right lens
    drawRoundedRect(
      headX + eye2X - (lensWidth / 2), // x
      headY + eyeY - (lensHeight / 2), // y
      lensWidth, // width
      lensHeight, // height
      new double[][] {cornerRadii[1], cornerRadii[0], cornerRadii[3], cornerRadii[2]}, // Corner radii (modified order so it mirrors to the right lens)
      false // filled
    );
    
    // Connection between lenses (a thick outline quadratic Bezier curve)
    
    double connectionY = headY + eyeY - (lensHeight * 0.3);
    double connectionX1 = headX + eye1X + (lensWidth / 2);
    double connectionX2 = headX + eye2X - (lensWidth / 2);
    double connectionBend = 4;
    
    ctx.setLineWidth(glassesThickness * 1.75);
    ctx.setLineCap(StrokeLineCap.BUTT); // We don't want the rounded end for this, because it will overlap the glasses
    
    ctx.beginPath();
    ctx.moveTo(connectionX1, connectionY);
    ctx.quadraticCurveTo(headX, connectionY - connectionBend, connectionX2, connectionY);
    ctx.stroke();
    
    // Glasses arms
    
    ctx.setLineWidth(glassesThickness * 0.9);
    
    // Left arm
    
    double cx = headX + eye1X - (lensWidth * 0.5) + (glassesThickness / 2);
    double cy = headY + eyeY - (lensHeight * 0.5) + (glassesThickness / 2);
    
    ctx.beginPath();
    
    ctx.moveTo(cx, cy);
    
    cx = headX - (headWidth / 2);
    cy += headHeight * 0.04;
    ctx.lineTo(cx, cy);
    
    ctx.stroke();
    
    // Right arm
    
    cx = headX + eye2X + (lensWidth * 0.5) - (glassesThickness / 2);
    cy = headY + eyeY - (lensHeight * 0.5) + (glassesThickness / 2);
    
    ctx.beginPath();
    
    ctx.moveTo(cx, cy);
    
    cx = headX + (headWidth / 2);
    cy += headHeight * 0.04;
    ctx.lineTo(cx, cy);
    
    ctx.stroke();
    
    ctx.setLineCap(StrokeLineCap.ROUND); // Set the line cap back to my preferred default of rounded
  }
  
  
  
  public static void drawLowerHair() {
    // This hair is drawn beneath the head, and swoops down to form the long hair to shoulders
    
    int numStrands = (int) randBetween(180, 220); // How many hairs there are
    
    double hairExtent = 250; // How many degrees the hair covers
    double startAngle = 90 - (hairExtent / 2); // 90 is straight up
    double endAngle = 90 + (hairExtent / 2);
    double hairPadding = -5;
    
    // The j-loop draws an arc of hair 3 times, each time at a slightly higher y-value to make the hair more "filled in"
    for (int j = 0; j < 5; j++) {
      for (int i = 0; i < numStrands; i++) {
        double padding = hairPadding * randBetween(0.5, 1); // Randomize padding slightly
        
        double iRatio = (i + Math.random()) / numStrands; // This double varies from 0-1 as i increases (with some slight randomness to make it less regular)
        double angle = (hairExtent * iRatio) + startAngle; // This angle varies from startAngle to endAngle at even intervals every iteration
        
        // This just sets (x, y) to the point on the border of the head ellipse at the current angle
        // However, it also brings it closer to the center by `padding`, and treats the ellipse as slightly wider, so that the hair comes out from behind the head as well
        double x = headX + (((headWidth * 0.6) - padding) * Math.cos(Math.toRadians(angle)));
        double y = headY + (((headHeight * 0.5) - padding) * Math.sin(Math.toRadians(angle)));
        double length = randBetween(30, 65) * 1.7;
        int numCurls = (int) (randBetween(4, 7) * 1.7);
        
        drawSingleHair(x, y, 90 + randBetween(-7, 7), length, numCurls);
      }
      
      hairPadding += randBetween(8, 12); // Scoot the next j-iteration in by a bit
    }
  }
  
  
  
  public static void drawUpperHair() {
    int numStrands = 100;
    double hairExtent = 110; // How many degrees the hair spans
    double startAngle = -90 - (hairExtent / 2); // 90deg is straight up
    double endAngle = -90 + (hairExtent / 2);
    
    double yOffset = -25;
    
    for (int j = 0; j < 3; j++) {
      for (int i = 0; i < numStrands; i++) {
        double distToCenter = Math.abs(i - (numStrands / 2.0)) / (numStrands / 2.0); // A ratio from 0-1 that varies as the hair gets further from the center of the head
        
        double iRatio = (i + Math.random()) / numStrands;
        double angle = (hairExtent * iRatio) + startAngle; // This angle varies from startAngle to endAngle at even intervals every iteration
        
        boolean onLeft = i + randBetween(-5, 5) < numStrands / 2;
        
        double hairAngle = (distToCenter * 22) + 16.5; // Hair gradually gets more sloped away from the center of the head
        hairAngle += randBetween(-6, 10);
        if (onLeft) hairAngle = 180 - hairAngle;
        
        double xOffset = randBetween(7, 14) * (onLeft ? 1 : -1);
        double length = (distToCenter * 80) + 30;
        int numCurls = (int) (randBetween(8, 14) * (length / 120));
        double pad = randBetween(5, 20);
        double x = headX + (((headWidth * 0.5) - pad) * Math.cos(Math.toRadians(angle)));
        double y = headY + (((headHeight * 0.5) - pad) * Math.sin(Math.toRadians(angle)));
        
        drawSingleHair(x + xOffset, y + yOffset, hairAngle, length, numCurls);
      }
      
      yOffset += 12;
    }
  }
  
  
  
  public static void drawSingleHair(double x, double y, double angle, double length, int numCurls) {
    // Draws a hair at the given position, pointing in the direction of `angle`
    // The shape of the hair is a squiggle, made up of arcs
    
    angle = doubleMod(angle, 360); // Make sure angle is between 0-360
    
    double offset = Math.random() < 0.5 ? 0 : 180; // This is a randomly set flag that flips the orientation of each curl, to add more variation
    
    double lineWidth = randBetween(1.2, 2.5) * 0.7; // The randomized thickness of the hair
    
    // Cache the sin and cos values of the angle to avoid repeated calculations
    double sin = Math.sin(Math.toRadians(angle));
    double cos = Math.cos(Math.toRadians(angle));
    
    // unitGap is the gap between curls, and bigGap and smallGap determine where the arcs are placed to make the curls connect smoothly
    double unitGap = length / numCurls;
    double bigGap = unitGap * 1.5;
    double smallGap = unitGap * 0.5;
    
    // Randomly set the hair color to some shade of brown, using the HSL color space
    double h = 25 + randBetween(-13, 13);
    double s = randBetween(60, 85) / 100;
    double l = randBetween(25, 47) / 100;
    Color c = Color.hsb(h, s, l);
    
    // This j-loop serves to give the hair an outline
    // JavaFX is super weird with paths, so each arc has to be a new path
    // Because of this, we draw all the arcs in black first, and then draw the same arcs above that in (slightly thinner) brown
    for (int j = 0; j < 2; j++) {
      // cx = current x pos, cy = current y pos
      // These values will be slowly reaching towards the end of the hair strand
      double cx = x;
      double cy = y;
      
      // Each "curl" consists of 2 arcs, a small and large one, that connect smoothly and perfectly tile the line from (x, y) to (endX, endY)
      for (int i = 0; i < numCurls; i++) {
        ctx.beginPath();
        
        // We don't want the small arc on the first curl, since that would make it look weird and also stretch beyond the bounds of the hair strand
        if (i != 0) {
          // Draw the small arc:
          
          // Scoot halfway along smallGap to draw an arc in the center perfectly touching either side
          cx -= smallGap * cos * 0.5;
          cy -= smallGap * sin * 0.5;
          
          // Draw the rotated arc
          ctx.arc(cx, cy, smallGap / 2, smallGap / 2, -angle + offset, 180);
          
          // Scoot the rest of the way
          cx -= smallGap * cos * 0.5;
          cy -= smallGap * sin * 0.5;
        }
        
        // This logic makes the arcs black and then brown, as j iterates
        if (j == 0) {
          ctx.setStroke(Color.BLACK);
          ctx.setLineWidth(lineWidth + 1); // Black line 1px thicker so the hair has an outline
          ctx.stroke();
        }
        else {
          ctx.setStroke(c);
          ctx.setLineWidth(lineWidth);
          ctx.stroke();
        }
        
        // Draw the large arc:
        
        ctx.beginPath();
        
        // Scoot halfway along bigGap to draw an arc in the center perfectly touching either side (we have now gone further along the line than we scooted backwards)
        cx += bigGap * cos * 0.5;
        cy += bigGap * sin * 0.5;
        
        // Draw the rotated arc
        ctx.arc(cx, cy, bigGap / 2, bigGap / 2, -angle + 180 + offset, 180);
        
        // Scoot the rest of the way
        cx += bigGap * cos * 0.5;
        cy += bigGap * sin * 0.5;
        
        // This logic makes the arcs black and then brown, as j iterates
        if (j == 0) {
          ctx.setStroke(Color.BLACK);
          ctx.setLineWidth(lineWidth + 1);
          ctx.stroke();
        }
        else {
          ctx.setStroke(c);
          ctx.setLineWidth(lineWidth);
          ctx.stroke();
        }
      }
    }
  }
  
  
  
  // Misc helper methods:
  
  
  
  public static double doubleMod(double a, double b) {
    // Java doesn't let the modulus operator work on doubles, so this remedies that
    
    while (a > b) a -= b;
    while (a < 0) a += b;
    
    return a;
  }
  
  
  
  public static void drawRoundedRect(double x, double y, double width, double height, double[][] radii, boolean filled) {
    // Draws a rectangle with rounded corners
    // `radii` represents the radius of each corner, starting at top-left and going clockwise (and each element is [xRadius, yRadius]
    
    // If `filled` is true, it will fill *instead* of stroking, and vice versa for false
    
    // The coordinates of the centers of each arc ([0] is x and [1] is y)
    double[][] innerCoords = {
      {x + radii[0][0], y + radii[0][1]},
      {x + width - radii[1][0], y + radii[1][1]},
      {x + width - radii[2][0], y + height - radii[2][1]},
      {x + radii[3][0], y + height - radii[3][1]}
    };
    
    // The coordinates of the endpoints of the straight lines connecting the corner arcs
    double[][] outerCoords = {
      {x + radii[0][0], y},
      {x + width - radii[1][0], y},
      {x + width, y + radii[1][1]},
      {x + width, y + height - radii[2][1]},
      {x + width - radii[2][0], y + height},
      {x + radii[3][0], y + height},
      {x, y + height - radii[3][1]},
      {x, y + radii[0][1]}
    };
    
    if (filled) { // Fill:
      // Draw straight lines first:
      
      ctx.beginPath();
      
      ctx.moveTo(outerCoords[0][0], outerCoords[0][1]);
      
      for (int i = 0; i < 8; i++) {
        int j = (i + 1) % 8; // The "next" iteration, but looping around if we overflow past 7
        
        double[] p1 = outerCoords[i];
        double[] p2 = outerCoords[j];
        
        ctx.lineTo(p1[0], p1[1]);
        ctx.lineTo(p2[0], p2[1]);
      }
      
      ctx.fill();
      
      // Then draw ellipses for the corner arcs, so they're filled in
      
      for (int i = 0; i < 4; i++) {
        double[] pos = innerCoords[i];
        double radX = radii[i][0];
        double radY = radii[i][1];
        
        ctx.beginPath();
        
        ctx.arc(pos[0], pos[1], radX, radY, 0, 360);
        
        ctx.fill();
      }
    }
    else { // Stroke:
      // Draw each arc
      for (int i = 0; i < 4; i++) {
        double[] pos = innerCoords[i];
        
        ctx.beginPath();
        ctx.arc(pos[0], pos[1], radii[i][0], radii[i][1], 90 - (90 * i), 90);
        ctx.stroke();
        
        double[] p1 = outerCoords[i * 2];
        double[] p2 = outerCoords[(i * 2) + 1];
        
        ctx.beginPath();
        ctx.moveTo(p1[0], p1[1]);
        ctx.lineTo(p2[0], p2[1]);
        ctx.stroke();
      }
    }
  }
  
  // Overload drawRoundedRect to allow single-dimensional arrays of corner radii
  public static void drawRoundedRect(double x, double y, double width, double height, double[] radii, boolean filled) {
    // Turn the 1D array into a 2D array with the same x-radius and y-radius
    double[][] fixedRadii = new double[radii.length][2];
    for (int i = 0; i < radii.length; i++) {
      fixedRadii[i][0] = fixedRadii[i][1] = radii[i];
    }
    
    drawRoundedRect(x, y, width, height, fixedRadii, filled);
  }
  
  
  
  public static double randBetween(double min, double max) {
    return (Math.random() * (max - min)) + min;
  }
  
  
  
  public static int randSign() {
    // Randomly returns 1 or -1
    
    return Math.random() < 0.5 ? 1 : -1;
  }
  
  
  
  public static double[] pointOnQuadCurve(double[] cp, double[] a, double[] b, double t) {
    // Calculates the point along the quadratic Bezier curve from (a) to (b), with t indicating the percent along the curve
    
    double x = ((1 - t) * (1 - t) * a[0]) + (2 * (1 - t) * t * cp[0]) + (t * t * b[0]);
    double y = ((1 - t) * (1 - t) * a[1]) + (2 * (1 - t) * t * cp[1]) + (t * t * b[1]);
    
    return new double[] {x, y};
  }
  
  
  
  public static double[] velocityOnQuadCurve(double[] cp, double[] a, double[] b, double t) {
    // Calculates the DERIVATIVE of the point along the quadratic Bezier curve from (a) to (b), with t indicating the percent along the curve
    
    double x = (2 * (1 - t) * (cp[0] - a[0])) + (2 * t * (b[0] - cp[0]));
    double y = (2 * (1 - t) * (cp[1] - a[1])) + (2 * t * (b[1] - cp[1]));
    
    return new double[] {x, y};
  }
  
  
  
  // An instance of the Word class represents a string of characters of a specific color
  public static class Word {
    // Establish some global colors based on type
    public static Color defaultColor = Color.WHITE;
    public static Color instructionColor = Color.hsb(57, 0.87, 0.73);
    public static Color typeColor = Color.hsb(40, 1, 0.5);
    public static Color numberColor = Color.hsb(338, 1, 0.61);
    public static Color stringColor = Color.hsb(110, 0.76, 0.56);
    public static Color operatorColor = Color.hsb(40, 1, 0.5);
    public static Color commentColor = Color.hsb(202, 0.76, 0.5);
    public static Color lineNumberColor = Color.hsb(0, 0, 0.85);
    
    // Keywords for each color
    public static String[] instructionKeywords = {"instanceof", "assert", "if", "else", "switch", "case", "default", "break", "goto", "return", "for", "while", "do", "continue", "new", "throw", "throws", "try", "catch", "finally", "this", "super", "extends", "implements", "permits", "import", "true", "false", "null"};
    public static String[] typeKeywords = {"abstract", "boolean", "byte", "char", "class", "const", "double", "enum", "final", "float", "int", "interface", "long", "native", "non-sealed", "package", "private", "protected", "public", "sealed", "short", "static", "strictfp", "synchronized", "transient", "var", "void", "volatile"};
    public static String[] operatorKeywords = {"+", "-", "*", "/", "!", "<", ">", "=", "<=", ">=", "==", ";", ":", ",", ".", "(", ")", "{", "}", "[", "]", "%", "&&", "||", "++", "--", "+=", "-=", "*=", "/=", "!="};
    
    // Instance properties
    public String content;
    public Color color;
    
    // Direct constructor, rarely used outside of class
    public Word(String content, Color color) {
      this.content = content;
      this.color = color;
    }
    
    // Factory method for making a word with conditional logic
    public static Word make(String content) {
      String trimmed = content.trim(); // Ignore whitespace on edges
      Color color = Color.WHITE; // White is default color
      
      // Check if it matches any specific keyword color
      
      // Instruction words:
      for (int i = 0; i < instructionKeywords.length; i++) {
        if (trimmed.equals(instructionKeywords[i])) {
          color = instructionColor;
          return new Word(content, color);
        }
      }
      
      // Type words:
      for (int i = 0; i < typeKeywords.length; i++) {
        if (trimmed.equals(typeKeywords[i])) {
          color = typeColor;
          return new Word(content, color);
        }
      }
      
      // Operators:
      for (int i = 0; i < operatorKeywords.length; i++) {
        if (trimmed.equals(operatorKeywords[i])) {
          color = operatorColor;
          return new Word(content, color);
        }
      }
      
      return new Word(content, color); // If nothing matches, it will construct with default color
    }
    
    // So that I don't have to keep track of 2 different constructors, .make() is overloaded here to allow for specifying the color (even though new Word(content, color) would be the same)
    public static Word make(String content, Color color) {
      return new Word(content, color);
    }
    
    
    
    public void draw(double x, double y) {
      ctx.setFill(this.color);
      
      ctx.fillText(this.content, x, y);
    }
  }
  
  
  
  
  
  
  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
