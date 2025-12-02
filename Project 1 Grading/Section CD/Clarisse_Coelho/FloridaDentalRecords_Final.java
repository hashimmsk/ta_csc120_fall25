import java.util.Scanner;


/**
 * Records the teeth of one Florida family.
 * It offers a menu with options to:
 * 1) Print family record
 * 2) Extract a tooth
 * 3) Report the family root canal indices
 * @author Clarisse Coelho
 */

public class FloridaDentalRecords_Final {


    /**
     * Global Scanner object to use keyboard
     */

    private static final Scanner keyboard = new Scanner(System.in);


    /**
     * Max number of members is 6
     * Min number of members is 1
     */

    //--- Constants
    private static final int MAX_MEMBERS = 6;
    private static final int MIN_MEMBERS = 1;

    /**
     * The main method
     * Receives input of family total members.
     * Creates a one dimensional array with the total members in the family, and call a method to fill the array with teeth.
     * Prompts a menu with options to: Print, Extract, Roots, or Exit
     * If selected, the first 3 menu options call their respective methods for execution.
     *
     * @param args Passed in from the command line
     */
    public static void main(String[] args) {

        int familyMembers;
        boolean invalidNumMembers;

        System.out.println("""
                Welcome to the Floridian Tooth Records
                --------------------------------------
                """);

        System.out.printf("%-50s: ","Please enter number of people in the family");
        familyMembers = keyboard.nextInt();

        do {
            invalidNumMembers = false;

            if (familyMembers < MIN_MEMBERS || familyMembers > MAX_MEMBERS) {
                System.out.printf("%-50s: ","Invalid number of people, try again");
                familyMembers = keyboard.nextInt();
                invalidNumMembers = true;
            }

        }while(invalidNumMembers);

        String[] familyMembersPlane = new String[familyMembers];
        char[][][] teethArray = new char[familyMembers][2][];

        fillTeeth(teethArray,familyMembersPlane);

        //----MENU OPTIONS DISPLAY

        String menuOption;
        boolean validOption;

        do{
        System.out.printf("%-50s : ","\n(P)rint, (E)xtract, (R)oot, e(X)it");

        menuOption = keyboard.next();

        do {
            if (menuOption.equalsIgnoreCase("p")) {

                printAllTeeth(familyMembersPlane, teethArray);
                 validOption=true;

            } else if (menuOption.equalsIgnoreCase("e")) {

                extractTeeth(familyMembersPlane, teethArray);
                validOption=true;

            } else if (menuOption.equalsIgnoreCase("r")) {

                rootCanalIndices(teethArray);
                validOption=true;

            } else if (menuOption.equalsIgnoreCase("x")) {

                System.out.println("\nExiting the Floridian Tooth Records :-)");
                validOption=true;

            } else {
                System.out.printf("%-50s: ", "Invalid menu option, try again");
                menuOption = keyboard.next();
                validOption=false;
            }

        }while(!validOption);

        }while(!menuOption.equalsIgnoreCase("x"));

    } // end of main method

