/**
 * Filename:   P8.java         Due March 16, 2013
 */

 /**
  * Scanner separates characters of the source code into tokens that 
  * logically belong together (primitive data types and simple identifiers).
  * The stream of tokens is passed to the parser, that groups them together 
  * into syntactic structure.
  */

  import java.io.*;
  import java.text.DecimalFormat; 

  public class P8
  {
    public static String identifier;
    public static void main(String [] args)
    {
      LineNumberReader in = null;                           //instantiation
      String [] input;                         // array of String from input
      String inputStr;                                      // input line
      int numErr = 0;                        //  store number of errors found
      int numWar = 0;                       // store number of warnings found
      DecimalFormat df = new DecimalFormat("00");//format to print line number

      System.out.println();

      try
      {
       // input file for reading
       // constructor may throw FileNotFoundException
       in = new LineNumberReader(new FileReader(new File(args[0])));
       while( (inputStr = in.readLine()) != null)
       {
         // remove ";" before tokenizing 
         if( inputStr.endsWith(";") )
         {
          int endOfWord = inputStr.lastIndexOf(";");
          // parse into tokens, removing white space from both sides first
          input = inputStr.substring(0, endOfWord).trim().split(" +"); 

          boolean typeDetected = false;
          boolean identifierNotPresent = false;
          boolean publicVar = false;

           // check for public the first element in the array of input
          if( isPublicVar(input[0]) == true )
          {
            System.out.print( args[0] + ":");
            System.out.print(df.format(in.getLineNumber() ) + ": warning \"");
            System.out.println( input[0] + "\"" + " access instance var");
            publicVar = true;
            numWar++;
          }// end of check for public modifier

          // check primitive type the first element of array of strings 
          if( publicVar == false )
          {
           // typeDetected  = true;
            if( isDataType( input[0] ) == false)
            {
              System.out.print( args[0] + ":");
              System.out.print(df.format(in.getLineNumber() ) + ": \"");
              System.out.println(input[0] + "\" ,data type unknown" );
              numErr++;
            }else{ typeDetected = true;}
          }//end of check for type

          //check if there is an identifier after the first datatype detected
          if(typeDetected == true && input.length == 1)
          {
            System.out.print( args[0] + ":");
            System.out.print(df.format(in.getLineNumber() ) + ": ");
            System.out.println("no identifier specified");
            numErr++;
            identifierNotPresent = true;
          }
            
          // check for identifier if datatype already detected
          // and if identifierNotPresent 
        if( typeDetected == true && identifierNotPresent == false)
        {
          // loop through the rest of the input array to test identifiers
          for( int index = 1; index<input.length; index++)
          {
            // if identifier ends with coma
            // then its length cannot be 1,
            if( input[index].endsWith(",") )
            {
              // if ends with coma and length is 1, then there is no identif
              // or no element after...
              if(input[index].length() == 1 || index == input.length - 1)
              {
                numErr++;
                System.out.print( args[0] + ":");
                System.out.print(df.format(in.getLineNumber() ) + ": ");
                System.out.println("no identifier specified");
                identifierNotPresent = true;
                break;
              }
              // if length is >1, then test set String for testing
              // to everything before coma
              if( input[index].length() > 1) 
              {
                identifierNotPresent = false;
                int coma = input[index].indexOf(",");
                identifier = input[index].substring(0, coma);
              }
            } else { 
                 identifier = input[index];
                 identifierNotPresent = false;

            }//end of if end with ',' check

           // test the identifier
           // if the identifier is present
           if( identifierNotPresent == false)
           {
             if( isStartID (identifier) == false)
             {
                System.out.print( args[0] + ":");
                System.out.print(df.format(in.getLineNumber() ) + ": ");
                System.out.print("\'" + identifier.charAt(0) + "\': ");
                System.out.print("invalid first character of identifier, ");
                System.out.println("\"" + identifier + "\"");
                numErr++;
                break;
             }else{
              
               if( isID(identifier) != -1 )
               {
                System.out.print( args[0] + ":");
                System.out.print(df.format(in.getLineNumber() ) + ": ");
                System.out.print("\'" + identifier.charAt(isID(identifier)));
                System.out.print("\': invalid character in identifier, ");
                System.out.println("\"" + identifier + "\"");
                numErr++;
                break;
               }
             }
               //if start of identifier true, then test the rest of the id
           }

          }// end of for-loop where started identifier testing
        } //end of if-check for identifier

         }// end of line check (if ends with ";")
       }//end of while loop
        System.out.println( numWar + " warning");
        System.out.println( numErr + " errors");
      }
      catch( FileNotFoundException e) // handle exception
      {
       System.err.println("File \"" + args[0] + "\" is NOT found!");
      }
      catch( IOException e)
      {
       System.err.println("I/O Err: " + args[0]);
      }
    }
    // method description
    // to determine if the parameter passed is a legal Java 
    // primitive type
    // method signature: boolean isDataType(String dataType)
    // called from main()
    static boolean isDataType(String dataType)
    {
      String [] _arrData = {"byte", "boolean", "char", "int", "short",
                            "long", "float","double"};
      boolean data = false;

      for(int i = 0; i < _arrData.length; i++)
        if(dataType.equals(_arrData[i]))
          data  = true;
      return data;
    }
   //  method to check if parameter passed begins with a legal Java letter.
   // method signature: boolean isStartID(String id), called from main()
    static boolean isStartID(String id)
    {
     boolean idStart = false;
     if( Character.isJavaIdentifierStart(id.charAt(0)) == true)
     {
      idStart = true;
     } return idStart;
    }
    // method to determine if the parameter passed in is a legal 
    // identifier name. called from name
    // if a valid identifier return -1, else, index of the first invalid 
    //charecter
    static int isID( String id)
    {
     // see char handout for methods
     int index = -1; // return if valid

     for(int i = 0; i < id.length(); i++)
     {
       if(Character.isJavaIdentifierPart(id.charAt(i)) == false)
       {
         index = i;
         break;
       }
     }
     return index;
    }
    // method to check if the parameter passed is access modifier "public"
    // called from main() and displayed warning if returned true, increment 
    // namber of warnings
    static boolean isPublicVar(String accessModifier)
    {
      boolean modifier = false;
      if(accessModifier.equals("public"))
      {
       modifier = true; 
      }
      return modifier;
    }

  }

