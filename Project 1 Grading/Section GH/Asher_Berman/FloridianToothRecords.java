//

// Source code recreated from a .class file by IntelliJ IDEA

// (powered by FernFlower decompiler)

//



import java.util.Scanner;



public class FloridianToothRecords {

    public static final int MAX_PEOPLE = 6;

    public static final int MAX_TEETH = 8;

    public static final int UPPER = 0;

    public static final int LOWER = 1;



    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the Floridian Tooth Records");

        System.out.println("--------------------------------------");

        int numPeople = getNumberOfPeople(sc);

        String[] names = new String[numPeople];

        char[][][] teeth = new char[numPeople][2][8];

        int[][] toothCounts = new int[numPeople][2];

        inputFamilyData(sc, names, teeth, toothCounts, numPeople);

        menuLoop(sc, names, teeth, toothCounts, numPeople);

        sc.close();

    }



    public static int getNumberOfPeople(Scanner sc) {

        int numPeople = 0;

        System.out.print("Please enter number of people in the family : ");



        for(numPeople = sc.nextInt(); numPeople < 1 || numPeople > 6; numPeople = sc.nextInt()) {

            System.out.print("Invalid number of people, try again         : ");

        }



        sc.nextLine();

        return numPeople;

    }



    public static void inputFamilyData(Scanner sc, String[] names, char[][][] teeth, int[][] toothCounts, int numPeople) {

        label78:

        for(int personIndex = 0; personIndex < numPeople; ++personIndex) {

            System.out.print("Please enter the name for family member " + (personIndex + 1) + "   : ");

            String name = sc.nextLine().trim();

            names[personIndex] = name;

            System.out.print("Please enter the uppers for " + name + "       : ");

            String upperString = sc.nextLine().trim();



            while(true) {

                while(isValidTeethString(upperString)) {

                    if (upperString.length() <= 8) {

                        System.out.print("Please enter the lowers for " + name + "       : ");

                        String lowerString = sc.nextLine().trim();



                        while(true) {

                            while(isValidTeethString(lowerString)) {

                                if (lowerString.length() <= 8) {

                                    for(int i = 0; i < 8; ++i) {

                                        teeth[personIndex][0][i] = 0;

                                    }



                                    for(int var11 = 0; var11 < upperString.length(); ++var11) {

                                        teeth[personIndex][0][var11] = Character.toUpperCase(upperString.charAt(var11));

                                    }



                                    toothCounts[personIndex][0] = upperString.length();



                                    for(int j = 0; j < 8; ++j) {

                                        teeth[personIndex][1][j] = 0;

                                    }



                                    for(int var12 = 0; var12 < lowerString.length(); ++var12) {

                                        teeth[personIndex][1][var12] = Character.toUpperCase(lowerString.charAt(var12));

                                    }



                                    toothCounts[personIndex][1] = lowerString.length();

                                    continue label78;

                                }



                                System.out.print("Too many teeth, try again                   : ");

                                lowerString = sc.nextLine().trim();

                            }



                            System.out.print("Invalid teeth types, try again              : ");

                            lowerString = sc.nextLine().trim();

                        }

                    }



                    System.out.print("Too many teeth, try again                   : ");

                    upperString = sc.nextLine().trim();

                }



                System.out.print("Invalid teeth types, try again              : ");

                upperString = sc.nextLine().trim();

            }

        }



    }



    public static boolean isValidTeethString(String s) {

        int idx = 0;

        if (s.length() == 0) {

            return false;

        } else {

            while(idx < s.length()) {

                char c = Character.toUpperCase(s.charAt(idx));

                if (c != 'I' && c != 'B' && c != 'M') {

                    return false;

                }



                ++idx;

            }



            return true;

        }

    }



    public static void menuLoop(Scanner sc, String[] names, char[][][] teeth, int[][] toothCounts, int numPeople) {

        String choice = "";



        while(true) {

            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");



            for(choice = sc.nextLine().trim(); choice.length() == 0; choice = sc.nextLine().trim()) {

                System.out.print("Invalid menu option, try again              : ");

            }



            char opt = Character.toUpperCase(choice.charAt(0));

            if (opt != 'P') {

                if (opt != 'E') {

                    if (opt != 'R') {

                        if (opt == 'X') {

                            System.out.println("\nExiting the Floridian Tooth Records :-)");

                            break;

                        }



                        System.out.print("Invalid menu option, try again              : ");

                        choice = sc.nextLine().trim();

                        opt = choice.length() > 0 ? Character.toUpperCase(choice.charAt(0)) : 0;

                        if (opt == 'P') {

                            printFamilyRecords(names, teeth, toothCounts, numPeople);

                        } else if (opt == 'E') {

                            extractTooth(sc, names, teeth, toothCounts, numPeople);

                        } else if (opt == 'R') {

                            computeRootCanalIndices(teeth, toothCounts, numPeople);

                        } else if (opt == 'X') {

                            System.out.println("\nExiting the Floridian Tooth Records :-)");

                            break;

                        }

                    } else {

                        computeRootCanalIndices(teeth, toothCounts, numPeople);

                    }

                } else {

                    extractTooth(sc, names, teeth, toothCounts, numPeople);

                }

            } else {

                printFamilyRecords(names, teeth, toothCounts, numPeople);

            }

        }



    }



    public static void printFamilyRecords(String[] names, char[][][] teeth, int[][] toothCounts, int numPeople) {

        int personIndex = 0;

        System.out.println();



        while(personIndex < numPeople) {

            System.out.println(names[personIndex]);

            System.out.print("  Uppers:  ");

            int layer = 0;



            for(int toothIdx = 0; toothIdx < toothCounts[personIndex][layer]; ++toothIdx) {

                System.out.print(toothIdx + 1 + ":" + teeth[personIndex][layer][toothIdx]);

                if (toothIdx < toothCounts[personIndex][layer] - 1) {

                    System.out.print("  ");

                }

            }



            System.out.println();

            System.out.print("  Lowers:  ");

            layer = 1;



            for(int var8 = 0; var8 < toothCounts[personIndex][layer]; ++var8) {

                System.out.print(var8 + 1 + ":" + teeth[personIndex][layer][var8]);

                if (var8 < toothCounts[personIndex][layer] - 1) {

                    System.out.print("  ");

                }

            }



            System.out.println();

            ++personIndex;

        }



    }



    public static void extractTooth(Scanner sc, String[] names, char[][][] teeth, int[][] toothCounts, int numPeople) {

        int personIndex = -1;

        System.out.print("Which family member                         : ");

        String memberName = sc.nextLine().trim();



        for(personIndex = findPersonIndex(names, memberName, numPeople); personIndex == -1; personIndex = findPersonIndex(names, memberName, numPeople)) {

            System.out.print("Invalid family member, try again            : ");

            memberName = sc.nextLine().trim();

        }



        System.out.print("Which tooth layer (U)pper or (L)ower        : ");

        String layerInput = sc.nextLine().trim();



        int layer;

        while(true) {

            while(layerInput.length() == 0) {

                System.out.print("Invalid layer, try again                    : ");

                layerInput = sc.nextLine().trim();

            }



            char layerChar = Character.toUpperCase(layerInput.charAt(0));

            if (layerChar == 'U') {

                layer = 0;

                break;

            }



            if (layerChar == 'L') {

                layer = 1;

                break;

            }



            System.out.print("Invalid layer, try again                    : ");

            layerInput = sc.nextLine().trim();

        }



        System.out.print("Which tooth number                          : ");



        while(true) {

            while(sc.hasNextInt()) {

                int toothNumber = sc.nextInt();

                sc.nextLine();

                if (toothNumber >= 1 && toothNumber <= 8) {

                    if (toothNumber > toothCounts[personIndex][layer]) {

                        System.out.print("Invalid tooth number, try again             : ");

                    } else {

                        char current = teeth[personIndex][layer][toothNumber - 1];

                        if (current != 'M') {

                            teeth[personIndex][layer][toothNumber - 1] = 'M';

                            return;

                        }



                        System.out.print("Missing tooth, try again                    : ");

                    }

                } else {

                    System.out.print("Invalid tooth number, try again             : ");

                }

            }



            sc.nextLine();

            System.out.print("Invalid tooth number, try again             : ");

        }

    }



    public static int findPersonIndex(String[] names, String query, int numPeople) {

        int i = 0;



        for(String lowerQuery = query.toLowerCase(); i < numPeople; ++i) {

            if (names[i].toLowerCase().equals(lowerQuery)) {

                return i;

            }

        }



        return -1;

    }



    public static void computeRootCanalIndices(char[][][] teeth, int[][] toothCounts, int numPeople) {

        int I = 0;

        int B = 0;

        int M = 0;



        for(int personIndex = 0; personIndex < numPeople; ++personIndex) {

            for(int layer = 0; layer <= 1; ++layer) {

                for(int t = 0; t < toothCounts[personIndex][layer]; ++t) {

                    char c = teeth[personIndex][layer][t];

                    if (c == 'I') {

                        ++I;

                    } else if (c == 'B') {

                        ++B;

                    } else if (c == 'M') {

                        ++M;

                    }

                }

            }

        }



        System.out.println();

        if (I == 0) {

            if (B == 0) {

                if (M == 0) {

                    System.out.println("Infinite number of root canals (all coefficients zero).");

                } else {

                    System.out.println("No root canals (equation is -" + M + " = 0).");

                }

            } else {

                double x = (double)M / (double)B;

                System.out.printf("One root canal at     %.2f%n", x);

            }



        } else {

            double discriminant = (double)B * (double)B + (double)4.0F * (double)I * (double)M;

            if (discriminant < (double)0.0F) {

                System.out.println("No real root canals (discriminant < 0).");

            } else {

                double root1 = ((double)(-B) + Math.sqrt(discriminant)) / ((double)2.0F * (double)I);

                double root2 = ((double)(-B) - Math.sqrt(discriminant)) / ((double)2.0F * (double)I);

                System.out.printf("One root canal at     %.2f%n", root1);

                System.out.printf("Another root canal at %.2f%n", root2);

            }

        }

    }

}