    /**
     * Receive input of family member name and store it in a String 1D array.
     * Receive the user's input as a string of the lower and upper teeth of that family member.
     * Each letter of the string is uppercased and stored separately at a 3D array.
     * It checks if the teeth entries stored in the array are valid ("B,""M," or "I") and if the length is valid (up to 8)
     * @param teethArray 3D String array with the dimensions: member, teeth layer and teeth index.
     * @param familyMembersPlane 1D String array with the members name
     *
     */
    public static void fillTeeth(char[][][]teethArray, String[]familyMembersPlane){

        String upperLowCase;
        String lowerLowCase;
        String upper;
        String lower;


        for (int member = 0; member < familyMembersPlane.length; member++) {
            System.out.printf("%-50s: ","Please enter the name for family member " + (member +1));

            familyMembersPlane[member] = keyboard.next();



            //----Fill up the 3D Array with the STRING for UPPER provided

            boolean invalidEntry;
            boolean invalidLength;
            int upperIndex;
            int lowerIndex;

            System.out.printf("%-50s: ","Please enter the uppers for " + familyMembersPlane[member] );
            upperLowCase = keyboard.next();
            upper = upperLowCase.toUpperCase();


            //----Fills the array and checks if the info is valid for UPPERS


            do {
                invalidEntry = false;
                invalidLength = false;

                teethArray[member][0] = new char[upper.length()];

                for (upperIndex = 0; upperIndex < upper.length(); upperIndex++) {

                    teethArray[member][0][upperIndex] = (upper.charAt(upperIndex));

                    // Check if the inserted letter is correct ----------------------------------------------

                    if(upper.charAt(upperIndex) == ('I') || upper.charAt(upperIndex) == ('M') || upper.charAt(upperIndex) == ('B') ) {

                    } else {
                        invalidEntry = true;
                    }

                    if(upper.length() > 8) {
                        invalidLength = true;
                    }

                } // end of FOR LOOP that fills the array


                //----Analyse if input is valid

                if(invalidEntry && !invalidLength) {
                    System.out.printf("%-50s: ","Invalid teeth types, try again");
                    upperLowCase = keyboard.next();
                    upper = upperLowCase.toUpperCase();
                }else if(invalidLength && !invalidEntry){
                    System.out.printf("%-50s: ","Too many teeth, try again");
                    upperLowCase = keyboard.next();
                    upper = upperLowCase.toUpperCase();

                }else if(invalidEntry && invalidLength){
                    System.out.printf("%-50s: ","Too many teeth, try again");
                    upperLowCase = keyboard.next();
                    upper = upperLowCase.toUpperCase();

                }

            }while(invalidEntry || invalidLength);

            //----LOWERS INPUT

            System.out.printf("%-50s: ","Enter the lowers for " + familyMembersPlane[member] );
            lowerLowCase = keyboard.next();
            lower = lowerLowCase.toUpperCase();


            //----Fills the array and checks if the info is valid for LOWER TEETH

            do {
                invalidEntry = false;
                invalidLength = false;

                teethArray[member][1] = new char[lower.length()];
                for (lowerIndex = 0; lowerIndex < lower.length(); lowerIndex++) {

                    teethArray[member][1][lowerIndex] = (lower.charAt(lowerIndex));

                    if (lower.charAt(lowerIndex)==('I')
                            || lower.charAt(lowerIndex)==('B')
                            || lower.charAt(lowerIndex)==('M')) {

                    } else {
                        invalidEntry = true;
                    }

                    if(lower.length() > 8){
                        invalidLength = true;
                    }
                }

                //----Analyse if all entries are valid

                if(invalidEntry && !invalidLength) {
                    System.out.printf("%-50s: ","Invalid teeth types, try again");
                    lowerLowCase = keyboard.next();
                    lower=lowerLowCase.toUpperCase();
                }else if(invalidLength && !invalidEntry){
                    System.out.printf("%-50s: ","Too many teeth. Try again");
                    lowerLowCase = keyboard.next();
                    lower=lowerLowCase.toUpperCase();

                }else if(invalidEntry && invalidLength){
                    System.out.printf("%-50s: ","Invalid teeth types and too many teeth. Try again");
                    lowerLowCase = keyboard.next();
                    lower=lowerLowCase.toUpperCase();

                }


            }while(invalidEntry || invalidLength );


        }// end of OUTER FOR LOOP

    } // end of FillTeeth method

    /**
     * Receives a String containing the desired person's name.
     * Compares the given name with each element in the array until a match is found.
     * Once the match is located, returns the corresponding index.
     *
     * @param checkMember name of person the method will find the respective index
     * @param familyMembersPlane 1D String array with the members name
     * @return member index number
     *
     */
    public static int findMemberIndex(String checkMember,String[]familyMembersPlane){

        int memberIndex=-1;

        //----DoWhile loop to run until a valid family member is found

        do {
            for ( int loopIndex = 0; loopIndex < familyMembersPlane.length; loopIndex++) {

                if (familyMembersPlane[loopIndex].equalsIgnoreCase(checkMember)) {
                    memberIndex = loopIndex;
                    break;
                }
            }
                if (memberIndex == -1) {

                    System.out.printf("%-50s: ","Invalid Member. Try Again:");
                    checkMember = keyboard.next();
                }

        }while(memberIndex==-1);

        return memberIndex;

    } // end of FIND MEMBER INDEX method

     /**
     * Prints the name of each family member and their respective teeth layers.
     * @param teethArray 3D String array with the dimensions: member, teeth layer and teeth index.
     * @param familyMembersPlane 1D String array with the members name
     *
     */
    public static void printAllTeeth(String[] familyMembersPlane, char[][][]teethArray){

        System.out.println("");

        //----Cascade of FOR Loops to print the members, their tooth layers and teeth.

        for (int memberIndex = 0; memberIndex < familyMembersPlane.length; memberIndex++) {

            System.out.println(familyMembersPlane[memberIndex]);

            for (int layerIndex = 0; layerIndex < 2; layerIndex++) {

                if(layerIndex==0){
                    System.out.print("  Uppers:   ");
                }else{
                    System.out.print("  Lowers:   ");
                }

                for(int teethIndex = 0; teethIndex < teethArray[memberIndex][layerIndex].length; teethIndex++) {

                    System.out.print((teethIndex+1) + ": " + teethArray[memberIndex][layerIndex][teethIndex]+ "  ");

                }
                System.out.println("");
            }

            System.out.println("");
        }

    } // end of Print All Teeth method

    /**
     * Receives the name of member the extraction will happen.
     * Calls method to find the index of the member.
     * Receives the layer and teeth that will be extracted
     * Checks if the teeth selected to be extracted is not already missing
     * Edits the Array: substitutes teeth on that position, "B" or "I," to "M"
     * @param teethArray 3D String array with the dimensions: member, teeth layer and teeth index.
     * @param familyMembersPlane 1D String array with the members name
     *
     */
    public static void extractTeeth(String[]familyMembersPlane,char[][][]teethArray){
        int memberIndex;
        int teethNumber;
        boolean teethInvalid = false;
        int teethLayer;
        boolean missingAllTeeth = false;

        //-------Choose member

            System.out.printf("%-50s: ","What member do you want to extract a teeth?");
            String checkMember = keyboard.next();

            memberIndex = findMemberIndex(checkMember, familyMembersPlane);
            
            
            

        //-------Choose Layer

        System.out.printf("%-50s: ","Which tooth layer (U)pper or (L)ower?");

        do {
            String checkLayer = keyboard.next();

            if (checkLayer.equalsIgnoreCase("U")) {

                teethLayer = 0;

            } else if (checkLayer.equalsIgnoreCase("L")){
                teethLayer = 1;

            }else{
                teethLayer = -1;
                System.out.printf("%-50s: ","Invalid layer, try again");
            }
        }while(teethLayer == -1);
        
        
        //----Check if person has a tooth to be extracted


        for (int i = 0; i < teethArray[memberIndex][teethLayer].length; i++) {

            if(teethArray[memberIndex][teethLayer][i] == 'I' || teethArray[memberIndex][teethLayer][i] == 'B'){
                missingAllTeeth = false;
                break;

            }else{

                missingAllTeeth=true;
            }

        }// end of FOR Loop

        if(missingAllTeeth){
            System.out.println("All teeth are already missing in this layer");
            return;
        }


        //-------Choose tooth number

        System.out.printf("%-50s: ","Which tooth number");

        do {
            teethNumber = (keyboard.nextInt() -1);
            teethInvalid = false;


            if(teethNumber > teethArray[memberIndex][teethLayer].length-1 || teethNumber < 0){

                System.out.printf("%-50s: ","Invalid tooth number, try again");
                teethInvalid = true;

            }else if (teethArray[memberIndex][teethLayer][teethNumber]==('M')){

                System.out.printf("%-50s: ","Missing tooth, try again");
                teethInvalid = true;
            }

        }while(teethInvalid);


        //------Extraction itself

        teethArray[memberIndex][teethLayer][teethNumber] = 'M';

    } // end of Extract Teeth method

    /**
     * Counts total of each teeth type, then calculates the root canal indices using the quadratic equation to solve it
     * Prints the Canal Indices
     * @param teethArray 3D String array with the dimensions: member, teeth layer and teeth index.
     *
     */
    public static void rootCanalIndices(char[][][]teethArray){

    //----Counts how many of each tooth type you have

        int totalM=0;
        int totalB=0;
        int totalI=0;
        int memberIndex;
        int layerIndex;
        int teethIndex;

        for ( memberIndex = 0; memberIndex < teethArray.length; memberIndex++) {
            for ( layerIndex = 0; layerIndex < 2; layerIndex++) {
                for ( teethIndex = 0; teethIndex < teethArray[memberIndex][layerIndex].length; teethIndex++) {

                if(teethArray[memberIndex][layerIndex][teethIndex]=='M'){
                    totalM +=1;

                    }else if(teethArray[memberIndex][layerIndex][teethIndex]=='B'){
                    totalB+=1;

                    }else if(teethArray[memberIndex][layerIndex][teethIndex]=='I'){
                        totalI+=1;

                    }
                }
            }
        } // end of FOR loop to count letters.

        //---------Perform Actual calculations

        double delta;
        double root1; // positive root
        double root2; // negative root

        //-----Check Delta discriminant first


        delta = ((Math.pow(totalB,2)) - (4*totalI*(-totalM)));

        //-----Quadratic formula


        if(delta>0 && totalI!=0) {
            root1 = (((-totalB) + Math.sqrt(delta)) / (2 * totalI));
            root2 = (((-totalB) - Math.sqrt(delta)) / (2 * totalI));

            System.out.printf("One root canal at %.2f\n",root1);
            System.out.printf("Another root canal at %.2f\n",root2);

        } else if (delta==0 && totalI!=0) {
            root1 = (    (-totalB) / (2 * totalI)) ;
            System.out.printf("One root canal at %.2f\n",root1);

        } else if (delta<0 || totalI==0) {

            System.out.print("No real roots\n");

        }


    } // end of Root Canal Method

}//end of FloridaDentalRecords Class